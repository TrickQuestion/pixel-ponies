package com.annatala.pixelponies.actors.mobs.common;

import com.annatala.pixelponies.actors.mobs.Mob;
import com.annatala.utils.Bundle;

public class MultiKindMob extends Mob {
	
	private static final String KIND_TAG = "KIND";
	protected int kind = 0;
	
	@Override
	public int getKind() {
		return kind;
	}
	
	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(KIND_TAG, kind);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		kind = bundle.optInt(KIND_TAG, 0);
	}
	
}
