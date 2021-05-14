package crimson_twilight.immersive_energy.common.items;

import crimson_twilight.immersive_energy.INail;

public class ItemIEnNail extends ItemIEnBase implements INail
{
    private int damage;

    public ItemIEnNail(String name, int stackSize, int damage)
    {
        super(name, stackSize);
        this.damage = damage;
    }

    @Override
    public void setDamage(int dmg) {
        this.damage = dmg;
    }

    @Override
    public int getDamage() {
        return this.damage;
    }
}
