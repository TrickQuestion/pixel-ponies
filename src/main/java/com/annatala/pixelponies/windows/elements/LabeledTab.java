package com.annatala.pixelponies.windows.elements;

import com.annatala.pixelponies.android.util.GuiProperties;
import com.annatala.noosa.Text;
import com.annatala.pixelponies.scenes.PixelScene;
import com.annatala.pixelponies.windows.WndTabbed;

public class LabeledTab extends Tab {

	private Text btLabel;

	public LabeledTab(WndTabbed parent, String label) {
		super(parent);
		btLabel.text(label);
		btLabel.measure();
	}

	@Override
	protected void createChildren() {
		super.createChildren();

		btLabel = PixelScene.createText(GuiProperties.titleFontSize());
		add(btLabel);
	}

	@Override
	protected void layout() {
		super.layout();

		btLabel.x = PixelScene.align(x + (width  - btLabel.width()) / 2);
		btLabel.y = PixelScene.align(y + (height - btLabel.baseLine()) / 2) - 1;
		if (!selected) {
			btLabel.y -= 2;
		}
	}

	@Override
	public void select(boolean value) {
		super.select(value);
		btLabel.am = selected ? 1.0f : 0.6f;
	}
}