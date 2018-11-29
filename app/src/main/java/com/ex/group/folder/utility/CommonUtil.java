package com.ex.group.folder.utility;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.ex.group.folder.dialog.CommonDialog_oneButton;

import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.ex.group.folder.R;

import static com.ex.group.folder.utility.ClientUtil.APPNAME;
import static com.ex.group.folder.utility.ClientUtil.EX_STORE_PACKAGE;
import static com.ex.group.folder.utility.ClientUtil.SGN_PACKAGE;
import static com.ex.group.folder.utility.ClientUtil.SSM_APP;
import static com.ex.group.folder.utility.ClientUtil.SSM_EXECUTE_CODE;
import static com.ex.group.folder.utility.ClientUtil.SSM_INSTALLER_PACKAGE;
import static com.ex.group.folder.utility.ClientUtil.SSM_PACKAGE;
import static com.ex.group.folder.utility.ClientUtil.V3_APK;
import static com.ex.group.folder.utility.ClientUtil.V3_APP;
import static com.ex.group.folder.utility.ClientUtil.V3_PACKAGE;


public class CommonUtil {


	public static final String TAG = "CommonUtil";

	public static String[] permissions = {
			Manifest.permission.READ_PHONE_STATE,
			Manifest.permission.WRITE_EXTERNAL_STORAGE,
			Manifest.permission.READ_SMS,
			Manifest.permission.WRITE_CONTACTS/*,
														Manifest.permission.REQUEST_INSTALL_PACKAGES*/
	};


	ArrayList<AppInfoPackage> essentialList;
	ArrayList<AppInfoPackage> uninstalledList;
	SharedPreferences installShared;
	int total = 0;
	int cnt = 0;

	AppInfoPackage secuwayApp;
	AppInfoPackage installerApp;
	AppInfoPackage ssmApp;
	AppInfoPackage v3App;


	public static int PERMISSIONS_REQUEST_READ_PHONE_STATE = 2;

	public void setEssentialApp() {
		essentialList = new ArrayList<AppInfoPackage>();

		secuwayApp = new AppInfoPackage();
		installerApp = new AppInfoPackage();
		ssmApp = new AppInfoPackage();
		secuwayApp = new AppInfoPackage();

		secuwayApp = new AppInfoPackage();
		secuwayApp.setApkName(ClientUtil.SGN_APK);
		secuwayApp.setAppName(ClientUtil.SGN_APP);
		secuwayApp.setPackageName(SGN_PACKAGE);

		installerApp = new AppInfoPackage();
		installerApp.setApkName(ClientUtil.SSM_INSTALLER_APK);
		installerApp.setAppName(ClientUtil.SSM_INSTALLER_APP);
		installerApp.setPackageName(SSM_INSTALLER_PACKAGE);

		ssmApp = new AppInfoPackage();
		ssmApp.setApkName("");
		ssmApp.setAppName(SSM_APP);
		ssmApp.setPackageName(SSM_PACKAGE);

		v3App = new AppInfoPackage();
		v3App.setApkName(V3_APK);
		v3App.setAppName(V3_APP);
		v3App.setPackageName(V3_PACKAGE);

		essentialList.add(secuwayApp);
		essentialList.add(installerApp);
		//   essentialList.add(ssmApp);
		essentialList.add(v3App);

	}
	public static String[]EssentialAppList={EX_STORE_PACKAGE,SSM_INSTALLER_PACKAGE,SSM_PACKAGE,V3_PACKAGE,SGN_PACKAGE};
	public static boolean isExistApp(Context context, String packageName) {
		PackageManager pm = context.getPackageManager();
		try {
			pm.getApplicationInfo(packageName, PackageManager.GET_META_DATA);
		} catch (Exception e) {
			return false;
		}
		return true;
	}



	public ArrayList<AppInfoPackage> checkEssential(Context context) {
		int cnt = 0;
		setEssentialApp();

		uninstalledList = new ArrayList<AppInfoPackage>();

		for (int i = 0; i < essentialList.size(); i++) {
			if (!isExistApp(context, essentialList.get(i).getPackageName()))
			{
				if ((SSM_INSTALLER_PACKAGE).equals(essentialList.get(i).getPackageName()))
				{ // SSM installer X
					if (!isExistApp(context, SSM_PACKAGE))
					{
						// SSM installer X, SSM 둘다 설치 X
						uninstalledList.add(essentialList.get(i));      //SSM installer 추가
					}
				}
				else
				{
					uninstalledList.add(essentialList.get(i));  //설치되지 않은 앱 정보 추가
				}
			}
			else
			{
				//SSM installer 설치, SSM 설치X
				if ((SSM_INSTALLER_PACKAGE).equals(essentialList.get(i)) && !isExistApp(context, SSM_PACKAGE))
				{
					uninstalledList.add(ssmApp);
				}
			}
		}

		for (int i = 0; i < uninstalledList.size(); i++) {
			Log.d(TAG, "미설치 앱 " + i + "... " + uninstalledList.get(i).getPackageName());
		}

		return uninstalledList;
	}










