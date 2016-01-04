package com.lianmeng.core.product.parser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.lianmeng.core.category.vo.FilterCategory;
import com.lianmeng.core.framework.sysparser.BaseParser;
import com.lianmeng.core.product.vo.ProductListVo;

public class ProductListParserNew extends BaseParser<Map<String,Object>> {

	@Override
	public Map<String, Object> parseJSON(String paramString)
			throws JSONException {
		
			Map<String,Object> map = new HashMap<String, Object>();
			if(super.checkResponse(paramString)!=null){
				JSONObject jsonObject = new JSONObject(paramString);
				String productlist = jsonObject.getString("DATA_INFO");
				List<ProductListVo> productList = JSON.parseArray(productlist, ProductListVo.class);
				map.put("productlist", productList);
				
				String count = jsonObject.getString("DATA_CNT");
				map.put("list_count", count);
				
				String list_filter = jsonObject.getString("PUBS_FINALS");
				map.put("list_filter", JSON.parseObject(list_filter, FilterCategory.class));
				
				return map;
			}else{
			return null;
			}
	}
	
}
