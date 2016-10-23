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
package com.annatala.pixelponies.items;

import com.annatala.pixelponies.android.util.TrackedRuntimeException;
import com.annatala.pixelponies.items.utility.Codex;
import com.annatala.pixelponies.items.utility.CommonCodex;
import com.annatala.pixelponies.items.utility.RareCodex;
import com.annatala.pixelponies.items.utility.UncommonCodex;
import com.annatala.pixelponies.items.weapon.melee.GoldenSword;
import com.annatala.pixelponies.items.weapon.melee.SacrificialSword;
import com.annatala.pixelponies.items.barding.AncientBarding;
import com.annatala.pixelponies.items.weapon.melee.Claymore;
import com.annatala.pixelponies.items.weapon.melee.Halberd;
import com.annatala.pixelponies.items.weapon.melee.CompositeCrossbow;
import com.annatala.pixelponies.items.weapon.melee.RubyCrossbow;
import com.annatala.pixelponies.items.weapon.melee.WoodenCrossbow;
import com.annatala.pixelponies.Dungeon;
import com.annatala.pixelponies.actors.hero.Hero;
import com.annatala.pixelponies.actors.mobs.npcs.WandMaker.Rotberry;
import com.annatala.pixelponies.items.barding.Barding;
import com.annatala.pixelponies.items.barding.ChainmailBarding;
import com.annatala.pixelponies.items.barding.ClothBarding;
import com.annatala.pixelponies.items.barding.PleatherBarding;
import com.annatala.pixelponies.items.barding.PlateBarding;
import com.annatala.pixelponies.items.barding.ScaleBarding;
import com.annatala.pixelponies.items.bags.Bag;
import com.annatala.pixelponies.items.food.ApplePie;
import com.annatala.pixelponies.items.food.Food;
import com.annatala.pixelponies.items.food.MysteriousHay;
import com.annatala.pixelponies.items.food.Ration;
import com.annatala.pixelponies.items.potions.Potion;
import com.annatala.pixelponies.items.potions.PotionOfExperience;
import com.annatala.pixelponies.items.potions.PotionOfFrost;
import com.annatala.pixelponies.items.potions.PotionOfHealing;
import com.annatala.pixelponies.items.potions.PotionOfHonesty;
import com.annatala.pixelponies.items.potions.PotionOfInvisibility;
import com.annatala.pixelponies.items.potions.PotionOfLevitation;
import com.annatala.pixelponies.items.potions.PotionOfLiquidFlame;
import com.annatala.pixelponies.items.potions.PotionOfMight;
import com.annatala.pixelponies.items.potions.PotionOfMindVision;
import com.annatala.pixelponies.items.potions.PotionOfParalyticGas;
import com.annatala.pixelponies.items.potions.PotionOfPurification;
import com.annatala.pixelponies.items.potions.PotionOfToxicGas;
import com.annatala.pixelponies.items.rings.Ring;
import com.annatala.pixelponies.items.rings.RingOfAccuracy;
import com.annatala.pixelponies.items.rings.RingOfDetection;
import com.annatala.pixelponies.items.rings.RingOfElements;
import com.annatala.pixelponies.items.rings.RingOfEvasion;
import com.annatala.pixelponies.items.rings.RingOfHaggler;
import com.annatala.pixelponies.items.rings.RingOfHaste;
import com.annatala.pixelponies.items.rings.RingOfHerbalism;
import com.annatala.pixelponies.items.rings.RingOfMending;
import com.annatala.pixelponies.items.rings.RingOfPower;
import com.annatala.pixelponies.items.rings.RingOfSatiety;
import com.annatala.pixelponies.items.rings.RingOfShadows;
import com.annatala.pixelponies.items.rings.RingOfThorns;
import com.annatala.pixelponies.items.scrolls.BlankScroll;
import com.annatala.pixelponies.items.scrolls.Scroll;
import com.annatala.pixelponies.items.scrolls.ScrollOfChallenge;
import com.annatala.pixelponies.items.scrolls.ScrollOfCurse;
import com.annatala.pixelponies.items.scrolls.ScrollOfDomination;
import com.annatala.pixelponies.items.scrolls.ScrollOfIdentify;
import com.annatala.pixelponies.items.scrolls.ScrollOfLoyalOath;
import com.annatala.pixelponies.items.scrolls.ScrollOfLullaby;
import com.annatala.pixelponies.items.scrolls.ScrollOfMagicMapping;
import com.annatala.pixelponies.items.scrolls.ScrollOfMirrorImage;
import com.annatala.pixelponies.items.scrolls.ScrollOfPsionicBlast;
import com.annatala.pixelponies.items.scrolls.ScrollOfRecharging;
import com.annatala.pixelponies.items.scrolls.ScrollOfRemoveCurse;
import com.annatala.pixelponies.items.scrolls.ScrollOfTeleportation;
import com.annatala.pixelponies.items.scrolls.ScrollOfTerror;
import com.annatala.pixelponies.items.scrolls.ScrollOfUpgrade;
import com.annatala.pixelponies.items.scrolls.ScrollOfWeaponUpgrade;
import com.annatala.pixelponies.items.wands.Wand;
import com.annatala.pixelponies.items.wands.WandOfAmok;
import com.annatala.pixelponies.items.wands.WandOfAvalanche;
import com.annatala.pixelponies.items.wands.WandOfBlink;
import com.annatala.pixelponies.items.wands.WandOfDisintegration;
import com.annatala.pixelponies.items.wands.WandOfFirebolt;
import com.annatala.pixelponies.items.wands.WandOfFlock;
import com.annatala.pixelponies.items.wands.WandOfLightning;
import com.annatala.pixelponies.items.wands.WandOfMagicMissile;
import com.annatala.pixelponies.items.wands.WandOfPoison;
import com.annatala.pixelponies.items.wands.WandOfRegrowth;
import com.annatala.pixelponies.items.wands.WandOfSlowness;
import com.annatala.pixelponies.items.wands.WandOfTelekinesis;
import com.annatala.pixelponies.items.wands.WandOfTeleportation;
import com.annatala.pixelponies.items.weapon.Weapon;
import com.annatala.pixelponies.items.weapon.melee.BattleAxe;
import com.annatala.pixelponies.items.weapon.melee.Bow;
import com.annatala.pixelponies.items.weapon.melee.CompoundBow;
import com.annatala.pixelponies.items.weapon.melee.Dagger;
import com.annatala.pixelponies.items.weapon.melee.Glaive;
import com.annatala.pixelponies.items.weapon.melee.SteelHorseshoes;
import com.annatala.pixelponies.items.weapon.melee.Kusarigama;
import com.annatala.pixelponies.items.weapon.melee.Longsword;
import com.annatala.pixelponies.items.weapon.melee.Mace;
import com.annatala.pixelponies.items.weapon.melee.Quarterstaff;
import com.annatala.pixelponies.items.weapon.melee.RubyBow;
import com.annatala.pixelponies.items.weapon.melee.Scythe;
import com.annatala.pixelponies.items.weapon.melee.Spear;
import com.annatala.pixelponies.items.weapon.melee.Sword;
import com.annatala.pixelponies.items.weapon.melee.WarHammer;
import com.annatala.pixelponies.items.weapon.melee.WoodenBow;
import com.annatala.pixelponies.items.weapon.missiles.Arrow;
import com.annatala.pixelponies.items.weapon.missiles.Boomerang;
import com.annatala.pixelponies.items.weapon.missiles.CommonArrow;
import com.annatala.pixelponies.items.weapon.missiles.CurareDart;
import com.annatala.pixelponies.items.weapon.missiles.Dart;
import com.annatala.pixelponies.items.weapon.missiles.FireArrow;
import com.annatala.pixelponies.items.weapon.missiles.IncendiaryDart;
import com.annatala.pixelponies.items.weapon.missiles.Javelin;
import com.annatala.pixelponies.items.weapon.missiles.ParalysisArrow;
import com.annatala.pixelponies.items.weapon.missiles.PoisonArrow;
import com.annatala.pixelponies.items.weapon.missiles.Shuriken;
import com.annatala.pixelponies.items.weapon.missiles.Tomahawk;
import com.annatala.pixelponies.plants.Dreamweed;
import com.annatala.pixelponies.plants.Earthroot;
import com.annatala.pixelponies.plants.Fadeleaf;
import com.annatala.pixelponies.plants.Firebloom;
import com.annatala.pixelponies.plants.Icecap;
import com.annatala.pixelponies.plants.Plant;
import com.annatala.pixelponies.plants.Sorrowmoss;
import com.annatala.pixelponies.plants.Sungrass;
import com.annatala.utils.Random;

