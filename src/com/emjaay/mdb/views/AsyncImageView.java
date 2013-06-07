package com.emjaay.mdb.views;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;

public class AsyncImageView extends ImageView {

	private ImageLoader imageLoader;
	private String imageUrl;
	
	public AsyncImageView(Context context) {
		super(context);
	}
	
	public AsyncImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	/**
	 * Set url for image to be displayed.<br>
	 * <br>
	 * Running asynchronously
	 * 
	 * @param url image url
	 */
	public void setImageUrl(String url) {
		if(TextUtils.isEmpty(url) || (imageUrl != null && imageUrl.equals(url))){
			return;
		}
		if(imageLoader != null){
			imageLoader.cancel(true);
			imageLoader = null;
		}
		imageLoader = new ImageLoader(url);
		imageLoader.execute((Void) null);
		
		imageUrl = url;
	}
	
	private class ImageLoader extends AsyncTask<Void, Void, Drawable> {
		
		private String mUrl;
		
		public ImageLoader(String url){
			this.mUrl = url;
		}

		@Override
		protected Drawable doInBackground(Void... params) {
			Drawable drawable = downloadImage();
			
			return drawable;
		}
		
		@Override
		protected void onPostExecute(Drawable drawable) {
			super.onPostExecute(drawable);
			
			if(drawable != null){
				setImageDrawable(drawable);
				imageLoader = null;
			}
		}
		
		private Drawable downloadImage() {
			try {
				URL url = new URL(mUrl);
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setUseCaches(true);
				InputStream in = connection.getInputStream();
				Drawable drawable = Drawable.createFromStream(in, "src");
				in.close();
				return drawable;
			} catch (Exception e) {
				e.printStackTrace();
			}
			
	        return null;
	    }
		
		@Override
		protected void onCancelled() {
			super.onCancelled();
			imageLoader = null;
		}
	}
	
}