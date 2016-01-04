package com.lianmeng.extand.lianmeng.help.activity;
import com.lianmeng.core.activity.R;
import com.lianmeng.core.activity.R.layout;
import com.lianmeng.core.framework.sysactivity.BaseWapperActivity;
import com.lianmeng.core.framework.util.Constant;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class HelpDetailActivity extends BaseWapperActivity {
 	@Override
	public void onClick(View v) {
	 
	}

	@Override
	protected void findViewById() {
		 
	}

	@Override
	protected void loadViewLayout() {
		// TODO Auto-generated method stub
		setContentView(R.layout.help_detail_activity);
		setTitle(getString(R.string.helpMsgHelpCenterMsg));
		selectedBottomTab(Constant.MORE);
		Bundle bundle = getIntent().getExtras();
		String detail_url = bundle.getString("detail_url");
		System.out.println(detail_url);

	}

	@Override
	protected void processLogic() {
 
	}

	@Override
	protected void setListener() {
  	}

}
