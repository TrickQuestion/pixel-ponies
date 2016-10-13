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
package com.annatala.pixelponies.actors.buffs;

import android.annotation.SuppressLint;

import com.annatala.noosa.Game;
import com.annatala.pixelponies.Badges;
import com.annatala.pixelponies.Dungeon;
import com.annatala.pixelponies.android.R;
import com.annatala.pixelponies.ResultDescriptions;
import com.annatala.pixelponies.actors.Char;
import com.annatala.pixelponies.actors.blobs.Blob;
import com.annatala.pixelponies.actors.blobs.Fire;
import com.annatala.pixelponies.actors.hero.Hero;
import com.annatala.pixelponies.items.Heap;
import com.annatala.pixelponies.items.Item;
import com.annatala.pixelponies.items.rings.RingOfElements.Resistance;
import com.annatala.pixelponies.scenes.GameScene;
import com.annatala.pixelponies.ui.BuffIndicator;
import com.annatala.utils.GLog;
import com.annatala.utils.Utils;
import com.annatala.utils.Bundle;
import com.annatala.utils.Random;

public class Burning extends Buff implements Hero.Doom {

	private static final String TXT_BURNS_UP		= Game.getVar(R.string.Burning_Burns);
	private static final String TXT_BURNED_TO_DEATH	= Game.getVar(R.string.Burning_Death);
	
	private static final float DURATION = 8f;
	
	private float left;
	
	@SuppressLint("RtlHardcoded")
	private static final String LEFT	= "left";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( LEFT, left );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle(bundle);
		left = bundle.getFloat( LEFT );
	}
	
	class burnItem implements itemAction{
		public Item act(Item srcItem){
			return srcItem.burn(target.getPos());
		}
		public void carrierFx(){
			Heap.burnFX( target.getPos() );
		}
		@Override
		public String actionText(Item srcItem) {
			return Utils.format(TXT_BURNS_UP, srcItem.toString());
		}
	}
		
	@Override
	public boolean act() {
		
		if (target.isAlive()) {

			int damage = Random.Int( 1, 5 );

			if (target instanceof Hero) {
				Buff.prolong( target, Light.class, TICK * 1.01f );

				// Give a small damage reduction if you're a lucky hero.
				if (Random.luckBonus() && damage > 1) damage--;
			}

			target.damage( damage, this );
			
			applyToCarriedItems(new burnItem());
		} else {
			detach();
		}
		
		if (Dungeon.level.flammable[target.getPos()]) {
			GameScene.add( Blob.seed( target.getPos(), 4, Fire.class ) );
		}
		
		spend( TICK );
		left -= TICK;
		
		if (left <= 0 ||
			Random.Float() > (2 + (float)target.hp() / target.ht()) / 3 ||
			(Dungeon.level.water[target.getPos()] && !target.flying)) {
			
			detach();
		}
		
		return true;
	}
	
	public void reignite( Char ch ) {
		left = duration( ch );
	}
	
	@Override
	public int icon() {
		return BuffIndicator.FIRE;
	}
	
	@Override
	public String toString() {
		return Game.getVar(R.string.Burning_Info);
	}

	public static float duration( Char ch ) {
		Resistance r = ch.buff( Resistance.class );
		return r != null ? r.durationFactor() * DURATION : DURATION;
	}

	@Override
	public void onDeath() {
		
		Badges.validateDeathFromFire();
		
		Dungeon.fail( Utils.format( ResultDescriptions.BURNING, Dungeon.depth ) );
		GLog.n( TXT_BURNED_TO_DEATH );
	}
}
