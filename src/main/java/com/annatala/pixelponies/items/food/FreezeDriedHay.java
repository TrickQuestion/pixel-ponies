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
import com.annatala.pixelponies.android.R;
import com.annatala.pixelponies.actors.buffs.Barkskin;
import com.annatala.pixelponies.actors.buffs.Bleeding;
import com.annatala.pixelponies.actors.buffs.Buff;
import com.annatala.pixelponies.actors.buffs.Cripple;
import com.annatala.pixelponies.actors.buffs.Hunger;
import com.annatala.pixelponies.actors.buffs.Invisibility;
import com.annatala.pixelponies.actors.buffs.Poison;
import com.annatala.pixelponies.actors.buffs.Weakness;
import com.annatala.pixelponies.actors.hero.Hero;
import com.annatala.pixelponies.effects.Speck;
import com.annatala.pixelponies.items.Item;
import com.annatala.pixelponies.sprites.ItemSpriteSheet;
import com.annatala.utils.GLog;
import com.annatala.utils.Random;

public class FreezeDriedHay extends Food {

	{
		image  = ItemSpriteSheet.FREEZE_DRIED_HAY;
		energy = Hunger.STARVING - Hunger.HUNGRY;
	}
	
	@Override
	public void execute( Hero hero, String action ) {
		
		super.execute( hero, action );
		
		if (action.equals( AC_EAT )) {
			
			switch (Random.Int( 5 )) {
			case 0:
				GLog.i(Game.getVar(R.string.FreezeDriedHay_Info1));
				Buff.affect( hero, Invisibility.class, Invisibility.DURATION );
				break;
			case 1:
				GLog.i(Game.getVar(R.string.FreezeDriedHay_Info2));
				Buff.affect( hero, Barkskin.class ).level( hero.ht() / 4 );
				break;
			case 2:
				GLog.i(Game.getVar(R.string.FreezeDriedHay_Info3));
				Buff.detach( hero, Poison.class );
				Buff.detach( hero, Cripple.class );
				Buff.detach( hero, Weakness.class );
				Buff.detach( hero, Bleeding.class );
				break;
			case 3:
				GLog.i(Game.getVar(R.string.FreezeDriedHay_Info4));
				if (hero.hp() < hero.ht()) {
					hero.hp(Math.min( hero.hp() + hero.ht() / 4, hero.ht() ));
					hero.getSprite().emitter().burst( Speck.factory( Speck.HEALING ), 1 );
				}
				break;
			}
		}
	}
	
	public int price() {
		return 10 * quantity();
	}
	
	@Override
	public Item burn(int cell){
		return morphTo(MysteriousHay.class);
	}
}
