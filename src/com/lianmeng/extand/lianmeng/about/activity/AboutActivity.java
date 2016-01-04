package com.lianmeng.extand.lianmeng.about.activity;

import com.lianmeng.core.activity.R;
import com.lianmeng.core.activity.R.id;
import com.lianmeng.core.activity.R.layout;
import com.lianmeng.core.framework.sysactivity.BaseWapperActivity;
import com.lianmeng.core.framework.util.Constant;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.TextView;

public class AboutActivity extends BaseWapperActivity {
	private TextView textVersion;

	@Override
	public void onClick(View v) {

	}

	@Override
	protected void findViewById() {
		textVersion = (TextView) findViewById(R.id.textVersion);
	}

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.about_activity);
		setTitle(getString(R.string.versionMsgAboutMsg));
		selectedBottomTab(Constant.MORE);
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
