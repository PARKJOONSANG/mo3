package com.ex.group.folder.utility;



 public class Constants {

    public static class URL{
//        public static String VPN_ADDRESS = "180.148.182.171";
        public static String VPN_ADDRESS = "180.148.182.24";            //L4 : 180.148.182.24  실제IP : 180.148..182.23, 180.148.182.123
        public static String SSO_ADDRESS = "http://128.200.121.68:9000/emp_ex/login.pe";            //L4 : 180.148.182.24  실제IP : 180.148..182.23, 180.148.182.123
    }
    public final static String CHROME_PACKAGE = "com.android.chrome";
    public static class IMEI{
            public static String FAILED = "001";              // 실패
            public static String SUCCESS = "002";           // 성공
            public static String UNDEFINED = "003";     //  등록되지 않은 전화번호
            public  static String ALREADY = "004";          // 이미 등록된 휴대폰 정보
    }
    public static class FCM{
        public static String SUCCESS = "OK";
    }

    public static class Status{
        public enum Connection_N_Status{
            LEVEL_NOLEVEL,         //0
            LEVEL_CONNECTING,      //1
            LEVEL_CONNECTED,       //2
            LEVEL_AUTH_FAILED,     //3
            LEVEL_PW_EXPIRED,      //4
            LEVEL_BLOCK_ACCESS,    //5
            LEVEL_DUP_LOGIN,       //6
            LEVEL_NONETWORK,       //7
            LEVEL_CRITICAL_ITEMS_NOT_FOUNTD,   // 8
            LEVEL_DISCONNECTED_DONE    //9
        }
    }

}
