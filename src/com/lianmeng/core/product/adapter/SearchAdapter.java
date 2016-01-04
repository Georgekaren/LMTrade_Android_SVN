package com.lianmeng.core.product.adapter;

import java.util.List;

import com.lianmeng.core.activity.R;
import com.lianmeng.core.framework.sysvo.StringV;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SearchAdapter extends BaseAdapter {
	private Context context;
	private List<StringV> search;
	
	public SearchAdapter(Context context,List<StringV> search){
		this.context = context;
		this.search = search;
		
		
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return search.size()+1;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(position==0){
			TextView tv = (TextView) View.inflate(context, R.layout.search_item, null);
			tv.setTextSize(35);
			tv.setTextColor(Color.RED);
			tv.setText(this.context.getString(R.string.searchProdTitleHotButtonNameMsg));
			return tv;		
		}
		TextView tv_content =  (TextView) View.inflate(context, R.layout.search_item, null);
			tv_content.setTextColor(Color.BLACK);	
			tv_content.setText("   "+search.get(position-1).getName());
			
			return tv_content;
	}

}
