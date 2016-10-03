package com.watabou.pixeldungeon.sprites;

import com.nyrds.pixeldungeon.items.accessories.Accessory;
import com.watabou.gltextures.TextureCache;
import com.watabou.noosa.Animation;
import com.watabou.noosa.Camera;
import com.watabou.noosa.CompositeTextureImage;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.tweeners.Tweener;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.items.barding.Barding;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.utils.Callback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mike on 16.04.2016.
 */
public class HeroSpriteDef extends MobSpriteDef {

	private static final int RUN_FRAMERATE = 20;
	private static final String BLANK_IMAGE = "layeredSprites/blank.png";
	private CompositeTextureImage avatar;

	// body goes as main texture
    private static final String LAYER_BODY        = "body";
	private static final String LAYER_BARDING 	  = "barding";
	private static final String LAYER_HEAD        = "head";
	private static final String LAYER_HELMET      = "helmet";
    private static final String LAYER_WINGS       = "wings";
    private static final String LAYER_WEAPON      = "weapon";
	private static final String LAYER_DEATH       = "death";
	private static final String LAYER_ACCESSORY   = "accessory";

	private Animation fly;

	private static final String[] layersOrder = {
		LAYER_BODY,
		LAYER_BARDING,
		LAYER_HEAD,
		LAYER_HELMET,
        LAYER_WINGS,
        LAYER_WEAPON,
		LAYER_DEATH,
		LAYER_ACCESSORY
	};

	Map<String,String> layersDesc = new HashMap<>();

	private Tweener  jumpTweener;
	private Callback jumpCallback;

	public HeroSpriteDef(String[] lookDesc){
		super("spritesDesc/Hero.json",0);
		applyLayersDesc(lookDesc);
	}

	public HeroSpriteDef(Barding barding){
		super("spritesDesc/ArmoredStatue.json",0);
		createStatueSprite(barding);
		applyLayersDesc(getLayersDesc());
	}

	public HeroSpriteDef(Hero hero, boolean link) {
		super("spritesDesc/Hero.json",0);
		createLayersDesc(hero);
		applyLayersDesc(getLayersDesc());
		if(link) {
			link(hero);
		}
	}

	public HeroSpriteDef(Hero hero, Accessory accessory) {
		super("spritesDesc/Hero.json",0);
		createLayersDesc(hero, accessory);
		applyLayersDesc(getLayersDesc());
	}

	public void createLayersDesc(Hero hero) {
		Accessory accessory = Accessory.equipped();
		createLayersDesc(hero, accessory);
	}

	public void createLayersDesc(Hero hero, Accessory accessory) {
		layersDesc.clear();

        String bardingImage = BLANK_IMAGE;
        String helmetImage = BLANK_IMAGE;
        String wingsImage = BLANK_IMAGE;
        String accessoryImage = BLANK_IMAGE;

        if (hero.belongings.barding != null) {
            bardingImage = "layeredSprites/barding/" + hero.gender().toString() + "/";
            bardingImage += bardingDescriptor(hero.belongings.barding) + ".png";
        }

        if (hero.heroClass.hasWings()) {
            wingsImage = "layeredSprites/wings/" + hero.heroClass.toString() + ".png";
        }

		if (accessory  == null){
			if (hero.belongings.barding != null && hero.belongings.barding.isHasHelmet()){
				helmetImage = "layeredSprites/helmet/" + hero.gender().toString() + "/";
                helmetImage += bardingDescriptor(hero.belongings.barding) + ".png";
			}
		} else {
			accessoryImage = accessory.getLayerFile();
		}

		layersDesc.put(LAYER_BODY, "layeredSprites/body/" + hero.heroClass.toString() + ".png");
        layersDesc.put(LAYER_BARDING, bardingImage);
		layersDesc.put(LAYER_HEAD, "layeredSprites/head/" + hero.heroClass.toString() + ".png");
		layersDesc.put(LAYER_HELMET, helmetImage);
        layersDesc.put(LAYER_WINGS, wingsImage);
        layersDesc.put(LAYER_WEAPON, BLANK_IMAGE);
		layersDesc.put(LAYER_DEATH, "layeredSprites/death/" + hero.heroClass.toString() +".png");
		layersDesc.put(LAYER_ACCESSORY, accessoryImage);
	}

