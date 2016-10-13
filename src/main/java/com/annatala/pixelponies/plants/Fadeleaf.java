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
package com.annatala.pixelponies.plants;

import com.annatala.pixelponies.android.R;
import com.annatala.noosa.Game;
import com.annatala.pixelponies.Dungeon;
import com.annatala.pixelponies.actors.Char;
import com.annatala.pixelponies.actors.buffs.Buff;
import com.annatala.pixelponies.actors.buffs.Vertigo;
import com.annatala.pixelponies.actors.hero.Hero;
import com.annatala.pixelponies.actors.mobs.Mob;
import com.annatala.pixelponies.effects.CellEmitter;
import com.annatala.pixelponies.effects.Speck;
import com.annatala.pixelponies.items.food.Food;
import com.annatala.pixelponies.items.potions.PotionOfMindVision;
import com.annatala.pixelponies.items.scrolls.ScrollOfTeleportation;
import com.annatala.pixelponies.sprites.ItemSpriteSheet;
import com.annatala.utils.Utils;

public class Fadeleaf extends Plant {

	private static final String TXT_NAME = Game.getVar(R.string.Fadeleaf_Name);
	private static final String TXT_DESC = Game.getVar(R.string.Fadeleaf_Desc);
	
	public Fadeleaf () {
		image = 6;
		plantName = TXT_NAME;
	}
	
	public void effect(int pos, Char ch) {
		if (ch instanceof Hero) {
			Hero hero = (Hero)ch;
			ScrollOfTeleportation.teleportHero( hero );
			hero.spendAndNext(1);
			hero.curAction = null;
			
		} else if (ch instanceof Mob) {
			
			int newPos = Dungeon.level.randomRespawnCell();
			if (Dungeon.level.cellValid(newPos)) {
				ch.setPos(newPos);
				ch.getSprite().place(ch.getPos());
				ch.getSprite().setVisible(Dungeon.visible[pos]);
			}
		}
		
		if (Dungeon.visible[pos]) {
			CellEmitter.get( pos ).start( Speck.factory( Speck.LIGHT ), 0.2f, 3 );
		}		
	}
	
	@Override
	public String desc() {
		return TXT_DESC;
	}
	
	public static class Seed extends Plant.Seed {
		{
			plantName = TXT_NAME;
			
			name = Utils.format(TXT_SEED, plantName);
			image = ItemSpriteSheet.SEED_FADELEAF;
			
			plantClass = Fadeleaf.class;
			alchemyClass = PotionOfMindVision.class;
		}
		
		@Override
		public String desc() {
			return TXT_DESC;
		}
		
		@Override
		public void execute( Hero hero, String action ) {
			
			super.execute( hero, action );
			
			if (action.equals( Food.AC_EAT )) {
				ScrollOfTeleportation.teleportHero( hero );
				hero.spendAndNext(1);
				hero.curAction = null;
				Buff.affect(hero, Vertigo.class, Vertigo.DURATION * 2);
			}
		}
	}
}