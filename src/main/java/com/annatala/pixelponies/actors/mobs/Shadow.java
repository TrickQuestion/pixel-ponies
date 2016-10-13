package com.annatala.pixelponies.actors.mobs;

import com.annatala.pixelponies.actors.Char;
import com.annatala.pixelponies.sprites.ShadowSprite;
import com.annatala.utils.Random;

public class Shadow extends Mob {
	{
		spriteClass = ShadowSprite.class;
		
		hp(ht(20));
		defenseSkill = 15;
		
		EXP = 5;
		maxLvl = 10;
		
		state = WANDERING;
	}

	@Override
	public float speed() {
		return 2;
	}
	
	@Override
	protected float attackDelay() {
		return 0.5f;
	}
	
	@Override
	public boolean isWallWalker() {
		return true;
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 5, 10 );
	}
	
	@Override
	public int attackSkill(Char target) {
		return 10;
	}
}
