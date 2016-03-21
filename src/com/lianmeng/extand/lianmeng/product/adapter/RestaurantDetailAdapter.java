package com.lianmeng.extand.lianmeng.product.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.lianmeng.core.activity.R;
import com.lianmeng.core.framework.sysadapter.ImageAsyncLoaderAdpter;
import com.lianmeng.core.framework.util.BadgeView;
import com.lianmeng.core.framework.util.Tools;
import com.lianmeng.extand.lianmeng.product.vo.RestaurantProductBuyVo;
import com.lianmeng.extand.lianmeng.product.widget.stickylistheaders.StickyListHeadersAdapter;
import com.lianmeng.extand.lianmeng.product.widget.stickylistheaders.StickyListHeadersListView;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;
 

public class RestaurantDetailAdapter extends  ImageAsyncLoaderAdpter<RestaurantProductBuyVo>  implements
		StickyListHeadersAdapter, SectionIndexer {
	private static final String TAG = "RestaurantDetailAdapter";
	private List<RestaurantProductBuyVo> list;
 	private Context context;
 	private Drawable[] drawables;
 	private boolean isPlay;
 	private Runnable runnable = new Runnable() {
		
		@Override
		public void run() {
			if (!isPlay)
				return ;
 			handler.postDelayed(this, 1000);
			notifyDataSetChanged();
 		}
	};
 	private Handler handler = new Handler();
	private SimpleDateFormat simpleDateFormat;
	//private final Activity mActivity;
	private String[] mFoods;
	private int[] mFoodsNum;// 食品选中的次数
	private int[] mSectionIndices;
	private String[] mSectionLetters;
	private LayoutInflater mInflater;
	private int CLICK_NUM = 0;

	private ArrayList<RestaurantProductBuyVo> selProduct;
	private TextView shopCart;// 购物车
	private TextView shopCartNum;// 购物车 商品数量
	private TextView shopCartPrice;// 购物车 总价
	private ViewGroup anim_mask_layout;// 动画层
	private ImageView buyImg;// 这是在界面上跑的小图片
	private int buyNum = 0;// 购买数量
	private BadgeView buyNumView;// 显示购买数量的控件

	public RestaurantDetailAdapter(List<RestaurantProductBuyVo> list, StickyListHeadersListView listView, Context context, TextView order_cart, TextView order_cart_num, TextView order_cart_price) {
		//
		super(context, listView.getWrappedList(), list);
		//mActivity = context.get;
		selProduct=new ArrayList<RestaurantProductBuyVo>();
		shopCart = order_cart;
		shopCartNum = order_cart_num;
		shopCartPrice = order_cart_price;
		this.list = list;
 		this.context = context;
 		drawables = new Drawable[list.size()];
 		 
 		simpleDateFormat = new SimpleDateFormat(context.getString(R.string.extendProdMsgDateFormartMsg));
		mInflater = LayoutInflater.from(context);
		mFoods = context.getResources().getStringArray(R.array.foods);
		initFoodNum();
		mSectionIndices = getSectionIndices();
		mSectionLetters = getSectionLetters();
	}

	private void initFoodNum() {
		 
	}
	
	private int getHasProdSize(String prodId){
		int rtn=0;
		for(int i=0;i<selProduct.size();i++){
			if(prodId.equals(selProduct.get(i).getId())){
				rtn++;
			}
		}
		return rtn;
	}

	private void removeHasProdSize(String prodId){
		for(int i=0;i<selProduct.size();i++){
			if(prodId.equals(selProduct.get(i).getId())){
				selProduct.remove(i);
				break;
			}
		}
	}
	
	private int[] getSectionIndices() {
		ArrayList<Integer> sectionIndices = new ArrayList<Integer>();
		String lastFirstChar ="";
		if(list.size()>0){
		    lastFirstChar=list.get(0).getName();
		    sectionIndices.add(0);
		}
		for (int i = 1; i < list.size(); i++) {
			if (list.get(i).getName() != lastFirstChar) {
				lastFirstChar = list.get(i).getName();
				sectionIndices.add(i);
			}
		}
		int[] sections = new int[sectionIndices.size()];
		for (int i = 0; i < sectionIndices.size(); i++) {
			sections[i] = sectionIndices.get(i);
		}
		return sections;
	}

	private String[] getSectionLetters() {
		String[] letters = new String[mSectionIndices.length];
		for (int i = 0; i < mSectionIndices.length; i++) {
			letters[i] = mFoods[mSectionIndices[i]].split("-")[1];
		}
		return letters;
	}

	@Override
	public int getCount() {
		return list.size();
	}
	
	public ArrayList<RestaurantProductBuyVo> getSelItems() {
		return selProduct;
	}

	/*@Override
	public Object getItem(int position) {
		return mFoods[position];
	}*/
	
	@Override
	public RestaurantProductBuyVo getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
 
		if (convertView == null) {
			
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.adapter_product_buy_item, parent,false);
			//view = inflate(R.layout.buy_item, null);
			holder.image= (ImageView) convertView
					.findViewById(R.id.image);
			holder.name = (TextView) convertView
					.findViewById(R.id.projuctTitle);
			holder.add = (ImageView) convertView
					.findViewById(R.id.add);
			holder.size = (TextView) convertView
					.findViewById(R.id.numText);
			holder.minus = (ImageView) convertView
					.findViewById(R.id.reduce);
			holder.price = (TextView) convertView
					.findViewById(R.id.projuctPrice);
			holder.id = (TextView) convertView
					.findViewById(R.id.limitProductId);
			convertView.setTag(R.layout.adapter_product_buy_item,holder);
		} else {
			holder = (ViewHolder) convertView.getTag(R.layout.adapter_product_buy_item);
		}
		
		holder.add.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int num = ++CLICK_NUM;
				holder.size.setText((getHasProdSize(list.get(position).getId())+1) + "");
				if (num > 0) {
				    double beginPrice=Double.valueOf(shopCartPrice.getText().toString()==""?"0.0":shopCartPrice.getText().toString());
				    double curPrice=beginPrice + list.get(position).getPrice();
					holder.minus.setVisibility(View.VISIBLE);
					shopCart.setText(num + "份￥" + curPrice + "");
					shopCartNum.setText(num+"");
					shopCartPrice.setText(String.valueOf(curPrice));
					selProduct.add(list.get(position));
				}
				int[] start_location = new int[2];// 一个整型数组，用来存储按钮的在屏幕的X、Y坐标
				v.getLocationInWindow(start_location);// 这是获取购买按钮的在屏幕的X、Y坐标（这也是动画开始的坐标）
				buyImg = new ImageView(context);
				buyImg.setImageBitmap(getAddDrawBitMap(position));// 设置buyImg的图片
				setAnim(buyImg, start_location);// 开始执行动画
				Log.i("asdasdsa", num + "");
			}
		});
		holder.minus.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				int num = --CLICK_NUM;
				if(num<0){
				    num=0;
				}
				Log.i("asdasdsa", num + "");
				if (num > 0) {
				    double beginPrice=Double.valueOf(shopCartPrice.getText().toString()==""?"0.0":shopCartPrice.getText().toString());
                    double curPrice=beginPrice - list.get(position).getPrice();
					holder.size.setText((getHasProdSize(list.get(position).getId())-1)+ "");
					shopCart.setText(num + "份￥" + curPrice + "");
					shopCartNum.setText(num+"");
					shopCartPrice.setText(String.valueOf(curPrice));
					removeHasProdSize(list.get(position).getId());
				} else {
					holder.minus.setVisibility(View.GONE);
				}
			}
		});
		//holder.add.setText("￥"+position);
		holder.price.setText("￥"+list.get(position).getPrice());
		holder.name.setText(list.get(position).getName());
		holder.id.setText(list.get(position).getId());
		// 定时器
		// LimitBuyVo item = getItem(position);
		// long v = item.getLefttime() - System.currentTimeMillis();
		// Date date = new Date(v);
		// holder.textProductCommentNum.setText(simpleDateFormat.format(date));
		
		//loadImage(position, list.get(position).getPic());
		Drawable d = drawables[position];
		if (d == null) {
			convertView.setTag(position);
			loadImage(position, list.get(position).getPic());
		} else {
			//holder.image.setBackgroundDrawable(d);
		}
		
		return convertView;
	}

	public Bitmap getAddDrawBitMap(int position) {
		Tools tools = new Tools();
	    View drawableViewPar = LayoutInflater.from(context).inflate(R.layout.act_product_down_cart_pay_list_item_operation, null);
	    TextView text = (TextView) drawableViewPar.findViewById(R.id.food_list_item_price_text_view);
	    text.setText("￥"+list.get(position).getPrice());
	    return tools.convertViewToBitmap(text);
	}

	/**
	 * @Description: 创建动画层
	 * @param
	 * @return void
	 * @throws
	 */
	private ViewGroup createAnimLayout() {
		ViewGroup rootView = (ViewGroup)((Activity) context).getWindow().getDecorView();
		LinearLayout animLayout = new LinearLayout(context);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		animLayout.setLayoutParams(lp);
		animLayout.setId(Integer.MAX_VALUE);
		//animLayout.setBackgroundResource(android.R.color.transparent);
		rootView.addView(animLayout);
		return animLayout;
	}

	private View addViewToAnimLayout(final ViewGroup vg, final View view,
			int[] location) {
		int x = location[0];
		int y = location[1];
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.leftMargin = x;
		lp.topMargin = y;
		view.setLayoutParams(lp);
		return view;
	}

	private void setAnim(final View v, int[] start_location) {
		anim_mask_layout = null;
		anim_mask_layout = createAnimLayout();
		anim_mask_layout.addView(v);// 把动画小球添加到动画层
		final View view = addViewToAnimLayout(anim_mask_layout, v,
				start_location);
		int[] end_location = new int[2];// 这是用来存储动画结束位置的X、Y坐标
		shopCart.getLocationInWindow(end_location);// shopCart是那个购物车

		// 计算位移
		int endX = 0 - start_location[0] + 40;// 动画位移的X坐标
		int endY = end_location[1] - start_location[1];// 动画位移的y坐标
		TranslateAnimation translateAnimationX = new TranslateAnimation(0,
				endX, 0, 0);
		translateAnimationX.setInterpolator(new LinearInterpolator());
		translateAnimationX.setRepeatCount(0);// 动画重复执行的次数
		translateAnimationX.setFillAfter(true);

		TranslateAnimation translateAnimationY = new TranslateAnimation(0, 0,
				0, endY);
		translateAnimationY.setInterpolator(new AccelerateInterpolator());
		translateAnimationY.setRepeatCount(0);// 动画重复执行的次数
		translateAnimationX.setFillAfter(true);

		AnimationSet set = new AnimationSet(false);
		set.setFillAfter(false);
		set.addAnimation(translateAnimationY);
		set.addAnimation(translateAnimationX);
		set.setDuration(800);// 动画的执行时间
		view.startAnimation(set);
		// 动画监听事件
		set.setAnimationListener(new AnimationListener() {
			// 动画的开始
			@Override
			public void onAnimationStart(Animation animation) {
				v.setVisibility(View.VISIBLE);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
			}

			// 动画的结束
			@Override
			public void onAnimationEnd(Animation animation) {
				v.setVisibility(View.GONE);
				buyNum++;// 让购买数量加1
				// buyNumView.setText(buyNum + "");//
				// buyNumView.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
				// buyNumView.show();
			}
		});

	}

	@Override
	public View getHeaderView(int position, View convertView, ViewGroup parent) {
		HeaderViewHolder holder;

		if (convertView == null) {
			holder = new HeaderViewHolder();
			convertView = mInflater.inflate(
					R.layout.adapter_product_main_food_list_header_restaurant, parent, false);
			holder.text = (TextView) convertView
					.findViewById(R.id.tv_food_list_head);
			convertView.setTag(holder);
		} else {
			holder = (HeaderViewHolder) convertView.getTag();
		}

		CharSequence headerChar = list.get(position).getName();
		holder.text.setText(headerChar);

		return convertView;
	}

	@Override
	public long getHeaderId(int position) {
		return Long.valueOf(list.get(position).getId());
	}

	@Override
	public int getPositionForSection(int section) {
		if (mSectionIndices.length == 0) {
			return 0;
		}
		if (section >= mSectionIndices.length) {
			section = mSectionIndices.length - 1;
		} else if (section < 0) {
			section = 0;
		}
		return mSectionIndices[section];
	}

	@Override
	public int getSectionForPosition(int position) {
		for (int i = 0; i < mSectionIndices.length; i++) {
			if (position < mSectionIndices[i]) {
				return i - 1;
			}
		}
		return mSectionIndices.length - 1;
	}

	@Override
	public Object[] getSections() {
		return mSectionLetters;
	}

	public void clear() {
		mFoods = new String[0];
		mSectionIndices = new int[0];
		mSectionLetters = new String[0];
		notifyDataSetChanged();
	}

	public void restore() {
		mFoods = context.getResources().getStringArray(R.array.foods);
		mSectionIndices = getSectionIndices();
		mSectionLetters = getSectionLetters();
		notifyDataSetChanged();
	}

	class HeaderViewHolder {
		TextView text;
	}

	class ViewHolder {
		TextView id;
		TextView name;
		ImageView add;
		TextView size;
		TextView price;
		ImageView minus;
		ImageView image;
	}
	
	@Override
	public void onImageLoadFinish(Integer position, Drawable drawable) {
		drawables[position] = drawable;
		
		View view = mListView.findViewWithTag(position);
 		if (view != null) {
			ImageView iv = (ImageView) view.findViewById(R.id.image);
			iv.setImageDrawable(drawable);
		}
	}

}
