package com.annatala.pixelponies.actors.blobs;

import com.annatala.pixelponies.android.R;
import com.annatala.noosa.Game;
import com.annatala.pixelponies.effects.BlobEmitter;
import com.annatala.pixelponies.effects.particles.DarknessParticle;

public class Darkness extends Blob {
	
	@Override
	protected void evolve() {
		int from = getWidth() + 1;
		int to   = getLength() - getWidth() - 1;

		for (int pos=from; pos < to; pos++) {
			if (cur[pos] > 0) {
				off[pos] = cur[pos];
				volume += off[pos];
			} else {
				off[pos] = 0;
			}
		}
	}
	
	@Override
	public void use( BlobEmitter emitter ) {
		super.use( emitter );	
		emitter.start(DarknessParticle.FACTORY, 0.9f, 0 );
	}
	
	@Override
	public String tileDesc() {
		return Game.getVar(R.string.Darkness_Info);
	}
}
