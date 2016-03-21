package com.lianmeng.core.category.adapter;

import java.util.List;

import com.lianmeng.core.activity.R;
import com.lianmeng.core.category.vo.CategoryVo;
import com.lianmeng.core.framework.util.ImageUtil;
import com.lianmeng.core.framework.util.ImageUtil.ImageCallback;
import com.lianmeng.core.framework.util.Logger;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CategoryAdaper extends BaseAdapter {

private static final String TAG = "CategoryAdaper";

	private List<CategoryVo> categoryInfos;
	
	
	private Context context;
	
	
	public CategoryAdaper( Context context ,List<CategoryVo> categoryInfos) {
		this.context = context ;
		this.categoryInfos = categoryInfos;
	}

	@Override
	public int getCount() {
		Logger.i(TAG, ""+categoryInfos.size());
		return categoryInfos.size();
	}

	@Override
	public Object getItem(int position) {
		return categoryInfos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder = new ViewHolder();
		CategoryVo vo = categoryInfos.get(position);
		if(convertView ==null){
			convertView = View.inflate(context, R.layout.old_adapter_category_item, null);
		}
		convertView.setTag(vo.getId());
		/**
		 * imgIcon
		 * @id/textContent
		 * @id/item_describe
		 */
		holder.iv = (ImageView) convertView.findViewById(R.id.imgIcon);
		holder.tv_content = (TextView) convertView.findViewById(R.id.textContent);
		holder.tv_describe = (TextView) convertView.findViewById(R.id.item_describe);
		String imgUrl = vo.getPic();
		String iImageUrl=imgUrl;
		if(iImageUrl!=null&&!iImageUrl.startsWith("http")){
			iImageUrl=context.getString(R.string.sysRequestHost)+imgUrl;
		}
		String imagePath = ImageUtil.getCacheImgPath().concat(ImageUtil.md5(iImageUrl));
		Bitmap bitmap = ImageUtil.loadImage(imagePath, iImageUrl, new ImageCallback(){

			@Override
			public void loadImage(Bitmap bitmap, String imagePath) {
				holder.iv.setImageBitmap(bitmap);
				notifyDataSetChanged();
			}
			
		});
		if(bitmap==null){
			holder.iv.setImageResource(R.drawable.category_diaper01);
		}else{
			holder.iv.setImageBitmap(bitmap);
		}
		holder.tv_content.setText(vo.getName());
		holder.tv_describe.setText(vo.getTag());
		return convertView;
	}

	static class ViewHolder{
		ImageView iv;
		TextView tv_content;
		TextView tv_describe;
	}
}
