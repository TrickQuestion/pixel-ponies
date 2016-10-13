package com.annatala.pixelponies.items;

import com.annatala.pixelponies.android.util.TrackedRuntimeException;
import com.annatala.pixelponies.items.rings.RatKingCrown;
import com.annatala.pixelponies.items.weapon.melee.SacrificialSword;
import com.annatala.pixelponies.items.barding.SpiderBarding;
import com.annatala.pixelponies.items.quest.HeartOfDarkness;
import com.annatala.pixelponies.items.potions.PotionOfPurification;
import com.annatala.pixelponies.items.utility.CrystallingKit;
import com.annatala.pixelponies.items.barding.AncientBarding;
import com.annatala.pixelponies.items.weapon.melee.Claymore;
import com.annatala.pixelponies.items.weapon.melee.Halberd;
import com.annatala.pixelponies.items.weapon.melee.CompositeCrossbow;
import com.annatala.pixelponies.items.weapon.melee.RubyCrossbow;
import com.annatala.pixelponies.items.weapon.melee.WoodenCrossbow;
import com.annatala.pixelponies.items.rings.BlackSkull;
import com.annatala.noosa.Game;
import com.annatala.pixelponies.actors.mobs.npcs.WandMaker;
import com.annatala.pixelponies.items.utility.Amulet;
import com.annatala.pixelponies.items.utility.Ankh;
import com.annatala.pixelponies.items.utility.Codex;
import com.annatala.pixelponies.items.utility.DewVial;
import com.annatala.pixelponies.items.weapon.melee.GoldenSword;
import com.annatala.pixelponies.items.weapon.melee.SteelHorseshoes;
import com.annatala.pixelponies.plants.Dewdrop;
import com.annatala.pixelponies.items.utility.LloydsBeacon;
import com.annatala.pixelponies.items.utility.SpiderCharm;
import com.annatala.pixelponies.items.utility.Stylus;
import com.annatala.pixelponies.items.utility.TomeOfMastery;
import com.annatala.pixelponies.items.utility.Torch;
import com.annatala.pixelponies.items.utility.Weightstone;
import com.annatala.pixelponies.items.barding.AssasinBarding;
import com.annatala.pixelponies.items.barding.BattleMageBarding;
import com.annatala.pixelponies.items.barding.ClothBarding;
import com.annatala.pixelponies.items.barding.ElfBarding;
import com.annatala.pixelponies.items.barding.FreeRunnerBarding;
import com.annatala.pixelponies.items.barding.HuntressBarding;
import com.annatala.pixelponies.items.barding.PleatherBarding;
import com.annatala.pixelponies.items.barding.MageBarding;
import com.annatala.pixelponies.items.barding.ChainmailBarding;
import com.annatala.pixelponies.items.barding.PlateBarding;
import com.annatala.pixelponies.items.barding.RogueBarding;
import com.annatala.pixelponies.items.barding.ScaleBarding;
import com.annatala.pixelponies.items.barding.ScoutBarding;
import com.annatala.pixelponies.items.barding.ShamanBarding;
import com.annatala.pixelponies.items.barding.SniperBarding;
import com.annatala.pixelponies.items.barding.WardenBarding;
import com.annatala.pixelponies.items.bags.Keyring;
import com.annatala.pixelponies.items.bags.PotionBelt;
import com.annatala.pixelponies.items.bags.Quiver;
import com.annatala.pixelponies.items.bags.ScrollHolder;
import com.annatala.pixelponies.items.bags.SeedPouch;
import com.annatala.pixelponies.items.bags.WandHolster;
import com.annatala.pixelponies.items.food.FreezeDriedHay;
import com.annatala.pixelponies.items.food.HayFries;
import com.annatala.pixelponies.items.food.MysteriousHay;
import com.annatala.pixelponies.items.food.OverpricedRation;
import com.annatala.pixelponies.items.food.ApplePie;
import com.annatala.pixelponies.items.food.ChangelingPie;
import com.annatala.pixelponies.items.food.Ration;
import com.annatala.pixelponies.items.food.RottenHay;
import com.annatala.pixelponies.items.food.RottenPie;
import com.annatala.pixelponies.items.food.RottenRation;
import com.annatala.pixelponies.items.keys.GoldenKey;
import com.annatala.pixelponies.items.keys.IronKey;
import com.annatala.pixelponies.items.keys.SkeletonKey;
import com.annatala.pixelponies.items.potions.PotionOfExperience;
import com.annatala.pixelponies.items.potions.PotionOfFrost;
import com.annatala.pixelponies.items.potions.PotionOfHealing;
import com.annatala.pixelponies.items.potions.PotionOfInvisibility;
import com.annatala.pixelponies.items.potions.PotionOfLevitation;
import com.annatala.pixelponies.items.potions.PotionOfLiquidFlame;
import com.annatala.pixelponies.items.potions.PotionOfMight;
import com.annatala.pixelponies.items.potions.PotionOfMindVision;
import com.annatala.pixelponies.items.potions.PotionOfParalyticGas;
import com.annatala.pixelponies.items.potions.PotionOfHonesty;
import com.annatala.pixelponies.items.potions.PotionOfToxicGas;
import com.annatala.pixelponies.items.quest.CorpseDust;
import com.annatala.pixelponies.items.quest.DarkGold;
import com.annatala.pixelponies.items.quest.DriedRose;
import com.annatala.pixelponies.items.quest.Pickaxe;
import com.annatala.pixelponies.items.quest.RatSkull;
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
import com.annatala.pixelponies.items.rings.RingOfStoneWalking;
import com.annatala.pixelponies.items.rings.RingOfThorns;
import com.annatala.pixelponies.items.scrolls.BlankScroll;
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
import com.annatala.pixelponies.items.weapon.melee.BattleAxe;
import com.annatala.pixelponies.items.weapon.melee.CompoundBow;
import com.annatala.pixelponies.items.weapon.melee.Dagger;
import com.annatala.pixelponies.items.weapon.melee.Glaive;
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
import com.annatala.pixelponies.items.weapon.missiles.Boomerang;
import com.annatala.pixelponies.items.weapon.missiles.CommonArrow;
import com.annatala.pixelponies.items.weapon.missiles.CurareDart;
import com.annatala.pixelponies.items.weapon.missiles.Dart;
import com.annatala.pixelponies.items.weapon.missiles.FireArrow;
import com.annatala.pixelponies.items.weapon.missiles.FrostArrow;
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
import com.annatala.pixelponies.plants.Sorrowmoss;
import com.annatala.pixelponies.plants.Sungrass;
import com.annatala.utils.Random;

