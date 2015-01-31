package com.testapplication.utils;

import java.util.List;

import com.testapplication.entity.Track;

public class LocalTracksFilter {
	
	public static void filter(List<Track> webTracks, List<Track> localTracks){
		for(int i = 0; i<webTracks.size(); i++){
			for(int j = 0; j<localTracks.size(); j++){
				if(webTracks.get(i).getTrackId()==localTracks.get(j).getTrackId()){
					webTracks.remove(i);
					i--;
					break;
				}
			}
		}
	}
	
}
