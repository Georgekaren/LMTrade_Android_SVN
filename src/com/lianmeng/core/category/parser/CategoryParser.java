package com.lianmeng.core.category.parser;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.lianmeng.core.category.vo.CategoryVo;
import com.lianmeng.core.framework.sysparser.BaseParser;

public class CategoryParser extends BaseParser<List<CategoryVo>> {

	@Override
	public List<CategoryVo> parseJSON(String paramString) throws JSONException {
		JSONObject obj = new JSONObject(paramString);
		String str = obj.getString("DATA_INFO");
		return JSON.parseArray(str, CategoryVo.class);
	}

	

}
