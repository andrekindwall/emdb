package com.emjaay.mdb.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.emjaay.mdb.data.Copy;
import com.emjaay.mdb.data.Movie;
import com.emjaay.mdb.exception.DatabaseException;

public class DatabaseHelper extends SQLiteOpenHelper {
	
	private static final String DATABASE_NAME = "emjaaymoviedb.db";
	private static final int DATABASE_VERSION = 5;
	
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL(MovieTable.CREATE);
		db.execSQL(CopyTable.CREATE);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		db.execSQL(MovieTable.DELETE);
		db.execSQL(CopyTable.DELETE);
		
	    onCreate(db);
	}
	
	public void clearCopies(){
		SQLiteDatabase db = getWritableDatabase();
		db.delete(CopyTable.TABLE_NAME, null, null);
		db.close();
	}
	
	public void insertCopy(Copy copy){
		ContentValues values = CopyTable.populate(copy);
		SQLiteDatabase db = getWritableDatabase();
		db.insert(CopyTable.TABLE_NAME, null, values);
		db.close();
	}
	
	public void insertCopies(ArrayList<Copy> copies){
		SQLiteDatabase db = getWritableDatabase();
		db.beginTransaction();
		for(Copy copy : copies){
			ContentValues values = CopyTable.populate(copy);
			db.insert(CopyTable.TABLE_NAME, null, values);
		}
		db.setTransactionSuccessful();
		db.endTransaction();
		db.close();
	}
	
	public void updateCopy(Copy copy){
		ContentValues values = CopyTable.populate(copy);
		SQLiteDatabase db = getWritableDatabase();
		db.update(CopyTable.TABLE_NAME, values, CopyTable.COLUMN_ADDED + "=" + copy.getAdded(), null);
		db.close();
	}
	
	/**
	 * Get all the imdbID's that exists in table 'copies' but not in table 'movies'
	 * 
	 * @return A list of all the missing imdbID's
	 */
	public ArrayList<String> getMissingMovies() {
		ArrayList<String> ids = new ArrayList<String>();
		String[] projection = new String[]{ CopyTable.COLUMN_IMDB_ID };
		
		String nestedSelect = "SELECT " + MovieTable.COLUMN_IMDB_ID + " FROM " + MovieTable.TABLE_NAME;
		String selection = CopyTable.COLUMN_IMDB_ID + " NOT IN (" + nestedSelect + ")";
		
		SQLiteDatabase db = getReadableDatabase();
		Cursor c = db.query(
				CopyTable.TABLE_NAME,  		// The table to query
				projection,                 // The columns to return
				selection,                  // The columns for the WHERE clause
				null,     					// The values for the WHERE clause
				null,                       // don't group the rows
				null,                       // don't filter by row groups
				null                 		// The sort order
				);
		
		c.moveToFirst();
		while(c.isAfterLast() == false){
			ids.add(c.getString(0));
			c.moveToNext();
		}
		c.close();
		db.close();
		
		return ids;
	}
	
	public ArrayList<Copy> getUnsyncedCopies() {
		ArrayList<Copy> copies = new ArrayList<Copy>();
		String[] projection = CopyTable.PROJECTION_ALL;
		String selection =  CopyTable.COLUMN_SYNCED + "=0";
		
		SQLiteDatabase db = getReadableDatabase();
		Cursor c = db.query(
				CopyTable.TABLE_NAME, 	 	// The table to query
				projection,                 // The columns to return
				selection,                  // The columns for the WHERE clause
				null,     					// The values for the WHERE clause
				null,                       // don't group the rows
				null,                       // don't filter by row groups
				null 						// The sort order
				);
		
		c.moveToFirst();
		while(c.isAfterLast() == false){
			try {
				copies.add(CopyTable.toCopy(c));
			} catch (DatabaseException e) {
				e.printStackTrace();
				return null;
			}
			c.moveToNext();
		}
		c.close();
		db.close();
		
		return copies;
	}
	
	public ArrayList<Copy> getCopies(String imdbId) {
		ArrayList<Copy> copies = new ArrayList<Copy>();
		String[] projection = CopyTable.PROJECTION_ALL;
		String selection =  CopyTable.COLUMN_IMDB_ID + "=?";
		String[] selectionArgs = new String[]{ imdbId };
		
		SQLiteDatabase db = getReadableDatabase();
		Cursor c = db.query(
				CopyTable.TABLE_NAME, 	 	// The table to query
				projection,                 // The columns to return
				selection,                  // The columns for the WHERE clause
				selectionArgs,     			// The values for the WHERE clause
				null,                       // don't group the rows
				null,                       // don't filter by row groups
				null 						// The sort order
				);
		
		c.moveToFirst();
		while(c.isAfterLast() == false){
			try {
				copies.add(CopyTable.toCopy(c));
			} catch (DatabaseException e) {
				e.printStackTrace();
				return null;
			}
			c.moveToNext();
		}
		c.close();
		db.close();
		
		return copies;
	}
	
	public void insertMovie(Movie movie, boolean replaceOnConflict){
		ContentValues values = MovieTable.populate(movie);
		SQLiteDatabase db = getWritableDatabase();
		db.insertWithOnConflict(MovieTable.TABLE_NAME, null, values, replaceOnConflict ? SQLiteDatabase.CONFLICT_REPLACE : SQLiteDatabase.CONFLICT_IGNORE);
		db.close();
	}
	
	public Movie getMovie(String imdbId) {
		Movie movie = null;
		
		String[] projection = MovieTable.PROJECTION_ALL;
		String selection = MovieTable.COLUMN_IMDB_ID + "=?";
		String[] selectionArgs = { imdbId };
		
		SQLiteDatabase db = getReadableDatabase();
		Cursor c = db.query(
				MovieTable.TABLE_NAME,  	// The table to query
				projection,                 // The columns to return
				selection,                  // The columns for the WHERE clause
				selectionArgs,              // The values for the WHERE clause
				null,                       // don't group the rows
				null,                       // don't filter by row groups
				null						// The sort order
				);
		
		c.moveToFirst();
		try {
			movie = MovieTable.toMovie(c);
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
		c.close();
		db.close();
		
		return movie;
	}
	
	public ArrayList<Movie> getMovies() {
		return getMovies(0);
	}
	
	public ArrayList<Movie> getMovies(int typeFilter) {
		ArrayList<Movie> movies = new ArrayList<Movie>();
		String[] projection = MovieTable.PROJECTION_ALL;
		String selection = typeFilter != 0 ? MovieTable.COLUMN_TYPE + "=?" : null;
		String[] selectionArgs = typeFilter != 0 ? new String[]{ Integer.toString(typeFilter) } : null;
		String sortOrder = MovieTable.COLUMN_TITLE + " ASC";
		
		SQLiteDatabase db = getReadableDatabase();
		Cursor c = db.query(
				MovieTable.TABLE_NAME,  	// The table to query
				projection,                 // The columns to return
				selection,                  // The columns for the WHERE clause
				selectionArgs,     			// The values for the WHERE clause
				null,                       // don't group the rows
				null,                       // don't filter by row groups
				sortOrder                   // The sort order
				);
		
		c.moveToFirst();
		while(c.isAfterLast() == false){
			try {
				movies.add(MovieTable.toMovie(c));
			} catch (DatabaseException e) {
				e.printStackTrace();
				return null;
			}
			c.moveToNext();
		}
		c.close();
		db.close();
		
		return movies;
	}
	
	public ArrayList<Movie> getMyMovies(int typeFilter) {
		ArrayList<Movie> movies = new ArrayList<Movie>();
		String[] projection = MovieTable.PROJECTION_ALL;
		String sortOrder = MovieTable.COLUMN_TITLE + " ASC";
		
		String selection;
		String[] selectionArgs;
		String nestedSelect = "SELECT DISTINCT " + CopyTable.COLUMN_IMDB_ID + " FROM " + CopyTable.TABLE_NAME;
		if(typeFilter == 0){
			selection = MovieTable.COLUMN_IMDB_ID + " IN (" + nestedSelect + ")";
			selectionArgs = null;
		} else {
			selection = MovieTable.COLUMN_IMDB_ID + " IN (" + nestedSelect + ") AND " + MovieTable.COLUMN_TYPE + "=?";
			selectionArgs = new String[]{ Integer.toString(typeFilter) };
		}
		
		SQLiteDatabase db = getReadableDatabase();
		Cursor c = db.query(
				MovieTable.TABLE_NAME,  	// The table to query
				projection,                 // The columns to return
				selection,                  // The columns for the WHERE clause
				selectionArgs,     			// The values for the WHERE clause
				null,                       // don't group the rows
				null,                       // don't filter by row groups
				sortOrder                   // The sort order
				);
		
		c.moveToFirst();
		while(c.isAfterLast() == false){
			try {
				movies.add(MovieTable.toMovie(c));
			} catch (DatabaseException e) {
				e.printStackTrace();
				return null;
			}
			c.moveToNext();
		}
		c.close();
		db.close();
		
		return movies;
	}

}