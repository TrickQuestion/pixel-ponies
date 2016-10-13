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
package com.annatala.pixelponies.actors.mobs.npcs;

import com.annatala.pixelponies.android.R;
import com.annatala.noosa.Game;
import com.annatala.noosa.audio.Sample;
import com.annatala.pixelponies.Assets;
import com.annatala.pixelponies.Challenges;
import com.annatala.pixelponies.Dungeon;
import com.annatala.pixelponies.Journal;
import com.annatala.pixelponies.actors.Actor;
import com.annatala.pixelponies.actors.Char;
import com.annatala.pixelponies.actors.blobs.Blob;
import com.annatala.pixelponies.actors.blobs.ParalyticGas;
import com.annatala.pixelponies.actors.buffs.Buff;
import com.annatala.pixelponies.actors.buffs.Paralysis;
import com.annatala.pixelponies.actors.buffs.Roots;
import com.annatala.pixelponies.actors.hero.Hero;
import com.annatala.pixelponies.actors.mobs.Mob;
import com.annatala.pixelponies.effects.CellEmitter;
import com.annatala.pixelponies.effects.Speck;
import com.annatala.pixelponies.items.Generator;
import com.annatala.pixelponies.items.Item;
import com.annatala.pixelponies.items.barding.Barding;
import com.annatala.pixelponies.items.barding.ClothBarding;
import com.annatala.pixelponies.items.quest.DriedRose;
import com.annatala.pixelponies.items.quest.RatSkull;
import com.annatala.pixelponies.items.weapon.Weapon;
import com.annatala.pixelponies.items.weapon.missiles.MissileWeapon;
import com.annatala.pixelponies.levels.SewerLevel;
import com.annatala.pixelponies.scenes.GameScene;
import com.annatala.pixelponies.sprites.FetidRatSprite;
import com.annatala.pixelponies.sprites.GhostSprite;
import com.annatala.pixelponies.windows.WndQuest;
import com.annatala.pixelponies.windows.WndSadGhost;
import com.annatala.utils.Bundle;
import com.annatala.utils.Random;

import java.util.HashSet;
import java.util.Set;

public class Ghost extends NPC {

	{
		spriteClass = GhostSprite.class;
		
		flying = true;
		
		state = WANDERING;
	}
	
	private static final String TXT_ROSE1 = Game.getVar(R.string.Ghost_Rose1);
	private static final String TXT_ROSE2 = Game.getVar(R.string.Ghost_Rose2);
	private static final String TXT_RAT1  = Game.getVar(R.string.Ghost_Rat1);
	private static final String TXT_RAT2  = Game.getVar(R.string.Ghost_Rat2);

	
	public Ghost() {
	}
	
	@Override
	public int defenseSkill( Char enemy ) {
		return 1000;
	}
	
	@Override
	public String defenseVerb() {
		return Game.getVar(R.string.Ghost_Defense);
	}
	
	@Override
	public float speed() {
		return 0.5f;
	}
	
	@Override
	protected Char chooseEnemy() {
		return DUMMY;
	}
	
	@Override
	public void damage( int dmg, Object src ) {
	}
	
	@Override
	public void add( Buff buff ) {
	}
	
	@Override
	public boolean reset() {
		return true;
	}
	
	@Override
	public boolean interact(final Hero hero) {
		getSprite().turnTo( getPos(), hero.getPos() );
		
		Sample.INSTANCE.play( Assets.SND_GHOST );
		
		if (Quest.given) {
			
			Item item = Quest.alternative ?
				hero.belongings.getItem( RatSkull.class ) :
				hero.belongings.getItem( DriedRose.class );	
			if (item != null) {
				GameScene.show( new WndSadGhost( this, item ) );
			} else {
				GameScene.show( new WndQuest( this, Quest.alternative ? TXT_RAT2 : TXT_ROSE2 ) );
				
				int newPos = -1;
				for (int i=0; i < 10; i++) {
					newPos = Dungeon.level.randomRespawnCell();
					if (newPos != -1) {
						break;
					}
				}
				if (newPos != -1) {
					
					Actor.freeCell( getPos() );
					
					CellEmitter.get( getPos() ).start( Speck.factory( Speck.LIGHT ), 0.2f, 3 );
					setPos(newPos);
					getSprite().place( getPos() );
					getSprite().setVisible(Dungeon.visible[getPos()]);
				}
			}
			
		} else {
			GameScene.show( new WndQuest( this, Quest.alternative ? TXT_RAT1 : TXT_ROSE1 ) );
			Quest.given = true;
			
			Journal.add( Journal.Feature.GHOST );
		}
		return true;
	}
		
	private static final HashSet<Class<?>> IMMUNITIES = new HashSet<>();
	static {
		IMMUNITIES.add( Paralysis.class );
		IMMUNITIES.add( Roots.class );
	}
	
	@Override
	public Set<Class<?>> immunities() {
		return IMMUNITIES;
	}
	
	public static class Quest {
		
		private static boolean spawned;
		private static boolean alternative;
		private static boolean given;
		private static boolean processed;

		private static int depth;
		private static int left2kill;
		
