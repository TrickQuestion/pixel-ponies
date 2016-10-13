package com.annatala.pixelponies.items.bags;

import com.annatala.pixelponies.items.Item;
import com.annatala.pixelponies.items.potions.Potion;
import com.annatala.pixelponies.sprites.ItemSpriteSheet;

public class PotionBelt extends Bag {

	{
		image = ItemSpriteSheet.BELT;
		
		size = 12;
	}
	
	@Override
	public boolean grab( Item item ) {
		return item instanceof Potion;
	}
	
	@Override
	public int price() {
		return 50;
	}
	
}
