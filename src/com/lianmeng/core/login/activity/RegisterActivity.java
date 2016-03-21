package com.lianmeng.core.login.activity;

import java.util.HashMap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lianmeng.core.account.activity.RestaurantAccountActivity;
import com.lianmeng.core.activity.R;
import com.lianmeng.core.activity.R.id;
import com.lianmeng.core.activity.R.layout;
import com.lianmeng.core.activity.R.string;
import com.lianmeng.core.framework.sysactivity.BaseWapperNewActivity;
import com.lianmeng.core.framework.sysactivity.BaseWapperNewActivity.DataCallback;
import com.lianmeng.core.framework.sysvo.RequestVo;
import com.lianmeng.core.framework.util.CommonUtil;
import com.lianmeng.core.login.parser.LoginParser;
import com.lianmeng.core.login.vo.UserInfo;

public class RegisterActivity extends BaseWapperNewActivity {
	private EditText login_name_edit;
	private EditText login_pwd_edit;
	private EditText login_pwd2_edit;
	private TextView register_text;  
	private DataCallback<UserInfo> callback;
	private SharedPreferences sp;
	 
	
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.register_text:
			String name =  login_name_edit.getText().toString().trim();
			String pwd = login_pwd_edit.getText().toString().trim();
			String pwd2 = login_pwd2_edit.getText().toString().trim();
			if(name==null||"".equals(name)){
				CommonUtil.showInfoDialog(this, getString(R.string.loginMsgNullErroUserNameMsg));
				return ;
			}
			if(pwd==null||"".equals(pwd)){
				CommonUtil.showInfoDialog(this, getString(R.string.loginMsgNullErroPasswordTextMsg));
				return;
			}
			if(pwd2==null||"".equals(pwd2)){
				CommonUtil.showInfoDialog(this, getString(R.string.loginMsgNullErroPasswordTextMsg));
				return;
			}
			if(!(CommonUtil.isValidMobiNumber(name)||CommonUtil.isValidEmail(name))){
				CommonUtil.showInfoDialog(this, getString(R.string.loginMsgErroUserFormatMsg));
				return;
			}
			if(!pwd.equals(pwd2)){
				CommonUtil.showInfoDialog(this, getString(R.string.loginMsgDifferentPasswordTwiceMsg));
				return;
			}
			
			RequestVo vo = new RequestVo();
			HashMap<String,String> map = new HashMap<String,String>();
			map.put("username", name);
			map.put("password",pwd);
			vo.requestDataMap=map;
			//vo.requestUrl= R.string.register; // 待调试
			vo.jsonParser = new LoginParser();
			vo.context=context;
			getDataFromServer(vo, callback);
			Editor ed =  sp.edit();
			ed.putString("userName", name);
			ed.putString("userPwd", pwd);
			ed.commit();
			showProgressDialog();
			break;
		
		} 
	}

	@Override
	protected void findViewById() {
		login_name_edit = (EditText) findViewById(R.id.login_name_edit);
		login_pwd_edit = (EditText) findViewById(R.id.login_pwd_edit);
		login_pwd2_edit = (EditText) findViewById(R.id.login_pwd2_edit);
		register_text = (TextView) findViewById(R.id.register_text);
	}

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.old_act_login_register);
		sp = getSharedPreferences("userinfo", MODE_PRIVATE);
		setTitle(getString(R.string.loginButtonRegisteNameMsg));
	}

	@Override
	protected void processLogic() {
		sp = getSharedPreferences("userinfo", MODE_PRIVATE);
		DataCallback<UserInfo> callback = new DataCallback<UserInfo>() {

			@Override
			public void processData(UserInfo paramObject, boolean paramBoolean) {
				String userId =  paramObject.userId;
				String usersession = paramObject.usersession;
				Editor ed =  sp.edit();
				ed.putString("userId", userId);
				ed.putString("usersession", usersession);
				ed.commit();
				Intent intent = new Intent (RegisterActivity.this,RestaurantAccountActivity.class);
				startActivity(intent);
				closeProgressDialog();
				
			}
		}; 
		
		this.callback=callback;
	}

	@Override
	protected void setListener() {
		//register_text.setOnClickListener(this);

	}

}
