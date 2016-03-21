package com.lianmeng.extand.lianmeng.product.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.lianmeng.core.activity.R;
import com.lianmeng.core.framework.sysvo.RequestVo;
import com.lianmeng.core.framework.util.CommonUtil;
import com.lianmeng.core.framework.util.Constant;
import com.lianmeng.core.framework.util.NetUtil;
import com.lianmeng.core.framework.util.ThreadPoolManager;

public abstract class RestaurantBaseActivity extends Activity implements View.OnClickListener {
	private final static Integer DEFAULT_INDEX = 0;
	protected int activityCase;
	//private ImageView classify;
	//private ImageView home;
	//private ImageView more;
	protected ProgressDialog progressDialog;
	//private ImageView search;
	//private ImageView shopCar;
	//rotected TextView textShopCarNum;
	protected Context context;
	private ThreadPoolManager threadPoolManager;

	public RestaurantBaseActivity() {
		threadPoolManager = ThreadPoolManager.getInstance();
	}

	/**
	 * Android生命周期回调方法-创建
	 */
	@Override
	public void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		context = getApplicationContext();
		initView();
	}

	/**
	 * 
	 */
	private void initView() {
		loadViewLayout();
		if (isLoadBottomTab()) {
			loadBottomTab();
			selectedBottomTab(DEFAULT_INDEX);
		}
		findViewById();
		setListener();
		processLogic();
	}

	/**
	 * 
	 * @author Mathew
	 * 
	 */
	@SuppressWarnings("unchecked")
	class BaseHandler extends Handler {
		private Context context;
		private DataCallback callBack;
		private RequestVo reqVo;

		public BaseHandler(Context context, DataCallback callBack, RequestVo reqVo) {
			this.context = context;
			this.callBack = callBack;
			this.reqVo = reqVo;
		}

		public void handleMessage(Message msg) {
			closeProgressDialog();
			if (msg.what == Constant.SUCCESS) {
				if (msg.obj == null) {
					CommonUtil.showInfoDialog(context, getString(R.string.net_error));
				} else {
					callBack.processData(msg.obj, true);
				}
			} else if (msg.what == Constant.NET_FAILED) {
				CommonUtil.showInfoDialog(context, getString(R.string.net_error));
			}
		}
	}

	class BaseTask implements Runnable {
		private Context context;
		private RequestVo reqVo;
		private Handler handler;

		public BaseTask(Context context, RequestVo reqVo, Handler handler) {
			this.context = context;
			this.reqVo = reqVo;
			this.handler = handler;
		}

		@Override
		public void run() {
			Object obj = null;
			Message msg = new Message();
			if (NetUtil.hasNetwork(context)) {
				obj = NetUtil.post(reqVo);
				msg.what = Constant.SUCCESS;
				msg.obj = obj;
				handler.sendMessage(msg);
			} else {
				msg.what = Constant.NET_FAILED;
				msg.obj = obj;
				handler.sendMessage(msg);
			}
		}

	}

	/**
	 * 
	 */
	private void loadBottomTab() {
		
	}

	/**
	 * tab条图片切换
	 * 
	 * @param paramViewId
	 */
	protected void selectedBottomTab(int paramViewId) {
		
	}

	/**
	 * 是否加载底部tab
	 * 
	 * @return
	 */
	protected Boolean isLoadBottomTab() {
		return true;
	}

	public abstract interface DataCallback<T> {
		public abstract void processData(T paramObject, boolean paramBoolean);
	}

	/**
	 * 从服务器上获取数据，并回调处理
	 * 
	 * @param reqVo
	 * @param callBack
	 */
	protected void getDataFromServer(RequestVo reqVo, DataCallback callBack) {
		showProgressDialog();
		BaseHandler handler = new BaseHandler(this, callBack, reqVo);
		BaseTask taskThread = new BaseTask(this, reqVo, handler);
		this.threadPoolManager.addTask(taskThread);
	}

	/**
	 * 显示提示框
	 */
	protected void showProgressDialog() {
		if ((!isFinishing()) && (this.progressDialog == null)) {
			this.progressDialog = new ProgressDialog(this);
		}
		this.progressDialog.setTitle(getString(R.string.loadTitle));
		this.progressDialog.setMessage(getString(R.string.LoadContent));
		this.progressDialog.show();
	}

	/**
	 * 关闭提示框
	 */
	protected void closeProgressDialog() {
		if (this.progressDialog != null)
			this.progressDialog.dismiss();
	}

	/**
	 * 
	 */
	protected abstract void findViewById();

	/**
	 * 
	 */
	protected abstract void loadViewLayout();

	/**
	 * 向后台请求数据
	 */
	protected abstract void processLogic();

	/**
	 * 
	 */
	protected abstract void setListener();

}
