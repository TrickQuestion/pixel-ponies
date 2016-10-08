package com.nyrds.pixeldungeon.items.common;

import com.nyrds.android.util.TrackedRuntimeException;
import com.nyrds.pixeldungeon.items.chaos.ChaosBarding;
import com.nyrds.pixeldungeon.items.chaos.ChaosBow;
import com.nyrds.pixeldungeon.items.chaos.ChaosCrystal;
import com.nyrds.pixeldungeon.items.chaos.ChaosStaff;
import com.nyrds.pixeldungeon.items.chaos.ChaosSword;
import com.nyrds.pixeldungeon.items.common.barding.SpiderBarding;
import com.nyrds.pixeldungeon.items.guts.HeartOfDarkness;
import com.watabou.pixeldungeon.items.utility.CrystallingKit;
import com.watabou.pixeldungeon.items.barding.AncientBarding;
import com.nyrds.pixeldungeon.items.guts.weapon.melee.Claymore;
import com.nyrds.pixeldungeon.items.guts.weapon.melee.Halberd;
import com.nyrds.pixeldungeon.items.guts.weapon.ranged.CompositeCrossbow;
import com.nyrds.pixeldungeon.items.guts.weapon.ranged.RubyCrossbow;
import com.nyrds.pixeldungeon.items.guts.weapon.ranged.WoodenCrossbow;
import com.nyrds.pixeldungeon.items.necropolis.BlackSkull;
import com.nyrds.pixeldungeon.items.necropolis.BladeOfSouls;
import com.nyrds.pixeldungeon.items.necropolis.SoulShard;
import com.watabou.noosa.Game;
import com.watabou.pixeldungeon.actors.mobs.npcs.WandMaker;
import com.watabou.pixeldungeon.items.utility.Amulet;
import com.watabou.pixeldungeon.items.utility.Ankh;
import com.watabou.pixeldungeon.items.utility.Codex;
import com.watabou.pixeldungeon.items.utility.DewVial;
import com.watabou.pixeldungeon.plants.Dewdrop;
import com.watabou.pixeldungeon.items.Gold;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.utility.LloydsBeacon;
import com.watabou.pixeldungeon.items.utility.SpiderCharm;
import com.watabou.pixeldungeon.items.utility.Stylus;
import com.watabou.pixeldungeon.items.utility.TomeOfMastery;
import com.watabou.pixeldungeon.items.utility.Torch;
import com.watabou.pixeldungeon.items.utility.Weightstone;
import com.watabou.pixeldungeon.items.barding.AssasinBarding;
import com.watabou.pixeldungeon.items.barding.BattleMageBarding;
import com.watabou.pixeldungeon.items.barding.ClothBarding;
import com.watabou.pixeldungeon.items.barding.ElfBarding;
import com.watabou.pixeldungeon.items.barding.FreeRunnerBarding;
import com.watabou.pixeldungeon.items.barding.HuntressBarding;
import com.watabou.pixeldungeon.items.barding.PleatherBarding;
import com.watabou.pixeldungeon.items.barding.MageBarding;
import com.watabou.pixeldungeon.items.barding.ChainmailBarding;
import com.watabou.pixeldungeon.items.barding.PlateBarding;
import com.watabou.pixeldungeon.items.barding.RogueBarding;
import com.watabou.pixeldungeon.items.barding.ScaleBarding;
import com.watabou.pixeldungeon.items.barding.ScoutBarding;
import com.watabou.pixeldungeon.items.barding.ShamanBarding;
import com.watabou.pixeldungeon.items.barding.SniperBarding;
import com.watabou.pixeldungeon.items.barding.WardenBarding;
import com.watabou.pixeldungeon.items.bags.Keyring;
import com.watabou.pixeldungeon.items.bags.PotionBelt;
import com.watabou.pixeldungeon.items.bags.Quiver;
import com.watabou.pixeldungeon.items.bags.ScrollHolder;
import com.watabou.pixeldungeon.items.bags.SeedPouch;
import com.watabou.pixeldungeon.items.bags.WandHolster;
import com.watabou.pixeldungeon.items.food.FreezeDriedHay;
import com.watabou.pixeldungeon.items.food.HayFries;
import com.watabou.pixeldungeon.items.food.MysteriousHay;
import com.watabou.pixeldungeon.items.food.OverpricedRation;
import com.watabou.pixeldungeon.items.food.ApplePie;
import com.watabou.pixeldungeon.items.food.ChangelingPie;
import com.watabou.pixeldungeon.items.food.Ration;
import com.watabou.pixeldungeon.items.food.RottenHay;
import com.watabou.pixeldungeon.items.food.RottenPie;
import com.watabou.pixeldungeon.items.food.RottenRation;
import com.watabou.pixeldungeon.items.keys.GoldenKey;
import com.watabou.pixeldungeon.items.keys.IronKey;
import com.watabou.pixeldungeon.items.keys.SkeletonKey;
import com.watabou.pixeldungeon.items.potions.PotionOfExperience;
import com.watabou.pixeldungeon.items.potions.PotionOfFrost;
import com.watabou.pixeldungeon.items.potions.PotionOfHealing;
import com.watabou.pixeldungeon.items.potions.PotionOfInvisibility;
import com.watabou.pixeldungeon.items.potions.PotionOfLevitation;
import com.watabou.pixeldungeon.items.potions.PotionOfLiquidFlame;
import com.watabou.pixeldungeon.items.potions.PotionOfMight;
import com.watabou.pixeldungeon.items.potions.PotionOfMindVision;
import com.watabou.pixeldungeon.items.potions.PotionOfParalyticGas;
import com.watabou.pixeldungeon.items.potions.PotionOfPurity;
import com.watabou.pixeldungeon.items.potions.PotionOfHonesty;
import com.watabou.pixeldungeon.items.potions.PotionOfToxicGas;
import com.watabou.pixeldungeon.items.quest.CorpseDust;
import com.watabou.pixeldungeon.items.quest.DarkGold;
import com.watabou.pixeldungeon.items.quest.DriedRose;
import com.watabou.pixeldungeon.items.quest.Pickaxe;
import com.watabou.pixeldungeon.items.quest.RatSkull;
import com.watabou.pixeldungeon.items.rings.RingOfAccuracy;
import com.watabou.pixeldungeon.items.rings.RingOfDetection;
import com.watabou.pixeldungeon.items.rings.RingOfElements;
import com.watabou.pixeldungeon.items.rings.RingOfEvasion;
import com.watabou.pixeldungeon.items.rings.RingOfHaggler;
import com.watabou.pixeldungeon.items.rings.RingOfHaste;
import com.watabou.pixeldungeon.items.rings.RingOfHerbalism;
import com.watabou.pixeldungeon.items.rings.RingOfMending;
import com.watabou.pixeldungeon.items.rings.RingOfPower;
import com.watabou.pixeldungeon.items.rings.RingOfSatiety;
import com.watabou.pixeldungeon.items.rings.RingOfShadows;
import com.watabou.pixeldungeon.items.rings.RingOfStoneWalking;
import com.watabou.pixeldungeon.items.rings.RingOfThorns;
import com.watabou.pixeldungeon.items.scrolls.BlankScroll;
import com.watabou.pixeldungeon.items.scrolls.ScrollOfChallenge;
import com.watabou.pixeldungeon.items.scrolls.ScrollOfCurse;
import com.watabou.pixeldungeon.items.scrolls.ScrollOfDomination;
import com.watabou.pixeldungeon.items.scrolls.ScrollOfIdentify;
import com.watabou.pixeldungeon.items.scrolls.ScrollOfLoyalOath;
import com.watabou.pixeldungeon.items.scrolls.ScrollOfLullaby;
import com.watabou.pixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.watabou.pixeldungeon.items.scrolls.ScrollOfMirrorImage;
import com.watabou.pixeldungeon.items.scrolls.ScrollOfPsionicBlast;
import com.watabou.pixeldungeon.items.scrolls.ScrollOfRecharging;
import com.watabou.pixeldungeon.items.scrolls.ScrollOfRemoveCurse;
import com.watabou.pixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.watabou.pixeldungeon.items.scrolls.ScrollOfTerror;
import com.watabou.pixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.watabou.pixeldungeon.items.scrolls.ScrollOfWeaponUpgrade;
import com.watabou.pixeldungeon.items.wands.WandOfAmok;
import com.watabou.pixeldungeon.items.wands.WandOfAvalanche;
import com.watabou.pixeldungeon.items.wands.WandOfBlink;
import com.watabou.pixeldungeon.items.wands.WandOfDisintegration;
import com.watabou.pixeldungeon.items.wands.WandOfFirebolt;
import com.watabou.pixeldungeon.items.wands.WandOfFlock;
import com.watabou.pixeldungeon.items.wands.WandOfLightning;
import com.watabou.pixeldungeon.items.wands.WandOfMagicMissile;
import com.watabou.pixeldungeon.items.wands.WandOfPoison;
import com.watabou.pixeldungeon.items.wands.WandOfRegrowth;
import com.watabou.pixeldungeon.items.wands.WandOfSlowness;
import com.watabou.pixeldungeon.items.wands.WandOfTelekinesis;
import com.watabou.pixeldungeon.items.wands.WandOfTeleportation;
import com.watabou.pixeldungeon.items.weapon.melee.BattleAxe;
import com.watabou.pixeldungeon.items.weapon.melee.CompoundBow;
import com.watabou.pixeldungeon.items.weapon.melee.Dagger;
import com.watabou.pixeldungeon.items.weapon.melee.Glaive;
import com.watabou.pixeldungeon.items.weapon.melee.Knuckles;
import com.watabou.pixeldungeon.items.weapon.melee.Kusarigama;
import com.watabou.pixeldungeon.items.weapon.melee.Longsword;
import com.watabou.pixeldungeon.items.weapon.melee.Mace;
import com.watabou.pixeldungeon.items.weapon.melee.Quarterstaff;
import com.watabou.pixeldungeon.items.weapon.melee.RubyBow;
import com.watabou.pixeldungeon.items.weapon.melee.Scythe;
import com.watabou.pixeldungeon.items.weapon.melee.Spear;
import com.watabou.pixeldungeon.items.weapon.melee.Sword;
import com.watabou.pixeldungeon.items.weapon.melee.WarHammer;
import com.watabou.pixeldungeon.items.weapon.melee.WoodenBow;
import com.watabou.pixeldungeon.items.weapon.missiles.Boomerang;
import com.watabou.pixeldungeon.items.weapon.missiles.CommonArrow;
import com.watabou.pixeldungeon.items.weapon.missiles.CurareDart;
import com.watabou.pixeldungeon.items.weapon.missiles.Dart;
import com.watabou.pixeldungeon.items.weapon.missiles.FireArrow;
import com.watabou.pixeldungeon.items.weapon.missiles.FrostArrow;
import com.watabou.pixeldungeon.items.weapon.missiles.IncendiaryDart;
import com.watabou.pixeldungeon.items.weapon.missiles.Javelin;
import com.watabou.pixeldungeon.items.weapon.missiles.ParalysisArrow;
import com.watabou.pixeldungeon.items.weapon.missiles.PoisonArrow;
import com.watabou.pixeldungeon.items.weapon.missiles.Shuriken;
import com.watabou.pixeldungeon.items.weapon.missiles.Tomahawk;
import com.watabou.pixeldungeon.plants.Dreamweed;
import com.watabou.pixeldungeon.plants.Earthroot;
import com.watabou.pixeldungeon.plants.Fadeleaf;
import com.watabou.pixeldungeon.plants.Firebloom;
import com.watabou.pixeldungeon.plants.Icecap;
import com.watabou.pixeldungeon.plants.Sorrowmoss;
import com.watabou.pixeldungeon.plants.Sungrass;
import com.watabou.utils.Random;

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
		registerItemClass(PotionOfPurity.class);
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
		registerItemClass(ChaosCrystal.class);
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
		registerItemClass(ChaosStaff.class);
		registerItemClass(ChaosSword.class);
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
		registerItemClass(Knuckles.class);
		registerItemClass(CompoundBow.class);
		registerItemClass(WoodenBow.class);
		registerItemClass(ChaosBow.class);
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
		registerItemClass(ChaosBarding.class);
		registerItemClass(PotionOfMight.class);
		registerItemClass(HeartOfDarkness.class);
		registerItemClass(GoldenSword.class);
		registerItemClass(BlackSkull.class);
		registerItemClass(SpiderBarding.class);
		registerItemClass(BladeOfSouls.class);
		registerItemClass(SoulShard.class);
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
