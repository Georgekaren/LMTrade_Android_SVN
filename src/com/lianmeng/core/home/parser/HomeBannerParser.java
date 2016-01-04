package com.lianmeng.core.home.parser;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.lianmeng.core.framework.sysparser.BaseParser;
import com.lianmeng.core.home.vo.HomeBannerVo;

/**
 * 首页baner 解析
 * @author liu
 *
 */
public class HomeBannerParser extends BaseParser<List<HomeBannerVo>>{

	@Override
	public List<HomeBannerVo> parseJSON(String paramString) throws JSONException {
		if (!TextUtils.isEmpty(checkResponse(paramString))) {
			JSONObject j = new JSONObject(paramString);
 			return JSON.parseArray(j.getString("DATA_INFO"), HomeBannerVo.class);
		}
		return null;
	}

}
