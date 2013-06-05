package com.emjaay.mdb;

import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

import com.emjaay.mdb.adapters.AbstractFragmentPagerAdapter;
import com.emjaay.mdb.communication.AbstractAsyncTask;
import com.emjaay.mdb.communication.AbstractAsyncTask.CommunicationCallback;
import com.emjaay.mdb.communication.imdb.ImdbDetailedMovieTask;
import com.emjaay.mdb.communication.result.ApiResult;
import com.emjaay.mdb.data.Movie;
import com.emjaay.mdb.database.DatabaseHelper;
import com.emjaay.mdb.fragments.AddCopyFragment;
import com.emjaay.mdb.fragments.DetailedMovieFragment;

public class DetailedMovieActivity extends AbstractFragmentActivity implements CommunicationCallback {
	
	public static final String EXTRAS_IMDB_ID = "imdbId";
	
	private ImdbDetailedMovieTask movieTask;
	
	private DetailedMoviePagerAdapter mPagerAdapter;
	private ViewPager mViewPager;
	
	private String imdbId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detailed_movie_activity);
	
		mPagerAdapter = new DetailedMoviePagerAdapter(getSupportFragmentManager());
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mPagerAdapter);
		
		imdbId = getIntent().getStringExtra(EXTRAS_IMDB_ID);
		
		showProgress(true);
		movieTask = new ImdbDetailedMovieTask(this, imdbId, this);
		movieTask.execute((Void) null);
		
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
	
	public class DetailedMoviePagerAdapter extends AbstractFragmentPagerAdapter {

		public DetailedMoviePagerAdapter(FragmentManager fm) {
			super(fm);
		}
		
		@Override
		public Fragment getItem(int position) {
			Fragment fragment;
			Bundle args;
			switch(position){
			case 0:
				fragment = new DetailedMovieFragment();
				args = new Bundle();
				args.putString(DetailedMovieFragment.EXTRAS_IMDB_ID, imdbId);
				fragment.setArguments(args);
				return fragment;
			case 1:
				fragment = new AddCopyFragment();
				args = new Bundle();
				args.putString(DetailedMovieFragment.EXTRAS_IMDB_ID, imdbId);
				fragment.setArguments(args);
				return fragment;
			}
			return null;
		}

		@Override
		public int getCount() {
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale locale = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.fragment_title_detailed_movie).toUpperCase(locale);
			case 1:
				return getString(R.string.fragment_title_copies).toUpperCase(locale);
			}
			return null;
		}
		
	}
	
	@Override
	public void taskComplete(AbstractAsyncTask task, ApiResult result) {
		movieTask = null;
		showProgress(false);
		
		if(result.isSuccess()){			
			try {
				JSONObject jo = new JSONObject(result.getResponse());
				Movie movie = Movie.fromJson(jo, true);
				DetailedMovieFragment fragment = (DetailedMovieFragment) mPagerAdapter.getActiveFragment(mViewPager, 0);
				if(fragment != null){
					fragment.setMovie(movie);
				}
				
				DatabaseHelper database = new DatabaseHelper(this);
				database.insertMovie(movie, true);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void taskCancelled(AbstractAsyncTask task) {
		movieTask = null;
		showProgress(false);
	}
	
}