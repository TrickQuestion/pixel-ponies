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
package com.watabou.pixeldungeon.windows;

import com.nyrds.android.util.GuiProperties;
import com.nyrds.pixeldungeon.ml.R;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.Image;
import com.watabou.noosa.Text;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.ui.Button;
import com.watabou.pixeldungeon.Assets;
import com.watabou.pixeldungeon.Badges;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.Statistics;
import com.watabou.pixeldungeon.actors.hero.Belongings;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.scenes.PixelScene;
import com.watabou.pixeldungeon.sprites.HeroSpriteDef;
import com.watabou.pixeldungeon.ui.BadgesList;
import com.watabou.pixeldungeon.ui.Icons;
import com.watabou.pixeldungeon.ui.ItemSlot;
import com.watabou.pixeldungeon.ui.QuickSlot;
import com.watabou.pixeldungeon.ui.RedButton;
import com.watabou.pixeldungeon.ui.ScrollPane;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.pixeldungeon.utils.Utils;
import com.watabou.pixeldungeon.windows.elements.RankingTab;
import com.watabou.pixeldungeon.windows.elements.Tab;

import java.util.Locale;

public class WndRanking extends WndTabbed {
	
	private static final String TXT_ERROR   = Game.getVar(R.string.WndRanking_Error);
	
	private static final String TXT_STATS	= Game.getVar(R.string.WndRanking_Stats);
	private static final String TXT_ITEMS	= Game.getVar(R.string.WndRanking_Items);
	private static final String TXT_BADGES	= Game.getVar(R.string.WndRanking_Badges);

	private static final int WIDTH			= 112;
	private static final int HEIGHT			= 144;
	
	private static final int TAB_WIDTH	= 40;
	
	private Thread thread;
	private String error = null;

	private Image busy;
	
	public WndRanking( final String gameFile ) {
		
		super();
		resize( WIDTH, HEIGHT );
		
		thread = new Thread() {
			@Override
			public void run() {
				try {
					Badges.loadGlobal();
					Dungeon.loadGameForRankings( gameFile );
				} catch (Exception e ) {
					error = TXT_ERROR + "->" +e.getMessage();
					GLog.i(error);
				}
			}
		};
		thread.start();
		
		busy = Icons.BUSY.get();	
		busy.origin.set( busy.width / 2, busy.height / 2 );
		busy.angularSpeed = 720;
		busy.x = (WIDTH - busy.width) / 2;
		busy.y = (HEIGHT - busy.height) / 2;
		add( busy );
	}
	
	@Override
	public void update() {
		super.update();
		
		if (thread != null && !thread.isAlive()) {
			thread = null;
			if (error == null) {
				remove( busy );
				createControls();
			} else {
				hide();
				Game.scene().add( new WndError( TXT_ERROR ) );
			}
		}
	}
	
	private void createControls() {
		
		String[] labels = 
			{TXT_STATS, TXT_ITEMS, TXT_BADGES};
		Group[] pages = 
			{new StatsTab(), new ItemsTab(), new BadgesTab()};
		
		for (int i=0; i < pages.length; i++) {
			
			add( pages[i] );
			
			Tab tab = new RankingTab(this, labels[i], pages[i] );
			tab.setSize( TAB_WIDTH, tabHeight() );
			add( tab );
		}
		
		select( 0 );
	}
	
	private class StatsTab extends Group {
		
		private static final int GAP	= 4;

		private final String TXT_TITLE      = Game.getVar(R.string.WndRanking_StaTitle);
		private final String TXT_CHALLENGES = Game.getVar(R.string.WndRanking_StaChallenges);
		
		private final String TXT_HEALTH	  = Game.getVar(R.string.WndRanking_StaHealth);
		private final String TXT_STR      = Game.getVar(R.string.WndRanking_StaHonesty);
		
		private final String TXT_DURATION = Game.getVar(R.string.WndRanking_StaDuration);
		
		private final String TXT_DEPTH    = Game.getVar(R.string.WndRanking_StaDepth);
		private final String TXT_ENEMIES  = Game.getVar(R.string.WndRanking_StaEnemies);
		private final String TXT_GOLD     = Game.getVar(R.string.WndRanking_StaGold);
		
		private final String TXT_FOOD     = Game.getVar(R.string.WndRanking_StaFood);
		private final String TXT_ALCHEMY  = Game.getVar(R.string.WndRanking_StaAlchemy);
		private final String TXT_ANKHS    = Game.getVar(R.string.WndRanking_StaAnkhs);
		
