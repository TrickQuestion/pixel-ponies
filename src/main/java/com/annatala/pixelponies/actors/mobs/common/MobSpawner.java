package com.annatala.pixelponies.actors.mobs.common;

import com.annatala.pixelponies.actors.mobs.JarOfSouls;
import com.annatala.pixelponies.Dungeon;
import com.annatala.pixelponies.actors.mobs.Bestiary;
import com.annatala.pixelponies.actors.mobs.Mob;
import com.annatala.pixelponies.levels.Level;

public class MobSpawner {

	static public Mob spawnRandomMob(Level level, int position) {
		Mob mob = Bestiary.mob(Dungeon.depth, level.levelKind());
		mob.setPos(position);
		mob.state = mob.WANDERING;
		level.spawnMob(mob);
		return mob;
	}

	static public void spawnJarOfSouls(Level level, int position) {
		Mob mob = new JarOfSouls();
		mob.setPos(position);
		mob.state = mob.WANDERING;
		level.spawnMob(mob);
	}

}
