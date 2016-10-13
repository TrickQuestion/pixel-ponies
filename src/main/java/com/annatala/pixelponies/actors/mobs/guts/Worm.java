package com.annatala.pixelponies.actors.mobs.guts;

import com.annatala.pixelponies.actors.Char;
import com.annatala.pixelponies.actors.blobs.ToxicGas;
import com.annatala.pixelponies.actors.buffs.Buff;
import com.annatala.pixelponies.actors.buffs.Paralysis;
import com.annatala.pixelponies.actors.buffs.Poison;
import com.annatala.pixelponies.actors.buffs.Roots;
import com.annatala.pixelponies.actors.mobs.Mob;
import com.annatala.pixelponies.items.Gold;
import com.annatala.utils.Random;

/**
 * Created by DeadDie on 12.02.2016
 */
public class Worm extends Mob {
    {
        hp(ht(195));
        defenseSkill = 15;

        EXP = 18;

        loot = Gold.class;
        lootChance = 0.4f;

        IMMUNITIES.add(Paralysis.class);
        IMMUNITIES.add(ToxicGas.class);
    }

    @Override
    public int attackProc( Char enemy, int damage ) {
        //Roots proc
        if (Random.Int(5) == 0 && !Random.luckBonus()){
            Buff.affect(enemy, Roots.class);
        }
        //Poison proc
        if (Random.Int(4) == 0 && !Random.luckBonus()){
            Buff.affect( enemy, Poison.class ).set( Random.Int( 7, 9 ) * Poison.durationFactor( enemy ) );
        }
        return damage;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(22, 45);
    }

    @Override
    public int attackSkill( Char target ) {
        return 20;
    }

    @Override
    public int dr() {
        return 50;
    }
}
