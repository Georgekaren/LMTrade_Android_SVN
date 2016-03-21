package com.lianmeng.core.address.vo;

/**
 * 地址信息
 * @author liu
 *
 */
public class Address {
	private int id;
	private String name;
	private String address_area;
	private String address_detail;
	/** 手机号码 */
    private String phonenumber;
    
	public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public Address() {
	}
	
	public Address(int id, String name, String address_area, String address_detail,String phonenumber) {
		super();
		this.id = id;
		this.name = name;
		this.address_area = address_area;
		this.address_detail = address_detail;
		this.phonenumber=phonenumber;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress_area() {
		return address_area;
	}
	public void setAddress_area(String address_area) {
		this.address_area = address_area;
	}
	public String getAddress_detail() {
		return address_detail;
	}
	public void setAddress_detail(String address_detail) {
		this.address_detail = address_detail;
	}
}	
