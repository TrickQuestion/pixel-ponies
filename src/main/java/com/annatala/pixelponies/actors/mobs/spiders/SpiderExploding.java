package com.annatala.pixelponies.actors.mobs.spiders;

import com.annatala.pixelponies.android.util.TrackedRuntimeException;
import com.annatala.pixelponies.actors.mobs.common.MultiKindMob;
import com.annatala.pixelponies.actors.Char;
import com.annatala.pixelponies.items.food.MysteriousHay;
import com.annatala.pixelponies.plants.Dreamweed;
import com.annatala.pixelponies.plants.Earthroot;
import com.annatala.pixelponies.plants.Fadeleaf;
import com.annatala.pixelponies.plants.Firebloom;
import com.annatala.pixelponies.plants.Icecap;
import com.annatala.pixelponies.plants.Plant;
import com.annatala.pixelponies.plants.Sorrowmoss;
import com.annatala.pixelponies.plants.Sungrass;
import com.annatala.utils.Random;

public class SpiderExploding extends MultiKindMob {

	static Class<?> PLantClasses[] = {
		Firebloom.class, 
		Icecap.class, 
		Sorrowmoss.class,
		Earthroot.class,
		Sungrass.class,
		Fadeleaf.class,
		Dreamweed.class
	};
	
	public SpiderExploding() {
		hp(ht(5));
		defenseSkill = 1;
		baseSpeed = 2f;
		
		EXP = 3;
		maxLvl = 9;
		
		kind = Random.IntRange(0, 6);
		
		loot = new MysteriousHay();
		lootChance = 0.067f;
	}
	
	@Override
	public int attackProc( Char enemy, int damage ) {
		
		try {
			Plant plant  = (Plant) PLantClasses[getKind()].newInstance();
			plant.pos = enemy.getPos();
			
			plant.effect(enemy.getPos(),enemy);
			
			die(this);
			
		} catch (Exception e) {
			throw new TrackedRuntimeException(e);
		} 
		return damage;
	}
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 3, 6 );
	}
	
	@Override
	public int attackSkill( Char target ) {
		return 1000;
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
