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
package com.watabou.pixeldungeon.items.weapon;

import com.nyrds.android.util.TrackedRuntimeException;
import com.nyrds.pixeldungeon.ml.R;
import com.watabou.noosa.Game;
import com.watabou.pixeldungeon.Badges;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.Gender;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.actors.hero.HeroClass;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.KindOfWeapon;
import com.watabou.pixeldungeon.items.weapon.enchantments.Death;
import com.watabou.pixeldungeon.items.weapon.enchantments.Fire;
import com.watabou.pixeldungeon.items.weapon.enchantments.Horror;
import com.watabou.pixeldungeon.items.weapon.enchantments.Instability;
import com.watabou.pixeldungeon.items.weapon.enchantments.Leech;
import com.watabou.pixeldungeon.items.weapon.enchantments.Luck;
import com.watabou.pixeldungeon.items.weapon.enchantments.Paralysis;
import com.watabou.pixeldungeon.items.weapon.enchantments.Piercing;
import com.watabou.pixeldungeon.items.weapon.enchantments.Poison;
import com.watabou.pixeldungeon.items.weapon.enchantments.Slow;
import com.watabou.pixeldungeon.items.weapon.enchantments.Swing;
import com.watabou.pixeldungeon.items.weapon.melee.Bow;
import com.watabou.pixeldungeon.items.weapon.melee.MeleeWeapon;
import com.watabou.pixeldungeon.items.weapon.missiles.MissileWeapon;
import com.watabou.pixeldungeon.sprites.ItemSprite;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.pixeldungeon.utils.Utils;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.IllegalFormatException;

public class Weapon extends KindOfWeapon {

	private static final String TXT_IDENTIFY     = Game.getVar(R.string.Weapon_Identify);
	private static final String TXT_INCOMPATIBLE = Game.getVar(R.string.Weapon_Incompatible);

	// NOTE: Weird issue here. For some reason I can't put these in the resource files.
	//       It must be parsing them in a way that breaks them, but I'm not sure why.
	private static final String TXT_MELEE_TO_STRING = "%s H:%d";
	private static final String TXT_RANGED_TO_STRING = "%s Y:%d";

	
	public float	ACU	= 1;
	public float	DLY	= 1f;

	public boolean enchatable = true;

	protected Gender gender = Gender.NEUTER;

	
	public enum Imbue {
		NONE, SPEED, ACCURACY
	}
	public Imbue imbue = Imbue.NONE;
	
	private int hitsToKnow = 20;
	
	private Enchantment enchantment;
	
	public void usedForHit() {
		if (!levelKnown && --hitsToKnow <= 0) {
			levelKnown = true;
			GLog.i(TXT_IDENTIFY, name(), toString());
			Badges.validateItemLevelAquired(this);
		}
	}
	
	@Override
	public void proc( Char attacker, Char defender, int damage ) {
		
		if (getEnchantment() != null) {
			getEnchantment().proc( this, attacker, defender, damage );
		}
		
		usedForHit();
	}
	
