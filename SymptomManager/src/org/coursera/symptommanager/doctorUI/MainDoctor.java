package org.coursera.symptommanager.doctorUI;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import org.coursera.symptommanager.Constants;
import org.coursera.symptommanager.InternetStatus;
import org.coursera.symptommanager.LoginScreenActivity;
import org.coursera.symptommanager.R;
import org.coursera.symptommanager.objects.DoctorDetail;
import org.coursera.symptommanager.objects.Medication;
import org.coursera.symptommanager.objects.PatientDetail;

import retrofit.RetrofitError;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainDoctor extends Activity {

	// declare the views
	ListView list;
	EditText search;
	boolean visible = false;
	SwipeRefreshLayout mSwipeRefreshLayout;
	boolean swipe = false;
	// the list which will be inside the remove medication dialog
	ListView list_medication;
	RelativeLayout layout;
	// the progress bar inside the add medication dialog
	ProgressBar progressBar1;
	// the progress bar inside the remove medication dialog
	ProgressBar progressBar2;
	// the profile of the current doctor
	DoctorDetail profile;
	// the array list with the doctor's patients
	ArrayList<PatientDetail> patientsList;
	// the array list with the available patients
	ArrayList<PatientDetail> avaialblePatientsList;
	// the array list with the selected patient's medications
	ArrayList<Medication> medicationList;
	// the parameter id
	String ID = "id";
	// the parameter id doctor
	String IDD = "idd";
	// the dialogs that will pop up after the click on the menu in each patient
	AlertDialog addDialog;
	AlertDialog removeDialog;
	AlertDialog removePatientDialog;
	// the selected patient's id
	long SELECTED_PATIENT_ID;
	// the selected medication from the docotr
	Medication SELECTED_MEDICATION;
	// the selected patient from the docotr to add to his/hers patients
	PatientDetail SELECTED_PATIENT;
	// the string that will hold the result of a web service
	String RESPONSE = "";
	// the parameter for the username
	String USERNAME = "username";
	// the parameter for the password
	String PASSWORD = "password";
	// the parameter for the server
	String SERVER = "server";
	// the parameter for the role of the user
	String ROLE = "role";
	// the list adapter
	MyAdapter adapter;
	MyAdapterA adapterA;
	// the flag that will tell the sustem that the remove patient button is
	// selected
	boolean remove = false;
	String query;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_doctor);
		// instantiate the views
		list = (ListView) findViewById(R.id.patients_list);
		mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.container);
		mSwipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				// Start showing the refresh animation
				swipe = true;
				mSwipeRefreshLayout.setRefreshing(true);
				// Simulate a long running activity
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						// getAll patients
						if (!InternetStatus.getInstance(
								MainDoctor.this).isOnline(MainDoctor.this)) {
							Toast.makeText(MainDoctor.this, R.string.no_internet,
									Toast.LENGTH_LONG).show();

						} else {
						new getPatients().execute();
						}
					}
				}, 5000);
			}
		});
		mSwipeRefreshLayout.setColorScheme(android.R.color.holo_red_light,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_blue_light);
		search = (EditText) findViewById(R.id.search);
		search.setVisibility(View.GONE);
		search.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3) {
				// When user changed the Text
				query = cs.toString();
				if (query.equalsIgnoreCase("")) {
					if (!InternetStatus.getInstance(
							MainDoctor.this).isOnline(MainDoctor.this)) {
						Toast.makeText(MainDoctor.this, R.string.no_internet,
								Toast.LENGTH_LONG).show();

					} else {
					new getPatients().execute();
					}
				} else {
					filterPatients();
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {

			}

			@Override
			public void afterTextChanged(Editable arg0) {
			}
		});
		layout = (RelativeLayout) findViewById(R.id.layout);
		progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
		progressBar1.setVisibility(View.GONE);
		getActionBar().setTitle(getString(R.string.welcome_doctor));
		getActionBar().setSubtitle(getString(R.string.patients));
		

	}
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.clear();
		if (remove){
			android.view.MenuInflater inflater = getMenuInflater();

			inflater.inflate(R.menu.main_doctor_cancel, menu);	
		} else {
			android.view.MenuInflater inflater = getMenuInflater();

			inflater.inflate(R.menu.main_doctor, menu);	
		}
		
		return super.onPrepareOptionsMenu(menu);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		android.view.MenuInflater inflater = getMenuInflater();

		inflater.inflate(R.menu.main_doctor, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// get the id of the clicked item
		int itemId = item.getItemId();
		// get the id of the search button
		int searchBtn = R.id.search;
		// get the id of the add_patient button
		int addBtn = R.id.add;
		// get the id of the remove_patient button
		int removeBtn = R.id.remove;
		// get the id of the edit_profile button
		int editProfile = R.id.edit_profile;
		// get the id of the logout button
		int logout = R.id.logout;
		int cancel = R.id.cancel;
		if (itemId == addBtn) {
			// fetch all the patients that doesn't have an assigned doctor
			availablePatients();
			return true;
		} else if (itemId == removeBtn) {
			remove = true;
			adapter = new MyAdapter(MainDoctor.this, getResources()
					.getIdentifier("simple_list_item_1", "layout",
							getPackageName()), 0, patientsList);
			list.setAdapter(adapter);
			invalidateOptionsMenu();
			return true;
		} else if (itemId == searchBtn) {
			// search list a patient
			if (visible) {
				visible = false;
				search.setVisibility(View.GONE);
				adapter = new MyAdapter(MainDoctor.this, getResources()
						.getIdentifier("simple_list_item_1", "layout",
								getPackageName()), 0, patientsList);
				list.setAdapter(adapter);
			} else {
				visible = true;
				search.setVisibility(View.VISIBLE);
			}

			return true;
		} else if (itemId == editProfile) {
			// the application will move to the next screen using an
			// intent with extra long the id of the selected doctor in
			// order to open the doctor profile screen
			Intent intent = new Intent(MainDoctor.this, EditProfile.class);
			intent.putExtra(ID, profile.getId());
			startActivity(intent);

			return true;
		} else if (itemId == logout) {
			// erase shared preferences and clear static values and redirect
			// user to login screen
			Constants.USERNAME = "";
			Constants.PASSWORD = "";
			Constants.SERVER = "";
			Constants.ROLE = "";
			Constants.ID = -1;
			Editor edit = Constants.settings.edit();
			edit.putString(USERNAME, "");
			edit.putString(PASSWORD, "");
			edit.putString(SERVER, "");
			edit.putString(ROLE, "");
			edit.putLong(ID, -1);
			edit.commit();

			// clear back stack so the user can't navigate back
			Intent intent = new Intent(MainDoctor.this,
					LoginScreenActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
					| Intent.FLAG_ACTIVITY_CLEAR_TASK);
			startActivity(intent);
			Toast.makeText(MainDoctor.this, getString(R.string.logged_out),
					Toast.LENGTH_LONG).show();
			return true;
		} else if (itemId == cancel){
			remove = false;
			adapter = new MyAdapter(MainDoctor.this, getResources()
					.getIdentifier("simple_list_item_1", "layout",
							getPackageName()), 0, patientsList);
			list.setAdapter(adapter);
			invalidateOptionsMenu();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	/**
	 * Runs an async task fetching the profile of the doctor
	 */
	private void checkIfDoctorHasAProfile() {
		if (!InternetStatus.getInstance(
				MainDoctor.this).isOnline(MainDoctor.this)) {
			Toast.makeText(MainDoctor.this, R.string.no_internet,
					Toast.LENGTH_LONG).show();

		} else {
		new getProfile().execute();
		}
	}

	public class getProfile extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			try {
				profile = Constants.svc.getProfileDoctor(Constants.account
						.getUsername());
				if (profile.getRegId().equalsIgnoreCase("")) {
					Constants.reportIssue(MainDoctor.this,
							getString(R.string.please_edit_profile));
					profile = Constants.svc.addRegIdToDoctor(profile.getId(),
							Constants.account.getRegId());
				}

			} catch (final RetrofitError e) {
				Constants.reportIssue(MainDoctor.this, e.getMessage());
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			getActionBar().setTitle(
					getString(R.string.Dr) + " " + profile.getName() + " "
							+ profile.getSurname());

			if (profile.getRegId().equalsIgnoreCase("")) {
				Toast.makeText(MainDoctor.this,
						getString(R.string.please_edit_profile),
						Toast.LENGTH_LONG).show();
				openOptionsMenu();

			}
			if (!InternetStatus.getInstance(
					MainDoctor.this).isOnline(MainDoctor.this)) {
				Toast.makeText(MainDoctor.this, R.string.no_internet,
						Toast.LENGTH_LONG).show();

			} else {
			new getPatients().execute();
			}

		}

		@Override
		protected void onPreExecute() {
		}
	}

	public class getPatients extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			try {
				patientsList = Constants.svc.getPatientList(profile.getId());
			} catch (final RetrofitError e) {
				Constants.reportIssue(MainDoctor.this, e.getMessage());
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (swipe) {
				swipe = false;
				mSwipeRefreshLayout.setRefreshing(false);
			} else {
				layout.setClickable(true);
				progressBar1.setVisibility(View.GONE);
			}
			if (patientsList.size() == 0) {
				Toast.makeText(MainDoctor.this,
						getString(R.string.no_patients), Toast.LENGTH_LONG)
						.show();
				adapter = new MyAdapter(MainDoctor.this, getResources()
						.getIdentifier("simple_list_item_1", "layout",
								getPackageName()), 0, patientsList);
				list.setAdapter(adapter);
			} else {
				adapter = new MyAdapter(MainDoctor.this, getResources()
						.getIdentifier("simple_list_item_1", "layout",
								getPackageName()), 0, patientsList);
				list.setAdapter(adapter);
			}

		}

		@Override
		protected void onPreExecute() {
			if (swipe) {

			} else {
				layout.setClickable(false);
				progressBar1.setVisibility(View.VISIBLE);
				patientsList = new ArrayList<PatientDetail>();
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		// check if screen is on if it is then show welcome message
		if (Constants.USERNAME.equalsIgnoreCase("")) {

		} else {
			if (Constants.SCREEN_ON) {
				Constants.showWelcomeMsg(MainDoctor.this, layout);
			}

		}
		checkIfDoctorHasAProfile();
	}

	// holder class for the list view
	static class ViewHolder {

		public Button menu;
		public TextView name;
		public TextView lastCheckin;

	}

	ViewHolder holder;

	// this is the class of the adapter that shows the list of patients
	private class MyAdapter extends ArrayAdapter<PatientDetail> {

		public MyAdapter(Context context, int resource, int textViewResourceId,
				ArrayList<PatientDetail> list) {
			super(context, resource, textViewResourceId, list);
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View row = convertView;
			if (row == null || row.getTag() == null) {
				holder = new ViewHolder();
				// setup the item layout view
				LayoutInflater inflater = (LayoutInflater) MainDoctor.this
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				row = inflater.inflate(R.layout.patients_details_item, parent,
						false);
				holder.name = (TextView) row.findViewById(R.id.name);
				holder.lastCheckin = (TextView) row
						.findViewById(R.id.last_checkin);
				holder.menu = (Button) row.findViewById(R.id.menu);
				if (remove) {
					holder.menu
							.setBackgroundResource(R.drawable.ic_action_discard);
				} else {
					holder.menu
							.setBackgroundResource(R.drawable.ic_action_overflow);
				}
				// here we will set tag to each menu button the current position
				// in order to know which position is clicked
				holder.menu.setTag(position);
			} else {
				holder = (ViewHolder) row.getTag();
			}

			// set data to each view
			holder.name.setText(patientsList.get(position).getName() + " "
					+ patientsList.get(position).getSurname());
			long mills = patientsList.get(position).getLastCheckIn();
			// if the patient doesn't have answer any of theirs questions else
			// add the last check in of them
			if (mills == -1) {
				holder.lastCheckin.setText(getString(R.string.last_checkin)
						+ " -");
			} else {
				holder.lastCheckin.setText(getString(R.string.last_checkin)
						+ " " + convertDateToReadableDate(mills));
			}
			holder.menu.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// we get the position of the list view that the menu button
					// exist
					int currentPosition = (int) v.getTag();
					if (remove) {
						removePatient(patientsList.get(currentPosition).getId());
					} else {

						// will call this method to open up a menu
						popupMenu(patientsList.get(currentPosition), v);
					}
				}
			});
			list.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// the application will move to the next screen using an
					// intent with extra long the id of the selected patient in
					// order to open the patient screen
					Intent intent = new Intent(MainDoctor.this,
							SelectedPatient.class);
					intent.putExtra(ID, patientsList.get(position).getId());
					intent.putExtra(IDD, profile.getId());
					startActivity(intent);
				}
			});
			return row;
		}
	}

	// this is the class of the adapter that shows the list of patients
	private class MyAdapterA extends ArrayAdapter<PatientDetail> {

		public MyAdapterA(Context context, int resource,
				int textViewResourceId, ArrayList<PatientDetail> list) {
			super(context, resource, textViewResourceId, list);
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View row = convertView;
			if (row == null || row.getTag() == null) {
				holder = new ViewHolder();
				// setup the item layout view
				LayoutInflater inflater = (LayoutInflater) MainDoctor.this
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				row = inflater.inflate(R.layout.patients_details_item, parent,
						false);
				holder.name = (TextView) row.findViewById(R.id.name);
				holder.lastCheckin = (TextView) row
						.findViewById(R.id.last_checkin);
				holder.menu = (Button) row.findViewById(R.id.menu);
				if (remove) {
					holder.menu
							.setBackgroundResource(R.drawable.ic_action_discard);
				} else {
					holder.menu
							.setBackgroundResource(R.drawable.ic_action_overflow);
				}
				// here we will set tag to each menu button the current position
				// in order to know which position is clicked
				holder.menu.setTag(position);
			} else {
				holder = (ViewHolder) row.getTag();
			}

			// set data to each view
			holder.name.setText(finalSeachList.get(position).getName() + " "
					+ finalSeachList.get(position).getSurname());
			long mills = finalSeachList.get(position).getLastCheckIn();
			// if the patient doesn't have answer any of theirs questions else
			// add the last check in of them
			if (mills == -1) {
				holder.lastCheckin.setText(getString(R.string.last_checkin)
						+ " -");
			} else {
				holder.lastCheckin.setText(getString(R.string.last_checkin)
						+ " " + convertDateToReadableDate(mills));
			}
			holder.menu.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// we get the position of the list view that the menu button
					// exist
					int currentPosition = (int) v.getTag();
					if (remove) {
						removePatient(finalSeachList.get(currentPosition)
								.getId());
					} else {

						// will call this method to open up a menu
						popupMenu(finalSeachList.get(currentPosition), v);
					}
				}
			});
			list.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// the application will move to the next screen using an
					// intent with extra long the id of the selected patient in
					// order to open the patient screen
					Intent intent = new Intent(MainDoctor.this,
							SelectedPatient.class);
					intent.putExtra(ID, finalSeachList.get(position).getId());
					intent.putExtra(IDD, profile.getId());
					startActivity(intent);
				}
			});
			return row;
		}
	}

	/**
	 * 
	 * Converts the provided milliseconds to a readable date in the form of Mon
	 * 4 Jan 2014 at 18:45
	 * 
	 * @param mills
	 * @return
	 */
	private String convertDateToReadableDate(long mills) {

		// get the locale in order to avoid lint check in the SimpleDateFormat
		Locale current = getResources().getConfiguration().locale;
		// instantiate the string for the result of the date
		String result = "";
		// Create a DateFormatter object for displaying date in specified
		// format.
		SimpleDateFormat dateFormater = new SimpleDateFormat("EEE dd MMM yyyy",
				current);
		SimpleDateFormat timeFormater = new SimpleDateFormat("HH:mm", current);

		// Create a calendar object that will convert the date and time value in
		// milliseconds to date.
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(mills);
		result = dateFormater.format(calendar.getTime()) + " at "
				+ timeFormater.format(calendar.getTime());
		return result;
	}

	/**
	 * Opens a menu next to the clicked button from the list with the patients
	 * that why we pass as parameters not only the patient object but also the
	 * view
	 * 
	 * @param details
	 * @param v
	 */
	private void popupMenu(final PatientDetail details, View v) {
		PopupMenu popup = new PopupMenu(this, v);
		MenuInflater inflater = popup.getMenuInflater();
		popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				// get the id of the clicked item
				int itemId = item.getItemId();
				// get the id of the add medication button
				int add_medication = R.id.add;
				// get the id of the remove medication
				int remove_medication = R.id.remove;
				if (itemId == add_medication) {
					// put the selected patient's id in the long variable
					SELECTED_PATIENT_ID = details.getId();
					// will open a dialog to prompt doctor to add a medication
					// name and description and add it to the patient
					addMedication();
					return true;
				} else if (itemId == remove_medication) {
					// put the selected patient's id in the long variable
					SELECTED_PATIENT_ID = details.getId();
					// will open a dialog with all the medications of the
					// patient so the doctor can select which to remove
					removeMedication();
					return true;
				} else {
					return false;
				}
			}
		});

		inflater.inflate(R.menu.patient_popup, popup.getMenu());

		popup.show();
	}

	/**
	 * Opens a dialog and prompt doctor to add a name and a description of the
	 * medication he/she want to give to the selected patient
	 * 
	 */
	private void addMedication() {
		AlertDialog.Builder builder = new AlertDialog.Builder(MainDoctor.this);
		LayoutInflater inflater = getLayoutInflater();
		View view = inflater.inflate(R.layout.add_medication_dialog, null);
		builder.setView(view);
		builder.setTitle(getString(R.string.add_medication));
		// instantiate the views
		final EditText title = (EditText) view.findViewById(R.id.title);
		final EditText description = (EditText) view
				.findViewById(R.id.description);
		progressBar1 = (ProgressBar) view.findViewById(R.id.progressBar1);
		progressBar1.setVisibility(View.GONE);

		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				if (title.getText().toString().equalsIgnoreCase("")
						|| description.getText().toString()
								.equalsIgnoreCase("")) {
					Toast.makeText(MainDoctor.this,
							getString(R.string.empty_mediaction),
							Toast.LENGTH_LONG).show();
				} else {
					SELECTED_MEDICATION = new Medication(-1, title.getText()
							.toString(), description.getText().toString(),
							SELECTED_PATIENT_ID);
					if (!InternetStatus.getInstance(
							MainDoctor.this).isOnline(MainDoctor.this)) {
						Toast.makeText(MainDoctor.this, R.string.no_internet,
								Toast.LENGTH_LONG).show();

					} else {
					new addMediaction().execute();
					}
				}
				addDialog.dismiss();
			}
		});

		builder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
					}
				});
		addDialog = builder.create();
		addDialog.show();
	}

	/**
	 * Opens a dialog with the assigned medications of the patient and lets the
	 * doctor to remove a mediaction
	 * 
	 */
	private void removeMedication() {
		AlertDialog.Builder builder = new AlertDialog.Builder(MainDoctor.this);
		LayoutInflater inflater = getLayoutInflater();
		View view = inflater.inflate(R.layout.remove_medication_dialog, null);
		builder.setView(view);
		builder.setTitle(getString(R.string.assigned_medication));
		// instantiate the views
		list_medication = (ListView) view.findViewById(R.id.madication_list);

		progressBar2 = (ProgressBar) view.findViewById(R.id.progressBar2);
		progressBar2.setVisibility(View.GONE);

		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				removeDialog.dismiss();
			}
		});
		removeDialog = builder.create();
		removeDialog.show();
		getAvailableMedication();
	}

	private void removePatient(final long patientID) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setMessage(R.string.delete_patient)
				.setTitle(R.string.attention);
		builder.setPositiveButton(R.string.yes,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						SELECTED_PATIENT_ID = patientID;
						if (!InternetStatus.getInstance(
								MainDoctor.this).isOnline(MainDoctor.this)) {
							Toast.makeText(MainDoctor.this, R.string.no_internet,
									Toast.LENGTH_LONG).show();

						} else {
						new removePatient().execute();
						}
					}
				});
		builder.setNegativeButton(R.string.no,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						invalidateOptionsMenu();
						adapter = new MyAdapter(MainDoctor.this, getResources()
								.getIdentifier("simple_list_item_1", "layout",
										getPackageName()), 0, patientsList);
						list.setAdapter(adapter);
					}
				});
		removePatientDialog = builder.create();
		removePatientDialog.show();
	}

	/**
	 * It will execute the add medication web service to add the selected
	 * medication to the patient
	 * 
	 */
	public class addMediaction extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			try {
				Constants.svc.addMedication(SELECTED_PATIENT_ID,
						SELECTED_MEDICATION.getName(),
						SELECTED_MEDICATION.getDesciption(),
						SELECTED_MEDICATION.getIdPatient());
			} catch (final RetrofitError e) {
				Constants.reportIssue(MainDoctor.this, e.getMessage());
				RESPONSE = e.getMessage();
			}
			if (RESPONSE.equalsIgnoreCase("")) {
				Constants.reportIssue(MainDoctor.this,
						getString(R.string.add_medication_ok));
				Constants.svc.sendPushPatient(SELECTED_PATIENT_ID,
						"New medication added", "Medication "
								+ SELECTED_MEDICATION.getName()
								+ " just added by your doctor");
			} else {
				RESPONSE = "";
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			progressBar1.setVisibility(View.GONE);
			layout.setClickable(true);
		}

		@Override
		protected void onPreExecute() {
			progressBar1.setVisibility(View.VISIBLE);
			layout.setClickable(false);
		}
	}

	/**
	 * Loads the available medication of the selected patient
	 */
	private void getAvailableMedication() {
		if (!InternetStatus.getInstance(
				MainDoctor.this).isOnline(MainDoctor.this)) {
			Toast.makeText(MainDoctor.this, R.string.no_internet,
					Toast.LENGTH_LONG).show();

		} else {
		new getAvailableMeds().execute();
		}
	}

	/**
	 * It will execute the get medication web service to fetch the medication of
	 * the selected the patient
	 * 
	 */
	public class getAvailableMeds extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {

			try {
				medicationList = Constants.svc
						.getMedication(SELECTED_PATIENT_ID);
			} catch (final RetrofitError e) {
				Constants.reportIssue(MainDoctor.this, e.getMessage());
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			progressBar2.setVisibility(View.GONE);
			layout.setClickable(true);
			if (medicationList == null) {
				Toast.makeText(MainDoctor.this,
						getString(R.string.no_medication), Toast.LENGTH_LONG)
						.show();
			} else {
				list_medication
						.setAdapter(new MyAdapter2(MainDoctor.this,
								getResources().getIdentifier(
										"simple_list_item_1", "layout",
										getPackageName()), 0, medicationList));
			}
		}

		@Override
		protected void onPreExecute() {
			progressBar2.setVisibility(View.VISIBLE);
			layout.setClickable(false);
		}
	}

	// holder class for the list view
	static class ViewHolder2 {

		public TextView name;

	}

	ViewHolder2 holder2;

	// this is the class of the adapter that shows the list of medication
	private class MyAdapter2 extends ArrayAdapter<Medication> {

		public MyAdapter2(Context context, int resource,
				int textViewResourceId, ArrayList<Medication> list) {
			super(context, resource, textViewResourceId, list);
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View row = convertView;
			if (row == null || row.getTag() == null) {
				holder2 = new ViewHolder2();
				// setup the item layout view
				LayoutInflater inflater = (LayoutInflater) MainDoctor.this
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				row = inflater.inflate(R.layout.medication_item, parent, false);
				holder2.name = (TextView) row
						.findViewById(R.id.title_medication);

			} else {
				holder2 = (ViewHolder2) row.getTag();
			}

			// set data to each view
			holder2.name.setText(medicationList.get(position).getName());
			list_medication.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// a dialog to make sure that the doctor wants to delete
					// this medication
					askForDeletion(position);

				}
			});
			return row;
		}
	}

	/**
	 * Open a dialog and ask the doctor if he/she is sure to delete the
	 * medication if yes then delete the medication and update the list of
	 * medication
	 */
	private void askForDeletion(final int position) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setMessage(R.string.delete_medication).setTitle(
				R.string.attention);
		builder.setPositiveButton(R.string.yes,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						SELECTED_MEDICATION = medicationList.get(position);
						if (!InternetStatus.getInstance(
								MainDoctor.this).isOnline(MainDoctor.this)) {
							Toast.makeText(MainDoctor.this, R.string.no_internet,
									Toast.LENGTH_LONG).show();

						} else {
						new removeMedication().execute();
						}
					}
				});
		builder.setNegativeButton(R.string.no,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
					}
				});
		builder.create().show();
	}

	/**
	 * It will execute the remove medication web service to remove the selected
	 * medication
	 * 
	 */
	public class removeMedication extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			try {
				Constants.svc.removeMedication(SELECTED_MEDICATION.getId());
			} catch (final RetrofitError e) {
				Constants.reportIssue(MainDoctor.this, e.getMessage());
				RESPONSE = e.getMessage();
			}
			if (RESPONSE.equalsIgnoreCase("")) {
				removeDialog.dismiss();
				Constants.reportIssue(MainDoctor.this,
						getString(R.string.remove_medication_ok));

			} else {
				RESPONSE = "";
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			progressBar2.setVisibility(View.GONE);
			layout.setClickable(true);
		}

		@Override
		protected void onPreExecute() {
			progressBar2.setVisibility(View.VISIBLE);
			layout.setClickable(false);
		}
	}

	/**
	 * Fetches all the available patients, the ones with no assigned doctor
	 */
	private void availablePatients() {
		// here we will use the same dialog as the remove medication method
		// because the UI i the same the items inside the list view will change
		AlertDialog.Builder builder = new AlertDialog.Builder(MainDoctor.this);
		LayoutInflater inflater = getLayoutInflater();
		View view = inflater.inflate(R.layout.remove_medication_dialog, null);
		builder.setView(view);
		builder.setTitle(getString(R.string.available_title));
		// instantiate the views
		list_medication = (ListView) view.findViewById(R.id.madication_list);

		progressBar2 = (ProgressBar) view.findViewById(R.id.progressBar2);

		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				removeDialog.dismiss();
			}
		});
		removeDialog = builder.create();
		removeDialog.show();
		if (!InternetStatus.getInstance(
				MainDoctor.this).isOnline(MainDoctor.this)) {
			Toast.makeText(MainDoctor.this, R.string.no_internet,
					Toast.LENGTH_LONG).show();

		} else {
		new availablePatients().execute();
		}
	}

	public class availablePatients extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			try {
				avaialblePatientsList = Constants.svc.getAvailablePatients();
			} catch (final RetrofitError e) {
				Constants.reportIssue(MainDoctor.this, e.getMessage());
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			progressBar2.setVisibility(View.GONE);
			layout.setClickable(true);
			if (avaialblePatientsList.size() == 0) {
				Toast.makeText(MainDoctor.this,
						getString(R.string.no_available_patients),
						Toast.LENGTH_LONG).show();
			} else {
				list_medication.setAdapter(new MyAdapter3(MainDoctor.this,
						getResources().getIdentifier("simple_list_item_1",
								"layout", getPackageName()), 0,
						avaialblePatientsList));
			}
		}

		@Override
		protected void onPreExecute() {
			progressBar2.setVisibility(View.VISIBLE);
			layout.setClickable(false);
			avaialblePatientsList = new ArrayList<PatientDetail>();
		}
	}

	// holder class for the list view
	static class ViewHolder3 {

		public TextView name;
		public TextView age;
		public TextView gender;

	}

	ViewHolder3 holder3;

	// this is the class of the adapter that shows the list of medication
	private class MyAdapter3 extends ArrayAdapter<PatientDetail> {

		public MyAdapter3(Context context, int resource,
				int textViewResourceId, ArrayList<PatientDetail> list) {
			super(context, resource, textViewResourceId, list);
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View row = convertView;
			if (row == null || row.getTag() == null) {
				holder3 = new ViewHolder3();
				// setup the item layout view
				LayoutInflater inflater = (LayoutInflater) MainDoctor.this
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				row = inflater.inflate(R.layout.patients_item, parent, false);
				holder3.name = (TextView) row.findViewById(R.id.name);
				holder3.age = (TextView) row.findViewById(R.id.age);
				holder3.gender = (TextView) row.findViewById(R.id.gender);

			} else {
				holder3 = (ViewHolder3) row.getTag();
			}

			// set data to each view
			holder3.name.setText(avaialblePatientsList.get(position).getName()
					+ " " + avaialblePatientsList.get(position).getSurname());
			long dob = avaialblePatientsList.get(position).getDob();
			if (dob == 0) {

			} else {
				holder3.age.setText(getAge(dob));
			}
			holder3.gender.setText(avaialblePatientsList.get(position)
					.getGender());
			list_medication.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// remove the dialog
					removeDialog.dismiss();
					// a dialog to make sure that the doctor wants to add
					// this patient as his
					askForAdding(position);

				}
			});
			return row;
		}
	}

	/**
	 * Open a dialog and ask the doctor if he/she is sure to delete the
	 * medication if yes then delete the medication and update the list of
	 * medication
	 */
	private void askForAdding(final int position) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setMessage(R.string.adding_patient)
				.setTitle(R.string.attention);
		builder.setPositiveButton(R.string.yes,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						SELECTED_PATIENT = avaialblePatientsList.get(position);
						if (!InternetStatus.getInstance(
								MainDoctor.this).isOnline(MainDoctor.this)) {
							Toast.makeText(MainDoctor.this, R.string.no_internet,
									Toast.LENGTH_LONG).show();

						} else {
						new addPatient().execute();
						}
					}
				});
		builder.setNegativeButton(R.string.no,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
					}
				});
		builder.create().show();
	}

	public class addPatient extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			try {
				Constants.svc.addPatient(profile.getId(),
						SELECTED_PATIENT.getId());
			} catch (final RetrofitError e) {
				Constants.reportIssue(MainDoctor.this, e.getMessage());
			}
			// avaialblePatientsList = Constants.svc.getAvailablePatients();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			progressBar2.setVisibility(View.GONE);
			layout.setClickable(true);
			if (!InternetStatus.getInstance(
					MainDoctor.this).isOnline(MainDoctor.this)) {
				Toast.makeText(MainDoctor.this, R.string.no_internet,
						Toast.LENGTH_LONG).show();

			} else {
			new getPatients().execute();
			}
			// if (avaialblePatientsList.size() == 0) {
			// Toast.makeText(MainDoctor.this,
			// getString(R.string.no_available_patients), Toast.LENGTH_LONG)
			// .show();
			// } else {
			// list_medication.setAdapter(new MyAdapter3(MainDoctor.this,
			// getResources().getIdentifier("simple_list_item_1",
			// "layout", getPackageName()), 0,
			// avaialblePatientsList));
			// }
		}

		@Override
		protected void onPreExecute() {
			progressBar2.setVisibility(View.VISIBLE);
			layout.setClickable(false);
			// avaialblePatientsList = new ArrayList<PatientDetail>();
		}
	}

	public class removePatient extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			try {
				Constants.svc.removePatient(SELECTED_PATIENT_ID);
			} catch (final RetrofitError e) {
				Constants.reportIssue(MainDoctor.this, e.getMessage());
				RESPONSE = e.getMessage();
			}
			if (RESPONSE.equalsIgnoreCase("")) {
				removePatientDialog.dismiss();
				Constants.reportIssue(MainDoctor.this,
						getString(R.string.remove_patient_ok));

			} else {
				RESPONSE = "";
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			remove = false;
			invalidateOptionsMenu();
			progressBar1.setVisibility(View.GONE);
			layout.setClickable(true);
			if (!InternetStatus.getInstance(
					MainDoctor.this).isOnline(MainDoctor.this)) {
				Toast.makeText(MainDoctor.this, R.string.no_internet,
						Toast.LENGTH_LONG).show();

			} else {
			new getPatients().execute();
			}
		}

		@Override
		protected void onPreExecute() {
			progressBar1.setVisibility(View.VISIBLE);
			layout.setClickable(false);
			// avaialblePatientsList = new ArrayList<PatientDetail>();
		}
	}

	public class getAssignedMediaction extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			try {
				Constants.svc.addPatient(profile.getId(),
						SELECTED_PATIENT.getId());
			} catch (final RetrofitError e) {
				Constants.reportIssue(MainDoctor.this, e.getMessage());
			}
			// avaialblePatientsList = Constants.svc.getAvailablePatients();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			progressBar2.setVisibility(View.GONE);
			layout.setClickable(true);
			if (!InternetStatus.getInstance(
					MainDoctor.this).isOnline(MainDoctor.this)) {
				Toast.makeText(MainDoctor.this, R.string.no_internet,
						Toast.LENGTH_LONG).show();

			} else {
			new getPatients().execute();
			}
			// if (avaialblePatientsList.size() == 0) {
			// Toast.makeText(MainDoctor.this,
			// getString(R.string.no_available_patients), Toast.LENGTH_LONG)
			// .show();
			// } else {
			// list_medication.setAdapter(new MyAdapter3(MainDoctor.this,
			// getResources().getIdentifier("simple_list_item_1",
			// "layout", getPackageName()), 0,
			// avaialblePatientsList));
			// }
		}

		@Override
		protected void onPreExecute() {
			progressBar2.setVisibility(View.VISIBLE);
			layout.setClickable(false);
			avaialblePatientsList = new ArrayList<PatientDetail>();
		}
	}

	private String getAge(long dob) {

		Calendar cal = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal2.setTimeInMillis(dob);
		return String.valueOf(cal.get(Calendar.YEAR) - cal2.get(Calendar.YEAR));

	}

	private void filterPatients() {
		if (!InternetStatus.getInstance(
				MainDoctor.this).isOnline(MainDoctor.this)) {
			Toast.makeText(MainDoctor.this, R.string.no_internet,
					Toast.LENGTH_LONG).show();

		} else {
		new searchPatient().execute();
		}

	}

	ArrayList<PatientDetail> patientsSeachListName;

	ArrayList<PatientDetail> patientsSeachListSurname;
	ArrayList<PatientDetail> finalSeachList;
	boolean end = false;

	private void createFinalList() {
		for (int i = 0; i < patientsSeachListName.size(); i++) {
			if (patientsSeachListName.get(i).getIdDoctor() == profile.getId()) {
				finalSeachList.add(patientsSeachListName.get(i));
			}
		}
		for (int j = 0; j < patientsSeachListSurname.size(); j++) {
			if (patientsSeachListSurname.get(j).getIdDoctor() == profile
					.getId()) {

				for (int o = 0; o < finalSeachList.size(); o++) {
					if (finalSeachList.get(o).getId() == patientsSeachListSurname
							.get(j).getId()) {
						end = true;
					} else {

					}
				}
				if (end) {
					end = false;
				} else {
					finalSeachList.add(patientsSeachListSurname.get(j));

				}
				if (finalSeachList.size() == 0) {
					finalSeachList.add(patientsSeachListSurname.get(j));
				}
			}
		}
	}

	public class searchPatient extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			try {
				patientsSeachListName = Constants.svc
						.findByNameContaining(query);
				patientsSeachListSurname = Constants.svc
						.findBySurnameContaining(query);
				createFinalList();
			} catch (final RetrofitError e) {
				Constants.reportIssue(MainDoctor.this, e.getMessage());
			}
			// avaialblePatientsList = Constants.svc.getAvailablePatients();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			progressBar1.setVisibility(View.GONE);
			layout.setClickable(true);
			if (finalSeachList.size() == 0) {
				adapterA = new MyAdapterA(MainDoctor.this, getResources()
						.getIdentifier("simple_list_item_1", "layout",
								getPackageName()), 0, finalSeachList);
				list.setAdapter(adapterA);
			} else {
				adapterA = new MyAdapterA(MainDoctor.this, getResources()
						.getIdentifier("simple_list_item_1", "layout",
								getPackageName()), 0, finalSeachList);
				list.setAdapter(adapterA);
			}
		}

		@Override
		protected void onPreExecute() {
			progressBar1.setVisibility(View.VISIBLE);
			layout.setClickable(false);
			patientsSeachListName = new ArrayList<PatientDetail>();
			patientsSeachListSurname = new ArrayList<PatientDetail>();
			finalSeachList = new ArrayList<PatientDetail>();
		}
	}

	
}
