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
import com.annatala.pixelponies.actors.buffs.Weakness;
import com.annatala.pixelponies.actors.hero.Hero;
import com.annatala.pixelponies.effects.Flare;
import com.annatala.pixelponies.effects.particles.ShadowParticle;
import com.annatala.pixelponies.items.Item;
import com.annatala.utils.GLog;

public class ScrollOfRemoveCurse extends Scroll {

	private static final String TXT_PROCCED	= Game.getVar(R.string.ScrollOfRemoveCurse_Proced);
	private static final String TXT_NOT_PROCCED	= Game.getVar(R.string.ScrollOfRemoveCurse_NoProced);
	
	@Override
	protected void doRead() {
		
		new Flare( 6, 32 ).show( getCurUser().getSprite(), 2f ) ;
		Sample.INSTANCE.play( Assets.SND_READ );
		Invisibility.dispel(getCurUser());
		
		boolean procced = uncurse( getCurUser(), getCurUser().belongings.backpack.items.toArray(new Item[getCurUser().belongings.backpack.items.size()]));
		procced = uncurse( getCurUser(), 
			getCurUser().belongings.weapon, 
			getCurUser().belongings.barding,
			getCurUser().belongings.mane,
			getCurUser().belongings.tail ) || procced;
		
		Weakness.detach( getCurUser(), Weakness.class );
		
		if (procced) {
			GLog.p( TXT_PROCCED );			
		} else {		
			GLog.i( TXT_NOT_PROCCED );		
		}
		
		setKnown();
		
		getCurUser().spendAndNext( TIME_TO_READ );
	}

	public static boolean uncurse( Hero hero, Item... items ) {
		
		boolean procced = false;
		for (int i=0; i < items.length; i++) {
			Item item = items[i];
			if (item != null && item.cursed) {
				item.cursed = false;
				procced = true;
			}
		}
		
		if (procced) {
			hero.getSprite().emitter().start( ShadowParticle.UP, 0.05f, 10 );
		}
		
		return procced;
	}
	
	@Override
	public int price() {
		return isKnown() ? 30 * quantity() : super.price();
	}
}
