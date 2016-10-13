package com.annatala.pixelponies.actors.mobs.common;

import com.annatala.pixelponies.levels.Tools;
import com.annatala.pixelponies.android.R;
import com.annatala.noosa.Game;
import com.annatala.noosa.audio.Sample;
import com.annatala.pixelponies.Assets;
import com.annatala.pixelponies.Badges;
import com.annatala.pixelponies.Dungeon;
import com.annatala.pixelponies.actors.Actor;
import com.annatala.pixelponies.actors.Char;
import com.annatala.pixelponies.actors.blobs.Darkness;
import com.annatala.pixelponies.actors.blobs.Foliage;
import com.annatala.pixelponies.actors.mobs.Boss;
import com.annatala.pixelponies.actors.mobs.Mob;
import com.annatala.pixelponies.actors.mobs.Shadow;
import com.annatala.pixelponies.actors.mobs.Wraith;
import com.annatala.pixelponies.effects.MagicMissile;
import com.annatala.pixelponies.effects.Speck;
import com.annatala.pixelponies.items.scrolls.ScrollOfWeaponUpgrade;
import com.annatala.pixelponies.items.wands.WandOfBlink;
import com.annatala.pixelponies.levels.Terrain;
import com.annatala.pixelponies.mechanics.Ballistica;
import com.annatala.utils.Callback;
import com.annatala.utils.Random;

/**
 * Created by DeadDie on 13.02.2016
 */
public class ShadowLord extends Boss {

	private boolean levelCreated = false;
	private int cooldown = -1;

	private static final String TXT_INTRO = Game.getVar(R.string.ShadowLord_Intro);
	private static final String TXT_DENY = Game.getVar(R.string.ShadowLord_Death);

	public ShadowLord() {
		hp(ht(260));
		defenseSkill = 40;

		EXP = 60;

		lootChance = 0.5f;
		loot = new ScrollOfWeaponUpgrade();
	}

	@Override
	public boolean isAbsoluteWalker() {
		return true;
	}

	public void spawnShadow() {
		int cell = Dungeon.level.getSolidCellNextTo(getPos());

		if (cell != -1) {
			Mob mob = new Shadow();

			mob.state = mob.WANDERING;
			Dungeon.level.spawnMob(mob, 1);

			WandOfBlink.appear(mob, cell);
		}
	}

	public void spawnWraith() {
		for (int i = 0; i < 4; i++) {
			int cell = Dungeon.level.getEmptyCellNextTo(getPos());

			if (cell != -1) {
				Mob mob = new Wraith();

				mob.state = mob.WANDERING;
				Dungeon.level.spawnMob(mob, 1);
				WandOfBlink.appear(mob, cell);
			}
		}
	}

	public void twistLevel() {

		if(!isAlive()) {
			return;
		}

		if(!levelCreated)
		{
			Tools.makeEmptyLevel(Dungeon.level);
			Tools.buildShadowLordMaze(Dungeon.level, 6);
			levelCreated = true;
		}

		int cell = Dungeon.level.getRandomTerrainCell(Terrain.PEDESTAL);
		if (Dungeon.level.cellValid(cell)) {
			if (Actor.findChar(cell) == null) {
				Mob mob = Crystal.makeShadowLordCrystal();
				Dungeon.level.spawnMob(mob);
				WandOfBlink.appear(mob, cell);

				int x, y;
				x = Dungeon.level.cellX(cell);
				y = Dungeon.level.cellY(cell);

				Dungeon.level.fillAreaWith(Darkness.class, x - 2, y - 2, 5, 5, 1);
			} else {
				damage(ht() / 9, this);
			}
		}
	}

	@Override
	protected boolean canAttack(Char enemy) {
		return Dungeon.level.distance(getPos(), enemy.getPos()) < 4 && Ballistica.cast(getPos(), enemy.getPos(), false, true) == enemy.getPos();
	}

	@Override
	protected boolean doAttack(Char enemy) {

		if (Dungeon.level.distance(getPos(), enemy.getPos()) <= 1) {
			return super.doAttack(enemy);
		} else {

			getSprite().zap(enemy.getPos());

			spend(1);

			if (hit(this, enemy, true)) {
				enemy.damage(damageRoll(), this);
			}
			return true;
		}
	}

	private void blink(int epos) {

		if (Dungeon.level.distance(getPos(), epos) == 1) {
			int y = Dungeon.level.cellX(getPos());
			int x = Dungeon.level.cellY(getPos());

			int ey = Dungeon.level.cellX(epos);
			int ex = Dungeon.level.cellY(epos);

			int dx = x - ex;
			int dy = y - ey;

			x += 2 * dx;
			y += 2 * dy;

			final int tgt = Dungeon.level.cell(x, y);
			if (Dungeon.level.cellValid(tgt)) {
				final Char ch = this;
				fx(getPos(), new Callback() {
					@Override
					public void call() {
						WandOfBlink.appear(ch, tgt);
					}
				});
			}
		}
	}

	protected void fx(int cell, Callback callback) {
		MagicMissile.purpleLight(getSprite().getParent(), getPos(), cell, callback);
		Sample.INSTANCE.play(Assets.SND_ZAP);
		getSprite().setVisible(false);
	}

	@Override
	public void damage(int dmg, Object src) {
		super.damage(dmg, src);
		if (src != this) {
			if (dmg > 0 && cooldown < 0) {
				state = FLEEING;
				if (src instanceof Char) {
					blink(((Char) src).getPos());
				}
				twistLevel();
				cooldown = 10;
			}
		}
	}

	@Override
	protected boolean act() {
		if (state == FLEEING) {
			cooldown--;
			if (cooldown < 0) {
				state = WANDERING;
				if (Math.random() < 0.7) {
					spawnWraith();
				} else {
					spawnShadow();
				}

				yell(TXT_INTRO);
			}
		}

		if (Dungeon.level.blobAmoutAt(Darkness.class, getPos()) > 0 && hp() < ht()) {
			getSprite().emitter().burst(Speck.factory(Speck.HEALING), 1);
			hp(Math.min(hp() + (ht() - hp()) / 4, ht()));
		}

		if (Dungeon.level.blobAmoutAt(Foliage.class, getPos()) > 0) {
			getSprite().emitter().burst(Speck.factory(Speck.BONE), 1);
			damage(1, this);
		}

		return super.act();
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange(30, 40);
	}

	@Override
	public int attackSkill(Char target) {
		return 30;
	}

	@Override
	public int dr() {
		return 40;
	}

	@Override
	public void die(Object cause) {
		super.die(cause);
		yell(TXT_DENY);
		Tools.makeEmptyLevel(Dungeon.level);
		Badges.validateBossSlain(Badges.Badge.SHADOW_LORD_SLAIN);
		//Tools.tileSplosion(Dungeon.level, Terrain.EMPTY_DECO, getPos(), 3);
	}
}
