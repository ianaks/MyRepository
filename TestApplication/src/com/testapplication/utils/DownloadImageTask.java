package com.testapplication.utils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

public class DownloadImageTask extends AsyncTask<String, Void, List<Bitmap>> {
	
	protected List<Bitmap> doInBackground(String... urls) {
	String urldisplay = urls[0];
	Bitmap mIcon11 = null;
	List<Bitmap> lstBitmap = new ArrayList<Bitmap>();
	try {
		InputStream in;
		if(urldisplay!=null && urldisplay.indexOf(".jpg")!=-1){
			in = new java.net.URL(urldisplay).openStream();
		    mIcon11 = BitmapFactory.decodeStream(in);
		    lstBitmap.add(0, mIcon11);
		}
	} catch (Exception e) {
	    Log.e("Error", e.getMessage());
	    e.printStackTrace();
	}
	return lstBitmap;
	}
	
	protected void onPostExecute(List<Bitmap> result) {
//		bmImage.setImageBitmap(result);
	}
}
