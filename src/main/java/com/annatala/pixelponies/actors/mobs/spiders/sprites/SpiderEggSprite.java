package com.annatala.pixelponies.actors.mobs.spiders.sprites;

import com.annatala.noosa.Animation;
import com.annatala.noosa.TextureFilm;
import com.annatala.pixelponies.Assets;
import com.annatala.pixelponies.sprites.MobSprite;

public class SpiderEggSprite extends MobSprite {
	
	public SpiderEggSprite() {
		texture( Assets.SPIDER_EGG );
		
		TextureFilm frames = new TextureFilm( texture, 12, 12 );
		
		idle = new Animation( 2, true );
		idle.frames( frames, 0, 1, 2, 1, 0 );
		
		run = new Animation( 1, true );
		run.frames( frames, 0 );
		
		attack = new Animation( 1, false );
		attack.frames( frames, 0 );
		
		die = new Animation( 10, false );
		die.frames( frames, 0, 3, 4, 5, 6  );
		
		play( idle );
	}
}
