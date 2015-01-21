package org.coursera.capstone.symptom.push;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 
 * This is a simple POJO to hold POST body content “will be converted to JSON
 * when sending the request”. Here we need to pass the following:
 * RegIds: One or more RegId can be added title & message: Two strings which
 * will form the data part of the content.
 * 
 */
public class Content implements Serializable {

	private List<String> registration_ids;
	private Map<String, String> data;

	public void addRegId(String regId) {
		if (registration_ids == null)
			registration_ids = new LinkedList<String>();
		registration_ids.add(regId);
	}

	public void createData(String title, String message) {
		if (data == null)
			data = new HashMap<String, String>();

		data.put("title", title);
		data.put("message", message);
	}

}