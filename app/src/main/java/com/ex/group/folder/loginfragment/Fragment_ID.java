package com.ex.group.folder.loginfragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ex.group.folder.LoginActivity;
import com.ex.group.folder.utility.LogMaker;
import com.nprotect.keycryptm.IxCustomInputActivity;

import com.ex.group.folder.R;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;


public class Fragment_ID extends Fragment  {

    private Activity mActivity = (LoginActivity)getActivity();
    /*UI PARAM INITIALIZING*/
    private EditText edit_ID;
    private EditText edit_PW;
    private TextView text_help;
    private TextView btn_login;
    private LinearLayout linear_pw, linear_id;
    private CheckBox login_id_cb_remember_ID;


    private String ID;
    private String PW;
    private final int KEY_PWD = 900;


    private OnFragmentInteractionListener mListener;

    public Fragment_ID() {
        // Required empty public constructor
    }


    public static Fragment_ID newInstance(String param1, String param2) {
        Fragment_ID fragment = new Fragment_ID();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            if(edit_ID!=null)
                checkIdSave();

        }else{
            if(edit_ID!=null && edit_PW!=null){
            edit_PW.setText("");

           }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.v("","OnAtictivityResult++");
        switch (requestCode){
            case KEY_PWD :
                if(resultCode == RESULT_OK){
                    PW = IxCustomInputActivity.getResult(edit_PW, data);
                    edit_PW.setText(PW);

                }
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.v("","test");

        View view = inflater.inflate(R.layout.fragment__id,container,false);
        edit_ID=(EditText)view.findViewById(R.id.edit_id);
        edit_PW=(EditText)view.findViewById(R.id.edit_pw);
        text_help=(TextView) view.findViewById(R.id.text_help);
        btn_login=(TextView) view.findViewById(R.id.btn_login);
        linear_pw=(LinearLayout) view.findViewById(R.id.linear_pw);
        linear_id =(LinearLayout) view.findViewById(R.id.linear_id);
        login_id_cb_remember_ID=(CheckBox)view.findViewById(R.id.login_id_cb_rememeber_ID);

        return view;

    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        checkIdSave();
        InputMethodManager imm2 = (InputMethodManager)((LoginActivity)getActivity()).getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm2.hideSoftInputFromWindow(edit_PW.getWindowToken(),0);


        edit_PW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager)((LoginActivity)getActivity()).getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edit_PW.getWindowToken(),0);
                securityKeyPad();
            }
        });
        final InputMethodManager imm = (InputMethodManager)((LoginActivity)getActivity()).getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow( edit_PW.getWindowToken(), 0);
        edit_PW.setLongClickable(false);
        edit_PW.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                    imm.hideSoftInputFromWindow(edit_PW.getWindowToken(),0);//키보드 숨기기
                }
                return false;
            }
        });
        edit_PW.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                switch(view.getId()){

                    case R.id.edit_pw:
                        if(hasFocus){
                            imm.hideSoftInputFromWindow(edit_PW.getWindowToken(),0);//키보드 숨기기
                            securityKeyPad();
                        }else{
                            imm.hideSoftInputFromWindow(edit_PW.getWindowToken(),0);//키보드 숨기기
                        }
                        break;
                }
            }
        });

        edit_ID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                ID= edit_ID.getText().toString();

                if(edit_ID.getText().toString().equals("")){
                    edit_ID.setCompoundDrawablesWithIntrinsicBounds(getContext().getResources().getDrawable(R.drawable.ic_person_black_24dp),null,null,null);
                    linear_id.setBackgroundResource(R.drawable.login_id_edit_box);
                }
                else{
                    edit_ID.setCompoundDrawablesWithIntrinsicBounds(getContext().getResources().getDrawable(R.drawable.ic_person_blue_24dp),null,null,null);
                     linear_id.setBackgroundResource(R.drawable.login_id_edit_box_written);}



                if(edit_ID.getText().toString().equals("")||edit_PW.getText().toString().equals("")){
                    btn_login.setBackgroundResource(R.drawable.login_id_button_unwritten_selector);
                    btn_login.setClickable(false);


                }
                else{
                    btn_login.setBackgroundResource(R.drawable.login_id_button_selector);
                    btn_login.setClickable(true);
                }

            }
        });
        edit_PW.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(edit_PW.getText().toString().equals("")){
                    edit_PW.setCompoundDrawablesWithIntrinsicBounds(getContext().getResources().getDrawable(R.drawable.login_password),null,null,null);
                    linear_pw.setBackgroundResource(R.drawable.login_id_edit_box);}
                else{ edit_PW.setCompoundDrawablesWithIntrinsicBounds(getContext().getResources().getDrawable(R.drawable.login_password_blue),null,null,null);
                     linear_pw.setBackgroundResource(R.drawable.login_id_edit_box_written);}



                if(edit_ID.getText().toString().equals("")||edit_PW.getText().toString().equals("")){
                    btn_login.setBackgroundResource(R.drawable.login_id_button_unwritten_selector);
                    btn_login.setClickable(false);
                }
                else{
                    btn_login.setBackgroundResource(R.drawable.login_id_button_selector);
                    btn_login.setClickable(true);
                }

            }
        });


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!edit_PW.getText().toString().equals("") && !edit_ID.getText().toString().equals(""))
                {
                    ((LoginActivity)getActivity()).LoginAction(edit_ID.getText().toString(),edit_PW.getText().toString(),
                            ((LoginActivity)getActivity()).WAY_ID,login_id_cb_remember_ID.isChecked());
                }
                else if(!edit_PW.getText().toString().equals(""))
                {

                }
                else if(edit_ID.getText().toString().equals(""))
                {

                }

            }
        });


    }

       @Override
    public void onStart() {
        LogMaker.logmaking("","");


        super.onStart();
    }

    @Override
    public void onResume() {
        Log.d(this.getClass().getSimpleName(), "onResume()");
        super.onResume();
    }


    @Override
    public void onPause() {
        Log.d(this.getClass().getSimpleName(), "onPause()");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.d(this.getClass().getSimpleName(), "onStop()");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        Log.d(this.getClass().getSimpleName(), "onDestroyView()");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Log.d(this.getClass().getSimpleName(), "onDestroy()");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        Log.d(this.getClass().getSimpleName(), "onDetach()");
        super.onDetach();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(this.getClass().getSimpleName(), "onSaveInstanceState()");
        super.onSaveInstanceState(outState);
    }

    public void checkIdSave(){

        if(
                ((LoginActivity)getActivity()).SAVEIDSTATE &&!((LoginActivity)getActivity()).USERID.equals("")){

            edit_ID.setText(((LoginActivity)getActivity()).USERID);
            edit_ID.setCompoundDrawablesWithIntrinsicBounds(getContext().getResources().getDrawable(R.drawable.ic_person_blue_24dp),null,null,null);
            linear_id.setBackgroundResource(R.drawable.login_id_edit_box_written);
            login_id_cb_remember_ID.setChecked(true);


        }
    }
    public final void securityKeyPad(){
        LogMaker.logStart();
        LogMaker.logmaking("============","securityKeyPad===========================");
        Intent intent = new Intent((LoginActivity)getActivity(), IxCustomInputActivity.class);
        //옵션설정
        IxCustomInputActivity.setLengthOfInput(intent,40);  //입력 최대 길이0
        IxCustomInputActivity.setTypeInputQwerty(intent);      //QWERTY 키보드
        IxCustomInputActivity.setTextOfTitle(intent,"계정정보를 입력해주세요.");//입력창 제목
        IxCustomInputActivity.setTextOfHint(intent,"비밀번호");
        startActivityForResult(intent,KEY_PWD);
        LogMaker.logEnd();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
