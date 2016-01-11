package com.lianmeng.core.home.activity;

import java.util.List;




import com.lianmeng.core.activity.R;
import com.lianmeng.core.home.activity.MyListView.OnRefreshListener;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MyExamActivity extends BaseActivity {

	private MyListView myListView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myexam);
		
		myListView=(MyListView) findViewById(R.id.myexam_listview);
		myListView.setAdapter(new MyAdatper());
		myListView.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				new AsyncTask<Void, Void, Void>() {
					protected Void doInBackground(Void... params) {

						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						
						myListView.onRefreshComplete();

					}

				}.execute();
				
			}
		});
		
		myListView.setOnItemClickListener(new ListView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), arg2+"", Toast.LENGTH_SHORT).show();
			}
		});
		myListView.setOnItemLongClickListener(new ListView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), arg2+"", Toast.LENGTH_SHORT).show();
				return true;
			}

			
		});
	}
	
	class MyAdatper extends BaseAdapter{
      
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 10;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View contentView, ViewGroup arg2) {
			// TODO Auto-generated method stub
			if(contentView==null)
			{
				contentView=LayoutInflater.from(getApplicationContext()).inflate(R.layout.myexam_item, null);
			}
			
			return contentView;
		}
		
	}
}
