/*
t * Pixel Dungeon
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
package com.annatala.pixelponies;

import android.support.annotation.NonNull;

import com.annatala.pixelponies.android.util.FileSystem;
import com.annatala.pixelponies.android.util.Scrambler;
import com.annatala.pixelponies.android.EventCollector;
import com.annatala.pixelponies.android.R;
import com.annatala.pixelponies.actors.mobs.npcs.AzuterronNPC;
import com.annatala.noosa.Game;
import com.annatala.noosa.audio.Sample;
import com.annatala.pixelponies.Rankings.gameOver;
import com.annatala.pixelponies.actors.Actor;
import com.annatala.pixelponies.actors.Char;
import com.annatala.pixelponies.actors.buffs.Amok;
import com.annatala.pixelponies.actors.buffs.Invisibility;
import com.annatala.pixelponies.actors.buffs.Light;
import com.annatala.pixelponies.actors.hero.Hero;
import com.annatala.pixelponies.actors.hero.HeroClass;
import com.annatala.pixelponies.actors.mobs.Mimic;
import com.annatala.pixelponies.actors.mobs.Mob;
import com.annatala.pixelponies.actors.mobs.npcs.Blacksmith;
import com.annatala.pixelponies.actors.mobs.npcs.Ghost;
import com.annatala.pixelponies.actors.mobs.npcs.Imp;
import com.annatala.pixelponies.actors.mobs.npcs.WandMaker;
import com.annatala.pixelponies.effects.Speck;
import com.annatala.pixelponies.items.utility.Ankh;
import com.annatala.pixelponies.items.Heap;
import com.annatala.pixelponies.items.potions.Potion;
import com.annatala.pixelponies.items.rings.Ring;
import com.annatala.pixelponies.items.scrolls.Scroll;
import com.annatala.pixelponies.items.wands.Wand;
import com.annatala.pixelponies.levels.DeadEndLevel;
import com.annatala.pixelponies.levels.Level;
import com.annatala.pixelponies.levels.Room;
import com.annatala.pixelponies.scenes.GameScene;
import com.annatala.utils.BArray;
import com.annatala.utils.GLog;
import com.annatala.pixelponies.windows.WndResurrect;
import com.annatala.utils.Bundle;
import com.annatala.utils.PathFinder;
import com.annatala.utils.Random;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;

public class Dungeon {

	public static int 	  potionsOfHonesty;
	public static int     scrollsOfUpgrade;
	public static int	  scrollsOfLoyalOath;
	public static int	  spellbooks;
	public static int     arcaneStyli;
	public static boolean dewVial; 			// true iff the dew vial can be spawned
	public static int     transmutation; 	// depth number for a well of transmutation
	public static int[]   comedyItems;		// depths at which comedy items should be spawned
	public static final int RUBBER_CHICKEN = 0;
	public static final int CREAM_PIE = 1;
	public static final int FUNNY_GLASSES = 2;
	public static final int SELTZER_BOTTLE = 3;

	public static int challenges;

	public static Hero  hero;
	public static Level level;

	public static int depth;
	private static int scrambledGold;
	private static boolean loading = false;


	public static HashSet<Integer> chapters;

	// Hero's field of view
	public static boolean[] visible;

	public static boolean nightMode;

	private static boolean[] passable;

	public static HeroClass heroClass;

	private static void initSizeDependentStuff(int w, int h) {
		int size = w * h;
		Actor.clear();
		visible = new boolean[size];
		passable = new boolean[size];

		Arrays.fill(visible, false);

		PathFinder.setMapSize(w, h);
	}

	public static void init() {
		challenges = PixelPonies.challenges();

		Scroll.initLabels();
		Potion.initColors();
		Wand.initWoods();
		Ring.initGems();

		Statistics.reset();
		Journal.reset();

		depth = 0;
		gold(0);

		potionsOfHonesty = 0;
		scrollsOfUpgrade = 0;
		scrollsOfLoyalOath = 0;
		spellbooks = 0;
		arcaneStyli = 0;
		dewVial = true;
		transmutation = Random.IntRange(6, 14);

		comedyItems = new int[4];
		resetComedy();

		chapters = new HashSet<>();

		Ghost.Quest.reset();
		WandMaker.Quest.reset();
		Blacksmith.Quest.reset();
		Imp.Quest.reset();

		Room.shuffleTypes();

		hero = new Hero(difficulty);

		Badges.reset();

		heroClass.initHero(hero);

		hero.levelKind = DungeonGenerator.getEntryLevelKind();
		hero.levelId = DungeonGenerator.getEntryLevel();

		SaveUtils.deleteLevels(heroClass);
	}

	public static boolean isChallenged(int mask) {
		return (challenges & mask) != 0;
	}

	private static void resetComedy() {
		for (int i = 0 ; i < 4 ; i++) {
			comedyItems[i] = -1;
		}
		for (int i = 0 ; i < 4 ; i++) {
			int tier = Random.Int(5);
			while (tier == comedyItems[0] || tier == comedyItems[1] || tier == comedyItems[2] ||
					tier == comedyItems[3]) {
				tier = Random.Int(5);
			}
			comedyItems[i] = tier;
		}
		for (int i = 0 ; i < 4 ; i++) {
			comedyItems[i] = comedyItems[i] * 5 + Random.Int(4);
			if (comedyItems[i] == 21 || comedyItems[i] == 25 || comedyItems[i] == 26) {
				comedyItems[i] += Random.Int(3);
			}
		}
	}

	private static void updateStatistics() {
		if (depth > Statistics.deepestFloor) {
			Statistics.deepestFloor = depth;

			Statistics.completedWithNoKilling = Statistics.qualifiedForNoKilling;
		}
	}

	@NonNull
	public static Level newLevel(Position pos) {

		Dungeon.level = null;
		updateStatistics();
		GLog.toFile("creating level: %s %s %d", pos.levelId, pos.levelKind, pos.levelDepth);
		Level level = DungeonGenerator.createLevel(pos);

		initSizeDependentStuff(pos.xs, pos.ys);

		level.create(pos.xs, pos.ys);

		Statistics.qualifiedForNoKilling = !level.isBossLevel();

		return level;
	}

	public static void resetLevel() {

		initSizeDependentStuff(level.getWidth(), level.getHeight());

		level.reset();
		switchLevel(level, level.entrance, hero.levelId);
	}

	public static String tip() {
		return level.getSign(hero.getPos()%level.getWidth(), hero.getPos()/level.getWidth());
	}

	public static boolean shopOnLevel() {
		if (hero.levelKind.equals("NecroLevel")){
			return false;
		} else{
			return depth == 6 || depth == 11 || depth == 16 || depth == 27;
		}

	}

	public static boolean bossLevel() {
		return Dungeon.level != null && Dungeon.level.isBossLevel();
	}

	@SuppressWarnings("deprecation")
	public static void switchLevel(final Level level, int pos, String levelId) {

		nightMode = new Date().getHours() < 7;

		Actor.init(level);

		Actor respawner = level.respawner();
		if (respawner != null) {
			Actor.add(level.respawner());
		}

		hero.setPos(pos);
		hero.levelKind = level.levelKind();
		hero.levelId = levelId;

		if (!level.cellValid(hero.getPos())) {
			hero.setPos(level.entrance);
		}

		Light light = hero.buff(Light.class);
		hero.viewDistance = light == null ? level.viewDistance : Math.max(Light.DISTANCE, level.viewDistance);

		Dungeon.level = level;
	}

	public static boolean pohNeeded() {
		int[] quota = {4, 2, 9, 4, 14, 6, 19, 8, 24, 9};
		return chance(quota, potionsOfHonesty);
	}

	public static boolean soloNeeded() {
		int[] quota = {4, 2, 9, 4, 14, 6, 19, 8, 24, 9};
		return chance(quota, scrollsOfLoyalOath);
	}

	public static boolean sbNeeded() {
		int[] quota = {4, 2, 9, 4, 14, 6, 19, 8, 24, 9};
		return chance(quota, spellbooks);
	}

	public static boolean souNeeded() {
		int[] quota = {5, 3, 10, 6, 15, 9, 20, 12, 25, 13};
		return chance(quota, scrollsOfUpgrade);
	}

	private static boolean chance(int[] quota, int number) {

		for (int i = 0; i < quota.length; i += 2) {
			int qDepth = quota[i];
			if (depth <= qDepth) {
				int qNumber = quota[i + 1];
				return Random.Float() < (float) (qNumber - number) / (qDepth - depth + 1);
			}
		}

		return false;
	}

	public static boolean asNeeded() {
		return Random.Int(12 * (1 + arcaneStyli)) < depth;
	}

	private static final String VERSION    = "version";
	private static final String CHALLENGES = "challenges";
	private static final String HERO       = "hero";
	private static final String GOLD       = "gold";
	private static final String DEPTH      = "depth";
	private static final String LEVEL      = "level";
	private static final String P_HONESTY  = "potionsOfHonesty";
	private static final String SOU        = "scrollsOfUpgrade";
	private static final String SOLO	   = "scrollsOfLoyalOath";
	private static final String SB		   = "spellbooks";
	private static final String AS         = "arcaneStyli";
	private static final String DV         = "dewVial";
	private static final String WT         = "transmutation";
	private static final String CHAPTERS   = "chapters";
	private static final String QUESTS     = "quests";
	private static final String BADGES     = "badges";

	public static void gameOver() {
		Dungeon.deleteGame(true);
	}

	public static void saveGame(String fileName) throws IOException {
		Bundle bundle = new Bundle();

		bundle.put(VERSION, Game.version);
		bundle.put(CHALLENGES, challenges);
		bundle.put(HERO, hero);
		bundle.put(GOLD, gold());
		bundle.put(DEPTH, depth);

		bundle.put(P_HONESTY, potionsOfHonesty);
		bundle.put(SOU, scrollsOfUpgrade);
		bundle.put(SOLO, scrollsOfLoyalOath);
		bundle.put(SB, spellbooks);
		bundle.put(AS, arcaneStyli);
		bundle.put(DV, dewVial);
		bundle.put(WT, transmutation);

		int count = 0;
		int ids[] = new int[chapters.size()];
		for (Integer id : chapters) {
			ids[count++] = id;
		}
		bundle.put(CHAPTERS, ids);

		Bundle quests = new Bundle();
		Ghost.Quest.storeInBundle(quests);
		WandMaker.Quest.storeInBundle(quests);
		Blacksmith.Quest.storeInBundle(quests);
		Imp.Quest.storeInBundle(quests);
		AzuterronNPC.Quest.storeInBundle(quests);
		bundle.put(QUESTS, quests);

		Room.storeRoomsInBundle(bundle);

		Statistics.storeInBundle(bundle);
		Journal.storeInBundle(bundle);

		Scroll.save(bundle);
		Potion.save(bundle);
		Wand.save(bundle);
		Ring.save(bundle);

		Bundle badges = new Bundle();
		Badges.saveLocal(badges);
		bundle.put(BADGES, badges);

		GLog.toFile("saving game: %s", fileName);

		OutputStream output = new FileOutputStream(FileSystem.getInteralStorageFile(fileName));
		Bundle.write(bundle, output);
		output.close();
	}

	public static void saveLevel() throws IOException {
		Bundle bundle = new Bundle();
		bundle.put(LEVEL, level);

		Position current = currentPosition();

		String saveTo = SaveUtils.depthFileForSave(hero.heroClass, current.levelDepth, current.levelKind,
				current.levelId);

		GLog.toFile("saving level: %s", saveTo);

		OutputStream output = new FileOutputStream(FileSystem.getInteralStorageFile(saveTo));
		Bundle.write(bundle, output);
		output.close();
	}

	public static void saveAll() throws IOException {
		float MBytesAvaliable = Game.getAvailableInternalMemorySize() / 1024f / 1024f;

		if (MBytesAvaliable < 2) {
			Game.toast("Low memory condition");
			GLog.toFile("Low memory!!!");
		}

		GLog.toFile("Saving: %5.2f MBytes available", MBytesAvaliable);

		if (hero.isAlive()) {

			Actor.fixTime();
			saveGame(SaveUtils.gameFile(hero.heroClass));
			saveLevel();

			GamesInProgress.set(hero.heroClass, depth, hero.lvl());

		} else if (WndResurrect.instance != null) {

			WndResurrect.instance.hide();
			Hero.reallyDie(WndResurrect.causeOfDeath);
		}
	}

	public static void loadGame() throws IOException {
		GLog.toFile("load Game");
		loadGame(SaveUtils.gameFile(heroClass), true);
	}

	public static void loadGameForRankings(String fileName) throws IOException {
		loadGame(fileName, false);
	}

	public static void loadGameFromBundle(Bundle bundle, boolean fullLoad) {
		Dungeon.challenges = bundle.getInt(CHALLENGES);

		Dungeon.level = null;
		Dungeon.depth = -1;

		Scroll.restore(bundle);
		Potion.restore(bundle);
		Wand.restore(bundle);
		Ring.restore(bundle);

		potionsOfHonesty = bundle.getInt(P_HONESTY);
		scrollsOfUpgrade = bundle.getInt(SOU);
		scrollsOfLoyalOath = bundle.getInt(SOLO);
		spellbooks = bundle.getInt(SB);
		arcaneStyli = bundle.getInt(AS);
		dewVial = bundle.getBoolean(DV);
		transmutation = bundle.getInt(WT);

		if (fullLoad) {
			chapters = new HashSet<>();
			int ids[] = bundle.getIntArray(CHAPTERS);
			if (ids != null) {
				for (int id : ids) {
					chapters.add(id);
				}
			}

			Bundle quests = bundle.getBundle(QUESTS);
			if (!quests.isNull()) {
				Ghost.Quest.restoreFromBundle(quests);
				WandMaker.Quest.restoreFromBundle(quests);
				Blacksmith.Quest.restoreFromBundle(quests);
				Imp.Quest.restoreFromBundle(quests);
				AzuterronNPC.Quest.restoreFromBundle(quests);
			} else {
				Ghost.Quest.reset();
				WandMaker.Quest.reset();
				Blacksmith.Quest.reset();
				Imp.Quest.reset();
				AzuterronNPC.Quest.reset();
			}

			Room.restoreRoomsFromBundle(bundle);
		}

		Bundle badges = bundle.getBundle(BADGES);
		if (!badges.isNull()) {
			Badges.loadLocal(badges);
		} else {
			Badges.reset();
		}

		@SuppressWarnings("unused")
		String version = bundle.getString(VERSION);

		Dungeon.hero = (Hero) bundle.get(HERO);

		gold(bundle.getInt(GOLD));
		depth = bundle.getInt(DEPTH);

		Statistics.restoreFromBundle(bundle);
		Journal.restoreFromBundle(bundle);
	}

	public static void loadGame(String fileName, boolean fullLoad) throws IOException {
		GLog.toFile("load Game %s", fileName);

		Bundle bundle = gameBundle(fileName);

		loadGameFromBundle(bundle, fullLoad);
	}

	public static Level loadLevel(Position next) throws IOException {
		loading = true;

		String loadFrom = SaveUtils.depthFileForLoad(heroClass, next.levelDepth, next.levelKind, next.levelId);

		GLog.toFile("loading level: %s", loadFrom);

		InputStream input;

		if (FileSystem.getFile(loadFrom).exists()) {
			input = new FileInputStream(FileSystem.getFile(loadFrom));
			Dungeon.level = null;
		} else {
			GLog.toFile("File %s not found!", loadFrom);
			return newLevel(next);
		}

		Bundle bundle = Bundle.read(input);

		input.close();

		if (bundle == null) {
			EventCollector.logEvent("Dungeon.loadLevel","read fail");
			return newLevel(next);
		}

		Level level = Level.fromBundle(bundle, "level");

		if(level == null) {
			level = newLevel(next);
		}

		level.levelId = next.levelId;
		initSizeDependentStuff(level.getWidth(), level.getHeight());

		loading = false;

		return level;
	}

	public static void deleteGame(boolean deleteLevels) {
		GLog.toFile("deleteGame");
		SaveUtils.deleteGameFile(heroClass);

		if (deleteLevels) {
			SaveUtils.deleteLevels(heroClass);
		}

		GamesInProgress.delete(heroClass);
	}

	public static Bundle gameBundle(String fileName) throws IOException {

		InputStream input = new FileInputStream(FileSystem.getFile(fileName));

		Bundle bundle = Bundle.read(input);
		input.close();

		return bundle;
	}

	public static void preview(GamesInProgress.Info info, Bundle bundle) {
		info.depth = bundle.getInt(DEPTH);
		if (info.depth == -1) {
			info.depth = bundle.getInt("maxDepth"); // FIXME
		}
		Hero.preview(info, bundle.getBundle(HERO));
	}

	public static void fail(String desc) {
		if (hero.belongings.getItem(Ankh.class) == null) {
			Rankings.INSTANCE.submit(Rankings.gameOver.LOSE, desc);
		}
	}

	public static void win(String desc, gameOver kind) {

		if (challenges != 0) {
			Badges.validateChampion();
		}

		Rankings.INSTANCE.submit(kind, desc);
	}

	public static void observe() {

		if (level == null) {
			return;
		}

		level.updateFieldOfView(hero);
		System.arraycopy(level.fieldOfView, 0, visible, 0, visible.length);

		BArray.or(level.visited, visible, level.visited);

		if (GameScene.isSceneReady()) {
			GameScene.afterObserve();
		}
	}

	private static void markActorsAsUnpassableIgnoreFov() {
		for (Actor actor : Actor.all()) {
			if (actor instanceof Char) {
				int pos = ((Char) actor).getPos();
				passable[pos] = false;
			}
		}
	}

	private static void markActorsAsUnpassable(boolean[] visible) {
		for (Actor actor : Actor.all()) {
			if (actor instanceof Char) {
				int pos = ((Char) actor).getPos();
				if (visible[pos]) {
					passable[pos] = false;
				}
			}
		}
	}

	public static int findPath(Char ch, int from, int to, boolean pass[], boolean[] visible) {

		if (level.adjacent(from, to)) {
			return Actor.findChar(to) == null && (pass[to] || level.avoid[to]) ? to : -1;
		}

		if (ch.flying || ch.buff(Amok.class) != null) {
			BArray.or(pass, level.avoid, passable);
		} else {
			System.arraycopy(pass, 0, passable, 0, level.getLength());
		}

		if (visible != null) {
			markActorsAsUnpassable(visible);
		} else {
			markActorsAsUnpassableIgnoreFov();
		}

		return PathFinder.getStep(from, to, passable);

	}

	public static int flee(Char ch, int cur, int from, boolean pass[], boolean[] visible) {

		if (ch.flying) {
			BArray.or(pass, level.avoid, passable);
		} else {
			System.arraycopy(pass, 0, passable, 0, level.getLength());
		}

		if (visible != null) {
			markActorsAsUnpassable(visible);
		} else {
			markActorsAsUnpassableIgnoreFov();
		}

		passable[cur] = true;

		return PathFinder.getStepBack(cur, from, passable);
	}

	public static void challengeAllMobs(Char ch, String sound) {

		if (Dungeon.level == null) {
			return;
		}

		for (Mob mob : Dungeon.level.mobs) {
			mob.beckon(ch.getPos());
		}

		for (Heap heap : Dungeon.level.allHeaps()) {
			if (heap.type == Heap.Type.MIMIC) {
				Mimic m = Mimic.spawnAt(heap.pos, heap.items);
				if (m != null) {
					m.beckon(ch.getPos());
					heap.destroy();
				}
			}
		}

		ch.getSprite().centerEmitter().start(Speck.factory(Speck.SCREAM), 0.3f, 3);

		Sample.INSTANCE.play(sound);
		if (ch instanceof Hero) {
			Invisibility.dispel((Hero) ch);
		}
	}

	public static Position currentPosition() {
		return new Position(hero.levelKind, hero.levelId, depth, hero.getPos());
	}

	private static int difficulty;

	public static void setDifficulty(int _difficulty) {
		difficulty = _difficulty;
		PixelPonies.setDifficulty(difficulty);
	}

	public static int gold() {
		return Scrambler.descramble(scrambledGold);
	}

	public static void gold(int value) {
		scrambledGold = Scrambler.scramble(value);
	}

	public static boolean isLoading() {
		return loading;
	}
}
