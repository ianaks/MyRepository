package test;

import com.testapplication.R;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

public class FragmentDetailActivity extends Activity {
	 String currentLink;
	    
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.fragment_detail_activity);
	        Bundle extras = getIntent().getExtras();
	        currentLink = extras.getString("selectedValue");
	    }	
	    
	    @Override
	    public void onConfigurationChanged(Configuration newConfig) {
	        super.onConfigurationChanged(newConfig);
	        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
	            Intent intent = new Intent(this, FragmentActivityMy.class);
	            intent.putExtra("selectedValue", currentLink);
	            startActivity(intent);
	        } 
	    }
}