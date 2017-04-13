package edu.ncsu.csc.itrust.controller.obstetrics.report;

public class ComplicationInfo {
	private boolean flag;
	private String title;
	private String message;
	
	/** This ID should be unique among complications. */
	private String id;
	
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	
	public boolean getFlag() {
		return this.flag;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return this.message;
	}
	
	public void setID(String id) {
		this.id = id;
	}
	
	public String getID() {
		return this.id;
	}
}