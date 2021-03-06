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
package com.annatala.pixelponies.items.barding;

import com.annatala.pixelponies.android.util.TrackedRuntimeException;
import com.annatala.pixelponies.android.R;
import com.annatala.noosa.Game;
import com.annatala.pixelponies.Badges;
import com.annatala.pixelponies.Dungeon;
import com.annatala.pixelponies.actors.Char;
import com.annatala.pixelponies.actors.hero.Hero;
import com.annatala.pixelponies.items.EquippableItem;
import com.annatala.pixelponies.items.Item;
import com.annatala.pixelponies.items.barding.glyphs.Affection;
import com.annatala.pixelponies.items.barding.glyphs.AntiEntropy;
import com.annatala.pixelponies.items.barding.glyphs.Bounce;
import com.annatala.pixelponies.items.barding.glyphs.Displacement;
import com.annatala.pixelponies.items.barding.glyphs.Entanglement;
import com.annatala.pixelponies.items.barding.glyphs.Metabolism;
import com.annatala.pixelponies.items.barding.glyphs.Multiplicity;
import com.annatala.pixelponies.items.barding.glyphs.Potential;
import com.annatala.pixelponies.items.barding.glyphs.Stench;
import com.annatala.pixelponies.items.barding.glyphs.Viscosity;
import com.annatala.pixelponies.sprites.ItemSprite;
import com.annatala.utils.GLog;
import com.annatala.utils.Utils;
import com.annatala.utils.Bundlable;
import com.annatala.utils.Bundle;
import com.annatala.utils.Random;

import java.util.ArrayList;

public class Barding extends EquippableItem {

	private static final String TXT_EQUIP_CURSED    = Game.getVar(R.string.Barding_EquipCursed);
	private static final String TXT_IDENTIFY        = Game.getVar(R.string.Barding_Identify);
	private static final String TXT_TO_STRING       = Game.getVar(R.string.Barding_ToString);
	private static final String TXT_INCOMPATIBLE    = Game.getVar(R.string.Barding_Incompatible);
	private static final String TXT_MAGIC_SAVE      = Game.getVar(R.string.Barding_Magic_Save);

	protected boolean hasHelmet;
	protected boolean hasCollar;
	protected boolean coverHair;

	public int tier;
	
	public int minHonesty;
	public int resistance;

	private int hitFractionsToKnow = 100;
	
	public Glyph glyph;
	
	public Barding(int tier ) {
		imageFile = "items/barding.png";

		this.tier = tier;
		
		minHonesty = typicalHonesty();
		resistance = typicalResistance();
		hasHelmet = false;
		hasCollar = false;
		coverHair = false;
	}
	
