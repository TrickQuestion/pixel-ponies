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

import com.annatala.noosa.Game;
import com.annatala.pixelponies.Badges;
import com.annatala.pixelponies.Dungeon;
import com.annatala.pixelponies.android.R;
import com.annatala.pixelponies.actors.hero.Hero;
import com.annatala.pixelponies.effects.Speck;
import com.annatala.pixelponies.items.Item;
import com.annatala.utils.GLog;
import com.annatala.pixelponies.windows.WndBag;

public class ScrollOfUpgrade extends InventoryScroll {

	private static final String TXT_LOOKS_BETTER = Game.getVar(R.string.ScrollOfUpgrade_LooksBetter);

	{
		inventoryTitle = Game.getVar(R.string.ScrollOfUpgrade_InvTitle);
		mode = WndBag.Mode.UPGRADEABLE;
	}

	@Override
	protected void onItemSelected( Item item ) {

		ScrollOfRemoveCurse.uncurse( Dungeon.hero, item );
		item.upgrade();

		GLog.p( TXT_LOOKS_BETTER, item.name() );

		Badges.validateItemLevelAquired( item );

		upgrade( getCurUser() );
	}

	public static void upgrade( Hero hero ) {
		hero.getSprite().emitter().start( Speck.factory( Speck.UP ), 0.2f, 3 );
	}
}
