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
import com.annatala.noosa.Image;
import com.annatala.noosa.Text;
import com.annatala.pixelponies.Badges;
import com.annatala.pixelponies.effects.BadgeBanner;
import com.annatala.pixelponies.scenes.PixelScene;
import com.annatala.pixelponies.ui.Window;

public class WndBadge extends Window {
	
	private static final int WIDTH = 120;
	private static final int MARGIN = 4;
	
	public WndBadge( Badges.Badge badge ) {
		
		super();
		
		Image icon = BadgeBanner.image( badge.image );
		icon.Scale().set( 2 );
		add( icon );
		
		Text info = PixelScene.createMultiline( badge.description, GuiProperties.regularFontSize());
		info.maxWidth(WIDTH - MARGIN * 2);
		info.measure();
		
		float w = Math.max( icon.width(), info.width() ) + MARGIN * 2;
		
		icon.x = (w - icon.width()) / 2;
		icon.y = MARGIN;
		
		float pos = icon.y + icon.height() + MARGIN;
		
		info.hardlight(0xFFFF00);
		info.x = PixelScene.align(w / 2 - info.width() / 2);
		info.y = PixelScene.align(pos);
		add(info);
		
		resize( (int)w, (int)(pos + info.height() + MARGIN) );
		
		BadgeBanner.highlight( icon, badge.image );
	}
}
