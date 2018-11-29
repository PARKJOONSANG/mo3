package com.ex.group.folder;

import android.app.Activity;
import android.app.KeyguardManager;
import android.bluetooth.BluetoothAdapter;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.hardware.fingerprint.FingerprintManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamsecurity.util.DEFCrypto;
import com.dreamsecurity.util.InvalidFormatException;
import com.ex.group.folder.dialog.CommonDialog;
import com.ex.group.folder.dialog.CommonDialog_oneButton;
import com.ex.group.folder.loginfragment.Fragment_FINGERPRINT;
import com.ex.group.folder.loginfragment.Fragment_ID;
import com.ex.group.folder.loginfragment.Fragment_PIN;
import com.ex.group.folder.retrofitclient.APIClient;
import com.ex.group.folder.retrofitclient.APIInterface;
import com.ex.group.folder.retrofitclient.pojo.RequestLogin;
import com.ex.group.folder.utility.BaseActivity;
import com.ex.group.folder.dialog.CustomprogressDialog;
import com.ex.group.folder.utility.ClientUtil;
import com.ex.group.folder.utility.CommonUtil;
import com.ex.group.folder.utility.LogMaker;
import com.nprotect.keycryptm.IxCustomInputActivity;
import com.samsung.android.sdk.pass.Spass;
import com.samsung.android.sdk.pass.SpassFingerprint;
import com.sktelecom.ssm.lib.SSMLib;

import java.io.IOException;
import java.lang.reflect.Field;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import com.ex.group.folder.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ex.group.board.constants.IntentConstants.ResultCode.OK;
import static com.ex.group.folder.utility.ClientUtil.RESULT_ERROR;
import static com.ex.group.folder.utility.ClientUtil.RESULT_SUCCECS;
import static com.ex.group.folder.utility.ClientUtil.SGN_PACKAGE;
import static com.ex.group.folder.utility.ClientUtil.SGVPN_STATUS;
/*
 *                                                                           -CREATED BY JSP 2018.11
 */

public class LoginActivity extends BaseActivity implements Handler.Callback, View.OnClickListener {


            public final String TAG ="◘◘◘◘◘◘  LoginActivity  ◘◘◘◘◘◘";
            public final String FORMAL_STATE = "1000";
            public final String NO_USER = "1001";
            public final String WRONG_PASSWORD = "1002";
            public final String RETIRED_USER = "1005";

            APIInterface apiInterface;
            APIClient apiClient;
            CustomprogressDialog cpd;
            public CommonDialog loginCommonDialog;
            public CommonDialog_oneButton loginCommonDialog_oneButton;
            public CommonDialog finishDialog;
            public SharedPreferences Pref;
            public SharedPreferences.Editor edit;
            long connectionTime;
            public String pwd;
            SSMLib ssmLib;

            EditText et_pwd;
            EditText et_id;
    /*▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄*/

            int loginWay = 0;

    /*▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄*/

    /*   로그인 방식 설정    @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@*/

            public Fragment_ID id;
            public Fragment_PIN pin;
            public Fragment_FINGERPRINT fingerprint;
            public FragmentManager fm = getSupportFragmentManager();
            public static String pincode;
            public final int WAY_ID = 0;
            public final int WAY_PIN = 1;
            public final int WAY_FP = 2;
            public ViewPager loginWayViewPager;
            private LoginWayPagerAdapter login_way_PagerAdapter;
            private TabLayout login_way_TabLayout;

            String[] loginway = {"ID/PW로그인", "PIN로그인", "지문 로그인"};

            /*########################################################################   로그인 방식 설정    */


            /*    지문 인식 code   @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@*/
            public static Context mContext;
            //	FingerPrint Authentication
            public KeyStore mKeyStore;
            public KeyGenerator mKeyGenerator;
            public SharedPreferences mSharedPreferences;
            public FingerprintManager fingerprintManager;
            public KeyguardManager keyguardManager;

            //Google FingerPrint
            // 지문인증
            public static final String DIALOG_FRAGMENT_TAG = "myFragment";
            public static final String SECRET_MESSAGE = "Very secret message";
            public static final String KEY_NAME_NOT_INVALIDATED = "key_not_invalidated";
            public static final String DEFAULT_KEY_NAME = "default_key";

            //SamSaung FingerPrint Spass
            public SpassFingerprint mSpassFingerprint;
            public Spass spass;
            public boolean onReadyIdentify = false;
            public boolean isFingerPrintAvailable = false;
            public boolean isSpassIsAvailable = false;
            public boolean isFeatureEnabled_custrom = false;
            public Handler mHandler;
            public static final int MSG_AUTH = 1000;
            public static final int MSG_REGISTER_SAMSUNG = 1004;
            public static final int MSG_REGISTER_GOOGLE = 1008;
            public static final int MSG_AUTH_UI_CUSTOM_TRANSPARENCY = 1010;
            public static final int MSG_NOT_SUPPORT = -1;
            public static final int MSG_GOOGLE_FINGERPRINTT_SUPPORT = 2;


    /*########################################################################   지문 인식 code    */
    /*▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄*/
    /*    security code   @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@*/
        public final int KEY_PWD = 900;

    /*▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄*/


    /*█▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀█*/
    /*█   Override                                                                                                                                                                                       █*/
    /*█▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄█*/
    /*█                                                                                                                                                                                                  █*/
    /*█                                                                                                                                                                                                  █*/
    /*█                                                                                                                                                                                                  █*/
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                Log.v("=========", "--------------------------------------------------------OnCreate--------------------------------------------");
                super.onCreate(savedInstanceState);
                setShared();
                setSharedboolean("FPSTATE", false);
                if(getSharedString("USERPWD").equals("")){
                    setSharedString("FIRSTRUN","Y");
                }
                pincode = getSharedString("PINCODE");
                connectionTime = getSharedlong("CONNECTIONTIME");
                LogMaker.logStart();
                LogMaker.logmaking("connectionTime", String.valueOf(connectionTime));
                LogMaker.logEnd();
                mContext = this;
                spass = new Spass();
                checkHardware_FingerPrintAvailable();//지문인식을 지원하는지 확인을 한다 //SHARED 값 FPSUPPORT 에 "Y"/"N" String값 저장
                setContentView(R.layout.activity_login);


