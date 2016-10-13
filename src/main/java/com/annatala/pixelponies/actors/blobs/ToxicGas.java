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
package com.annatala.pixelponies.actors.blobs;

import com.annatala.pixelponies.android.R;
import com.annatala.noosa.Game;
import com.annatala.pixelponies.Badges;
import com.annatala.pixelponies.Dungeon;
import com.annatala.pixelponies.ResultDescriptions;
import com.annatala.pixelponies.actors.Actor;
import com.annatala.pixelponies.actors.Char;
import com.annatala.pixelponies.actors.hero.Hero;
import com.annatala.pixelponies.effects.BlobEmitter;
import com.annatala.pixelponies.effects.Speck;
import com.annatala.pixelponies.items.Heap;
import com.annatala.utils.GLog;
import com.annatala.utils.Utils;
import com.annatala.utils.Random;

public class ToxicGas extends Blob implements Hero.Doom {
	
	@Override
	protected void evolve() {
		super.evolve();
		
		for (int pos=0; pos < getLength(); pos++) {
			if (cur[pos] > 0){
				poison(pos);
			}
		}
		
		Blob blob = Dungeon.level.blobs.get( ParalyticGas.class );
		if (blob != null) {
			
			int par[] = blob.cur;
			
			for (int i=0; i < getLength(); i++) {
				
				int t = cur[i];
				int p = par[i];
				
				if (p >= t) {
					volume -= t;
					cur[i] = 0;
				} else {
					blob.volume -= p;
					par[i] = 0;
				}
			}
		}
	}
	
	private void poison( int pos ) {
		int levelDamage = 5 + Dungeon.depth * 5;
		
		Char ch = Actor.findChar( pos );
		if (ch != null) {
			int damage = (ch.ht() + levelDamage) / 40;
			if (Random.Int( 40 ) < (ch.ht() + levelDamage) % 40) {
				damage++;
			}
			
			ch.damage( damage, this );
		}
		
		Heap heap = Dungeon.level.getHeap( pos );
		if (heap != null) {
			heap.poison();
		}
	}
	
	@Override
	public void use( BlobEmitter emitter ) {
		super.use( emitter );

		emitter.pour( Speck.factory( Speck.TOXIC ), 0.6f );
	}
	
	@Override
	public String tileDesc() {
		return Game.getVar(R.string.ToxicGas_Info);
	}
	
	@Override
	public void onDeath() {
		
		Badges.validateDeathFromGas();
		
		Dungeon.fail( Utils.format( ResultDescriptions.GAS, Dungeon.depth ) );
		GLog.n(Game.getVar(R.string.ToxicGas_Info1));
	}
}
