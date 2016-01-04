package com.lianmeng.core.home.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.lianmeng.core.activity.R;
import com.lianmeng.core.framework.engine.SyncImageLoader;
import com.lianmeng.core.framework.engine.SyncImageLoader.OnImageLoadListener;
import com.lianmeng.core.home.vo.HomeBannerVo;
/**
 * 首页BannerAdapter 
 * @author liu
 *
 */
public class HomeBannerAdapter extends BaseAdapter implements OnImageLoadListener {

 	private Context context;
	private List<HomeBannerVo> galleryList;
 	private SyncImageLoader syncImageLoader;
	private Drawable[] drawables;

	public HomeBannerAdapter(Context context, List<HomeBannerVo> arrayList) {
		this.context = context;
		this.galleryList = arrayList;
 		syncImageLoader = new SyncImageLoader();
		int size = arrayList.size();
		drawables = new Drawable[size];
		syncImageLoader.setLoadLimit(0, size);
		//加载图片
		
		for (int i = 0; i < size; i++) {
			String iImageUrl=arrayList.get(i).getUrl();
			if(iImageUrl!=null&&!iImageUrl.startsWith("http")){
				iImageUrl=context.getString(R.string.sysRequestHost)+iImageUrl;
			}
			syncImageLoader.loadImage(i, iImageUrl, this);
		}
	}

	@Override
	public int getCount() {
		return Integer.MAX_VALUE;
	}

	@Override
	public Object getItem(int postion) {
		return galleryList.get(postion);
	}

	@Override
	public long getItemId(int postion) {
		return postion;
	}

	@Override
	public View getView(int postion, View arg1, ViewGroup arg2) {
		ImageView imageView;
		if (arg1 == null) {
			imageView = new ImageView(context);
		} else {
			imageView = (ImageView) arg1;
		}
		Drawable drawable = drawables[postion % galleryList.size()];
		if (drawable == null)
			imageView.setImageResource(R.drawable.product_loading);
		else
			imageView.setImageDrawable(drawable);
		imageView.setScaleType(ImageView.ScaleType.FIT_XY);
		imageView.setBackgroundResource(R.drawable.home_backgroud);
		return imageView;
	}

	@Override
	public void onImageLoad(Integer t, Drawable drawable) {
		this.drawables[t] = drawable;
		notifyDataSetChanged();
	}

	@Override
	public void onError(Integer t) {

	}
}
