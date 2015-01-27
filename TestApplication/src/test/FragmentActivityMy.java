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
import com.testapplication.utils.TabPagerAdapter;
import com.testapplication.utils.TrackDAO;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class FragmentActivityMy extends FragmentActivity {
	
	// both fragments are on the screen
		private boolean mTwoPane;
		
		// list of local tracks
		private Track localTracks;
		// list of web tracks
		private List<Track> webTracks;
		
		// local data base
		private LocalDataBase localDataBase;
		
		// data access object
		private TrackDAO trackDAO;
		
		// instances for tab using
		private ViewPager tab;
	    private TabPagerAdapter tabAdapter;
	    private ActionBar actionBar;
	    
	    // widgets
	    private EditText searchMusic;
	    private ListView listView;
	    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.fragment);
    	
    	localDataBase = new LocalDataBase(this, 1);
    	//
     	trackDAO = new TrackDAO(localDataBase.getWritableDatabase());
     	
     	searchMusic = (EditText)findViewById(R.id.searchEdit);
     	
    	searchMusic.setOnEditorActionListener(new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                    KeyEvent event) {
                if (event != null&&event.getKeyCode() == KeyEvent.ACTION_DOWN) {
                   searchMusic(v.getText().toString()); 
                   
                   return true;

                }
                return false;
            }
        });
     	
     	setTabs();
     	
     	listView = (ListView)findViewById(R.id.lstLocalTracks);
     	
     	listView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
			}
     		
			});
     	
     	
     	
         
    	
    }
    
    private void setTabs(){
    	tabAdapter = new TabPagerAdapter(getSupportFragmentManager());
        tab = (ViewPager)findViewById(R.id.pager);
        tab.setOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                      actionBar = getActionBar();
                      actionBar.setSelectedNavigationItem(position);                    }
                });
        tab.setAdapter(tabAdapter);
        actionBar = getActionBar();
        //Enable Tabs on Action Bar
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        ActionBar.TabListener tabListener = new ActionBar.TabListener(){

			@Override
			public void onTabSelected(Tab tab2, FragmentTransaction ft) {
				tab.setCurrentItem(tab2.getPosition());				
			}
			@Override
			public void onTabUnselected(Tab tab, FragmentTransaction ft) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void onTabReselected(Tab tab, FragmentTransaction ft) {
				// TODO Auto-generated method stub
			
		}};
      //Add New Tab
      actionBar.addTab(actionBar.newTab().setText("Web").setTabListener(tabListener));
      actionBar.addTab(actionBar.newTab().setText("Local").setTabListener(tabListener));
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
        		"?term="+term+"&media=music"+"&entity=song", this);
    	Log.d("", getResources().getString(R.string.web_service_url) +
        		"?term="+term+"&media=music"+"&entity=song");
        webServiceConnection.execute();
        try {
			String result = webServiceConnection.get();
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
						track.setArtworkUrl100(jsonArray.getJSONObject(i).getString("artworkUrl100"));
						track.setArtworkUrl60(jsonArray.getJSONObject(i).getString("artworkUrl60"));
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
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}