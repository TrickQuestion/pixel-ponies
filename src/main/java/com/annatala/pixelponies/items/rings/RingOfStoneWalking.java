package com.annatala.pixelponies.items.rings;

import com.annatala.pixelponies.android.R;

import com.annatala.noosa.Game;
import com.annatala.pixelponies.Badges;
import com.annatala.pixelponies.Dungeon;
import com.annatala.pixelponies.ResultDescriptions;
import com.annatala.pixelponies.actors.hero.Hero;
import com.annatala.pixelponies.sprites.ItemSpriteSheet;
import com.annatala.utils.GLog;
import com.annatala.utils.Utils;

public class RingOfStoneWalking extends Artifact {


	public RingOfStoneWalking() {
		super();
		image = ItemSpriteSheet.RING_OF_STONE_WALKING;
		identify();
	}


	@Override
	protected ArtifactBuff buff( ) {
		return new StoneWalking();
	}
	
	public class StoneWalking extends ArtifactBuff implements Hero.Doom{

		@Override
		public void onDeath() {
			Badges.validateDeathInStone();
			
			Dungeon.fail( Utils.format( ResultDescriptions.IMMURED, Dungeon.depth ) );
			GLog.n( Game.getVar(R.string.RingOfStoneWalking_ImmuredInStone));
			
		}
	}
}
