package com.annatala.pixelponies.items.food;

import com.annatala.noosa.audio.Sample;
import com.annatala.pixelponies.Assets;
import com.annatala.pixelponies.Dungeon;
import com.annatala.pixelponies.actors.Char;
import com.annatala.pixelponies.actors.buffs.Hunger;
import com.annatala.pixelponies.actors.mobs.MimicPie;
import com.annatala.pixelponies.effects.CellEmitter;
import com.annatala.pixelponies.effects.Speck;
import com.annatala.pixelponies.items.Item;
import com.annatala.pixelponies.sprites.ItemSpriteSheet;

public class ChangelingPie extends Food {

	public ChangelingPie() {
		image  = ItemSpriteSheet.APPLE_PIE;
		energy = Hunger.STARVING;
	}
	
	@Override
	public Item pick(Char ch, int pos ) {
		int spawnPos = pos;
		
		if(ch.getPos() == pos) {
			spawnPos = Dungeon.level.getEmptyCellNextTo(ch.getPos());
			
			if (!Dungeon.level.cellValid(spawnPos)) {
				return this;
			}
		}
		
		MimicPie mob = new MimicPie();
		mob.setPos(spawnPos);
		mob.state = mob.WANDERING;
		mob.adjustStats(Dungeon.depth);
		
		Dungeon.level.spawnMob( mob );
		
		CellEmitter.get( pos ).burst( Speck.factory( Speck.STAR ), 10 );
		Sample.INSTANCE.play( Assets.SND_MIMIC );
		
		return null;
	}
	
}
