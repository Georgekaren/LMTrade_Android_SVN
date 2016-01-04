package com.lianmeng.extand.lianmeng.product.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.lianmeng.core.activity.R;
import com.lianmeng.core.activity.R.id;
import com.lianmeng.core.activity.R.layout;
import com.lianmeng.core.activity.R.string;
import com.lianmeng.core.framework.sysactivity.BaseWapperActivity;
import com.lianmeng.core.framework.sysvo.RequestVo;
import com.lianmeng.core.product.activity.ProductDetailActivity;
import com.lianmeng.extand.lianmeng.product.adapter.LimitbuyAdapter;
import com.lianmeng.extand.lianmeng.product.parser.LimitbuyParser;
import com.lianmeng.extand.lianmeng.product.vo.LimitBuyVo;
/**
 * 限时抢购
 * @author csl
 *
 */
public class LimitbuyActivity extends BaseWapperActivity {
	private List<LimitBuyVo> limitbuyList;
	private ListView listView;
	LimitbuyAdapter limitbuyAdapter;
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void findViewById() {
		listView = (ListView) findViewById(R.id.promBulldtinLv);
	}

	
	//加载布局文件
	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.prom_bulletin_activity);
		limitbuyList = new ArrayList<LimitBuyVo>();
		setTitle(getString(R.string.limitTitleTitleNameMsg));
		
		
	}
	//执行逻辑
	@Override
	protected void processLogic() {
		RequestVo reqVo = new RequestVo();
		reqVo.requestUrl = R.string.sysRequestServLet;//R.string.url_limitbuy
		reqVo.context = context;
		HashMap<String, String> requestDataMap = new HashMap<String, String>();
		String inmapData="{\"ServiceName\":\"extProdManagerService\" , \"Data\":{\"ACTION\":\"QRYLIMITPROD\"}}";
		requestDataMap.put("JsonData", inmapData);
		requestDataMap.put("page", "");
		requestDataMap.put("pageNum", "");
		reqVo.requestDataMap = requestDataMap;
		
		reqVo.jsonParser = new LimitbuyParser();
		
		super.getDataFromServer(reqVo, new DataCallback<List<LimitBuyVo>>() {

			@Override
			public void processData(List<LimitBuyVo> paramObject,
					boolean paramBoolean) {
				limitbuyList = paramObject;
				limitbuyAdapter = new LimitbuyAdapter(limitbuyList, listView, LimitbuyActivity.this);
				listView.setAdapter(limitbuyAdapter);
				limitbuyAdapter.start();
			}

			
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if (limitbuyAdapter != null)
			limitbuyAdapter.start();
	}
	
	@Override
	protected void onPause() {
 		super.onPause();
 		if (limitbuyAdapter != null)
			limitbuyAdapter.stop();
	}
	 
	//设置监听事件
	@Override
	protected void setListener() {
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				LimitBuyVo vo = (LimitBuyVo) listView.getItemAtPosition(position);
				Intent producutlistIntent = new Intent(LimitbuyActivity.this,ProductDetailActivity.class);
				//将ID传递到商品分类显示中，显示相关内容
				producutlistIntent.putExtra("id", vo.getId());
				//跳转到新的activity
				startActivity(producutlistIntent);
			}
		});
		
	}

	
}


