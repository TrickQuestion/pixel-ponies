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
import com.annatala.pixelponies.Badges;
import com.annatala.pixelponies.Dungeon;
import com.annatala.pixelponies.Journal;
import com.annatala.pixelponies.actors.Char;
import com.annatala.pixelponies.actors.buffs.Buff;
import com.annatala.pixelponies.actors.hero.Hero;
import com.annatala.pixelponies.items.EquippableItem;
import com.annatala.pixelponies.items.Item;
import com.annatala.pixelponies.items.quest.DarkGold;
import com.annatala.pixelponies.items.quest.Pickaxe;
import com.annatala.pixelponies.items.scrolls.ScrollOfUpgrade;
import com.annatala.pixelponies.levels.Room;
import com.annatala.pixelponies.levels.Room.Type;
import com.annatala.pixelponies.scenes.GameScene;
import com.annatala.pixelponies.sprites.BlacksmithSprite;
import com.annatala.utils.GLog;
import com.annatala.pixelponies.windows.WndBlacksmith;
import com.annatala.pixelponies.windows.WndQuest;
import com.annatala.utils.Bundle;
import com.annatala.utils.Random;

import java.util.Collection;

public class Blacksmith extends NPC {

	private static final String TXT_GOLD_1       = Game.getVar(R.string.Blacksmith_Gold1);
	private static final String TXT_BLOOD_1      = Game.getVar(R.string.Blacksmith_Blood1);
	private static final String TXT2             = Game.getVar(R.string.Blacksmith_Txt2);
	private static final String TXT3             = Game.getVar(R.string.Blacksmith_Txt3);
	private static final String TXT4             = Game.getVar(R.string.Blacksmith_Txt4);
	private static final String TXT_COMPLETED    = Game.getVar(R.string.Blacksmith_Completed);
	private static final String TXT_GET_LOST     = Game.getVar(R.string.Blacksmith_GetLost);
	private static final String TXT_LOOKS_BETTER = Game.getVar(R.string.Blacksmith_LooksBetter);
	
	{
		spriteClass = BlacksmithSprite.class;
	}
	
	@Override
	protected boolean act() {
		throwItem();		
		return super.act();
	}
	
	@Override
	public boolean interact(final Hero hero) {
		
		getSprite().turnTo( getPos(), hero.getPos() );
		
		if (!Quest.given) {
			
			GameScene.show( new WndQuest( this, 
				Quest.alternative ? TXT_BLOOD_1 : TXT_GOLD_1 ) {
				
				@Override
				public void onBackPressed() {
					super.onBackPressed();
					
					Quest.given = true;
					Quest.completed = false;
					
					Pickaxe pick = new Pickaxe();
					if (pick.doPickUp( hero )) {
						GLog.i( Hero.TXT_YOU_NOW_HAVE, pick.name() );
					} else {
						Dungeon.level.drop( pick, hero.getPos() ).sprite.drop();
					}
				}
			} );
			
			Journal.add( Journal.Feature.TROLL );
			
		} else if (!Quest.completed) {
			if (Quest.alternative) {
				
				Pickaxe pick = hero.belongings.getItem( Pickaxe.class );
				if (pick == null) {
					tell( TXT2 );
				} else if (!pick.bloodStained) {
					tell( TXT4 );
				} else {
					if (pick.isEquipped( hero )) {
						pick.doUnequip( hero, false );
					}
					pick.detach( hero.belongings.backpack );
					tell( TXT_COMPLETED );
					
					Quest.completed = true;
					Quest.reforged = false;
				}
				
			} else {
				
				Pickaxe pick = hero.belongings.getItem( Pickaxe.class );
				DarkGold gold = hero.belongings.getItem( DarkGold.class );
				if (pick == null) {
					tell( TXT2 );
				} else if (gold == null || gold.quantity() < 15) {
					tell( TXT3 );
				} else {
					if (pick.isEquipped( hero )) {
						pick.doUnequip( hero, false );
					}
					pick.detach( hero.belongings.backpack );
					gold.detachAll( hero.belongings.backpack );
					tell( TXT_COMPLETED );
					
					Quest.completed = true;
					Quest.reforged = false;
				}
				
			}
		} else if (!Quest.reforged) {
			
			GameScene.show( new WndBlacksmith( this, hero ) );
			
		} else {
			
			tell( TXT_GET_LOST );
			
		}
		return true;
	}
	
