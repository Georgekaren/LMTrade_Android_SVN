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
import android.widget.Toast;

import com.lianmeng.core.account.activity.RestaurantAccountActivity;
import com.lianmeng.core.activity.R;
import com.lianmeng.core.framework.sysvo.RequestVo;
import com.lianmeng.core.framework.util.CommonUtil;
import com.lianmeng.core.framework.util.SysU;
import com.lianmeng.core.login.parser.LoginParser;
import com.lianmeng.core.login.vo.UserInfo;
import com.lianmeng.extand.lianmeng.product.activity.RestaurantBaseActivity;

public class RestaurantLoginActivity extends RestaurantBaseActivity {
	private EditText login_name_edit;
	private EditText login_pwd_edit;
	//private TextView login_text;
	//private TextView register_text;
	//private CheckBox remember_pwd_checkbox;
	private SharedPreferences sp;
	private String userName;
	private String userPwd;
	private Button btn_login;
	private TextView to_register_txt;
	private ImageView iv_left;
	//private boolean autoLogin;
	//private boolean isnotLogin;

	/** 登录请求码 */
    public static final int NOT_LOGIN = 403;
    /** 登录结果码*/
    public static final int LOGIN_SUCCESS = 10000000;

    private int rtnType=0;
	protected void findViewById() {
		login_name_edit = (EditText) findViewById(R.id.edt_account);
		login_pwd_edit = (EditText) findViewById(R.id.edt_password);
		btn_login = (Button) findViewById(R.id.btn_login);
		to_register_txt = (TextView) findViewById(R.id.to_register_txt);
		iv_left  = (ImageView) findViewById(R.id.iv_left);
		//login_text = (TextView) findViewById(R.id.login_text);
		//register_text = (TextView) findViewById(R.id.register_text);
		//remember_pwd_checkbox = (CheckBox) findViewById(R.id.remember_pwd_checkbox);
	}

	protected void loadViewLayout() {
		sp = getSharedPreferences("userinfo", MODE_PRIVATE);
		setContentView(R.layout.act_login);
		setTitle(getString(R.string.loginTitleLoginButtonNameMsg));
		if(this.getIntent()!=null&&this.getIntent().getExtras()!=null&&this.getIntent().getExtras().containsKey("type")){
		    rtnType=(this.getIntent().getExtras().get("type")==null)?0:this.getIntent().getExtras().getInt("type");
		}else{
		    rtnType=0;
		}
	}

	protected void processLogic() {
		/*if (getIntent().getStringExtra("notlogin") != null) {
			isnotLogin = true;
		}
		this.autoLogin = sp.getBoolean("autoLogin", true);
		if (this.autoLogin) {

		}*/

	}

    @Override
    protected void setListener() {
        btn_login.setOnClickListener(this);
        to_register_txt.setOnClickListener(this);
        iv_left.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                finish();
                break;
            case R.id.btn_login:
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
                                Toast.makeText(RestaurantLoginActivity.this, getString(R.string.loginMsgSignErroUserMsg), Toast.LENGTH_LONG).show();
                                return;
                            }
                            Editor ed = sp.edit();

                            ed.putString("userName", RestaurantLoginActivity.this.userName);
                            ed.putString("userPwd", RestaurantLoginActivity.this.userPwd);
                           /* if (remember_pwd_checkbox.isChecked()) {
                                ed.putBoolean("autoLogin", true);
                            } else {
                                ed.putBoolean("autoLogin", false);
                            }*/
                            ed.putBoolean("autoLogin", false);
                            ed.commit();

                            SysU.USERID=paramObject.getUserId();
                            if(rtnType>0){
                                finish();
                            }else{
                            Intent intent = new Intent(RestaurantLoginActivity.this, RestaurantAccountActivity.class);
                            startActivity(intent);
                            }
                            
                            finish();
                        } else {
                            Toast.makeText(RestaurantLoginActivity.this, getString(R.string.loginMsgSignErroUserMsg), Toast.LENGTH_LONG).show();
                        }
                    }
                });

                break;
            case R.id.to_register_txt:
                Intent register = new Intent(RestaurantLoginActivity.this, RestaurantRegisterActivity.class);
                startActivity(register);

                break;

            }
    }

  
}
