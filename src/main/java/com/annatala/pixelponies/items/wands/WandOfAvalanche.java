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
package com.annatala.pixelponies.items.wands;

import com.annatala.noosa.Camera;
import com.annatala.noosa.Game;
import com.annatala.noosa.audio.Sample;
import com.annatala.pixelponies.Assets;
import com.annatala.pixelponies.Dungeon;
import com.annatala.pixelponies.android.R;
import com.annatala.pixelponies.ResultDescriptions;
import com.annatala.pixelponies.actors.Actor;
import com.annatala.pixelponies.actors.Char;
import com.annatala.pixelponies.actors.buffs.Buff;
import com.annatala.pixelponies.actors.buffs.Paralysis;
import com.annatala.pixelponies.effects.CellEmitter;
import com.annatala.pixelponies.effects.MagicMissile;
import com.annatala.pixelponies.effects.Speck;
import com.annatala.pixelponies.mechanics.Ballistica;
import com.annatala.utils.BArray;
import com.annatala.utils.GLog;
import com.annatala.utils.Utils;
import com.annatala.utils.Callback;
import com.annatala.utils.PathFinder;
import com.annatala.utils.Random;

public class WandOfAvalanche extends SimpleWand {
	{
		hitChars = false;
	}
	
	@Override
	protected void onZap( int cell ) {
		
		Sample.INSTANCE.play( Assets.SND_ROCKS );
		
		int level = effectiveLevel();
		
		Ballistica.distance = Math.min( Ballistica.distance, 8 + level );
		
		int size = 1 + level / 3;
		PathFinder.buildDistanceMap( cell, BArray.not( Dungeon.level.solid, null ), size );
		
		for (int i=0; i < Dungeon.level.getLength(); i++) {
			
			int d = PathFinder.distance[i];
			
			if (d < Integer.MAX_VALUE) {
				
				Char ch = Actor.findChar( i ); 
				if (ch != null) {
					
					ch.getSprite().flash();
					
					ch.damage( Random.Int( 2, 6 + (size - d) * 2 ), this );
					
					if (ch.isAlive() && Random.Int( 2 + d ) == 0) {
						Buff.prolong( ch, Paralysis.class, Random.IntRange( 2, 6 ) );
					}
				}

				CellEmitter.get( i ).start( Speck.factory( Speck.ROCK ), 0.07f, 3 + (size - d) );
				Camera.main.shake( 3, 0.07f * (3 + (size - d)) );
			}
		}
		
		if (getCurUser() != null && !getCurUser().isAlive()) {
			Dungeon.fail( Utils.format( ResultDescriptions.WAND, name, Dungeon.depth ) );
			GLog.n(Game.getVar(R.string.WandOfAvalanche_Info1));
		}
	}
	
	protected void fx( int cell, Callback callback ) {
		MagicMissile.earth( wandUser.getSprite().getParent(), wandUser.getPos(), cell, callback );
		Sample.INSTANCE.play( Assets.SND_ZAP );
	}
	
	@Override
	public String desc() {
		return Game.getVar(R.string.WandOfAvalanche_Info);
	}
}
