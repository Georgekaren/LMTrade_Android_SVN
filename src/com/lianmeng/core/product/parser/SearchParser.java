package com.lianmeng.core.product.parser;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.lianmeng.core.framework.sysparser.BaseParser;
import com.lianmeng.core.product.vo.ProductListVo;

public class SearchParser extends BaseParser<List<ProductListVo>> {

	@Override
	public List<ProductListVo> parseJSON(String paramString) throws JSONException {
		if(super.checkResponse(paramString)!=null){
			JSONObject jsonObject = new JSONObject(paramString);
			String search_keywords = jsonObject.getString("DATA_INFO");
			List<ProductListVo> products = JSON.parseArray(search_keywords, ProductListVo.class);
			return products;
		}
		return null;
	}

}
