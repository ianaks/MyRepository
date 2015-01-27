package test;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;
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

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class FragmentActivityMy extends FragmentActivity {
	
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
	    
	    // widgets
	    private EditText searchMusic;
	    
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
                if (event != null&& ((event.getKeyCode() == KeyEvent.KEYCODE_ENTER)||
                		(event.getKeyCode() == KeyEvent.ACTION_DOWN))) {
                   searchMusic(v.getText().toString()); 
                   
                   return true;

                }
                return false;
            }
        });
     	
     	setTabs();
     	
         
    	
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
			public void onTabSelected(Tab tab, FragmentTransaction ft) {
				// TODO Auto-generated method stub
				
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
        		"?term"+term+"&media=music"+"&entity=song");
        webServiceConnection.execute();
        try {
			Object result = webServiceConnection.get();
			if (!(result instanceof String)) {
				String jsonString;
				try {
					jsonString = EntityUtils.toString(((HttpResponse) result).getEntity());
				
					ObjectMapper objectMapper = new ObjectMapper();
					TypeFactory typeFactory = objectMapper.getTypeFactory();
					webTracks = objectMapper.readValue(jsonString, typeFactory.constructCollectionType(List.class, Track.class));
				} catch (JsonParseException e) {
				   e.printStackTrace();
				  } catch (JsonMappingException e) {
				   e.printStackTrace();
				  } catch (IOException e) {
				   e.printStackTrace();
				  }catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
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