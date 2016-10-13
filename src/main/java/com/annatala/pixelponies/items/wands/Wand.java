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
package com.annatala.pixelponies.items.wands;

import com.annatala.pixelponies.android.util.Scrambler;
import com.annatala.pixelponies.android.R;
import com.annatala.noosa.Game;
import com.annatala.noosa.audio.Sample;
import com.annatala.pixelponies.Assets;
import com.annatala.pixelponies.Badges;
import com.annatala.pixelponies.Dungeon;
import com.annatala.pixelponies.actors.Actor;
import com.annatala.pixelponies.actors.Char;
import com.annatala.pixelponies.actors.buffs.Buff;
import com.annatala.pixelponies.actors.buffs.Invisibility;
import com.annatala.pixelponies.actors.hero.Hero;
import com.annatala.pixelponies.actors.hero.HeroClass;
import com.annatala.pixelponies.actors.hero.HeroSubClass;
import com.annatala.pixelponies.effects.MagicMissile;
import com.annatala.pixelponies.items.Item;
import com.annatala.pixelponies.items.ItemStatusHandler;
import com.annatala.pixelponies.items.weapon.KindOfWeapon;
import com.annatala.pixelponies.items.bags.Bag;
import com.annatala.pixelponies.items.rings.RingOfPower.Power;
import com.annatala.pixelponies.mechanics.Ballistica;
import com.annatala.pixelponies.scenes.CellSelector;
import com.annatala.pixelponies.scenes.GameScene;
import com.annatala.pixelponies.sprites.ItemSpriteSheet;
import com.annatala.pixelponies.ui.QuickSlot;
import com.annatala.utils.GLog;
import com.annatala.utils.Utils;
import com.annatala.utils.Bundle;
import com.annatala.utils.Callback;
import com.annatala.utils.Random;

import java.util.ArrayList;

public abstract class Wand extends KindOfWeapon {

	public static final String AC_ZAP = Game.getVar(R.string.Wand_ACZap);

	private static final String TXT_WOOD = Game.getVar(R.string.Wand_Wood);
	private static final String TXT_DAMAGE = Game.getVar(R.string.Wand_Damage);
	private static final String TXT_WEAPON = Game.getVar(R.string.Wand_Weapon);

	private static final String TXT_FIZZLES = Game
			.getVar(R.string.Wand_Fizzles);
	private static final String TXT_SELF_TARGET = Game
			.getVar(R.string.Wand_SelfTarget);

	private static final float TIME_TO_ZAP = 1f;

	private int maxCharges = Scrambler.scramble(initialCharges());
	private int curCharges = Scrambler.scramble(maxCharges());
	
	protected Char wandUser;

	protected Charger charger;

	private boolean curChargeKnown = false;

	protected boolean hitChars = true;

	private static final Class<?>[] wands = { WandOfTeleportation.class,
			WandOfSlowness.class, WandOfFirebolt.class, WandOfPoison.class,
			WandOfRegrowth.class, WandOfBlink.class, WandOfLightning.class,
			WandOfAmok.class, WandOfTelekinesis.class, WandOfFlock.class,
			WandOfDisintegration.class, WandOfAvalanche.class };
	private static final String[] woods = Game.getVars(R.array.Wand_Wood_Types);
	private static final Integer[] images = { ItemSpriteSheet.WAND_HOLLY,
			ItemSpriteSheet.WAND_YEW, ItemSpriteSheet.WAND_EBONY,
			ItemSpriteSheet.WAND_CHERRY, ItemSpriteSheet.WAND_TEAK,
			ItemSpriteSheet.WAND_ROWAN, ItemSpriteSheet.WAND_WILLOW,
			ItemSpriteSheet.WAND_MAHOGANY, ItemSpriteSheet.WAND_BAMBOO,
			ItemSpriteSheet.WAND_PURPLEHEART, ItemSpriteSheet.WAND_OAK,
			ItemSpriteSheet.WAND_BIRCH };

	private static ItemStatusHandler<Wand> handler;

	private String wood;

	@SuppressWarnings("unchecked")
	public static void initWoods() {
		handler = new ItemStatusHandler<>((Class<? extends Wand>[]) wands,
				woods, images);
	}

	public static void save(Bundle bundle) {
		handler.save(bundle);
	}

	@SuppressWarnings("unchecked")
	public static void restore(Bundle bundle) {
		handler = new ItemStatusHandler<>((Class<? extends Wand>[]) wands,
				woods, images, bundle);
	}

