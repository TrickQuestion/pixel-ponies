package com.annatala.pixelponies.windows;

import com.annatala.pixelponies.android.util.GuiProperties;
import com.annatala.pixelponies.android.util.ModdingMode;
import com.annatala.pixelponies.android.util.TrackedRuntimeException;
import com.annatala.pixelponies.android.R;
import com.annatala.noosa.Game;
import com.annatala.noosa.InterstitialPoint;
import com.annatala.noosa.Text;
import com.annatala.pixelponies.Dungeon;
import com.annatala.pixelponies.PixelPonies;
import com.annatala.pixelponies.SaveUtils;
import com.annatala.pixelponies.scenes.GameScene;
import com.annatala.pixelponies.scenes.PixelScene;
import com.annatala.pixelponies.ui.DonateButton;
import com.annatala.pixelponies.ui.Icons;
import com.annatala.pixelponies.ui.RedButton;
import com.annatala.pixelponies.ui.SimpleButton;
import com.annatala.pixelponies.ui.TextButton;
import com.annatala.pixelponies.ui.Window;

import java.util.ArrayList;

public class WndSaveSlotSelect extends Window implements InterstitialPoint {

	private static final int    WIDTH         = 120;
	private static final int    MARGIN        = 2;
	private static final int    BUTTON_HEIGHT = 20;
	private static final int    BUTTON_WIDTH  = 58;

	public static final  String EMPTY_STRING  = "";

	private boolean saving;
	private String  slot;

	WndSaveSlotSelect(final boolean _saving) {
		String options[] = slotInfos();

		Text tfTitle = PixelScene.createMultiline(Game.getVar(R.string.WndSaveSlotSelect_SelectSlot), GuiProperties.titleFontSize());
		tfTitle.hardlight(TITLE_COLOR);
		tfTitle.x = tfTitle.y = MARGIN;
		tfTitle.maxWidth(WIDTH - MARGIN * 2);
		tfTitle.measure();
		add(tfTitle);

		Text tfMesage = PixelScene.createMultiline(windowText(), GuiProperties.regularFontSize());
		tfMesage.maxWidth(WIDTH - MARGIN * 2);
		tfMesage.measure();
		tfMesage.x = MARGIN;
		tfMesage.y = tfTitle.y + tfTitle.height() + MARGIN;
		add(tfMesage);

		float pos = tfMesage.y + tfMesage.height() + MARGIN;

		ArrayList<TextButton> buttons = new ArrayList<>();
		for (int i = 0; i < options.length / 2 + 1; i++) {
			for (int j = 0; j < 2; j++) {
				final int index = i * 2 + j;
				if (!(index < options.length)) {
					break;
				}

				float additionalMargin = 0;
				float x = MARGIN + j * (BUTTON_WIDTH + MARGIN);

				final RedButton btn = new RedButton(options[index]) {
					@Override
					protected void onClick() {
						hide();
						onSelect(index);
					}
				};
				buttons.add(btn);

				if (!options[index].isEmpty()) {
					SimpleButton deleteBtn = new SimpleButton(Icons.get(Icons.EXIT)) {
						protected void onClick() {
							final int slotIndex = index;
							WndOptions reallyDelete = new WndOptions(Game.getVar(R.string.WndSaveSlotSelect_Delete_Title), "",
									Game.getVar(R.string.WndSaveSlotSelect_Delete_Yes),
									Game.getVar(R.string.WndSaveSlotSelect_Delete_No)) {
								@Override
								protected void onSelect(int index) {
									if(index==0) {
										SaveUtils.deleteSaveFromSlot(slotNameFromIndexAndMod(slotIndex), Dungeon.heroClass);
										WndSaveSlotSelect.this.hide();
										GameScene.show(new WndSaveSlotSelect(_saving));
									}
								}
							};
							GameScene.show(reallyDelete);
						}
					};
					deleteBtn.setPos(x + BUTTON_WIDTH - deleteBtn.width() - MARGIN, pos);
					additionalMargin = deleteBtn.width() + MARGIN;
					add(deleteBtn);
				}

				btn.setRect(x, pos, BUTTON_WIDTH - additionalMargin - MARGIN, BUTTON_HEIGHT);
				add(btn);
			}
			pos += BUTTON_HEIGHT + MARGIN;
		}

		resize(WIDTH, (int) pos);


		saving = _saving;

		if (!saving) {
			for (int i = 0; i < 10; i++) {
				if (!isSlotIndexUsed(i)) {
					buttons.get(i).enable(false);
				}
			}
		}

		if (PixelPonies.donated() == 0 && PixelPonies.canDonate()) {
			DonateButton btn = new DonateButton();
			add(btn);
			btn.setPos(width / 2 - btn.width() / 2, height);
			resize(width, (int) (height + btn.height()));
		}
	}

	private static boolean isSlotIndexUsed(int index) {
		return SaveUtils.slotUsed(slotNameFromIndex(index), Dungeon.heroClass)
				|| SaveUtils.slotUsed(slotNameFromIndexAndMod(index), Dungeon.heroClass);
	}

	private static String getSlotToLoad(int index) {
		String slot = slotNameFromIndexAndMod(index);
		if (SaveUtils.slotUsed(slot, Dungeon.heroClass)) {
			return slot;
		} else {
			return slotNameFromIndex(index);
		}
	}

	private static String windowText() {
		if (PixelPonies.donated() == 0 && PixelPonies.canDonate()) {
			return Game.getVar(R.string.WndSaveSlotSelect_dontLike);
		}
		return EMPTY_STRING;
	}

	private static String slotNameFromIndex(int i) {
		return Integer.toString(i + 1);
	}

	private static String slotNameFromIndexAndMod(int i) {
		return ModdingMode.activeMod() + "_" + slotNameFromIndex(i);
	}

	private static String[] slotInfos() {
		String[] ret = new String[10];

		for (int i = 0; i < ret.length; i++) {
			ret[i] = SaveUtils.slotInfo(getSlotToLoad(i), Dungeon.heroClass);
		}

		return ret;
	}

	protected void onSelect(int index) {
		final InterstitialPoint returnTo = this;

		if (saving) {
			try {
				Dungeon.saveAll();
				slot = slotNameFromIndexAndMod(index);
				SaveUtils.copySaveToSlot(slot, Dungeon.heroClass);

			} catch (Exception e) {
				throw new TrackedRuntimeException(e);
			}
		}

		Game.paused = true;

		slot = getSlotToLoad(index);

		returnToWork(true);
	}

	@Override
	public void returnToWork(boolean res) {
		Game.executeInGlThread(new Runnable() {
			@Override
			public void run() {
				Game.paused = false;

				if (!saving) {
					SaveUtils.loadGame(slot, Dungeon.hero.heroClass);
				}
			}
		});
	}

}
