package com.annatala.pixelponies.actors.mobs.guts;

import com.annatala.pixelponies.actors.Char;
import com.annatala.pixelponies.actors.buffs.Buff;
import com.annatala.pixelponies.actors.buffs.Paralysis;
import com.annatala.pixelponies.actors.buffs.Roots;
import com.annatala.pixelponies.actors.mobs.Mob;
import com.annatala.utils.Random;

/**
 * Created by DeadDie on 12.02.2016
 */
public class Nightmare extends Mob {

    {
        hp(ht(80));
        defenseSkill = 24;

        EXP = 0;
    }

    @Override
    public int attackProc( Char enemy, int damage ) {
        //Roots proc
        if (Random.Int(8) == 0 && !Random.luckBonus()){
            Buff.affect(enemy, Roots.class);
        }
        //Paralysis proc
        if (Random.Int(8) == 0 && !Random.luckBonus()){
            Buff.affect(enemy, Paralysis.class);
        }
        return damage;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(20, 25);
    }

    @Override
    public int attackSkill( Char target ) { return 26; }

    @Override
    public int dr() { return 10; }

    @Override
    protected boolean act(){
        super.act();

        state = HUNTING;

        return true;
    }
}
