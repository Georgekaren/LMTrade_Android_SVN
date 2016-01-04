package com.lianmeng.core.product.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.lianmeng.core.framework.sysparser.BaseParser;

public class SearchRecommondParser extends BaseParser<String[]> {

	@Override
	public String[] parseJSON(String paramString) throws JSONException {
		if(super.checkResponse(paramString)!=null){
			JSONObject jsonObject = new JSONObject(paramString);
			String search_keywords = jsonObject.getString("DATA_INFO");
			ArrayList<HashMap<String,String>> searchMapList= JSON.parseObject(search_keywords, ArrayList.class);
			
			String[] search = new String[searchMapList.size()];
			for(int i=0;i<searchMapList.size();i++){
				search[i]=searchMapList.get(i).get("name");
			}
			return search;
		}
		return null;
		
	}

}
