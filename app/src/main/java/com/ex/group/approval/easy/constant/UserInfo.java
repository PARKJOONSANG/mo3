package com.ex.group.approval.easy.constant;

public class UserInfo {
	private String userName;
	private String userID;
	private String userRole;
	private String userSuffixDept;
	
	static UserInfo userInfo = new UserInfo();
	
	public UserInfo() { }
	
	public static UserInfo getInstance() {
		if(userInfo == null) {
			userInfo = new UserInfo();
		}
		return userInfo;
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public String getUserSuffixDept() {
		return userSuffixDept;
	}

	public void setUserSuffixDept(String userSuffixDept) {
		this.userSuffixDept = userSuffixDept;
	}
	
}
