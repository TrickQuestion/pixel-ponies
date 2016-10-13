
package com.annatala.pixelponies.items.weapon.missiles;

import com.annatala.pixelponies.actors.Char;
import com.annatala.pixelponies.actors.buffs.Buff;
import com.annatala.pixelponies.actors.buffs.Slow;

public class FrostArrow extends Arrow {

	public static final float DURATION	= 5f;
	
	public FrostArrow() {
		this( 1 );
	}
	
	public FrostArrow( int number ) {
		super();
		quantity(number);
		
		baseMin = 0;
		baseMax = 6;
		baseDly = 0.75;
		
		image = FROST_ARROW_IMAGE;
		
		updateStatsForInfo();
	}
	
	@Override
	public int price() {
		return quantity() * 5;
	}

	@Override
	public void proc( Char attacker, Char defender, int damage ) {
		if(activateSpecial(attacker, defender, damage)) {
			Buff.prolong( defender, Slow.class, DURATION );
		}
		super.proc( attacker, defender, damage );
	}
}
