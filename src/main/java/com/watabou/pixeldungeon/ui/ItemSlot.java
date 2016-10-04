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
package com.watabou.pixeldungeon.ui;

import com.watabou.noosa.Text;
import com.watabou.noosa.ui.Button;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.barding.Barding;
import com.watabou.pixeldungeon.items.rings.Artifact;
import com.watabou.pixeldungeon.items.rings.Ring;
import com.watabou.pixeldungeon.items.weapon.Weapon;
import com.watabou.pixeldungeon.items.weapon.melee.Bow;
import com.watabou.pixeldungeon.items.weapon.melee.MeleeWeapon;
import com.watabou.pixeldungeon.items.weapon.missiles.Arrow;
import com.watabou.pixeldungeon.items.weapon.missiles.MissileWeapon;
import com.watabou.pixeldungeon.scenes.PixelScene;
import com.watabou.pixeldungeon.sprites.ItemSprite;
import com.watabou.pixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.pixeldungeon.utils.Utils;

public class ItemSlot extends Button {

	public static final int DEGRADED	= 0xFF4444;
	public static final int UPGRADED	= 0x44FF44;
	public static final int WARNING		= 0xFF8800;
	
	private static final float ENABLED	= 1.0f;
	private static final float DISABLED	= 0.3f;

	protected ItemSprite icon;
	protected Text topLeft;
	protected Text topRight;
	protected Text bottomRight;

	private static final String TXT_HONESTY	= "H:%d";
	private static final String TXT_TYPICAL_HONESTY	= "H:%d?";
	private static final String TXT_LOYALTY = "Y:%d";
	private static final String TXT_TYPICAL_LOYALTY	= "Y:%d?";
	private static final String TXT_MAGIC = "M:%d";
	private static final String TXT_TYPICAL_MAGIC	= "M:%d?";
	private static final String TXT_UNKNOWN_MAGIC	= "M:?";

	private static final String TXT_LEVEL	= "%+d";
	
	// Special items for containers
	public static final Item CHEST = new Item() {
		public int image() { return ItemSpriteSheet.CHEST; }
	};
	public static final Item LOCKED_CHEST = new Item() {
		public int image() { return ItemSpriteSheet.LOCKED_CHEST; }
	};
	public static final Item TOMB = new Item() {
		public int image() { return ItemSpriteSheet.TOMB; }
	};
	public static final Item SKELETON = new Item() {
		public int image() { return ItemSpriteSheet.BONES; }
	};
	
	public ItemSlot() {
		super();
	}
	
	public ItemSlot( Item item ) {
		this();
		item( item );
	}
		
	@Override
	protected void createChildren() {
		
		super.createChildren();
		
		icon = new ItemSprite();
		add( icon );
		
		topLeft = Text.createBasicText( PixelScene.font1x );
		topLeft.setScale(0.8f,0.8f);
		add( topLeft );
		
		topRight = Text.createBasicText( PixelScene.font1x );
		topRight.setScale(0.8f,0.8f);
		add( topRight );
		
		bottomRight = Text.createBasicText( PixelScene.font1x );
		bottomRight.setScale(0.8f,0.8f);
		add( bottomRight );
	}
	
	@Override
	protected void layout() {
		super.layout();
		
		icon.x = x + (width - icon.width) / 2;
		icon.y = y + (height - icon.height) / 2;
		
		if (topLeft != null) {
			topLeft.x = x;
			topLeft.y = y;
		}
		
		if (topRight != null) {
			topRight.x = x + (width - topRight.width());
			topRight.y = y;
		}
		
		if (bottomRight != null) {
			bottomRight.x = x + (width - bottomRight.width());
			bottomRight.y = y + (height - bottomRight.height());
		}
	}
	
