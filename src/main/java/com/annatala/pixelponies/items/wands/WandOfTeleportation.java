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
package com.annatala.pixelponies.items.wands;

import com.annatala.pixelponies.android.R;
import com.annatala.noosa.Game;
import com.annatala.noosa.audio.Sample;
import com.annatala.pixelponies.Assets;
import com.annatala.pixelponies.Dungeon;
import com.annatala.pixelponies.actors.Actor;
import com.annatala.pixelponies.actors.Char;
import com.annatala.pixelponies.actors.mobs.Boss;
import com.annatala.pixelponies.effects.MagicMissile;
import com.annatala.pixelponies.items.scrolls.ScrollOfTeleportation;
import com.annatala.utils.GLog;
import com.annatala.utils.Utils;
import com.annatala.utils.Callback;

public class WandOfTeleportation extends Wand {

	public static void teleport(Char ch) {
		int pos = Dungeon.level.randomRespawnCell();
		
		if(!Dungeon.level.cellValid(pos)) {
			GLog.i(Game.getVar(R.string.WandOfTeleportation_Info2));
			return;
		}
		
		ch.setPos(pos);
		ch.getSprite().place(ch.getPos());
		ch.getSprite().setVisible(Dungeon.visible[pos]);
		GLog.i(Utils.format(Game.getVar(R.string.WandOfTeleportation_Info1), getCurUser().getName(),
				ch.getName_objective()));
	}

	@Override
	protected void onZap(int cell) {

		Char ch = Actor.findChar(cell);

		if (ch == getCurUser()) {

			setKnown();
			ScrollOfTeleportation.teleportHero(getCurUser());

		} else if (ch != null && ! (ch instanceof Boss) ) {

			teleport(ch);

		} else {
			GLog.i(Game.getVar(R.string.WandOfTeleportation_Info2));
		}
	}

	protected void fx(int cell, Callback callback) {
		MagicMissile.coldLight(wandUser.getSprite().getParent(), wandUser.getPos(), cell, callback);
		Sample.INSTANCE.play(Assets.SND_ZAP);
	}

	@Override
	public String desc() {
		return Game.getVar(R.string.WandOfTeleportation_Info);
	}
}
