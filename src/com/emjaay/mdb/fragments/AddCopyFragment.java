package com.emjaay.mdb.fragments;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.emjaay.mdb.R;
import com.emjaay.mdb.data.Copy;
import com.emjaay.mdb.database.DatabaseHelper;

public class AddCopyFragment extends Fragment implements OnClickListener {

	public static final String EXTRAS_IMDB_ID = "imdbId";

	private static final int VIEW_ADD_COPY = 1;
	private static final int VIEW_SHOW_COPIES = 2;
	
	private String imdbId;
	private ArrayList<Copy> copies;

	private LinearLayout addCopyContainer;
	private RadioGroup mediaTypeRadioGroup;
	private LinearLayout showCopiesContainer;
	private TextView copiesView;
	
	public AddCopyFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.add_copy_fragment, container, false);
		
		addCopyContainer = (LinearLayout) rootView.findViewById(R.id.add_copy_container);
		mediaTypeRadioGroup = (RadioGroup) rootView.findViewById(R.id.media_type);
		showCopiesContainer = (LinearLayout) rootView.findViewById(R.id.show_copies_container);
		copiesView = (TextView) rootView.findViewById(R.id.copies);
		
		getExtras();
		
		getCopies();
		
		updateCopiesView();
		
		if(copies.size() > 0){
			showViewContainer(VIEW_SHOW_COPIES);
		} else {
			showViewContainer(VIEW_ADD_COPY);
		}

		rootView.findViewById(R.id.add_copy_button).setOnClickListener(this);
		rootView.findViewById(R.id.add_another_copy_button).setOnClickListener(this);
		
		return rootView;
	}
	
	private void showViewContainer(int view) {
		switch(view){
		case VIEW_ADD_COPY:
			addCopyContainer.setVisibility(View.VISIBLE);
			showCopiesContainer.setVisibility(View.GONE);
			break;
		case VIEW_SHOW_COPIES:
			addCopyContainer.setVisibility(View.GONE);
			showCopiesContainer.setVisibility(View.VISIBLE);
			break;
		}
	}
	
	private void getCopies() {
		DatabaseHelper database = new DatabaseHelper(getActivity());
		copies = database.getCopies(imdbId);
	}

	private void getExtras() {
		Bundle extras = getArguments();
		if(extras != null){
			imdbId = extras.getString(EXTRAS_IMDB_ID);
		}
	}
	
	public void addCopy(Copy copy) {
		DatabaseHelper database = new DatabaseHelper(getActivity());
		database.insertCopy(copy);
	}
	
	private void updateCopiesView() {
		copiesView.setText("You own " + copies.size() + " copy(ies)");
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.add_copy_button:
			Copy copy = new Copy();
			copy.setImdbId(imdbId);
			switch (mediaTypeRadioGroup.getCheckedRadioButtonId()) {
			case R.id.radio_vhs:
				copy.setMediaType(Copy.TYPE_VHS);
				break;
			case R.id.radio_dvd:
				copy.setMediaType(Copy.TYPE_DVD);
				break;
			case R.id.radio_bluray:
				copy.setMediaType(Copy.TYPE_BLURAY);
				break;
			case R.id.radio_digital:
				copy.setMediaType(Copy.TYPE_DIGITAL);
				break;
			}
			copies.add(copy);
			addCopy(copy);
			updateCopiesView();
			showViewContainer(VIEW_SHOW_COPIES);
			break;
		case R.id.add_another_copy_button:
			showViewContainer(VIEW_ADD_COPY);
			break;
		}
	}

}