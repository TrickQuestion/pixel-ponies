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
import com.watabou.pixeldungeon.items.armor.AssasinArmor;
import com.watabou.pixeldungeon.items.armor.BardArmor;
import com.watabou.pixeldungeon.items.armor.BattleMageArmor;
import com.watabou.pixeldungeon.items.armor.ClassArmor;
import com.watabou.pixeldungeon.items.armor.FarmerArmor;
import com.watabou.pixeldungeon.items.armor.FreeRunnerArmor;
import com.watabou.pixeldungeon.items.armor.ScoutArmor;
import com.watabou.pixeldungeon.items.armor.ShamanArmor;
import com.watabou.pixeldungeon.items.armor.SniperArmor;
import com.watabou.pixeldungeon.items.armor.WardenArmor;
import com.watabou.pixeldungeon.items.armor.WarlockArmor;
import com.watabou.utils.Bundle;

public enum HeroSubClass {

	NONE(      	null, null, null ),
    FARMER(    	Game.getVar(R.string.HeroSubClass_NameFarmer), Game.getVar(R.string.HeroSubClass_DescFarmer), FarmerArmor.class),
	BARD(      	Game.getVar(R.string.HeroSubClass_NameBard), Game.getVar(R.string.HeroSubClass_DescBard), BardArmor.class),
	ROYAL_GUARD(Game.getVar(R.string.HeroSubClass_NameRoyalGuard), Game.getVar(R.string.HeroSubClass_DescRoyalGuard), FarmerArmor.class),
	MAGICIAN(   Game.getVar(R.string.HeroSubClass_NameMagician),   Game.getVar(R.string.HeroSubClass_DescMagician), FarmerArmor.class),
	ARTIST(   	Game.getVar(R.string.HeroSubClass_NameArtist),   Game.getVar(R.string.HeroSubClass_DescArtist), FarmerArmor.class),
	PRINCESS(	Game.getVar(R.string.HeroSubClass_NamePrincess),   Game.getVar(R.string.HeroSubClass_DescPrincess), FarmerArmor.class),
	SCOUT(  	Game.getVar(R.string.HeroSubClass_NameScout),   Game.getVar(R.string.HeroSubClass_DescScout), FarmerArmor.class),
	BEASTMASTER(Game.getVar(R.string.HeroSubClass_NameBeastmaster),   Game.getVar(R.string.HeroSubClass_DescBeastmaster), FarmerArmor.class),
	THUNDERBOLT(Game.getVar(R.string.HeroSubClass_NameThunderbolt),   Game.getVar(R.string.HeroSubClass_DescThunderbost), FarmerArmor.class),
	SHAMAN(    	Game.getVar(R.string.HeroSubClass_NameShaman),   Game.getVar(R.string.HeroSubClass_DescShaman), FarmerArmor.class),
	SEER(     	Game.getVar(R.string.HeroSubClass_NameSeer),  Game.getVar(R.string.HeroSubClass_DescSeer), FarmerArmor.class),
	ENCHANTER(	Game.getVar(R.string.HeroSubClass_NameEnchanter), Game.getVar(R.string.HeroSubClass_DescEnchanter), FarmerArmor.class),
	NOCTURNE(	Game.getVar(R.string.HeroSubClass_NameNocturne), Game.getVar(R.string.HeroSubClass_DescNocturne), FarmerArmor.class),
	ASSASSIN(	Game.getVar(R.string.HeroSubClass_NameAssassin), Game.getVar(R.string.HeroSubClass_DescAssassin), FarmerArmor.class),
	VAMPONY(	Game.getVar(R.string.HeroSubClass_NameVampony), Game.getVar(R.string.HeroSubClass_DescVampony), FarmerArmor.class);

	private String title;
	private String desc;
	private Class<? extends ClassArmor> armorClass;
	
	HeroSubClass(String title, String desc, Class<? extends ClassArmor> armorClass) {
		this.title = title;
		this.desc  = desc;
		this.armorClass = armorClass;
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

	public ClassArmor classArmor() {
		try {
			return armorClass.newInstance();
		} catch (Exception e) {
			throw new TrackedRuntimeException(e);
		}
	}
}
