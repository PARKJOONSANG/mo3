package com.ex.group.mail.addressbook.data;

import android.os.Parcel;
import android.os.Parcelable;

public class EmployeeAddJobData implements Parcelable {

	public String id = "";
	public String deptCode = "";
	public String suffixDept = "";
	public String companyCd = "";
	public String twitter = "";
	public String CompanyNm = "";
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	public EmployeeAddJobData() {}
	
	public final EmployeeAddJobData setId(String s) { this.id = s; return this; }
	public final EmployeeAddJobData setDeptCode(String s) { this.deptCode = s; return this; }
	public final EmployeeAddJobData setSuffixDept(String s) { this.suffixDept = s; return this; }
	public final EmployeeAddJobData setCompanyCd(String s) { this.companyCd = s; return this; }
	public final EmployeeAddJobData setTwitter(String s) { this.twitter = s; return this; }
	public final EmployeeAddJobData setCompanyNm(String s) { this.CompanyNm = s; return this; }

	public final String getId() { return id; }	
	public final String getDeptCode() { return deptCode; }	
	public final String getSuffixDept() { return suffixDept; }	
	public final String getCompanyCd() { return companyCd; }	
	public final String getTwitter() { return twitter; }	
	public final String getCompanyNm() { return CompanyNm; }	

	@Override
	public void writeToParcel(Parcel parcel, int arg1) {
		// TODO Auto-generated method stub
		
		parcel.writeString(id);
		parcel.writeString(deptCode);
		parcel.writeString(suffixDept);
		parcel.writeString(companyCd);
		parcel.writeString(twitter);
		parcel.writeString(CompanyNm);
		
	}
	
	public static final Parcelable.Creator<EmployeeAddJobData> CREATOR =
		new Parcelable.Creator<EmployeeAddJobData>() {
		public EmployeeAddJobData createFromParcel(Parcel source) {
			EmployeeAddJobData data = new EmployeeAddJobData();
			
			data.id = source.readString();
			data.deptCode = source.readString();
			data.suffixDept = source.readString();
			data.companyCd = source.readString();
			data.twitter = source.readString();
			data.CompanyNm = source.readString();
			
			return data;
		}
		public EmployeeAddJobData[] newArray(int size) {
			// TODO Auto-generated method stub
			return new EmployeeAddJobData[size];
		}
	};
}
