package com.testapplication.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class Track implements android.os.Parcelable {

	private int trackId;
	private String artistName;
	private String trackName;
	private int trackTimeMillis;
	private String artworkUrl100;
	private String artworkUrl60;
	private int rating;
	
	public Track() {
		
	}
	
	public Track(int artistId, int trackId, String artistName,
			String trackName, int trackTimeMillis){
		this.trackId = trackId;
		this.artistName = artistName;
		this.trackName = trackName;
		this.trackTimeMillis = trackTimeMillis;
		
	}
	
	public int getTrackId() {
		return trackId;
	}
	
	public void setTrackId(int trackId) {
		this.trackId = trackId;
	}
	
	public String getArtistName() {
		return artistName;
	}
	
	public void setArtistName(String artistName) {
		this.artistName = artistName;
	}
	
	public String getTrackName() {
		return trackName;
	}
	
	public void setTrackName(String trackName) {
		this.trackName = trackName;
	}
	
	public int getTrackTimeMillis() {
		return trackTimeMillis;
	}
	
	public void setTrackTimeMillis(int trackTimeMillis) {
		this.trackTimeMillis = trackTimeMillis;
	}
	
	public String getArtworkUrl100() {
		return artworkUrl100;
	}

	public void setArtworkUrl100(String artworkUrl100) {
		this.artworkUrl100 = artworkUrl100;
	}

	public String getArtworkUrl60() {
		return artworkUrl60;
	}

	public void setArtworkUrl60(String artworkUrl60) {
		this.artworkUrl60 = artworkUrl60;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	@Override
	public int describeContents() {
		return 0;
		
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeValue(trackId);
		dest.writeValue(artistName);
		dest.writeValue(trackName);
		dest.writeValue(trackTimeMillis);	
		dest.writeValue(artworkUrl60);
		dest.writeValue(artworkUrl100);
	}	
	
	 private Track(Parcel in) {
         artistName = in.readString();
         artworkUrl100 = in.readString();
         artworkUrl60 = in.readString();
         rating = in.readInt();
         trackId = in.readInt();
         trackName = in.readString();
         trackTimeMillis = in.readInt();
     }
	
	public static final Parcelable.Creator<Track> CREATOR
		    = new Parcelable.Creator<Track>() {
		public Track createFromParcel(Parcel in) {
		    return new Track(in);
		}
		
		public Track[] newArray(int size) {
		    return new Track[size];
		}
		};
	
}