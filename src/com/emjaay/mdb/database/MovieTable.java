package com.emjaay.mdb.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

import com.emjaay.mdb.data.Movie;
import com.emjaay.mdb.exception.DatabaseException;

public class MovieTable extends AbstractTable implements BaseColumns {
	
    public static final String TABLE_NAME		= "movies";
    public static final String COLUMN_TITLE		= "title";
    public static final String COLUMN_YEAR		= "year";
    public static final String COLUMN_RATED		= "rated";
    public static final String COLUMN_RELEASED	= "released";
    public static final String COLUMN_RUNTIME	= "runtime";
    public static final String COLUMN_GENRE		= "genre";
    public static final String COLUMN_DIRECTOR	= "director";
    public static final String COLUMN_WRITER	= "writer";
    public static final String COLUMN_ACTORS	= "actors";
    public static final String COLUMN_PLOT		= "plot";
    public static final String COLUMN_POSTER	= "poster";
    public static final String COLUMN_RATING	= "rating";
    public static final String COLUMN_VOTES		= "votes";
    public static final String COLUMN_IMDB_ID	= "imdb_id";
    public static final String COLUMN_TYPE		= "type";

    public static final String[] PROJECTION_ALL = {
		    COLUMN_TITLE,
		    COLUMN_YEAR,
		    COLUMN_RATED,
		    COLUMN_RELEASED,
		    COLUMN_RUNTIME,
		    COLUMN_GENRE,
		    COLUMN_DIRECTOR,
		    COLUMN_WRITER,
		    COLUMN_ACTORS,
		    COLUMN_PLOT,
		    COLUMN_POSTER,
		    COLUMN_RATING,
		    COLUMN_VOTES,
		    COLUMN_IMDB_ID,
		    COLUMN_TYPE
		    };
    
    public static final String CREATE = "CREATE TABLE " + TABLE_NAME + " ("
			+ COLUMN_IMDB_ID + " STRING UNIQUE, "
			+ COLUMN_TITLE + " TEXT, "
			+ COLUMN_YEAR + " INTEGER, "
			+ COLUMN_RATED + " TEXT, "
			+ COLUMN_RELEASED + " TEXT, "
			+ COLUMN_RUNTIME + " TEXT, "
			+ COLUMN_GENRE + " TEXT, "
			+ COLUMN_DIRECTOR + " TEXT, "
			+ COLUMN_WRITER + " TEXT, "
			+ COLUMN_ACTORS + " TEXT, "
			+ COLUMN_PLOT + " TEXT, "
			+ COLUMN_POSTER + " TEXT, "
			+ COLUMN_RATING + " REAL, "
			+ COLUMN_VOTES + " INTEGER, "
			+ COLUMN_TYPE + " INTEGER"
			+ ");";
    
    public static final String DELETE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    
    public static ContentValues populate(Movie movie){
    	ContentValues values = new ContentValues();
		values.put(COLUMN_IMDB_ID, movie.getImdbId());
		values.put(COLUMN_TITLE, movie.getTitle());
		values.put(COLUMN_YEAR, movie.getYear());
		values.put(COLUMN_RATED, movie.getRated());
		values.put(COLUMN_RELEASED, movie.getReleased());
		values.put(COLUMN_RUNTIME, movie.getRuntime());
		values.put(COLUMN_GENRE, movie.getGenre());
		values.put(COLUMN_DIRECTOR, movie.getDirector());
		values.put(COLUMN_WRITER, movie.getWriter());
		values.put(COLUMN_ACTORS, movie.getActors());
		values.put(COLUMN_PLOT, movie.getPlot());
		values.put(COLUMN_POSTER, movie.getPoster());
		values.put(COLUMN_RATING, movie.getRating());
		values.put(COLUMN_VOTES, movie.getVotes());
		values.put(COLUMN_TYPE, movie.getType());
		
		return values;
    }
    
    public static Movie toMovie(Cursor c) throws DatabaseException {
    	Movie movie = new Movie();
    	
    	int indexImdbId;
    	try{
    		indexImdbId = c.getColumnIndexOrThrow(COLUMN_IMDB_ID);
    	} catch(IllegalArgumentException e){
    		throw new DatabaseException("Missing required column imdb_id");
    	}
    	int indexTitle = c.getColumnIndex(COLUMN_TITLE);
    	int indexYear = c.getColumnIndex(COLUMN_YEAR);
    	int indexRated = c.getColumnIndex(COLUMN_RATED);
    	int indexReleased = c.getColumnIndex(COLUMN_RELEASED);
    	int indexRuntime = c.getColumnIndex(COLUMN_RUNTIME);
    	int indexGenre = c.getColumnIndex(COLUMN_GENRE);
    	int indexDirector = c.getColumnIndex(COLUMN_DIRECTOR);
    	int indexWriter = c.getColumnIndex(COLUMN_WRITER);
    	int indexActors = c.getColumnIndex(COLUMN_ACTORS);
    	int indexPlot = c.getColumnIndex(COLUMN_PLOT);
    	int indexPoster = c.getColumnIndex(COLUMN_POSTER);
    	int indexRating = c.getColumnIndex(COLUMN_RATING);
    	int indexVotes = c.getColumnIndex(COLUMN_VOTES);
    	int indexType = c.getColumnIndex(COLUMN_TYPE);
    	
    	movie.setImdbId(c.getString(indexImdbId));
    	if(indexTitle != -1) movie.setTitle(c.getString(indexTitle));
    	if(indexYear != -1) movie.setYear(c.getInt(indexYear));
    	if(indexRated != -1) movie.setRated(c.getString(indexRated));
    	if(indexReleased != -1) movie.setReleased(c.getString(indexReleased));
    	if(indexRuntime != -1) movie.setRuntime(c.getString(indexRuntime));
    	if(indexGenre != -1) movie.setGenre(c.getString(indexGenre));
    	if(indexDirector != -1) movie.setDirector(c.getString(indexDirector));
    	if(indexWriter != -1) movie.setWriter(c.getString(indexWriter));
    	if(indexActors != -1) movie.setActors(c.getString(indexActors));
    	if(indexPlot != -1) movie.setPlot(c.getString(indexPlot));
    	if(indexPoster != -1) movie.setPoster(c.getString(indexPoster));
    	if(indexRating != -1) movie.setRating(c.getFloat(indexRating));
    	if(indexVotes != -1) movie.setVotes(c.getInt(indexVotes));
    	if(indexType != -1) movie.setType(c.getInt(indexType));
    	
    	return movie;
    }
    
}