	private void tell( String text ) {
		GameScene.show( new WndQuest( this, text ) );
	}
	
	public static String verify( Item item1, Item item2 ) {
		
		if (item1 == item2) {
			return Game.getVar(R.string.Blacksmith_Verify1);
		}
		
		if (item1.getClass() != item2.getClass()) {
			return Game.getVar(R.string.Blacksmith_Verify2);
		}
		
		if (!item1.isIdentified() || !item2.isIdentified()) {
			return Game.getVar(R.string.Blacksmith_Verify3);
		}
		
		if (item1.cursed || item2.cursed) {
			return Game.getVar(R.string.Blacksmith_Verify4);
		}
		
		if (item1.level() < 0 || item2.level() < 0) {
			return Game.getVar(R.string.Blacksmith_Verify5);
		}
		
		if (!item1.isUpgradable() || !item2.isUpgradable()) {
			return Game.getVar(R.string.Blacksmith_Verify6);
		}
		
		return null;
	}
	
	public static void upgrade( Item item1, Item item2 ) {
		
		Item first, second;
		if (item2.level() > item1.level()) {
			first = item2;
			second = item1;
		} else {
			first = item1;
			second = item2;
		}

		Sample.INSTANCE.play( Assets.SND_EVOKE );
		ScrollOfUpgrade.upgrade( Dungeon.hero );
		Item.evoke( Dungeon.hero );
		
		if (first.isEquipped( Dungeon.hero )) {
			((EquippableItem)first).doUnequip( Dungeon.hero, true );
		}
		first.upgrade();
		GLog.p( TXT_LOOKS_BETTER, first.name() );
		Dungeon.hero.spendAndNext( 2f );
		Badges.validateItemLevelAquired( first );
		
		if (second.isEquipped( Dungeon.hero )) {
			((EquippableItem)second).doUnequip( Dungeon.hero, false );
		}
		second.detachAll( Dungeon.hero.belongings.backpack );
		
		Quest.reforged = true;
		
		Journal.remove( Journal.Feature.TROLL );
	}
	
	@Override
	public int defenseSkill( Char enemy ) {
		return 1000;
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
	
	public static class Quest {
		
		private static boolean spawned;
		
		private static boolean alternative;
		private static boolean given;
		private static boolean completed;
		private static boolean reforged;
		
		public static void reset() {
			spawned		= false;
			given		= false;
			completed	= false;
			reforged	= false;
		}
		
		private static final String NODE	= "blacksmith";
		
		private static final String SPAWNED		= "spawned";
		private static final String ALTERNATIVE	= "alternative";
		private static final String GIVEN		= "given";
		private static final String COMPLETED	= "completed";
		private static final String REFORGED	= "reforged";
		
		public static void storeInBundle( Bundle bundle ) {
			
			Bundle node = new Bundle();
			
			node.put( SPAWNED, spawned );
			
			if (spawned) {
				node.put( ALTERNATIVE, alternative );
				node.put( GIVEN, given );
				node.put( COMPLETED, completed );
				node.put( REFORGED, reforged );
			}
			
			bundle.put( NODE, node );
		}
		
		public static void restoreFromBundle( Bundle bundle ) {

			Bundle node = bundle.getBundle( NODE );
			
			if (!node.isNull() && (spawned = node.getBoolean( SPAWNED ))) {
				alternative	=  node.getBoolean( ALTERNATIVE );
				given = node.getBoolean( GIVEN );
				completed = node.getBoolean( COMPLETED );
				reforged = node.getBoolean( REFORGED );
			} else {
				reset();
			}
		}
		
		public static void spawn( Collection<Room> rooms ) {
			if (!spawned && Dungeon.depth > 11 && Random.Int( 15 - Dungeon.depth ) == 0) {
				
				Room blacksmith;
				for (Room r : rooms) {
					if (r.type == Type.STANDARD && r.width() > 4 && r.height() > 4) {
						blacksmith = r;
						blacksmith.type = Type.BLACKSMITH;
						
						spawned = true;
						alternative = Random.Int( 2 ) == 0;
						
						given = false;
						
						break;
					}
				}
			}
		}
	}
}
