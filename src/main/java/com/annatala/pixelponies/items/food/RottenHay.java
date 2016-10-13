package com.annatala.pixelponies.items.food;

import com.annatala.pixelponies.sprites.ItemSpriteSheet;

public class RottenHay extends RottenFood {
	public RottenHay() {
		image   = ItemSpriteSheet.ROTTEN_HAY;
	}
	
	@Override
	public Food purify() {
		return new MysteriousHay();
	}
}
