package com.lianmeng.core.login.parser;


import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.lianmeng.core.framework.sysparser.BaseParser;
import com.lianmeng.core.framework.util.Logger;
import com.lianmeng.core.login.vo.User;

public class UserinfoParser extends BaseParser<User> {

	private static final String TAG = "UserinfoParser";

	@Override
	public User parseJSON(String paramString) throws JSONException {
		User userInfoList=null;
		if(super.checkResponse(paramString)!=null){
			Logger.d(TAG, "解析Userinfo数据");
			JSONObject json = new JSONObject(paramString);
			String useinfoObj = json.getString("USER_INFO");
		    userInfoList = JSON.parseObject(useinfoObj, User.class);
			//return userInfoList;
		}
		return userInfoList;
	}

}
