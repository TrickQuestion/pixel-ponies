package com.annatala.pixelponies.actors.mobs;

import com.annatala.pixelponies.actors.mobs.common.MultiKindMob;
import com.annatala.pixelponies.actors.blobs.ToxicGas;
import com.annatala.pixelponies.actors.buffs.Amok;
import com.annatala.pixelponies.actors.buffs.Blindness;
import com.annatala.pixelponies.actors.buffs.Paralysis;
import com.annatala.pixelponies.actors.buffs.Sleep;
import com.annatala.pixelponies.actors.buffs.Terror;
import com.annatala.pixelponies.items.weapon.enchantments.Death;
import com.annatala.utils.Callback;
import com.annatala.utils.Random;

public class RunicSkull extends MultiKindMob {

	protected boolean activated = false;
	private boolean zapping     = false;

	protected static final int RED_SKULL	 = 0;
	protected static final int BLUE_SKULL	 = 1;
	protected static final int GREEN_SKULL   = 2;
	protected static final int PURPLE_SKULL  = 3;

	public RunicSkull() {

		hp(ht(70));
		EXP = 5;
		defenseSkill = 15;

		pacified = true;
		kind = Random.Int(4);
		state = WANDERING;

		IMMUNITIES.add( Paralysis.class );
		IMMUNITIES.add( ToxicGas.class );
		IMMUNITIES.add( Terror.class );
		IMMUNITIES.add( Death.class );
		IMMUNITIES.add( Amok.class );
		IMMUNITIES.add( Blindness.class );
		IMMUNITIES.add( Sleep.class );
	}

	static public RunicSkull makeNewSkull(int k) {
		RunicSkull skull = new RunicSkull();
		skull.kind = k;
		return skull;
	}

	public void Activate() {
		this.activated = true;
	}

	public void Deactivate() {
		this.activated = false;
	}

	@Override
	public boolean act()
	{
		if (activated){
			if (!zapping) {
				PlayZap();
				zapping = true;
			}
		} else{
			getSprite().idle();
			zapping = false;
		}
		return super.act();
	}

	@Override
	public int getKind() {
		return kind;
	}

	@Override
	protected boolean getCloser( int target ) {
		return false;
	}

	@Override
	protected boolean getFurther( int target ) {
		return false;
	}

	@Override
	public void onZapComplete() {
		PlayZap();
	}

	public void PlayZap() {
		getSprite().zap(
				getPos(),
				new Callback() {
					@Override
					public void call() {
					}
				});
	}

	@Override
	public boolean canBePet(){
		return false;
	}
}
