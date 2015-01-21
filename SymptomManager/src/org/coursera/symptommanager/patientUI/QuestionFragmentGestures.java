package org.coursera.symptommanager.patientUI;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.coursera.symptommanager.Constants;
import org.coursera.symptommanager.InternetStatus;
import org.coursera.symptommanager.R;
import org.coursera.symptommanager.objects.PatientDetail;
import org.coursera.symptommanager.objects.Question;

import retrofit.RetrofitError;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.gesture.Prediction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;

public class QuestionFragmentGestures extends Fragment {

	RelativeLayout layout;
	ProgressBar loading;
	TextView textView1;
	TextView textView2;
	TextView textView3;
	GestureOverlayView gOverlay;
	// gestures names
	String YES = "yes";
	String NO = "no";
	String ONE = "one";
	String TWO = "two";
	String THREE = "three";
	// the long that will hold the patient id which is send with the intent
	long patientID;
	// the strign that will hold the question title
	String title;
	// the string that will hold the question type
	String type;
	// error message
	String ERROR = "";
	// the integer that will hold the position of the pager adapter
	int position;
	// the parameter id
	static String ID = "id";
	static String TITLE = "title";
	static String TYPE = "type";
	static String POSITION = "position";
	// type 1 question
	Question q1;
	// type 2 question
	Question q2;
	// type 3 question
	Question q3;

	GestureLibrary mLibrary;

	public static QuestionFragmentGestures newInstance(long patientID,
			String title, String type, int position) {
		QuestionFragmentGestures fragment = new QuestionFragmentGestures();
		Bundle args = new Bundle();
		args.putLong(ID, patientID);
		args.putString(TITLE, title);
		args.putString(TYPE, type);
		args.putInt(POSITION, position);
		fragment.setArguments(args);
		return fragment;
	}

