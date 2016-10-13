package com.annatala.pixelponies.items.quest;

import com.annatala.pixelponies.android.R;
import com.annatala.pixelponies.actors.mobs.guts.MimicAmulet;
import com.annatala.noosa.Game;
import com.annatala.noosa.audio.Sample;
import com.annatala.pixelponies.Assets;
import com.annatala.pixelponies.Dungeon;
import com.annatala.pixelponies.actors.Char;
import com.annatala.pixelponies.effects.CellEmitter;
import com.annatala.pixelponies.effects.Speck;
import com.annatala.pixelponies.items.Item;
import com.annatala.pixelponies.sprites.ItemSpriteSheet;

public class PseudoAmulet extends Item {

	public PseudoAmulet() {

		image  = ItemSpriteSheet.AMULET;
		name = Game.getVar(R.string.Amulet_Name);
		info = Game.getVar(R.string.Amulet_Info);
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

		MimicAmulet mimic = new MimicAmulet();
		mimic.setPos(spawnPos);
		mimic.state = mimic.WANDERING;
		mimic.adjustStats(Dungeon.depth);
		
		Dungeon.level.spawnMob( mimic );
		
		CellEmitter.get( pos ).burst( Speck.factory( Speck.STAR ), 10 );
		Sample.INSTANCE.play( Assets.SND_MIMIC );
		
		return null;
	}
	
}
