package com.lianmeng.core.scar.activity;

import java.util.HashMap;

import com.lianmeng.core.activity.R;
import com.lianmeng.core.activity.R.id;
import com.lianmeng.core.activity.R.layout;
import com.lianmeng.core.activity.R.string;
import com.lianmeng.core.address.adapter.AddressManageAdapter;
import com.lianmeng.core.address.adapter.AddressManageAdapter.OnItemButtonListener;
import com.lianmeng.core.address.vo.AddressDetail;
import com.lianmeng.core.framework.sysactivity.BaseWapperNewActivity;
import com.lianmeng.core.framework.sysactivity.BaseWapperNewActivity.DataCallback;
import com.lianmeng.core.framework.sysparser.SuccessParser;
import com.lianmeng.core.framework.sysvo.RequestVo;
import com.lianmeng.core.framework.util.Constant;
import com.lianmeng.core.framework.util.Logger;
import com.lianmeng.core.framework.util.NetUtil;
import com.lianmeng.core.framework.util.SysU;
import com.lianmeng.core.order.vo.Addup;
import com.lianmeng.core.pay.activity.RestaurantPaymentCenterActivity;
import com.lianmeng.core.scar.adapter.ShoppingCarAdapter;
import com.lianmeng.core.scar.parser.ShoppingCarParser;
import com.lianmeng.core.scar.vo.Cart;
import com.lianmeng.core.scar.vo.CartProduct;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;

public class ShoppingCarActivity extends BaseWapperNewActivity  implements OnItemButtonListener, OnItemLongClickListener ,View.OnClickListener{
	protected static final String TAG = "ShoppingCarActivity";

	private ListView shopcar_product_list;
	private ShoppingCarAdapter mAdapter;
	
	private TextView shopcar_total_buycount_text_1; //数量总计
	private TextView shopcar_total_bonus_text_1; //赠送积分总计
	private TextView shopcar_total_money_text_1; //金额总计(不含运费)
	private String productId;
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.shopcar_bottom_toPay_text:
			startActivity(new Intent(this, RestaurantPaymentCenterActivity.class));
			finish();
			break;
		}
	}

	@Override
	protected void findViewById() {
		shopcar_product_list = (ListView) findViewById(R.id.shopcar_product_list);
		shopcar_total_buycount_text_1= (TextView) findViewById(R.id.shopcar_total_buycount_text_1);
		shopcar_total_bonus_text_1= (TextView) findViewById(R.id.shopcar_total_bonus_text_1);
		shopcar_total_money_text_1= (TextView) findViewById(R.id.shopcar_total_money_text_1);
		findViewById(R.id.shopcar_bottom_toPay_text).setOnClickListener(this);
		
	}

	protected void setLi(){
		mAdapter.setListener(this);
	}
	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.old_act_shopping_car);
		setTitle(getString(R.string.shoppingCartTitleTitleNameMsg));
		//selectedBottomTab(Constant.SHOPCAR);
	}

	@Override
	protected void processLogic() {
		//productId=getIntent().getExtras().toString();
		
		RequestVo requestVo = new RequestVo();
		requestVo.context=context;
		requestVo.jsonParser = new ShoppingCarParser();
		requestVo.requestUrl = R.string.sysRequestServLet;
		String inmapData="{\"ServiceName\":\"srvOrderManagerService\" , \"Data\":{\"ACTION\":\"QRYBASEORDER\",\"userId\":\"1\"}}";
		HashMap<String, String> prodMap = new HashMap<String, String>();
		prodMap.put("JsonData", inmapData);
		requestVo.requestDataMap = prodMap;
		
		getDataFromServer(requestVo, new DataCallback<Cart>() {

			@Override
			public void processData(Cart paramObject, boolean paramBoolean) {
				
				Logger.d(TAG, paramObject.productlist.size()+"");
			    mAdapter = new ShoppingCarAdapter(ShoppingCarActivity.this, paramObject);
				shopcar_product_list.setAdapter(mAdapter);	
				setLi();
				if (paramObject.productlist.size() > 0) {
					Addup addup = paramObject.cart_addup ;
					shopcar_total_buycount_text_1.setText(addup.total_count + "");
					shopcar_total_bonus_text_1.setText(addup.total_point + "");
					shopcar_total_money_text_1.setText(addup.total_price + "");
				} else {
					setContentView(R.layout.old_act_shopping_none_car);
				}
				
			}
		});
	}

	@Override
	protected void setListener() {
		//mAdapter.setListener(this);
	}
	
	
	
	
	@Override
	public void onItemClick(View view, final int position) {
		switch (view.getId()) {
		
		case R.id.shopcar_item_delete_text:// 删除
			final CartProduct item = (CartProduct) mAdapter.getItem(position);
			String prodId=item.getId();
			String userId=SysU.USERID;
			HashMap<String, String> requestDataMap = new HashMap<String, String>();
			String inmapData="{\"ServiceName\":\"srvOrderManagerService\" , \"Data\":{\"ACTION\":\"REMOVEORDER\",\"prodId\":\""+prodId+"\",\"userId\":\""+userId+"\"}}";
			requestDataMap.put("JsonData", inmapData);
			requestDataMap.put("id", prodId);
			RequestVo vo = new RequestVo(R.string.sysRequestServLet, context, requestDataMap,
					new SuccessParser());
			Boolean bool = (Boolean) NetUtil.post(vo);
			if (bool != null) {
				if (bool) {
					mAdapter.removeItem(position);
					Toast.makeText(ShoppingCarActivity.this, R.string.delete_success, Toast.LENGTH_LONG).show();
				}
			}
			
			break;
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		// TODO Auto-generated method stub
		return false;
	}

}
