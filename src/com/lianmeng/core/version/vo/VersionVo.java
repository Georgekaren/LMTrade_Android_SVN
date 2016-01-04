package com.lianmeng.core.version.vo;

public class VersionVo {

	private String id;
	private String updatePath;
	private String no;
	private String state;
	
	public VersionVo() {
	}

	public VersionVo(String id, String state, String no, String updatePath) {
		super();
		this.id = id;
		this.state = state;
		this.no = no;
		this.updatePath = updatePath;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUpdatePath() {
		return updatePath;
	}
	public void setUpdate_path(String updatePath) {
		this.updatePath = updatePath;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	
	
	
}
