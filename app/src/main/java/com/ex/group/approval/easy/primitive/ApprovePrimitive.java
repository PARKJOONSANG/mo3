package com.ex.group.approval.easy.primitive;

import com.ex.group.approval.easy.constant.UserInfo;
import com.skt.pe.common.activity.SKTActivity.Action;
import com.skt.pe.common.primitive.Primitive;


@SuppressWarnings("serial")
public class ApprovePrimitive extends Primitive {
	// 문서결재 승인, 반려, 회수하기
	public final static String name = "COMMON_APPROVALNEW_APPROVE";
	
	private final static String[] paramNames = {
		"docID", "userID", "action"
	};
	
	public ApprovePrimitive() {
		super(ApprovePrimitive.name, paramNames, Action.SERVICE_RETRIEVING);
		this.init();
	}
	
	private void init() {
//		this.setUserID("19203920");
//		this.setUserID("19516018");
		UserInfo userInfo = UserInfo.getInstance();
		this.setUserID(userInfo.getUserID());
	}
	
	public void setDocID(String docID) {
		super.addParameter(paramNames[0], docID);
	}
	
	public void setUserID(String userID) {
		super.addParameter(paramNames[1], userID);
	}
	
	public void setAction(String action) {
		super.addParameter(paramNames[2], action);
	}
	
	public String getAction() {
		return super.getParameter(paramNames[2]);
	}
	
	public static class ApproveAction {
		public final static String APPROVE = "approve";
		public final static String REJECT = "reject";
		public final static String RECALL = "recall";
	}
}
