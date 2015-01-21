package org.coursera.symptommanager.broadcastReceivers;

import java.util.Calendar;
import java.util.concurrent.Callable;

import org.coursera.symptommanager.CallableTask;
import org.coursera.symptommanager.Constants;
import org.coursera.symptommanager.LoginScreenActivity;
import org.coursera.symptommanager.SymptomSvc;
import org.coursera.symptommanager.TaskCallback;
import org.coursera.symptommanager.objects.PatientDetail;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

/**
 * This is a Broadcast Receiver that will handle the situation when a patient
 * faces 12 hours of I can't eat or severe pain or 16 hours of moderate pain. If
 * that happens an alarm fires and this receiver is triggered and shows a
 * notification to the user
 * 
 */
public class NotifyDoctorForPain extends BroadcastReceiver {

	// the name of the shared preferences
	String ID = "symptomMngr";
	// the parameter for the severe pain
	String SEVERE = "severe";
	// the parameter for the moderate pain
	String MODERATE = "moderate";
	// the parameter for the cant_eat pain
	String CANT_EAT = "cant_eat";

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
	// the parameter for the id
	String SEVERE_PAIN = "severe_pain";
	// the parameter for the id
	String MODERATE_PAIN = "moderate_pain";
	long severe = 0;
	long moderate = 0;
	long cantEat = 0;
	// the reason for the notification
	boolean severePain;
	boolean moderatePain;
	boolean cantEatPain;
	Context cnt;
	Uri alarmSound;

	@Override
	public void onReceive(Context context, Intent intent) {
		cnt = context;
		severePain = false;
		moderatePain = false;
		cantEatPain = false;
		getSharedPreferenciesData();
		if (intent.hasExtra(SEVERE)) {
			severe = intent.getLongExtra(SEVERE, 0);
			if (Constants.SEVERE_PAIN != 0) {
				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(severe);
				Calendar current = Calendar.getInstance();
				current.setTimeInMillis(Constants.SEVERE_PAIN);
				if ((current.get(Calendar.HOUR) - c.get(Calendar.HOUR))
				>= 12) {
					severePain = true;
				} else {

				}

			} else {
				Constants.SEVERE_PAIN = severe;
			}
			if (Constants.MODERATE_PAIN != 0) {
				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(severe);
				Calendar current = Calendar.getInstance();
				current.setTimeInMillis(Constants.MODERATE_PAIN);
				if ((current.get(Calendar.HOUR) - c.get(Calendar.HOUR))
				 >= 16) {

					moderatePain = true;
				} else {

				}

			} else {
				Constants.MODERATE_PAIN = severe;
			}
		}
		if (intent.hasExtra(MODERATE)) {
			moderate = intent.getLongExtra(MODERATE, 0);
			if (Constants.MODERATE_PAIN != 0) {
				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(moderate);
				Calendar current = Calendar.getInstance();
				current.setTimeInMillis(Constants.MODERATE_PAIN);
				if ((current.get(Calendar.HOUR) - c.get(Calendar.HOUR))
				 >= 16) {
					moderatePain = true;
				} else {

				}

			} else {
				Constants.MODERATE_PAIN = moderate;
			}
		}
		if (intent.hasExtra(CANT_EAT)) {
			cantEat = intent.getLongExtra(CANT_EAT, 0);
			if (Constants.CANT_EAT != 0) {
				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(cantEat);
				Calendar current = Calendar.getInstance();
				current.setTimeInMillis(Constants.CANT_EAT);
				if ((current.get(Calendar.HOUR) - c.get(Calendar.HOUR))
				 >= 12) {
					cantEatPain = true;
				} else {

				}

			} else {
				Constants.CANT_EAT = cantEat;
			}
			if (Constants.SEVERE_PAIN != 0) {
				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(cantEat);
				Calendar current = Calendar.getInstance();
				current.setTimeInMillis(Constants.SEVERE_PAIN);
				 if ((current.get(Calendar.HOUR) - c.get(Calendar.HOUR))
				 >= 12) {
					severePain = true;
				} else {

				}

			} else {
				Constants.SEVERE_PAIN = cantEat;
			}
			if (Constants.MODERATE_PAIN != 0) {
				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(cantEat);
				Calendar current = Calendar.getInstance();
				current.setTimeInMillis(Constants.MODERATE_PAIN);
				 if ((current.get(Calendar.HOUR) - c.get(Calendar.HOUR))
				 >= 16) {severePain = true;
				} else {

				}

			} else {
				Constants.MODERATE_PAIN = cantEat;
			}
		}
		saveConstantsValuesToSharedPrefs();if (!intent.hasExtra(MODERATE) && !intent.hasExtra(SEVERE)
				&& !intent.hasExtra(CANT_EAT)) {
			if (Constants.MODERATE_PAIN != 0) {
				Calendar c = Calendar.getInstance();
				Calendar c1 = Calendar.getInstance();
				c1.setTimeInMillis(Constants.MODERATE_PAIN);
			if ((c.get(Calendar.HOUR) - c1.get(Calendar.HOUR)) >= 16) {
					moderatePain = true;
				} else {

				}
			}
			if (Constants.CANT_EAT != 0) {
				Calendar c = Calendar.getInstance();
				Calendar c1 = Calendar.getInstance();
				c1.setTimeInMillis(Constants.CANT_EAT);
			if ((c.get(Calendar.HOUR) - c1.get(Calendar.HOUR)) >= 12) {
					cantEatPain = true;
				} else {

				}
			}

			if (Constants.SEVERE_PAIN != 0) {
				Calendar c = Calendar.getInstance();
				Calendar c1 = Calendar.getInstance();
				c1.setTimeInMillis(Constants.SEVERE_PAIN);
				if ((c.get(Calendar.HOUR) - c1.get(Calendar.HOUR)) >= 12) {
					severePain = true;
				} else {

				}
			}

		}
		if (severePain || cantEatPain || moderatePain) {
			getDoctorsIdAndSendPush();
//		} else {
//		saveConstantsValuesToSharedPrefs();
//		}
		}
	}

