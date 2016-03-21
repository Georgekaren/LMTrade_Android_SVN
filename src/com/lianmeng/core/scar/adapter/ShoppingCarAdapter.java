package com.lianmeng.core.scar.adapter;

import java.util.List;

import com.lianmeng.core.activity.R;
import com.lianmeng.core.address.adapter.AddressManageAdapter.OnItemButtonListener;
import com.lianmeng.core.framework.util.ImageUtil;
import com.lianmeng.core.framework.util.ImageUtil.ImageCallback;
import com.lianmeng.core.scar.vo.Cart;
import com.lianmeng.core.scar.vo.CartProduct;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class ShoppingCarAdapter extends BaseAdapter  implements OnClickListener {
	private Context context;
	private List<CartProduct> productlist;
	private CartProduct cartProduct;
	private OnItemButtonListener listener;
	private int selPosi;
	
	public ShoppingCarAdapter() {
		super();
	}

	public ShoppingCarAdapter(Context context, Cart cart) {
		super();
		this.context = context;
		productlist = cart.productlist;
	}

	@Override
	public int getCount() {
		return productlist.size();
	}

	@Override
	public Object getItem(int position) {
		return productlist.get(position);
	}
	
	public void removeItem(int position) {
		 productlist.remove(position);
     	 notifyDataSetChanged();        
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = new View(context);
		view = View.inflate(context, R.layout.old_adapter_shopping_car_listitem, null);
		
		cartProduct = productlist.get(position);
		selPosi=position;
		final ImageView shopcar_item_prodImage_img= (ImageView) view.findViewById(R.id.shopcar_item_prodImage_img);
		TextView shopcar_item_prodName_text = new TextView(context);//名称
		shopcar_item_prodName_text = (TextView) view.findViewById(R.id.shopcar_item_prodName_text);
		TextView shopcar_item_prodId_text = new TextView(context);//编码
		shopcar_item_prodId_text = (TextView) view.findViewById(R.id.shopcar_item_prodId_text);
		TextView shopcar_item_prodPrice_text = new TextView(context);//单价
		shopcar_item_prodPrice_text = (TextView) view.findViewById(R.id.shopcar_item_prodPrice_text);
		TextView shopcar_item_prodCount_text = new TextView(context);//数量
		shopcar_item_prodCount_text = (TextView) view.findViewById(R.id.shopcar_item_prodCount_text);
		TextView shopcar_item_subtotal_text = new TextView(context);//小计
		shopcar_item_subtotal_text = (TextView) view.findViewById(R.id.shopcar_item_subtotal_text);
		EditText shopcar_item_prodCount_edit=(EditText) view.findViewById(R.id.shopcar_item_prodCount_edit);
		TextView shopcar_item_del_button = new TextView(context);//删除按钮
		shopcar_item_del_button=(TextView) view.findViewById(R.id.shopcar_item_delete_text);
		shopcar_item_del_button.setOnClickListener(new View.OnClickListener() {  
            @Override  
            public void onClick(View v) { 
            	if (listener != null) {
        			listener.onItemClick(v, selPosi);
        		  
        		}      
            }  
        });  
		String imageUrl =cartProduct.pic;
		String iImageUrl=imageUrl;
		if(iImageUrl!=null&&!iImageUrl.startsWith("http")){
			iImageUrl=context.getString(R.string.sysRequestHost)+imageUrl;
		}
		String imagePath = ImageUtil.getCacheImgPath().concat(ImageUtil.md5(iImageUrl));
		shopcar_item_prodImage_img.setTag(imagePath);
		Bitmap bitmap = ImageUtil.loadImage(imagePath, iImageUrl, new ImageCallback() {
			
			@Override
			public void loadImage(Bitmap bitmap, String imagePath) {
				ImageView iView = (ImageView) shopcar_item_prodImage_img.findViewWithTag(imagePath);
				if(iView!=null){
					iView.setImageBitmap(bitmap);
				}	
				
			}
		} );
		if(bitmap==null){
			shopcar_item_prodImage_img.setImageResource(R.drawable.product_loading);
		}else{
			shopcar_item_prodImage_img.setImageBitmap(bitmap);
		}
		
		
		
		shopcar_item_prodId_text.setText(cartProduct.id + "");
		shopcar_item_prodName_text.setText(cartProduct.name);
		shopcar_item_prodPrice_text.setText(cartProduct.price + "");
		shopcar_item_prodCount_text.setText(cartProduct.prodNum + "");
		shopcar_item_prodCount_edit.setText(cartProduct.prodNum + "");
		shopcar_item_subtotal_text.setText(cartProduct.subtotal + "");

		return view;
	}

	public void setListener(OnItemButtonListener listener) {
		this.listener = listener;
	}

	@Override
	public void onClick(View v) {
		/*if (listener != null) {
			listener.onItemClick(v, (Integer) v.getTag());
		}*/
	}
	
}
