package com.testapplication.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import test.FragmentActivityMy;

import com.testapplication.entity.Track;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class WebServiceConnection extends AsyncTask<String, List<Track>, String>{

	private ProgressDialog pDlg;
	private Context context;
	
	private static final int CONN_TIMEOUT = 300000;
    private static final int SOCKET_TIMEOUT = 500000;
    
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
	protected String doInBackground(String... params) {
		
		HttpClient httpclient = new DefaultHttpClient(getHttpParams());
		Object response = null;
		HttpGet httpget = new HttpGet(url);
		try {
			response = httpclient.execute(httpget);
			if(response!=null && response instanceof HttpResponse){
				BufferedReader reader = new BufferedReader(new InputStreamReader(((HttpResponse) response).getEntity().getContent(), "UTF-8"));
				StringBuilder builder= new StringBuilder();
				for (String line = null; (line = reader.readLine()) != null;) {
				    builder.append(line).append("\n");
				}
				return builder.toString();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			Log.e("inventory", "ClientProtocolException - " + e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			Log.e("inventory", "IOException - " + e.getMessage());
		}
		
		return null;
	}
	
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		
		if(result!=null && result instanceof String)
		FragmentActivityMy.setWebList(result);
		
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
