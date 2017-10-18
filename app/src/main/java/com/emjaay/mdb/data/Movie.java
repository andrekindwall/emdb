package com.emjaay.mdb.data;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.emjaay.mdb.R;

public class Movie implements Parcelable {
	
	public static final String EXTRAS_KEY = "com.emjaay.mdb.data.movie";
	
	private static final String API_IMDB_ID = "imdbID";
	private static final String API_TITLE = "Title";
	private static final String API_RATED = "Rated";
	private static final String API_RELEASED = "Released";
	private static final String API_RUNTIME = "Runtime";
	private static final String API_GENRE = "Genre";
	private static final String API_DIRECTOR = "Director";
	private static final String API_WRITER = "Writer";
	private static final String API_ACTORS = "Actors";
	private static final String API_PLOT = "Plot";
	private static final String API_POSTER = "Poster";
	private static final String API_YEAR = "Year";
	private static final String API_TYPE = "Type";
	private static final String API_VOTES = "imdbVotes";
	private static final String API_RATING = "imdbRating";
	
	private static final String API_TYPE_MOVIE = "movie";
	private static final String API_TYPE_SERIES = "series";
	private static final String API_TYPE_EPISODE = "episode";
	
	public static final int TYPE_UNKNOWN = -1;
	public static final int TYPE_MOVIE = 1;
	public static final int TYPE_SERIES = 2;
	public static final int TYPE_EPISODE = 3;
	
	private String imdbId;
	private String title;
	private String rated;
	private String released;
	private String runtime;
	private String genre;
	private String director;
	private String writer;
	private String actors;
	private String plot;
	private String poster;
	private int year;
	private int type;
	private int votes;
	private float rating;
	
	public Movie(){
		
	}
	
	public Movie(Parcel parcel){
		readFromParcel(parcel);
	}
	
	public void setActors(String actors) {
		this.actors = actors;
	}
	
	public String getActors() {
		return actors;
	}
	
	public void setDirector(String director) {
		this.director = director;
	}
	
	public String getDirector() {
		return director;
	}
	
	public void setGenre(String genre) {
		this.genre = genre;
	}
	
	public String getGenre() {
		return genre;
	}
	
	public void setImdbId(String imdbId) {
		this.imdbId = imdbId;
	}
	
	public String getImdbId() {
		return imdbId;
	}
	
	public void setPlot(String plot) {
		this.plot = plot;
	}
	
	public String getPlot() {
		return plot;
	}
	
	public void setPoster(String poster) {
		this.poster = poster;
	}
	
	public String getPoster() {
		return poster;
	}
	
	public void setRated(String rated) {
		this.rated = rated;
	}
	
	public String getRated() {
		return rated;
	}
	
	public void setRating(float rating) {
		this.rating = rating;
	}
	
	public float getRating() {
		return rating;
	}
	
	public void setReleased(String released) {
		this.released = released;
	}
	
	public String getReleased() {
		return released;
	}
	
	public void setRuntime(String runtime) {
		this.runtime = runtime;
	}
	
