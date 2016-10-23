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
package com.annatala.pixelponies.items.weapon.melee;

import com.annatala.noosa.Game;
import com.annatala.pixelponies.Dungeon;
import com.annatala.pixelponies.actors.Char;
import com.annatala.pixelponies.actors.buffs.Buff;
import com.annatala.pixelponies.actors.buffs.FlavourBuff;
import com.annatala.pixelponies.actors.buffs.Vertigo;
import com.annatala.pixelponies.android.R;
import com.annatala.pixelponies.items.Gold;
import com.annatala.pixelponies.sprites.CharSprite;
import com.annatala.pixelponies.sprites.ItemSpriteSheet;
import com.annatala.utils.GLog;
import com.annatala.utils.Random;

public class RubberChicken extends MeleeWeapon {

	public static final String RC_INFO = Game.getVar(R.string.RubberChicken_Info);
	public static final String RC_SMASH = Game.getVar(R.string.RubberChicken_Smash);
	public static final String RC_APPLY = Game.getVar(R.string.RubberChicken_StaApply);

	{
		identify();
		image = ItemSpriteSheet.RUBBER_CHICKEN;
	}

	public RubberChicken() {
		super( 0, 0.5f, 1.5f );
		MIN = 0;
		MAX = 1;
	}

	@Override
	public String desc() {
		return RC_INFO;
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
	public void proc(Char attacker, Char defender, int damage ) {

		if (Random.Int(7) == 0){
			this.removeItemFrom(Dungeon.hero);
            Class <? extends FlavourBuff> buffClass = Vertigo.class;
            Buff.prolong( defender, buffClass, 3 );
			GLog.p(RC_SMASH);
			Dungeon.hero.setLaughter(Dungeon.hero.laughter() + 1);
			Dungeon.hero.getSprite().showStatus(CharSprite.POSITIVE, RC_APPLY);
			// Badges.validateLaughterAttained;
		}
		usedForHit();
	}

	@Override
	public int price() {
		return 100 * quantity();
	}
}
