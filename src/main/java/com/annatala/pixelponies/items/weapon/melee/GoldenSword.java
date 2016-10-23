package com.annatala.pixelponies.items.weapon.melee;

import com.annatala.pixelponies.Dungeon;
import com.annatala.pixelponies.actors.Char;
import com.annatala.pixelponies.items.Gold;
import com.annatala.pixelponies.sprites.ItemSprite.Glowing;
import com.annatala.utils.Random;

public class GoldenSword extends SpecialWeapon {
	{
		imageFile = "items/swords.png";
		image = 5;
		enchatable = false;
	}

	public GoldenSword() {
		super( 3, 1.1f, 0.8f );
	}
	
	@Override
	public Glowing glowing() {
		float period = 1;
		return new Glowing(0xFFFF66, period);
	}

	@Override
	public void proc( Char attacker, Char defender, int damage ) {

		// Gold proc (less likely sword will get destroyed if hero is lucky)
		// ...although I'm not sure if this destroys the sword or not?
		if (Random.Int(8) == 0 && !Random.luckBonus() ){
			int price = this.price() / 10;
			if ( price > 500 ) { price = 500;}
			Dungeon.level.drop(new Gold(price), defender.getPos());
		}
		usedForHit();
	}

}
