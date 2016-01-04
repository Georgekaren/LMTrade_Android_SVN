package com.lianmeng.extand.lianmeng.more.activity;

import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;

import com.lianmeng.core.account.activity.AccountActivity;
import com.lianmeng.core.activity.R;
import com.lianmeng.core.activity.R.id;
import com.lianmeng.core.activity.R.layout;
import com.lianmeng.core.activity.R.string;
import com.lianmeng.core.framework.sysactivity.BaseWapperActivity;
import com.lianmeng.core.framework.util.Constant;
import com.lianmeng.core.framework.util.Logger;
import com.lianmeng.core.login.activity.LoginActivity;
import com.lianmeng.core.product.activity.ProductHistoryActivity;
import com.lianmeng.extand.lianmeng.about.activity.AboutActivity;
import com.lianmeng.extand.lianmeng.help.activity.HelpActivity;

public class MoreActivity extends BaseWapperActivity {

	private View mBrowseRl;
	private RelativeLayout helpRelLay;
	private RelativeLayout aboutRelLay;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.recent_browse_rl:
			startActivity(new Intent(this, ProductHistoryActivity.class));
			break;
		case R.id.helpRelLay:
			startActivity(new Intent(this, HelpActivity.class));
			break;
		case R.id.aboutRelLay:
			startActivity(new Intent(this, AboutActivity.class));
			break;
		case R.id.my_account_rl:
			startActivity(new Intent(this, AccountActivity.class));
			break;
		case R.id.login:
			startActivity(new Intent(this, LoginActivity.class));
			break;

		}
	}

	@Override
	protected void findViewById() {
		mBrowseRl = findViewById(R.id.recent_browse_rl);
		helpRelLay = (RelativeLayout) findViewById(R.id.helpRelLay);
		findViewById(R.id.login).setOnClickListener(this);
		findViewById(R.id.aboutRelLay).setOnClickListener(this);
		findViewById(R.id.my_account_rl).setOnClickListener(this);
	}

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.more_activity);
		selectedBottomTab(Constant.MORE);
		setHeadLeftVisibility(View.INVISIBLE);
		setTitle(R.string.more);
	}

	@Override
	protected void processLogic() {

	}

	@Override
	protected void setListener() {
		mBrowseRl.setOnClickListener(this);
		helpRelLay.setOnClickListener(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null)
			Logger.d("More", "测试获取到数据" + data.getParcelableExtra("address"));
	}

}
