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
import com.annatala.pixelponies.actors.buffs.Blindness;
import com.annatala.pixelponies.actors.hero.Hero;
import com.annatala.pixelponies.android.R;
import com.annatala.pixelponies.sprites.ItemSpriteSheet;
import com.annatala.pixelponies.windows.WndStory;
import com.annatala.utils.Bundle;
import com.annatala.utils.GLog;
import com.annatala.utils.Random;

public class RareCodex extends Codex {

	public RareCodex(){
		image     = ItemSpriteSheet.RARE_CODEX;
		idTag	  = "rare-codex-id";

		//TODO Need rework this. Transifex just hates string-arrays
		maxId     = Game.getVars(R.array.RareCodex_Titles).length;
		id        = Random.Int(maxId);
		name	  = Game.getVars(R.array.RareCodex_Titles)[id];
	}

	@Override
	public void execute( Hero hero, String action ) {
		if (action.equals( AC_READ )) {

			if (hero.buff( Blindness.class ) != null) {
				GLog.w( TXT_BLINDED );
				return;
			}

			setCurUser(hero);

			WndStory.showCustomStory(Game.getVars(R.array.RareCodex_Stories)[id]);
		} else {

			super.execute( hero, action );

		}
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle(bundle);
		id   = bundle.getInt( idTag );
		if(!(id < maxId)){
			id = Random.Int(maxId);
		}
		name = Game.getVars(R.array.RareCodex_Titles)[id];
	}

	@Override
	public int price(){
		return 100;
	}
}
