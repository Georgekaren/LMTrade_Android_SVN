package com.lianmeng.extand.lianmeng.help.activity;
import com.lianmeng.core.activity.R;
import com.lianmeng.core.activity.R.layout;
import com.lianmeng.core.framework.sysactivity.BaseWapperNewActivity;
import com.lianmeng.core.framework.util.Constant;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class HelpDetailActivity extends BaseWapperNewActivity {
 	

	@Override
	protected void findViewById() {
		 
	}

	@Override
	protected void loadViewLayout() {
		// TODO Auto-generated method stub
		setContentView(R.layout.old_act_help_detail);
		setTitle(getString(R.string.helpMsgHelpCenterMsg));
		
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
