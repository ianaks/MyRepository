package test;

import com.testapplication.R;
import com.testapplication.entity.Track;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FragmentDetailActivity extends Activity {
	 String currentLink;
	    
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.fragment_detail_activity);
	        Bundle extras = getIntent().getExtras();
	        Track track = (Track)extras.getParcelable("track");
	        String type = extras.getString("type");
	        
	        Button button = (Button)findViewById(R.id.ratingButton);
	        if(type.equals("web"))
	        	button.setVisibility(View.GONE);
	        else
	        	button.setVisibility(View.VISIBLE);
	        
	        
	    }	
	    
	    @Override
	    public void onConfigurationChanged(Configuration newConfig) {
	        super.onConfigurationChanged(newConfig);
	        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
	        	
	        } 
	    }
}