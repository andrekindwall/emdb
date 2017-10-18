package com.emjaay.mdb.communication.imdb;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;

import com.emjaay.mdb.R;
import com.emjaay.mdb.communication.AbstractAsyncTask;
import com.emjaay.mdb.data.Movie;
import com.emjaay.mdb.database.DatabaseHelper;

public class ImdbDetailedMovieTask extends AbstractAsyncTask {

	public static final String EXTRA_IMDB_ID = "imdbId";
	
	private Context context;
	
	private String imdbID;
	
	public ImdbDetailedMovieTask(Context context, String imdbID, CommunicationCallback callback) {
		this.context = context;
		this.imdbID = imdbID;
		this.callback = callback;
	}
	
	@Override
	protected Boolean doInBackground(Void... params) {
		String encodedImdbId = urlEncode(imdbID);
		
		String url = String.format(context.getString(R.string.api_imdb_detailed_movie), encodedImdbId);
		
		//TODO Use cached data
		contactServer(url);
		
		if(result.isSuccess()){			
			try {
				JSONObject jo = new JSONObject(result.getResponse());
				Movie movie = Movie.fromJson(jo, true);

				DatabaseHelper database = new DatabaseHelper(context);
				database.insertMovie(movie, true);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		return true;
	}
	
	@Override
	protected void onPostExecute(final Boolean success) {
		if(callback != null){
			Bundle bundle = new Bundle();
			bundle.putString(EXTRA_IMDB_ID, imdbID);
			result.setBundle(bundle);
			
			callback.taskComplete(this, result);
		}
	}

	@Override
	protected boolean isExternalApi() {
		return true;
	}

}
