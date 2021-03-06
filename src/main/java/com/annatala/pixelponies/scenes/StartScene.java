/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
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
import com.annatala.pixelponies.android.EventCollector;
import com.annatala.pixelponies.android.R;
import com.annatala.noosa.Camera;
import com.annatala.noosa.Game;
import com.annatala.noosa.Group;
import com.annatala.noosa.Image;
import com.annatala.noosa.Text;
import com.annatala.noosa.audio.Sample;
import com.annatala.noosa.particles.Emitter;
import com.annatala.noosa.ui.Button;
import com.annatala.pixelponies.Assets;
import com.annatala.pixelponies.Badges;
import com.annatala.pixelponies.Dungeon;
import com.annatala.pixelponies.GamesInProgress;
import com.annatala.pixelponies.PixelPonies;
import com.annatala.pixelponies.actors.hero.HeroClass;
import com.annatala.pixelponies.effects.BannerSprites;
import com.annatala.pixelponies.effects.BannerSprites.Type;
import com.annatala.pixelponies.effects.Speck;
import com.annatala.pixelponies.ui.Archs;
import com.annatala.pixelponies.ui.ExitButton;
import com.annatala.pixelponies.ui.Icons;
import com.annatala.pixelponies.ui.RedButton;
import com.annatala.utils.Utils;
import com.annatala.pixelponies.windows.WndChallenges;
import com.annatala.pixelponies.windows.WndClass;
import com.annatala.pixelponies.windows.WndMessage;
import com.annatala.pixelponies.windows.WndOptions;

import java.util.HashMap;
import java.util.Locale;

public class StartScene extends PixelScene {

	private static final float BUTTON_HEIGHT = 24;
	private static final float GAP = 2;

	private static final String TXT_LOAD = Game.getVar(R.string.StartScene_Load);
	private static final String TXT_NEW = Game.getVar(R.string.StartScene_New);

	private static final String TXT_ERASE = Game.getVar(R.string.StartScene_Erase);
	public static final String TXT_DPTH_LVL = Game.getVar(R.string.StartScene_Depth);

	private static final String TXT_REALLY = Game.getVar(R.string.StartScene_Really);
	private static final String TXT_WARNING = Game.getVar(R.string.StartScene_Warning);
	private static final String TXT_YES = Game.getVar(R.string.StartScene_Yes);
	private static final String TXT_NO = Game.getVar(R.string.StartScene_No);

	private static final String TXT_UNDER_CONSTRUCTION = "Class currently unavailable, check back soon";

	private static final String TXT_UNLOCK_UNICORN = Game.getVar(R.string.StartScene_UnlockUnicorn);
	private static final String TXT_UNLOCK_PEGASUS = Game.getVar(R.string.StartScene_UnlockPegasus);
	private static final String TXT_UNLOCK_ZEBRA = Game.getVar(R.string.StartScene_UnlockZebra);
	private static final String TXT_UNLOCK_NIGHTWING = Game.getVar(R.string.StartScene_UnlockNightwing);

	private static final String TXT_WIN_THE_GAME = Game
			.getVar(R.string.StartScene_WinGame);

	private static final float WIDTH_P = 116;
	private static final float HEIGHT_P = 220;

	private static final float WIDTH_L = 224;
	private static final float HEIGHT_L = 124;

	private static HashMap<HeroClass, ClassShield> shields = new HashMap<>();

	private float buttonX;
	private float buttonY;

	private GameButton btnLoad;
	private GameButton btnNewGame;

	private boolean unicornReady = false;
	private boolean pegasusReady = false;
	private boolean zebraReady = false;
	private boolean nightwingReady = false;
	
	private Group unlockUnicorn;
	private Group unlockPegasus;
	private Group unlockZebra;
	private Group unlockNightwing;

	private static HeroClass curClass;

