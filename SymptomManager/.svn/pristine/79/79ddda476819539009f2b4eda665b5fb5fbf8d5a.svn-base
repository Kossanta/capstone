package org.coursera.symptommanager;

import org.coursera.symptommanager.oauth.SecuredRestBuilder;
import org.coursera.symptommanager.unsafe.EasyHttpClient;

import retrofit.client.ApacheClient;
import android.content.Context;
import android.content.Intent;


public class SymptomSvc {

	public static final String CLIENT_ID = "mobile";

	private static SymptomSvcApi symptomSvc;

	public static synchronized SymptomSvcApi getOrShowLogin(Context ctx) {
		if (symptomSvc != null) {
			return symptomSvc;
		} else {
			Intent i = new Intent(ctx, LoginScreenActivity.class);
			ctx.startActivity(i);
			return null;
		}
	}

	public static synchronized SymptomSvcApi init(String server, String user,
			String pass) {

		symptomSvc = new SecuredRestBuilder()
				.setLoginEndpoint(server + SymptomSvcApi.TOKEN_PATH)
				.setUsername(user)
				.setPassword(pass)
				.setClientId(CLIENT_ID)
				.setClient(
						new ApacheClient(new EasyHttpClient()))
				.setEndpoint(server)
				//.setLogLevel(LogLevel.FULL)
				.build()
				.create(SymptomSvcApi.class);

		return symptomSvc;
	}
}
