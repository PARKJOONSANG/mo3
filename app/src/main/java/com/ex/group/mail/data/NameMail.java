package com.ex.group.mail.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 자동 완성에 들어갈 이름과 메일주소 데이타
 * @author sjsun5318
 *
 */
public class NameMail implements Parcelable {
	private String name;
	private String mail;
	
	public NameMail() {	}
	
	public NameMail(String name, String mail){
		this.name = name;
		this.mail = mail;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the mail
	 */
	public String getMail() {
		return mail;
	}

	/**
	 * @param mail the mail to set
	 */
	public void setMail(String mail) {
		this.mail = mail;
	}
	
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public void writeToParcel(Parcel dest, int flags) {

		dest.writeString(name);
		dest.writeString(mail);
		
	}
	
	
	public static final Parcelable.Creator<NameMail> CREATOR = new Creator<NameMail>() {
		
		public NameMail createFromParcel(Parcel parcel) {
			NameMail data = new NameMail();
			
			data.setName(parcel.readString());
			data.setMail(parcel.readString());
			return data;
		}

		@Override
		public NameMail[] newArray(int size) {
			return new NameMail[size];
		}
	};
	
	
	
}