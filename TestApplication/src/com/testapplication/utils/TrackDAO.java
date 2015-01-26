package com.testapplication.utils;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.testapplication.database.LocalDataBase;
import com.testapplication.entity.Track;

public class TrackDAO {
	
	private SQLiteDatabase sqLiteDatabase;
	
	public TrackDAO(SQLiteDatabase sqLiteDatabase){
		this.sqLiteDatabase = sqLiteDatabase;
	}
	
	public void insertTrackInLocalDB(int trackId, String artistName, 
    		String trackName, int trackTimeMillis, String artworkUrl60,
	        String artworkUrl100, int rating){
    	String insertQuery = "INSERT INTO " + 
    		    LocalDataBase.TABLE_NAME + 
    		     " (" + LocalDataBase.TRACKID + LocalDataBase.ARTISTNAME + LocalDataBase.TRACKNAME +
    		     LocalDataBase.TRACKTIMEMILLIS + LocalDataBase.ARTWORKURL60 + 
    		     LocalDataBase.ARTWORKURL100 + LocalDataBase.RATING + ")"
    		     		+ " VALUES (" + trackId + ",'" + artistName + "','" 
    		     		+ trackName + "'," + trackTimeMillis + ",'" + 
    		     		artworkUrl60 + "','" + artworkUrl100 + "'," 
    		     		+ rating + ")";
    		    sqLiteDatabase.execSQL(insertQuery);
    }
    
    public List<Track> getAllTracksFromLocalDB(){
    	List<Track> listTracks = new ArrayList<Track>();
    	Cursor cursor = sqLiteDatabase.query(LocalDataBase.TABLE_NAME, null, null, 													// columns
				null, null, null, null);
		while (cursor.moveToNext()) {
			Track track = new Track();
			track.setArtistName(cursor.getString(cursor.getColumnIndex(LocalDataBase.ARTISTNAME)));
			track.setArtworkUrl100(cursor.getString(cursor.getColumnIndex(LocalDataBase.ARTWORKURL100)));
			track.setArtworkUrl60(cursor.getString(cursor.getColumnIndex(LocalDataBase.ARTWORKURL60)));
			track.setRating(cursor.getInt(cursor.getColumnIndex(LocalDataBase.RATING)));
			track.setTrackId(cursor.getInt(cursor.getColumnIndex(LocalDataBase.TRACKID)));
			track.setTrackName(cursor.getString(cursor.getColumnIndex(LocalDataBase.TRACKNAME)));
			track.setTrackTimeMillis(cursor.getInt(cursor.getColumnIndex(LocalDataBase.TRACKTIMEMILLIS)));
			listTracks.add(track);
		}
		cursor.close();
		return listTracks;
    }
    
    public void updateTracksRating(int trackId, int rating){
    	String updateQuery = "UPDATE TABLE " + 
    		    LocalDataBase.TABLE_NAME + 
    		     "SET" + LocalDataBase.RATING + "="
    		     		 + rating 
    		     		 + "WHERE" + LocalDataBase.TRACKID + "=" + trackId;
    		    sqLiteDatabase.execSQL(updateQuery);
    }

}
