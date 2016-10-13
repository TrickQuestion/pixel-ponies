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
import com.annatala.noosa.audio.Sample;
import com.annatala.pixelponies.Assets;
import com.annatala.pixelponies.Dungeon;
import com.annatala.pixelponies.actors.Actor;
import com.annatala.pixelponies.actors.buffs.Blindness;
import com.annatala.pixelponies.actors.buffs.Buff;
import com.annatala.pixelponies.actors.hero.Hero;
import com.annatala.pixelponies.actors.hero.HeroClass;
import com.annatala.pixelponies.actors.mobs.Mob;
import com.annatala.pixelponies.effects.CellEmitter;
import com.annatala.pixelponies.effects.Speck;
import com.annatala.pixelponies.items.wands.WandOfBlink;
import com.annatala.pixelponies.scenes.CellSelector;
import com.annatala.pixelponies.scenes.GameScene;
import com.annatala.utils.GLog;

public class RogueBarding extends ClassBarding {
	
	private static final String TXT_FOV       = Game.getVar(R.string.RogueBarding_Fov);
	private static final String TXT_NOT_ROGUE = Game.getVar(R.string.RogueBarding_NotRogue);
	
	private static final String AC_SPECIAL = Game.getVar(R.string.RogueBarding_ACSpecial);
	
	{
		image = 8;
	}
	
	@Override
	public String special() {
		return AC_SPECIAL;
	}
	
	@Override
	public void doSpecial() {			
		GameScene.selectCell( teleporter );
	}
	
	@Override
	public boolean doEquip( Hero hero ) {
		if (hero.heroClass == HeroClass.PEGASUS) {
			return super.doEquip( hero );
		} else {
			GLog.w( TXT_NOT_ROGUE );
			return false;
		}
	}
	
	@Override
	public String desc() {
		return Game.getVar(R.string.RogueBarding_Desc);
	}

	protected static CellSelector.Listener teleporter = new  CellSelector.Listener() {
		
		@Override
		public void onSelect( Integer target ) {
			if (target != null) {

				if (!Dungeon.level.fieldOfView[target] || 
					!(Dungeon.level.passable[target] || Dungeon.level.avoid[target]) || 
					Actor.findChar( target ) != null) {
					
					GLog.w( TXT_FOV );
					return;
				}
				
				getCurUser().hp(getCurUser().hp() - (getCurUser().hp() / 3));
				
				for (Mob mob : Dungeon.level.mobs) {
					if (Dungeon.level.fieldOfView[mob.getPos()]) {
						Buff.prolong( mob, Blindness.class, 2 );
						mob.state = mob.WANDERING;
						mob.getSprite().emitter().burst( Speck.factory( Speck.LIGHT ), 4 );
					}
				}
				
				WandOfBlink.appear( getCurUser(), target );
				CellEmitter.get( target ).burst( Speck.factory( Speck.WOOL ), 10 );
				Sample.INSTANCE.play( Assets.SND_PUFF );
				Dungeon.level.press( target, getCurUser() );
				Dungeon.observe();
				
				getCurUser().spendAndNext( Actor.TICK );
			}
		}
		
		@Override
		public String prompt() {
			return Game.getVar(R.string.RogueBarding_Prompt);
		}
	};
}