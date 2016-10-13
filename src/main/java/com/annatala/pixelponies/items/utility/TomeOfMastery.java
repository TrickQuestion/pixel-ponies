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
package com.annatala.pixelponies.items.utility;

import com.annatala.pixelponies.android.R;
import com.annatala.noosa.Game;
import com.annatala.noosa.audio.Sample;
import com.annatala.pixelponies.Assets;
import com.annatala.pixelponies.Badges;
import com.annatala.pixelponies.actors.buffs.Blindness;
import com.annatala.pixelponies.actors.hero.Hero;
import com.annatala.pixelponies.actors.hero.HeroSubClass;
import com.annatala.pixelponies.effects.Speck;
import com.annatala.pixelponies.effects.SpellSprite;
import com.annatala.pixelponies.items.Item;
import com.annatala.pixelponies.scenes.GameScene;
import com.annatala.pixelponies.sprites.ItemSpriteSheet;
import com.annatala.utils.GLog;
import com.annatala.utils.Utils;
import com.annatala.pixelponies.windows.WndChooseWay;

import java.util.ArrayList;

public class TomeOfMastery extends Item {

	private static final String TXT_BLINDED	= Game.getVar(R.string.TomeOfMastery_Blinded);

	public static final float TIME_TO_READ = 10;

	public static final String AC_READ                 = Game.getVar(R.string.TomeOfMastery_ACRead);
	private static final String TXT_WAY_ALREADY_CHOSEN = Game.getVar(R.string.TomeOfMastery_WayAlreadyChosen);

	{
		stackable = false;
		image = ItemSpriteSheet.MASTERY;
	}
	
	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );		
		actions.add( AC_READ );

		// I want it to be impossible to lose the Tome (for now).
		actions.remove( AC_DROP );
		actions.remove( AC_THROW );
		
		return actions;
	}
	
	@Override
	public void execute( Hero hero, String action ) {
		if (action.equals( AC_READ )) {
			
			if (hero.buff( Blindness.class ) != null) {
				GLog.w( TXT_BLINDED );
				return;
			}

			if(hero.subClass != HeroSubClass.NONE) {
				GLog.w( TXT_WAY_ALREADY_CHOSEN );
				return;
			}

			setCurUser(hero);
			
			GameScene.show( new WndChooseWay(
					this, hero.heroClass.firstWay(), hero.heroClass.secondWay() ) );
			
		} else {
			
			super.execute( hero, action );
			
		}
	}
	
	@Override
	public boolean doPickUp( Hero hero ) {
		Badges.validateMastery();
		return super.doPickUp( hero );
	}
	
	@Override
	public boolean isUpgradable() {
		return false;
	}
	
	@Override
	public boolean isIdentified() {
		return true;
	}
	
	public void choose( HeroSubClass way ) {
		
		detach( getCurUser().belongings.backpack );
				
		getCurUser().subClass = way;
		
		getCurUser().getSprite().operate( getCurUser().getPos() );
		Sample.INSTANCE.play( Assets.SND_MASTERY );
		
		SpellSprite.show( getCurUser(), SpellSprite.MASTERY );
		getCurUser().getSprite().emitter().burst( Speck.factory( Speck.MASTERY ), 12 );
		GLog.w(Game.getVar(R.string.TomeOfMastery_Choose), Utils.capitalize( way.toString() ) );
		
		getCurUser().checkIfFurious();
		getCurUser().updateLook();
		
		getCurUser().spendAndNext( TomeOfMastery.TIME_TO_READ );
		getCurUser().busy();

		
	}
}
