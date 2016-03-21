package com.lianmeng.core.account.activity;

import java.util.HashMap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lianmeng.core.activity.R;
import com.lianmeng.core.address.activity.RestaurantSelectAddressActivity;
import com.lianmeng.core.framework.engine.CallbackImplements;
import com.lianmeng.core.framework.engine.SyncImageLoaderNoAdapter;
import com.lianmeng.core.framework.sysvo.RequestVo;
import com.lianmeng.core.framework.util.Constant;
import com.lianmeng.core.framework.util.Logger;
import com.lianmeng.core.framework.util.SysU;
import com.lianmeng.core.login.activity.RestaurantLoginActivity;
import com.lianmeng.core.login.parser.UserinfoParser;
import com.lianmeng.core.login.vo.User;
import com.lianmeng.core.order.activity.RestaurantOrderListActivity;
import com.lianmeng.extand.lianmeng.product.activity.RestaurantBaseActivity;

public class RestaurantAccountActivity extends RestaurantBaseActivity {
	private static final String TAG = "AccountActivity";
	private TextView my_name_text; // 用户名
	private TextView my_tele_text; // 电话
	private ImageView my_pic_img;  //图标

	private RelativeLayout ll_account_myorder; // 我的订单
	private RelativeLayout my_ll_user_header; // 图标
	private LinearLayout ll_account_address_manage; // 地址管理

	private SharedPreferences sp;
	private SyncImageLoaderNoAdapter syncImageLoader = new SyncImageLoaderNoAdapter(); 
	private User info;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_account_myorder:
			Logger.d(TAG, "跳转我的订单activity");
			Intent orderIntent = new Intent(this, RestaurantOrderListActivity.class);
			orderIntent.putExtra("totoalOrderCount", info.getOrdercount());
			startActivity(orderIntent);
			break;
		case R.id.ll_account_address_manage:
			Logger.d(TAG, "跳转地址管理activity");
			Intent addressManagerIntent = new Intent(this, RestaurantSelectAddressActivity.class);
			startActivity(addressManagerIntent);
			break;
		case R.id.my_ll_user_header:
			Logger.d(TAG, "跳转登陆activity");
			Intent loginIntent = new Intent(this, RestaurantLoginActivity.class);
			startActivity(loginIntent);
			
			break;
		}
	}

	@Override
	protected void findViewById() {
		my_name_text = (TextView) this.findViewById(R.id.my_name_text);
		my_tele_text = (TextView) this.findViewById(R.id.my_tv_user_phone);
		my_pic_img = (ImageView) this.findViewById(R.id.myiv_user_header);
		my_ll_user_header = (RelativeLayout) this.findViewById(R.id.my_ll_user_header);
		ll_account_myorder = (RelativeLayout) this.findViewById(R.id.ll_account_myorder);
		ll_account_address_manage = (LinearLayout) this.findViewById(R.id.ll_account_address_manage);
	}

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.act_account_restaurant);
		selectedBottomTab(Constant.MORE);
		setTitle(R.string.my_account_title);
		sp = getSharedPreferences("userinfo", MODE_PRIVATE);

	}

	@Override
	protected void processLogic() {
	    if(SysU.USERID==null||SysU.USERID.length()==0){
	        Intent loginIntent = new Intent(this, RestaurantLoginActivity.class);
            startActivity(loginIntent);
	    }else{
		RequestVo vo = new RequestVo();
		vo.context = context;
		vo.requestUrl = R.string.sysRequestServLet;
		vo.jsonParser = new UserinfoParser();
		String inmapData="{\"ServiceName\":\"userManagerService\" , \"Data\":{\"ACTION\":\"QRYUSERINFO\",\"id\":\""+SysU.USERID+"\"}}";
		HashMap<String, String> prodMap = new HashMap<String, String>();
		prodMap.put("JsonData", inmapData);
		vo.requestDataMap = prodMap;
		super.getDataFromServer(vo, new DataCallback<User>() {
			@Override
			public void processData(User paramObject, boolean paramBoolean) {
				if (paramObject != null) {
					info = paramObject;
					Logger.d(TAG, Thread.currentThread().getName());
					String username = sp.getString("userName", "");
					Logger.d(TAG, "userName:"+username);
					my_name_text.setText(info.getUserName());
					my_tele_text.setText(info.getTelephone());
					if(info.getPic()!=null&&info.getPic().length()>1){
					    loadRemoteImage(getString(R.string.sysRequestHost)+info.getPic(),my_pic_img);
					}
				}
			}
		});
	    }
		
		
		
	}

	@Override
	protected void setListener() {
	    my_ll_user_header.setOnClickListener(this);
		ll_account_myorder.setOnClickListener(this);
		ll_account_address_manage.setOnClickListener(this);
		
	}

	
	
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
