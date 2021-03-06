package com.annatala.pixelponies.actors.mobs.npcs;

import com.annatala.pixelponies.items.quest.HeartOfDarkness;
import com.annatala.pixelponies.android.R;
import com.annatala.pixelponies.actors.mobs.guts.TreacherousSpirit;
import com.annatala.noosa.Game;
import com.annatala.pixelponies.Dungeon;
import com.annatala.pixelponies.Journal;
import com.annatala.pixelponies.actors.Char;
import com.annatala.pixelponies.actors.buffs.Buff;
import com.annatala.pixelponies.actors.buffs.Paralysis;
import com.annatala.pixelponies.actors.buffs.Roots;
import com.annatala.pixelponies.actors.hero.Hero;
import com.annatala.pixelponies.items.Item;
import com.annatala.pixelponies.items.potions.PotionOfMight;
import com.annatala.pixelponies.scenes.GameScene;
import com.annatala.utils.GLog;
import com.annatala.pixelponies.windows.WndBag;
import com.annatala.pixelponies.windows.WndQuest;
import com.annatala.pixelponies.windows.WndTradeItem;
import com.annatala.utils.Bundle;

import java.util.HashSet;
import java.util.Set;

public class AzuterronNPC extends NPC {

	private static final String TXT_QUEST_START = Game.getVar(R.string.AzuterronNPC_Quest_Start);
	private static final String TXT_QUEST_END = Game.getVar(R.string.AzuterronNPC_Quest_End);
	private static final String TXT_QUEST = Game.getVar(R.string.AzuterronNPC_Quest_Reminder);
	
	public AzuterronNPC() {
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
	protected boolean act() {

		throwItem();

		getSprite().turnTo( getPos(), Dungeon.hero.getPos() );
		spend( TICK );
		return true;
	}

	public static WndBag sell() {
		return GameScene.selectItem( itemSelector, WndBag.Mode.FOR_SALE, Game.getVar(R.string.Shopkeeper_Sell));
	}

	private static WndBag.Listener itemSelector = new WndBag.Listener() {
		@Override
		public void onSelect( Item item ) {
			if (item != null) {
				WndBag parentWnd = sell();
				GameScene.show( new WndTradeItem( item, parentWnd ) );
			}
		}
	};

	@Override
	public boolean interact(final Hero hero) {
		getSprite().turnTo( getPos(), hero.getPos() );
		if(Quest.completed) {
			sell();
			return true;
		}
		if (Quest.given) {
			
			Item item = hero.belongings.getItem( HeartOfDarkness.class );
			if (item != null) {

				item.removeItemFrom(Dungeon.hero);

				Item reward = new PotionOfMight();

				if (reward.doPickUp( Dungeon.hero )) {
					GLog.i( Hero.TXT_YOU_NOW_HAVE, reward.name() );
				} else {
					Dungeon.level.drop(reward, hero.getPos()).sprite.drop();
				}
				Quest.complete();
				GameScene.show( new WndQuest( this, TXT_QUEST_END ) );
			} else {
				GameScene.show( new WndQuest( this, TXT_QUEST ) );
			}
			
		} else {
			GameScene.show( new WndQuest( this, TXT_QUEST_START ) );
			Quest.given = true;
			Quest.process();
			Journal.add( Journal.Feature.AZUTERRON );
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

		private static boolean completed;
		private static boolean given;
		private static boolean processed;

		private static int depth;

		public static void reset() {
			completed = false;
			processed = false;
			given = false;
		}

		private static final String COMPLETED   = "completed";
		private static final String NODE		= "azuterron";
		private static final String GIVEN		= "given";
		private static final String PROCESSED	= "processed";
		private static final String DEPTH		= "depth";

		public static void storeInBundle( Bundle bundle ) {
			Bundle node = new Bundle();

			node.put(GIVEN, given);
			node.put(DEPTH, depth);
			node.put(PROCESSED, processed);
			node.put(COMPLETED, completed);

			bundle.put( NODE, node );
		}
		
		public static void restoreFromBundle( Bundle bundle ) {
			
			Bundle node = bundle.getBundle( NODE );

			if (!node.isNull()) {
				given	= node.getBoolean( GIVEN );
				depth	= node.getInt( DEPTH );
				processed	= node.getBoolean( PROCESSED );
				completed = node.getBoolean( COMPLETED );
			}
		}

		public static void process() {
			if (given && !processed) {
				TreacherousSpirit enemy = new TreacherousSpirit();
				enemy.setPos(Dungeon.level.randomRespawnCell());
				if (enemy.getPos() != -1) {
					Dungeon.level.spawnMob(enemy);
					processed = true;
				}
			}
		}

		public static void complete() {
			completed = true;
			Journal.remove( Journal.Feature.AZUTERRON );
		}
	}
}


