package com.lianmeng.core.home.activity;



import com.lianmeng.core.account.activity.RestaurantAccountActivity;
import com.lianmeng.core.activity.R;
import com.lianmeng.core.order.activity.RestaurantOrderListActivity;
import com.lianmeng.extand.lianmeng.discover.activity.ZoneActivity;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost;

@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity {
    /** Called when the activity is first created. */
	private TabHost tabHost;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.act_main_down_menu);
        
        
        tabHost=this.getTabHost();
        TabHost.TabSpec spec;
        Intent intent;

        intent=new Intent().setClass(this, HomeActivity.class);
         
        spec=tabHost.newTabSpec(getString(R.string.home)).setIndicator(getString(R.string.home)).setContent(intent);
        tabHost.addTab(spec);
        
        intent=new Intent().setClass(this,ZoneActivity.class);
        spec=tabHost.newTabSpec(getString(R.string.find)).setIndicator(getString(R.string.find)).setContent(intent);
        tabHost.addTab(spec);
        
        intent=new Intent().setClass(this, RestaurantOrderListActivity.class);
        spec=tabHost.newTabSpec(getString(R.string.order)).setIndicator(getString(R.string.order)).setContent(intent);
        tabHost.addTab(spec);
        
     
        intent=new Intent().setClass(this, RestaurantAccountActivity.class);
        spec=tabHost.newTabSpec(getString(R.string.mine)).setIndicator(getString(R.string.mine)).setContent(intent);
        tabHost.addTab(spec);
        
        tabHost.setCurrentTab(0);
        setImgLight(R.id.tab_home_layout);
        View.OnClickListener voClick=new View.OnClickListener() {
            @Override
			public void onClick(View v) {
				
            	setImgLight(v.getId());
            	setImgNoLight(v.getId());
            	
            	 
				switch (v.getId()) {
				case R.id.tab_home_layout:
					tabHost.setCurrentTab(0);
					
					break;
				case R.id.tab_discover_layout:
					tabHost.setCurrentTab(1);
					break;
				case R.id.tab_order_layout:
					tabHost.setCurrentTab(2);
					break;
				case R.id.tab_mine_layout:
					tabHost.setCurrentTab(3);
					break;
				default:
					break;
				}
			}
        };
        
		LinearLayout radioly = (LinearLayout) this.findViewById(R.id.basic_tab_footer);
		for (int i = 0; i < radioly.getChildCount(); i++) {
			radioly.getChildAt(i).setOnClickListener(voClick);
			
		}
        
        
    }

    
    public void setImgLight(int inVid){
    	if (findViewById(inVid) instanceof LinearLayout) {
			LinearLayout limg = (LinearLayout) findViewById(inVid);
			for (int i = 0; i < limg.getChildCount(); i++) {
				if (limg.getChildAt(i) instanceof ImageView) {
					ImageView img = (ImageView) limg.getChildAt(i);
					img.setSelected(true);
				}
			}
		} else if (findViewById(inVid) instanceof RelativeLayout) {
			RelativeLayout limg = (RelativeLayout) findViewById(inVid);
			for (int i = 0; i < limg.getChildCount(); i++) {
				if (limg.getChildAt(i) instanceof ImageView) {
					ImageView img = (ImageView) limg.getChildAt(i);
					img.setSelected(true);
				}
			}
		}
    }
    
    public void setImgNoLight(int ortherId){
    	
    	LinearLayout radioly = (LinearLayout) this.findViewById(R.id.basic_tab_footer);
		for (int ij = 0; ij < radioly.getChildCount(); ij++) {
			int inVid=radioly.getChildAt(ij).getId();
			if (ortherId != inVid) {
				if (findViewById(inVid) instanceof LinearLayout) {
					LinearLayout limg = (LinearLayout) findViewById(inVid);
					for (int i = 0; i < limg.getChildCount(); i++) {
						if (limg.getChildAt(i) instanceof ImageView) {
							ImageView img = (ImageView) limg.getChildAt(i);
							img.setSelected(false);
						}
					}
				} else if (findViewById(inVid) instanceof RelativeLayout) {
					RelativeLayout limg = (RelativeLayout) findViewById(inVid);
					for (int i = 0; i < limg.getChildCount(); i++) {
						if (limg.getChildAt(i) instanceof ImageView) {
							ImageView img = (ImageView) limg.getChildAt(i);
							img.setSelected(false);
						}
					}
				}
			}
			
		}
    	
    	
    }
	 
    
   
}