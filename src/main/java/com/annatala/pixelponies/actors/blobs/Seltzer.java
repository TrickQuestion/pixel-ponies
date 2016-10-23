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

import com.annatala.pixelponies.Dungeon;
import com.annatala.pixelponies.actors.Actor;
import com.annatala.pixelponies.actors.Char;
import com.annatala.pixelponies.actors.buffs.Buff;
import com.annatala.pixelponies.actors.buffs.Frost;
import com.annatala.pixelponies.effects.CellEmitter;
import com.annatala.pixelponies.effects.particles.FlowParticle;
import com.annatala.pixelponies.effects.particles.SnowParticle;
import com.annatala.pixelponies.items.Heap;
import com.annatala.utils.Random;

public class Seltzer {

	// for newInstance used if Buff.affect
	public Seltzer() {
	}
	// It's not really a blob...
	
	public static void affect( int cell, Fire fire ) {
		
		if (fire != null) {
			fire.clearBlob( cell );
		}

		if (Dungeon.visible[cell]) {
			CellEmitter.get( cell ).start( FlowParticle.FACTORY, 0.2F, 20 );
		}
	}
}
