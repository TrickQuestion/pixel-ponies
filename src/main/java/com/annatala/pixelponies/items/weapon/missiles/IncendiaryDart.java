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
import com.annatala.pixelponies.Dungeon;
import com.annatala.pixelponies.android.R;
import com.annatala.pixelponies.actors.Actor;
import com.annatala.pixelponies.actors.Char;
import com.annatala.pixelponies.actors.blobs.Blob;
import com.annatala.pixelponies.actors.blobs.Fire;
import com.annatala.pixelponies.actors.buffs.Buff;
import com.annatala.pixelponies.actors.buffs.Burning;
import com.annatala.pixelponies.items.Item;
import com.annatala.pixelponies.scenes.GameScene;
import com.annatala.pixelponies.sprites.ItemSpriteSheet;
import com.annatala.utils.Random;

public class IncendiaryDart extends MissileWeapon {

	public IncendiaryDart() {
		this( 1 );
	}
	
	public IncendiaryDart( int number ) {
		super();
		
		image = ItemSpriteSheet.INCENDIARY_DART;
		
		minAttribute = 5;
		
		MIN = 1;
		MAX = 2;
		
		quantity(number);
	}
	
	@Override
	protected void onThrow( int cell ) {
		Char enemy = Actor.findChar( cell );
		if (enemy == null || enemy == getCurUser()) {
			if (Dungeon.level.flammable[cell]) {
				GameScene.add( Blob.seed( cell, 4, Fire.class ) );
			} else {
				super.onThrow( cell );
			}
		} else {
			if (!getCurUser().shoot( enemy, this )) {
				Dungeon.level.drop( this, cell ).sprite.drop();
			}
		}
	}
	
	@Override
	public void proc( Char attacker, Char defender, int damage ) {
		Buff.affect( defender, Burning.class ).reignite( defender );
		super.proc( attacker, defender, damage );
	}
	
	@Override
	public String desc() {
		return Game.getVar(R.string.IncendiaryDart_Info);
	}
	
	@Override
	public Item random() {
		int newQuantity = Random.Int( 2, 5 );
		if (Random.luckBonus()) newQuantity++;
		if (Random.luckBonus()) newQuantity++;
		if (Random.luckBonus()) newQuantity++;
		quantity(newQuantity);
		return this;
	}
	
	@Override
	public int price() {
		return 10 * quantity();
	}
}
