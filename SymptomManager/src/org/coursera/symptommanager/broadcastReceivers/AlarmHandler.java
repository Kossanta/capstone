package org.coursera.symptommanager.broadcastReceivers;

import org.coursera.symptommanager.Constants;
import org.coursera.symptommanager.R;
import org.coursera.symptommanager.WakeLocker;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;

/**
 * 
 * This is the Broadcast Receiver that will be triggered by the Alarm the
 * application fires when the patient had set them to fire
 * 
 */
public class AlarmHandler extends BroadcastReceiver {
	// Notification ID to allow for future updates
	private static final int MY_NOTIFICATION_ID = 1;

	// Notification Text Elements
	private CharSequence contentTitle;
	private CharSequence contentText;
	// the name of the shared preferences
	String ID = "symptomMngr";
	// the parameter for the username
	String USERNAME = "username";
	// the parameter for the password
	String PASSWORD = "password";
	// the parameter for the server
	String SERVER = "server";
	// the parameter for the role of the user
	String ROLE = "role";
	// the parameter for the role Doctor
	String DOCTOR = "doctor";
	// the parameter for the id
	String ID_PARAMETER = "id";
	// Notification Action Elements
	private Intent mNotificationIntent;
	private PendingIntent mContentIntent;
	Context cnt;
	Uri alarmSound;
	private long[] mVibratePattern = { 0, 200, 200, 300 };

	@Override
	public void onReceive(Context context, Intent intent) {
		cnt = context;

		if (checkIfUserIsLoggedIn(context)) {
			createNotification();
		} else {

		}

	}
	/**
	 * Creates a notification an show to the user the appropriate messsage
	 */
	private void createNotification() {
		WakeLocker.acquire(cnt);
		alarmSound = RingtoneManager
				.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		mNotificationIntent = new Intent(
				"org.coursera.symptommanager.action_symptom_manager");
		mContentIntent = PendingIntent.getActivity(cnt, 0, mNotificationIntent,
				Intent.FLAG_ACTIVITY_NEW_TASK);
		contentTitle = cnt.getResources().getString(R.string.alarm_title);
		contentText = contentTitle;
		Notification.Builder notificationBuilder = new Notification.Builder(cnt)
				//.setSmallIcon(android.R.drawable.stat_sys_warning)
		.setSmallIcon(R.drawable.alert)
				.setAutoCancel(true).setContentTitle(contentTitle)
				.setTicker(contentTitle)
				.setContentText(contentText).setContentIntent(mContentIntent)
				.setVibrate(mVibratePattern).setSound(alarmSound);

		// Pass the Notification to the NotificationManager:
		NotificationManager mNotificationManager = (NotificationManager) cnt
				.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(MY_NOTIFICATION_ID,
				notificationBuilder.build());
		//WakeLocker.release();

	}

	/**
	 * if username/password is NOT empty the user is logged in and the method
	 * returns true otherwise is NOT logged in and the method returns false
	 */
	private boolean checkIfUserIsLoggedIn(Context context) {
		Constants.settings = context.getSharedPreferences(ID,
				Application.MODE_PRIVATE);
		Constants.USERNAME = Constants.settings.getString(USERNAME, "");
		Constants.PASSWORD = Constants.settings.getString(PASSWORD, "");
		Constants.ROLE = Constants.settings.getString(ROLE, "");
		Constants.SERVER = Constants.settings.getString(SERVER, "");
		Constants.ID = Constants.settings.getLong(ID_PARAMETER, -1);

		if (Constants.USERNAME.equalsIgnoreCase("")) {
			return false;
		} else {
			return true;
		}
	}
}
