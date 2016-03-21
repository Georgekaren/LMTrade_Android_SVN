package com.lianmeng.core.product.activity;

import java.util.HashMap;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lianmeng.core.activity.R;
import com.lianmeng.core.activity.R.id;
import com.lianmeng.core.activity.R.layout;
import com.lianmeng.core.activity.R.string;
import com.lianmeng.core.framework.sysactivity.BaseWapperNewActivity;
import com.lianmeng.core.framework.sysactivity.BaseWapperNewActivity.DataCallback;
import com.lianmeng.core.framework.sysparser.SuccessParser;
import com.lianmeng.core.framework.sysvo.RequestVo;
import com.lianmeng.core.framework.util.Logger;
import com.lianmeng.core.framework.util.SysU;
import com.lianmeng.core.product.adapter.productGalleryAdapter;
import com.lianmeng.core.product.parser.ProductDetailParser;
import com.lianmeng.core.product.vo.ProdcutHistory;
import com.lianmeng.core.product.vo.ProductDetail;
import com.lianmeng.core.scar.activity.ShoppingCarActivity;

public class ProductDetailActivity extends BaseWapperNewActivity {

	private static final String TAG = "ProductDetailActivity";
	private ProductDetail productDetail;
	private TextView titleBack;
	private Gallery productGallery;
	private ImageView imgPoint;
	private TextView textProductNameValue;
	private TextView textProductIdValue;
	private TextView textOriginalPriceValue;
	private TextView textProdGradeValue;
	private TextView textPriceValue;
	private EditText prodNumValue;
	private TextView textPutIntoShopcar;
	private ImageView imgServiceImg;
	private TextView textProdIsStock;
	private TextView textProductCommentNum;
	private TextView orderTelTv;
	private ProductDetail currentProduct;

	/*@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.textProdToCollect:
			if (currentProduct == null)
				return ;
			HashMap<String, String> requestDataMap = new HashMap<String, String>();
			requestDataMap.put("id", currentProduct.getId() + "");
			String inMapData="{\"ServiceName\":\"extProdManagerService\" , \"Data\":{\"ACTION\":\"MIMADDFAVORITEPROD\",\"userId\":\""+SysU.USERID+"\",\"id\":\""+currentProduct.getId() +"\"}}";
			requestDataMap.put("JsonData", inMapData);
			RequestVo reqVo = new RequestVo(R.string.sysRequestServLet, context, requestDataMap, new SuccessParser());
			getDataFromServer(reqVo, new DataCallback<Boolean>() {

				@Override
				public void processData(Boolean paramObject, boolean paramBoolean) {
					if (paramObject!= null && paramObject) {
						Toast.makeText(ProductDetailActivity.this, getString(R.string.prodDetailMsgOrderDetailFavoriteSucceedNameMsg), Toast.LENGTH_LONG).show();
					} else {
						Toast.makeText(ProductDetailActivity.this, getString(R.string.prodDetailMsgOrderDetailFavoriteFaildNameMsg), Toast.LENGTH_LONG).show();
					}
				}
			});
			break;
			
		}
	}*/