	// when fragments is created
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// the bundle has the extra id get it and save it as
		// doctorID
		Bundle bundle = this.getArguments();
		patientID = bundle.getLong(ID);
		title = bundle.getString(TITLE);
		type = bundle.getString(TYPE);
		position = bundle.getInt(POSITION);
		if (type.equalsIgnoreCase("3")) {
			// that means that in the last question the fragment will create an
			// option menu in the action bar
			setHasOptionsMenu(true);
		}
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		ViewGroup rootView = (ViewGroup) inflater.inflate(
				R.layout.question_fragment_gestures, container, false);
		mLibrary = GestureLibraries.fromRawResource(getActivity(),
				R.raw.gestures);
		if (!mLibrary.load()) {
			getActivity().finish();
		}
		textView1 = (TextView) rootView.findViewById(R.id.textView1);
		textView2 = (TextView) rootView.findViewById(R.id.textView2);
		textView3 = (TextView) rootView.findViewById(R.id.textView3);
		gOverlay = (GestureOverlayView) rootView.findViewById(R.id.gOverlay);
		layout = (RelativeLayout) rootView.findViewById(R.id.layout);
		loading = (ProgressBar) rootView.findViewById(R.id.progressBar1);
		loading.setVisibility(View.GONE);
		textView1.setText(title);
		if (type.equalsIgnoreCase("1")) {
			textView2.setText(getString(R.string.hint_question1));
			if (StartCheckInGestures.questions.get(position) != null){
				gOverlay.setVisibility(View.GONE);
				textView3.setText(StartCheckInGestures.questions.get(position).getResponse());
			} else {
				gOverlay.setVisibility(View.VISIBLE);	
			}
			gOverlay.addOnGesturePerformedListener(new OnGesturePerformedListener() {

				@Override
				public void onGesturePerformed(GestureOverlayView overlay,
						Gesture gesture) {
					// Get gesture predictions
					ArrayList<Prediction> predictions = mLibrary
							.recognize(gesture);
					// Ignore weak predictions
					// Get highest-ranked prediction
					if (predictions.size() > 0) {
						Prediction prediction = predictions.get(0);

						if (prediction.score > 2.0) {
							if (prediction.name.equals(ONE)) {

								q1 = new Question(1, title,
										getString(R.string.well_controlled), 0);
								StartCheckInGestures.questions
										.set(position, q1);
								textView3
										.setText(getString(R.string.well_controlled));
								Toast.makeText(getActivity(),
										getString(R.string.well_controlled),
										Toast.LENGTH_SHORT).show();
								gOverlay.setVisibility(View.GONE);
								
								if ((position + 1) == StartCheckInGestures.NUM_PAGES) {

								} else {
									StartCheckInGestures.mPager
											.setCurrentItem(position + 1);
								}
							} else if (prediction.name.equals(TWO)) {

								q1 = new Question(1, title,
										getString(R.string.moderate), 0);
								StartCheckInGestures.questions
										.set(position, q1);
								textView3.setText(getString(R.string.moderate));
								Toast.makeText(getActivity(),
										getString(R.string.moderate),
										Toast.LENGTH_SHORT).show();
								gOverlay.setVisibility(View.GONE);
								if ((position + 1) == StartCheckInGestures.NUM_PAGES) {

								} else {
									StartCheckInGestures.mPager
											.setCurrentItem(position + 1);
								}
							} else if (prediction.name.equals(THREE)) {

								q1 = new Question(1, title,
										getString(R.string.severe), 0);
								StartCheckInGestures.questions
										.set(position, q1);
								textView3.setText(getString(R.string.severe));
								Toast.makeText(getActivity(),
										getString(R.string.severe),
										Toast.LENGTH_SHORT).show();
								gOverlay.setVisibility(View.GONE);
								if ((position + 1) == StartCheckInGestures.NUM_PAGES) {

								} else {
									StartCheckInGestures.mPager
											.setCurrentItem(position + 1);
								}
							} else {
								Toast.makeText(getActivity(), "No prediction",
										Toast.LENGTH_SHORT).show();

							}
						}
					}
				}

			});

		} else if (type.equalsIgnoreCase("2")) {
			textView2.setText(getString(R.string.hint_question2));
			if (StartCheckInGestures.questions.get(position) != null){
				gOverlay.setVisibility(View.GONE);
				textView3.setText(StartCheckInGestures.questions.get(position).getResponse());
			} else {
				gOverlay.setVisibility(View.VISIBLE);	
			}
			gOverlay.addOnGesturePerformedListener(new OnGesturePerformedListener() {

				@Override
				public void onGesturePerformed(GestureOverlayView overlay,
						Gesture gesture) {
					// Get gesture predictions
					ArrayList<Prediction> predictions = mLibrary
							.recognize(gesture);
					// Ignore weak predictions
					// Get highest-ranked prediction
					if (predictions.size() > 0) {
						Prediction prediction = predictions.get(0);

						if (prediction.score > 2.0) {
							if (prediction.name.equals(YES)) {

								selectDatetime();
								textView3.setText(getString(R.string.yes));
								Toast.makeText(getActivity(),
										getString(R.string.yes),
										Toast.LENGTH_SHORT).show();
								gOverlay.setVisibility(View.GONE);
							} else if (prediction.name.equals(NO)) {

								q2 = new Question(2, title,
										getString(R.string.no), 0);
								StartCheckInGestures.questions
										.set(position, q2);
								textView3.setText(getString(R.string.no));
								Toast.makeText(getActivity(),
										getString(R.string.no),
										Toast.LENGTH_SHORT).show();
								gOverlay.setVisibility(View.GONE);
								if ((position + 1) == StartCheckInGestures.NUM_PAGES) {

								} else {
									StartCheckInGestures.mPager
											.setCurrentItem(position + 1);
								}
							} else {
								Toast.makeText(getActivity(), "No prediction",
										Toast.LENGTH_SHORT).show();

							}
						}
					}
				}

			});

		} else if (type.equalsIgnoreCase("3")) {
			textView2.setText(getString(R.string.hint_question3));
	
			if (StartCheckInGestures.questions.get(position) != null){
				gOverlay.setVisibility(View.GONE);
				textView3.setText(StartCheckInGestures.questions.get(position).getResponse());
			} else {
				gOverlay.setVisibility(View.VISIBLE);	
			}
			gOverlay.addOnGesturePerformedListener(new OnGesturePerformedListener() {

				@Override
				public void onGesturePerformed(GestureOverlayView overlay,
						Gesture gesture) {
					// Get gesture predictions
					ArrayList<Prediction> predictions = mLibrary
							.recognize(gesture);
					// Ignore weak predictions
					// Get highest-ranked prediction
					if (predictions.size() > 0) {
						Prediction prediction = predictions.get(0);

						if (prediction.score > 2.0) {
							if (prediction.name.equals(ONE)) {

								q3 = new Question(3, title,
										getString(R.string.no), 0);
								StartCheckInGestures.questions
										.set(position, q3);
								textView3.setText(getString(R.string.no));
								Toast.makeText(getActivity(),
										getString(R.string.no),
										Toast.LENGTH_SHORT).show();
								gOverlay.setVisibility(View.GONE);
								if ((position + 1) == StartCheckInGestures.NUM_PAGES) {

								} else {
									StartCheckInGestures.mPager
											.setCurrentItem(position + 1);
								}
							} else if (prediction.name.equals(TWO)) {

								q3 = new Question(3, title,
										getString(R.string.some), 0);
								StartCheckInGestures.questions
										.set(position, q3);
								textView3.setText(getString(R.string.some));
								Toast.makeText(getActivity(),
										getString(R.string.some),
										Toast.LENGTH_SHORT).show();
								gOverlay.setVisibility(View.GONE);
								if ((position + 1) == StartCheckInGestures.NUM_PAGES) {

								} else {
									StartCheckInGestures.mPager
											.setCurrentItem(position + 1);
								}
							} else if (prediction.name.equals(THREE)) {

								q3 = new Question(3, title,
										getString(R.string.eat), 0);
								StartCheckInGestures.questions
										.set(position, q3);
								textView3.setText(getString(R.string.eat));
								Toast.makeText(getActivity(),
										getString(R.string.eat),
										Toast.LENGTH_SHORT).show();
								gOverlay.setVisibility(View.GONE);
								if ((position + 1) == StartCheckInGestures.NUM_PAGES) {

								} else {
									StartCheckInGestures.mPager
											.setCurrentItem(position + 1);
								}
							} else {
								Toast.makeText(getActivity(), "No prediction",
										Toast.LENGTH_SHORT).show();

							}
						}
					}
				}

			});

		}

