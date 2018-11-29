package com.ex.group.board.data;

public class C2dmData {
	
	public static String KeyBmsg = "msg";
	public static String KeyBType = "bType";
	public static String KeyBName = "bName";
	
	private String msg = "";
	private String bType = "";
	private String bName = "";
	
	
	
	public C2dmData() {
		super();
		msg = "";
		bType = "";
		bName = "";
		// TODO Auto-generated constructor stub
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getbType() {
		return bType;
	}
	public void setbType(String bType) {
		this.bType = bType;
	}
	public String getbName() {
		return bName;
	}
	public void setbName(String bName) {
		this.bName = bName;
	}
	@Override
	public String toString() {
		return "C2dmData [bName=" + bName + ", bType=" + bType + ", msg=" + msg
				+ "]";
	}
	
	
	
}
