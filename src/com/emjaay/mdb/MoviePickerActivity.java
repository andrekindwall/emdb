package com.emjaay.mdb;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.emjaay.mdb.adapters.MoviePickerListAdapter;
import com.emjaay.mdb.data.Movie;
import com.emjaay.mdb.database.DatabaseHelper;

public class MoviePickerActivity extends AbstractFragmentActivity implements OnItemClickListener {

	private ArrayList<Movie> movies;
	private MoviePickerListAdapter adapter;
	
	// UI references.
	private ListView mListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.movie_picker_activity);

		movies = getIntent().getParcelableArrayListExtra(Movie.EXTRAS_KEY);

		adapter = new MoviePickerListAdapter(this);
		adapter.setItems(movies);

		mListView = (ListView) findViewById(R.id.listview);
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(this);

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

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Movie movie = movies.get(position);
		DatabaseHelper database = new DatabaseHelper(this);
		database.insertMovie(movie, false);
		
		Intent intent = new Intent(this, DetailedMovieActivity.class);
		intent.putExtra(DetailedMovieActivity.EXTRAS_IMDB_ID, movie.getImdbId());
		startActivity(intent);
	}
}