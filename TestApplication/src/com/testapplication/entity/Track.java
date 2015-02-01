package com.testapplication.entity;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class Track implements android.os.Parcelable {

	private int trackId;
	private String artistName;
	private String trackName;
	private int trackTimeMillis;
	private String artworkUrl100;
	private String artworkUrl60String;
	private Bitmap artworkUrl60;
	private int rating;
	
	public Track() {
		
	}
	
	public Track(int trackId, String artistName,
			String trackName, int trackTimeMillis, String artworkUrl60String,
			String artworkUrl100, int rating){
		this.trackId = trackId;
		this.artistName = artistName;
		this.trackName = trackName;
		this.trackTimeMillis = trackTimeMillis;
		this.artworkUrl60String = artworkUrl60String;
		this.artworkUrl100 = artworkUrl100;
		this.rating = rating;
		
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

	public Bitmap getArtworkUrl60() {
		return artworkUrl60;
	}

	public void setArtworkUrl60(Bitmap artworkUrl60) {
		this.artworkUrl60 = artworkUrl60;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public String getArtworkUrl60String() {
		return artworkUrl60String;
	}

	public void setArtworkUrl60String(String artworkUrl60String) {
		this.artworkUrl60String = artworkUrl60String;
	}

	@Override
	public int describeContents() {
		return 0;
		
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(trackId);
		dest.writeString(artistName);
		dest.writeString(trackName);
		dest.writeInt(trackTimeMillis);	
		dest.writeString(artworkUrl60String);
		dest.writeString(artworkUrl100);
		dest.writeInt(rating);
	}	
	
	 private Track(Parcel in) {
		 trackId = in.readInt();
         artistName = in.readString();
         trackName = in.readString();
         trackTimeMillis = in.readInt();
         artworkUrl60String = in.readString();
         artworkUrl100 = in.readString();
         rating = in.readInt();
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