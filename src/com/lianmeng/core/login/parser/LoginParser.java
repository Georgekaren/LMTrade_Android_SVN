package com.lianmeng.core.login.parser;

import org.json.JSONException;
import org.json.JSONObject;

import com.lianmeng.core.framework.sysparser.BaseParser;
import com.lianmeng.core.login.vo.UserInfo;


public class LoginParser extends BaseParser<UserInfo> {

	@Override
	public UserInfo parseJSON(String paramString) throws JSONException {
		UserInfo localUserInfo = new UserInfo();
		if (super.checkResponse(paramString) != null) {
			
			JSONObject jsonObject = new JSONObject(paramString).getJSONObject("USER_INFO");
			String userid = jsonObject.getString("id");
			String usersession = jsonObject.getString("name");
			localUserInfo.userId = userid;
			localUserInfo.usersession = usersession;
			//return localUserInfo;
		}
		return localUserInfo;
	}

}
