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

import com.annatala.pixelponies.Badges;
import com.annatala.pixelponies.Dungeon;
import com.annatala.pixelponies.Statistics;
import com.annatala.pixelponies.actors.Char;
import com.annatala.pixelponies.actors.blobs.ToxicGas;
import com.annatala.pixelponies.actors.buffs.Burning;
import com.annatala.pixelponies.actors.buffs.Frost;
import com.annatala.pixelponies.actors.buffs.Paralysis;
import com.annatala.pixelponies.actors.buffs.Roots;
import com.annatala.pixelponies.items.food.MysteriousHay;
import com.annatala.pixelponies.sprites.PiranhaSprite;
import com.annatala.utils.Random;

public class Piranha extends Mob {
	
	public Piranha() {
		spriteClass = PiranhaSprite.class;
		
		hp(ht(10 + Dungeon.depth * 5));
		defenseSkill = 10 + Dungeon.depth * 2;

		baseSpeed = 2f;
		
		EXP = 0;
		
		IMMUNITIES.add( Burning.class );
		IMMUNITIES.add( Paralysis.class );
		IMMUNITIES.add( ToxicGas.class );
		IMMUNITIES.add( Roots.class );
		IMMUNITIES.add( Frost.class );
	}
	
	@Override
	protected boolean act() {
		if (!Dungeon.level.water[getPos()]) {
			die( null );
			return true;
		} else {
			return super.act();
		}
	}
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange( Dungeon.depth, 4 + Dungeon.depth * 2 );
	}
	
	@Override
	public int attackSkill( Char target ) {
		return 20 + Dungeon.depth * 2;
	}
	
	@Override
	public int dr() {
		return Dungeon.depth;
	}
	
	@Override
	public void die( Object cause ) {
		Dungeon.level.drop( new MysteriousHay(), getPos() ).sprite.drop();
		super.die( cause );
		
		Statistics.piranhasKilled++;
		Badges.validatePiranhasKilled();
	}
	
	@Override
	public boolean reset() {
		return true;
	}
	
	@Override
	protected boolean getCloser( int target ) {
		
		if (rooted) {
			return false;
		}
		
		int step = Dungeon.findPath( this, getPos(), target, Dungeon.level.water, null);
		if (step != -1) {
			move( step );
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	protected boolean getFurther( int target ) {
		int step = Dungeon.flee( this, getPos(), target, Dungeon.level.water, null);
		if (step != -1) {
			move( step );
			return true;
		} else {
			return false;
		}
	}
}
