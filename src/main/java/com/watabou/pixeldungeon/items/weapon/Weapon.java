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
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Actor;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.Gender;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.actors.hero.HeroClass;
import com.watabou.pixeldungeon.actors.mobs.Mob;
import com.watabou.pixeldungeon.items.Item;
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
import com.watabou.pixeldungeon.items.weapon.missiles.Arrow;
import com.watabou.pixeldungeon.items.weapon.missiles.MissileWeapon;
import com.watabou.pixeldungeon.levels.Level;
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
	private static final String TXT_MAGIC_SAVE   = Game.getVar(R.string.Weapon_Magic_Save);

	// NOTE: Weird issue here. For some reason I can't put these in the resource files.
	//       It must be parsing them in a way that breaks them, but I'm not sure why.
	private static final String TXT_MELEE_TO_STRING = "%s H:%d";
	private static final String TXT_BOW_TO_STRING = "%s Y:%d";

	
	public float	ACU	= 1;
	public float	DLY	= 1f;

	public boolean enchatable = true;

	protected Gender gender = Gender.NEUTER;

	
	public enum Imbue {
		NONE, SPEED, ACCURACY
	}
	public Imbue imbue = Imbue.NONE;
	
	private int hitFractionsToKnow = 200;
	
	private Enchantment enchantment;
	
	public void usedForHit() {

		if (!levelKnown) {

			// Might need to update this if we ever make wands equippable again, not sure.
			if (this.isEquipped(Dungeon.hero)) {

				// Drop hitFractionsToKnow based on Generosity level!
				// Multiplying fractions by 10, then Generosity removes (gen+1) * 2 many.
				// For base this takes ~26 hits.
				// Eventually this drops to ~8 for very generous characters.
				hitFractionsToKnow -= Dungeon.hero.effectiveGenerosity() * 2 + 2;

			// Does it even make sense in this case...?
			} else {
				hitFractionsToKnow -= 10;
			}

			if (hitFractionsToKnow <= 0) {
				levelKnown = true;
				GLog.i(TXT_IDENTIFY, name(), toString());
				Badges.validateItemLevelAquired(this);
			}

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
			}

		// Should only be thrown weapons at this point.
		} else {
			encumbrance = minAttribute - hero.effectiveHonesty();

			// Zebras can use weapons well if they're within 2 of the required honesty limit.
			if (hero.heroClass == HeroClass.ZEBRA) {
				encumbrance -= 2;
			}
		}

		float accuracy = ACU;
		if (encumbrance > 0) {
			accuracy /= (float) Math.pow(1.5, encumbrance);
		}
		if (imbue == Imbue.ACCURACY) {
			accuracy *= 1.5f;
		}
		// Earth ponies are more accurate with melee weapons.
		if (hero.heroClass == HeroClass.EARTH_PONY && this instanceof MeleeWeapon && !(this instanceof Bow)) {
			accuracy *= 1.2f;
		}
		// Earth ponies aren't too accurate with thrown weapons.
		if (hero.heroClass == HeroClass.EARTH_PONY && this instanceof MissileWeapon) {
			accuracy *= 0.9f;
		}

		// Nightwings are more accurate with bows.
		// EDIT: REMOVED AS THIS IS HANDLED IN Arrow!!!

		return accuracy;
	}

	
	@Override
	public float speedFactor( Hero hero ) {

		int encumbrance = minAttribute - hero.effectiveHonesty();

		boolean isShot = this instanceof Arrow && hero.bowEquipped();
		isShot = isShot && (!hero.isFlanked() || hero.heroClass == HeroClass.NIGHTWING);

		if (isShot) {

			GLog.w("Twang! (debug)");
			encumbrance =  hero.belongings.weapon.minAttribute() - hero.effectiveLoyalty();

			// Earth ponies and zeebees are ALWAYS slow with bows!
			if (Dungeon.hero.heroClass == HeroClass.EARTH_PONY ||
					Dungeon.hero.heroClass == HeroClass.ZEBRA) {
					encumbrance = (encumbrance > 0) ? encumbrance + 2 : 2;
			}


		// Zeebees can throw well if they're within two points of the minimum honesty.
		} else if (this instanceof MissileWeapon && !(this instanceof Arrow)){
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
		boolean isMelee = this instanceof MeleeWeapon && !(this instanceof Bow);
		boolean isThrown = this instanceof MissileWeapon && !(this instanceof Arrow);

		if (isMelee || (isThrown && hero.heroClass == HeroClass.ZEBRA)) {
			int bonus = hero.effectiveHonesty() - minAttribute;
			if (bonus > 0) {
				damage += Random.IntRange( 0, bonus );
			}
		}
		return damage;
	}
	
	public Item upgrade( boolean enchant ) {		
		if (getEnchantment() != null) {
			if (!enchant && Random.Int( level() ) > 0) {

				// Add in a chance for Magic level to prevent the overwriting.
				if (Random.Int( level() + 4 ) + 3 > Dungeon.hero.effectiveMagic()) {
					GLog.w(TXT_INCOMPATIBLE);
					enchant(null);
				} else {
					GLog.w(TXT_MAGIC_SAVE);
				}
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
		if (this instanceof Arrow) {
			return super.toString();
		} else if (this instanceof Bow) {
			return levelKnown ? Utils.format(TXT_BOW_TO_STRING, super.toString(), minAttribute) : super.toString();
		} else {
			return levelKnown ? Utils.format(TXT_MELEE_TO_STRING, super.toString(), minAttribute) : super.toString();
		}
	}
	
	@Override
	public String name() {
		return getEnchantment() == null ? super.name() : getEnchantment().name( super.name(), gender );
	}
	
	@Override
	public Item random() {
		if (Random.luckBonus() || Random.luckBonus()) {
			int n = 1;
			if (Random.luckBonus()) {
				n++;
				if (Random.luckBonus()) {
					n++;
				}
			}
			if (Random.Int( 5 ) < 3  && !Random.luckBonus()) {
				degrade( n );
				cursed = true;
			} else {
				upgrade( n );
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
		private static final float[] luckChances= new float[]{ 10, 10, 2, 3, 2, 3, 3, 3, 3, 3, 4 };

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
				if (Random.luckBonus()) {
					return ((Class<Enchantment>) enchants[Random.chances(luckChances)]).newInstance();
				} else {
					return ((Class<Enchantment>) enchants[Random.chances(chances)]).newInstance();
				}
			} catch (Exception e) {
				throw new TrackedRuntimeException(e);
			}
		}
		
	}
}
