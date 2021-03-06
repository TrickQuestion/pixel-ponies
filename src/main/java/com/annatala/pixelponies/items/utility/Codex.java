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

import com.annatala.pixelponies.android.R;
import com.annatala.noosa.Game;
import com.annatala.pixelponies.actors.buffs.Blindness;
import com.annatala.pixelponies.actors.hero.Hero;
import com.annatala.pixelponies.items.Item;
import com.annatala.pixelponies.sprites.ItemSpriteSheet;
import com.annatala.utils.GLog;
import com.annatala.pixelponies.windows.WndStory;
import com.annatala.utils.Bundle;
import com.annatala.utils.Random;

import java.util.ArrayList;

public abstract class Codex extends Item {

	protected static final String TXT_BLINDED	= Game.getVar(R.string.Codex_Blinded);
	public static final String AC_READ	= Game.getVar(R.string.Codex_ACRead);

	protected static String idTag;
	protected int maxId;
	protected int id;

	protected String text;

	public Codex(){
		stackable = false;
	}
	
	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		actions.add( AC_READ );
		return actions;
	}
	
	@Override
	public boolean isIdentified() {
		return true;
	}
	
	@Override
	public boolean isUpgradable() {
		return false;
	}

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle(bundle);
		bundle.put(idTag, id);
	}

	@Override
	public Item burn(int cell){
		return null;
	}

}
