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
import com.annatala.pixelponies.actors.mobs.npcs.Ghost;
import com.annatala.pixelponies.items.Item;
import com.annatala.pixelponies.items.quest.DriedRose;
import com.annatala.pixelponies.scenes.PixelScene;
import com.annatala.pixelponies.sprites.ItemSprite;
import com.annatala.pixelponies.ui.RedButton;
import com.annatala.pixelponies.ui.Window;
import com.annatala.utils.GLog;
import com.annatala.utils.Utils;

public class WndSadGhost extends Window {
	
	private static final String TXT_ROSE     = Game.getVar(R.string.WndSadGhost_Rose);
	private static final String TXT_RAT      = Game.getVar(R.string.WndSadGhost_Rat);
	private static final String TXT_WEAPON   = Game.getVar(R.string.WndSadGhost_Wepon);
	private static final String TXT_BARDING = Game.getVar(R.string.WndSadGhost_Barding);
	private static final String TXT_FAREWELL = Game.getVar(R.string.WndSadGhost_Farewell);
	
	private static final int WIDTH		= 120;
	private static final int BTN_HEIGHT	= 18;
	private static final float GAP		= 2;
	
	public WndSadGhost( final Ghost ghost, final Item item ) {
		
		super();
		
		IconTitle titlebar = new IconTitle();
		titlebar.icon( new ItemSprite( item ) );
		titlebar.label( Utils.capitalize( item.name() ) );
		titlebar.setRect( 0, 0, WIDTH, 0 );
		add( titlebar );
		
		Text message = PixelScene.createMultiline( item instanceof DriedRose ? TXT_ROSE : TXT_RAT, GuiProperties.regularFontSize() );
		message.maxWidth(WIDTH);
		message.measure();
		message.y = titlebar.bottom() + GAP;
		add( message );
		
		RedButton btnWeapon = new RedButton( TXT_WEAPON ) {
			@Override
			protected void onClick() {
				selectReward( ghost, item, Ghost.Quest.weapon );
			}
		};
		btnWeapon.setRect( 0, message.y + message.height() + GAP, WIDTH, BTN_HEIGHT );
		add( btnWeapon );
		
		RedButton btnBarding = new RedButton( TXT_BARDING ) {
			@Override
			protected void onClick() {
				selectReward( ghost, item, Ghost.Quest.barding);
			}
		};
		btnBarding.setRect( 0, btnWeapon.bottom() + GAP, WIDTH, BTN_HEIGHT );
		add( btnBarding );
		
		resize( WIDTH, (int)btnBarding.bottom() );
	}
	
	private void selectReward( Ghost ghost, Item item, Item reward ) {
		hide();

		item.removeItemFrom(Dungeon.hero);

		if (reward.doPickUp( Dungeon.hero )) {
			GLog.i( Hero.TXT_YOU_NOW_HAVE, reward.name() );
		} else {
			Dungeon.level.drop( reward, ghost.getPos() ).sprite.drop();
		}
		
		ghost.say( TXT_FAREWELL );
		ghost.die( null );
		
		Ghost.Quest.complete();
	}
}
