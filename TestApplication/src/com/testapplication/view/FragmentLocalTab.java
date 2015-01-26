package com.testapplication.view;


import com.testapplication.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentLocalTab extends Fragment {
	
  @Override
      public View onCreateView(LayoutInflater inflater, ViewGroup container,
              Bundle savedInstanceState) {
          View local = inflater.inflate(R.layout.fragment_local_tab, container, false);
//          ((TextView)android.findViewById(R.id.textView)).setText("Android");
          return local;
}}