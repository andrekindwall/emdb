package com.emjaay.mdb.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

import com.emjaay.mdb.data.Copy;
import com.emjaay.mdb.exception.DatabaseException;

public class CopyTable extends AbstractTable implements BaseColumns {
	
    public static final String TABLE_NAME			= "copies";
    public static final String COLUMN_IMDB_ID		= "imdb_id";
    public static final String COLUMN_MEDIA_TYPE	= "media_type";

    public static final String[] PROJECTION_ALL = {
    		_ID,
	    	COLUMN_IMDB_ID,
	    	COLUMN_MEDIA_TYPE
		    };
    
    public static final String CREATE = "CREATE TABLE " + TABLE_NAME + " ("
			+ _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ COLUMN_IMDB_ID + " STRING, "
			+ COLUMN_MEDIA_TYPE + " INTEGER"
			+ ");";
    
    public static final String DELETE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    
    public static ContentValues populate(Copy copy){
    	ContentValues values = new ContentValues();
//		values.put(_ID, copy.getId());
		values.put(COLUMN_IMDB_ID, copy.getImdbId());
		values.put(COLUMN_MEDIA_TYPE, copy.getMediaType());
		
		return values;
    }
    
    public static Copy toCopy(Cursor c) throws DatabaseException {
    	Copy copy = new Copy();
    	
    	int indexId;
    	try{
    		indexId = c.getColumnIndexOrThrow(_ID);
    	} catch(IllegalArgumentException e){
    		throw new DatabaseException("Missing required column _id");
    	}
    	int indexImdbId = c.getColumnIndex(COLUMN_IMDB_ID);
    	int indexMediaType = c.getColumnIndex(COLUMN_MEDIA_TYPE);
    	
    	copy.setId(c.getInt(indexId));
    	if(indexImdbId != -1) copy.setImdbId(c.getString(indexImdbId));
    	if(indexMediaType != -1) copy.setMediaType(c.getInt(indexMediaType));
    	
    	return copy;
    }
    
}