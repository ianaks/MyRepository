package test;

import com.testapplication.R;
import com.testapplication.database.LocalDataBase;
import com.testapplication.entity.Track;
import com.testapplication.utils.TrackDAO;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RatingBar;

public class FragmentRatingActivity extends Activity {
	 String currentLink;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_rating_activity);
        Bundle extras = getIntent().getExtras();
        final int rating = (int)extras.getInt("rating");
        
        FragmentRating fragmentRating = (FragmentRating)getFragmentManager().findFragmentById(R.id.fragment_detail);
        
        fragmentRating.fillData(rating);
        
        final RatingBar ratingBar = (RatingBar)findViewById(R.id.ratingBar);
        Button saveButton = (Button)findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v){
				int rating = (int) ratingBar.getRating();	
				Intent intent = getIntent();
				intent.putExtra("rating", rating);
				setResult(RESULT_OK, intent);
				finish();				
			}
		});
        
    }	
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        	
        } 
    }
}