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
package com.annatala.pixelponies.actors.mobs.npcs;

import com.annatala.pixelponies.Dungeon;
import com.annatala.pixelponies.actors.Gender;
import com.annatala.pixelponies.actors.hero.Hero;
import com.annatala.pixelponies.actors.mobs.Fraction;
import com.annatala.pixelponies.actors.mobs.Mob;
import com.annatala.pixelponies.items.Heap;
import com.annatala.pixelponies.levels.Level;
import com.annatala.utils.Random;

public abstract class NPC extends Mob {
	
	protected NPC() {
		gender = Gender.MASCULINE;

		hp(ht(1));
		EXP = 0;
	
		hostile = false;
		state = PASSIVE;
		
		fraction = Fraction.NEUTRAL;
	}
	
	protected void throwItem() {
		Heap heap = Dungeon.level.getHeap( getPos() );
		if (heap != null) {
			int n;
			do {
				n = getPos() + Level.NEIGHBOURS8[Random.Int( 8 )];
			} while (!Dungeon.level.passable[n] && !Dungeon.level.avoid[n]);
			Dungeon.level.drop( heap.pickUp(), n ).sprite.drop( getPos() );
		}
	}
	
	@Override
	public void beckon( int cell ) {
	}
	
	@Override
	public boolean interact(final Hero hero){
		swapPosition(hero);
		return true;
	}

	public boolean friendly() {
		return true;
	}
}
