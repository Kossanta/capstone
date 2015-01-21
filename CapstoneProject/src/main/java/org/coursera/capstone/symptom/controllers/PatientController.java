package org.coursera.capstone.symptom.controllers;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletResponse;
import org.coursera.capstone.symptom.Application;
import org.coursera.capstone.symptom.Constants;
import org.coursera.capstone.symptom.push.Post2Gcm;
import org.coursera.capstone.symptom.repository.CheckIn;
import org.coursera.capstone.symptom.repository.CheckInRepository;
import org.coursera.capstone.symptom.repository.DoctorDetail;
import org.coursera.capstone.symptom.repository.DoctorDetailRepository;
import org.coursera.capstone.symptom.repository.Medication;
import org.coursera.capstone.symptom.repository.MedicationRepository;
import org.coursera.capstone.symptom.repository.PatientDetail;
import org.coursera.capstone.symptom.repository.PatientDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 
 * The controller which handles ONLY the patient requests.
 * 
 */
// Tell Spring that this class is a Controller that should
// handle the patient HTTP requests(This separation
// happens because we added the @RequestMapping annotation with the /patient)
// for the DispatcherServlet
@Controller
@RequestMapping(Constants.PATIENT_SVC_PATH)
public class PatientController {

	// the path for get patient profile with the username path parameter
	final String GET_PATIENT_PROFILE = "/{username}/getPatientProfile";
	// the path for update patient profile
	final String UPDATE_PATIENT_PROFILE = "/updatePatientProfile";
	// the path for create patient profile
	final String CREATE_PATIENT_PROFILE = "/createPatientProfile";
	// the path for get medication with the id path parameter
	final String GET_MEDICATION = "/getMedication/{id}";
	// the path for the send checkin
	final String SEND_CHECKIN = "/sendCheckin";
	// the path for the get doctor profile with the id path parameter
	final String GET_DOCTOR_PROFILE = "/getDoctorProfile/{id}";
	// the path for send push notification with the id path parameter
	final String SEND_PUSH_2_DOCTOR = "/sendPush/{id}";
	// the parameter for the username
	final String USERNAME = "username";
	// the parameter for the id
	final String ID = "id";
	// the parameter for the title
	final String TITLE = "title";
	// the parameter for the message
	final String MESSAGE = "message";
	// the parameter for the raw
	final String RAW = "raw";
	// the parameter for the idPatient
	final String ID_PATIENT = "idPatient";
	// the parameter for the time
	final String TIME = "time";
	// the parameter for the regId
	final String REGID_PARAMETER = "regId";
	// the path for population of the table patient's details with all the
	// available
	// users which are hard coded
	final String POPULATE_PATIENT_DETAILS_TABLE = "/populatePatients";
	// The path to add the registration id of the device for the push
	// notification to the patient account
	final String ADD_REGID = "/addRegId";

	// The CheckInRepository that we are going to store our checkins
	// in. We don't explicitly construct a CheckInRepository, but
	// instead mark this object as a dependency that needs to be
	// injected by Spring.
	//
	@Autowired
	private CheckInRepository checkInRepo;

	// The PatientDetailRepository that we are going to store our patientDetails
	// in. We don't explicitly construct a PatientDetailRepository, but
	// instead mark this object as a dependency that needs to be
	// injected by Spring.
	//
	@Autowired
	private PatientDetailRepository patientDetailRepo;

	// The MedicationRepository that we are going to store our medications
	// in. We don't explicitly construct a MedicationRepository, but
	// instead mark this object as a dependency that needs to be
	// injected by Spring.
	//
	@Autowired
	private MedicationRepository pillRepo;

	// The DoctorDetailRepository that we are going to store our doctorDetails
	// in. We don't explicitly construct a DoctorDetailRepository, but
	// instead mark this object as a dependency that needs to be
	// injected by Spring.
	//
	@Autowired
	private DoctorDetailRepository doctorDetailRepo;

	/**
	 * Receives GET requests to /{username}/getPatientProfile and returns the
	 * current patientDetail object that is saved in the database with the given
	 * username. The @PathVariable annotation tell Spring to get the "username"
	 * value from the path and add it to the username method parameter. The
	 * HttpServletResponse will contain the response of the request. The @ResponseBody
	 * annotation tells Spring to convert the return value from the method back
	 * into JSON and put it into the body of the HTTP response to the client.
	 */
	@RequestMapping(value = GET_PATIENT_PROFILE, method = RequestMethod.GET)
	public @ResponseBody
	PatientDetail getProfilePatient(@PathVariable(USERNAME) String username,
			HttpServletResponse response) throws IOException {
		if (patientDetailRepo.findByUsername(username) == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
		} else {
			response.setStatus(HttpServletResponse.SC_OK);
			return patientDetailRepo.findByUsername(username);
		}
	}

