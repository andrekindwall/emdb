package com.emjaay.mdb.communication;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;

import com.emjaay.mdb.R;
import com.emjaay.mdb.data.Copy;
import com.emjaay.mdb.database.DatabaseHelper;

public class LoginTask extends AbstractAsyncTask {

	public static final String EXTRA_EMAIL = "email";
	public static final String EXTRA_PASSWORD = "password";

	private Context context;
	private String email;
	private String password;
	
	public LoginTask(Context context, String email, String password, CommunicationCallback callback){
		this.context = context;
		this.email = email;
		this.password = password;
		this.callback = callback;
	}
	
	@Override
	protected Boolean doInBackground(Void... params) {
		String encodedEmail = urlEncode(email);
		String baseUrl = getBaseUrl(context);
		String url = baseUrl + String.format(context.getString(R.string.api_login), encodedEmail, password);
		
		contactServer(url);
		
		updateCopies();
		
		return true;
	}
	
	private void updateCopies() {
		if(result.isSuccess()){
			DatabaseHelper database = new DatabaseHelper(context);
			database.clearCopies();
			
			try {
				JSONObject jo = new JSONObject(result.getResponse());
				JSONArray ja = jo.getJSONArray("copies");
				ArrayList<Copy> copies = Copy.fromJson(ja);
				
				database.insertCopies(copies);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	protected void onPostExecute(final Boolean success) {
		if(callback != null){
			Bundle bundle = new Bundle();
			bundle.putString(EXTRA_EMAIL, email);
			bundle.putString(EXTRA_PASSWORD, password);
			result.setBundle(bundle);
			
			callback.taskComplete(this, result);
		}
	}

	@Override
	protected boolean isExternalApi() {
		return false;
	}
}