package com.nyrds.pixeldungeon.mobs.common;

import android.support.annotation.NonNull;

import com.nyrds.pixeldungeon.ml.R;
import com.watabou.noosa.Game;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.Journal;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.blobs.ToxicGas;
import com.watabou.pixeldungeon.actors.buffs.Poison;
import com.watabou.pixeldungeon.actors.mobs.Mob;
import com.watabou.pixeldungeon.items.Generator;
import com.watabou.pixeldungeon.items.barding.Barding;
import com.watabou.pixeldungeon.items.barding.ClothBarding;
import com.watabou.pixeldungeon.items.scrolls.ScrollOfPsionicBlast;
import com.watabou.pixeldungeon.items.weapon.enchantments.Death;
import com.watabou.pixeldungeon.items.weapon.enchantments.Leech;
import com.watabou.pixeldungeon.sprites.CharSprite;
import com.watabou.pixeldungeon.sprites.HeroSpriteDef;
import com.watabou.pixeldungeon.utils.Utils;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class ArmoredStatue extends Mob {

	@NonNull
	private Barding barding;

	private boolean flipInitially = false;

	public ArmoredStatue(boolean flipInitially) {
		this.flipInitially = flipInitially;

		EXP = 0;
		state = PASSIVE;

		do {
			barding = (Barding) Generator.random( Generator.Category.BARDING);
		} while (!(barding instanceof Barding) || barding.level() < 0);

		barding.identify();
		barding.inscribe( Barding.Glyph.random() );

		hp(ht(15 + Dungeon.depth * 5));
		defenseSkill = 4 + Dungeon.depth + barding.resistance;
		
		RESISTANCES.add( ToxicGas.class );
		RESISTANCES.add( Poison.class );
		RESISTANCES.add( Death.class );
		RESISTANCES.add( ScrollOfPsionicBlast.class );
		IMMUNITIES.add( Leech.class );
	}

	private static final String BARDING = "barding";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put(BARDING, barding);
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		barding = (Barding) bundle.get(BARDING);
	}
	
	@Override
	protected boolean act() {
		if (Dungeon.visible[getPos()]) {
			Journal.add( Journal.Feature.STATUE );
		}
		return super.act();
	}

	@Override
	public int dr() {
		return Dungeon.depth + barding.resistance;
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 4, 8 );
	}

	@Override
	public int attackSkill( Char target ) {
		return (9 + Dungeon.depth) * 2;
	}

	@Override
	public int defenseProc(Char enemy, int damage) {
		damage = super.defenseProc(enemy, damage);
		return barding.proc(enemy, this, damage);
	}

	@Override
	public void damage( int dmg, Object src ) {

		if (state == PASSIVE) {
			state = HUNTING;
		}
		
		super.damage( dmg, src );
	}
	@Override
	public void beckon( int cell ) {
	}
	
	@Override
	public void die( Object cause ) {
		if (barding != null) {
			Dungeon.level.drop(barding, getPos() ).sprite.drop();
		}
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
		return Utils.format(Game.getVar(R.string.ArmoredStatue_Desc), barding.name());
	}

	@Override
	public CharSprite sprite() {
		if(barding != null)
		{
			HeroSpriteDef sprite = new HeroSpriteDef(barding);
			sprite.flipHorizontal = flipInitially;
			return sprite;
		}
		else{
			HeroSpriteDef sprite = new HeroSpriteDef(new ClothBarding());
			sprite.flipHorizontal = flipInitially;
			return sprite;
		}
	}
}
