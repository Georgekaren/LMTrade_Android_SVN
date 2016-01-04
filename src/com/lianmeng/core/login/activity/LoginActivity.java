package com.lianmeng.core.login.activity;

import java.util.HashMap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lianmeng.core.account.activity.AccountActivity;
import com.lianmeng.core.activity.R;
import com.lianmeng.core.activity.R.id;
import com.lianmeng.core.activity.R.layout;
import com.lianmeng.core.activity.R.string;
import com.lianmeng.core.framework.sysactivity.BaseWapperActivity;
import com.lianmeng.core.framework.sysactivity.BaseWapperActivity.DataCallback;
import com.lianmeng.core.framework.sysvo.RequestVo;
import com.lianmeng.core.framework.util.CommonUtil;
import com.lianmeng.core.login.parser.LoginParser;
import com.lianmeng.core.login.vo.UserInfo;

public class LoginActivity extends BaseWapperActivity {
	private EditText login_name_edit;
	private EditText login_pwd_edit;
	private TextView login_text;
	private TextView register_text;
	private CheckBox remember_pwd_checkbox;
	private SharedPreferences sp;
	private String userName;
	private String userPwd;
	private boolean autoLogin;
	private boolean isnotLogin;

	protected void findViewById() {
		login_name_edit = (EditText) findViewById(R.id.login_name_edit);
		login_pwd_edit = (EditText) findViewById(R.id.login_pwd_edit);
		login_text = (TextView) findViewById(R.id.login_text);
		register_text = (TextView) findViewById(R.id.register_text);
		remember_pwd_checkbox = (CheckBox) findViewById(R.id.remember_pwd_checkbox);
	}

	protected void loadViewLayout() {
		sp = getSharedPreferences("userinfo", MODE_PRIVATE);
		setContentView(R.layout.login_activity);
		setTitle(getString(R.string.loginTitleLoginButtonNameMsg));
	}

	protected void processLogic() {
		if (getIntent().getStringExtra("notlogin") != null) {
			isnotLogin = true;
		}
		this.autoLogin = sp.getBoolean("autoLogin", true);
		if (this.autoLogin) {

		}

	}

	protected void setListener() {
		login_text.setOnClickListener(this);
		register_text.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_text:
			String userName = login_name_edit.getText().toString().trim();
			String userPwd = login_pwd_edit.getText().toString().trim();

			if (userName == null || "".equals(userName)) {
				CommonUtil.showInfoDialog(this, getString(R.string.loginMsgNullErroUserNameMsg));
				return;
			}
			if (userPwd == null || "".equals(userPwd)) {
				CommonUtil.showInfoDialog(this, getString(R.string.loginMsgNullErroPasswordTextMsg));
				return;
			}
			this.userName = userName;
			this.userPwd = userPwd;
			showProgressDialog();
			RequestVo vo = new RequestVo();
			vo.requestUrl = R.string.sysRequestLoginServLet;
			vo.jsonParser = new LoginParser();
			vo.context = this;
			HashMap<String,String> map = new HashMap<String,String>();
			map.put("username", userName);
			map.put("password", userPwd);
			map.put("loginact", "login");
			String inMapData="{\"ServiceName\":\"userManagerService\" , \"Data\":{\"ACTION\":\"QRYUSER\",\"name\":\""+userName+"\",\"password\":\""+userPwd+"\"}}";
			map.put("JsonData", inMapData);
			vo.requestDataMap = map;
			super.getDataFromServer(vo, new DataCallback<UserInfo>() {

				@Override
				public void processData(UserInfo paramObject, boolean paramBoolean) {
					if (paramObject != null) {
						if (paramObject.getUserId() == null) {
							Toast.makeText(LoginActivity.this, getString(R.string.loginMsgSignErroUserMsg), Toast.LENGTH_LONG).show();
							return;
						}
						Editor ed = sp.edit();

						ed.putString("userName", LoginActivity.this.userName);
						ed.putString("userPwd", LoginActivity.this.userPwd);
						if (remember_pwd_checkbox.isChecked()) {
							ed.putBoolean("autoLogin", true);
						} else {
							ed.putBoolean("autoLogin", false);
						}
						ed.commit();

						if (isnotLogin) {
							setResult(LOGIN_SUCCESS);
						} else {
							Intent intent = new Intent(LoginActivity.this, AccountActivity.class);
							startActivity(intent);
						}
						finish();
					} else {
						Toast.makeText(LoginActivity.this, getString(R.string.loginMsgSignErroUserMsg), Toast.LENGTH_LONG).show();
					}
				}
			});

			break;
		case R.id.register_text:
			Intent register = new Intent(LoginActivity.this, RegisterActivity.class);
			startActivity(register);

			break;

		}
	}
}
