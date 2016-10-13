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
package com.annatala.pixelponies.windows;

import com.annatala.pixelponies.android.util.GuiProperties;
import com.annatala.noosa.Game;
import com.annatala.noosa.Text;
import com.annatala.pixelponies.Dungeon;
import com.annatala.pixelponies.android.R;
import com.annatala.pixelponies.actors.hero.Hero;
import com.annatala.pixelponies.actors.mobs.npcs.WandMaker;
import com.annatala.pixelponies.items.Item;
import com.annatala.pixelponies.items.wands.Wand;
import com.annatala.pixelponies.scenes.PixelScene;
import com.annatala.pixelponies.sprites.ItemSprite;
import com.annatala.pixelponies.ui.RedButton;
import com.annatala.pixelponies.ui.Window;
import com.annatala.utils.GLog;
import com.annatala.utils.Utils;

public class WndWandmaker extends Window {
	
	private static final String TXT_MESSAGE	   = Game.getVar(R.string.WndWandmaker_Message);
	private static final String TXT_BATTLE     = Game.getVar(R.string.WndWandmaker_Battle);
	private static final String TXT_NON_BATTLE = Game.getVar(R.string.WndWandmaker_NonBattle);

	private static final String TXT_FARAWELL   = Game.getVar(R.string.WndWandmaker_Farawell);
	
	private static final int WIDTH		= 120;
	private static final int BTN_HEIGHT	= 18;
	private static final float GAP		= 2;
	
	public WndWandmaker( final WandMaker wandmaker, final Item item ) {
		
		super();
		
		IconTitle titlebar = new IconTitle();
		titlebar.icon( new ItemSprite( item ) );
		titlebar.label( Utils.capitalize( item.name() ) );
		titlebar.setRect( 0, 0, WIDTH, 0 );
		add( titlebar );
		
		Text message = PixelScene.createMultiline( TXT_MESSAGE, GuiProperties.regularFontSize() );
		message.maxWidth(WIDTH);
		message.measure();
		message.y = titlebar.bottom() + GAP;
		add( message );
		
		RedButton btnBattle = new RedButton( TXT_BATTLE ) {
			@Override
			protected void onClick() {
				selectReward( wandmaker, item, WandMaker.makeBattleWand() );
			}
		};
		btnBattle.setRect( 0, message.y + message.height() + GAP, WIDTH, BTN_HEIGHT );
		add( btnBattle );
		
		RedButton btnNonBattle = new RedButton( TXT_NON_BATTLE ) {
			@Override
			protected void onClick() {
				selectReward( wandmaker, item, WandMaker.makeNonBattleWand() );
			}
		};
		btnNonBattle.setRect( 0, btnBattle.bottom() + GAP, WIDTH, BTN_HEIGHT );
		add( btnNonBattle );
		
		resize( WIDTH, (int)btnNonBattle.bottom() );
	}
	
	private void selectReward( WandMaker wandmaker, Item item, Wand reward ) {
		hide();

		item.removeItemFrom(Dungeon.hero);

		reward.identify();
		if (reward.doPickUp( Dungeon.hero )) {
			GLog.i( Hero.TXT_YOU_NOW_HAVE, reward.name() );
		} else {
			Dungeon.level.drop( reward, wandmaker.getPos() ).sprite.drop();
		}
		
		wandmaker.say(Utils.format( TXT_FARAWELL, Dungeon.hero.className() ) );
		wandmaker.destroy();
		
		wandmaker.getSprite().die();
		
		WandMaker.Quest.complete();
	}
}
