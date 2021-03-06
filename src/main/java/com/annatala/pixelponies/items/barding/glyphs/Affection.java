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
package com.annatala.pixelponies.items.barding.glyphs;

import com.annatala.pixelponies.android.R;
import com.annatala.noosa.Game;
import com.annatala.pixelponies.Dungeon;
import com.annatala.pixelponies.actors.Char;
import com.annatala.pixelponies.actors.buffs.Buff;
import com.annatala.pixelponies.actors.buffs.Charm;
import com.annatala.pixelponies.effects.Speck;
import com.annatala.pixelponies.items.barding.Barding;
import com.annatala.pixelponies.items.barding.Barding.Glyph;
import com.annatala.pixelponies.items.quest.DriedRose;
import com.annatala.pixelponies.sprites.ItemSprite;
import com.annatala.pixelponies.sprites.ItemSprite.Glowing;
import com.annatala.utils.Utils;
import com.annatala.utils.GameMath;
import com.annatala.utils.Random;

public class Affection extends Glyph {

	private static final String TXT_AFFECTION = Game.getVar(R.string.Affection_Txt);

	private static ItemSprite.Glowing PINK = new ItemSprite.Glowing(0xFF4488);

	@Override
	public int proc(Barding barding, Char attacker, Char defender, int damage) {

		int level = (int) GameMath.gate(0, barding.level(), 6);

		if (Dungeon.level.adjacent(attacker.getPos(), defender.getPos()) && Random.Int(level / 2 + 5) >= 4) {

			int duration = Random.IntRange(2, 5);

			float attackerFactor = 1;
			float defenderFactor = 1;

			if(defender.buff(DriedRose.OneWayLoveBuff.class)!= null) {
				attackerFactor *= 2;
				defenderFactor *= 0;
			}

			if(defender.buff(DriedRose.OneWayCursedLoveBuff.class)!=null) {
				attackerFactor *= 0;
				defenderFactor *= 2;
			}

			Buff.affect(attacker, Charm.class, Charm.durationFactor(attacker) * duration * attackerFactor);
			attacker.getSprite().centerEmitter().start(Speck.factory(Speck.HEART), 0.2f, 5);

			Buff.affect(defender, Charm.class, Random.Float(Charm.durationFactor(defender) * duration / 2, duration) * defenderFactor);
			defender.getSprite().centerEmitter().start(Speck.factory(Speck.HEART), 0.2f, 5);
		}

		return damage;
	}

	@Override
	public String name(String weaponName) {
		return Utils.format(TXT_AFFECTION, weaponName);
	}

	@Override
	public Glowing glowing() {
		return PINK;
	}
}
