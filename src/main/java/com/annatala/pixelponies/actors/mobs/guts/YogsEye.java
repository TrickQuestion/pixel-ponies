package com.annatala.pixelponies.actors.mobs.guts;

import com.annatala.pixelponies.actors.Char;
import com.annatala.pixelponies.actors.mobs.Boss;
import com.annatala.pixelponies.items.Gold;
import com.annatala.utils.Random;

/**
 * Created by DeadDie on 12.02.2016
 */
public class YogsEye extends Boss {
    {
        hp(ht(165));
        defenseSkill = 25;

        EXP = 25;

        loot = Gold.class;
        lootChance = 0.5f;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(40, 45);
    }

    @Override
    public int attackSkill( Char target ) {
        return 24;
    }

    @Override
    public int dr() {
        return 2;
    }
}