import java.util.HashMap;


public class ItemFactory {

	static private HashMap <String, Class<? extends Item>> mItemsList;

	static  {
		initItemsMap();
	}
	private static void registerItemClass(Class<? extends Item> itemClass) {
		mItemsList.put(itemClass.getSimpleName(), itemClass);
	}
	
	private static void initItemsMap() {

		mItemsList = new HashMap<>();

		registerItemClass(Amulet.class);
		registerItemClass(Ankh.class);
		registerItemClass(CrystallingKit.class);
		registerItemClass(Codex.class);
		registerItemClass(Dewdrop.class);
		registerItemClass(DewVial.class);
		registerItemClass(Gold.class);
		registerItemClass(LloydsBeacon.class);
		registerItemClass(Stylus.class);
		registerItemClass(TomeOfMastery.class);
		registerItemClass(Torch.class);
		registerItemClass(Weightstone.class);
		registerItemClass(ChangelingPie.class);
		registerItemClass(MysteriousHay.class);
		registerItemClass(FreezeDriedHay.class);
		registerItemClass(Ration.class);
		registerItemClass(ApplePie.class);
		registerItemClass(RottenHay.class);
		registerItemClass(RottenRation.class);
		registerItemClass(RottenPie.class);
		registerItemClass(HayFries.class);
		registerItemClass(OverpricedRation.class);
		registerItemClass(ScrollOfTerror.class);
		registerItemClass(BlankScroll.class);
		registerItemClass(ScrollOfMagicMapping.class);
		registerItemClass(ScrollOfRecharging.class);
		registerItemClass(ScrollOfLullaby.class);
		registerItemClass(ScrollOfCurse.class);
		registerItemClass(ScrollOfWeaponUpgrade.class);
		registerItemClass(ScrollOfIdentify.class);
		registerItemClass(ScrollOfUpgrade.class);
		registerItemClass(ScrollOfChallenge.class);
		registerItemClass(ScrollOfMirrorImage.class);
		registerItemClass(ScrollOfTeleportation.class);
		registerItemClass(ScrollOfDomination.class);
		registerItemClass(ScrollOfRemoveCurse.class);
		registerItemClass(ScrollOfPsionicBlast.class);
		registerItemClass(ScrollOfLoyalOath.class);
		registerItemClass(PotionOfLevitation.class);
		registerItemClass(PotionOfHonesty.class);
		registerItemClass(PotionOfMindVision.class);
		registerItemClass(PotionOfParalyticGas.class);
		registerItemClass(PotionOfToxicGas.class);
		registerItemClass(PotionOfHealing.class);
		registerItemClass(PotionOfPurification.class);
		registerItemClass(PotionOfLiquidFlame.class);
		registerItemClass(PotionOfFrost.class);
		registerItemClass(PotionOfInvisibility.class);
		registerItemClass(PotionOfExperience.class);
		registerItemClass(Dreamweed.Seed.class);
		registerItemClass(Sungrass.Seed.class);
		registerItemClass(Icecap.Seed.class);
		registerItemClass(WandMaker.Rotberry.Seed.class);
		registerItemClass(Firebloom.Seed.class);
		registerItemClass(Sorrowmoss.Seed.class);
		registerItemClass(Earthroot.Seed.class);
		registerItemClass(Fadeleaf.Seed.class);
		registerItemClass(RatSkull.class);
		registerItemClass(SpiderCharm.class);
		registerItemClass(RingOfDetection.class);
		registerItemClass(RingOfShadows.class);
		registerItemClass(RingOfHerbalism.class);
		registerItemClass(RingOfPower.class);
		registerItemClass(RingOfHaste.class);
		registerItemClass(RingOfSatiety.class);
		registerItemClass(RingOfEvasion.class);
		registerItemClass(RingOfAccuracy.class);
		registerItemClass(RingOfThorns.class);
		registerItemClass(RingOfHaggler.class);
		registerItemClass(RingOfElements.class);
		registerItemClass(RingOfMending.class);
		registerItemClass(RingOfStoneWalking.class);
		registerItemClass(CorpseDust.class);
		registerItemClass(RatKingCrown.class);
		registerItemClass(DriedRose.class);
		registerItemClass(WandOfRegrowth.class);
		registerItemClass(WandOfPoison.class);
		registerItemClass(WandOfLightning.class);
		registerItemClass(WandOfFirebolt.class);
		registerItemClass(WandOfAmok.class);
		registerItemClass(WandOfMagicMissile.class);
		registerItemClass(WandOfFlock.class);
		registerItemClass(WandOfDisintegration.class);
		registerItemClass(WandOfAvalanche.class);
		registerItemClass(WandOfSlowness.class);
		registerItemClass(WandOfBlink.class);
		registerItemClass(WandOfTelekinesis.class);
		registerItemClass(WandOfTeleportation.class);
		registerItemClass(Spear.class);
		registerItemClass(SacrificialSword.class);
		registerItemClass(Kusarigama.class);
		registerItemClass(Dagger.class);
		registerItemClass(Longsword.class);
		registerItemClass(WarHammer.class);
		registerItemClass(Mace.class);
		registerItemClass(Quarterstaff.class);
		registerItemClass(BattleAxe.class);
		registerItemClass(Glaive.class);
		registerItemClass(Sword.class);
		registerItemClass(Scythe.class);
		registerItemClass(SteelHorseshoes.class);
		registerItemClass(CompoundBow.class);
		registerItemClass(WoodenBow.class);
		registerItemClass(RubyBow.class);
		registerItemClass(Pickaxe.class);
		registerItemClass(IncendiaryDart.class);
		registerItemClass(Dart.class);
		registerItemClass(Boomerang.class);
		registerItemClass(Shuriken.class);
		registerItemClass(Tomahawk.class);
		registerItemClass(Javelin.class);
		registerItemClass(CurareDart.class);
		registerItemClass(FireArrow.class);
		registerItemClass(CommonArrow.class);
		registerItemClass(ParalysisArrow.class);
		registerItemClass(PoisonArrow.class);
		registerItemClass(FrostArrow.class);
		registerItemClass(PlateBarding.class);
		registerItemClass(ChainmailBarding.class);
		registerItemClass(ClothBarding.class);
		registerItemClass(ScaleBarding.class);
		registerItemClass(PleatherBarding.class);
		registerItemClass(HuntressBarding.class);
		registerItemClass(WardenBarding.class);
		registerItemClass(SniperBarding.class);
		registerItemClass(ShamanBarding.class);
		registerItemClass(ElfBarding.class);
		registerItemClass(ScoutBarding.class);
		registerItemClass(RogueBarding.class);
		registerItemClass(AssasinBarding.class);
		registerItemClass(FreeRunnerBarding.class);
		registerItemClass(MageBarding.class);
		registerItemClass(BattleMageBarding.class);
		registerItemClass(PotionBelt.class);
		registerItemClass(Keyring.class);
		registerItemClass(WandHolster.class);
		registerItemClass(SeedPouch.class);
		registerItemClass(Quiver.class);
		registerItemClass(ScrollHolder.class);
		registerItemClass(SkeletonKey.class);
		registerItemClass(GoldenKey.class);
		registerItemClass(IronKey.class);
		registerItemClass(DarkGold.class);
		registerItemClass(AncientBarding.class);
		registerItemClass(Claymore.class);
		registerItemClass(Halberd.class);
		registerItemClass(WoodenCrossbow.class);
		registerItemClass(CompositeCrossbow.class);
		registerItemClass(RubyCrossbow.class);
		registerItemClass(PotionOfMight.class);
		registerItemClass(HeartOfDarkness.class);
		registerItemClass(GoldenSword.class);
		registerItemClass(BlackSkull.class);
		registerItemClass(SpiderBarding.class);
	}
	
	public static Class<? extends Item> itemClassRandom() {
		return Random.element(mItemsList.values());
	}

	public static boolean isValidItemClass(String itemClass) {
		return mItemsList.containsKey(itemClass);
	}

	public static Item itemByName(String selectedItemClass) {
		try {
			return itemsClassByName(selectedItemClass).newInstance();
		} catch (InstantiationException e) {
			throw new TrackedRuntimeException("", e);
		} catch (IllegalAccessException e) {
			throw new TrackedRuntimeException("", e);
		}
	}


		public static Class<? extends Item> itemsClassByName(String selectedItemClass) {

		Class<? extends Item> itemClass = mItemsList.get(selectedItemClass);
		if(itemClass != null) {
			return itemClass;
		} else {
			Game.toast("Unknown iten: [%s], spawning Gold instead",selectedItemClass);
			return Gold.class;
		}
	}

}
