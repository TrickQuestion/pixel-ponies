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

import com.annatala.noosa.Camera;
import com.annatala.noosa.Game;
import com.annatala.pixelponies.Dungeon;
import com.annatala.pixelponies.android.R;
import com.annatala.pixelponies.actors.Char;
import com.annatala.pixelponies.effects.Lightning;
import com.annatala.pixelponies.items.barding.Barding;
import com.annatala.pixelponies.items.barding.Barding.Glyph;
import com.annatala.pixelponies.levels.traps.LightningTrap;
import com.annatala.pixelponies.sprites.ItemSprite;
import com.annatala.pixelponies.sprites.ItemSprite.Glowing;
import com.annatala.utils.Utils;
import com.annatala.utils.Random;

public class Potential extends Glyph {

	private static final String TXT_POTENTIAL = Game.getVar(R.string.Potential_Txt);
	
	private static ItemSprite.Glowing BLUE = new ItemSprite.Glowing( 0x66CCEE );
	
	@Override
	public int proc(Barding barding, Char attacker, Char defender, int damage) {

		int level = Math.max( 0, barding.level() );
		
		if (Dungeon.level.adjacent( attacker.getPos(), defender.getPos() ) && Random.Int( level + 7 ) >= 6) {
			
			int dmg = Random.IntRange( 1, damage );
			attacker.damage( dmg, LightningTrap.LIGHTNING );
			dmg = Random.IntRange( 1, dmg );
			defender.damage( dmg, LightningTrap.LIGHTNING );
			
			checkOwner( defender );
			if (defender == Dungeon.hero) {
				Camera.main.shake( 2, 0.3f );
			}
			
			int[] points = {attacker.getPos(), defender.getPos()};
			attacker.getSprite().getParent().add( new Lightning( points, 2, null ) );
		}
		
		return damage;
	}
	
	@Override
	public String name( String weaponName) {
		return Utils.format( TXT_POTENTIAL, weaponName );
	}

	@Override
	public Glowing glowing() {
		return BLUE;
	}
}
