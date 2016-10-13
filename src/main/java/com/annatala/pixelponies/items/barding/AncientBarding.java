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
package com.annatala.pixelponies.items.barding;

import com.annatala.pixelponies.android.R;
import com.annatala.noosa.Game;
import com.annatala.pixelponies.actors.hero.Hero;
import com.annatala.utils.GLog;


public class AncientBarding extends Barding {

	private static final String TXT_HORN	= Game.getVar(R.string.AncientBarding_NotHorn);

	public AncientBarding() {
		super( 6 );
		image = 5;
	}

	@Override
	public boolean doEquip( Hero hero ) {
		if (hero.heroClass.hasHorn()) {
			return super.doEquip( hero );
		} else {
			GLog.w( TXT_HORN );
			return false;
		}
	}

	@Override
	public String desc() {
		return Game.getVar(R.string.AncientBarding_Desc);
	}

}
