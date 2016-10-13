package com.annatala.pixelponies.items.food;

import com.annatala.pixelponies.sprites.ItemSpriteSheet;

public class RottenPie extends RottenFood {
	public RottenPie() {
		image   = ItemSpriteSheet.ROTTEN_PIE;
	}
	
	@Override
	public Food purify() {
		return new ApplePie();
	}
}
