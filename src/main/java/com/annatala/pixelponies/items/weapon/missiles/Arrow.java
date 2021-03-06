package com.annatala.pixelponies.items.weapon.missiles;

import com.annatala.pixelponies.android.R;
import com.annatala.noosa.Game;
import com.annatala.pixelponies.actors.Char;
import com.annatala.pixelponies.actors.hero.Hero;
import com.annatala.pixelponies.actors.hero.HeroClass;
import com.annatala.pixelponies.items.Item;
import com.annatala.pixelponies.items.weapon.melee.Bow;
import com.annatala.pixelponies.scenes.GameScene;
import com.annatala.pixelponies.ui.QuickSlot;
import com.annatala.utils.GLog;
import com.annatala.utils.Random;

import java.util.ArrayList;

public abstract class Arrow extends MissileWeapon {

	
	protected final int COMMON_ARROW_IMAGE    = 0;
	protected final int FIRE_ARROW_IMAGE      = 1;
	protected final int POISON_ARROW_IMAGE    = 2;
	protected final int PARALYSIS_ARROW_IMAGE = 3;
	protected final int FROST_ARROW_IMAGE     = 4;

	protected double baseAcu = 1;
	protected double baseDly = 1;
	protected double baseMax = 1;
	protected double baseMin = 1;

	protected Bow firedFrom;

	public Arrow() {
		this(1);
	}

	public Arrow(int number) {
		super();
		minAttribute = 0;
		quantity(number);
		defaultAction = Game.getVar(R.string.Arrow_ACShoot);
	}

	protected void updateStatsForInfo() {
		MAX = (int) baseMax;
		MIN = (int) baseMin;
		ACU = (float) baseAcu;
		DLY = (float) baseDly;
	}

	protected boolean activateSpecial(Char attacker, Char defender, int damage) {
		if (firedFrom != null) {
			return true;
		}

		return Random.Float(1f) < 0.25f;
	}

	@Override
	public void execute(Hero hero, String action) {
		super.execute(hero, action);

		setCurUser(hero);
		curItem = this;
		if (action.equals(Game.getVar(R.string.Arrow_ACShoot))) {
			if (hero.bowEquipped()) {
				if (!hero.isFlanked() || hero.heroClass == HeroClass.NIGHTWING) {
					GameScene.selectCell(shooter);
				} else {
					GLog.w(Game.getVar(R.string.Arrow_Flanked));
					QuickSlot.cancel();
				}
			} else {
				GameScene.selectCell(thrower);
			}
		}
	}

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions( hero );
		if (hero.belongings.weapon instanceof Bow) {
			actions.add(Game.getVar(R.string.Arrow_ACShoot));
		} else {
			actions.remove(Game.getVar(R.string.Arrow_ACShoot));
		}

		return actions;
	}

	@Override
	public Item random() {
		int newQuantity = Random.Int(15, 30);

		// More arrows for the lucky.
		if (Random.luckBonus()) newQuantity = newQuantity * 12 / 10;
		if (Random.luckBonus()) newQuantity = newQuantity * 12 / 10;
		if (Random.luckBonus()) newQuantity = newQuantity * 12 / 10;

		quantity(newQuantity);
		return this;
	}

	public void onFire(int cell) {

		firedFrom = (Bow) getCurUser().belongings.weapon;

		MAX = (int) (baseMax * firedFrom.dmgFactor());
		MIN = (int) (baseMin * firedFrom.dmgFactor());
		ACU = (float) (baseAcu * firedFrom.acuFactor());
		DLY = (float) (baseDly * firedFrom.dlyFactor());

		float sDelta = getCurUser().effectiveLoyalty() - firedFrom.minAttribute();

		if (sDelta < 0) {
			DLY += sDelta * 0.5;
			ACU -= sDelta * 0.1;
		} else if (sDelta > 2) {
			MAX += MIN;
		}

		if (getCurUser().heroClass == HeroClass.NIGHTWING) {
			ACU *= 1.1;
			DLY *= 0.9;
		}

		firedFrom.usedForHit();
		firedFrom.useArrowType(this);

		super.onThrow(cell);

	}

	@Override
	protected void onThrow(int cell) {
		miss(cell);
	}

	@Override
	public Item burn(int cell) {
		return null;
	}
	
	@Override
	public String imageFile() {
		return "items/arrows.png";
	}

	@Override
	public String info() {return Game.getVar(R.string.Arrow_Info) + desc(); }
}