	@Override
	public void create() {
		super.create();

		Badges.loadGlobal();

		uiCamera.setVisible(false);

		int w = Camera.main.width;
		int h = Camera.main.height;

		float width, height;
		if (PixelPonies.landscape()) {
			width = WIDTH_L;
			height = HEIGHT_L;
		} else {
			width = WIDTH_P;
			height = HEIGHT_P;
		}

		float left = (w - width) / 2;
		float top = (h - height) / 2;
		float bottom = h - top;

		Archs archs = new Archs();
		archs.setSize(w, h);
		add(archs);

		Image title = BannerSprites.get(Type.SELECT_YOUR_HERO);
		title.x = align((w - title.width()) / 2);
		title.y = align(top);
		add(title);

		buttonX = left;
		buttonY = bottom - BUTTON_HEIGHT;

		btnNewGame = new GameButton(TXT_NEW) {
			@Override
			protected void onClick() {
				if (GamesInProgress.check(curClass) != null) {
					StartScene.this.add(new WndOptions(TXT_REALLY, TXT_WARNING,
							TXT_YES, TXT_NO) {
						@Override
						protected void onSelect(int index) {
							if (index == 0) {
								selectDifficulty();
							}
						}
					});

				} else {
					selectDifficulty();
				}
			}
		};
		add(btnNewGame);

		btnLoad = new GameButton(TXT_LOAD) {
			@Override
			protected void onClick() {
				InterlevelScene.mode = InterlevelScene.Mode.CONTINUE;
				Game.switchScene(InterlevelScene.class);
				Dungeon.heroClass = curClass;
			}
		};
		add(btnLoad);

		float centralHeight = buttonY - title.y - title.height();

		HeroClass[] classes = { HeroClass.EARTH_PONY, HeroClass.UNICORN,
				HeroClass.PEGASUS, HeroClass.ZEBRA, HeroClass.NIGHTWING};
		for (HeroClass cl : classes) {
			ClassShield shield = new ClassShield(cl);
			shields.put(cl, shield);
			add(shield);
		}
		if (PixelPonies.landscape()) {
			float shieldW = width / 5;
			float shieldH = Math.min(centralHeight, shieldW);
			top = title.y + title.height + (centralHeight - shieldH) / 2;
			for (int i = 0; i < classes.length; i++) {
				ClassShield shield = shields.get(classes[i]);
				shield.setRect(left + i * shieldW, top, shieldW, shieldH);
			}

			ChallengeButton challenge = new ChallengeButton();
			challenge.setPos(w / 2 - challenge.width() / 2, 0);
			add(challenge);

		} else {
			float shieldW = width / 3;
			float shieldH = Math.min(centralHeight / 3, shieldW * 1.2f);
			top = title.y + title.height() + centralHeight / 2 - shieldH;
			for (int i = 0; i < classes.length; i++) {
				ClassShield shield = shields.get(classes[i]);

				if (i < 3) {
					shield.setRect(left + i * shieldW, top - shieldH * 0.5f,
							shieldW, shieldH);
				} else {
					shield.setRect(left + (i % 3) * shieldW * 2, top + shieldH, shieldW, shieldH);
				}
			}

			ChallengeButton challenge = new ChallengeButton();
			challenge.setPos(w / 2 - challenge.width() / 2, top + shieldH * 1.5f
					- challenge.height() / 2);
			add(challenge);
		}

		unlockUnicorn = new Group();
		add(unlockUnicorn);

		unlockPegasus = new Group();
		add(unlockPegasus);

		unlockZebra = new Group();
		add(unlockZebra);

		unlockNightwing = new Group();
		add(unlockNightwing);

		Text text = PixelScene.createMultiline(TXT_UNDER_CONSTRUCTION, GuiProperties.titleFontSize());
		text.maxWidth((int) width);
		text.measure();
		float pos = (bottom - BUTTON_HEIGHT) + (BUTTON_HEIGHT - text.height()) / 2;
		text.hardlight(0xFFFF00);
		text.x = PixelScene.align(w / 2 - text.width() / 2);
		text.y = PixelScene.align(pos);
		unlockUnicorn.add(text);
		unlockPegasus.add(text);
		unlockZebra.add(text);
		unlockNightwing.add(text);


//		unlock = new Group();
//		add(unlock);
//
//		unlockElf = new Group();
//		add(unlockElf);
//
//		if (!(huntressUnlocked = Badges.isUnlocked( Badges.Badge.BOSS_SLAIN_3) || (PixelPonies.donated() >= 1) )) {
//			Text text = PixelScene
//					.createMultiline(TXT_UNLOCK, GuiProperties.titleFontSize());
//			text.maxWidth((int) width);
//			text.measure();
//
//			float pos = (bottom - BUTTON_HEIGHT)
//					+ (BUTTON_HEIGHT - text.height()) / 2;
//
//			text.hardlight(0xFFFF00);
//			text.x = PixelScene.align(w / 2 - text.width() / 2);
//			text.y = PixelScene.align(pos);
//			unlock.add(text);
//
//		}
//
//		if (!(elfUnlocked = Badges.isUnlocked( Badges.Badge.BOSS_SLAIN_4) || (PixelPonies.donated() >= 2) )) {
//			Text text = PixelScene
//					.createMultiline(TXT_UNLOCK_ELF, GuiProperties.titleFontSize());
//			text.maxWidth((int) width);
//			text.measure();
//
//			float pos = (bottom - BUTTON_HEIGHT)
//					+ (BUTTON_HEIGHT - text.height()) / 2;
//
//			text.hardlight(0xFFFF00);
//			text.x = PixelScene.align(w / 2 - text.width() / 2);
//			text.y = PixelScene.align(pos);
//			unlockElf.add(text);
//
//		}
		
		ExitButton btnExit = new ExitButton();
		btnExit.setPos(Camera.main.width - btnExit.width(), 0);
		add(btnExit);

		curClass = null;
		updateClass(HeroClass.values()[PixelPonies.lastClass()]);

		fadeIn();
	}

