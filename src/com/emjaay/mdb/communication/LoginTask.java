package com.emjaay.mdb.communication;

import android.content.Context;
import android.os.Bundle;

import com.emjaay.mdb.R;

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
		
		return true;
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