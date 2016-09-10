package com.watabou.pixeldungeon.effects;

import com.nyrds.android.util.GuiProperties;
import com.watabou.noosa.Game;
import com.watabou.noosa.SystemText;
import com.watabou.pixeldungeon.DungeonTilemap;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.scenes.PixelScene;
import com.watabou.utils.SparseArray;

import java.util.ArrayList;

public class SystemFloatingText extends SystemText {

	private static final float LIFESPAN = 1f;
	private static final float DISTANCE = DungeonTilemap.SIZE;

	private float timeLeft;

	private int key = -1;

	private static SparseArray<ArrayList<SystemFloatingText>> stacks = new SparseArray<>();

	public SystemFloatingText() {
		super(null, GuiProperties.mediumTitleFontSize(), false);

		speed.y = -DISTANCE / LIFESPAN;
	}


	@Override
	public void update() {
		super.update();

		if (timeLeft > 0) {
			if ((timeLeft -= Game.elapsed) <= 0) {
				killAndErase();
			} else {
				float p = timeLeft / LIFESPAN;
				alpha(p > 0.5f ? 1 : p * 2);
			}
		}
	}

	@Override
	public void kill() {
		ArrayList<SystemFloatingText> stack = stacks.get(key);
		if (stack != null) {
			stack.remove(this);

			if (stack.isEmpty()) {
				stacks.remove(key);
			}
		}
		super.kill();
	}

	public void reset(float x, float y, String text, int color) {

		revive();

		text(text);
		hardlight(color);

		measure();
		this.x = PixelScene.align(x - width() / 2);
		this.y = y - height();

		timeLeft = LIFESPAN;
	}
	
	/* STATIC METHODS */

	public static void show(float x, float y, String text, int color) {
		((SystemFloatingText) GameScene.status()).reset(x, y, text, color);
	}

	public static void show(float x, float y, int key, String text, int color) {
		SystemFloatingText txt = (SystemFloatingText) GameScene.status();
		txt.reset(x, y, text, color);
		push(txt, key);
	}

	private static void push(SystemFloatingText txt, int key) {

		txt.key = key;

		ArrayList<SystemFloatingText> stack = stacks.get(key);
		if (stack == null) {
			stack = new ArrayList<>();
			stacks.put(key, stack);
		}

		if (stack.size() > 0) {
			SystemFloatingText below = txt;
			int aboveIndex = stack.size() - 1;
			while (aboveIndex >= 0) {
				SystemFloatingText above = stack.get(aboveIndex);
				if (above.y + above.height() > below.y) {
					above.y = below.y - above.height();

					below = above;
					aboveIndex--;
				} else {
					break;
				}
			}
		}

		stack.add(txt);
	}
}
