package org.coursera.symptommanager.doctorUI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.coursera.symptommanager.Constants;
import org.coursera.symptommanager.InternetStatus;
import org.coursera.symptommanager.R;
import org.coursera.symptommanager.flag_numbers.Country;
import org.coursera.symptommanager.flag_numbers.CountryAdapter;
import org.coursera.symptommanager.flag_numbers.CustomPhoneNumberFormattingTextWatcher;
import org.coursera.symptommanager.flag_numbers.OnPhoneChangedListener;
import org.coursera.symptommanager.flag_numbers.PhoneUtils;
import org.coursera.symptommanager.objects.DoctorDetail;

import retrofit.RetrofitError;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

public class EditProfile extends Activity {

	// the long that will hold the doctor id which is send with the intent
	long doctorID;
	// the parameter id
	String ID = "id";
	// the doctor profile object
	DoctorDetail profile;
	EditText phone;
	// declare the views
	EditText name;
	EditText surname;
	EditText email;
	EditText mPhoneEdit;
	Spinner mSpinner;
	ProgressBar loading;
	RelativeLayout layout;
	// the string with the error from the retrofit exception
	String ERROR = "";
	String mLastEnteredPhone;
	SparseArray<ArrayList<Country>> mCountriesMap = new SparseArray<ArrayList<Country>>();
	PhoneNumberUtil mPhoneNumberUtil = PhoneNumberUtil.getInstance();
	CountryAdapter mAdapter;
	// for e mail validation
	Pattern patternObj;
	Matcher matcherObj;
	String regExpn = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	static final TreeSet<String> CANADA_CODES = new TreeSet<String>();

