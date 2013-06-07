package com.emjaay.mdb.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.emjaay.mdb.R;
import com.emjaay.mdb.data.Movie;
import com.emjaay.mdb.views.AsyncImageView;

public class MoviePickerListAdapter extends BaseAdapter {
	
	private ArrayList<Movie> movies;
	private Context context;
	private LayoutInflater inflater;
	
	class ViewHolder {
		public AsyncImageView imageView;
		public TextView titleTextView;
		public TextView yearTextView;
		public TextView typeTextView;
    }
	
	public MoviePickerListAdapter(Context context){
		this.context = context;
		this.inflater = LayoutInflater.from(context);
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
            convertView = inflater.inflate(R.layout.list_movie_picker_row, parent, false);
            holder = new ViewHolder();
            holder.imageView = (AsyncImageView) convertView.findViewById(R.id.image);
            holder.titleTextView = (TextView) convertView.findViewById(R.id.title);
            holder.yearTextView = (TextView) convertView.findViewById(R.id.year);
            holder.typeTextView = (TextView) convertView.findViewById(R.id.type);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        
        Movie movie = movies.get(position);
        
//        holder.imageView.setImageUrl(movie.getPoster());
        holder.titleTextView.setText(movie.getTitle());
        holder.yearTextView.setText(Integer.toString(movie.getYear()));
        holder.typeTextView.setText(movie.getTypeName(context));
        
        return convertView;
	}

}
