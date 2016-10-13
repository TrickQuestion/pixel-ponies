package com.annatala.pixelponies.actors.mobs;

import com.annatala.pixelponies.items.scrolls.ScrollOfPsionicBlast;
import com.annatala.pixelponies.items.weapon.enchantments.Death;

abstract public class Boss extends Mob {

	public Boss() {
		state = HUNTING;

		RESISTANCES.add( Death.class );
		RESISTANCES.add( ScrollOfPsionicBlast.class );
	}

	@Override
	public boolean canBePet() {
		return false;
	}
}
