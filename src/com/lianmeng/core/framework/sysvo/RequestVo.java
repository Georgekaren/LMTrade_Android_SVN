package com.lianmeng.core.framework.sysvo;

import java.util.HashMap;

import com.lianmeng.core.framework.sysparser.BaseParser;

import android.content.Context;


public class RequestVo {
	public int requestUrl;
	public Context context;
	public HashMap<String, String> requestDataMap;
	public BaseParser<?> jsonParser;

	public RequestVo() {
	}

	public RequestVo(int requestUrl, Context context, HashMap<String, String> requestDataMap, BaseParser<?> jsonParser) {
		super();
		this.requestUrl = requestUrl;
		this.context = context;
		this.requestDataMap = requestDataMap;
		this.jsonParser = jsonParser;
	}
}
