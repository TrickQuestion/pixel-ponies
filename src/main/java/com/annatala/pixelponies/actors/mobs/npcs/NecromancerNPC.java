package com.annatala.pixelponies.actors.mobs.npcs;

import com.annatala.pixelponies.android.R;
import com.annatala.noosa.Game;
import com.annatala.pixelponies.Dungeon;
import com.annatala.pixelponies.actors.Char;
import com.annatala.pixelponies.actors.buffs.Buff;
import com.annatala.pixelponies.actors.hero.Hero;
import com.annatala.pixelponies.items.keys.SkeletonKey;
import com.annatala.pixelponies.levels.RegularLevel;
import com.annatala.pixelponies.levels.Room;
import com.annatala.pixelponies.levels.Terrain;
import com.annatala.pixelponies.scenes.GameScene;
import com.annatala.utils.GLog;
import com.annatala.pixelponies.windows.WndQuest;
import com.annatala.utils.Bundle;
import com.annatala.utils.Random;

public class NecromancerNPC extends NPC {

	private static final String TXT_INTRO    = Game.getVar(R.string.NecromancerNPC_Intro2);
	private static final String TXT_MESSAGE1 = Game.getVar(R.string.NecromancerNPC_Message1);
	private static final String TXT_MESSAGE2 = Game.getVar(R.string.NecromancerNPC_Message2);
	private static final String TXT_MESSAGE3 = Game.getVar(R.string.NecromancerNPC_Message3);
	private static final String TXT_MESSAGE4 = Game.getVar(R.string.NecromancerNPC_Message4);

	private static String[] TXT_PHRASES = {TXT_MESSAGE1, TXT_MESSAGE2, TXT_MESSAGE3, TXT_MESSAGE4};

	@Override
	protected Char chooseEnemy() {
		return DUMMY;
	}

	@Override
	public void damage(int dmg, Object src) {
	}

	@Override
	public int defenseSkill(Char enemy) {
		return 1000;
	}

	@Override
	public void add(Buff buff) {
	}

	private static final String NODE       = "necromancer";
	private static final String INTRODUCED = "introduced";

	private        boolean introduced = false;

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);

		Bundle node = new Bundle();
		node.put(INTRODUCED, introduced);

		bundle.put(NODE, node);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);

		Bundle node = bundle.getBundle(NODE);

		if(node.isNull()){
			return;
		}

		introduced = node.getBoolean(INTRODUCED);
	}

	public static void spawn(RegularLevel level, Room room) {
			NecromancerNPC npc = new NecromancerNPC();
		    int cell;
			do {
				cell = room.random(level);
			} while (level.map[cell] == Terrain.LOCKED_EXIT);
		    npc.setPos(cell);
			level.spawnMob(npc);
	}

	@Override
	public boolean interact(final Hero hero) {
		getSprite().turnTo(getPos(), hero.getPos());

		if (!introduced) {
			GameScene.show(new WndQuest(this, TXT_INTRO));
			introduced = true;

			SkeletonKey key = new SkeletonKey();

			if (key.doPickUp( Dungeon.hero )) {
				GLog.i( Hero.TXT_YOU_NOW_HAVE, key.name() );
			} else {
				Dungeon.level.drop( key, Dungeon.hero.getPos() ).sprite.drop();
			}

		} else {
			int index = Random.Int(0, TXT_PHRASES.length);
			say(TXT_PHRASES[index]);
		}
		return true;
	}
}
