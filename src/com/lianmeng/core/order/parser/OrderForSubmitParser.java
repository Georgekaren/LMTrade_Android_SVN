package com.lianmeng.core.order.parser;

import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.lianmeng.core.framework.sysparser.BaseParser;
import com.lianmeng.core.framework.util.Logger;
import com.lianmeng.core.order.vo.OrderForSubmit;
public class OrderForSubmitParser extends BaseParser<OrderForSubmit> {

	private static final String TAG = "OrderForSubmitParser";

	@Override
	public OrderForSubmit parseJSON(String paramString) throws JSONException {
		JSONObject json = new JSONObject(paramString);
		Logger.d(TAG, "解析OrderList订单列表数据");
		String response = json.getString("response");
		if(response==null){
			Logger.d(TAG, "获取数据失败");
			return null;
		}else{
			String orderinfo = json.getString("orderinfo");
			if(orderinfo!=null){
				OrderForSubmit order = JSON.parseObject(orderinfo, OrderForSubmit.class);
				System.out.println(order.getOrderid());
				System.out.println(order.getPaymenttype());
				System.out.println(order.getPrice());
				return order;
			}else{
				Logger.d(TAG, "orderinfo解析失败");
			}
		}
		return null;
	}

}
