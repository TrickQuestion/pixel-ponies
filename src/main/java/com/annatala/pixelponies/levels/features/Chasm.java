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
package com.annatala.pixelponies.levels.features;

import com.annatala.noosa.Camera;
import com.annatala.noosa.Game;
import com.annatala.noosa.audio.Sample;
import com.annatala.pixelponies.Assets;
import com.annatala.pixelponies.Badges;
import com.annatala.pixelponies.Dungeon;
import com.annatala.pixelponies.android.R;
import com.annatala.pixelponies.ResultDescriptions;
import com.annatala.pixelponies.actors.buffs.Buff;
import com.annatala.pixelponies.actors.buffs.Cripple;
import com.annatala.pixelponies.actors.hero.Hero;
import com.annatala.pixelponies.actors.mobs.Mob;
import com.annatala.pixelponies.levels.RegularLevel;
import com.annatala.pixelponies.levels.Room;
import com.annatala.pixelponies.scenes.GameScene;
import com.annatala.pixelponies.scenes.InterlevelScene;
import com.annatala.pixelponies.sprites.MobSprite;
import com.annatala.utils.GLog;
import com.annatala.utils.Utils;
import com.annatala.pixelponies.windows.WndOptions;
import com.annatala.utils.Random;

public class Chasm {
	
	private static final String TXT_CHASM = Game.getVar(R.string.Chasm_Chasm);
	private static final String TXT_YES   = Game.getVar(R.string.Chasm_Yes);
	private static final String TXT_NO    = Game.getVar(R.string.Chasm_No);
	private static final String TXT_JUMP  = Game.getVar(R.string.Chasm_Jump);
	
	public static boolean jumpConfirmed = false;
	
	public static void heroJump( final Hero hero ) {
		GameScene.show( 
			new WndOptions( TXT_CHASM, TXT_JUMP, TXT_YES, TXT_NO ) {
				@Override
				protected void onSelect( int index ) {
					if (index == 0) {
						jumpConfirmed = true;
						hero.resume();
					}
				}
			}
		);
	}
	
	public static void heroFall(int pos, Hero hero) {
		
		jumpConfirmed = false;
				
		Sample.INSTANCE.play( Assets.SND_FALLING );
		hero.releasePets();

		if (hero.isAlive()) {
			hero.clearActions();
			InterlevelScene.mode = InterlevelScene.Mode.FALL;
			if (Dungeon.level instanceof RegularLevel) {
				Room room = ((RegularLevel)Dungeon.level).room( pos );
				InterlevelScene.fallIntoPit = room != null && room.type == Room.Type.WEAK_FLOOR;
			} else {
				InterlevelScene.fallIntoPit = false;
			}
			Game.switchScene( InterlevelScene.class );
		} else {
			hero.getSprite().setVisible(false);
		}
	}
	
	public static void heroLand() {
		
		Hero hero = Dungeon.hero;
		
		hero.getSprite().burst( hero.getSprite().blood(), 10 );
		Camera.main.shake( 4, 0.2f );
		
		Buff.prolong( hero, Cripple.class, Cripple.DURATION );
		hero.damage( Random.IntRange( hero.ht() / 3, hero.ht() / 2 ), new Hero.Doom() {
			@Override
			public void onDeath() {
				Badges.validateDeathFromFalling();
				
				Dungeon.fail( Utils.format( ResultDescriptions.FALL, Dungeon.depth ) );
				GLog.n(Game.getVar(R.string.Chasm_Info));
			}
		} );
	}

	public static void mobFall( Mob mob ) {
		// Destroy instead of kill to prevent dropping loot
		mob.destroy();
		
		mob.getSprite().removeAllStates();
		((MobSprite)mob.getSprite()).fall();
	}
}
