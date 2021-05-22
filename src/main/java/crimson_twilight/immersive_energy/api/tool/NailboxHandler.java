package crimson_twilight.immersive_energy.api.tool;

import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class NailboxHandler
{
    private static final List<Predicate<ItemStack>> nails = new ArrayList<>();

    static
    {
        nails.add((s) -> (s.getItem() instanceof INail));
    }

    public static boolean isNail(ItemStack s)
    {
        for(Predicate<ItemStack> p : nails)
            if(p.test(s))
                return true;
        return false;
    }

    public static void addNailType(Predicate<ItemStack> in)
    {
        nails.add(in);
    }
}
