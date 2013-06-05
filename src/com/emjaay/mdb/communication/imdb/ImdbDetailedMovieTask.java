package com.emjaay.mdb.communication.imdb;

import android.content.Context;
import android.os.Bundle;

import com.emjaay.mdb.R;
import com.emjaay.mdb.communication.AbstractAsyncTask;

public class ImdbDetailedMovieTask extends AbstractAsyncTask {

	public static final String EXTRA_IMDB_ID = "imdbId";
	
	private Context context;
	
	private String imdbId;
	
	public ImdbDetailedMovieTask(Context context, String imdbId, CommunicationCallback callback) {
		this.context = context;
		this.imdbId = imdbId;
		this.callback = callback;
	}
	
	@Override
	protected Boolean doInBackground(Void... params) {
		String encodedImdbId = urlEncode(imdbId);
		
		String url = String.format(context.getString(R.string.api_imdb_detailed_movie), encodedImdbId);
		
		//TODO Use cached data
		contactServer(url);
		
		return true;
	}
	
	@Override
	protected void onPostExecute(final Boolean success) {
		if(callback != null){
			Bundle bundle = new Bundle();
			bundle.putString(EXTRA_IMDB_ID, imdbId);
			result.setBundle(bundle);
			
			callback.taskComplete(this, result);
		}
	}

	@Override
	protected boolean isExternalApi() {
		return true;
	}

}
