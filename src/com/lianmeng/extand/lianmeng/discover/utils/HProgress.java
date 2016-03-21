package com.lianmeng.extand.lianmeng.discover.utils;
 

import com.lianmeng.extand.lianmeng.discover.widget.CustomProgressDialog;

import android.content.Context;


public class HProgress {
	private static CustomProgressDialog dialog = null;

	public static void show(Context context, String message) {
		try {
			if(context==null)return;
			if (dialog != null)
				dialog.dismiss();
			dialog = new CustomProgressDialog(context,null);
			// dialog.setCancelable(false);
			dialog.show();
		} catch (Exception e) {

		}
	}

	public static void dismiss() {
		try {
			if (dialog != null)
				dialog.dismiss();
		} catch (Exception e) {

		}
	}
}
