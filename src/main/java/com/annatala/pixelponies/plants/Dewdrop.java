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

import com.annatala.noosa.Game;
import com.annatala.noosa.audio.Sample;
import com.annatala.pixelponies.Assets;
import com.annatala.pixelponies.Dungeon;
import com.annatala.pixelponies.android.R;
import com.annatala.pixelponies.actors.hero.Hero;
import com.annatala.pixelponies.actors.hero.HeroClass;
import com.annatala.pixelponies.effects.Speck;
import com.annatala.pixelponies.items.utility.DewVial;
import com.annatala.pixelponies.items.Item;
import com.annatala.pixelponies.sprites.CharSprite;
import com.annatala.pixelponies.sprites.ItemSpriteSheet;

public class Dewdrop extends Item {

	private static final String TXT_VALUE	= "%+dHP";
	
	{
		name = Game.getVar(R.string.Dewdrop_Name);
		image = ItemSpriteSheet.DEWDROP;
		
		stackable = true;
	}
	
	@Override
	public boolean doPickUp( Hero hero ) {
		
		DewVial vial = hero.belongings.getItem( DewVial.class );
		
		if (hero.hp() < hero.ht() || vial == null || vial.isFull()) {
			
			int value = 1 + (Dungeon.depth - 1) / 5;
			if (hero.heroClass == HeroClass.ZEBRA) {
				value++;
			}
			
			int effect = Math.min( hero.ht() - hero.hp(), value * quantity() );
			if (effect > 0) {
				hero.hp(hero.hp() + effect);
				hero.getSprite().emitter().burst( Speck.factory( Speck.HEALING ), 1 );
				hero.getSprite().showStatus( CharSprite.POSITIVE, TXT_VALUE, effect );
			}
		} else {
			vial.collectDew( this );
		}
		
		Sample.INSTANCE.play( Assets.SND_DEWDROP );
		hero.spendAndNext( TIME_TO_PICK_UP );
		
		return true;
	}
	
	@Override
	public Item burn(int cell){
		return null;
	}
}
