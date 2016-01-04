package com.lianmeng.core.account.activity;

import java.util.HashMap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lianmeng.core.activity.R;
import com.lianmeng.core.activity.R.id;
import com.lianmeng.core.activity.R.layout;
import com.lianmeng.core.activity.R.string;
import com.lianmeng.core.address.activity.AddressManagerActivity;
import com.lianmeng.core.favorite.activity.MyFavoriteActivity;
import com.lianmeng.core.framework.sysactivity.BaseWapperActivity;
import com.lianmeng.core.framework.sysactivity.BaseWapperActivity.DataCallback;
import com.lianmeng.core.framework.sysparser.SuccessParser;
import com.lianmeng.core.framework.sysvo.RequestVo;
import com.lianmeng.core.framework.util.Constant;
import com.lianmeng.core.framework.util.Logger;
import com.lianmeng.core.framework.util.SysU;
import com.lianmeng.core.home.activity.HomeActivity;
import com.lianmeng.core.login.parser.UserinfoParser;
import com.lianmeng.core.login.vo.User;
import com.lianmeng.core.order.activity.OrderListActivity;

public class AccountActivity extends BaseWapperActivity {
	private static final String TAG = "AccountActivity";
	private TextView my_name_text; // 用户名
	private TextView my_bonus_text; // 积分
	private TextView my_level_text; // 等级
	private LinearLayout ll_account_myorder; // 我的订单
	private LinearLayout ll_account_address_manage; // 地址管理
	private LinearLayout ll_account_conservation; // 收藏夹
	private SharedPreferences sp;

	private User info;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_account_myorder:
			Logger.d(TAG, "跳转我的订单activity");
			Intent orderIntent = new Intent(this, OrderListActivity.class);
			orderIntent.putExtra("totoalOrderCount", info.getOrdercount());
			startActivity(orderIntent);
			break;
		case R.id.ll_account_address_manage:
			Logger.d(TAG, "跳转地址管理activity");
			Intent addressManagerIntent = new Intent(this, AddressManagerActivity.class);
			startActivity(addressManagerIntent);
			break;
		case R.id.ll_account_conservation:
			Logger.d(TAG, "跳转收藏夹activity");
			Intent myFavoriteIntent = new Intent(this, MyFavoriteActivity.class);
			myFavoriteIntent.putExtra("totalFavoriteCount", info.getFavoritescount());
			startActivity(myFavoriteIntent);
			break;
		}
	}

	@Override
	protected void findViewById() {
		my_name_text = (TextView) this.findViewById(R.id.my_name_text);
		my_bonus_text = (TextView) this.findViewById(R.id.my_bonus_text);
		my_level_text = (TextView) this.findViewById(R.id.my_level_text);
		ll_account_myorder = (LinearLayout) this.findViewById(R.id.ll_account_myorder);
		ll_account_address_manage = (LinearLayout) this.findViewById(R.id.ll_account_address_manage);
		ll_account_conservation = (LinearLayout) this.findViewById(R.id.ll_account_conservation);
	}

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.my_account_activity);
		setHeadRightVisibility(View.VISIBLE);
		setHeadRightText(getString(R.string.accountMsgExitMsg));
		selectedBottomTab(Constant.MORE);
		setTitle(R.string.my_account_title);
		sp = getSharedPreferences("userinfo", MODE_PRIVATE);

	}

	@Override
	protected void processLogic() {
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
					my_bonus_text.setText(info.getBonus() + "");
					my_level_text.setText(info.getLevel() + "");
					String username = sp.getString("userName", "");
					Logger.d(TAG, "userName:"+username);
					my_name_text.setText(username);
				}
			}
		});
	}

	@Override
	protected void setListener() {
		ll_account_myorder.setOnClickListener(this);
		ll_account_address_manage.setOnClickListener(this);
		ll_account_conservation.setOnClickListener(this);
	}

	@Override
	protected void onHeadRightButton(View v) {
		HashMap map = new HashMap();
		map.put("loginact", "logout");
		String inMapData="{\"ServiceName\":\"userManagerService\" , \"Data\":{\"ACTION\":\"LOGOUTUSER\"}}";
		map.put("JsonData", inMapData);
		RequestVo reqVo = new RequestVo(R.string.sysRequestLoginServLet, context, map, new SuccessParser());
		super.getDataFromServer(reqVo, new DataCallback<Boolean>() {
			@Override
			public void processData(Boolean paramObject, boolean paramBoolean) {
				startActivity(new Intent(AccountActivity.this, HomeActivity.class));
			}
		});
	}
 
}
