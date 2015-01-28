package test;

import com.testapplication.R;

import android.app.Fragment;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class FragmentList extends Fragment {
	 @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	  	Bundle savedInstanceState) {
	  	  View view = inflater.inflate(R.layout.fragment_list, container, false);
	  	  return view;
	    }   
	 
	 
}