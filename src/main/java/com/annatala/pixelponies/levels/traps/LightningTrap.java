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
package com.annatala.pixelponies.levels.traps;

import android.support.annotation.Nullable;

import com.annatala.pixelponies.android.R;
import com.annatala.noosa.Camera;
import com.annatala.noosa.Game;
import com.annatala.pixelponies.Dungeon;
import com.annatala.pixelponies.ResultDescriptions;
import com.annatala.pixelponies.actors.Char;
import com.annatala.pixelponies.actors.hero.Hero;
import com.annatala.pixelponies.effects.CellEmitter;
import com.annatala.pixelponies.effects.Lightning;
import com.annatala.pixelponies.effects.particles.SparkParticle;
import com.annatala.utils.GLog;
import com.annatala.utils.Utils;
import com.annatala.utils.Random;

public class LightningTrap {

	private static final String name = Game.getVar(R.string.LightningTrap_Name);
	
	// 00x66CCEE
	
	public static void trigger( int pos, @Nullable Char ch ) {
		
		if (ch != null) {
			ch.damage( Math.max( 1, Random.Int( ch.hp() / 3, 2 * ch.hp() / 3 ) ), LIGHTNING );
			if (ch == Dungeon.hero) {
				
				Camera.main.shake( 2, 0.3f );
				
				if (!ch.isAlive()) {
					Dungeon.fail( Utils.format( ResultDescriptions.TRAP, name, Dungeon.depth ) );
					GLog.n(Game.getVar(R.string.LightningTrap_Desc));
				} else {
					((Hero)ch).belongings.charge( false );
				}
			}
			
			int[] points = new int[2];
			
			points[0] = pos - Dungeon.level.getWidth();
			points[1] = pos + Dungeon.level.getWidth();
			ch.getSprite().getParent().add( new Lightning( points, 2, null ) );
			
			points[0] = pos - 1;
			points[1] = pos + 1;
			ch.getSprite().getParent().add( new Lightning( points, 2, null ) );
		}
		
		CellEmitter.center( pos ).burst( SparkParticle.FACTORY, Random.IntRange( 3, 4 ) );
		
	}
	
	public static final Electricity LIGHTNING = new Electricity();
	public static class Electricity {	
	}
}