	static {
		CANADA_CODES.add("204");
		CANADA_CODES.add("236");
		CANADA_CODES.add("249");
		CANADA_CODES.add("250");
		CANADA_CODES.add("289");
		CANADA_CODES.add("306");
		CANADA_CODES.add("343");
		CANADA_CODES.add("365");
		CANADA_CODES.add("387");
		CANADA_CODES.add("403");
		CANADA_CODES.add("416");
		CANADA_CODES.add("418");
		CANADA_CODES.add("431");
		CANADA_CODES.add("437");
		CANADA_CODES.add("438");
		CANADA_CODES.add("450");
		CANADA_CODES.add("506");
		CANADA_CODES.add("514");
		CANADA_CODES.add("519");
		CANADA_CODES.add("548");
		CANADA_CODES.add("579");
		CANADA_CODES.add("581");
		CANADA_CODES.add("587");
		CANADA_CODES.add("604");
		CANADA_CODES.add("613");
		CANADA_CODES.add("639");
		CANADA_CODES.add("647");
		CANADA_CODES.add("672");
		CANADA_CODES.add("705");
		CANADA_CODES.add("709");
		CANADA_CODES.add("742");
		CANADA_CODES.add("778");
		CANADA_CODES.add("780");
		CANADA_CODES.add("782");
		CANADA_CODES.add("807");
		CANADA_CODES.add("819");
		CANADA_CODES.add("825");
		CANADA_CODES.add("867");
		CANADA_CODES.add("873");
		CANADA_CODES.add("902");
		CANADA_CODES.add("905");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// if the delivered intent has the extra id get it and save it as
		// doctorID
		if (getIntent().hasExtra(ID)) {
			doctorID = getIntent().getLongExtra(ID, -1);

		}
		setContentView(R.layout.edit_profile_doctor);
		// set the name on the action bar
		getActionBar().setTitle(getString(R.string.edit_profile));
		// instantiate the views
		layout = (RelativeLayout) findViewById(R.id.layout);
		phone = (EditText) findViewById(R.id.phone);
		phone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				createPhoneDialog();

			}
		});
		name = (EditText) findViewById(R.id.name);
		surname = (EditText) findViewById(R.id.surname);
		email = (EditText) findViewById(R.id.email);
		loading = (ProgressBar) findViewById(R.id.progressBar1);
		loading.setVisibility(View.GONE);

		getDoctorProfile();

	}

	private void getDoctorProfile() {
		if (!InternetStatus.getInstance(
				EditProfile.this).isOnline(EditProfile.this)) {
			Toast.makeText(EditProfile.this, R.string.no_internet,
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
			} catch (final RetrofitError e) {
				Constants.reportIssue(EditProfile.this, e.getMessage());
				ERROR = e.getMessage();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			loading.setVisibility(View.GONE);
			layout.setClickable(true);
			if (ERROR.equalsIgnoreCase("")) {
				name.setText(profile.getName());
				surname.setText(profile.getSurname());
				email.setText(profile.getEmail());
				phone.setText(profile.getPhone());
				mLastEnteredPhone = profile.getPhone();
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		android.view.MenuInflater inflater = getMenuInflater();

		inflater.inflate(R.menu.edit_profile, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// get the id of the clicked item
		int itemId = item.getItemId();
		// get the id of the send button
		int send = R.id.send;
		if (itemId == send) {
			// check if the fields are correct
			if (checkForEmptyFields()) {
				if (isValidEmail(email.getText().toString())) {
					// fetch all the patients that doesn't have an assigned doctor
					if (!InternetStatus.getInstance(
							EditProfile.this).isOnline(EditProfile.this)) {
						Toast.makeText(EditProfile.this, R.string.no_internet,
								Toast.LENGTH_LONG).show();

					} else {
					new updateProfile().execute();
					}
				} else {
					Toast.makeText(EditProfile.this, R.string.email_or_phone_error,
							Toast.LENGTH_LONG).show();
				}
			} else {

			}

			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	public class updateProfile extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			try {
				profile = Constants.svc.updateProfileDoctor(new DoctorDetail(
						profile.getUsername(), name.getText().toString(),
						surname.getText().toString(), email.getText()
								.toString(), phone.getText().toString(),
						profile.getRegId(), profile.getId()));
			} catch (final RetrofitError e) {
				Constants.reportIssue(EditProfile.this, e.getMessage());
				ERROR = e.getMessage();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			loading.setVisibility(View.GONE);
			layout.setClickable(true);
			if (!ERROR.equalsIgnoreCase("")) {
				ERROR = "";

			} else {
				Toast.makeText(EditProfile.this, R.string.ok_update_profile,
						Toast.LENGTH_LONG).show();
				finish();
			}
		}

		@Override
		protected void onPreExecute() {
			loading.setVisibility(View.VISIBLE);
			layout.setClickable(false);
		}
	}

	private boolean checkForEmptyFields() {
		if (email.getText().toString().equalsIgnoreCase("")
				|| phone.getText().toString().equalsIgnoreCase("-")
				|| phone.getText().toString().equalsIgnoreCase("")
				|| name.getText().toString().equalsIgnoreCase("")
				|| surname.getText().toString().equalsIgnoreCase("")) {
			Toast.makeText(EditProfile.this, R.string.please_add_contact,
					Toast.LENGTH_LONG).show();
			return false;
		} else {
			return true;
		}
	}

	private String checkFields() {
		mPhoneEdit.setError(null);
		String phone = validate();
		if (phone == null) {
			mPhoneEdit.requestFocus();
			mPhoneEdit
					.setError(getString(R.string.label_error_incorrect_phone));
		} else {
			
		}
		return phone;
	}

	private Boolean isValidEmail(String inputEmail) {
		patternObj = Pattern.compile(regExpn);

		matcherObj = patternObj.matcher(inputEmail);
		if (matcherObj.matches()) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		// check if screen is on if it is then show welcome message
		if (Constants.USERNAME.equalsIgnoreCase("")) {

		} else {
			if (Constants.SCREEN_ON) {
				Constants.showWelcomeMsg(EditProfile.this, layout);
			}

		}
	}



	protected OnPhoneChangedListener mOnPhoneChangedListener = new OnPhoneChangedListener() {
		@Override
		public void onPhoneChanged(String phone) {
			try {
				mLastEnteredPhone = phone;
				Phonenumber.PhoneNumber p = mPhoneNumberUtil.parse(phone, null);
				ArrayList<Country> list = mCountriesMap.get(p.getCountryCode());
				Country country = null;
				if (list != null) {
					if (p.getCountryCode() == 1) {
						String num = String.valueOf(p.getNationalNumber());
						if (num.length() >= 3) {
							String code = num.substring(0, 3);
							if (CANADA_CODES.contains(code)) {
								for (Country c : list) {
									// Canada has priority 1, US has priority 0
									if (c.getPriority() == 1) {
										country = c;
										break;
									}
								}
							}
						}
					}
					if (country == null) {
						for (Country c : list) {
							if (c.getPriority() == 0) {
								country = c;
								break;
							}
						}
					}
				}
				if (country != null) {
					final int position = country.getNum();
					mSpinner.post(new Runnable() {
						@Override
						public void run() {
							mSpinner.setSelection(position);
						}
					});
				}
			} catch (NumberParseException ignore) {
			}

		}
	};

	protected void initCodes(Context context) {
		new AsyncPhoneInitTask(context).execute();
	}

	protected class AsyncPhoneInitTask extends
			AsyncTask<Void, Void, ArrayList<Country>> {

		private int mSpinnerPosition = -1;
		private Context mContext;

		public AsyncPhoneInitTask(Context context) {
			mContext = context;
		}

		@Override
		protected ArrayList<Country> doInBackground(Void... params) {
			ArrayList<Country> data = new ArrayList<Country>(233);
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new InputStreamReader(mContext
						.getApplicationContext().getAssets()
						.open("countries.dat"), "UTF-8"));

				// do reading, usually loop until end of file reading
				String line;
				int i = 0;
				while ((line = reader.readLine()) != null) {
					// process line
					Country c = new Country(mContext, line, i);
					data.add(c);
					ArrayList<Country> list = mCountriesMap.get(c
							.getCountryCode());
					if (list == null) {
						list = new ArrayList<Country>();
						mCountriesMap.put(c.getCountryCode(), list);
					}
					list.add(c);
					i++;
				}
			} catch (IOException e) {
				// log the exception
			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e) {
						// log the exception
					}
				}
			}
			if (!TextUtils.isEmpty(mPhoneEdit.getText())) {
				return data;
			}
			String countryRegion = PhoneUtils
					.getCountryRegionFromPhone(mContext);
			int code = mPhoneNumberUtil.getCountryCodeForRegion(countryRegion);
			ArrayList<Country> list = mCountriesMap.get(code);
			if (list != null) {
				for (Country c : list) {
					if (c.getPriority() == 0) {
						mSpinnerPosition = c.getNum();
						break;
					}
				}
			}
			return data;
		}

		@Override
		protected void onPostExecute(ArrayList<Country> data) {
			mAdapter.addAll(data);
			if (mSpinnerPosition > 0) {
				mSpinner.setSelection(mSpinnerPosition);
			}

		}
	}

	// protected abstract void send();

	protected String validate() {
		String region = null;
		String phone = null;
		if (mLastEnteredPhone != null) {
			try {
				Phonenumber.PhoneNumber p = mPhoneNumberUtil.parse(
						mLastEnteredPhone, null);
				StringBuilder sb = new StringBuilder(16);
				sb.append('+').append(p.getCountryCode())
						.append(p.getNationalNumber());
				phone = sb.toString();
				region = mPhoneNumberUtil.getRegionCodeForNumber(p);
			} catch (NumberParseException ignore) {
			}
		}
		if (region != null) {
			return phone;
		} else {
			return null;
		}
	}

	protected void hideKeyboard(View v) {
		InputMethodManager imm = (InputMethodManager) v.getContext()
				.getApplicationContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
	}

	protected void showKeyboard(View v) {
		InputMethodManager imm = (InputMethodManager) v.getContext()
				.getApplicationContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
	}

	AlertDialog phoneDialog;

	private void createPhoneDialog() {
		initCodes(EditProfile.this);
		AlertDialog.Builder builder = new AlertDialog.Builder(EditProfile.this);
		LayoutInflater inflater = getLayoutInflater();
		View view = inflater.inflate(R.layout.phone_dialog, null);
		builder.setView(view);
		builder.setTitle(getString(R.string.add_phone));
		// instantiate the views
		mSpinner = (Spinner) view.findViewById(R.id.spinner);
		mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				Country c = (Country) mSpinner.getItemAtPosition(position);
				if (mLastEnteredPhone != null
						&& mLastEnteredPhone.startsWith(c.getCountryCodeStr())) {
					return;
				}
				mPhoneEdit.getText().clear();
				mPhoneEdit.getText().insert(
						mPhoneEdit.getText().length() > 0 ? 1 : 0,
						String.valueOf(c.getCountryCode()));
				mPhoneEdit.setSelection(mPhoneEdit.length());
				mLastEnteredPhone = null;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		mAdapter = new CountryAdapter(this);

		mSpinner.setAdapter(mAdapter);

		mPhoneEdit = (EditText) view.findViewById(R.id.phone);
		mPhoneEdit
				.addTextChangedListener(new CustomPhoneNumberFormattingTextWatcher(
						mOnPhoneChangedListener));
		InputFilter filter = new InputFilter() {
			public CharSequence filter(CharSequence source, int start, int end,
					Spanned dest, int dstart, int dend) {
				for (int i = start; i < end; i++) {
					char c = source.charAt(i);
					if (dstart > 0 && !Character.isDigit(c)) {
						return "";
					}
				}
				return null;
			}
		};

		mPhoneEdit.setFilters(new InputFilter[] { filter });
		mPhoneEdit.setImeOptions(EditorInfo.IME_ACTION_SEND);
		mPhoneEdit.setImeActionLabel(getString(R.string.send_email),
				EditorInfo.IME_ACTION_SEND);
		mPhoneEdit
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event) {
						if (actionId == EditorInfo.IME_ACTION_SEND) {
							Toast.makeText(EditProfile.this, "SEND SEND",
									Toast.LENGTH_LONG).show();
							return true;
						}
						return false;
					}
				});

		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				if (checkFields() == null){
				Toast.makeText(EditProfile.this, getString(R.string.no_phone), Toast.LENGTH_SHORT).show();	
				phone.setText("-");
				} else {
				phone.setText(mPhoneEdit.getText().toString());
				phoneDialog.dismiss();
				}
			}
		});

		builder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
					
					}
				});
		phoneDialog = builder.create();
		phoneDialog.show();
	}
}
