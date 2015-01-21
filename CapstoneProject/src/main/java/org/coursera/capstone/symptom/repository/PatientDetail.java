package org.coursera.capstone.symptom.repository;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class PatientDetail {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private String username;
	private String name;
	private String surname;
	private String email;
	private String phone;
	private long dob;
	private String gender;
	private long lastCheckIn;
	@Lob
	@Column(name="NOTIFYTIME", length = 512)
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
			long lastCheckIn, String notifyTime, long idDoctor, String regId,
			long severePain, long moderatePain, long cantEat, String uid) {
		super();
		this.username = username;
		this.name = name;
		this.surname = surname;
		this.email = email;
		this.phone = phone;
		this.dob = dob;
		this.gender = gender;
		this.lastCheckIn = lastCheckIn;
		this.notifyTime = notifyTime;
		this.idDoctor = idDoctor;
		this.regId = regId;
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

	public String getNotifyTime() {
		return notifyTime;
	}

	public void setNotifyTime(String notifyTime) {
		this.notifyTime = notifyTime;
	}

	public long getIdDoctor() {
		return idDoctor;
	}

	public void setIdDoctor(long idDoctor) {
		this.idDoctor = idDoctor;
	}

	public String getRegId() {
		return regId;
	}

	public void setRegId(String regId) {
		this.regId = regId;
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

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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
