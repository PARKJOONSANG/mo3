package com.ex.group.folder;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.ex.group.folder.dialog.CommonDialog_oneButton;
//import com.ex.group.folder.service.StoreJobServiece;
import com.ex.group.folder.service.AppStateService;
import com.ex.group.folder.service.FirebaseIDService;
import com.ex.group.folder.utility.BaseActivity;
import com.ex.group.folder.utility.CommonUtil;
import com.ex.group.folder.utility.FolderUtil;
import com.ex.group.folder.utility.LogMaker;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.sktelecom.ssm.lib.SSMLib;


import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.net.ssl.HttpsURLConnection;

import static com.ex.group.folder.utility.ClientUtil.APPNAME;
import static com.ex.group.folder.utility.ClientUtil.EX_STORE_PACKAGE;
import static com.ex.group.folder.utility.ClientUtil.SSM_APP;
import static com.ex.group.folder.utility.ClientUtil.SSM_INSTALLER_PACKAGE;
import static com.ex.group.folder.utility.ClientUtil.SSM_PACKAGE;
import static com.ex.group.folder.utility.ClientUtil.V3_APP;
import static com.ex.group.folder.utility.ClientUtil.V3_PACKAGE;

import static com.ex.group.folder.utility.CommonUtil.EssentialAppList;
import static com.ex.group.folder.utility.CommonUtil.isExistApp;
import static com.ex.group.folder.utility.FolderUtil.isRooted;
import static com.sktelecom.ssm.remoteprotocols.ResultCode.OK;


public class IntroActivity extends BaseActivity {
    private static final int PERMISSION_CHECK = 9134;
    private static final int VPN_SERVICE_PERMISSION_GROUP = 9130;
    private static final int VPN_SERVICE_PERMISSION_ALLOW = 9131;
    private static final int VPN_SERVICE_PERMISSION_DNEY = 9132;
    private static final int APP_PERMISSION_RETURN = 9133;
    private static final int SSM_EXECUTE_CODE = 580;


    private ImageView image, image1;
    SharedPreferences userPref;
    String vpnId;
    String vpnPw;
    String token;
    String today;
    String fcmDate;
    SimpleDateFormat dateFormat;
    long connectionTime;
    BluetoothAdapter bAdater;
    WifiManager wifi;
    int Wi_Fi;
    int bluetooth;

    CommonDialog_oneButton CDO;
    boolean rtn = false;
    public static SSMLib ssmLib;
    int ssmCode;


    int IMEI_CYCLE = -7;
    static String TAG = "=====IntroActivity=========";
    public String STORETAG = "◘◘◘◘◘ FROMSTORE@INTRO ◘◘◘◘◘";
    String mdmUrl = "https://mdm.ex.co.kr:52444/agent/setRegTargetDeviceInfoHphone.do";


    CommonDialog_oneButton finishDialog;


