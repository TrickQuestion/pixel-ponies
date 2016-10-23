package com.annatala.pixelponies.levels;

import com.annatala.pixelponies.android.util.JsonHelper;
import com.annatala.pixelponies.android.util.TrackedRuntimeException;
import com.annatala.pixelponies.items.ItemFactory;
import com.annatala.pixelponies.actors.mobs.common.MobFactory;
import com.annatala.pixelponies.actors.mobs.Mob;
import com.annatala.pixelponies.items.Item;
import com.annatala.utils.Utils;
import com.annatala.utils.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PredesignedLevel extends CommonLevel {

	private JSONObject mLevelDesc;
	private String     mDescFile;

	private final String descFileKey = "descFile";

	{
		color1 = 0x48763c;
		color2 = 0x59994a;
	}

	//for restoreFromBundle
	public PredesignedLevel() {
		super();
	}

	public PredesignedLevel(String fileName) {
		mDescFile = fileName;
		readDescFile(mDescFile);
	}

	private void readDescFile(String descFile) {
		mLevelDesc = JsonHelper.readFile(descFile);

		if (mLevelDesc == null) {
			throw new TrackedRuntimeException(Utils.format("Malformed level [%s] description", descFile));
		}
	}

	@Override
	public String tilesTex() {
		return mLevelDesc.optString("tiles", "tiles0.png");
	}

	public String tilesTexEx() {
		return mLevelDesc.optString("tiles_x", null);
	}

	public String waterTex() { return mLevelDesc.optString("water", "water0.png"); }

	@Override
	public void create(int w, int h) {
		try {
			width = mLevelDesc.getInt("width");
			height = mLevelDesc.getInt("height");

			initSizeDependentStuff();

			JSONArray map = mLevelDesc.getJSONArray("map");

			for (int i = 0; i < map.length(); i++) {
				set(i, map.getInt(i));
			}

			setupLinks();

		} catch (JSONException e) {
			throw new TrackedRuntimeException(e);
		}
		buildFlagMaps();
		cleanWalls();
		createMobs();
		createItems();
	}

	private void setupLinks() throws JSONException {
		JSONArray entranceDesc = mLevelDesc.getJSONArray("entrance");

		entrance = cell(entranceDesc.getInt(0), entranceDesc.getInt(1));

		if(mLevelDesc.has("exit")) {
			JSONArray exitDesc = mLevelDesc.getJSONArray("exit");
			setExit(cell(exitDesc.getInt(0), exitDesc.getInt(1)), 0);
			return;
		}

		if(mLevelDesc.has("multiexit")){
			JSONArray multiExitDesc = mLevelDesc.getJSONArray("multiexit");
			for(int i=0;i<multiExitDesc.length();++i) {
				JSONArray exitDesc = multiExitDesc.getJSONArray(i);
				setExit(cell(exitDesc.getInt(0), exitDesc.getInt(1)), i);
			}
		}
	}

	@Override
	protected boolean build() {

		return true;
	}

	@Override
	protected void decorate() {
	}

	@Override
	protected void createMobs() {
		try {
			if (mLevelDesc.has("mobs")) {
				JSONArray mobsDesc = mLevelDesc.getJSONArray("mobs");

				for (int i = 0; i < mobsDesc.length(); ++i) {
					JSONObject mobDesc = mobsDesc.optJSONObject(i);
					int x = mobDesc.getInt("x");
					int y = mobDesc.getInt("y");

					if (cellValid(x, y)) {
						String kind = mobDesc.getString("kind");
						Mob mob = MobFactory.mobClassByName(kind).newInstance();
						mob.setPos(cell(x, y));
						spawnMob(mob);
					}
				}
			}
		} catch (JSONException e) {
			throw new TrackedRuntimeException("bad mob description", e);
		} catch (Exception e) {
			throw new TrackedRuntimeException(e);
		}
	}

	@Override
	protected void createItems() {
		try {
			if (mLevelDesc.has("items")) {
				JSONArray itemsDesc = mLevelDesc.getJSONArray("items");

				for (int i = 0; i < itemsDesc.length(); ++i) {
					JSONObject itemDesc = itemsDesc.optJSONObject(i);
					int x = itemDesc.getInt("x");
					int y = itemDesc.getInt("y");

					if (cellValid(x, y)) {
						String kind = itemDesc.getString("kind");
						Item item = ItemFactory.itemsClassByName(kind).newInstance();
						if (itemDesc.has("quantity")) {
							item.quantity(itemDesc.getInt("quantity"));
						}
						drop(item, cell(x, y));
					}
				}
			}
		} catch (JSONException e) {
			throw new TrackedRuntimeException("bad items description", e);
		} catch (Exception e) {
			throw new TrackedRuntimeException(e);
		}
	}

	@Override
	public String getSign(int atX, int atY) {
		try {
			if (mLevelDesc.has("signs")) {
				JSONArray signs = mLevelDesc.getJSONArray("signs");

				for (int i = 0; i < signs.length(); ++i) {
					JSONObject itemDesc = signs.optJSONObject(i);
					int x = itemDesc.getInt("x");
					int y = itemDesc.getInt("y");
					if (x == atX && y == atY) {
						return itemDesc.getString("text");
					}

				}
			}
		} catch (JSONException e) {
			throw new TrackedRuntimeException("bad signs description", e);
		} catch (Exception e) {
			throw new TrackedRuntimeException(e);
		}
		return super.getSign(atX, atY);
	}


	@Override
	protected int nTraps() {
		return 0;
	}

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(descFileKey, mDescFile);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		mDescFile = bundle.getString(descFileKey);
		readDescFile(mDescFile);
	}
}
