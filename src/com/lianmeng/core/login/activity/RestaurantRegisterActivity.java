package com.lianmeng.core.login.activity;

import java.util.HashMap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lianmeng.core.account.activity.RestaurantAccountActivity;
import com.lianmeng.core.activity.R;
import com.lianmeng.core.framework.sysvo.RequestVo;
import com.lianmeng.core.framework.util.CommonUtil;
import com.lianmeng.core.framework.util.SysU;
import com.lianmeng.core.login.parser.LoginParser;
import com.lianmeng.core.login.vo.UserInfo;
import com.lianmeng.extand.lianmeng.product.activity.RestaurantBaseActivity;

public class RestaurantRegisterActivity extends RestaurantBaseActivity {
	private EditText login_name_edit;
	private EditText login_pwd_edit;
	private EditText edt_account;
	private Button btn_register; 
	private TextView to_login_txt;
	private ImageView iv_left;
	private DataCallback<UserInfo> callback;
	private SharedPreferences sp;
	 
	
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btn_register:
			String name =  login_name_edit.getText().toString().trim();
			String pwd = login_pwd_edit.getText().toString().trim();
			String telephone = edt_account.getText().toString().trim();
			if(name==null||"".equals(name)){
				CommonUtil.showInfoDialog(this, getString(R.string.loginMsgNullErroUserNameMsg));
				return ;
			}
			if(telephone==null||"".equals(telephone)){
                CommonUtil.showInfoDialog(this, getString(R.string.loginMsgNullErroTelephoneMsg));
                return ;
            }
			if(pwd==null||"".equals(pwd)){
				CommonUtil.showInfoDialog(this, getString(R.string.loginMsgNullErroPasswordTextMsg));
				return;
			}
			
			if(!(CommonUtil.isValidMobiNumber(telephone)||CommonUtil.isValidEmail(telephone))){
				CommonUtil.showInfoDialog(this, getString(R.string.loginMsgErroUserFormatMsg));
				return;
			}
			
			
			RequestVo vo = new RequestVo();
			HashMap<String,String> map = new HashMap<String,String>();
			map.put("username", name);
			map.put("password",pwd);
			String inmapData="{\"ServiceName\":\"userManagerService\" , \"Data\":{\"ACTION\":\"ADDUSER\",\"name\":\""+name+"\",\"password\":\""+pwd+"\",\"telephone\":\""+telephone+"\"}}";
			map.put("JsonData", inmapData);
			vo.requestDataMap=map;
			vo.requestUrl= R.string.sysRequestServLet; // 待调试
			vo.jsonParser = new LoginParser();
			vo.context=context;
			getDataFromServer(vo, callback);
			Editor ed =  sp.edit();
			ed.putString("userName", name);
			ed.putString("userPwd", pwd);
			ed.commit();
			showProgressDialog();
			break;
		case R.id.to_login_txt:
		    Intent intent = new Intent (RestaurantRegisterActivity.this,RestaurantLoginActivity.class);
            startActivity(intent);
		    break;
		case R.id.iv_left:
		    finish();
		    break;
		} 
	}

	@Override
	protected void findViewById() {
		login_name_edit = (EditText) findViewById(R.id.edt_username);
		edt_account = (EditText) findViewById(R.id.edt_account);
		login_pwd_edit = (EditText) findViewById(R.id.edt_password);
		btn_register = (Button) findViewById(R.id.btn_register);
		to_login_txt = (TextView) findViewById(R.id.to_login_txt);
		iv_left  = (ImageView) findViewById(R.id.iv_left);
	}

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.act_register);
		sp = getSharedPreferences("userinfo", MODE_PRIVATE);
		setTitle(getString(R.string.loginButtonRegisteNameMsg));
	}

	@Override
	protected void processLogic() {
		sp = getSharedPreferences("userinfo", MODE_PRIVATE);
		DataCallback<UserInfo> callback = new DataCallback<UserInfo>() {

			@Override
			public void processData(UserInfo paramObject, boolean paramBoolean) {
			    if(paramObject!=null&&paramObject.getUserId()!=null){
				String userId =  paramObject.userId;
				String usersession = paramObject.usersession;
				Editor ed =  sp.edit();
				ed.putString("userId", userId);
				ed.putString("usersession", usersession);
				ed.commit();
				SysU.USERID=userId;
				Intent intent = new Intent (RestaurantRegisterActivity.this,RestaurantAccountActivity.class);
				startActivity(intent);
			    }
				closeProgressDialog();
				
			}
		}; 
		
		this.callback=callback;
	}

	@Override
	protected void setListener() {
	    btn_register.setOnClickListener(this);
	    to_login_txt.setOnClickListener(this);
	    iv_left.setOnClickListener(this);
	}

}
