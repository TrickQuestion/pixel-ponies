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
package com.annatala.pixelponies.windows;

import android.graphics.RectF;

import com.annatala.pixelponies.android.util.GuiProperties;
import com.annatala.opengl.TextureCache;
import com.annatala.noosa.ColorBlock;
import com.annatala.noosa.Image;
import com.annatala.noosa.Text;
import com.annatala.noosa.audio.Sample;
import com.annatala.pixelponies.Assets;
import com.annatala.pixelponies.Dungeon;
import com.annatala.pixelponies.PixelPonies;
import com.annatala.pixelponies.actors.hero.Belongings;
import com.annatala.pixelponies.actors.hero.Hero;
import com.annatala.pixelponies.items.Gold;
import com.annatala.pixelponies.items.Item;
import com.annatala.pixelponies.items.barding.Barding;
import com.annatala.pixelponies.items.bags.Bag;
import com.annatala.pixelponies.items.bags.Keyring;
import com.annatala.pixelponies.items.bags.PotionBelt;
import com.annatala.pixelponies.items.bags.Quiver;
import com.annatala.pixelponies.items.bags.ScrollHolder;
import com.annatala.pixelponies.items.bags.SeedPouch;
import com.annatala.pixelponies.items.bags.WandHolster;
import com.annatala.pixelponies.items.food.RottenFood;
import com.annatala.pixelponies.items.scrolls.BlankScroll;
import com.annatala.pixelponies.items.scrolls.Scroll;
import com.annatala.pixelponies.items.wands.Wand;
import com.annatala.pixelponies.items.weapon.melee.MeleeWeapon;
import com.annatala.pixelponies.items.weapon.missiles.Arrow;
import com.annatala.pixelponies.items.weapon.missiles.Boomerang;
import com.annatala.pixelponies.plants.Plant.Seed;
import com.annatala.pixelponies.scenes.GameScene;
import com.annatala.pixelponies.scenes.PixelScene;
import com.annatala.pixelponies.sprites.ItemSpriteSheet;
import com.annatala.pixelponies.ui.Icons;
import com.annatala.pixelponies.ui.ItemSlot;
import com.annatala.pixelponies.ui.QuickSlot;
import com.annatala.utils.Utils;
import com.annatala.pixelponies.windows.elements.Tab;

public class WndBag extends WndTabbed {
	
	public enum Mode {
		ALL,
		UNIDENTIFED,
		UPGRADEABLE,
		QUICKSLOT,
		FOR_SALE,
		WEAPON,
		BARDING,
		WAND,
		SEED,
		INSCRIBABLE,
		MOISTABLE,
		UPGRADABLE_WEAPON
	}


	protected static final int COLS_P	= 4;
	protected static final int COLS_L	= 6;

	protected static final int SLOT_SIZE	= 28;
	protected static final int SLOT_MARGIN	= 1;
	
	private static final int TAB_WIDTH_P	= 18;
	private static final int TAB_WIDTH_L	= 26;
		
	private Listener listener;
	private WndBag.Mode mode;
	private String title;

	protected int count;
	protected int col;
	protected int row;
	
	private int nCols;
	private int nRows;
	
	private static Mode lastMode;
	private static Bag lastBag;
	
	private Text txtTitle;
	
	public WndBag( Bag bag, Listener listener, Mode mode, String title ) {
		
		super();
	
		nCols = PixelPonies.landscape() ? COLS_L : COLS_P;
		nRows = (Belongings.BACKPACK_SIZE + 4 + 1) / nCols + ((Belongings.BACKPACK_SIZE + 4 + 1) % nCols > 0 ? 1 : 0);
		
		this.listener = listener;
		this.mode = mode;
		this.title = title;
		
		lastMode = mode;
		lastBag = bag;
		
		int panelWidth = SLOT_SIZE * nCols + SLOT_MARGIN * (nCols - 1);
		
		txtTitle = PixelScene.createMultiline( title != null ? title : Utils.capitalize( bag.name() ), GuiProperties.titleFontSize());
		txtTitle.maxWidth(panelWidth);
		txtTitle.hardlight( TITLE_COLOR );
		txtTitle.measure();
		txtTitle.x = (int)(panelWidth - txtTitle.width()) / 2;
		if(txtTitle.x<0) {
			txtTitle.x = 0;
		}
		txtTitle.y = 0;
		add( txtTitle );
		
		placeItems( bag );
		
		resize( 
			panelWidth, 
			(int) (SLOT_SIZE * nRows + SLOT_MARGIN * (nRows - 1) + txtTitle.y + txtTitle.height() + SLOT_MARGIN) );
		
		Belongings stuff = Dungeon.hero.belongings;
		Bag[] bags = {
			stuff.backpack, 
			stuff.getItem( PotionBelt.class ), 
			stuff.getItem( SeedPouch.class ), 
			stuff.getItem( ScrollHolder.class ),
			stuff.getItem( WandHolster.class ),
			stuff.getItem( Keyring.class ),
			stuff.getItem( Quiver.class ) };
		
		for (Bag b : bags) {
			if (b != null) {
				BagTab tab = new BagTab(this, b );
				if(PixelPonies.landscape()){
					tab.setSize( TAB_WIDTH_L, tabHeight() );
				} else {
					tab.setSize( TAB_WIDTH_P, tabHeight() );
				}
				
				add( tab );
				
				tab.select( b == bag );
			}
		}
	}
	
