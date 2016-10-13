package com.annatala.pixelponies.actors.mobs.common;

import com.annatala.pixelponies.Dungeon;
import com.annatala.pixelponies.actors.Char;
import com.annatala.pixelponies.actors.blobs.Darkness;
import com.annatala.pixelponies.actors.blobs.Foliage;
import com.annatala.pixelponies.actors.blobs.ParalyticGas;
import com.annatala.pixelponies.actors.blobs.ToxicGas;
import com.annatala.pixelponies.actors.buffs.Buff;
import com.annatala.pixelponies.actors.buffs.Weakness;
import com.annatala.pixelponies.items.scrolls.ScrollOfPsionicBlast;
import com.annatala.pixelponies.items.wands.SimpleWand;
import com.annatala.pixelponies.items.wands.Wand;
import com.annatala.pixelponies.levels.Terrain;
import com.annatala.pixelponies.mechanics.Ballistica;
import com.annatala.pixelponies.scenes.GameScene;
import com.annatala.utils.Random;

public class Crystal extends MultiKindMob {

	static private int ctr = 0;
	
	public Crystal() {
		adjustLevel(Dungeon.depth);
		
		loot = SimpleWand.createRandomSimpleWand();
		((Wand)loot).upgrade(Dungeon.depth);
		
		lootChance = 0.25f;
	}

	static public Crystal makeShadowLordCrystal() {
		Crystal crystal = new Crystal();
		crystal.kind = 2;
		crystal.lootChance = 0;

		return crystal;
	}

	private void adjustLevel(int depth) {
		kind = (ctr++)%2;
		
		hp(ht(Dungeon.depth * 4 + 1));
		defenseSkill = depth * 2 + 1;
		EXP = depth + 1;
		maxLvl = depth + 2;
		
		IMMUNITIES.add( ScrollOfPsionicBlast.class );
		IMMUNITIES.add( ToxicGas.class );
		IMMUNITIES.add( ParalyticGas.class );
	}
	
	@Override
	public int getKind() {
		return kind;
	}
	
	@Override
	public int damageRoll() {

		// These guys pack a wallop, so let's reduce damage a little for lucky heroes.
		if (Random.luckBonus()) {
			return Random.NormalIntRange( hp() / 3, ht() / 3 );
		}
		return Random.NormalIntRange( hp() / 2, ht() / 2 );
	}
	
	@Override
	protected boolean canAttack( Char enemy ) {
		return Ballistica.cast( getPos(), enemy.getPos(), false, true ) == enemy.getPos();
	}
	
	@Override
	public int attackSkill( Char target ) {
		if(kind < 2 ) {
			return 1000;
		} else {
			return 35;
		}
	}
	
	@Override
	public int dr() {
		return EXP / 3;
	}

	@Override
	public int attackProc( final Char enemy, int damage ) {

		if(kind < 2) {
			final Wand wand = ((Wand) loot);

			wand.mobWandUse(this, enemy.getPos());

			return 0;
		} else {
			getSprite().zap(enemy.getPos());
			if (enemy == Dungeon.hero && Random.Int(5) < 3 && !Random.luckBonus()) {
				Buff.prolong(enemy, Weakness.class, Weakness.duration(enemy));
			}
			return damage;
		}
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
	public void die(Object cause) {
		int pos = getPos();

		if(Dungeon.level.map[pos]== Terrain.PEDESTAL) {
			Dungeon.level.set(pos,Terrain.EMBERS);
			int x,y;
			x = Dungeon.level.cellX(pos);
			y = Dungeon.level.cellY(pos);

			Dungeon.level.clearAreaFrom(Darkness.class, x - 2, y - 2, 5, 5);
			Dungeon.level.fillAreaWith(Foliage.class, x - 2, y - 2, 5, 5, 1);

			GameScene.updateMap();
		}
		super.die(cause);
	}

	@Override
	public boolean canBePet(){
		return false;
	}
}
