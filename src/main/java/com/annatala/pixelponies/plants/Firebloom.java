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
import com.annatala.pixelponies.actors.blobs.Blob;
import com.annatala.pixelponies.actors.blobs.Fire;
import com.annatala.pixelponies.actors.buffs.Buff;
import com.annatala.pixelponies.actors.buffs.Burning;
import com.annatala.pixelponies.actors.buffs.Speed;
import com.annatala.pixelponies.actors.hero.Hero;
import com.annatala.pixelponies.effects.CellEmitter;
import com.annatala.pixelponies.effects.particles.FlameParticle;
import com.annatala.pixelponies.items.food.Food;
import com.annatala.pixelponies.items.potions.PotionOfLiquidFlame;
import com.annatala.pixelponies.scenes.GameScene;
import com.annatala.pixelponies.sprites.ItemSpriteSheet;
import com.annatala.utils.Utils;

public class Firebloom extends Plant {

	private static final String TXT_NAME = Game.getVar(R.string.Firebloom_Name);
	private static final String TXT_DESC = Game.getVar(R.string.Firebloom_Desc);

	public Firebloom() {
		image = 0;
		plantName = TXT_NAME;
	}

	public void effect(int pos, Char ch) {
		GameScene.add(Blob.seed(pos, 2, Fire.class));

		if (Dungeon.visible[pos]) {
			CellEmitter.get(pos).burst(FlameParticle.FACTORY, 5);
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
			image = ItemSpriteSheet.SEED_FIREBLOOM;

			plantClass = Firebloom.class;
			alchemyClass = PotionOfLiquidFlame.class;
		}

		@Override
		public String desc() {
			return TXT_DESC;
		}

		@Override
		public void execute(Hero hero, String action) {

			super.execute(hero, action);

			if (action.equals(Food.AC_EAT)) {
				Buff.affect(hero, Burning.class).reignite(hero);
				Buff.affect(hero, Speed.class, Speed.DURATION);
			}
		}
	}
}
