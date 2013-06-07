package com.emjaay.mdb.application;

import java.io.File;

import android.app.Application;

public class EmdbApplication extends Application {
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		enableHttpResponseCache();
	}
	
	private void enableHttpResponseCache() {
	    try {
	        long httpCacheSize = 10 * 1024 * 1024; // 10 MiB
	        File httpCacheDir = new File(getCacheDir(), "http");
	        Class.forName("android.net.http.HttpResponseCache")
	            .getMethod("install", File.class, long.class)
	            .invoke(null, httpCacheDir, httpCacheSize);
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	}
}
