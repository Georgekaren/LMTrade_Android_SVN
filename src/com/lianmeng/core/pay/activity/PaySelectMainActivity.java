package com.lianmeng.core.pay.activity;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ComponentName;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.lianmeng.core.activity.R;
import com.lianmeng.core.activity.R.id;
import com.lianmeng.core.activity.R.layout;
import com.lianmeng.core.activity.R.string;
import com.lianmeng.core.framework.sysparser.SuccessParser;
import com.lianmeng.core.framework.sysvo.RequestVo;
import com.lianmeng.core.framework.util.NetUtil;
import com.lianmeng.core.framework.util.SysU;
import com.lianmeng.core.order.activity.OrdrSubmitOkActivity;
import com.pingplusplus.android.PaymentActivity;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
/**
 * @author sunkai
 * 
 * ping++ sdk 示例程序，仅供开发者参考。
 * 【说明文档】https://github.com/PingPlusPlus/pingpp-android/blob/master/docs/ping%2B%2B安卓SDK使用文档.md
 * 
 * 【注意】运行该示例，需要用户填写一个YOUR_URL。
 * 
 * ping++ sdk 使用流程如下：
 * 1）客户端已经有订单号、订单金额、支付渠道
 * 2）客户端请求服务端获得charge。服务端生成charge的方式参考ping++ 官方文档，地址 https://pingxx.com/guidance/server/import
 * 3）收到服务端的charge，调用ping++ sdk 。
 * 4）onActivityResult 中获得支付结构。
 * 5）如果支付成功。服务端会收到ping++ 异步通知，支付成功依据服务端异步通知为准。
 */
public class PaySelectMainActivity extends Activity implements View.OnClickListener{

	/**
	 *开发者需要填一个服务端URL 该URL是用来请求支付需要的charge。务必确保，URL能返回json格式的charge对象。
	 *服务端生成charge 的方式可以参考ping++官方文档，地址 https://pingxx.com/guidance/server/import 
	 *
	 *【 http://218.244.151.190/demo/charge 】是 ping++ 为了方便开发者体验 sdk 而提供的一个临时 url 。
	 * 改 url 仅能调用【模拟支付控件】，开发者需要改为自己服务端的 url 。 
	 */
	private static String YOUR_URL ="http://218.244.151.190/demo/charge";
	public static final String URL = YOUR_URL;
    
    private static final int REQUEST_CODE_PAYMENT = 1;
    
    /**
     * 银联支付渠道
     */
    private static final String CHANNEL_UPACP = "upacp";
    /**
     * 微信支付渠道
     */
    private static final String CHANNEL_WECHAT = "wx";
    /**
     * 支付支付渠道
     */
    private static final String CHANNEL_ALIPAY = "alipay";
    /**
     * 百度支付渠道
     */
    private static final String CHANNEL_BFB = "bfb";
    /**
     * 京东支付渠道
     */
    private static final String CHANNEL_JDPAY_WAP = "jdpay_wap";

