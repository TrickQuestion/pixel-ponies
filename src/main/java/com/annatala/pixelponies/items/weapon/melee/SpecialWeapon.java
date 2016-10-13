package com.annatala.pixelponies.items.weapon.melee;

import com.annatala.pixelponies.actors.Char;
import com.annatala.pixelponies.actors.hero.Hero;

public class SpecialWeapon extends MeleeWeapon{

	protected int range;
	
	public SpecialWeapon(int tier, float acu, float dly) {
		super(tier, acu, dly);
		range = 1;
	}

	public int getRange() {
		return range;
	}
	
	public void applySpecial(Hero hero, Char tgt ) {
	}

}
