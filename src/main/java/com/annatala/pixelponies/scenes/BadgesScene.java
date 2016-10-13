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
package com.annatala.pixelponies.scenes;

import com.annatala.pixelponies.android.util.GuiProperties;
import com.annatala.noosa.Camera;
import com.annatala.noosa.Game;
import com.annatala.noosa.NinePatch;
import com.annatala.noosa.Text;
import com.annatala.noosa.audio.Music;
import com.annatala.pixelponies.Assets;
import com.annatala.pixelponies.Badges;
import com.annatala.pixelponies.Chrome;
import com.annatala.pixelponies.android.R;
import com.annatala.pixelponies.PixelPonies;
import com.annatala.pixelponies.ui.Archs;
import com.annatala.pixelponies.ui.BadgesList;
import com.annatala.pixelponies.ui.ExitButton;
import com.annatala.pixelponies.ui.ScrollPane;
import com.annatala.pixelponies.ui.Window;

public class BadgesScene extends PixelScene {
	
	private static final String TXT_TITLE = Game.getVar(R.string.BadgesScene_Title);
	
	@Override
	public void create() {
		super.create();
		
		Music.INSTANCE.play( Assets.THEME, true );
		Music.INSTANCE.volume( 1f );
		
		uiCamera.setVisible(false);
		
		int w = Camera.main.width;
		int h = Camera.main.height;
		
		Archs archs = new Archs();
		archs.setSize( w, h );
		add( archs );
		
		int pw = Math.min( 160, w - 6 );
		int ph = h - 30;
		
		NinePatch panel = Chrome.get( Chrome.Type.WINDOW );
		panel.size( pw, ph );
		panel.x = (w - pw) / 2;
		panel.y = (h - ph) / 2;
		add( panel );
		
		Text title = PixelScene.createText( TXT_TITLE, GuiProperties.titleFontSize());
		title.hardlight( Window.TITLE_COLOR );
		title.measure();
		title.x = align( (w - title.width()) / 2 );
		title.y = align( (panel.y - title.baseLine()) / 2 );
		add( title );
		
		Badges.loadGlobal();
		
		ScrollPane list = new BadgesList( true );
		add( list );
		
		list.setRect( 
			panel.x + panel.marginLeft(), 
			panel.y + panel.marginTop(), 
			panel.innerWidth(), 
			panel.innerHeight() );
		
		ExitButton btnExit = new ExitButton();
		btnExit.setPos( Camera.main.width - btnExit.width(), 0 );
		add( btnExit );
		
		fadeIn();
	}
	
	@Override
	protected void onBackPressed() {
		PixelPonies.switchNoFade( TitleScene.class );
	}
}
