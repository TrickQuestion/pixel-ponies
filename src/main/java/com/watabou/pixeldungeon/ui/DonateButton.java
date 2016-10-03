package com.watabou.pixeldungeon.ui;

import com.nyrds.pixeldungeon.ml.R;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.ui.Button;
import com.watabou.pixeldungeon.Assets;
import com.watabou.pixeldungeon.PixelPonies;
import com.watabou.pixeldungeon.windows.WndDonate;

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