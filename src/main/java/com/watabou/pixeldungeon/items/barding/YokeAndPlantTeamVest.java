package com.watabou.pixeldungeon.items.barding;

import com.nyrds.pixeldungeon.ml.R;
import com.watabou.noosa.Game;
import com.watabou.pixeldungeon.actors.buffs.Harvest;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.actors.hero.HeroSubClass;
import com.watabou.pixeldungeon.utils.GLog;

public class YokeAndPlantTeamVest extends ClassBarding {

	private static final String TXT_NOT_FARMER = Game.getVar(R.string.YokeAndPlantTeamVest_NotFarmer);
	private static final String AC_SPECIAL = Game.getVar(R.string.YokeAndPlantTeamVest_ACSpecial);

	public YokeAndPlantTeamVest() {
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
		return Game.getVar(R.string.YokeAndPlantTeamVest_Desc);
	}
}