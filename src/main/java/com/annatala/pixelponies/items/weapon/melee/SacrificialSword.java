package com.annatala.pixelponies.items.weapon.melee;

import com.annatala.pixelponies.Dungeon;
import com.annatala.pixelponies.actors.Char;
import com.annatala.pixelponies.actors.hero.Hero;
import com.annatala.pixelponies.actors.mobs.Boss;
import com.annatala.pixelponies.actors.mobs.Mob;
import com.annatala.pixelponies.actors.mobs.npcs.NPC;
import com.annatala.pixelponies.sprites.ItemSprite.Glowing;

public class SacrificialSword extends SpecialWeapon {
	{
		imageFile = "items/swords.png";
		image = 4;
		enchatable = false;
	}
	
	public SacrificialSword() {
		super( 2, 1f, 1f );
	}
	
	@Override
	public Glowing glowing() {
		
		float period = (float) Math.max(0.1, 0.1/baseChance(Dungeon.hero));
		//GLog.i("base chance: %.3f %.3f",baseChance(Dungeon.hero), period);
		
		return new Glowing(0xFF4466, period);
		
	}

	private double baseChance(Hero hero) {
		double armorPenalty = 1;
		
		if(hero.belongings.barding != null) {
			armorPenalty += hero.belongings.barding.tier;
		}
		
		double classBonus = 1;

//		if(hero.subClass == HeroSubClass.WARDEN ) {
//			classBonus = 1.5;
//		}
//
//		if(hero.subClass == HeroSubClass.SHAMAN) {
//			classBonus = 2.0;
//		}

		return (0.25 + (hero.lvl() * 4 + Math.pow(level(),2)) * 0.01) * classBonus / armorPenalty;
	}
	
	public void applySpecial(Hero hero, Char tgt ) {
		
		if(tgt instanceof Boss) {
			return;
		}
		
		if(tgt instanceof NPC) {
			return;
		}
		
		if(! (tgt instanceof Mob) ) {
			return;
		}
		
		Mob mob = (Mob) tgt;

		double conversionChance =     baseChance(hero) + 
									- mob.defenseSkill(hero)*0.01*mob.speed()
									- tgt.hp()*0.01;

		double roll = Math.random();
		
		//GLog.i("chance %.3f roll %.3f\n", conversionChance, roll);
		
		
		if(roll < conversionChance ) {
			Mob.makePet(mob, hero);
		}
	}
}
