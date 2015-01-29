package test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.ParseException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.testapplication.R;
import com.testapplication.database.LocalDataBase;
import com.testapplication.entity.Track;
import com.testapplication.service.WebServiceConnection;
import com.testapplication.utils.DownloadImageTask;
import com.testapplication.utils.TrackDAO;
import com.testapplication.utils.TracksLocalListAdapter;
import com.testapplication.utils.TracksWebListAdapter;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
		private LocalDataBase localDataBase;
		
		// data access object
		private TrackDAO trackDAO;
		
	    // widgets
	    private EditText searchMusic;
	    private Button buttonWeb;
	    private Button buttonLocal;
	    private ListView lstWebTracks;
	    private ListView lstLocalTracks;
	    private TextView empty;
	    
	    private boolean webPressed = true;
	    
	    // Result of WebServiceConnection
	    private static String result;
	    
	    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.fragment);
    	
    	localDataBase = new LocalDataBase(this, 1);
    	//
     	trackDAO = new TrackDAO(localDataBase.getWritableDatabase());
     	
     	searchMusic = (EditText)findViewById(R.id.searchEdit);
     	
     	localTracks = trackDAO.getAllTracksFromLocalDB();
     	
     	lstWebTracks = (ListView)findViewById(R.id.lstWebTracks);
     	
     	lstLocalTracks = (ListView)findViewById(R.id.lstLocalTracks);
     	
     	buttonWeb = (Button)findViewById(R.id.buttonWeb);
     	
     	buttonLocal = (Button)findViewById(R.id.buttonLocal);
     	
     	empty = (TextView)findViewById(R.id.empty);
     	
//     	fragmentWebTab = new FragmentWebTab();
//     	fragmentLocalTab = new FragmentLocalTab();
     	
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
			}
		});
     	  	
     	searchMusic.setOnEditorActionListener(new OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                	if(webPressed==true){
                		searchMusic(v.getText().toString()); 
                		setList(webTracks, "web");
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
        		"?term="+term+"&media=music&entity=song", this);
    	Log.d("", getResources().getString(R.string.web_service_url) +
        		"?term="+term+"&media=music"+"&entity=song");
        webServiceConnection.execute();
        if (result!=null) {
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
			
		} else {
			showAlertDialog("Server is temporarily unavailable");
		}
    }
    
    public void setList(List<Track> tracks, final String type) {
		final List<Track> listTracks = tracks;	
		
		ListView lstTracks;
		if(type.equals("web"))
			lstTracks = lstWebTracks;
		else 
			lstTracks = lstLocalTracks;
		if(listTracks.size() > 0){

			lstTracks.setOnItemClickListener(new OnItemClickListener() {			
				@Override
				public void onItemClick(AdapterView<?> arg0, View view,
						int position, long id) {
					FragmentDetail fragment = (FragmentDetail)getFragmentManager().findFragmentById(R.id.fragment_detail);
			     	 if (fragment != null) {
			     			fragment.fillDetail(listTracks.get(position));
			     	 } else {
			     		Intent intentDettaglio = new Intent(view.getContext(), FragmentDetailActivity.class);
			     		if(type.equals("web"))
			     			intentDettaglio.putExtra("type", "web");
			     		else 
			     			intentDettaglio.putExtra("type", "local");
						intentDettaglio.putExtra("track", listTracks.get(position));
						
						startActivity(intentDettaglio);
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
    
    public static void setWebList(String newResult){
    	result = newResult;
    }
	
}