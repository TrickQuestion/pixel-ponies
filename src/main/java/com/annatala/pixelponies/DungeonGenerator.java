package com.annatala.pixelponies;

import com.annatala.pixelponies.android.util.JsonHelper;
import com.annatala.pixelponies.android.util.TrackedRuntimeException;
import com.annatala.pixelponies.levels.FakeLastLevel;
import com.annatala.pixelponies.levels.GutsLevel;
import com.annatala.pixelponies.levels.NecroBossLevel;
import com.annatala.pixelponies.levels.NecroLevel;
import com.annatala.pixelponies.levels.PredesignedLevel;
import com.annatala.pixelponies.levels.ShadowLordLevel;
import com.annatala.pixelponies.android.EventCollector;
import com.annatala.pixelponies.levels.SpiderLevel;
import com.annatala.pixelponies.levels.CavesBossLevel;
import com.annatala.pixelponies.levels.CavesLevel;
import com.annatala.pixelponies.levels.CityBossLevel;
import com.annatala.pixelponies.levels.CityLevel;
import com.annatala.pixelponies.levels.DeadEndLevel;
import com.annatala.pixelponies.levels.HallsBossLevel;
import com.annatala.pixelponies.levels.HallsLevel;
import com.annatala.pixelponies.levels.LastLevel;
import com.annatala.pixelponies.levels.LastShopLevel;
import com.annatala.pixelponies.levels.Level;
import com.annatala.pixelponies.levels.PrisonBossLevel;
import com.annatala.pixelponies.levels.PrisonLevel;
import com.annatala.pixelponies.levels.SewerBossLevel;
import com.annatala.pixelponies.levels.SewerLevel;
import com.annatala.pixelponies.windows.WndStory;
import com.annatala.utils.GLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class DungeonGenerator {
	public static final String START_LEVEL 	  = "PredesignedLevel";
	public static final String DEAD_END_LEVEL = "DeadEndLevel";
	public static final String HALLS_LEVEL    = "HallsLevel";
	public static final String CITY_LEVEL     = "CityLevel";
	public static final String CAVES_LEVEL    = "CavesLevel";
	public static final String PRISON_LEVEL   = "PrisonLevel";
	public static final String SEWER_LEVEL    = "SewerLevel";
	public static final String SPIDER_LEVEL   = "SpiderLevel";
	public static final String GUTS_LEVEL     = "GutsLevel";

	static JSONObject mDungeonMap;
	static JSONObject mLevels;
	static JSONObject mGraph;

	static private HashMap<String, Class<? extends Level>> mLevelKindList;
	private static HashMap<String, Integer>                mStoryMap;

	static {
		initLevelsMap();
	}
	private static void registerLevelClass(Class<? extends Level> levelClass) {
		mLevelKindList.put(levelClass.getSimpleName(), levelClass);
	}

	private static void initLevelsMap() {
		if(PixelPonies.isAlpha()) {
			mDungeonMap = JsonHelper.readFile("levelsDesc/Dungeon_alpha.json");
		} else {
			mDungeonMap = JsonHelper.readFile("levelsDesc/Dungeon.json");
		}


		try {
			mLevels = mDungeonMap.getJSONObject("Levels");
			mGraph = mDungeonMap.getJSONObject("Graph");
		} catch (JSONException e) {
			throw new TrackedRuntimeException(e);
		}

		mLevelKindList = new HashMap<>();

		registerLevelClass(SewerLevel.class);
		registerLevelClass(SewerBossLevel.class);
		registerLevelClass(SpiderLevel.class);
		registerLevelClass(PrisonLevel.class);
		registerLevelClass(PrisonBossLevel.class);
		registerLevelClass(CavesLevel.class);
		registerLevelClass(CavesBossLevel.class);
		registerLevelClass(CityLevel.class);
		registerLevelClass(CityBossLevel.class);
		registerLevelClass(LastShopLevel.class);
		registerLevelClass(HallsLevel.class);
		registerLevelClass(HallsBossLevel.class);
		registerLevelClass(LastLevel.class);
		registerLevelClass(DeadEndLevel.class);

		registerLevelClass(PredesignedLevel.class);
		registerLevelClass(GutsLevel.class);
		registerLevelClass(ShadowLordLevel.class);
		registerLevelClass(FakeLastLevel.class);

		registerLevelClass(NecroLevel.class);
		registerLevelClass(NecroBossLevel.class);
	}

	public static String getEntryLevelKind() {
		try {
			return mLevels.getJSONObject(getEntryLevel()).getString("kind");
		} catch (JSONException e) {
			throw new TrackedRuntimeException(e);
		}
	}

	public static String getEntryLevel() {
		try {
			return mDungeonMap.getString("Entrance");
		} catch (JSONException e) {
			throw new TrackedRuntimeException(e);
		}
	}

	public static int exitCount(String levelId) {
		try {
			return mGraph.getJSONArray(levelId).getJSONArray(0).length();
		} catch (JSONException e) {
			throw new TrackedRuntimeException(e);
		}
	}

	public static Position ascend(Position current) {
		return descendOrAscend(current, false);
	}

	private static Position descendOrAscend(Position current, boolean descend) {
		try {

			if (current.levelId.equals("unknown")) {
				current.levelId = guessLevelId(current.levelKind, current.levelDepth);
			}

			JSONArray currentLevel = mGraph.getJSONArray(current.levelId);

			JSONArray nextLevelSet = currentLevel.getJSONArray(descend ? 0 : 1);
			Position next = new Position();
			int index = 0;

			next.cellId = -1;

			if (descend && !current.levelId.equals(getEntryLevel())) {
				if (Dungeon.level != null) { // not first descend
					if(Dungeon.level.isExit(current.cellId)) {
						index = Dungeon.level.exitIndex(current.cellId);
						next.cellId = -(index + 1);
					}
				}
			}

			if (!descend) {
				if (currentLevel.length() > 2) {
					int exitIndex = currentLevel.getJSONArray(2).getInt(0);
					next.cellId = -exitIndex;
				}
			}

			if(index >= nextLevelSet.length()) {
				index = 0;
				EventCollector.logEvent("DungeonGenerator","wrong next level index");
			}

			String nextLevelId = nextLevelSet.getString(index);

			JSONObject nextLevelDesc = mLevels.getJSONObject(nextLevelId);

			next.levelId    = nextLevelId;
			next.levelDepth = nextLevelDesc.getInt("depth");
			next.levelKind  = nextLevelDesc.getString("kind");

			JSONArray levelSize = nextLevelDesc.getJSONArray("size");
			next.xs = levelSize.getInt(0);
			next.ys = levelSize.getInt(1);

			return next;
		} catch (JSONException e) {
			throw new TrackedRuntimeException(e);
		}

	}

	public static Position descend(Position current) {
		return descendOrAscend(current, true);
	}

	public static Level createLevel(Position pos) {
		Class<? extends Level> levelClass = mLevelKindList.get(pos.levelKind);
		if (levelClass == null) {
			GLog.w("Unknown level type: %s", pos.levelKind);
			pos.levelKind = DEAD_END_LEVEL;

			return createLevel(pos);
		}
		try {
			Level ret;
			if (levelClass == PredesignedLevel.class) {
				String levelFile = mLevels.getJSONObject(pos.levelId).getString("file");
				ret = new PredesignedLevel(levelFile);
			} else {
				ret = levelClass.newInstance();
			}
			ret.levelId = pos.levelId;
			return ret;
		} catch (InstantiationException | IllegalAccessException | JSONException e) {
			throw new TrackedRuntimeException(e);
		}
	}

	public static void showStory(Level level) {
		if (mStoryMap == null) {
			mStoryMap = new HashMap<>();
			mStoryMap.put(START_LEVEL, WndStory.ID_START);
			mStoryMap.put(SEWER_LEVEL, WndStory.ID_SEWERS);
			mStoryMap.put(SPIDER_LEVEL, WndStory.ID_SPIDERS);
			mStoryMap.put(PRISON_LEVEL, WndStory.ID_PRISON);
			mStoryMap.put(CAVES_LEVEL, WndStory.ID_CAVES);
			mStoryMap.put(CITY_LEVEL, WndStory.ID_METROPOLIS);
			mStoryMap.put(HALLS_LEVEL, WndStory.ID_HALLS);
			mStoryMap.put(GUTS_LEVEL, WndStory.ID_GUTS);
		}

		Integer id = mStoryMap.get(level.levelKind());
		if (id == null) {
			return;
		}

		WndStory.showChapter(id);
	}

	public static String guessLevelId(String levelKind, int levelDepth) {
		try {
			JSONArray ids = mLevels.names();
			for (int i = 0; i < ids.length(); i++) {
				String id = ids.getString(i);
				JSONObject levelDesc = mLevels.getJSONObject(id);

				if (levelDesc.getString("kind").equals(levelKind)) {
					if (levelDesc.getInt("depth") == levelDepth) {
						return id;
					}
				}
			}
		} catch (JSONException e) {
			throw new TrackedRuntimeException(e);
		}
		return "1";
	}
}
