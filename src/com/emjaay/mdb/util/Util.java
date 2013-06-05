package com.emjaay.mdb.util;

import java.security.MessageDigest;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

public class Util {
	
	public static String sha256(String input){
		try{
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			digest.reset();
			
			byte[] byteData = digest.digest(input.getBytes("UTF-8"));
			StringBuffer sb = new StringBuffer();
			
			for (int i = 0; i < byteData.length; i++){
				sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
			}
			return sb.toString();
		} catch(Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	private static String saltPassword(String password) {
		return password + ":em+jaay";
	}
	
	public static String hashPassword(String password){
		if(TextUtils.isEmpty(password)){
			return "";
		}
		
		String saltedPassword = saltPassword(password);
		
		return sha256(saltedPassword);
	}
	
	public static boolean isNetworkAvailable(Context context) {
	    ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

}