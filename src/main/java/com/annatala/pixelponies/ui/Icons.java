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
package com.annatala.pixelponies.ui;

import com.annatala.noosa.Image;
import com.annatala.pixelponies.Assets;
import com.annatala.pixelponies.actors.hero.HeroClass;

public enum Icons {

	COMPASS,
	DEPTH,
	SKULL,
	BUSY,
	MIND_CONTROL,
	SLEEP,
	ALERT,

	BACKPACK,
	KEYRING,
	SEED_POUCH,
	POTIONS_BELT,
	SCROLL_HOLDER,
	QUIVER,
	WAND_HOLSTER,

	CHECKED,
	UNCHECKED,
	EXIT,
	TRANSPARENT_EXIT,
	WARNING,

	TARGET,
	EARTH_PONY,
	UNICORN,
	PEGASUS,
	ZEBRA,
	NIGHTWING,

	SETTINGS,
	PLEASE_DONATE,
	THANKS_DONATED,
	EARTH_PATRON,
	PEGASUS_PATRON,
	UNICORN_PATRON,
	ALICORN_PATRON,

	CHALLENGE_OFF,
	CHALLENGE_ON,
	AUTHORS_ICON;

	
	public Image get() {
		return get( this );
	}
	
	public static Image get( Icons type ) {
		Image icon = new Image( Assets.ICONS );
		switch (type) {

			// 7x5 for COMPASS, 8x8 for others.
			case COMPASS:
				icon.frame( icon.texture.uvRect( 0, 0, 7, 5 ) );
				break;
			case DEPTH:
				icon.frame( icon.texture.uvRect( 8, 0, 16, 8 ) );
				break;
			case SKULL:
				icon.frame( icon.texture.uvRect( 16, 0, 24, 8 ) );
				break;
			case BUSY:
				icon.frame( icon.texture.uvRect( 24, 0, 32, 8 ) );
				break;
			case MIND_CONTROL:
				icon.frame( icon.texture.uvRect( 32, 0, 40, 8 ) );
				break;
			case SLEEP:
				icon.frame( icon.texture.uvRect( 40, 0, 48, 8 ) );
				break;
			case ALERT:
				icon.frame( icon.texture.uvRect( 48, 0, 56, 8 ) );
				break;

			// 10x10
			case BACKPACK:
				icon.frame( icon.texture.uvRect( 0, 8, 10, 18 ) );
				break;
			case KEYRING:
				icon.frame( icon.texture.uvRect( 10, 8, 20, 18 ) );
				break;
			case SEED_POUCH:
				icon.frame( icon.texture.uvRect( 20, 8, 30, 18 ) );
				break;
			case POTIONS_BELT:
				icon.frame( icon.texture.uvRect( 30, 8, 40, 18 ) );
				break;
			case SCROLL_HOLDER:
				icon.frame( icon.texture.uvRect( 40, 8, 50, 18 ) );
				break;
			case QUIVER:
				icon.frame( icon.texture.uvRect( 50, 8, 60, 18 ) );
				break;
			case WAND_HOLSTER:
				icon.frame( icon.texture.uvRect( 60, 8, 70, 18 ) );
				break;

			// 12x12
			case CHECKED:
				icon.frame( icon.texture.uvRect( 0, 18, 12, 30 ) );
				break;
			case UNCHECKED:
				icon.frame( icon.texture.uvRect( 12, 18, 24, 30 ) );
				break;
			case EXIT:
				icon.frame( icon.texture.uvRect( 24, 18, 36, 30 ) );
				break;
			case TRANSPARENT_EXIT:
				icon.frame( icon.texture.uvRect( 36, 18, 48, 30 ) );
				break;
			case WARNING:
				icon.frame( icon.texture.uvRect( 48, 18, 60, 30 ) );
				break;

			// 16x16, first line (in game stuff)
			case TARGET:
				icon.frame( icon.texture.uvRect( 0, 30, 16, 46 ) );
				break;
			case EARTH_PONY:
				icon.frame( icon.texture.uvRect( 16, 30, 32, 46 ) );
				break;
			case UNICORN:
				icon.frame( icon.texture.uvRect( 32, 30, 48, 46 ) );
				break;
			case PEGASUS:
				icon.frame( icon.texture.uvRect( 48, 30, 64, 46 ) );
				break;
			case ZEBRA:
				icon.frame( icon.texture.uvRect( 64, 30, 80, 46 ) );
				break;
			case NIGHTWING:
				icon.frame( icon.texture.uvRect( 80, 30, 96, 46 ) );
				break;

			// 16x16, second line (system stuff)
			case SETTINGS:
				icon.frame( icon.texture.uvRect( 0, 46, 16, 62 ) );
				break;
			case PLEASE_DONATE:
				icon.frame( icon.texture.uvRect( 16, 46, 32, 62 ) );
				break;
			case THANKS_DONATED:
				icon.frame( icon.texture.uvRect( 32, 46, 48, 62 ) );
				break;
			case EARTH_PATRON:
				icon.frame( icon.texture.uvRect( 48, 46, 64, 62 ) );
				break;
			case PEGASUS_PATRON:
				icon.frame( icon.texture.uvRect( 64, 46, 80, 62 ) );
				break;
			case UNICORN_PATRON:
				icon.frame( icon.texture.uvRect( 80, 46, 96, 62 ) );
				break;
			case ALICORN_PATRON:
				icon.frame( icon.texture.uvRect( 96, 46, 112, 62 ) );
				break;

			// Large icons: 24x24, 24x24, 32x16
			case CHALLENGE_OFF:
				icon.frame( icon.texture.uvRect( 0, 62, 24, 86 ) );
				break;
			case CHALLENGE_ON:
				icon.frame( icon.texture.uvRect( 24, 62, 48, 86 ) );
				break;
			case AUTHORS_ICON:
				icon.frame( icon.texture.uvRect( 48, 62, 80, 78 ) );
				break;
		}
		return icon;
	}
	
	public static Image get( HeroClass cl ) {
		switch (cl) {
		case EARTH_PONY:
			return get(EARTH_PONY);
		case UNICORN:
			return get(UNICORN);
		case PEGASUS:
			return get(PEGASUS);
		case ZEBRA:
			return get(ZEBRA);
		case NIGHTWING:
			return get(NIGHTWING);
		default:
			return null;
		}
	}
}
