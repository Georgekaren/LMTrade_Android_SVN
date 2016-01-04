package com.lianmeng.core.product.parser;


import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.lianmeng.core.framework.sysparser.BaseParser;
import com.lianmeng.core.product.vo.ProductDetail;

public class ProductDetailParser extends BaseParser<ProductDetail> {
	@Override
	public ProductDetail parseJSON(String paramString) throws JSONException {
		if(super.checkResponse(paramString)!=null){
			JSONObject jsonObject = new JSONObject(paramString);
			String product = jsonObject.getString("DATA_INFO");
			ProductDetail productDetail = JSON.parseObject(product,ProductDetail.class);
			return productDetail;
		}
		return null;
	}

}
