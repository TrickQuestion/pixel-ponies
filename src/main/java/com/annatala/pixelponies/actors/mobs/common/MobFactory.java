package com.annatala.pixelponies.actors.mobs.common;

import com.annatala.pixelponies.actors.mobs.elementals.AirElemental;
import com.annatala.pixelponies.actors.mobs.elementals.EarthElemental;
import com.annatala.pixelponies.actors.mobs.elementals.WaterElemental;
import com.annatala.pixelponies.actors.mobs.guts.MimicAmulet;
import com.annatala.pixelponies.actors.mobs.guts.Nightmare;
import com.annatala.pixelponies.actors.mobs.guts.SuspiciousRat;
import com.annatala.pixelponies.actors.mobs.guts.Worm;
import com.annatala.pixelponies.actors.mobs.guts.YogsBrain;
import com.annatala.pixelponies.actors.mobs.guts.YogsEye;
import com.annatala.pixelponies.actors.mobs.guts.YogsHeart;
import com.annatala.pixelponies.actors.mobs.guts.YogsTeeth;
import com.annatala.pixelponies.actors.mobs.guts.ZombieGnoll;
import com.annatala.pixelponies.actors.mobs.DeathKnight;
import com.annatala.pixelponies.actors.mobs.DreadKnight;
import com.annatala.pixelponies.actors.mobs.EnslavedSoul;
import com.annatala.pixelponies.actors.mobs.ExplodingSkull;
import com.annatala.pixelponies.actors.mobs.JarOfSouls;
import com.annatala.pixelponies.actors.mobs.Lich;
import com.annatala.pixelponies.actors.mobs.RunicSkull;
import com.annatala.pixelponies.actors.mobs.Zombie;
import com.annatala.pixelponies.actors.mobs.spiders.SpiderEgg;
import com.annatala.pixelponies.actors.mobs.spiders.SpiderExploding;
import com.annatala.pixelponies.actors.mobs.spiders.SpiderMind;
import com.annatala.pixelponies.actors.mobs.spiders.SpiderNest;
import com.annatala.pixelponies.actors.mobs.spiders.SpiderQueen;
import com.annatala.pixelponies.actors.mobs.spiders.SpiderServant;
import com.annatala.noosa.Game;
import com.annatala.pixelponies.actors.mobs.Bat;
import com.annatala.pixelponies.actors.mobs.Brute;
import com.annatala.pixelponies.actors.mobs.Crab;
import com.annatala.pixelponies.actors.mobs.DM300;
import com.annatala.pixelponies.actors.mobs.Elemental;
import com.annatala.pixelponies.actors.mobs.Eye;
import com.annatala.pixelponies.actors.mobs.Gnoll;
import com.annatala.pixelponies.actors.mobs.Golem;
import com.annatala.pixelponies.actors.mobs.Goo;
import com.annatala.pixelponies.actors.mobs.King;
import com.annatala.pixelponies.actors.mobs.King.Undead;
import com.annatala.pixelponies.actors.mobs.Mimic;
import com.annatala.pixelponies.actors.mobs.MimicPie;
import com.annatala.pixelponies.actors.mobs.Mob;
import com.annatala.pixelponies.actors.mobs.Monk;
import com.annatala.pixelponies.actors.mobs.Piranha;
import com.annatala.pixelponies.actors.mobs.Rat;
import com.annatala.pixelponies.actors.mobs.Scorpio;
import com.annatala.pixelponies.actors.mobs.Shadow;
import com.annatala.pixelponies.actors.mobs.Shaman;
import com.annatala.pixelponies.actors.mobs.Skeleton;
import com.annatala.pixelponies.actors.mobs.Spinner;
import com.annatala.pixelponies.actors.mobs.WeaponStatue;
import com.annatala.pixelponies.actors.mobs.Succubus;
import com.annatala.pixelponies.actors.mobs.Swarm;
import com.annatala.pixelponies.actors.mobs.Tengu;
import com.annatala.pixelponies.actors.mobs.Thief;
import com.annatala.pixelponies.actors.mobs.Warlock;
import com.annatala.pixelponies.actors.mobs.Wraith;
import com.annatala.pixelponies.actors.mobs.Yog;
import com.annatala.pixelponies.actors.mobs.Yog.BurningFist;
import com.annatala.pixelponies.actors.mobs.Yog.Larva;
import com.annatala.pixelponies.actors.mobs.Yog.RottingFist;
import com.annatala.pixelponies.actors.mobs.npcs.Ghost.FetidRat;
import com.annatala.pixelponies.actors.mobs.npcs.Hedgehog;
import com.annatala.utils.Random;

