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
package com.annatala.pixelponies.ui;

import com.annatala.input.Touchscreen.Touch;
import com.annatala.noosa.Camera;
import com.annatala.noosa.CompositeTextureImage;
import com.annatala.noosa.Image;
import com.annatala.noosa.NinePatch;
import com.annatala.noosa.Text;
import com.annatala.noosa.TouchArea;
import com.annatala.noosa.particles.Emitter;
import com.annatala.noosa.ui.Component;
import com.annatala.pixelponies.Assets;
import com.annatala.pixelponies.Dungeon;
import com.annatala.pixelponies.actors.hero.Hero;
import com.annatala.pixelponies.effects.Speck;
import com.annatala.pixelponies.effects.particles.BloodParticle;
import com.annatala.pixelponies.items.keys.IronKey;
import com.annatala.pixelponies.scenes.GameScene;
import com.annatala.pixelponies.scenes.PixelScene;
import com.annatala.pixelponies.windows.WndGame;
import com.annatala.pixelponies.windows.WndHero;

public class StatusPane extends Component {
	
	private NinePatch             shield;
	private CompositeTextureImage avatar;
	private Emitter               blood;

	private Image hp;
	private Image exp;
	
	private int lastLvl = -1;
	private int lastKeys = -1;
	
	private Text level;
	private Text depth;
	private Text keys;
	
	private DangerIndicator danger;
	private LootIndicator loot;
	private BuffIndicator buffs;
	private Compass compass;
	
	private MenuButton btnMenu;
//  Removed hats button from layout
//	private MenuButton btnHats;

	private Hero hero;

	public StatusPane(Hero _hero) {
		super(true);
		hero = _hero;
		createChildren();
	}

	@Override
	protected void createChildren() {
		
		shield = new NinePatch( Assets.getStatus(), 80, 0, 30   + 18, 0 );
		add( shield );
		
		add( new TouchArea( 0, 1, 30, 30 ) {
			@Override
			protected void onClick( Touch touch ) {
				Image sprite = hero.getSprite();
				if (!sprite.isVisible()) {
					Camera.main.focusOn( sprite );
				}
				GameScene.show( new WndHero() );
			}
		} );
		
		btnMenu = new MenuButton(new Image(Assets.getStatus(), 114, 3, 12, 11), WndGame.class);
		add( btnMenu );

		//Remove the hats button. It's also been removed from the status assets Assets.getStatus()
//		btnHats = new MenuButton(new Image(Assets.getStatus(), 114, 18, 12, 11), WndHats.class);
//
//		if(!Flavours.haveHats()) {
//			btnHats.enable(false);
//		}
//
//		add(btnHats);

		avatar = hero.getHeroSprite().avatar();
		add(avatar);

		blood = new Emitter();
		blood.pos( avatar );
		blood.pour( BloodParticle.FACTORY, 0.3f );
		blood.autoKill = false;
		blood.on = false;
		add( blood );

		int mainExit = Dungeon.level.entrance;

		if(Dungeon.level.hasExit(0)) {
			mainExit = Dungeon.level.getExit(0);
		}

		compass = new Compass(mainExit);
		add(compass);


		hp = new Image( Assets.HP_BAR );
		add( hp );
		
		exp = new Image( Assets.XP_BAR );
		add( exp );
		
		level = Text.createBasicText(PixelScene.font1x);
		level.hardlight( 0xFFEBA4 );
		add( level );
		
		depth = Text.createBasicText( Integer.toString( Dungeon.depth ), PixelScene.font1x);
		depth.hardlight( 0xCACFC2 );
		depth.measure();
		add( depth );
		
		hero.belongings.countIronKeys();
		keys = Text.createBasicText( PixelScene.font1x);
		keys.hardlight( 0xCACFC2 );
		add( keys );
		
		danger = new DangerIndicator();
		add( danger );
		
		loot = new LootIndicator();
		add( loot );
		
		buffs = new BuffIndicator( hero );
		add( buffs );
	}
	
	@Override
	protected void layout() {
		
		height = 32;
		
		shield.size( width, shield.height );
		
		avatar.x = PixelScene.align( camera(), shield.x + 15 - avatar.width / 2 );
		avatar.y = PixelScene.align( camera(), shield.y + 16 - avatar.height / 2 );
		
		compass.x = avatar.x + avatar.width / 2 - compass.origin.x;
		compass.y = avatar.y + avatar.height / 2 - compass.origin.y;

		hp.x = 30;
		hp.y = 3;
		
		depth.x = width - 24 - depth.width()    - 18;
		depth.y = 6;
		
		keys.y = 6;
		
		danger.setPos( width - danger.width(), 40 );
		
		loot.setPos( width - loot.width(),  danger.bottom() + 2 );
		
		buffs.setPos( 32, 11 );
		
		btnMenu.setPos( width - btnMenu.width(), 1 );
//		btnHats.setPos( width - btnHats.width(), btnMenu.bottom() );
	}
	
	@Override
	public void update() {
		super.update();
		
		float health = (float)hero.hp() / hero.ht();

		if (health == 0) {
			avatar.tint( 0x000000, 0.6f );
			blood.on = false;
		} else if (health < 0.25f) {
			avatar.tint( 0xcc0000, 0.4f );
			blood.on = true;
		} else {
			avatar.resetColor();
			blood.on = false;
		}

		hp.Scale().x = health;
		exp.Scale().x = (width / exp.width) * hero.getExp() / hero.maxExp();
		
		if (hero.lvl() != lastLvl) {
			
			if (lastLvl != -1) {
				Emitter emitter = (Emitter)recycle( Emitter.class );
				emitter.revive();
				emitter.pos( 27, 27 );
				emitter.burst( Speck.factory( Speck.STAR ), 12 );
			}
			
			lastLvl = hero.lvl();
			level.text( Integer.toString( lastLvl ) );
			level.measure();
			level.x = PixelScene.align( 27.0f - level.width() / 2 );
			level.y = PixelScene.align( 27.5f - level.baseLine() / 2 );
		}
		
		int k = IronKey.curDepthQuantity;
		if (k != lastKeys) {
			lastKeys = k;
			keys.text( Integer.toString( lastKeys ) );
			keys.measure();
			keys.x = width - 8 - keys.width()    - 18;
		}
	}

}
