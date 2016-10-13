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

import com.annatala.noosa.Game;
import com.annatala.noosa.audio.Sample;
import com.annatala.pixelponies.Assets;
import com.annatala.pixelponies.android.R;
import com.annatala.pixelponies.actors.buffs.Invisibility;
import com.annatala.pixelponies.actors.hero.Hero;
import com.annatala.pixelponies.effects.SpellSprite;
import com.annatala.pixelponies.effects.particles.EnergyParticle;
import com.annatala.utils.GLog;

public class ScrollOfRecharging extends Scroll {
	
	@Override
	protected void doRead() {
		
		int count = getCurUser().belongings.charge( true );		
		charge( getCurUser() );
		
		Sample.INSTANCE.play( Assets.SND_READ );
		Invisibility.dispel(getCurUser());
		
		if (count > 0) {
			GLog.i((count > 1 ? Game.getVar(R.string.ScrollOfRecharging_Info1b) 
					          : Game.getVar(R.string.ScrollOfRecharging_Info1a)) );
			SpellSprite.show( getCurUser(), SpellSprite.CHARGE );
		} else {
			GLog.i(Game.getVar(R.string.ScrollOfRecharging_Info2));
		}
		setKnown();
		
		getCurUser().spendAndNext( TIME_TO_READ );
	}

	public static void charge( Hero hero ) {
		hero.getSprite().centerEmitter().burst( EnergyParticle.FACTORY, 15 );
	}
	
	@Override
	public int price() {
		return isKnown() ? 40 * quantity() : super.price();
	}
}
