package com.emjaay.mdb.communication.imdb;

import android.content.Context;
import android.os.Bundle;

import com.emjaay.mdb.R;
import com.emjaay.mdb.communication.AbstractAsyncTask;

public class ImdbSearchTask extends AbstractAsyncTask {

	public static final String EXTRA_TITLE = "title";
	public static final String EXTRA_YEAR = "year";
	
	private Context context;
	
	private String title;
	private String year;
	
	public ImdbSearchTask(Context context, CommunicationCallback callback) {
		this.context = context;
		this.callback = callback;
	}
	
	public ImdbSearchTask(Context context, String title, String year, CommunicationCallback callback) {
		this.context = context;
		this.title = title;
		this.year = year;
		this.callback = callback;
	}
	
	public ImdbSearchTask setTitle(String title) {
		this.title = title;
		return this;
	}
	
	public ImdbSearchTask setYear(String year) {
		this.year = year;
		return this;
	}
	
	@Override
	protected Boolean doInBackground(Void... params) {
		String encodedTitle = urlEncode(title);
		
		String url = String.format(context.getString(R.string.api_imdb_search), encodedTitle, year);
		
		contactServer(url);
		
		return true;
	}
	
	@Override
	protected void onPostExecute(final Boolean success) {
		if(callback != null){
			Bundle bundle = new Bundle();
			bundle.putString(EXTRA_TITLE, title);
			bundle.putString(EXTRA_YEAR, year);
			result.setBundle(bundle);
			
			callback.taskComplete(this, result);
		}
	}

	@Override
	protected boolean isExternalApi() {
		return true;
	}

}
