package com.annatala.pixelponies.actors.mobs;

import com.annatala.pixelponies.actors.Char;
import com.annatala.pixelponies.actors.buffs.Buff;
import com.annatala.pixelponies.actors.buffs.Poison;
import com.annatala.pixelponies.items.Gold;
import com.annatala.utils.Random;

/**
 * Created by DeadDie on 12.02.2016
 */
public class Zombie extends UndeadMob {
    {
        hp(ht(45));
        defenseSkill = 20;

        EXP = 10;
        maxLvl = 15;

        loot = Gold.class;
        lootChance = 0.02f;
    }

    @Override
    public int attackProc( Char enemy, int damage ) {
        //Poison proc
        if (Random.Int(5) < 3 && !Random.luckBonus()){
            Buff.affect( enemy, Poison.class ).set( Random.Int( 2, 4 ) * Poison.durationFactor( enemy ) );
        }

        return damage;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(5, 15);
    }

    @Override
    public int attackSkill( Char target ) {
        return 10;
    }

    @Override
    public int dr() {
        return 20;
    }


}
