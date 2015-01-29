package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.testapplication.R;
import com.testapplication.database.LocalDataBase;
import com.testapplication.entity.Track;
import com.testapplication.utils.DownloadImageTask;
import com.testapplication.utils.TrackDAO;
import com.testapplication.utils.TracksLocalListAdapter;
import com.testapplication.utils.TracksWebListAdapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class FragmentActivityMy extends FragmentActivity {
	
	// both fragments are on the screen
		private boolean mTwoPane;
		
		// list of local tracks
		private List<Track> localTracks;
		// list of web tracks
		private List<Track> webTracks;
		
		// local data base
		private static LocalDataBase localDataBase;
		
		// data access object
		private TrackDAO trackDAO;
		
	    // widgets
	    private EditText searchMusic;
	    private Button buttonWeb;
	    private Button buttonLocal;
	    private ListView lstWebTracks;
	    private ListView lstLocalTracks;
	    private TextView empty;
	    private Button buttonRate;
	    private Button buttonSave;
	    private RatingBar ratingBar;
	    
	    private boolean webPressed = true;
	    
	    // fragments
	    FragmentDetail fragmentDetail;
	    FragmentRating fragmentRating;
	    
	    Context context;
	    
	    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.fragment);
    	
    	context = this;
    	
    	fragmentDetail = (FragmentDetail)getFragmentManager().findFragmentById(R.id.fragment_detail);
    	fragmentRating = (FragmentRating)getFragmentManager().findFragmentById(R.id.fragment_rating);
    	
    	localDataBase = new LocalDataBase(this, 1);
     	trackDAO = new TrackDAO(localDataBase.getWritableDatabase());
     	
     	searchMusic = (EditText)findViewById(R.id.searchEdit);     	
     	localTracks = trackDAO.getAllTracksFromLocalDB();     	
     	lstWebTracks = (ListView)findViewById(R.id.lstWebTracks);     	
     	lstLocalTracks = (ListView)findViewById(R.id.lstLocalTracks);     	
     	buttonWeb = (Button)findViewById(R.id.buttonWeb);     	
     	buttonLocal = (Button)findViewById(R.id.buttonLocal);     	
     	empty = (TextView)findViewById(R.id.empty);
     	buttonRate = (Button)findViewById(R.id.ratingButton);    
     	buttonSave = (Button)findViewById(R.id.saveButton); 
     	ratingBar = (RatingBar)findViewById(R.id.ratingBar);
     	
     	if(buttonRate!=null){
     		buttonRate.setVisibility(View.GONE);
     		buttonRate.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(fragmentRating!=null){
						findViewById(R.id.fragment_rating).setVisibility(View.VISIBLE);
						findViewById(R.id.fragment_detail).setVisibility(View.GONE);
					}
				}
			});
     	}
     	
     	buttonWeb.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				buttonWeb.setBackgroundResource(R.drawable.gradient_bg_hover);
				buttonLocal.setBackgroundResource(R.drawable.gradient_bg);
				webPressed = true;
				if(webTracks!=null && webTracks.size()>0){
					lstWebTracks.setVisibility(View.VISIBLE);
					lstLocalTracks.setVisibility(View.GONE);
				} else{ 
					empty.setVisibility(View.VISIBLE);
				lstWebTracks.setVisibility(View.GONE);
				lstLocalTracks.setVisibility(View.GONE);
				}
				if(buttonRate!=null)
					buttonRate.setVisibility(View.GONE);
			}
		});
     	
     	buttonLocal.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				buttonWeb.setBackgroundResource(R.drawable.gradient_bg);
				buttonLocal.setBackgroundResource(R.drawable.gradient_bg_hover);
				buttonLocal.setActivated(true);
				buttonWeb.setActivated(false);
				webPressed = false;
				if(localTracks!=null && localTracks.size()>0){
					lstWebTracks.setVisibility(View.GONE);
					lstLocalTracks.setVisibility(View.VISIBLE);
				} else{ 
					empty.setVisibility(View.VISIBLE);
					lstWebTracks.setVisibility(View.GONE);
					lstLocalTracks.setVisibility(View.GONE);
				}
				if(buttonRate!=null)
					buttonRate.setVisibility(View.VISIBLE);
			}
		});
     	  	
     	searchMusic.setOnEditorActionListener(new OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                	if(webPressed==true){
                		searchMusic(v.getText().toString()); 
                	} else{
                		localTracks = trackDAO.getAllTracksFromLocalDB();
                		setList(localTracks, "local");
                	}
                	searchMusic.requestFocus();
                }    
                return false;
            }
        });

   }
     	
    
    private void showAlertDialog(String msg) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				this);
		alertDialogBuilder.setTitle("Connection error");
		alertDialogBuilder
			.setMessage(msg)
			.setCancelable(false)
			.setPositiveButton("OK",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					dialog.cancel();
				}
			  });

			AlertDialog alertDialog = alertDialogBuilder.create();

			alertDialog.show();
		}
    
    private void searchMusic(String term){
    	WebServiceConnection webServiceConnection = new WebServiceConnection(
        		getResources().getString(R.string.web_service_url) +
        		"?term="+term+"&media=music&entity=song");
    	Log.d("", getResources().getString(R.string.web_service_url) +
        		"?term="+term+"&media=music"+"&entity=song");
    	
        webServiceConnection.execute();
    }
    
    public void setList(List<Track> tracks, final String type) {
		final List<Track> listTracks = tracks;	
		
		ListView lstTracks;
		if(type.equals("web"))
			lstTracks = lstWebTracks;
		else 
			lstTracks = lstLocalTracks;
		if(listTracks!=null && listTracks.size() > 0){

			lstTracks.setOnItemClickListener(new OnItemClickListener() {			
				@Override
				public void onItemClick(AdapterView<?> arg0, View view,
						int position, long id) {
					
			     	 if (fragmentDetail != null) {
			     			fragmentDetail.fillDetail(listTracks.get(position), type);
			     	 } else{
			     		 Intent intent = new Intent(context, FragmentDetailActivity.class);
			     		 intent.putExtra("track", listTracks.get(position));
			     		 intent.putExtra("type", type);
			     		 startActivity(intent);
			     	 }
				}
			});
			if(type.equals("web")){
				TracksWebListAdapter adapter = new TracksWebListAdapter(this,  R.layout.list_item_web_track, tracks);
				lstTracks.setAdapter(adapter);
			} else{
				TracksLocalListAdapter adapter = new TracksLocalListAdapter(this,  R.layout.list_item_local_track, tracks);
				lstTracks.setAdapter(adapter);
			}
			empty.setVisibility(View.GONE);
			lstTracks.setVisibility(View.VISIBLE);
		} else {
			empty.setVisibility(View.VISIBLE);
			tracks.clear();
			lstTracks.setVisibility(View.GONE);
		}
    }	   
 	

    class WebServiceConnection extends AsyncTask<String, List<Track>, String>{

    	private ProgressDialog pDlg;
    	
    	private static final int CONN_TIMEOUT = 300000;
        private static final int SOCKET_TIMEOUT = 500000;
        
    	private String url;
    	
    	public WebServiceConnection(String url) {
    		this.url = url;
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
    		
    		if(result!=null && result instanceof String){
    				String jsonString;
    				try {
    					
    					jsonString = result;
    					
    					JSONObject jsonObject = new JSONObject(jsonString);
    					JSONArray jsonArray = jsonObject.getJSONArray("results");
    					webTracks = new ArrayList<Track>();
    					for(int i=0; i<jsonArray.length();i++){
    						Track track= new Track();
    						track.setArtistName(jsonArray.getJSONObject(i).getString("artistName"));
    						try {
    							DownloadImageTask downloadImageTask = new DownloadImageTask();
    							downloadImageTask.execute(jsonArray.getJSONObject(i).getString("artworkUrl60"));
    							
    							track.setArtworkUrl60(downloadImageTask.get().get(0));	
    							
    							} catch (InterruptedException e) {
    								// TODO Auto-generated catch block
    								e.printStackTrace();
    							} catch (ExecutionException e) {
    								// TODO Auto-generated catch block
    								e.printStackTrace();
    							}
    						
    						track.setArtworkUrl100(jsonArray.getJSONObject(i).getString("artworkUrl100"));
    						track.setTrackId(jsonArray.getJSONObject(i).getInt("trackId"));
    						track.setTrackName(jsonArray.getJSONObject(i).getString("trackName"));
    						track.setTrackTimeMillis(jsonArray.getJSONObject(i).getInt("trackTimeMillis"));
    						webTracks.add(track);
    					}
    				} catch (ParseException e1) {
    						// TODO Auto-generated catch block
    						e1.printStackTrace();
    				  } catch (JSONException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				} 
    				setList(webTracks, "web");
    				
    			} else {
    				showAlertDialog("Server is temporarily unavailable");
    			}
    		
    		pDlg.dismiss();
    		
    		
    	}
    	
    	private HttpParams getHttpParams() {
            
            HttpParams htpp = new BasicHttpParams();
             
            HttpConnectionParams.setConnectionTimeout(htpp, CONN_TIMEOUT);
            HttpConnectionParams.setSoTimeout(htpp, SOCKET_TIMEOUT);
             
            return htpp;
        }

    	private void showProgressDialog() {
            
    		pDlg = new ProgressDialog(context);
            pDlg.setMessage("Wait...");
            pDlg.setCancelable(false);
            pDlg.show();

        }
    	
    }


	public static LocalDataBase getLocalDataBase() {
		return localDataBase;
	}   
    	
}