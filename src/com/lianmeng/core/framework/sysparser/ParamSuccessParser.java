package com.lianmeng.core.framework.sysparser;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

/**
 * 请求是否成功，成功为true 
 * @author liu
 *
 */
public class ParamSuccessParser extends BaseParser<String> {

	@Override
	public String parseJSON(String paramString) throws JSONException {
		if (!TextUtils.isEmpty(checkResponse(paramString))) {
		    JSONObject j = new JSONObject(paramString);
            String addresslist = j.getString("DATA_INFO");
			return addresslist;
		}
		return "false";
	}
}
