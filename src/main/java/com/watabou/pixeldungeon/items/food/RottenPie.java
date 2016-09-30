package com.watabou.pixeldungeon.items.food;

import com.watabou.pixeldungeon.sprites.ItemSpriteSheet;

public class RottenPie extends RottenFood {
	public RottenPie() {
		image   = ItemSpriteSheet.ROTTEN_PIE;
	}
	
	@Override
	public Food purify() {
		return new ApplePie();
	}
}
