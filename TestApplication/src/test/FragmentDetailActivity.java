package test;

import com.testapplication.R;
import com.testapplication.entity.Track;
import com.testapplication.utils.TrackDAO;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

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
        
        findViewById(R.id.layoutDetail).setVisibility(View.VISIBLE);
        
        context = this;
        
        FragmentDetail fragmentDetail = (FragmentDetail)getFragmentManager().findFragmentById(R.id.fragment_detail);
        
        fragmentDetail.fillDetail(track, type);
        final Button ratingButton = (Button)findViewById(R.id.ratingButton);
        
        if(type.equals("web"))
        	ratingButton.setVisibility(View.GONE);
        else
        	ratingButton.setVisibility(View.VISIBLE);
        
        ratingButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, FragmentRatingActivity.class);
				intent.putExtra("rating", trackDAO.getRatingById(track.getTrackId()));
				startActivityForResult(intent, 1);
			}
		});
        
    }	
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
	    if(resultCode==RESULT_OK && requestCode==1){
		    Bundle bundle = data.getExtras();
		    int rating = bundle.getInt("rating");
		    trackDAO.updateTracksRating(track.getTrackId(), rating);
	    }
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        	
        } 
    }
}