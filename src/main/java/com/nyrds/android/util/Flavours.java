package com.nyrds.android.util;

import com.nyrds.pixeldungeon.ml.BuildConfig;
import com.nyrds.pixeldungeon.support.Iap;

public class Flavours {

	public static final String CHROME_WEB_STORE = "ChromeWebStore";
	public static final String AMAZON           = "Amazon";
	public static final String YANDEX           = "Yandex";
	public static final String GOOGLE_PLAY      = "GooglePlay";

	//Turning off hats completely
	public static boolean haveHats() {
		return false;
//		return BuildConfig.FLAVOR.equals(GOOGLE_PLAY) && Iap.googleIapUsable();
	}

	//Turning off donations completely
	public static boolean haveDonations() {
		return false;
//		return (BuildConfig.FLAVOR.equals(GOOGLE_PLAY) || BuildConfig.FLAVOR.equals(CHROME_WEB_STORE))
//				&& Iap.googleIapUsable();
	}

	//Turning off ads completely
	public static boolean haveAds() {
		return false;
//		return BuildConfig.FLAVOR.equals(GOOGLE_PLAY)
//				|| BuildConfig.FLAVOR.equals(AMAZON)
//				|| BuildConfig.FLAVOR.equals(YANDEX);
	}
}
