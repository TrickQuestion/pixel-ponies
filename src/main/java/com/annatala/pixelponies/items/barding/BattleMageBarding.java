package com.annatala.pixelponies.items.barding;

import com.annatala.pixelponies.android.R;
import com.annatala.noosa.Game;
import com.annatala.noosa.audio.Sample;
import com.annatala.pixelponies.Assets;
import com.annatala.pixelponies.Dungeon;
import com.annatala.pixelponies.actors.Actor;
import com.annatala.pixelponies.actors.buffs.Buff;
import com.annatala.pixelponies.actors.buffs.Burning;
import com.annatala.pixelponies.actors.buffs.Roots;
import com.annatala.pixelponies.actors.hero.Hero;
import com.annatala.pixelponies.actors.mobs.Mob;
import com.annatala.pixelponies.effects.particles.ElmoParticle;

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