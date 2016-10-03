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
package com.watabou.pixeldungeon;

import com.nyrds.pixeldungeon.ml.R;
import com.watabou.noosa.Game;

public class Challenges {

	public static final int NO_FOOD				= 1;
	public static final int NO_BARDING			= 2;
	public static final int NO_HEALING			= 4;
	public static final int NO_HERBALISM		= 8;
	public static final int SWARM_INTELLIGENCE	= 16;
	public static final int DARKNESS			= 32;
	
	public static final String[] NAMES = Game.getVars(R.array.Challenges_Names);
	
	public static final int[] MASKS = {
		NO_FOOD, NO_BARDING, NO_HEALING, NO_HERBALISM, SWARM_INTELLIGENCE, DARKNESS
	};
	
}
