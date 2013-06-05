package com.emjaay.mdb.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.emjaay.mdb.R;
import com.emjaay.mdb.data.Movie;

public class MovieListAdapter extends BaseAdapter {
	
	private ArrayList<Movie> movies;
	private LayoutInflater inflater;
	
	class ViewHolder {
		public ImageView imageView;
		public TextView titleTextView;
		public TextView yearTextView;
    }
	
	public MovieListAdapter(Context context){
		inflater = LayoutInflater.from(context);
	}
	
	public void setItems(ArrayList<Movie> movies) {
		this.movies = movies;
	}
	
	@Override
	public int getCount() {
		return movies != null ? movies.size() : 0;
	}

	@Override
	public Movie getItem(int position) {
		return movies != null ? movies.get(position) : null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_movie_row, parent, false);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.image);
            holder.titleTextView = (TextView) convertView.findViewById(R.id.title);
            holder.yearTextView = (TextView) convertView.findViewById(R.id.year);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        
        Movie movie = movies.get(position);
        
//        holder.imageView.setImageDrawable(drawable);
        holder.titleTextView.setText(movie.getTitle());
        holder.yearTextView.setText(Integer.toString(movie.getYear()));
        
        return convertView;
	}

}
