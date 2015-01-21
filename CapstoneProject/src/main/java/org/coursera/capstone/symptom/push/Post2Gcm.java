package org.coursera.capstone.symptom.push;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import com.google.gson.Gson;

/**
 * 
 * This class will send the POST request. Here we need to pass the following:
 * 
 * Server API Key: this key will be added to POST headers. Content object which
 * will be converted in JSON format using Jackson library
 * 
 */
public class Post2Gcm {

	public static void post(String apiKey, Content content) {

		try {

			Gson gson = new Gson();
			String json = gson.toJson(content);
			HttpPost httpPost = new HttpPost(
					"https://android.googleapis.com/gcm/send");
			httpPost.setHeader("Authorization", "key=" + apiKey);
			StringEntity entity = new StringEntity(json, HTTP.UTF_8);
			entity.setContentType("application/json");
			httpPost.setEntity(entity);

			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(httpPost);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent(), "UTF-8"));
			String json1 = reader.readLine();
			String json2 = reader.readLine();

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}