	private static final String GLYPH	= "glyph";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( GLYPH, glyph );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		glyph = (Glyph)bundle.get( GLYPH );
	}
	
	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		actions.add( isEquipped( hero ) ? AC_UNEQUIP : AC_EQUIP );
		return actions;
	}
	
	@Override
	public boolean doEquip( Hero hero ) {
		
		detach( hero.belongings.backpack );
		
		if (hero.belongings.barding == null || hero.belongings.barding.doUnequip( hero, true, false )) {
			
			hero.belongings.barding = this;
			
			cursedKnown = true;
			if (cursed) {
				equipCursed( hero );
				GLog.n( TXT_EQUIP_CURSED, toString() );
			}
			
			hero.updateLook();
			
			hero.spendAndNext( 2 * time2equip( hero ) );
			return true;
			
		} else {
			
			collect( hero.belongings.backpack );
			return false;
			
		}
	}
	
	@Override
	protected float time2equip( Hero hero ) {
		return hero.speed();
	}
	
	@Override
	public boolean doUnequip( Hero hero, boolean collect, boolean single ) {
		if (super.doUnequip( hero, collect, single )) {
			
			hero.belongings.barding = null;
			hero.updateLook();
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public boolean isEquipped( Hero hero ) {
		return hero.belongings.barding == this;
	}
	
	@Override
	public Item upgrade() {
		return upgrade( false );
	}
	
	public Item upgrade( boolean inscribe ) {
		
		if (glyph != null) {
			if (!inscribe && Random.Int( level() ) > 0) {

				// Add in a chance for Magic level to prevent the overwriting.
				if (Random.Int( level() + 4 ) + 3 > Dungeon.hero.effectiveMagic()) {
					GLog.w(TXT_INCOMPATIBLE);
					inscribe(null);
				} else {
					GLog.w(TXT_MAGIC_SAVE);
				}
			}
		} else {
			if (inscribe) {
				inscribe( Glyph.random() );
			}
		}
		
		resistance += tier;
		minHonesty--;
		
		return super.upgrade();
	}

	@Override
	public Item degrade() {
		resistance -= tier;
		minHonesty++;
		
		return super.degrade();
	}
	
	public int proc( Char attacker, Char defender, int damage ) {
		
		if (glyph != null) {
			damage = glyph.proc( this, attacker, defender, damage );
		}
		
		if (!levelKnown) {

			// Drop hitFractionsToKnow based on Generosity level!
			// Multiplying fractions by 10, then Generosity removes (gen+1) * 2 many.
			// For base this takes 13 hits.
			// Eventually this drops to 4 for very generous characters.
			if (defender instanceof Hero) {
				hitFractionsToKnow -= ((Hero) defender).effectiveGenerosity() * 2 + 2;

			// Does it even make sense in this case...?
			} else {
				hitFractionsToKnow -= 10;
			}

			if (hitFractionsToKnow <= 0) {
				levelKnown = true;
				GLog.w( TXT_IDENTIFY, name(), toString() );
				Badges.validateItemLevelAquired( this );
			}
		}
		
		return damage;
	}
	
	@Override
	public String toString() {
		return levelKnown ? Utils.format( TXT_TO_STRING, super.toString(), minHonesty) : super.toString();
	}
	
	@Override
	public String name() {
		return glyph == null ? super.name() : glyph.name( super.name() );
	}
	
	@Override
	public String info() {
		final String p = "\n\n";
		
		StringBuilder info = new StringBuilder( desc() );
		
		String name = name();
		
		if (levelKnown) {
			info.append(p);
			info.append(Utils.capitalize(Utils.format(Game.getVar(R.string.Barding_MaxSoak), name, Math.max(resistance, 0 ))));
			
			if (minHonesty > Dungeon.hero.effectiveHonesty()) {
				if (isEquipped( Dungeon.hero )) {
					info.append(Game.getVar(R.string.Barding_InadequateHonestyWearing));
				} else {
					info.append(Game.getVar(R.string.Barding_InadequateHonestyNotWearing));
				}
			}
		} else {
			info.append(Utils.format(Game.getVar(R.string.Barding_TypicalSoakAndHonesty), name, typicalResistance(), typicalHonesty()));
			if (typicalHonesty() > Dungeon.hero.effectiveHonesty()) {
				info.append(" ").append(Game.getVar(R.string.Barding_ProbablyInadequateHonesty));
			}
		}
		info.append(" ");
		if (glyph != null) {
			info.append(Game.getVar(R.string.Barding_IsInscribed));
		}
		
		if (isEquipped( Dungeon.hero )) {
			info.append(Utils.format(Game.getVar(R.string.Barding_Wearing), name,
				(cursed ? Game.getVar(R.string.Barding_CursedGrip) : ".") ));
		} else {
			if (cursedKnown && cursed) {
				info.append(Utils.format(Game.getVar(R.string.Barding_SenseCurse), name));
			}
		}
		
		return info.toString();
	}
	
	@Override
	public Item random() {
		if (Random.luckBonus() || Random.luckBonus()) {
			int n = 1;
			if (Random.luckBonus()) {
				n++;
				if (Random.luckBonus()) {
					n++;
				}
			}
			if (Random.Int( 5 ) < 3 && !Random.luckBonus()) {
				degrade( n );
				cursed = true;
			} else {
				upgrade( n );
			}
		}
		
		if (Random.Int( 8 ) == 0 || (Random.luckBonus() && Random.luckBonus())) {
			inscribe( Glyph.random() );
		}
		
		return this;
	}
	
	public int typicalHonesty() {
		return tier * 2;
	}
	
	public int typicalResistance() {
		return tier * 2;
	}
	
	@Override
	public int price() {
		int price = 10 * (1 << (tier - 1));
		if (glyph != null) {
			price *= 1.5;
		}
		if (cursed && cursedKnown) {
			price /= 2;
		}
		if (levelKnown) {
			if (level() > 0) {
				price *= (level() + 1);
			} else if (level() < 0) {
				price /= (1 - level());
			}
		}
		if (price < 1) {
			price = 1;
		}
		return price;
	}
	
	public Barding inscribe(Glyph glyph ) {
		
		if (glyph != null && this.glyph == null) {
			resistance += tier;
		} else if (glyph == null && this.glyph != null) {
			resistance -= tier;
		}
		
		this.glyph = glyph;
		
		return this;
	}

	@Override
	public ItemSprite.Glowing glowing() {
		return glyph != null ? glyph.glowing() : null;
	}

	public boolean isHasHelmet() {
		return hasHelmet;
	}

	public boolean isHasCollar() {
		return hasCollar;
	}

	public boolean isCoveringHair() {
		return coverHair;
	}

	public static abstract class Glyph implements Bundlable {
		
		private static final Class<?>[] glyphs = new Class<?>[]{ 
			Bounce.class, Affection.class, AntiEntropy.class, Multiplicity.class, 
			Potential.class, Metabolism.class, Stench.class, Viscosity.class,
			Displacement.class, Entanglement.class };
		
		private static final float[] chances= new float[]{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };
			
		public abstract int proc(Barding barding, Char attacker, Char defender, int damage );
		
		public String name() {
			return name( Game.getVar(R.string.Barding_GlyphWord));
		}
		
		public String name( String bardingName ) {
			return bardingName;
		}
		
		@Override
		public void restoreFromBundle( Bundle bundle ) {	
		}

		@Override
		public void storeInBundle( Bundle bundle ) {	
		}
		
		@Override
		public boolean dontPack() {
			return false;
		}
		
		public ItemSprite.Glowing glowing() {
			return ItemSprite.Glowing.WHITE;
		}
		
		public boolean checkOwner( Char owner ) {
			if (!owner.isAlive() && owner instanceof Hero) {
				
				((Hero)owner).killerGlyph = this;
				Badges.validateDeathFromGlyph();
				return true;
				
			} else {
				return false;
			}
		}
		
		@SuppressWarnings("unchecked")
		public static Glyph random() {
			try {
				return ((Class<Glyph>)glyphs[ Random.chances( chances ) ]).newInstance();
			} catch (Exception e) {
				throw new TrackedRuntimeException(e);
			}
		}
		
	}
}
