package com.watabou.pixeldungeon.items.barding;

import com.nyrds.pixeldungeon.ml.R;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.pixeldungeon.Assets;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Actor;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.buffs.Burning;
import com.watabou.pixeldungeon.actors.buffs.Roots;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.actors.mobs.Mob;
import com.watabou.pixeldungeon.effects.particles.ElmoParticle;

public class BattleMageBarding extends ClassBarding {
	
	private static final String AC_SPECIAL = Game.getVar(R.string.MageBarding_ACSpecial);
	
	private static final String TXT_NOT_MAGE = Game.getVar(R.string.MageBarding_NotMage);
	
	{
		name = Game.getVar(R.string.MageBarding_Name);
		hasCollar = true;
		image = 12;
	}
	
	@Override
	public String special() {
		return AC_SPECIAL;
	}
	
	@Override
	public String desc() {
		return Game.getVar(R.string.MageBarding_Desc);
	}
	
	@Override
	public void doSpecial() {	

		for (Mob mob : Dungeon.level.mobs) {
			if (Dungeon.level.fieldOfView[mob.getPos()]) {
				Buff.affect( mob, Burning.class ).reignite( mob );
				Buff.prolong( mob, Roots.class, 3 );
			}
		}
		
		getCurUser().hp(getCurUser().hp() - (getCurUser().hp() / 3));
		
		getCurUser().spend( Actor.TICK );
		getCurUser().getSprite().operate( getCurUser().getPos() );
		getCurUser().busy();
		
		getCurUser().getSprite().centerEmitter().start( ElmoParticle.FACTORY, 0.15f, 4 );
		Sample.INSTANCE.play( Assets.SND_READ );
	}
	
	@Override
	public boolean doEquip( Hero hero ) {
//		if (hero.heroClass == HeroClass.UNICORN && hero.subClass == HeroSubClass.PRINCESS) {
//			return super.doEquip( hero );
//		} else {
//			GLog.w( TXT_NOT_MAGE );
//			return false;
//		}
		return false;
	}
}