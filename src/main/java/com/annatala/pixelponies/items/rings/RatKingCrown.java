package com.annatala.pixelponies.items.rings;

import com.annatala.pixelponies.android.R;
import com.annatala.noosa.Game;
import com.annatala.pixelponies.ui.BuffIndicator;

public class RatKingCrown extends Artifact {

	public RatKingCrown() {
		imageFile = "items/artifacts.png";
		image = 17;
	}

	@Override
	public boolean isIdentified() {
		return true;
	}

	@Override
	protected ArtifactBuff buff() {
		return new RatKingAuraBuff();
	}

	public class RatKingAuraBuff extends ArtifactBuff {
		@Override
		public int icon() {
			return BuffIndicator.RATTNESS;
		}

		@Override
		public String toString() {
			return Game.getVar(R.string.RatKingCrown_Buff);
		}
	}
}
