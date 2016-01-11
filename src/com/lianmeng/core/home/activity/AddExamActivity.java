package com.lianmeng.core.home.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.lianmeng.core.activity.R;
import com.lianmeng.core.framework.sysactivity.BaseWapperActivity;
import com.lianmeng.core.framework.sysactivity.BaseWapperActivity.DataCallback;
import com.lianmeng.core.framework.sysactivity.BaseWapperNewActivity;
import com.lianmeng.core.framework.sysvo.RequestVo;
import com.lianmeng.core.framework.util.Logger;
import com.lianmeng.core.home.activity.PullToRefreshView.PullToRefreshListener;
import com.lianmeng.core.home.activity.RefreshScrollView.OnRefreshScrollViewListener;
import com.lianmeng.core.home.adapter.HomeAdapter;
import com.lianmeng.core.home.adapter.HomeBannerAdapter;
import com.lianmeng.core.home.parser.HomeBannerParser;
import com.lianmeng.core.home.vo.HomeBannerVo;
import com.lianmeng.core.home.vo.HomeCategory;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class AddExamActivity extends  BaseWapperNewActivity {

	private static final String TAG = "AddExamActivity";
	private RotateAnimation animation;
	private RotateAnimation reverseAnimation;
	private RefreshScrollView myListView;
	private Gallery mGallery;
	
	
	private List<ImageView> mSlideViews;
	private TextView searchEdit;
	private boolean isPlay;
	private Handler handler = new Handler();

	//分类的九宫格
	private GridView gridView_classify;

	private Adapter_GridView adapter_GridView_classify;
	// 首页轮播
	private AbSlidingPlayView viewPager;
	// 扫一扫
	private ImageView iv_shao;
	// 分类九宫格的资源文件
	private int[] pic_path_classify = { R.drawable.menu_guide_1,
			R.drawable.menu_guide_2, R.drawable.menu_guide_3,
			R.drawable.menu_guide_4};
	
	/** 存储首页轮播的界面 */
	private ArrayList<View> allListView;
	
	
	PullToRefreshView pullToRefreshView;
	ScrollView scroll;
	
	/** 首页轮播的界面的资源 */
	private int[] resId = {R.drawable.menu_viewpager_1,R.drawable.menu_viewpager_2};
	
	/*private Runnable runnable = new Runnable() {
		
		@Override
		public void run() {
			if  (!isPlay)
				return ;
			//mGallery.setSelection(mGallery.getSelectedItemPosition() + 1);
 			//handler.postDelayed(this, 4000);
 			//Logger.d(TAG, getString(R.string.homeMsgNextPictureMsg));
		}
	};*/
	
	@Override
	protected void onResume() {
		super.onResume();
		isPlay = true;
	//	runnable.run();
	}
	@Override
	protected void onPause() {
		super.onPause();
		isPlay = false;
	}
	
	@Override
	protected void processLogic() {
		HashMap<String,String> inMap=new HashMap<String,String>();
		inMap.put("JsonData", "{\"ServiceName\":\"homeManagerService\" , \"Data\":{}}");
		RequestVo reqVo = new RequestVo(R.string.sysRequestServLet, this, inMap, new HomeBannerParser());
		getDataFromServer(reqVo, new DataCallback<List<HomeBannerVo>>() {
			@Override
			public void processData(List<HomeBannerVo> paramObject, boolean paramBoolean) {
				HomeBannerAdapter adapter = new HomeBannerAdapter(AddExamActivity.this, paramObject);
				mGallery.setAdapter(adapter);

			}
		});
	}

	  
	@Override
	protected void findViewById() {
		
		mGallery = (Gallery) findViewById(R.id.gallery);
		gridView_classify = (GridView) findViewById(R.id.my_gridview);
		gridView_classify.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter_GridView_classify = new Adapter_GridView(this, pic_path_classify);
		gridView_classify.setAdapter(adapter_GridView_classify);
		
		viewPager = (AbSlidingPlayView) findViewById(R.id.viewPager_menu);
		//设置播放方式为顺序播放
		viewPager.setPlayType(1);
		//设置播放间隔时间
		viewPager.setSleepTime(3000);

		gridView_classify.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				//挑战到宝贝搜索界面
				//Intent intent = new Intent(this, WareActivity.class);
				//startActivity(intent);
			}
		});
		initViewPager() ;
		
		scroll = (ScrollView) findViewById(R.id.home_pull_scroll);
		pullToRefreshView = (PullToRefreshView) findViewById(R.id.home_pull);
		pullToRefreshView.setOnRefreshListener(new PullToRefreshListener() {
			@Override
			public void onRefresh() {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				pullToRefreshView.finishRefreshing();
			}
		}, 0);
		
		/*myListView=(RefreshScrollView) findViewById(R.id.home_scrollView1);
		myListView.setOnRefreshScrollViewListener(new OnRefreshScrollViewListener() {
			String se="";
			String ir=se;
			@Override
			public void onRefresh() {
				new AsyncTask<Void, Void, Void>() {
					protected Void doInBackground(Void... params) {

						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						 
						// myListView.onRefreshComplete();

					}

				}.execute();
				
			}
		});
		*/
		LinearLayout home_linearLayout=(LinearLayout) findViewById(R.id.view_home_ext_layout);
		home_linearLayout.setOnTouchListener(new OnTouchListener() {  

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				pullToRefreshView.onTouchEvent(event);
				return true;
			}
            });
		/*LinearLayout home_linearLayout1=(LinearLayout) findViewById(R.id.view_home_ext_layout1);
		home_linearLayout1.setOnTouchListener(new OnTouchListener() {  

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				myListView.onTouchEvent(event);
				return false;
			}
            });
 
		RelativeLayout home_linearLayout2=(RelativeLayout) findViewById(R.id.view_home_ext_layout2);
		home_linearLayout2.setOnTouchListener(new OnTouchListener() {  

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				myListView.onTouchEvent(event);
				return false;
			}
            });
		*/
	}
	
	
	
	
	
	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.myhomeexam);
	}
	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		
	}
	
	
	 
	
	 
	
	private void initViewPager() {

		if (allListView != null) {
			allListView.clear();
			allListView = null;
		}
		allListView = new ArrayList<View>();

		for (int i = 0; i < resId.length; i++) {
			//导入ViewPager的布局
			View view = LayoutInflater.from(this).inflate(R.layout.pic_item, null);
			ImageView imageView = (ImageView) view.findViewById(R.id.pic_item);
			imageView.setImageResource(resId[i]);
			allListView.add(view);
		}
		
		
		viewPager.addViews(allListView);
		//开始轮播
		viewPager.startPlay();
		viewPager.setOnItemClickListener(new AbOnItemClickListener() {
			@Override
			public void onClick(int position) {
				//跳转到详情界面
			//	Intent intent = new Intent(this, BabyActivity.class);
			//	startActivity(intent);
			}
		});
	}
	 
}
