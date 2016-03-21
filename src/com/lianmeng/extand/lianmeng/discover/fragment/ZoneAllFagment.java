package com.lianmeng.extand.lianmeng.discover.fragment;


import java.util.HashMap;
import java.util.LinkedList;

import com.lianmeng.core.activity.R;
import com.lianmeng.core.framework.util.SysU;
import com.lianmeng.extand.lianmeng.discover.activity.PostInfoActivity;
import com.lianmeng.extand.lianmeng.discover.adapter.ZoneAllAdapter;
import com.lianmeng.extand.lianmeng.discover.json.ZoneAllParser.ZoneResult;
import com.lianmeng.extand.lianmeng.discover.net.ActionOfUrl.JsonAction;
import com.lianmeng.extand.lianmeng.discover.net.NetAsyncTask;
import com.lianmeng.extand.lianmeng.discover.refresh_list.RefreshListView;
import com.lianmeng.extand.lianmeng.discover.refresh_list.RefreshListView.IHListViewListener;
import com.lianmeng.extand.lianmeng.discover.utils.AppToast;
import com.lianmeng.extand.lianmeng.discover.utils.HProgress;
import com.lianmeng.extand.lianmeng.discover.utils.ImageFetcher;
import com.lianmeng.extand.lianmeng.model.ZoneAllItem;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import butterknife.ButterKnife;
import butterknife.InjectView;



public class ZoneAllFagment extends Fragment implements OnClickListener,IHListViewListener{

	@InjectView(R.id.list_zone_topic) RefreshListView mListView;
	private ZoneAllAdapter mAdapter;
	private LinearLayout mLayoutNotice;
	private LinearLayout mLayoutGood;
	private LinearLayout mLayoutHot;
	private LinearLayout mLayoutScore;
	private ImageFetcher mImageFetcher;
	private HashMap inHashMapParam;
	private Context context;
	private LinkedList<ZoneAllItem> mListInfos=new LinkedList<ZoneAllItem>();
	private int currentPage = 1;
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case R.id.ui_show_dialog:
				if(getActivity()!=null)
				HProgress.show(getActivity(), null);
				break;
			case R.id.ui_dismiss_dialog:
				HProgress.dismiss();
				break;
			case R.id.ui_show_text://网络超时和数据解析异常
				if(getActivity()!=null)
					AppToast.showShortText(getActivity(), msg.arg1);
				break;
			case R.id.ui_update_ui:
				break;
			}
		}
	};
	public static ZoneAllFagment newInstance(Context context) {
		ZoneAllFagment fragment = new ZoneAllFagment();
		fragment.context=context;
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
       // context=context;
        return fragment;
    }
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View contentView = inflater.inflate(R.layout.act_discover_fram_view_zone_all, container, false);
		ButterKnife.inject(this, contentView);
		addHeadView(inflater);
		mImageFetcher = new ImageFetcher(getActivity(), 300);
		mImageFetcher.setLoadingImage(R.drawable.zone_list_profile);
		mAdapter = new ZoneAllAdapter(getActivity(),mListInfos,mImageFetcher,mListView);
		mListView.setAdapter(mAdapter);
		mListView.setPullLoadEnable(true);	
		mListView.setHListViewListener(this);
		AddItemToContainer(currentPage, 1);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				if(position<=1)return;
				ZoneAllItem info=mAdapter.mListInfo.get(position-2);
				Intent intent = new Intent(getActivity(),PostInfoActivity.class);
				Bundle mBundle = new Bundle();    
			    mBundle.putSerializable("zoneAll",info);    
			    intent.putExtras(mBundle);
			    startActivity(intent);
			}
		});
		return contentView;
	}
	private void addHeadView(LayoutInflater inflater) {
		View headView = inflater.inflate(R.layout.act_discover_view_zone_topic_head,null);
		mLayoutNotice=(LinearLayout)headView.findViewById(R.id.layout_notice);
		mLayoutGood=(LinearLayout)headView.findViewById(R.id.layout_good);
		mLayoutHot=(LinearLayout)headView.findViewById(R.id.layout_hot);
		mLayoutScore=(LinearLayout)headView.findViewById(R.id.layout_score);
		mLayoutNotice.setOnClickListener(this);
		mLayoutGood.setOnClickListener(this);
		mLayoutHot.setOnClickListener(this);
		mLayoutScore.setOnClickListener(this);
		mListView.addHeaderView(headView);
	}
	private void parseArgument() {
		Bundle bundle = getArguments();		
	}
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		currentPage=1;
		AddItemToContainer(currentPage, 1);
	}
	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		AddItemToContainer(++currentPage, 2);
	}
	public class NetCotnent extends NetAsyncTask{

		private ZoneResult result;
		private int mType = 1;
		public NetCotnent(Handler uiHandler,int type) {
			super(uiHandler);
			this.mType=type;
			if(mListInfos.size()==0)
				setDialogId(1);
		}
		@Override
		protected void handlePreExecute() {
			// TODO Auto-generated method stub
			
		}

		@Override
		protected String handleNetworkProcess(String... arg0) throws Exception {
			// TODO Auto-generated method stub
			result=(ZoneResult) httptask.getRequestbyPOSTRest(JsonAction.ZONEALL,arg0[0], inHashMapParam);
			
			return result!=null? HANDLE_SUCCESS : HANDLE_FAILED;
		}

		@Override
		protected void handleResult() {
			// TODO Auto-generated method stub
			if (result!=null&&result.isSuccess()) {
				mListInfos=result.getListInfo();				
				mListView.stopLoadMore();
				mListView.stopRefresh();
				if (mType == 1&&mListInfos.size()>0) {
					mAdapter.mListInfo=mListInfos;
					mAdapter.notifyDataSetChanged();
				} else if (mType == 2&&mListInfos.size()>0) {
					mAdapter.addItemLast(mListInfos);
					mAdapter.notifyDataSetChanged();
					mListView.setSelection(mListView.getLastVisiblePosition());
				}
			}
			 	            
		}
		
	}
	private void AddItemToContainer(int pageindex, int type) {
		String url = "album/1733789/masn/p/" + pageindex + "/24/";
		//
		if(context!=null){
		url=context.getString(R.string.sysRequestHost)+context.getString(R.string.sysRequestServLet);
		}
		inHashMapParam=new HashMap();
		inHashMapParam.put("JsonData", "{\"ServiceName\":\"discoverManagerService\" , \"Data\":{\"ACTION\":\"QRYDISCOVERHOME\",\"id\":\""+SysU.USERID+"\"}}");
		new NetCotnent(mHandler,type).execute(url);
	}
	@Override
	public void onResume() {
		super.onResume();
		mImageFetcher.setExitTasksEarly(false);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {
		case R.id.layout_notice:
			
			break;
		case R.id.layout_good:
			
			break;
		case R.id.layout_hot:
			
			break;
		case R.id.layout_score:
			
			break;
		}
	}
}
