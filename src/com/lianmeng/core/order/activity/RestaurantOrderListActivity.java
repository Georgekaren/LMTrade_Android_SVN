package com.lianmeng.core.order.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.lianmeng.core.activity.R;
import com.lianmeng.core.framework.sysvo.PageVo;
import com.lianmeng.core.framework.sysvo.RequestVo;
import com.lianmeng.core.framework.util.Constant;
import com.lianmeng.core.framework.util.SysU;
import com.lianmeng.core.login.activity.RestaurantLoginActivity;
import com.lianmeng.core.order.adapter.OrderListLvAdapter;
import com.lianmeng.core.order.parser.OrderListParser;
import com.lianmeng.core.order.vo.OrderList;
import com.lianmeng.core.pay.activity.RestaurantPaymentCenterActivity;
import com.lianmeng.extand.lianmeng.product.activity.RestaurantBaseActivity;

public class RestaurantOrderListActivity extends RestaurantBaseActivity implements OnItemClickListener {
	private static final String TAG = "RestaurantOrderListActivity";
	private ListView my_order_list;
	private TextView my_order_month;
	private TextView my_order_all;
	private TextView my_order_notsend;
	
	private ImageView my_order_month_cursor;
	private ImageView my_order_all_cursor;
	private ImageView my_order_notsend_cursor;
	private int flag = 0;
	private View pageView;

