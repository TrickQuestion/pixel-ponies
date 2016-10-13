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

import com.annatala.pixelponies.android.util.TrackedRuntimeException;
import com.annatala.pixelponies.android.R;
import com.annatala.noosa.Game;
import com.annatala.pixelponies.Dungeon;
import com.annatala.pixelponies.PixelPonies;
import com.annatala.pixelponies.scenes.GameScene;
import com.annatala.pixelponies.scenes.InterlevelScene;
import com.annatala.pixelponies.scenes.RankingsScene;
import com.annatala.pixelponies.scenes.TitleScene;
import com.annatala.pixelponies.ui.Icons;
import com.annatala.pixelponies.ui.RedButton;
import com.annatala.pixelponies.ui.Window;

public class WndGame extends Window {

	private static final String TXT_SETTINGS = Game
			.getVar(R.string.WndGame_Settings);
	private static final String TXT_CHALLEGES = Game
			.getVar(R.string.WndGame_Challenges);
	private static final String TXT_RANKINGS = Game
			.getVar(R.string.WndGame_Ranking);
	private static final String TXT_START = Game.getVar(R.string.WndGame_Start);
	private static final String TXT_MENU = Game.getVar(R.string.WndGame_menu);
	private static final String TXT_EXIT = Game.getVar(R.string.WndGame_Exit);
	private static final String TXT_RETURN = Game
			.getVar(R.string.WndGame_Return);

	private static final int WIDTH = 120;
	private static final int BTN_HEIGHT = 20;
	private static final int GAP = 2;

	private int pos;

	public WndGame() {
		
		super();
		
		addButton( new RedButton( TXT_SETTINGS ) {
			@Override
			protected void onClick() {
				hide();
				GameScene.show( new WndSettings( true ) );
			}
		} );
		
		
		if(Dungeon.hero.getDifficulty() < 2 && Dungeon.hero.isAlive()) {
			addButton( new RedButton( Game.getVar(R.string.WndGame_Save) ) {
				@Override
				protected void onClick() {
					GameScene.show(new WndSaveSlotSelect(true));
				}
			} );
		}
		
		if(Dungeon.hero.getDifficulty() < 2) {
			addButton( new RedButton( Game.getVar(R.string.WndGame_Load) ) {
				@Override
				protected void onClick() {
					GameScene.show(new WndSaveSlotSelect(false));
				}
			} );
		}
		
		if (Dungeon.challenges > 0) {
			addButton( new RedButton( TXT_CHALLEGES ) {
				@Override
				protected void onClick() {
					hide();
					GameScene.show( new WndChallenges( Dungeon.challenges, false ) );
				}
			} );
		}
		
		if (!Dungeon.hero.isAlive()) {
			
			RedButton btnStart;
			addButton( btnStart = new RedButton( TXT_START ) {
				@Override
				protected void onClick() {
					Dungeon.hero = null;
					PixelPonies.challenges( Dungeon.challenges );
					InterlevelScene.mode = InterlevelScene.Mode.DESCEND;
					InterlevelScene.noStory = true;
					Game.switchScene( InterlevelScene.class );
				}
			} );
			btnStart.icon( Icons.get( Dungeon.hero.heroClass ) );
			
			addButton( new RedButton( TXT_RANKINGS ) {
				@Override
				protected void onClick() {
					InterlevelScene.mode = InterlevelScene.Mode.DESCEND;
					Game.switchScene( RankingsScene.class );
				}
			} );
		}
				
		addButton( new RedButton( TXT_MENU ) {
			@Override
			protected void onClick() {
				try {
					Dungeon.saveAll();
				} catch (Exception e) {
					throw new TrackedRuntimeException(e);
				}
				Game.switchScene( TitleScene.class );
			}
		} );
		
		addButton( new RedButton( TXT_EXIT ) {
			@Override
			protected void onClick() {
				Game.shutdown();
			}
		} );
		
		addButton( new RedButton( TXT_RETURN ) {
			@Override
			protected void onClick() {
				hide();
			}
		} );
		
		resize( WIDTH, pos );
	}

	private void addButton(RedButton btn) {
		add(btn);
		btn.setRect(0, pos > 0 ? pos += GAP : 0, WIDTH, BTN_HEIGHT);
		pos += BTN_HEIGHT;
	}
}
