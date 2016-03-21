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
import com.lianmeng.core.framework.sysactivity.BaseWapperNewActivity;
import com.lianmeng.core.framework.sysactivity.BaseWapperNewActivity.DataCallback;
import com.lianmeng.core.framework.sysparser.StringVParser;
import com.lianmeng.core.framework.sysvo.RequestVo;
import com.lianmeng.core.framework.sysvo.StringV;
import com.lianmeng.core.framework.util.CommonUtil;
import com.lianmeng.core.framework.util.Constant;
import com.lianmeng.core.product.adapter.SearchAdapter;
import com.lianmeng.core.product.parser.SearchRecommondParser;

public class SearchActivity extends BaseWapperNewActivity {
	private EditText keyWordEdit;
 	private ListView hotWordsLv;
	private List<StringV> search;
	
	
	
	/*@Override
	public void onClick(View v) {
		switch(v.getId()){ 
		case R.id.searchTv:
			break;
		}
	}*/
	
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
		setContentView(R.layout.old_act_search_main);
		//setHeadLeftVisibility(View.INVISIBLE);
		setTitle(getString(R.string.searchProdTitleButtonNameMsg));
		//setHeadRightText(getString(R.string.searchProdTitleButtonNameMsg));
		//setHeadRightVisibility(View.VISIBLE);
		//selectedBottomTab(Constant.SEARCH);
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
