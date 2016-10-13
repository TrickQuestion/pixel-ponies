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

import com.annatala.pixelponies.android.R;
import com.annatala.noosa.Game;
import com.annatala.pixelponies.actors.buffs.Buff;
import com.annatala.pixelponies.actors.buffs.Levitation;
import com.annatala.pixelponies.actors.hero.Hero;
import com.annatala.pixelponies.items.food.RottenFood;
import com.annatala.pixelponies.items.scrolls.Scroll;
import com.annatala.pixelponies.items.weapon.missiles.Arrow;
import com.annatala.utils.GLog;

public class PotionOfLevitation extends Potion {

	@Override
	protected void apply( Hero hero ) {
		setKnown();
		Buff.affect( hero, Levitation.class, Levitation.DURATION );
		GLog.i(Game.getVar(R.string.PotionOfLevitation_Apply));
	}
	
	@Override
	public String desc() {
		return Game.getVar(R.string.PotionOfLevitation_Info);
	}
	
	@Override
	public int price() {
		return isKnown() ? 35 * quantity() : super.price();
	}
	
	@Override
	protected void moistenArrow(Arrow arrow) {
		detachMoistenItems(arrow, 10);
		GLog.i(TXT_ITEM_FLIES_AWAY , arrow.name());
		moistenEffective();
	}
	
	@Override
	protected void moistenScroll(Scroll scroll) {
		detachMoistenItems(scroll,3);
		GLog.i(TXT_ITEM_FLIES_AWAY , scroll.name());
		moistenEffective();
	}
	
	@Override
	protected void moistenRottenFood(RottenFood rfood) {
		detachMoistenItems(rfood,1);
		
		GLog.i(TXT_ITEM_FLIES_AWAY , rfood.name());
		moistenEffective();
	}
}
