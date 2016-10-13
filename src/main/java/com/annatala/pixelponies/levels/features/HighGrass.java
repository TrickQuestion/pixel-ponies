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
package com.annatala.pixelponies.levels.features;

import com.annatala.noosa.particles.Emitter;
import com.annatala.pixelponies.Challenges;
import com.annatala.pixelponies.Dungeon;
import com.annatala.pixelponies.actors.Char;
import com.annatala.pixelponies.actors.buffs.Harvest;
import com.annatala.pixelponies.actors.buffs.Hunger;
import com.annatala.pixelponies.actors.hero.Hero;
import com.annatala.pixelponies.effects.CellEmitter;
import com.annatala.pixelponies.effects.particles.LeafParticle;
import com.annatala.pixelponies.plants.Dewdrop;
import com.annatala.pixelponies.items.Generator;
import com.annatala.pixelponies.items.rings.RingOfHerbalism.Herbalism;
import com.annatala.pixelponies.levels.Level;
import com.annatala.pixelponies.levels.Terrain;
import com.annatala.pixelponies.scenes.GameScene;
import com.annatala.pixelponies.sprites.ItemSprite;
import com.annatala.utils.Random;

public class HighGrass {

	public static void trample( Level level, int pos, Char ch ) {
		
		Dungeon.level.set( pos, Terrain.GRASS );
		GameScene.updateMap( pos );
		
		if (!Dungeon.isChallenged( Challenges.NO_HERBALISM )) {
			int herbalismLevel = 0;
			if (ch != null) {
				Herbalism herbalism = ch.buff( Herbalism.class );
				if (herbalism != null) {
					herbalismLevel = herbalism.level;
				}
			}
			
			// Seed
			if (herbalismLevel >= 0 && Random.Int( 18 ) <= Random.Int( herbalismLevel + 1 )) {
				ItemSprite is = level.drop( Generator.random( Generator.Category.SEED ), pos ).sprite;
				if(is != null) {
					is.drop();
				}
			}
			
			// Dew
			if (herbalismLevel >= 0 && Random.Int( 6 ) <= Random.Int( herbalismLevel + 1 )) {
				ItemSprite is = level.drop( new Dewdrop(), pos ).sprite;
				if(is != null) {
					is.drop();
				}
			}
		}
		
		int leaves = 4;


		// TODO: Cannibalize for Zebra, maybe.
		// Barkskin
//		if (ch instanceof Hero && ((Hero)ch).subClass == HeroSubClass.WARDEN) {
//			Buff.affect( ch, Barkskin.class ).level( ch.ht() / 3 );
//			leaves = 8;
//		}
//
//		if (ch instanceof Hero && ((Hero) ch).subClass == HeroSubClass.DERP) {
//			Buff.prolong( ch, Invisibility.class, 5);
//			leaves = 2;
//		}

		if (ch instanceof Hero && ch.buffs().contains(Harvest.class)) {
			ch.buff( Hunger.class ).satisfy((Hunger.STARVING - Hunger.HUNGRY) / 4.0F);
		}
		
		Emitter emitter = CellEmitter.get(pos);
		if (emitter != null) {
			emitter.burst(LeafParticle.LEVEL_SPECIFIC, leaves);
		}
		Dungeon.observe();
	}
}
