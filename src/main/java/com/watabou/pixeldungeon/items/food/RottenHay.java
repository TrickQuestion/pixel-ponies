package com.watabou.pixeldungeon.items.food;

import com.watabou.pixeldungeon.sprites.ItemSpriteSheet;

public class RottenHay extends RottenFood {
	public RottenHay() {
		image   = ItemSpriteSheet.ROTTEN_HAY;
	}
	
	@Override
	public Food purify() {
		return new MysteriousHay();
	}
}
