package crimson_twilight.immersive_energy.common.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import com.google.common.collect.ArrayListMultimap;

import blusunrize.immersiveengineering.common.util.IELogger;
import crimson_twilight.immersive_energy.common.Config.IEnConfig;
import crimson_twilight.immersive_energy.common.Config.IEnConfig.Ores;
import crimson_twilight.immersive_energy.common.util.IEnLogger;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

public class IEnWorldGen implements IWorldGenerator
{
	public static class OreGen
	{
		String name;
		WorldGenMinable mineableGen;
		int minY;
		int maxY;
		int chunkOccurence;
		int weight;

		public OreGen(String name, IBlockState state, int maxVeinSize, Block replaceTarget, int minY, int maxY, int chunkOccurence, int weight)
		{
			this.name = name;
			this.mineableGen = new WorldGenMinable(state, maxVeinSize, BlockMatcher.forBlock(replaceTarget));
			this.minY = minY;
			this.maxY = maxY;
			this.chunkOccurence = chunkOccurence;
			this.weight = weight;
			System.out.println("OreGen!");
		}

		public void generate(World world, Random rand, int x, int z)
		{
			BlockPos pos;
			//System.out.println("generate!");
			for(int i = 0; i < chunkOccurence; i++)
				if(rand.nextInt(100) < weight)
				{
					pos = new BlockPos(x+rand.nextInt(16), minY+rand.nextInt(maxY-minY), z+rand.nextInt(16));
					mineableGen.generate(world, rand, pos);
				}
		}
	}

	public static ArrayList<OreGen> orespawnList = new ArrayList<OreGen>();
	public static ArrayList<Integer> oreDimBlacklist = new ArrayList<Integer>();
	public static HashMap<String, Boolean> retrogenMap = new HashMap<String, Boolean>();

	public static OreGen addOreGen(String name, IBlockState state, int maxVeinSize, int minY, int maxY, int chunkOccurence, int weight)
	{
		OreGen gen = new OreGen(name, state, maxVeinSize, Blocks.STONE, minY, maxY, chunkOccurence, weight);
		orespawnList.add(gen);
		//System.out.println("Running addOreGen!");
		return gen;
	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider)
	{
		//System.out.println("@Override generate!");
		this.generateOres(random, chunkX, chunkZ, world, true);
	}

	public void generateOres(Random random, int chunkX, int chunkZ, World world, boolean newGeneration)
	{
		//System.out.println("generateOres!");
		if(world.provider.getDimension() != 1 && world.provider.getDimension() != -1)
		{
			//System.out.println("generateOres:correct Dim!");
			for(OreGen gen : orespawnList)
			{
				//System.out.println("generateOres:checkList");
				if(newGeneration||retrogenMap.get("retrogen_"+gen.name))
				{
					//System.out.println("generateOres:new/retroGen!");
					gen.generate(world, random, chunkX*16, chunkZ*16);
				}
			}
		}
	}

	@SubscribeEvent
	public void chunkSave(ChunkDataEvent.Save event)
	{
		//System.out.println("chunkSave!");
		NBTTagCompound nbt = new NBTTagCompound();
		event.getData().setTag("ImmersiveEnergy", nbt);
		nbt.setBoolean(IEnConfig.Ores.retrogen_key, true);
	}

	@SubscribeEvent
	public void chunkLoad(ChunkDataEvent.Load event)
	{
		//System.out.println("chunkLoad!");
		int dimension = event.getWorld().provider.getDimension();
		if((!event.getData().getCompoundTag("ImmersiveEnergy").hasKey(IEnConfig.Ores.retrogen_key))&&(Ores.retrogen_thorium||Ores.retrogen_tungsten))
		{
			if(IEnConfig.Ores.retrogen_log_flagChunk)
				IEnLogger.info("Chunk "+event.getChunk().getPos()+" has been flagged for Ore RetroGeneration by IEn.");
			retrogenChunks.put(dimension, event.getChunk().getPos());
		}
	}

	public static ArrayListMultimap<Integer, ChunkPos> retrogenChunks = ArrayListMultimap.create();

	@SubscribeEvent
	public void serverWorldTick(TickEvent.WorldTickEvent event)
	{
		if(event.side==Side.CLIENT||event.phase==TickEvent.Phase.START)
			return;
		int dimension = event.world.provider.getDimension();
		int counter = 0;
		List<ChunkPos> chunks = retrogenChunks.get(dimension);
		if(chunks!=null&&chunks.size() > 0)
			for(int i = 0; i < 2; i++)
			{
				chunks = retrogenChunks.get(dimension);
				if(chunks==null||chunks.size() <= 0)
					break;
				counter++;
				ChunkPos loc = chunks.get(0);
				long worldSeed = event.world.getSeed();
				Random fmlRandom = new Random(worldSeed);
				long xSeed = (fmlRandom.nextLong() >> 3);
				long zSeed = (fmlRandom.nextLong() >> 3);
				fmlRandom.setSeed(xSeed*loc.x+zSeed*loc.z^worldSeed);
				this.generateOres(fmlRandom, loc.x, loc.z, event.world, false);
				chunks.remove(0);
			}
		if(counter > 0 && IEnConfig.Ores.retrogen_log_remaining)
			IELogger.info("Retrogen was performed on "+counter+" Chunks, "+Math.max(0, chunks.size())+" chunks remaining");
	}
}