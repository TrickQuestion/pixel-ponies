/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
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

package com.watabou.pixeldungeon.actors.hero;

import android.support.annotation.NonNull;

import com.nyrds.pixeldungeon.items.guts.weapon.melee.Claymore;
import com.nyrds.pixeldungeon.ml.BuildConfig;
import com.nyrds.pixeldungeon.ml.R;
import com.watabou.noosa.Game;
import com.watabou.pixeldungeon.Badges;
import com.watabou.pixeldungeon.items.TomeOfMastery;
import com.watabou.pixeldungeon.items.barding.ClassBarding;
import com.watabou.pixeldungeon.items.barding.ClothBarding;
import com.watabou.pixeldungeon.items.food.Ration;
import com.watabou.pixeldungeon.items.potions.PotionOfHonesty;
import com.watabou.pixeldungeon.items.rings.RingOfHaste;
import com.watabou.pixeldungeon.items.rings.RingOfShadows;
import com.watabou.pixeldungeon.items.rings.RingOfThorns;
import com.watabou.pixeldungeon.items.scrolls.ScrollOfIdentify;
import com.watabou.pixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.watabou.pixeldungeon.items.wands.WandOfMagicMissile;
import com.watabou.pixeldungeon.items.weapon.melee.Dagger;
import com.watabou.pixeldungeon.items.weapon.melee.Knuckles;
import com.watabou.pixeldungeon.items.weapon.melee.Scythe;
import com.watabou.pixeldungeon.items.weapon.melee.Spear;
import com.watabou.pixeldungeon.items.weapon.melee.WoodenBow;
import com.watabou.pixeldungeon.items.weapon.missiles.Boomerang;
import com.watabou.pixeldungeon.items.weapon.missiles.CommonArrow;
import com.watabou.pixeldungeon.items.weapon.missiles.Dart;
import com.watabou.pixeldungeon.ui.QuickSlot;
import com.watabou.pixeldungeon.actors.Gender;
import com.watabou.utils.Bundle;

public enum HeroClass {

	EARTH_PONY(
			Game.getVar(R.string.HeroClass_EarthPony),
			Game.getVars(R.array.HeroClass_EarthPonyPerks),
			Gender.MASCULINE,
			false,
			false,
			new HeroSubClass[]
					{ HeroSubClass.FARMER, HeroSubClass.BARD, HeroSubClass.ROYAL_GUARD }
	),
	UNICORN(
			Game.getVar(R.string.HeroClass_Unicorn),
			Game.getVars(R.array.HeroClass_UnicornPerks),
			Gender.FEMININE,
			true,
			false,
			new HeroSubClass[]
					{ HeroSubClass.MAGICIAN, HeroSubClass.ARTIST, HeroSubClass.PRINCESS }
	),
	PEGASUS(
			Game.getVar(R.string.HeroClass_Pegasus),
			Game.getVars(R.array.HeroClass_PegasusPerks),
			Gender.FEMININE,
			false,
			true,
			new HeroSubClass[]
					{ HeroSubClass.SCOUT, HeroSubClass.BEASTMASTER, HeroSubClass.THUNDERBOLT }
	),
	ZEBRA(
			Game.getVar(R.string.HeroClass_Zebra),
			Game.getVars(R.array.HeroClass_ZebraPerks),
			Gender.MASCULINE,
			false,
			false,
			new HeroSubClass[]
					{ HeroSubClass.SHAMAN, HeroSubClass.SEER, HeroSubClass.ENCHANTER }
	),
	NIGHTWING(
			Game.getVar(R.string.HeroClass_Nightwing),
			Game.getVars(R.array.HeroClass_NightwingPerks),
			Gender.FEMININE,
			false,
			true,
			new HeroSubClass[]
					{ HeroSubClass.NOCTURNE, HeroSubClass.ASSASSIN, HeroSubClass.VAMPONY }
	);

	private final String title;
	private final String[] perks;
	private final Gender gender;
	private final boolean hasHorn;
	private final boolean hasWings;
	private HeroSubClass[] subClasses;

	HeroClass(String title, String[] perks, Gender gender, boolean hasHorn, boolean hasWings,
			  HeroSubClass[] subClasses) {
		this.title = title;
		this.perks = perks;
		this.gender = gender;
		this.hasHorn = hasHorn;
		this.hasWings = hasWings;
		this.subClasses = subClasses;
	}

	public void initHero(Hero hero) {
		hero.heroClass = this;
		hero.setGender(this.gender);
		initCommon(hero);

		switch (this) {

			case EARTH_PONY:
				initEarthPony(hero);
				break;

			case UNICORN:
				initUnicorn(hero);
				break;

			case PEGASUS:
				initPegasus(hero);
				break;

			case ZEBRA:
				initZebra(hero);
				break;

			case NIGHTWING:
				initNightwing(hero);
				break;


		}

		if (Badges.isUnlocked(masteryBadge()) && hero.getDifficulty() < 3) {
			new TomeOfMastery().collect(hero);
		}

		hero.updateAwareness();
	}

