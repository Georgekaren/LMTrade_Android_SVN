package com.lianmeng.extand.lianmeng.help.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.lianmeng.core.activity.R;
import com.lianmeng.core.activity.R.id;
import com.lianmeng.core.activity.R.layout;
import com.lianmeng.core.activity.R.string;
import com.lianmeng.core.framework.sysactivity.BaseWapperActivity;
import com.lianmeng.core.framework.sysvo.RequestVo;
import com.lianmeng.core.framework.util.Constant;
import com.lianmeng.extand.lianmeng.help.adapter.HelpListViewAdapter;
import com.lianmeng.extand.lianmeng.help.parser.HelpParser;
import com.lianmeng.extand.lianmeng.help.vo.HelpVo;

public class HelpActivity extends BaseWapperActivity {
 	private ListView lv_help_info;
	Map<String,Object> helpMap;
	@Override
	public void onClick(View v) {
		 
	}

	@Override
	protected void findViewById() {
 		lv_help_info = (ListView) findViewById(R.id.listHelpInfo);
	}

	@Override
	protected void loadViewLayout() {
		this.setContentView(R.layout.help_activity);
		setTitle(getString(R.string.helpMsgHelpCenterMsg));
		selectedBottomTab(Constant.MORE);
	}

	@Override
	protected void processLogic() {
		RequestVo requestVo = new RequestVo();
		//requestVo.requestUrl = R.string.help;// 待调试
		requestVo.context = context;
		requestVo.jsonParser = new HelpParser();
		super.getDataFromServer(requestVo, new DataCallback<Map<String,Object>>() {

			@Override
			public void processData(Map<String,Object> paramObject,
					boolean paramBoolean) {
				if(paramObject!=null){
					HelpListViewAdapter adapter = new HelpListViewAdapter(context, paramObject, lv_help_info);
					lv_help_info.setAdapter(adapter);
					helpMap = paramObject;
				}
			}
		});

	}

	@Override
	protected void setListener() {
 		lv_help_info.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				List helpList = (ArrayList) helpMap.get("help");
				HelpVo helpVo = (HelpVo)helpList.get(position);
				String detail_url = helpVo.getDetail_url();
				Intent intent = new Intent(context,HelpDetailActivity.class);
				intent.putExtra("detail_url", detail_url);
				startActivity(intent);
				
				
			}
		});
		
	}

}

