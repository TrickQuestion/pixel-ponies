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
package com.annatala.pixelponies.items.scrolls;

import com.annatala.pixelponies.android.R;
import com.annatala.noosa.Game;
import com.annatala.pixelponies.actors.hero.Hero;
import com.annatala.pixelponies.sprites.CharSprite;
import com.annatala.utils.GLog;

public class ScrollOfLoyalOath extends Scroll {

	@Override
	protected void doRead() {


		setKnown();
		Hero hero = getCurUser();

		hero.setLoyalty(hero.loyalty() + 1);
		hero.getSprite().showStatus( CharSprite.POSITIVE, Game.getVar(R.string.ScrollOfLoyalOath_StaApply));
		GLog.p(Game.getVar(R.string.ScrollOfLoyalOath_Apply));

		// Badges.validateLoyaltyAttained();  TODO: Maybe add this for each stat?

		getCurUser().spendAndNext( TIME_TO_READ );
	}

	@Override
	public int price() {
		return isKnown() ? 100 * quantity() : super.price();
	}
}
