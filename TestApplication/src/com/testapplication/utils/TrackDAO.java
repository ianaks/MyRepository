package com.testapplication.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.testapplication.database.LocalDataBase;
import com.testapplication.entity.Track;

public class TrackDAO {
	
	private SQLiteDatabase sqLiteDatabase;
	
	public TrackDAO(SQLiteDatabase sqLiteDatabase){
		this.sqLiteDatabase = sqLiteDatabase;
	}
	
	public String insertTrackInLocalDB(int trackId, String artistName, 
    		String trackName, int trackTimeMillis, String artworkUrl60,
	        String artworkUrl100){
		if(getTrackById(trackId)==0){
			try{
		    	String insertQuery = "INSERT INTO " + 
		    		    LocalDataBase.TABLE_NAME + 
		    		     " (" + LocalDataBase.TRACKID + "," + LocalDataBase.ARTISTNAME+ "," + LocalDataBase.TRACKNAME+ "," +
		    		     LocalDataBase.TRACKTIMEMILLIS+ "," + LocalDataBase.ARTWORKURL60+ "," + 
		    		     LocalDataBase.ARTWORKURL100+ "," + LocalDataBase.RATING + ")"
		    		     		+ " VALUES (" + trackId + ",?,?," + trackTimeMillis + ",?,?, 0)";
		    	Log.v("db", insertQuery);
		    	this.sqLiteDatabase.rawQuery(insertQuery, new String[] { artistName, trackName, 
		    			artworkUrl60, artworkUrl100});
			}catch(SQLiteException e){
				return e.getMessage();
			}
	    	return "OK";
		} else return "EXISTS";
    }
	
	public int getTrackById(int trackId){
		
    	String selectQuery = "SELECT * FROM " + 
    		    LocalDataBase.TABLE_NAME + 
    		     " WHERE " + LocalDataBase.TRACKID + "=" + trackId;
    	Cursor cursor =  this.sqLiteDatabase.rawQuery(selectQuery, null);
    	if(cursor!=null && cursor.getCount()!=0){
    		cursor.close();
    		return 1;
    	} else return 0;
    			   
    }
	
	public List<Track> getTrackByName(String s){
		List<Track> listTracks = new ArrayList<Track>();
    	String selectQuery = "SELECT * FROM " + 
    		    LocalDataBase.TABLE_NAME + 
    		     " WHERE " + LocalDataBase.TRACKNAME + " LIKE '%" + s + "%'"
    		     + " OR " + LocalDataBase.ARTISTNAME + " LIKE '%" + s + "%'";
    	Cursor cursor =  this.sqLiteDatabase.rawQuery(selectQuery, null);
		while (cursor.moveToNext()) {
			Track track = new Track();
			track.setArtistName(cursor.getString(cursor.getColumnIndex(LocalDataBase.ARTISTNAME)));
			track.setArtworkUrl100(cursor.getString(cursor.getColumnIndex(LocalDataBase.ARTWORKURL100)));
			track.setArtworkUrl60String(cursor.getString(cursor.getColumnIndex(LocalDataBase.ARTWORKURL60)));
			try {
				DownloadImageTask downloadImageTask = new DownloadImageTask();
				downloadImageTask.execute(track.getArtworkUrl60String());
				
				track.setArtworkUrl60(downloadImageTask.get().get(0));	
				
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			track.setRating(cursor.getInt(cursor.getColumnIndex(LocalDataBase.RATING)));
			track.setTrackId(cursor.getInt(cursor.getColumnIndex(LocalDataBase.TRACKID)));
			track.setTrackName(cursor.getString(cursor.getColumnIndex(LocalDataBase.TRACKNAME)));
			track.setTrackTimeMillis(cursor.getInt(cursor.getColumnIndex(LocalDataBase.TRACKTIMEMILLIS)));
			listTracks.add(track);
		}
		cursor.close();
		return listTracks;
    			   
    }
    
    public List<Track> getAllTracksFromLocalDB(){
    	List<Track> listTracks = new ArrayList<Track>();
    	Cursor cursor = sqLiteDatabase.query(LocalDataBase.TABLE_NAME, null, null, 													// columns
				null, null, null, null);
		while (cursor.moveToNext()) {
			Track track = new Track();
			track.setArtistName(cursor.getString(cursor.getColumnIndex(LocalDataBase.ARTISTNAME)));
			track.setArtworkUrl100(cursor.getString(cursor.getColumnIndex(LocalDataBase.ARTWORKURL100)));
			track.setArtworkUrl60String(cursor.getString(cursor.getColumnIndex(LocalDataBase.ARTWORKURL60)));
			try {
				DownloadImageTask downloadImageTask = new DownloadImageTask();
				downloadImageTask.execute(track.getArtworkUrl60String());
				
				track.setArtworkUrl60(downloadImageTask.get().get(0));	
				
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
    	try{
	    	String updateQuery = "UPDATE " + 
	    		    LocalDataBase.TABLE_NAME + 
	    		     " SET " + LocalDataBase.RATING + "="
	    		     		 + rating 
	    		     		 + " WHERE " + LocalDataBase.TRACKID + "=" + trackId;
	    		    sqLiteDatabase.execSQL(updateQuery);
    	} catch(SQLiteException e){
    		Log.v("SQLException", e.getMessage());
    	}
    }
    
    public boolean removeItem(int trackId){
    	try{
    		 return sqLiteDatabase.delete(LocalDataBase.TABLE_NAME, LocalDataBase.TRACKID + "=" + trackId, null) > 0;    		
    	} catch(SQLiteException e){
    		Log.v("SQLException", e.getMessage());
    		return false;
    	}
    }

}
