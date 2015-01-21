package org.coursera.symptommanager;

import java.util.ArrayList;

import org.coursera.symptommanager.objects.Account;
import org.coursera.symptommanager.objects.CheckIn;
import org.coursera.symptommanager.objects.DoctorDetail;
import org.coursera.symptommanager.objects.Medication;
import org.coursera.symptommanager.objects.PatientDetail;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * 
 * This is the API for the Symptom Management application. It contains all the
 * web services in form of method using Retrofit
 * 
 */
public interface SymptomSvcApi {

	// the path to the authentication web service
	public static final String TOKEN_PATH = "/oauth/token";
	// The path where we expect the PatientSvc to live
	final String PATIENT_SVC_PATH = "/patient";
	// The path where we expect the DoctorSvc to live
	final String DOCTOR_SVC_PATH = "/doctor";
	// The path to add user
	final String ADD_USER = "/addUser";
	// The path to add the registration id of the device for the push
	// notification to the user account
	final String ADD_REGID = "/addRegId";
	// The path to add the registration id of the device for the push
	// notification to the doctor account
	final String ADD_REGID_DOCTOR = DOCTOR_SVC_PATH + "/addRegId";
	// The path to add the registration id of the device for the push
	// notification to the patient account
	final String ADD_REGID_PATIENT = PATIENT_SVC_PATH + "/addRegId";
	// the parameter for the registration id
	final String REGID_PARAMETER = "regId";
	// the path for the test web service to check the authentication of the user
	final String TEST = "/test";
	// the path for the test web service to populate the account table
	final String POPULATE_ACCOUNT_TABLE = "/populateUsers";
	// the path for the test web service to populate the patient's details table
	final String POPULATE_PATIENTS_TABLE = PATIENT_SVC_PATH
			+ "/populatePatients";
	// the path for the test web service to populate the doctor's details table
	final String POPULATE_DOCTOR_TABLE = DOCTOR_SVC_PATH + "/populateDoctors";
	// the path to get the patient's profile
	final String GET_PATIENT_PROFILE = PATIENT_SVC_PATH
			+ "/{username}/getPatientProfile";
	// the parameter for the username
	final String USERNAME = "username";
	// the path to create the patient's profile
	final String CREATE_PATIENT_PROFILE = PATIENT_SVC_PATH
			+ "/createPatientProfile";
	// the path to update the patient's profile
	final String UPDATE_PATIENT_PROFILE = PATIENT_SVC_PATH
			+ "/updatePatientProfile";
	// the path to search the database with the patients with the given name
	final String FIND_PATIENT_BY_NAME = DOCTOR_SVC_PATH
			+ "/searchByName/{name}";
	// the path to search the database with the patients with the given surname
	final String FIND_PATIENT_BY_SURNAME = DOCTOR_SVC_PATH
			+ "/searchBySurname/{name}";
	// the path to get all the medication the patient has
	final String GET_MEDICATION = PATIENT_SVC_PATH + "/getMedication/{id}";
	// the parameter for the id
	final String ID = "id";
	// the parameter for the id2
	final String ID2 = "id2";
	// the path to send the current check in (questionnair) from patient
	final String SEND_CHECKIN = PATIENT_SVC_PATH + "/sendCheckin";
	// the path to send a push notification to a doctor
	final String SEND_PUSH_2_DOCTOR = PATIENT_SVC_PATH + "/sendPush/{id}";
	// the parameter for the title
	final String TITLE = "title";
	// the parameter for the message
	final String MESSAGE = "message";
	// the path to get the doctor's profile
	final String GET_DOCTOR_PROFILE = DOCTOR_SVC_PATH
			+ "/{username}/getDoctorProfile";
	// the path to create the doctor's profile
	final String CREATE_DOCTOR_PROFILE = DOCTOR_SVC_PATH
			+ "/createDoctorProfile";
	// the path to update the doctor's profile
	final String UPDATE_DOCTOR_PROFILE = DOCTOR_SVC_PATH
			+ "/updateDoctorProfile";
	// the path to get the list of patients of the selected doctor
	final String GET_PATIENT_LIST = DOCTOR_SVC_PATH + "/{id}/getPatientList";
	// the path to get all the available patients.The ones that doesn't have any
	// doctor
	final String GET_AVAILABLE_PATIENTS = DOCTOR_SVC_PATH
			+ "/getAvailablePatients";
	// the path to get all the check ins of the current patient
	final String GET_PATIENTS_CHECKINS = DOCTOR_SVC_PATH + "/getCheckins/{id}";
	// the path to get the selected patient's profile
	final String GET_PATIENT_PROFILE_DOCTOR = DOCTOR_SVC_PATH
			+ "/getPatientProfile/{id}";
	// the path to get the patient's doctor profile
	final String GET_DOCTOR_PROFILE_PATIENT = PATIENT_SVC_PATH
			+ "/getDoctorProfile/{id}";
	// the path to add a medication to a patient
	final String ADD_MEDICATION = DOCTOR_SVC_PATH + "/addMedication";
	// the path to remove a medication from a patient
	final String REMOVE_MEDICATION = DOCTOR_SVC_PATH + "/removeMedication/{id}";
	// the path to send a push notification to a patient
	final String SEND_PUSH_2_PATIENT = DOCTOR_SVC_PATH + "/sendPush/{id}";
	// the path to update the idDoctor on a patient profile
	final String UDATE_PATIENT_PROFILE = DOCTOR_SVC_PATH
			+ "/{id}/updateProfilePatient/{id2}";
	// the path to remove a patient from a doctor
	final String REMOVE_PATIENT = DOCTOR_SVC_PATH + "/removePatient/{id}";

