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
package com.annatala.pixelponies.actors.hero;

import com.annatala.pixelponies.android.R;
import com.annatala.noosa.Game;
import com.annatala.pixelponies.Badges;
import com.annatala.pixelponies.Dungeon;
import com.annatala.pixelponies.items.utility.Amulet;
import com.annatala.pixelponies.items.Item;
import com.annatala.pixelponies.items.weapon.KindOfWeapon;
import com.annatala.pixelponies.items.barding.Barding;
import com.annatala.pixelponies.items.bags.Bag;
import com.annatala.pixelponies.items.keys.IronKey;
import com.annatala.pixelponies.items.keys.Key;
import com.annatala.pixelponies.items.rings.Artifact;
import com.annatala.pixelponies.items.scrolls.ScrollOfRemoveCurse;
import com.annatala.pixelponies.items.wands.Wand;
import com.annatala.utils.Bundle;
import com.annatala.utils.Random;

import java.util.Iterator;

public class Belongings implements Iterable<Item> {

	public static final int BACKPACK_SIZE	= 19;
	
	private Hero owner;
	
	public Bag backpack;	

	public KindOfWeapon weapon = null;
	public Barding barding = null;
	public Artifact mane = null;
	public Artifact tail = null;

	public Belongings( Hero owner ) {
		this.owner = owner;
		
		backpack = new Bag() {{
			name = Game.getVar(R.string.Belongings_Name);
			size = BACKPACK_SIZE;
		}};
		backpack.owner = owner;
	}
	
	private static final String WEAPON		= "weapon";
	private static final String BARDING = "barding";
	private static final String MANE		= "mane";
	private static final String TAIL		= "tail";
	
	public void storeInBundle( Bundle bundle ) {
		
		backpack.storeInBundle(bundle);
		
		bundle.put( WEAPON, weapon );
		bundle.put(BARDING, barding);
		bundle.put( MANE, mane );
		bundle.put( TAIL, tail );
	}
	
	public void restoreFromBundle( Bundle bundle ) {
		
		backpack.clear();
		backpack.restoreFromBundle(bundle);
		
		weapon = (KindOfWeapon)bundle.get( WEAPON );
		if (weapon != null) {
			weapon.activate( owner );
		}
		
		barding = (Barding)bundle.get( BARDING );
		
		mane = (Artifact)bundle.get( MANE );
		if (mane != null) {
			mane.activate( owner );
		}
		
		tail = (Artifact)bundle.get( TAIL );
		if (tail != null) {
			tail.activate( owner );
		}
	}
	
	@SuppressWarnings("unchecked")
	public<T extends Item> T getItem( Class<T> itemClass ) {

		for (Item item : this) {
			if (itemClass.isInstance( item )) {
				return (T)item;
			}
		}
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Key> T getKey( Class<T> kind, int depth ) {
		
		for (Item item : backpack) {
			if (item.getClass() == kind && ((Key)item).depth == depth) {
				return (T)item;
			}
		}
		
		return null;
	}
	
	public void countIronKeys() {
		
		IronKey.curDepthQuantity = 0;
		
		for (Item item : backpack) {
			if (item instanceof IronKey && ((IronKey)item).depth == Dungeon.depth) {
				IronKey.curDepthQuantity++;
			}
		}
	}
	
	public void identify() {
		for (Item item : this) {
			item.identify();
		}
	}
	
	public void observe() {
		if (weapon != null) {
			weapon.identify();
			Badges.validateItemLevelAquired( weapon );
		}
		if (barding != null) {
			barding.identify();
			Badges.validateItemLevelAquired(barding);
		}
		if (mane != null) {
			mane.identify();
			Badges.validateItemLevelAquired( mane );
		}
		if (tail != null) {
			tail.identify();
			Badges.validateItemLevelAquired( tail );
		}
		for (Item item : backpack) {
			item.cursedKnown = true;
		}
	}
	
	public void uncurseEquipped() {
		ScrollOfRemoveCurse.uncurse( owner, barding, weapon, mane, tail );
	}
	
	public Item randomUnequipped() {
		return Random.element( backpack.items );
	}

	public boolean removeItem(Item itemToRemove) {
		Iterator<Item> it = iterator();

		while(it.hasNext()) {
			Item item = it.next();
			if(item == itemToRemove) {
				it.remove();
				return true;
			}
		}
		return false;
	}

	public void resurrect( int depth ) {

		for (Item item : backpack.items.toArray(new Item[backpack.items.size()])) {
			if (item instanceof Key) {
				if (((Key) item).depth == depth) {
					item.detachAll(backpack);
				}
			} else if (item instanceof Amulet) {

			} else if (!item.isEquipped(owner)) {
				item.detachAll(backpack);
			}
		}
		
		if (weapon != null) {
			weapon.cursed = false;
			weapon.activate( owner );
		}
		
		if (barding != null) {
			barding.cursed = false;
		}
		
		if (mane != null) {
			mane.cursed = false;
			mane.activate( owner );
		}
		if (tail != null) {
			tail.cursed = false;
			tail.activate( owner );
		}
	}
	
	public int charge( boolean full) {
		
		int count = 0;
		
		for (Item item : this) {
			if (item instanceof Wand) {
				Wand wand = (Wand)item;
				if (wand.curCharges() < wand.maxCharges()) {
					wand.curCharges(full ? wand.maxCharges() : wand.curCharges() + 1);
					count++;
					
					wand.updateQuickslot();
				}
			}
		}
		
		return count;
	}
	
	public int discharge() {
		
		int count = 0;
		
		for (Item item : this) {
			if (item instanceof Wand) {
				Wand wand = (Wand)item;
				if (wand.curCharges() > 0) {
					wand.curCharges(wand.curCharges() - 1);
					count++;
					
					wand.updateQuickslot();
				}
			}
		}
		
		return count;
	}

	@Override
	public Iterator<Item> iterator() {
		return new ItemIterator(); 
	}

	private class ItemIterator implements Iterator<Item> {

		private int index = 0;
		
		private Iterator<Item> backpackIterator = backpack.iterator();
		
		private Item[] equipped = {weapon, barding, mane, tail};
		private int backpackIndex = equipped.length;
		
		@Override
		public boolean hasNext() {
			
			for (int i=index; i < backpackIndex; i++) {
				if (equipped[i] != null) {
					return true;
				}
			}
			
			return backpackIterator.hasNext();
		}

		@Override
		public Item next() {
			while (index < backpackIndex) {
				Item item = equipped[index++];
				if (item != null) {
					return item;
				}
			}
			index++;
			return backpackIterator.next();
		}

		@Override
		public void remove() {
			if(index == 0) {
				throw new IllegalStateException();
			}
			switch (index-1) {
			case 0:
				equipped[0] = weapon = null;
				break;
			case 1:
				equipped[1] = barding = null;
				break;
			case 2:
				equipped[2] = mane = null;
				break;
			case 3:
				equipped[3] = tail = null;
				break;
			default:
				backpackIterator.remove();
			}
		}
	}
}
