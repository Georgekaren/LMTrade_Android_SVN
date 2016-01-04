package com.lianmeng.core.framework.sysparser;

import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.lianmeng.core.address.vo.AddressDetail;
import com.lianmeng.core.framework.sysvo.StringV;

/**
 * 地址详细信息
 * 
 * @author liu
 * 
 */
public class StringVParser extends BaseParser<List<StringV>> {

	@Override
	public List<StringV> parseJSON(String paramString) throws JSONException {
		if (!TextUtils.isEmpty(checkResponse(paramString))) {
			JSONObject j = new JSONObject(paramString);
			String addresslist = j.getString("DATA_INFO");
			return JSON.parseArray(addresslist, StringV.class);
		}
		return null;
	}
}
