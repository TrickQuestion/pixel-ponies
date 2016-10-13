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

import com.annatala.pixelponies.android.util.ModdingMode;
import com.annatala.pixelponies.android.util.TrackedRuntimeException;
import com.annatala.pixelponies.android.EventCollector;
import com.annatala.pixelponies.android.R;
import com.annatala.pixelponies.DungeonGenerator;
import com.annatala.noosa.Camera;
import com.annatala.noosa.Game;
import com.annatala.noosa.Group;
import com.annatala.noosa.SkinnedBlock;
import com.annatala.noosa.Text;
import com.annatala.noosa.Visual;
import com.annatala.noosa.audio.Music;
import com.annatala.noosa.audio.Sample;
import com.annatala.noosa.particles.Emitter;
import com.annatala.pixelponies.Assets;
import com.annatala.pixelponies.Badges;
import com.annatala.pixelponies.Dungeon;
import com.annatala.pixelponies.DungeonTilemap;
import com.annatala.pixelponies.FogOfWar;
import com.annatala.pixelponies.PixelPonies;
import com.annatala.pixelponies.Statistics;
import com.annatala.pixelponies.actors.Actor;
import com.annatala.pixelponies.actors.blobs.Blob;
import com.annatala.pixelponies.actors.mobs.Mob;
import com.annatala.pixelponies.effects.BannerSprites;
import com.annatala.pixelponies.effects.BlobEmitter;
import com.annatala.pixelponies.effects.EmoIcon;
import com.annatala.pixelponies.effects.Flare;
import com.annatala.pixelponies.effects.FloatingText;
import com.annatala.pixelponies.effects.Ripple;
import com.annatala.pixelponies.effects.SpellSprite;
import com.annatala.pixelponies.effects.SystemFloatingText;
import com.annatala.pixelponies.items.Heap;
import com.annatala.pixelponies.items.Item;
import com.annatala.pixelponies.items.wands.WandOfBlink;
import com.annatala.pixelponies.levels.RegularLevel;
import com.annatala.pixelponies.levels.features.Chasm;
import com.annatala.pixelponies.plants.Plant;
import com.annatala.pixelponies.sprites.CharSprite;
import com.annatala.pixelponies.sprites.DiscardedItemSprite;
import com.annatala.pixelponies.sprites.HeroSpriteDef;
import com.annatala.pixelponies.sprites.ItemSprite;
import com.annatala.pixelponies.sprites.PlantSprite;
import com.annatala.pixelponies.ui.AttackIndicator;
import com.annatala.pixelponies.ui.Banner;
import com.annatala.pixelponies.ui.BusyIndicator;
import com.annatala.pixelponies.ui.GameLog;
import com.annatala.pixelponies.ui.HealthIndicator;
import com.annatala.pixelponies.ui.QuickSlot;
import com.annatala.pixelponies.ui.ResumeIndicator;
import com.annatala.pixelponies.ui.StatusPane;
import com.annatala.pixelponies.ui.Toast;
import com.annatala.pixelponies.ui.Toolbar;
import com.annatala.pixelponies.ui.Window;
import com.annatala.utils.GLog;
import com.annatala.pixelponies.windows.WndBag;
import com.annatala.pixelponies.windows.WndBag.Mode;
import com.annatala.pixelponies.windows.WndGame;
import com.annatala.utils.Random;

import java.io.IOException;
import java.util.HashSet;

public class GameScene extends PixelScene {

	private static final String TXT_WELCOME      = Game.getVar(R.string.GameScene_Welcome);
	private static final String TXT_WELCOME_BACK = Game.getVar(R.string.GameScene_WelcomeBack);
	private static final String TXT_NIGHT_MODE   = Game.getVar(R.string.GameScene_NightMode);

	private static final String TXT_CHASM   = Game.getVar(R.string.GameScene_Chasm);
	private static final String TXT_WATER   = Game.getVar(R.string.GameScene_Water);
	private static final String TXT_GRASS   = Game.getVar(R.string.GameScene_Grass);
	private static final String TXT_SECRETS = Game.getVar(R.string.GameScene_Secrets);

