package com.emjaay.mdb.data;

public class Copy
{
	public static final int TYPE_VHS = 1;
	public static final int TYPE_DVD = 2;
	public static final int TYPE_BLURAY = 3;
	public static final int TYPE_DIGITAL = 4;
	
	private int id;
	private String imdbId;
	private int mediaType;

	public void setId(int id){
		this.id = id;
	}
	
	public int getId(){
		return this.id;
	}

	public void setImdbId(String imdbId){
		this.imdbId = imdbId;
	}
	
	public String getImdbId(){
		return this.imdbId;
	}
	
	public void setMediaType(int mediaType){
		this.mediaType = mediaType;
	}
	
	public int getMediaType(){
		return this.mediaType;
	}
	
}