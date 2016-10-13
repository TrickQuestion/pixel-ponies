package com.annatala.pixelponies.actors.mobs;

import com.annatala.pixelponies.actors.mobs.common.MobSpawner;
import com.annatala.pixelponies.Dungeon;
import com.annatala.pixelponies.actors.Actor;
import com.annatala.pixelponies.actors.Char;
import com.annatala.pixelponies.effects.Pushing;
import com.annatala.utils.Callback;

public class JarOfSouls extends UndeadMob {

	public JarOfSouls() {
		hp(ht(100));
		defenseSkill = 1;

		pacified = true;
		
		EXP    = 0;
		maxLvl = 13;

		postpone(20);
		
	}

	@Override
	public int damageRoll() {
		return 0;
	}
	
	@Override
	public int attackSkill( Char target ) {
		return 0;
	}
	
	@Override
	protected boolean act(){
		super.act();
		if (enemySeen){
			spawnUndead();
		}
		return true;
	}

	private void spawnUndead(){
		PlayZap();

		Mob newMob = MobSpawner.spawnRandomMob(Dungeon.level, getPos());

		int mobPos = Dungeon.level.getEmptyCellNextTo(getPos());

		if (Dungeon.level.cellValid(mobPos)) {
			newMob.setPos(mobPos);
			Actor.addDelayed(new Pushing(newMob, getPos(), newMob.getPos()), -1);
		}
		postpone(15);
	}

	@Override
	public int dr() {
		return 0;
	}

	@Override
	protected boolean getCloser( int target ) {
		return false;
	}

	@Override
	protected boolean getFurther( int target ) {
		return false;
	}

	@Override
	public boolean canBePet() {
		return false;
	}

	public void PlayZap() {
		getSprite().zap(
				getEnemy().getPos(),
				new Callback() {
					@Override
					public void call() {
					}
				});
	}
}
