package com.lianmeng.core.home.adapter;
 

import com.lianmeng.core.activity.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class HomeClassGridAdapter extends BaseAdapter {
private Context context;
private int[] data;
	
	public HomeClassGridAdapter(Context context,int[] data){
		
		this.context=context;
		this.data=data;
	}
	
	@Override
	public int getCount() {
		return data.length;
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View currentView, ViewGroup arg2) {
		HolderView holderView=null;
		if (currentView==null) {
			holderView=new HolderView();
			currentView=LayoutInflater.from(context).inflate(R.layout.adapter_home_class_img_grid, null);
			holderView.iv_pic=(ImageView) currentView.findViewById(R.id.iv_adapter_grid_pic);
			currentView.setTag(holderView);
		}else {
			holderView=(HolderView) currentView.getTag();
		}
		
		
		holderView.iv_pic.setImageResource(data[position]);
		
		
		return currentView;
	}
	
	
	public class HolderView {
		
		private ImageView iv_pic;
		
	}

}