	private static volatile GameScene scene;

	private SkinnedBlock   water;
	private DungeonTilemap tiles;
	private FogOfWar       fog;
	private HeroSpriteDef  heroSprite;

	private GameLog log;

	private static CellSelector cellSelector;

	private Group ripples;
	private Group plants;
	private Group heaps;
	private Group mobs;
	private Group emitters;
	private Group effects;
	private Group gases;
	private Group spells;
	private Group statuses;
	private Group emoicons;

	private Toolbar toolbar;
	private Toast   prompt;

	private volatile boolean sceneCreated = false;

	@Override
	public void create() {

		Music.INSTANCE.play(Assets.TUNE, true);
		Music.INSTANCE.volume(1f);

		PixelPonies.lastClass(Dungeon.hero.heroClass.ordinal());

		super.create();

		Camera.main.zoom(defaultZoom + PixelPonies.zoom());

		scene = this;

		Group terrain = new Group();
		add(terrain);

		water = new SkinnedBlock(Dungeon.level.getWidth() * DungeonTilemap.SIZE,
				Dungeon.level.getHeight() * DungeonTilemap.SIZE, Dungeon.level.waterTex());
		terrain.add(water);

		ripples = new Group();
		terrain.add(ripples);

		tiles = new DungeonTilemap(Dungeon.level.tilesTex(), Dungeon.level.tilesTexEx());
		terrain.add(tiles);

		Dungeon.level.addVisuals(this);

		plants = new Group();
		add(plants);

		int size = Dungeon.level.plants.size();
		for (int i = 0; i < size; i++) {
			addPlantSprite(Dungeon.level.plants.valueAt(i));
		}

		heaps = new Group();
		add(heaps);

		for (Heap heap : Dungeon.level.allHeaps()) {
			addHeapSprite(heap);
		}

		emitters = new Group();
		effects = new Group();
		emoicons = new Group();

		mobs = new Group();
		add(mobs);

		// hack to save bugged saves...
		boolean buggedSave = false;
		HashSet<Mob> filteredMobs = new HashSet<>();
		for (Mob mob : Dungeon.level.mobs) {
			if (mob.getPos() != -1) {
				filteredMobs.add(mob);
			}  else {
				buggedSave = true;
			}
		}

		if(buggedSave) {
			EventCollector.logEvent("bug","bugged save","mob.pos==-1");
		}

		Dungeon.level.mobs = filteredMobs;

		for (Mob mob : Dungeon.level.mobs) {
			addMobSprite(mob);
			if (Statistics.amuletObtained) {
				mob.beckon(Dungeon.hero.getPos());
			}
		}

		add(emitters);
		add(effects);

		gases = new Group();
		add(gases);

		for (Blob blob : Dungeon.level.blobs.values()) {
			blob.emitter = null;
			addBlobSprite(blob);
		}

		fog = new FogOfWar(Dungeon.level.getWidth(), Dungeon.level.getHeight());
		fog.updateVisibility(Dungeon.visible, Dungeon.level.visited, Dungeon.level.mapped);
		add(fog);

		brightness(PixelPonies.brightness());

		spells = new Group();
		add(spells);

		statuses = new Group();
		add(statuses);

		add(emoicons);

		add(new HealthIndicator());

		add(cellSelector = new CellSelector(tiles));

		scene.heroSprite = new HeroSpriteDef(Dungeon.hero, true);
		scene.heroSprite.place(Dungeon.hero.getPos());
		Dungeon.hero.updateLook();

		scene.mobs.add(scene.heroSprite);

		StatusPane sb = new StatusPane(Dungeon.hero);
		sb.camera = uiCamera;
		sb.setSize(uiCamera.width, 0);
		add(sb);

		toolbar = new Toolbar();
		toolbar.camera = uiCamera;
		toolbar.setRect(0, uiCamera.height - toolbar.height(), uiCamera.width, toolbar.height());
		add(toolbar);

		AttackIndicator attack = new AttackIndicator();
		attack.camera = uiCamera;
		attack.setPos(uiCamera.width - attack.width(), toolbar.top() - attack.height());
		add(attack);

		ResumeIndicator resume = new ResumeIndicator();
		resume.camera = uiCamera;
		resume.setPos(uiCamera.width - resume.width(), attack.top() - resume.height());
		add(resume);

		log = new GameLog();
		log.camera = uiCamera;
		log.setRect(0, toolbar.top(), attack.left(), 0);
		add(log);

		if (Dungeon.depth < Statistics.deepestFloor) {
			GLog.i(TXT_WELCOME_BACK, Dungeon.depth);
		} else {
			GLog.i(TXT_WELCOME, Dungeon.depth);
			Sample.INSTANCE.play(Assets.SND_DESCEND);
		}
		switch (Dungeon.level.feeling) {
			case CHASM:
				GLog.w(TXT_CHASM);
				break;
			case WATER:
				GLog.w(TXT_WATER);
				break;
			case GRASS:
				GLog.w(TXT_GRASS);
				break;
			default:
		}
		if (Dungeon.level instanceof RegularLevel
				&& ((RegularLevel) Dungeon.level).secretDoors > Random.IntRange(3, 4)) {
			GLog.w(TXT_SECRETS);
		}
		if (Dungeon.nightMode && !Dungeon.bossLevel()) {
			GLog.w(TXT_NIGHT_MODE);
		}

		BusyIndicator busy = new BusyIndicator();
		busy.camera = uiCamera;
		busy.x = 1;
		busy.y = sb.bottom() + 1;
		add(busy);

		sceneCreated = true;

		switch (InterlevelScene.mode) {
			case RESURRECT:
				WandOfBlink.appear(Dungeon.hero, Dungeon.level.entrance);
				new Flare(8, 32).color(0xFFFF66, true).show(heroSprite, 2f);
				break;
			case RETURN:
				WandOfBlink.appear(Dungeon.hero, Dungeon.hero.getPos());
				break;
			case FALL:
				Chasm.heroLand();
				break;
			case DESCEND:

				DungeonGenerator.showStory(Dungeon.level);

				if (Dungeon.hero.isAlive() && Dungeon.depth != 22) {
					Badges.validateNoKilling();
				}
				break;
			default:
		}

		Camera.main.target = heroSprite;
		fadeIn();
		Dungeon.observe();
	}

