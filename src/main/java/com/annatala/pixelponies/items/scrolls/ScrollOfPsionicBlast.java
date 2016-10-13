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
package com.annatala.pixelponies.items.scrolls;

import com.annatala.noosa.audio.Sample;
import com.annatala.pixelponies.Assets;
import com.annatala.pixelponies.Dungeon;
import com.annatala.pixelponies.actors.buffs.Blindness;
import com.annatala.pixelponies.actors.buffs.Buff;
import com.annatala.pixelponies.actors.buffs.Invisibility;
import com.annatala.pixelponies.actors.mobs.Mob;
import com.annatala.pixelponies.scenes.GameScene;
import com.annatala.utils.Random;

public class ScrollOfPsionicBlast extends Scroll {

	@Override
	protected void doRead() {
		
		GameScene.flash( 0xFFFFFF );
		
		Sample.INSTANCE.play( Assets.SND_BLAST );
		Invisibility.dispel(getCurUser());
		
		for (Mob mob : Dungeon.level.mobs.toArray(new Mob[Dungeon.level.mobs.size()])) {
			if (Dungeon.level.fieldOfView[mob.getPos()]) {

				// Magic is going to make this whole spell significantly more powerful.
				int blindTurns = 2 + Random.Int(Dungeon.hero.effectiveMagic()) / 2;

				// Let's make magic cause magic% damage as min level.
				int min = Dungeon.hero.effectiveMagic() * (1 + mob.ht()/100);

				// Keeping the old max. (When min > max, the two switch.)
				int max = mob.ht() * 2/3;

				Buff.prolong( mob, Blindness.class, blindTurns );
				mob.damage( Random.IntRange( min, max ), this );
			}
		}

		// Luck won't help you get unblind faster! You did this to yourself.
		Buff.prolong( getCurUser(), Blindness.class, Random.Int( 3, 6 ) );
		Dungeon.observe();
		
		setKnown();
		
		getCurUser().spendAndNext( TIME_TO_READ );
	}

	@Override
	public int price() {
		return isKnown() ? 80 * quantity() : super.price();
	}
}
