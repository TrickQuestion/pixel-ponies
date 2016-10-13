package com.annatala.pixelponies.ui;

import com.annatala.noosa.Image;
import com.annatala.noosa.audio.Sample;
import com.annatala.noosa.ui.Button;
import com.annatala.pixelponies.Assets;
import com.annatala.pixelponies.windows.WndPremiumSettings;

public class PremiumPrefsButton extends Button {
	
	private Image image;
	
	public PremiumPrefsButton() {
		super();
		
		width = image.width;
		height = image.height;
	}
	
	@Override
	protected void createChildren() {
		super.createChildren();
		
		image = Icons.THANKS_DONATED.get();
		add( image );
	}
	
	@Override
	protected void layout() {
		super.layout();
		
		image.x = x;
		image.y = y;
	}
	
	@Override
	protected void onTouchDown() {
		image.brightness( 1.5f );
		Sample.INSTANCE.play( Assets.SND_CLICK );
	}
	
	@Override
	protected void onTouchUp() {
		image.resetColor();
	}
	
	@Override
	protected void onClick() {
		getParent().add( new WndPremiumSettings( ) );
	}
}
