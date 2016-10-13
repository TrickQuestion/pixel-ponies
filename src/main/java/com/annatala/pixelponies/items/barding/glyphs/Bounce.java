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
import com.annatala.pixelponies.Dungeon;
import com.annatala.pixelponies.actors.Actor;
import com.annatala.pixelponies.actors.Char;
import com.annatala.pixelponies.effects.Pushing;
import com.annatala.pixelponies.items.barding.Barding;
import com.annatala.pixelponies.items.barding.Barding.Glyph;
import com.annatala.pixelponies.levels.Level;
import com.annatala.utils.Utils;
import com.annatala.utils.Random;

public class Bounce extends Glyph {

	private static final String TXT_BOUNCE = Game.getVar(R.string.Bounce_Txt);
	
	@Override
	public int proc(Barding barding, Char attacker, Char defender, int damage) {

		int level = Math.max( 0, barding.level() );
		
		if (Dungeon.level.adjacent( attacker.getPos(), defender.getPos() ) && Random.Int( level + 5) >= 4) {
			
			for (int i=0; i < Level.NEIGHBOURS8.length; i++) {
				int ofs = Level.NEIGHBOURS8[i];
				if (attacker.getPos() - defender.getPos() == ofs) {
					int newPos = attacker.getPos() + ofs;
					if ( newPos < 0 || newPos > Dungeon.level.passable.length){
						newPos = defender.getPos();
					}
					if ((Dungeon.level.passable[newPos] || Dungeon.level.avoid[newPos]) && Actor.findChar( newPos ) == null) {
						
						Actor.addDelayed( new Pushing( attacker, attacker.getPos(), newPos ), -1 );
						
						attacker.setPos(newPos);

						Dungeon.level.press(newPos, attacker);
					}
					break;
				}
			}

		}
		
		return damage;
	}
	
	@Override
	public String name( String weaponName) {
		return Utils.format( TXT_BOUNCE, weaponName );
	}

}
