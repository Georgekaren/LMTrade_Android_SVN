package com.lianmeng.core.framework.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.lianmeng.core.activity.R;
import com.lianmeng.core.framework.sysvo.RequestVo;

/**
 * 
 * @author Mathew
 * 
 */
public class NetUtil {
	private static final String NOT_LOGIN = "notlogin";
	private static final String TAG = "NetUtil";
	private static Header[] headers = new BasicHeader[11];
	static {
		headers[0] = new BasicHeader("Appkey", "");
		headers[1] = new BasicHeader("Udid", "");
		headers[2] = new BasicHeader("Os", "");
		headers[3] = new BasicHeader("Osversion", "");
		headers[4] = new BasicHeader("Appversion", "");
		headers[5] = new BasicHeader("Sourceid", "");
		headers[6] = new BasicHeader("Ver", "");
		headers[7] = new BasicHeader("Userid", "");
		headers[8] = new BasicHeader("Usersession", "");
		headers[9] = new BasicHeader("Unique", "");
		headers[10] = new BasicHeader("Cookie", "");
		//headers[11] = new BasicHeader("charset", "UTF-8");
		
		
	}
	
	/*
	 * 
	 */
	public static Object post(RequestVo vo) {
		DefaultHttpClient client = new DefaultHttpClient();
		String url = vo.context.getString(R.string.sysRequestHost).concat(vo.context.getString(vo.requestUrl));
		Logger.d(TAG, "Post " + url);
		HttpPost post = new HttpPost(url);
		post.setHeaders(headers);
		post.addHeader("charset", HTTP.UTF_8);
		Object obj = null;
		try {
			if (vo.requestDataMap != null) {
				HashMap<String, String> map = vo.requestDataMap;
				if(map.get("JsonData")!=null&&!"".equals(map.get("JsonData"))){
					post.addHeader(HTTP.CONTENT_TYPE, SysU.APPLICATION_JSON);
			        StringEntity se = new StringEntity(URLEncoder.encode(map.get("JsonData")).toString(), HTTP.UTF_8);
			        se.setContentType(SysU.CONTENT_TYPE_TEXT_JSON);
			        se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, SysU.APPLICATION_JSON));
			        post.setEntity(se);
				}else{
				ArrayList<BasicNameValuePair> pairList = new ArrayList<BasicNameValuePair>();
				for (Map.Entry<String, String> entry : map.entrySet()) {
					BasicNameValuePair pair = new BasicNameValuePair(entry.getKey(), entry.getValue());
					pairList.add(pair);
				}
				HttpEntity entity = new UrlEncodedFormEntity(pairList, vo.context.getString(R.string.sysRequestCharacter));
				post.setEntity(entity);
				}
			}
			HttpResponse response = client.execute(post);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				setCookie(response);
				String result = EntityUtils.toString(response.getEntity(), "UTF-8");
				try {
					if (invilidateLogin(result)) {
						return Status.Login;
					}
					obj = vo.jsonParser.parseJSON(result);
				} catch (JSONException e) {
					Logger.e(TAG, e.getLocalizedMessage(), e);
				}
				return obj;
			}
		} catch (ClientProtocolException e) {
			Logger.e(TAG, e.getLocalizedMessage(), e);
		} catch (IOException e) {
			Logger.e(TAG, e.getLocalizedMessage(), e);
		}
 		return null;
	}

	/**
	 * 添加Cookie
	 * @param response
	 */
	private static void setCookie(HttpResponse response) {
		if (response.getHeaders("Set-Cookie").length > 0) {
			Logger.d(TAG, response.getHeaders("Set-Cookie")[0].getValue()) ;
			headers[10] = new BasicHeader("Cookie", response.getHeaders("Set-Cookie")[0].getValue());
		}
	}
	/**
	 * 验证是否需要登录
	 * @param result
	 * @return
	 * @throws JSONException
	 */
	private static boolean invilidateLogin(String result) throws JSONException {
		JSONObject jsonObject = new JSONObject(result);
		String responseCode = jsonObject.getString("response");
		if (NOT_LOGIN.equals(responseCode)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @param vo
	 * @return
	 */
	public static Object get(RequestVo vo) {
		DefaultHttpClient client = new DefaultHttpClient();
		String url = vo.context.getString(R.string.sysRequestHost).concat(vo.context.getString(vo.requestUrl));
		Logger.d(TAG, "Get " + url);
		HttpGet get = new HttpGet(url);
		get.setHeaders(headers);
		Object obj = null;
		try {

			HttpResponse response = client.execute(get);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				setCookie(response);
				String result = EntityUtils.toString(response.getEntity(), vo.context.getString(R.string.sysRequestCharacter));
				Logger.d(TAG, result);
				try {
					if (invilidateLogin(result)) {
						return Status.Login;
					}
					obj = vo.jsonParser.parseJSON(result);
				} catch (JSONException e) {
					Logger.e(TAG, e.getLocalizedMessage(), e);
				}
				return obj;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获得网络连接是否可用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean hasNetwork(Context context) {
		ConnectivityManager con = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo workinfo = con.getActiveNetworkInfo();
		if (workinfo == null || !workinfo.isAvailable()) {
			Toast.makeText(context, R.string.net_error, Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
	
	public static enum Status {
		Login
	}
	
	/**
	 * 
	 * @param vo
	 * @return
	 */
	public static Object getNew(RequestVo vo) {
		DefaultHttpClient client = new DefaultHttpClient();
		String ingetData="";
		if (vo.requestDataMap != null) {
			HashMap<String, String> map = vo.requestDataMap;
			ingetData=map.get(vo.context.getString(R.string.sysRequestGetDataFlag));
			ingetData="?"+vo.context.getString(R.string.sysRequestGetDataFlag)+"="+ingetData;
		}
		String strUrl = vo.context.getString(R.string.sysRequestHost).concat(vo.context.getString(vo.requestUrl));
		Logger.d(TAG, "Get " + strUrl);
		
		URI uri = null;
		try {
			URL url = new URL(strUrl+ingetData);
		    uri = new URI(url.getProtocol(), url.getHost()+":"+url.getPort(), url.getPath(), url.getQuery(),null);
			
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		HttpGet get = new HttpGet(uri);
		get.setHeaders(headers);
		Object obj = null;
		try {

			HttpResponse response = client.execute(get);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				setCookie(response);
				String result = EntityUtils.toString(response.getEntity(), vo.context.getString(R.string.sysRequestCharacter));
				Logger.d(TAG, result);
				try {
					if (invilidateLogin(result)) {
						return Status.Login;
					}
					obj = vo.jsonParser.parseJSON(result);
				} catch (JSONException e) {
					Logger.e(TAG, e.getLocalizedMessage(), e);
				}
				return obj;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
     * Description: <br>
     * 
     * @author XXX<br>
     * @taskId <br>
     * @param inputStr s
     * @return <br>
     */
    private static String xmlParseConvert(String inputStr) {

        return inputStr.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("'", "&apos;").replace("\"", "&quot;").replace("{", "%7B").replace("}", "%7D");

    }
    
    /*
	 * 
	 */
	public static Object postNew(RequestVo vo) {
		DefaultHttpClient client = new DefaultHttpClient();
		String url = vo.context.getString(R.string.sysRequestHost).concat(vo.context.getString(vo.requestUrl));
		Logger.d(TAG, "Post " + url);
		HttpPost post = new HttpPost(url);
		post.setHeaders(headers);
		Object obj = null;
		try {
			if (vo.requestDataMap != null) {
				HashMap<String, String> map = vo.requestDataMap;
				ArrayList<BasicNameValuePair> pairList = new ArrayList<BasicNameValuePair>();
				for (Map.Entry<String, String> entry : map.entrySet()) {
					BasicNameValuePair pair = new BasicNameValuePair(entry.getKey(), entry.getValue());
					pairList.add(pair);
				}
				HttpEntity entity = new UrlEncodedFormEntity(pairList, vo.context.getString(R.string.sysRequestCharacter));
				post.setEntity(entity);
			}
			HttpResponse response = client.execute(post);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				setCookie(response);
				String result = EntityUtils.toString(response.getEntity(), "UTF-8");
				try {
					if (invilidateLogin(result)) {
						return Status.Login;
					}
					obj = vo.jsonParser.parseJSON(result);
				} catch (JSONException e) {
					Logger.e(TAG, e.getLocalizedMessage(), e);
				}
				return obj;
			}
		} catch (ClientProtocolException e) {
			Logger.e(TAG, e.getLocalizedMessage(), e);
		} catch (IOException e) {
			Logger.e(TAG, e.getLocalizedMessage(), e);
		}
 		return null;
	}

	
}
