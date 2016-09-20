package com.watabou.pixeldungeon.items.armor;

import com.nyrds.pixeldungeon.ml.R;
import com.watabou.noosa.Game;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.buffs.SugarRush;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.actors.hero.HeroSubClass;
import com.watabou.pixeldungeon.utils.GLog;

public class BardArmor extends ClassArmor {

	private static final String TXT_NOT_BARD = Game.getVar(R.string.BardArmor_NotBard);
	private static final String AC_SPECIAL = Game.getVar(R.string.BardArmor_ACSpecial);

	public BardArmor() {
		image = 7;
		hasHelmet = false;
	}	
	
	@Override
	public String special() {
		return AC_SPECIAL;
	}
	
	@Override
	public void doSpecial() {

        Buff.affect( getCurUser(), SugarRush.class, 10 );

	}
	
	@Override
	public boolean doEquip( Hero hero ) {
		if (hero.subClass == HeroSubClass.BARD) {
			return super.doEquip( hero );
		} else {
			GLog.w( TXT_NOT_BARD );
			return false;
		}
	}
	
	@Override
	public String desc() {
		return Game.getVar(R.string.BardArmor_Desc);
	}
}