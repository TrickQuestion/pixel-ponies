package com.annatala.pixelponies.scenes;

import com.annatala.pixelponies.android.util.GuiProperties;
import com.annatala.pixelponies.android.R;
import com.annatala.noosa.Camera;
import com.annatala.noosa.Game;
import com.annatala.noosa.NinePatch;
import com.annatala.noosa.Text;
import com.annatala.noosa.ui.Component;
import com.annatala.pixelponies.Chrome;
import com.annatala.pixelponies.PixelPonies;
import com.annatala.pixelponies.Preferences;
import com.annatala.pixelponies.ui.Archs;
import com.annatala.pixelponies.ui.RedButton;
import com.annatala.pixelponies.ui.ScrollPane;

public class WelcomeScene extends PixelScene {

	private static final String TTL_Welcome = Game.getVar(R.string.Welcome_Title);

	private static final String TXT_Welcome = Game.getVar(R.string.Welcome_Text);



	private static final int GAP = 4;

	@Override
	public void create() {
		super.create();

		String[] upds = { TXT_Welcome  };

		Text[] updTexts = new Text[upds.length];

		for (int i = 0; i < upds.length; i++) {
			updTexts[i] = createMultiline(upds[upds.length - i - 1],  GuiProperties.regularFontSize());
		}

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
		for (int i = 0; i < upds.length; i++) {
			updTexts[i].maxWidth((int) panel.innerWidth());
			updTexts[i].measure();

			updTexts[i].setPos(0, yPos);
			yPos += updTexts[i].height() + GAP;
			content.add(updTexts[i]);
		}

		content.setSize(panel.innerWidth(), yPos);

		RedButton okay = new RedButton(Game.getVar(R.string.Welcome_Ok)) {
			@Override
			protected void onClick() {
				PixelPonies.version(Game.versionCode);
				if(Preferences.INSTANCE.getInt(Preferences.KEY_COLLECT_STATS,0) == 0) {
					Game.switchScene(AllowStatisticsCollectionScene.class);
				} else {
					Game.switchScene(TitleScene.class);
				}
			}
		};

		okay.setRect((w - pw) / 2, h - 22, pw, 18);
		add(okay);

		Archs archs = new Archs();
		archs.setSize(Camera.main.width, Camera.main.height);
		addToBack(archs);

		fadeIn();
	}
}
