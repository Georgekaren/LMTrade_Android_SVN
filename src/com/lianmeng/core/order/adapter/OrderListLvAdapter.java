package com.lianmeng.core.order.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lianmeng.core.activity.R;
import com.lianmeng.core.framework.sysadapter.ImageAsyncLoaderAdpter;
import com.lianmeng.core.order.activity.RestaurantOrderListActivity;
import com.lianmeng.core.order.vo.OrderList;
import com.lianmeng.extand.lianmeng.product.vo.RestaurantProductBuyVo;


public class OrderListLvAdapter extends ImageAsyncLoaderAdpter<OrderList>  implements OnClickListener {
	private static final String TAG = "OrderListLvAdapter";
	private Context context;
	private List<OrderList> paramList;
	//private List<OrderList> cancelablelist;
	//private List<OrderList> uncancelablelist;
	private RestaurantOrderListActivity activity;
	private Drawable[] drawables;
	
	private OrderList orderVo = null;
	public OrderListLvAdapter(Context context,ListView lView, List<OrderList> paramList){
	    super(context, lView, paramList);
		this.context = context;
		this.activity = (RestaurantOrderListActivity) context;
		this.paramList = paramList;
		drawables = new Drawable[this.paramList.size()];
		//this.cancelablelist = cancelablelist;
		//this.uncancelablelist = uncancelablelist;
		//Log.i(TAG, "cancelablelist可以取消的数目为:"+cancelablelist.size());
		//Log.i(TAG, "uncancelablelist不可以取消的数目为:"+uncancelablelist.size());
	}
	@Override
	public int getCount() {
		return paramList.size();
	}

