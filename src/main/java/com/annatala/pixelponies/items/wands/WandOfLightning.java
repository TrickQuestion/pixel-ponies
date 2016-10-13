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
package com.annatala.pixelponies.items.wands;

import com.annatala.pixelponies.android.R;
import com.annatala.noosa.Camera;
import com.annatala.noosa.Game;
import com.annatala.pixelponies.Dungeon;
import com.annatala.pixelponies.ResultDescriptions;
import com.annatala.pixelponies.actors.Actor;
import com.annatala.pixelponies.actors.Char;
import com.annatala.pixelponies.effects.CellEmitter;
import com.annatala.pixelponies.effects.Lightning;
import com.annatala.pixelponies.effects.particles.SparkParticle;
import com.annatala.pixelponies.levels.Level;
import com.annatala.pixelponies.levels.traps.LightningTrap;
import com.annatala.utils.GLog;
import com.annatala.utils.Utils;
import com.annatala.utils.Callback;
import com.annatala.utils.Random;

import java.util.ArrayList;
import java.util.HashSet;

public class WandOfLightning extends SimpleWand  {
	
	private ArrayList<Char> affected = new ArrayList<>();
	
	private int[] points = new int[20];
	private int nPoints;
	
	@Override
	protected void onZap( int cell ) {

		if (getCurUser()!=null && !getCurUser().isAlive()) {
			Dungeon.fail( Utils.format( ResultDescriptions.WAND, name, Dungeon.depth ) );
			GLog.n(Game.getVar(R.string.WandOfLightning_Info1));
		}
	}
	
	private void hit( Char ch, int damage ) {
		
		if (damage < 1) {
			return;
		}
		
		if (ch == Dungeon.hero) {
			Camera.main.shake( 2, 0.3f );
		}
		
		affected.add( ch );
		ch.damage( Dungeon.level.water[ch.getPos()] && !ch.flying ? damage * 2 : damage, LightningTrap.LIGHTNING  );
		
		ch.getSprite().centerEmitter().burst( SparkParticle.FACTORY, 3 );
		ch.getSprite().flash();
		
		points[nPoints++] = ch.getPos();
		
		HashSet<Char> ns = new HashSet<>();
		for (int i=0; i < Level.NEIGHBOURS8.length; i++) {
			Char n = Actor.findChar( ch.getPos() + Level.NEIGHBOURS8[i] );
			if (n != null && !affected.contains( n )) {
				ns.add( n );
			}
		}
		
		if (!ns.isEmpty()) {
			hit( Random.element( ns ), Random.Int( damage / 2, damage ) );
		}
	}
	
	@Override
	protected void fx( int cell, Callback callback ) {
		
		nPoints = 0;
		points[nPoints++] = wandUser.getPos();
		
		Char ch = Actor.findChar( cell );
		if (ch != null) {
			
			affected.clear();
			int lvl = effectiveLevel();
			hit( ch, Random.Int( 5 + lvl / 2, 10 + lvl ) );

		} else {
			
			points[nPoints++] = cell;
			CellEmitter.center( cell ).burst( SparkParticle.FACTORY, 3 );
			
		}
		wandUser.getSprite().getParent().add( new Lightning( points, nPoints, callback ) );
	}
	
	@Override
	public String desc() {
		return Game.getVar(R.string.WandOfLightning_Info);
	}
}
