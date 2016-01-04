package com.lianmeng.extand.lianmeng.product.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.lianmeng.core.activity.R;
import com.lianmeng.core.activity.R.id;
import com.lianmeng.core.activity.R.layout;
import com.lianmeng.core.activity.R.string;
import com.lianmeng.core.framework.sysactivity.BaseWapperActivity;
import com.lianmeng.core.framework.sysvo.RequestVo;
import com.lianmeng.extand.lianmeng.product.adapter.BrandAdapter;
import com.lianmeng.extand.lianmeng.product.parser.BrandParser;
import com.lianmeng.extand.lianmeng.product.vo.BrandCategory;

import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

public class BrandActivity extends BaseWapperActivity {
	private List<BrandCategory> list;
	private TextView textBrandInfoNull;
	private TextView textTitle;
	private ExpandableListView expandableLV;
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void findViewById() {
		expandableLV =  (ExpandableListView) findViewById(R.id.listBrandInfo);
		//listView = (ListView) findViewById(R.id.productList);
		System.out.println(expandableLV==null);
	}
	
	//加载布局文件
	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.brand_activity);
		//setContentView(R.layout.product_list_activity);
		list = new ArrayList<BrandCategory>();
		setTitle(getString(R.string.brandProdTitleTitleNameMsg));
	}
	//执行逻辑
	@Override
	protected void processLogic() {
		RequestVo reqVo = new RequestVo();
		reqVo.requestUrl = R.string.sysRequestServLet;
		reqVo.context = context;
		HashMap<String, String> requestDataMap = new HashMap<String, String>();
		//4 代表文具
		String inmapData="{\"ServiceName\":\"extProdManagerService\" , \"Data\":{\"ACTION\":\"QRYPRODTYPEVALUELIST\"}}";
		requestDataMap.put("JsonData", inmapData);
		reqVo.requestDataMap = requestDataMap;
		
		reqVo.jsonParser = new BrandParser();
		
		super.getDataFromServer(reqVo, new DataCallback<List<BrandCategory>>() {

			@Override
			public void processData(List<BrandCategory> paramObject,
					boolean paramBoolean) {
				list = paramObject;
				BrandAdapter brandAdapter = new BrandAdapter(list, expandableLV, BrandActivity.this);
				expandableLV.setAdapter(brandAdapter);
			}

			
		});
	}
	//设置监听事件
	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		
	}
}
