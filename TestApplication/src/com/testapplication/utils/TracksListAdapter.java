package com.testapplication.utils;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.testapplication.R;
import com.testapplication.entity.Track;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TracksListAdapter extends ArrayAdapter<Track> {

	private int textViewResourceId;
	private String typeOfList;
	
	public TracksListAdapter(Context context, int textViewResourceId, List<Track> tracks,
			String typeOfList) {
		super(context, textViewResourceId, tracks);
		this.textViewResourceId = textViewResourceId;
		this.typeOfList = typeOfList;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = null;
		
		if (convertView == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = vi.inflate(textViewResourceId, null);
			
			final ViewHolder holder = new ViewHolder();
			
			holder.track = (TextView) view.findViewById(R.id.trackTitle);
			holder.duration = (TextView) view.findViewById(R.id.duration);
			holder.artist = (TextView) view.findViewById(R.id.artist);
			holder.icon = (ImageView) view.findViewById(R.id.track_icon);
			holder.rating = (ImageView) view.findViewById(R.id.rating_image);
			view.setTag(holder);
			
		} else {
			view = convertView;
		}
		
		ViewHolder holder = (ViewHolder)view.getTag();
		
		Track track = getItem(position);
		
		if(track!=null){
			long hours = TimeUnit.MILLISECONDS.toHours(track.getTrackTimeMillis());
			long minutes = TimeUnit.MILLISECONDS.toMinutes(track.getTrackTimeMillis());
			long seconds = TimeUnit.MILLISECONDS.toSeconds(track.getTrackTimeMillis());
			
			if(hours!=0){
				holder.duration.setText("" + hours + ":" + minutes + ":" + seconds);
			} else 
				holder.duration.setText("" + minutes + ":" + seconds);
			
			
			if(track.getTrackName()!=null)
				holder.track.setText(track.getTrackName());
			if(track.getArtistName()!=null)
				holder.artist.setText(track.getArtistName());
			if(track.getArtworkUrl60()!=null && track.getArtworkUrl60().indexOf(".jpg")!=-1){
				DownloadImageTask downloadImageTask = new DownloadImageTask();
				downloadImageTask.execute(track.getArtworkUrl60());
				try {
					holder.icon.setImageBitmap(downloadImageTask.get());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			if(typeOfList.equals("local")){
				holder.rating.setVisibility(View.VISIBLE);
			} else
				holder.rating.setVisibility(View.GONE);
		}
		return view;
	}
	
	private static class ViewHolder {
		private TextView track;
		private TextView artist;
		private TextView duration;
		private ImageView icon;
		private ImageView rating;
	}
	
}

