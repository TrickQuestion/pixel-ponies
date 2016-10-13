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

import com.annatala.noosa.Game;
import com.annatala.pixelponies.Dungeon;
import com.annatala.pixelponies.android.R;
import com.annatala.pixelponies.actors.Char;
import com.annatala.pixelponies.actors.buffs.Terror;
import com.annatala.pixelponies.actors.hero.Hero;
import com.annatala.pixelponies.items.Gold;
import com.annatala.pixelponies.items.Item;
import com.annatala.pixelponies.items.rings.RingOfHaggler;
import com.annatala.pixelponies.items.utility.TomeOfMastery;
import com.annatala.pixelponies.sprites.CharSprite;
import com.annatala.pixelponies.sprites.ThiefSprite;
import com.annatala.utils.GLog;
import com.annatala.utils.Utils;
import com.annatala.utils.Bundle;
import com.annatala.utils.Random;

public class Thief extends Mob {

	protected static final String TXT_STOLE	  = Game.getVar(R.string.Thief_Stole);
	protected static final String TXT_CARRIES = Game.getVar(R.string.Thief_Carries);
	
	public Item item;
	
	{
		spriteClass = ThiefSprite.class;
		
		hp(ht(20));
		defenseSkill = 12;
		
		EXP = 5;
		maxLvl = 10;
		
		loot = RingOfHaggler.class;
		lootChance = 0.01f;
		
		FLEEING = new Fleeing();
	}
	
	private static final String ITEM = "item";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( ITEM, item );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		item = (Item)bundle.get( ITEM );
	}
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 1, 7 );
	}
	
	@Override
	protected float attackDelay() {
		return 0.5f;
	}
	
	@Override
	public void die( Object cause ) {

		super.die( cause );
		
		if (item != null) {
			Dungeon.level.drop( item, getPos() ).sprite.drop();
		}
	}
	
	@Override
	public int attackSkill( Char target ) {
		return 12;
	}
	
	@Override
	public int dr() {
		return 3;
	}
	
	@Override
	public int attackProc( Char enemy, int damage ) {
		if (item == null && enemy instanceof Hero && steal( (Hero)enemy )) {
			state = FLEEING;
		}
		
		return damage;
	}
	
	@Override
	public int defenseProc(Char enemy, int damage) {
		if (state == FLEEING) {
			Dungeon.level.drop( new Gold(), getPos() ).sprite.drop();
		}
		
		return damage;
	}
	
	protected boolean steal( Hero hero ) {
		
		Item item = hero.belongings.randomUnequipped();

		// Kludge: don't let the Tome of Mastery get stolen. I want it to be STUCK in inventory.
		if (item != null && !(item instanceof TomeOfMastery)) {
			
			GLog.w( TXT_STOLE, this.getName(), item.name() );
			
			item.detachAll( hero.belongings.backpack );
			this.item = item;
			
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public String description() {
		String desc = Game.getVar(R.string.Thief_Desc);
		
		if (item != null) {
			desc += Utils.format( TXT_CARRIES, Utils.capitalize( this.getName() ), item.name() );
		}
		
		return desc;
	}
	
	private class Fleeing extends Mob.Fleeing {
		@Override
		protected void nowhereToRun() {
			if (buff( Terror.class ) == null) {
				getSprite().showStatus( CharSprite.NEGATIVE, TXT_RAGE );
				state = HUNTING;
			} else {
				super.nowhereToRun();
			}
		}
	}
}
