package com.lianmeng.core.product.adapter;

import java.util.List;

import com.lianmeng.core.activity.R;
import com.lianmeng.core.category.vo.FilterCategory;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ProductFilterAdapter extends BaseAdapter {

	private Context context;
	private List<FilterCategory> filter;
	private ListView lv;
	
	

	public ProductFilterAdapter(Context context, List<FilterCategory> filter,
			ListView lv) {
		super();
		this.context = context;
		this.filter = filter;
		this.lv = lv;
	}

	@Override
	public int getCount() {
		return filter.size();
	}

	@Override
	public Object getItem(int position) {
		return filter.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HolderView holder ;
		FilterCategory f = new FilterCategory();
		f = filter.get(position);
		if(convertView == null){
			holder = new HolderView();
			convertView = View.inflate(context, R.layout.old_adapter_product_filter_sift_items, null);
			convertView.setTag(holder);
		}else{
			holder = (HolderView) convertView.getTag();
		}
		holder.select = (TextView) convertView.findViewById(R.id.categoryTitle);
		holder.select.setText(context.getString(R.string.prodListTitleProdAllMsg));
		holder.title = (TextView) convertView.findViewById(R.id.titleBack);
		holder.title.setText(f.getKey());
		return convertView;
	}

	static class HolderView{
		TextView title;
		TextView select;
	}
}
