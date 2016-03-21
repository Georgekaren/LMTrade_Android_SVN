package com.lianmeng.core.scar.activity;

import com.lianmeng.core.activity.R;
import com.lianmeng.core.framework.sysactivity.BaseWapperNewActivity;
import com.lianmeng.core.product.activity.ProductDetailActivity;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class shopping_none_car_activity extends BaseWapperNewActivity implements View.OnClickListener {
	TextView shopcar_toBuy_text ;
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void findViewById() {
		shopcar_toBuy_text = (TextView) findViewById(R.id.shopcar_toBuy_text);

	}

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.old_act_shopping_none_car);

	}

	@Override
	protected void processLogic() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void setListener() {
		shopcar_toBuy_text.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(shopping_none_car_activity.this,ProductDetailActivity.class);
				startActivity(intent);
				finish();
			}
		});

	}

}
