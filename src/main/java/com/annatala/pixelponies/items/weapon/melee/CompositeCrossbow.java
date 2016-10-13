package com.annatala.pixelponies.items.weapon.melee;

import com.annatala.pixelponies.items.Item;


public class CompositeCrossbow extends Bow {

	public CompositeCrossbow() {
		super(4, 1.1f, 1.6f);
		imageFile = "items/ranged.png";
		image = 3;
	}

	@Override
	public Item burn(int cell) {
		return null;
	}

	@Override
	public double acuFactor() {
		return 1 + level() * 0.5;
	}

	@Override
	public double dmgFactor() {
		return 1 + level() * 0.75;
	}

	public double dlyFactor() {
		return 1.1;
	}
}