package org.coursera.capstone.symptom.repository;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class CheckIn {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Lob
	@Column(name="RAW", length = 1024)
	private String raw;
	private long idPatient;
	private long time;

	public CheckIn() {
	}

	public CheckIn(String raw,
			long idPatient, long time) {
		super();
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
	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
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
