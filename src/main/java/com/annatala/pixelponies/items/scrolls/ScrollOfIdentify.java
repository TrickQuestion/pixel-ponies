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
import com.annatala.pixelponies.Badges;
import com.annatala.pixelponies.effects.Identification;
import com.annatala.pixelponies.items.Item;
import com.annatala.utils.GLog;
import com.annatala.utils.Utils;
import com.annatala.pixelponies.windows.WndBag;

public class ScrollOfIdentify extends InventoryScroll {

	{
		inventoryTitle = Game.getVar(R.string.ScrollOfIdentify_InvTitle);
		mode = WndBag.Mode.UNIDENTIFED;
	}
	
	@Override
	protected void onItemSelected( Item item ) {
		
		getCurUser().getSprite().getParent().add( new Identification( getCurUser().getSprite().center().offset( 0, -16 ) ) );
		
		item.identify();
		GLog.i(Utils.format(Game.getVar(R.string.ScrollOfIdentify_Info1), item));
		
		Badges.validateItemLevelAquired( item );
	}

	@Override
	public int price() {
		return isKnown() ? 30 * quantity() : super.price();
	}
}