	public Wand() {
		calculateDamage();

		defaultAction = AC_ZAP;
		
		try {
			image = handler.image(this);
			wood = handler.label(this);
		} catch (Exception e) {
			// Wand of Magic Missile
		}
		
	}

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		if (curCharges() > 0 || !curChargeKnown) {
			actions.add(AC_ZAP);
		}

		actions.remove(AC_EQUIP);
		actions.remove(AC_UNEQUIP);
		
		if (hero.heroClass == HeroClass.UNICORN
			|| hero.subClass == HeroSubClass.SHAMAN) {
			
			if(hero.belongings.weapon == this) {
				actions.add(AC_UNEQUIP); 
			} else {
				actions.add(AC_EQUIP);
			}
		}
		return actions;
	}

	@Override
	public boolean doUnequip(Hero hero, boolean collect) {
		onDetach();
		return super.doUnequip(hero, collect);
	}

	@Override
	public void activate(Hero hero) {
		charge(hero);
	}

	@Override
	public void execute(Hero hero, String action) {
		if (action.equals(AC_ZAP)) {

			setCurUser(hero);
			wandUser = hero;
			curItem = this;
			GameScene.selectCell(zapper);

			// Unicorns automatically identify wands by trying to fire them.
			if (Dungeon.hero.heroClass == HeroClass.UNICORN) {
				this.identify();
			}


		} else {

			super.execute(hero, action);

		}
	}

	public void zap(int cell) {
		onZap(cell);
	}
	
	protected abstract void onZap(int cell);

	@Override
	public boolean collect(Bag container) {
		if (super.collect(container)) {
			if (container.owner != null) {
				charge(container.owner);
			}
			return true;
		} else {
			return false;
		}
	}

	public void charge(Char owner) {
		(charger = new Charger()).attachTo(owner);
	}

	@Override
	public void onDetach() {
		stopCharging();
	}

	public void stopCharging() {
		if (charger != null) {
			charger.detach();
			charger = null;
		}
	}

	public int effectiveLevel() {

		// Let's pump wands final eL based on user's magic level, by a small margin.
		// Note that this can reduce a wand's power if your magic is awful.
		int magicBonus = 0;
		if (wandUser == Dungeon.hero && level() > 0) {
			magicBonus = (Dungeon.hero.effectiveMagic() / 5) - 1;
			if (Random.Int(5) < Dungeon.hero.effectiveMagic() % 5) {
				magicBonus++;
			}
		}

		if (charger != null) {
			Power power = charger.target.buff(Power.class);
			return power == null ? super.level() + magicBonus : Math.max(super.level() + power.level, 0) + magicBonus;
		} else {
			return super.level() + magicBonus;
		}
	}

	protected boolean isKnown() {
		return handler.isKnown(this);
	}

	public void setKnown() {
		if (!isKnown()) {
			handler.know(this);
		}

		Badges.validateAllWandsIdentified();
	}

	@Override
	public Item identify() {

		setKnown();
		curChargeKnown = true;
		super.identify();

		updateQuickslot();

		return this;
	}

	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder(super.toString());

		String status = status();
		if (status != null) {
			sb.append(" (" + status + ")");
		}

		return sb.toString();
	}

	@Override
	public String name() {
		return isKnown() ? name : Utils.format(
				Game.getVar(R.string.Wand_Name), wood);
	}

	@Override
	public String info() {
		StringBuilder info = new StringBuilder(isKnown() ? desc()
				: Utils.format(TXT_WOOD, wood));
		if (Dungeon.hero.heroClass == HeroClass.UNICORN) {
			if (levelKnown) {
				info.append("\n\n");
				info.append(Utils.format(TXT_DAMAGE, MIN + (MAX - MIN) / 2));
			}
		}
		return info.toString();
	}

	@Override
	public boolean isIdentified() {
		return super.isIdentified() && isKnown() && curChargeKnown;
	}

	@Override
	public String status() {
		if (levelKnown) {
			return (curChargeKnown ? curCharges() : "?") + "/" + maxCharges();
		} else {
			return null;
		}
	}

	@Override
	public Item upgrade() {

		super.upgrade();

		updateLevel();
		curCharges(Math.min(curCharges() + 1, maxCharges()));
		updateQuickslot();

		return this;
	}

	@Override
	public Item degrade() {
		super.degrade();

		updateLevel();
		updateQuickslot();

		return this;
	}

	protected void updateLevel() {
		maxCharges(Math.min(initialCharges() + level(), 9));
		curCharges(Math.min(curCharges(), maxCharges()));

		calculateDamage();
	}

	protected int initialCharges() {
		return 2;
	}

	private void calculateDamage() {
		int tier = 1 + effectiveLevel() / 3;
		MIN = tier;
		MAX = (tier * tier - tier + 10) / 2 + effectiveLevel();
	}

	public void mobWandUse(Char user, final int tgt) {
		wandUser = user;
		
		fx(tgt, new Callback() {
			@Override
			public void call() {
				onZap(tgt);
			}
		});
		
	}
	
	protected void fx(int cell, Callback callback) {
		MagicMissile.blueLight(wandUser.getSprite().getParent(), wandUser.getPos(), cell,
				callback);
		Sample.INSTANCE.play(Assets.SND_ZAP);
	}

	protected void wandUsed() {
		curCharges(curCharges() - 1);
		updateQuickslot();

		getCurUser().spendAndNext(TIME_TO_ZAP);
	}

	@Override
	public Item random() {

		// Changing this to be totally laugh-determined.
		if (Random.luckBonus()) {
			upgrade();
			if (Random.luckBonus() && Random.luckBonus()) {
				upgrade();
			}
		}

		return this;
	}

	public static boolean allKnown() {
		return handler.known().size() == wands.length;
	}

	@Override
	public int price() {
		int price = 50;
		if (cursed && cursedKnown) {
			price /= 2;
		}
		if (levelKnown) {
			if (level() > 0) {
				price *= (level() + 1);
			} else if (level() < 0) {
				price /= (1 - level());
			}
		}
		if (price < 1) {
			price = 1;
		}
		return price;
	}

	private static final String MAX_CHARGES = "maxCharges";
	private static final String CUR_CHARGES = "curCharges";
	private static final String CUR_CHARGE_KNOWN = "curChargeKnown";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(MAX_CHARGES, maxCharges());
		bundle.put(CUR_CHARGES, curCharges());
		bundle.put(CUR_CHARGE_KNOWN, curChargeKnown);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		maxCharges(bundle.getInt(MAX_CHARGES));
		curCharges(bundle.getInt(CUR_CHARGES));
		curChargeKnown = bundle.getBoolean(CUR_CHARGE_KNOWN);
	}

	protected void wandEffect(final int cell) {
		setKnown();

		QuickSlot.target(curItem, Actor.findChar(cell));

		if (curCharges() > 0) {

			getCurUser().busy();

			fx(cell, new Callback() {
				@Override
				public void call() {
					onZap(cell);
					wandUsed();
				}
			});

			Invisibility.dispel(getCurUser());
		} else {

			getCurUser().spendAndNext(TIME_TO_ZAP);
			GLog.w(TXT_FIZZLES);
			levelKnown = true;

			// Unicorns automatically identify wands by firing them.
			if (Dungeon.hero.heroClass == HeroClass.UNICORN) {
				this.identify();
			}

			updateQuickslot();
		}

	}

	protected static CellSelector.Listener zapper = new CellSelector.Listener() {

		@Override
		public void onSelect(Integer target) {

			if (target != null) {

				if (target == getCurUser().getPos()) {
					GLog.i(TXT_SELF_TARGET);
					return;
				}

				final Wand curWand = (Wand) Wand.curItem;
				
				final int cell = Ballistica.cast(getCurUser().getPos(), target, true, curWand.hitChars);
				getCurUser().getSprite().zap(cell);
				
				curWand.wandEffect(cell);
			}
		}

		@Override
		public String prompt() {
			return Game.getVar(R.string.Wand_Prompt);
		}
	};

	public int curCharges() {
		return Scrambler.descramble(curCharges);
	}

	public void curCharges(int curCharges) {
		this.curCharges = Scrambler.scramble(curCharges);
	}

	public int maxCharges() {
		return Scrambler.descramble(maxCharges);
	}

	public void maxCharges(int maxCharges) {
		this.maxCharges = Scrambler.scramble(maxCharges);
	}

	protected class Charger extends Buff {
		private static final float TIME_TO_CHARGE = 40f;

		@Override
		public boolean dontPack(){
			return true;
		}

		@Override
		public boolean attachTo(Char target) {
			super.attachTo(target);
			delay();

			return true;
		}

		@Override
		public boolean act() {

			if (curCharges() < maxCharges()) {
				curCharges(curCharges() + 1);
				updateQuickslot();
			}

			delay();

			return true;
		}

		protected void delay() {
			float time2charge = ((Hero) target).heroClass == HeroClass.UNICORN ? TIME_TO_CHARGE
					/ (float) Math.sqrt(1 + effectiveLevel())
					: TIME_TO_CHARGE;
			spend(time2charge);
		}
	}

	public boolean affectTarget() {
		return true;
	}
}
