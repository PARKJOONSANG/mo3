package com.ex.group.mail.addressbook.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 사원 정보 클래스
 * @author jokim
 *
 */
public class EmployeeData implements Parcelable 
{
	private static String checkTrim(String a_sParam) { return a_sParam == null ? "" : a_sParam.trim(); }	
	private static String checkPhone(String phone) { return phone == null ? "" : phone.replace("-", "").trim(); }
	
    public String m_szSerialNo = "";			// 아이디
	public String m_szEmpId = "";			// 사번
	public String m_nfuId = "";				// 전자문서 아이디
    public String m_szName = "";				// 이름
    public String m_szDepartment = "";		// 소속부서
    public String m_szUpTeam = "";			// 부서상위명
    public String m_szTeam = "";				// 부서하위명
    public String m_szLocation = "";			// 근무지
    public String m_szCellPhoneNo = "";		// 휴대 전화 번호
    public String m_szOfficePhoneNo = "";	// 사무실 전화 번호
    public String m_szMail = "";				// 외부 메일
    public String m_szInnerMail = "";		// 내부 메일
    public String m_szOutMail = "";		// 외부 메일
    public String m_szInnerPhoneNo = "";		// 2015-03-17 Join 추가 - 기존 내부 메일 항목이 구내전화로 바꼈기 때문에 추가
    public String m_szRole = "";				// 담당역활
    public String m_szWork = "";				// 담당업무
    public String m_szPicturePath = "";		// 사진 경로
    public String m_szCompany = "";			// 관계사 이름
    public String m_szWzone = "";			// wzone 번호
    public String m_szMessenger = "";		// 메신저 주소
    public String m_szTwitter = "";			// 트위터 주소
    public String m_szType = "";				// 타입
    public String m_szCompanyCd = "";		// 관계사코드
    public String m_szTemp = "";
    public String m_szVvip = "";
    public String m_szEngName = "";
    public String m_szTeamLeader = "";
    public boolean isSelected;			// 선택 여부
    
    public String dutNm = "";            // 직책;
    private String currentKey = "";
    private String landline = ""; //집전화
    private String memo = ""; //메모
    private String engName = "";
    private String vvip = "";
    
    /****** 도로공사 추가사항 *******/
    public String jobCd = "";
    public String jobNm = "";
    public String joinDate = "";
    public String promotionDate = "";
    /*******************************/
    
    
    
    public EmployeeAddJobData[] m_szEmployeeAddJobData;
    
	public int describeContents() { return 0; }
	
	public EmployeeData() { }	
	
//	public final EmployeeData setOfficePhoneNo(String s) { m_szOfficePhoneNo = checkPhone(s); return this; }
//	public final EmployeeData setCellPhoneNo(String s) { m_szCellPhoneNo = checkPhone(s); return this; }
	public final EmployeeData setOfficePhoneNo(String s) { m_szOfficePhoneNo = checkTrim(s); return this; }
	public final EmployeeData setCellPhoneNo(String s) { m_szCellPhoneNo = checkTrim(s); return this; }
	
	public final EmployeeData setSerialNo(String s) { m_szSerialNo = checkTrim(s); return this; }
	public final EmployeeData setNfuId(String s) { m_nfuId = checkTrim(s); return this; }
	public final EmployeeData setDepartment(String s) { m_szDepartment = checkTrim(s); return this; }
	public final EmployeeData setUpTeam(String s) { m_szUpTeam = checkTrim(s); return this; }
	public final EmployeeData setTeam(String s) { m_szTeam = checkTrim(s); return this; }
	public final EmployeeData setName(String s) { m_szName = checkTrim(s); return this; }
	public final EmployeeData setPicturePath(String s) { m_szPicturePath = checkTrim(s); return this; }
	public final EmployeeData setLocation(String s) { m_szLocation = checkTrim(s); return this; }	
	public final EmployeeData setMail(String s) { m_szMail = checkTrim(s); return this; }
	public final EmployeeData m_szOutMail(String s) { m_szOutMail = checkTrim(s); return this; }
	public final EmployeeData setInnerMail(String s) { m_szInnerMail = checkTrim(s); return this; }
	public final EmployeeData setInnerPhoneNo(String s) { m_szInnerPhoneNo = checkTrim(s); return this; }	// 2015-03-17 Join 추가 - 기존 내부 메일 항목이 구내전화로 바꼈기 때문에 추가
	public final EmployeeData setWork(String s) { m_szWork = checkTrim(s); return this; }
	public final EmployeeData setCompany(String s) { m_szCompany = checkTrim(s); return this; }
	public final EmployeeData setWzone(String s) { m_szWzone = checkTrim(s); return this; }
	public final EmployeeData setRole(String s) { m_szRole = checkTrim(s); return this; }
	public final EmployeeData setMessenger(String s) { m_szMessenger = checkTrim(s); return this; }
	public final EmployeeData setTwitter(String s) { m_szTwitter = checkTrim(s); return this; }
	public final EmployeeData setType(String s) { m_szType = checkTrim(s); return this; }
	public final EmployeeData setCompanyCD(String s) { m_szCompanyCd = checkTrim(s); return this; }
	public final EmployeeData setTemp(String s) { m_szTemp = checkTrim(s); return this; }
	public final EmployeeData setEmpId(String s) { m_szEmpId = checkTrim(s); return this; }	
	public final EmployeeData setCurrentKey(String s) { this.currentKey = s; return this; }
	public final EmployeeData setLandline(String s) { this.landline = s; return this; }
	public final EmployeeData setMemo(String s) { this.memo = s; return this; }
	public final EmployeeData setEngName(String s) { this.engName = s; return this; }
	public final EmployeeData setDutNm(String s) { this.dutNm = s; return this; }
	public final EmployeeData setVvip(String s) { this.m_szVvip = s; return this; }
	public final EmployeeData setTeamLeader(String s) { this.m_szTeamLeader = s; return this; }
	public final EmployeeData setJobCd(String s) { this.jobCd = s; return this; }
	public final EmployeeData setJobNm(String s) { this.jobNm = s; return this; }
	public final EmployeeData setJoinDate(String s) { this.joinDate = s; return this; }
	public final EmployeeData setPromotionDate(String s) { this.promotionDate = s; return this; }
	
	
	public final String getCurrentKey() { return currentKey; }	
	public final String getLandline() { return landline; }	
	public final String getMemo() { return memo; }
	public final String getEngName() { return engName; }
	public final String getVvip() { return vvip; }
	public final String getJoinDate() { return joinDate; }
	public final String getPromotionDate() { return promotionDate; }
	
	
	
