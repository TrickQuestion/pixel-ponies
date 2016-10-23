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
package com.annatala.pixelponies.actors.mobs.npcs;

import com.annatala.noosa.Game;
import com.annatala.pixelponies.Dungeon;
import com.annatala.pixelponies.android.R;
import com.annatala.pixelponies.actors.buffs.Buff;
import com.annatala.pixelponies.actors.hero.Hero;
import com.annatala.pixelponies.effects.CellEmitter;
import com.annatala.pixelponies.effects.particles.ElmoParticle;
import com.annatala.pixelponies.items.Heap;
import com.annatala.pixelponies.items.Item;
import com.annatala.pixelponies.scenes.GameScene;
import com.annatala.pixelponies.sprites.CharSprite;
import com.annatala.pixelponies.sprites.ShopkeeperSprite;
import com.annatala.pixelponies.windows.WndBag;
import com.annatala.pixelponies.windows.WndTradeItem;
import com.annatala.utils.GLog;

public class Shopkeeper extends NPC {

	{
		spriteClass = ShopkeeperSprite.class;
	}
	
	@Override
	protected boolean act() {
		
		throwItem();
		
		getSprite().turnTo( getPos(), Dungeon.hero.getPos() );
		spend( TICK );
		return true;
	}
	
	@Override
	public void damage( int dmg, Object src ) {
		flee();
	}
	
	@Override
	public void add( Buff buff ) {
		flee();
	}
	
	protected void flee() {
		for (Heap heap: Dungeon.level.allHeaps()) {
			if (heap.type == Heap.Type.FOR_SALE) {
				CellEmitter.get( heap.pos ).burst( ElmoParticle.FACTORY, 4 );
				heap.destroy();
			}
		}
		
		destroy();
		
		getSprite().killAndErase();
		CellEmitter.get( getPos() ).burst( ElmoParticle.FACTORY, 6 );

		Dungeon.hero.setLaughter(Dungeon.hero.laughter() + 1);
		Dungeon.hero.getSprite().showStatus(CharSprite.POSITIVE, Game.getVar(R.string.Shopkeeper_StaApply));
		GLog.p(Game.getVar(R.string.Shopkeeper_GoneApply));
		//Badges.validateLaughterAttained();

	}
	
	@Override
	public boolean reset() {
		return true;
	}
	
	public static WndBag sell() {
		return GameScene.selectItem( itemSelector, WndBag.Mode.FOR_SALE, Game.getVar(R.string.Shopkeeper_Sell));
	}
	
	private static WndBag.Listener itemSelector = new WndBag.Listener() {
		@Override
		public void onSelect( Item item ) {
			if (item != null) {
				WndBag parentWnd = sell();
				GameScene.show( new WndTradeItem( item, parentWnd ) );
			}
		}
	};

	@Override
	public boolean interact(final Hero hero) {
		sell();
		return true;
	}
}