	@Override
	public OrderList getItem(int position) {
		Log.i(TAG, "当前列表下总订单大小为："+ paramList.size());
		/*Log.i(TAG, "可取消订单的数目为："+ cancelablelist.size());
		Log.i(TAG, "不可取消订单的数目为："+ uncancelablelist.size());
		if(cancelablelist.size()>0 && position<=cancelablelist.size()-1){
			return cancelablelist.get(position);
		}else if(position>cancelablelist.size()-1 && position<=paramList.size()-1){
			return uncancelablelist.get(position-cancelablelist.size());
		}*/
		return paramList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.i(TAG, "当前位置为："+ position);
		View view = null;
		//if(cancelablelist.size()>0 && position<=cancelablelist.size()-1){
			view = View.inflate(context, R.layout.adapter_order_listitem_cancelable, null);
			Holder.orderId_text = (TextView) view.findViewById(R.id.orderId_text);
			Holder.orderPrice_text = (TextView) view.findViewById(R.id.orderPrice_text);
			Holder.orderTime_text = (TextView) view.findViewById(R.id.orderTime_text);
			Holder.orderState_text = (TextView)view.findViewById(R.id.orderState_text);
			Holder.textPayOrder = (Button)view.findViewById(R.id.btn_pay);
			Holder.textCancelOrder = (Button)view.findViewById(R.id.textCancelOrder);
			
			Holder.imo_tv_time = (TextView) view.findViewById(R.id.imo_tv_time);
			Holder.imo_tv_back_msg = (TextView) view.findViewById(R.id.imo_tv_back_msg);
			Holder.imo_tv_msg = (TextView) view.findViewById(R.id.imo_tv_msg);
			Holder.textPrice = (TextView) view.findViewById(R.id.textPrice);
			Holder.imo_iv_icon = (ImageView) view.findViewById(R.id.imo_iv_icon);
			
			
			
			
			Holder.textCancelOrder.setOnClickListener(activity);
			Holder.textPayOrder.setOnClickListener(activity);
            
			Holder.textCancelOrder.setTag(position);
		/*}else if(position>cancelablelist.size()-1 && position<=paramList.size()-1){
			view = View.inflate(context, R.layout.adapter_order_simple_listitem, null);
			Holder.orderId_text = (TextView) view.findViewById(R.id.orderId_text);
			Holder.orderPrice_text = (TextView) view.findViewById(R.id.orderPrice_text);
			Holder.orderTime_text = (TextView) view.findViewById(R.id.orderTime_text);
			Holder.orderState_text = (TextView)view.findViewById(R.id.orderState_text);
		}else{
			Log.i(TAG, "XXXXXXXXXXXXXXXX");
		}*/
		
		orderVo = this.paramList.get(position);
		/*if(cancelablelist.size()>0 && position<=cancelablelist.size()-1){
			orderVo = cancelablelist.get(position);
		}else if(position>cancelablelist.size()-1 && position<=paramList.size()-1){
			orderVo = uncancelablelist.get(position-cancelablelist.size());
		}else{
			Log.i(TAG, "XXXXXXXXXXXXXXXX");
		}
		Log.i(TAG, orderVo.getFlag()+"-可取消的上-"+orderVo.getStatus());
		*/
		paint(orderVo);
		Drawable d = drawables[position];
        if (d == null) {
            view.setTag(position);
            loadImage(position, orderVo.getPic());
        }
		return view;
	}
	private void paint(OrderList orderVo) {
	    String stateName=orderVo.getStatus();
	    String buttonName=context.getString(R.string.orderListLableMsgMsgOrderCancel);
	    if(stateName!=null&&stateName.equals("1003001")){
	        stateName="购物车中";
	        Holder.textCancelOrder.setText(buttonName);
	        Holder.textPayOrder.setVisibility(View.VISIBLE);
	        Holder.imo_tv_msg.setText("您的物品正等您");
            Holder.imo_tv_time.setText("去支付"); //送达时间
            Holder.imo_tv_back_msg.setText("后发货");
	    }else if(stateName!=null&&stateName.equals("1003002")){
	        stateName="已确认";
	        Holder.textCancelOrder.setText(buttonName);
	        Holder.textPayOrder.setVisibility(View.VISIBLE);
	        Holder.imo_tv_msg.setText("您的物品正等您");
            Holder.imo_tv_time.setText("去支付"); //送达时间
            Holder.imo_tv_back_msg.setText("后发货");
        }else if(stateName!=null&&stateName.equals("1003003")){
            stateName="已支付";
            Holder.textCancelOrder.setVisibility(View.GONE);
            Holder.textPayOrder.setVisibility(View.GONE);
            //Holder.textCancelOrder.setEnabled(false);
            Holder.imo_tv_msg.setText("您的物品正");
            Holder.imo_tv_time.setText("配货"); //送达时间
            Holder.imo_tv_back_msg.setText("");
        }else if(stateName!=null&&stateName.equals("1003004")){
            stateName="已发货";
            Holder.textCancelOrder.setVisibility(View.GONE);
            Holder.textPayOrder.setVisibility(View.GONE);
            Holder.imo_tv_msg.setText("您的物品正");
            Holder.imo_tv_time.setText("配送"); //送达时间
            Holder.imo_tv_back_msg.setText("即将到达");
        }else if(stateName!=null&&stateName.equals("1003005")){
            stateName="已派送";
            Holder.textCancelOrder.setVisibility(View.GONE);
            Holder.imo_tv_msg.setText("您的物品正");
            Holder.imo_tv_time.setText("配送"); //送达时间
            Holder.imo_tv_back_msg.setText("即将到达");
        }else if(stateName!=null&&stateName.equals("1003006")){
            stateName="已送达";
            Holder.textCancelOrder.setVisibility(View.GONE);
            Holder.textPayOrder.setVisibility(View.GONE);
            Holder.imo_tv_msg.setText("您的物品已经");
            Holder.imo_tv_time.setText("送达"); //送达时间
            Holder.imo_tv_back_msg.setText("待确认");
        }else if(stateName!=null&&stateName.equals("1003007")){
            stateName="已确认";
            Holder.textCancelOrder.setVisibility(View.GONE);
            Holder.textPayOrder.setVisibility(View.GONE);
            Holder.imo_tv_msg.setText("您的物品已经");
            Holder.imo_tv_time.setText("送达"); //送达时间
            Holder.imo_tv_back_msg.setText("待确认");
        }else if(stateName!=null&&stateName.equals("1003008")){
            stateName="已取消";
            Holder.textCancelOrder.setVisibility(View.GONE);
            Holder.textPayOrder.setVisibility(View.GONE);
            Holder.imo_tv_msg.setText("您的物品已经");
            Holder.imo_tv_time.setText("退出"); //送达时间
            Holder.imo_tv_back_msg.setText("战场");
        }
		Holder.orderId_text.setText("订单编号"+orderVo.getOrderid()+"");
		Holder.textPrice.setText(orderVo.getPrice()+"");
		Holder.orderPrice_text.setText("优惠 ￥0.0");
		Holder.orderTime_text.setText("下单时间:"+orderVo.getTime()+"");
		Holder.orderState_text.setText(stateName);
		
		/*if("1".equals(orderVo.getFlag()+"")){
			Holder.textCancelOrder.setText(context.getString(R.string.orderListLableMsgMsgOrderCancel));
		}*/
		//Holder.textPayOrder.setOnClickListener(this);
	}
	static class Holder{
		//订单编号
		static TextView orderId_text;
		//总价
		static TextView orderPrice_text;
		//下单时间
		static TextView orderTime_text;
		//状态
		static TextView orderState_text;
		//取消订单
		static Button textCancelOrder;
		//去支付
        static Button textPayOrder;
        
        static TextView imo_tv_time;
        static TextView imo_tv_back_msg;
        static TextView imo_tv_msg;
        static TextView textPrice;
        static ImageView imo_iv_icon;
	}
    @Override
    public void onClick(View v) {
       
    }
    
    @Override
    public void onImageLoadFinish(Integer position, Drawable drawable) {
        drawables[position] = drawable;
        
        View view = mListView.findViewWithTag(position);
        if (view != null) {
            ImageView iv = (ImageView) view.findViewById(R.id.imo_iv_icon);
            iv.setImageDrawable(drawable);
        }
    }
}
