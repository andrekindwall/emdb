package com.emjaay.mdb.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.emjaay.mdb.R;
import com.emjaay.mdb.data.Movie;
import com.emjaay.mdb.database.DatabaseHelper;

public class DetailedMovieFragment extends Fragment {

	public static final String EXTRAS_IMDB_ID = "imdbId";
	
	private Movie movie;

	private TextView titleView;
	private TextView yearView;
	private TextView ratedView;
	private TextView releasedView;
	private TextView runtimeView;
	private TextView genreView;
	private TextView directorView;
	private TextView writerView;
	private TextView actorsView;
	private TextView plotView;
	private TextView posterView;
	private TextView ratingView;
	private TextView votesView;
	private TextView typeView;
	
	public DetailedMovieFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.detailed_movie_fragment, container, false);
		
		titleView = (TextView) rootView.findViewById(R.id.title);
		yearView = (TextView) rootView.findViewById(R.id.year);
		ratedView = (TextView) rootView.findViewById(R.id.rated);
		releasedView = (TextView) rootView.findViewById(R.id.released);
		runtimeView = (TextView) rootView.findViewById(R.id.runtime);
		genreView = (TextView) rootView.findViewById(R.id.genre);
		directorView = (TextView) rootView.findViewById(R.id.director);
		writerView = (TextView) rootView.findViewById(R.id.writer);
		actorsView = (TextView) rootView.findViewById(R.id.actors);
		plotView = (TextView) rootView.findViewById(R.id.plot);
		posterView = (TextView) rootView.findViewById(R.id.poster);
		ratingView = (TextView) rootView.findViewById(R.id.rating);
		votesView = (TextView) rootView.findViewById(R.id.votes);
		typeView = (TextView) rootView.findViewById(R.id.type);
		
		getExtras();
		
		return rootView;
	}
	
	private void getExtras() {
		Bundle extras = getArguments();
		if(extras != null){
			String imdbId = extras.getString(EXTRAS_IMDB_ID);
			if(imdbId != null){
				DatabaseHelper database = new DatabaseHelper(getActivity());
				setMovie(database.getMovie(imdbId));
			}
		}
	}
	
	public void setMovie(Movie movie) {
		this.movie = movie;
		
		updateView();
	}
	
	private void updateView() {
		if(movie != null){
			titleView.setText(movie.getTitle());
			yearView.setText(Integer.toString(movie.getYear()));
			ratedView.setText(movie.getRated());
			releasedView.setText(movie.getReleased());
			runtimeView.setText(movie.getRuntime());
			genreView.setText(movie.getGenre());
			directorView.setText(movie.getDirector());
			writerView.setText(movie.getWriter());
			actorsView.setText(movie.getActors());
			plotView.setText(movie.getPlot());
			posterView.setText(movie.getPoster());
			ratingView.setText(Float.toString(movie.getRating()));
			votesView.setText(Integer.toString(movie.getVotes()));
			typeView.setText(movie.getTypeName(getActivity()));
		}
	}

}