package com.lianmeng.core.product.activity;

import java.util.List;

import com.lianmeng.core.activity.R;
import com.lianmeng.core.activity.R.id;
import com.lianmeng.core.activity.R.layout;
import com.lianmeng.core.activity.R.string;
import com.lianmeng.core.framework.service.IECManager;
import com.lianmeng.core.framework.sysactivity.BaseWapperNewActivity;
import com.lianmeng.core.framework.util.Constant;
import com.lianmeng.core.product.adapter.ProductAdapter;
import com.lianmeng.core.product.vo.ProdcutHistory;
import com.lianmeng.core.product.vo.ProductListVo;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * 浏览记录
 * @author liu
 *
 */
public class ProductHistoryActivity extends BaseWapperNewActivity implements OnItemClickListener {

	private ListView mListView;
	private IECManager ecManager;
	private ProductAdapter productAdapter;

	 

	@Override
	protected void findViewById() {
		mListView = (ListView) findViewById(R.id.simple_list_view);
	}

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.old_act_product_hist_listview);
		
		ecManager = getECManager();
	}

	@Override
	protected void processLogic() {
		 
		 	
	}
	public void loadData() {
		List<ProdcutHistory> histories = ecManager.getAllProductHistory();
		if (histories.size() > 0) {
			productAdapter = new ProductAdapter(this, mListView);
			productAdapter.addAll(histories);
			mListView.setAdapter(productAdapter);
		} else {
			setContentView(R.layout.old_act_product_his_empty);
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		loadData();
	}
	
	@Override
	protected void onHeadRightButton(View v) {
		ecManager.clearProductHistory();
		setContentView(R.layout.old_act_product_his_empty);
	}
	
	@Override
	protected void setListener() {
		mListView.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		ProductListVo item = productAdapter.getItem(position);
		Intent intent = new Intent(this, ProductDetailActivity.class);
		intent.putExtra("id", item.getId());
		startActivity(intent );
	}
}
