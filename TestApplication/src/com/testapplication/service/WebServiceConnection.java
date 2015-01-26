package com.testapplication.service;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import com.testapplication.entity.Track;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class WebServiceConnection extends AsyncTask<String, List<Track>, Object>{

	private ProgressDialog pDlg;
	private Context context;
	
	private static final int CONN_TIMEOUT = 3000;
    private static final int SOCKET_TIMEOUT = 5000;
    
	private String url;
	
	public WebServiceConnection(String url, Context context) {
		this.url = url;
		this.context = context;
	}
    		
    List<Track> tracks;
	
	@Override
    protected void onPreExecute() {
        showProgressDialog();
    }
	
	@Override
	protected Object doInBackground(String... params) {
		
		HttpClient httpclient = new DefaultHttpClient(getHttpParams());
		Object response = null;
		HttpGet httpget = new HttpGet(url);
		try {
			response = httpclient.execute(httpget);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			Log.e("inventory", "ClientProtocolException - " + e.getMessage());
			response = e.getMessage();
		} catch (IOException e) {
			e.printStackTrace();
			Log.e("inventory", "IOException - " + e.getMessage());
			response = e.getMessage();
		}
		
		return response;
	}
	
	@Override
	protected void onPostExecute(Object result) {
		super.onPostExecute(result);
		
		pDlg.dismiss();
		
		
	}
	
	private HttpParams getHttpParams() {
        
        HttpParams htpp = new BasicHttpParams();
         
        HttpConnectionParams.setConnectionTimeout(htpp, CONN_TIMEOUT);
        HttpConnectionParams.setSoTimeout(htpp, SOCKET_TIMEOUT);
         
        return htpp;
    }

	private void showProgressDialog() {
        
		pDlg = new ProgressDialog(this.context);
        pDlg.setMessage("Wait...");
        pDlg.setCancelable(false);
        pDlg.show();

    }
	
}
