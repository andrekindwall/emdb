package com.emjaay.mdb.fragments;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.emjaay.mdb.R;
import com.emjaay.mdb.adapters.MovieListAdapter;
import com.emjaay.mdb.data.Movie;
import com.emjaay.mdb.database.DatabaseHelper;

public class HomeFragment extends Fragment {
	
	public static final String EXTRAS_TYPE = "type";
	
	private ListView listView;
	private TextView responseTextView;

	private MovieListAdapter listAdapter;
	
	private int typeFilter;
	
	public HomeFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.home_fragment, container, false);
		listView = (ListView) rootView.findViewById(R.id.home_listview);
		responseTextView = (TextView) rootView.findViewById(R.id.home_response);
		
		typeFilter = getArguments().getInt(EXTRAS_TYPE);
		
		listAdapter = new MovieListAdapter(getActivity());
		listView.setAdapter(listAdapter);
		
		DatabaseHelper database = new DatabaseHelper(getActivity());
		
		ArrayList<Movie> movies = database.getMyMovies(typeFilter);
		listAdapter.setItems(movies);
		
		updateViewVisibilities();
		
		return rootView;
	}
	
	public void updateMovies() {
		if(listAdapter != null){
			DatabaseHelper database = new DatabaseHelper(getActivity());
			ArrayList<Movie> movies = database.getMyMovies(typeFilter);
			listAdapter.setItems(movies);
			
			updateViewVisibilities();
			
			listAdapter.notifyDataSetChanged();
		}
	}
	
	private void updateViewVisibilities() {
		if(listAdapter.getCount() == 0){
			responseTextView.setVisibility(View.VISIBLE);
			responseTextView.setText(R.string.no_movies);
			listView.setVisibility(View.GONE);
		} else {
			listView.setVisibility(View.VISIBLE);
			responseTextView.setVisibility(View.GONE);
		}
	}

}
