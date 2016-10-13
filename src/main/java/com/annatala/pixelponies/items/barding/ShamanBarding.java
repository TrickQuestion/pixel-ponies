package com.annatala.pixelponies.items.barding;

import com.annatala.pixelponies.android.R;
import com.annatala.noosa.Game;
import com.annatala.noosa.audio.Sample;
import com.annatala.pixelponies.Assets;
import com.annatala.pixelponies.Dungeon;
import com.annatala.pixelponies.actors.Actor;
import com.annatala.pixelponies.actors.blobs.Blob;
import com.annatala.pixelponies.actors.blobs.Regrowth;
import com.annatala.pixelponies.actors.hero.Hero;
import com.annatala.pixelponies.actors.hero.HeroClass;
import com.annatala.pixelponies.actors.hero.HeroSubClass;
import com.annatala.pixelponies.actors.mobs.Mob;
import com.annatala.pixelponies.scenes.GameScene;
import com.annatala.utils.GLog;

public class ShamanBarding extends ClassBarding {
	
	private static final String TXT_NOT_ELF = Game.getVar(R.string.ElfBarding_NotElf);
	private static final String AC_SPECIAL = Game.getVar(R.string.ElfBarding_ACSpecial);
	
	public ShamanBarding()
	{
		name = Game.getVar(R.string.ElfBarding_Name);
		image = 19;
		hasHelmet = true;
		coverHair = true;
	}	
	
	@Override
	public String special() {
		return AC_SPECIAL;
	}
	
	@Override
	public void doSpecial() {
		
		for (Mob mob : Dungeon.level.mobs) {
			if (Dungeon.level.fieldOfView[mob.getPos()]) {
				GameScene.add( Blob.seed( mob.getPos(), 100, Regrowth.class ) );
			}
		}
		
		getCurUser().hp(getCurUser().hp() - (getCurUser().hp() / 3));
		
		getCurUser().spend( Actor.TICK );
		getCurUser().getSprite().operate( getCurUser().getPos() );
		getCurUser().busy();
		
		Sample.INSTANCE.play( Assets.SND_READ );
		
		GameScene.add( Blob.seed( getCurUser().getPos(), 100, Regrowth.class ) );
	}
	
	@Override
	public boolean doEquip( Hero hero ) {
		if (hero.heroClass == HeroClass.NIGHTWING && hero.subClass == HeroSubClass.SHAMAN) {
			return super.doEquip( hero );
		} else {
			GLog.w( TXT_NOT_ELF );
			return false;
		}
	}
	
	@Override
	public String desc() {
		return Game.getVar(R.string.ElfBarding_Desc);
	}
}