		return rootView;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// Inflate the menu items for use in the action bar
		inflater.inflate(
				getResources().getIdentifier("send", "menu",
						getActivity().getPackageName()), menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// get the id of the clicked item
		int itemId = item.getItemId();
		// get the id of the send button
		int menuBtnId = getResources().getIdentifier("send", "id",
				getActivity().getPackageName());

		if (itemId == menuBtnId) {
			boolean exit = false;
			for (int i = 0; i < StartCheckInGestures.questions.size(); i++) {
				if (StartCheckInGestures.questions.get(i) == null) {
					exit = true;
				}
			}
			// if we click the menu item
			if (StartCheckInGestures.questions.size() == 0) {
				Toast.makeText(getActivity(),
						getString(R.string.missing_questions),
						Toast.LENGTH_LONG).show();
			} else {
				if (exit) {
					Toast.makeText(getActivity(),
							getString(R.string.missing_questions),
							Toast.LENGTH_LONG).show();
				} else {
					if (!InternetStatus.getInstance(
							getActivity()).isOnline(getActivity())) {
						Toast.makeText(getActivity(), R.string.no_internet,
								Toast.LENGTH_LONG).show();

					} else {
					new send().execute();
					}
				}
			}
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}

	}

	private void selectDatetime() {
		if (isAdded()) {
			DialogFragment newFragment = new DatePickerFragment();
			newFragment.show(getFragmentManager(), "datePicker");
		}
	}

