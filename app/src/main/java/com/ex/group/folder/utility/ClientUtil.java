package com.ex.group.folder.utility;


import android.os.Environment;

/**
 * 상수 및 static 메소드 정의
 */
public class ClientUtil {
	
	public final static String SERVER_IP = "http://128.200.121.68";
	public final static int SERVER_PORT = 9000;
	public final static String SERVER_URL = "/emp_ex/service.pe";
	public final static String logPrimitive = "primitive=COMMON_APP_APPLOG";
	public final static String FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();

	public final static String SGN_APK = "sgvpn-service.apk";
	public final static String SGN_APP = "vpn-service";
	public final static String SGN_PACKAGE = "com.sgvpn.vpn_service";


//	public final static String SECUWAYSSL_APK = "SecuwaySSLService.apk";
//	public final static String SECUWAYSSL_APP = "SECUWAYSSL";
//	public final static String SECUWAYSSL_PACKAGE = "net.secuwiz.SecuwaySSL.mobileservice";


	public final static String SSM_INSTALLER_APK = "SSMinstaller.apk";
	public final static String SSM_INSTALLER_APP = "SSM_INSTALLER";
	public final static String SSM_INSTALLER_PACKAGE = "com.sktelecom.ssm.installer";
    public static final int SSM_EXECUTE_CODE = 580;


	public final static String SSM_APP = "SSM";
	public final static String SSM_APK = "SSM.apk";
	public final static String SSM_PACKAGE = "com.sktelecom.ssm";


	public final static String V3_APK = "V3Enterprise.apk";
	public final static String V3_APP = "V3";
	public final static String V3_PACKAGE = "com.ahnlab.v3mobileenterprise";

	public final static String EX_STORE_PACKAGE = "com.ex.group.store";

	// ESSENTIAL APP UNINSTALLED STATE;
	public final static String[] APPNAME={"STORE",
										  "SSM_INSTALLER",
										  "SSM",
										  "V3",
										  "VPN"};
	public final static int STORE_UNINSTALLED=0;
	public final static int SSM_INSTALLER_UNINSTALLED=1;
	public final static int SSM_UNINSTALLED=2;
	public final static int V3_UNINSTALLED=3;
	public final static int VPN_UNINSTALLED=4;


	public final static String SGVPN_STATUS = "com.sgvpn.vpnservice.STATUS";
	public final static String SGVPN_API = "com.sgvpn.vpn_service.api.MobileApi";

	//COMMUNICATION with RETROFIT
	public final static String RESULT_SUCCECS ="1000";
	public final static String RESULT_ERROR = "1009";

	public static String companyCd = "";
	
	/** 로그인 한 사용자 ID 정보 **/
	public String empId	= "";
	/** 가입 ID 정보 **/
	public String userId = "";
	
	public  String userNm = "";
	public  String officeEmailAddr = "";
	public  String mdn = "";
	public  String deptNm = "";
	public  String App_ver = "";
	
	
	private static ClientUtil instance = null;
	private ClientUtil(){};
	
	public static ClientUtil getInstance(){
		if(instance == null){
			instance = new ClientUtil();
		}
		return instance;
	}
}
