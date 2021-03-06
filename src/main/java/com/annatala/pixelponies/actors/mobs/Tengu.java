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

import com.annatala.noosa.Game;
import com.annatala.noosa.audio.Sample;
import com.annatala.pixelponies.Assets;
import com.annatala.pixelponies.Badges;
import com.annatala.pixelponies.android.R;
import com.annatala.pixelponies.Badges.Badge;
import com.annatala.pixelponies.Dungeon;
import com.annatala.pixelponies.actors.Actor;
import com.annatala.pixelponies.actors.Char;
import com.annatala.pixelponies.actors.blobs.ToxicGas;
import com.annatala.pixelponies.actors.buffs.Poison;
import com.annatala.pixelponies.actors.hero.HeroSubClass;
import com.annatala.pixelponies.effects.CellEmitter;
import com.annatala.pixelponies.effects.Speck;
import com.annatala.pixelponies.items.utility.TomeOfMastery;
import com.annatala.pixelponies.items.keys.SkeletonKey;
import com.annatala.pixelponies.items.scrolls.ScrollOfMagicMapping;
import com.annatala.pixelponies.levels.Terrain;
import com.annatala.pixelponies.mechanics.Ballistica;
import com.annatala.pixelponies.scenes.GameScene;
import com.annatala.pixelponies.sprites.TenguSprite;
import com.annatala.utils.Utils;
import com.annatala.utils.Random;

public class Tengu extends Boss {

	private static final int JUMP_DELAY = 5;
	
	public Tengu() {
		spriteClass = TenguSprite.class;
		
		hp(ht(120));
		EXP = 20;
		defenseSkill = 20;
		
		RESISTANCES.add( ToxicGas.class );
		RESISTANCES.add( Poison.class );
	}
	
	private int timeToJump = JUMP_DELAY;
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 8, 15 );
	}
	
	@Override
	public int attackSkill( Char target ) {
		return 20;
	}
	
	@Override
	public int dr() {
		return 5;
	}
	
	@Override
	public void die( Object cause ) {

		// Reset so Tengu only drops tome if you didn't have it at start.
		// It should be impossible to lose the tome from your inventory now -- can't drop, throw, be stolen.
		if (Dungeon.hero.subClass == HeroSubClass.NONE && !Dungeon.hero.belongings.backpack.contains(new TomeOfMastery()) ) {
			Dungeon.level.drop(new TomeOfMastery(), getPos()).sprite.drop();
		}

		GameScene.bossSlain();
		Dungeon.level.drop( new SkeletonKey(), getPos() ).sprite.drop();
		super.die(cause);
		
		Badges.validateBossSlain(Badge.BOSS_SLAIN_2);

		say(Game.getVar(R.string.Tengu_Info1));
	}
	
	@Override
	protected boolean getCloser( int target ) {
		if (Dungeon.level.fieldOfView[target]) {
			jump();
			return true;
		} else {
			return super.getCloser( target );
		}
	}
	
	@Override
	protected boolean canAttack( Char enemy ) {
		return Ballistica.cast( getPos(), enemy.getPos(), false, true ) == enemy.getPos();
	}
	
	@Override
	protected boolean doAttack( Char enemy ) {
		timeToJump--;
		if (timeToJump <= 0 && Dungeon.level.adjacent( getPos(), enemy.getPos() )) {
			jump();
			return true;
		} else {
			return super.doAttack( enemy );
		}
	}
	
	private void jump() {
		timeToJump = JUMP_DELAY;
		
		for (int i=0; i < 4; i++) {
			int trapPos;
			do {
				trapPos = Random.Int( Dungeon.level.getLength() );
			} while (!Dungeon.level.fieldOfView[trapPos] || !Dungeon.level.passable[trapPos]);
			
			if (Dungeon.level.map[trapPos] == Terrain.INACTIVE_TRAP) {
				Dungeon.level.set( trapPos, Terrain.POISON_TRAP );
				GameScene.updateMap( trapPos );
				ScrollOfMagicMapping.discover( trapPos );
			}
		}
		
		int newPos;
		do {
			newPos = Random.Int( Dungeon.level.getLength() );
		} while (
			!Dungeon.level.fieldOfView[newPos] || 
			!Dungeon.level.passable[newPos] || 
			Dungeon.level.adjacent( newPos, getEnemy().getPos() ) ||
			Actor.findChar( newPos ) != null);
		
		getSprite().move( getPos(), newPos );
		move( newPos );
		
		if (Dungeon.visible[newPos]) {
			CellEmitter.get( newPos ).burst( Speck.factory( Speck.WOOL ), 6 );
			Sample.INSTANCE.play( Assets.SND_PUFF );
		}
		
		spend( 1 / speed() );
	}
	
	@Override
	public void notice() {
		super.notice();
		String tenguYell = Game.getVar(R.string.Tengu_Info2);
		yell(Utils.format(tenguYell, Dungeon.hero.heroClass.toString()));
	}	
}
