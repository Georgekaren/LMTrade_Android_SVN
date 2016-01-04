package com.lianmeng.core.favorite.parser;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.lianmeng.core.framework.sysparser.BaseParser;
import com.lianmeng.core.framework.util.Logger;
import com.lianmeng.core.product.vo.Product;

public class FavoriteParser extends BaseParser<List<Product>> {
	private static final String TAG = "FavoriteParser";
	@Override
	public List<Product> parseJSON(String paramString) throws JSONException {
		Logger.d(TAG, "解析收藏夹中的内容");
		JSONObject json = new JSONObject(paramString);
		String productlist = json.getString("DATA_INFO");
		List<Product> products = JSON.parseArray(productlist, Product.class);
		return products;
	}

}