	public boolean hasAllPermissionGranted(int[] grantResults) {
		boolean check = false;
		for (int result : grantResults) {
			//   Log.i(TAG, "result..."+result);
			if (result == PackageManager.PERMISSION_DENIED) {
				return false;
			}
		}
		return true;
	}




	public static String nullCheckParams(String mdn, String mac, String imei) {


		String errorMessage = "";

		if (!(mdn != null && mdn.length() > 0)) {
			errorMessage = "전화번호";
		}

		if (!(mac != null && mdn.length() > 0)) {
			if (errorMessage.length() > 0) {
				errorMessage = errorMessage + ", Mac Address";
			} else {
				errorMessage = errorMessage + "Mac Address";
			}

		}

		if (!(imei != null && mdn.length() > 0)) {
			if (errorMessage.length() > 0) {
				errorMessage = errorMessage + ", IMEI";
			} else {
				errorMessage = errorMessage + "IMEI";
			}
		}

		if (errorMessage.length() > 0) {
			if (mac == null) {
				errorMessage = errorMessage + " 값이 없습니다. Wi-Fi를 켠 후 다시 시도 해 주세요.";
			} else {
				errorMessage = errorMessage + " 값이 없습니다. 확인해 주세요.";
			}


			return errorMessage;
		}

		return "";
	}


	/**
	 * 전화번호를 010-xxxx-xxxx 형식으로 바꿔준다. ( "-" 기호 삽입 )
	 * @return String 010-xxxx-xxxx (형식)
	 */
	public static String checkPhoneNumber(String phoneNumber) {

		String first = "";
		String second = "";
		String third = "";

		if (phoneNumber != null) {

			if (!phoneNumber.contains("-")) {

				if (phoneNumber.length() == 10) {
					first = phoneNumber.substring(0, 3);
					second = phoneNumber.substring(3, 6);
					third = phoneNumber.substring(6, phoneNumber.length());
				} else if (phoneNumber.length() == 11) {
					first = phoneNumber.substring(0, 3);
					second = phoneNumber.substring(3, 7);
					third = phoneNumber.substring(7, phoneNumber.length());
				} else {
					return "";
				}

				return first + "-" + second + "-" + third;
			} else {
				return phoneNumber;
			}
		}

		return "";
	}

	/**
	 * 전화 번호 반환
	 * @param
	 * @return
	 * @permission <uses-permission android:name="android.permission.READ_PHONE_STATE" />
	 */
	public static String getPhoneNumber(Context context) {
		TelephonyManager tManager = (TelephonyManager) context.getSystemService
				(Context.TELEPHONY_SERVICE);

		return tManager != null ? tManager.getLine1Number() : "";
	}

	/**
	 * mdn을 얻는다
	 * @param context context
	 * @return mdn
	 */
	public static String getMdn(Context context) {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

		}
		String mdn = tm.getLine1Number();
		if(mdn != null) {
			if(mdn.indexOf("+82") == 0) {
				mdn = mdn.replaceFirst("\\+82", "0");
			}
		} else {
			mdn = "";
		}
		Log.v("VARIABLE_FIND---->","MDN : "+mdn);
		return mdn;
	}
	
	/**
	 * Mac Address를 얻는다.
	 * @param context
	 * @return
	 */
	public static String getMacAddr(Context context) {
		 try {
		        List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
		        for (NetworkInterface nif : all) {
		            if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

		            byte[] macBytes = nif.getHardwareAddress();
		            if (macBytes == null) {
						Log.v("VARIABLE_FIND---->","MACADRESS : NULL");
		                return "";
		            }

		            StringBuilder res1 = new StringBuilder();
                    Log.v("VARIABLE_FIND---->","MACADRESS : "+macBytes.toString());
		            for (byte b : macBytes) {
		                res1.append(Integer.toHexString(b & 0xFF) + ":");
		            }

		            if (res1.length() > 0) {
		                res1.deleteCharAt(res1.length() - 1);
		            }
					Log.v("VARIABLE_FIND---->","MACADRESS : "+res1.toString());
		            return res1.toString();
		        }
		    } catch (Exception ex) {
		        //handle exception
		    }
		    return "";
	}

	/**
	 * imsi를 얻는다.
	 * @param context context
	 * @return imsi
	 */
	public static String getImsi(Context context) {
		TelephonyManager mTelephonyMgr = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE); 
		return mTelephonyMgr.getSubscriberId(); 
	}

	/**
	 * imei를 얻는다
	 * @param context context
	 * @return imei
	 */
	public static String getImei(Context context) {
		TelephonyManager mTelephonyMgr = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        Log.v("VARIABLE_FIND---->","IMEINUMBER : "+mTelephonyMgr.getDeviceId());
		return mTelephonyMgr.getDeviceId();
	}
	
	/**
	 * 3G/LTE 네트웍 정보를 얻는다
	 * @param context context
	 * @return 네트웍 정보
	 */
	public static NetworkInfo getMobileNetwork3G(Context context) {
		ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		return cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
	}

	/***
	 * 네트워크 연결 상태 판별
	 * WI-Fi : 1
	 * MOBILE DATA : 0
	 */
	public static int getMobileData(Context context) {
		ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		
		int resValue = -1;
		if(activeNetwork != null){
			if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI){
				resValue = ConnectivityManager.TYPE_WIFI;	//1
			}
			if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE){
				resValue = ConnectivityManager.TYPE_MOBILE;	//0
			}
		}
		////LTE off 시
		
		return resValue;
		
	}
	
