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
package com.annatala.pixelponies.items.wands;

import com.annatala.noosa.Game;
import com.annatala.noosa.audio.Sample;
import com.annatala.noosa.tweeners.AlphaTweener;
import com.annatala.pixelponies.Assets;
import com.annatala.pixelponies.Dungeon;
import com.annatala.pixelponies.android.R;
import com.annatala.pixelponies.actors.Actor;
import com.annatala.pixelponies.actors.Char;
import com.annatala.pixelponies.effects.MagicMissile;
import com.annatala.pixelponies.effects.Speck;
import com.annatala.pixelponies.mechanics.Ballistica;
import com.annatala.utils.Callback;

public class WandOfBlink extends Wand {

	@Override
	protected void onZap( int cell ) {

		int level = effectiveLevel();
		
		if (Ballistica.distance > level + 4) {
			cell = Ballistica.trace[level + 3];
		} else if (Actor.findChar( cell ) != null && Ballistica.distance > 1) {
			cell = Ballistica.trace[Ballistica.distance - 2];
		}
		
		getCurUser().getSprite().setVisible(true);
		appear( getCurUser(), cell );
		Dungeon.observe();
	}
	
	@Override
	protected void fx( int cell, Callback callback ) {
		MagicMissile.whiteLight( wandUser.getSprite().getParent(), wandUser.getPos(), cell, callback );
		Sample.INSTANCE.play( Assets.SND_ZAP );
		wandUser.getSprite().setVisible(false);
	}
	
	public static void appear( Char ch, int pos ) {
		
		ch.getSprite().interruptMotion();
		
		ch.move( pos );
		ch.getSprite().place( pos );
		
		if (ch.invisible == 0) {
			ch.getSprite().alpha( 0 );
			ch.getSprite().getParent().add( new AlphaTweener( ch.getSprite(), 1, 0.4f ) );
		}
		
		ch.getSprite().emitter().start( Speck.factory( Speck.LIGHT ), 0.2f, 3 );
		Sample.INSTANCE.play( Assets.SND_TELEPORT );
	}
	
	@Override
	public String desc() {
		return Game.getVar(R.string.WandOfBlink_Info);
	}
	
	@Override
	public boolean affectTarget() {
		return false;
	}
}
