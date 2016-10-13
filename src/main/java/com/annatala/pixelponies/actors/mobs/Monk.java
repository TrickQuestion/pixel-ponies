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

import com.annatala.pixelponies.android.R;
import com.annatala.noosa.Game;
import com.annatala.pixelponies.Dungeon;
import com.annatala.pixelponies.actors.Char;
import com.annatala.pixelponies.actors.buffs.Amok;
import com.annatala.pixelponies.actors.buffs.Terror;
import com.annatala.pixelponies.actors.hero.Hero;
import com.annatala.pixelponies.actors.mobs.npcs.Imp;
import com.annatala.pixelponies.items.weapon.KindOfWeapon;
import com.annatala.pixelponies.items.food.Ration;
import com.annatala.pixelponies.items.weapon.melee.SteelHorseshoes;
import com.annatala.pixelponies.sprites.MonkSprite;
import com.annatala.utils.GLog;
import com.annatala.utils.Random;

public class Monk extends Mob {

	public static final String TXT_DISARM = Game.getVar(R.string.Monk_Disarm);
	
	public Monk() {
		spriteClass = MonkSprite.class;
		
		hp(ht(70));
		defenseSkill = 30;
		
		EXP = 11;
		maxLvl = 21;
		
		loot = new Ration();
		lootChance = 0.153f;
		
		IMMUNITIES.add( Amok.class );
		IMMUNITIES.add( Terror.class );
	}
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 12, 16 );
	}
	
	@Override
	public int attackSkill( Char target ) {
		return 30;
	}
	
	@Override
	protected float attackDelay() {
		return 0.5f;
	}
	
	@Override
	public int dr() {
		return 2;
	}
	
	@Override
	public void die( Object cause ) {
		Imp.Quest.process( this );
		
		super.die( cause );
	}
	
	@Override
	public int attackProc( Char enemy, int damage ) {

		// This guy's now a lot harder if you have low laughter for luck.
		if (Random.Int( 4 ) == 0 && enemy == Dungeon.hero && !Random.luckBonus()) {
			
			Hero hero = Dungeon.hero;
			KindOfWeapon weapon = hero.belongings.weapon;
			
			if (weapon != null && !(weapon instanceof SteelHorseshoes) && !weapon.cursed) {
				hero.belongings.weapon = null;
				Dungeon.level.drop( weapon, hero.getPos() ).sprite.drop();
				GLog.w( TXT_DISARM, getName(), weapon.name() );
			}
		}
		
		return damage;
	}
}