	@Override
	public void destroy() {

		Badges.saveGlobal();

		super.destroy();
	}

	private void updateClass(HeroClass cl) {

		if (curClass == cl) {
			add(new WndClass(cl));
			return;
		}

		if (curClass != null) {
			shields.get(curClass).highlight(false);
		}
		shields.get(curClass = cl).highlight(true);


		if (cl == HeroClass.UNICORN && !unicornReady) {
			unlockUnicorn.setVisible(true);
			unlockPegasus.setVisible(false);
			unlockZebra.setVisible(false);
			unlockNightwing.setVisible(false);
			btnLoad.setVisible(false);
			btnNewGame.setVisible(false);
			return;
		}

		if (cl == HeroClass.PEGASUS && !pegasusReady) {
			unlockUnicorn.setVisible(false);
			unlockPegasus.setVisible(true);
			unlockZebra.setVisible(false);
			unlockNightwing.setVisible(false);
			btnLoad.setVisible(false);
			btnNewGame.setVisible(false);
			return;
		}

		if (cl == HeroClass.ZEBRA && !zebraReady) {
			unlockUnicorn.setVisible(false);
			unlockPegasus.setVisible(false);
			unlockZebra.setVisible(true);
			unlockNightwing.setVisible(false);
			btnLoad.setVisible(false);
			btnNewGame.setVisible(false);
			return;
		}

		if (cl == HeroClass.NIGHTWING && !nightwingReady) {
			unlockUnicorn.setVisible(false);
			unlockPegasus.setVisible(false);
			unlockZebra.setVisible(false);
			unlockNightwing.setVisible(true);
			btnLoad.setVisible(false);
			btnNewGame.setVisible(false);
			return;
		}

		unlockUnicorn.setVisible(false);
		unlockPegasus.setVisible(false);
		unlockZebra.setVisible(false);
		unlockNightwing.setVisible(false);

		GamesInProgress.Info info = GamesInProgress.check(curClass);
		if (info != null) {

			btnLoad.setVisible(true);
			btnLoad.secondary(Utils.format(TXT_DPTH_LVL, info.depth,
					info.level));

			btnNewGame.setVisible(true);
			btnNewGame.secondary(TXT_ERASE);

			float w = (Camera.main.width - GAP) / 2 - buttonX;

			btnLoad.setRect(buttonX, buttonY, w, BUTTON_HEIGHT);
			btnNewGame.setRect(btnLoad.right() + GAP, buttonY, w,
					BUTTON_HEIGHT);

		} else {
			btnLoad.setVisible(false);

			btnNewGame.setVisible(true);
			btnNewGame.secondary(null);
			btnNewGame.setRect(buttonX, buttonY, Camera.main.width
					- buttonX * 2, BUTTON_HEIGHT);
		}

	}

	private void selectDifficulty() {
		
		WndOptions difficultyOptions = new WndOptions(Game.getVar(R.string.StartScene_DifficultySelect), "",
				Game.getVar(R.string.StartScene_DifficultyEasy), 
				Game.getVar(R.string.StartScene_DifficultyNormalWithSaves), 
				Game.getVar(R.string.StartScene_DifficultyNormal),
				Game.getVar(R.string.StartScene_DifficultyExpert)) {
			@Override
			protected void onSelect(int index) {
					startNewGame(index);
			}
		};

		add(difficultyOptions);
	}
	
	private void startNewGame(int difficulty) {

		Dungeon.setDifficulty(difficulty);
		Dungeon.hero = null;
		Dungeon.heroClass = curClass;

		EventCollector.logEvent("game","new", curClass.name());
		EventCollector.logEvent("game","mod", PixelPonies.activeMod());
		EventCollector.logEvent("game","difficulty",String.valueOf(difficulty));

		InterlevelScene.mode = InterlevelScene.Mode.DESCEND;
		
		if (PixelPonies.intro()) {
			PixelPonies.intro(false);
			Game.switchScene(IntroScene.class);
		} else {
			Game.switchScene(InterlevelScene.class);
		}
	}

	@Override
	protected void onBackPressed() {
		PixelPonies.switchNoFade(TitleScene.class);
	}

	private static class GameButton extends RedButton {

		private Text secondary;

		public GameButton(String primary) {
			super(primary);

			this.secondary.text(null);
		}

		@Override
		protected void createChildren() {
			super.createChildren();

			secondary = createText(GuiProperties.smallFontSize());

			add(secondary);
		}

