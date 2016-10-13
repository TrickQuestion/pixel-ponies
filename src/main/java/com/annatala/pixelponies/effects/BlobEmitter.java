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
package com.annatala.pixelponies.effects;

import com.annatala.noosa.particles.Emitter;
import com.annatala.pixelponies.Dungeon;
import com.annatala.pixelponies.DungeonTilemap;
import com.annatala.pixelponies.actors.blobs.Blob;
import com.annatala.utils.Random;

public class BlobEmitter extends Emitter {
	
	private Blob blob;
	
	public BlobEmitter( Blob blob ) {
		this.blob = blob;
		blob.use( this );
	}
	
	@Override
	protected void emit( int index ) {
		
		if (blob.volume <= 0) {
			return;
		}
		
		int[] map = blob.cur;
		float size = DungeonTilemap.SIZE;
		
		for (int i=0; i < Dungeon.level.getLength(); i++) {
			if (map[i] > 0 && Dungeon.visible[i]) {
				float x = ((i % Dungeon.level.getWidth()) + Random.Float()) * size;
				float y = ((i / Dungeon.level.getWidth()) + Random.Float()) * size;
				factory.emit( this, index, x, y );
			}
		}
	}
}
