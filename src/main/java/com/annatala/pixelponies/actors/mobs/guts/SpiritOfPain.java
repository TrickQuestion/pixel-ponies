package com.annatala.pixelponies.actors.mobs.guts;

import com.annatala.pixelponies.actors.Char;
import com.annatala.pixelponies.actors.mobs.Mob;
import com.annatala.utils.Random;

/**
 * Created by DeadDie on 19.04.2016
 */
public class SpiritOfPain extends Mob {

    {
        hp(ht(80));
        defenseSkill = 30;

        EXP = 0;

        state = HUNTING;
        flying = true;
    }

    @Override
    public int attackSkill(Char target) {
        return 30;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(5, 10);
    }

    @Override
    public int dr() {
        return 20;
    }

}