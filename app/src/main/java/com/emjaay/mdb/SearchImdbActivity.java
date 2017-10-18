package com.emjaay.mdb;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.emjaay.mdb.communication.AbstractAsyncTask;
import com.emjaay.mdb.communication.AbstractAsyncTask.CommunicationCallback;
import com.emjaay.mdb.communication.imdb.ImdbSearchTask;
import com.emjaay.mdb.communication.result.ApiResult;
import com.emjaay.mdb.data.Movie;

public class SearchImdbActivity extends AbstractFragmentActivity implements CommunicationCallback {
  
	private ImdbSearchTask searchTask = null;
	
	private String mTitle;
	private String mYear;
	
	private EditText mTitleView;
	private EditText mYearView;
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.search_imdb_activity);

	    mTitleView = (EditText) findViewById(R.id.title);
	    mYearView = (EditText) findViewById(R.id.year);

	    mYearView.setOnEditorActionListener(new OnEditorActionListener(){
			public boolean onEditorAction(TextView v, int id, KeyEvent event){
				if (id == R.id.search_imdb || id == EditorInfo.IME_NULL || id == EditorInfo.IME_ACTION_DONE){
					attemptSearch();
					return true;
				}
				return false;
			}
	    });
	    
	    findViewById(R.id.search_imdb_button).setOnClickListener(new OnClickListener(){
		    public void onClick(View v){
		    	attemptSearch();
		    }
	    });
	    getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void attemptSearch(){
	    if (this.searchTask != null){	    	
	    	return;
	    }
	    
	    // Reset errors.
	    mTitleView.setError(null);
	    
	    // Store values at the time of the search attempt
	    mTitle = mTitleView.getText().toString().trim();
	    mYear = mYearView.getText().toString().trim();
	    
	    boolean cancel = false;
	    View focusView = null;
	    
	    if(TextUtils.isEmpty(this.mTitle)){
			mTitleView.setError(getString(R.string.error_field_required));
			focusView = mTitleView;
			cancel = true;
	    }
	    if(TextUtils.isDigitsOnly(mYear) == false){
			mYearView.setError(getString(R.string.error_field_numbers_required));
			focusView = mYearView;
			cancel = true;
	    }
	    
	    if (cancel) {
	    	focusView.requestFocus();	    	
	    } else {
	    	showProgress(true);
		    searchTask = new ImdbSearchTask(this, mTitle, mYear, this);
		    searchTask.execute();
	    }
	}

	public void taskComplete(AbstractAsyncTask task, ApiResult result){
		this.searchTask = null;
		showProgress(false);
		
		if(result.isSuccess()){
			try{
				JSONObject jo = new JSONObject(result.getResponse());
				JSONArray ja = jo.getJSONArray("Search");
			    ArrayList<Movie> movies = Movie.fromJson(ja, false);
			    
			    Intent intent = new Intent(this, MoviePickerActivity.class);
			    intent.putParcelableArrayListExtra(Movie.EXTRAS_KEY, movies);
			    startActivity(intent);
			    return;
			} catch (JSONException e){
				e.printStackTrace();
		    }
		} else {
			switch(result.getErrorCode()){
			case ApiResult.ERROR_SERVER:
				Toast.makeText(this, R.string.could_not_communicate, Toast.LENGTH_SHORT).show();
				break;
			}
		}
  }
  
	public void taskCancelled(AbstractAsyncTask task){
		searchTask = null;
		showProgress(false);
	}
}