package org.coursera.symptommanager.broadcastReceivers;

import org.coursera.symptommanager.services.ScreenReceiverService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * This is a Broadcast Receiver that will be triggered when the system detects
 * SCREEN_OFF and SCREEN_ON events. If that happens then the receiver starts a
 * service with the screen state boolean in order to know if the screen is ON or
 * OFF
 * 
 */
public class ScreenReceiver extends BroadcastReceiver {

	boolean status;

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
			status = true;
		} else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
			status = false;
		}
		Intent i = new Intent(context, ScreenReceiverService.class);
		i.putExtra("screen_state", status);
		context.startService(i);
	}

}
