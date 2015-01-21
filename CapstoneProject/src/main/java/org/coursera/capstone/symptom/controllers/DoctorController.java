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
 * The controller which handles ONLY the doctor requests.
 * 
 */
// Tell Spring that this class is a Controller that should
// handle the doctor HTTP requests(This separation
// happens because we added the @RequestMapping annotation with the /doctor)
// for the DispatcherServlet
@Controller
@RequestMapping(Constants.DOCTOR_SVC_PATH)
public class DoctorController {

	// the path for get doctor profile with the username path parameter
	final String GET_DOCTOR_PROFILE = "/{username}/getDoctorProfile";
	// the path for update doctor profile
	final String UPDATE_DOCTOR_PROFILE = "/updateDoctorProfile";
	// the path for get create doctor profile
	final String CREATE_DOCTOR_PROFILE = "/createDoctorProfile";
	// the path for get patient list with the id path parameter
	final String GET_PATIENT_LIST = "/{id}/getPatientList";
	// the path for update profile patient with the id and id2 path parameters
	final String UDATE_PATIENT_PROFILE = "/{id}/updateProfilePatient/{id2}";
	// the path for remove patient with the id path parameter
	final String REMOVE_PATIENT = "/removePatient/{id}";
	// the path for get available patients
	final String GET_AVAILABLE_PATIENTS = "/getAvailablePatients";
	// the path for get checkins with the id path parameter
	final String GET_PATIENTS_CHECKINS = "/getCheckins/{id}";
	// the path for get patient profile with the id path parameter
	final String GET_PATIENT_PROFILE = "/getPatientProfile/{id}";
	// the path for add medication
	final String ADD_MEDICATION = "/addMedication";
	// the path for remove medication with the id path parameter
	final String REMOVE_MEDICATION = "/removeMedication/{id}";
	// the path for sending push notification to patient with the id path
	// parameter
	final String SEND_PUSH_2_PATIENT = "/sendPush/{id}";
	// the register id parameter
	final String REGID_PARAMETER = "regId";
	// the id parameter
	final String ID = "id";
	// the id2 parameter
	final String ID2 = "id2";
	// the name parameter
	final String NAME = "name";
	// the title parameter
	final String TITLE = "title";
	// the message parameter
	final String MESSAGE = "message";
	// the description parameter
	final String DESCRIPTION = "description";
	// the id patient parameter
	final String ID_PATIENT = "idPatient";
	// the path for population of the table doctor's details with all the
	// available
	// users which are hard coded
	final String POPULATE_DOCTOR_DETAILS_TABLE = "/populateDoctors";
	// the path for searching patient by name with the name path parameter
	final String FIND_PATIENT_BY_NAME = "/searchByName/{name}";
	// the path for searching patient by surname with the name path parameter
	final String FIND_PATIENT_BY_SURNAME = "/searchBySurname/{name}";
	// The path to add the registration id of the device for the push
	// notification to the doctor account
	final String ADD_REGID = "/addRegId";

	// The CheckInRepository that we are going to store our checkins
	// in. We don't explicitly construct a CheckInRepository, but
	// instead mark this object as a dependency that needs to be
	// injected by Spring.
	//
	@Autowired
	private CheckInRepository checkInRepo;

	// The DoctorDetailRepository that we are going to store our doctorDetails
	// in. We don't explicitly construct a DoctorDetailRepository, but
	// instead mark this object as a dependency that needs to be
	// injected by Spring.
	//
	@Autowired
	private DoctorDetailRepository doctorDetailRepo;

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

	/**
	 * Receives GET requests to /{username}/getDoctorProfile and returns the
	 * current doctorDetail object that is saved in the database with the given
	 * username. The @PathVariable annotation tell Spring to get the "username"
	 * value from the path and add it to the username method parameter. The
	 * HttpServletResponse will contain the response of the request. The @ResponseBody
	 * annotation tells Spring to convert the return value from the method back
	 * into JSON and put it into the body of the HTTP response to the client.
	 */
	@RequestMapping(value = GET_DOCTOR_PROFILE, method = RequestMethod.GET)
	public @ResponseBody
	DoctorDetail getProfileDoctor(@PathVariable("username") String username,
			HttpServletResponse response) throws IOException {
		if (doctorDetailRepo.findByUsername(username) == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
		} else {
			response.setStatus(HttpServletResponse.SC_OK);
			return doctorDetailRepo.findByUsername(username);
		}
	}

