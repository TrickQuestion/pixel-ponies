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
package com.annatala.pixelponies.actors.buffs;

import com.annatala.pixelponies.Dungeon;
import com.annatala.pixelponies.actors.Char;
import com.annatala.pixelponies.actors.mobs.Fraction;
import com.annatala.pixelponies.actors.mobs.Mob;
import com.annatala.pixelponies.ui.BuffIndicator;
import com.annatala.utils.Bundle;

public class Terror extends FlavourBuff {

	public static final float DURATION = 10f;
	public Char source;
	
	@Override
	public int icon() {
		return BuffIndicator.TERROR;
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		// It's not really correct...
		source = Dungeon.hero;
	}
	
	@Override
	public String toString() {
		return "Terror";
	}
	
	public static void recover( Char target ) {
		Terror terror = target.buff( Terror.class );
		if (terror != null && terror.cooldown() < DURATION) {
			target.remove( terror );
		}
	}

	@Override
	public boolean attachTo(Char target) {
		if(super.attachTo(target)) {
			if(target instanceof Mob) {
				Mob tgt = (Mob)target;
				tgt.setFraction(Fraction.DUNGEON);
			}
			return true;
		}
		return false;
	}
}