	// ---------------- GENERAL WEB SERVICES ----------------
	/**
	 * POST /addUser - Adds the account from the parameters to the database
	 * 
	 * @param account
	 * @return
	 */
	@POST(ADD_USER)
	public Account addUser(@Body Account account);

	/**
	 * POST /populateUsers - Fills the account table with the list of accounts
	 * from the parameters
	 * 
	 * @param list
	 * @return
	 */
	@POST(POPULATE_ACCOUNT_TABLE)
	public Void populateUsers(@Body ArrayList<Account> list);

	/**
	 * 
	 * @param list
	 * @return
	 */
	@POST(POPULATE_PATIENTS_TABLE)
	public Void populatePatientsProfiles(@Body ArrayList<PatientDetail> list);

	/**
	 * POST /doctor/populateDoctors - Fills the doctorProfile table with the
	 * list of doctorProfiles from the parameters
	 * 
	 * @param list
	 * @return
	 */
	@POST(POPULATE_DOCTOR_TABLE)
	public Void populateDoctorsProfiles(@Body ArrayList<DoctorDetail> list);

	/**
	 * POST /addRegId - Adds the registration id from the parameters to the
	 * account from the parameters and updates the database
	 * 
	 */
	@POST(ADD_REGID)
	public Account addRegIdTo(@Body Account account,
			@Query(REGID_PARAMETER) String regId);

	/**
	 * GET /test - It's a simple web service that we use in order to check if
	 * the current session is authenticated
	 * 
	 */
	@GET(TEST)
	public Void getTest();

	// ---------------- PATIENT'S WEB SERVICES ----------------
	/**
	 * GET /patient/{username}/getPatientProfile - Returns a patientDetail
	 * object which has the username from the parameters that is a path variable
	 * 
	 */
	@GET(GET_PATIENT_PROFILE)
	public PatientDetail getProfilePatient(@Path(USERNAME) String username);

	/**
	 * POST /patient/createPatientProfile - Creates an entry in the database
	 * with the given patientDetail object from the parameters
	 * 
	 */
	@POST(CREATE_PATIENT_PROFILE)
	public PatientDetail createProfilePatient(@Body PatientDetail details);

	/**
	 * POST /patient/updatePatientProfile - Replace the given patientDetail
	 * object in the parameters with the entry in the database that has the same
	 * id
	 * 
	 */
	@POST(UPDATE_PATIENT_PROFILE)
	public PatientDetail updateProfilePatient(@Body PatientDetail details);

	/**
	 * GET /patient/getMedication/{id} - Returns a list with medication that are
	 * associated with the idPatient equal to id from the parameters the id is a
	 * path variable
	 * 
	 */
	@GET(GET_MEDICATION)
	public ArrayList<Medication> getMedication(@Path(ID) long id);

	/**
	 * POST /patient/sendCheckin - Creates a Checkin object with the values from
	 * the parameters and saves it in the CheckIn database
	 * 
	 */
	@POST(SEND_CHECKIN)
	public Void sendCheckIn(@Query(ID) long id, @Query("raw") String raw,
			@Query("idPatient") long idPatient, @Query("time") long time);

	/**
	 * POST /patient/sendPush/{id} - Sends a push notification to the doctor
	 * with the same id from the parameters. The push notification will contail
	 * the title from the parameters and the message from the parameters
	 * 
	 */
	@POST(SEND_PUSH_2_DOCTOR)
	public Void sendPushDoctor(@Path(ID) long id, @Query(TITLE) String title,
			@Query(MESSAGE) String message);

	// ---------------- DOCTOR'S WEB SERVICES ----------------

	/**
	 * GET /doctor/{username}/getDoctorProfile - Returns a doctorDetail object
	 * which has the username from the parameters that is a path variable
	 * 
	 */
	@GET(GET_DOCTOR_PROFILE)
	public DoctorDetail getProfileDoctor(@Path(USERNAME) String username);

	/**
	 * POST /doctor/createDoctorProfile - Creates an entry in the database with
	 * the given doctorDetail object from the parameters
	 * 
	 */
	@POST(CREATE_DOCTOR_PROFILE)
	public PatientDetail createProfileDoctor(@Body DoctorDetail details);

