package com.lianmeng.core.home.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.lianmeng.core.activity.R;
import com.lianmeng.core.framework.engine.CallbackImplements;
import com.lianmeng.core.framework.engine.SyncImageLoaderNoAdapter;
import com.lianmeng.core.framework.sysactivity.BaseWapperNewActivity;
import com.lianmeng.core.framework.sysvo.RequestVo;
import com.lianmeng.core.framework.util.SysU;
import com.lianmeng.core.home.view.AbOnItemClickListener;
import com.lianmeng.core.home.view.HomeTopSlidingImagePlayView;
import com.lianmeng.core.home.view.PullToRefreshView;
import com.lianmeng.core.home.view.PullToRefreshView.PullToRefreshListener;
import com.lianmeng.core.home.adapter.HomeClassGridAdapter;
import com.lianmeng.core.home.adapter.HomeBannerAdapter;
import com.lianmeng.core.home.parser.HomeBannerParser;
import com.lianmeng.core.home.vo.HomeBannerVo;
import com.lianmeng.extand.lianmeng.product.activity.RestaurantDetailActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class HomeActivity extends  BaseWapperNewActivity implements View.OnClickListener {

	private static final String TAG = "HomeActivity";
	private SharedPreferences sp;
	private boolean isPlay;
	
	//分类的九宫格
	private GridView gridView_classify;

	private HomeClassGridAdapter adapter_GridView_classify;
	// 首页轮播
	private HomeTopSlidingImagePlayView viewPager;
	// 扫一扫
	private ImageView iv_shao;
	// 分类九宫格的资源文件
	private int[] pic_path_classify = { R.drawable.menu_guide_1,R.drawable.menu_guide_2, R.drawable.menu_guide_3,R.drawable.menu_guide_4};
	private String[] pic_path_classify_sign = {"1001","1005", "1003","1004"};
	private String[] pic_path_classify_sign_name = {"水果","日用品", "食品","饮料"};
    
	
	/** 存储首页轮播的界面 */
	private ArrayList<View> allListView;
	
	
	PullToRefreshView pullToRefreshView;
	ScrollView scroll;
	
	/** 首页轮播的界面的资源 */
	private int[] resId = {R.drawable.menu_viewpager_1,R.drawable.menu_viewpager_2};
	private SyncImageLoaderNoAdapter syncImageLoader = new SyncImageLoaderNoAdapter(); 
	@InjectView(R.id.mid_title) TextView mid_title;
	@InjectView(R.id.lilayout_home_product_apple_su) ImageView lilayout_home_product_apple_su;
	@InjectView(R.id.lilayout_home_product_pear_su) ImageView lilayout_home_product_pear_su;
	@InjectView(R.id.lilayout_home_product_kekou_su) ImageView lilayout_home_product_kekou_su;
	@InjectView(R.id.lilayout_home_product_toothpaste_su) ImageView lilayout_home_product_toothpaste_su;
	@InjectView(R.id.lilayout_hone_display_big_image_control) LinearLayout lilayout_hone_display_big_image_control;

	
	
	@Override
	public void onClick(View v) {
	     
	      switch(v.getId()){
	          case R.id.lilayout_home_product_apple_su:
	              intoProduct("2002","新疆冰芯苹果");
	          break;
	          case R.id.lilayout_home_product_pear_su:
                  intoProduct("2001","梨");
              break;
	          case R.id.lilayout_home_product_kekou_su:
                  intoProduct("2101","可乐");
              break;
	          case R.id.lilayout_home_product_toothpaste_su:
                  intoProduct("2201","牙膏");
              break;
	      }
	      
	     
	 }  
	
	private void getUserDataFromShape(){
	    sp = getSharedPreferences("userinfo", MODE_PRIVATE);
	   String userId=sp.getString("userId", "-1");
	   if(userId!=null&&!userId.equals("-1")&&!userId.equals("")){
	       SysU.USERID=userId;
	   }
	}
	
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
	public void intoProduct(String type,String titleName){
	    Intent Intent = new Intent(HomeActivity.this, RestaurantDetailActivity.class);
        String inName="日常用品";
        if(titleName!=null&&!"".equals(titleName)){
            inName=titleName;
        }
        Intent.putExtra("type", type);
        Intent.putExtra("name", inName);
        startActivity(Intent);
	}
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
				HomeBannerAdapter adapter = new HomeBannerAdapter(HomeActivity.this, paramObject);
				//mGallery.setAdapter(adapter);

			}
		});
		getUserDataFromShape();
	}

	  
	@Override
	protected void findViewById() {
		
		gridView_classify = (GridView) findViewById(R.id.my_gridview);
		gridView_classify.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter_GridView_classify = new HomeClassGridAdapter(this, pic_path_classify);
		gridView_classify.setAdapter(adapter_GridView_classify);
		
		viewPager = (HomeTopSlidingImagePlayView) findViewById(R.id.viewPager_menu);
		//设置播放方式为顺序播放
		viewPager.setPlayType(1);
		//设置播放间隔时间
		viewPager.setSleepTime(3000);

		gridView_classify.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				String inName="日常用品";
				
				intoProduct(pic_path_classify_sign[position],pic_path_classify_sign_name[position]);
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
		
		
		LinearLayout home_linearLayout=(LinearLayout) findViewById(R.id.view_home_ext_layout);
		home_linearLayout.setOnTouchListener(new OnTouchListener() {  

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				pullToRefreshView.onTouchEvent(event);
				return true;
			}
            });
		
		
		ButterKnife.inject(this);
		mid_title.setText(R.string.homeMsgDefaultSchoolTitleMsg);
		
		
		loadRemoteImage(getString(R.string.sysRequestHost)+"images/home_product_apple_su.jpg",lilayout_home_product_apple_su);
		loadRemoteImage(getString(R.string.sysRequestHost)+"images/home_product_pear_su.jpg",lilayout_home_product_pear_su);
		loadRemoteImage(getString(R.string.sysRequestHost)+"images/home_product_kekou_su.jpg",lilayout_home_product_kekou_su);
		loadRemoteImage(getString(R.string.sysRequestHost)+"images/home_product_toothpaste_su.jpg",lilayout_home_product_toothpaste_su);
        
		
	}
	
	
	
	
	
	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.act_home);
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
			View view = LayoutInflater.from(this).inflate(R.layout.act_home_layout, null);
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
				Intent Intent = new Intent(HomeActivity.this, RestaurantDetailActivity.class);
				Intent.putExtra("keyWord", "");
				startActivity(Intent);
				 
			}
		});
	}
	 
	
	// url:图片的url地址  
    // id:ImageView控件
    private void loadRemoteImage(String url,ImageView imageView) { 
        imageView.setOnClickListener(this);
        CallbackImplements callbackImplements = new CallbackImplements(imageView);  
        Drawable cacheImage = syncImageLoader.loadDrawable(url,   callbackImplements);  
        // 如果图片缓存存在,则在外部设置image.否则是调用的callback函数设置  
        if (cacheImage != null) {  
            imageView.setImageDrawable(cacheImage);  
        }  
    }



   
}
