package org.coursera.symptommanager.patientUI;

import java.util.ArrayList;

import org.coursera.symptommanager.Constants;
import org.coursera.symptommanager.InternetStatus;
import org.coursera.symptommanager.R;
import org.coursera.symptommanager.objects.Medication;
import org.coursera.symptommanager.objects.Question;

import retrofit.RetrofitError;
import android.app.ActionBar.LayoutParams;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class StartCheckInClassic extends FragmentActivity {

	// The number of pages will be defined by the number of medication per
	// patient + 2 (the first question and the last which are standart)
	public static int NUM_PAGES = 0;
	// the long that will hold the patient id which is send with the intent
	long patientID;
	// the parameter id
	String ID = "id";
	//
	String ERROR = "";
	// the array list that will hold the medication of the current patient
	ArrayList<Medication> medication;
	// a static array list that will hold the question results
	public static ArrayList<Question> questions = null;
	// declare the views
	// The pager widget, which handles animation and allows swiping horizontally
	public static ViewPager mPager;
	RelativeLayout layout;
	LinearLayout indicators;
	ProgressBar loading;

	// The pager adapter, which provides the pages to the view pager widget.
	private PagerAdapter mPagerAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// if the delivered intent has the extra id get it and save it as
		// doctorID

		if (getIntent().hasExtra(ID)) {
			patientID = getIntent().getLongExtra(ID, -1);

		}
		setContentView(R.layout.check_in_classic);
		// instantiate the views
		// Instantiate a ViewPager and a PagerAdapter.
		mPager = (ViewPager) findViewById(R.id.pager);

		indicators = (LinearLayout) findViewById(R.id.indicators);
		layout = (RelativeLayout) findViewById(R.id.layout);
		loading = (ProgressBar) findViewById(R.id.progressBar1);
		loading.setVisibility(View.GONE);
		getPatientMedication();

	}

	@Override
	public void onBackPressed() {
		if (mPager.getCurrentItem() == 0) {
			// If the user is currently looking at the first step, allow the
			// system to handle the
			// Back button. This calls finish() on this activity and pops the
			// back stack.
			super.onBackPressed();
		} else {
			// Otherwise, select the previous step.
			mPager.setCurrentItem(mPager.getCurrentItem() - 1);
		}
	}

	/**
	 * A simple pager adapter that represents 5 ScreenSlidePageFragment objects,
	 * in sequence.
	 */
	private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
		public ScreenSlidePagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a PlaceholderFragment (defined as a static inner class
			// below).
			if (position == 0) {
				// the fragment instantiates will patient ID, title of question
				// , type of question
				// the type of question is needed in order to know how many
				// answers I should create
				return QuestionFragmentClassic.newInstance(patientID,
						getString(R.string.first_checkin_question), "1",
						position);

			} else if (position == NUM_PAGES - 1) {
				// the last page
				return QuestionFragmentClassic.newInstance(patientID,
						getString(R.string.third_checkin_question), "3",
						position);
			} else {
				return QuestionFragmentClassic.newInstance(patientID,
						getString(R.string.second_checkin_question) + " "
								+ medication.get(position - 1).getName() + "?", "2",
						position);
			}

		}

		@Override
		public int getCount() {
			return NUM_PAGES;
		}
	}

	/**
	 * This will get the current patient's medication in order to create the
	 * adapter that will hold the fragments to show the correct questions
	 */
	private void getPatientMedication() {
		if (!InternetStatus.getInstance(
				StartCheckInClassic.this).isOnline(StartCheckInClassic.this)) {
			Toast.makeText(StartCheckInClassic.this, R.string.no_internet,
					Toast.LENGTH_LONG).show();

		} else {
		new getMedication().execute();
		}
	}

	public class getMedication extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			try {
				medication = Constants.svc.getMedication(patientID);
			} catch (final RetrofitError e) {
				Constants.reportIssue(StartCheckInClassic.this, e.getMessage());
				ERROR = e.getMessage();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			loading.setVisibility(View.GONE);
			layout.setClickable(true);
			if (ERROR.equalsIgnoreCase("")) {
				// we add +2 because the questions have 2 standard questions +
				// the medication questions
				NUM_PAGES = medication.size() + 2;
				for (int i = 0; i < NUM_PAGES; i++) {
					ImageView img = new ImageView(StartCheckInClassic.this);
					img.setTag(i);
					if (i == 0) {
						img.setImageResource(R.drawable.full);
					} else {
						img.setImageResource(R.drawable.empty);
					}
					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					indicators.addView(img, params);
				}
				mPager.setOnPageChangeListener(new OnPageChangeListener() {

					@Override
					public void onPageSelected(int arg0) {
						for (int i = 0; i < indicators.getChildCount(); i++) {
							ImageView img = (ImageView) indicators
									.getChildAt(i);
							img.setImageResource(R.drawable.empty);
						}
						ImageView image = (ImageView) indicators
								.getChildAt(arg0);
						if (image != null){
							image.setImageResource(R.drawable.full);
						}

					}

					@Override
					public void onPageScrolled(int arg0, float arg1, int arg2) {

					}

					@Override
					public void onPageScrollStateChanged(int arg0) {
	
					}
				});
				questions = new ArrayList<Question>();
				for (int i = 0; i < NUM_PAGES; i++) {
					questions.add(null);
				}
				mPagerAdapter = new ScreenSlidePagerAdapter(
						getFragmentManager());
				mPager.setAdapter(mPagerAdapter);
			} else {
				ERROR = "";
			}

		}

		@Override
		protected void onPreExecute() {
			medication = new ArrayList<Medication>();
			loading.setVisibility(View.VISIBLE);
			layout.setClickable(false);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		// check if screen is on if it is then show welcome message
		if (Constants.USERNAME.equalsIgnoreCase("")) {

		} else {
			if (Constants.SCREEN_ON) {
				Constants.showWelcomeMsg(StartCheckInClassic.this, layout);
			}

		}
	}
}
