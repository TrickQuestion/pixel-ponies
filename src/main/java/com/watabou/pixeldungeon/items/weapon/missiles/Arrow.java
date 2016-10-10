package com.watabou.pixeldungeon.items.weapon.missiles;

import com.nyrds.pixeldungeon.ml.R;
import com.watabou.noosa.Game;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.actors.hero.HeroClass;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.weapon.melee.Bow;
import com.watabou.utils.Random;

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
		defaultAction = Game.getVar(R.string.Arrow_ACFire);
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
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions( hero );
		if (hero.belongings.weapon instanceof Bow) {
			actions.add(Game.getVar(R.string.Arrow_ACFire));
		} else {
			actions.remove(Game.getVar(R.string.Arrow_ACFire));
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
		if (getCurUser().bowEquiped()) {

			if (Dungeon.level.adjacent(getCurUser().getPos(), cell)
					&& getCurUser().heroClass != HeroClass.NIGHTWING) {
				miss(cell);
				return;
			}

			firedFrom = (Bow) getCurUser().belongings.weapon;

			MAX = (int) (baseMax * firedFrom.dmgFactor());
			MIN = (int) (baseMin * firedFrom.dmgFactor());
			ACU = (float) (baseAcu * firedFrom.acuFactor());
			DLY = (float) (baseDly * firedFrom.dlyFactor());

			float sDelta = getCurUser().effectiveLoyalty() - firedFrom.minAttribute();

			if (sDelta < 0) {
				DLY += sDelta * 0.5;
				ACU -= sDelta * 0.1;
			}

			if (sDelta > 2) {
				MAX += MIN;
			}

			if (getCurUser().heroClass == HeroClass.NIGHTWING) {
				ACU *= 1.1;
				DLY *= 0.9;
			}

			firedFrom.usedForHit();
			firedFrom.useArrowType(this);

			super.onThrow(cell);

		// This should no longer be possible.
		} else {
			miss(cell);
		}
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
}
