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
import com.annatala.pixelponies.android.R;
import com.annatala.pixelponies.actors.Char;
import com.annatala.pixelponies.actors.buffs.Buff;
import com.annatala.pixelponies.actors.buffs.Roots;
import com.annatala.pixelponies.effects.CellEmitter;
import com.annatala.pixelponies.effects.particles.EarthParticle;
import com.annatala.pixelponies.items.barding.Barding.Glyph;
import com.annatala.pixelponies.plants.Earthroot;
import com.annatala.pixelponies.sprites.ItemSprite;
import com.annatala.pixelponies.sprites.ItemSprite.Glowing;
import com.annatala.utils.Utils;
import com.annatala.utils.Random;

public class Entanglement extends Glyph {

	private static final String TXT_ENTANGLEMENT = Game.getVar(R.string.Entanglement_Txt);
	
	private static ItemSprite.Glowing GREEN = new ItemSprite.Glowing( 0x448822 );
	
	@Override
	public int proc(com.annatala.pixelponies.items.barding.Barding barding, Char attacker, Char defender, int damage ) {

		int level = Math.max( 0, barding.level() );
		
		if (Random.Int( 4 ) == 0) {
			
			Buff.prolong( defender, Roots.class, 5 - level / 5 );
			Buff.affect( defender, Earthroot.Barding.class ).level( 5 * (level + 1) );
			CellEmitter.bottom( defender.getPos() ).start( EarthParticle.FACTORY, 0.05f, 8 );
			Camera.main.shake( 1, 0.4f );
			
		}

		return damage;
	}
	
	@Override
	public String name( String weaponName) {
		return Utils.format( TXT_ENTANGLEMENT, weaponName );
	}

	@Override
	public Glowing glowing() {
		return GREEN;
	}
		
}
