package com.lianmeng.core.address.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lianmeng.core.activity.R;
import com.lianmeng.core.address.parser.AddressManageParser;
import com.lianmeng.core.address.vo.AddressDetail;
import com.lianmeng.core.framework.dao.AreaDao;
import com.lianmeng.core.framework.sysvo.Area;
import com.lianmeng.core.framework.sysvo.RequestVo;
import com.lianmeng.core.framework.util.Logger;
import com.lianmeng.core.framework.util.SysU;
import com.lianmeng.core.login.activity.RestaurantLoginActivity;
import com.lianmeng.extand.lianmeng.product.activity.RestaurantBaseActivity;

/**
 * 
 * 地址添加、以及修改<br>
 * 返回结果为:添加成功，结果包含数据地址列表的最新信息 data.getPracelableArrayList("addressList");
 * 
 * @author liu
 * 
 */
public class RestaurantAddAddressActivity extends RestaurantBaseActivity implements OnItemSelectedListener {

	private static final String TAG = "RestaurantAddAddressActivity";

	private List<Area> allProvince;

	private ArrayAdapter<Area> mProvinceAdapter;

	private View cityLy;

	private View areaLy;

	private Spinner mProvinceSpinner;

	private Spinner mCitySpinner;

	private Spinner mAreaSpinner;

	private AreaDao areaDao;

	private ArrayAdapter<Area> mCityeAdapter;

	private ArrayAdapter<Area> mAreaAdapter;

	private TextView mNameEdit;

	private TextView mMobileEdit;

	//private TextView mTelEdit;

	private TextView mDetailEdit;

	//private TextView mZipcodeEdit;
	/** 是否是修改 */
	private boolean isEdit;
	private boolean isCityFirst = true;
	private boolean isAreaFirst = true;

	private AddressDetail address;
	
	private ImageView head_left;
	private TextView head_title;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		case R.id.btn_save_addr:// 确定
			if (TextUtils.isEmpty(mNameEdit.getText())) {
				Toast.makeText(this, getString(R.string.addressLabelNoAddressName), Toast.LENGTH_LONG).show();
				return;
			}
			if (TextUtils.isEmpty(mMobileEdit.getText())) {
				Toast.makeText(this,getString(R.string.addressLabelNoTeleName), Toast.LENGTH_LONG).show();
				return;
			}