	/**
	 * Receives GET requests to /getDoctorProfile/{id} and returns the current
	 * doctorDetail object that is saved in the database with the given id. The @PathVariable
	 * annotation tell Spring to get the "id" value from the path and add it to
	 * the id method parameter. The HttpServletResponse will contain the
	 * response of the request. The @ResponseBody annotation tells Spring to
	 * convert the return value from the method back into JSON and put it into
	 * the body of the HTTP response to the client.
	 */
	@RequestMapping(value = GET_DOCTOR_PROFILE, method = RequestMethod.GET)
	public @ResponseBody
	DoctorDetail getDoctorProfile(@PathVariable(ID) long id,
			HttpServletResponse response) throws IOException {
		if (doctorDetailRepo.findOne(id) == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
		} else {
			response.setStatus(HttpServletResponse.SC_OK);
			return doctorDetailRepo.findOne(id);
		}
	}

	/**
	 * Receives POST requests to /createPatientProfile and returns the
	 * patientDetail object that is just saved in the database. The @RequestBody
	 * annotation on the details parameter is what tells Spring to interpret the
	 * HTTP request body as JSON and convert it into a PatientDetail object to
	 * pass into the method. The HttpServletResponse will contain the response
	 * of the request. The @ResponseBody annotation tells Spring to convert the
	 * return value from the method back into JSON and put it into the body of
	 * the HTTP response to the client.
	 */
	@RequestMapping(value = CREATE_PATIENT_PROFILE, method = RequestMethod.POST)
	public @ResponseBody
	PatientDetail createProfilePatient(@RequestBody PatientDetail details,
			HttpServletResponse response) throws IOException {
		patientDetailRepo.save(details);
		return patientDetailRepo.findByUsername(details.getUsername());
	}

