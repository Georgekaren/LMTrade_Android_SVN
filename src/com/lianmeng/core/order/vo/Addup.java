package com.lianmeng.core.order.vo;

/**
 * 
 * 购物车总计
 * 
 * @author liu
 * 
 */
public class Addup {
	/** 商品数量总计 */
	public double total_count;

	/** 商品金额总计 */
	public double total_price;

	/** 商品积分总计 */
	public double total_point;

	public Addup() {
 	}
	

	public Addup(int total_count, double total_price, int total_point) {
		super();
		this.total_count = total_count;
		this.total_price = total_price;
		this.total_point = total_point;
	}



	public double getTotal_count() {
		return total_count;
	}

	public void setTotal_count(double total_count) {
		this.total_count = total_count;
	}

	public double getTotal_price() {
		return total_price;
	}

	public void setTotal_price(double total_price) {
		this.total_price = total_price;
	}

	public double getTotal_point() {
		return total_point;
	}

	public void setTotal_point(double total_point) {
		this.total_point = total_point;
	}
	
	 
	
}