			if (TextUtils.isEmpty(mDetailEdit.getText())) {
				Toast.makeText(this, getString(R.string.addressLabelNoAddressDetailName), Toast.LENGTH_LONG).show();
				return;
			}
			/*if (TextUtils.isEmpty(mZipcodeEdit.getText())) {
				Toast.makeText(this,getString(R.string.addressLabelNoZipName), Toast.LENGTH_LONG).show();
				return;
			}*/
			if(SysU.USERID==null||SysU.USERID.length()==0){
	            Intent loginIntent = new Intent(this, RestaurantLoginActivity.class);
	            startActivity(loginIntent);
	        }else{
			HashMap<String, String> requestDataMap = new HashMap<String, String>();

			requestDataMap.put("name", mNameEdit.getText().toString());
			requestDataMap.put("phonenumber", mMobileEdit.getText().toString());
			requestDataMap.put("fixedtel", "");
			StringBuilder builder = new StringBuilder();
			String provinceId="",cityId="",areaId="";
			Area area = (Area) mProvinceSpinner.getSelectedItem();
			builder.append(area.getId());
			provinceId=String.valueOf(area.getId());
			builder.append(",");
			area = (Area) mCitySpinner.getSelectedItem();
			cityId=String.valueOf(area.getId());
			builder.append(area.getId());
			builder.append(",");
			area = (Area) mAreaSpinner.getSelectedItem();
			areaId=String.valueOf(area.getId());
			builder.append(area.getId());
			requestDataMap.put("areaid", builder.toString());
			requestDataMap.put("areadetail", mDetailEdit.getText().toString());
			requestDataMap.put("zipcode", "");

			String editId="";
			String serviceFunction="ADDADDRESS";
			if (isEdit) {
				requestDataMap.put("id", address.getId() + "");
				editId=address.getId() + "";
				serviceFunction="MODIFYADDRESS";
			}
			String inmapData="{\"ServiceName\":\"addressManagerService\" , \"Data\":{\"ACTION\":\""+serviceFunction+"\",\"userId\":\""+SysU.USERID+"\"" +
        		",\"id\":\""+editId+"\",\"name\":\""+mNameEdit.getText().toString()+"\",\"teleNo\":\""+mMobileEdit.getText().toString()+"\",\"fixedTelNo\":\"\",\"provinceId\":\""+provinceId+"\",\"cityId\":\""+cityId+"\",\"areaId\":\""+areaId+"\"" +
        		",\"detail\":\""+mDetailEdit.getText().toString()+"\",\"zipCode\":\"\",\"isDefault\":\"0\",\"level\":\"4\",\"position\":\"1\",\"note\":\"1\"}}";
			requestDataMap.put("JsonData", inmapData);
			RequestVo vo = new RequestVo(R.string.sysRequestServLet, this, requestDataMap, new AddressManageParser());
			getDataFromServer(vo, new DataCallback<ArrayList<AddressDetail>>() {

				@Override
				public void processData(ArrayList<AddressDetail> paramObject, boolean paramBoolean) {
					Logger.d(TAG, paramObject.toString());
					Intent data = new Intent();

					Toast.makeText(RestaurantAddAddressActivity.this, isEdit ? R.string.edit_success : R.string.add_succuess,
							Toast.LENGTH_LONG).show();
					data.putParcelableArrayListExtra("addressList", paramObject);
					setResult(200, data);
					finish();
				}
			});
	        }
			break;
		/*case R.id.cancel_address_button:// 取消
			finish();
			break;*/
		case R.id.iv_left:
			finish();
			break;
		}
	}

	@Override
	protected void findViewById() {
		mProvinceSpinner = (Spinner) findViewById(R.id.edtv_dormitory_area_provice);// 省
		mCitySpinner = (Spinner) findViewById(R.id.edtv_dormitory_area_city);// 市
		mAreaSpinner = (Spinner) findViewById(R.id.edtv_which_area_ridgepole);// 区
		cityLy = findViewById(R.id.ll_dormitory_city);
		areaLy = findViewById(R.id.ll_which_ridgepole);

		mNameEdit = (TextView) findViewById(R.id.edtv_receiver_name);// 名称
		mMobileEdit = (TextView) findViewById(R.id.edtv_receiver_mobile);// 手机
		//mTelEdit = (TextView) findViewById(R.id.add_address_tel_edit);// 固定电话
		mDetailEdit = (TextView) findViewById(R.id.edtv_real_where);// 详细地址
		//mZipcodeEdit = (TextView) findViewById(R.id.add_address_zipcode_edit);// 邮编

		findViewById(R.id.btn_save_addr).setOnClickListener(this);
		
		
		head_left=(ImageView) findViewById(R.id.iv_left);
		head_title=(TextView) findViewById(R.id.mid_title);
		head_title.setText(getString(R.string.address_manage));
		head_left.setOnClickListener(this);
		
	}

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.act_add_address_restaurant);
	}

	@Override
	protected void processLogic() {

		address = getIntent().getParcelableExtra("address");
		if (address != null) {
			isEdit = true;
			setTitle(getString(R.string.addressButtonAddressModifyName));
			Logger.d("TAG", address.toString());
		} else {
			setTitle(getString(R.string.addressButtonAddressAddName));
		}

		areaDao = new AreaDao(this);
		allProvince = areaDao.getAllProvince();

		int provinceSelectId = -1;
		int citySelectId = -1;
		int areaSelectId = -1;
		if (isEdit) { // 修改回显数据
			provinceSelectId = address.getProvinceid();
			citySelectId = address.getCityid();
			areaSelectId = address.getAreaid();
			mNameEdit.setText(address.getName());
			mMobileEdit.setText(address.getPhonenumber());
			//mTelEdit.setText(address.getFixedtel());
			mDetailEdit.setText(address.getAreadetail());
			//mZipcodeEdit.setText(address.getZipcode());
		}

		updateProvince(allProvince);
		int areaId;
		Area area;
		if (isEdit) {
			area = selectedSpinner(allProvince, provinceSelectId, mProvinceSpinner);
			areaId = address.getProvinceid();
		} else {
			area = allProvince.get(0);
			areaId = area.getId();
		}
		List<Area> citys = areaDao.findBySuperCode(areaId);
		updateCity(citys);
		area.setSub_area(citys);

		if (isEdit) {
			area = selectedSpinner(citys, citySelectId, mCitySpinner);
			areaId = area.getId();
		} else {
			area = citys.get(0);
		}

		List<Area> areas = areaDao.findBySuperCode(area.getId());
		updateArea(areas);
		if (isEdit)
			selectedSpinner(areas, areaSelectId, mAreaSpinner);
		if (areas != null && areas.size() > 0) {
			area.setSub_area(areas);
		}

	}

	public void updateProvince(List<Area> areas) {
		mProvinceAdapter = new ArrayAdapter<Area>(context,R.layout.abc_simple_spinner_item, areas);
		mProvinceAdapter.setDropDownViewResource(R.layout.abc_simple_dropdown_hint);
		mProvinceSpinner.setAdapter(mProvinceAdapter);

	}

	public void updateCity(List<Area> areas) {
		if (areas != null && areas.size() > 0) {
			mCityeAdapter = new ArrayAdapter<Area>(context, R.layout.abc_simple_spinner_item, areas);
			mCityeAdapter.setDropDownViewResource(R.layout.abc_simple_dropdown_hint);
			mCitySpinner.setAdapter(mCityeAdapter);
			cityLy.setVisibility(View.VISIBLE);
		} else
			cityLy.setVisibility(View.GONE);
	}

	public void updateArea(List<Area> areas) {
		if (areas != null && areas.size() > 0) {
			mAreaAdapter = new ArrayAdapter<Area>(context, R.layout.abc_simple_spinner_item, areas);
			mAreaAdapter.setDropDownViewResource(R.layout.abc_simple_dropdown_hint);
			mAreaSpinner.setAdapter(mAreaAdapter);
			areaLy.setVisibility(View.VISIBLE);
		} else
			areaLy.setVisibility(View.GONE);
	}

	private Area selectedSpinner(List<Area> areas, int select, Spinner spinner) {

		if (select != -1) {
			int i = 0;
			for (Area area : areas) {
				if (area.getId() == select) {
					spinner.setSelection(i);
					Logger.d(TAG, "select " + select);
					return area;
				}
				i++;
			}
		}
		return null;
	}

	@Override
	protected void setListener() {
		mProvinceSpinner.setOnItemSelectedListener(this);
		mCitySpinner.setOnItemSelectedListener(this);
		mAreaSpinner.setOnItemSelectedListener(this);
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		Logger.d(TAG, position + " ");
 		Area item;
		List<Area> sub_area;
		switch (parent.getId()) {
		case R.id.edtv_dormitory_area_provice:
			if (isEdit && isCityFirst) {
				isCityFirst = false; 
				return;
			}
			item = (Area) mProvinceAdapter.getItem(position);
			sub_area = item.getSub_area();
			if (sub_area == null) {
				sub_area = areaDao.findBySuperCode(item.getId());
			}
			updateCity(sub_area);
			break;
		case R.id.edtv_dormitory_area_city:
			if (isEdit && isAreaFirst) {
				isAreaFirst = false; 
				return;
			}
			
			item = (Area) mCityeAdapter.getItem(position);
			sub_area = item.getSub_area();
			if (sub_area == null) {
				sub_area = areaDao.findBySuperCode(item.getId());
			}
			updateArea(sub_area);
			break;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		Logger.d(TAG, "onResume()");
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}

}