import java.util.HashMap;


public class MobFactory {

	static private HashMap <String, Class<? extends Mob>> mMobsList;
	
	private static void registerMobClass(Class<? extends Mob> mobClass) {
		mMobsList.put(mobClass.getSimpleName(), mobClass);
	}
	
	private static void initMobsMap() {
		
		mMobsList = new HashMap<>();
		registerMobClass(Rat.class);
		registerMobClass(Gnoll.class);
		registerMobClass(Crab.class);
		registerMobClass(Swarm.class);
		registerMobClass(Thief.class);
		registerMobClass(Skeleton.class);
		registerMobClass(Goo.class);
		
		registerMobClass(Shaman.class);
		registerMobClass(Shadow.class);
		registerMobClass(Bat.class);
		registerMobClass(Brute.class);
		registerMobClass(Tengu.class);
		
		registerMobClass(SpiderServant.class);
		registerMobClass(SpiderExploding.class);
		registerMobClass(SpiderMind.class);
		registerMobClass(SpiderEgg.class);
		registerMobClass(SpiderNest.class);
		registerMobClass(SpiderQueen.class);
		
		registerMobClass(Spinner.class);
		registerMobClass(Elemental.class);
		registerMobClass(Monk.class);
		registerMobClass(DM300.class);
		
		registerMobClass(AirElemental.class);
		registerMobClass(WaterElemental.class);
		registerMobClass(EarthElemental.class);
		registerMobClass(Warlock.class);
		registerMobClass(Golem.class);
		registerMobClass(Succubus.class);
		registerMobClass(King.class);
		registerMobClass(Undead.class);
		
		registerMobClass(Eye.class);
		registerMobClass(Scorpio.class);
		registerMobClass(Yog.class);
		registerMobClass(Larva.class);
		registerMobClass(BurningFist.class);
		registerMobClass(RottingFist.class);
		
		registerMobClass(FetidRat.class);
		
		registerMobClass(Wraith.class);
		registerMobClass(Mimic.class);
		registerMobClass(MimicPie.class);
		registerMobClass(WeaponStatue.class);
		registerMobClass(Piranha.class);

		registerMobClass(MimicAmulet.class);
		registerMobClass(Worm.class);
		registerMobClass(YogsBrain.class);
		registerMobClass(YogsEye.class);
		registerMobClass(YogsHeart.class);
		registerMobClass(YogsTeeth.class);
		registerMobClass(ZombieGnoll.class);
		registerMobClass(ShadowLord.class);
		registerMobClass(Nightmare.class);
		registerMobClass(SuspiciousRat.class);

		registerMobClass(ArmoredStatue.class);
		registerMobClass(GoldenStatue.class);

		registerMobClass(DeathKnight.class);
		registerMobClass(DreadKnight.class);
		registerMobClass(EnslavedSoul.class);
		registerMobClass(ExplodingSkull.class);
		registerMobClass(JarOfSouls.class);
		registerMobClass(Lich.class);
		registerMobClass(RunicSkull.class);
		registerMobClass(Zombie.class);

		registerMobClass(Hedgehog.class);
	}
	
	public static Class<? extends Mob> mobClassRandom() {
		if(mMobsList==null) {
			initMobsMap();
		}
		
		return Random.element(mMobsList.values());
	}
	
	public static Class<? extends Mob> mobClassByName(String selectedMobClass) {
		
		if(mMobsList==null) {
			initMobsMap();
		}
		
		Class<? extends Mob> mobClass = mMobsList.get(selectedMobClass);
		if(mobClass != null) {
			return mobClass;
		} else {
			Game.toast("Unknown mob: [%s], spawning Rat instead",selectedMobClass);
			return Rat.class;
		}
	}

}
