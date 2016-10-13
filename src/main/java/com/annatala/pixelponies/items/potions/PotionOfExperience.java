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
package com.annatala.pixelponies.items.potions;

import com.annatala.noosa.Game;
import com.annatala.pixelponies.android.R;
import com.annatala.pixelponies.actors.hero.Hero;

public class PotionOfExperience extends Potion {

	@Override
	protected void apply( Hero hero ) {
		setKnown();
		hero.earnExp( hero.maxExp() - hero.getExp());
	}
	
	@Override
	public String desc() {
		return Game.getVar(R.string.PotionOfExperience_Info);
	}
	
	@Override
	public int price() {
		return isKnown() ? 80 * quantity() : super.price();
	}
}
