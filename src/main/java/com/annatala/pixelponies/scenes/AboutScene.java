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

import android.content.Intent;
import android.net.Uri;

import com.annatala.pixelponies.android.util.GuiProperties;
import com.annatala.pixelponies.android.R;
import com.annatala.input.Touchscreen.Touch;
import com.annatala.noosa.Camera;
import com.annatala.noosa.Game;
import com.annatala.noosa.Image;
import com.annatala.noosa.Text;
import com.annatala.noosa.TouchArea;
import com.annatala.pixelponies.PixelPonies;
import com.annatala.pixelponies.effects.Flare;
import com.annatala.pixelponies.ui.Archs;
import com.annatala.pixelponies.ui.ExitButton;
import com.annatala.pixelponies.ui.Icons;
import com.annatala.pixelponies.ui.Window;

public class AboutScene extends PixelScene {

	private static final String TXT      = Game.getVar(R.string.AboutScene_Txt);
	private static final String OUR_SITE = Game.getVar(R.string.AboutScene_OurSite);
	private static final String LNK      = Game.getVar(R.string.AboutScene_Lnk);
	private static final String SND      = Game.getVar(R.string.AboutScene_Snd);
	private static final String TRN      = Game.getVar(R.string.AboutScene_TranslatedBy);
	
	private Text createTouchEmail(final String address, Text text2)
	{
		Text text = createText(address, text2);
		text.hardlight( Window.TITLE_COLOR );
		
		TouchArea area = new TouchArea( text ) {
			@Override
			protected void onClick( Touch touch ) {
				Intent intent = new Intent( Intent.ACTION_SEND);
				intent.setType("message/rfc822");
				intent.putExtra(Intent.EXTRA_EMAIL, new String[]{address} );
				intent.putExtra(Intent.EXTRA_SUBJECT, Game.getVar(R.string.app_name) );

				Game.instance().startActivity( Intent.createChooser(intent, SND) );
			}
		};
		add(area);
		return text;
	}
	
	private Text createTouchLink(final String address, Text visit)
	{
		Text text = createText(address, visit);
		text.hardlight( Window.TITLE_COLOR );
		
		TouchArea area = new TouchArea( text ) {
			@Override
			protected void onClick( Touch touch ) {
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(address));

				Game.instance().startActivity( Intent.createChooser(intent, OUR_SITE) );
			}
		};
		add(area);
		return text;
	}
	
	private void placeBellow(Text elem, Text upper)
	{
		elem.x = upper.x;
		elem.y = upper.y + upper.height();
	}

	private Text createText(String text, Text upper)
	{
		Text multiline = createMultiline( text, GuiProperties.regularFontSize() );
		multiline.maxWidth(Camera.main.width * 5 / 6);
		multiline.measure();
		add( multiline );
		if(upper!=null){
			placeBellow(multiline, upper);
		}
		return multiline;
	}
	
	@Override
	public void create() {
		super.create();
		
		//uiCamera.visible = false;
		//camera().zoom( defaultZoom + PixelPonies.zoom() );
		
		Text text = createText( TXT, null );
		
		text.camera = uiCamera;
		
		text.x = align( (Camera.main.width - text.width()) / 2 );
		text.y = align( (Camera.main.height - text.height()) / 3 );
		

		Text email = createTouchEmail(Game.getVar(R.string.AboutScene_Mail), text);

		Text visit = createText("\n\n"+Game.getVar(R.string.AboutScene_OurSite)+"\n\n", email);
		Text site  = createTouchLink(LNK, visit);		
		
		createText("\n\n"+TRN, site);
		
		Image wolfCat = Icons.AUTHORS_ICON.get();
		wolfCat.x = align( text.x + (text.width() - wolfCat.width) / 2 );
		wolfCat.y = text.y - wolfCat.height - 8;
		add( wolfCat );
		
		new Flare( 7, 64 ).color( 0x332211, true ).show( wolfCat, 0 ).angularSpeed = -20;
		
		Archs archs = new Archs();
		archs.setSize( Camera.main.width, Camera.main.height );
		addToBack( archs );
		
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
