/*
 * Pixel Dungeon
 * Copyright (C) 2012-2014  Oleg Dolya
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.annatala.pixelponies.items.barding.glyphs;

import com.annatala.pixelponies.android.R;
import com.annatala.noosa.Game;
import com.annatala.pixelponies.actors.Char;
import com.annatala.pixelponies.actors.buffs.Hunger;
import com.annatala.pixelponies.effects.Speck;
import com.annatala.pixelponies.items.barding.Barding;
import com.annatala.pixelponies.items.barding.Barding.Glyph;
import com.annatala.pixelponies.sprites.CharSprite;
import com.annatala.pixelponies.sprites.ItemSprite;
import com.annatala.pixelponies.sprites.ItemSprite.Glowing;
import com.annatala.pixelponies.ui.BuffIndicator;
import com.annatala.utils.Utils;
import com.annatala.utils.Random;

public class Metabolism extends Glyph {

	private static final String TXT_METABOLISM = Game.getVar(R.string.Metabolism_Txt);
	
	private static ItemSprite.Glowing RED = new ItemSprite.Glowing( 0xCC0000 );
	
	@Override
	public int proc(Barding barding, Char attacker, Char defender, int damage) {

		int level = Math.max( 0, barding.level() );
		if (Random.Int( level / 2 + 5 ) >= 4) {
			
			int healing = Math.min( defender.ht() - defender.hp(), Random.Int( 1, defender.ht() / 5 ) );

			if (healing > 0) {
				
				Hunger hunger = defender.buff( Hunger.class );

				if (hunger != null && !hunger.isStarving()) {

					hunger.satisfy(-Hunger.STARVING / 10);
					BuffIndicator.refreshHero();
				}
					defender.hp(defender.hp() + healing);
					defender.getSprite().emitter().burst( Speck.factory( Speck.HEALING ), 1 );
					defender.getSprite().showStatus( CharSprite.POSITIVE, Integer.toString( healing ) );
			}

		}
		
		return damage;
	}
	
	@Override
	public String name( String weaponName) {
		return Utils.format( TXT_METABOLISM, weaponName );
	}

	@Override
	public Glowing glowing() {
		return RED;
	}
}
