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

import com.annatala.pixelponies.android.R;
import com.annatala.noosa.Game;
import com.annatala.noosa.audio.Sample;
import com.annatala.pixelponies.Assets;
import com.annatala.pixelponies.Badges;
import com.annatala.pixelponies.Statistics;
import com.annatala.pixelponies.actors.buffs.Hunger;
import com.annatala.pixelponies.actors.hero.Hero;
import com.annatala.pixelponies.effects.Speck;
import com.annatala.pixelponies.effects.SpellSprite;
import com.annatala.pixelponies.items.Item;
import com.annatala.pixelponies.items.scrolls.ScrollOfRecharging;
import com.annatala.utils.GLog;

import java.util.ArrayList;

abstract public class Food extends Item {

	public static final float TIME_TO_EAT	= 3f;
	
	public static final String AC_EAT = Game.getVar(R.string.Food_ACEat);
	
	public float energy   = 0;
	public String message = Game.getVar(R.string.Food_Message);
	
	{
		stackable = true;
	}
	
	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		actions.add( AC_EAT );
		return actions;
	}
	
	@Override
	public void execute( Hero hero, String action ) {
		if (action.equals( AC_EAT )) {
			
			detach( hero.belongings.backpack );
			
			hero.buff( Hunger.class ).satisfy(energy);
			GLog.i( message );
			
			switch (hero.heroClass) {
			case EARTH_PONY:
				if (hero.hp() < hero.ht()) {
					hero.hp(Math.min( hero.hp() + 5, hero.ht() ));
					hero.getSprite().emitter().burst( Speck.factory( Speck.HEALING ), 1 );
				}
				break;
			case UNICORN:
				hero.belongings.charge( false );
				ScrollOfRecharging.charge( hero );
				break;
			case PEGASUS:
			case ZEBRA:
			case NIGHTWING:
				break;
			}
			
			hero.getSprite().operate( hero.getPos() );
			hero.busy();
			SpellSprite.show( hero, SpellSprite.FOOD );
			Sample.INSTANCE.play( Assets.SND_EAT );
			
			hero.spend( TIME_TO_EAT );
			
			Statistics.foodEaten++;
			Badges.validateFoodEaten();
			
		} else {
		
			super.execute( hero, action );
			
		}
	}
	
	@Override
	public boolean isUpgradable() {
		return false;
	}
	
	@Override
	public boolean isIdentified() {
		return true;
	}
}
