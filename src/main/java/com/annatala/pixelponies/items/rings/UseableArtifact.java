package com.annatala.pixelponies.items.rings;

import com.annatala.pixelponies.android.R;
import com.annatala.noosa.Game;
import com.annatala.pixelponies.actors.hero.Hero;

import java.util.ArrayList;

public class UseableArtifact extends Artifact {

	public static final String AC_USE = Game.getVar(R.string.Artifact_Use);

	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		actions.add( AC_USE );
		return actions;
	}
}
