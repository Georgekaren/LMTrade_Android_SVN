package com.lianmeng.extand.lianmeng.about.activity;

import com.lianmeng.core.activity.R;
import com.lianmeng.core.activity.R.id;
import com.lianmeng.core.activity.R.layout;
import com.lianmeng.core.framework.sysactivity.BaseWapperNewActivity;
import com.lianmeng.core.framework.util.Constant;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.TextView;

public class AboutActivity extends BaseWapperNewActivity {
	private TextView textVersion;

	 

	@Override
	protected void findViewById() {
		textVersion = (TextView) findViewById(R.id.textVersion);
	}

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.old_act_about);
		setTitle(getString(R.string.versionMsgAboutMsg));
	}

	@Override
	protected void processLogic() {
		textVersion.setText(getString(R.string.versionMsgVersionNoMsg) + getVersion());
	}

	@Override
	protected void setListener() {
	}

	private String getVersion() {
		try {
			PackageManager manager = getPackageManager();
			PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
			return info.versionName;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
