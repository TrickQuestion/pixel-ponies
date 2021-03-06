/*
 * Pixel Dungeon
 * Copyright (C) 2012-2014  Oleg Dolya
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
package com.annatala.pixelponies.items.rings;

import com.annatala.pixelponies.android.R;
import com.annatala.noosa.Game;
import com.annatala.pixelponies.Badges;
import com.annatala.pixelponies.Dungeon;
import com.annatala.pixelponies.actors.Char;
import com.annatala.pixelponies.items.Item;
import com.annatala.pixelponies.items.ItemStatusHandler;
import com.annatala.pixelponies.sprites.ItemSpriteSheet;
import com.annatala.utils.GLog;
import com.annatala.utils.Utils;
import com.annatala.utils.Bundle;
import com.annatala.utils.Random;

public abstract class Ring extends Artifact {

	private static final String TXT_IDENTIFY = Game.getVar(R.string.Ring_Identify);
	private static final String TXT_TO_STRING = Game.getVar(R.string.Ring_ToString);
	private static final String TXT_UNKNOWN_TO_STRING = Game.getVar(R.string.Ring_UnknownToString);

	private static final Class<?>[] rings = {
			RingOfMending.class,
			RingOfDetection.class,
			RingOfShadows.class,
			RingOfPower.class,
			RingOfHerbalism.class,
			RingOfAccuracy.class,
			RingOfEvasion.class,
			RingOfSatiety.class,
			RingOfHaste.class,
			RingOfHaggler.class,
			RingOfElements.class,
			RingOfThorns.class
	};
	private static final String[] gems = Game.getVars(R.array.Ring_Gems);
	private static final Integer[] images = {
			ItemSpriteSheet.RING_PEARL,
			ItemSpriteSheet.RING_OPAL,
			ItemSpriteSheet.RING_GARNET,
			ItemSpriteSheet.RING_RUBY,
			ItemSpriteSheet.RING_AMETHYST,
			ItemSpriteSheet.RING_JASPER,
			ItemSpriteSheet.RING_BISMUTH,
			ItemSpriteSheet.RING_LAPIS_LAZULI,
			ItemSpriteSheet.RING_PERIDOT,
			ItemSpriteSheet.RING_SAPPHIRE,
			ItemSpriteSheet.RING_ROSE_QUARTZ,
			ItemSpriteSheet.RING_RAINBOW_QUARTZ};

	private static ItemStatusHandler<Ring> handler;

	private String gem;

	private int ticksToKnow = 200;

	private int tier;

	@SuppressWarnings("unchecked")
	public static void initGems() {
		handler = new ItemStatusHandler<>((Class<? extends Ring>[]) rings, gems, images);
	}

	public static void save(Bundle bundle) {
		handler.save(bundle);
	}

	@SuppressWarnings("unchecked")
	public static void restore(Bundle bundle) {
		handler = new ItemStatusHandler<>((Class<? extends Ring>[]) rings, gems, images, bundle);
	}

	public Ring(String tierString) {
		super(true);
		syncGem();
		this.tier = Integer.parseInt(tierString);
		this.minAttribute = this.typicalMinimum();
	}


	public void syncGem() {
		image	= handler.image( this );
		gem		= handler.label( this );
	}
	
	@Override
	public Item upgrade() {
		
		super.upgrade();
		this.minAttribute--;
		
		if (buff != null) {
			
			Char owner = buff.target;
			buff.detach();
			if ((buff = buff()) != null) {
				buff.attachTo( owner );
			}
		}
		
		return this;
	}

	@Override
	public Item degrade() {

		super.degrade();
		this.minAttribute++;

		if (buff != null) {

			Char owner = buff.target;
			buff.detach();
			if ((buff = buff()) != null) {
				buff.attachTo( owner );
			}
		}

		return this;
	}

	public int typicalMinimum() { return this.tier == 0 ? 0 : this.tier * 2 + 1; }

	public boolean isKnown() {
		return handler.isKnown( this );
	}
	
	protected void setKnown() {
		if (!isKnown()) {
			handler.know( this );
		}
		
		Badges.validateAllRingsIdentified();
	}
	
	@Override
	public String name() {
		return isKnown() ? name : Utils.format(Game.getVar(R.string.Ring_Name), gem);
	}
	
	@Override
	public String info() {

		final String p = "\n\n";

		StringBuilder info = new StringBuilder( );

		String typical  = Game.getVar(R.string.Ring_Info_Typical);
		String upgraded = Game.getVar(R.string.Ring_Info_Upgraded);
		String degraded = Game.getVar(R.string.Ring_Info_Degraded);

		if (!isKnown()) {
			info.append(Utils.format(Game.getVar(R.string.Ring_Info_Unknown), gem));
		} else {
			info.append(this.desc());
			info.append(p);

			String quality = levelKnown && level() != 0 ? (level() > 0 ? upgraded : degraded) : typical;
			info.append(Utils.capitalize(Utils.format(Game.getVar(R.string.Ring_Info_Known), this.name, quality, this.tier)));
			info.append(" ");
			if (levelKnown && this.minAttribute > Dungeon.hero.effectiveMagic()) {
				info.append(Game.getVar(R.string.Ring_Is_Too_Magical));
			} else if (isKnown() && typicalMinimum() > Dungeon.hero.effectiveMagic()){
				info.append(Game.getVar(R.string.Ring_Maybe_Too_Magical));
			}
		}

		if (isEquipped( Dungeon.hero )) {
			info.append(p);
			if (Dungeon.hero.belongings.weapon == this) {
				info.append(Utils.format(Game.getVar(R.string.Ring_Info_Horn), name()));
			} else if (Dungeon.hero.belongings.mane == this) {
				info.append(Utils.format(Game.getVar(R.string.Ring_Info_Mane), name()));
			} else if (Dungeon.hero.belongings.tail == this) {
				info.append(Utils.format(Game.getVar(R.string.Ring_Info_Tail), name()));
			}
			info.append(Utils.format((cursed ? Game.getVar(R.string.Ring_Info_Cursed) : ".")));
		} else if (cursed && cursedKnown) {
			info.append(Utils.format(Game.getVar(R.string.Ring_Info_Danger), name()));
		}

		return info.toString();
	}
	
	@Override
	public boolean isIdentified() {
		return super.isIdentified() && isKnown();
	}
	
	@Override
	public boolean isUpgradable() {
		return true;
	}
	
	@Override
	public Item identify() {
		setKnown();
		return super.identify();
	}
	
	@Override
	public Item random() {
		level(Random.Int( 0, 1 ));

		// Make the highest level luck-dependent. Yes, this makes the curses worse, too.
		if (level() == 1 && (Random.luckBonus() || Random.luckBonus())) {
			level(2);
		}

		// Changing this so ring curses can be lucked out of a little easier.
		if (Random.Int(2) == 0 && !Random.luckBonus() && !Random.luckBonus()) {
			level(-level());
			cursed = true;
		}
		return this;
	}
	
	public static boolean allKnown() {
		return handler.known().size() == rings.length;
	}
	
	@Override
	public int price() {  // TODO: Fix this
		int price = 80;
		if (cursed && cursedKnown) {
			price /= 2;
		}
		if (levelKnown) {
			if (level() >= 0) {
				price *= (level() + 2);
			} else if (level() < 0) {
				price /= (1 - level());
			}
		}
		if (price < 1) {
			price = 1;
		}
		return price;
	}

	@Override
	public String toString() {
		if (levelKnown) {
			if (isKnown()) {
				return Utils.format(TXT_TO_STRING, super.toString(), minAttribute);
			} else {
				return Utils.format(TXT_UNKNOWN_TO_STRING, super.toString());
			}
		} else {
			return super.toString();
		}
	}
	
	public class RingBuff extends ArtifactBuff {
		
//		private final String TXT_KNOWN = Game.getVar(R.string.Ring_BuffKnown);
		
		public int level;

		public RingBuff() {
			level = Ring.this.level() + 1;
		}
		// TODO: Might break Lv. 0 rings?
		
		@Override
		public boolean attachTo( Char target ) {

//			TODO: Keep this logic here in case we want to add it for another subclass later.
//			if (target instanceof Hero && ((Hero)target).heroClass == HeroClass.PEGASUS && !isKnown()) {
//				setKnown();
//				GLog.i( TXT_KNOWN, name() );
//				Badges.validateItemLevelAquired( Ring.this );
//			}
			
			return super.attachTo(target);
		}
		
		@Override
		public boolean act() {

			// Very high generosity nearly doubles how quickly rings are identified.
			if (isEquipped(Dungeon.hero) && Random.Int(16) < Dungeon.hero.effectiveGenerosity()) {
				ticksToKnow--;
			}
			
			if (!isIdentified() && --ticksToKnow <= 0) {
				String gemName = name();
				identify();
				GLog.w( TXT_IDENTIFY, gemName, Ring.this.toString() );
				Badges.validateItemLevelAquired( Ring.this );
			}
			
			spend( TICK );
			
			return true;
		}


	}

}