package com.emjaay.mdb;

import java.util.Locale;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.MenuItem;

import com.emjaay.mdb.adapters.AbstractFragmentPagerAdapter;
import com.emjaay.mdb.data.Movie;
import com.emjaay.mdb.fragments.HomeFragment;

public class HomeActivity extends AbstractFragmentActivity implements OnPageChangeListener
{
	private HomePagerAdapter mPagerAdapter;
	private ViewPager mViewPager;
	
	protected void onCreate(Bundle paramBundle){
		super.onCreate(paramBundle);
		setContentView(R.layout.home_activity);
				
		mViewPager = ((ViewPager)findViewById(R.id.pager));
		mViewPager.setOnPageChangeListener(this);
		
		mPagerAdapter = new HomePagerAdapter(getSupportFragmentManager());
		mViewPager.setAdapter(mPagerAdapter);
		
		onPageSelected(0);
	}
	
	protected void onResume(){
		super.onResume();
		
		for(int i=0; i<3; i++){
			HomeFragment localHomeFragment = (HomeFragment)this.mPagerAdapter.getActiveFragment(this.mViewPager, i);
			if (localHomeFragment != null){
				localHomeFragment.updateMovies();
			}
		}
	}
	
	public boolean onCreateOptionsMenu(Menu menu){
		switch (this.mViewPager.getCurrentItem()){
		case 0:
			getMenuInflater().inflate(R.menu.home_menu, menu);
			return true;
		}
		return false;
	}
	
	public boolean onOptionsItemSelected(MenuItem item){
		switch (item.getItemId()){
		case R.id.item_home_add:
			Intent intent = new Intent(this, SearchImdbActivity.class);
			startActivity(intent);
			break;
		}
		return true;
	}
	
	public class HomePagerAdapter extends AbstractFragmentPagerAdapter{
		
		public HomePagerAdapter(FragmentManager fm){
			super(fm);
		}
		
		public int getCount(){
			return 3;
		}
		
		public Fragment getItem(int position){
			int type = 0;
			switch(position){
			case 0:
				type = Movie.TYPE_MOVIE;
				break;
			case 1:
				type = Movie.TYPE_SERIES;
				break;
			case 2:
				type = Movie.TYPE_EPISODE;
				break;
			}
			HomeFragment fragment = new HomeFragment();
			Bundle args = new Bundle();
			args.putInt("type", type);
			fragment.setArguments(args);
			return fragment;
		}
		
		public CharSequence getPageTitle(int position){
			Locale localLocale = Locale.getDefault();
			switch (position){
			case 0:
				return HomeActivity.this.getString(2131034115).toUpperCase(localLocale);
			case 1:
				return HomeActivity.this.getString(2131034116).toUpperCase(localLocale);
			case 2:
				return HomeActivity.this.getString(2131034118).toUpperCase(localLocale);
			}
			return null;
		}
	}

	@Override
	public void onPageScrollStateChanged(int state) {
		
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		
	}

	@Override
	public void onPageSelected(int position) {
		supportInvalidateOptionsMenu();
	}
}