package com.ex.group.approval.easy.constant;

public class ApprovalConstant {
	public static class protID {
		// N_ : 일반결재       E_ : 간이결재   
		public final static String COUNT = "RpcMobileSancWaitReviewRecvCount";	// 일반/간이결재 건수
		
		/*public final static String WAIT = "RpcMobileSancWaitDocList";		// 일반/간이결재 목록 - 결재대기
		public final static String DOING = "RpcMobileSancDoingDocList";		// 일반/간이결재 목록 - 결재진행
		public final static String DONE = "RpcMobileSancDoneDocList";		// 일반/간이결재 목록 - 결재완료
		public final static String RETURN = "RpcMobileSancReturnDocList";		// 일반/간이결재 목록 - 결재반려
*/		
		public final static String N_DOCINFO = "RpcMobileDocInfo";			// 일반/간이결재 문서 조회 - 일반결재
		
		public final static String N_PROCESS = "RpcMobileSancProcess";		// 일반/간이결재 승인/반려 - 일반결재 승인/반려 처리
		public final static String E_APPROVE = "RpcMobileWfSancProcess";		// 일반/간이결재 승인/반려 - 간이결재 승인
		public final static String E_RETURN = "RpcMobileWfSancReturn";		// 일반/간이결재 승인/반려 - 간이결재 반려
		
		public final static String N_RECALL = "RpcMobileDraftCancel";		// 기안회수 - 일반결재
		public final static String E_RECALL = "RpcMobileWfDraftCancel";		// 기안회수 - 간이결재
		
		public final static String DISPLAY = "RpcMobileReviewRecvDocList";		// 받은 공람목록 조회
	}
	
	public static class Detail {
		final public static String WEBVIEW_BASE_URL = "http://128.200.121.68:9000/gis/proxy2.jsp?resourceUrl=";
	}
	
	public static class SystemType {
		public final static String NORMAL = "1";
		public final static String EASY = "1";
	}
	
	public static class Action {
		public final static String APPROVE = "approve";
		public final static String REJECT = "reject";
		public final static String RECALL = "recall";
	}
	
	public static class DocType {
		public final static String APPROVAL_LIST = "0";
		public final static String APPROVAL_ING = "1";
		public final static String APPROVAL_DONE = "2";
		public final static String APPROVAL_RETURN = "3";
		public final static String APPROVAL_VIEW = "4";
		public final static String APPROVAL_DEFAULT = "0";
	}
	
	public static class Boolean {
		public final static String TRUE = "Y";
		public final static String FALSE = "N";
	}
	
	public static class RequestCode {
		final public static int REFRESH = 99999;
		final public static int FINISH = 99998;
		final public static int SELECT_COOPERATOR = 1;
		final public static int SELECT_LINE = 2;
	}
	
	public static class ResponseCode {
		final public static int OK = 12333;
		final public static int CANCEL = 12334;
	}
	
	public static class EasyApprovalType {
		public final static int OUTSIDE = 0;
		public final static int VACATION = 1;
	}
	
	public static class IntentArg {
		final public static String PRIMITIVE = "INTENT_PRIMITIVE";
	}
	
	public static class Tags {
		final public static String INTENT = "APPROVAL_INTENT";
	}
}
