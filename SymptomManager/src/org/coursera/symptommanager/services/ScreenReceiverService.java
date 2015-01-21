package org.coursera.symptommanager.services;

import org.coursera.symptommanager.Constants;
import org.coursera.symptommanager.broadcastReceivers.ScreenReceiver;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

/**
 * This service registers the receiver in it's onCreate method for the ON OFF
 * screen events. We do that because in Android we cant register this receiver
 * in the manifest
 * 
 */
public class ScreenReceiverService extends Service {

	// parameter for the boolean extra in the intent
	String STATE = "screen_state";

	@Override
	public void onCreate() {
		super.onCreate();
		// register a broadcast receiver that will handle the on/off screen
		// logic
		IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		BroadcastReceiver mReceiver = new ScreenReceiver();
		registerReceiver(mReceiver, filter);
	}

	@Override
	public void onStart(Intent intent, int startId) {
		if (intent == null) {
		} else {
			boolean screenOn = intent.getBooleanExtra(STATE, true);
			if (!screenOn) {
				Constants.SCREEN_ON = true;
			} else {
				Constants.SCREEN_ON = false;
			}
		}
	}

	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}
}