    private EditText amountEditText;
    private Button wechatButton;
    private Button alipayButton;
    private Button upmpButton;
    private Button bfbButton;
    private Button jdpayButton;
    private String orderNo="";
    private String currentAmount = "";

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_select_main);
        
        amountEditText = (EditText) findViewById(R.id.amountEditText);
        wechatButton = (Button) findViewById(R.id.wechatButton);
        alipayButton = (Button) findViewById(R.id.alipayButton);
        upmpButton = (Button) findViewById(R.id.upmpButton);
        bfbButton = (Button) findViewById(R.id.bfbButton);
        jdpayButton =(Button) findViewById(R.id.jdpayButton);
        
        wechatButton.setOnClickListener(PaySelectMainActivity.this);
        alipayButton.setOnClickListener(PaySelectMainActivity.this);
        upmpButton.setOnClickListener(PaySelectMainActivity.this);
        bfbButton.setOnClickListener(PaySelectMainActivity.this);
        jdpayButton.setOnClickListener(PaySelectMainActivity.this);
        //PingppLog.DEBUG = true;
    
        amountEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals(currentAmount)) {
                    amountEditText.removeTextChangedListener(this);
                    String replaceable = String.format("[%s, \\s.]", NumberFormat.getCurrencyInstance(Locale.CHINA).getCurrency().getSymbol(Locale.CHINA));
                    String cleanString = s.toString().replaceAll(replaceable, "");

                    if (cleanString.equals("") || new BigDecimal(cleanString).toString().equals("0")) {
                        amountEditText.setText(null);
                    } else {
                        double parsed = Double.parseDouble(cleanString);
                        String formatted = NumberFormat.getCurrencyInstance(Locale.CHINA).format((parsed / 100));
                        currentAmount = formatted;
                        amountEditText.setText(formatted);
                        amountEditText.setSelection(formatted.length());
                    }
                    amountEditText.addTextChangedListener(this);
                }
            }
        });
    }

    public void onClick(View view) {
        String amountText = amountEditText.getText().toString();
        String userId=getIntent().getStringExtra("userId");
        String prodId=getIntent().getStringExtra("prodId");
        
        if (amountText.equals("")) return;

        String replaceable = String.format("[%s, \\s.]", NumberFormat.getCurrencyInstance(Locale.CHINA).getCurrency().getSymbol(Locale.CHINA));
        String cleanString = amountText.toString().replaceAll(replaceable, "");
        int amount = Integer.valueOf(new BigDecimal(cleanString).toString());

        // 支付宝，微信支付，银联，百度钱包 按键的点击响应处理
        if (view.getId() == R.id.upmpButton) {
            new PaymentTask().execute(new PaymentRequest(CHANNEL_UPACP, amount,userId,prodId));
        } else if (view.getId() == R.id.alipayButton) {
            new PaymentTask().execute(new PaymentRequest(CHANNEL_ALIPAY, amount,userId,prodId));
        } else if (view.getId() == R.id.wechatButton) {
            new PaymentTask().execute(new PaymentRequest(CHANNEL_WECHAT, amount,userId,prodId));
        } else if (view.getId() == R.id.bfbButton) {
        	new PaymentTask().execute(new PaymentRequest(CHANNEL_BFB, amount,userId,prodId));
        } else if(view.getId() == R.id.jdpayButton){
        	new PaymentTask().execute(new PaymentRequest(CHANNEL_JDPAY_WAP, amount,userId,prodId));
        }
    }

    class PaymentTask extends AsyncTask<PaymentRequest, Void, String> {

        @Override
        protected void onPreExecute() {

            //按键点击之后的禁用，防止重复点击
            wechatButton.setOnClickListener(null);
            alipayButton.setOnClickListener(null);
            upmpButton.setOnClickListener(null);
            bfbButton.setOnClickListener(null);
        }

        @Override
        protected String doInBackground(PaymentRequest... pr) {

            PaymentRequest paymentRequest = pr[0];
            String data = null;
            String json = new Gson().toJson(paymentRequest);
                json="{\"ServiceName\":\"payOrderManagerService\" , \"Data\":{\"ACTION\":\"MIMPAYORDERLIST\",\"userId\":\""+SysU.USERID+"\",\"prodId\":"+paymentRequest.prodId+",\"channel\":\""+paymentRequest.channel+"\",\"amount\":\""+paymentRequest.amount+"\"}}";
            try {
                //向Your Ping++ Server SDK请求数据
            	String paramString = postJson(getString(R.string.sysRequestHost)+getString(R.string.sysRequestServLet), json);
            	if(checkResponse(paramString)!=null){
                 JSONObject jsonObject = new JSONObject(paramString.replace("\\/", "[[\\/]]").replace("\\\"", "[[\\\"]]"));
                 //String newData=checkNewCharge(jsonObject.getJSONObject("DATA_INFO"));
    			 data = jsonObject.getString("DATA_INFO");
    			 orderNo= jsonObject.getString("ORDERNO");
    			 data=data.replace("[[/]]", "\\/").replace("[[\"]]", "\\\"");
    			 
            	}
            	//data=postJson(URL, json);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return data;
        }

        /**
         * 获得服务端的charge，调用ping++ sdk。
         */
        @Override
        protected void onPostExecute(String data) {
        	if(null==data){
        		showMsg(SysU.payLableProdRequestErro,SysU.payLableProdCheckUrl, SysU.payLableProdCheckUrlNoCharge);
        		return;
        	}
        	Log.d("charge", data);
        	
            Intent intent = new Intent();
            String packageName = getPackageName();
            ComponentName componentName = new ComponentName(packageName, packageName + ".wxapi.WXPayEntryActivity");
            intent.setComponent(componentName);
            intent.putExtra(PaymentActivity.EXTRA_CHARGE, data);
            startActivityForResult(intent, REQUEST_CODE_PAYMENT);
        }

    }
    
	/**
	 * onActivityResult 获得支付结果，如果支付成功，服务器会收到ping++ 服务器发送的异步通知。
	 * 最终支付成功根据异步通知为准
	 */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        wechatButton.setOnClickListener(PaySelectMainActivity.this);
        alipayButton.setOnClickListener(PaySelectMainActivity.this);
        upmpButton.setOnClickListener(PaySelectMainActivity.this);
        bfbButton.setOnClickListener(PaySelectMainActivity.this);

        //支付页面返回处理
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getExtras().getString("pay_result");
                /* 处理返回值
                 * "success" - payment succeed
                 * "fail"    - payment failed
                 * "cancel"  - user canceld
                 * "invalid" - payment plugin not installed
                 */
                String errorMsg = data.getExtras().getString("error_msg"); // 错误信息
                String extraMsg = data.getExtras().getString("extra_msg"); // 错误信息
                showMsg(result, errorMsg, extraMsg);
                if("success".equals(result)){
                	
                	HashMap<String, String> requestDataMap = new HashMap<String, String>();
					String inmapData="{\"ServiceName\":\"payOrderManagerService\" , \"Data\":{\"ACTION\":\"MIMPAYORDERLIST\",\"userId\":\""+SysU.USERID+"\",\"orderNo\":\""+orderNo+"\"}}";
					requestDataMap.put("JsonData", inmapData);
					RequestVo vo = new RequestVo(R.string.sysRequestServLet, this, requestDataMap,
							new SuccessParser());
					Boolean bool = (Boolean) NetUtil.post(vo);
					if (bool != null) {
						if (bool) {
							Intent intent = new Intent(this,OrdrSubmitOkActivity.class);
		        			startActivity(intent);
						}
					}
                	
                	
                }
            }
        }
    }
    
    public void showMsg(String title, String msg1, String msg2) {
    	String str = title;
    	if (null !=msg1 && msg1.length() != 0) {
    		str += "\n" + msg1;
    	}
    	if (null !=msg2 && msg2.length() != 0) {
    		str += "\n" + msg2;
    	}
    	AlertDialog.Builder builder = new Builder(PaySelectMainActivity.this);
    	builder.setMessage(str);
    	builder.setTitle(SysU.SIGN);
    	builder.setPositiveButton(SysU.OK, null);
    	builder.create().show();
    }

    private static String postJson(String url, String json) throws IOException {
        MediaType type = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(type, json);
        Request request = new Request.Builder().url(url).post(body).build();

        OkHttpClient client = new OkHttpClient();
        Response response = client.newCall(request).execute();

        return response.body().string();
    }

    class PaymentRequest {
        String channel;
        int amount;
        String userid;
        String prodId;
        
        public PaymentRequest(String channel, int amount,String userid,String prodId) {
            this.channel = channel;
            this.amount = amount;
            this.userid = userid;
            this.prodId = prodId;
        }
    }
    
    
    /**
	 * 
	 * @param res
	 * @throws JSONException
	 */
	public String checkResponse(String paramString) throws JSONException {
		if (paramString == null) {
			return null;
		} else {
			JSONObject jsonObject = new JSONObject(paramString);
			String result = jsonObject.getString(SysU.RESPONSE_CODE);
			if (result != null && !result.equals("error")) {
				return result;
			} else {
				return null;
			}

		}
	}
	
	
	

}
