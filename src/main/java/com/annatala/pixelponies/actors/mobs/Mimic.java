/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.annatala.pixelponies.actors.mobs;

import com.annatala.noosa.audio.Sample;
import com.annatala.pixelponies.Assets;
import com.annatala.pixelponies.Dungeon;
import com.annatala.pixelponies.actors.Actor;
import com.annatala.pixelponies.actors.Char;
import com.annatala.pixelponies.effects.CellEmitter;
import com.annatala.pixelponies.effects.Pushing;
import com.annatala.pixelponies.effects.Speck;
import com.annatala.pixelponies.items.Gold;
import com.annatala.pixelponies.items.Item;
import com.annatala.pixelponies.items.scrolls.ScrollOfPsionicBlast;
import com.annatala.pixelponies.levels.Level;
import com.annatala.pixelponies.sprites.MimicSprite;
import com.annatala.utils.Bundle;
import com.annatala.utils.Random;

import java.util.ArrayList;
import java.util.List;

public class Mimic extends Mob {
	
	private int level;
	
	public Mimic() {
		spriteClass = MimicSprite.class;
		IMMUNITIES.add( ScrollOfPsionicBlast.class );
	}

	public ArrayList<Item> items;
	
	private static final String LEVEL	= "level";
	private static final String ITEMS	= "items";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( ITEMS, items );
		bundle.put( LEVEL, level );
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		items = new ArrayList<>(bundle.getCollection(ITEMS, Item.class));
		adjustStats( bundle.getInt( LEVEL ) );
	}
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange( ht() / 10, ht() / 4 );
	}
	
	@Override
	public int attackSkill( Char target ) {
		return 9 + level;
	}
	
	@Override
	public int attackProc( Char enemy, int damage ) {
		if (enemy == Dungeon.hero && Random.Int( 2 ) == 0 && !Random.luckBonus()) {
			Gold gold = new Gold( Random.Int( Dungeon.gold() / 10, Dungeon.gold() / 2 ) );
			if (gold.quantity() > 0) {
				Dungeon.gold(Dungeon.gold() - gold.quantity());
				Dungeon.level.drop( gold, Dungeon.hero.getPos() ).sprite.drop();
			}
		}
		return super.attackProc( enemy, damage );
	}
	
	public void adjustStats( int level ) {
		this.level = level;
		
		ht((3 + level) * 4);
		EXP = 2 + 2 * (level - 1) / 5;
		defenseSkill = attackSkill( null ) / 2;
		
		enemySeen = true;
	}
	
	@Override
	public void die( Object cause ) {

		super.die( cause );
		
		if (items != null) {
			for (Item item : items) {
				Dungeon.level.drop( item, getPos() ).sprite.drop();
			}
		}
	}
	
	@Override
	public boolean reset() {
		state = WANDERING;
		return true;
	}

	public static Mimic spawnAt( int pos, List<Item> items ) {
		Level level = Dungeon.level;
		Char ch = Actor.findChar( pos ); 
		if (ch != null) {
			int newPos = Dungeon.level.getEmptyCellNextTo(pos);
			
			if (!Dungeon.level.cellValid(newPos)) {
			
				Actor.addDelayed( new Pushing( ch, ch.getPos(), newPos ), -1 );
				
				ch.setPos(newPos);
				level.press(newPos, ch);

			} else {
				return null;
			}
		}
		
		Mimic m = new Mimic();
		m.items = new ArrayList<>(items);
		m.adjustStats( Dungeon.depth );
		m.hp(m.ht());
		m.setPos(pos);
		m.state = m.HUNTING;
		level.spawnMob(m,1);
		
		m.getSprite().turnTo( pos, Dungeon.hero.getPos() );
		
		if (Dungeon.visible[m.getPos()]) {
			CellEmitter.get( pos ).burst( Speck.factory( Speck.STAR ), 10 );
			Sample.INSTANCE.play( Assets.SND_MIMIC );
		}
		
		return m;
	}

	@Override
	public boolean canBePet() {
		return false;
	}
}
