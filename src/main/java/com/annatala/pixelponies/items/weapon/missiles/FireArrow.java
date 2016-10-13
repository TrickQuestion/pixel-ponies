
package com.annatala.pixelponies.items.weapon.missiles;

import com.annatala.pixelponies.actors.Char;
import com.annatala.pixelponies.actors.buffs.Buff;
import com.annatala.pixelponies.actors.buffs.Burning;

public class FireArrow extends Arrow {

	public FireArrow() {
		this( 1 );
	}
	
	public FireArrow( int number ) {
		super();
		quantity(number);
		
		baseMin = 1;
		baseMax = 6;
		baseDly = 0.75;
		
		image = FIRE_ARROW_IMAGE;
		
		updateStatsForInfo();
	}
	
	@Override
	public int price() {
		return quantity() * 5;
	}

	@Override
	public void proc( Char attacker, Char defender, int damage ) {
		if(activateSpecial(attacker, defender, damage)) {
			Buff.affect( defender, Burning.class ).reignite( defender );
		}
		super.proc( attacker, defender, damage );
	}
}
