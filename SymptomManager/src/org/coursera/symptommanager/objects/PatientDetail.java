package org.coursera.symptommanager.objects;

/**
 * 
 * This is the object that represents the patient details and has the
 * following parameters 
 * long id : 			which is the id of the patient detail object 
 * String username : 	which is the username of the patient 
 * String name : which is the name of the patient 
 * String surname : 	which is the surname of the patient 
 * String email : 		which is the email of the patient 
 * String phone : 		which is the phone of the patient 
 * long dob : 			which is the date of birth in milliseconds of the patient
 * String gender : 		which is the gender of the patient 
 * long lastCheckIn : 	which is the time stamp of the last checkIn of the patient in milliseconds 
 * String notifyTime : 	which is a JSONArray with the time stamps of the time the patient
 * 					   	would like to be informed to use the application
 * long idDoctor : 		which is the id of the patient's doctor
 * String regId : 		which is the registration id of the device of the patient
 * long severPain : 	which is the time stamp of the time the patient started facing sever pain
 * long moderatePain : 	which is the time stamp of the time the patient started facing moderate pain
 * long cantEat : 		which is the time stamp of the time the patient started to feel like I can't eat
 * String uid : 		which is the unique medical record for the patient
 */

public class PatientDetail {

	private long id;
	private String username;
	private String name;
	private String surname;
	private String email;
	private String phone;
	private long dob;
	private String gender;
	private long lastCheckIn;
	private String notifyTime;
	private long idDoctor;
	private String regId;
	long severePain;
	long moderatePain;
	long cantEat;
	String uid;

	public PatientDetail() {
	}

	public PatientDetail(String username, String name, String surname,
			String email, String phone, long dob, String gender,
			long lastCheckIn, long idDoctor, String regId, long id,
			String notifyTime, long severePain, long moderatePain,
			long cantEat, String uid) {
		super();
		this.username = username;
		this.name = name;
		this.surname = surname;
		this.email = email;
		this.phone = phone;
		this.dob = dob;
		this.gender = gender;
		this.lastCheckIn = lastCheckIn;
		this.idDoctor = idDoctor;
		this.regId = regId;
		this.id = id;
		this.notifyTime = notifyTime;
		this.severePain = severePain;
		this.moderatePain = moderatePain;
		this.cantEat = cantEat;
		this.uid = uid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public long getDob() {
		return dob;
	}

	public void setDob(long dob) {
		this.dob = dob;
	}

	public String getRegId() {
		return regId;
	}

	public void setRegId(String regId) {
		this.regId = regId;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public long getLastCheckIn() {
		return lastCheckIn;
	}

	public void setLastCheckIn(long lastCheckIn) {
		this.lastCheckIn = lastCheckIn;
	}

	public long getIdDoctor() {
		return idDoctor;
	}

	public void setIdDoctor(long idDoctor) {
		this.idDoctor = idDoctor;
	}

	public String getNotifyTime() {
		return notifyTime;
	}

	public void setNotifyTime(String notifyTime) {
		this.notifyTime = notifyTime;
	}

	public long getSeverePain() {
		return severePain;
	}

	public void setSeverePain(long severePain) {
		this.severePain = severePain;
	}

	public long getModeratePain() {
		return moderatePain;
	}

	public void setModeratePain(long moderatePain) {
		this.moderatePain = moderatePain;
	}

	public long getCantEat() {
		return cantEat;
	}

	public void setCantEat(long cantEat) {
		this.cantEat = cantEat;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	//
	// /**
	// * Two Videos will generate the same hashcode if they have exactly the
	// same
	// * values for their name, url, and duration.
	// *
	// */
	// @Override
	// public int hashCode() {
	// // Google Guava provides great utilities for hashing
	// return Objects.hashCode(name, url, duration);
	// }
	//
	// /**
	// * Two Videos are considered equal if they have exactly the same values
	// for
	// * their name, url, and duration.
	// *
	// */
	// @Override
	// public boolean equals(Object obj) {
	// if (obj instanceof Video) {
	// Video other = (Video) obj;
	// // Google Guava provides great utilities for equals too!
	// return Objects.equal(name, other.name)
	// && Objects.equal(url, other.url)
	// && duration == other.duration;
	// } else {
	// return false;
	// }
	// }

}
