package com.testapplication.view;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.TypeFactory;

import com.testapplication.R;
import com.testapplication.database.LocalDataBase;
import com.testapplication.entity.Track;
import com.testapplication.service.WebServiceConnection;
import com.testapplication.utils.TabPagerAdapter;
import com.testapplication.utils.TrackDAO;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;



public class ActivityMain extends FragmentActivity {
	
	// both fragments are on the screen
	private boolean mTwoPane;
	
	// list of local tracks
	private Track localTracks;
	// list of web tracks
	private Track webTracks;
	
	// local data base
	private LocalDataBase localDataBase;
	
	// data access object
	private TrackDAO trackDAO;
	
	// instances for tab using
	private ViewPager tab;
    private TabPagerAdapter tabAdapter;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment);
         
//        mTwoPane = true;
// 
//        ((CircleListFragment) getSupportFragmentManager().findFragmentById(
//                R.id.circle_list)).setActivateOnItemClick(true);
        FragmentListDetail fragment = new FragmentListDetail();
        
        // In case this activity was started with special instructions from an
        // Intent, pass the Intent's extras to the fragment as arguments
        fragment.setArguments(getIntent().getExtras());
        
        // Add the fragment to the 'fragment_container' FrameLayout
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_detail, fragment).commit();
        // data base initializing
//     	localDataBase = new LocalDataBase(this, 1);
//
//     	trackDAO = new TrackDAO(localDataBase.getWritableDatabase());
//     	
//     	 setContentView(R.layout.activity_list);
//         tabAdapter = new TabPagerAdapter(getSupportFragmentManager());
//         tab = (ViewPager)findViewById(R.id.pager);
//         tab.setOnPageChangeListener(
//                 new ViewPager.SimpleOnPageChangeListener() {
//                     @Override
//                     public void onPageSelected(int position) {
//                       actionBar = getActionBar();
//                       actionBar.setSelectedNavigationItem(position);                    }
//                 });
//         tab.setAdapter(tabAdapter);
//         actionBar = getActionBar();
//         //Enable Tabs on Action Bar
//         actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
//         ActionBar.TabListener tabListener = new ActionBar.TabListener(){
// 	      @Override
// 	      public void onTabReselected(android.app.ActionBar.Tab tab,
// 	          FragmentTransaction ft) {
// 	        // TODO Auto-generated method stub
// 	      }
// 	      @Override
// 	       public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
//// 	              Tab.setCurrentItem(tab.getPosition());
// 	          }
// 	      @Override
// 	      public void onTabUnselected(android.app.ActionBar.Tab tab,
// 	          FragmentTransaction ft) {
// 	        // TODO Auto-generated method stub
// 	      }};
//       //Add New Tab
//       actionBar.addTab(actionBar.newTab().setText("Web").setTabListener(tabListener));
//       actionBar.addTab(actionBar.newTab().setText("Local").setTabListener(tabListener));
     	
     	
        
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
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
        		"?term"+term+"&media=music"+"&entity=song", this);
        webServiceConnection.execute();
        try {
			Object result = webServiceConnection.get();
			if (!(result instanceof Exception)) {
				String jsonString = (String) result;
			
				try{
					ObjectMapper objectMapper = new ObjectMapper();
					TypeFactory typeFactory = objectMapper.getTypeFactory();
					webTracks = objectMapper.readValue(jsonString, typeFactory.constructCollectionType(List.class, Track.class));
				} catch (JsonParseException e) {
				   e.printStackTrace();
				  } catch (JsonMappingException e) {
				   e.printStackTrace();
				  } catch (IOException e) {
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
