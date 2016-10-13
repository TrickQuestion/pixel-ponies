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
import com.annatala.pixelponies.Dungeon;
import com.annatala.pixelponies.ResultDescriptions;
import com.annatala.pixelponies.actors.Char;
import com.annatala.pixelponies.effects.particles.SparkParticle;
import com.annatala.pixelponies.items.Generator;
import com.annatala.pixelponies.levels.traps.LightningTrap;
import com.annatala.pixelponies.mechanics.Ballistica;
import com.annatala.pixelponies.sprites.CharSprite;
import com.annatala.pixelponies.sprites.ShamanSprite;
import com.annatala.utils.GLog;
import com.annatala.utils.Utils;
import com.annatala.utils.Random;

public class Shaman extends Mob {

	private static final float TIME_TO_ZAP	= 2f;
	
	private static final String TXT_LIGHTNING_KILLED = Game.getVar(R.string.Shaman_Killed);
	
	private int fleeState = 0;
	
	public Shaman() {
		spriteClass = ShamanSprite.class;
		
		hp(ht(18));
		defenseSkill = 8;
		
		EXP = 6;
		maxLvl = 14;
		
		loot = Generator.Category.SCROLL;
		lootChance = 0.33f;
		
		RESISTANCES.add( LightningTrap.Electricity.class );
	}
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 2, 6 );
	}
	
	@Override
	public int attackSkill( Char target ) {
		return 11;
	}
	
	@Override
	public int dr() {
		return 4;
	}
	
	@Override
	protected boolean canAttack( Char enemy ) {
		return Ballistica.cast( getPos(), enemy.getPos(), false, true ) == enemy.getPos();
	}
	
	@Override
	public int defenseProc(Char enemy, int damage) {
		
		if( hp() > 2*ht() / 3 && fleeState < 1) {
			state = FLEEING;
			fleeState++;
			return damage/2;
		}
		
		if( hp() > ht() / 3 && fleeState < 2 ) {
			state = FLEEING;
			fleeState++;
			return damage/2;
		}
		
		return damage;
	}
	
	@Override
	protected boolean getFurther(int target) {
		
		if(Dungeon.level.distance(getPos(), target) >2) {
			state = HUNTING;
		}
		
		return super.getFurther(target);
	}
	
	@Override
	protected boolean doAttack( Char enemy ) {

		if (Dungeon.level.distance( getPos(), enemy.getPos() ) <= 1) {
			
			return super.doAttack( enemy );
			
		} else {
			
			boolean visible = Dungeon.level.fieldOfView[getPos()] || Dungeon.level.fieldOfView[enemy.getPos()]; 
			if (visible) {
				getSprite().zap( enemy.getPos() );
			}
			
			spend( TIME_TO_ZAP );
			
			if (hit( this, enemy, true )) {
				int dmg = Random.Int( 2, 12 );
				if (Dungeon.level.water[enemy.getPos()] && !enemy.flying) {
					dmg *= 1.5f;
				}
				enemy.damage( dmg, LightningTrap.LIGHTNING );
				
				enemy.getSprite().centerEmitter().burst( SparkParticle.FACTORY, 3 );
				enemy.getSprite().flash();
				
				if (enemy == Dungeon.hero) {
					
					Camera.main.shake( 2, 0.3f );
					
					if (!enemy.isAlive()) {
						Dungeon.fail( Utils.format( ResultDescriptions.MOB, 
							Utils.indefinite( getName() ), Dungeon.depth ) );
						GLog.n( TXT_LIGHTNING_KILLED, getName() );
					}
				}
			} else {
				enemy.getSprite().showStatus( CharSprite.NEUTRAL,  enemy.defenseVerb() );
			}
			
			return !visible;
		}
	}
}