	public static WndBag lastBag( Listener listener, Mode mode, String title ) {
		
		if (mode == lastMode && lastBag != null && 
			Dungeon.hero.belongings.backpack.contains( lastBag )) {
			
			return new WndBag( lastBag, listener, mode, title );
			
		} else {
			
			return new WndBag( Dungeon.hero.belongings.backpack, listener, mode, title );
			
		}
	}
	
	public static WndBag seedPouch( Listener listener, Mode mode, String title ) {
		SeedPouch pouch = Dungeon.hero.belongings.getItem( SeedPouch.class );
		return pouch != null ?
			new WndBag( pouch, listener, mode, title ) :
			new WndBag( Dungeon.hero.belongings.backpack, listener, mode, title );
	}
	
	protected void placeItems( Bag container ) {
		
		// Equipped items
		Belongings stuff = Dungeon.hero.belongings;
		placeItem( stuff.weapon != null ? stuff.weapon : new Placeholder( ItemSpriteSheet.WEAPON ) );
		placeItem( stuff.barding != null ? stuff.barding : new Placeholder( ItemSpriteSheet.BARDING) );
		placeItem( stuff.mane != null ? stuff.mane : new Placeholder( ItemSpriteSheet.RING ) );
		placeItem( stuff.tail != null ? stuff.tail : new Placeholder( ItemSpriteSheet.RING ) );
		
		// Unequipped items
		for (Item item : container.items) {
			placeItem( item );
		}
		
		// Empty slots
		while (count-4 < container.size) {
			placeItem( null );
		}
		
		// Gold
		if (container == Dungeon.hero.belongings.backpack) {
			row = nRows - 1;
			col = nCols - 1;
			placeItem( new Gold(Dungeon.gold()) );
		}
	}
	
	protected void placeItem( final Item item ) {
		
		int x = col * (SLOT_SIZE + SLOT_MARGIN);
		int y = (int) (txtTitle.height() + SLOT_MARGIN + row * (SLOT_SIZE + SLOT_MARGIN));
		
		add( new ItemButton( item ).setPos( x, y ) );
		
		if (++col >= nCols) {
			col = 0;
			row++;
		}
		
		count++;
	}
	
	@Override
	public void onMenuPressed() {
		if (listener == null) {
			hide();
		}
	}
	
	@Override
	public void onBackPressed() {
		if (listener != null) {
			listener.onSelect( null );
		}
		super.onBackPressed();
	}
	
	@Override
	public void onClick( Tab tab ) {
		hide();
		GameScene.show( new WndBag( ((BagTab)tab).bag, listener, mode, title ) );
	}
	
	@Override
	protected int tabHeight() {
		return 20;
	}
	
	private class BagTab extends Tab {
		
		private Image icon;

		private Bag bag;
		
		public BagTab(WndBag parent, Bag bag ) {
			super(parent);
			
			this.bag = bag;
			
			icon = icon();
			add( icon );
		}
		
		@Override
		public void select( boolean value ) {
			super.select( value );
			icon.am = selected ? 1.0f : 0.6f;
		}
		
