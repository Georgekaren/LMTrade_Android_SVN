package com.lianmeng.core.pay.activity;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.lianmeng.core.activity.R;
import com.lianmeng.core.address.activity.RestaurantSelectAddressActivity;
import com.lianmeng.core.address.vo.Address;
import com.lianmeng.core.address.vo.AddressDetail;
import com.lianmeng.core.framework.engine.SyncImageLoader;
import com.lianmeng.core.framework.sysparser.SuccessParser;
import com.lianmeng.core.framework.sysvo.RequestVo;
import com.lianmeng.core.framework.util.Logger;
import com.lianmeng.core.framework.util.SysU;
import com.lianmeng.core.login.activity.RestaurantLoginActivity;
import com.lianmeng.core.order.activity.RestaurantOrdrSubmitOkActivity;
import com.lianmeng.core.order.vo.CheckoutAddup;
import com.lianmeng.core.order.vo.Delivery;
import com.lianmeng.core.order.vo.InvoiceInfo;
import com.lianmeng.core.pay.parser.PaymentCenterParser;
import com.lianmeng.core.pay.vo.Payment;
import com.lianmeng.core.scar.vo.CartProduct;
import com.lianmeng.extand.lianmeng.product.activity.RestaurantBaseActivity;
import com.pingplusplus.android.PaymentActivity;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class RestaurantPaymentCenterActivity extends RestaurantBaseActivity implements View.OnClickListener{
	private static final String TAG = null;
	private List<CartProduct> productlistInfo;
	//private ListView payment_product_list;
	private LinearLayout payment_product_list_mGallery; 
	private LayoutInflater mInflater;
	private SyncImageLoader syncImageLoader;
	//收货地址
	private TextView textAdress2;
	private TextView textAdress3;
	//private TextView textAdress4;
	//支付方式
	//private TextView payment_payValue_text;
	//送货时间
	private TextView payment_sendTimeValue_text;
	//发票
	//private TextView payment_invoiceValue_text;
	//留言
	private TextView payment_remarkView_text;
	//数量总计
	private TextView shopcar_total_buycount_text;
	//赠送总积分
	//private TextView shopcar_total_bonus_text;
	//金额总计
	private TextView shopcar_total_money_text;
	//提交按钮
	private TextView ordr_submit_bottom_text;
	
	private TextView shopcar_aof_tv_amount;
	private TextView shopcar_aof_tv_needToPay;
	
	//@InjectView(R.id.iv_left)
	private ImageView head_left;
	//@InjectView(R.id.mid_title)
	private TextView head_title;
	
	private String orderNo="";
	private double needPrice=0.0;
	private int payType=0;
	
	private RadioGroup lll_order_sel_payment;
	private RadioButton lll_order_weixin_payment;
	private RadioButton lll_order_wangyin_payment;
    
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ordr_submit_bottom_text:
		    
			//finish();
			//Intent intent = new Intent(this,OrdrSubmitOkActivity.class);
			//startActivity(intent);
			//Intent intent = new Intent(this,PaySelectMainActivity.class);
			//intent.putExtra("userId", SysU.USERID);
			String prodIds="[";
			for(int i=0;i<productlistInfo.size();i++){
				prodIds=prodIds+"\""+productlistInfo.get(i).id+"\",";
			}
			prodIds=prodIds+"]";
			//intent.putExtra("prodId",prodIds);
			//startActivity(intent);
                if (productlistInfo.size() > 0) {
                    ordr_submit_bottom_text.setEnabled(false);
                    ordr_submit_bottom_text.setSelected(false);
                    String userId = SysU.USERID;
                    // 支付宝，微信支付，银联，百度钱包 按键的点击响应处理
                    String replaceable = String.format("[%s, \\s.]", NumberFormat.getCurrencyInstance(Locale.CHINA)
                        .getCurrency().getSymbol(Locale.CHINA));
                    String cleanString = String.valueOf(needPrice).replaceAll(replaceable, "");
                    int amount = Integer.valueOf(new BigDecimal(cleanString).toString());
                    // int amount = Integer.valueOf(new BigDecimal(needPrice).toString());
                    if (payType == 2) {
                        new PaymentTask().execute(new PaymentRequest(CHANNEL_UPACP, amount, userId, prodIds));
                    }
                    else if (payType == 1) {
                        new PaymentTask().execute(new PaymentRequest(CHANNEL_WECHAT, amount, userId, prodIds));
                    }
                    else {
                        new PaymentTask().execute(new PaymentRequest(CHANNEL_ALIPAY, amount, userId, prodIds));
                    }
                }
			break;
		
		case R.id.select_address:
		    Intent intent2 = new Intent(this,RestaurantSelectAddressActivity.class);
		    intent2.putExtra("manageType", 1);
			startActivityForResult(intent2, 2000);
			break;
		case R.id.iv_left:
			finish();
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 2000 && resultCode == 200) {
		    if(requestCode == 2000&&data.getIntExtra("opr", 0)==1){ 
			AddressDetail parcelableExtra = data.getParcelableExtra("address");
			textAdress2.setText(parcelableExtra.getName()+"   "+parcelableExtra.getPhonenumber());
			textAdress3.setText(parcelableExtra.getAreadetail());
			Logger.d(TAG, "已选择地址" + parcelableExtra.toString());
		    }
		}else  if (requestCode == REQUEST_CODE_PAYMENT) {
            if(resultCode == Activity.RESULT_OK){
            String result = data.getExtras().getString("pay_result");
            /* 处理返回值
             * "success" - payment succeed
             * "fail"    - payment failed
             * "cancel"  - user canceld
             * "invalid" - payment plugin not installed
             */
            String errorMsg = data.getExtras().getString("error_msg"); // 错误信息
            String extraMsg = data.getExtras().getString("extra_msg"); // 错误信息
            //showMsg(result, errorMsg, extraMsg);
            if("success".equals(result)){
               
                HashMap<String, String> requestDataMap = new HashMap<String, String>();
                String inmapData="{\"ServiceName\":\"payOrderManagerService\" , \"Data\":{\"ACTION\":\"MIMMODIFYPAYSTATEORDER\",\"userId\":\""+SysU.USERID+"\",\"orderNo\":\""+orderNo+"\"}}";
                requestDataMap.put("JsonData", inmapData);
                RequestVo vo = new RequestVo(R.string.sysRequestServLet, this, requestDataMap,
                        new SuccessParser());
                getDataFromServer(vo, new DataCallback<Boolean>() {
                    @Override
                    public void processData(Boolean paramObject, boolean paramBoolean) {
                        if(paramObject){
                            Intent intent = new Intent(RestaurantPaymentCenterActivity.this,RestaurantOrdrSubmitOkActivity.class);
                            intent.putExtra("orderNo", orderNo);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
               
            }else{
                showMsg(result, errorMsg, extraMsg);
            }
           }else{
               ordr_submit_bottom_text.setEnabled(true); 
           }
        
   
       
    }
			
	}

	@Override
	protected void findViewById() {
		//payment_product_list = (ListView) this.findViewById(R.id.payment_product_list);
		payment_product_list_mGallery = (LinearLayout) findViewById(R.id.aof_ll_products); 
		ordr_submit_bottom_text = (TextView) this.findViewById(R.id.ordr_submit_bottom_text);
		// 收件人信息
		textAdress2 = (TextView) this.findViewById(R.id.textAdress2);//收件人
		textAdress3 = (TextView) this.findViewById(R.id.textAdress3);//收件人地址
		//textAdress4 = (TextView) this.findViewById(R.id.textAdress4);//详细地址
		
		
		//支付方式
		//payment_payValue_text = (TextView) this.findViewById(R.id.aof_tv_payStyle_label);
		//送货时间
		payment_sendTimeValue_text = (TextView) this.findViewById(R.id.aof_tv_deliveryTime);
		//发票
		//payment_invoiceValue_text = (TextView) this.findViewById(R.id.payment_invoiceValue_text);
		//留言
		payment_remarkView_text = (TextView) this.findViewById(R.id.aof_et_message);
		//数量总计
		shopcar_total_buycount_text = (TextView) this.findViewById(R.id.aof_tv_productsNum);
		//赠送总积分
		//shopcar_total_bonus_text = (TextView) this.findViewById(R.id.shopcar_total_bonus_text);
		//金额总计
		shopcar_aof_tv_amount = (TextView) this.findViewById(R.id.aof_tv_amount);
		//应付金额
		shopcar_total_money_text = (TextView) this.findViewById(R.id.aof_tv_prosperity_balance);
		//付款按钮计算得到的金额
		shopcar_aof_tv_needToPay = (TextView) this.findViewById(R.id.aof_tv_needToPay);
		lll_order_sel_payment= (RadioGroup)this.findViewById(R.id.ll_order_sel_payment);
        lll_order_wangyin_payment= (RadioButton)this.findViewById(R.id.lll_order_wangyin_payment);
        lll_order_weixin_payment= (RadioButton)this.findViewById(R.id.lll_order_weixin_payment);
        
		head_title = (TextView) this.findViewById(R.id.mid_title);
		head_left=(ImageView) this.findViewById(R.id.iv_left);
		head_title.setText(getString(R.string.uphandcheckout));
		head_left.setOnClickListener(this);
		
		
		
		findViewById(R.id.select_address).setOnClickListener(this);
		this.lll_order_sel_payment.setOnCheckedChangeListener(new OnCheckedChangeListenerImp());
		
	}

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.act_pay_content_center);
		//setHeadRightVisibility(View.VISIBLE);
		orderNo=getIntent().getStringExtra("orderNo");
		//setHeadRightText(R.string.uphandcheckout);
		mInflater = LayoutInflater.from(this);
		syncImageLoader = new SyncImageLoader();
		setTitle(R.string.check_out);
	}

	@Override
	protected void processLogic() {
	    if(SysU.USERID==null||SysU.USERID.length()==0){
            Intent loginIntent = new Intent(this, RestaurantLoginActivity.class);
            loginIntent.putExtra("payCenter", 1);
            startActivity(loginIntent);
            
        }else{
		RequestVo vo = new RequestVo();
		vo.context = this;
		vo.requestDataMap = new HashMap<String, String>();
		vo.requestDataMap.put("sku", "1200001:3|1200004:2");
		
		String inmapData="{\"ServiceName\":\"srvOrderManagerService\" , \"Data\":{\"ACTION\":\"CHECKBASEORDER\",\"userId\":\""+SysU.USERID+"\",\"orderNo\":\""+orderNo+"\"}}";
		HashMap<String, String> prodMap = new HashMap<String, String>();
		prodMap.put("JsonData", inmapData);
		vo.requestDataMap = prodMap;
		vo.requestUrl = R.string.sysRequestServLet;
		vo.jsonParser = new PaymentCenterParser();
		getDataFromServer(vo, new DataCallback<Map<String,Object>>() {
			@Override
			public void processData(Map<String,Object> paramObject, boolean paramBoolean) {
				if(paramObject!=null){
					Address address_info = (Address) paramObject.get("addressInfo");
					Payment paymentInfo = (Payment) paramObject.get("paymentInfo");
					Delivery deliveryInfo = (Delivery) paramObject.get("deliveryInfo");
					InvoiceInfo invoiceInfo = (InvoiceInfo) paramObject.get("invoiceInfo");
				    productlistInfo = (List<CartProduct>) paramObject.get("productlistInfo");
					List<String> checkout = (List<String>)paramObject.get("checkout");
					CheckoutAddup checkoutAdd = (CheckoutAddup) paramObject.get("checkoutAdd");
					needPrice=checkoutAdd.getTotal_price();
					// 收件人信息
					textAdress2.setText(address_info.getName()+"   "+address_info.getPhonenumber());
					//textAdress2.setText(address_info.getName()==null?"":address_info.getName());
					textAdress3.setText(address_info.getAddress_detail()==null?"":address_info.getAddress_detail());
					/*textAdress3.setText(address_info.getAddress_area()+"");
					textAdress4.setText(address_info.getAddress_detail()+"");*/
					//支付方式
					
					/*if("3".equals(paymentInfo.getType())){
						payment_payValue_text.setText(getString(R.string.payLablePayTypealipay));
					}else{
						payment_payValue_text.setText(getString(R.string.payLablePayTypeCashOnDelivery));
					}*/
					//送货时间
					if(deliveryInfo.getType()==1){
						payment_sendTimeValue_text.setText(getString(R.string.orderDetailLablePfNeedAtWork));
					}else if(deliveryInfo.getType()==2){
						payment_sendTimeValue_text.setText(getString(R.string.orderDetailLablePfNeedAtWeekend));
					}else if(deliveryInfo.getType()==3){
						payment_sendTimeValue_text.setText(getString(R.string.orderDetailLablePfNeedAtWordAndWeekend));
					}
					//发票
					//payment_invoiceValue_text.setText(invoiceInfo.getTitle()+"");
					//留言
					payment_remarkView_text.setText(invoiceInfo.getContent()+"");
					//数量总计
					shopcar_total_buycount_text.setText(checkoutAdd.getTotal_count()+"");
					//总积分
					//shopcar_total_bonus_text.setText(checkoutAdd.getTotal_point()+"");
					//金额总计
					shopcar_aof_tv_amount.setText(checkoutAdd.getTotal_price()+"");
					shopcar_aof_tv_needToPay.setText(checkoutAdd.getTotal_price()+"");
					shopcar_total_money_text.setText(checkoutAdd.getTotal_price()+"");
					for (int i = 0; i < productlistInfo.size(); i++)  
			        {  
			              
			            View view = mInflater.inflate(R.layout.act_pay_content_center_pay_img_item,payment_product_list_mGallery, false);  
			            String iImageUrl=productlistInfo.get(i).getPic();
			            if(iImageUrl!=null&&!iImageUrl.startsWith("http")){
			    			iImageUrl=getString(R.string.sysRequestHost)+iImageUrl;
			    		}
			            syncImageLoader.loadImage(i, iImageUrl, imageLoadListener);
			            // img.setImageResource(productlistInfo.get(i).getPic());  
			            TextView txt = (TextView) view.findViewById(R.id.item_iv_image_text);  
			            txt.setText(productlistInfo.get(i).getName());  
			            payment_product_list_mGallery.addView(view);  
			        }
					
					
				}
			}
		});
        }

	}

	SyncImageLoader.OnImageLoadListener imageLoadListener = new SyncImageLoader.OnImageLoadListener() {

		@Override
		public void onImageLoad(Integer position, Drawable drawable) {
			View view=payment_product_list_mGallery.getChildAt(position);
			ImageView img = (ImageView) view.findViewById(R.id.item_iv_image);  
			img.setImageDrawable(drawable);
		}

		@Override
		public void onError(Integer t) {
			//onImageLoadError(t);
		}

	};

	@Override
	protected void setListener() {
		ordr_submit_bottom_text.setOnClickListener(this);
	}
	
	
	
	class PaymentTask extends AsyncTask<PaymentRequest, Void, String> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(PaymentRequest... pr) {

            PaymentRequest paymentRequest = pr[0];
            String data = null;
            String json = new Gson().toJson(paymentRequest);
                json="{\"ServiceName\":\"payOrderManagerService\" , \"Data\":{\"ACTION\":\"MIMPAYORDERLIST\",\"orderNo\":\""+orderNo+"\",\"userId\":\""+SysU.USERID+"\",\"prodId\":"+paymentRequest.prodId+",\"channel\":\""+paymentRequest.channel+"\",\"amount\":\""+paymentRequest.amount+"\"}}";
            try {
                //向Your Ping++ Server SDK请求数据
                String paramString = postJson(getString(R.string.sysRequestHost)+getString(R.string.sysRequestServLet), json);
                if(checkResponse(paramString)!=null){
                 JSONObject jsonObject = new JSONObject(paramString.replace("\\/", "[[\\/]]").replace("\\\"", "[[\\\"]]"));
                 //String newData=checkNewCharge(jsonObject.getJSONObject("DATA_INFO"));
                 data = jsonObject.getString("DATA_INFO");
                 //orderNo= jsonObject.getString("ORDERNO");
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
	
    public void showMsg(String title, String msg1, String msg2) {
        String str = title;
        if (null !=msg1 && msg1.length() != 0) {
            str += "\n" + msg1;
        }
        if (null !=msg2 && msg2.length() != 0) {
            str += "\n" + msg2;
        }
        AlertDialog.Builder builder = new Builder(RestaurantPaymentCenterActivity.this);
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

    
    private class OnCheckedChangeListenerImp implements OnCheckedChangeListener{

        public void onCheckedChanged(RadioGroup group, int checkedId) {
            String temp = null;
            if (RestaurantPaymentCenterActivity.this.lll_order_wangyin_payment.getId() == checkedId) {
                payType=2;
            }
            else if (RestaurantPaymentCenterActivity.this.lll_order_weixin_payment.getId() == checkedId) {
                payType=1;
            }else{
                
            }
          
        }
    }
   
    
}
