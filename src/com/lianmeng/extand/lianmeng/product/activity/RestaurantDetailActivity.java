package com.lianmeng.extand.lianmeng.product.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lianmeng.core.activity.R;
import com.lianmeng.core.framework.sysparser.ParamSuccessParser;
import com.lianmeng.core.framework.sysparser.StringVParser;
import com.lianmeng.core.framework.sysparser.SuccessParser;
import com.lianmeng.core.framework.sysvo.RequestVo;
import com.lianmeng.core.framework.util.InjectView;
import com.lianmeng.core.framework.util.Injector;
import com.lianmeng.core.framework.util.SysU;
import com.lianmeng.core.login.activity.RestaurantLoginActivity;
import com.lianmeng.core.pay.activity.RestaurantPaymentCenterActivity;
import com.lianmeng.extand.lianmeng.product.adapter.RestaurantDetailAdapter;
import com.lianmeng.extand.lianmeng.product.parser.RestaurantProductParser;
import com.lianmeng.extand.lianmeng.product.vo.RestaurantProductBuyVo;
import com.lianmeng.extand.lianmeng.product.widget.stickylistheaders.StickyListHeadersListView;
 

public class RestaurantDetailActivity extends RestaurantBaseActivity implements
		AdapterView.OnItemClickListener,
		StickyListHeadersListView.OnHeaderClickListener,
		StickyListHeadersListView.OnStickyHeaderOffsetChangedListener,
		StickyListHeadersListView.OnStickyHeaderChangedListener {

	private RestaurantDetailAdapter mAdapter;
	private List<RestaurantProductBuyVo> prodectBuyList;
	
	private boolean fadeHeader = true;
	@InjectView(R.id.linear_above_toHome)
	private LinearLayout above_toHome;
	@InjectView(R.id.tv_common_above_head)
	private TextView above_tittle;
	@InjectView(R.id.iv_head_left)
	private ImageView head_left;
	//@InjectView(R.id.tv_common_above_head)
	//private TextView head_tittle;
	@InjectView(R.id.food_list_shipping_fee)
	private TextView order_cart;
	@InjectView(R.id.food_list_shipping_num)
	private TextView order_cart_num;
	@InjectView(R.id.food_list_shipping_price)
	private TextView order_cart_price;
	
	@InjectView(R.id.goto_cart)
	private FrameLayout goto_cart;
	
	private String restaurant_name;

	private StickyListHeadersListView stickyList;

	

	private void initView() {
		 
		//above_tittle = (TextView)findViewById(R.id.tv_common_above_head);
		above_tittle.setText(restaurant_name);
		head_left.setImageResource(R.drawable.abc_ic_ab_back_holo_dark);
		
	}
	
	private void toLogin(){
	    Intent loginIntent = new Intent(this, RestaurantLoginActivity.class);
	    loginIntent.putExtra("type", 1);
        startActivity(loginIntent);
	}
	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		above_toHome.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
				
			}
		});
		goto_cart.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
			    if(SysU.USERID==null||SysU.USERID.length()==0){
			        toLogin();
			    }else{
				HashMap<String, String> requestDataMa = new HashMap<String, String>();
				//requestDataMa.put("id", currentProduct.getId() + "");
				//requestDataMa.put("count", prodNumValue.getText().toString());
				String totalPrice=order_cart_price.getText().toString();
				String totalNum=order_cart_num.getText().toString();
				String currentProduct="";
				
				ArrayList<RestaurantProductBuyVo> arsel=mAdapter.getSelItems();
				for(int i=0;i<arsel.size();i++){
					if (i == 0) {
						currentProduct ="\""+arsel.get(i).getId()+"\"";
					} else {
						currentProduct = currentProduct + ",\""
								+ arsel.get(i).getId()+"\"";
					}
				}
				if(arsel.size()==0){
				    return;
				}
				String inMap="{\"ServiceName\":\"srvOrderManagerService\" , \"Data\":{\"ACTION\":\"ADDORDER\",\"prodId\":["+currentProduct+"],\"userId\":\""+SysU.USERID+"\",\"prodNum\":\""+totalNum+"\",\"totalPrice\":\""+totalPrice+"\"}}";
				requestDataMa.put("JsonData", inMap);
				RequestVo reqVo = new RequestVo(R.string.sysRequestServLet, context, requestDataMa , new ParamSuccessParser());
				getDataFromServer(reqVo , new DataCallback<String>() {

					@Override
					public void processData(String paramObject, boolean paramBoolean) {
 						if (paramObject != null && !paramObject.equals("false")) {
 						    String orderNo=paramObject;
 							Intent intent = new Intent(RestaurantDetailActivity.this, RestaurantPaymentCenterActivity.class);
 							intent.putExtra("orderNo", orderNo);
 							startActivity(intent);
							finish();
								/*
 							AlertDialog.Builder builder = new Builder(RestaurantDetailActivity.this);
 							builder.setTitle(getString(R.string.prodDetailMsgOrderDetailFavoriteSucceedNameMsg));
 							builder.setPositiveButton(getString(R.string.prodDetailTitleContinueShopingMsg), new DialogInterface.OnClickListener() {
 								@Override
 								public void onClick(DialogInterface dialog, int which) {
 									finish();
 								}
 							});
 							builder.setNegativeButton(getString(R.string.prodDetailTitleInShopingCartMsg), new DialogInterface.OnClickListener() {
 								@Override
 								public void onClick(DialogInterface dialog, int which) {
 									Intent intent = new Intent(RestaurantDetailActivity.this, ShoppingCarActivity.class);
 									startActivity(intent);
 									finish();
 								}
 							});

 							builder.create().show();*/
 						} else {
 							Toast.makeText(RestaurantDetailActivity.this, getString(R.string.prodDetailMsgOrderDetailFavoriteFaildNameMsg), Toast.LENGTH_LONG).show();
 						}
					}
				});
			    }
				
			}
		});
		
		//mAdapter = new RestaurantDetailAdapter(this,order_cart,order_cart_num,order_cart_price);

		stickyList = (StickyListHeadersListView) findViewById(R.id.list_restaurant_detail);
		stickyList.setOnItemClickListener(this);
		stickyList.setOnHeaderClickListener(this);
		stickyList.setOnStickyHeaderChangedListener(this);
		stickyList.setOnStickyHeaderOffsetChangedListener(this);
		stickyList.addHeaderView(getLayoutInflater().inflate(R.layout.act_product_view_list_header_restaurant, null));
		//stickyList.addFooterView(getLayoutInflater().inflate(
		//		R.layout.restaurant_list_footer, null));
		stickyList.setDrawingListUnderStickyHeader(true);
		stickyList.setAreHeadersSticky(true);
		
