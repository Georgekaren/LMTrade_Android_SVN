package com.lianmeng.core.home.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/** 
 * Description: 首页九宫格类型按钮排列视图<br> 
 *  
 * @author XXX<br>
 * @version 8.0<br>
 * @taskId <br>
 * @CreateDate 2016年1月28日 <br>
 * @since V8<br>
 * @see com.lianmeng.core.home.view <br>
 */
public class HomeClassGridView extends GridView {

	public HomeClassGridView(Context context) {
		super(context);
	}

	public HomeClassGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public HomeClassGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,MeasureSpec.AT_MOST);

		super.onMeasure(widthMeasureSpec, expandSpec);
	}

}
