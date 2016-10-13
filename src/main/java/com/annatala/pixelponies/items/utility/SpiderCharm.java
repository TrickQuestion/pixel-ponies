package com.annatala.pixelponies.items.utility;

import com.annatala.pixelponies.actors.mobs.spiders.SpiderServant;
import com.annatala.pixelponies.Dungeon;
import com.annatala.pixelponies.actors.Actor;
import com.annatala.pixelponies.actors.buffs.Buff;
import com.annatala.pixelponies.actors.hero.Hero;
import com.annatala.pixelponies.actors.mobs.Mob;
import com.annatala.pixelponies.effects.Pushing;
import com.annatala.pixelponies.effects.Wound;
import com.annatala.pixelponies.items.rings.UseableArtifact;
import com.annatala.pixelponies.plants.Sungrass.Health;
import com.annatala.pixelponies.sprites.ItemSprite.Glowing;
import com.annatala.pixelponies.sprites.ItemSpriteSheet;

public class SpiderCharm extends UseableArtifact {

	public SpiderCharm() {
		image = ItemSpriteSheet.SPIDER_CHARM;
	}

	private static final Glowing WHITE = new Glowing( 0xFFFFFF );
	
	@Override
	public Glowing glowing() {
		return WHITE;
	}
	
	@Override
	public void execute( final Hero ch, String action ) {
		setCurUser(ch);
		
		if (action.equals( AC_USE )) {
			Wound.hit(ch);
			ch.damage(ch.ht()/4, this);
			Buff.detach(ch, Health.class);
			
			int spawnPos = Dungeon.level.getEmptyCellNextTo(ch.getPos());
			
			if (Dungeon.level.cellValid(spawnPos)) {
				Mob pet = Mob.makePet(new SpiderServant(), getCurUser());
				pet.setPos(spawnPos);
				
				Dungeon.level.spawnMob(pet );
				Actor.addDelayed( new Pushing( pet, ch.getPos(), pet.getPos() ), -1 );
			}
			return;
		}
		super.execute( ch, action );
	}
}