		@Override
		protected void layout() {
			super.layout();

			if (secondary.text() != null && secondary.text().length() > 0) {
				text.y = align(y
						+ (height - text.height() - secondary.height())
						/ 2);

				secondary.x = align(x + (width - secondary.width()) / 2);
				secondary.y = align(text.y + text.height());
			} else {
				text.y = align(y + (height - text.height()) / 2);
			}
		}

		public void secondary(String text) {
			secondary.text(text);
			secondary.measure();
		}
	}

	private class ClassShield extends Button {

		private static final float MIN_BRIGHTNESS = 0.6f;

		private static final int BASIC_NORMAL = 0x444444;
		private static final int BASIC_HIGHLIGHTED = 0xCACFC2;

		private static final int MASTERY_NORMAL = 0x7711AA;
		private static final int MASTERY_HIGHLIGHTED = 0xCC33FF;

		private static final int WIDTH = 24;
		private static final int HEIGHT = 30;
		private static final int SCALE = 2;

		private HeroClass cl;

		private Image avatar;
		private Text name;
		private Emitter emitter;

		private float brightness;

		private int normal;
		private int highlighted;

		public ClassShield(HeroClass cl) {
			super();

			this.cl = cl;

			avatar.frame(cl.ordinal() * WIDTH, 0, WIDTH, HEIGHT);
			avatar.Scale().set(SCALE);

			if (Badges.isUnlocked(cl.masteryBadge())) {
				normal = MASTERY_NORMAL;
				highlighted = MASTERY_HIGHLIGHTED;
			} else {
				normal = BASIC_NORMAL;
				highlighted = BASIC_HIGHLIGHTED;
			}

			name.text(cl.toString().toUpperCase(Locale.getDefault()));
			name.measure();
			name.hardlight(normal);

			brightness = MIN_BRIGHTNESS;
			updateBrightness();
		}

		@Override
		protected void createChildren() {

			super.createChildren();

			avatar = new Image(Assets.AVATARS);
			add(avatar);

			name = PixelScene.createText(GuiProperties.titleFontSize());
			add(name);

			emitter = new Emitter();
			add(emitter);
		}

		@Override
		protected void layout() {

			super.layout();

			avatar.x = align(x + (width - avatar.width()) / 2);
			avatar.y = align(y + (height - avatar.height() - name.height()) / 2);

			name.x = align(x + (width - name.width()) / 2);
			name.y = avatar.y + avatar.height() + SCALE;

			emitter.pos(avatar.x, avatar.y, avatar.width(), avatar.height());
		}

		@Override
		protected void onTouchDown() {

			emitter.revive();
			emitter.start(Speck.factory(Speck.LIGHT), 0.05f, 7);

			Sample.INSTANCE.play(Assets.SND_CLICK, 1, 1, 1.2f);
			updateClass(cl);
		}

		@Override
		public void update() {
			super.update();

			if (brightness < 1.0f && brightness > MIN_BRIGHTNESS) {
				if ((brightness -= Game.elapsed) <= MIN_BRIGHTNESS) {
					brightness = MIN_BRIGHTNESS;
				}
				updateBrightness();
			}
		}

		public void highlight(boolean value) {
			if (value) {
				brightness = 1.0f;
				name.hardlight(highlighted);
			} else {
				brightness = 0.999f;
				name.hardlight(normal);
			}

			updateBrightness();
		}

		private void updateBrightness() {
			avatar.gm = avatar.bm = avatar.rm = avatar.am = brightness;
		}
	}

	private class ChallengeButton extends Button {

		private Image image;

		public ChallengeButton() {
			super();

			width = image.width;
			height = image.height;

			image.am = Badges.isUnlocked(Badges.Badge.VICTORY) ? 1.0f : 0.5f;
		}

		@Override
		protected void createChildren() {

			super.createChildren();

			image = Icons
					.get(PixelPonies.challenges() > 0 ? Icons.CHALLENGE_ON
							: Icons.CHALLENGE_OFF);
			add(image);
		}

		@Override
		protected void layout() {

			super.layout();

			image.x = align(x);
			image.y = align(y);
		}

		@Override
		protected void onClick() {
			if (Badges.isUnlocked(Badges.Badge.VICTORY)) {
				StartScene.this.add(new WndChallenges(
						PixelPonies.challenges(), true) {
					public void onBackPressed() {
						super.onBackPressed();
						image.copy(Icons.get(PixelPonies.challenges() > 0 ? Icons.CHALLENGE_ON
								: Icons.CHALLENGE_OFF));
					}
				});
			} else {
				StartScene.this.add(new WndMessage(TXT_WIN_THE_GAME));
			}
		}

		@Override
		protected void onTouchDown() {
			Sample.INSTANCE.play(Assets.SND_CLICK);
		}
	}
}