//	/**
//	 * TODO (jbyou)
//	 * 4G 네트웍 정보를 얻는다
//	 * @param context context
//	 * @return 네트웍 정보
//	 */
//	public static NetworkInfo getMobileNetwork4G(Context context) {
//		Log.i(TAG, "=== getMobileNetwork4G ===");
//		ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
//		return cm.getNetworkInfo(ConnectivityManager.TYPE_WIMAX);
//	}
	
	/**
	 * WIFI 네트웍 정보를 얻는다
	 * @param context context
	 * @return 네트웍 정보
	 */
	public static NetworkInfo getWifiNetwork(Context context) {
		ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		return cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
	}

	/**
	 * 3G 네트웍 정보를 얻는다
	 * @param context context
	 * @return 네트웍 정보
	 */
	public static NetworkInfo getMobileNetwork(Context context) {
		ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		return cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
	}

	/**
	 * 어플아이디를 얻는다
	 * @param context context
	 * @return 어플아이디
	 */
	public static String getAppId(Context context) {
		String appId = ""; 
		try { 
			PackageManager pm = context.getPackageManager();
			PackageInfo i = pm.getPackageInfo(context.getPackageName(), 0); 
			appId = i.applicationInfo.loadDescription(pm) + "";
		} catch(NameNotFoundException e) { }
		return appId;
	}

	/**
	 * 버전을 얻는다
	 * @param context context
	 * @return 버전
	 */
	public static String getVersion(Context context) {
		String version = "";
		try { 
			PackageInfo i = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			version = i.versionName + ""; 
		} catch(NameNotFoundException e) { }
		return version;
	}

	/**
	 * 버전코드를 얻는다
	 * @param context context
	 * @return 버전코드
	 */
	public static int getVersionCode(Context context) {
		int versionCode = 1;
		try { 
			PackageInfo i = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			versionCode = i.versionCode; 
		} catch(NameNotFoundException e) { }
		return versionCode;		
	}

	/**
	 * 어플명을 얻는다
	 * @param context context
	 * @return 어플명
	 */
	public static String getAppName(Context context) {
		String appName = ""; 
		try { 
			PackageManager pm = context.getPackageManager();
			PackageInfo i = pm.getPackageInfo(context.getPackageName(), 0); 
			appName = i.applicationInfo.loadLabel(pm) + "";
		} catch(NameNotFoundException e) { }
		return appName;
	}

	/**
	 * 언어코드를 얻는다
	 * @param context context
	 * @return 언어코드
	 */
	public static String getLang(Context context) {
		return context.getResources().getConfiguration().locale.getLanguage();
	}

	/**
	 * 설치여부를 판단한다
	 * @param context context
	 * @param appId 어플아이디
	 * @return 설치여부
	 */
	public static String isInstallPackage(Context context, String appId){
		PackageManager manager = context.getPackageManager();
		List<PackageInfo> packages = manager.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
		for(PackageInfo pack : packages) {
			ApplicationInfo app = pack.applicationInfo;
			String b_appId = app.loadDescription(manager) + "";

			if(b_appId.equals(appId)) {
//				SKTUtil.log(android.util.Log.DEBUG, "packageInfo", pack.packageName + "(" + app.loadLabel(manager) + "/" + b_appId + "/" + pack.versionName + ")");
				return pack.versionName+"";
			}
		}

		return null;
	}

	/**
	 * 설치여부를 판단한다
	 * @param context context
	 * @param pkgName 패키지명
	 * @return 설치여부
	 */
	public static String isInstallPackage_2(Context context, String pkgName){
		PackageManager manager = context.getPackageManager();
		List<PackageInfo> packages = manager.getInstalledPackages(0);
		for(PackageInfo pack : packages) {
			ApplicationInfo app = pack.applicationInfo;
			String b_pkgName = app.packageName + "";

			if(b_pkgName.equals(pkgName)) {
//				SKTUtil.log(android.util.Log.DEBUG, "packageInfo", pack.packageName + "(" + app.loadLabel(manager) + "/" + app.loadDescription(manager) + "/" + pack.versionName + ")");
				return pack.versionName+"";
			}
		}

		return null;
	}

	/**
	 * 키보드를 숨긴다
	 * @param a_oView view
	 * @return 키보드숨김여부
	 */
	public static boolean hideKeyboard(View a_oView) {
		InputMethodManager imm = (InputMethodManager) a_oView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		return imm.hideSoftInputFromWindow(a_oView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}

	public static void showKeyboard(View a_oView) {
		InputMethodManager imm = (InputMethodManager) a_oView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInputFromWindow(a_oView.getApplicationWindowToken(),  InputMethodManager.SHOW_FORCED, 0); 
	}

	/**
	 * 키보드를 숨긴다
	 * @param activity activity
	 */
	public static void hideKeyboard(Activity activity) {
		activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
	}

}