	public void destroy() {

		scene = null;
		Badges.saveGlobal();

		super.destroy();
	}

	@Override
	public synchronized void pause() {
		try {
			Dungeon.saveAll();
			Badges.saveGlobal();
		} catch (IOException e) {
			throw new TrackedRuntimeException(e);
		}
	}

	@Override
	public synchronized void update() {
		if (!sceneCreated) {
			return;
		}

		if (Dungeon.hero == null) {
			return;
		}

		if(Dungeon.level == null) {
			return;
		}

		super.update();

		water.offset(0, -5 * Game.elapsed);

		Actor.process(Game.elapsed);

		if (Dungeon.hero.isReady() && !Dungeon.hero.paralysed) {
			log.newLine();
		}

		if (!PixelPonies.realtime()) {
			cellSelector.enabled = Dungeon.hero.isReady();
		} else {
			cellSelector.enabled = Dungeon.hero.isAlive();
		}

	}

	@Override
	protected void onBackPressed() {
		if (!cancel()) {
			add(new WndGame());
		}
	}

	@Override
	protected void onMenuPressed() {
		if (Dungeon.hero.isReady()) {
			selectItem(null, WndBag.Mode.ALL, null);
		}
	}

	public void brightness(boolean value) {
		if (fog != null) {
			water.rm = water.gm = water.bm = tiles.rm = tiles.gm = tiles.bm = value ? 1.5f : 1.0f;

			if (value) {
				fog.am = +2f;
				fog.aa = -1f;
			} else {
				fog.am = +1f;
				fog.aa = 0f;
			}
		}
	}

