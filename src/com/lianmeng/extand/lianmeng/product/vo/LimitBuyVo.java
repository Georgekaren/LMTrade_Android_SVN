/**
 * 
 */
package com.lianmeng.extand.lianmeng.product.vo;

import com.lianmeng.core.framework.util.SysU;

/**
 * @author Administrator
 *
 */
public class LimitBuyVo {

	public LimitBuyVo(){
		super();
	}
	
	private String id;
	private String name;
	private String pic;
	private double price;
	private double limitprice;
	private long lefttime;

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getLimitprice() {
		return limitprice;
	}

	public void setLimitprice(double limitprice) {
		this.limitprice = limitprice;
	}

	public long getLefttime() {
		return lefttime;
	}

	public void setLefttime(long lefttime) {
		this.lefttime = lefttime;
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

	public LimitBuyVo(String id, String name, String pic, double price, double limitprice, long lefttime) {
		super();
		this.id = id;
		this.name = name;
		this.pic = pic;
		this.price = price;
		this.limitprice = limitprice;
		this.lefttime = lefttime;
	}
	
	
	
}
