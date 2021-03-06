package org.coursera.symptommanager.objects;

/**
 * 
 * This is the object that represents the account and has the following
 * parameters 
 * long id : which is the id of the account 
 * String username : which is the username of the account 
 * String password : which is the password of the account 
 * String role : which is the role of the account doctor or patient
 * 
 */
public class Account {

	private long id;
	private String username;
	private String password;
	private String regId;
	private String role;

	public Account() {
	}

	public Account(long id, String username, String password, String regId,
			String role) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.regId = regId;
		this.role = role;
	}

	// getters and setters for the parameters
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getRegId() {
		return regId;
	}

	public void setRegId(String regId) {
		this.regId = regId;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
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
