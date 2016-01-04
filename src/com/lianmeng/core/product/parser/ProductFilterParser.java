package com.lianmeng.core.product.parser;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.lianmeng.core.category.vo.FilterCategory;
import com.lianmeng.core.framework.sysparser.BaseParser;

public class ProductFilterParser extends BaseParser<List<FilterCategory>> {

	@Override
	public List<FilterCategory> parseJSON(String paramString)
			throws JSONException {
		if (super.checkResponse(paramString) != null) {
			JSONObject jsonObject = new JSONObject(paramString);
			String filter = jsonObject.getString("PUBS_FINALS");
			return JSON.parseArray(filter, FilterCategory.class);
		}else{

		return null;
		}
	}

	

}
