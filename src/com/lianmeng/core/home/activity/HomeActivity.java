package com.lianmeng.core.home.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lianmeng.core.activity.R;
import com.lianmeng.core.activity.R.drawable;
import com.lianmeng.core.activity.R.id;
import com.lianmeng.core.activity.R.layout;
import com.lianmeng.core.activity.R.string;
import com.lianmeng.core.framework.sysactivity.BaseWapperActivity;
import com.lianmeng.core.framework.sysactivity.BaseWapperActivity.DataCallback;
import com.lianmeng.core.framework.sysvo.RequestVo;
import com.lianmeng.core.framework.util.Constant;
import com.lianmeng.core.framework.util.Logger;
import com.lianmeng.core.home.adapter.HomeAdapter;
import com.lianmeng.core.home.adapter.HomeBannerAdapter;
import com.lianmeng.core.home.parser.HomeBannerParser;
import com.lianmeng.core.home.vo.HomeBannerVo;
import com.lianmeng.core.home.vo.HomeCategory;
import com.lianmeng.core.product.activity.SearchProductListActivity;
import com.lianmeng.extand.lianmeng.product.activity.BrandActivity;
import com.lianmeng.extand.lianmeng.product.activity.BulletinActivity;
import com.lianmeng.extand.lianmeng.product.activity.HotproductActivity;
import com.lianmeng.extand.lianmeng.product.activity.LimitbuyActivity;
import com.lianmeng.extand.lianmeng.product.activity.NewproductActivity;
/**
 * 主页面
 * 
 * 搜索<br>
 * 点击搜索按钮激活 SearchActivity 数据存在 "key_words" 通过Intent.getStringExtra("key_words") 获取
 * @author liu
 *
 */
@SuppressWarnings("deprecation")
public class HomeActivity extends BaseWapperActivity implements OnItemClickListener, OnItemSelectedListener {

	private static final String TAG = "HomeActivity";
	private ListView mCategoryListView;
	
	private Gallery mGallery;
	private List<ImageView> mSlideViews;
	private TextView searchEdit;
	private boolean isPlay;
	
	private Runnable runnable = new Runnable() {
		
		@Override
		public void run() {
			if  (!isPlay)
				return ;
			mGallery.setSelection(mGallery.getSelectedItemPosition() + 1);
 			handler.postDelayed(this, 4000);
 			Logger.d(TAG, getString(R.string.homeMsgNextPictureMsg));
		}
	};
	
	private Handler handler = new Handler();

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.home_searchok: //搜索确定按钮
			String words = searchEdit.getText().toString();
			if (TextUtils.isEmpty(words)) {
				Toast.makeText(this, getString(R.string.homeMsgNoInputMsg), Toast.LENGTH_LONG).show();
				return ;
			}
			Intent Intent = new Intent(this, SearchProductListActivity.class);
			Intent.putExtra("keyWord", words);
			startActivity(Intent);
			break;
		}
	}
	@Override
	protected void onResume() {
		super.onResume();
		isPlay = true;
		runnable.run();
	}
	@Override
	protected void onPause() {
		super.onPause();
		isPlay = false;
	}
	
	@Override
	protected void findViewById() {
		mCategoryListView = (ListView) findViewById(R.id.custonInfoListView);
		mGallery = (Gallery) findViewById(R.id.gallery);
		mSlideViews = new ArrayList<ImageView>();
		mSlideViews.add((ImageView) findViewById(R.id.imgPoint0));
		mSlideViews.add((ImageView) findViewById(R.id.imgPoint1));
		mSlideViews.add((ImageView) findViewById(R.id.imgPoint2));
		mSlideViews.add((ImageView) findViewById(R.id.imgPoint3));
		mSlideViews.add((ImageView) findViewById(R.id.imgPoint4));
		searchEdit = (TextView) findViewById(R.id.editSearchInfo);
		findViewById(R.id.home_searchok).setOnClickListener(this);
	}

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.home_activity);
		setHeadLeftVisibility(View.INVISIBLE);
		setHeadBackgroundResource(R.drawable.head_bg_home);
		selectedBottomTab(Constant.HOME);
	}

	@Override
	protected void processLogic() {
		List<HomeCategory> categroy = new ArrayList<HomeCategory>();
		categroy.add(new HomeCategory(R.drawable.home_classify_01, getString(R.string.homeMsgClass01Msg)));
		categroy.add(new HomeCategory(R.drawable.home_classify_02, getString(R.string.homeMsgClass02Msg)));
		categroy.add(new HomeCategory(R.drawable.home_classify_03, getString(R.string.homeMsgClass03Msg)));
		categroy.add(new HomeCategory(R.drawable.home_classify_04, getString(R.string.homeMsgClass04Msg)));
		categroy.add(new HomeCategory(R.drawable.home_classify_05, getString(R.string.homeMsgClass05Msg)));
		mCategoryListView.setAdapter(new HomeAdapter(this, categroy));
		HashMap<String,String> inMap=new HashMap<String,String>();
		inMap.put("JsonData", "{\"ServiceName\":\"homeManagerService\" , \"Data\":{}}");
		RequestVo reqVo = new RequestVo(R.string.sysRequestServLet, this, inMap, new HomeBannerParser());
		getDataFromServer(reqVo, new DataCallback<List<HomeBannerVo>>() {
			@Override
			public void processData(List<HomeBannerVo> paramObject, boolean paramBoolean) {
				HomeBannerAdapter adapter = new HomeBannerAdapter(HomeActivity.this, paramObject);
				mGallery.setAdapter(adapter);

			}
		});
	}

	@Override
	protected void setListener() {
		mGallery.setOnItemSelectedListener(this);
		mCategoryListView.setOnItemClickListener(this);
	}

	/**
	 * 首页栏点击
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		switch (position) {
		case 0://水果
			startActivity(new Intent(this,LimitbuyActivity.class));
			break;
		case 1://日用品
			startActivity(new Intent(this, BulletinActivity.class));
			break;
		case 2://饮料
			startActivity(new Intent(this,NewproductActivity.class));
			break;
		case 3://食品
			startActivity(new Intent(this,HotproductActivity.class));
			break;
		case 4://文具
			startActivity(new Intent(this,BrandActivity.class));
			break;
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
 		int size = mSlideViews.size();
		for (int i = 0; i < size; i++) {
			int j = position % size;
			ImageView imageView = mSlideViews.get(i);
			if (j == i)
				imageView.setBackgroundResource(R.drawable.slide_adv_selected);
			else
				imageView.setBackgroundResource(R.drawable.slide_adv_normal);
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}
}