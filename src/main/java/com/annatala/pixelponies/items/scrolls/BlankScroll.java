package com.annatala.pixelponies.items.scrolls;

import com.annatala.pixelponies.android.R;
import com.annatala.noosa.Game;
import com.annatala.pixelponies.items.Item;
import com.annatala.pixelponies.sprites.ItemSpriteSheet;
import com.annatala.utils.GLog;

public class BlankScroll extends Scroll {
	{
		image = ItemSpriteSheet.SCROLL_BLANK;
		stackable = true;
	}
	
	@Override
	public Item burn(int cell){
		return null;
	}
	
	@Override
	public boolean isUpgradable() {
		return false;
	}
	
	@Override
	public boolean isIdentified() {
		return true;
	}
	
	@Override
	public int price() {
		return 10 * quantity();
	}

	@Override
	protected void doRead() {
		curItem.collect( getCurUser().belongings.backpack );
		getCurUser().spendAndNext( TIME_TO_READ );
		
		GLog.i(Game.getVar(R.string.BlankScroll_ReallyBlank));
	}
	
	@Override
	public String name() {
		return name; 
	}
	
	@Override
	public String info() {
		return desc();
	}
}
