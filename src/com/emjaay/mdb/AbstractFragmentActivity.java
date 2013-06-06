package com.emjaay.mdb;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

public abstract class AbstractFragmentActivity extends FragmentActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		
	}
	
	protected void showProgress(final boolean show) {
		setProgressBarIndeterminateVisibility(show ? true : false);
	}
}