package com.annatala.pixelponies.scenes;

import com.annatala.pixelponies.android.util.GuiProperties;
import com.annatala.pixelponies.android.R;
import com.annatala.noosa.Camera;
import com.annatala.noosa.Game;
import com.annatala.noosa.NinePatch;
import com.annatala.noosa.Text;
import com.annatala.noosa.ui.Component;
import com.annatala.pixelponies.Chrome;
import com.annatala.pixelponies.Preferences;
import com.annatala.pixelponies.ui.Archs;
import com.annatala.pixelponies.ui.RedButton;
import com.annatala.pixelponies.ui.ScrollPane;

public class AllowStatisticsCollectionScene extends PixelScene {

	private static final String TTL_Welcome = Game.getVar(R.string.AllowStatisticsCollectionScene_Title);

	private static final int GAP = 4;

	@Override
	public void create() {
		super.create();

		Text title = createMultiline(TTL_Welcome, GuiProperties.bigTitleFontSize());

		int w = Camera.main.width;
		int h = Camera.main.height;

		int pw = w - 10;

		title.maxWidth(pw);
		title.measure();

		title.x = align((w - title.width()) / 2);
		title.y = align(8);
		add(title);

		NinePatch panel = Chrome.get(Chrome.Type.WINDOW);

		panel.x = (w - pw) / 2;
		panel.y = title.y + title.height() + GAP * 2;
		int ph = (int) (h - panel.y - 22);

		panel.size(pw, ph);

		add(panel);

		ScrollPane list = new ScrollPane(new Component());
		add(list);
		list.setRect(panel.x + panel.marginLeft(), panel.y + panel.marginTop(), panel.innerWidth(),
				panel.innerHeight());
		list.scrollTo(0, 0);

		Component content = list.content();
		content.clear();

		float yPos = 0;
		Text text = createMultiline(Game.getVar(R.string.AllowStatisticsCollectionScene_Request), GuiProperties.regularFontSize());
		text.maxWidth((int) panel.innerWidth());
		text.measure();

		content.add(text);
		yPos += text.height() + GAP;

		content.setSize(panel.innerWidth(), yPos);

		RedButton allow = new RedButton(Game.getVar(R.string.AllowStatisticsCollectionScene_Allow)) {
			@Override
			protected void onClick() {
				Preferences.INSTANCE.put(Preferences.KEY_COLLECT_STATS, 100);
				Game.instance().initEventCollector();
				Game.switchScene(TitleScene.class);
			}
		};

		RedButton deny = new RedButton(Game.getVar(R.string.AllowStatisticsCollectionScene_Deny)) {
			@Override
			protected void onClick() {
				Preferences.INSTANCE.put(Preferences.KEY_COLLECT_STATS, -100);
				Game.instance().initEventCollector();
				Game.switchScene(TitleScene.class);
			}
		};

		allow.setRect((w - pw) / 2, h - 22, pw/2 - GAP, 18);
		deny.setRect((w - pw) / 2 + pw/2 , h - 22, pw/2-GAP, 18);
		add(allow);
		add(deny);

		Archs archs = new Archs();
		archs.setSize(Camera.main.width, Camera.main.height);
		addToBack(archs);

		fadeIn();
	}
}
