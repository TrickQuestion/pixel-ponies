package com.nyrds.pixeldungeon.items.guts.weapon.melee;

import com.watabou.pixeldungeon.items.weapon.melee.SpecialWeapon;

public class Halberd extends SpecialWeapon {

	public Halberd() {
		super( 6, 1.1f, 1.2f );
		range = 2;
		imageFile = "items/polearms.png";
		image = 2;
	}
}
