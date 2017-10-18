package com.emjaay.mdb.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

public abstract class AbstractFragmentPagerAdapter extends FragmentPagerAdapter {
	
	private FragmentManager fm;
	
	public AbstractFragmentPagerAdapter(FragmentManager fm) {
		super(fm);
		this.fm = fm;
	}
	
	public Fragment getActiveFragment(ViewPager container, int position) {
		String name = makeFragmentName(container.getId(), position);
		return  fm.findFragmentByTag(name);
	}
	
	private static String makeFragmentName(int viewId, int index) {
	    return "android:switcher:" + viewId + ":" + index;
	}
}
