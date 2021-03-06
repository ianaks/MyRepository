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

public class TracksLocalListAdapter extends ArrayAdapter<Track> {

	private int textViewResourceId;
	
	public TracksLocalListAdapter(Context context, int textViewResourceId, List<Track> tracks) {
		super(context, textViewResourceId, tracks);
		this.textViewResourceId = textViewResourceId;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder = null;
		
		Track track = getItem(position);
		
		if (convertView == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = vi.inflate(textViewResourceId, null);
			
			holder = new ViewHolder();
			
			holder.track = (TextView) convertView.findViewById(R.id.localTrackTitle);
			holder.duration = (TextView) convertView.findViewById(R.id.localDuration);
			holder.artist = (TextView) convertView.findViewById(R.id.localArtist);
			holder.icon = (ImageView) convertView.findViewById(R.id.local_track_icon);
			holder.rating = (ImageView) convertView.findViewById(R.id.rating_image);
			convertView.setTag(holder);
			
		} else {
			holder = (ViewHolder)convertView.getTag();
		}
		
		if(track!=null){
			long hours = TimeUnit.MILLISECONDS.toHours(track.getTrackTimeMillis());
			long minutes = TimeUnit.MILLISECONDS.toMinutes(track.getTrackTimeMillis());
			long seconds = TimeUnit.MILLISECONDS.toSeconds(track.getTrackTimeMillis());
			
			if(hours!=0){
				holder.duration.setText("" + hours + ":" + minutes + ":" + Long.toString(seconds).substring(0, 2));
			} else 
				holder.duration.setText("" + minutes + ":" + Long.toString(seconds).substring(0, 2));
			
			
			if(track.getTrackName()!=null)
				holder.track.setText(track.getTrackName());
			if(track.getArtistName()!=null)
				holder.artist.setText(track.getArtistName());
			if(track.getArtworkUrl60()!=null){
				holder.icon.setImageBitmap(track.getArtworkUrl60());				
			}
			holder.rating.setVisibility(View.VISIBLE);
			if(track.getRating()==0)
				holder.rating.setBackgroundResource(R.drawable.rating_none);
			else if(track.getRating()==1)
				holder.rating.setBackgroundResource(R.drawable.rating_one);
			else if(track.getRating()==2)
				holder.rating.setBackgroundResource(R.drawable.rating_two);
			else if(track.getRating()==3)
				holder.rating.setBackgroundResource(R.drawable.rating_three);
			else if(track.getRating()==4)
				holder.rating.setBackgroundResource(R.drawable.rating_four);
			else if(track.getRating()==5)
				holder.rating.setBackgroundResource(R.drawable.rating_five);
		}
		return convertView;
	}
	
	private static class ViewHolder {
		private TextView track;
		private TextView artist;
		private TextView duration;
		private ImageView icon;
		private ImageView rating;
	}
	
}