import java.util.HashMap;

public class Generator {

	public enum Category {
		WEAPON	( 15,	Weapon.class ),
		BARDING ( 10,	Barding.class ),
		POTION	( 50,	Potion.class ),
		SCROLL	( 40,	Scroll.class ),
		WAND	( 4,	Wand.class ),
		RING	( 2,	Ring.class ),
		SEED	( 5,	Plant.Seed.class ),
		FOOD	( 0,	Food.class ),
		GOLD	( 50,	Gold.class ),
		RANGED  ( 2,	Bow.class),
		BULLETS ( 5,	Arrow.class),
		CODEX   ( 0,    Codex.class);
		
		public Class<?>[] classes;
		public float[] probs;
		public float[] luckyProbs;
		
		public float prob;
		public Class<? extends Item> superClass;
		
		Category(float prob, Class<? extends Item> superClass) {
			this.prob = prob;
			this.superClass = superClass;
		}
		
		public static int order( Item item ) {
			for (int i=0; i < values().length; i++) {
				if (values()[i].superClass.isInstance( item )) {
					return i;
				}
			}
			
			return item instanceof Bag ? Integer.MAX_VALUE : Integer.MAX_VALUE - 1;
		}
	}

	private static HashMap<Category,Float> categoryProbs = new HashMap<>();
	
