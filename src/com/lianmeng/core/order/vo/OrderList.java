package com.lianmeng.core.order.vo;

/**
 * 订单列表
 * 
 * @author liu
 * 
 */
public class OrderList {

	/** 订单编号 */
	private String orderid;

	/** 订单显示状态 */
	private String status;

	/** 订单金额 */
	private double price;

	/** 下单时间 */
	private String time;

	/** 订单标识，1=>可删除可修改 2=>不可修改 3=>已完成 */
	private int flag;

	private String pic;
	
    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public OrderList() {
	}

	public OrderList(String orderid, String status, double price, String time, int flag, String pic) {
		super();
		this.orderid = orderid;
		this.status = status;
		this.price = price;
		this.time = time;
		this.flag = flag;
		this.pic = pic;
	}

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

}
