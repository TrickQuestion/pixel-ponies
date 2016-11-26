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
package com.annatala.pixelponies.items.utility;

import com.annatala.noosa.Game;
import com.annatala.pixelponies.Dungeon;
import com.annatala.pixelponies.actors.Actor;
import com.annatala.pixelponies.actors.Char;
import com.annatala.pixelponies.actors.blobs.Fire;
import com.annatala.pixelponies.actors.blobs.Seltzer;
import com.annatala.pixelponies.actors.hero.Hero;
import com.annatala.pixelponies.android.R;
import com.annatala.pixelponies.items.Item;
import com.annatala.pixelponies.mechanics.Ballistica;
import com.annatala.pixelponies.scenes.CellSelector;
import com.annatala.pixelponies.scenes.GameScene;
import com.annatala.pixelponies.sprites.ItemSpriteSheet;
import com.annatala.utils.Utils;

import java.util.ArrayList;

public class SeltzerBottle extends Item {

	public static final String AC_SPRAY = Game.getVar(R.string.SeltzerBottle_Spray);
	public static final String TXT_INFO = Game.getVar(R.string.SeltzerBottle_Info);
	public static final String TXT_SQUIRTS = Game.getVar(R.string.SeltzerBottle_Squirts);
	public static final String TXT_SQUIRT = Game.getVar(R.string.SeltzerBottle_Squirt);
	public static final String TXT_EMPTY = Game.getVar(R.string.SeltzerBottle_Empty);
	private static final float TIME_TO_SPRAY = 1.0F;

	private int squirts;

	{
		identify();
		image = ItemSpriteSheet.SELTZER_BOTTLE;
		stackable = false;
	}

	public SeltzerBottle() {
		squirts = 3;
	}

	@Override
	public void execute( final Hero hero, String action ) {
		if (action.equals(AC_SPRAY)) {
			setCurUser(hero);
			GameScene.selectCell(sprayer);
			squirts--;
		} else {
			super.execute(hero, action);
		}
	}

	@Override
	public ArrayList<String> actions(Hero hero ) {
		ArrayList<String> actions = super.actions(hero);
		if (squirts > 0) {
			actions.add(AC_SPRAY);
		}
		return actions;
	}

	protected static CellSelector.Listener sprayer = new CellSelector.Listener() {
		@Override
		public void onSelect(Integer target) {

			if (target != null) {
				getCurUser().spendAndNext(TIME_TO_SPRAY);
				int hitCell = Ballistica.cast(getCurUser().getPos(), target, false, true);

				Seltzer.affect(target, (Fire)Dungeon.level.blobs.get(Fire.class));

				if (hitCell == getCurUser().getPos()) {
					return;
				}

				Char chr = null;

				if (Dungeon.level.distance(getCurUser().getPos(), hitCell) < 4) {
					chr = Actor.findChar(hitCell);
				}

				if (chr != null) {

					if (chr.flying) {
						chr.move(Ballistica.trace[1]);
						chr.getSprite().move(chr.getPos(), Ballistica.trace[1]);
					}

					Dungeon.observe();

				}

			}

		}

		@Override
		public String prompt() {
			return TXT_DIR_SHOOT;
		}
	};

	@Override
	public boolean isUpgradable() {
		return false;
	}

	@Override
	public String desc() {
		String desc = TXT_INFO + " ";
		if (squirts > 1) {
			desc += Utils.format( TXT_SQUIRTS, squirts );
		} else if (squirts == 1) {
			desc += TXT_SQUIRT;
		} else {
			desc += TXT_EMPTY;
		}
		return desc;
	}

	@Override
	public boolean isIdentified() {
		return true;
	}

	@Override
	public int price() {
		return 50 * squirts;
	}

}
