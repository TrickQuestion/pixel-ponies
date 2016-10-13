package com.annatala.pixelponies.actors.mobs.spiders;

import android.util.SparseBooleanArray;

import com.annatala.pixelponies.actors.mobs.common.MobSpawner;
import com.annatala.pixelponies.actors.mobs.spiders.sprites.SpiderEggSprite;
import com.annatala.pixelponies.Dungeon;
import com.annatala.pixelponies.actors.mobs.Mob;
import com.annatala.pixelponies.items.Generator;

public class SpiderEgg extends Mob {

	private static SparseBooleanArray eggsLaid = new SparseBooleanArray();

	public SpiderEgg() {
		spriteClass = SpiderEggSprite.class;

		hp(ht(2));
		defenseSkill = 0;
		baseSpeed = 0f;

		EXP = 0;
		maxLvl = 9;

		postpone(20);
		
		loot = Generator.random(Generator.Category.SEED);
		lootChance = 0.2f;
	}

	public static void lay(int pos) {
		eggsLaid.append(pos, true);
		SpiderSpawner.spawnEgg(Dungeon.level, pos);
	}

	public static boolean laid(int pos) {
		return eggsLaid.get(pos, false);
	}
	
	@Override
	protected boolean act() {
		super.act();

		Mob newSpider = MobSpawner.spawnRandomMob(Dungeon.level, getPos());
		if(isPet()) {
			Mob.makePet(newSpider, Dungeon.hero);
		}

		remove();
		eggsLaid.delete(getPos());

		return true;
	}

	@Override
	public boolean canBePet() {
		return false;
	}
}
