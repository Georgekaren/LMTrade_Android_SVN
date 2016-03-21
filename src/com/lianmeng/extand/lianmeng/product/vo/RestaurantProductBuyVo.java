/**
 * 
 */
package com.lianmeng.extand.lianmeng.product.vo;


/**
 * @author Administrator
 *
 */
public class RestaurantProductBuyVo {

	public RestaurantProductBuyVo(){
		super();
	}
	
	private String id;
	private String name;
	private String pic;
	private double price;

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public RestaurantProductBuyVo(String id, String name, String pic, double price, double limitprice, long lefttime) {
		super();
		this.id = id;
		this.name = name;
		this.pic = pic;
		this.price = price;
		
	}
	
	
	
}
