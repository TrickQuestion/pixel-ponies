package com.nyrds.pixeldungeon.support;

import android.graphics.Color;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.nyrds.android.util.Flavours;
import com.nyrds.android.util.Util;
import com.nyrds.pixeldungeon.ml.R;
import com.watabou.noosa.Game;
import com.watabou.noosa.InterstitialPoint;
import com.watabou.pixeldungeon.PixelDungeon;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mike on 24.05.2016.
 */
public class Ads {
	private static InterstitialAd mSaveAndLoadAd;
	private static InterstitialAd mEasyModeSmallScreenAd;

	private static boolean isSmallScreen() {
		return (Game.width() < 400 || Game.height() < 400);
	}

	private static boolean needDisplaySmallScreenEasyModeIs() {
		return Game.getDifficulty() == 0 && isSmallScreen() && PixelDungeon.donated() == 0;
	}

	private static boolean googleAdsUsable() {
		return Flavours.haveAds();
	}

	public static void displayEasyModeBanner() {
		if (googleAdsUsable() && Util.isConnectedToInternet()) {
			if (isSmallScreen()) {
				initEasyModeIntersitial();
			} else {
				Game.instance().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (Game.instance().getLayout().getChildCount() == 1) {
							AdView adView = new AdView(Game.instance());
							adView.setAdSize(AdSize.SMART_BANNER);
							adView.setAdUnitId(Game.getVar(R.string.easyModeAdUnitId));
							adView.setBackgroundColor(Color.TRANSPARENT);
							AdRequest adRequest = new AdRequest.Builder().addTestDevice(Game.getVar(R.string.testDevice))
									.build();
							Game.instance().getLayout().addView(adView, 0);
							adView.loadAd(adRequest);
							Game.setNeedSceneRestart(true);
						}
					}
				});
			}
		}
	}

	public static void removeEasyModeBanner() {
		if (googleAdsUsable()) {
			Game.instance().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					if (Game.instance().getLayout().getChildCount() == 2) {
						Game.instance().getLayout().removeViewAt(0);
						Game.setNeedSceneRestart(true);
					}
				}

			});
		}
	}

	private static Map<InterstitialAd, Boolean> mAdLoadInProgress = new HashMap<>();

	private static void requestNewInterstitial(final InterstitialAd isAd) {

		Boolean loadAlreadyInProgress = mAdLoadInProgress.get(isAd);

		if (loadAlreadyInProgress != null && loadAlreadyInProgress) {
			return;
		}

		AdRequest adRequest = new AdRequest.Builder().addTestDevice(Game.getVar(R.string.testDevice)).build();

		isAd.setAdListener(new AdListener() {
			@Override
			public void onAdClosed() {
				super.onAdClosed();
			}

			@Override
			public void onAdFailedToLoad(int errorCode) {
				super.onAdFailedToLoad(errorCode);
				mAdLoadInProgress.put(isAd, false);
			}

			@Override
			public void onAdLoaded() {
				super.onAdLoaded();
				mAdLoadInProgress.put(isAd, false);
			}

			@Override
			public void onAdOpened() {
				super.onAdOpened();
			}

			@Override
			public void onAdLeftApplication() {
				super.onAdLeftApplication();
			}
		});

		mAdLoadInProgress.put(isAd, true);
		isAd.loadAd(adRequest);

	}

	private static void displayIsAd(final InterstitialPoint work, final InterstitialAd isAd) {
		Game.instance().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (isAd == null) {
					work.returnToWork(false);
					return;
				}

				if (!isAd.isLoaded()) {
					work.returnToWork(false);
					return;
				}

				isAd.setAdListener(new AdListener() {
					@Override
					public void onAdClosed() {
						requestNewInterstitial(isAd);
						work.returnToWork(true);
					}
				});
				isAd.show();
			}
		});
	}

	public static void displaySaveAndLoadAd(final InterstitialPoint work) {
		displayIsAd(work, mSaveAndLoadAd);
	}

	public static void displayEasyModeSmallScreenAd(final InterstitialPoint work) {
		if(needDisplaySmallScreenEasyModeIs()) {
			displayIsAd(work, mEasyModeSmallScreenAd);
		} else {
			work.returnToWork(true);
		}
	}

	private static void initEasyModeIntersitial() {
		if (googleAdsUsable() && Util.isConnectedToInternet()) {
			{
				Game.instance().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (mEasyModeSmallScreenAd == null) {
							mEasyModeSmallScreenAd = new InterstitialAd(Game.instance());
							mEasyModeSmallScreenAd.setAdUnitId(Game.getVar(R.string.easyModeSmallScreenAdUnitId));
							requestNewInterstitial(mEasyModeSmallScreenAd);
						}
					}
				});
			}
		}
	}

	public static void initSaveAndLoadIntersitial() {
		if (googleAdsUsable() && Util.isConnectedToInternet()) {
			{
				Game.instance().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (mSaveAndLoadAd == null) {
							mSaveAndLoadAd = new InterstitialAd(Game.instance());
							mSaveAndLoadAd.setAdUnitId(Game.getVar(R.string.saveLoadAdUnitId));
							requestNewInterstitial(mSaveAndLoadAd);
						}
					}
				});
			}
		}
	}


}
