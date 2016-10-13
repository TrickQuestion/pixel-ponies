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
package com.annatala.pixelponies.actors.buffs;

import com.annatala.pixelponies.actors.Char;
import com.annatala.pixelponies.actors.hero.Hero;
import com.annatala.pixelponies.items.rings.RingOfMending;

public class Regeneration extends Buff {

    private static final float REGENERATION_DELAY = 10.0F;

    @Override
    public boolean act() {
        if (target.isAlive()) {

            if (target.hp() < target.ht()) {
                if (target instanceof Hero && ((Hero) target).isStarving()) {
                } else {
                    target.hp(target.hp() + 1);
                }
            }

            float bonus = 0;
            for (Buff buff : target.buffs(RingOfMending.Rejuvenation.class)) {
                bonus += (float) ((RingOfMending.Rejuvenation) buff).level;
            }

            // TODO: Expand this to affect other monsters, if we ever add the six stats to them.
            // Currently this is worth a tad less than +3 ring steps for highly generous characters.
            if ( target instanceof Hero && !((Hero) target).isStarving() ) {
                bonus += (float) (((Hero) target).effectiveGenerosity() - 3) * 0.30F;
            }

            spend((float) (REGENERATION_DELAY / Math.pow(1.2, bonus)));
        } else {
            deactivate();
        }

        return true;
    }

	@Override
	public boolean attachTo( Char target ) {

		if(target.buff(Regeneration.class) != null) {
			return false;
		}

		return super.attachTo(target);
	}
}
