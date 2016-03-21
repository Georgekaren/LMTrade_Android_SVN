package com.lianmeng.extand.lianmeng.discover.json;

import org.json.JSONException;

import com.lianmeng.extand.lianmeng.discover.json.FindHairParser.HairResult;
import com.lianmeng.extand.lianmeng.discover.json.HairCommentParser.HairComResult;
import com.lianmeng.extand.lianmeng.discover.json.ZoneAllParser.ZoneResult;
import com.lianmeng.extand.lianmeng.discover.net.ActionOfUrl.JsonAction;


public class JsonParser {

	synchronized public static JsonResult parse(String response, JsonAction act)
			throws JSONException {
		switch(act) {
		case FINDHAIR:
			return getFindHair(response);
		case ZONEALL:
			return getZoneAll(response);
		case HAIR_COMMENT:
			return getHairComment(response);
		default :
			return null;
			
		}
		
	}
	public static HairResult getFindHair(String json) throws JSONException {
		FindHairParser parser=new FindHairParser();
		return (HairResult) parser.parse(json);
	}
	public static ZoneResult getZoneAll(String json) throws JSONException {
		ZoneAllParser parser=new ZoneAllParser();
		return (ZoneResult) parser.parse(json);
	}
	public static HairComResult getHairComment(String json) throws JSONException {
		HairCommentParser parser=new HairCommentParser();
		return (HairComResult) parser.parse(json);
	}
}
