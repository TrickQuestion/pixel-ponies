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
package com.annatala.pixelponies.items.weapon.missiles;

import com.annatala.noosa.Game;
import com.annatala.pixelponies.android.R;
import com.annatala.pixelponies.actors.Char;
import com.annatala.pixelponies.actors.buffs.Buff;
import com.annatala.pixelponies.actors.buffs.Paralysis;
import com.annatala.pixelponies.items.Item;
import com.annatala.pixelponies.sprites.ItemSpriteSheet;
import com.annatala.utils.Random;

public class CurareDart extends MissileWeapon {

	public static final float DURATION	= 3f;

	public CurareDart() {
		this( 1 );
	}
	
	public CurareDart( int number ) {
		super();
		
		image = ItemSpriteSheet.CURARE_DART;
		
		minAttribute = 7;
		
		MIN = 1;
		MAX = 3;
		
		quantity(number);
	}
	
	@Override
	public void proc( Char attacker, Char defender, int damage ) {
		Buff.prolong( defender, Paralysis.class, DURATION );
		super.proc( attacker, defender, damage );
	}
	
	@Override
	public String desc() {
		return Game.getVar(R.string.CurareDart_Info);
	}
	
	@Override
	public Item random() {
		int newQuantity = Random.Int( 1, 4 );
		if (Random.luckBonus()) newQuantity++;
		if (Random.luckBonus()) newQuantity++;
		if (Random.luckBonus()) newQuantity++;

		quantity(newQuantity);
		return this;
	}
	
	@Override
	public int price() {
		return 12 * quantity();
	}
}
