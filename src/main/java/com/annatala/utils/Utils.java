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
package com.annatala.utils;

import com.annatala.pixelponies.android.util.TrackedRuntimeException;
import com.annatala.pixelponies.android.BuildConfig;
import com.annatala.pixelponies.android.EventCollector;
import com.annatala.noosa.Game;

import java.util.Locale;

public class Utils {

	private static final Class<?> strings      = getR_Field("string");
	private static final Class<?> stringArrays = getR_Field("array");

	static private Class<?> getR_Field(String field) {
		try {
			return Class.forName("com.annatala.pixelponies.android.R$" + field);
		} catch (ClassNotFoundException e) {// well this is newer happens :) 
			EventCollector.logException(e);
		}
		return null;
	}

	public static String capitalize(String str) {
		return Character.toUpperCase(str.charAt(0)) + str.substring(1);
	}

	public static String format(String format, Object... args) {
		return String.format(Locale.ENGLISH, format, args);
	}

	private static String VOWELS = "aoeiu";

	public static String indefinite(String noun) {

		if (noun.length() == 0) {
			return "a";
		} else {
			return (VOWELS.indexOf(Character.toLowerCase(noun.charAt(0))) != -1 ? "an " : "a ") + noun;
		}
	}

	public static String[] getClassParams(String className, String paramName, String[] defaultValues, boolean warnIfAbsent) {

		if (className.length() == 0) { // isEmpty() require api level 9
			return defaultValues;
		}

		try {
			return Game.getVars(stringArrays.getField(className + "_" + paramName).getInt(null));
		} catch (NoSuchFieldException e) {
			if (warnIfAbsent) {
				GLog.w("no definition for  %s_%s :(", className, paramName);
			}
		} catch (Exception e) {
			throw new TrackedRuntimeException(e);
		}

		return defaultValues;
	}


	public static String getClassParam(String className, String paramName, String defaultValue, boolean warnIfAbsent) {
		if (className.length() == 0) { // isEmpty() require api level 9
			return defaultValue;
		}

		try {
			return Game.getVar(strings.getField(className + "_" + paramName).getInt(null));
		} catch (NoSuchFieldException e) {
			if (BuildConfig.DEBUG && warnIfAbsent) {
				GLog.w("no definition for  %s_%s :(", className, paramName);
			}
		} catch (Exception e) {
			throw new TrackedRuntimeException(e);
		}

		return defaultValue;
	}

	public static boolean canUseClassicFont(String localeCode) {
		return !(localeCode.startsWith("ko")
				|| localeCode.startsWith("zh")
				|| localeCode.startsWith("ja"));

	}

}
