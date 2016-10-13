package com.annatala.pixelponies.effects;

import com.annatala.noosa.Game;
import com.annatala.noosa.Image;
import com.annatala.pixelponies.Dungeon;
import com.annatala.pixelponies.DungeonTilemap;
import com.annatala.pixelponies.actors.Char;

public class DeathStroke extends Image {

	private static final float TIME_TO_FADE = 0.8f;
	
	private float time;
	
	public DeathStroke() {
		super( Effects.get( Effects.Type.DEATHSTROKE ) );
		origin.set( width / 2, height / 2 );
	}
	
	public void reset( int p ) {
		revive();
		
		x = (p % Dungeon.level.getWidth()) * DungeonTilemap.SIZE + (DungeonTilemap.SIZE - width) / 2;
		y = (p / Dungeon.level.getWidth()) * DungeonTilemap.SIZE + (DungeonTilemap.SIZE - height) / 2;
		
		time = TIME_TO_FADE;
	}
	
	@Override
	public void update() {
		super.update();
		
		if ((time -= Game.elapsed) <= 0) {
			kill();
		} else {
			float p = time / TIME_TO_FADE;
			alpha( p );
			scale.x = 1 + p;
		}
	}
	
	public static void hit( Char ch ) {
		hit( ch, 0 );
	}
	
	public static void hit( Char ch, float angle ) {
		if (ch.getSprite().hasParent()) {
			DeathStroke w = (DeathStroke)ch.getSprite().getParent().recycle( DeathStroke.class );
			ch.getSprite().getParent().bringToFront( w );
			w.reset( ch.getPos() );
			w.angle = angle;
		}
	}
}
