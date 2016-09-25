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
package com.watabou.pixeldungeon.items.rings;

import com.nyrds.pixeldungeon.ml.R;
import com.watabou.noosa.Game;
import com.watabou.pixeldungeon.Badges;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.actors.hero.HeroClass;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.ItemStatusHandler;
import com.watabou.pixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.pixeldungeon.utils.Utils;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

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
			ItemSpriteSheet.RING_DIAMOND,
			ItemSpriteSheet.RING_OPAL,
			ItemSpriteSheet.RING_GARNET,
			ItemSpriteSheet.RING_RUBY,
			ItemSpriteSheet.RING_AMETHYST,
			ItemSpriteSheet.RING_TOPAZ,
			ItemSpriteSheet.RING_ONYX,
			ItemSpriteSheet.RING_TOURMALINE,
			ItemSpriteSheet.RING_EMERALD,
			ItemSpriteSheet.RING_SAPPHIRE,
			ItemSpriteSheet.RING_QUARTZ,
			ItemSpriteSheet.RING_AGATE};

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
		level(Random.Int( 0, 2 ));
		if (Random.Float() < 0.3f) {
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
//			if (target instanceof Hero && ((Hero)target).heroClass == HeroClass.ROGUE && !isKnown()) {
//				setKnown();
//				GLog.i( TXT_KNOWN, name() );
//				Badges.validateItemLevelAquired( Ring.this );
//			}
			
			return super.attachTo(target);
		}
		
		@Override
		public boolean act() {
			
			if (!isIdentified() && --ticksToKnow <= 0) {
				String gemName = name();
				identify();
				GLog.w( TXT_IDENTIFY, gemName, Ring.this.toString() );
				Badges.validateItemLevelAquired( Ring.this );
			}
			
			spend( TICK );
			
			return true;
		}


		@Override
		public String toString() {
			if (isKnown()) {
				return Utils.format( TXT_TO_STRING, super.toString(), minAttribute );
			} else {
				return Utils.format( TXT_UNKNOWN_TO_STRING, super.toString() );
			}
		}
	}

}