package com.annatala.pixelponies.windows.elements;

import com.annatala.pixelponies.android.util.GuiProperties;
import com.annatala.noosa.Image;
import com.annatala.noosa.Text;
import com.annatala.noosa.ui.Component;
import com.annatala.pixelponies.scenes.PixelScene;
import com.annatala.pixelponies.ui.ScrollPane;
import com.annatala.pixelponies.ui.Window;
import com.annatala.utils.Utils;
import com.annatala.pixelponies.windows.IconTitle;

public class GenericInfo {

	static final int WIDTH      = 120;
	static final int GAP        = 2;
	static final int MAX_HEIGHT = 120;
	
	static public void makeInfo(Window parent, Image icon, String title, int titleColor, String desc){
		
		IconTitle titlebar = new IconTitle();
		titlebar.icon( icon );
		titlebar.label( Utils.capitalize( title ), titleColor );
		titlebar.setRect( 0, 0, WIDTH, 0 );
		parent.add( titlebar );
		
		Text txtInfo = PixelScene.createMultiline( desc, GuiProperties.regularFontSize() );
		txtInfo.maxWidth(WIDTH);
		txtInfo.measure();
		txtInfo.setPos(0, 0);
		
		int wndHeight = (int) Math.min((titlebar.bottom() + txtInfo.height() + 3 * GAP),MAX_HEIGHT);
		parent.resize( WIDTH, wndHeight);
		
		int scroolZoneHeight = (int) (wndHeight - titlebar.bottom() - GAP * 2);

		ScrollPane list = new ScrollPane(new Component());
		parent.add(list);
		
		list.setRect(0, titlebar.height() + GAP, WIDTH, scroolZoneHeight);
		
		Component content = list.content();
		content.clear();

		content.add(txtInfo);
		content.setSize(txtInfo.width(), txtInfo.height());
	}
}
