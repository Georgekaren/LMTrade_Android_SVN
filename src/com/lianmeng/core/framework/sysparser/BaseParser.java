package com.lianmeng.core.framework.sysparser;

import org.json.JSONException;
import org.json.JSONObject;

import com.lianmeng.core.framework.util.SysU;

public abstract class BaseParser<T> {

	public abstract T parseJSON(String paramString) throws JSONException;

	/**
	 * 
	 * @param res
	 * @throws JSONException
	 */
	public String checkResponse(String paramString) throws JSONException {
		if (paramString == null) {
			return null;
		} else {
			JSONObject jsonObject = new JSONObject(paramString);
			String result = jsonObject.getString(SysU.RESPONSE_CODE);
			if (result != null && !result.equals("error")) {
				return result;
			} else {
				return null;
			}

		}
	}
}
