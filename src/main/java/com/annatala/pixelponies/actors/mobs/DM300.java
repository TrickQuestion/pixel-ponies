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
package com.annatala.pixelponies.actors.mobs;

import com.annatala.pixelponies.android.R;
import com.annatala.noosa.Camera;
import com.annatala.noosa.Game;
import com.annatala.noosa.audio.Sample;
import com.annatala.pixelponies.Assets;
import com.annatala.pixelponies.Badges;
import com.annatala.pixelponies.Dungeon;
import com.annatala.pixelponies.actors.Actor;
import com.annatala.pixelponies.actors.Char;
import com.annatala.pixelponies.actors.blobs.Blob;
import com.annatala.pixelponies.actors.blobs.ToxicGas;
import com.annatala.pixelponies.actors.buffs.Buff;
import com.annatala.pixelponies.actors.buffs.Paralysis;
import com.annatala.pixelponies.effects.CellEmitter;
import com.annatala.pixelponies.effects.Speck;
import com.annatala.pixelponies.effects.particles.ElmoParticle;
import com.annatala.pixelponies.items.keys.SkeletonKey;
import com.annatala.pixelponies.items.rings.RingOfThorns;
import com.annatala.pixelponies.levels.Level;
import com.annatala.pixelponies.levels.Terrain;
import com.annatala.pixelponies.scenes.GameScene;
import com.annatala.pixelponies.sprites.DM300Sprite;
import com.annatala.utils.GLog;
import com.annatala.utils.Random;

public class DM300 extends Boss {
	
	public DM300() {
		spriteClass = DM300Sprite.class;
		
		hp(ht(200));
		EXP = 30;
		defenseSkill = 18;
		
		loot = new RingOfThorns().random();

		lootChance = 0.333f;
		
		IMMUNITIES.add( ToxicGas.class );
	}
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 18, 24 );
	}
	
	@Override
	public int attackSkill( Char target ) {
		return 28;
	}
	
	@Override
	public int dr() {
		return 10;
	}
	
	@Override
	public boolean act() {

		GameScene.add( Blob.seed( getPos(), 30, ToxicGas.class ) );
		
		return super.act();
	}
	
	@Override
	public void move( int step ) {
		super.move( step );

		// This is deterministic, so I don't want luck involved.
		if (Dungeon.level.map[step] == Terrain.INACTIVE_TRAP && hp() < ht()) {
			
			hp(hp() + Random.Int( 1, ht() - hp() ));
			getSprite().emitter().burst( ElmoParticle.FACTORY, 5 );
			
			if (Dungeon.visible[step] && Dungeon.hero.isAlive()) {
				GLog.n(Game.getVar(R.string.DM300_Info1));
			}
		}
		
		int cell = step + Level.NEIGHBOURS8[Random.Int( Level.NEIGHBOURS8.length )];
		
		if (Dungeon.visible[cell]) {
			CellEmitter.get( cell ).start( Speck.factory( Speck.ROCK ), 0.07f, 10 );
			Camera.main.shake( 3, 0.7f );
			Sample.INSTANCE.play( Assets.SND_ROCKS );

			if (Dungeon.level.water[cell]) {
				GameScene.ripple( cell );
			} else if (Dungeon.level.map[cell] == Terrain.EMPTY) {
				Dungeon.level.set( cell, Terrain.EMPTY_DECO );
				GameScene.updateMap( cell );
			}
		}

		Char ch = Actor.findChar( cell );
		if (ch != null && ch != this) {
			Buff.prolong( ch, Paralysis.class, 2 );
		}
	}
	
	@Override
	public void die( Object cause ) {
		
		super.die( cause );
		
		GameScene.bossSlain();
		Dungeon.level.drop( new SkeletonKey(), getPos() ).sprite.drop();
		
		Badges.validateBossSlain(Badges.Badge.BOSS_SLAIN_3);
		
		yell(Game.getVar(R.string.DM300_Info2));
	}
	
	@Override
	public void notice() {
		super.notice();
		yell(Game.getVar(R.string.DM300_Info3));
	}
}
