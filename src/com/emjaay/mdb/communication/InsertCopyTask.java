package com.emjaay.mdb.communication;

import android.content.Context;
import android.os.Bundle;

import com.emjaay.mdb.R;
import com.emjaay.mdb.data.Copy;
import com.emjaay.mdb.database.DatabaseHelper;
import com.emjaay.mdb.util.UserData;

public class InsertCopyTask extends AbstractAsyncTask {
	
	public static final String EXTRA_IMDBID = "imdbID";
	public static final String EXTRA_MEDIATYPE = "mediaType";
	public static final String EXTRA_ADDED = "added";

	private Context context;
	private Copy copy;
	
	public InsertCopyTask(Context context, Copy copy, CommunicationCallback callback){
		this.context = context;
		this.copy = copy;
		this.callback = callback;
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		String email = UserData.getEmail(context);
		String password = UserData.getPassword(context);
		String encodedEmail = urlEncode(email);
		
		String baseUrl = getBaseUrl(context);
		String url = baseUrl + String.format(context.getString(R.string.api_insertcopy), encodedEmail, password, copy.getImdbId(), copy.getMediaType(), copy.getAdded());
		
		contactServer(url);
		
		return true;
	}
	
	@Override
	protected void onPostExecute(final Boolean success) {
		if(result.isSuccess()){
			copy.setSynced(true);
			DatabaseHelper database = new DatabaseHelper(context);
			database.updateCopy(copy);
		}
		
		if(callback != null){
			Bundle bundle = new Bundle();
			bundle.putString(EXTRA_IMDBID, copy.getImdbId());
			bundle.putInt(EXTRA_MEDIATYPE, copy.getMediaType());
			bundle.putLong(EXTRA_ADDED, copy.getAdded());
			result.setBundle(bundle);
			
			callback.taskComplete(this, result);
		}
	}

	@Override
	protected boolean isExternalApi() {
		return false;
	}
}
