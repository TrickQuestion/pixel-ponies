package com.annatala.pixelponies.items.food;

import com.annatala.pixelponies.sprites.ItemSpriteSheet;

public class RottenRation extends RottenFood {
	public RottenRation() {
		image   = ItemSpriteSheet.ROTTEN_RATION;
	}
	
	@Override
	public Food purify() {
		return new Ration();
	}
}
