package com.ex.group.approval.easy.addressbook.data;

/**
 * Sms 관련 정보 클래스
 * @author jokim
 *
 */
public class SmsData {
	private String name;
	private String phone;
	
	public SmsData(String name, String phone) {
		this.name = name;
		this.phone = phone;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
}