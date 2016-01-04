package com.lianmeng.core.product.activity;

import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.lianmeng.core.activity.R;
import com.lianmeng.core.activity.R.id;
import com.lianmeng.core.activity.R.layout;
import com.lianmeng.core.activity.R.string;
import com.lianmeng.core.framework.sysactivity.BaseWapperActivity;
import com.lianmeng.core.framework.sysactivity.BaseWapperActivity.DataCallback;
import com.lianmeng.core.framework.sysparser.StringVParser;
import com.lianmeng.core.framework.sysvo.RequestVo;
import com.lianmeng.core.framework.sysvo.StringV;
import com.lianmeng.core.framework.util.CommonUtil;
import com.lianmeng.core.framework.util.Constant;
import com.lianmeng.core.product.adapter.SearchAdapter;
import com.lianmeng.core.product.parser.SearchRecommondParser;

public class SearchActivity extends BaseWapperActivity {
	private EditText keyWordEdit;
 	private ListView hotWordsLv;
	private List<StringV> search;
	
	
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){ 
		case R.id.searchTv:
			 
			
			
//			RequestVo vo = new RequestVo();
//			vo.context = SearchActivity.this;
//			HashMap map = new HashMap();
//			map.put("keyword", keyWord);
//			map.put("page", 1);
//			map.put("pageNum", 10);
//			
//			vo.requestDataMap = map;
//			vo.requestUrl = R.string.search;
//			vo.jsonParser = new SearchParser();
//			super.getDataFromServer(vo, new DataCallback<List<Product>>() {
//
//				@Override
//				public void processData(List<Product> paramObject,
//						boolean paramBoolean) {
//					SearchResultAdapter adapter= new SearchResultAdapter(context, paramObject);
//					
//				}
//			});
			
			
			break;
		}
	}
	
	@Override
	protected void onHeadRightButton(View v) {
		String keyWord = keyWordEdit.getText().toString();
		if(keyWord==null||"".equals(keyWord)){
			CommonUtil.showInfoDialog(SearchActivity.this, getString(R.string.searchProdMsgInputHiteNameMsg));
			return;
		}
		Intent intent = new Intent(SearchActivity.this,SearchProductListActivity.class);
		intent.putExtra("keyWord", keyWord);
		startActivity(intent);
 	}

	@Override
	protected void findViewById() {
 		hotWordsLv = (ListView) findViewById(R.id.hotWordsLv);
		keyWordEdit = (EditText) findViewById(R.id.keyWordEdit);
		Intent intent = new Intent();
		
	}

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.search_activity);
		setHeadLeftVisibility(View.INVISIBLE);
		setTitle(getString(R.string.searchProdTitleButtonNameMsg));
		setHeadRightText(getString(R.string.searchProdTitleButtonNameMsg));
		setHeadRightVisibility(View.VISIBLE);
		selectedBottomTab(Constant.SEARCH);
	}

	@Override
	protected void processLogic() {
		showProgressDialog();
		RequestVo vo = new RequestVo();
		vo.context = SearchActivity.this;
		vo.jsonParser = new StringVParser();
		vo.requestUrl = R.string.sysRequestServLet;
		HashMap<String, String> prodMap = new HashMap<String, String>();
		String inmapData="{\"ServiceName\":\"extProdManagerService\" , \"Data\":{\"ACTION\":\"QRYPRODFINALSLIST\",\"finalkeyword\":\"SUBBRAND\"}}";
		prodMap.put("JsonData", inmapData);
		vo.requestDataMap = prodMap;
		
		super.getDataFromServer(vo, new DataCallback<List<StringV>>() {


			@Override
			public void processData(List<StringV> paramObject,
					boolean paramBoolean) {
				if(paramObject!=null){
					search = paramObject;
					SearchAdapter adapter = new SearchAdapter(context,paramObject);
					hotWordsLv.setAdapter(adapter);
					closeProgressDialog();
				}
			}
		});
	}
	
	
	
	@Override
	protected void setListener() {
 		hotWordsLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			}
		});
	}

}
