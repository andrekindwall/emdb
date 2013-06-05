package com.emjaay.mdb.util;

import android.content.Context;
import android.content.SharedPreferences;

public class UserData {
	
	private static final String SHARED_PREFS_NAME = "emjaayuserprefs";
	
	private static final String PREFS_EMAIL = "prefs_email";
	private static final String PREFS_PASSWORD = "prefs_password";
	
	private static SharedPreferences getSharedPreferences(Context context){
		return context.getSharedPreferences(SHARED_PREFS_NAME, 0);
	}
	
	public static void setEmail(Context context, String email){
		SharedPreferences prefs = getSharedPreferences(context);
		prefs.edit().putString(PREFS_EMAIL, email).commit();
	}
	
	public static String getEmail(Context context){
		SharedPreferences prefs = getSharedPreferences(context);
		return prefs.getString(PREFS_EMAIL, "");
	}
	
	public static void setPassword(Context context, String password){
		SharedPreferences prefs = getSharedPreferences(context);
		prefs.edit().putString(PREFS_PASSWORD, password).commit();
	}
	
	public static String getPassword(Context context){
		SharedPreferences prefs = getSharedPreferences(context);
		return prefs.getString(PREFS_PASSWORD, "");
	}
	
	public static void clearUserData(Context context){
		SharedPreferences prefs = getSharedPreferences(context);
		prefs.edit()
				.putString(PREFS_EMAIL, "")
				.putString(PREFS_PASSWORD, "")
				.commit();
	}
}