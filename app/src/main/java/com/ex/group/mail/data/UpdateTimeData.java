package com.ex.group.mail.data;

import com.skt.pe.util.StringUtil;

/**
 * 업데이트 시간 데이타
 * @author sjsun5318
 *
 */
public class UpdateTimeData {

	private String companyCd;
	private String boxId;
	private String boxChangKey;
	private String mdn;
	private String boxType;
	private String upDateTime;
	
	public UpdateTimeData(){}
	
	public UpdateTimeData(String companyCd, String boxId, String boxChangKey, String mdn, String boxType, String upDateTime) {
		this.companyCd = companyCd;
		if(StringUtil.isNull(boxId)) {
			this.boxId = boxType;
			this.boxChangKey = boxType;
		} else {
			this.boxId = boxId;
			this.boxChangKey = boxChangKey;
		}
		
		this.mdn = mdn;
		this.boxType = boxType;
		this.upDateTime = upDateTime;
		
	}
	
	public String getCompanyCd() {
		return companyCd;
	}
	public void setCompanyCd(String companyCd) {
		this.companyCd = companyCd;
	}
	public String getBoxId() {
		
		return boxId;
	}
	public void setBoxId(String boxId) {
		if(StringUtil.isNull(boxId)) {
			this.boxId = boxType;
		} else {
			this.boxId = boxId;
		}
	}
	public String getBoxChangKey() {
		return boxChangKey;
	}
	public void setBoxChangKey(String boxChangKey) {
		if(StringUtil.isNull(boxChangKey)) {
			this.boxChangKey = boxType;
		} else {
			this.boxChangKey = boxChangKey;
		}
	}
	public String getMdn() {
		return mdn;
	}
	public void setMdn(String mdn) {
		this.mdn = mdn;
	}
	public String getBoxType() {
		return boxType;
	}
	public void setBoxType(String boxType) {
		this.boxType = boxType;
	}
	public String getUpDateTime() {
		return upDateTime;
	}
	public void setUpDateTime(String upDateTime) {
		this.upDateTime = upDateTime;
	}
	
	
}
