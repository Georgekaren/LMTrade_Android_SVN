package com.lianmeng.core.product.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lianmeng.core.activity.R;
import com.lianmeng.core.framework.sysactivity.BaseWapperNewActivity;
import com.lianmeng.core.framework.sysactivity.BaseWapperNewActivity.DataCallback;
import com.lianmeng.core.framework.sysvo.RequestVo;

import com.lianmeng.core.framework.util.Logger;
import com.lianmeng.core.product.adapter.ProductLvAdapter;
import com.lianmeng.core.product.parser.ProductListParser;
import com.lianmeng.core.product.vo.ProductListVo;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ProductListActivity extends BaseWapperNewActivity {

	private static final String TAG = "ProductListActivity";

	
	private TextView tv_prodlist_ranksale;
	private TextView tv_prodlist_rankprice;
	private TextView tv_prodlist_rankgood;
	private TextView tv_prodlist_ranktime;
	private ListView lv_prodlist_listprod;
	private ImageView iv_prodlist_noresult;
	private TextView tv_prodlist_noresult;

	private List<ProductListVo> productInfos;
	private SharedPreferences sp;
	boolean rankUp;
	private String cId ;
	private String siftsize;
	private String filterData;
	private String orderby;
	
	////////////////////
	private Map<String, Object> mapAllResult;
	/////////////////
	/*@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.backTv:
			
		case R.id.textRankSale:
			orderby = "sale_down";
			
			*//**
			 * tv_prodlist_ranksale;
	 			tv_prodlist_rankprice;
	 			tv_prodlist_rankgood;
				tv_prodlist_ranktime;
				lv_prodlist_listprod;
			 *//*
			tv_prodlist_rankprice.setBackgroundResource(R.drawable.segment_normal_2_bg);
			tv_prodlist_ranksale.setBackgroundResource(R.drawable.segment_selected_1_bg);
			tv_prodlist_ranktime.setBackgroundResource(R.drawable.segment_normal_2_bg);
			tv_prodlist_rankgood.setBackgroundResource(R.drawable.segment_normal_2_bg);
			
			getRankSaleData(orderby,filterData);
			break;
		case R.id.textRankPrice:
			//可以保持用户以前设置的排序方式，提高用户体验
			
			*//**
			 * tv_prodlist_ranksale;
	 			tv_prodlist_rankprice;
	 			tv_prodlist_rankgood;
				tv_prodlist_ranktime;
				
			 *//*
			if(rankUp){
				orderby = "price_up";
				Editor editor = sp.edit();
				editor.putBoolean("productrank", true);
				editor.commit();
			}else{
				orderby ="price_down";
				Editor editor = sp.edit();
				editor.putBoolean("productrank", false);
				editor.commit();
			}
			tv_prodlist_rankprice.setBackgroundResource(R.drawable.segment_selected_1_bg);
			tv_prodlist_ranksale.setBackgroundResource(R.drawable.segment_normal_2_bg);
			tv_prodlist_ranktime.setBackgroundResource(R.drawable.segment_normal_2_bg);
			tv_prodlist_rankgood.setBackgroundResource(R.drawable.segment_normal_2_bg);
			
			getRankSaleData(orderby,filterData);
			break;
		case R.id.textRankGood:
			orderby ="comment_down";
			tv_prodlist_rankprice.setBackgroundResource(R.drawable.segment_normal_2_bg);
			tv_prodlist_ranksale.setBackgroundResource(R.drawable.segment_normal_2_bg);
			tv_prodlist_ranktime.setBackgroundResource(R.drawable.segment_normal_2_bg);
			tv_prodlist_rankgood.setBackgroundResource(R.drawable.segment_selected_1_bg);
			getRankSaleData(orderby,filterData);
			break;
		case R.id.textRankTime:
			orderby ="shelves_down";
			tv_prodlist_rankprice.setBackgroundResource(R.drawable.segment_normal_2_bg);
			tv_prodlist_ranksale.setBackgroundResource(R.drawable.segment_normal_2_bg);
			tv_prodlist_ranktime.setBackgroundResource(R.drawable.segment_selected_1_bg);
			tv_prodlist_rankgood.setBackgroundResource(R.drawable.segment_normal_2_bg);
			getRankSaleData(orderby,filterData);
			break;
		default:
			break;
		}
	}*/

	@Override
	protected void onHeadRightButton(View v) {
		super.onHeadRightButton(v);
		Intent siftIntent = new Intent(ProductListActivity.this,ProductFilterActivity.class);
		siftIntent.putExtra("cId", getIntent().getStringExtra("cId"));
		
		startActivityForResult(siftIntent, 121);
	}
	
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(data!=null){
			siftsize = data.getStringExtra("filterSize");
			filterData =data.getStringExtra("filter");
			getRankSaleData(orderby,filterData);
			
			System.out.println(filterData);
		}
		
	}





	private void getRankSaleData(String orderby,String sift) {
		
		RequestVo prodReqVo = new RequestVo();
		prodReqVo.requestUrl = R.string.sysRequestServLet;
		prodReqVo.context = context;
		HashMap<String, String> prodMap = new HashMap<String, String>();
		prodMap.put("page", "1");
		prodMap.put("pageNum", "8");
		cId = getIntent().getStringExtra("cId");
		Logger.i(TAG, cId);
		prodMap.put("cId", cId);
		String inmapData="{\"ServiceName\":\"extProdManagerService\" , \"Data\":{\"ACTION\":\"QRYBASECONTENTANDCOLORPROD\",\"type\":\""+cId+"\"";
		
		
		//{\"ServiceName\":\"extProdManagerService\" , \"Data\":{\"ACTION\":\"QRYBASECONTENTANDCOLORPROD\",\"type\":\"1\",\"ordertype\":\"sale_down\"}}
		if(sift!=null){
			prodMap.put("filter", filterData);
		}
		if(orderby!=null){
			prodMap.put("orderby", orderby);
			inmapData=inmapData+",\"ordertype\":\""+orderby+"\"";
		}
		inmapData=inmapData+"}}";
		prodMap.put("JsonData", inmapData);
		prodReqVo.requestDataMap = prodMap;
		
		prodReqVo.jsonParser = new ProductListParser();
		super.getDataFromServer(prodReqVo, new DataCallback<List<ProductListVo>>(){

			@Override
			public void processData(List<ProductListVo> paramObject,
					boolean paramBoolean) {
				productInfos = paramObject;
				if(productInfos==null){
					tv_prodlist_noresult.setVisibility(View.VISIBLE);
					iv_prodlist_noresult.setVisibility(View.VISIBLE);
				}
				ProductLvAdapter adapter = new ProductLvAdapter(context, lv_prodlist_listprod, productInfos);
				lv_prodlist_listprod.setAdapter(adapter);
			}
			
		});
		
		
		/*
		 
		prodReqVo.jsonParser = new ProductListParserNew();
		super.getDataFromServer(prodReqVo, new DataCallback<Map<String, Object>>(){

			@Override
			public void processData(Map<String, Object> paramObject,
					boolean paramBoolean) {
				mapAllResult = paramObject;
				productInfos = (List<ProductListVo>) mapAllResult.get("productlist");
				if(productInfos==null){
					tv_prodlist_noresult.setVisibility(View.VISIBLE);
					iv_prodlist_noresult.setVisibility(View.VISIBLE);
				}
				ProductLvAdapter adapter = new ProductLvAdapter(context, lv_prodlist_listprod, productInfos);
				lv_prodlist_listprod.setAdapter(adapter);
			}

		});
		*/
		/////////////////////////////////
		
		
	}

	@Override
	protected void findViewById() {
	
		tv_prodlist_ranksale = (TextView) findViewById(R.id.textRankSale);
		tv_prodlist_rankprice = (TextView) findViewById(R.id.textRankPrice);
		tv_prodlist_rankgood = (TextView) findViewById(R.id.textRankGood);
		tv_prodlist_ranktime = (TextView) findViewById(R.id.textRankTime);
		lv_prodlist_listprod = (ListView) findViewById(R.id.productList);
		iv_prodlist_noresult = (ImageView) findViewById(R.id.listProduct);
		tv_prodlist_noresult = (TextView) findViewById(R.id.textNull);
		
	
		setTitle(getString(R.string.prodListTitleTitleNameMsg));
		/*setHeadRightText(getString(R.string.prodListTitleFilterHeadNameMsg));
		setHeadRightVisibility(View.VISIBLE);
		tv_prodlist_ranksale.setOnClickListener(this);
		tv_prodlist_rankprice.setOnClickListener(this);
		tv_prodlist_rankgood.setOnClickListener(this);
		tv_prodlist_ranktime.setOnClickListener(this);*/
	}

	@Override
	protected void loadViewLayout() {
		/**
		 * private TextView tv_prodlist_back; private TextView
		 * tv_prodlist_title; private Button bt_prodlist_sift; private TextView
		 * tv_prodlist_ranksale; private TextView tv_prodlist_rankprice; private
		 * TextView tv_prodlist_rankgood; private TextView tv_prodlist_ranktime;
		 * private ListView tv_prodlist_listprod; private ImageView
		 * iv_prodlist_noresult; private TextView tv_prodlist_noresult;
		 */
		setContentView(R.layout.old_act_product_list_activity);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		
		rankUp = sp.getBoolean("productrank", false);
		if(rankUp == false){
			Editor editor = sp.edit();
			editor.putBoolean("productrank", false);
			editor.commit();
		}
		
	//selectedBottomTab(Constant.CLASSIFY);
	}

	@Override
	protected void processLogic() {
		RequestVo prodReqVo = new RequestVo();
		prodReqVo.requestUrl = R.string.sysRequestServLet;
		prodReqVo.context = context;
		HashMap<String, String> prodMap = new HashMap<String, String>();
		prodMap.put("page", "1");
		prodMap.put("pageNum", "8");
		prodMap.put("cId", getIntent().getStringExtra("cId"));
        String inmapData="{\"ServiceName\":\"extProdManagerService\" , \"Data\":{\"ACTION\":\"QRYBASECONTENTANDCOLORPROD\",\"type\":\""+getIntent().getStringExtra("cId")+"\"";
		if(orderby!=null){
			prodMap.put("orderby", orderby);
			inmapData=inmapData+",\"ordertype\":\""+orderby+"\"";
		}
		inmapData=inmapData+"}}";
		prodMap.put("JsonData", inmapData);
		prodReqVo.requestDataMap = prodMap;
		prodReqVo.jsonParser = new ProductListParser();
		super.getDataFromServer(prodReqVo, new DataCallback<List<ProductListVo>>(){

			@Override
			public void processData(List<ProductListVo> paramObject,
					boolean paramBoolean) {
				productInfos = paramObject;
				if(productInfos==null){
					tv_prodlist_noresult.setVisibility(View.VISIBLE);
					iv_prodlist_noresult.setVisibility(View.VISIBLE);
				}
				ProductLvAdapter adapter = new ProductLvAdapter(context, lv_prodlist_listprod, productInfos);
				lv_prodlist_listprod.setAdapter(adapter);
			}
			
		});
	}

	@Override
	protected void setListener() {
		lv_prodlist_listprod.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String itemID = productInfos.get(position).getId();
				//ProductDetailActivity
				Intent detailIntent = new Intent(ProductListActivity.this,ProductDetailActivity.class);
				detailIntent.putExtra("id", itemID);
				Logger.i(TAG, itemID+"");
				startActivity(detailIntent);
				
			}
			
		});
	}

}
