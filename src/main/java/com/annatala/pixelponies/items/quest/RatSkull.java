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
package com.annatala.pixelponies.items.quest;

import com.annatala.pixelponies.android.R;
import com.annatala.noosa.Game;
import com.annatala.pixelponies.items.rings.Artifact;
import com.annatala.pixelponies.sprites.ItemSpriteSheet;
import com.annatala.pixelponies.ui.BuffIndicator;

public class RatSkull extends Artifact {

	public RatSkull() {
		image = ItemSpriteSheet.SKULL;
	}

	@Override
	public boolean isIdentified() {
		return true;
	}

	@Override
	protected ArtifactBuff buff() {
		return new RatterAura();
	}

	@Override
	public String info() {
		return super.info() + "\n\n" + Game.getVar(R.string.RatSkull_Info2);
	}

	public class RatterAura extends ArtifactBuff {
		@Override
		public int icon() {
			return BuffIndicator.RAT_SKULL;
		}

		@Override
		public String toString() {
			return Game.getVar(R.string.RatSkull_Buff);
		}
	}

	@Override
	public int price() {
		return 100;
	}
}