	public void createStatueSprite(Barding barding) {
        layersDesc.clear();

        String bardingImage = BLANK_IMAGE;
        String helmetImage = BLANK_IMAGE;
        if (barding != null) {
            bardingImage = "layeredSprites/barding/masculine/" + bardingDescriptor(barding) + ".png";
            if (barding.isHasHelmet()) {
                helmetImage = "layeredSprites/helmet/masculine/" + bardingDescriptor(barding) + ".png";
            }
        }

		layersDesc.put(LAYER_BODY, "layeredSprites/body/(statue).png");
        layersDesc.put(LAYER_BARDING, bardingImage);
		layersDesc.put(LAYER_HEAD, "layeredSprites/head/(statue).png");
		layersDesc.put(LAYER_HELMET, helmetImage);
        layersDesc.put(LAYER_WINGS, BLANK_IMAGE);
        layersDesc.put(LAYER_WEAPON, BLANK_IMAGE);
		layersDesc.put(LAYER_DEATH, "layeredSprites/death/(statue).png");
        layersDesc.put(LAYER_ACCESSORY, BLANK_IMAGE);
	}

	public void heroUpdated(Hero hero) {
		createLayersDesc(hero);
		applyLayersDesc(getLayersDesc());
		avatar();
	}

	public String[] getLayersDesc() {
		String [] ret = new String[layersOrder.length];
		for(int i = 0;i<layersOrder.length;++i){
			ret[i] = layersDesc.get(layersOrder[i]);
		}

		return ret;
	}

	public void applyLayersDesc(String[] lookDesc) {
		clearLayers();
		for(int i = 0;i<layersOrder.length && i<lookDesc.length;++i){
			addLayer(layersOrder[i],TextureCache.get(lookDesc[i]));
		}
	}

	private String bardingDescriptor(Barding barding) {
		return barding.trueName();
	}

	@Override
	protected void loadAdditionalData(JSONObject json, TextureFilm film, int kind) throws JSONException {
		fly     = readAnimation(json, "fly", film);
		operate = readAnimation(json, "operate", film);
	}

	@Override
	public void place(int p) {
		super.place(p);
		if(ch instanceof Hero) {
			Camera.main.target = this;
		}
	}

	@Override
	public void move(int from, int to) {
		super.move(from, to);
		if (ch.flying) {
			play(fly);
		}
		if(ch instanceof Hero) {
			Camera.main.target = this;
		}
	}

	public void jump(int from, int to, Callback callback) {
		jumpCallback = callback;

		int distance = Dungeon.level.distance(from, to);
		jumpTweener = new JumpTweener(this, worldToCamera(to), distance * 4,
				distance * 0.1f);
		jumpTweener.listener = this;
		getParent().add(jumpTweener);

		turnTo(from, to);
		play(fly);
	}

	@Override
	public void onComplete(Tweener tweener) {
		if (tweener == jumpTweener) {

			if (getVisible() && Dungeon.level.water[ch.getPos()] && !ch.flying) {
				GameScene.ripple(ch.getPos());
			}
			if (jumpCallback != null) {
				jumpCallback.call();
			}
		} else {
			super.onComplete(tweener);
		}
	}

	public boolean sprint(boolean on) {
		run.delay = on ? 0.625f / RUN_FRAMERATE : 1f / RUN_FRAMERATE;
		return on;
	}

	public CompositeTextureImage avatar() {
		if(avatar==null) {
			avatar = new CompositeTextureImage(texture);
			avatar.frame(idle.frames[0]);
		}

		avatar.clearLayers();


		avatar.addLayer(getLayerTexture(LAYER_BODY));
        avatar.addLayer(getLayerTexture(LAYER_BARDING));
		avatar.addLayer(getLayerTexture(LAYER_HEAD));
        avatar.addLayer(getLayerTexture(LAYER_HELMET));
        avatar.addLayer(getLayerTexture(LAYER_WINGS));
        avatar.addLayer(getLayerTexture(LAYER_WEAPON));
		avatar.addLayer(getLayerTexture(LAYER_DEATH));
		avatar.addLayer(getLayerTexture(LAYER_ACCESSORY));

		return avatar;
	}
}