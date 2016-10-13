package com.annatala.pixelponies.ui;

import com.annatala.pixelponies.android.R;
import com.annatala.noosa.Game;
import com.annatala.noosa.Image;
import com.annatala.noosa.audio.Sample;
import com.annatala.noosa.ui.Button;
import com.annatala.pixelponies.Assets;
import com.annatala.pixelponies.PixelPonies;
import com.annatala.pixelponies.windows.WndDonate;

public class DonateButton extends Button {

	private Image image;

	public DonateButton() {
		super();

		width = image.width;
		height = image.height;
	}

	private void updateImage() {
		
		if(image != null) {
			remove(image);
		}
		
		switch (PixelPonies.donated()) {
		default:
		case 0:
			image = Icons.PLEASE_DONATE.get();
			break;
		case 1:
			image = Icons.EARTH_PATRON.get();
			break;
		case 2:
			image = Icons.PEGASUS_PATRON.get();
			break;
		case 3:
			image = Icons.UNICORN_PATRON.get();
			break;
		case 4:
			image = Icons.ALICORN_PATRON.get();
			break;
		}
		
		add(image);
		layout();
	}
	
	@Override
	protected void createChildren() {
		super.createChildren();

		updateImage();
	}

	public String getText() {
		switch (PixelPonies.donated()) {
		case 1:
		case 2:
		case 3:
			return Game.getVar(R.string.DonateButton_Thanks);
		default:
			return Game.getVar(R.string.DonateButton_PleaseDonate);
		}
	}

	@Override
	protected void layout() {
		super.layout();

		image.x = x;
		image.y = y;
	}

	@Override
	protected void onTouchDown() {
		image.brightness(1.5f);
		Sample.INSTANCE.play(Assets.SND_CLICK);
	}

	@Override
	protected void onTouchUp() {
		image.resetColor();
	}

	@Override
	protected void onClick() {
		getParent().add(new WndDonate());
	}
}
