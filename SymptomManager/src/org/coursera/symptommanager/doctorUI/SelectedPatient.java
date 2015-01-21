package org.coursera.symptommanager.doctorUI;

import org.coursera.symptommanager.R;

import android.app.Activity;
import android.os.Bundle;

public class SelectedPatient extends Activity {

	// selected patient ID
	long patientID;
	// id parameter
	String ID = "id";
	// selected doctor's ID
		long doctorID;
		// id parameter for doctor
		String IDD = "idd";
	// showing details parameter
	String DETAILS = "details";
	public static boolean mShowingBack = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.patient_detail_data_container);
		// if the delivered intent has the extra id get it and save it as
		// patientID
		if (getIntent().hasExtra(ID)) {
			patientID = getIntent().getLongExtra(ID, -1);

		}
		if (getIntent().hasExtra(IDD)) {
			doctorID = getIntent().getLongExtra(IDD, -1);

		}
		// create an instance of the PatientData fragment
		PatientDataFragment fragment = new PatientDataFragment();
		// create a bundle and add inside the patient ID
		// this bundle will be passed on the fragment
		Bundle bundle = new Bundle();
		bundle.putLong(ID, patientID);
		bundle.putLong(IDD, doctorID);
		fragment.setArguments(bundle);
		// adding the patient data fragments in the container
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, fragment).commit();
		}
	}

}