                mHandler = new Handler(this);
                UI_SETTING();
                //FingerSetting(checkHardware_FingerPrintAvailable());
            }

            @Override
            protected void onResume() {
                super.onResume();

            }

            @Override
            protected void onStart() {
                super.onStart();
            }

            @Override
            protected void onPause() {
                super.onPause();
            }

            @Override
            protected void onStop() {
                super.onStop();
            }

            @Override
            protected void onDestroy() {
                super.onDestroy();
            }

            @Override
            public boolean handleMessage(Message message) {
                switch (message.what) {
                    case MSG_REGISTER_GOOGLE:


                        if (!USERPWD.equals("")) {
                            //지문 등록이 안된 구글지원 경우이다
                            if (loginCommonDialog == null) {
                                loginCommonDialog = new CommonDialog(LoginActivity.this, "로그인", getString(R.string.login_fingerprint_register_first), false, new View.OnClickListener() {
                                    @Override

                                    //확인 리스너
                                    public void onClick(View view) {
                                        Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
                                        startActivity(intent);
                                        loginCommonDialog.dismiss();
                                    }
                                }, new View.OnClickListener() {
                                    @Override

                                    //취소 리스너
                                    public void onClick(View view) {
                                        loginCommonDialog.dismiss();
                                        loginWayViewPager.setCurrentItem(0);

                                    }
                                });
                                loginCommonDialog.show();
                            }
                            setSharedString("FPSUPPORT", "Y");
                        } else {
                            if (loginCommonDialog == null) {
                                loginCommonDialog_oneButton = new CommonDialog_oneButton(LoginActivity.this, "로그인", getString(R.string.login_first_lauch_noti), false, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        loginCommonDialog_oneButton.dismiss();
                                        loginWayViewPager.setCurrentItem(0);
                                    }
                                });
                                loginCommonDialog_oneButton.show();
                            }
                        }
                        break;


                    case MSG_REGISTER_SAMSUNG:

                        //지문등록이 안된 삼성SPASS 지원 경우이다.
                        if (!USERPWD.equals("")) {
                            if (loginCommonDialog == null) {
                                loginCommonDialog = new CommonDialog(LoginActivity.this, "로그인", getString(R.string.login_fingerprint_register_first), false, new View.OnClickListener() {
                                    @Override

                                    //확인 리스너
                                    public void onClick(View view) {
                                    /*Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);startActivity(intent);
                                    loginCommonDialog.dismiss();*/
                                        SpassFingerprint.RegisterListener mRegisterListener = new SpassFingerprint.RegisterListener() {
                                            @Override
                                            public void onFinished() {

                                                Log.e(TAG, "RegisterListener.onFinished()");
                                            }
                                        };
                                        mSpassFingerprint.registerFinger(LoginActivity.this, mRegisterListener);

                                    }
                                }, new View.OnClickListener() {
                                    @Override

                                    //취소 리스너
                                    public void onClick(View view) {
                                        loginCommonDialog.dismiss();
                                        loginWayViewPager.setCurrentItem(0);

                                    }
                                });
                                loginCommonDialog.show();
                            }
                            setSharedString("FPSUPPORT", "Y");
                        } else {
                            if (loginCommonDialog_oneButton == null) {
                                loginCommonDialog_oneButton = new CommonDialog_oneButton(LoginActivity.this, "로그인", getString(R.string.login_first_lauch_noti), false, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        loginCommonDialog_oneButton.dismiss();
                                        loginWayViewPager.setCurrentItem(0);
                                    }
                                });
                                loginCommonDialog_oneButton.show();
                            }
                        }
                        break;


                    case MSG_AUTH_UI_CUSTOM_TRANSPARENCY:


                        if (!USERPWD.equals("")) {
                            setSharedString("FPSUPPORT", "Y");
                            samsungFingerPrint();
                        } else {
                            if (loginCommonDialog_oneButton == null) {
                                loginCommonDialog_oneButton = new CommonDialog_oneButton(LoginActivity.this, "로그인", getString(R.string.login_first_lauch_noti), false, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        loginCommonDialog_oneButton.dismiss();
                                        loginWayViewPager.setCurrentItem(0);
                                    }
                                });
                                loginCommonDialog_oneButton.show();
                            }
                        }
                        break;


                    case MSG_GOOGLE_FINGERPRINTT_SUPPORT:

                        if (!USERPWD.equals("")) {
                            setSharedString("FPSUPPORT", "Y");
                            googleFingerPrint();
                        } else {
                            if (loginCommonDialog_oneButton == null) {
                                loginCommonDialog_oneButton = new CommonDialog_oneButton(LoginActivity.this, "로그인", getString(R.string.login_first_lauch_noti), false, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        loginCommonDialog_oneButton.dismiss();
                                        loginWayViewPager.setCurrentItem(0);
                                    }
                                });
                                loginCommonDialog_oneButton.show();
                            }
                        }

                        break;
                    case MSG_NOT_SUPPORT:
                        setSharedString("FPSUPPORT", "N");
                    case MSG_AUTH:
                        break;
                }
                return false;
            }

            @Override
            public void onBackPressed() {

                finish();
            }

            @Override
            public void onClick(View view) {

                switch (view.getId()) {

                    case R.id.userPw:
                        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(et_pwd.getWindowToken(), 0);
                        securityKeyPad();
                        break;

                }
            }
    /*█                                                                                                                                                                                                  █*/
    /*█                                                                                                                                                                                                  █*/
    /*█                                                                                                                                                                                                  █*/
    /*▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀*/


    /*█▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀█*/
    /*█   UISETTING                                                                                                                                                                                      █*/
    /*█▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄█*/
    /*█                                                                                                                                                                                                  █*/
    /*█                                                                                                                                                                                                  █*/
    /*█                                                                                                                                                                                                  █*/
            public void UI_SETTING() {
                LogMaker.logStart();
                LogMaker.logmaking("UISETTING", "START");

                if (getSharedString("FPSUPPORT").equals("Y"))//지문인식 지원 확인 후 viewpager UI 세팅
                {
                    //Initialize logiWayTabLayout
                    id = new Fragment_ID();
                    pin = new Fragment_PIN();
                    fingerprint = new Fragment_FINGERPRINT();
                    login_way_TabLayout = (TabLayout) findViewById(R.id.loginwayTabLayout);
                    login_way_TabLayout.addTab(login_way_TabLayout.newTab().setText("ID/PW로그인"));
                    login_way_TabLayout.addTab(login_way_TabLayout.newTab().setText("PIN 로그인"));
                    login_way_TabLayout.addTab(login_way_TabLayout.newTab().setText("지문 로그인"));
                    logmaking("FingerPrintSupporting", getSharedString("FPSUPPORT").toString());
                } else {
                    //Initialize logiWayTabLayout
                    id = new Fragment_ID();
                    pin = new Fragment_PIN();
                    login_way_TabLayout = (TabLayout) findViewById(R.id.loginwayTabLayout);
                    login_way_TabLayout.addTab(login_way_TabLayout.newTab().setText("ID/PW로그인"));
                    login_way_TabLayout.addTab(login_way_TabLayout.newTab().setText("PIN 로그인"));
                    logmaking("FingerPrintSupporting", getSharedString("FPSUPPORT").toString());
                }

                login_way_TabLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        setIndicator(login_way_TabLayout, 0, 0);
                    }
                });


                //로그인 뷰페이저 초기화
                loginWayViewPager = (ViewPager) findViewById(R.id.loginwayViewpager);

                //Creating loginway pagerAdpter adapter
                login_way_PagerAdapter = new LoginWayPagerAdapter(getSupportFragmentManager(), login_way_TabLayout.getTabCount());
                loginWayViewPager.setAdapter(login_way_PagerAdapter);
                loginWayViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(login_way_TabLayout));
                loginWayViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {

                        for (int i = 0; i < login_way_TabLayout.getTabCount(); i++) { // 상단의 TAB 페이지의 인디케이터 세팅을한다.
                            logStart();
                            logmaking("GET_TABCOUNT", login_way_TabLayout.getTabCount());
                            Log.v("convertView ->", "--------->Position :" + String.valueOf(position) + "     " + i);
                            if (i == position) {
                                View convertView;
                                login_way_TabLayout.getTabAt(i).setCustomView(null);
                                LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                convertView = inflater.inflate(R.layout.tab_bullet, login_way_TabLayout, false);
                                convertView.setVisibility(View.VISIBLE);
                                TextView text;
                                text = (TextView) convertView.findViewById(R.id.text);
                                text.setText(loginway[i]);
                                text.setTextColor(getResources().getColor(R.color.login_tab_bg));
                                login_way_TabLayout.getTabAt(i).setCustomView(convertView);
                            } else {
                                View convertView;
                                login_way_TabLayout.getTabAt(i).setCustomView(null);
                                LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                convertView = inflater.inflate(R.layout.tab_bullet2, login_way_TabLayout, false);
                                TextView text2;
                                text2 = (TextView) convertView.findViewById(R.id.text2);
                                text2.setText(loginway[i]);
                                text2.setTextColor(getResources().getColor(R.color.white));
                                login_way_TabLayout.getTabAt(i).setCustomView(convertView);
                            }

                        }

                    }

                    @Override
                    public void onPageScrollStateChanged(int position) {


                    }
                });
                //Set TabSelectedListener
                login_way_TabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {//탭 선택시 해당 뷰페이저로 이동시킨다.

                        if (getSharedString("USERPWD").equals("") && tab.getPosition() != 0) {

                            //만약 ID 로그인 방법이 아닌 다른 ㅏㅇ이 띄워 진다면 무조건 최초 1회 로그인해야한다는 다이얼로그를 띄운다.
                            if (loginCommonDialog_oneButton == null) {
                                loginCommonDialog_oneButton = new CommonDialog_oneButton(LoginActivity.this, "로그인", getString(R.string.login_first_lauch_noti), false, loginFailedListener);
                                loginCommonDialog_oneButton.show();
                            }
                            login_way_TabLayout.getTabAt(0);

                        } else if (getSharedString("PINCODE").equals("") && tab.getPosition() == 1) {

                            //PIN 창이 선택돘을때 만약에 PIN  코드가 지정 dㅏㄶ았다면 PIN 코드 등록을 유도하는 다이얼로그를 띄운다.
                            if (loginCommonDialog_oneButton == null) {
                                loginCommonDialog_oneButton = new CommonDialog_oneButton(LoginActivity.this, "로그인", getString(R.string.pin_regi_noti), false, loginFailedListener);
                                loginCommonDialog_oneButton.show();
                            }
                            login_way_TabLayout.getTabAt(0);

                        }else if(tab.getPosition()==2 && (checkHardware_FingerPrintAvailable() == 2||checkHardware_FingerPrintAvailable() == 3)  ){

                            //지문인식 창이 선택 됐을 때 만약 지문 등록이 돼 있지 않다면 지문등록을 유도하는 다이얼로그를 띄운다.
                            if (loginCommonDialog_oneButton == null) {
                                loginCommonDialog_oneButton = new CommonDialog_oneButton(LoginActivity.this, "로그인", getString(R.string.fp_regi_content), false, loginFailedListener);
                                loginCommonDialog_oneButton.show();
                            }
                            login_way_TabLayout.getTabAt(0);

                        }
                        else if(tab.getPosition()==2 &&(checkHardware_FingerPrintAvailable()==0)){
                            //지문인식 창이 선택 됐을 때 만약에 삼성 지문인식을 지원한다면 무조건 삼성 지문인식이을 실행한다. 여기에서 실행하는 이유은
                            //프래그먼트 페이저에서 뷰가 미리생성되어 생기는 문제를 막기 위해서이다.
                            loginWayViewPager.setCurrentItem(tab.getPosition());
                            FingerSetting(checkHardware_FingerPrintAvailable());


                        }else {

                            //상위의 조건에 해당사항이 없다면 선택 된 페이지로 이동한다.
                            loginWayViewPager.setCurrentItem(tab.getPosition());

                        }


                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {

                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                        if (getSharedString("USERPWD").equals("") && tab.getPosition() != 0) {

                            // 만약 ID 로그인 방법이 아닌 창이 띄워 진다면 무조건 최초 1회 로그인해야한다는 다이얼로그를 띄운다.
                            if (loginCommonDialog_oneButton == null) {
                                loginCommonDialog_oneButton = new CommonDialog_oneButton(LoginActivity.this, "로그인",
                                        getString(R.string.login_first_lauch_noti), false, loginFailedListener);
                                loginCommonDialog_oneButton.show();
                            }
                            login_way_TabLayout.getTabAt(0);


                        } else if (getSharedString("PINCODE").equals("") && tab.getPosition() == 1) {

                            //PIN 창이 선택 됐을때 만약에 핀코드가 지정 되지 않았다면 PIN코드 등록을 유도하는 다이얼로그를 띄운다.
                            if (loginCommonDialog_oneButton == null) {
                                loginCommonDialog_oneButton = new CommonDialog_oneButton(LoginActivity.this, "로그인",
                                        getString(R.string.pin_regi_noti), false, loginFailedListener);
                                loginCommonDialog_oneButton.show();
                            }
                            login_way_TabLayout.getTabAt(0);

                        } else if(tab.getPosition()==2 && (checkHardware_FingerPrintAvailable() == 2||checkHardware_FingerPrintAvailable() == 3)  ){

                            // 지문인식 창이 선택 됐을 때 만약 지문이 등록 돼있지 않다면 지문등록을 유도하는 다이얼로그를 띄운다.
                            if (loginCommonDialog_oneButton == null) {
                                loginCommonDialog_oneButton = new CommonDialog_oneButton(LoginActivity.this, "로그인",
                                        getString(R.string.fp_regi_content), false, loginFailedListener);
                                loginCommonDialog_oneButton.show();
                            }
                            login_way_TabLayout.getTabAt(0);

                        }else if(tab.getPosition()==2 &&(checkHardware_FingerPrintAvailable()==0)){
                            //지문인식 창이 선택 됐을 때 만약에 삼성 지문인식을 지원한다면 무조건 삼성 지문인식을 실행한다.여기에서 실행하는 이유는
                            // 프래그먼트 페이저에서 뷰가 미리생성되어 생기는 문제를 막기 위해서이다.
                            loginWayViewPager.setCurrentItem(tab.getPosition());
                            FingerSetting(checkHardware_FingerPrintAvailable());


                        }
                        else {
                            //상위의 조건에 해당사항이 없다면 선택 된 페이지로 이동한다.
                            loginWayViewPager.setCurrentItem(tab.getPosition());

                        }

                    }
                });

                //shared에서 로그인 방법을 가저온다. SHAREDPREFERENCES --> USERINFO -->  WAYTOLOGIN  --->  [0:ID로그인] [1:PIN로그인] [2:FINGERPRINT로그인]
                if (getSharedint("WAYTOLOGIN") == 0) {
                    loginWayViewPager.setCurrentItem(0);

                    for (int i = 0; i < login_way_TabLayout.getTabCount(); i++) { // 상단의 TAB 페이지의 인디케이터 세팅을한다.
                        logStart();
                        logmaking("GET_TABCOUNT", login_way_TabLayout.getTabCount());
                        Log.v("convertView ->", "--------->Position :" + String.valueOf(0) + "     " + i);
                        if (i == 0) {
                            View convertView;
                            login_way_TabLayout.getTabAt(i).setCustomView(null);
                            LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            convertView = inflater.inflate(R.layout.tab_bullet, login_way_TabLayout, false);
                            convertView.setVisibility(View.VISIBLE);
                            TextView text;
                            text = (TextView) convertView.findViewById(R.id.text);
                            text.setText(loginway[i]);
                            text.setTextColor(getResources().getColor(R.color.login_tab_bg));
                            login_way_TabLayout.getTabAt(i).setCustomView(convertView);
                        } else {
                            View convertView;
                            login_way_TabLayout.getTabAt(i).setCustomView(null);
                            LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            convertView = inflater.inflate(R.layout.tab_bullet2, login_way_TabLayout, false);
                            TextView text2;
                            text2 = (TextView) convertView.findViewById(R.id.text2);
                            text2.setText(loginway[i]);
                            text2.setTextColor(getResources().getColor(R.color.white));
                            login_way_TabLayout.getTabAt(i).setCustomView(convertView);
                        }
                    }
                } else if (getSharedint("WAYTOLOGIN") == 1) {
                    loginWayViewPager.setCurrentItem(1);
                } else if (getSharedint("WAYTOLOGIN") == 2) {
                    loginWayViewPager.setCurrentItem(2);
                }
                loginWayViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        if (loginCommonDialog_oneButton != null) {
                            loginCommonDialog_oneButton.dismiss();
                            loginCommonDialog_oneButton = null;

                        }
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {


                    }
                });
                //보안 키패드 적용을 위한 세팅
                et_pwd = (EditText) findViewById(R.id.userPw);
                et_pwd.setOnClickListener(this);
                final InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(et_pwd.getWindowToken(), 0);
                et_pwd.setLongClickable(false);
                et_pwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean hasFocus) {
                        switch (view.getId()) {
                            case R.id.userPw:
                                if (hasFocus) {
                                    LogMaker.logmaking("UISETTING] - [LOGINWay", loginWay);
                                    imm.hideSoftInputFromWindow(et_pwd.getWindowToken(), 0);  //Hide Keyboard
                                    securityKeyPad();
                                    LogMaker.logmaking("UISETTING] - [onFocusChanfeListener", "EditPassWord.setOnFocusChageListener");

                                } else {
                                    LogMaker.logmaking("UISETTING] - [LOGINWay", loginWay);
                                    imm.hideSoftInputFromWindow(et_pwd.getWindowToken(), 0);  //Hide Keyboard
                                }
                                break;
                        }
                    }
                });


            }
    /*█                                                                                                                                                                                                  █*/
    /*█                                                                                                                                                                                                  █*/
    /*█                                                                                                                                                                                                  █*/
    /*▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀*/





    /*█▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀█*/
    /*█   FingerPrintSetting                                                                                                                                                                             █*/
    /*█▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄█*/
    /*█                                                                                                                                                                                                  █*/
    /*█                 지문인식이 정상적으로 작동하는지 단말상태를 확인하는것이다. 삼성 지문인식을 써야하는지 확인  해준다                                                                                         █*/
    /*█                 case -99 : 지문인식 불가능함                                                                                                                                                       █*/
    /*█                 case  0  : 삼성 지문인식 가능                                                                                                                                                      █*/
    /*█                 case  1  : 구글 지문인식 가능                                                                                                                                                      █*/
    /*█                 false : 지문인식지원 안됨  -> 삼성 체크(SettingfingerPrintUI)                                                                                                                       █*/
    /*█                 true  : 구글지문인식 지원                                                                                                                                                          █*/

            public String checkFingerPrint() {
                String message = "";

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    keyguardManager = getSystemService(KeyguardManager.class);
                    fingerprintManager = getSystemService(FingerprintManager.class);

        //                if (!keyguardManager.isKeyguardSecure()) {      //화면잠금 설정 되었는지
        //                    message = getString(R.string.set_lock_screen);
        //                }
        //                else{
                    try {
                        if (!fingerprintManager.hasEnrolledFingerprints()) {        //지문 등록 여부 확인
                            message = getString(R.string.set_fingerprint);
                        } else {
                            message = "";
                        }
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    }
        //                }
                }
                return message;
            }

            public int checkHardware_FingerPrintAvailable() {
                LogMaker.logStart();


                final int IMPOSSIBLE = -99;
                final int POSSIBLE = 1;
                final int SAMSUNGPASS = 0;
                final int POSSIBLE_FPNEED = 2;
                final int SAMSUNGPASS_FPNEED = 3;
                int retVal = -12;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    keyguardManager = getSystemService(KeyguardManager.class);
                    fingerprintManager = getSystemService(FingerprintManager.class);
                    LogMaker.logmaking("CHECKSTATE", fingerprintManager.isHardwareDetected());
                    try {
                        if (fingerprintManager.isHardwareDetected()) {
                            isFingerPrintAvailable = true;
                            if (checkFingerPrint().equals("")) {
                                retVal = POSSIBLE;
                                setSharedString("FPSUPPORT", "Y");
                            } else {
                                retVal = POSSIBLE_FPNEED;
                                setSharedString("FPSUPPORT", "Y");
                            }

                        } else {
                            isFingerPrintAvailable = false;
                        }

                    } catch (SecurityException e) {
                        e.printStackTrace();
                    }
                }


                if (isFingerPrintAvailable == false) {
                    mSpassFingerprint = new SpassFingerprint(LoginActivity.this);


                    try {
                        spass.initialize(mContext);
                        isSpassIsAvailable = spass.isFeatureEnabled(Spass.DEVICE_FINGERPRINT);
                        isFeatureEnabled_custrom = spass.isFeatureEnabled(Spass.DEVICE_FINGERPRINT_CUSTOMIZED_DIALOG);
                        if (isSpassIsAvailable == true) {
                            if (mSpassFingerprint.hasRegisteredFinger()) {
                                retVal = SAMSUNGPASS;
                                setSharedString("FPSUPPORT", "Y");
                            } else {
                                retVal = SAMSUNGPASS_FPNEED;
                                setSharedString("FPSUPPORT", "Y");
                            }

                        } else {
                            retVal = IMPOSSIBLE;
                            setSharedString("FPSUPPORT", "N");
                        }
                    } catch (Exception e) {
                        setSharedString("FPSUPPORT", "N");
                        e.printStackTrace();

                    }
                }

                LogMaker.logStart();
                LogMaker.logmaking("ANDROID FINGERPRINT TYPE\n-99 = 지원 불가\n1=지원가능\n0=삼성지문 가능", retVal);
                return retVal;

            }

            /*만약에 CheckHardware 이후 결과에 따른 UI SETTING을 해준다.*/



            public void FingerSetting(int val) {


                switch (val) {
                    case 3:
                        mHandler.sendEmptyMessage(MSG_REGISTER_SAMSUNG);

                        break;
                    //지문등록을 해야한다.
                    case 2:
                        mHandler.sendEmptyMessage(MSG_REGISTER_GOOGLE);
                        break;
                    //지문 인식 불가능
                    case -99:
                        mHandler.sendEmptyMessage(MSG_NOT_SUPPORT);
                        break;

                    //삼성 지문인식
                    case 0:
                        mHandler.sendEmptyMessage(MSG_AUTH_UI_CUSTOM_TRANSPARENCY);
                        break;

                    //지문인식 가능
                    case 1:
                        mHandler.sendEmptyMessage(MSG_GOOGLE_FINGERPRINTT_SUPPORT);
                        break;
                }


            }

            private void setDialogTitleAndTransparency() {
                if (isFeatureEnabled_custrom) {
                    try {
                        if (mSpassFingerprint != null) {
                            mSpassFingerprint.setDialogTitle("모바일오피스 지문인증", 0x555555);
                            mSpassFingerprint.setCanceledOnTouchOutside(false);
                            mSpassFingerprint.setDialogBgTransparency(0);
                        }
                    } catch (IllegalStateException ise) {
                        Toast.makeText(LoginActivity.this, ise.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e(TAG, "setDialogTitleAndTransparency : " + ise.getMessage());
                    }
                }
            }

            private void startIdentifyDialog(boolean backup) {
                if (onReadyIdentify == false) {
                    onReadyIdentify = true;
                    try {
                        if (mSpassFingerprint != null) {
                            mSpassFingerprint.startIdentifyWithDialog(LoginActivity.this, mIdentifyListenerDialog, backup);
                        }
                    } catch (IllegalStateException e) {

                    }
                }
            }

            private SpassFingerprint.IdentifyListener mIdentifyListenerDialog = new SpassFingerprint.IdentifyListener() {
                @Override
                public void onFinished(int eventStatus) {
                    LogMaker.logStart();
                    LogMaker.logmaking("SPASS 지문인식 완료-", eventStatus);

                    int FingerprintIndex = 0;
                    boolean isFailedIdentify = false;
                    onReadyIdentify = false;
                    try {
                        FingerprintIndex = mSpassFingerprint.getIdentifiedFingerprintIndex();
                    } catch (IllegalStateException ise) {
                        ise.printStackTrace();
                    }


                    if (eventStatus == SpassFingerprint.STATUS_AUTHENTIFICATION_SUCCESS) {
                        LogMaker.logmaking("FINGERPRINTINDEX", FingerprintIndex);
                        Pref = getSharedPreferences("USERINFO", MODE_PRIVATE);
                        edit = Pref.edit();
                        edit.putInt("WAYTOLOGIN", 1);   //지문인증
                        edit.apply();

                        LogMaker.logmaking("Password authentification Success", SpassFingerprint.STATUS_AUTHENTIFICATION_PASSWORD_SUCCESS);
                        vibrate(15);
                        LoginAction(getSharedString("USERID"), getSharedString("USERPWD"), WAY_FP, true);

                        //실제적인 로그인이 진행되면 된다.

                    } else if (eventStatus == SpassFingerprint.STATUS_AUTHENTIFICATION_PASSWORD_SUCCESS) // Identify operation succeeded with alternative password
                    {

                    } else if (eventStatus == SpassFingerprint.STATUS_USER_CANCELLED) {
                        LogMaker.logmaking("User cancel this identify", SpassFingerprint.STATUS_USER_CANCELLED);
                    } else if (eventStatus == SpassFingerprint.STATUS_TIMEOUT_FAILED) {
                        LogMaker.logmaking("The time for identify is finished", SpassFingerprint.STATUS_TIMEOUT_FAILED);
                    } else if (!spass.isFeatureEnabled(Spass.DEVICE_FINGERPRINT_AVAILABLE_PASSWORD)) {
                        if (eventStatus == SpassFingerprint.STATUS_BUTTON_PRESSED) {
                            LogMaker.logmaking("User pressed the own button", SpassFingerprint.STATUS_BUTTON_PRESSED);
                            Toast.makeText(mContext, "Please connect own Backup Menu", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        LogMaker.logmaking("Authentification Fail for identify", eventStatus);
                        isFailedIdentify = true;
                    }


                    LogMaker.logEnd();


                }

                @Override
                public void onReady() {
                    LogMaker.logStart();
                    LogMaker.logmaking("", "SPASS 지문인식 대기중 ");
                    LogMaker.logEnd();
                }

                @Override
                public void onStarted() {
                    LogMaker.logStart();
                    LogMaker.logmaking("", "SPASS 지문인식 사용자 지문 스캔중");
                    LogMaker.logEnd();
                }

                @Override
                public void onCompleted() {
                    LogMaker.logStart();
                    LogMaker.logmaking("", "SPASS 지문인식 완료");
                    LogMaker.logEnd();

                }
            };

            public void samsungFingerPrint() {

                LogMaker.logStart();
                LogMaker.logmaking("---------------------------UISETTING", "--------------------");
                LogMaker.logEnd();


                if (!("").equals(getSharedString("USERPWD")))//한번이라도 비밀번호 입력 & 접속 이력이 있는 경우 이용할 수 있다.
                {
                    setDialogTitleAndTransparency();
                    startIdentifyDialog(false);

                } else {
                    setDialogTitleAndTransparency();
                    startIdentifyDialog(false);

                    //원래는 여기에 다이얼로그를 띄워야한다. 최초 로그인 기록이 없으면 지문인식을 이용할 수 없다고 말이다.
                }
            }

            public void googleFingerPrint() {
                LogMaker.logStart();
                LogMaker.logmaking(TAG, "setFIngerINit");
                LogMaker.logEnd();
                try {
                    mKeyStore = KeyStore.getInstance("AndroidKeyStore");
                } catch (KeyStoreException e) {
                    throw new RuntimeException("Failed to get an instnace of KeyStore", e);
                }

                try {
                    mKeyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
                } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
                    throw new RuntimeException("Failed to get an instance of KeyGenerator", e);
                }

                Cipher defaultCipher;
                Cipher cipherNotInvalidated;
                try {
                    defaultCipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
                    cipherNotInvalidated = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
                } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
                    throw new RuntimeException("Failed to get an instance of Cipher", e);
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    //if(vpnConn.getVpnStatus() != Connection_N_Status.LEVEL_CONNECTED.ordinal())
                    Log.i(TAG, "fingerprintManager Init");
                    createKey(DEFAULT_KEY_NAME, true);

                    if (initCipher(defaultCipher, DEFAULT_KEY_NAME)) {
                            /*Log.i(TAG,"==================================fragment show==============================");
                            FingerprintAuthenticationDialogFragment fragment = new FingerprintAuthenticationDialogFragment();
                            fragment.setCancelable(false);
                            fragment.setCryptoObject(new FingerprintManager.CryptoObject(defaultCipher));
                            fragment.setStage(FingerprintAuthenticationDialogFragment.Stage.FINGERPRINT);
                            fragment.show(getFragmentManager(),DIALOG_FRAGMENT_TAG);*/

                        fingerprint.setCryptoObject(new FingerprintManager.CryptoObject(defaultCipher));

                    }

                }

            }

            public void createKey(String keyName, boolean invalidatedByBiometricEnrollment) {
                //The enrolling flow for fingerprint. This is where you ask the user to set up fingerprint
                //for your flow. Use of keys is necessary if you need to know if the set of
                //enrolled fingerprints has changed.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    try {
                        mKeyStore.load(null);
                        // Set the alias of the entry in Android KeyStore where the key will appear
                        // And the constrains (purpose) in the constructor of the Builder
                        KeyGenParameterSpec.Builder builder = new KeyGenParameterSpec.Builder(keyName, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT).setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                                //Require the user to authenticate with a fingerprint to authorize every use
                                //of the key
                                .setUserAuthenticationRequired(true)
                                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7);
                        //This is a workaround to avoid crashes on devices whose API level is < 24
                        //because KeyGenParameterSpec.Builder#setInvalidateByBiometricEnrollment is only
                        //visible on API level +24/
                        //Ideally there should be a compat library for KeyGenParameterSpec.Builder but
                        //which isn't available yet.
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            builder.setInvalidatedByBiometricEnrollment(invalidatedByBiometricEnrollment);
                        }
                        mKeyGenerator.init(builder.build());
                        mKeyGenerator.generateKey();
                    } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException | CertificateException | IOException e) {
                        throw new RuntimeException(e);
                    }

                }
            }

            private boolean initCipher(Cipher cipher, String keyName) {
                try {
                    mKeyStore.load(null);
                    SecretKey key = (SecretKey) mKeyStore.getKey(keyName, null);
                    cipher.init(Cipher.ENCRYPT_MODE, key);
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }

            /**
             * Proceed the purchase operation
             *
             * @param withFingerprint {@code true} if the purchase was made by using a fingerprint
             * @param cryptoObject    the Crypto object
             */
            public void onPurchased(boolean withFingerprint, FingerprintManager.CryptoObject cryptoObject) {
                if (withFingerprint) {
                    //If the user has authenticated with fingerprint, verify that using cryptography and thenn show the confirmation message.
                    assert cryptoObject != null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        tryEncrypt(cryptoObject.getCipher());
                    }

                } else {
                    // Authentication happened with backyp password. Just show the confirmation message.
                    showconfirmation(null);
                }
            }

            // Show confirmation, if fingerprint was used show crypto information.
            private void showconfirmation(byte[] encrypted) {
                if (encrypted != null) {
                    Pref = getSharedPreferences("USERINFO", MODE_PRIVATE);
                    edit = Pref.edit();
                    edit.putInt("WAYTOLOGIN", 1);//지문인증
                    edit.apply();
                    LoginAction(getSharedString("USERID"), getSharedString("USERPWD"), WAY_FP, true);

                }
            }

            private void tryEncrypt(Cipher cipher) {
                Log.v(
                        TAG, "------------tryEncrypt-------------");
                try {
                    byte[] encrypted = cipher.doFinal(SECRET_MESSAGE.getBytes());
                    showconfirmation(encrypted);
                } catch (BadPaddingException | IllegalBlockSizeException e) {
                    Toast.makeText(this, R.string.failed_to_encrypt, Toast.LENGTH_LONG).show();
                    Log.v(TAG, "Failed to encrypt the data with the gnerated key." + e.getMessage());
                }
            }
    /*█                                                                                                                                                                                                  █*/
    /*█                                                                                                                                                                                                  █*/
    /*█                                                                                                                                                                                                  █*/
    /*▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀*/


    /*█▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀█*/
    /*█   Security KeyPad                                                                                                                                                                                █*/
    /*█▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄█*/
    /*█                                                                                                                                                                                                  █*/
    /*█                                                                                                                                                                                                  █*/
    /*█                                                                                                                                                                                                  █*/
             public final void securityKeyPad() {
            LogMaker.logStart();
            LogMaker.logmaking("============", "securityKeyPad===========================");
            Intent intent = new Intent(LoginActivity.this, IxCustomInputActivity.class);
            //옵션설정
            IxCustomInputActivity.setLengthOfInput(intent, 40);  //입력 최대 길이0
            IxCustomInputActivity.setTypeInputQwerty(intent);      //QWERTY 키보드
            IxCustomInputActivity.setTextOfTitle(intent, "계정정보를 입력해주세요.");//입력창 제목
            IxCustomInputActivity.setTextOfHint(intent, "비밀번호");
            startActivityForResult(intent, KEY_PWD);
            LogMaker.logEnd();
        }
    /*█                                                                                                                                                                                                  █*/
    /*█                                                                                                                                                                                                  █*/
    /*█                                                                                                                                                                                                  █*/
    /*▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀*/

    /*█▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀█*/
    /*█   Encrypt                                                                                                                                                                                        █*/
    /*█▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄█*/
    /*█                                                                                                                                                                                                  █*/
    /*█                                                                                                                                                                                                  █*/
    /*█                                                                                                                                                                                                  █*/
            public String Encryptpwd(String inputString) {

            try {
                System.out.println("input : ");
                String src = inputString;
                System.out.println("------------------------------------------------------");
                String test1 = DEFCrypto.getInstance().getSha256Def(src);
                String test2 = DEFCrypto.getInstance(1).getSha256Def(src);
                System.out.println("SHA256+HEX_STRING - LENGTH : " + test1.length());
                System.out.println("RLT :  " + test1);
                System.out.println("------------------------------------------------------");
                System.out.println("SHA256+BASE64ENC   - LENGTH : " + test2.length());
                System.out.println("RLT : " + test2);
                System.out.println("------------------------------------------------------");
                return test1;
            } catch (InvalidFormatException var4) {
                var4.printStackTrace();
                return null;
            } catch (ArrayIndexOutOfBoundsException var5) {
                System.out.println("input is null -USAGE : java Test 1234");
                return null;
            }

        }
    /*█                                                                                                                                                                                                  █*/
    /*█                                                                                                                                                                                                  █*/
    /*█                                                                                                                                                                                                  █*/
    /*▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀*/

    /*█▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀█*/
    /*█  LOGINACTION                               █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █*/
    /*█▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄█*/
    /*█                                                                                                                                                                                                  █*/
    /*█                                                                                                                                                                                                  █*/
    /*█                                                                                                                                                                                                  █*/
            public void LoginAction(final String ID, final String PWD, final int loginWay, final boolean safeID) {



                logmaking("id", ID);
                logmaking("pwd", PWD);
                logmaking("loginWay", loginWay);
                logmaking("safeID", safeID);


                apiInterface = APIClient.getClient().create(APIInterface.class);
                cpd = new CustomprogressDialog(LoginActivity.this, null);
                cpd.show();
                Call<RequestLogin> requestLogin = apiInterface.request_user_info(ID, Encryptpwd(PWD), "A", CommonUtil.getMdn(LoginActivity.this));
                requestLogin.enqueue(new Callback<RequestLogin>() {
                    @Override
                    public void onResponse(Call<RequestLogin> call, Response<RequestLogin> response) {
                        RequestLogin result_response = response.body();

                        //SystemErrocheck
                        String result = result_response.result;
                        String userNm = result_response.userNm;
                        String userType = result_response.userType;
                        String authResult = result_response.authResult;
                        String deptCd = result_response.deptCd;
                        String emailAddr = result_response.emailAddr;
                        String mobile = result_response.mobile;
                        String photoUrl = result_response.photoUrl;
                        String resultMsg = result_response.resultMsg;

                        String lastLoginDt = result_response.lastLoginDt;

                        String secretyKey = result_response.secretKey;
                        String encPwd = result_response.encPwd;
                        String nonceUpdateDt = result_response.nonceUpdateDt;
                        String nonce = result_response.nonce;

                        LogMaker.logmaking("result_response.result", result_response.result);
                        if (result_response.result.toString().equals(RESULT_SUCCECS)) {
                            LogMaker.logmaking("RESUllt", result_response.authResult);
                            switch (result_response.authResult.toString()) {
                                /* [FORMAL_STATE="1000"][NO_USER="1001"][WRONG_PASSWORD="1002"][RETIRED_USER="1005"]  */

                                case FORMAL_STATE:
                                    setSharedString("USERID", ID);
                                    setSharedString("USERPWD", PWD);
                                    setSharedint("WAYTOLOGIN", loginWay);
                                    setSharedboolean("SAVEIDSTATE", safeID);

                                    setSharedString("USERNAME", result_response.userNm);
                                    setSharedString("USERTYPE", result_response.userType);
                                    setSharedString("USERDEPARTURECODE", result_response.deptCd);

                                    setSharedString("LASTLOGINDT", lastLoginDt);
                                    setSharedString("SECURITYKEY", secretyKey);
                                    setSharedString("ENCPWD", encPwd);
                                    setSharedString("NONCEUPDATEDT", nonceUpdateDt);
                                    setSharedString("NONCE", nonce);

                                    contentResolver();// APP_STORE에 사용자 정보 제공
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                    break;

                                case NO_USER:
                                    if (loginCommonDialog_oneButton == null) {
                                        loginCommonDialog_oneButton = new CommonDialog_oneButton(LoginActivity.this, getString(R.string.login_failure), resultMsg
                                                , false, loginFailedListener);
                                        loginCommonDialog_oneButton.show();
                                    }
                                    break;

                                case WRONG_PASSWORD:
                                    if (loginCommonDialog_oneButton == null) {
                                        loginCommonDialog_oneButton = new CommonDialog_oneButton(LoginActivity.this, getString(R.string.login_failure), resultMsg
                                                , false, loginFailedListener);
                                        loginCommonDialog_oneButton.show();
                                    }
                                    break;

                                case RETIRED_USER:
                                    if (loginCommonDialog_oneButton == null) {
                                        loginCommonDialog_oneButton = new CommonDialog_oneButton(LoginActivity.this, getString(R.string.login_failure), resultMsg
                                                , false, loginFailedListener);
                                        loginCommonDialog_oneButton.show();
                                    }
                                    break;
                            }
                        } else if (result.equals(RESULT_ERROR)) {
                            if (loginCommonDialog_oneButton == null) {
                                loginCommonDialog_oneButton = new CommonDialog_oneButton(LoginActivity.this, getString(R.string.login_failure), resultMsg
                                        , false, loginFailedListener);
                                loginCommonDialog_oneButton.show();
                            }

                        }
                        logmaking("result", result);
                        logmaking("userNm", userNm);
                        logmaking("userType", userType);
                        logmaking("authResult", authResult);
                        logmaking("deptCd", deptCd);
                        logmaking("emailAdd", emailAddr);
                        logmaking("mobile", mobile);
                        logmaking("photoUrl", photoUrl);
                        logmaking("resultMsg", resultMsg);
                        try{
                        setSharedint("FPCHECK",checkHardware_FingerPrintAvailable());
                        }catch (Exception e){}
                        cpd.dismiss();
                    }

                    @Override
                    public void onFailure(Call<RequestLogin> call, Throwable t) {
                        cpd.dismiss();
                        if (loginCommonDialog_oneButton == null) {
                            loginCommonDialog_oneButton = new CommonDialog_oneButton(LoginActivity.this, getString(R.string.networkError),
                                    getString(R.string.check_network), false, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    loginCommonDialog_oneButton.dismiss();
                                    loginCommonDialog_oneButton = null;
                                    finish();
                                }
                            });
                            loginCommonDialog_oneButton.show();
                        }
                    }

                });


            }


            public View.OnClickListener loginFailedListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loginWayViewPager.setCurrentItem(0);

                    if (loginCommonDialog_oneButton != null){
                        loginCommonDialog_oneButton.dismiss();
                        loginCommonDialog_oneButton = null;
                    }
                }
            };

            public void contentResolver() {
                ContentResolver contentResolver;
                contentResolver = getContentResolver();
                ContentValues val = new ContentValues();
                val.put("USERID", getSharedString("USERID"));
                val.put("USERNM", getSharedString("USERNAME"));
                val.put("USERTYPE", getSharedString("USERTYPE"));
                val.put("PLATFORMCD", "A");
                getContentResolver().insert(Uri.parse("content://com.ex.store.provider.userappinfo" + "/USER_INFO"), val);

                Cursor cursor = contentResolver.query(
                        Uri.parse("content://com.ex.store.provider.userappinfo/APP_INFO"),
                        null,
                        null, null, null
                );
                String appId, appName, packageName, appVer, appCondition;
                while (cursor.moveToNext()) {
                    appId = cursor.getString(1);
                    appName = cursor.getString(2);
                    packageName = cursor.getString(3);
                    appVer = cursor.getString(4);
                    //appCondition = cursor.getString(5);
                    String str = appId + ", " + appName + ", " + packageName + ", " + appVer + ", " /*+ appCondition*/;
                    System.out.println(str);
                }




            }
    /*█                                                                                                                                                                                                  █*/
    /*█                                                                                                                                                                                                  █*/
    /*█                                                                                                                                                                                                  █*/
    /*▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀*/


    /*█▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀█*/
    /*█ Login WayPager                                                                                                                                                                                   █*/
    /*█▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄█*/
    /*█                                                                                                                                                                                                  █*/
    /*█                                                                                                                                                                                                  █*/
    /*█                                                                                                                                                                                                  █*/
            public class LoginWayPagerAdapter extends FragmentPagerAdapter {

                //Count number of tabs
                private int tabCount;

                public LoginWayPagerAdapter(FragmentManager fm, int tabCount) {
                    super(fm);
                    this.tabCount = tabCount;
                }

                @Override
                public Fragment getItem(int position) {
                    //Returning the current tabs


                    switch (position) {

                        case 0:
                            return id;
                        case 1:
                            return pin;
                        case 2:
                            return fingerprint;
                        default:
                            return null;
                    }


                }

                @Override
                public int getCount() {
                    return tabCount;
                }
            }

            //setting loginPagertab indicator custom
            //tablayout의 크기를 조절한다.
            public void setIndicator(TabLayout tabs, int leftDip, int rightDip) {
                Class tabLayout = tabs.getClass();
                Field tabStrip = null;

                try {
                    tabStrip = tabLayout.getDeclaredField("mTabStrip");
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
                tabStrip.setAccessible(true);
                LinearLayout llTab = null;
                try {
                    llTab = (LinearLayout) tabStrip.get(tabs);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
                int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());

                for (int i = 0; i < tabs.getTabCount(); i++) {

                    View child = llTab.getChildAt(i);
                    child.setPadding(0, 0, 0, 0);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
                    params.leftMargin = left;
                    params.rightMargin = right;
                    child.setLayoutParams(params);
                    child.invalidate();

                }


            }

    /*█                                                                                                                                                                                                  █*/
    /*█                                                                                                                                                                                                  █*/
    /*█                                                                                                                                                                                                  █*/
    /*▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀*/


}
