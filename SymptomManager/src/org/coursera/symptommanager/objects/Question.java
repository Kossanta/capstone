package org.coursera.symptommanager.objects;

/**
 * 
 * This is the object that represents the questions the patient answers and has the following parameters 
 * long id : which is the id of the account 
 * String title : which is the title of the question 
 * String response : which is the response of the question 
 * long timestamp : which is the the time the patient finishes the check in (questionnaire) in mills
 * 
 */
public class Question {

	private long id;
	private String title;
	private String response;
	private long timestamp;

	public Question() {
	}

	public Question(long id, String title, String response, long timestamp) {
		super();
		this.id = id;
		this.title = title;
		this.response = response;
		this.timestamp = timestamp;
	}

	// getters and setters for the parameters
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

}
