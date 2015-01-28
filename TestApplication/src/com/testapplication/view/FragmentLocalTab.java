package com.testapplication.view;


import java.util.LinkedList;
import java.util.List;

import test.FragmentActivityMy;
import test.FragmentDetail;
import test.FragmentDetailActivity;

import com.testapplication.R;
import com.testapplication.entity.Track;
import com.testapplication.utils.TracksListAdapter;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class FragmentLocalTab extends Fragment {
	
	ListView lstTracks;
	
	TextView empty;
	
	View local;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
              Bundle savedInstanceState) {
          local = inflater.inflate(R.layout.fragment_local_tab, container, false);
          return local;
    }
  
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
      // TODO Auto-generated method stub
      super.onViewCreated(view, savedInstanceState);
      
      lstTracks = (ListView) getView().findViewById(R.id.lstLocalTracks);

      empty = (TextView) getView().findViewById(R.id.empty_local);
      
      local = getView();
      
      empty.setVisibility(View.VISIBLE);
      lstTracks.setVisibility(View.GONE);

   }
    
    public void setList(final List<Track> tracks, String type) {
		List<Track> list = tracks;
			
		if (list == null) {
			list = new LinkedList<Track>();
		}
		
		TextView empty = (TextView)getView().findViewById(R.id.empty_local);
		ListView lstTracks;
			lstTracks = (ListView) getView().findViewById(R.id.lstLocalTracks);
		if(list.size() > 0){

			lstTracks.setOnItemClickListener(new OnItemClickListener() {			
				@Override
				public void onItemClick(AdapterView<?> arg0, View view,
						int position, long id) {
					FragmentDetail fragment = new FragmentDetail();
			     	 if (fragment != null && fragment.isInLayout()) {
			     			fragment.fillDetail(tracks.get(position));
			     	 } else {
			     		Intent intentDettaglio = new Intent(view.getContext(), FragmentDetailActivity.class);

						intentDettaglio.putExtra("type", "local");
						intentDettaglio.putExtra("track", tracks.get(position));
						
						startActivity(intentDettaglio);
			          }
				}
			});
			TracksListAdapter adapter = new TracksListAdapter(getView().getContext(),  R.layout.list_item_track, tracks, "local");
			lstTracks.setAdapter(adapter);
			empty.setVisibility(View.GONE);
			lstTracks.setVisibility(View.VISIBLE);
		} else {
			empty.setVisibility(View.VISIBLE);
			list.clear();
			lstTracks.setVisibility(View.GONE);
		}
 	   
 	}
  
}