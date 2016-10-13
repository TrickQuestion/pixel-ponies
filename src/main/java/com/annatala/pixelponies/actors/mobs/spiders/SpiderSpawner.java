package com.annatala.pixelponies.actors.mobs.spiders;

import com.annatala.pixelponies.actors.mobs.Mob;
import com.annatala.pixelponies.levels.Level;

public class SpiderSpawner {

	static public void spawnQueen(Level level, int position) {
		Mob mob = new SpiderQueen();
		mob.setPos(position);
		mob.state = mob.WANDERING;
		level.spawnMob(mob);
	}

	static public void spawnEgg(Level level, int position) {
		Mob mob = new SpiderEgg();
		mob.setPos(position);
		mob.state = mob.SLEEPING;
		level.spawnMob(mob);
	}

	static public void spawnNest(Level level, int position) {
		Mob mob = new SpiderNest();
		mob.setPos(position);
		mob.state = mob.SLEEPING;
		level.spawnMob(mob);
	}

}
