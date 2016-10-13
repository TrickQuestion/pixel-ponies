package com.annatala.pixelponies.items.quest;

import com.annatala.pixelponies.android.R;
import com.annatala.noosa.Game;
import com.annatala.pixelponies.items.rings.Artifact;
import com.annatala.pixelponies.ui.BuffIndicator;

public class HeartOfDarkness extends Artifact {

	public HeartOfDarkness() {
		imageFile = "items/artifacts.png";
		image = 18;
	}

	@Override
	public boolean isIdentified() {
		return true;
	}

	@Override
	protected Artifact.ArtifactBuff buff() {
		return new HeartOfDarknessBuff();
	}

	public class HeartOfDarknessBuff extends Artifact.ArtifactBuff {
		@Override
		public int icon() {
			return BuffIndicator.DARKVEIL;
		}

		@Override
		public String toString() {
			return Game.getVar(R.string.DarkVeil_Buff);
		}
	}
}
