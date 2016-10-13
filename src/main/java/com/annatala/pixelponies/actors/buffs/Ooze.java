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

import com.annatala.noosa.Game;
import com.annatala.pixelponies.Dungeon;
import com.annatala.pixelponies.android.R;
import com.annatala.pixelponies.ResultDescriptions;
import com.annatala.pixelponies.ui.BuffIndicator;
import com.annatala.utils.GLog;
import com.annatala.utils.Utils;

public class Ooze extends Buff {
	
	private static final String TXT_DEATH = Game.getVar(R.string.Ooze_Death);

	public int damage	= 1;
	
	@Override
	public int icon() {
		return BuffIndicator.OOZE;
	}
	
	@Override
	public String toString() {
		return Game.getVar(R.string.Ooze_Info);
	}
	
	@Override
	public boolean act() {
		if (target.isAlive()) {
			target.damage( damage, this );
			if (!target.isAlive() && target == Dungeon.hero) {
				Dungeon.fail( Utils.format( ResultDescriptions.OOZE, Dungeon.depth ) );
				GLog.n( TXT_DEATH, toString() );
			}
			spend( TICK );
		}
		if (Dungeon.level.water[target.getPos()]) {
			detach();
		}
		return true;
	}
}
