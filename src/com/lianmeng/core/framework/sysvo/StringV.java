package com.lianmeng.core.framework.sysvo;

/**
 * 简单Stringvo
 * @author shen.zhi
 *
 */
public class StringV {
	
	private String id;
	private String name;
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public StringV() {
	}

	public StringV(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	
}	
