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
package com.watabou.pixeldungeon.actors.hero;

import com.nyrds.android.util.TrackedRuntimeException;
import com.nyrds.pixeldungeon.ml.R;
import com.watabou.noosa.Game;
import com.watabou.pixeldungeon.items.barding.HarlequinCostume;
import com.watabou.pixeldungeon.items.barding.ClassBarding;
import com.watabou.pixeldungeon.items.barding.RoyalBarding;
import com.watabou.pixeldungeon.items.barding.YokeAndPlantTeamVest;
import com.watabou.utils.Bundle;

public enum HeroSubClass {

	NONE(      	null, null, null ),
    FARMER(    	Game.getVar(R.string.HeroSubClass_NameFarmer), Game.getVar(R.string.HeroSubClass_DescFarmer), YokeAndPlantTeamVest.class),
	BARD(      	Game.getVar(R.string.HeroSubClass_NameBard), Game.getVar(R.string.HeroSubClass_DescBard), HarlequinCostume.class),
	ROYAL_GUARD(Game.getVar(R.string.HeroSubClass_NameRoyalGuard), Game.getVar(R.string.HeroSubClass_DescRoyalGuard), RoyalBarding.class),
	MAGICIAN(   Game.getVar(R.string.HeroSubClass_NameMagician),   Game.getVar(R.string.HeroSubClass_DescMagician), YokeAndPlantTeamVest.class),
	ARTIST(   	Game.getVar(R.string.HeroSubClass_NameArtist),   Game.getVar(R.string.HeroSubClass_DescArtist), YokeAndPlantTeamVest.class),
	PRINCESS(	Game.getVar(R.string.HeroSubClass_NamePrincess),   Game.getVar(R.string.HeroSubClass_DescPrincess), YokeAndPlantTeamVest.class),
	SCOUT(  	Game.getVar(R.string.HeroSubClass_NameScout),   Game.getVar(R.string.HeroSubClass_DescScout), YokeAndPlantTeamVest.class),
	BEASTMASTER(Game.getVar(R.string.HeroSubClass_NameBeastmaster),   Game.getVar(R.string.HeroSubClass_DescBeastmaster), YokeAndPlantTeamVest.class),
	THUNDERBOLT(Game.getVar(R.string.HeroSubClass_NameThunderbolt),   Game.getVar(R.string.HeroSubClass_DescThunderbost), YokeAndPlantTeamVest.class),
	SHAMAN(    	Game.getVar(R.string.HeroSubClass_NameShaman),   Game.getVar(R.string.HeroSubClass_DescShaman), YokeAndPlantTeamVest.class),
	SEER(     	Game.getVar(R.string.HeroSubClass_NameSeer),  Game.getVar(R.string.HeroSubClass_DescSeer), YokeAndPlantTeamVest.class),
	ENCHANTER(	Game.getVar(R.string.HeroSubClass_NameEnchanter), Game.getVar(R.string.HeroSubClass_DescEnchanter), YokeAndPlantTeamVest.class),
	NOCTURNE(	Game.getVar(R.string.HeroSubClass_NameNocturne), Game.getVar(R.string.HeroSubClass_DescNocturne), YokeAndPlantTeamVest.class),
	ASSASSIN(	Game.getVar(R.string.HeroSubClass_NameAssassin), Game.getVar(R.string.HeroSubClass_DescAssassin), YokeAndPlantTeamVest.class),
	VAMPONY(	Game.getVar(R.string.HeroSubClass_NameVampony), Game.getVar(R.string.HeroSubClass_DescVampony), YokeAndPlantTeamVest.class);

	private String title;
	private String desc;
	private Class<? extends ClassBarding> bardingClass;
	
	HeroSubClass(String title, String desc, Class<? extends ClassBarding> bardingClass) {
		this.title = title;
		this.desc  = desc;
		this.bardingClass = bardingClass;
	}
	
	public String toString() {
		return title;
	}
	
	public String desc() {
		return desc;
	}
	
	private static final String SUBCLASS = "subClass";
	
	public void storeInBundle( Bundle bundle ) {
		bundle.put( SUBCLASS, title );
	}
	
	public static HeroSubClass restoreFromBundle(Bundle bundle) {
        String value = bundle.getString(SUBCLASS);
        for (HeroSubClass subClass : HeroSubClass.values()) {
            if (subClass.title.equals(value)) {
                return subClass;
            }
        }
        return null;
	}

	public ClassBarding classBarding() {
		try {
			return bardingClass.newInstance();
		} catch (Exception e) {
			throw new TrackedRuntimeException(e);
		}
	}
}
