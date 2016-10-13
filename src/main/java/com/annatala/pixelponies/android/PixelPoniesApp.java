package com.annatala.pixelponies.android;

import android.app.Application;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

@ReportsCrashes(mailTo = "wolf.trickster@gmail.com", mode = ReportingInteractionMode.TOAST, resToastText = R.string.PixelPoniesApp_SendCrash)
public class PixelPoniesApp extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		if(!BuildConfig.DEBUG) {
			ACRA.init(this);
		}

		try {
			Class.forName("android.os.AsyncTask");
		} catch (Throwable ignore) {
		}
	}
}
