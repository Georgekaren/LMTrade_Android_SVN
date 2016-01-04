package com.lianmeng.extand.lianmeng.product.parser;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.lianmeng.core.framework.sysparser.BaseParser;
import com.lianmeng.extand.lianmeng.product.vo.BulletinVo;

public class BulletinParser extends BaseParser<List<BulletinVo>> {

	@Override
	public List<BulletinVo> parseJSON(String paramString) throws JSONException {
		if (super.checkResponse(paramString) != null) {
			JSONObject jsonObject = new JSONObject(paramString);
			String topic = jsonObject.getString("DATA_INFO");
			return JSON.parseArray(topic, BulletinVo.class);
		}else{

		return null;
		}
	}

}
