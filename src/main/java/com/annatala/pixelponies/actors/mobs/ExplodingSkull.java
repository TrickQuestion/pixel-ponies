package com.annatala.pixelponies.actors.mobs;

import com.annatala.pixelponies.android.util.TrackedRuntimeException;
import com.annatala.pixelponies.actors.Char;
import com.annatala.pixelponies.items.Gold;
import com.annatala.utils.Random;

/**
 * Created by DeadDie on 12.02.2016
 */
public class ExplodingSkull extends UndeadMob {
    {
        hp(ht(10));
        defenseSkill = 1;

        baseSpeed = 1.5f;

        EXP = 1;
        maxLvl = 1;

        loot = Gold.class;
        lootChance = 0.02f;
    }

    @Override
    public int attackProc( Char enemy, int damage ) {

        try {

            die(this);

        } catch (Exception e) {
            throw new TrackedRuntimeException(e);
        }
        return damage;
    }

    @Override
    public int damageRoll() {

        // There's a small chance you can save for half damage here.
        if (Random.luckBonus() && Random.luckBonus()) return Random.NormalIntRange(18, 28);

        return Random.NormalIntRange(35, 55);
    }

    @Override
    public int attackSkill( Char target ) {
        return 125;
    }

    @Override
    public int dr() {
        return 1;
    }


}
