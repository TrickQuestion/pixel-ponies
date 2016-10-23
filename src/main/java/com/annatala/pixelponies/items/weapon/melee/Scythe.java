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
package com.annatala.pixelponies.items.weapon.melee;

import com.annatala.pixelponies.android.R;
import com.annatala.noosa.Game;
import com.annatala.noosa.audio.Sample;
import com.annatala.pixelponies.Assets;
import com.annatala.pixelponies.Badges;
import com.annatala.pixelponies.actors.hero.Hero;
import com.annatala.pixelponies.items.Item;
import com.annatala.pixelponies.items.scrolls.ScrollOfUpgrade;
import com.annatala.pixelponies.items.weapon.missiles.Boomerang;
import com.annatala.pixelponies.scenes.GameScene;
import com.annatala.pixelponies.sprites.ItemSpriteSheet;
import com.annatala.utils.GLog;
import com.annatala.pixelponies.windows.WndBag;

import java.util.ArrayList;

public class Scythe extends MeleeWeapon {

	public static final String AC_REFORGE = Game.getVar(R.string.Scythe_ACReforge);
	
	private static final String TXT_SELECT_WEAPON = Game.getVar(R.string.Scythe_Select);
	private static final String TXT_REFORGED      = Game.getVar(R.string.Scythe_Reforged);
	private static final String TXT_NOT_BOOMERANG = Game.getVar(R.string.Scythe_NotBoomerang);
	
	private static final float TIME_TO_REFORGE	= 2f;
	
	private boolean  equipped;
	{
		image = ItemSpriteSheet.SHORT_SWORD;
	}
	
	public Scythe() {
		super( 1, 1f, 1f );
		
		minAttribute = 4;
		MAX = 12;
	}
	
	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		if (level() > 0) {
			actions.add( AC_REFORGE );
		}
		return actions;
	}
	
	@Override
	public void execute( Hero hero, String action ) {
		if (action.equals(AC_REFORGE)) {
			
			if (hero.belongings.weapon == this) {
				equipped = true;
				hero.belongings.weapon = null;
			} else {
				equipped = false;
				detach( hero.belongings.backpack );
			}
			
			setCurUser(hero);
			
			GameScene.selectItem( itemSelector, WndBag.Mode.WEAPON, TXT_SELECT_WEAPON );
			
		} else {
			
			super.execute( hero, action );
			
		}
	}
	
	@Override
	public String desc() {
		return Game.getVar(R.string.Scythe_Info);
	}
	
	private final WndBag.Listener itemSelector = new WndBag.Listener() {
		@Override
		public void onSelect( Item item ) {
			if (item != null && !(item instanceof Boomerang) && !(item instanceof RubberChicken)) {
				
				Sample.INSTANCE.play( Assets.SND_EVOKE );
				ScrollOfUpgrade.upgrade( getCurUser() );
				evoke( getCurUser() );
				
				GLog.w( TXT_REFORGED, item.name() );
				
				((MeleeWeapon)item).safeUpgrade();
				getCurUser().spendAndNext( TIME_TO_REFORGE );
				
				Badges.validateItemLevelAquired( item );
				
			} else {
				
				if (item instanceof Boomerang) {
					GLog.w( TXT_NOT_BOOMERANG );
				}
				
				if (equipped) {
					getCurUser().belongings.weapon = Scythe.this;
				} else {
					collect( getCurUser().belongings.backpack );
				}
			}
		}
	};
}
