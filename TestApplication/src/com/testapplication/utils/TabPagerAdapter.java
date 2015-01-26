package com.testapplication.utils;

import com.testapplication.view.FragmentLocalTab;
import com.testapplication.view.FragmentWebTab;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class TabPagerAdapter extends FragmentStatePagerAdapter {
	
    public TabPagerAdapter(FragmentManager fm) {
	    super(fm);
	    // TODO Auto-generated constructor stub
	  }
	    
	  @Override
	  public Fragment getItem(int i) {
	    switch (i) {
	        case 0:
	            //Fragement for Web Tab
	            return new FragmentWebTab();
	        case 1:
	           //Fragment for Local Tab
	            return new FragmentLocalTab();
	        }
	    return null;
	  }
	  @Override
	  public int getCount() {
	    // TODO Auto-generated method stub
	    return 2; //No of Tabs
	  }
    }