package com.annatala.pixelponies.levels.painters;

import com.annatala.pixelponies.levels.Level;
import com.annatala.pixelponies.levels.Room;
import com.annatala.pixelponies.levels.Terrain;
import com.annatala.utils.Point;

public class NecroExitPainter extends ExitPainter {

	public static void paint( Level level, Room room ) {
		//fill( level, room, Terrain.WALL );
		fill( level, room, 1, Terrain.EMPTY );
		
		for (Room.Door door : room.connected.values()) {
			door.set( Room.Door.Type.REGULAR );
		}

		Point center = room.center();
		int centerIndex = level.getWidth() * (center.y - 1) + center.x ;

		level.setExit(centerIndex,1);
		set( level, level.getExit(1), Terrain.LOCKED_EXIT );
	}
}