	private static void initDebug(Hero hero) {
		for(int i = 0;i<100;i++) {
			hero.collect(new ScrollOfMagicMapping());
		}

		Badges.validateBossSlain(Badges.Badge.BOSS_SLAIN_3);

		hero.collect(new TomeOfMastery());
		hero.collect(new Claymore().identify().upgrade(100));

		hero.collect(new RingOfShadows().degrade());
		hero.collect(new RingOfThorns());
		hero.collect(new RingOfHaste().upgrade().identify());
		hero.collect(new Spear().upgrade().identify());

		hero.ht(1000);
		hero.hp(1000);
		hero.attackSkill = 1000;
		//hero.defenseSkill = 1000;

	}

	private static void initCommon(Hero hero) {
		(hero.belongings.barding = new ClothBarding()).identify();
		hero.collect(new Ration());
		if(BuildConfig.DEBUG) initDebug(hero);
		QuickSlot.cleanStorage();
	}

	public Badges.Badge masteryBadge() {
		switch (this) {
		case EARTH_PONY:
			return Badges.Badge.MASTERY_EARTH;
		case UNICORN:
			return Badges.Badge.MASTERY_UNICORN;
		case PEGASUS:
			return Badges.Badge.MASTERY_PEGASUS;
		case ZEBRA:
			return Badges.Badge.MASTERY_ZEBRA;
		case NIGHTWING:
			return Badges.Badge.MASTERY_NIGHTWING;
		}
		return null;
	}

	private void initEarthPony(Hero hero) {
		hero.setHonesty(hero.honesty() + 1);
		hero.setLaughter(hero.laughter() + 1);
		hero.setMagic(hero.magic() - 1);
		hero.ht(hero.ht() + 5);
		hero.hp(hero.ht());

		(hero.belongings.weapon = new Scythe()).identify();

		hero.collect(new Dart(8));

		QuickSlot.selectItem(Dart.class, 0);

		new PotionOfHonesty().setKnown();
	}

	private void initUnicorn(Hero hero) {
		hero.setGenerosity(hero.generosity() + 1);
		hero.setMagic(hero.magic() + 1);
		hero.setLaughter(hero.laughter() - 1);

		(hero.belongings.weapon = new Knuckles()).identify();

		WandOfMagicMissile wand = new WandOfMagicMissile();
		hero.collect(wand.identify());

		QuickSlot.selectItem(wand, 0);

		new ScrollOfIdentify().setKnown();
	}

	private static void initPegasus(Hero hero) {

		// TODO: None of this is right.

		(hero.belongings.weapon = new Dagger()).identify();
		(hero.belongings.mane = new RingOfShadows()).upgrade().identify();

		hero.collect(new Dart(8).identify());

		hero.belongings.mane.activate(hero);

		QuickSlot.selectItem(Dart.class, 0);

		new ScrollOfMagicMapping().setKnown();
	}

	private static void initZebra(Hero hero) {

		// TODO: None of this is right.

		hero.ht(hero.ht() - 5);
		hero.hp(hero.ht());

		(hero.belongings.weapon = new Dagger()).identify();
		Boomerang boomerang = new Boomerang();
		hero.collect(boomerang.identify());

		QuickSlot.selectItem(boomerang, 0);
	}

	private void initNightwing(Hero hero) {

		// TODO: None of this is right.

		hero.setHonesty(hero.honesty() - 1);

		hero.ht(hero.ht() - 5);
		hero.hp(hero.ht());

		(hero.belongings.barding = new ClothBarding()).upgrade().identify();
		(hero.belongings.weapon = new WoodenBow()).upgrade().identify();

		hero.collect(new Dagger().upgrade().identify());
		hero.collect(new CommonArrow(20));

		QuickSlot.selectItem(CommonArrow.class, 0);
	}



	@Override
	public String toString() {
		return title;
	}

	@NonNull
	public String[] perks() {
		return perks;
	}

	public Gender gender() {
		return gender;
	}

	public boolean hasHorn() { return hasHorn; }

	public boolean hasWings() { return hasWings; }

	public HeroSubClass firstWay() { return this.subClasses[0]; }
	public HeroSubClass secondWay() { return this.subClasses[1]; }
	public HeroSubClass secretWay() { return this.subClasses[2]; }

	private static final String CLASS = "class";

	public void storeInBundle(Bundle bundle) {
		bundle.put(CLASS, title);
	}

	public static HeroClass restoreFromBundle(Bundle bundle) {
		String value = bundle.getString(CLASS);
		for (HeroClass heroClass : HeroClass.values()) {
			if (heroClass.title.equals(value)) {
				return heroClass;
			}
		}
		return null;
	}

	public ClassBarding classBarding() {
		return null;
	}


}
