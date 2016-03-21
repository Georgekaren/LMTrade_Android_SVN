package com.lianmeng.core.address.adapter;

import java.util.Collection;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.lianmeng.core.activity.R;
import com.lianmeng.core.address.activity.RestaurantSelectAddressActivity;
import com.lianmeng.core.address.vo.AddressDetail;
import com.lianmeng.core.framework.dao.AreaDao;
import com.lianmeng.core.framework.sysadapter.ArrayWapperAdapter;
import com.lianmeng.core.framework.sysvo.Area;

/**
 * 选择地址
 * @author liu
 *
 */
public class SelectAdressAdapter extends ArrayWapperAdapter<AddressDetail> implements OnClickListener {
	private LayoutInflater inflater;
	private AreaDao areaDao;
	private String[] areaDetail;
	private Context inContext;
	private RestaurantSelectAddressActivity activity;
	public static class AddressManageViewHoler {
		TextView receiver;
		TextView phone;
		TextView ads;
		ImageView img;
		TextView ia_tv_del;
	}

	public SelectAdressAdapter(Context context) {
		super(context);
		inContext=context;
		this.activity = (RestaurantSelectAddressActivity)context;
		areaDao = new AreaDao(context);
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 	}

	@Override
	public void addAll(Collection<? extends AddressDetail> collection) {
		clear();
		super.addAll(collection);
		areaDetail = new String[collection.size()];
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		AddressManageViewHoler holer;
		if (convertView == null) {
			view = inflater.inflate(R.layout.act_address_manage_select_listitem, null);
			holer = new AddressManageViewHoler();
			holer.receiver = (TextView) view.findViewById(R.id.ia_tv_name);
			holer.phone = (TextView) view.findViewById(R.id.ia_tv_phone);
			holer.ads = (TextView) view.findViewById(R.id.ia_tv_address);
			holer.img = ((ImageView)view.findViewById(R.id.ia_tv_default_img));
			holer.ia_tv_del = (TextView) view.findViewById(R.id.ia_tv_del);
			view.setTag(holer);
		} else {
			view = convertView;
			holer = (AddressManageViewHoler) view.getTag();
		}
		AddressDetail item = getItem(position);
		holer.receiver.setText(item.getName());
		holer.phone.setText(item.getPhonenumber());
		String string = areaDetail[position];
		if (string == null) {
			StringBuilder builder = new StringBuilder();
			Area area = areaDao.findByCantCode(item.getProvinceid());
			builder.append(area.getValue());
			area = areaDao.findByCantCode(item.getCityid());
			builder.append(area.getValue());
			area = areaDao.findByCantCode(item.getAreaid());
			builder.append(area.getValue());
			builder.append(" " + item.getAreadetail());
			areaDetail[position] = builder.toString();
		} 
		holer.ads.setText(areaDetail[position]);
		if(position==0){
		holer.img.setImageResource(R.drawable.icon_circle_choose);
		view.setBackgroundColor(inContext.getResources().getColor(R.color.bg_address_left_orange));
		}

		holer.ia_tv_del.setOnClickListener(activity);
		return view;
	}

    @Override
    public void onClick(View v) {
        
    }

}
