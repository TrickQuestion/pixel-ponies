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
import com.annatala.pixelponies.Badges;
import com.annatala.pixelponies.actors.hero.Hero;
import com.annatala.pixelponies.android.R;
import com.annatala.pixelponies.items.Item;
import com.annatala.pixelponies.sprites.CharSprite;
import com.annatala.pixelponies.sprites.ItemSpriteSheet;
import com.annatala.utils.GLog;

import java.util.ArrayList;

public class Spellbook extends Item {

	public static final String AC_STUDY	  = Game.getVar(R.string.Spellbook_ACStudy);
	public static final float TIME_TO_STUDY = 3F;

	{
		stackable = true;
		name = Game.getVar(R.string.Spellbook_Name);
		image = ItemSpriteSheet.SPELLBOOK;
	}

	@Override
	public ArrayList<String> actions(Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		actions.add( AC_STUDY );
		return actions;
	}

	@Override
	public void execute( Hero hero, String action ) {
		if (action.equals( AC_STUDY )) {

			detach( hero.belongings.backpack );
			hero.spend( TIME_TO_STUDY );
			hero.setMagic(hero.magic() + 1);
			hero.getSprite().showStatus(CharSprite.POSITIVE, Game.getVar(R.string.Spellbook_StaApply));
			GLog.p(Game.getVar(R.string.Spellbook_Apply));
			//Badges.validateMagicAttained();
		} else {
			super.execute( hero, action );
		}
	}

	@Override
	public boolean isUpgradable() {
		return false;
	}
	
	@Override
	public boolean isIdentified() {
		return true;
	}

	@Override
	public String desc() {
		return Game.getVar(R.string.Spellbook_Info);
	}
	
	@Override
	public int price() {
		return 100 * quantity();
	}
}
