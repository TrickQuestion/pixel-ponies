package com.watabou.pixeldungeon.items.armor;

import com.nyrds.pixeldungeon.ml.R;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.pixeldungeon.Assets;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Actor;
import com.watabou.pixeldungeon.actors.blobs.Blob;
import com.watabou.pixeldungeon.actors.blobs.Regrowth;
import com.watabou.pixeldungeon.actors.buffs.Harvest;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.actors.hero.HeroClass;
import com.watabou.pixeldungeon.actors.hero.HeroSubClass;
import com.watabou.pixeldungeon.actors.mobs.Mob;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.utils.Random;

public class FarmerArmor extends ClassArmor {

	private static final String TXT_NOT_FARMER = Game.getVar(R.string.FarmerArmor_NotFarmer);
	private static final String AC_SPECIAL = Game.getVar(R.string.FarmerArmor_ACSpecial);

	public FarmerArmor() {
		image = 6;
		hasHelmet = false;
	}	
	
	@Override
	public String special() {
		return AC_SPECIAL;
	}
	
	@Override
	public void doSpecial() {

        Buff.affect( getCurUser(), Harvest.class, 10 );

	}
	
	@Override
	public boolean doEquip( Hero hero ) {
		if (hero.subClass == HeroSubClass.FARMER) {
			return super.doEquip( hero );
		} else {
			GLog.w( TXT_NOT_FARMER );
			return false;
		}
	}
	
	@Override
	public String desc() {
		return Game.getVar(R.string.FarmerArmor_Desc);
	}
}