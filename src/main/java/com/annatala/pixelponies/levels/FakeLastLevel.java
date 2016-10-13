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
package com.annatala.pixelponies.levels;

import com.annatala.pixelponies.items.quest.PseudoAmulet;
import com.annatala.pixelponies.android.R;
import com.annatala.noosa.Game;
import com.annatala.noosa.Scene;
import com.annatala.pixelponies.Assets;
import com.annatala.pixelponies.levels.painters.Painter;
import com.annatala.utils.Random;

import java.util.Arrays;

public class FakeLastLevel extends Level {

	private static final int SIZE = 9;
	
	{
		color1 = 0x801500;
		color2 = 0xa68521;
	}
	
	private int pedestal;
	
	@Override
	public String tilesTex() {
		return Assets.TILES_HALLS;
	}
	
	@Override
	public String waterTex() {
		return Assets.WATER_HALLS;
	}
	
	@Override
	protected boolean build() {

		Arrays.fill( map, Terrain.WALL );
		Painter.fill( this, 2, 2, SIZE-2, SIZE-2, Terrain.WATER );
		Painter.fill( this, 3, 3, SIZE-4, SIZE-4, Terrain.EMPTY );
		Painter.fill( this, SIZE/2, SIZE/2, 3, 3, Terrain.EMPTY_SP );
		
		entrance = SIZE * getWidth() + SIZE / 2 + 1;
		map[entrance] = Terrain.ENTRANCE;

		setExit(entrance - getWidth() * ( SIZE - 1 ),0);
		map[getExit(0)] = Terrain.LOCKED_EXIT;

		/*secondaryExit = entrance - getWidth() * SIZE + getWidth() * 2;
		map[secondaryExit] = Terrain.TRANSPARENT_EXIT;*/

		pedestal = (SIZE / 2 + 1) * (getWidth() + 1);
		map[pedestal] = Terrain.PEDESTAL;
		map[pedestal-1] = map[pedestal+1] = Terrain.STATUE_SP;
		
		feeling = Feeling.NONE;
		
		return true;
	}

	@Override
	protected void decorate() {
		for (int i=0; i < getLength(); i++) {
			if (map[i] == Terrain.EMPTY && Random.Int( 10 ) == 0) { 
				map[i] = Terrain.EMPTY_DECO;
			}
		}
	}

	@Override
	protected void createMobs() {
		/*MimicAmulet mimic = new MimicAmulet();
		mimic.setPos(pedestal);
		mimic.adjustStats(25);
		mobs.add(mimic);
		mimic.getSprite().turnTo(pedestal, Dungeon.hero.getPos());*/
	}

	@Override
	protected void createItems() {
		drop( new PseudoAmulet(), pedestal );
	}
	
	@Override
	public int randomRespawnCell() {
		return -1;
	}

	@Override
	public String tileName( int tile ) {
		switch (tile) {
		case Terrain.WATER:
			return Game.getVar(R.string.LastLevel_TileWater);
		case Terrain.GRASS:
			return Game.getVar(R.string.LastLevel_TileGrass);
		case Terrain.HIGH_GRASS:
			return Game.getVar(R.string.LastLevel_TileHighGrass);
		case Terrain.STATUE:
		case Terrain.STATUE_SP:
			return Game.getVar(R.string.LastLevel_TileStatue);
		default:
			return super.tileName( tile );
		}
	}
	
	@Override
	public String tileDesc(int tile) {
		switch (tile) {
		case Terrain.WATER:
			return Game.getVar(R.string.LastLevel_TileDescWater);
		case Terrain.STATUE:
		case Terrain.STATUE_SP:
			return Game.getVar(R.string.LastLevel_TileDescStatue);
		default:
			return super.tileDesc(tile);
		}
	}
	
	@Override
	public void addVisuals( Scene scene ) {
		HallsLevel.addVisuals( this, scene );
	}

	@Override
	public boolean isBossLevel() {
		return true;
	}
}