	private void getDoctorsIdAndSendPush() {
		new getDoctorId().execute();
	}

	public class getDoctorId extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {

			PatientDetail profile = Constants.svc
					.getProfilePatient(Constants.USERNAME);
			if (cantEatPain) {
				Constants.svc.sendPushDoctor(profile.getIdDoctor(), "Patient "
						+ profile.getName() + " " + profile.getSurname()
						+ " cant eat", "Your patient " + profile.getName()
						+ " " + profile.getSurname()
						+ " can't eat for the past 12 hours");
			} else if (severePain) {
				Constants.svc
						.sendPushDoctor(
								profile.getIdDoctor(),
								"Patient " + profile.getName() + " "
										+ profile.getSurname()
										+ " deals with severe pain",
								"Your patient "
										+ profile.getName()
										+ " "
										+ profile.getSurname()
										+ " deals with severe pain for the past 12 hours");
			} else if (moderatePain) {
				Constants.svc.sendPushDoctor(profile.getIdDoctor(), "Patient "
						+ profile.getName() + " " + profile.getSurname()
						+ " deals with moderate pain", "Your patient "
						+ profile.getName() + " " + profile.getSurname()
						+ " deals with moderate pain for the past 16 hours");
			}
			// get the shared preferences with mode private
			Constants.settings = cnt.getSharedPreferences(ID,
					Application.MODE_PRIVATE);
			Editor edit = Constants.settings.edit();
			Constants.SEVERE_PAIN = 0;
			Constants.MODERATE_PAIN = 0;
			Constants.CANT_EAT = 0;
			edit.putLong(SEVERE, Constants.SEVERE_PAIN);
			edit.putLong(MODERATE, Constants.MODERATE_PAIN);
			edit.putLong(CANT_EAT, Constants.CANT_EAT);
			edit.putString(USERNAME, Constants.USERNAME);
			edit.putString(PASSWORD, Constants.PASSWORD);
			edit.putString(SERVER, Constants.SERVER);
			edit.putString(ROLE, Constants.ROLE);
			edit.putLong(ID_PARAMETER, Constants.ID);
			
			edit.commit();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

		}

		@Override
		protected void onPreExecute() {
		}
	}

	/**
	 * Return all the values that are saved in the Shared Preferences
	 */
	private void getSharedPreferenciesData() {

		Constants.settings = cnt.getSharedPreferences(ID,
				Application.MODE_PRIVATE);
		// get the shared preferences with mode private
		Constants.USERNAME = Constants.settings.getString(USERNAME, "");
		Constants.PASSWORD = Constants.settings.getString(PASSWORD, "");
		Constants.ROLE = Constants.settings.getString(ROLE, "");
		Constants.SERVER = Constants.settings.getString(SERVER, "");
		Constants.ID = Constants.settings.getLong(ID_PARAMETER, -1);
		Constants.SEVERE_PAIN = Constants.settings.getLong(SEVERE, 0);
		Constants.MODERATE_PAIN = Constants.settings.getLong(MODERATE, 0);
		Constants.CANT_EAT = Constants.settings.getLong(CANT_EAT, 0);
		
		// if he/she is first we will authenticate the user and then
		// we will create a client with the credentials of the user
		authenticateUserAndCreateClient();

	}

	/**
	 * Saves the current values in the Shared Preferences
	 */
	private void saveConstantsValuesToSharedPrefs() {
		// get the shared preferences with mode private
		Constants.settings = cnt.getSharedPreferences(ID,
				Application.MODE_PRIVATE);
		Editor edit = Constants.settings.edit();
		edit.putLong(SEVERE, Constants.SEVERE_PAIN);
		edit.putLong(MODERATE, Constants.MODERATE_PAIN);
		edit.putLong(CANT_EAT, Constants.CANT_EAT);
		edit.putString(USERNAME, Constants.USERNAME);
		edit.putString(PASSWORD, Constants.PASSWORD);
		edit.putString(SERVER, Constants.SERVER);
		edit.putString(ROLE, Constants.ROLE);
		edit.putLong(ID_PARAMETER, Constants.ID);
		
		edit.commit();
		
	}

	/**
	 * Authenticate's the user and then create a client with the credentials of
	 * the user
	 */
	private void authenticateUserAndCreateClient() {
		Constants.svc = SymptomSvc.init(
				"https://" + Constants.SERVER + ":8443", Constants.USERNAME,
				Constants.PASSWORD);

		CallableTask.invoke(new Callable<Void>() {

			@Override
			public Void call() throws Exception {
				return Constants.svc.getTest();
			}
		}, new TaskCallback<Void>() {

			@Override
			public void success(Void result) {
				// OAuth 2.0 grant was successful
			}

			@Override
			public void error(Exception e) {
				Log.e(LoginScreenActivity.class.getName(),
						"Error logging in via OAuth.", e);

				Toast.makeText(
						cnt,
						"Login failed, check your Internet connection and credentials.",
						Toast.LENGTH_SHORT).show();
			}
		});

	}
}
