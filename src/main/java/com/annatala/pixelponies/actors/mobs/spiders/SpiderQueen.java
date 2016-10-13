package com.annatala.pixelponies.actors.mobs.spiders;

import com.annatala.pixelponies.items.barding.SpiderBarding;
import com.annatala.pixelponies.actors.mobs.spiders.sprites.SpiderQueenSprite;
import com.annatala.pixelponies.Badges;
import com.annatala.pixelponies.Dungeon;
import com.annatala.pixelponies.actors.Char;
import com.annatala.pixelponies.actors.buffs.Buff;
import com.annatala.pixelponies.actors.buffs.Poison;
import com.annatala.pixelponies.actors.buffs.Regeneration;
import com.annatala.pixelponies.actors.mobs.Boss;
import com.annatala.pixelponies.items.utility.SpiderCharm;
import com.annatala.utils.Random;

public class SpiderQueen extends Boss {
	
	public SpiderQueen() {
		spriteClass = SpiderQueenSprite.class;
		
		hp(ht(120));
		defenseSkill = 18;
		
		EXP = 11;
		maxLvl = 21;

		float dice = Random.Float();
		if( dice < 0.50) {
			loot = new SpiderCharm();
		} else{
			loot = new SpiderBarding();
		}
		lootChance = 1f;
		
		Buff.affect(this, Regeneration.class);
	}
	
	@Override
	protected boolean act(){
		if(Random.Int(0, 20) == 0 && !SpiderEgg.laid(getPos())) {
			SpiderEgg.lay(getPos());
		}
		
		return super.act();
	}
	
	@Override
	public int attackProc( Char enemy, int damage ) {
		if (Random.Int( 5 ) < 3 && !Random.luckBonus()) {
			Buff.affect( enemy, Poison.class ).set( Random.Int( 7, 9 ) * Poison.durationFactor( enemy ) );
		}
		
		return damage;
	}
	
	@Override
	protected boolean canAttack(Char enemy) {
		return Dungeon.level.adjacent(getPos(), enemy.getPos()) && hp() > ht() / 2;
	}
	
	@Override
	protected boolean getCloser( int target ) {
		if (hp() < ht() / 2) {
			if (state == HUNTING && Dungeon.level.distance(getPos(), target) < 5) {
				return getFurther(target);
			}
			return super.getCloser(target);
		} else {
			return super.getCloser( target );
		}
	}
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 12, 20 );
	}
	
	@Override
	public int attackSkill( Char target ) {
		return 22;
	}
	
	@Override
	public int dr() {
		return 10;
	}

	@Override
	public void die(Object cause) {
		super.die(cause);
		Badges.validateBossSlain(Badges.Badge.SPIDER_QUEEN_SLAIN);
	}
}
