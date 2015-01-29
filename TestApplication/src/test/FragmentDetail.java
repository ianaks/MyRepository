package test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.testapplication.R;
import com.testapplication.entity.Track;
import com.testapplication.utils.DownloadImageTask;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

public class FragmentDetail extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
  	Bundle savedInstanceState) {
  	  View view = inflater.inflate(R.layout.fragment_detail, container, false);
  	  return view;
    }
   
    public void fillDetail(Track track, String type){
    	if(track!=null){
    		ImageView imageView = (ImageView)getView().findViewById(R.id.image_detail);
    		if(track.getArtworkUrl100()!=null){
    			try {
					DownloadImageTask downloadImageTask = new DownloadImageTask();
					downloadImageTask.execute(track.getArtworkUrl100());
					
					imageView.setImageBitmap(downloadImageTask.get().get(0));	
					
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
    		}
    		
			
			TextView trackName = (TextView)getView().findViewById(R.id.trackTitleDetail);
			TextView artistTitle = (TextView)getView().findViewById(R.id.artistTitleDetail);
			TextView duration = (TextView)getView().findViewById(R.id.durationDetail);
			
			if(track.getTrackName()!=null)
				trackName.setText(track.getTrackName());
			if(track.getArtistName()!=null)
				artistTitle.setText(track.getArtistName());
			
			long hours = TimeUnit.MILLISECONDS.toHours(track.getTrackTimeMillis());
			long minutes = TimeUnit.MILLISECONDS.toMinutes(track.getTrackTimeMillis());
			long seconds = TimeUnit.MILLISECONDS.toSeconds(track.getTrackTimeMillis());
			
			if(hours!=0){
				duration.setText("" + hours + ":" + minutes + ":" + ((Long.toString(seconds).length()>2)?Long.toString(seconds).substring(0, 2):Long.toString(seconds)));
			} else 
				duration.setText("" + minutes + ":" + ((Long.toString(seconds).length()>2)?Long.toString(seconds).substring(0, 2):Long.toString(seconds)));
			
    	}
    }
}
