package com.annatala.pixelponies.items.rings;

import com.annatala.noosa.Game;
import com.annatala.pixelponies.actors.hero.Hero;
import com.annatala.pixelponies.actors.mobs.Mob;
import com.annatala.pixelponies.android.R;
import com.annatala.pixelponies.sprites.ItemSpriteSheet;
import com.annatala.utils.Bundle;

public class FunnyGlasses extends Artifact {

	public FunnyGlasses() {
		identify();
		image = ItemSpriteSheet.FUNNY_GLASSES;
	}

	@Override
	public String desc() {
		return Game.getVar(R.string.FunnyGlasses_Info);
	}

	@Override
	public int price() {
		return 100 * quantity();
	}

}
