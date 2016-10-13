package com.annatala.pixelponies.sprites;

import com.annatala.pixelponies.android.util.JsonHelper;
import com.annatala.pixelponies.android.util.TrackedRuntimeException;
import com.annatala.pixelponies.android.util.Util;
import com.annatala.pixelponies.items.ItemFactory;
import com.annatala.opengl.TextureCache;
import com.annatala.noosa.Animation;
import com.annatala.noosa.Game;
import com.annatala.noosa.TextureFilm;
import com.annatala.noosa.audio.Sample;
import com.annatala.pixelponies.Assets;
import com.annatala.pixelponies.Dungeon;
import com.annatala.pixelponies.DungeonTilemap;
import com.annatala.pixelponies.actors.Char;
import com.annatala.pixelponies.effects.DeathRay;
import com.annatala.pixelponies.effects.Lightning;
import com.annatala.pixelponies.effects.MagicMissile;
import com.annatala.utils.Utils;
import com.annatala.utils.Callback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MobSpriteDef extends MobSprite {

	private int     bloodColor;
	private boolean levitating;
	private int     framesInRow;
	private int     kind;
	private String  zapEffect;
	Callback        zapCallback;

	static private Map<String, JSONObject> defMap = new HashMap<>();

	private String name;

	public MobSpriteDef(String defName, int kind) {
		super();

		name = defName;

		if (!defMap.containsKey(name)) {
			defMap.put(name, JsonHelper.readFile(name));
		}

		selectKind(kind);
	}

	@Override
	public void selectKind(int kind) {

		Util.storeEventInAcra("selectKind", name);

		this.kind = kind;
		JSONObject json = defMap.get(name);

		try {
			texture(json.getString("texture"));

			if(json.has("layers")) {
				JSONArray layers = json.getJSONArray("layers");

				for(int i=0;i<layers.length();++i) {
					JSONObject layer = layers.getJSONObject(i);
					addLayer(layer.getString("id"),TextureCache.get(layer.get("texture")));
				}
			}

			int width = json.getInt("width");

			TextureFilm film = new TextureFilm(texture, width, json.getInt("height"));

			bloodColor = json.optInt("bloodColor", 0xFFBB0000);
			levitating = json.optBoolean("levitating", false);
			framesInRow = texture.width / width;

			idle = readAnimation(json, "idle", film);
			run = readAnimation(json, "run", film);
			attack = readAnimation(json, "attack", film);
			die = readAnimation(json, "die", film);

			if (json.has("zap")) {
				zap = readAnimation(json, "zap", film);
			} else {
				zap = attack.clone();
			}

			if (json.has("zapEffect")) {
				zapEffect = json.getString("zapEffect");

				zapCallback = new Callback() {
					@Override
					public void call() {
						ch.onZapComplete();
					}
				};
			}

			loadAdditionalData(json,film, kind);

		} catch (Exception e) {
			Game.toast(Utils.format("Something bad happens when loading %s", name), e);
			throw new TrackedRuntimeException(Utils.format("Something bad happens when loading %s", name), e);
		}

		play(idle);
	}

	protected void loadAdditionalData(JSONObject json, TextureFilm film, int kind) throws JSONException {
	}

	protected Animation readAnimation(JSONObject root, String animKind, TextureFilm film) throws JSONException {
		JSONObject jsonAnim = root.getJSONObject(animKind);

		Animation anim = new Animation(jsonAnim.getInt("fps"), jsonAnim.getBoolean("looped"));

		List<Integer> framesSeq = new ArrayList<>(16);

		JSONArray jsonFrames = jsonAnim.getJSONArray("frames");

		int nextFrame;

		for (int i = 0; (nextFrame = jsonFrames.optInt(i, -1)) != -1; ++i) {
			framesSeq.add(nextFrame);
		}

		anim.frames(film, framesSeq, kind * framesInRow);

		return anim;
	}

	@Override
	public void link(Char ch) {
		super.link(ch);
		if (levitating) {
			add(State.LEVITATING);
		}
	}

	@Override
	public void die() {
		super.die();
		if (levitating) {
			remove(State.LEVITATING);
		}
	}

	@Override
	public void zap(int cell) {

		turnTo(ch.getPos(), cell);
		play(zap);

		if (zapEffect != null) {
			if (!Dungeon.visible[ch.getPos()] && Dungeon.visible[cell]){
				return;
			}

			int[] points = new int[2];
			points[0] = ch.getPos();
			points[1] = cell;

			if(ItemFactory.isValidItemClass(zapEffect)) {
				((MissileSprite)getParent().recycle( MissileSprite.class )).
						reset(ch.getPos(), cell, ItemFactory.itemByName(zapEffect), null, zapCallback );
				return;
			}

			if(zapEffect.equals("Lightning")) {
				getParent().add(new Lightning(points, 2, zapCallback));
				return;
			}

			if(zapEffect.equals("Shadow")) {
				MagicMissile.shadow(getParent(), ch.getPos(), cell, zapCallback);
				Sample.INSTANCE.play(Assets.SND_ZAP);
				return;
			}

			if(zapEffect.equals("Fire")) {
				MagicMissile.fire(getParent(), ch.getPos(), cell, zapCallback);
				return;
			}

			if(zapEffect.equals("DeathRay")) {
				getParent().add(new DeathRay(center(), DungeonTilemap.tileCenterToWorld(cell)));
				return;
			}
		}
	}

	@Override
	public int blood() {
		return bloodColor;
	}

}