	private void addHeapSprite(Heap heap) {
		ItemSprite sprite = heap.sprite = (ItemSprite) heaps.recycle(ItemSprite.class);
		sprite.revive();
		sprite.link(heap);
		heaps.add(sprite);
	}

	private void addDiscardedSprite(Heap heap) {
		heap.sprite = (DiscardedItemSprite) heaps.recycle(DiscardedItemSprite.class);
		heap.sprite.revive();
		heap.sprite.link(heap);
		heaps.add(heap.sprite);
	}

	private void addPlantSprite(Plant plant) {
		(plant.sprite = (PlantSprite) plants.recycle(PlantSprite.class)).reset(plant);
	}

	private static void addBlobSprite(final Blob gas) {
		if(isSceneReady())
		if (gas.emitter == null) {
			scene.gases.add(new BlobEmitter(gas));
		}
	}

	public static void addMobSprite(Mob mob) {
		CharSprite sprite = mob.sprite();
		sprite.setVisible(Dungeon.visible[mob.getPos()]);
		scene.mobs.add(sprite);
		sprite.link(mob);
	}

	private void prompt(String text) {

		if (prompt != null) {
			prompt.killAndErase();
			prompt = null;
		}

		if (text != null) {
			prompt = new Toast(text) {
				@Override
				protected void onClose() {
					cancel();
				}
			};
			prompt.camera = uiCamera;
			prompt.setPos((uiCamera.width - prompt.width()) / 2, uiCamera.height - 60);
			add(prompt);
		}
	}

	private void showBanner(Banner banner) {
		banner.camera = uiCamera;
		banner.x = align(uiCamera, (uiCamera.width - banner.width) / 2);
		banner.y = align(uiCamera, (uiCamera.height - banner.height) / 3);
		add(banner);
	}

	// -------------------------------------------------------

	public static void add(Plant plant) {
		if (scene != null && Dungeon.level != null) {
			scene.addPlantSprite(plant);
		} else {
			EventCollector.logException(new Exception("add(Plant)"));
		}
	}

	public static void add(Blob gas) {
		if (scene != null && Dungeon.level != null) {
			Actor.add(gas);
			addBlobSprite(gas);
		} else {
			EventCollector.logException(new Exception("add(Blob)"));
		}
	}

	public static void add(Heap heap) {
		if (scene != null && Dungeon.level != null) {
			scene.addHeapSprite(heap);
		} else {
			EventCollector.logException(new Exception("add(Heap)"));
		}
	}

	public static void discard(Heap heap) {
		if (scene != null && Dungeon.level != null) {
			scene.addDiscardedSprite(heap);
		} else {
			EventCollector.logException(new Exception("discard(Heap)"));
		}
	}

	public static boolean isSceneReady() {
		return scene != null && Dungeon.level != null;
	}

	public static void add(EmoIcon icon) {
		scene.emoicons.add(icon);
	}

	public static void effect(Visual effect) {
		scene.effects.add(effect);
	}

	public static Ripple ripple(int pos) {
		Ripple ripple = (Ripple) scene.ripples.recycle(Ripple.class);
		ripple.reset(pos);
		return ripple;
	}

	public static SpellSprite spellSprite() {
		return (SpellSprite) scene.spells.recycle(SpellSprite.class);
	}

	public static Emitter emitter() {
		if (scene != null) {
			Emitter emitter = (Emitter) scene.emitters.recycle(Emitter.class);
			emitter.revive();
			return emitter;
		} else {
			return null;
		}
	}

	public static Text status() {
		if (ModdingMode.getClassicTextRenderingMode()) {
			return scene != null ? (FloatingText) scene.statuses.recycle(FloatingText.class) : null;
		} else {
			return scene != null ? (SystemFloatingText) scene.statuses.recycle(SystemFloatingText.class) : null;
		}
	}

