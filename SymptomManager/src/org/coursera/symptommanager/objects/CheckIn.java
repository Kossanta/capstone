package org.coursera.symptommanager.objects;

/**
 * 
 * This is the object that represents the check in (questionnaire) the patient
 * does and has the following parameters 
 * long id : 		which is the id of the check in 
 * String raw : 	which is a JSONArray that contaoins JSONObjects 
 * 					with the questions the patient had answered in this checkin 
 * String image : 	which is the image the patient uploaded of his condition this may be empty 
 * long idPatient : which is patient's id that answer the questions
 * 
 */

public class CheckIn {

	private long id;
	private String raw;
	private long idPatient;
	private long time;

	public CheckIn() {
	}

	public CheckIn(long id, String raw, long idPatient, long time) {
		super();
		this.id = id;
		this.raw = raw;
		this.idPatient = idPatient;
		this.time = time;
	}

	public String getRaw() {
		return raw;
	}

	public void setRaw(String raw) {
		this.raw = raw;
	}

	public long getIdPatient() {
		return idPatient;
	}

	public void setIdPatient(long idPatient) {
		this.idPatient = idPatient;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

}
