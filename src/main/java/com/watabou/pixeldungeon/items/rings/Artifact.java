package com.watabou.pixeldungeon.items.rings;

import com.nyrds.pixeldungeon.ml.R;
import com.watabou.noosa.Game;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.actors.hero.HeroSubClass;
import com.watabou.pixeldungeon.items.weapon.KindOfWeapon;
import com.watabou.pixeldungeon.ui.QuickSlot;
import com.watabou.pixeldungeon.utils.GLog;

import java.util.ArrayList;

public class Artifact extends KindOfWeapon {

	static final float TIME_TO_EQUIP = 1f;
	protected Buff buff;
	private boolean isRing;

	protected Artifact() {
		this.isRing = false;
	}

	protected Artifact(boolean isRing) {
		this.isRing = isRing;
	}

	@Override
	public boolean doEquip(Hero hero) {
		setCurUser(hero);

		detachAll(hero.belongings.backpack);

		if (hero.belongings.tail == null || hero.belongings.tail.doUnequip(hero, true, false)) {

			hero.belongings.tail = this;
			activate(hero);

			cursedKnown = true;
			if (cursed) {
				equipCursed(hero);
				GLog.n(Game.getVar(R.string.Ring_Cursed_Tail), this);
			}
			hero.updateLook();

			hero.spendAndNext(Artifact.TIME_TO_EQUIP);
			return true;

		} else {
			collect(hero.belongings.backpack);
			return false;
		}
	}

	public boolean doEquipMane(Hero hero) {
		setCurUser(hero);

		detachAll(hero.belongings.backpack);

		if (hero.belongings.mane == null || hero.belongings.mane.doUnequip(hero, true, false)) {

			hero.belongings.mane = this;
			activate(hero);

			cursedKnown = true;
			if (cursed) {
				equipCursed(hero);
				GLog.n(Game.getVar(R.string.Ring_Cursed_Mane), this);
			}
			hero.updateLook();
			hero.spendAndNext(Artifact.TIME_TO_EQUIP);

			return true;

		} else {
			collect(hero.belongings.backpack);
			return false;
		}
	}

	public boolean doEquipHorn(Hero hero) {
		setCurUser(hero);

		detachAll(hero.belongings.backpack);

		if (hero.belongings.weapon == null || hero.belongings.weapon.doUnequip(hero, true, false)) {

			hero.belongings.weapon = this;
			activate(hero);

			QuickSlot.refresh();

			cursedKnown = true;
			if (cursed) {
				equipCursed(hero);
				GLog.n(Game.getVar(R.string.Ring_Cursed_Horn), this);
			}
			hero.updateLook();

			hero.spendAndNext(Artifact.TIME_TO_EQUIP);
			return true;

		} else {
			collect(hero.belongings.backpack);
			return false;
		}
	}

	@Override
	public boolean doUnequip(Hero hero, boolean collect, boolean single) {
		if (super.doUnequip(hero, collect, single)) {

			if (hero.belongings.weapon == this) {
				hero.belongings.weapon = null;
			} else if (hero.belongings.mane == this) {
				hero.belongings.mane = null;
			} else if (hero.belongings.tail == this) {
				hero.belongings.tail = null;
			}

			hero.remove(buff);
			buff = null;

			return true;
		} else {
			return false;
		}
	}

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);

		if (!isEquipped(hero)) {

			// Remove EQUIP action created in KindOfWeapon and replace with specific equip actions
			actions.remove(AC_EQUIP);

			if (hero.heroClass.hasHorn()) {
				if (hero.subClass == HeroSubClass.PRINCESS && this.isRing) {
					actions.add(AC_EQUIP_HORN);
				}
			} else {
				actions.add(AC_EQUIP_MANE);
			}
			actions.add(AC_EQUIP_TAIL);
		}
		return actions;
	}

	@Override
	public boolean isEquipped(Hero hero) {
		return hero.belongings.weapon == this || hero.belongings.mane == this || hero.belongings.tail == this;
	}

	@Override
	public void activate(Hero hero) {
		buff = buff();
		if (buff != null) {
			buff.attachTo(hero);
		}
	}

	@Override
	public boolean isUpgradable() {
		return false;
	}

	protected ArtifactBuff buff() {
		return null;
	}

	public class ArtifactBuff extends Buff {
		@Override
		public boolean dontPack() {
			return true;
		}
	}

	public String getText() {
		return null;
	}

	public int getColor() {
		return 0;
	}
}
