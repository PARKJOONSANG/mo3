package com.ex.group.mail.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.ex.group.mail.util.EmailClientUtil;

/**
 * 메일함 테이타
 * @author sjsun5318
 *
 */
public class EmailMainListData implements Parcelable {
	private String id;
	private String mdn;
	private String companyCd;
	private String boxType;
	private String boxId;
	private String boxChangeKey;
	private String boxName;
	private String totalCnt;
	private String unreadCnt;
	private String parentBoxType;
	private String updatedate;
	private String boxOrder;
	private String boxLevel;
	
	
	public EmailMainListData(){
	
	}
	
	
	/**
	 * describeContents 메소드
	 */
	@Override
	public int describeContents() {
		return 0;
	}

	/**
	 * writeToParcel 메소드
	 */
	@Override
	public void writeToParcel(Parcel p, int flags) {
		p.writeString(id);
		p.writeString(mdn);
		p.writeString(companyCd);
		p.writeString(boxType);
		p.writeString(boxId);
		p.writeString(boxChangeKey);
		p.writeString(boxName);
		p.writeString(totalCnt);
		p.writeString(unreadCnt);
		p.writeString(parentBoxType);
		p.writeString(updatedate);
		p.writeString(boxOrder);
		p.writeString(boxLevel);		
	}

	/**
	 * Parcelable 만들기
	 */
	public static final Parcelable.Creator<EmailMainListData> CREATOR = new Creator<EmailMainListData>() {
		
		public EmailMainListData createFromParcel(Parcel parcel) {
			EmailMainListData data = new EmailMainListData();
			data.setId(parcel.readString());
			data.setMdn(parcel.readString());
			data.setCompanyCd(parcel.readString());
			data.setBoxType(parcel.readString());
			data.setBoxId(parcel.readString());
			data.setBoxChangeKey(parcel.readString());
			data.setBoxName(parcel.readString());
			data.setTotalCnt(parcel.readString());
			data.setUnreadCnt(parcel.readString());
			data.setParentBoxType(parcel.readString());
			data.setUpdatedate(parcel.readString());
			data.setBoxOrder(parcel.readString());
			data.setBoxLevel(parcel.readString());
			
			return data;
		}
		
		public EmailMainListData[] newArray(int size) {
			return new EmailMainListData[size];
		}
		
	};
	
	





	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getBoxOrder() {
		return boxOrder;
	}
	public void setBoxOrder(String boxOrder) {
		this.boxOrder = boxOrder;
	}
	public String getUpdatedate() {
		return updatedate;
	}
	public void setUpdatedate(String updatedate) {
		this.updatedate = updatedate;
	}
	public String getMdn() {
		return mdn;
	}
	public void setMdn(String mdn) {
		this.mdn = mdn;
	}
	
	
	public String getBoxLevel() {
		return boxLevel;
	}


	public void setBoxLevel(String boxLevel) {
		this.boxLevel = boxLevel;
	}


	public String getParentBoxType() {
		return parentBoxType;
	}
	public void setParentBoxType(String parentBoxType) {
		this.parentBoxType = parentBoxType;
	}
	/**
	 * @return the totalCnt
	 */
	public String getTotalCnt() {
		return totalCnt;
	}
	/**
	 * @param totalCnt the totalCnt to set
	 */
	public void setTotalCnt(String totalCnt) {
		this.totalCnt = totalCnt;
	}
	/**
	 * @return the unreadCnt
	 */
	public String getUnreadCnt() {
		return unreadCnt;
	}
	/**
	 * @param unreadCnt the unreadCnt to set
	 */
	public void setUnreadCnt(String unreadCnt) {
		this.unreadCnt = unreadCnt;
	}
	/**
	 * @return the companyCd
	 */
	public String getCompanyCd() {
		return companyCd;
	}
	/**
	 * @param companyCd the companyCd to set
	 */
	public void setCompanyCd(String companyCd) {
		this.companyCd = companyCd;
	}
	/**
	 * @return the boxType
	 */
	public String getBoxType() {
		return boxType;
	}
	/**
	 * @param boxType the boxType to set
	 */
	public void setBoxType(String boxType) {
		this.boxType = boxType;
	}
	/**
	 * @return the boxId
	 */
	public String getBoxId() {
		return boxId;
	}
	/**
	 * @param boxId the boxId to set
	 */
	public void setBoxId(String boxId) {
		this.boxId = boxId;
	}
	/**
	 * @return the boxChangeKey
	 */
	public String getBoxChangeKey() {
		return boxChangeKey;
	}
	/**
	 * @param boxChangeKey the boxChangeKey to set
	 */
	public void setBoxChangeKey(String boxChangeKey) {
		this.boxChangeKey = boxChangeKey;
	}
	/**
	 * @return the boxName
	 */
	public String getBoxName() {
		return boxName;
	}
	/**
	 * @param boxName the boxName to set
	 */
	public void setBoxName(String boxName) {
		this.boxName = boxName;
	}

	
	public void setEncryption() {
//		id 				= EmailClientUtil.seedEncrypt(id);
//		mdn 			= EmailClientUtil.seedEncrypt(mdn);
//		companyCd 		= EmailClientUtil.seedEncrypt(companyCd);
		boxType 		= EmailClientUtil.seedEncrypt(boxType);
		boxId 			= EmailClientUtil.seedEncrypt(boxId);
		boxChangeKey 	= EmailClientUtil.seedEncrypt(boxChangeKey);
		boxName 		= EmailClientUtil.seedEncrypt(boxName);
		totalCnt 		= EmailClientUtil.seedEncrypt(totalCnt);
		unreadCnt 		= EmailClientUtil.seedEncrypt(unreadCnt);
		parentBoxType 	= EmailClientUtil.seedEncrypt(parentBoxType);
		updatedate 		= EmailClientUtil.seedEncrypt(updatedate);
//		boxOrder 		= EmailClientUtil.seedEncrypt(boxOrder);
		boxLevel 		= EmailClientUtil.seedEncrypt(boxLevel);
	}

	public void setDecryption() {
//		id 				= EmailClientUtil.seedDecrypt(id);
//		mdn 			= EmailClientUtil.seedDecrypt(mdn);
//		companyCd 		= EmailClientUtil.seedDecrypt(companyCd);
		boxType 		= EmailClientUtil.seedDecrypt(boxType);
		boxId 			= EmailClientUtil.seedDecrypt(boxId);
		boxChangeKey 	= EmailClientUtil.seedDecrypt(boxChangeKey);
		boxName 		= EmailClientUtil.seedDecrypt(boxName);
		totalCnt 		= EmailClientUtil.seedDecrypt(totalCnt);
		unreadCnt 		= EmailClientUtil.seedDecrypt(unreadCnt);
		parentBoxType 	= EmailClientUtil.seedDecrypt(parentBoxType);
		updatedate 		= EmailClientUtil.seedDecrypt(updatedate);
//		boxOrder 		= EmailClientUtil.seedDecrypt(boxOrder);
		boxLevel 		= EmailClientUtil.seedDecrypt(boxLevel);
	}
	
	
}
