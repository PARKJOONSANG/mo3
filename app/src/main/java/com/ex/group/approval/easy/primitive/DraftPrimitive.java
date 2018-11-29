package com.ex.group.approval.easy.primitive;

import com.ex.group.approval.easy.constant.ApprovalConstant;
import com.ex.group.approval.easy.constant.UserInfo;
import com.skt.pe.common.activity.SKTActivity.Action;
import com.skt.pe.common.primitive.Primitive;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


@SuppressWarnings("serial")
public class DraftPrimitive extends Primitive {
	// 간이기안 - 외출/휴가 신청하기
	public final static String name = "COMMON_APPROVALSTAGING_DRAFT";
	private Date fromDate, untilDate, targetDate, fromTime, untilTime;
	private String totalVacation;	// 총 휴가일 수
	private String exceptCount;		// 제외일 수
	
	private final static String[] paramNames = {
		"systemType", "draftKind", "fromDate", "untilDate", "targetDate",
		"fromTime", "untilTime", "exclusiveDate", "formCode", "telNum",
		"location", "descript", "isOwnCar", "cooperator", "approvalStep",
		"approvalKind", "userID"
	};
	
	public DraftPrimitive() {
		super(DraftPrimitive.name, paramNames, Action.SERVICE_RETRIEVING);
		this.init();
	}
	
	private void init() {
		this.setSystemType(ApprovalConstant.SystemType.EASY);
		UserInfo userInfo = UserInfo.getInstance();
		this.setUserID(userInfo.getUserID());
	}
	
	public void setSystemType(String systemType) {
		super.addParameter(paramNames[0], systemType);
	}
	
	public void setDraftKind(String draftKind) {
		super.addParameter(paramNames[1], draftKind);
	}
	
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
		super.addParameter(paramNames[2], formatDate(fromDate));
	}
	
	public Date getFromDate() {
		return this.fromDate;
	}
	
	public void setUntilDate(Date untilDate) {
		this.untilDate = untilDate;
		super.addParameter(paramNames[3], formatDate(untilDate));
	}
	
	public Date getUntilDate() {
		return this.untilDate;
	}
	
	public void setTargetDate(Date targetDate) {
		this.targetDate = targetDate;
		super.addParameter(paramNames[4], formatDate(targetDate));
	}
	
	public Date getTargetDate() {
		return this.targetDate;
	}
	
	public void setFromTime(Date fromTime) {
		this.fromTime = fromTime;
		super.addParameter(paramNames[5], formatTime(fromTime));
	}
	
	public Date getFromTime() {
		return this.fromTime;
	}
	
	public void setUntilTime(Date untilTime) {
		this.untilTime = untilTime;
		super.addParameter(paramNames[6], formatTime(untilTime));
	}
	
	public Date getUntilTime() {
		return this.untilTime;
	}
	
	public void setExclusiveDate(String exclusiveDate) {
		super.addParameter(paramNames[7], exclusiveDate);
	}
	
	public void setFormCode(String formCode) {
		super.addParameter(paramNames[8], formCode);
	}
	
	public void setTelNum(String telNum) {
		super.addParameter(paramNames[9], telNum);
	}
	
	
	public void setLocation(String location) {
		super.addParameter(paramNames[10], location);
	}
	
	public void setDescript(String descript) {
		super.addParameter(paramNames[11], descript);
	}
	
	public void setIsOwnCar(boolean isOwnCar) {
		String strIsOwnCar = isOwnCar == true ? ApprovalConstant.Boolean.TRUE : ApprovalConstant.Boolean.FALSE;
		super.addParameter(paramNames[12], strIsOwnCar);
	}
	
	public void setCooperator(String cooperator) {
		super.addParameter(paramNames[13], cooperator);
	}
	
	public void setApprovalStep(String approvalStep) {
		super.addParameter(paramNames[14], approvalStep);
	}
	
	public void setApprovalKind(String approvalKind) {
		super.addParameter(paramNames[15], approvalKind);
	}
	
	public void setUserID(String userID) {
		super.addParameter(paramNames[16], userID);
	}
	
	public static String formatDate(Date inputDate) {
		SimpleDateFormat format = new SimpleDateFormat(DateFormat.dateFormat);
		return format.format(inputDate).toString();
	}
	
	public static Date parseDate(String inputDate) {
		SimpleDateFormat format = new SimpleDateFormat(DateFormat.dateFormat);
		try {
			return format.parse(inputDate);
		} catch (ParseException e) {
			return null;
		}
	}
	
	public static String formatTime(Date inputTime) {
		SimpleDateFormat format = new SimpleDateFormat(DateFormat.timeFormat);
		return format.format(inputTime).toString();
	}
	
	private static class DateFormat {
		public final static String dateFormat = "yyyy-MM-dd";
		public final static String timeFormat = "HH:mm";
	}
	
	public static class DraftKind {
		public final static String OUTSIDE = "0";
		public final static String VACATIION = "1";
	}
	
	public static class ApprovalKind {
		public final static String DRAFTER = "125";
		public final static String NORMAL = "110";
		public final static String AUTHORIZED = "113";
		public final static String SUBSTITUTED = "114";
		public final static String NONE = "none";
//		public final static String NORMAL = "normal";
//		public final static String AUTHORIZED = "authorized";
//		public final static String SUBSTITUTED = "substituted";
//		public final static String NONE = "none";
	}
	
	public static class Tags {
		public final static String FORM_CODE_NAME = "FORM_CODE_NAME";
	}

	public String getTotalVacation() {
		return totalVacation;
	}

	public void setTotalVacation(String totalVacation) {
		this.totalVacation = totalVacation;
	}

	public String getExceptCount() {
		return exceptCount;
	}

	public void setExceptCount(String exceptCount) {
		this.exceptCount = exceptCount;
	}
}
