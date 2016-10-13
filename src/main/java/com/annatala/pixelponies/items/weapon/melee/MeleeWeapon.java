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
package com.annatala.pixelponies.items.weapon.melee;

import com.annatala.noosa.Game;
import com.annatala.pixelponies.Dungeon;
import com.annatala.pixelponies.android.R;
import com.annatala.pixelponies.items.Item;
import com.annatala.pixelponies.items.weapon.Weapon;
import com.annatala.utils.Utils;
import com.annatala.utils.Random;

public class MeleeWeapon extends Weapon {
	
	private int tier;
	
	public MeleeWeapon( int tier, float acu, float dly ) {
		this.tier = tier;
		
		ACU = acu;
		DLY = dly;
		
		minAttribute = typicalMinimum();
		
		MIN = min();
		MAX = max();
	}
	
	protected int min() {
		return tier;
	}
	
	protected int max() {
		return (int)((tier * tier - tier + 10) / ACU * DLY);
	}
	
	@Override
	public Item upgrade() {
		return upgrade( false );
	}
	
	public Item upgrade( boolean enchant ) {
		minAttribute--;
		MIN++;
		MAX += tier;
		
		return super.upgrade( enchant );
	}
	
	public Item safeUpgrade() {
		return upgrade( getEnchantment() != null );
	}
	
	@Override
	public Item degrade() {		
		minAttribute++;
		MIN--;
		MAX -= tier;
		return super.degrade();
	}
	
	public int typicalMinimum() {
		return 1 + tier * 2;
	}
	
	@Override
	public String info() {
		final String p = "\n\n";
		
		StringBuilder info = new StringBuilder( desc() );

		String typical  = Game.getVar(R.string.MeleeWeapon_AWord);
		String upgraded = Game.getVar(R.string.MeleeWeapon_UpgradedType);
		String degraded = Game.getVar(R.string.MeleeWeapon_DegradedType);
		String quality = levelKnown && level() != 0 ? (level() > 0 ? upgraded : degraded) : typical;
		info.append(p);
		info.append(Utils.capitalize(Utils.format(Game.getVar(R.string.MeleeWeapon_Info), name, quality, tier)));
		info.append(" ");
		
		if (levelKnown) {
			info.append(Utils.format(Game.getVar(R.string.MeleeWeapon_AverageDam), (MIN + (MAX - MIN) / 2)));
		} else {
			if (this instanceof Bow) {
				info.append(Utils.format(Game.getVar(R.string.MeleeWeapon_AverageDamAndLoyalty), (min() + (max() - min()) / 2), typicalMinimum()));
				if (typicalMinimum() > Dungeon.hero.effectiveLoyalty()) {
					info.append(" ").append(Game.getVar(R.string.MeleeWeapon_ProbablyInadequateLoyalty));
				}
			} else {
				info.append(Utils.format(Game.getVar(R.string.MeleeWeapon_AverageDamAndHonesty), (min() + (max() - min()) / 2), typicalMinimum()));
				if (typicalMinimum() > Dungeon.hero.effectiveHonesty()) {
					info.append(" ").append(Game.getVar(R.string.MeleeWeapon_ProbablyInadequateHonesty));
				}
			}
		}

		quality = "";
		info.append(" ");
		if (DLY != 1f) {
			quality += (DLY < 1f ? Game.getVar(R.string.MeleeWeapon_FastType) : Game.getVar(R.string.MeleeWeapon_SlowType));
			if (ACU != 1f) {
				quality += " ";
				if ((ACU > 1f) == (DLY < 1f)) {
					quality += Game.getVar(R.string.MeleeWeapon_AndWord);
				} else {
					quality += Game.getVar(R.string.MeleeWeapon_ButWord);
				}
				quality += " ";
				quality += ACU > 1f ? Game.getVar(R.string.MeleeWeapon_AccurateType) : Game.getVar(R.string.MeleeWeapon_InaccurateType);
			}
			info.append(Utils.format(Game.getVar(R.string.MeleeWeapon_RatherSomething), quality));
		} else if (ACU != 1f) {
			quality += ACU > 1f ? Game.getVar(R.string.MeleeWeapon_AccurateType) : Game.getVar(R.string.MeleeWeapon_InaccurateType);
			info.append(Utils.format(Game.getVar(R.string.MeleeWeapon_RatherSomething), quality));
		}

		info.append(" ");
		switch (imbue) {
		case SPEED:
			info.append(Game.getVar(R.string.MeleeWeapon_BalancedSpeed));
			break;
		case ACCURACY:
			info.append(Game.getVar(R.string.MeleeWeapon_BalancedAccuracy));
			break;
		case NONE:
		}

		info.append(" ");
		if (getEnchantment() != null) {
			info.append(Game.getVar(R.string.MeleeWeapon_IsEnchanted));
		}

		if (levelKnown && (isEquipped(Dungeon.hero) || Dungeon.hero.belongings.backpack.items.contains( this ))) {
			if (this instanceof Bow) {
				info.append(p);
				if (minAttribute > Dungeon.hero.effectiveLoyalty()) {
					info.append(Utils.format(Game.getVar(R.string.MeleeWeapon_InadequateLoyalty), name));
				}
			} else {
				if (minAttribute > Dungeon.hero.effectiveHonesty()) {
					info.append(p);
					info.append(Utils.format(Game.getVar(R.string.MeleeWeapon_InadequateHonesty), name));
				}
				if (minAttribute < Dungeon.hero.effectiveHonesty()) {
					info.append(p);
					info.append(Utils.format(Game.getVar(R.string.MeleeWeapon_ExcessHonesty), name));
				}
			}

		}
		
		if (isEquipped( Dungeon.hero )) {
			info.append(p);
			info.append(Utils.format(Game.getVar(R.string.MeleeWeapon_HoldReady), name, (cursed ? Game.getVar(R.string.MeleeWeapon_CursedGrip) : ".")) );
		} else {
			if (cursedKnown && cursed) {
				info.append(p);
				info.append(Utils.format(Game.getVar(R.string.MeleeWeapon_SenseCurse), name));
			}
		}
		
		return info.toString();
	}
	
	@Override
	public int price() {
		int price = 20 * (1 << (tier - 1));
		if (getEnchantment() != null) {
			price *= 1.5;
		}
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
	
	@Override
	public Item random() {
		super.random();

		// Make enchantments a tiny bit more likely for the lucky.
		int difficultyDrop = 0;
		if (Random.luckBonus()) difficultyDrop++;
		if (Random.luckBonus()) difficultyDrop++;
		if (Random.luckBonus()) difficultyDrop++;

		if (Random.Int( 10 + level() - difficultyDrop ) == 0) {
			enchant( Enchantment.random() );
		}
		
		return this;
	}
}
