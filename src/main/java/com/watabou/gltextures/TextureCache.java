/*
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

package com.watabou.gltextures;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader.TileMode;
import android.support.annotation.NonNull;

import com.nyrds.android.util.ModdingMode;
import com.nyrds.android.util.TrackedRuntimeException;
import com.watabou.glwrap.Texture;

import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TextureCache {

	private static Map<Object, SmartTexture> all = new HashMap<>();

	// No dithering, no scaling, 32 bits per pixel
	private static BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
	static {
		bitmapOptions.inScaled = false;
		bitmapOptions.inDither = false;
		bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
	}

	public static SmartTexture createSolid(int color) {
		String key = "1x1:" + color;

		if (all.containsKey(key)) {

			return all.get(key);

		} else {

			Bitmap bmp = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
			bmp.eraseColor(color);

			SmartTexture tx = new SmartTexture(bmp);
			all.put(key, tx);

			return tx;
		}
	}

	public static SmartTexture createGradient(int width, int height,
			int... colors) {

		String key = "" + width + "x" + height + ":" + Arrays.toString(colors);

		if (all.containsKey(key)) {

			return all.get(key);

		} else {

			Bitmap bmp = Bitmap.createBitmap(width, height,
					Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(bmp);
			Paint paint = new Paint();
			paint.setShader(new LinearGradient(0, 0, 0, height, colors, null,
					TileMode.CLAMP));
			canvas.drawPaint(paint);

			SmartTexture tx = new SmartTexture(bmp);
			all.put(key, tx);
			return tx;
		}

	}

	public static void add(Object key, SmartTexture tx) {
		all.put(key, tx);
	}

	public static SmartTexture get(@NonNull Object src) {

		if (all.containsKey(src)) {

			return all.get(src);

		} else if (src instanceof SmartTexture) {

			return (SmartTexture) src;

		} else {

			SmartTexture tx = new SmartTexture(getBitmap(src));
			all.put(src, tx);
			return tx;
		}

	}

	public static void clear() {

		for (Texture txt : all.values()) {
			txt.delete();
		}
		all.clear();

	}

	public static void reload() {
		for (SmartTexture tx : all.values()) {
			tx.reload();
		}
	}

	public static Bitmap getBitmap(Object src) {
		if (src instanceof String) {
			String resName = (String) src;
			InputStream stream = ModdingMode.getInputStream(resName);
			if(stream != null){
				return BitmapFactory.decodeStream(stream);
			}
			throw new TrackedRuntimeException("Failed to load "+ resName + "(mod: "+ ModdingMode.activeMod() + ")");
		} else if (src instanceof Bitmap) {
			return (Bitmap) src;
		}

		throw new TrackedRuntimeException("Bad resource source for Bitmap "+ src.getClass().getName());
	}

	public static boolean contains(Object key) {
		return all.containsKey(key);
	}

}