	@Override
	protected void findViewById() {
		productGallery = (Gallery) findViewById(R.id.productGallery);
		textProductIdValue = (TextView) findViewById(R.id.textProductIdValue);
		textProductNameValue = (TextView) findViewById(R.id.textProductNameValue);
		textOriginalPriceValue = (TextView) findViewById(R.id.textOriginalPriceValue);
		textPriceValue = (TextView) findViewById(R.id.textPriceValue);
		textProductCommentNum = (TextView) findViewById(R.id.textProductCommentNum);
		prodNumValue = (EditText) findViewById(R.id.prodNumValue);
		textPutIntoShopcar = (TextView) findViewById(R.id.textPutIntoShopcar);
		orderTelTv = (TextView) findViewById(R.id.orderTelTv);
		//findViewById(R.id.textProdToCollect).setOnClickListener(this);
	}

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.old_act_product_detail);
		setTitle(getString(R.string.prodDetailTitleOrderDetailNameMsg));
	}

	@Override
	protected void onHeadRightButton(View v) {

	}

	@Override
	protected void processLogic() {
		String selProdId  = getIntent().getStringExtra("id");
		if (selProdId==null||"".equals(selProdId)) {
			
			Logger.d(TAG, "selProdId is null");
			finish();
			return ;
		}
		
		RequestVo requestVo = new RequestVo();
		requestVo.requestUrl =R.string.sysRequestServLet;// R.string.product;
		requestVo.context = context;
		HashMap<String, String> requestDataMap = new HashMap<String, String>();
		//requestDataMap.put("id", id + "");
		String inMapData="{\"ServiceName\":\"extProdManagerService\" , \"Data\":{\"ACTION\":\"QRYPRODDETAIL\",\"id\":\""+selProdId+"\"}}";
		requestDataMap.put("JsonData", inMapData);
		requestVo.requestDataMap = requestDataMap ;
		requestVo.jsonParser = new ProductDetailParser();
		getDataFromServer(requestVo, new DataCallback<ProductDetail>() {
			@Override
			public void processData(ProductDetail paramObject, boolean paramBoolean) {
				if (paramObject != null) {
					currentProduct = paramObject;
					getECManager().addProductToHistory(
					new ProdcutHistory(paramObject.getId(), paramObject.getName(), paramObject.getPic().get(0), paramObject.getMarketprice(), paramObject.getPrice(), paramObject.getComment_count(),
							System.currentTimeMillis()));
					
					productGalleryAdapter adapter = new productGalleryAdapter(context, paramObject, productGallery);
					productGallery.setAdapter(adapter);

					textProductNameValue.setText(paramObject.getName());
					textProductIdValue.setText(paramObject.getId() + "");
					textOriginalPriceValue.setText(paramObject.getMarketprice() + "");
					textPriceValue.setText(paramObject.getPrice() + "");
					textProductCommentNum.setText(paramObject.getComment_count() + "");
				}
			}
		});

	}

	@Override
	protected void setListener() {
		productGallery.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
			}
		});
		textPutIntoShopcar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				HashMap<String, String> requestDataMa = new HashMap<String, String>();
				requestDataMa.put("id", currentProduct.getId() + "");
				requestDataMa.put("count", prodNumValue.getText().toString());
				String inMap="{\"ServiceName\":\"srvOrderManagerService\" , \"Data\":{\"ACTION\":\"ADDORDER\",\"prodId\":[\""+currentProduct.getId()+"\"],\"userId\":\""+SysU.USERID+"\",\"prodNum\":\""+prodNumValue.getText().toString()+"\",\"totalPrice\":\"13\"}}";
				requestDataMa.put("JsonData", inMap);
				RequestVo reqVo = new RequestVo(R.string.sysRequestServLet, context, requestDataMa , new SuccessParser());
				getDataFromServer(reqVo , new DataCallback<Boolean>() {

					@Override
					public void processData(Boolean paramObject, boolean paramBoolean) {
 						if (paramObject != null && paramObject) {
 							AlertDialog.Builder builder = new Builder(ProductDetailActivity.this);
 							builder.setTitle(getString(R.string.prodDetailMsgOrderDetailFavoriteSucceedNameMsg));
 							builder.setPositiveButton(getString(R.string.prodDetailTitleContinueShopingMsg), new DialogInterface.OnClickListener() {
 								@Override
 								public void onClick(DialogInterface dialog, int which) {
 									finish();
 								}
 							});
 							builder.setNegativeButton(getString(R.string.prodDetailTitleInShopingCartMsg), new DialogInterface.OnClickListener() {
 								@Override
 								public void onClick(DialogInterface dialog, int which) {
 									Intent intent = new Intent(ProductDetailActivity.this, ShoppingCarActivity.class);
 									startActivity(intent);
 									finish();
 								}
 							});

 							builder.create().show();
 						} else {
 							Toast.makeText(ProductDetailActivity.this, getString(R.string.prodDetailMsgOrderDetailFavoriteFaildNameMsg), Toast.LENGTH_LONG).show();
 						}
					}
				});
				
				 
			}
		});

		orderTelTv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_CALL);
				//intent.setData(Uri.parse("tel:01088499999"));
				startActivity(intent);
			}
		});

	}

}