	/**
	 * Receives POST requests to /populatePatients and returns void. The @RequestBody
	 * annotation on the list parameter is what tells Spring to interpret the
	 * HTTP request body as JSON and convert it into an ArrayList<PatientDetail>
	 * array to pass into the method. The HttpServletResponse will contain the
	 * response of the request. If the first hard coded patient doesn't exist in
	 * the repository then for all the objects in the list each object will be
	 * saved in the repository. The request returns void. The reason for this
	 * request is to hard code the patient's details into the repository
	 */
	@RequestMapping(value = POPULATE_PATIENT_DETAILS_TABLE, method = RequestMethod.POST)
	public ResponseEntity<Void> populatePatientsProfiles(
			@RequestBody ArrayList<PatientDetail> list,
			HttpServletResponse response) {
		if (patientDetailRepo.findByUsername("george") == null) {
			for (int i = 0; i < list.size(); i++) {
				patientDetailRepo.save(list.get(i));
			}
		} else {

		}
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	/**
	 * Receives POST requests to /updatePatientProfile and returns the
	 * patientDetail object that is just updated in the database. The @RequestBody
	 * annotation on the details parameter is what tells Spring to interpret the
	 * HTTP request body as JSON and convert it into a PatientDetail object to
	 * pass into the method. The HttpServletResponse will contain the response
	 * of the request. The @ResponseBody annotation tells Spring to convert the
	 * return value from the method back into JSON and put it into the body of
	 * the HTTP response to the client.
	 */
	@RequestMapping(value = UPDATE_PATIENT_PROFILE, method = RequestMethod.POST)
	public @ResponseBody
	PatientDetail updateProfilePatient(@RequestBody PatientDetail details,
			HttpServletResponse response) throws IOException {
		patientDetailRepo.save(details);
		return patientDetailRepo.findByUsername(details.getUsername());
	}

	/**
	 * Receives GET requests to /getMedication/{id} and returns the array list
	 * with all the medication of that are saved in the database with the given
	 * patient id. The @PathVariable annotation tell Spring to get the "id"
	 * value from the path and add it to the id method parameter. The
	 * HttpServletResponse will contain the response of the request. The @ResponseBody
	 * annotation tells Spring to convert the return value from the method back
	 * into JSON and put it into the body of the HTTP response to the client.
	 * This method finds all the entries in the MedicationRepository that have
	 * patient id equal to the method parameter id.
	 */
	@RequestMapping(value = GET_MEDICATION, method = RequestMethod.GET)
	public @ResponseBody
	ArrayList<Medication> getMedication(@PathVariable(ID) long id,
			HttpServletResponse response) throws IOException {
		if (pillRepo.findByIdPatient(id) == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
		} else {
			response.setStatus(HttpServletResponse.SC_OK);
			return pillRepo.findByIdPatient(id);
		}
	}

	/**
	 * Receives POST requests to /sendCheckin. The @RequestParam annotation on
	 * the String raw tell Spring to use the "raw" parameter in the HTTP
	 * request's query string as the value for the raw method parameter. The @RequestParam
	 * annotation on the long idPatient tell Spring to use the "idPatient"
	 * parameter in the HTTP request's query string as the value for the
	 * idPatient method parameter. The @RequestParam annotation on the long time
	 * tell Spring to use the "time" parameter in the HTTP request's query
	 * string as the value for the time method parameter. The
	 * HttpServletResponse will contain the response of the request. The request
	 * returns void. This method creates a Checkin object form the method
	 * parameters and saves this object to the CheckInRepository.
	 * 
	 */
	@RequestMapping(value = SEND_CHECKIN, method = RequestMethod.POST)
	public ResponseEntity<Void> sendCheckIn(@RequestParam(ID) long id,
			@RequestParam(RAW) String raw,
			@RequestParam(ID_PATIENT) long idPatient,
			@RequestParam(TIME) long time) {
		checkInRepo.save(new CheckIn(raw, idPatient, time));
		return new ResponseEntity<Void>(HttpStatus.OK);

	}

	/**
	 * Receives POST requests to /sendPush/{id}. The @RequestParam annotation on
	 * the String title tell Spring to use the "title" parameter in the HTTP
	 * request's query string as the value for the title method parameter. The @RequestParam
	 * annotation on the String message tell Spring to use the "message"
	 * parameter in the HTTP request's query string as the value for the message
	 * method parameter. The @PathVariable annotation tell Spring to get the
	 * "id" value from the path and add it to the id method parameter. The
	 * HttpServletResponse will contain the response of the request. The request
	 * returns void. This method checks the DoctorDetailRepository if a
	 * doctorDetail entry with the given id exists. If yes then it triggers the
	 * Post2Gcm method to send a push notification to the doctor.
	 * 
	 */
	@RequestMapping(value = SEND_PUSH_2_DOCTOR, method = RequestMethod.POST)
	public ResponseEntity<Void> sendPushDoctor(@PathVariable(ID) long id,
			@RequestParam(TITLE) String title,
			@RequestParam(MESSAGE) String message) {
		if (doctorDetailRepo.findOne(id) == null) {
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		} else {
			Post2Gcm.post(Constants.APIKEY, Application.createContent(
					doctorDetailRepo.findOne(id).getRegId(), title, message));
			return new ResponseEntity<Void>(HttpStatus.OK);
		}
	}

	/**
	 * Receives POST requests to /addRegId and returns the patientDetail object
	 * that is just updated in the database. The @RequestParam annotation on the
	 * long id tell Spring to use the "id" parameter in the HTTP request's query
	 * string as the value for the id method parameter. The @RequestParam
	 * annotation on the String regId tell Spring to use the "regId" parameter
	 * in the HTTP request's query string as the value for the regId method
	 * parameter. The HttpServletResponse will contain the response of the
	 * request.The @ResponseBody annotation tells Spring to convert the return
	 * value from the method back into JSON and put it into the body of the HTTP
	 * response to the client. This method checks the PatientDetailRepository if
	 * a patientDetail entry with the given id exists. If yes then it adds the
	 * given regId with the setter setRegId and saves the new object to the
	 * database.
	 * 
	 */
	@RequestMapping(value = ADD_REGID, method = RequestMethod.POST)
	public @ResponseBody
	PatientDetail addRegIdToPatient(@RequestParam(ID) long id,
			@RequestParam(REGID_PARAMETER) String regId,
			HttpServletResponse response) {
		if (patientDetailRepo.findOne(id) == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
		} else {
			PatientDetail detail = patientDetailRepo.findOne(id);
			detail.setRegId(regId);
			patientDetailRepo.save(detail);
			response.setStatus(HttpServletResponse.SC_OK);
			return patientDetailRepo.findOne(id);
		}
	}
}
