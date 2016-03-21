package com.lianmeng.core.address.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.lianmeng.core.activity.R;
import com.lianmeng.core.address.adapter.SelectAdressAdapter;
import com.lianmeng.core.address.parser.AddressManageParser;
import com.lianmeng.core.address.vo.AddressDetail;
import com.lianmeng.core.framework.sysparser.SuccessParser;
import com.lianmeng.core.framework.sysvo.Area;
import com.lianmeng.core.framework.sysvo.RequestVo;
import com.lianmeng.core.framework.util.Logger;
import com.lianmeng.core.framework.util.SysU;
import com.lianmeng.core.login.activity.RestaurantLoginActivity;
import com.lianmeng.extand.lianmeng.product.activity.RestaurantBaseActivity;
import com.lianmeng.extand.lianmeng.product.activity.RestaurantBaseActivity.DataCallback;

/**
 * 地址选择，点击栏目返回<br>
 * data.getParcelableExtra("address") 获取数据 为AddressDetail 对象
 * 
 * @author liu
 * 
 */
public class RestaurantSelectAddressActivity extends RestaurantBaseActivity implements OnItemClickListener {

    private static final String TAG = "RestaurantSelectAddressActivity";
	private ListView addressItemlv;
	private SelectAdressAdapter mAdapter;

	//@InjectView(R.id.iv_left)
	private ImageView head_left;
	//@InjectView(R.id.mid_title)
	private TextView head_title;
	private ImageView head_right;
		
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ia_tv_del:
	      int position = addressItemlv.getPositionForView(v);
	      final AddressDetail selAddress=(AddressDetail) addressItemlv.getItemAtPosition(position);
	      String addId= String.valueOf(selAddress.getId());
	      
	      HashMap<String, String> requestDataMap = new HashMap<String, String>();
          String serviceFunction="REMOVEADDRESS";
         
          String inmapData="{\"ServiceName\":\"addressManagerService\" , \"Data\":{\"ACTION\":\""+serviceFunction+"\",\"userId\":\""+SysU.USERID+"\",\"id\":\""+addId+"\"}}";
          requestDataMap.put("JsonData", inmapData);
          RequestVo vo = new RequestVo(R.string.sysRequestServLet, this, requestDataMap, new SuccessParser());
          getDataFromServer(vo, new DataCallback<Boolean>() {

              @Override
              public void processData(Boolean paramObject, boolean paramBoolean) {
                  Logger.d(TAG, paramObject.toString());
                  Toast.makeText(RestaurantSelectAddressActivity.this, R.string.remove_succuess, Toast.LENGTH_LONG).show();
                  mAdapter.remove(selAddress);
                  addressItemlv.setAdapter(mAdapter);  
              }
          });
	      break;
		case R.id.iv_left:
			finish();
			break;
		case R.id.iv_right:
			startActivityForResult(new Intent(this, RestaurantAddAddressActivity.class), 200);
			break;
		}
	}

	@Override
	protected void findViewById() {
		addressItemlv = (ListView) findViewById(R.id.address_manage_list);
		head_left=(ImageView) findViewById(R.id.iv_left);
		head_title=(TextView) findViewById(R.id.mid_title);
		head_right=(ImageView) findViewById(R.id.iv_right);
		head_right.setImageResource(R.drawable.btn_addr_add_selector);
		head_right.setVisibility(View.VISIBLE);
		head_title.setText(getString(R.string.address_manage));
		head_left.setOnClickListener(this);
		head_right.setOnClickListener(this);
		mAdapter = new SelectAdressAdapter(RestaurantSelectAddressActivity.this);
		addressItemlv.setAdapter(mAdapter);
		
	}

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.act_address_manage);
		setTitle(R.string.select_address);
		//setHeadLeftBackgroundResource(R.drawable.new_head_normal_selector);
		//setHeadLeftText(R.string.check_out);
		//setHeadRightText(R.string.address_manage);
		//setHeadRightVisibility(View.VISIBLE);

	}

	@Override
	protected void processLogic() {
	    if(SysU.USERID==null||SysU.USERID.length()==0){
            Intent loginIntent = new Intent(this, RestaurantLoginActivity.class);
            startActivity(loginIntent);
        }else{
		HashMap<String, String> addressMap = new HashMap<String, String>();
		String inmapData="{\"ServiceName\":\"addressManagerService\" , \"Data\":{\"ACTION\":\"QRYADDRESSLIST\",\"userId\":\""+SysU.USERID+"\"}}";
		addressMap.put("JsonData", inmapData);
		RequestVo reqVo = new RequestVo(R.string.sysRequestServLet, this, addressMap, new AddressManageParser());
		getDataFromServer(reqVo, new DataCallback<List<AddressDetail>>() {

			@Override
			public void processData(List<AddressDetail> paramObject, boolean paramBoolean) {
				mAdapter.addAll(paramObject);
			}
		});
        }
	}

	@Override
	protected void setListener() {
		//addressItemlv.setOnItemClickListener(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 200 && resultCode == 200) {
		//if(data.getParcelableExtra("opr")!=null&&"addr".equals(data.getParcelableExtra("opr")))
			processLogic();
		}
	}

	/*@Override
	protected void onHeadRightButton(View v) {
		startActivityForResult(new Intent(this, RestaurantAddressManagerActivity.class), 200);
	}*/

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	    int manageType = getIntent().getIntExtra("manageType", 0);
	    if(manageType==1){
	    Intent data = new Intent();
		data.putExtra("opr", 1);
		data.putExtra("address", mAdapter.getItem(position));
		setResult(200, data);
		finish();
	    }
	}
}
