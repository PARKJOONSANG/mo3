/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package com.ex.group.folder.fingerprint;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.ex.group.folder.R;

/**
 * Small helper class to manage text/icon around fingerprint authentication UI.
 */
@TargetApi(Build.VERSION_CODES.M)
public class FingerprintUiHelper extends FingerprintManager.AuthenticationCallback {

    private AnimationDrawable waitResponse;
    private Animation shake;
    private boolean errorState=false;
    public static boolean err;
    private static final long ERROR_TIMEOUT_MILLIS = 800;
    private static final long SUCCESS_DELAY_MILLIS = 800;


    private final FingerprintManager mFingerprintManager;
    private final ImageView mIcon;
    private final TextView mErrorTextView;
    private final Callback mCallback;
    private CancellationSignal mCancellationSignal;


    private boolean mSelfCancelled;

    /**
     * Constructor for {@link FingerprintUiHelper}.
     */
    public FingerprintUiHelper(Context context,FingerprintManager fingerprintManager, ImageView icon, TextView errorTextView, Callback callback) {
        mFingerprintManager = fingerprintManager;
        mIcon = icon;
        shake=AnimationUtils.loadAnimation(context,R.anim.wrong);
        mErrorTextView = errorTextView;
        mCallback = callback;

    }

    public boolean isFingerprintAuthAvailable() {
        // The line below prevents the false positive inspection from Android Studio
        // noinspection ResourceType
        return mFingerprintManager.isHardwareDetected() && mFingerprintManager.hasEnrolledFingerprints();
    }

    public void startListening(FingerprintManager.CryptoObject cryptoObject) {
        if (!isFingerprintAuthAvailable()) {



            return;
        }else{
        mCancellationSignal = new CancellationSignal();
        mSelfCancelled = false;
        // The line below prevents the false positive inspection from Android Studio
        // noinspection ResourceType
        mFingerprintManager.authenticate(cryptoObject, mCancellationSignal, 0 /* flags */, this, null);
        if(errorState!=true){
            errorState=false;
             mIcon.setImageResource(R.drawable.login_fingerprint_blinkingicon);
        mIcon.setScaleType(ImageView.ScaleType.FIT_CENTER);
        waitResponse =(AnimationDrawable)mIcon.getDrawable();
        mErrorTextView.setText("");
        mIcon.post(new Runnable() {
            @Override
            public void run() {
                waitResponse.start();
            }
        });

        }

        }
    }

    public void stopListening() {
        if (mCancellationSignal != null) {
            mSelfCancelled = true;
            mCancellationSignal.cancel();
            mCancellationSignal = null;
        }
    }

    @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString) {
        if (!mSelfCancelled) {
            if(errMsgId==5){err=false;}else{err=true;}
            showError(errMsgId,errString);
            mIcon.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mCallback.onError();
                }
            }, ERROR_TIMEOUT_MILLIS);
        }
    }

    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        showError(helpMsgId,helpString);
    }

    @Override
    public void onAuthenticationFailed() {
        showError(1,mIcon.getResources().getString(
                R.string.fingerprint_not_recognized));
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        mErrorTextView.removeCallbacks(mResetErrorTextRunnable);
        mIcon.setImageResource(R.drawable.ic_fingerprint_success);
//        mIcon.setImageResource(R.drawable.ic_fp_40px2);  //아이콘 변경
        mErrorTextView.setTextColor(
                mErrorTextView.getResources().getColor(R.color.success_color2, null));
        mErrorTextView.setText(
                mErrorTextView.getResources().getString(R.string.fingerprint_success));
        mIcon.postDelayed(new Runnable() {
            @Override
            public void run() {
                mCallback.onAuthenticated();

            }
        }, SUCCESS_DELAY_MILLIS);
    }

    /*
    *  public static final int FINGERPRINT_ACQUIRED_GOOD = 0;
    public static final int FINGERPRINT_ACQUIRED_IMAGER_DIRTY = 3;
    public static final int FINGERPRINT_ACQUIRED_INSUFFICIENT = 2;
    public static final int FINGERPRINT_ACQUIRED_PARTIAL = 1;
    public static final int FINGERPRINT_ACQUIRED_TOO_FAST = 5;
    public static final int FINGERPRINT_ACQUIRED_TOO_SLOW = 4;
    public static final int FINGERPRINT_ERROR_CANCELED = 5;
    public static final int FINGERPRINT_ERROR_HW_UNAVAILABLE = 1;
    public static final int FINGERPRINT_ERROR_LOCKOUT = 7;
    public static final int FINGERPRINT_ERROR_NO_SPACE = 4;
    public static final int FINGERPRINT_ERROR_TIMEOUT = 3;
    public static final int FINGERPRINT_ERROR_UNABLE_TO_PROCESS = 2;
    * */

    private void showError(int Statemsg,CharSequence error) {
        Log.v("WHAT THE ERROR","--->"+error+"   msg = "+Statemsg);
        if(Statemsg==5||Statemsg==7 ||Statemsg==9){
            mIcon.setImageResource(R.drawable.fingerprint_wrong);
            errorState = true;
        }else{
            mIcon.setImageResource(R.drawable.fingerprint_wrong);
            mIcon.startAnimation(shake);
            mErrorTextView.removeCallbacks(mResetErrorTextRunnable);

        }
        if(Statemsg==5){}else{
        mErrorTextView.setText(error);
        mErrorTextView.setTextColor(
                mErrorTextView.getResources().getColor(R.color.warning_color, null));
        mErrorTextView.postDelayed(mResetErrorTextRunnable, ERROR_TIMEOUT_MILLIS);}

    }

    private Runnable mResetErrorTextRunnable = new Runnable() {
        @Override
        public void run() {
            if(errorState==true){
                Log.v("Now Error STATE : ","--"+errorState);

            }else{

            mErrorTextView.setTextColor(
                    mErrorTextView.getResources().getColor(R.color.hint_color, null));
            mErrorTextView.setText(
                    mErrorTextView.getResources().getString(R.string.fingerprint_hint));
            mIcon.setImageResource(R.drawable.login_fingerprint_blinkingicon);
            mIcon.setScaleType(ImageView.ScaleType.FIT_CENTER);
            waitResponse =(AnimationDrawable)mIcon.getDrawable();
            mIcon.post(new Runnable() {
                @Override
                public void run() {
                    waitResponse.start();
                }
            });
        }
        }
    };

    public interface Callback {

        void onAuthenticated();

        void onError();
    }

    public class getErrorMsg{
        public String getHelpError() {
            return helpError;
        }

        public void setHelpError(String helpError) {
            this.helpError = helpError;
        }

        public String getError() {
            return Error;
        }

        public void setError(String error) {
            Error = error;
        }

        String helpError;
        String Error;


    }
}
