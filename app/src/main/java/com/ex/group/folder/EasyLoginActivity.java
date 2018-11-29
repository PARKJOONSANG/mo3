package com.ex.group.folder;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ex.group.folder.dialog.CommonDialog;
import com.ex.group.folder.dialog.CommonDialog_oneButton;
import com.ex.group.folder.utility.BaseActivity;

import com.ex.group.folder.R;
import com.ex.group.folder.utility.LogMaker;
import com.samsung.android.sdk.pass.Spass;
import com.samsung.android.sdk.pass.SpassFingerprint;

import java.security.KeyStore;

import javax.crypto.KeyGenerator;

public class EasyLoginActivity extends BaseActivity {
    private final String TAG =" ====== EASYLOGINACTIVITY ======";
    private ImageView button_prev;
    private TextView pin_set;
    private LinearLayout login_setting_id, login_setting_pin, login_setting_fp;
    private LinearLayout login_pin;
    private TextView id_text, pin_text, fp_text;
    private RadioButton id_radio, pin_radio, fp_radio;
    private boolean id_bool = false, pin_bool = false, fp_bool = false;
    private final int ID = 0, PIN = 1, FP = 2;
    private CommonDialog cd;
    private CommonDialog_oneButton cdone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_easy_login);
        spass = new Spass();
        UISETTING();
        mContext=EasyLoginActivity.this;
        int state = getSharedint("WAYTOLOGIN");
        switch (state) {

            case 0:
                id_bool = true;
                pin_bool = false;
                fp_bool = false;

                id_radio.setChecked(true);
                pin_radio.setChecked(false);
                fp_radio.setChecked(false);


                login_setting_id.setBackground(getResources().getDrawable(R.drawable.menu_slide_easylogin_setting_c));
                login_setting_pin.setBackground(getResources().getDrawable(R.drawable.menu_slide_easylogin_setting));
                login_setting_fp.setBackground(getResources().getDrawable(R.drawable.menu_slide_easylogin_setting));

                id_text.setTextColor(getResources().getColor(R.color.main_title_bg));
                pin_text.setTextColor(getResources().getColor(R.color.login_id_edit_line));
                fp_text.setTextColor(getResources().getColor(R.color.login_id_edit_line));
                break;
            case 1:
                id_bool = false;
                pin_bool = true;
                fp_bool = false;

                id_radio.setChecked(false);
                pin_radio.setChecked(true);
                fp_radio.setChecked(false);


                login_setting_id.setBackground(getResources().getDrawable(R.drawable.menu_slide_easylogin_setting));
                login_setting_pin.setBackground(getResources().getDrawable(R.drawable.menu_slide_easylogin_setting_c));
                login_setting_fp.setBackground(getResources().getDrawable(R.drawable.menu_slide_easylogin_setting));

                id_text.setTextColor(getResources().getColor(R.color.login_id_edit_line));
                pin_text.setTextColor(getResources().getColor(R.color.main_title_bg));
                fp_text.setTextColor(getResources().getColor(R.color.login_id_edit_line));

                break;
            case 2:
                id_bool = false;
                pin_bool = false;
                fp_bool = true;

                id_radio.setChecked(false);
                pin_radio.setChecked(false);
                fp_radio.setChecked(true);


                login_setting_id.setBackground(getResources().getDrawable(R.drawable.menu_slide_easylogin_setting));
                login_setting_pin.setBackground(getResources().getDrawable(R.drawable.menu_slide_easylogin_setting));
                login_setting_fp.setBackground(getResources().getDrawable(R.drawable.menu_slide_easylogin_setting_c));

                id_text.setTextColor(getResources().getColor(R.color.login_id_edit_line));
                pin_text.setTextColor(getResources().getColor(R.color.login_id_edit_line));
                fp_text.setTextColor(getResources().getColor(R.color.main_title_bg));


                break;
            default:

                break;
        }

        Log.v(TAG,String.valueOf(getSharedint("FPCHECK")));
        if(getSharedint("FPCHECK")==-99  || getSharedint("FPCHECK")==-12){
            login_setting_fp.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setSharedint("FPCHECK",checkHardware_FingerPrintAvailable());
        if(getSharedint("WAYTOLOGIN")==2&&(getSharedint("FPCHECK")==2||getSharedint("FPCHECK")==3)){

            id_bool = true;
            pin_bool = false;
            fp_bool = false;

            id_radio.setChecked(true);
            pin_radio.setChecked(false);
            fp_radio.setChecked(false);


            login_setting_id.setBackground(getResources().getDrawable(R.drawable.menu_slide_easylogin_setting_c));
            login_setting_pin.setBackground(getResources().getDrawable(R.drawable.menu_slide_easylogin_setting));
            login_setting_fp.setBackground(getResources().getDrawable(R.drawable.menu_slide_easylogin_setting));

            id_text.setTextColor(getResources().getColor(R.color.main_title_bg));
            pin_text.setTextColor(getResources().getColor(R.color.login_id_edit_line));
            fp_text.setTextColor(getResources().getColor(R.color.login_id_edit_line));

            setSharedint("WAYTOLOGIN", 0);

        }
    }

    private void UISETTING() {

        login_setting_id = (LinearLayout) findViewById(R.id.login_setting_id);
        login_setting_pin = (LinearLayout) findViewById(R.id.login_setting_pin);
        login_setting_fp = (LinearLayout) findViewById(R.id.login_setting_fp);
        login_pin = (LinearLayout) findViewById(R.id.login_pin);

        id_text = (TextView) findViewById(R.id.id_text);
        pin_text = (TextView) findViewById(R.id.pin_text);
        fp_text = (TextView) findViewById(R.id.fp_text);

        id_radio = (RadioButton) findViewById(R.id.id_radio);
        pin_radio = (RadioButton) findViewById(R.id.pin_radio);
        fp_radio = (RadioButton) findViewById(R.id.fp_radio);
        pin_set = (TextView) findViewById(R.id.pin_set);
        id_radio.setClickable(false);
        pin_radio.setClickable(false);
        fp_radio.setClickable(false);

        pin_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EasyLoginActivity.this, PIN_REGISTERActivity.class);
                startActivity(intent);
            }
        });
        button_prev = (ImageView) findViewById(R.id.button_prev);
        button_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                vibrate(15);
            }
        });

        login_setting_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (id_bool == false) {
                    id_bool = true;
                    pin_bool = false;
                    fp_bool = false;

                    id_radio.setChecked(true);
                    pin_radio.setChecked(false);
                    fp_radio.setChecked(false);


                    login_setting_id.setBackground(getResources().getDrawable(R.drawable.menu_slide_easylogin_setting_c));
                    login_setting_pin.setBackground(getResources().getDrawable(R.drawable.menu_slide_easylogin_setting));
                    login_setting_fp.setBackground(getResources().getDrawable(R.drawable.menu_slide_easylogin_setting));

                    id_text.setTextColor(getResources().getColor(R.color.main_title_bg));
                    pin_text.setTextColor(getResources().getColor(R.color.login_id_edit_line));
                    fp_text.setTextColor(getResources().getColor(R.color.login_id_edit_line));
                    vibrate(10);
                    checkLoginState(0);

                }

            }
        });
        login_pin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pin_bool == false) {
                    id_bool = false;
                    pin_bool = true;
                    fp_bool = false;

                    id_radio.setChecked(false);
                    pin_radio.setChecked(true);
                    fp_radio.setChecked(false);


                    login_setting_id.setBackground(getResources().getDrawable(R.drawable.menu_slide_easylogin_setting));
                    login_setting_pin.setBackground(getResources().getDrawable(R.drawable.menu_slide_easylogin_setting_c));
                    login_setting_fp.setBackground(getResources().getDrawable(R.drawable.menu_slide_easylogin_setting));

                    id_text.setTextColor(getResources().getColor(R.color.login_id_edit_line));
                    pin_text.setTextColor(getResources().getColor(R.color.main_title_bg));
                    fp_text.setTextColor(getResources().getColor(R.color.login_id_edit_line));
                    vibrate(10);
                    checkLoginState(1);

                }

            }
        });
        login_setting_fp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fp_bool == false) {
                    id_bool = false;
                    pin_bool = false;
                    fp_bool = true;

                    id_radio.setChecked(false);
                    pin_radio.setChecked(false);
                    fp_radio.setChecked(true);


                    login_setting_id.setBackground(getResources().getDrawable(R.drawable.menu_slide_easylogin_setting));
                    login_setting_pin.setBackground(getResources().getDrawable(R.drawable.menu_slide_easylogin_setting));
                    login_setting_fp.setBackground(getResources().getDrawable(R.drawable.menu_slide_easylogin_setting_c));

                    id_text.setTextColor(getResources().getColor(R.color.login_id_edit_line));
                    pin_text.setTextColor(getResources().getColor(R.color.login_id_edit_line));
                    fp_text.setTextColor(getResources().getColor(R.color.main_title_bg));


                    vibrate(10);
                    checkLoginState(2);

                }

            }
        });
    }
    private void registerFingerprint() {
        if (onReadyIdentify == false) {
            if (onReadyEnroll == false) {
                onReadyEnroll = true;
                if (mSpassFingerprint != null) {
                    mSpassFingerprint.registerFinger(EasyLoginActivity.this, mRegisterListener);
                }
                Log.e("", "Jump to the Enroll screen");
            } else {
                Log.e("", "Please wait and try to register again");
            }
        } else {
            Log.e("", "Please cancel Identify first");
        }
    }
    private void checkLoginState(int i) {

        switch (i) {

            case 0:
                setSharedint("WAYTOLOGIN", 0);


                break;

            case 1:
                if (getSharedString("PINCODE").equals("")) {
                    cd = new CommonDialog(EasyLoginActivity.this, getString(R.string.pin_regi_title), getString(R.string.pin_regi_content), false,
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {// 확인 리스너
                                    Intent intent = new Intent(EasyLoginActivity.this, PIN_REGISTERActivity.class);
                                    startActivity(intent);
                                    finish();
                                    cd.dismiss();
                                }
                            },
                            new View.OnClickListener() {
                                @Override


                                public void onClick(View view) {

                                    cd.dismiss();

                                }
                            });
                    cd.show();
                } else {
                    setSharedint("WAYTOLOGIN", 1);
                }


                break;

            case 2:
                int fpCheck = getSharedint("FPCHECK");
                Log.d(TAG,String.valueOf(fpCheck));
                /*sharedint FPCHECK
                 *
                 * case samsumgpossible                        0
                 * case possible                               1
                 * case impossible                             -99
                 * case possible but need register             2
                 * case possible but need samsung register     3
                 * */

                        switch (fpCheck) {
                            case 0:

                                setSharedint("WAYTOLOGIN", 2);
                                break;


                            case 1:

                                setSharedint("WAYTOLOGIN", 2);
                                break;


                            case 2:

                                    //구글 지문등록 안됨
                                    cd = new CommonDialog(EasyLoginActivity.this, getString(R.string.fp_regi_title), getString(R.string.inform_fingerprint), true,
                                        new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
                                                startActivity(intent);
                                                setSharedint("WAYTOLOGIN", 2);
                                                cd.dismiss();
                                            }
                                        },
                                        new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                                cd.dismiss();

                                        id_bool = true;
                                        pin_bool = false;
                                        fp_bool = false;

                                        id_radio.setChecked(true);
                                        pin_radio.setChecked(false);
                                        fp_radio.setChecked(false);


                                        login_setting_id.setBackground(getResources().getDrawable(R.drawable.menu_slide_easylogin_setting_c));
                                        login_setting_pin.setBackground(getResources().getDrawable(R.drawable.menu_slide_easylogin_setting));
                                        login_setting_fp.setBackground(getResources().getDrawable(R.drawable.menu_slide_easylogin_setting));

                                        id_text.setTextColor(getResources().getColor(R.color.main_title_bg));
                                        pin_text.setTextColor(getResources().getColor(R.color.login_id_edit_line));
                                        fp_text.setTextColor(getResources().getColor(R.color.login_id_edit_line));

                                        setSharedint("WAYTOLOGIN", 0);
                                    }
                                });
                                cd.show();
                                break;

                            case 3:

                                //삼성 지문등록 안됨
                                cd = new CommonDialog(EasyLoginActivity.this, getString(R.string.fp_regi_title), getString(R.string.inform_fingerprint), true,
                                    new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            registerFingerprint();
                                            setSharedint("WAYTOLOGIN", 2);
                                            cd.dismiss();

                                        }
                                    },
                                    new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            cd.dismiss();

                                            id_bool = true;
                                            pin_bool = false;
                                            fp_bool = false;

                                            id_radio.setChecked(true);
                                            pin_radio.setChecked(false);
                                            fp_radio.setChecked(false);


                                            login_setting_id.setBackground(getResources().getDrawable(R.drawable.menu_slide_easylogin_setting_c));
                                            login_setting_pin.setBackground(getResources().getDrawable(R.drawable.menu_slide_easylogin_setting));
                                            login_setting_fp.setBackground(getResources().getDrawable(R.drawable.menu_slide_easylogin_setting));

                                            id_text.setTextColor(getResources().getColor(R.color.main_title_bg));
                                            pin_text.setTextColor(getResources().getColor(R.color.login_id_edit_line));
                                            fp_text.setTextColor(getResources().getColor(R.color.login_id_edit_line));

                                            setSharedint("WAYTOLOGIN", 0);
                                        }
                                    });
                                cd.show();
                                break;


                        }




                break;


        }


    }



    /*    지문 인식 code   @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@*/
    public  Context mContext;
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
    public boolean onReadyEnroll = false;
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
    public SpassFingerprint.RegisterListener mRegisterListener = new SpassFingerprint.RegisterListener() {
        @Override
        public void onFinished() {
            onReadyEnroll = false;
            Log.e("", "RegisterListener.onFinished()");
        }
    };
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
            mSpassFingerprint = new SpassFingerprint(EasyLoginActivity.this);


            try {
                spass.initialize(EasyLoginActivity.this);
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

}
