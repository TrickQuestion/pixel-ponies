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

import com.annatala.pixelponies.android.R;
import com.annatala.noosa.Game;
import com.annatala.pixelponies.actors.hero.Hero;
import com.annatala.pixelponies.actors.hero.HeroSubClass;
import com.annatala.utils.GLog;
import com.annatala.utils.Bundle;

import java.util.ArrayList;

abstract public class ClassBarding extends Barding {
	
	private static final String TXT_LOW_HEALTH   = Game.getVar(R.string.ClassBarding_LowHealth);
	private static final String TXT_NOT_EQUIPPED = Game.getVar(R.string.ClassBarding_NotEquipped);
	
	{
		levelKnown = true;
		cursedKnown = true;
		defaultAction = special();
	}
	
	public ClassBarding() {
		super( 6 );
	}
	
	public static Barding upgrade (Hero owner, Barding barding) {

		ClassBarding classBarding;
		if(owner.subClass == HeroSubClass.NONE) {
			classBarding = owner.heroClass.classBarding();
		} else {
			classBarding = owner.subClass.classBarding();
		}

		classBarding.minHonesty = barding.minHonesty;
		classBarding.resistance = barding.resistance;
		
		classBarding.inscribe( barding.glyph );
		
		return classBarding;
	}
	
	private static final String HONESTY = "minHonesty";
	private static final String RESISTANCE = "resistance";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put(HONESTY, minHonesty);
		bundle.put(RESISTANCE, resistance);
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		minHonesty = bundle.getInt(HONESTY);
		resistance = bundle.getInt(RESISTANCE);
	}
	
	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		if (hero.hp() >= 3 && isEquipped( hero )) {
			actions.add( special() );
		}
		return actions;
	}
	
	@Override
	public void execute( Hero hero, String action ) {
		if (action.equals(special())) {
			
			if (hero.hp() < 3) {
				GLog.w( TXT_LOW_HEALTH );
			} else if (!isEquipped( hero )) {
				GLog.w( TXT_NOT_EQUIPPED );
			} else {
				setCurUser(hero);
				doSpecial();
			}
			
		} else {	
			super.execute( hero, action );		
		}
	}
	
	abstract public String special();
	abstract public void doSpecial();
	
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
		return 0;
	}
	
	@Override
	public String desc() {
		return Game.getVar(R.string.ClassBarding_Desc);
	}
}
