package com.ex.group.approval.easy.primitive;

import android.util.Log;

import com.ex.group.approval.easy.constant.ApprovalConstant;
import com.ex.group.approval.easy.constant.UserInfo;
import com.skt.pe.common.activity.SKTActivity.Action;
import com.skt.pe.common.exception.SKTException;
import com.skt.pe.common.primitive.Primitive;
import com.skt.pe.common.service.XMLData;


@SuppressWarnings("serial")
public class CountPrimitive extends Primitive {
	// 2014-07-17 Join 수정 : 간이결재 고도화로 인한 수정
	private final String TAG = "CountPrimitive";
	
	// 문서 건수 조회
	public final static String name = "COMMON_APPROVALNEW_COUNT";
	
	private final static String[] paramNames = {
		"protID", "userID"
	};

	private UserInfo userInfo;
	private int sanc_wait;
	private String user_name;
	private String user_role;
	private String user_dept;
	
	public CountPrimitive() {
		super(CountPrimitive.name, paramNames, Action.SERVICE_RETRIEVING);
		this.init();
	}
	
	private void init() {
		this.setProtID(ApprovalConstant.protID.COUNT);
//		this.setUserID("19203920");
//		this.setUserID("19516018");
		userInfo = UserInfo.getInstance();
		this.setUserID(userInfo.getUserID());
	}
	
	public void setProtID(String protID) {
		super.addParameter(paramNames[0], protID);
	}
	
	public void setUserID(String userID) {
		super.addParameter(paramNames[1], userID);
	}
	
	public int getSanc_wait() {
		return this.sanc_wait;
	}
	
	public String getUser_name() {
		return this.user_name;
	}
	
	public String getUser_role() {
		return this.user_role;
	}
	
	public String getUser_dept() {
		return this.user_dept;
	}
	
	@Override
	public void convertXML(XMLData xmldata) throws SKTException {
		super.convertXML(xmldata);
		this.sanc_wait = parseInt(xmldata.get("wfsanc_wait"));	//간이결재 건수
//		this.sanc_wait = parseInt(xmldata.get("sanc_wait"));	//전자결재 건수
		this.user_name = xmldata.get("user_name");
		this.user_role = xmldata.get("user_role");
		this.user_dept = xmldata.get("user_dept");
		
		Log.d(TAG, "this.sanc_wait ================= " + this.sanc_wait);
		Log.d(TAG, "this.user_name ================= " + this.user_name);
		Log.d(TAG, "this.user_role ================= " + this.user_role);
		Log.d(TAG, "this.user_dept ================= " + this.user_dept);
		
	}

	@Override
	protected String toPrimitiveString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append("sanc_wait=");
		sb.append(this.getSanc_wait());
		sb.append("user_name=");
		sb.append(this.getUser_name());
		sb.append("user_role=");
		sb.append(this.getUser_role());
		sb.append("user_dept=");
		sb.append(this.getUser_dept());

		return sb.toString();
	}
}