	/**
	 * POST /doctor/updateDoctorProfile - Replace the given doctorDetail object
	 * in the parameters with the entry in the database that has the same id
	 * 
	 */
	@POST(UPDATE_DOCTOR_PROFILE)
	public DoctorDetail updateProfileDoctor(@Body DoctorDetail details);

	/**
	 * GET /doctor/{id}/getPatientList - Returns a list of patientDetails
	 * objects that have the id from the parameters as a idDoctor. The id is
	 * path variable
	 * 
	 */
	@GET(GET_PATIENT_LIST)
	public ArrayList<PatientDetail> getPatientList(@Path(ID) long id);

	/**
	 * GET /doctor/getAvailablePatients - Returns a list with all the available
	 * patient that means all the patient that have -1 in the idDoctor in the
	 * database
	 * 
	 */
	@GET(GET_AVAILABLE_PATIENTS)
	public ArrayList<PatientDetail> getAvailablePatients();

	/**
	 * GET /doctor/getCheckins/{id} - Returns a list with all the checkin
	 * objects that have id equal to the id from the parameters. The id is a
	 * path variable
	 * 
	 */
	@GET(GET_PATIENTS_CHECKINS)
	public ArrayList<CheckIn> getCheckins(@Path(ID) long id);

	/**
	 * GET /doctor/getPatientProfile/{id} - Returns the patientDetail object
	 * that has the id equal to the id from the parameters. The id is a path
	 * variable
	 * 
	 */
	@GET(GET_PATIENT_PROFILE_DOCTOR)
	public PatientDetail getPatientProfile(@Path(ID) long id);

	/**
	 * GET /doctor/getDoctorProfile/{id} - Returns the doctorDetail object that
	 * has the id equal to the id from the parameters. The id is a path variable
	 */
	@GET(GET_DOCTOR_PROFILE_PATIENT)
	public DoctorDetail getDoctorProfile(@Path(ID) long id);

	/**
	 * POST /doctor/addMedication - Add a new entry in the database of
	 * medications with the given id, name, description, idPatient from the
	 * parameters
	 * 
	 */
	@POST(ADD_MEDICATION)
	public Void addMedication(@Query(ID) long id, @Query("name") String name,
			@Query("description") String description,
			@Query("idPatient") long idPatient);

	/**
	 * POST /doctor/removeMedication - Deletes an entry from the database that
	 * has the id equal to the id from the parameters. The id is a path variable
	 * 
	 */
	@POST(REMOVE_MEDICATION)
	public Void removeMedication(@Path(ID) long id);

	/**
	 * POST /doctor/sendPush/{id} - Sends a push notification the the patient
	 * that has id equal to the id from the parameters. The id is a path
	 * variable. The push notification will have title the title from the
	 * parameters and message the message from the parameters
	 * 
	 */
	@POST(SEND_PUSH_2_PATIENT)
	public Void sendPushPatient(@Path(ID) long id, @Query(TITLE) String title,
			@Query(MESSAGE) String message);

	/**
	 * GET /doctor/{id}/updateProfilePatient/{id2} - Update the patientDetail
	 * object that has id equal to the id from the parameters and replace the
	 * objects idDoctor to be equal to the id2. The id and id2 are path
	 * variables
	 */
	@GET(UDATE_PATIENT_PROFILE)
	public Void addPatient(@Path(ID) long id, @Path(ID2) long id2);

	/**
	 * GET /doctor/removePatient/{id} - Update the patientDetail object that has
	 * id equal to the id2 from the parameters and replace the objects idDoctor
	 * to be -1. The id2 is path variables
	 */
	@GET(REMOVE_PATIENT)
	public Void removePatient(@Path(ID) long id2);

	/**
	 * POST /doctor/addRegId - Adds the registration id from the parameters to
	 * the doctorDetail object that has as id equal to the id from the
	 * parameters
	 * 
	 */
	@POST(ADD_REGID_DOCTOR)
	public DoctorDetail addRegIdToDoctor(@Query("id") long id,
			@Query(REGID_PARAMETER) String regId);

	/**
	 * POST /patient/addRegId - Adds the registration id from the parameters to
	 * the patientDetail object that has as id equal to the id from the
	 * parameters
	 * 
	 */
	@POST(ADD_REGID_PATIENT)
	public PatientDetail addRegIdToPatient(@Query("id") long id,
			@Query(REGID_PARAMETER) String regId);

	/**
	 * GET /doctor/searchByName/{name} - Search the database for the
	 * doctorDetail objects that the name column contains the str from the
	 * parameters, and returns all the objects in a list that satisfy this
	 * condition
	 */
	@GET(FIND_PATIENT_BY_NAME)
	public ArrayList<PatientDetail> findByNameContaining(
			@Path("name") String str);

	/**
	 * GET /doctor/searchBySurname/{name} - Search the database for the
	 * doctorDetail objects that the surname column contains the str from the
	 * parameters, and returns all the objects in a list that satisfy this
	 * condition
	 */
	@GET(FIND_PATIENT_BY_SURNAME)
	public ArrayList<PatientDetail> findBySurnameContaining(
			@Path("name") String str);
}