		public static Weapon weapon;
		public static Barding barding;
		
		public static void reset() {
			spawned = false; 
			
			weapon = null;
			barding = null;
		}
		
		private static final String NODE		= "sadGhost";
		
		private static final String SPAWNED		= "spawned";
		private static final String ALTERNATIVE	= "alternative";
		private static final String LEFT2KILL	= "left2kill";
		private static final String GIVEN		= "given";
		private static final String PROCESSED	= "processed";
		private static final String DEPTH		= "depth";
		private static final String WEAPON		= "weapon";
		private static final String BARDING = "barding";
		
		public static void storeInBundle( Bundle bundle ) {
			
			Bundle node = new Bundle();
			
			node.put( SPAWNED, spawned );
			
			if (spawned) {
				
				node.put( ALTERNATIVE, alternative );
				if (!alternative) {
					node.put( LEFT2KILL, left2kill );
				}
				
				node.put( GIVEN, given );
				node.put( DEPTH, depth );
				node.put( PROCESSED, processed );
				
				node.put( WEAPON, weapon );
				node.put(BARDING, barding);
			}
			
			bundle.put( NODE, node );
		}
		
		public static void restoreFromBundle( Bundle bundle ) {
			
			Bundle node = bundle.getBundle( NODE );
			
			if (!node.isNull() && (spawned = node.getBoolean( SPAWNED ))) {
				
				alternative	=  node.getBoolean( ALTERNATIVE );
				if (!alternative) {
					left2kill = node.getInt( LEFT2KILL );
				}
				
				given	= node.getBoolean( GIVEN );
				depth	= node.getInt( DEPTH );
				processed	= node.getBoolean( PROCESSED );
				
				weapon	= (Weapon)node.get( WEAPON );
				barding = (Barding)node.get( BARDING );
			} else {
				reset();
			}
		}
		
		public static void spawn( SewerLevel level ) {
			if (!spawned && Dungeon.depth > 1 && Random.Int( 5 - Dungeon.depth ) == 0) {
				
				Ghost ghost = new Ghost();
				do {
					ghost.setPos(level.randomRespawnCell());
				} while (ghost.getPos() == -1);
				level.mobs.add( ghost );
				Actor.occupyCell( ghost );
				
				spawned = true;
				alternative = Random.Int( 2 ) == 0;
				if (!alternative) {
					left2kill = 8;
				}
				
				given = false;
				processed = false;
				depth = Dungeon.depth;
				
				do {
					weapon = (Weapon)Generator.random( Generator.Category.WEAPON );
				} while (weapon instanceof MissileWeapon);
				
				if (Dungeon.isChallenged( Challenges.NO_BARDING)) {
					barding = (Barding)new ClothBarding().degrade();
				} else {
					barding = (Barding)Generator.random( Generator.Category.BARDING);
				}
					
				for (int i=0; i < 3; i++) {
					Item another;
					do {
						another = Generator.random( Generator.Category.WEAPON );
					} while (another instanceof MissileWeapon);
					if (another.level() > weapon.level()) {
						weapon = (Weapon)another;
					}
					another = Generator.random( Generator.Category.BARDING);
					if (another.level() > barding.level()) {
						barding = (Barding)another;
					}
				}
				weapon.identify();
				barding.identify();
			}
		}

		public static void process( int pos ) {
			if (spawned && given && !processed && (depth == Dungeon.depth)) {
				if (alternative) {
					
					FetidRat rat = new FetidRat();
					rat.setPos(Dungeon.level.randomRespawnCell());
					if (rat.getPos() != -1) {
						Dungeon.level.spawnMob(rat);
						processed = true;
					}
					
				} else {
					
					if (Random.Int( left2kill ) == 0) {
						Dungeon.level.drop( new DriedRose(), pos ).sprite.drop();
						processed = true;
					} else {
						left2kill--;
					}
					
				}
			}
		}
		
		public static void complete() {
			weapon = null;
			barding = null;
			
			Journal.remove( Journal.Feature.GHOST );
		}
	}
	
	public static class FetidRat extends Mob {

		public FetidRat() {
			spriteClass = FetidRatSprite.class;
			
			hp(ht(15));
			defenseSkill = 5;
			
			EXP = 0;
			maxLvl = 5;	
			
			state = WANDERING;
			lootChance = 1;
			loot = new RatSkull();
		}
		
		@Override
		public int damageRoll() {
			return Random.NormalIntRange( 2, 6 );
		}
		
		@Override
		public int attackSkill( Char target ) {
			return 12;
		}
		
		@Override
		public int dr() {
			return 2;
		}
		
		@Override
		public int defenseProc( Char enemy, int damage ) {
			GameScene.add( Blob.seed( getPos(), 20, ParalyticGas.class ) );
			return super.defenseProc(enemy, damage);
		}

		private static final HashSet<Class<?>> IMMUNITIES = new HashSet<>();
		static {
			IMMUNITIES.add( Paralysis.class );
		}
		
		@Override
		public Set<Class<?>> immunities() {
			return IMMUNITIES;
		}
	}
}
