package com.annatala.pixelponies.actors.mobs.guts;

import com.annatala.pixelponies.Dungeon;
import com.annatala.pixelponies.actors.Char;
import com.annatala.pixelponies.actors.blobs.ToxicGas;
import com.annatala.pixelponies.actors.buffs.Buff;
import com.annatala.pixelponies.actors.buffs.Levitation;
import com.annatala.pixelponies.actors.buffs.Paralysis;
import com.annatala.pixelponies.actors.mobs.Mob;
import com.annatala.pixelponies.items.keys.SkeletonKey;
import com.annatala.utils.Bundle;
import com.annatala.utils.Random;

public class MimicAmulet extends Mob {

	private int level;

	public MimicAmulet() {
		
		baseSpeed = 1.25f;
		
		flying = true;

		level = Dungeon.depth;
		
		IMMUNITIES.add( ToxicGas.class );
		IMMUNITIES.add( Paralysis.class );
	}
	
	private static final String LEVEL	= "level";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle(bundle);
		bundle.put(LEVEL, level);
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle(bundle);
		adjustStats(bundle.getInt(LEVEL));
	}
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange( ht() / 10, ht() / 4 );
	}
	
	@Override
	public int attackSkill( Char target ) {
		return 9 + level;
	}
	
	@Override
	public void die(Object cause) {
		super.die(cause);
		Dungeon.level.drop(new SkeletonKey(), getPos());
	}

	@Override
	protected boolean act() {
		if(buff(Levitation.class)==null) {
			Buff.affect(this, Levitation.class, 1000000);
		}
		return super.act();
	}

	public void adjustStats( int level ) {
		this.level = level;

		hp(ht((3 + level) * 5));
		EXP = 2 + 2 * (level - 1) / 5;
		defenseSkill = 2 * attackSkill( null ) / 3;
		
		enemySeen = true;
	}

	@Override
	public boolean canBePet() {
		return false;
	}
}
