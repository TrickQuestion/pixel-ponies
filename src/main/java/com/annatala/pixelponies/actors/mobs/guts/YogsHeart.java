package com.annatala.pixelponies.actors.mobs.guts;

import com.annatala.pixelponies.Dungeon;
import com.annatala.pixelponies.actors.Actor;
import com.annatala.pixelponies.actors.Char;
import com.annatala.pixelponies.actors.blobs.ToxicGas;
import com.annatala.pixelponies.actors.buffs.Amok;
import com.annatala.pixelponies.actors.buffs.Paralysis;
import com.annatala.pixelponies.actors.buffs.Sleep;
import com.annatala.pixelponies.actors.buffs.Terror;
import com.annatala.pixelponies.actors.mobs.Boss;
import com.annatala.pixelponies.actors.mobs.Mob;
import com.annatala.pixelponies.actors.mobs.Yog;
import com.annatala.pixelponies.effects.Pushing;
import com.annatala.pixelponies.items.potions.PotionOfHealing;
import com.annatala.utils.Random;

/**
 * Created by DeadDie on 12.02.2016
 */
public class YogsHeart extends Boss {
    {
        hp(ht(300));
        defenseSkill = 40;

        EXP = 12;

        IMMUNITIES.add(ToxicGas.class);
        IMMUNITIES.add(Paralysis.class);
        IMMUNITIES.add(Amok.class);
        IMMUNITIES.add(Sleep.class);
        IMMUNITIES.add(Terror.class);
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(25, 35);
    }

    @Override
    public int attackSkill( Char target ) {
        return 21;
    }

    @Override
    public int dr() {
        return 12;
    }

    @Override
    public int defenseProc(Char enemy, int damage) {

        int larvaPos = Dungeon.level.getEmptyCellNextTo(getPos());

        if (Dungeon.level.cellValid(larvaPos)) {
            Yog.Larva larva = new Yog.Larva();
            larva.setPos(larvaPos);
            Dungeon.level.spawnMob(larva, 0);
            Actor.addDelayed(new Pushing(larva, getPos(), larva.getPos()), -1);
        }

        return super.defenseProc(enemy, damage);
    }

	@Override
	public boolean act() {

		Mob mob = Dungeon.level.getRandomMob();

		if(mob!=null && mob.isAlive() && !mob.isPet()) {
			PotionOfHealing.heal(mob,0.2f);
		}

		return super.act();
	}
}
