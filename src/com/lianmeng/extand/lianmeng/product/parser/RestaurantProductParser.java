package com.lianmeng.extand.lianmeng.product.parser;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.lianmeng.core.framework.sysparser.BaseParser;
import com.lianmeng.extand.lianmeng.product.vo.RestaurantProductBuyVo;

public class RestaurantProductParser extends BaseParser<List<RestaurantProductBuyVo>> {

	@Override
	public List<RestaurantProductBuyVo> parseJSON(String paramString) throws JSONException {
		if(super.checkResponse(paramString)!=null){
			JSONObject jsonObject = new JSONObject(paramString);
			String productlist = jsonObject.getString("DATA_INFO");
			return JSON.parseArray(productlist, RestaurantProductBuyVo.class);
		}else{
		return null;
		}
	}

}