	private List<OrderList> orderlist = new ArrayList<OrderList>();
	private List<OrderList> orderlistInMonth = new ArrayList<OrderList>();
	private List<OrderList> ordercanceled = new ArrayList<OrderList>();
	private List<OrderList> cancelablelist;
	private List<OrderList> uncancelablelist;
	private OrderListLvAdapter adapter;
	private PageVo pageVo;
	TextView previousPage ;
	TextView nextPage ;
	TextView textPage ;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.textCancelOrder:
			int position = my_order_list.getPositionForView(v);
			//TextView cancelorder = (TextView) my_order_list.findViewWithTag(position);
			OrderList order = (OrderList) my_order_list.getItemAtPosition(position);
			//Toast.makeText(context, getString(R.string.orderListMsgCancelOrderNo)+order.getOrderid(), 0).show();
			//processCancelOrder(order.getOrderid());
			cancelProcessLogic(order.getOrderid());
			break;
		case R.id.btn_pay:
            int position_sel = my_order_list.getPositionForView(v);
            OrderList order_sel = (OrderList) my_order_list.getItemAtPosition(position_sel);
            String orderNo=order_sel.getOrderid();
            Intent intent = new Intent(RestaurantOrderListActivity.this, RestaurantPaymentCenterActivity.class);
            intent.putExtra("orderNo", orderNo);
            startActivity(intent);
            break;	
		case R.id.my_order_month:
			flag = 0;
			my_order_month.setTextColor(getResources().getColor(R.color.my_ordr_tab_check));
			my_order_all.setTextColor(getResources().getColor(R.color.my_ordr_tab_no_check));
			my_order_notsend.setTextColor(getResources().getColor(R.color.my_ordr_tab_no_check));
			my_order_month_cursor.setVisibility(View.VISIBLE); 
			my_order_all_cursor.setVisibility(View.INVISIBLE);
			my_order_notsend_cursor.setVisibility(View.INVISIBLE);
			processLogic();
			break;
		case R.id.my_order_all: //待收货
			flag = 1;
			my_order_month.setTextColor(getResources().getColor(R.color.my_ordr_tab_no_check));
			my_order_all.setTextColor(getResources().getColor(R.color.my_ordr_tab_check));
			my_order_notsend.setTextColor(getResources().getColor(R.color.my_ordr_tab_no_check));
			my_order_month_cursor.setVisibility(View.INVISIBLE); 
			my_order_all_cursor.setVisibility(View.VISIBLE);
			my_order_notsend_cursor.setVisibility(View.INVISIBLE);
			processLogic();
			break;
		case R.id.my_order_notsend: //待支付
			flag = 2;
			my_order_month.setTextColor(getResources().getColor(R.color.my_ordr_tab_no_check));
			my_order_all.setTextColor(getResources().getColor(R.color.my_ordr_tab_no_check));
			my_order_notsend.setTextColor(getResources().getColor(R.color.my_ordr_tab_check));
			my_order_month_cursor.setVisibility(View.INVISIBLE); 
			my_order_all_cursor.setVisibility(View.INVISIBLE);
			my_order_notsend_cursor.setVisibility(View.VISIBLE);
			processLogic();
			break;
		case R.id.my_page_previous:
			Log.i(TAG, "显示前一页------1");
			Log.i(TAG,pageVo.wantedPageNum+"");
			if(pageVo.wantedPageNum<=1){
				previousPage.setClickable(false);
				return;
			}
			processLogic();
			pageVo.wantedPageNum -= 1;
			Log.i(TAG, "显示前一页------2");
			Log.i(TAG,pageVo.wantedPageNum+"");
			textPage.setText(pageVo.wantedPageNum+"/"+pageVo.totalPageNum);
			break;
		case R.id.my_page_next:
			Log.i(TAG, "显示下一页-------1");
			Log.i(TAG,pageVo.wantedPageNum+"");
			if(pageVo.wantedPageNum>=pageVo.totalPageNum){
				nextPage.setClickable(false);
				return;
			}
			processLogic();
			pageVo.wantedPageNum += 1;
			Log.i(TAG, "显示下一页-------2");
			Log.i(TAG,pageVo.wantedPageNum+"");
			textPage.setText(pageVo.wantedPageNum+"/"+pageVo.totalPageNum);
			break;
		}
	}

	private void processCancelOrder(String orderid) {
		RequestVo vo = new RequestVo();
		vo.context = this;
		
	}

	@Override
	protected void findViewById() {
		my_order_list = (ListView) this.findViewById(R.id.my_order_list);
		my_order_month = (TextView) this.findViewById(R.id.my_order_month);
		my_order_all = (TextView) this.findViewById(R.id.my_order_all);
		my_order_notsend = (TextView) this.findViewById(R.id.my_order_notsend);
		
		my_order_month_cursor = (ImageView) this.findViewById(R.id.my_order_month_cursor);
		my_order_all_cursor = (ImageView) this.findViewById(R.id.my_order_all_cursor);
		my_order_notsend_cursor = (ImageView) this.findViewById(R.id.my_order_notsend_cursor);
	}

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.act_order);
		//setHeadLeftVisibility(View.VISIBLE);
		setTitle(R.string.my_ordr_title);
		//setHeadBackgroundResource(R.drawable.head_bg);
		selectedBottomTab(Constant.HOME);
		int totoalOrderCount = getIntent().getIntExtra("totoalOrderCount",0);
		Log.i(TAG, totoalOrderCount+"");
		pageVo = new PageVo(0, totoalOrderCount, 1);
	}

	@Override
	protected void processLogic() {
	    if(SysU.USERID==null||SysU.USERID.length()==0){
            Intent loginIntent = new Intent(this, RestaurantLoginActivity.class);
            startActivity(loginIntent);
        }else{
		RequestVo vo = new RequestVo();
		vo.context = this;
		vo.requestDataMap = new HashMap<String, String>();
		// TODO listview内容的分页处理
		vo.requestDataMap.put("type", flag + 1 + "");
		vo.requestDataMap.put("page", pageVo.wantedPageNum+"");
		vo.requestDataMap.put("pageNum",pageVo.pageLenth+"");

		vo.jsonParser = new OrderListParser();
		vo.requestUrl = R.string.sysRequestServLet;
		String inmapData="{\"ServiceName\":\"srvOrderManagerService\" , \"Data\":{\"ACTION\":\"QRYHASORDER\",\"userId\":\""+SysU.USERID+"\",\"state\":\""+flag+"\"}}";
		HashMap<String, String> prodMap = new HashMap<String, String>();
		prodMap.put("JsonData", inmapData);
		vo.requestDataMap = prodMap;
		Log.i(TAG, inmapData+"");
		getDataFromServer(vo, new DataCallback<List<OrderList>>() {
			@Override
			public void processData(List<OrderList> paramObject,
					boolean paramBoolean) {
				if (paramObject != null) {
				    orderlist=paramObject;
					//divide(paramObject);
					/*if(pageView!=null){
						my_order_list.removeFooterView(pageView);
					}*/
					
					adapter = new OrderListLvAdapter(
	                    RestaurantOrderListActivity.this,my_order_list, orderlist);
					my_order_list.setAdapter(adapter);
					//setAdapterForDivide();
				}
			}
		});
        }
	}
	
	
	protected void cancelProcessLogic(String orderNo) {
        if(SysU.USERID==null||SysU.USERID.length()==0){
            Intent loginIntent = new Intent(this, RestaurantLoginActivity.class);
            startActivity(loginIntent);
        }else{
        RequestVo vo = new RequestVo();
        vo.context = this;
        vo.requestDataMap = new HashMap<String, String>();
        vo.jsonParser = new OrderListParser();
        vo.requestUrl = R.string.sysRequestServLet;
        String inmapData="{\"ServiceName\":\"srvOrderManagerService\" , \"Data\":{\"ACTION\":\"MODIFYSTATEANDQRYHASORDER\",\"userId\":\""+SysU.USERID+"\",\"orderNo\":\""+orderNo+"\",\"state\":\""+flag+"\",\"inputstate\":\"1003008\"}}";
        HashMap<String, String> prodMap = new HashMap<String, String>();
        prodMap.put("JsonData", inmapData);
        vo.requestDataMap = prodMap;
        Log.i(TAG, inmapData+"");
        getDataFromServer(vo, new DataCallback<List<OrderList>>() {
            @Override
            public void processData(List<OrderList> paramObject,
                    boolean paramBoolean) {
                if (paramObject != null) {
                    orderlist=paramObject;
                    adapter = new OrderListLvAdapter(
                        RestaurantOrderListActivity.this,my_order_list, orderlist);
                    my_order_list.setAdapter(adapter);
                }
            }
        });
        }
    }
	/*private void setAdapterForDivide() {
		switch (flag) {
		case 0:
			cancelableInit(orderlistInMonth);
			adapter = new OrderListLvAdapter(
					RestaurantOrderListActivity.this, orderlistInMonth,cancelablelist,cancelablelist);
			
			//分页处理
			if(orderlistInMonth.size()>=pageVo.pageLenth){
				showPageBar();
			}
			my_order_list.setAdapter(adapter);
			break;
		case 1:
			cancelableInit(orderlist);
			adapter = new OrderListLvAdapter(
					RestaurantOrderListActivity.this, orderlist,cancelablelist,uncancelablelist);
			//分页处理
			if(orderlist.size()>=pageVo.pageLenth){
				showPageBar();
			}
			my_order_list.setAdapter(adapter);
			break;
		case 2:
			adapter = new OrderListLvAdapter(
					RestaurantOrderListActivity.this, ordercanceled,cancelablelist,uncancelablelist);
			//分页处理
			if(ordercanceled.size()==pageVo.pageLenth){
				showPageBar();
			}
			my_order_list.setAdapter(adapter);
			break;
		}
	}*/

	private void cancelableInit(List<OrderList> list) {
		cancelablelist = new ArrayList<OrderList>();
		uncancelablelist = new ArrayList<OrderList>();
		//对订单是否可以取消做不同的处理
		for (OrderList item : list) {
			if(item.getFlag()==1){
				cancelablelist.add(item);
				Log.i(TAG, item.getFlag()+"-可取消的上-"+item.getStatus());
			}else{
				uncancelablelist.add(item);
				Log.i(TAG, item.getFlag()+"-不 可取消的上-"+item.getStatus());
			}
		}
		Log.i(TAG, "cancelablelist可以取消的数目为:"+cancelablelist.size());
		Log.i(TAG, "uncancelablelist不可以取消的数目为:"+uncancelablelist.size());
		
	}

	private void showPageBar() {
		pageView = View.inflate(RestaurantOrderListActivity.this, R.layout.act_order_item_view_page, null);
		previousPage = (TextView) pageView.findViewById(R.id.my_page_previous);
	    nextPage = (TextView) pageView.findViewById(R.id.my_page_next);
        textPage = (TextView) pageView.findViewById(R.id.my_page_text);
		previousPage.setOnClickListener(this);
		nextPage.setOnClickListener(this);
		textPage.setText(pageVo.wantedPageNum+"/"+pageVo.totalPageNum);
		
		my_order_list.addFooterView(pageView);
	}
	protected void divide(List<OrderList> paramObject) {
		this.orderlist = paramObject;
		for (OrderList item : orderlist) {
			// 将已取消的条目筛选出来
			if (getString(R.string.orderListMsgOrderStateCancel).equals(item.getStatus())) {
				ordercanceled.add(item);
			}
			// 将订单中近一个月内的订单放到orderlistInMonth中
			try {
				Calendar now = Calendar.getInstance();
				SimpleDateFormat format = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				Date date = format.parse(item.getTime());
				Calendar ordertime = Calendar.getInstance();
				ordertime.setTime(date);
				ordertime.add(Calendar.MONTH, 1);
				if (ordertime.compareTo(now) >= 0) {
					orderlistInMonth.add(item);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void setListener() {
		my_order_month.setOnClickListener(this);
		my_order_all.setOnClickListener(this);
		my_order_notsend.setOnClickListener(this);
		my_order_list.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if(pageView!=null&&view.equals(pageView)){
			view.setClickable(false);
			return;
		}
		TextView text = (TextView) view.findViewById(R.id.orderId_text);
		String orderId = text.getText().toString().trim();
		Log.i(TAG, "你点击了订单号"+orderId+"的订单，跳转订单详情页面");
		//Intent orderDetailIntent = new Intent(context,OrderDetailActivity.class);
		//orderDetailIntent.putExtra("orderId", orderId);
		//startActivity(orderDetailIntent);
	
	}
}