		public StatsTab() {
			super();
			
			Hero hero = Dungeon.hero;
			String heroClass = hero.className();

			HeroSpriteDef heroSprite = new HeroSpriteDef(hero, false);

			IconTitle title = new IconTitle();
			title.icon( heroSprite.avatar() );
			title.label( Utils.format( TXT_TITLE, hero.lvl(), heroClass ).toUpperCase( Locale.ENGLISH ) );
			title.setRect( 0, 0, WIDTH, 0 );
			title.color(0xCC33FF);
			add( title );
			
			float pos = title.bottom();
			
			if (Dungeon.challenges > 0) {
				RedButton btnCatalogus = new RedButton( TXT_CHALLENGES ) {
					@Override
					protected void onClick() {
						Game.scene().add( new WndChallenges( Dungeon.challenges, false ) );
					}
				};
				btnCatalogus.setRect( 0, pos + GAP, btnCatalogus.reqWidth() + 2, btnCatalogus.reqHeight() + 2 );
				add( btnCatalogus );
				
				pos = btnCatalogus.bottom();
			}
			
			pos += GAP + GAP;
			
			pos = statSlot( this, difficultyToText(hero.getDifficulty()), "", pos );
			
			pos += GAP;
			
			pos = statSlot( this, TXT_STR, Integer.toString( hero.effectiveHonesty() ), pos );
			pos = statSlot( this, TXT_HEALTH, Integer.toString( hero.ht() ), pos );
			
			pos += GAP;
			
			pos = statSlot( this, TXT_DURATION, Integer.toString( (int)Statistics.duration ), pos );
			
			pos += GAP;
			
			pos = statSlot( this, TXT_DEPTH, Integer.toString( Statistics.deepestFloor ), pos );
			pos = statSlot( this, TXT_ENEMIES, Integer.toString( Statistics.enemiesSlain ), pos );
			pos = statSlot( this, TXT_GOLD, Integer.toString( Statistics.goldCollected ), pos );
			
			pos += GAP;
			
			pos = statSlot( this, TXT_FOOD, Integer.toString( Statistics.foodEaten ), pos );
			pos = statSlot( this, TXT_ALCHEMY, Integer.toString( Statistics.potionsCooked ), pos );
			pos = statSlot( this, TXT_ANKHS, Integer.toString( Statistics.ankhsUsed ), pos );
		}
		
		private String difficultyToText(int difficulty) {

			switch (difficulty) {
			case 0:
				return Game.getVar(R.string.StartScene_DifficultyEasy);
			case 1:
				return Game.getVar(R.string.StartScene_DifficultyNormalWithSaves);
			case 2:
				return Game.getVar(R.string.StartScene_DifficultyNormal);
			case 3:
				return Game.getVar(R.string.StartScene_DifficultyExpert);
			}
			
			return "";
		}
		
		private float statSlot( Group parent, String label, String value, float pos ) {
			
			Text txt = PixelScene.createText( label, GuiProperties.regularFontSize() );
			txt.y = pos;
			parent.add( txt );
			
			txt = PixelScene.createText( value, GuiProperties.regularFontSize() );
			txt.measure();
			txt.x = PixelScene.align( WIDTH * 0.65f );
			txt.y = pos;
			parent.add( txt );
			
			return pos + GAP + txt.baseLine();
		}
	}
	
	private class ItemsTab extends Group {
		
		private float pos;
		
		public ItemsTab() {
			super();
			
			Belongings stuff = Dungeon.hero.belongings;
			if (stuff.weapon != null) {
				addItem( stuff.weapon );
			}
			if (stuff.armor != null) {
				addItem( stuff.armor );
			}
			if (stuff.mane != null) {
				addItem( stuff.mane );
			}
			if (stuff.tail != null) {
				addItem( stuff.tail );
			}
			
			for(int i = 0;i<3;++i) {
				Object qsItem = QuickSlot.getEarlyLoadItem(i);
				if(qsItem instanceof Item && Dungeon.hero.belongings.backpack.contains((Item)qsItem)){
					addItem((Item)qsItem);
				} else if( qsItem instanceof Class){
					@SuppressWarnings("unchecked")
					Item item = Dungeon.hero.belongings.getItem( (Class<? extends Item>)qsItem );
					if (item != null) {
						addItem( item );
					}					
				}
			}
		}
		
		private void addItem( Item item ) {
			ItemButton slot = new ItemButton( item );
			slot.setRect( 0, pos, width, ItemButton.HEIGHT );
			add( slot );
			
			pos += slot.height() + 1;
		}
	}
	
	private class BadgesTab extends Group {
		
		public BadgesTab() {
			super();
			
			camera = WndRanking.this.camera;
			
			ScrollPane list = new BadgesList( false );
			add( list );
			
			list.setSize( WIDTH, HEIGHT );
		}
	}
	
	private class ItemButton extends Button {
		
		public static final int HEIGHT	= 23;
		
		private Item item;
		
		private ItemSlot slot;
		private ColorBlock bg;
		private Text name;
		
		public ItemButton( Item item ) {
			
			super();

			this.item = item;
			
			slot.item( item );
			if (item.cursed && item.cursedKnown) {
				bg.ra = +0.2f;
				bg.ga = -0.1f;
			} else if (!item.isIdentified()) {
				bg.ra = 0.1f;
				bg.ba = 0.1f;
			}
		}
		
		@Override
		protected void createChildren() {	
			
			bg = new ColorBlock( HEIGHT, HEIGHT, 0xFF4A4D44 );
			add( bg );
			
			slot = new ItemSlot();
			add( slot );
			
			name = PixelScene.createText( "?", GuiProperties.smallFontSize());
			add( name );
			
			super.createChildren();
		}
		
		@Override
		protected void layout() {
			bg.x = x;
			bg.y = y;
			
			slot.setRect( x, y, HEIGHT, HEIGHT );
			
			name.x = slot.right() + 2;
			name.y = y + (height - name.baseLine()) / 2;
			
			String str = Utils.capitalize( item.name() );
			name.text( str );
			name.measure();
			if (name.width() > width - name.x) {
				do {
					str = str.substring( 0, str.length() - 1 );
					name.text( str + "..." );
					name.measure();
				} while (name.width() > width - name.x);
			}
			
			super.layout();
		}
		
		@Override
		protected void onTouchDown() {
			bg.brightness( 1.5f );
			Sample.INSTANCE.play( Assets.SND_CLICK, 0.7f, 0.7f, 1.2f );
		}

		protected void onTouchUp() {
			bg.brightness( 1.0f );
		}

		@Override
		protected void onClick() {
			Game.scene().add( new WndItem( null, item ) );
		}
	}
}
