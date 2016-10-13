package com.annatala.pixelponies.actors.mobs.elementals;

import com.annatala.pixelponies.Dungeon;
import com.annatala.pixelponies.actors.Char;
import com.annatala.pixelponies.actors.blobs.Blob;
import com.annatala.pixelponies.actors.blobs.Fire;
import com.annatala.pixelponies.actors.blobs.Regrowth;
import com.annatala.pixelponies.actors.blobs.ToxicGas;
import com.annatala.pixelponies.actors.buffs.Paralysis;
import com.annatala.pixelponies.actors.buffs.Roots;
import com.annatala.pixelponies.actors.mobs.Mob;
import com.annatala.pixelponies.levels.Terrain;
import com.annatala.pixelponies.levels.TerrainFlags;
import com.annatala.pixelponies.plants.Earthroot;
import com.annatala.pixelponies.scenes.GameScene;
import com.annatala.utils.Random;

public class EarthElemental extends Mob {
	
	private int kind;
	
	public EarthElemental() {
		adjustLevel(Dungeon.depth);

		loot = new Earthroot.Seed();
		lootChance = 0.1f;
	}

	private void adjustLevel(int depth) {
		kind = Math.min(depth/5, 4);
		
		hp(ht(depth * 10 + 1));
		defenseSkill = depth * 2 + 1;
		EXP = depth + 1;
		maxLvl = depth + 2;
		
		IMMUNITIES.add(Roots.class);
		IMMUNITIES.add(Paralysis.class);
		IMMUNITIES.add(ToxicGas.class);
		IMMUNITIES.add(Fire.class);
	}

	@Override
	public int getKind() {
		return kind;
	}

	@Override
	public float speed() {
		if(TerrainFlags.is(Dungeon.level.map[getPos()], TerrainFlags.LIQUID)) {
			return super.speed() * 0.5f;
		} else {
			return super.speed();
		}
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange(hp() / 5, ht() / 5);
	}

	@Override
	public int attackSkill(Char target) {
		return defenseSkill / 2;
	}

	@Override
	public int dr() {
		return EXP;
	}

	@Override
	public int attackProc(Char enemy, int damage) {

		int cell = enemy.getPos();

		// Elemental ground-pound is less likely to affect lucky characters.
		if (Random.Int(5) < 3  && !Random.luckBonus()) {
			int c = Dungeon.level.map[cell];
			if (c == Terrain.EMPTY || c == Terrain.EMBERS
					|| c == Terrain.EMPTY_DECO || c == Terrain.GRASS
					|| c == Terrain.HIGH_GRASS) {
				
				GameScene.add(Blob.seed(cell, Math.max(EXP,10) * 15, Regrowth.class));
			}
		}
		return damage;
	}
}
