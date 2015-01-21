package org.coursera.symptommanager.objects;


/**
 * 
 * This is the object that represents the medication and has the
 * following parameters 
 * long id : 				which is the id of the medication object
 * String name : 			which is the name of the medication 
 * String description : 	which is the description of the medication 
 * long idPatient :			which is the id of the patient that has this medication
  */
public class Medication {

	private long id;
	private String name;
	private String description;
	private long idPatient;

	public Medication() {
	}

	public Medication(long id, String name, String description, long idPatient) {
		super();
		this.id = id;
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