	static {
		
		Category.GOLD.classes = new Class<?>[]{ 
			Gold.class };
		Category.GOLD.probs = new float[]{ 1 };
		Category.GOLD.luckyProbs = new float[]{ 1 };
		
		Category.SCROLL.classes = new Class<?>[]{ 
			ScrollOfIdentify.class, 
			ScrollOfTeleportation.class, 
			ScrollOfRemoveCurse.class, 
			ScrollOfUpgrade.class,
			ScrollOfRecharging.class,
			ScrollOfMagicMapping.class,
			ScrollOfChallenge.class,
			ScrollOfTerror.class,
			ScrollOfLullaby.class,
			ScrollOfWeaponUpgrade.class,
			ScrollOfPsionicBlast.class,
			ScrollOfMirrorImage.class,
			BlankScroll.class,
			ScrollOfDomination.class,
			ScrollOfCurse.class,
			ScrollOfLoyalOath.class
		};
		Category.SCROLL.probs = 		new float[]{ 30, 10, 15, 0, 10, 15, 12, 8, 8, 0, 4, 6, 10, 8, 6, 0};
		Category.SCROLL.luckyProbs = 	new float[]{ 30, 10, 15, 0, 10, 20, 6, 8, 8, 0, 8, 6, 15, 12, 3, 0};
		
		Category.POTION.classes = new Class<?>[]{ 
			PotionOfHealing.class, 
			PotionOfExperience.class,
			PotionOfToxicGas.class, 
			PotionOfParalyticGas.class,
			PotionOfLiquidFlame.class,
			PotionOfLevitation.class,
			PotionOfHonesty.class,
			PotionOfMindVision.class,
			PotionOfPurification.class,
			PotionOfInvisibility.class,
			PotionOfMight.class,
			PotionOfFrost.class };
		Category.POTION.probs = 		new float[]{ 45, 4, 15, 10, 15, 10, 0, 20, 12, 10, 0, 10 };
		Category.POTION.luckyProbs = 	new float[]{ 45, 6, 10, 7, 15, 12, 0, 20, 12, 15, 0, 10 };

		Category.WAND.classes = new Class<?>[]{ 
			WandOfTeleportation.class, 
			WandOfSlowness.class,
			WandOfFirebolt.class,
			WandOfRegrowth.class,
			WandOfPoison.class,
			WandOfBlink.class,
			WandOfLightning.class,
			WandOfAmok.class,
			WandOfTelekinesis.class,
			WandOfFlock.class,
			WandOfMagicMissile.class,
			WandOfDisintegration.class,
			WandOfAvalanche.class };
		Category.WAND.probs = 		new float[]{ 10, 10, 15, 6, 10, 11, 15, 10, 6, 10, 0, 5, 5 };
		Category.WAND.luckyProbs = 	new float[]{ 10, 10, 18, 6, 12, 14, 18, 8, 6, 5, 0, 8, 5 };
		
		Category.WEAPON.classes = new Class<?>[]{ 
			Dagger.class, 
			SteelHorseshoes.class,
			Quarterstaff.class, 
			Spear.class, 
			Mace.class, 
			Sword.class, 
			Longsword.class,
			BattleAxe.class,
			WarHammer.class, 
			Glaive.class,
			Scythe.class,
			Dart.class,
			Javelin.class,
			IncendiaryDart.class,
			CurareDart.class,
			Shuriken.class,
			Boomerang.class,
			Tomahawk.class,
			Kusarigama.class,
			SacrificialSword.class,
			Claymore.class,
		    Halberd.class,
			GoldenSword.class,};
		Category.WEAPON.probs = 		new float[]{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0.1f, 0.1f, 1, 1, 0.1f};
		Category.WEAPON.luckyProbs =	new float[]{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0.15f, 0.15f, 1, 1, 0.15f};
		
		Category.BARDING.classes = new Class<?>[]{
			ClothBarding.class,
			PleatherBarding.class,
			ChainmailBarding.class,
			ScaleBarding.class,
			PlateBarding.class,
			AncientBarding.class};
		Category.BARDING.probs = new float[]{ 1, 1, 1, 1, 1, 1 };
		Category.BARDING.luckyProbs = new float[]{ 1, 1, 1, 1, 1, 1 };
		
		Category.FOOD.classes = new Class<?>[]{ 
			Ration.class, 
			ApplePie.class,
			MysteriousHay.class };
		Category.FOOD.probs = new float[]{ 4, 1, 0 };
		Category.FOOD.luckyProbs = new float[]{ 4, 1, 0 };

		Category.RING.classes = new Class<?>[]{ 
			RingOfMending.class,
			RingOfDetection.class,
			RingOfShadows.class,
			RingOfPower.class,
			RingOfHerbalism.class,
			RingOfAccuracy.class,
			RingOfEvasion.class,
			RingOfSatiety.class,
			RingOfHaste.class,
			RingOfElements.class,
			RingOfHaggler.class,
			RingOfThorns.class };
		Category.RING.probs = new float[]{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0 };
		Category.RING.luckyProbs = new float[]{ 6, 5, 4, 4, 4, 5, 6, 4, 7, 4, 0, 0 };

		Category.SEED.classes = new Class<?>[]{ 
			Firebloom.Seed.class,
			Icecap.Seed.class,
			Sorrowmoss.Seed.class,
			Dreamweed.Seed.class,
			Sungrass.Seed.class,
			Earthroot.Seed.class,
			Fadeleaf.Seed.class,
			Rotberry.Seed.class };
		Category.SEED.probs = new float[]{ 1, 1, 1, 1, 1, 1, 1, 0 };
		Category.SEED.luckyProbs = new float[]{ 1, 1, 1, 1, 1, 1, 1, 0 };

		Category.RANGED.classes = new Class<?>[] {
				WoodenBow.class,
				WoodenCrossbow.class,
				CompoundBow.class,
				CompositeCrossbow.class,
				RubyBow.class,
				RubyCrossbow.class
		};
		Category.RANGED.probs = new float[]{ 3, 4, 3, 3, 1, 1 };
		Category.RANGED.luckyProbs = new float[]{ 2, 3, 3, 3, 2, 2 };
		
		Category.BULLETS.classes = new Class<?>[] {
				CommonArrow.class,
				FireArrow.class,
				PoisonArrow.class,
				ParalysisArrow.class,
		};
		Category.BULLETS.probs = new float[]{ 10, 2, 2, 2 };
		Category.BULLETS.luckyProbs = new float[]{ 10, 3, 3, 3 };

		Category.CODEX.classes = new Class<?>[]{
				CommonCodex.class,
				UncommonCodex.class,
				RareCodex.class
		};
		Category.CODEX.probs = new float[] { 25, 5, 1 };
		Category.CODEX.luckyProbs = new float[] { 9, 3, 1 };

	}
	
