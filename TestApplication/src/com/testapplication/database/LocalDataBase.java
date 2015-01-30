package com.testapplication.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class LocalDataBase extends SQLiteOpenHelper {

    public final static String DB_NAME = "localDB.db";
    public final static String TABLE_NAME = "tracks";
    Context context;
    
	// table fields
	public static final String TRACKID = "trackid";
	public static final String TRACKNAME = "trackname";
	public static final String ARTISTNAME = "artistname";
	public static final String TRACKTIMEMILLIS = "trackTimeMillis";
	public static final String ARTWORKURL100 = "artworkUrl100";
	public static final String ARTWORKURL60 = "artworkUrl60";
	public static final String RATING = "rating";
	
	final static String VARCHAR = "varchar";
	final static String INT = "int";
	
	//SQL entries
    final static String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
    		"(" + TRACKID + " " + INT + "," + 
    		TRACKNAME + " " + VARCHAR + "(255)," +
    		ARTISTNAME + " " +VARCHAR + "(255)," +
    		TRACKTIMEMILLIS + " " + INT + "," +
    		ARTWORKURL100 + " " + VARCHAR + "(255)," +
    		ARTWORKURL60 + " " + VARCHAR + "(255)," +
    		RATING + " " + INT + ")";
    final static String DROP_TABLE = "DROP TABLE IF EXISTS" + TABLE_NAME;
	

	public LocalDataBase(Context context, int dbVersion) {
		super(context, DB_NAME, null, dbVersion);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.v("db", CREATE_TABLE);
		db.execSQL(CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
	    onCreate(db);
	}
	
}