	/**
	 * Receives POST requests to /createDoctorProfile and returns the
	 * doctorDetail object that is just saved in the database. The @RequestBody
	 * annotation on the details parameter is what tells Spring to interpret the
	 * HTTP request body as JSON and convert it into a PatientDetail object to
	 * pass into the method. The HttpServletResponse will contain the response
	 * of the request. The @ResponseBody annotation tells Spring to convert the
	 * return value from the method back into JSON and put it into the body of
	 * the HTTP response to the client.
	 */
	@RequestMapping(value = CREATE_DOCTOR_PROFILE, method = RequestMethod.POST)
	public @ResponseBody
	DoctorDetail createProfileDoctor(@RequestBody DoctorDetail details,
			HttpServletResponse response) throws IOException {
		doctorDetailRepo.save(details);
		return doctorDetailRepo.findByUsername(details.getUsername());
	}

	/**
	 * Receives POST requests to /populateDoctors and returns void. The @RequestBody
	 * annotation on the list parameter is what tells Spring to interpret the
	 * HTTP request body as JSON and convert it into an ArrayList<DoctorDetail>
	 * array to pass into the method. The HttpServletResponse will contain the
	 * response of the request. If the first hard coded patient doesn't exist in
	 * the repository then for all the objects in the list each object will be
	 * saved in the repository. The request returns void. The reason for this
	 * request is to hard code the doctor's details into the repository
	 */
	@RequestMapping(value = POPULATE_DOCTOR_DETAILS_TABLE, method = RequestMethod.POST)
	public ResponseEntity<Void> populateDoctorsProfiles(
			@RequestBody ArrayList<DoctorDetail> list,
			HttpServletResponse response) {
		if (doctorDetailRepo.findByUsername("drJohn") == null) {
			for (int i = 0; i < list.size(); i++) {
				doctorDetailRepo.save(list.get(i));
			}
		} else {

		}
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	/**
	 * Receives POST requests to /updateDoctorProfile and returns the
	 * doctorDetail object that is just updated in the database. The @RequestBody
	 * annotation on the details parameter is what tells Spring to interpret the
	 * HTTP request body as JSON and convert it into a DoctorDetail object to
	 * pass into the method. The HttpServletResponse will contain the response
	 * of the request. The @ResponseBody annotation tells Spring to convert the
	 * return value from the method back into JSON and put it into the body of
	 * the HTTP response to the client.
	 */
	@RequestMapping(value = UPDATE_DOCTOR_PROFILE, method = RequestMethod.POST)
	public @ResponseBody
	DoctorDetail updateProfileDoctor(@RequestBody DoctorDetail details,
			HttpServletResponse response) throws IOException {
		doctorDetailRepo.save(details);
		return doctorDetailRepo.findByUsername(details.getUsername());
	}

	/**
	 * Receives GET requests to /{id}/getPatientList and returns the array with
	 * the patientProfiles that are saved in the database with the given doctor
	 * id. The @PathVariable annotation tell Spring to get the "id" value from
	 * the path and add it to the id method parameter. The HttpServletResponse
	 * will contain the response of the request. The @ResponseBody annotation
	 * tells Spring to convert the return value from the method back into JSON
	 * and put it into the body of the HTTP response to the client.
	 */
	@RequestMapping(value = GET_PATIENT_LIST, method = RequestMethod.GET)
	public @ResponseBody
	ArrayList<PatientDetail> getPatientList(@PathVariable(ID) long id,
			HttpServletResponse response) throws IOException {
		response.setStatus(HttpServletResponse.SC_OK);
		return patientDetailRepo.findByIdDoctor(id);

	}

	/**
	 * Receives GET requests to /{id}/updateProfilePatient/{id2} and returns
	 * void. The @PathVariable annotation tell Spring to get the "id" value from
	 * the path and add it to the idDoctor method parameter. The @PathVariable
	 * annotation tell Spring to get the "id2" value from the path and add it to
	 * the idPatient method parameter. This request gets the patientDetail
	 * object from the database with the given idPatient and change the idDoctor
	 * value with the idDoctor method parameter via the setIdDoctor setter of
	 * the object
	 */
	@RequestMapping(value = UDATE_PATIENT_PROFILE, method = RequestMethod.GET)
	public ResponseEntity<Void> addPatient(@PathVariable(ID) long idDoctor,
			@PathVariable(ID2) long idPatient) throws IOException {
		PatientDetail profile = patientDetailRepo.findOne(idPatient);
		profile.setIdDoctor(idDoctor);
		patientDetailRepo.save(profile);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	/**
	 * Receives GET requests to /removePatient/{id} and returns void. The @PathVariable
	 * annotation tell Spring to get the "id" value from the path and add it to
	 * the id method parameter. This request finds the patientDetail from the
	 * database from the idPatient method parameter and sets his/hers idDoctor
	 * to -1 via the setter method setIdDoctor of the object
	 */
	@RequestMapping(value = REMOVE_PATIENT, method = RequestMethod.GET)
	public ResponseEntity<Void> removePatient(@PathVariable(ID) long idPatient)
			throws IOException {
		PatientDetail profile = patientDetailRepo.findOne(idPatient);
		profile.setIdDoctor(-1);
		patientDetailRepo.save(profile);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	/**
	 * Receives GET requests to /getAvailablePatients and returns the array with
	 * the patientProfiles that are saved in the database with id doctor equals
	 * to -1. The @ResponseBody annotation tells Spring to convert the return
	 * value from the method back into JSON and put it into the body of the HTTP
	 * response to the client.
	 */
	@RequestMapping(value = GET_AVAILABLE_PATIENTS, method = RequestMethod.GET)
	public @ResponseBody
	ArrayList<PatientDetail> getAvailablePatients(HttpServletResponse response)
			throws IOException {
		if (patientDetailRepo.findByIdDoctor(-1) == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
		} else {
			return patientDetailRepo.findByIdDoctor(-1);
		}

	}

	/**
	 * Receives GET requests to /getCheckins/{id} and returns the array with the
	 * checkins that are saved in the database with id equals with the given id
	 * mehtod parameter. The @PathVariable annotation tell Spring to get the
	 * "id" value from the path and add it to the id method parameter. The @ResponseBody
	 * annotation tells Spring to convert the return value from the method back
	 * into JSON and put it into the body of the HTTP response to the client.
	 */
	@RequestMapping(value = GET_PATIENTS_CHECKINS, method = RequestMethod.GET)
	public @ResponseBody
	ArrayList<CheckIn> getCheckins(@PathVariable(ID) long id,
			HttpServletResponse response) throws IOException {
		if (checkInRepo.findByIdPatient(id) == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
		} else {
			return checkInRepo.findByIdPatient(id);
		}

	}

	/**
	 * Receives GET requests to /getPatientProfile/{id} and returns the patient
	 * profile that is saved in the database with id equals with the given id
	 * method parameter. The @PathVariable annotation tell Spring to get the
	 * "id" value from the path and add it to the id method parameter. The @ResponseBody
	 * annotation tells Spring to convert the return value from the method back
	 * into JSON and put it into the body of the HTTP response to the client.
	 */
	@RequestMapping(value = GET_PATIENT_PROFILE, method = RequestMethod.GET)
	public @ResponseBody
	PatientDetail getPatientProfile(@PathVariable(ID) long id,
			HttpServletResponse response) throws IOException {
		if (patientDetailRepo.findOne(id) == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
		} else {
			response.setStatus(HttpServletResponse.SC_OK);
			return patientDetailRepo.findOne(id);
		}
	}

	/**
	 * Receives POST requests to /addMedication. The @RequestParam annotation on
	 * the String name tell Spring to use the "name" parameter in the HTTP
	 * request's query string as the value for the name method parameter. The @RequestParam
	 * annotation on the String description tell Spring to use the "description"
	 * parameter in the HTTP request's query string as the value for the
	 * description method parameter. The @RequestParam annotation on the long
	 * idPatient tell Spring to use the "idPatient" parameter in the HTTP
	 * request's query string as the value for the idPatient method parameter.
	 * The @RequestParam annotation on the long id tell Spring to use the "id"
	 * parameter in the HTTP request's query string as the value for the id
	 * method parameter. The HttpServletResponse will contain the response of
	 * the request. The request returns void. This method gets all the
	 * medication objects that are stored in the MedicationRepository with the
	 * given idPatient, and checks if the medication already exist and if not
	 * add this medication to the database
	 * 
	 */
	@RequestMapping(value = ADD_MEDICATION, method = RequestMethod.POST)
	public @ResponseBody
	ResponseEntity<Void> addMedication(@RequestParam(ID) long id,
			@RequestParam(NAME) String name,
			@RequestParam(DESCRIPTION) String description,
			@RequestParam(ID_PATIENT) long idPatient) {
		ArrayList<Medication> pills;
		pills = pillRepo.findByIdPatient(id);
		for (int i = 0; i < pills.size(); i++) {
			if (name.equalsIgnoreCase(pills.get(i).getName())) {

				return new ResponseEntity<Void>(HttpStatus.CONFLICT);

			}
		}
		pillRepo.save(new Medication(name, description, idPatient));
		return new ResponseEntity<Void>(HttpStatus.OK);

	}

	/**
	 * Receives GET requests to /removeMedication/{id} and returns void. The @PathVariable
	 * annotation tell Spring to get the "id" value from the path and add it to
	 * the id method parameter. The @ResponseBody annotation tells Spring to
	 * convert the return value from the method back into JSON and put it into
	 * the body of the HTTP response to the client.
	 */
	@RequestMapping(value = REMOVE_MEDICATION, method = RequestMethod.POST)
	public @ResponseBody
	ResponseEntity<Void> removeMedication(@PathVariable(ID) long id)
			throws IOException {
		if (pillRepo.findById(id) == null) {

			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		} else {
			pillRepo.delete(pillRepo.findById(id));
			return new ResponseEntity<Void>(HttpStatus.OK);
		}
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
	 * returns void. This method checks the PatientDetailRepository if a
	 * patientDetail entry with the given id exists. If yes then it triggers the
	 * Post2Gcm method to send a push notification to the patient.
	 * 
	 */
	@RequestMapping(value = SEND_PUSH_2_PATIENT, method = RequestMethod.POST)
	public ResponseEntity<Void> sendPushPatient(@PathVariable(ID) long id,
			@RequestParam(TITLE) String title,
			@RequestParam(MESSAGE) String message) {
		if (patientDetailRepo.findOne(id) == null) {
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		} else {
			Post2Gcm.post(Constants.APIKEY, Application.createContent(
					patientDetailRepo.findOne(id).getRegId(), title, message));
			return new ResponseEntity<Void>(HttpStatus.OK);
		}
	}

	/**
	 * Receives POST requests to /addRegId and returns the doctorDetail object
	 * that is just updated in the database. The @RequestParam annotation on the
	 * long id tell Spring to use the "id" parameter in the HTTP request's query
	 * string as the value for the id method parameter. The @RequestParam
	 * annotation on the String regId tell Spring to use the "regId" parameter
	 * in the HTTP request's query string as the value for the regId method
	 * parameter. The HttpServletResponse will contain the response of the
	 * request.The @ResponseBody annotation tells Spring to convert the return
	 * value from the method back into JSON and put it into the body of the HTTP
	 * response to the client. This method checks the DoctorDetailRepository if
	 * a doctorDetail entry with the given id exists. If yes then it adds the
	 * given regId with the setter setRegId and saves the new object to the
	 * database.
	 * 
	 */
	@RequestMapping(value = ADD_REGID, method = RequestMethod.POST)
	public @ResponseBody
	DoctorDetail addRegIdToDactor(@RequestParam(ID) long id,
			@RequestParam(REGID_PARAMETER) String regId,
			HttpServletResponse response) {
		if (doctorDetailRepo.findOne(id) == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
		} else {
			DoctorDetail detail = doctorDetailRepo.findOne(id);
			detail.setRegId(regId);
			doctorDetailRepo.save(detail);
			response.setStatus(HttpServletResponse.SC_OK);
			return doctorDetailRepo.findOne(id);
		}
	}

	/**
	 * Receives GET requests to /searchByName/{name} and returns an array with
	 * the patient profiles that are saved in the database with name containing
	 * the given name method parameter. The @PathVariable annotation tell Spring
	 * to get the "name" value from the path and add it to the name method
	 * parameter. The @ResponseBody annotation tells Spring to convert the
	 * return value from the method back into JSON and put it into the body of
	 * the HTTP response to the client.
	 */
	@RequestMapping(value = FIND_PATIENT_BY_NAME, method = RequestMethod.GET)
	public @ResponseBody
	ArrayList<PatientDetail> findByNameContaining(
			@PathVariable(NAME) String name) {
		return patientDetailRepo.findByNameContaining(name);
	}

	/**
	 * Receives GET requests to /searchBySurname/{name} and returns an array
	 * with the patient profiles that are saved in the database with surname
	 * containing the given name method parameter. The @PathVariable annotation
	 * tell Spring to get the "name" value from the path and add it to the name
	 * method parameter. The @ResponseBody annotation tells Spring to convert
	 * the return value from the method back into JSON and put it into the body
	 * of the HTTP response to the client.
	 */
	@RequestMapping(value = FIND_PATIENT_BY_SURNAME, method = RequestMethod.GET)
	public @ResponseBody
	ArrayList<PatientDetail> findBySurnameContaining(
			@PathVariable(NAME) String name) {
		return patientDetailRepo.findBySurnameContaining(name);
	}

}
