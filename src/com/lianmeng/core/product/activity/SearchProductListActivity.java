package com.lianmeng.core.product.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.lianmeng.core.activity.R;
import com.lianmeng.core.activity.R.id;
import com.lianmeng.core.activity.R.layout;
import com.lianmeng.core.activity.R.string;
import com.lianmeng.core.framework.sysactivity.BaseWapperActivity;
import com.lianmeng.core.framework.sysactivity.BaseWapperActivity.DataCallback;
import com.lianmeng.core.framework.sysvo.RequestVo;
import com.lianmeng.core.framework.util.Constant;
import com.lianmeng.core.product.adapter.ProductAdapter;
import com.lianmeng.core.product.parser.SearchParser;
import com.lianmeng.core.product.vo.ProductListVo;

public class SearchProductListActivity extends BaseWapperActivity implements OnItemClickListener {
	private TextView textRankSale;
	private TextView textRankPrice;
	private TextView textRankGood;
	private TextView textRankTime;
	private ListView productList;
	private RequestVo vo;
	private HashMap<String, String> map;
	private String priceOrder = "price_up";
	private DataCallback<List<ProductListVo>> callback;
	private ProductAdapter adapter;

	public SearchProductListActivity() {
		DataCallback<List<ProductListVo>> callback = new DataCallback<List<ProductListVo>>() {


			@Override
			public void processData(List<ProductListVo> paramObject, boolean paramBoolean) {
				adapter = new ProductAdapter(context, productList, paramObject);
				productList.setAdapter(adapter);
				setTitle(getString(R.string.searchProdTitleLeftNameMsg)+ paramObject.size() + getString(R.string.searchProdTitleRightNameMsg));
			}
		};

		this.callback = callback;

	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.backTv:
			Intent intent = new Intent(SearchProductListActivity.this, SearchActivity.class);
			finish();
			startActivity(intent);

			break;
		case R.id.textRankSale:
			checkInMapData("sale_down","");
			//map.put("orderby", "sale_down");
			//vo.requestDataMap = map;
			super.getDataFromServer(vo, callback);
			showProgressDialog();
			break;
		case R.id.textRankPrice:
			if (priceOrder == "price_up") {
				checkInMapData(priceOrder,"");
				//map.put("orderby", priceOrder);
				//vo.requestDataMap = map;
				priceOrder = "price_down";
				super.getDataFromServer(vo, callback);
				showProgressDialog();

			} else {
				checkInMapData(priceOrder,"");
				//map.put("orderby", priceOrder);
				//vo.requestDataMap = map;
				priceOrder = "price_up";
				super.getDataFromServer(vo, callback);
				showProgressDialog();

			}
			break;
		case R.id.textRankGood:
			checkInMapData("comment_down","");
			//map.put("orderby", "comment_down");
			//vo.requestDataMap = map;
			super.getDataFromServer(vo, callback);
			showProgressDialog();

			break;
		case R.id.textRankTime:
			//map.put("orderby", "shelves_down");
			checkInMapData("shelves_down","");
			//vo.requestDataMap = map;
			super.getDataFromServer(vo, callback);
			showProgressDialog();

			break;
		}
	}

	@Override
	protected void findViewById() {
		textRankSale = (TextView) findViewById(R.id.textRankSale);
		textRankPrice = (TextView) findViewById(R.id.textRankPrice);
		textRankGood = (TextView) findViewById(R.id.textRankGood);
		textRankTime = (TextView) findViewById(R.id.textRankTime);
		productList = (ListView) findViewById(R.id.productList);

	}

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.search_product_list);
		selectedBottomTab(Constant.SEARCH);
	}

	@Override
	protected void processLogic() {
		
		vo = new RequestVo();
		vo.context = SearchProductListActivity.this;
		String orderby="sale_down";
		String brandid="";
		checkInMapData(orderby,brandid);
		vo.requestUrl = R.string.sysRequestServLet;
		
		vo.jsonParser = new SearchParser();
		super.getDataFromServer(vo, callback);

	}

	public void checkInMapData(String orderby,String brandid){
		String keyWord = getIntent().getStringExtra("keyWord");
		map = new HashMap<String, String>();
		
		map.put("keyword", keyWord);
		map.put("page", 1 + "");
		map.put("pageNum", 10 + "");
		map.put("orderby", orderby);
		/*try {
			keyWord=URLEncoder.encode(keyWord, "UTF-8");
		} catch (UnsupportedEncodingException e) {

		}*/
		String inmapData="{\"ServiceName\":\"extProdManagerService\" , \"Data\":{\"ACTION\":\"QRYBASECONTENTPROD\",\"name\":\""+keyWord+"\",\"type\":\"\"";
		if(orderby!=null){
			inmapData=inmapData+",\"ordertype\":\""+orderby+"\"";
			//,\"brandid\":\"1002001\"
		}
		if(brandid!=null){
			map.put("brandid", brandid);
			inmapData=inmapData+",\"brandid\":\""+brandid+"\"";
		}
		inmapData=inmapData+"}}";
		map.put("JsonData", inmapData);
		vo.requestDataMap = map;
	}
	@Override
	protected void setListener() {
		textRankSale.setOnClickListener(this);
		textRankPrice.setOnClickListener(this);
		textRankGood.setOnClickListener(this);
		textRankTime.setOnClickListener(this);
		productList.setOnItemClickListener(this);

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
 		Intent intent = new Intent(this, ProductDetailActivity.class);
		intent.putExtra("id", adapter.getItem(position).getId());
		startActivity(intent);
	}

}