	public String getRuntime() {
		return runtime;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	public int getType() {
		return type;
	}
	
	public void setVotes(int votes) {
		this.votes = votes;
	}
	
	public int getVotes() {
		return votes;
	}
	
	public void setWriter(String writer) {
		this.writer = writer;
	}
	
	public String getWriter() {
		return writer;
	}
	
	public void setYear(int year) {
		this.year = year;
	}
	
	public int getYear() {
		return year;
	}
	
	public String getTypeName(Context context){
		switch(type){
		case TYPE_MOVIE:
			return context.getString(R.string.movie);
		case TYPE_SERIES:
			return context.getString(R.string.series);
		case TYPE_EPISODE:
			return context.getString(R.string.episode);
		}
		return "";
	}
	
	public String toString(){
		return getClass().toString();
	}
	
	public static Movie fromJson(JSONObject jo, boolean isDetailed){
		Movie movie = new Movie();
		try{
			if (jo.has(API_IMDB_ID)){
				movie.setImdbId(jo.getString(API_IMDB_ID));
			}
			if (jo.has(API_TITLE)){
				movie.setTitle(jo.getString(API_TITLE));
			}
			if (jo.has(API_YEAR)){
				movie.setYear(jo.getInt(API_YEAR));
			}
			if (isDetailed){
				if (jo.has(API_RATED)){
					movie.setRated(jo.getString(API_RATED));
				}
				if (jo.has(API_RELEASED)){
					movie.setReleased(jo.getString(API_RELEASED));
				}
				if (jo.has(API_RUNTIME)){
					movie.setRuntime(jo.getString(API_RUNTIME));
				}
				if (jo.has(API_GENRE)){
					movie.setGenre(jo.getString(API_GENRE));
				}
				if (jo.has(API_DIRECTOR)){
					movie.setDirector(jo.getString(API_DIRECTOR));
				}
				if (jo.has(API_WRITER)){
					movie.setWriter(jo.getString(API_WRITER));
				}
				if (jo.has(API_ACTORS)){
					movie.setActors(jo.getString(API_ACTORS));
				}
				if (jo.has(API_PLOT)){
					movie.setPlot(jo.getString(API_PLOT));
				}
				if (jo.has(API_POSTER)){
					movie.setPoster(jo.getString(API_POSTER));
				}
				if (jo.has(API_RATING)){
					movie.setRating((float)jo.getDouble(API_RATING));
				}
				if (jo.has(API_TYPE)){
					String type = jo.getString(API_TYPE);
					if (type.equals(API_TYPE_MOVIE)){
						movie.setType(TYPE_MOVIE);
					}
					if (type.equals(API_TYPE_SERIES)){
						movie.setType(TYPE_SERIES);
					}
					if (type.equals(API_TYPE_EPISODE)){
						movie.setType(TYPE_EPISODE);
					}
				}
				if (jo.has(API_VOTES)){
					//Change from "123,456" to "123456" so it can be parsed as int
					String votes = jo.getString(API_VOTES);
					movie.setVotes(Integer.parseInt(votes.replace(",", "")));
				}
			}
		} catch (JSONException e){
			e.printStackTrace();
		}
		
		return movie;
	}
	
	public static ArrayList<Movie> fromJson(JSONArray ja, boolean isDetailed){
		ArrayList<Movie> movies = new ArrayList<Movie>();
		try{
			for(int i=0; i<ja.length(); i++){
				JSONObject jo = ja.getJSONObject(i);
				movies.add(fromJson(jo, isDetailed));
			}
		}catch (JSONException e){
			e.printStackTrace();
		}
		return movies;
	}
	
	public int describeContents(){
		return 0;
	}
	
	public void writeToParcel(Parcel dest, int flags){
		dest.writeString(imdbId);
		dest.writeString(title);
		dest.writeString(rated);
		dest.writeString(released);
		dest.writeString(runtime);
		dest.writeString(genre);
		dest.writeString(director);
		dest.writeString(writer);
		dest.writeString(actors);
		dest.writeString(plot);
		dest.writeString(poster);
		dest.writeInt(year);
		dest.writeInt(type);
		dest.writeInt(votes);
		dest.writeFloat(rating);
	}
	
	private void readFromParcel(Parcel parcel){
		imdbId = parcel.readString();
		title = parcel.readString();
		rated = parcel.readString();
		released = parcel.readString();
		runtime = parcel.readString();
		genre = parcel.readString();
		director = parcel.readString();
		writer = parcel.readString();
		actors = parcel.readString();
		plot = parcel.readString();
		poster = parcel.readString();
		year = parcel.readInt();
		type = parcel.readInt();
		votes = parcel.readInt();
		rating = parcel.readFloat();
	}
	
	public static final Creator<Movie> CREATOR = new Creator<Movie>() {
		public Movie createFromParcel(Parcel in) {
			return new Movie(in);
		}
		
		public Movie[] newArray(int size) {
			return new Movie[size];
		}
	};
}