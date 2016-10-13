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
package com.annatala.pixelponies.items.barding;

import java.util.HashMap;

import com.annatala.pixelponies.android.R;
import com.annatala.noosa.Game;
import com.annatala.pixelponies.Dungeon;
import com.annatala.pixelponies.actors.hero.Hero;
import com.annatala.pixelponies.actors.hero.HeroClass;
import com.annatala.pixelponies.actors.mobs.Mob;
import com.annatala.pixelponies.items.Item;
import com.annatala.pixelponies.items.weapon.missiles.Shuriken;
import com.annatala.pixelponies.sprites.MissileSprite;
import com.annatala.utils.GLog;
import com.annatala.utils.Callback;

public class HuntressBarding extends ClassBarding {
	
	private static final String TXT_NO_ENEMIES   = Game.getVar(R.string.HuntressBarding_NoEnemies);
	private static final String TXT_NOT_HUNTRESS = Game.getVar(R.string.HuntressBarding_NotHuntress);
	
	private static final String AC_SPECIAL = Game.getVar(R.string.HuntressBarding_ACSpecial);
	
	{
		image = 14;
		hasHelmet = true;
		coverHair = true;
	}
	
	private HashMap<Callback, Mob> targets = new HashMap<>();
	
	@Override
	public String special() {
		return AC_SPECIAL;
	}
	
	@Override
	public void doSpecial() {
		
		Item proto = new Shuriken();
		
		for (Mob mob : Dungeon.level.mobs) {
			if (Dungeon.level.fieldOfView[mob.getPos()]) {
				
				Callback callback = new Callback() {	
					@Override
					public void call() {
						getCurUser().attack( targets.get( this ) );
						targets.remove( this );
						if (targets.isEmpty()) {
							getCurUser().spendAndNext( getCurUser().attackDelay() );
						}
					}
				};
				
				((MissileSprite)getCurUser().getSprite().getParent().recycle( MissileSprite.class )).
					reset( getCurUser().getPos(), mob.getPos(), proto, null, callback );
				
				targets.put( callback, mob );
			}
		}
		
		if (targets.size() == 0) {
			GLog.w( TXT_NO_ENEMIES );
			return;
		}
		
		getCurUser().hp(getCurUser().hp() - (getCurUser().hp() / 3));
		
		getCurUser().getSprite().zap( getCurUser().getPos() );
		getCurUser().busy();
	}
	
	@Override
	public boolean doEquip( Hero hero ) {
		if (hero.heroClass == HeroClass.ZEBRA) {
			return super.doEquip( hero );
		} else {
			GLog.w( TXT_NOT_HUNTRESS );
			return false;
		}
	}
	
	@Override
	public String desc() {
		return Game.getVar(R.string.HuntressBarding_Desc);
	}
}