	public void writeToParcel(Parcel parcel, int arg1) {
		// TODO Auto-generated method stub
		parcel.writeString(m_szSerialNo);
		parcel.writeString(m_szEmpId);
		parcel.writeString(m_szName);
		parcel.writeString(m_szDepartment);
		parcel.writeString(m_szUpTeam);
		parcel.writeString(m_szTeam);
		parcel.writeString(m_szLocation);
		parcel.writeString(m_szCellPhoneNo);
		parcel.writeString(m_szOfficePhoneNo);
		parcel.writeString(m_szMail);
		parcel.writeString(m_szOutMail);
		parcel.writeString(m_szInnerMail);
		parcel.writeString(m_szInnerPhoneNo);			// 2015-03-17 Join 추가 - 기존 내부 메일 항목이 구내전화로 바꼈기 때문에 추가
		parcel.writeString(m_szRole);
		parcel.writeString(m_szWork);
		parcel.writeString(m_szPicturePath);
		parcel.writeString(m_szCompany);
		parcel.writeString(m_szWzone);
		parcel.writeString(m_szMessenger);
		parcel.writeString(m_szTwitter);
		parcel.writeString(m_szType);
		parcel.writeString(m_szCompanyCd);
		parcel.writeString(m_szTemp);
		parcel.writeString(m_szVvip);
		parcel.writeString(vvip);
		parcel.writeString(dutNm);
		parcel.writeString(currentKey);
		parcel.writeString(m_szTeamLeader);
		parcel.writeString(jobCd);
		parcel.writeString(jobNm);
		parcel.writeString(joinDate);
		parcel.writeString(promotionDate);
		
	}
	
	public static final Parcelable.Creator<EmployeeData> CREATOR =
			new Parcelable.Creator<EmployeeData>() {
		public EmployeeData createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			EmployeeData data = new EmployeeData();
			
			data.m_szSerialNo = source.readString();
			data.m_szEmpId = source.readString();
			data.m_szName = source.readString();
			data.m_szDepartment = source.readString();
			data.m_szUpTeam = source.readString();
			data.m_szTeam = source.readString();
			data.m_szLocation = source.readString();
			data.m_szCellPhoneNo = source.readString();
			data.m_szOfficePhoneNo = source.readString();
			data.m_szMail = source.readString();
			data.m_szOutMail = source.readString();
			data.m_szInnerMail = source.readString();
			data.m_szInnerPhoneNo = source.readString();		// 2015-03-17 Join 추가 - 기존 내부 메일 항목이 구내전화로 바꼈기 때문에 추가
			data.m_szRole = source.readString();
			data.m_szWork = source.readString();
			data.m_szPicturePath = source.readString();
			data.m_szCompany = source.readString();
			data.m_szWzone = source.readString();
			data.m_szMessenger = source.readString();
			data.m_szTwitter = source.readString();
			data.m_szType = source.readString();
			data.m_szCompanyCd = source.readString();
			data.m_szTemp = source.readString();
			data.m_szVvip = source.readString();
			data.m_szEngName = source.readString();
			data.m_szTeamLeader = source.readString();
			data.jobCd = source.readString();
			data.jobNm = source.readString();
			
			data.dutNm = source.readString();
			data.currentKey = source.readString();
			data.vvip = source.readString();
			data.joinDate = source.readString();
			data.promotionDate = source.readString();
			
			return data;
		}

		public EmployeeData[] newArray(int size) {
			// TODO Auto-generated method stub
			return new EmployeeData[size];
		}
	};
} // end public class EmployeeData