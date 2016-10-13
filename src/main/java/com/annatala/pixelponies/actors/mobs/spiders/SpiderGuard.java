package com.annatala.pixelponies.actors.mobs.spiders;

import com.annatala.pixelponies.actors.mobs.common.MultiKindMob;
import com.annatala.pixelponies.Badges;
import com.annatala.pixelponies.actors.Char;
import com.annatala.pixelponies.actors.buffs.Buff;
import com.annatala.pixelponies.actors.buffs.Paralysis;
import com.annatala.pixelponies.items.food.MysteriousHay;
import com.annatala.utils.Random;

public class SpiderGuard extends MultiKindMob {

	public SpiderGuard() {
		hp(ht(35));
		defenseSkill = 15;
		baseSpeed = 1.2f;
		
		EXP = 4;
		maxLvl = 10;
		
		kind = 1;
		
		loot = new MysteriousHay();
		lootChance = 0.067f;
	}
	
	@Override
	public int attackProc( Char enemy, int damage ) {
		if (Random.Int( 8 ) == 0 && !Random.luckBonus()) {
			Buff.prolong( enemy, Paralysis.class, 3);
		}
		return damage;
	}
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 8, 14 );
	}
	
	@Override
	public int attackSkill( Char target ) {
		return 17;
	}
	
	@Override
	public int dr() {
		return 7;
	}

	@Override
	public void die( Object cause ) {
		super.die( cause );
		Badges.validateRare( this );
	}

}
