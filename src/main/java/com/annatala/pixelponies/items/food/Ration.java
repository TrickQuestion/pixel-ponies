package com.annatala.pixelponies.items.food;

import com.annatala.pixelponies.actors.buffs.Hunger;
import com.annatala.pixelponies.items.Item;
import com.annatala.pixelponies.sprites.ItemSpriteSheet;

public class Ration extends Food {
	{
		image = ItemSpriteSheet.RATION;
		energy = Hunger.HUNGRY;
	}
	
	@Override
	public int price() {
		return 10 * quantity();
	}
	
	@Override
	public Item poison(int cell){
		return morphTo(RottenRation.class);
	}
}
