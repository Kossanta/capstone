package org.coursera.capstone.symptom.repository;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Medication {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private String name;
	private String description;
	private long idPatient;

	public Medication() {
	}

	public Medication(String name, String description, long idPatient) {
		super();
		this.name = name;
		this.description = description;
		this.idPatient = idPatient;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesciption() {
		return description;
	}

	public void setDesciption(String description) {
		this.description = description;
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
