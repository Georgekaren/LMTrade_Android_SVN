package com.lianmeng.core.framework.sysactivity;

import java.util.List;
import java.util.Vector;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lianmeng.core.activity.R;
import com.lianmeng.core.framework.application.ECApplication;
import com.lianmeng.core.framework.service.IECManager; 
import com.lianmeng.core.framework.sysvo.RequestVo;
import com.lianmeng.core.framework.util.CommonUtil;
import com.lianmeng.core.framework.util.Constant;
import com.lianmeng.core.framework.util.Logger;
import com.lianmeng.core.framework.util.NetUtil;
import com.lianmeng.core.framework.util.NetUtil.Status;
import com.lianmeng.core.framework.util.ThreadPoolManager;
import com.lianmeng.core.login.activity.LoginActivity;

public abstract class BaseWapperNewActivity extends BaseActivity{
	private static final String TAG = "BaseWapperActivity";

	private final static Integer DEFAULT_INDEX = 1;
	
	/** 登录请求码 */
	public static final int NOT_LOGIN = 403;
	
	/** 登录结果码*/
	public static final int LOGIN_SUCCESS = 10000000;
	
	protected int activityCase;
	private ImageView classify;
	private LinearLayout home;
	private ImageView more;
	protected ProgressDialog progressDialog;
	private ImageView search;
	private ImageView shopCar;
	protected TextView textShopCarNum;
	protected Context context;
	private ThreadPoolManager threadPoolManager;
	// private ECApplication application;
	private LinearLayout layout_content;

	/** ContentView */
	private View inflate;
 
	private ButtomClick buttomClick;
	private ECApplication application;

	private List<BaseTask> record = new Vector<BaseTask>();

	public BaseWapperNewActivity() {
		threadPoolManager = ThreadPoolManager.getInstance();
	}

	/**
	 * Android生命周期回调方法-创建
	 */
	@Override
	public void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		application = (ECApplication) getApplication();
		application.addActvity(this);
		 
		context = getApplicationContext();
 		initView();
 	}
	 
	
	public IECManager getECManager() {
		return application.getEcManager();
	}

	 

	 

	/**
	 * 
	 */
	private void initView() {
		 
		loadViewLayout();
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
				if (msg.obj == null&&callBack==null) {
					CommonUtil.showInfoDialog(context, getString(R.string.net_error));
				} else { 
					//callBack.processData(msg.obj, true);
				}
			} else if (msg.what == Constant.NET_FAILED) {
				CommonUtil.showInfoDialog(context, getString(R.string.net_error));
			}
			
			Logger.d(TAG, "recordSize:" + record.size());
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
			Message msg = Message.obtain();
			try {
				if (NetUtil.hasNetwork(context)) {
					obj = NetUtil.post(reqVo);
					if (obj instanceof Status) {
						Intent intent = new Intent(BaseWapperNewActivity.this, LoginActivity.class);
						intent.putExtra("notlogin", "notlogin");
						startActivityForResult(intent, NOT_LOGIN);
					} else {
						msg.what = Constant.SUCCESS;
						msg.obj = obj;
						handler.sendMessage(msg);
						record.remove(this);
					}
				} else {
					msg.what = Constant.NET_FAILED;
					msg.obj = obj;
					handler.sendMessage(msg);
					record.remove(this);
				}
			} catch (Exception e) {
				record.remove(this);
				throw new RuntimeException(e);
			}
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == NOT_LOGIN) {
			if (resultCode == LOGIN_SUCCESS) {
				int size = record.size();
				if (size > 0) {
					for (int i = 0; i < size; i ++) {
						threadPoolManager.addTask(record.get(0));
					}
				}
			} else {
				finish();
			}
		}
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
		record.add(taskThread);
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

	private class ButtomClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			/*case R.id.head_left:
				onHeadLeftButton(v);
				break;
			case R.id.head_right:
				onHeadRightButton(v);
				break;*/
			}
		}
	}

	protected void onHeadLeftButton(View v) {
		finish();
	}

	protected void onHeadRightButton(View v) {

	}

	 

	@Override
	protected void onDestroy() {
		super.onDestroy();
		application.removeActvity(this);
		record.clear();
		record = null;
		classify = null;
		home = null;
		more = null;
		search = null;
		shopCar = null;
		textShopCarNum = null;
		context = null;
		threadPoolManager = null;
		layout_content = null;
		inflate = null;
		 
		buttomClick = null;
		if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
		}
		application = null;
	}
}