	static Calendar cal = Calendar.getInstance();

	public class DatePickerFragment extends DialogFragment implements
			DatePickerDialog.OnDateSetListener {
		boolean mFirst = true;

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current date as the default date in the picker
			final Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);
			DatePickerDialog d = new DatePickerDialog(getActivity(), this,
					year, month, day);
			DatePicker dp = d.getDatePicker();
			dp.setMaxDate(Calendar.getInstance().getTimeInMillis() - 1000);
			// Create a new instance of DatePickerDialog and return it
			return d;
		}

		public void onDateSet(DatePicker view, int year, int month, int day) {
			if (mFirst) {
				mFirst = false;
				cal.set(year, month, day);
				DialogFragment newFragment = new TimePickerFragment();
				newFragment.show(getFragmentManager(), "timePicker");
			}
		}
	}

	public class TimePickerFragment extends DialogFragment implements
			TimePickerDialog.OnTimeSetListener {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current time as the default values for the picker
			final Calendar c = Calendar.getInstance();
			int hour = c.get(Calendar.HOUR_OF_DAY);
			int minute = c.get(Calendar.MINUTE);

			// Create a new instance of TimePickerDialog and return it
			return new TimePickerDialog(getActivity(), this, hour, minute,
					DateFormat.is24HourFormat(getActivity()));
		}

		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
			cal.set(Calendar.MINUTE, minute);

			q2 = new Question(2, title, getString(R.string.yes),
					cal.getTimeInMillis());
			StartCheckInGestures.questions.set(position, q2);
			if ((position + 1) == StartCheckInGestures.NUM_PAGES) {

			} else {
				StartCheckInGestures.mPager.setCurrentItem(position + 1);
			}
		}
	}

	public class CustomTimePickerDialog extends TimePickerDialog {

		private final static int TIME_PICKER_INTERVAL = 30;
		private TimePicker timePicker;
		private final OnTimeSetListener callback;

		public CustomTimePickerDialog(Context context,
				OnTimeSetListener callBack, int hourOfDay, int minute,
				boolean is24HourView) {
			super(context, callBack, hourOfDay, minute / TIME_PICKER_INTERVAL,
					is24HourView);
			this.callback = callBack;
		}

		@Override
		public void onClick(DialogInterface dialog, int which) {
			if (callback != null && timePicker != null) {
				timePicker.clearFocus();
				callback.onTimeSet(timePicker, timePicker.getCurrentHour(),
						timePicker.getCurrentMinute() * TIME_PICKER_INTERVAL);
			}
		}

		@Override
		protected void onStop() {
		}

		@Override
		public void onAttachedToWindow() {
			super.onAttachedToWindow();
			try {
				Class<?> classForid = Class
						.forName("com.android.internal.R$id");
				Field timePickerField = classForid.getField("timePicker");
				this.timePicker = (TimePicker) findViewById(timePickerField
						.getInt(null));
				Field field = classForid.getField("minute");

				NumberPicker mMinuteSpinner = (NumberPicker) timePicker
						.findViewById(field.getInt(null));
				mMinuteSpinner.setMinValue(0);
				mMinuteSpinner.setMaxValue((60 / TIME_PICKER_INTERVAL) - 1);
				List<String> displayedValues = new ArrayList<String>();
				for (int i = 0; i < 60; i += TIME_PICKER_INTERVAL) {
					displayedValues.add(String.format("%02d", i));
				}
				mMinuteSpinner.setDisplayedValues(displayedValues
						.toArray(new String[0]));

				Field field1 = classForid.getField("hour");

				NumberPicker mHourSpinner = (NumberPicker) timePicker
						.findViewById(field1.getInt(null));
				mHourSpinner.setMinValue(9);
				mHourSpinner.setMaxValue(23);
				List<String> displayedValues1 = new ArrayList<String>();
				for (int i = 9; i < 24; i += 1) {
					displayedValues1.add(String.format("%02d", i));
				}
				mHourSpinner.setDisplayedValues(displayedValues1
						.toArray(new String[0]));
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	public class send extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			try {
				String raw = new Gson().toJson(StartCheckInGestures.questions);
				Constants.svc.sendCheckIn(-1, raw, patientID, Calendar
						.getInstance().getTimeInMillis());
				PatientDetail profile = Constants.svc
						.getPatientProfile(patientID);
				profile.setLastCheckIn(Calendar.getInstance().getTimeInMillis());
				// PatientDetail newProfile =
				Constants.svc.updateProfilePatient(profile);
				checkTimes();
				if (profile.getIdDoctor() == -1) {
				} else {
					Constants.svc.sendPushDoctor(profile.getIdDoctor(),
							profile.getName() + " " + profile.getSurname()
									+ " just submit a check up",
							profile.getName() + " " + profile.getSurname()
									+ " just submit a check up");
				}
			} catch (final RetrofitError e) {
				Constants.reportIssue(getActivity(), e.getMessage());
				ERROR = e.getMessage();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			loading.setVisibility(View.GONE);
			layout.setClickable(true);
			Intent intent = new Intent(getActivity(), MainPatient.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
					| Intent.FLAG_ACTIVITY_CLEAR_TASK);
			startActivity(intent);
			if (ERROR.equalsIgnoreCase("")) {
				getActivity().finish();
			} else {
				ERROR = "";
			}

		}

		@Override
		protected void onPreExecute() {
			loading.setVisibility(View.VISIBLE);
			layout.setClickable(false);
		}
	}

	private void checkTimes() {

		// one hour
		int REFRESH_RATE = 60 * 60 * 1000;
		AlarmManager mAlarmManager = (AlarmManager) getActivity()
				.getSystemService(Activity.ALARM_SERVICE);
		Intent mNotificationReceiverIntent = new Intent(
				getActivity(),
				org.coursera.symptommanager.broadcastReceivers.NotifyDoctorForPain.class);
		mNotificationReceiverIntent
				.setAction("org.coursera.systemmanager.firstIntent");
		for (int i = 0; i < StartCheckInGestures.questions.size(); i++) {
			 if (StartCheckInGestures.questions.get(i).getId() == 3) {
					if (StartCheckInGestures.questions.get(i).getResponse()
							.equalsIgnoreCase(getString(R.string.eat))) {
						mNotificationReceiverIntent.putExtra("cant_eat", Calendar
								.getInstance().getTimeInMillis());
					}
				} else if (StartCheckInGestures.questions.get(i).getId() == 1) {
				if (StartCheckInGestures.questions.get(i).getResponse()
						.equalsIgnoreCase(getString(R.string.moderate))) {
					mNotificationReceiverIntent.putExtra("moderate", Calendar
							.getInstance().getTimeInMillis());
				} else if (StartCheckInGestures.questions.get(i).getResponse()
						.equalsIgnoreCase(getString(R.string.severe))) {
					mNotificationReceiverIntent.putExtra("severe", Calendar
							.getInstance().getTimeInMillis());
				}
			}
		}
		// generate a single alarm with the extras in the intent and fire now
		PendingIntent mNotificationReceiverPendingIntent = PendingIntent
				.getBroadcast(getActivity(), (int)Calendar
						.getInstance().getTimeInMillis(), mNotificationReceiverIntent, 0);
		mAlarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
				SystemClock.elapsedRealtime(),
				mNotificationReceiverPendingIntent);
		// and then generate another alarm with no extras and file in one hour
		// and every one hour since
		Intent mNotificationReceiverIntent1 = new Intent(
				getActivity(),
				org.coursera.symptommanager.broadcastReceivers.NotifyDoctorForPain.class);
		PendingIntent mNotificationReceiverPendingIntent1 = PendingIntent
				.getBroadcast(getActivity(), 0, mNotificationReceiverIntent1,
						PendingIntent.FLAG_UPDATE_CURRENT);
		mAlarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
				SystemClock.elapsedRealtime() + REFRESH_RATE, REFRESH_RATE,
				mNotificationReceiverPendingIntent1);
	}
}
