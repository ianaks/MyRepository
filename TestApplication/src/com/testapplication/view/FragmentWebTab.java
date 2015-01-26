package com.testapplication.view;

import com.testapplication.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentWebTab extends Fragment {
	
  @Override
      public View onCreateView(LayoutInflater inflater, ViewGroup container,
              Bundle savedInstanceState) {
          View web = inflater.inflate(R.layout.fragment_web_tab, container, false);
//          ((TextView)android.findViewById(R.id.textView)).setText("Android");
          return web;
}}
