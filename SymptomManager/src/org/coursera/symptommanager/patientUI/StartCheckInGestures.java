package org.coursera.symptommanager.patientUI;

import java.util.ArrayList;

import org.coursera.symptommanager.Constants;
import org.coursera.symptommanager.InternetStatus;
import org.coursera.symptommanager.R;
import org.coursera.symptommanager.objects.Medication;
import org.coursera.symptommanager.objects.Question;

import retrofit.RetrofitError;
import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.gesture.Prediction;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class StartCheckInGestures extends FragmentActivity {

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
	// the dialog that will guide you through the tutorial
	AlertDialog tutorialDialog;
	AlertDialog tutorialDialog1;
	AlertDialog tutorialDialog2;
	AlertDialog tutorialDialog3;
	AlertDialog tutorialDialog4;
	AlertDialog tutorialDialog5;
	// gestures names
	String YES = "yes";
	String NO = "no";
	String ONE = "one";
	String TWO = "two";
	String THREE = "three";
	// the flag to prompt the user to make again the gesture
	boolean correct = false;

	GestureLibrary mLibrary;

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
		mLibrary = GestureLibraries.fromRawResource(this, R.raw.gestures);
		if (!mLibrary.load()) {
			finish();
		}
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
	private class ScreenSlideGesturePagerAdapter extends
			FragmentStatePagerAdapter {
		public ScreenSlideGesturePagerAdapter(FragmentManager fm) {
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
				return QuestionFragmentGestures.newInstance(patientID,
						getString(R.string.first_checkin_question), "1",
						position);

			} else if (position == NUM_PAGES - 1) {
				// the last page
				return QuestionFragmentGestures.newInstance(patientID,
						getString(R.string.third_checkin_question), "3",
						position);
			} else {
				return QuestionFragmentGestures.newInstance(patientID,
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
				StartCheckInGestures.this).isOnline(StartCheckInGestures.this)) {
			Toast.makeText(StartCheckInGestures.this, R.string.no_internet,
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
				Constants
						.reportIssue(StartCheckInGestures.this, e.getMessage());
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
				questions = new ArrayList<Question>();
				for (int i = 0; i < NUM_PAGES; i++) {
					questions.add(null);
				}
				createDialogTutorial();
				for (int i = 0; i < NUM_PAGES; i++) {
					ImageView img = new ImageView(StartCheckInGestures.this);
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

	private void createDialogTutorial() {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				StartCheckInGestures.this);
		LayoutInflater inflater = getLayoutInflater();
		View view = inflater.inflate(R.layout.tutorial_dialog1, null);
		builder.setView(view);
		builder.setTitle(getString(R.string.start_tutorial));
		builder.setCancelable(false);
		builder.setPositiveButton("Start",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						createDialogTutorial1();
						tutorialDialog.dismiss();
					}
				});

		builder.setNegativeButton("Skip",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {

						mPagerAdapter = new ScreenSlideGesturePagerAdapter(
								getFragmentManager());
						mPager.setAdapter(mPagerAdapter);
						tutorialDialog.dismiss();
					}
				});
		tutorialDialog = builder.create();
		tutorialDialog.show();
	}

	private void createDialogTutorial1() {
		ImageView drawingImageView;
		Bitmap bitmap;

		AlertDialog.Builder builder = new AlertDialog.Builder(
				StartCheckInGestures.this);
		LayoutInflater inflater = getLayoutInflater();
		View view = inflater.inflate(R.layout.tutorial_dialog2, null);
		builder.setView(view);
		builder.setCancelable(false);
		drawingImageView = (ImageView) view.findViewById(R.id.drawing);
		bitmap = Bitmap.createBitmap(250, 250, Bitmap.Config.ARGB_8888);
		createSample(YES, drawingImageView, bitmap);
		builder.setTitle(getString(R.string.tutorial_step1));
		GestureOverlayView gOverlay = (GestureOverlayView) view
				.findViewById(R.id.gOverlay);
		gOverlay.addOnGesturePerformedListener(new OnGesturePerformedListener() {

			@Override
			public void onGesturePerformed(GestureOverlayView overlay,
					Gesture gesture) {
				// Get gesture predictions
				ArrayList<Prediction> predictions = mLibrary.recognize(gesture);
				// Ignore weak predictions
				// Get highest-ranked prediction
				if (predictions.size() > 0) {
					Prediction prediction = predictions.get(0);

					if (prediction.score > 2.0) {
						if (prediction.name.equals(YES)) {

							if (correct) {
								createDialogTutorial2();
								tutorialDialog1.dismiss();
								correct = false;
							} else {
								Toast.makeText(
										StartCheckInGestures.this,
										getString(R.string.perform_gesture_again),
										Toast.LENGTH_SHORT).show();
								correct = true;
							}

						} else {
							Toast.makeText(StartCheckInGestures.this,
									"No prediction", Toast.LENGTH_SHORT).show();

						}
					}
				}
			}

		});

		builder.setNegativeButton("Skip",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {

						mPagerAdapter = new ScreenSlideGesturePagerAdapter(
								getFragmentManager());
						mPager.setAdapter(mPagerAdapter);
						tutorialDialog1.dismiss();
					}
				});
		tutorialDialog1 = builder.create();
		tutorialDialog1.show();

	}

	private void createDialogTutorial2() {
		ImageView drawingImageView;
		Bitmap bitmap;
		AlertDialog.Builder builder = new AlertDialog.Builder(
				StartCheckInGestures.this);
		LayoutInflater inflater = getLayoutInflater();
		View view = inflater.inflate(R.layout.tutorial_dialog2, null);
		builder.setView(view);
		builder.setCancelable(false);
		TextView title_tutorial = (TextView) view
				.findViewById(R.id.title_tutorial);
		title_tutorial.setText(getString(R.string.title_tutorial2));
		drawingImageView = (ImageView) view.findViewById(R.id.drawing);
		bitmap = Bitmap.createBitmap(250, 250, Bitmap.Config.ARGB_8888);
		createSample(NO, drawingImageView, bitmap);
		builder.setTitle(getString(R.string.tutorial_step2));
		GestureOverlayView gOverlay = (GestureOverlayView) view
				.findViewById(R.id.gOverlay);
		gOverlay.addOnGesturePerformedListener(new OnGesturePerformedListener() {

			@Override
			public void onGesturePerformed(GestureOverlayView overlay,
					Gesture gesture) {
				// Get gesture predictions
				ArrayList<Prediction> predictions = mLibrary.recognize(gesture);
				// Ignore weak predictions
				// Get highest-ranked prediction
				if (predictions.size() > 0) {
					Prediction prediction = predictions.get(0);

					if (prediction.score > 2.0) {
						if (prediction.name.equals(NO)) {

							if (correct) {
								createDialogTutorial3();
								tutorialDialog2.dismiss();
								correct = false;
							} else {
								Toast.makeText(
										StartCheckInGestures.this,
										getString(R.string.perform_gesture_again),
										Toast.LENGTH_SHORT).show();
								correct = true;
							}

						} else {
							Toast.makeText(StartCheckInGestures.this,
									"No prediction", Toast.LENGTH_SHORT).show();

						}
					}
				}
			}

		});

		builder.setNegativeButton("Skip",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {

						mPagerAdapter = new ScreenSlideGesturePagerAdapter(
								getFragmentManager());
						mPager.setAdapter(mPagerAdapter);
						tutorialDialog2.dismiss();
					}
				});
		tutorialDialog2 = builder.create();
		tutorialDialog2.show();

	}

	private void createDialogTutorial3() {
		ImageView drawingImageView;
		Bitmap bitmap;
		AlertDialog.Builder builder = new AlertDialog.Builder(
				StartCheckInGestures.this);
		LayoutInflater inflater = getLayoutInflater();
		View view = inflater.inflate(R.layout.tutorial_dialog2, null);
		builder.setView(view);
		builder.setCancelable(false);
		TextView title_tutorial = (TextView) view
				.findViewById(R.id.title_tutorial);
		title_tutorial.setText(getString(R.string.title_tutorial3));
		drawingImageView = (ImageView) view.findViewById(R.id.drawing);
		bitmap = Bitmap.createBitmap(250, 250, Bitmap.Config.ARGB_8888);
		createSample(ONE, drawingImageView, bitmap);
		builder.setTitle(getString(R.string.tutorial_step3));
		GestureOverlayView gOverlay = (GestureOverlayView) view
				.findViewById(R.id.gOverlay);
		gOverlay.addOnGesturePerformedListener(new OnGesturePerformedListener() {

			@Override
			public void onGesturePerformed(GestureOverlayView overlay,
					Gesture gesture) {
				// Get gesture predictions
				ArrayList<Prediction> predictions = mLibrary.recognize(gesture);
				// Ignore weak predictions
				// Get highest-ranked prediction
				if (predictions.size() > 0) {
					Prediction prediction = predictions.get(0);

					if (prediction.score > 2.0) {
						if (prediction.name.equals(ONE)) {

							if (correct) {
								createDialogTutorial4();
								tutorialDialog3.dismiss();
								correct = false;
							} else {
								Toast.makeText(
										StartCheckInGestures.this,
										getString(R.string.perform_gesture_again),
										Toast.LENGTH_SHORT).show();
								correct = true;
							}

						} else {
							Toast.makeText(StartCheckInGestures.this,
									"No prediction", Toast.LENGTH_SHORT).show();

						}
					}
				}
			}

		});

		builder.setNegativeButton("Skip",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {

						mPagerAdapter = new ScreenSlideGesturePagerAdapter(
								getFragmentManager());
						mPager.setAdapter(mPagerAdapter);
						tutorialDialog3.dismiss();
					}
				});
		tutorialDialog3 = builder.create();
		tutorialDialog3.show();

	}

	private void createDialogTutorial4() {
		ImageView drawingImageView;
		Bitmap bitmap;
		AlertDialog.Builder builder = new AlertDialog.Builder(
				StartCheckInGestures.this);
		LayoutInflater inflater = getLayoutInflater();
		View view = inflater.inflate(R.layout.tutorial_dialog2, null);
		builder.setView(view);
		builder.setCancelable(false);
		TextView title_tutorial = (TextView) view
				.findViewById(R.id.title_tutorial);
		title_tutorial.setText(getString(R.string.title_tutorial4));
		drawingImageView = (ImageView) view.findViewById(R.id.drawing);
		bitmap = Bitmap.createBitmap(250, 250, Bitmap.Config.ARGB_8888);
		createSample(TWO, drawingImageView, bitmap);
		builder.setTitle(getString(R.string.tutorial_step4));
		GestureOverlayView gOverlay = (GestureOverlayView) view
				.findViewById(R.id.gOverlay);
		gOverlay.addOnGesturePerformedListener(new OnGesturePerformedListener() {

			@Override
			public void onGesturePerformed(GestureOverlayView overlay,
					Gesture gesture) {
				// Get gesture predictions
				ArrayList<Prediction> predictions = mLibrary.recognize(gesture);
				// Ignore weak predictions
				// Get highest-ranked prediction
				if (predictions.size() > 0) {
					Prediction prediction = predictions.get(0);

					if (prediction.score > 2.0) {
						if (prediction.name.equals(TWO)) {

							if (correct) {
								createDialogTutorial5();
								tutorialDialog4.dismiss();
								correct = false;
							} else {
								Toast.makeText(
										StartCheckInGestures.this,
										getString(R.string.perform_gesture_again),
										Toast.LENGTH_SHORT).show();
								correct = true;
							}

						} else {
							Toast.makeText(StartCheckInGestures.this,
									"No prediction", Toast.LENGTH_SHORT).show();

						}
					}
				}
			}

		});

		builder.setNegativeButton("Skip",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {

						mPagerAdapter = new ScreenSlideGesturePagerAdapter(
								getFragmentManager());
						mPager.setAdapter(mPagerAdapter);
						tutorialDialog4.dismiss();
					}
				});
		tutorialDialog4 = builder.create();
		tutorialDialog4.show();

	}

	private void createDialogTutorial5() {
		ImageView drawingImageView;
		Bitmap bitmap;
		AlertDialog.Builder builder = new AlertDialog.Builder(
				StartCheckInGestures.this);
		LayoutInflater inflater = getLayoutInflater();
		View view = inflater.inflate(R.layout.tutorial_dialog2, null);
		builder.setView(view);
		builder.setCancelable(false);
		TextView title_tutorial = (TextView) view
				.findViewById(R.id.title_tutorial);
		title_tutorial.setText(getString(R.string.title_tutorial5));
		drawingImageView = (ImageView) view.findViewById(R.id.drawing);
		bitmap = Bitmap.createBitmap(250, 250, Bitmap.Config.ARGB_8888);
		createSample(THREE, drawingImageView, bitmap);
		builder.setTitle(getString(R.string.tutorial_step5));
		GestureOverlayView gOverlay = (GestureOverlayView) view
				.findViewById(R.id.gOverlay);
		gOverlay.addOnGesturePerformedListener(new OnGesturePerformedListener() {

			@Override
			public void onGesturePerformed(GestureOverlayView overlay,
					Gesture gesture) {
				// Get gesture predictions
				ArrayList<Prediction> predictions = mLibrary.recognize(gesture);
				// Ignore weak predictions
				// Get highest-ranked prediction
				if (predictions.size() > 0) {
					Prediction prediction = predictions.get(0);

					if (prediction.score > 2.0) {
						if (prediction.name.equals(THREE)) {

							if (correct) {
								mPagerAdapter = new ScreenSlideGesturePagerAdapter(
										getFragmentManager());
								mPager.setAdapter(mPagerAdapter);
								tutorialDialog5.dismiss();
								correct = false;
							} else {
								Toast.makeText(
										StartCheckInGestures.this,
										getString(R.string.perform_gesture_again),
										Toast.LENGTH_SHORT).show();
								correct = true;
							}

						} else {
							Toast.makeText(StartCheckInGestures.this,
									"No prediction", Toast.LENGTH_SHORT).show();

						}
					}
				}
			}

		});

		builder.setNegativeButton("Skip",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {

						mPagerAdapter = new ScreenSlideGesturePagerAdapter(
								getFragmentManager());
						mPager.setAdapter(mPagerAdapter);
						tutorialDialog5.dismiss();
					}
				});
		tutorialDialog5 = builder.create();
		tutorialDialog5.show();

	}

	private void createSample(String title, ImageView image, Bitmap bitmap) {
		Canvas canvas = new Canvas(bitmap);
		image.setImageBitmap(bitmap);
		int startx;
		int starty;
		int endx;
		int endy;
		// Line
		Paint paint = new Paint();
		paint.setColor(Color.GREEN);
		paint.setStrokeWidth(2);

		if (title.equalsIgnoreCase(YES)) {
			startx = 100;
			starty = 150;
			endx = 150;
			endy = 200;
			canvas.drawLine(startx, starty, endx, endy, paint);
			startx = 150;
			starty = 200;
			endx = 225;
			endy = 50;
			canvas.drawLine(startx, starty, endx, endy, paint);
		} else if (title.equalsIgnoreCase(NO)) {
			startx = 50;
			starty = 50;
			endx = 200;
			endy = 200;
			canvas.drawLine(startx, starty, endx, endy, paint);
			startx = 200;
			starty = 200;
			endx = 200;
			endy = 50;
			canvas.drawLine(startx, starty, endx, endy, paint);
			startx = 200;
			starty = 50;
			endx = 50;
			endy = 200;
			canvas.drawLine(startx, starty, endx, endy, paint);
		} else if (title.equalsIgnoreCase(ONE)) {
			startx = 150;
			starty = 50;
			endx = 150;
			endy = 200;
			canvas.drawLine(startx, starty, endx, endy, paint);
			startx = 150;
			starty = 200;
			endx = 220;
			endy = 200;
			canvas.drawLine(startx, starty, endx, endy, paint);
		} else if (title.equalsIgnoreCase(TWO)) {
			startx = 70;
			starty = 50;
			endx = 70;
			endy = 200;
			canvas.drawLine(startx, starty, endx, endy, paint);
			startx = 70;
			starty = 200;
			endx = 170;
			endy = 200;
			canvas.drawLine(startx, starty, endx, endy, paint);
			startx = 170;
			starty = 200;
			endx = 170;
			endy = 50;
			canvas.drawLine(startx, starty, endx, endy, paint);
		} else if (title.equalsIgnoreCase(THREE)) {
			startx = 50;
			starty = 50;
			endx = 50;
			endy = 200;
			canvas.drawLine(startx, starty, endx, endy, paint);
			startx = 50;
			starty = 200;
			endx = 120;
			endy = 200;
			canvas.drawLine(startx, starty, endx, endy, paint);
			startx = 120;
			starty = 200;
			endx = 120;
			endy = 50;
			canvas.drawLine(startx, starty, endx, endy, paint);
			startx = 120;
			starty = 50;
			endx = 180;
			endy = 50;
			canvas.drawLine(startx, starty, endx, endy, paint);
			startx = 180;
			starty = 50;
			endx = 180;
			endy = 200;
			canvas.drawLine(startx, starty, endx, endy, paint);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		// check if screen is on if it is then show welcome message
		if (Constants.USERNAME.equalsIgnoreCase("")) {

		} else {
			if (Constants.SCREEN_ON) {
				Constants.showWelcomeMsg(StartCheckInGestures.this, layout);
			}

		}
	}
}