	private static final String ENCHANTMENT	= "enchantment";
	private static final String IMBUE		= "imbue";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( ENCHANTMENT, getEnchantment() );
		bundle.put( IMBUE, imbue );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		enchantment = (Enchantment)bundle.get( ENCHANTMENT );
		imbue = bundle.getEnum( IMBUE, Imbue.class );
	}
	
	@Override
	public float accuracyFactor(Hero hero ) {

		int encumbrance = 0;
		if (this instanceof MeleeWeapon) {
			if (this instanceof Bow) {
				encumbrance = minAttribute - hero.effectiveLoyalty();
			} else {
				encumbrance = minAttribute - hero.effectiveHonesty();
				if (hero.heroClass == HeroClass.NIGHTWING) {
					encumbrance += 3;
				}
			}
		} else {
			encumbrance = minAttribute - hero.effectiveLoyalty();
			if (hero.heroClass == HeroClass.ZEBRA) {
				encumbrance -= 2;
			}
		}
		
		return 
			(encumbrance > 0 ? (float)(ACU / Math.pow( 1.5, encumbrance )) : ACU) *
			(imbue == Imbue.ACCURACY ? 1.5f : 1.0f);
	}
	
	@Override
	public float speedFactor( Hero hero ) {

		int encumbrance = 0;
		if (this instanceof MeleeWeapon) {
			if (this instanceof Bow) {
				encumbrance = minAttribute - hero.effectiveLoyalty();
			} else {
				encumbrance = minAttribute - hero.effectiveHonesty();
			}
		} else {
			encumbrance = minAttribute - hero.effectiveHonesty();
			if (hero.heroClass == HeroClass.ZEBRA) {
				encumbrance -= 2;
			}
		}

		return 
			(encumbrance > 0 ? (float)(DLY * Math.pow( 1.2, encumbrance )) : DLY) *
			(imbue == Imbue.SPEED ? 0.6f : 1.0f);
	}
	
	@Override
	public int damageRoll( Hero hero ) {
		
		int damage = super.damageRoll( hero );
		int bonus = 0;

		if (this instanceof MeleeWeapon) {
			if (this instanceof Bow) {
				bonus = 0;
			} else {
				bonus = hero.effectiveHonesty() - minAttribute;
			}
		} else {
			if (hero.heroClass == HeroClass.ZEBRA) {
				bonus = hero.effectiveLoyalty() - minAttribute;
			}
		}

		if (bonus > 0) {
			damage += Random.IntRange( 0, bonus );
		}
		
		return damage;
	}
	
	public Item upgrade( boolean enchant ) {		
		if (getEnchantment() != null) {
			if (!enchant && Random.Int( level() ) > 0) {
				GLog.w( TXT_INCOMPATIBLE );
				enchant( null );
			}
		} else {
			if (enchant) {
				enchant( Enchantment.random() );
			}
		}
		
		return super.upgrade();
	}
	
	@Override
	public String toString() {
		if (this instanceof Bow || this instanceof MissileWeapon) {
			return levelKnown ? Utils.format(TXT_RANGED_TO_STRING, super.toString(), minAttribute) : super.toString();
		}
		return levelKnown ? Utils.format(TXT_MELEE_TO_STRING, super.toString(), minAttribute) : super.toString();
	}
	
	@Override
	public String name() {
		return getEnchantment() == null ? super.name() : getEnchantment().name( super.name(), gender );
	}
	
	@Override
	public Item random() {
		if (Random.Float() < 0.4) {
			int n = 1;
			if (Random.Int( 3 ) == 0) {
				n++;
				if (Random.Int( 3 ) == 0) {
					n++;
				}
			}
			if (Random.Int( 2 ) == 0) {
				upgrade( n );
			} else {
				degrade( n );
				cursed = true;
			}
		}
		return this;
	}
	
	public Weapon enchant( Enchantment ench ) {
		if(enchatable) {
			enchantment = ench;
		} else {
			enchantment = null;
		}
		return this;
	}
	
	public boolean isEnchanted() {
		return getEnchantment() != null;
	}
	
	@Override
	public ItemSprite.Glowing glowing() {
		return getEnchantment() != null ? getEnchantment().glowing() : null;
	}
	
	public Enchantment getEnchantment() {
		return enchantment;
	}

	public static abstract class Enchantment implements Bundlable {
		
		protected final String[] TXT_NAME = Utils.getClassParams(getClass().getSimpleName(), "Name", new String[]{"","",""}, true);
		
		private static final Class<?>[] enchants = new Class<?>[]{ 
			Fire.class, Poison.class, Death.class, Paralysis.class, Leech.class, 
			Slow.class, Swing.class, Piercing.class, Instability.class, Horror.class, Luck.class };
		private static final float[] chances= new float[]{ 10, 10, 1, 2, 1, 2, 3, 3, 3, 2, 2 };
			
		public abstract boolean proc( Weapon weapon, Char attacker, Char defender, int damage );
		
		public String name( String weaponName, Gender gender) {
			try{
				return Utils.format( TXT_NAME[gender.ordinal()], weaponName );
			} catch (IllegalFormatException e){
				GLog.w("ife in %s", getClass().getSimpleName());
			} catch (NullPointerException e){
				GLog.w("npe in %s", getClass().getSimpleName());
			}
			return weaponName;
		}
		
		@Override
		public void restoreFromBundle( Bundle bundle ) {	
		}

		@Override
		public void storeInBundle( Bundle bundle ) {	
		}
		
		public boolean dontPack() {
			return false;
		}
		
		public ItemSprite.Glowing glowing() {
			return ItemSprite.Glowing.WHITE;
		}
		
		@SuppressWarnings("unchecked")
		public static Enchantment random() {
			try {
				return ((Class<Enchantment>)enchants[ Random.chances( chances ) ]).newInstance();
			} catch (Exception e) {
				throw new TrackedRuntimeException(e);
			}
		}
		
	}
}
