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
package com.annatala.pixelponies.items.potions;

import com.annatala.noosa.Game;
import com.annatala.noosa.audio.Sample;
import com.annatala.pixelponies.Assets;
import com.annatala.pixelponies.Dungeon;
import com.annatala.pixelponies.android.R;
import com.annatala.pixelponies.actors.blobs.Blob;
import com.annatala.pixelponies.actors.blobs.ParalyticGas;
import com.annatala.pixelponies.actors.blobs.ToxicGas;
import com.annatala.pixelponies.actors.buffs.Buff;
import com.annatala.pixelponies.actors.buffs.GasesImmunity;
import com.annatala.pixelponies.actors.hero.Hero;
import com.annatala.pixelponies.effects.CellEmitter;
import com.annatala.pixelponies.effects.Speck;
import com.annatala.pixelponies.items.food.RottenFood;
import com.annatala.utils.BArray;
import com.annatala.utils.GLog;
import com.annatala.utils.PathFinder;

public class PotionOfPurification extends Potion {

	private static final String TXT_FRESHNESS = Game.getVar(R.string.PotionOfPurity_Freshness);
	private static final String TXT_NO_SMELL  = Game.getVar(R.string.PotionOfPurity_NoSmell);
	
	private static final int DISTANCE	= 2;
	
	@Override
	public void shatter( int cell ) {
		
		PathFinder.buildDistanceMap( cell, BArray.not( Dungeon.level.losBlocking, null ), DISTANCE );
		
		boolean procd = false;
		
		Blob[] blobs = {
			Dungeon.level.blobs.get( ToxicGas.class ), 
			Dungeon.level.blobs.get( ParalyticGas.class )
		};
		
		for (int j=0; j < blobs.length; j++) {
			
			Blob blob = blobs[j];
			if (blob == null) {
				continue;
			}
			
			for (int i=0; i < Dungeon.level.getLength(); i++) {
				if (PathFinder.distance[i] < Integer.MAX_VALUE) {
					
					int value = blob.cur[i]; 
					if (value > 0) {
						
						blob.cur[i] = 0;
						blob.volume -= value;
						procd = true;
						
						CellEmitter.get( i ).burst( Speck.factory( Speck.DISCOVER ), 1 );
					}

				}
			}
		}
		
		boolean heroAffected = PathFinder.distance[Dungeon.hero.getPos()] < Integer.MAX_VALUE;
		
		if (procd) {
			
			splash( cell );
			Sample.INSTANCE.play( Assets.SND_SHATTER );
			
			setKnown();
			
			if (heroAffected) {
				GLog.p( TXT_FRESHNESS );
			}
			
		} else {
			
			super.shatter( cell );
			
			if (heroAffected) {
				GLog.i( TXT_FRESHNESS );
				setKnown();
			}
			
		}
	}
	
	@Override
	protected void apply( Hero hero ) {
		GLog.w( TXT_NO_SMELL );
		Buff.prolong( hero, GasesImmunity.class, GasesImmunity.DURATION );
		setKnown();
	}
	
	@Override
	public String desc() {
		return Game.getVar(R.string.PotionOfPurity_Info);
	}
	
	@Override
	public int price() {
		return isKnown() ? 50 * quantity() : super.price();
	}
	
	@Override
	protected void moistenRottenFood(RottenFood rfood) {
		
		detachMoistenItems(rfood,1);
		moistenEffective();
		GLog.i(TXT_ROTTEN_FOOD_MOISTEN, rfood.name());
		
		getCurUser().collect(rfood.purify());
	}
}
