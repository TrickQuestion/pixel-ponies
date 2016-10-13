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
package com.annatala.pixelponies.items.scrolls;

import com.annatala.noosa.audio.Sample;
import com.annatala.pixelponies.Assets;
import com.annatala.pixelponies.actors.buffs.Blindness;
import com.annatala.pixelponies.actors.buffs.Buff;
import com.annatala.pixelponies.actors.buffs.Charm;
import com.annatala.pixelponies.actors.buffs.FlavourBuff;
import com.annatala.pixelponies.actors.buffs.Invisibility;
import com.annatala.pixelponies.actors.buffs.Roots;
import com.annatala.pixelponies.actors.buffs.Slow;
import com.annatala.pixelponies.actors.buffs.Vertigo;
import com.annatala.pixelponies.actors.buffs.Weakness;
import com.annatala.pixelponies.actors.hero.Hero;
import com.annatala.pixelponies.effects.particles.ShadowParticle;
import com.annatala.utils.Random;

public class ScrollOfCurse extends Scroll {

	static Class<?> badBuffs[] = {
			Blindness.class,
			Charm.class,
			Roots.class,
			Slow.class,
			Vertigo.class,
			Weakness.class
		};

	@SuppressWarnings("unchecked")
	@Override
	protected void doRead() {
		Invisibility.dispel(getCurUser());
		
		if(getCurUser() instanceof Hero) {
			Hero hero =  getCurUser();
			
			hero.getSprite().emitter().burst( ShadowParticle.CURSE, 6 );
			Sample.INSTANCE.play( Assets.SND_CURSED );
			
			Class <? extends FlavourBuff> buffClass = (Class<? extends FlavourBuff>) Random.oneOf(badBuffs);
			Buff.prolong( hero, buffClass, 10);
			
			if(getCurUser().belongings.barding != null) {
				getCurUser().belongings.barding.cursed = true;
			}
			if(getCurUser().belongings.weapon != null) {
				getCurUser().belongings.weapon.cursed = true;
			}
			if(getCurUser().belongings.mane != null) {
				getCurUser().belongings.mane.cursed = true;
			}
			
			if(getCurUser().belongings.tail != null) {
				getCurUser().belongings.tail.cursed = true;
			}
		}
		
		setKnown();
		getCurUser().spendAndNext( TIME_TO_READ );
	}

	@Override
	public int price() {
		return isKnown() ? 300 * quantity() : super.price();
	}
}
