package test;

import java.util.ArrayList;
import java.util.LinkedList;
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
import com.testapplication.utils.TracksListAdapter;
import com.testapplication.view.FragmentLocalTab;
import com.testapplication.view.FragmentWebTab;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.ListFragment;
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
		private List<Track> localTracks;
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
	    
	    private FragmentLocalTab fragmentLocalTab;
	    private FragmentWebTab fragmentWebTab;
	    
	    // widgets
	    private EditText searchMusic;
	    private ListView lstWeb;
	    private ListView lstLocal;
	    private TextView emptyWeb;
	    private TextView emptyLocal;
	    
	    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.fragment);
    	
    	localDataBase = new LocalDataBase(this, 1);
    	//
     	trackDAO = new TrackDAO(localDataBase.getWritableDatabase());
     	
     	searchMusic = (EditText)findViewById(R.id.searchEdit);
     	
     	localTracks = trackDAO.getAllTracksFromLocalDB();
     	
     	fragmentWebTab = new FragmentWebTab();
     	fragmentLocalTab = new FragmentLocalTab();
     	
     	setTabs();
     	
    	searchMusic.setOnEditorActionListener(new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                    KeyEvent event) {
                if (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                	if(tab.getCurrentItem()==0){
                		searchMusic(v.getText().toString()); 
                		fragmentWebTab.setList(webTracks, "web");
                	} else{
                		localTracks = trackDAO.getAllTracksFromLocalDB();
                		fragmentLocalTab.setList(localTracks, "local");
                	}
                   
                   return true;

                }
                return false;
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

				    switch (tab2.getPosition() + 1)
				    {
				    case 1:
				    	getSupportFragmentManager().beginTransaction()
			            .replace(R.id.pager, fragmentWebTab)
			            .commit();
				        break;

				    case 2:
				    	getSupportFragmentManager().beginTransaction()
			            .replace(R.id.pager, fragmentLocalTab)
			            .commit();      
				        break;
				    }
				    
				     
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
    
    public class TabPagerAdapter extends FragmentStatePagerAdapter {
    	
        public TabPagerAdapter(FragmentManager fm) {
    	    super(fm);
    	    // TODO Auto-generated constructor stub
    	  }
    	    
    	  @Override
    	  public Fragment getItem(int i) {
    	    switch (i) {
    	        case 0:
    	            //Fragement for Web Tab
    	            return new FragmentWebTab();
    	        case 1:
    	           //Fragment for Local Tab
    	            return new FragmentLocalTab();
    	        }
    	    return null;
    	  }
    	  @Override
    	  public int getCount() {
    	    // TODO Auto-generated method stub
    	    return 2; //No of Tabs
    	  }
        }
	
	
}