package com.emjaay.mdb.communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.emjaay.mdb.R;
import com.emjaay.mdb.communication.result.ApiResult;

public abstract class AbstractAsyncTask extends AsyncTask<Void, Void, Boolean> {
	
	protected CommunicationCallback callback;
	protected ApiResult result;
	
	public interface CommunicationCallback {
		public void taskComplete(AbstractAsyncTask task, ApiResult result);
		public void taskCancelled(AbstractAsyncTask task);
	}
	
	protected abstract boolean isExternalApi();
	
	protected String getBaseUrl(Context context) {
		return context.getString(R.string.baseUrl);
	}
	
	public void setCommunicationListener(CommunicationCallback callback) {
		this.callback = callback;
	}
	
	/**
	 * Contacting server and creates the ApiResult
	 * @param url
	 */
	protected void contactServer(String url) {
		result = new ApiResult();
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpget = new HttpGet(url);
			HttpResponse response = httpClient.execute(httpget);
			
			if(response.getStatusLine().getStatusCode() != 200){
				Log.w("emjaay", "HttpGet failed with status code " + response.getStatusLine().getStatusCode());
				result.setSuccess(false);
				result.setErrorCode(ApiResult.ERROR_SERVER);
				return;
			}
			
			BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			
		    String line;
		    if((line = br.readLine()) != null) {
	    		result.setResponse(line);
	    		
		    	if(isExternalApi()){
		    		result.setSuccess(true);
		    	} else {
		    		JSONObject json = new JSONObject(line);
		    		boolean success = json.getBoolean("success");
		    		
		    		result.setSuccess(success);
		    		
		    		if(!success){
		    			int errorCode = json.getInt("error_code");
		    			result.setErrorCode(errorCode);
		    		}
		    	}
		    	
		    }
		    br.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return;
	}

	@Override
	protected void onCancelled() {
		if(callback != null){
			callback.taskCancelled(this);
		}
	}
	
	public static String urlEncode(String input) {
		try {
			return URLEncoder.encode(input, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		}
	}
}