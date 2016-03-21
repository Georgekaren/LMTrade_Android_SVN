package com.lianmeng.core.order.activity;

import java.util.HashMap;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.lianmeng.core.account.activity.RestaurantAccountActivity;
import com.lianmeng.core.activity.R;
import com.lianmeng.core.framework.sysvo.RequestVo;
import com.lianmeng.core.framework.util.SysU;
import com.lianmeng.core.order.parser.OrderForSubmitParser;
import com.lianmeng.core.order.vo.OrderForSubmit;
import com.lianmeng.extand.lianmeng.product.activity.RestaurantBaseActivity;

public class RestaurantOrdrSubmitOkActivity extends RestaurantBaseActivity {
	private TextView orderid_value_text;
	private TextView paymoney_value_text;
	private TextView paytype_value_text;
	private TextView continue_shoping_text;
	private TextView to_ordr_detail_text;
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.continue_shoping_text:
			finish();
			break;
		case R.id.to_ordr_detail_text:
			finish();
			Intent intent = new Intent(this,RestaurantAccountActivity.class);
			startActivity(intent);
			break;
		}

	}

	@Override
	protected void findViewById() {
		orderid_value_text = (TextView) this.findViewById(R.id.orderid_value_text);
		paymoney_value_text = (TextView) this.findViewById(R.id.paymoney_value_text);
		paytype_value_text = (TextView) this.findViewById(R.id.paytype_value_text);
		continue_shoping_text = (TextView) this.findViewById(R.id.continue_shoping_text);
		to_ordr_detail_text = (TextView) this.findViewById(R.id.to_ordr_detail_text);
		continue_shoping_text.setOnClickListener(this);
		to_ordr_detail_text.setOnClickListener(this);
		String orderNo=getIntent().getStringExtra("orderNo");
        orderid_value_text.setText(orderNo);
	}

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.act_order_submit_ok);
		setTitle(R.string.uphandsuccess);
		//setHeadLeftVisibility(View.INVISIBLE);
		
	}

	@Override
	protected void processLogic() {
	    
		RequestVo vo = new RequestVo();
		vo.context = this;
		//vo.requestUrl = R.string.ordersumbit;
		vo.jsonParser = new OrderForSubmitParser();
		String inmapData="{\"ServiceName\":\"srvOrderManagerService\" , \"Data\":{\"ACTION\":\"PAYENDDISPLAYORDER\",\"userId\":\""+SysU.USERID+"\"}}";
		HashMap<String, String> prodMap = new HashMap<String, String>();
		prodMap.put("JsonData", inmapData);
		vo.requestDataMap = prodMap;
		vo.requestUrl = R.string.sysRequestServLet;
		getDataFromServer(vo, new DataCallback<OrderForSubmit>() {
			@Override
			public void processData(OrderForSubmit paramObject,
					boolean paramBoolean) {
				//orderid_value_text.setText(paramObject.orderid);
				paymoney_value_text.setText(paramObject.price);
//				支付方式
				String type = paramObject.getPaymenttype().trim();
				if("3".equals(type)){
					paytype_value_text.setText(getString(R.string.payLablePayTypealipay));
				}else{
					paytype_value_text.setText(getString(R.string.payLablePayTypeCashOnDelivery));
				}
			}
		});
	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub

	}

}