    AnimationDrawable animatic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_intro);
        super.onCreate(savedInstanceState);



        //job 등록



        Intent intent1 = new Intent(IntroActivity.this, AppStateService.class);
        startService(intent1);


        //Intro를 시작할때 스토어에서 시작한 부분이라면 메세지를 받아서 진행한다.
        onStoreCallback();


        ssmLib = SSMLib.getInstance(IntroActivity.this);
        if (ssmLib.checkSSMValidation() != OK) {
            ssmLib.initialize();
        }

        image1 = (ImageView) findViewById(R.id.image1);
        image = (ImageView) findViewById(R.id.image);

        image1.setImageResource(R.drawable.anima);
        image1.setScaleType(ImageView.ScaleType.FIT_CENTER);
        animatic = (AnimationDrawable) image1.getDrawable();


        image1.postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, 500);
        image1.post(new Runnable() {
            @Override
            public void run() {
                animatic.start();
            }
        });
        userPref = getSharedPreferences("USERINFO", MODE_PRIVATE);
        vpnId = userPref.getString("USERID", "");
        vpnPw = userPref.getString("USERPWD", "");
        fcmDate = userPref.getString("FCMDATE", "");
        connectionTime = userPref.getLong("CONNECTIONTIME", 0);
        ssmCode = ssmLib.initialize();


        Log.v("=============", "---------------------------------------OnCreate------------------------------------------------");


        bAdater = BluetoothAdapter.getDefaultAdapter();
        wifi = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);

        bluetooth = BluetoothAdapter.STATE_OFF;
        if (bAdater != null) {
            if (bAdater.getState() == BluetoothAdapter.STATE_ON) {
                bluetooth = BluetoothAdapter.STATE_ON;
            }
        }

        Wi_Fi = ConnectivityManager.TYPE_MOBILE;
        if (wifi != null) {
            if (CommonUtil.getMobileData(IntroActivity.this) == ConnectivityManager.TYPE_WIFI) {
                Wi_Fi = ConnectivityManager.TYPE_WIFI;
                image1.post(new Runnable() {
                    @Override
                    public void run() {
                        wifi.setWifiEnabled(false);
                    }
                });


            }
//            wifi.getWifiState();
        }


        SharedPreferences.Editor edit = userPref.edit();
        edit.putInt("BLUETOOTH", bluetooth);
        edit.putInt("WIFI", Wi_Fi);
        edit.apply();

        LogMaker.logStart();
        LogMaker.logmaking("vpnId", vpnId);
        LogMaker.logmaking("vpnPw", vpnPw);
        LogMaker.logmaking("fcmDate", fcmDate);
        LogMaker.logmaking("connectionTime", String.valueOf(connectionTime));
        LogMaker.logmaking("bluetooth", bluetooth);
        LogMaker.logmaking("Wi_Fi", Wi_Fi);
        LogMaker.logEnd();


        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        today = dateFormat.format(new Date());


        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        FirebaseApp.initializeApp(IntroActivity.this);
        token = FirebaseInstanceId.getInstance().getToken();
        LogMaker.logStart();
        Log.v("", "TOKEN :" + token);
        LogMaker.logEnd();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v("=============", "---------------------------------------OnResume------------------------------------------------");
        boolean resultPermission = false;
        resultPermission = permissionCheck();

        if (resultPermission == true) {
            checkPhoneState();
        }

    }

    public void checkssm() {
        Log.i(TAG, "===========checkSSM===========");

        ssmLib = new FolderUtil().initSSMLib(IntroActivity.this);


        String ssm_msg = "";//caution
        ssm_msg = FolderUtil.getMessage(IntroActivity.this, ssmLib.checkSSMValidation(), SSM_APP);
        Log.i(TAG, "checkSSM >>> check SSMValidation Code = " + ssm_msg + " check ssmCode" + ssmLib.checkSSMValidation());
        if (!("").equals(ssm_msg)) {    //SSM 비정상
            if (ssmLib.checkSSMValidation() == -1) {
                ssmLib.doingBind();
                LogMaker.logmaking("AFTER BINDING SSM", "");
                ssm_msg = FolderUtil.getMessage(IntroActivity.this, ssmLib.checkSSMValidation(), SSM_APP);
                checkV3();

            } else {
                finishDialog = new CommonDialog_oneButton(IntroActivity.this, getString(R.string.alert), ssm_msg, false, finishListener);
                finishDialog.show();
            }
        } else {

            checkV3();
        }
    }

    public void checkV3() {
        Log.i(TAG, "===========checkV3===========");
        //V3 앱 패키지명 : com.ahnlab.v3mobileenterprise
        if (ssmCode != OK) new FolderUtil().initSSMLib(IntroActivity.this);

        String v3_msg = "";
        v3_msg = FolderUtil.getMessage(IntroActivity.this, ssmLib.checkV3Validation(), V3_APP);    //V3 검사

        Log.i(TAG, "checkV3 >>> check V3Validation Code v3_msg = " + ssmLib.checkV3Validation());
        if (!("").equals(v3_msg)) {        //V3 기기 관리자 미등록 등 에러
            if (ssmLib.checkV3Validation() == -1) {

                Intent scanV3Intent = new Intent();
                scanV3Intent.setAction("com.sktelecom.ssm.action.ACTION_V3_SCAN_REQUEST");
                ssmLib.sendBroadcast(scanV3Intent);
                Log.i(TAG, "V3 sendBroadcast result=====>>>>" + ssmLib.sendBroadcast(scanV3Intent));
                Log.d("", "LoginActivity runAuthAction call location 3");
                Log.i(TAG, "V3 sendBroadcast result=====>>>>" + ssmLib.sendBroadcast(scanV3Intent));


                int ssmResult;


                image.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent loginIntent = new Intent(IntroActivity.this, LoginActivity.class);
                        animatic.stop();
                        loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(loginIntent);
                        finish();
                    }
                }, 1000);

                LogMaker.logmaking("AFTER BINDING V3", "");

            } else {
                finishDialog = new CommonDialog_oneButton(IntroActivity.this, getString(R.string.alert), v3_msg, false, V3ExecuteListener);
                finishDialog.show();
            }
        } else {    //V3 정상 설치
            Intent scanV3Intent = new Intent();
            scanV3Intent.setAction("com.sktelecom.ssm.action.ACTION_V3_SCAN_REQUEST");
            ssmLib.sendBroadcast(scanV3Intent);
            Log.i(TAG, "V3 sendBroadcast result=====>>>>" + ssmLib.sendBroadcast(scanV3Intent));
            Log.d("", "LoginActivity runAuthAction call location 3");
            Log.i(TAG, "V3 sendBroadcast result=====>>>>" + ssmLib.sendBroadcast(scanV3Intent));


            int ssmResult;


            image.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent loginIntent = new Intent(IntroActivity.this, LoginActivity.class);
                    animatic.stop();
                    loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent);
                    finish();
                }
            }, 1000);


        }
    }

    View.OnClickListener V3ExecuteListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finishDialog.dismiss();
            if (CommonUtil.isExistApp(IntroActivity.this, V3_PACKAGE)) {
                Intent intent = getPackageManager().getLaunchIntentForPackage(V3_PACKAGE);
                startActivity(intent);
            }
        }
    };

    public void CheckEssentialApp() {
        int length = EssentialAppList.length;

        boolean[] notInstalled = new boolean[length];
        for (int i = 0; i < length; i++) {
            if (!isExistApp(IntroActivity.this, EssentialAppList[i])) {
                LogMaker.logmaking(APPNAME[i], "UNINSTALLED");
                notInstalled[i] = false;
            } else {
                LogMaker.logmaking(APPNAME[i], "INSTALLED");
                notInstalled[i] = true;
            }

        }

        if (notInstalled[0] == false) {
            //ex 온라인 스토어로 이동한다.
            LogMaker.logmaking("GO_TO_ONLINE_STORE", "STORE");
            gotoWebStore();

        } else if (notInstalled[1] == true && notInstalled[2] == false) {

            //ssmInstaller로 넘어간다.
            LogMaker.logmaking("GO_TO_SSM_INSTALLER", "SSM");
            SSM_INSTALL();
        } else if (notInstalled[2] == false && notInstalled[1] == true) {
            //스토어 앱으로 이동한다.
            LogMaker.logmaking("GO_TO_STORE", "SSM_INSTALLER");
            gotoStore();
        } else if (notInstalled[1] == false && notInstalled[2] == false) {
            //스토어 앱으로 이동한다.
            LogMaker.logmaking("GO_TO_STORE", "SSM_INSTALLER");
            gotoStore();
        } else if (notInstalled[3] == false) {
            //스토어앱으로 이동한다.
            LogMaker.logmaking("GO_TO_STORE", "V3");
            gotoStore();
        } else if (notInstalled[4] == false) {
            //스토어앱으로 이동한다.
            LogMaker.logmaking("GO_TO_STORE", "VPN");
            gotoStore();
        } else {
            rootingCheck();
        }

    }

    public void gotoStore() {
        CDO = new CommonDialog_oneButton(IntroActivity.this, getString(R.string.intro_launching_error), getString(R.string.intro_notInstalled_SSM_INSTALLER),
                false, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CDO.dismiss();
                Intent intent = getPackageManager().getLaunchIntentForPackage(EX_STORE_PACKAGE);
                startActivity(intent);


            }
        });
        CDO.show();

    }

    public void gotoWebStore() {

        CDO = new CommonDialog_oneButton(IntroActivity.this, getString(R.string.intro_launching_error), getString(R.string.intro_notInstalled_STORE),
                false, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://store.ex.co.kr/"));
                startActivity(intent);
                finish();
                CDO.dismiss();

            }
        });
        CDO.show();
    }

    public void SSM_INSTALL() {

        CDO = new CommonDialog_oneButton(IntroActivity.this, getString(R.string.intro_launching_error), getString(R.string.intro_notInstalled_SSM),
                false, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getPackageManager().getLaunchIntentForPackage(SSM_INSTALLER_PACKAGE);
                startActivityForResult(intent, SSM_EXECUTE_CODE);

                CDO.dismiss();
            }
        });
        CDO.show();
    }

    public void checkPhoneState() {
        String mac = CommonUtil.getMacAddr(IntroActivity.this);
        String mdn = CommonUtil.getMdn(IntroActivity.this);
        String imei = CommonUtil.getImei(IntroActivity.this);
        LogMaker.logStart();
        LogMaker.logmaking("mac", mac);
        LogMaker.logmaking("mdn", mdn);
        LogMaker.logmaking("imei", imei);
        LogMaker.logEnd();

        if (token != null && !("").equals(token) && !fcmDate.equals(today)) {
            FirebaseIDService.sendToken(IntroActivity.this, token);
        }


        //IMEI 등록 to SSM 서버
        /**
         * @param result
         *   성공 : 002
         * , 등록되지 않은 전화번호 :  003
         * , 이미 등록된 휴대폰 정보 :  004
         */
        // 일주일에 한 번
        String authDate = userPref.getString("AUTHDATE", "");
        Calendar aWeekAgoCal = GregorianCalendar.getInstance();
        Calendar authCal = GregorianCalendar.getInstance();

        aWeekAgoCal.add(aWeekAgoCal.DATE, IMEI_CYCLE);

        LogMaker.logStart();
        LogMaker.logmaking("인증날짜", authCal.getTime().toString());
        LogMaker.logmaking("단말 인증 필요", authCal.before(aWeekAgoCal));
        LogMaker.logEnd();
        try {
            if (!("").equals(authDate)) {
                authCal.setTime(dateFormat.parse(authDate));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d("", "IntroActivity getPhoneState Step Test 1-1 = " + authDate + ":" + authCal.before(aWeekAgoCal));


        if (("").equals(authDate) || authCal.before(aWeekAgoCal))//인증만료일이 지났을 경우
        {
            String errorMessage = "";
            errorMessage = CommonUtil.nullCheckParams(mdn, mac, imei);


            if (("").equals(errorMessage)) {    //mdn, mac, imei 검증
                int networkStatus = CommonUtil.getMobileData(IntroActivity.this);
                Log.i(TAG, "networkStatus!!!!!" + networkStatus);


                if (networkStatus == -1) {
                    finishDialog = new CommonDialog_oneButton(IntroActivity.this,
                            getString(R.string.hi_failed), getString(R.string.check_network), false, finishListener);
                    finishDialog.show();

                } else {
                    new ImeiAsyncTask().execute(mdn, mac, imei);
                }
            } else {
                finishDialog = new CommonDialog_oneButton(IntroActivity.this,
                        getString(R.string.alert), errorMessage, false, finishListener);
                finishDialog.show();
            }
        } else {
            CheckEssentialApp();

        }

    }

    public void rootingCheck() {
        //모든 필수앱 설치 완료

        LogMaker.logmaking("==========checkPhoneState", "==================");
        Log.d("", "IntroActivity getPhoneState Step Test 3");


        String ssm_msg = "";

        if (!isRooted()) {    //루팅 되지 않은 정상 단말

            LogMaker.logmaking("==========checkPhoneState", "==================");
            Log.d("", "IntroActivity getPhoneState Step Test 4");
            Log.i(TAG, "checkSSM >>> ssmCode = " + ssmCode);
            LogMaker.logEnd();
            LogMaker.logmaking("SSM", "ssmcheck");
            checkssm();

        } else {    //루팅 된 단말
            ssm_msg = getString(R.string.rooted);
            finishDialog = new CommonDialog_oneButton(IntroActivity.this,
                    getString(R.string.alert), ssm_msg, false, finishListener);
            finishDialog.show();
        }
    }

    public class ImeiAsyncTask extends AsyncTask<String, Void, String> {
        int CONN_TIMEOUT = 15;
        int CONN_TIMEOUT2 = 10;
        int READ_TIMEOUT = 15;
        String POST = "POST";

        ProgressDialog imeiDialog;

        HttpsURLConnection conn = null;
        URL url = null;
        int retry = 0;

        OutputStream os = null;
        InputStream is = null;
        ByteArrayOutputStream baos = null;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            imeiDialog = new ProgressDialog(IntroActivity.this);
            ((AlertDialog) imeiDialog).setMessage(getResources().getString(R.string.dialog_wait));
            imeiDialog.setCancelable(false);
            imeiDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String result = "";
            int retry = 0;
            String resultCode = "";

            try {
                if (params == null) {
                    throw new Exception("params is null");
                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                result = e.getMessage();
            }

            try {
                //trustAllHosts();
                url = new URL(mdmUrl);

                conn = (HttpsURLConnection) url.openConnection();
                if (retry == 1) {
                    conn.setConnectTimeout(CONN_TIMEOUT * 1000);
                } else {
                    conn.setConnectTimeout(CONN_TIMEOUT2 * 1000);
                }


                conn.setReadTimeout(READ_TIMEOUT * 1000);
                conn.setRequestMethod(POST);
                conn.setRequestProperty("Cache-Control", "no-cache");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);
                conn.setDoInput(true);

                JSONObject obj = new JSONObject();
                obj.put("hPhone", params[0]);
                obj.put("mac", params[1]);
                obj.put("imei", params[2]);
                obj.put("iosDeviceSerialNum", "");
                obj.put("companyAsset", "0");
                obj.put("osKind", "1");
                String ob = obj.toString();
                Log.v("Check OBJ", "" + ob);
                os = conn.getOutputStream();
                os.write(obj.toString().getBytes());
                Log.v("URL HTTP", "-->" + os);
                os.flush();

                String response;


                int responseCode = conn.getResponseCode();
                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    is = conn.getInputStream();
                    baos = new ByteArrayOutputStream();
                    byte[] byteBuffer = new byte[1024];
                    byte[] byteData = null;

                    int nLength = 0;

                    while ((nLength = is.read(byteBuffer, 0, byteBuffer.length)) != -1) {
                        baos.write(byteBuffer, 0, nLength);
                    }
                    byteData = baos.toByteArray();

                    response = new String(byteData);
                    LogMaker.logStart();
                    LogMaker.logmaking("response", response);
                    LogMaker.logEnd();
                    JSONObject responseJSON = new JSONObject(response);

                    String responseResult = responseJSON.getString("result");
                    resultCode = responseJSON.getString("resultCode");

                    Log.i(TAG, "DATA response = " + response);
                    Log.i(TAG, "responseResult = " + responseResult);
                    Log.i(TAG, "resultCode = " + resultCode);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.i(TAG, "stack trace===>>> " + e.getMessage());
            }


            return resultCode;
        }


        /**
         * @param result 성공 : 002
         *               , 등록되지 않은 전화번호 :  003
         *               , 이미 등록된 휴대폰 정보 :  004
         */
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.i(TAG, "=========onPostExecute=========");
            if (imeiDialog != null && imeiDialog.isShowing()) {
                imeiDialog.dismiss();
            }

            if (("002").equals(result) || ("004").equals(result)) {
                Message msg = imeiHandler.obtainMessage();
                msg.what = 0;
                msg.obj = result;
                imeiHandler.sendMessage(msg);
                //prefAuthInfo = getSharedPreferences("authInfo", MODE_PRIVATE);
                SharedPreferences.Editor editor = userPref.edit();
                editor.putString("AUTHDATE", today);
                Log.i(TAG, " insert IMEI today============>>> " + today);
                editor.apply();
//				return;
            } else if (("003").equals(result)) {
                Message msg = imeiHandler.obtainMessage();
                msg.what = -1;
                msg.obj = result;
                imeiHandler.sendMessage(msg);
            } else {
                Message msg = imeiHandler.obtainMessage();
                msg.what = 99;
                msg.obj = result;
                imeiHandler.sendMessage(msg);
            }

        }

    }

    Handler imeiHandler = new Handler() {
        public void handleMessage(Message msg) {
//			imeiHandler.handleMessage(msg);
            switch (msg.what) {
                case 0:    //등록 성공
                    Log.i(TAG, "SUCCESS!!");
                    CheckEssentialApp();


                    break;
                case -1: //등록 실패(업무용 단말 등)
                    Log.i(TAG, "DEVICE IS NOT REGISTERED... ");
                    finishDialog = new CommonDialog_oneButton(IntroActivity.this,
                            getString(R.string.hi_failed), getString(R.string.error_003), false, finishListener);
                    finishDialog.show();
                    break;

                case 99:
                    Log.i(TAG, "DEVICE REGISTERATION FAILED... ");
                    finishDialog = new CommonDialog_oneButton(IntroActivity.this,
                            getString(R.string.hi_failed), getString(R.string.error_005), false, finishListener);
                    finishDialog.show();
                    break;
                default:
                    break;
            }
        }
    };


    View.OnClickListener finishListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = getPackageManager().getLaunchIntentForPackage(SSM_PACKAGE);
            startActivity(intent);
            finishDialog.dismiss();
            //finish();
        }
    };
    View.OnClickListener finishListener2 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = getPackageManager().getLaunchIntentForPackage(SSM_PACKAGE);
            startActivity(intent);
            finishDialog.dismiss();
            //finish();
        }
    };

    public boolean permissionCheck() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(CommonUtil.permissions, CommonUtil.PERMISSIONS_REQUEST_READ_PHONE_STATE);
                return false;
            } else {
                if ("".equals(userPref.getString("FIRSTRUN", ""))) {
                    requestPermissions(CommonUtil.permissions, CommonUtil.PERMISSIONS_REQUEST_READ_PHONE_STATE);
                    SharedPreferences.Editor edit = userPref.edit();
                    edit.putString("FIRSTRUN", "Y");
                    edit.apply();
                }
                return true;
            }
        } else {
            return true;
        }
    }

    @Override
    public boolean shouldShowRequestPermissionRationale(@NonNull String permission) {
        return super.shouldShowRequestPermissionRationale(permission);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 2:
                Log.v("", "=\n\n=======================onRequestPermissionResult=======================");
                if (new CommonUtil().hasAllPermissionGranted(grantResults)) {
                } else {
                    Toast.makeText(IntroActivity.this, R.string.permission_grant, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(Uri.parse("package:+" + getApplicationContext().getPackageName()));
                    startActivity(intent);
                    finish();
                }
                break;
        }
    }


    @Override
    protected void onStop() {
        super.onStop();

    }



    /*█▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀█*/
    /*█ Intent Meesage를 받고 난 후 로그인 상태에 따라서 실행 방법을 결정해 준다.                                                                                                                              █*/
    /*█▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄█*/
    /*█                                                                                                                                                                                                  █*/
    /*█                                                                                                                                                                                                  █*/

        public void onStoreCallback() {


            getIntent();
            Log.e(STORETAG, "LOGINSTATE : " + getSharedString("LOGINSTATE"));
            if (getSharedString("LOGINSTATE").equals("LOGIN")) {  //LOGIN STATE를 판단해준다.
                Intent intentFromSTORE = getIntent();
                Log.e(STORETAG, "====" + intentFromSTORE.getStringExtra("packageName"));
                if (intentFromSTORE.getStringExtra("packageName") != null) {  //혹시 intent messeage가 null인지 확인한다.

                    Intent intent = new Intent(IntroActivity.this, MainActivity.class);
                    intent.putExtra("packageName", intentFromSTORE.getExtras().getString("packageName"));
                    //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }

            }


        }
    /*█                                                                                                                                                                                                  █*/
    /*█                                                                                                                                                                                                  █*/
    /*█                                                                                                                                                                                                  █*/
    /*▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀*/




}