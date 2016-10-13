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
import com.annatala.noosa.audio.Sample;
import com.annatala.pixelponies.Assets;
import com.annatala.pixelponies.Dungeon;
import com.annatala.pixelponies.android.R;
import com.annatala.pixelponies.actors.buffs.Invisibility;
import com.annatala.pixelponies.actors.hero.Hero;
import com.annatala.pixelponies.items.wands.WandOfBlink;
import com.annatala.utils.GLog;

public class ScrollOfTeleportation extends Scroll {

	public static final String TXT_TELEPORTED = Game.getVar(R.string.ScrollOfTeleportation_Teleport);
	public static final String TXT_NO_TELEPORT = Game.getVar(R.string.ScrollOfTeleportation_NoTeleport);
	
	@Override
	protected void doRead() {

		Sample.INSTANCE.play( Assets.SND_READ );
		Invisibility.dispel(getCurUser());
		
		teleportHero( getCurUser() );
		setKnown();
		
		getCurUser().spendAndNext( TIME_TO_READ );
	}
	
	public static void teleportHero( Hero  hero ) {

		if(Dungeon.level.isBossLevel()) {
			GLog.w( TXT_NO_TELEPORT );
			return;
		}

		int pos = Dungeon.level.randomRespawnCell();

		if (!Dungeon.level.cellValid(pos)) {
			
			GLog.w( TXT_NO_TELEPORT );
			
		} else {

			WandOfBlink.appear( hero, pos );
			Dungeon.level.pressHero( pos, hero );
			Dungeon.observe();
			
			GLog.i( TXT_TELEPORTED );
			
		}
	}

	@Override
	public int price() {
		return isKnown() ? 40 * quantity() : super.price();
	}
}
