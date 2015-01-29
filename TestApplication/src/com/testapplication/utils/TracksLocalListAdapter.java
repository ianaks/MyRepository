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
		ViewHolder viewHolder = null;
		
		if (convertView == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = vi.inflate(textViewResourceId, null);
			
			final ViewHolder holder = new ViewHolder();
			
			holder.track = (TextView) convertView.findViewById(R.id.localTrackTitle);
			holder.duration = (TextView) convertView.findViewById(R.id.localDuration);
			holder.artist = (TextView) convertView.findViewById(R.id.localArtist);
			holder.icon = (ImageView) convertView.findViewById(R.id.local_track_icon);
			holder.rating = (ImageView) convertView.findViewById(R.id.rating_image);
			convertView.setTag(holder);
			
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		Track track = getItem(position);
		
		if(track!=null){
			long hours = TimeUnit.MILLISECONDS.toHours(track.getTrackTimeMillis());
			long minutes = TimeUnit.MILLISECONDS.toMinutes(track.getTrackTimeMillis());
			long seconds = TimeUnit.MILLISECONDS.toSeconds(track.getTrackTimeMillis());
			
			if(hours!=0){
				viewHolder.duration.setText("" + hours + ":" + minutes + ":" + Long.toString(seconds).substring(0, 2));
			} else 
				viewHolder.duration.setText("" + minutes + ":" + Long.toString(seconds).substring(0, 2));
			
			
			if(track.getTrackName()!=null)
				viewHolder.track.setText(track.getTrackName());
			if(track.getArtistName()!=null)
				viewHolder.artist.setText(track.getArtistName());
			if(track.getArtworkUrl60()!=null){
				viewHolder.icon.setImageBitmap(track.getArtworkUrl60());				
			}
			viewHolder.rating.setVisibility(View.VISIBLE);
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

