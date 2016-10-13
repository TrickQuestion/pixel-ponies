package com.annatala.pixelponies.actors.mobs.guts;

import com.annatala.pixelponies.actors.Char;
import com.annatala.pixelponies.actors.buffs.Paralysis;
import com.annatala.pixelponies.actors.mobs.Mob;
import com.annatala.pixelponies.items.Gold;
import com.annatala.utils.Random;

/**
 * Created by DeadDie on 12.02.2016
 */
public class PseudoRat extends Mob {
    {
        hp(ht(320));
        defenseSkill = 30;

        EXP = 20;
        maxLvl = 35;

        loot = Gold.class;
        lootChance = 0.8f;

        state = HUNTING;

        IMMUNITIES.add(Paralysis.class);
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(40, 70);
    }

    @Override
    public int attackSkill( Char target ) {
        return 30;
    }

    @Override
    public int dr() {
        return 25;
    }

    @Override
    public int getKind() {
        return 1;
    }

    @Override
    public void die(Object cause) {
        super.die(cause);
        
    }
}
