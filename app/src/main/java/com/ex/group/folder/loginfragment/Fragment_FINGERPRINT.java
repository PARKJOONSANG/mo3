package com.ex.group.folder.loginfragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.fingerprint.FingerprintManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ex.group.folder.LoginActivity;
import com.ex.group.folder.dialog.CommonDialog_oneButton;
import com.ex.group.folder.fingerprint.FingerprintUiHelper;

import com.ex.group.folder.R;
import com.skt.pe.common.vpn.SGVPNConnection;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment_FINGERPRINT.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment_FINGERPRINT#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_FINGERPRINT extends Fragment implements TextView.OnEditorActionListener, FingerprintUiHelper.Callback {

    private static String TAG = "████████  FRAGMENT_FINGERPRINT ████████    ";
    private View mFingerprintContent;
    private View mBackupContent;
    private EditText mPassword;
    private CheckBox mUseFingerprintFutureCheckBox;
    private TextView mPasswordDescriptionTextView;
    private TextView mNewFingerprintEnrolledTextView;
    private boolean state = false;

    private Stage mStage = Stage.FINGERPRINT;

    private FingerprintManager.CryptoObject mCryptoObject;
    private FingerprintUiHelper mFingerprintUiHelper;
    private LoginActivity mActivity;

    private InputMethodManager mInputMethodManager;
    private SharedPreferences mSharedPreferences;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Fragment_FINGERPRINT() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_FINGERPRINT.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_FINGERPRINT newInstance(String param1, String param2) {
        Fragment_FINGERPRINT fragment = new Fragment_FINGERPRINT();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.v(TAG, "onAttach ");

        mActivity = (LoginActivity) getActivity();
        mInputMethodManager = context.getSystemService(InputMethodManager.class);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        Log.v(TAG, "onCreate ");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.v(TAG, "onCreateView ");

        View v = inflater.inflate(R.layout.fragment__fingerprint, container, false);


        mFingerprintContent = v.findViewById(R.id.fingerprint_container);
        mBackupContent = v.findViewById(R.id.backup_container);
        mPassword = (EditText) v.findViewById(R.id.password);
        mPassword.setOnEditorActionListener(this);
        mPasswordDescriptionTextView = (TextView) v.findViewById(R.id.password_description);
        mUseFingerprintFutureCheckBox = (CheckBox) v.findViewById(R.id.use_fingerprint_in_future_check);
        mNewFingerprintEnrolledTextView = (TextView) v.findViewById(R.id.new_fingerprint_enrolled_description);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mFingerprintUiHelper = new FingerprintUiHelper(mActivity, mActivity.getSystemService(FingerprintManager.class), (ImageView) v.findViewById(R.id.fingerprint_icon), (TextView) v.findViewById(R.id.fingerprint_status), this);
        }
        updateStage();
        if (!mFingerprintUiHelper.isFingerprintAuthAvailable()) {
            goToBackup();
        }
        return v;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.v(TAG, "onActivity created");

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.v(TAG, "onStart ");

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v(TAG, "onResume ");


        if (((LoginActivity) getActivity()).checkHardware_FingerPrintAvailable() == 1 ) {

            if (getUserVisibleHint()) {

                Log.v(TAG, " [INVISIBLE]");
                try {

                    ((LoginActivity) getActivity()).FingerSetting(((LoginActivity) getActivity()).checkHardware_FingerPrintAvailable());


                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                if (((LoginActivity) getActivity()).getSharedString("USERPWD").equals("")) {

                } else {

                    try {

                        ((LoginActivity) getActivity()).FingerSetting(((LoginActivity) getActivity()).checkHardware_FingerPrintAvailable());


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            }

        }


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /**
     * Sets the crypto object to be passed in when authenticating with fingerprint.
     */
    public void setCryptoObject(FingerprintManager.CryptoObject cryptoObject) {
        mCryptoObject = cryptoObject;
        mFingerprintUiHelper.startListening(mCryptoObject);


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        Log.v(TAG, "setUserVisibleHint ");
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            Log.v(TAG, " [ visibilty : VISIBLE ]");

        } else {


            if (state = true) {
                Log.v(TAG, " [ visibilty : INVISIBLE ]");
                /*if (mFingerprintUiHelper != null)
                    mFingerprintUiHelper.stopListening();

            */

            }
            state = false;

        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.v(TAG, "onStop ");

    }

    @Override
    public void onPause() {
        super.onPause();
        state = false;
        Log.v(TAG, "onPause ");

        if (getUserVisibleHint()) {
            Log.v(TAG, "[  visibilty : YES  ]");
        } else {
            if (state = true) {
                Log.v(TAG, "[  visibilty : NO  ]");
                if (mFingerprintUiHelper != null)
                    mFingerprintUiHelper.stopListening()
                            ;
            }
            state = false;

        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "onDestroy ");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
        if (actionId == EditorInfo.IME_ACTION_GO) {
            verifyPassword();
            return true;
        }
        return false;
    }

    @Override
    public void onAuthenticated() {
        // Callback from FingerprintUiHelper. Let the activity know that authentication was
        // successful.
        mActivity.onPurchased(true /* withFingerprint */, mCryptoObject);

    }

    @Override
    public void onError() {
        if (FingerprintUiHelper.err == false) {
            FingerprintUiHelper.err = true;
            mActivity.loginWayViewPager.setCurrentItem(0);
        } else
            goToBackup();

    }

    private void updateStage() {
        switch (mStage) {
            case FINGERPRINT:
                //mFingerprintContent.setVisibility(View.VISIBLE);
                //mBackupContent.setVisibility(View.GONE);
                break;
            case NEW_FINGERPRINT_ENROLLED:
                // Intentional fall through
            case PASSWORD:
                //  Toast.makeText(getContext(), R.string.set_fingerprint, Toast.LENGTH_SHORT).show();
//                mCancelButton.setText(R.string.cancel);
//                mSecondDialogButton.setText(R.string.confirm);
//                mFingerprintContent.setVisibility(View.GONE);
//                mBackupContent.setVisibility(View.VISIBLE);
//                if (mStage == Stage.NEW_FINGERPRINT_ENROLLED) {
//                    mPasswordDescriptionTextView.setVisibility(View.GONE);
//                    mNewFingerprintEnrolledTextView.setVisibility(View.VISIBLE);
//                    mUseFingerprintFutureCheckBox.setVisibility(View.VISIBLE);
//                }
                break;
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    /**
     * Checks whether the current entered password is correct, and dismisses the the dialog and
     * let's the activity know about the result.
     */
    private void verifyPassword() {
        if (!checkPassword(mPassword.getText().toString())) {
            return;
        }
        if (mStage == Stage.NEW_FINGERPRINT_ENROLLED) {
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putBoolean(getString(R.string.use_fingerprint_to_authenticate_key), mUseFingerprintFutureCheckBox.isChecked());
            editor.apply();

            if (mUseFingerprintFutureCheckBox.isChecked()) {
                // Re-create the key so that fingerprints including new ones are validated.
                mActivity.createKey(LoginActivity.DEFAULT_KEY_NAME, true);
                mStage = Stage.FINGERPRINT;
            }
        }
        mPassword.setText("");
        mActivity.onPurchased(false /* without Fingerprint */, null);

    }

    /**
     * @return true if {@code password} is correct, false otherwise
     */
    private boolean checkPassword(String password) {
        // Assume the password is always correct.
        // In the real world situation, the password needs to be verified in the server side.
        return password.length() > 0;
    }

    private void goToBackup() {
        mStage = Stage.FINGERPRINT;
        updateStage();
        //mPassword.requestFocus();
        // Fingerprint is not used anymore. Stop listening for it.
        mFingerprintUiHelper.stopListening();
        mFingerprintUiHelper.startListening(mCryptoObject);
    }

    private final Runnable mShowKeyboardRunnable = new Runnable() {
        @Override
        public void run() {
            mInputMethodManager.showSoftInput(mPassword, 0);
        }
    };

    /**
     * Enumeration to indicate which authentication method the user is trying to authenticate with.
     */
    public enum Stage {
        FINGERPRINT,
        NEW_FINGERPRINT_ENROLLED,
        PASSWORD
    }
}
