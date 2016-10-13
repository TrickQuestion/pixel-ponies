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
package com.annatala.pixelponies.items.utility;

import com.annatala.pixelponies.android.R;
import com.annatala.pixelponies.DungeonGenerator;
import com.annatala.pixelponies.Position;
import com.annatala.noosa.Game;
import com.annatala.noosa.audio.Sample;
import com.annatala.pixelponies.Assets;
import com.annatala.pixelponies.Dungeon;
import com.annatala.pixelponies.actors.Actor;
import com.annatala.pixelponies.actors.hero.Hero;
import com.annatala.pixelponies.items.Item;
import com.annatala.pixelponies.items.wands.WandOfBlink;
import com.annatala.pixelponies.levels.Level;
import com.annatala.pixelponies.scenes.InterlevelScene;
import com.annatala.pixelponies.sprites.ItemSprite.Glowing;
import com.annatala.pixelponies.sprites.ItemSpriteSheet;
import com.annatala.utils.GLog;
import com.annatala.utils.Utils;
import com.annatala.utils.Bundle;

import java.util.ArrayList;

public class LloydsBeacon extends Item {

	private static final String TXT_PREVENTING = Game.getVar(R.string.LloidsBeacon_Preventing);
	private static final String TXT_CREATURES  = Game.getVar(R.string.LloidsBeacon_Creatures);
	private static final String TXT_RETURN     = Game.getVar(R.string.LloidsBeacon_Return);
	private static final String TXT_INFO       = Game.getVar(R.string.LloidsBeacon_Info);
	private static final String TXT_SET        = Game.getVar(R.string.LloidsBeacon_Set);

	public static final float TIME_TO_USE = 1;

	public static final String AC_SET		= Game.getVar(R.string.LloidsBeacon_ACSet);
	public static final String AC_RETURN	= Game.getVar(R.string.LloidsBeacon_ACReturn);
	
	private Position returnTo;
	
	public LloydsBeacon() {
		image = ItemSpriteSheet.BEACON;
		returnTo = new Position();
		returnTo.levelDepth = -1;	
		
		name = Game.getVar(R.string.LloidsBeacon_Name);
	}
	
	private static final String DEPTH	 = "depth";
	private static final String POS		 = "pos";
	
	private static final String POSITION = "position";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		
		bundle.put(POSITION, returnTo);
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle(bundle);
		
		returnTo = (Position) bundle.get(POSITION);
		if(returnTo == null) { //pre remix.19.0 code, remove in future
			returnTo = new Position(DungeonGenerator.getEntryLevelKind(),
									DungeonGenerator.getEntryLevel(),
									bundle.getInt( DEPTH ),
									bundle.getInt( POS ));
		}
	}
	
	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		actions.add( AC_SET );
		if (returnTo.levelDepth != -1) {
			actions.add( AC_RETURN );
		}
		return actions;
	}
	
	@Override
	public void execute( Hero hero, String action ) {
		
		if (action.equals(AC_SET) || action.equals(AC_RETURN)) {
			
			if (Dungeon.bossLevel()) {
				hero.spend( LloydsBeacon.TIME_TO_USE );
				GLog.w( TXT_PREVENTING );
				return;
			}
			
			for (int i=0; i < Level.NEIGHBOURS8.length; i++) {
				if (Actor.findChar( hero.getPos() + Level.NEIGHBOURS8[i] ) != null) {
					GLog.w( TXT_CREATURES );
					return;
				}
			}
		}
		
		if (action.equals(AC_SET)) {
			
			returnTo = Dungeon.currentPosition();
			
			hero.spend( LloydsBeacon.TIME_TO_USE );
			hero.busy();
			
			hero.getSprite().operate( hero.getPos() );
			Sample.INSTANCE.play( Assets.SND_BEACON );
			
			GLog.i( TXT_RETURN );
			
		} else if (action.equals(AC_RETURN)) {
			if (returnTo.levelDepth == Dungeon.depth && returnTo.levelKind.equals(hero.levelKind)) {
				reset();
				WandOfBlink.appear( hero, returnTo.cellId );
				Dungeon.level.press( returnTo.cellId, hero );
				Dungeon.observe();
			} else {
				InterlevelScene.mode = InterlevelScene.Mode.RETURN;
				InterlevelScene.returnTo = new Position(returnTo);
				reset();
				Game.switchScene( InterlevelScene.class );
			}
		} else {
			
			super.execute( hero, action );
			
		}
	}
	
	public void reset() {
		returnTo.levelDepth = -1;
	}
	
	@Override
	public boolean isUpgradable() {
		return false;
	}
	
	@Override
	public boolean isIdentified() {
		return true;
	}
	
	private static final Glowing WHITE = new Glowing( 0xFFFFFF );
	
	@Override
	public Glowing glowing() {
		return returnTo.levelDepth != -1 ? WHITE : null;
	}
	
	@Override
	public String info() {
		return TXT_INFO + (returnTo.levelDepth == -1 ? "" : Utils.format( TXT_SET, returnTo.levelDepth ) );
	}
}
