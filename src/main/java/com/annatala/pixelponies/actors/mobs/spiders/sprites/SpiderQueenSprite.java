package com.annatala.pixelponies.actors.mobs.spiders.sprites;

import com.annatala.noosa.Animation;
import com.annatala.noosa.TextureFilm;
import com.annatala.pixelponies.Assets;
import com.annatala.pixelponies.sprites.MobSprite;

public class SpiderQueenSprite extends MobSprite {
	
	public SpiderQueenSprite() {
		texture( Assets.SPIDER_QUEEN );
		
		TextureFilm frames = new TextureFilm( texture, 16, 15 );
		
		idle = new Animation( 4, true );
		idle.frames( frames, 0, 0, 0, 1, 0, 0, 1, 1 );
		
		run = new Animation( 10, true );
		run.frames( frames, 0, 2, 3, 4 );
		
		attack = new Animation( 12, false );
		attack.frames( frames, 0, 5, 6, 7, 8 );
		
		zap = attack.clone();
		
		die = new Animation( 15, false );
		die.frames( frames,  0, 9, 10, 11, 12 );
		
		play( idle );
	}
}
