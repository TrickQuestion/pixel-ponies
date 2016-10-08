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
package com.watabou.pixeldungeon.items.weapon.missiles;

import com.watabou.noosa.Game;
import com.nyrds.pixeldungeon.ml.R;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.Bleeding;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class Tomahawk extends MissileWeapon {

	public Tomahawk() {
		this( 1 );
	}
	
	public Tomahawk(int number ) {
		super();
		image = ItemSpriteSheet.TOMAHAWK;
		
		minAttribute = 10;
		
		MIN = 4;
		MAX = 20;
		
		quantity(number);
	}
	
	@Override
	public void proc( Char attacker, Char defender, int damage ) {
		super.proc( attacker, defender, damage );
		Buff.affect( defender, Bleeding.class ).set( damage );
	}	
	
	@Override
	public String desc() {
		return Game.getVar(R.string.Tomahawk_Info);
	}
	
	@Override
	public Item random() {
		int newQuantity = Random.Int( 3, 10 );
		if (Random.luckBonus()) newQuantity+= 2;
		if (Random.luckBonus()) newQuantity+= 2;
		if (Random.luckBonus()) newQuantity+= 2;

		quantity(newQuantity);
		return this;
	}
	
	@Override
	public int price() {
		return 20 * quantity();
	}
}
