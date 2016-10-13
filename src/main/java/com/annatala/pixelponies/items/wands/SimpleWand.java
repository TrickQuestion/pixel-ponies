package com.annatala.pixelponies.items.wands;

import com.annatala.pixelponies.android.util.TrackedRuntimeException;
import com.annatala.utils.Random;

public abstract class SimpleWand extends Wand {
	
	@SuppressWarnings("rawtypes")
	private static Class[] variants = {	WandOfAmok.class, 
		WandOfAvalanche.class, 
		WandOfDisintegration.class, 
		WandOfFirebolt.class, 
		WandOfLightning.class, 
		WandOfMagicMissile.class, 
		WandOfPoison.class, 
		WandOfRegrowth.class, 
		WandOfSlowness.class};
	
	static public SimpleWand createRandomSimpleWand() {
		try {
			return (SimpleWand) Random.element(variants).newInstance();
		} catch (Exception e) {
			throw new TrackedRuntimeException(e);
		}
	}
	
	
}