	public static void reset() {
		for (Category cat : Category.values()) {
			categoryProbs.put( cat, cat.prob );
		}
	}
	
	public static Item random() {
		return random( Random.chances( categoryProbs ) );
	}
	
	public static Item random( Category cat ) {
		try {
			
			categoryProbs.put( cat, categoryProbs.get( cat ) / 2 );
			
			switch (cat) {
			case BARDING:
				return randomBarding();
			case WEAPON:
				return randomWeapon();
			default:
				float[] theseProbs = (Random.luckBonus()) ? cat.luckyProbs : cat.probs;
				return ((Item)cat.classes[Random.chances( theseProbs )].newInstance()).random();
			}
		} catch (Exception e) {
			throw new TrackedRuntimeException("item generator",e);
		}
	}
	
	public static Item random( Class<? extends Item> cl ) {
		try {
			return cl.newInstance().random();
		} catch (Exception e) {
			throw new TrackedRuntimeException(e);
		}
	}
	
	private static Barding randomBarding() throws Exception {
		
		int curStr = Hero.STARTING_HONESTY + Dungeon.potionsOfHonesty;
		
		Category cat = Category.BARDING;
		float[] theseProbs = (Random.luckBonus()) ? cat.luckyProbs : cat.probs;

		Barding a1 = (Barding)cat.classes[Random.chances( theseProbs )].newInstance();
		Barding a2 = (Barding)cat.classes[Random.chances( theseProbs )].newInstance();
		
		a1.random();
		a2.random();
		
		return Math.abs( curStr - a1.minHonesty) < Math.abs( curStr - a2.minHonesty) ? a1 : a2;
	}
	
	private static Weapon randomWeapon() throws Exception {
		
		int curStr = Hero.STARTING_HONESTY + Dungeon.potionsOfHonesty;
		
		Category cat = Category.WEAPON;
		float[] theseProbs = (Random.luckBonus()) ? cat.luckyProbs : cat.probs;

		Weapon w1 = (Weapon)cat.classes[Random.chances( theseProbs )].newInstance();
		Weapon w2 = (Weapon)cat.classes[Random.chances( theseProbs )].newInstance();
		
		w1.random();
		w2.random();
		
		return Math.abs( curStr - w1.minAttribute ) < Math.abs( curStr - w2.minAttribute ) ? w1 : w2;
	}
}
