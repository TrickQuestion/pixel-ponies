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
import com.annatala.pixelponies.actors.buffs.Buff;
import com.annatala.pixelponies.actors.buffs.Burning;
import com.annatala.pixelponies.actors.buffs.Hunger;
import com.annatala.pixelponies.actors.buffs.Paralysis;
import com.annatala.pixelponies.actors.buffs.Poison;
import com.annatala.pixelponies.actors.buffs.Roots;
import com.annatala.pixelponies.actors.buffs.Slow;
import com.annatala.pixelponies.actors.hero.Hero;
import com.annatala.pixelponies.items.Item;
import com.annatala.pixelponies.sprites.ItemSpriteSheet;
import com.annatala.utils.GLog;
import com.annatala.utils.Random;

public class MysteriousHay extends Food {

	{
		image   = ItemSpriteSheet.MYSTERIOUS_HAY;
		energy  = Hunger.STARVING - Hunger.HUNGRY;
		message = Game.getVar(R.string.MysteriousHay_Message);
	}
	
	@Override
	public void execute( Hero hero, String action ) {
		
		super.execute( hero, action );
		
		if (action.equals( AC_EAT )) {

			// Small luck chance to not suffer ill effects.
			if (Random.luckBonus() && Random.luckBonus()) return;

			switch (Random.Int( 5 )) {
			case 0:
				GLog.w(Game.getVar(R.string.MysteriousHay_Info1));
				Buff.affect( hero, Burning.class ).reignite( hero );
				break;
			case 1:
				GLog.w(Game.getVar(R.string.MysteriousHay_Info2));
				Buff.prolong( hero, Roots.class, Paralysis.duration( hero ) );
				break;
			case 2:
				GLog.w(Game.getVar(R.string.MysteriousHay_Info3));
				Buff.affect( hero, Poison.class ).set( Poison.durationFactor( hero ) * hero.ht() / 5 );
				break;
			case 3:
				GLog.w(Game.getVar(R.string.MysteriousHay_Info4));
				Buff.prolong( hero, Slow.class, Slow.duration( hero ) );
				break;
			}
		}
	}
	
	public int price() {
		return 5 * quantity();
	}

	@Override
	public Item burn(int cell){
		return morphTo(HayFries.class);
	}
	
	@Override
	public Item freeze(int cell){
		return morphTo(FreezeDriedHay.class);
	}
	
	@Override
	public Item poison(int cell){
		return morphTo(RottenHay.class);
	}
}
