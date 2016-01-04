package com.lianmeng.core.order.parser;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.lianmeng.core.framework.sysparser.BaseParser;
import com.lianmeng.core.framework.util.Logger;
import com.lianmeng.core.order.vo.OrderList;

public class OrderListParser extends BaseParser<List<OrderList>> {

	private static final String TAG = "OrderListParser";

	@Override
	public List<OrderList> parseJSON(String paramString) throws JSONException {
		Logger.d(TAG, "解析OrderList订单列表数据");
		if(paramString == null){
			return null;
		}else{
			JSONObject json = new JSONObject(paramString);
			String result = json.getString("response");
			if(result!=null && !result.equals("error")){
				String orderlist = json.getString("DATA_INFO");
				List<OrderList> list = JSON.parseArray(orderlist, OrderList.class);
				return list;
			}else{
				return null;
			}
		}
	}
}
