package com.annatala.pixelponies.items.scrolls;

import com.annatala.noosa.audio.Sample;
import com.annatala.pixelponies.Assets;
import com.annatala.pixelponies.Dungeon;
import com.annatala.pixelponies.actors.buffs.Invisibility;
import com.annatala.pixelponies.actors.mobs.Boss;
import com.annatala.pixelponies.actors.mobs.Mob;
import com.annatala.pixelponies.actors.mobs.npcs.NPC;
import com.annatala.pixelponies.effects.Flare;
import com.annatala.utils.Random;

import java.util.ArrayList;

public class ScrollOfDomination extends Scroll {

	@Override
	protected void doRead() {
		
		Sample.INSTANCE.play( Assets.SND_DOMINANCE );
		Invisibility.dispel(getCurUser());
		
		ArrayList<Mob> mobsInSight = new ArrayList<>();
		
		for (Mob mob : Dungeon.level.mobs.toArray(new Mob[Dungeon.level.mobs.size()])) {
			if (Dungeon.level.fieldOfView[mob.getPos()] && !(mob instanceof Boss) && !mob.isPet() && !(mob instanceof NPC)) {
				mobsInSight.add(mob);
			}
		}
		
		while(!mobsInSight.isEmpty()) {
			Mob pet = Random.element(mobsInSight);

			if(pet.canBePet()) {
				Mob.makePet(pet, getCurUser());
				new Flare(3, 32).show(pet.getSprite(), 2f);
				break;
			}
			mobsInSight.remove(pet);
		}
		
		Dungeon.observe();
		
		setKnown();
		
		getCurUser().spendAndNext( TIME_TO_READ );
	}

	@Override
	public int price() {
		return isKnown() ? 80 * quantity() : super.price();
	}
}
