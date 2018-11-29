package com.ex.group.board.constants;

public class IntentConstants {
	public final static String PRIMITIVE = "PRIMITIVE";
	public final static String TWEET = "TWEET";
	public final static String ACTIVITY_INFO = "ACTIVITY_INFO";
	public final static String USER_SEQ = "USER_SEQ";
	public final static String USER_NAME = "USER_NAME";
	public final static String EMP_ID = "EMP_ID";
	public final static String TAB_ID = "TAB_ID";
	
	private IntentConstants() {
	}
	
	public final static class BroadCastCode {
		public final static String REFRESH = "com.skt.pe.group.twitter.REFRESH";
		public final static String LIST_REFRESH = "com.skt.pe.group.twitter.LIST_REFRESH";
		public final static String MEMBER_REFRESH = "com.skt.pe.group.twitter.MEMBER_REFRESH";
	}
	
	public final static class RequestCode {
		public final static int NORMAL = 99900;
		public final static int REFRESH = 99901;
		public final static int SELECT_MENTION = 99902;
		public final static int FINISH = 99903;
	}
	
	public final static class ResultCode {
		public final static int OK = 99900;
		public final static int CANCEL = 99901;
		public final static int REFRESH = 99902;
	}
	
	public final static class TweetList {
		public final static String LIST_TYPE = "LIST_TYPE";
	}
	
	public final static class TweetWrite {
		public final static String WRITE_TYPE = "WRITE_TYPE";
		public final static String TITLE = "TITLE";
	}
	
	public final static class TweetMember {
		public final static String REQUEST_TYPE = "REQUEST_TYPE";
		public final static String SELECT_MEMBER = "SELECT_MEMBER";
		
		public final static class RequestType {
			public final static int LIST = 1;
			public final static int MENTION = 2;
			public final static int FOLLOWING = 3;
			public final static int FOLLOWER = 4;
		}
	}
	
	public static class Key {
		public final static String REGI_ID = "RegistrationId";
		public final static String PUSH_TYPE = "PushType";
		public final static String APPID = "appId";
		public final static String TICKER = "ticker";
		public final static String TITLE = "title";
		public final static String MESSAGE = "msg";
		public final static String PARAMETER = "params";
	}
	
	public static class Action {
		public final static String PUSH_REFRESH = "com.ex.group.push.refresh";
		public final static String PUSH_REGISTRATION = "com.ex.group.push.registration";
		public final static String PUSH_PARAMETER = "com.ex.group.push.parameter";
	}
	
//	public final static class TweetProfile {
//		public final static String WHO = "PROFILE_WHO";
//		
//		public final static class Who {
//			public final static int ME = 1;
//			public final static int OTHER = 2;
//		}
//	}
}