	public void item( Item item ) {
		if (item == null) {

			active = false;

			icon.setVisible(false);
			topLeft.setVisible(false);
			topRight.setVisible(false);
			bottomRight.setVisible(false);
			return;
		}

		active = true;
		icon.setVisible(true);
		topLeft.setVisible(true);
		topRight.setVisible(true);
		bottomRight.setVisible(true);

		icon.view(item.imageFile(), item.image(), item.glowing());

		topLeft.text(item.status());

		// Horse jesus this is janky and stupid. I guess Watabou never heard of polymorphism.
		boolean isBarding = item instanceof Barding;
		boolean isMelee = item instanceof MeleeWeapon;
		boolean isMissile = item instanceof MissileWeapon;
		boolean isBow = item instanceof Bow;
		boolean isArrow = item instanceof Arrow;
		boolean isRing = item instanceof Ring;

		if (isBow) {

			if (item.levelKnown) {
				topRight.text(Utils.format(TXT_LOYALTY, ((Bow) item).minAttribute()));
				if (((Bow) item).minAttribute() > Dungeon.hero.effectiveLoyalty()) {
					topRight.hardlight(DEGRADED);
				} else {
					topRight.resetColor();
				}
			} else {
				topRight.text(Utils.format(TXT_TYPICAL_LOYALTY, ((MeleeWeapon) item).typicalMinimum()));
				topRight.hardlight(WARNING);
			}
			topRight.measure();

		} else if (isMissile && !isArrow) {

			topRight.text(Utils.format(TXT_LOYALTY, ((MissileWeapon) item).minAttribute()));
			if (((MissileWeapon) item).minAttribute() > Dungeon.hero.effectiveLoyalty()) {
				topRight.hardlight(DEGRADED);
			} else {
				topRight.resetColor();
			}
			topRight.measure();

		} else if (isMelee) {

			if (item.levelKnown) {
				topRight.text(Utils.format(TXT_HONESTY, ((MeleeWeapon) item).minAttribute()));
				if (((MeleeWeapon) item).minAttribute() > Dungeon.hero.effectiveHonesty()) {
					topRight.hardlight(DEGRADED);
				} else {
					topRight.resetColor();
				}
			} else {
				topRight.text(Utils.format(TXT_TYPICAL_HONESTY, ((MeleeWeapon) item).typicalMinimum()));
				topRight.hardlight(WARNING);
			}
			topRight.measure();

		} else if (isBarding) {

			if (item.levelKnown) {
				topRight.text(Utils.format(TXT_HONESTY, ((Barding) item).minHonesty));
				if (((Barding) item).minHonesty > Dungeon.hero.effectiveHonesty()) {
					topRight.hardlight(DEGRADED);
				} else {
					topRight.resetColor();
				}
			} else {
				topRight.text(Utils.format(TXT_TYPICAL_HONESTY, ((Barding) item).typicalHonesty()));
				topRight.hardlight(WARNING);
			}
			topRight.measure();

		} else if (isRing) {

			if (item.levelKnown) {
				topRight.text(Utils.format(TXT_MAGIC, ((Ring) item).minAttribute()));
				if (((Ring) item).minAttribute() > Dungeon.hero.effectiveMagic()) {
					topRight.hardlight(DEGRADED);
				} else {
					topRight.resetColor();
				}

			} else if (((Ring) item).isKnown()){
				topRight.text(Utils.format(TXT_TYPICAL_MAGIC, ((Ring) item).typicalMinimum()));
				topRight.hardlight(WARNING);
			} else {
				topRight.text(Utils.format(TXT_UNKNOWN_MAGIC));
				topRight.hardlight(WARNING);
			}
			topRight.measure();

		} else {
			
			topRight.text( null );
			
		}

		int level = item.visiblyUpgraded(); 

		if (level != 0 || (item.cursed && item.cursedKnown)) {
			bottomRight.text( item.levelKnown ? Utils.format( TXT_LEVEL, level ) : "" );
			bottomRight.measure();
			bottomRight.hardlight( level > 0 ? UPGRADED : DEGRADED );
		} else {
			bottomRight.text( null );
		}
		
		if(item instanceof Artifact) {
			Artifact artifact = (Artifact) item;
			String text = artifact.getText();
			
			if(text!=null) {
				topLeft.text(artifact.getText());
				topLeft.hardlight(artifact.getColor());
				topLeft.setVisible(true);
				topLeft.measure();
			}
		}
		
		layout();
	
	}
	
	public void enable( boolean value ) {
		
		active = value;
		
		float alpha = value ? ENABLED : DISABLED;
		icon.alpha( alpha );
		topLeft.alpha( alpha );
		topRight.alpha( alpha );
		bottomRight.alpha( alpha );
	}
	
	public void showParams( boolean value ) {
		if (value) {
			add( topRight );
			add( bottomRight );
		} else {
			remove( topRight );
			remove( bottomRight );
		}
	}
}
