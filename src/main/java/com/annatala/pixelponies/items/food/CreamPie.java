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
package com.annatala.pixelponies.items.food;

import com.annatala.noosa.Game;
import com.annatala.pixelponies.actors.Actor;
import com.annatala.pixelponies.actors.Char;
import com.annatala.pixelponies.actors.buffs.Buff;
import com.annatala.pixelponies.actors.buffs.Hunger;
import com.annatala.pixelponies.actors.buffs.Vertigo;
import com.annatala.pixelponies.actors.hero.Hero;
import com.annatala.pixelponies.android.R;
import com.annatala.pixelponies.sprites.CharSprite;
import com.annatala.pixelponies.sprites.ItemSpriteSheet;
import com.annatala.utils.GLog;

public class CreamPie extends Pie {

	{
		image = ItemSpriteSheet.CREAM_PIE;
		energy = Hunger.STARVING - Hunger.HUNGRY;
		message = Game.getVar(R.string.CreamPie_Message);
	}

	@Override
	protected void onThrow( int cell ) {
		Char target = Actor.findChar(cell);

		if (target == getCurUser()) {
			target.add(Buff.affect(target, Vertigo.class, Vertigo.duration(target)));
			Hero hero = (Hero) target;
			if (!hero.hasHadLaughterIncreasedByPie) {
				hero.setLaughter(hero.laughter() + 1);
				hero.hasHadLaughterIncreasedByPie = true;
				String text = "You pied yourself. It was really funny! You learned to laugh a little more.";
				hero.getSprite().showStatus(CharSprite.POSITIVE, text); //TODO replace this trickster
				GLog.p(text);
				//Badges.validateLaughterAttained(); TODO implement this
			} else {
				String text = "YOU PIE YOURSELF";
				hero.getSprite().showStatus(CharSprite.POSITIVE, text); //TODO replace this trickster
				GLog.p(text);
			}
		} else if (target == null){
			super.onThrow(cell); //Throw it on the ground
		} else { //Target is an enemy
			if (!getCurUser().shoot(target, this)) { //If you miss the enemy
				miss(cell); //Throw it on the ground
			} else { //Else you hit the enemy
				target.add(Buff.affect(target, Vertigo.class, Vertigo.duration(target)));
			}
		}
	}

	@Override
	public boolean isFunnySplat() {
		return true;
	}
	
	@Override
	public int price() {
		return 100 * quantity();
	}
	
}
