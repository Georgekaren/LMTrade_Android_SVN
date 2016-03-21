package com.lianmeng.core.home.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;


@SuppressLint("UseSparseArrays")
@SuppressWarnings("unused")
public class HomeTopImageViewPagerAdapter extends PagerAdapter{
	
	/** The m context. */
	private Context mContext;
	
	/** The m list views. */
	private ArrayList<View> mListViews = null;
	
	/** The m views. */
	private HashMap <Integer,View> mViews = null;


	/**
	 * Instantiates a new ab view pager adapter.
	 * @param context the context
	 * @param mListViews the m list views
	 */
	public HomeTopImageViewPagerAdapter(Context context,ArrayList<View> mListViews) {
		this.mContext = context;
		this.mListViews = mListViews;
		this.mViews = new HashMap <Integer,View>();
	}

	
	@Override
	public int getCount() {
		return mListViews.size();
	}

	
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == (arg1);
	}

	
	@Override
	public Object instantiateItem(View container, int position) {
		View v = mListViews.get(position);
		((ViewPager) container).addView(v);
		return v;
	}

	
	@Override
	public void destroyItem(View container, int position, Object object) {
		((ViewPager) container).removeView((View)object);
	}
	
	
	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}
	

}
