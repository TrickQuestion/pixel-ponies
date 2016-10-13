package com.annatala.pixelponies.actors.mobs.guts;

import com.annatala.pixelponies.items.quest.HeartOfDarkness;
import com.annatala.pixelponies.Dungeon;
import com.annatala.pixelponies.actors.Actor;
import com.annatala.pixelponies.actors.Char;
import com.annatala.pixelponies.actors.mobs.Mob;
import com.annatala.pixelponies.effects.Pushing;
import com.annatala.utils.Random;

/**
 * Created by DeadDie on 12.02.2016
 */
public class TreacherousSpirit extends Mob {
    {
        hp(ht(200));
        defenseSkill = 35;

        EXP = 45;
        maxLvl = 30;

        state = WANDERING;
        lootChance = 1f;
        loot = HeartOfDarkness.class;
    }

    @Override
    public int attackProc( Char enemy, int damage ) {

        //Summon proc
        if (Random.Int(3) == 0 && !Random.luckBonus()){
            int spiritPos = Dungeon.level.getEmptyCellNextTo(getPos());

            if (Dungeon.level.cellValid(spiritPos)) {
                SpiritOfPain spirit = new SpiritOfPain();
                spirit.setPos(spiritPos);
                Dungeon.level.spawnMob(spirit, 0);
                Actor.addDelayed(new Pushing(spirit, getPos(), spirit.getPos()), -1);
            }
        }
        return damage;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(30, 45);
    }

    @Override
    public int attackSkill( Char target ) {
        return 35;
    }

    @Override
    public int dr() {
        return 25;
    }

}