	public static void pickUp(Item item) {
		scene.toolbar.pickup(item);
	}

	public static void updateMap() {
		if (scene != null && Dungeon.level != null) {
			scene.tiles.updateAll();
		} else {
			EventCollector.logException(new Exception("updateMap"));
		}
	}

	public static void updateMap(int cell) {
		if (scene != null && Dungeon.level != null) {
			scene.tiles.updateCell(cell);
		}else {
			EventCollector.logException(new Exception("updateMap(int)"));
		}
	}

	public static void discoverTile(int pos, int oldValue) {
		if (scene != null && Dungeon.level != null) {
			scene.tiles.discover(pos, oldValue);
		}else{
			EventCollector.logException(new Exception("discoverTile"));
		}
	}

	public static void show(Window wnd) {
		cancelCellSelector();
		scene.add(wnd);
	}

	public static void afterObserve() {
		if (scene != null && scene.sceneCreated) {
			scene.fog.updateVisibility(Dungeon.visible, Dungeon.level.visited, Dungeon.level.mapped);

			for (Mob mob : Dungeon.level.mobs) {
				mob.getSprite().setVisible(Dungeon.visible[mob.getPos()]);
			}
		}else {
			EventCollector.logException(new Exception("afterObserve()"));
		}
	}

	public static void flash(int color) {
		scene.fadeIn(0xFF000000 | color, true);
	}

	public static void gameOver() {
		Banner gameOver = new Banner(BannerSprites.get(BannerSprites.Type.GAME_OVER));
		gameOver.show(0x000000, 1f);
		scene.showBanner(gameOver);

		Sample.INSTANCE.play(Assets.SND_DEATH);
	}

	public static void bossSlain() {
		if (Dungeon.hero.isAlive()) {
			Banner bossSlain = new Banner(BannerSprites.get(BannerSprites.Type.BOSS_SLAIN));
			bossSlain.show(0xFFFFFF, 0.3f, 5f);
			scene.showBanner(bossSlain);

			Sample.INSTANCE.play(Assets.SND_BOSS);
		}
	}

	public static void handleCell(int cell) {
		cellSelector.select(cell);
	}

	public static void selectCell(CellSelector.Listener listener) {
		cellSelector.listener = listener;
		scene.prompt(listener.prompt());
	}

	private static boolean cancelCellSelector() {
		if (cellSelector != null && cellSelector.listener != null && cellSelector.listener != defaultCellListener) {
			cellSelector.cancel();
			return true;
		} else {
			return false;
		}
	}

	public static WndBag selectItem(WndBag.Listener listener, WndBag.Mode mode, String title) {
		cancelCellSelector();

		WndBag wnd = mode == Mode.SEED ? WndBag.seedPouch(listener, mode, title)
				: WndBag.lastBag(listener, mode, title);
		scene.add(wnd);

		return wnd;
	}

	static boolean cancel() {
		if (Dungeon.hero != null && (Dungeon.hero.curAction != null || Dungeon.hero.restoreHealth)) {

			Dungeon.hero.curAction = null;
			Dungeon.hero.restoreHealth = false;
			return true;

		} else {

			return cancelCellSelector();

		}
	}

	public static void ready() {
		selectCell(defaultCellListener);
		QuickSlot.cancel();
	}

	private static final CellSelector.Listener defaultCellListener = new CellSelector.Listener() {
		@Override
		public void onSelect(Integer cell) {
			if (Dungeon.hero.handle(cell)) {
				// Actor.next();
				Dungeon.hero.next();
			}
		}

		@Override
		public String prompt() {
			return null;
		}
	};

	public void updateToolbar() {
		if (toolbar != null) {
			toolbar.updateLayout();
		} else {
			EventCollector.logException(new Exception("updateToolbar(int)"));
		}
	}

	@Override
	public void resume() {
		super.resume();
		afterObserve();
	}
}
