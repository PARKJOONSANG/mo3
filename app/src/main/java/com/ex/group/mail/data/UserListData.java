package com.ex.group.mail.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.ex.group.mail.util.EmailClientUtil;

/**
 * 메일함 테이타
 * @author sjsun5318
 *
 */
public class UserListData implements Parcelable {
	private String id;
	private String name;
	private String rankName;
	private String deptName;
	
	
	public UserListData(){
	
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
		p.writeString(name);
		p.writeString(rankName);
		p.writeString(deptName);
	}

	/**
	 * Parcelable 만들기
	 */
	public static final Parcelable.Creator<UserListData> CREATOR = new Creator<UserListData>() {
		
		public UserListData createFromParcel(Parcel parcel) {
			UserListData data = new UserListData();
			data.setId(parcel.readString());
			data.setName(parcel.readString());
			data.setRankName(parcel.readString());
			data.setDeptName(parcel.readString());
			
			return data;
		}
		
		public UserListData[] newArray(int size) {
			return new UserListData[size];
		}
		
	};
	
	





	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	

	/**
	 * @return the rankName 직급
	 */
	public String getRankName() {
		return rankName;
	}
	/**
	 * @param companyCd the companyCd to set
	 */
	public void setRankName(String rankName) {
		this.rankName = rankName;
	}
	/**
	 * @return the boxType
	 */
	public String getDeptName() {
		return deptName;
	}
	/**
	 * @param boxType the boxType to set
	 */
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	/**
	 * @return the boxId
	 */
	

	
	public void setEncryption() {
		id 				= EmailClientUtil.seedEncrypt(id);
		name 			= EmailClientUtil.seedEncrypt(name);
		rankName 		= EmailClientUtil.seedEncrypt(rankName);
		deptName 		= EmailClientUtil.seedEncrypt(deptName);
	}

	public void setDecryption() {
		id 				= EmailClientUtil.seedDecrypt(id);
		name 			= EmailClientUtil.seedDecrypt(name);
		rankName 		= EmailClientUtil.seedDecrypt(rankName);
		deptName 		= EmailClientUtil.seedDecrypt(deptName);
	}
	
	
}
