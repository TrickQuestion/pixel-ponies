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
package com.annatala.pixelponies.actors.mobs;

import com.annatala.pixelponies.actors.Char;
import com.annatala.pixelponies.actors.buffs.Buff;
import com.annatala.pixelponies.actors.buffs.Burning;
import com.annatala.pixelponies.actors.buffs.Frost;
import com.annatala.pixelponies.effects.Speck;
import com.annatala.pixelponies.items.potions.PotionOfLiquidFlame;
import com.annatala.pixelponies.items.scrolls.ScrollOfPsionicBlast;
import com.annatala.pixelponies.items.wands.WandOfFirebolt;
import com.annatala.pixelponies.items.weapon.enchantments.Fire;
import com.annatala.pixelponies.sprites.ElementalSprite;
import com.annatala.utils.Random;

public class Elemental extends Mob {

	public Elemental() {
		spriteClass = ElementalSprite.class;
		
		hp(ht(65));
		defenseSkill = 20;
		
		EXP = 10;
		maxLvl = 20;
		
		flying = true;
		
		loot = new PotionOfLiquidFlame();
		lootChance = 0.1f;
		
		IMMUNITIES.add( Burning.class );
		IMMUNITIES.add( Fire.class );
		IMMUNITIES.add( WandOfFirebolt.class );
		IMMUNITIES.add( ScrollOfPsionicBlast.class );
	}
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 16, 20 );
	}
	
	@Override
	public int attackSkill( Char target ) {
		return 25;
	}
	
	@Override
	public int dr() {
		return 5;
	}
	
	@Override
	public int attackProc( Char enemy, int damage ) {
		if (Random.Int( 5 ) < 3 && !Random.luckBonus()) {
			Buff.affect( enemy, Burning.class ).reignite( enemy );
		}
		
		return damage;
	}
	
	@Override
	public void add( Buff buff ) {
		if (buff instanceof Burning) {
			if (hp() < ht()) {
				hp(hp() + 1);
				getSprite().emitter().burst( Speck.factory( Speck.HEALING ), 1 );
			}
		} else {
			if (buff instanceof Frost) {
				damage( Random.NormalIntRange( 1, ht() * 2 / 3 ), buff );
			}
			super.add( buff );
		}
	}	
}
