package test;

import com.testapplication.R;
import com.testapplication.database.LocalDataBase;
import com.testapplication.entity.Track;
import com.testapplication.utils.TrackDAO;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RatingBar;

public class FragmentDetailActivity extends Activity {
	 String currentLink;
	 Context context;
	 
	// data access object
	private TrackDAO trackDAO;
	
	private Track track;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_detail_activity);
        
        trackDAO = new TrackDAO(FragmentActivityMy.getLocalDataBase().getWritableDatabase());
        Bundle extras = getIntent().getExtras();
        track = (Track)extras.getParcelable("track");
        String type = extras.getString("type"); 
        
        context = this;
        
        FragmentDetail fragmentDetail = (FragmentDetail)getFragmentManager().findFragmentById(R.id.fragment_detail);
        
        fragmentDetail.fillDetail(track, type);
        
        final RatingBar ratingBar = (RatingBar)findViewById(R.id.ratingBar);
        ratingBar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, FragmentRatingActivity.class);
				intent.putExtra("rating", track.getRating());
				startActivityForResult(intent, 0);
			}
		});
        
    }	
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
	    if(resultCode==RESULT_OK && requestCode==0){
	    Bundle bundle = data.getExtras();
	    int rating = bundle.getInt("rating");
	    trackDAO.updateTracksRating(track.getTrackId(), rating);
    }
    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        	
        } 
    }
}