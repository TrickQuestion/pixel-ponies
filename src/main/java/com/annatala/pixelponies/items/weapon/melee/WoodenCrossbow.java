
package com.annatala.pixelponies.items.weapon.melee;

import com.annatala.pixelponies.items.Item;

public class WoodenCrossbow extends Bow {

	public WoodenCrossbow() {

		super( 2, 1.2f, 1.6f );
		imageFile = "items/ranged.png";
		image = 1;
	}

	@Override
	public Item burn(int cell) {
		return null;
	}

	@Override
	public double acuFactor() {
		return 1 + level() * 0.15;
	}

	@Override
	public double dmgFactor() {
		return 1 + level() * 0.35;
	}
}