//		stickyList.setStickyHeaderTopOffset(-20);
		
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		view.setAlpha(10);
		Toast.makeText(this, "Item " + position + " clicked!",
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onHeaderClick(StickyListHeadersListView l, View header,
			int itemPosition, long headerId, boolean currentlySticky) {
		Toast.makeText(this,
				"Header " + headerId + " currentlySticky ? " + currentlySticky,
				Toast.LENGTH_SHORT).show();
	}

	@Override
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void onStickyHeaderOffsetChanged(StickyListHeadersListView l,
			View header, int offset) {
		if (fadeHeader&& Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			header.setAlpha(1 - (offset / (float) header.getMeasuredHeight()));
		}
	}

	@Override
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void onStickyHeaderChanged(StickyListHeadersListView l, View header,
			int itemPosition, long headerId) {
		header.setAlpha(1);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void loadViewLayout() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.act_product_main_restaurant);
		Injector.get(this).inject();//init views
		Intent intent = getIntent();
		restaurant_name = intent.getStringExtra("name");
		initView();
		//setListener();
	}

	//执行逻辑
		@Override
		protected void processLogic() {
			RequestVo reqVo = new RequestVo();
			reqVo.requestUrl = R.string.sysRequestServLet;
			reqVo.context = context;
			String intype=getIntent().getStringExtra("type");
			HashMap<String, String> requestDataMap = new HashMap<String, String>();
			String inmapData="{\"ServiceName\":\"extProdManagerService\" , \"Data\":{\"ACTION\":\"QRYBASECONTENTPROD\",\"type\":\""+intype+"\"}}";
			requestDataMap.put("JsonData", inmapData);
			requestDataMap.put("page", "");
			requestDataMap.put("pageNum", "");
			reqVo.requestDataMap = requestDataMap;
			
			reqVo.jsonParser = new RestaurantProductParser();
			
			super.getDataFromServer(reqVo, new DataCallback<List<RestaurantProductBuyVo>>() {

				@Override
				public void processData(List<RestaurantProductBuyVo> paramObject,
						boolean paramBoolean) {
				    prodectBuyList = paramObject;
					mAdapter = new RestaurantDetailAdapter(prodectBuyList, stickyList, RestaurantDetailActivity.this,order_cart,order_cart_num,order_cart_price);
					stickyList.setAdapter(mAdapter);

				}

				
			});
		}
		

	

}