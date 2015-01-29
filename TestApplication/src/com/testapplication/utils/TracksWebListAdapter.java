package com.testapplication.utils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.testapplication.R;
import com.testapplication.entity.Track;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TracksWebListAdapter extends ArrayAdapter<Track> {

	private int textViewResourceId;
	
	public TracksWebListAdapter(Context context, int textViewResourceId, List<Track> tracks) {
		super(context, textViewResourceId, tracks);
		this.textViewResourceId = textViewResourceId;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = null;
		Track track = getItem(position);
		
		if (convertView == null) {
			
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = vi.inflate(textViewResourceId, null);
			
			ViewHolder holder = new ViewHolder();	
			
			
			holder.track = (TextView) view.findViewById(R.id.webTrackTitle);
			holder.duration = (TextView) view.findViewById(R.id.webDuration);
			holder.artist = (TextView) view.findViewById(R.id.webArtist);
			holder.icon = (ImageView) view.findViewById(R.id.web_track_icon);
			view.setTag(holder);
			
		} else {
			view = convertView;
		}
		
		ViewHolder holder = (ViewHolder)view.getTag();
		
		if(track!=null){
			long hours = TimeUnit.MILLISECONDS.toHours(track.getTrackTimeMillis());
			long minutes = TimeUnit.MILLISECONDS.toMinutes(track.getTrackTimeMillis());
			long seconds = TimeUnit.MILLISECONDS.toSeconds(track.getTrackTimeMillis());
			
			if(hours!=0){
				holder.duration.setText("" + hours + ":" + minutes + ":" + ((Long.toString(seconds).length()>2)?Long.toString(seconds).substring(0, 2):Long.toString(seconds)));
			} else 
				holder.duration.setText("" + minutes + ":" + ((Long.toString(seconds).length()>2)?Long.toString(seconds).substring(0, 2):Long.toString(seconds)));
			
			
			if(track.getTrackName()!=null)
				holder.track.setText(track.getTrackName());
			if(track.getArtistName()!=null)
				holder.artist.setText(track.getArtistName());
			if(track.getArtworkUrl60()!=null){
				holder.icon.setImageBitmap(track.getArtworkUrl60());				
			}
		}
		return view;
	}
	
	private static class ViewHolder {
		private TextView track;
		private TextView artist;
		private TextView duration;
		private ImageView icon;
	}
	
}

