package com.annatala.pixelponies.actors.mobs.spiders;

import com.annatala.pixelponies.Dungeon;
import com.annatala.pixelponies.actors.Char;
import com.annatala.pixelponies.actors.buffs.Blindness;
import com.annatala.pixelponies.actors.buffs.Buff;
import com.annatala.pixelponies.actors.buffs.Charm;
import com.annatala.pixelponies.actors.buffs.FlavourBuff;
import com.annatala.pixelponies.actors.buffs.Levitation;
import com.annatala.pixelponies.actors.buffs.Roots;
import com.annatala.pixelponies.actors.buffs.Slow;
import com.annatala.pixelponies.actors.buffs.Vertigo;
import com.annatala.pixelponies.actors.buffs.Weakness;
import com.annatala.pixelponies.actors.hero.Hero;
import com.annatala.pixelponies.actors.mobs.Mob;
import com.annatala.pixelponies.items.food.MysteriousHay;
import com.annatala.pixelponies.mechanics.Ballistica;
import com.annatala.utils.Random;

public class SpiderMind extends Mob {

	static Class<?> BuffsForEnemy[] = {
		Blindness.class,
		Charm.class,
		Levitation.class,
		Roots.class,
		Slow.class,
		Vertigo.class,
		Weakness.class
	};
	
	public SpiderMind() {
		hp(ht(5));
		defenseSkill = 1;
		baseSpeed = 0.8f;
		
		EXP = 6;
		maxLvl = 9;
		
		loot = new MysteriousHay();
		lootChance = 0.067f;
	}
	
	@Override
	protected boolean canAttack( Char enemy ) {
		return !Dungeon.level.adjacent( getPos(), enemy.getPos() ) && Ballistica.cast( getPos(), enemy.getPos(), false, true ) == enemy.getPos();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public int attackProc( Char enemy, int damage ) {
		
		if(enemy instanceof Hero) {
			Class <? extends FlavourBuff> buffClass = (Class<? extends FlavourBuff>) Random.oneOf(BuffsForEnemy);		
			Buff.prolong( enemy, buffClass, 3 );
		}
		
		return damage;
	}
	
	@Override
	protected boolean getCloser( int target ) {
		if (state == HUNTING) {
			return enemySeen && getFurther( target );
		} else {
			return super.getCloser( target );
		}
	}
	
	@Override
	public int damageRoll() {
		return 0;
	}
	
	@Override
	public int attackSkill( Char target ) {
		return 10;
	}
	
	@Override
	public int dr() {
		return 0;
	}
	
	@Override
	public void die( Object cause ) {
		super.die( cause );
	}

}
