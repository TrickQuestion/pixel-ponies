package com.annatala.pixelponies.items.food;

import com.annatala.pixelponies.android.R;
import com.annatala.noosa.Game;
import com.annatala.noosa.audio.Sample;
import com.annatala.pixelponies.Assets;
import com.annatala.pixelponies.Dungeon;
import com.annatala.pixelponies.actors.blobs.Blob;
import com.annatala.pixelponies.actors.blobs.ConfusionGas;
import com.annatala.pixelponies.actors.blobs.ParalyticGas;
import com.annatala.pixelponies.actors.blobs.ToxicGas;
import com.annatala.pixelponies.actors.buffs.Hunger;
import com.annatala.pixelponies.actors.hero.Hero;
import com.annatala.pixelponies.effects.CellEmitter;
import com.annatala.pixelponies.effects.Speck;
import com.annatala.pixelponies.levels.Terrain;
import com.annatala.pixelponies.scenes.GameScene;
import com.annatala.utils.GLog;
import com.annatala.utils.Random;

abstract public class RottenFood extends Food{
	{
		energy  = (Hunger.STARVING - Hunger.HUNGRY)/2;
		message = Game.getVar(R.string.RottenFood_Message);
	}
	
	private boolean molder(int cell){
		
		Sample.INSTANCE.play( Assets.SND_ROTTEN_DROP );

		// Small luck chance for food to be edible.
		if (Random.luckBonus() && Random.luckBonus()) return false;
		
		switch (Random.Int( 4 )) {
		case 0:
			GameScene.add( Blob.seed( cell, 150 + 10 * Dungeon.depth, ConfusionGas.class ) );
			CellEmitter.get( cell ).burst( Speck.factory( Speck.CONFUSION ), 10 );
			break;
		case 1:
			GameScene.add( Blob.seed( cell, 500, ParalyticGas.class ) );
			CellEmitter.get( cell ).burst( Speck.factory( Speck.PARALYSIS ), 10 );
			break;
		case 2:
			GameScene.add( Blob.seed( cell, 500, ToxicGas.class ));
			CellEmitter.get( cell ).burst( Speck.factory( Speck.TOXIC ), 10 );
			break;
		case 3:
			return false;
		}
		
		return true;
	}
	
	public Food purify() {
		return this;
	}
	
	@Override
	public void execute( Hero hero, String action ) {
		
		super.execute( hero, action );
		
		if (action.equals( AC_EAT )) {
			GLog.w(message);
			molder(hero.getPos());
		}
	}
	
	@Override
	protected void onThrow( int cell ) {
	   if (Dungeon.level.map[cell] == Terrain.WELL || Dungeon.level.pit[cell]) {
			super.onThrow( cell );
		} else  {
			if(! molder( cell )){
				super.onThrow(cell);
			}
		}
	}
	
	public int price() {
		return 1 * quantity();
	}

}
