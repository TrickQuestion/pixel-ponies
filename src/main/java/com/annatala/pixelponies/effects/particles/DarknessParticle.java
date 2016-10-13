package com.annatala.pixelponies.effects.particles;

import com.annatala.noosa.particles.Emitter;
import com.annatala.noosa.particles.Emitter.Factory;
import com.annatala.noosa.particles.PixelParticle;
import com.annatala.utils.Random;

public class DarknessParticle extends PixelParticle.Shrinking {

	public static final Factory FACTORY = new Factory() {
		@Override
		public void emit(Emitter emitter, int index, float x, float y) {
			((DarknessParticle) emitter.recycle(DarknessParticle.class)).reset(x, y);
		}

		@Override
		public boolean lightMode() {
			return true;
		}
	};

	public DarknessParticle() {
		lifespan = 1.2f;
		speed.set(0, 6);

		color(0xff442244);
	}

	public void reset(float x, float y) {
		revive();

		this.x = x;
		this.y = y;

		float offs = -Random.Float(lifespan);
		left = lifespan - offs;
	}

	@Override
	public void update() {
		super.update();

		float p = left / lifespan;
		am = p < 0.5f ? p : 1 - p;
		scale.x = (1 - p) * 4;
		scale.y = 16 + (1 - p) * 16;
	}
}