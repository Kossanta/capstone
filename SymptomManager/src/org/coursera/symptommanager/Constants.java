package org.coursera.symptommanager;

import java.util.ArrayList;
import java.util.UUID;

import org.coursera.symptommanager.objects.Account;
import org.coursera.symptommanager.objects.DoctorDetail;
import org.coursera.symptommanager.objects.PatientDetail;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * This is a class that contains static variables
 * 
 */
public class Constants {

	// the static variable of the flag for the screen on off
	public static boolean SCREEN_ON;
	// the static variable for the shared preferences that will hold useful data
	// for the application like if the user is logged in
	public static SharedPreferences settings;
	// the static variable of the username of the logged in user which is saved
	// in the shared preferences in order to let us know if the user is logged
	// in
	public static String USERNAME;
	// the static variable of the password of the logged in user which is saved
	// in the shared preferences in order to let us know if the user is logged
	// in
	public static String PASSWORD;
	// the static variable of the role of the logged in user which is saved
	// in the shared preferences in order to let us know what UI flow to follow
	public static String ROLE;
	// the static variable of the server that user has logged in which is saved
	// in the shared preferences in order to automatic login
	public static String SERVER;
	// the static variable of the id that user has logged in which is saved
	// in the shared preferences in order to automatic login
	public static long ID;
	// the static variable of the severe pain that user has logged in which is
	// saved
	// in the shared preferences in order to automatic login
	public static long SEVERE_PAIN;
	// the static variable of the moderate pain that user has logged in which is
	// saved
	// in the shared preferences in order to automatic login
	public static long MODERATE_PAIN;
	// the static variable of the cant eat that user has logged in which is
	// saved
	// in the shared preferences in order to automatic login
	public static long CANT_EAT;
	// this is the project ID from the Google APIs which is needed for the push
	// to identify the application (I have create a new project in the Google
	// APIs with the name Coursera - Capstone Project
	public static String project_id = "535692598169";
	// this is the registration id for the push notification
	// CAUTION this id is for the device as this is how the push notification
	// work please refer to
	// https://developer.android.com/google/gcm/index.html
	// The idea is that when a user (doctor or patient) log in in the
	// application it will automatically take this registration id. Even if the
	// user logs out he/she will still has this registration id (I do that
	// because I want the phone to keep receiving notifications if needed even
	// if the
	// user has logged out for a reason.
	public static String regId;
	// This is the client the will execute the web services
	public static SymptomSvcApi svc;
	// here we will declare the account of the user which is public static so we
	// can access it from everywhere if needed
	public static Account account;

	/**
	 * this is a method that will show with an annimation a welcome message on
	 * the screen when the user open the application an is logged in or when the
	 * screen opens with the application on it and the user is logged in of
	 * course
	 */
	public static void showWelcomeMsg(Context context, RelativeLayout layout) {
		final TextView text = new TextView(context);
		Constants.SCREEN_ON = false;
		text.setBackgroundColor(Color.GRAY);
		RelativeLayout.LayoutParams params;
		text.setTextColor(Color.BLACK);
		text.setText(context.getText(R.string.welcome_msg) + " "
				+ Constants.USERNAME);

		text.setGravity(Gravity.CENTER);
		text.setPadding(0, 16, 0, 16);
		params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_HORIZONTAL);
		params.topMargin = -160;
		layout.addView(text, params);
		TranslateAnimation moveDown;
		moveDown = new TranslateAnimation(0, 0, 0, 500);
		moveDown.setDuration(2000);
		moveDown.setFillAfter(true);
		moveDown.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				TranslateAnimation moveUp = new TranslateAnimation(0, 0, 500, 0);

				moveUp.setDuration(2000);
				moveUp.setFillAfter(true);
				text.startAnimation(moveUp);

			}
		});
		text.startAnimation(moveDown);
	}

	// this is the list of account that will hold all the users
	public static ArrayList<Account> listOfAccounts;
	public static ArrayList<DoctorDetail> listOfDoctors;
	public static ArrayList<PatientDetail> listOfPatients;

	/**
	 * This method fill all the list with hard coded users. NOTE this is used
	 * because I use hard coded users and patient and doctors. This is
	 * acceptable by the seminar according to the professors
	 */
	public static void fillListWithUsers() {
		listOfAccounts = new ArrayList<Account>();
		listOfDoctors = new ArrayList<DoctorDetail>();
		listOfPatients = new ArrayList<PatientDetail>();
		listOfAccounts.add(new Account(-1, "drJohn", "dr32i", "", "doctor"));
		listOfAccounts.add(new Account(-1, "drPeter", "dr54j", "", "doctor"));
		listOfAccounts.add(new Account(-1, "george", "pt12o", "", "patient"));
		listOfAccounts.add(new Account(-1, "hope", "pt66n", "", "patient"));
		listOfAccounts.add(new Account(-1, "maria", "pt19j", "", "patient"));
		listOfAccounts.add(new Account(-1, "paul", "pt06q", "", "patient"));
		listOfDoctors.add(new DoctorDetail("drJohn", "John", "Smith", "", "-",
				"", -1));
		listOfDoctors.add(new DoctorDetail("drPeter", "Peter", "Smith", "", "-",
				"", -1));
		listOfPatients.add(new PatientDetail("george", "George", "Smith", "",
				"-", 0, "male", -1, -1, "", -1, "[]", 0, 0, 0, UUID.randomUUID()
						.toString()));
		listOfPatients.add(new PatientDetail("hope", "Hope", "Smith", "", "-",
				0, "female", -1, -1, "", -1, "[]", 0, 0, 0, UUID.randomUUID()
						.toString()));
		listOfPatients.add(new PatientDetail("maria", "Mary", "Smith", "", "-",
				0, "female", -1, -1, "", -1, "[]", 0, 0, 0, UUID.randomUUID()
						.toString()));
		listOfPatients.add(new PatientDetail("paul", "Paul", "Smith", "", "-",
				0, "male", -1, -1, "", -1, "[]", 0, 0, 0, UUID.randomUUID()
						.toString()));

	}

	/**
	 * This is a method that shows a Toast method in a background thread
	 * 
	 * @param activity
	 *            = is the context of the activity where the method is being
	 *            called
	 * @param message
	 *            = is the message the toast will show to the user
	 */
	public static void reportIssue(final Activity activity, final String message) {
		activity.runOnUiThread(new Runnable() {
			public void run() {
				Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
			}
		});
	}

}