		@Override
		protected void layout() {
			super.layout();
			
			icon.copy( icon() );
			icon.x = x + (width - icon.width) / 2;
			icon.y = y + (height - icon.height) / 2 - 2 - (selected ? 0 : 1);
			if (!selected && icon.y < y + CUT) {
				RectF frame = icon.frame();
				frame.top += (y + CUT - icon.y) / icon.texture.height;
				icon.frame( frame );
				icon.y = y + CUT;
			}
		}
		
		private Image icon() {
			if (bag instanceof SeedPouch) {
				return Icons.get( Icons.SEED_POUCH );
			} else if (bag instanceof ScrollHolder) {
				return Icons.get( Icons.SCROLL_HOLDER );
			} else if (bag instanceof WandHolster) {
				return Icons.get( Icons.WAND_HOLSTER );
			} else if (bag instanceof PotionBelt) {
				return Icons.get( Icons.POTIONS_BELT );
			} else if (bag instanceof Keyring) {
				return Icons.get( Icons.KEYRING );
			} else if (bag instanceof Quiver) {
				return Icons.get( Icons.QUIVER);
			} else {
				return Icons.get( Icons.BACKPACK );
			}
		}
	}
	
	private static class Placeholder extends Item {		
		{
			name = null;
		}
		
		public Placeholder( int image ) {
			this.image = image;
		}
		
		@Override
		public boolean isIdentified() {
			return true;
		}
		
		@Override
		public boolean isEquipped( Hero hero ) {
			return true;
		}
	}
	
	private class ItemButton extends ItemSlot {
		
		private static final int NORMAL		= 0xFF4A4D44;
		private static final int EQUIPPED	= 0xFF63665B;
		
		private Item item;
		private ColorBlock bg;
		
		public ItemButton( Item item ) {
			
			super( item );

			this.item = item;
			if (item instanceof Gold) {
				bg.setVisible(false);
			}
			
			width = height = SLOT_SIZE;
		}
		
		@Override
		protected void createChildren() {	
			bg = new ColorBlock( SLOT_SIZE, SLOT_SIZE, NORMAL );
			add( bg );
			
			super.createChildren();
		}
		
		@Override
		protected void layout() {
			bg.x = x;
			bg.y = y;
			
			super.layout();
		}
		
		@Override
		public void item( Item item ) {
			
			super.item( item );
			if (item != null) {

				bg.texture( TextureCache.createSolid( item.isEquipped( Dungeon.hero ) ? EQUIPPED : NORMAL ) );
				if (item.cursed && item.cursedKnown) {
					bg.ra = +0.2f;
					bg.ga = -0.1f;
				} else if (!item.isIdentified()) {
					bg.ra = 0.1f;
					bg.ba = 0.1f;
				}
				
				if (item.name() == null) {
					enable( false );
				} else {
					enable( 
						mode == Mode.FOR_SALE && (item.price() > 0) && (!item.isEquipped( Dungeon.hero ) || !item.cursed) ||
						mode == Mode.UPGRADEABLE && item.isUpgradable() || 
						mode == Mode.UNIDENTIFED && !item.isIdentified() ||
						mode == Mode.QUICKSLOT && (item.defaultAction != null) ||
						mode == Mode.WEAPON && (item instanceof MeleeWeapon || item instanceof Boomerang) ||
						mode == Mode.BARDING && (item instanceof Barding) ||
						mode == Mode.WAND && (item instanceof Wand) ||
						mode == Mode.SEED && (item instanceof Seed) ||
						mode == Mode.INSCRIBABLE && (item instanceof Barding || item instanceof BlankScroll) ||
						mode == Mode.MOISTABLE && ( item instanceof Arrow || item instanceof Scroll || item instanceof RottenFood ) ||
						mode == Mode.UPGRADABLE_WEAPON && ((item instanceof MeleeWeapon || item instanceof Boomerang ) && (item.isUpgradable())) ||
						mode == Mode.ALL
					);
				}
			} else {
				bg.color( NORMAL );
			}
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
			if (listener != null) {
				
				hide();
				listener.onSelect( item );
				
			} else {
				
				WndBag.this.add( new WndItem( WndBag.this, item ) );
				
			}
		}
		
		@Override
		protected boolean onLongClick() {
			if (listener == null && item.defaultAction != null) {
				hide();
				QuickSlot.selectItem(item instanceof Wand ? item : item.getClass(),0);
				return true;
			} else {
				return false;
			}
		}
	}
	
	public interface Listener {
		void onSelect( Item item );
	}
}
