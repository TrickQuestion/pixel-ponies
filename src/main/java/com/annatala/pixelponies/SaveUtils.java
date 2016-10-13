package com.annatala.pixelponies;

import com.annatala.pixelponies.android.util.FileSystem;
import com.annatala.noosa.Game;
import com.annatala.pixelponies.GamesInProgress.Info;
import com.annatala.pixelponies.actors.hero.HeroClass;
import com.annatala.pixelponies.scenes.InterlevelScene;
import com.annatala.utils.GLog;
import com.annatala.utils.Utils;

import java.io.File;

public class SaveUtils {
	private static final String GAME_FILE_EXTENSION = ".dat";
	private static final String DEPTH_FILE_EXTENSION = "%d.dat";

	static private boolean hasClassTag(HeroClass cl, String fname) {
		return fname.contains(cl.toString());
	}

	public static void loadGame(String slot, HeroClass heroClass) {
		
		GLog.toFile("Loading: class :%s slot: %s", heroClass.toString(), slot);
		Dungeon.deleteGame(true);
		copyFromSaveSlot(slot, heroClass);
		
		InterlevelScene.mode = InterlevelScene.Mode.CONTINUE;
		Game.switchScene(InterlevelScene.class);
		Dungeon.heroClass = heroClass;
	}

	public static String slotInfo(String slot, HeroClass cl) {
		if(slotUsed(slot, cl)) {
			
			String localName = slot +"/"+ gameFile(cl);
			
			Info info = GamesInProgress.checkFile(localName);
			
			if(info!= null) {
				return Utils.format("d: %2d   l: %2d", info.depth,
						info.level);
			}
		}
		
		return "";
	}
	
	public static boolean slotUsed(String slot, HeroClass cl) {
		String[] slotFiles = FileSystem.getInteralStorageFile(slot)
				.getAbsoluteFile().list();
		if (slotFiles == null) {
			return false;
		}

		for (String file : slotFiles) {
			if (file.endsWith(gameFile(cl))) {
				return true;
			}
		}

		return false;
	}

	private static boolean isRelatedTo(String path,HeroClass cl) {
		return ( path.endsWith(".dat") && hasClassTag(cl, path) ) || path.endsWith(gameFile(cl));
	}

	public static void copyAllClassesToSlot(String slot) {
		for(HeroClass hc: HeroClass.values()) {
			copySaveToSlot(slot,hc);
		}
	}
	
	public static void copyAllClassesFromSlot(String slot) {
		for(HeroClass hc: HeroClass.values()) {
			copyFromSaveSlot(slot,hc);
		}
	}
	
	public static void deleteGameAllClasses() {
		for(HeroClass hc: HeroClass.values()) {
			deleteLevels(hc);
			deleteGameFile(hc);
		}
	}
	
	public static void copyFromSaveSlot(String slot, HeroClass heroClass) {
		String[] files = FileSystem.getInteralStorageFile(slot).list();
		if(files == null) {
			return;
		}
		
		for (String file : files) {
			if (isRelatedTo(file, heroClass)) {

				String from = FileSystem.getInteralStorageFile(slot + "/" + file).getAbsolutePath();
				String to = FileSystem.getInteralStorageFile(file).getAbsolutePath();

				GLog.toFile("restoring file: %s, (%s -> %s)", file, from, to);

				FileSystem.copyFile(from, to);
			}
		}
	}

	public static void deleteSaveFromSlot(String slot, HeroClass cl) {
		File slotDir = FileSystem.getInteralStorageFile(slot)
				.getAbsoluteFile();

		File[] slotFiles = slotDir.listFiles();

		if (slotFiles != null) {
			for (File file : slotFiles) {
				String path = file.getAbsolutePath();
				if (isRelatedTo(path, cl)) {
					GLog.toFile("deleting %s", path);
					if(!file.delete()) {
						GLog.toFile("Failed to delete file: %s !", path);
					}
				}
			}
		}
	}

	public static void copySaveToSlot(String slot, HeroClass cl) {
		GLog.toFile("Saving: class :%s slot: %s", cl.toString(), slot);

		deleteSaveFromSlot(slot, cl);

		String[] files = Game.instance().fileList();

		for (String file : files) {
			if (isRelatedTo(file, cl)) {
				
				String from = FileSystem.getInteralStorageFile(file).getAbsolutePath();
				String to = FileSystem.getInteralStorageFile(slot + "/" + file).getAbsolutePath();
				
				GLog.toFile("storing file: %s, (%s -> %s)", file, from, to);
				
				FileSystem.copyFile(from,to);
			}
		}
	}

	public static void deleteLevels(HeroClass cl) {
		GLog.toFile("Deleting levels: class :%s", cl.toString());
		
		String[] files = Game.instance().fileList();

		for (String file : files) {
			if (file.endsWith(".dat") && hasClassTag(cl, file)) {
				GLog.toFile("deleting: %s", file);
				if(!Game.instance().deleteFile(file)){
					GLog.toFile("Failed to delete file: %s !", file);
				}
			}
		}
	}

	public static void deleteGameFile(HeroClass cl) {
		String gameFile = gameFile(cl);
		GLog.toFile("Deleting gamefile: class :%s file: %s", cl.toString(), gameFile);
		Game.instance().deleteFile(gameFile);
	}

	public static String gameFile(HeroClass cl) {
		return cl.toString() + GAME_FILE_EXTENSION;
	}

	public static String depthFileForLoad(HeroClass cl, int depth, String levelKind, String levelId) {
		String newFormat = depthFileForSave(cl, depth, levelKind, levelId);
		if(FileSystem.getInteralStorageFile(newFormat).exists()) {
			return newFormat;
		}
		
		return Utils.format(levelKind + "_" + _depthFile(cl), depth);
	}

	public static String depthFileForSave(HeroClass heroClass, int levelDepth, String levelKind, String levelId) {
		return Utils.format(levelKind + "_" + levelId + "_" + _depthFile(heroClass), levelDepth);
	}
	
	private static String _depthFile(HeroClass cl) {
		return cl.toString() + DEPTH_FILE_EXTENSION;
	}
}
