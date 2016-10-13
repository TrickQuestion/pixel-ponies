package com.annatala.pixelponies.actors.mobs.spiders;

import com.annatala.pixelponies.actors.mobs.common.MultiKindMob;
import com.annatala.pixelponies.actors.Char;
import com.annatala.pixelponies.actors.buffs.Buff;
import com.annatala.pixelponies.actors.buffs.Poison;
import com.annatala.pixelponies.items.food.MysteriousHay;
import com.annatala.utils.Random;

public class SpiderServant extends MultiKindMob {
	
	public SpiderServant() {
		hp(ht(25));
		defenseSkill = 5;
		baseSpeed = 1.1f;
		
		EXP = 2;
		maxLvl = 9;
		
		kind = 0;
		
		loot = new MysteriousHay();
		lootChance = 0.067f;
	}
	
	@Override
	public int attackProc( Char enemy, int damage ) {
		if (Random.Int( 3 ) == 0 && !Random.luckBonus()) {
			Buff.affect(enemy, Poison.class).set(Random.Int(2, 3) * Poison.durationFactor(enemy));
		}
		return damage;
	}
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 4, 6 );
	}
	
	@Override
	public int attackSkill( Char target ) {
		return 11;
	}
	
	@Override
	public int dr() {
		return 5;
	}

}
