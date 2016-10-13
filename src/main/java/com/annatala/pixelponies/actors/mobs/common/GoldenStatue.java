package com.annatala.pixelponies.actors.mobs.common;

import com.annatala.pixelponies.items.weapon.melee.GoldenSword;
import com.annatala.pixelponies.android.R;
import com.annatala.noosa.Game;
import com.annatala.pixelponies.Dungeon;
import com.annatala.pixelponies.Journal;
import com.annatala.pixelponies.actors.Char;
import com.annatala.pixelponies.actors.blobs.ToxicGas;
import com.annatala.pixelponies.actors.buffs.Buff;
import com.annatala.pixelponies.actors.buffs.Burning;
import com.annatala.pixelponies.actors.buffs.Poison;
import com.annatala.pixelponies.actors.mobs.Mob;
import com.annatala.pixelponies.items.scrolls.ScrollOfPsionicBlast;
import com.annatala.pixelponies.items.weapon.Weapon;
import com.annatala.pixelponies.items.weapon.enchantments.Death;
import com.annatala.pixelponies.items.weapon.enchantments.Leech;
import com.annatala.pixelponies.sprites.CharSprite;
import com.annatala.utils.Utils;
import com.annatala.utils.Bundle;
import com.annatala.utils.Random;

public class GoldenStatue extends Mob {

	private Weapon weapon;

	private boolean flipInitially;

	public GoldenStatue(boolean flipInitially) {
		this.flipInitially = flipInitially;

		EXP = 0;
		state = PASSIVE;

		weapon = new GoldenSword();
		weapon.identify();
		weapon.upgrade(4);
		
		hp(ht(15 + Dungeon.depth * 5));
		defenseSkill = 4 + Dungeon.depth;
		
		RESISTANCES.add( ToxicGas.class );
		RESISTANCES.add( Poison.class );
		RESISTANCES.add( Death.class );
		RESISTANCES.add( ScrollOfPsionicBlast.class );
		IMMUNITIES.add( Leech.class );
	}

	private static final String WEAPON	= "weapon";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( WEAPON, weapon );
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		weapon = (Weapon)bundle.get( WEAPON );
	}

	@Override
	protected boolean act() {
		if (Dungeon.visible[getPos()]) {
			Journal.add( Journal.Feature.STATUE );
		}
		return super.act();
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange( weapon.MIN, weapon.MAX );
	}

	@Override
	public int attackSkill( Char target ) {
		return (int)((9 + Dungeon.depth) * weapon.ACU);
	}

	@Override
	protected float attackDelay() {
		return weapon.DLY;
	}

	@Override
	public int dr() {
		return Dungeon.depth;
	}

	@Override
	public void damage( int dmg, Object src ) {

		if (state == PASSIVE) {
			state = HUNTING;
		}

		super.damage( dmg, src );
	}

	@Override
	public int attackProc( Char enemy, int damage ) {

		if (Random.Int( 8 ) == 0 && !Random.luckBonus()) {
			Buff.affect( enemy, Burning.class ).reignite( enemy );
		}

		return damage;
	}

	@Override
	public void beckon( int cell ) {
	}

	@Override
	public void die( Object cause ) {
		Dungeon.level.drop( weapon, getPos() ).sprite.drop();
		super.die( cause );
	}

	@Override
	public void destroy() {
		Journal.remove( Journal.Feature.STATUE );
		super.destroy();
	}

	@Override
	public boolean reset() {
		state = PASSIVE;
		return true;
	}

	@Override
	public String description() {
		return Utils.format(Game.getVar(R.string.GoldenStatue_Desc), weapon.name());
	}

	@Override
	public CharSprite sprite() {
		CharSprite sprite = super.sprite();
		sprite.flipHorizontal = flipInitially;
		return sprite;
	}
}
