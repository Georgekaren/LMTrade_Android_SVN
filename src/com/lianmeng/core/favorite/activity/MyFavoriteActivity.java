package com.lianmeng.core.favorite.activity;

import java.util.HashMap;
import java.util.List;

import android.media.ToneGenerator;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lianmeng.core.activity.R;
import com.lianmeng.core.activity.R.drawable;
import com.lianmeng.core.activity.R.id;
import com.lianmeng.core.activity.R.layout;
import com.lianmeng.core.activity.R.string;
import com.lianmeng.core.favorite.adapter.MyFavoriteAdapter;
import com.lianmeng.core.favorite.parser.FavoriteParser;
import com.lianmeng.core.framework.sysactivity.BaseWapperActivity;
import com.lianmeng.core.framework.sysactivity.BaseWapperActivity.DataCallback;
import com.lianmeng.core.framework.sysparser.SuccessParser;
import com.lianmeng.core.framework.sysvo.PageVo;
import com.lianmeng.core.framework.sysvo.RequestVo;
import com.lianmeng.core.framework.util.Constant;
import com.lianmeng.core.framework.util.Logger;
import com.lianmeng.core.framework.util.NetUtil;
import com.lianmeng.core.framework.util.SysU;
import com.lianmeng.core.product.vo.Product;

public class MyFavoriteActivity extends BaseWapperActivity {
	private static final String TAG = "MyFavoriteActivity";
	private ListView myfavorite_product_list;
	private PageVo pageVo;
	private View pageView;
	TextView previousPage;
	TextView nextPage;
	TextView textPage;
	MyFavoriteAdapter adapter;
	private List<Product> data;
	
	@Override
	protected void onHeadRightButton(View v) {

		HashMap<String, String> requestDataMap = new HashMap<String, String>();
		String inmapData="{\"ServiceName\":\"extProdManagerService\" , \"Data\":{\"ACTION\":\"MIMREMOVEFAVORITEPROD\",\"userId\":\""+SysU.USERID+"\",\"id\":\"\"}}";
		requestDataMap.put("JsonData", inmapData);
		RequestVo vo = new RequestVo(R.string.sysRequestServLet, context, requestDataMap,
				new SuccessParser());
		Boolean bool = (Boolean) NetUtil.post(vo);
		if (bool != null) {
			if (bool) {
				data.clear();
				// 分页处理
				if (pageView != null) {
					myfavorite_product_list.removeFooterView(pageView);
				}
				adapter.notifyDataSetChanged();
				setHeadRightVisibility(View.INVISIBLE);
				setContentView(R.layout.empty);
				TextView tv = (TextView) this.findViewById(R.id.empty_text);
				tv.setText(getString(R.string.favoriteMsgNoProductMsg));
			}
		}
		
		
	};
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.my_page_previous:
			Logger.d(TAG, "显示前一页------1");
			Logger.d(TAG,pageVo.wantedPageNum+"");
			if(pageVo.wantedPageNum<=1){
				previousPage.setClickable(false);
				return;
			}
			processLogic();
			pageVo.wantedPageNum -= 1;
			Logger.d(TAG, "显示前一页------2");
			Logger.d(TAG,pageVo.wantedPageNum+"");
			textPage.setText(pageVo.wantedPageNum+"/"+pageVo.totalPageNum);
			break;
		case R.id.my_page_next:
			Logger.d(TAG, "显示下一页-------1");
			Logger.d(TAG,pageVo.wantedPageNum+"");
			if(pageVo.wantedPageNum>=pageVo.totalPageNum){
				nextPage.setClickable(false);
				return;
			}
			processLogic();
			pageVo.wantedPageNum += 1;
			Logger.d(TAG, "显示下一页-------2");
			Logger.d(TAG,pageVo.wantedPageNum+"");
			textPage.setText(pageVo.wantedPageNum+"/"+pageVo.totalPageNum);
			break;
		}
	}

	@Override
	protected void findViewById() {
		myfavorite_product_list = (ListView) this
				.findViewById(R.id.myfavorite_product_list);

	}

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.my_favorite_activity);
		setHeadLeftVisibility(View.VISIBLE);
		setHeadRightVisibility(View.VISIBLE);
		setHeadRightText(getString(R.string.favoriteButtonClearProductMsg));
		setTitle(R.string.my_favorite_title);
		setHeadBackgroundResource(R.drawable.head_bg);
		selectedBottomTab(Constant.HOME);
		int totalFavoriteCount = getIntent().getIntExtra("totalFavoriteCount",
				0);
		Logger.d(TAG, totalFavoriteCount + "------------");
		pageVo = new PageVo(0, totalFavoriteCount, 1);
	}

	@Override
	protected void processLogic() {
		RequestVo favoriteRequeset = new RequestVo();
		favoriteRequeset.context = this;
		favoriteRequeset.jsonParser = new FavoriteParser();
		
		String inmapData="{\"ServiceName\":\"extProdManagerService\" , \"Data\":{\"ACTION\":\"QRYBASEFAVORITEPROD\",\"userId\":\""+SysU.USERID+"\"}}";
		favoriteRequeset.requestUrl = R.string.sysRequestServLet;
		favoriteRequeset.requestDataMap = new HashMap<String, String>();
		favoriteRequeset.requestDataMap.put("JsonData", inmapData);
		favoriteRequeset.requestDataMap.put("page", pageVo.wantedPageNum + "");
		favoriteRequeset.requestDataMap.put("pageNum", pageVo.pageLenth + "");
		getDataFromServer(favoriteRequeset, new DataCallback<List<Product>>() {
			@Override
			public void processData(List<Product> paramObject,
					boolean paramBoolean) {
				data = paramObject;
				// 分页处理
				if (pageView != null) {
					myfavorite_product_list.removeFooterView(pageView);
				}
				if (paramObject.size() >= pageVo.pageLenth) {
					showPageBar();
				}
				adapter  = new MyFavoriteAdapter(MyFavoriteActivity.this,myfavorite_product_list,data);
				myfavorite_product_list.setAdapter(adapter);
			}
		});
	}

	private void showPageBar() {
		pageView = View.inflate(MyFavoriteActivity.this, R.layout.page, null);
		previousPage = (TextView) pageView.findViewById(R.id.my_page_previous);
		nextPage = (TextView) pageView.findViewById(R.id.my_page_next);
		textPage = (TextView) pageView.findViewById(R.id.my_page_text);
		previousPage.setOnClickListener(this);
		nextPage.setOnClickListener(this);
		textPage.setText(pageVo.wantedPageNum + "/" + pageVo.totalPageNum);
		myfavorite_product_list.addFooterView(pageView);
	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub

	}

}
