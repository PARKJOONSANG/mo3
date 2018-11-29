package com.ex.group.folder.loginfragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ex.group.folder.LoginActivity;
import com.ex.group.folder.MainActivity;
import com.ex.group.folder.dialog.CommonDialog;
import com.ex.group.folder.dialog.CommonDialog_oneButton;
import com.ex.group.folder.utility.BaseActivity;
import com.ex.group.folder.utility.LogMaker;

import java.util.Random;
import com.ex.group.folder.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment_PIN.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment_PIN#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_PIN extends Fragment {

    /*여기에 정의하자 widget들을*/
    private final int PIN_LENGTH =4;
    private TextView login_pin_text1;
    private LinearLayout login_pin_secretbox;
    private ImageView []iv_secret = new ImageView[4];
    private boolean []iv_secret_boolmap ={false,false,false,false};
    private String [] PIN={"0","0","0","0"};
    private String PINSTRING="";
    private TextView [] tv_number = new TextView[10];
    private ImageView[] iv_number = new ImageView[10];

    private ImageView iv_shuffle,iv_delete;
    private Integer [] iv_secret_ID ={R.id.iv_secret1,R.id.iv_secret2,R.id.iv_secret3,R.id.iv_secret4};

    private Integer [] tv_number_ID ={R.id.tv_number_batch11,R.id.tv_number_batch12,R.id.tv_number_batch13,
                                      R.id.tv_number_batch21,R.id.tv_number_batch22,R.id.tv_number_batch23,
                                      R.id.tv_number_batch31,R.id.tv_number_batch32,R.id.tv_number_batch33,
                                      R.id.tv_number_batch42};

    private Integer [] iv_number_ID ={R.id.iv_number_batch11,R.id.iv_number_batch12,R.id.iv_number_batch13,
                                      R.id.iv_number_batch21,R.id.iv_number_batch22,R.id.iv_number_batch23,
                                      R.id.iv_number_batch31,R.id.iv_number_batch32,R.id.iv_number_batch33,
                                      R.id.iv_number_batch42};
    private LinearLayout keypad_layout;
    Animation up,down;


    private int[] randomSequence;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Fragment_PIN() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_PIN.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_PIN newInstance(String param1, String param2) {
        Fragment_PIN fragment = new Fragment_PIN();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }


    }
    int f=0;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(getUserVisibleHint()){

            LogMaker.logmaking("USERPWDchecking",((LoginActivity)getActivity()).USERPWD);
            if(!((LoginActivity)getActivity()).USERPWD.equals("")) //최초 일회 로그인 확인
            {

                if(!((LoginActivity)getActivity()).PINCODE.equals("")) //PINCODE 등록 확인
                {
                    if (login_pin_text1 != null) {
                        login_pin_text1.setText(getString(R.string.pin_help));
                        keypad_layout.setAnimation(AnimationUtils.loadAnimation((LoginActivity) getActivity(), R.anim.up));
                        // keypad_layout.setVisibility(View.GONE);
                        keypad_layout.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //keypad_layout.startAnimation(up);
                                randomSequence = random0to9();
                                for (int i = 0; i < 10; i++) {
                                    tv_number[i].setText(String.valueOf(randomSequence[i]));
                                }
                                keypad_layout.setVisibility(View.VISIBLE);
                            }
                        }, 40);


                    }
                }
                else //PIN등록 요청
                {
                    if(((LoginActivity)getActivity()).loginCommonDialog_oneButton==null) {
                        ((LoginActivity) getActivity()).loginCommonDialog_oneButton
                                = new CommonDialog_oneButton((LoginActivity) getActivity(), "로그인", getString(R.string.pin_regi_noti), false,
                                ((LoginActivity) getActivity()).loginFailedListener);
                        ((LoginActivity) getActivity()).loginCommonDialog_oneButton.show();
                    }
                    login_pin_text1.setText("PIN등록을 먼저 해주세요");

                }

            }
            else  //최초 일회 로그인 확인 예외 처리
            {
                if(((LoginActivity)getActivity()).loginCommonDialog_oneButton==null) {
                    ((LoginActivity) getActivity()).loginCommonDialog_oneButton
                            = new CommonDialog_oneButton((LoginActivity) getActivity(), "로그인", getString(R.string.login_first_lauch_noti), false,
                            ((LoginActivity) getActivity()).loginFailedListener);
                    ((LoginActivity) getActivity()).loginCommonDialog_oneButton.show();
                }
                login_pin_text1.setText(getString(R.string.login_first_lauch_noti));
            }

            }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


         //ANIAMATION SETTING
        up = AnimationUtils.loadAnimation((LoginActivity)getActivity(),R.anim.up);
        up.setInterpolator(AnimationUtils.loadInterpolator((LoginActivity)getActivity(),android.R.anim.accelerate_decelerate_interpolator));
        down=AnimationUtils.loadAnimation((LoginActivity)getActivity(),R.anim.down);

        //UISETTING
        View view =inflater.inflate(R.layout.fragment__pin,container,false);

        login_pin_text1=(TextView)view.findViewById(R.id.login_pin_text1);

        keypad_layout=(LinearLayout) view.findViewById(R.id.keypad_layout);
        iv_shuffle=(ImageView)view.findViewById(R.id.iv_shuffle);
        iv_delete=(ImageView)view.findViewById(R.id.iv_delete);
        login_pin_secretbox=(LinearLayout)view.findViewById(R.id.login_pin_secretbox);
        //비밀 번호 활성화
        for(int i =0;i<4;i++){
            iv_secret[i]=(ImageView)view.findViewById(iv_secret_ID[i]);
        }

        //키패드 UI SETTING
        for(f=0;f<10;f++) {
            iv_number[f] = (ImageView) view.findViewById(iv_number_ID[f]);


            tv_number[f]=(TextView)view.findViewById(tv_number_ID[f]);
            iv_number[f].setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if( motionEvent.getAction()==MotionEvent.ACTION_BUTTON_PRESS){
                        Vibrator vibrator=(Vibrator) ((LoginActivity)getActivity()).getSystemService(Context.VIBRATOR_SERVICE);
                        //vibrator.vibrate(5);
                    }

                    return false;
                }
            });
        }
        /*onclickListener 설정 */
        //키패드 UI 활성화
        for(f=0;f<10;f++){
            final int k =f;// f 값은 iv_number[f].setOnclickListener로 하면 후위 연산에 의해 값이 바뀐다 .따라서 값이 바뀌지 않는 final int k를 생성 해 줘야한다.

            iv_number[k].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    ////////////////////
                    input_PINCODE(tv_number[k],iv_secret,PIN);
                    Vibrator vibrator=(Vibrator) ((LoginActivity)getActivity()).getSystemService(Context.VIBRATOR_SERVICE);
                    vibrator.vibrate(5);
                }
            });
        }

        iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete_PINCODE();
                Vibrator vibrator=(Vibrator) ((LoginActivity)getActivity()).getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(5);
            }
        });
        iv_shuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                randomSequence=random0to9();
                for(int i=0;i<10;i++){
                    tv_number[i].setText(String.valueOf(randomSequence[i]));}
                Vibrator vibrator=(Vibrator) ((LoginActivity)getActivity()).getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(5);
            }
        });



        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    public int[] random0to9(){
        int randnum[]=new int[10];
        int num;
        Random rand =new Random();

        for(int i =0; i<10;i++){
            randnum[i]=rand.nextInt(10);
            for(int j=0;j<i;j++){
                if(randnum[i]==randnum[j])
                {i--;}
            }
        }
        for(int i =0;i<10;i++){
            Log.v("Lets Seethe Randum","Sequence : "+randnum[i]);
        }
        return  randnum;
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser&&isResumed() )//화면에 UI 가 나타날때?
        {



                if(!((LoginActivity)getActivity()).getSharedString("USERPWD").equals("")) //최초 일회 로그인 확인
                {

                    if(!((LoginActivity)getActivity()).PINCODE.equals("")) //PINCODE 등록 확인
                    {
                        if (login_pin_text1 != null) {
                            login_pin_text1.setText(getString(R.string.pin_help));
                            keypad_layout.setAnimation(AnimationUtils.loadAnimation((LoginActivity) getActivity(), R.anim.up));
                            // keypad_layout.setVisibility(View.GONE);
                            keypad_layout.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //keypad_layout.startAnimation(up);
                                    randomSequence = random0to9();
                                    for (int i = 0; i < 10; i++) {
                                        tv_number[i].setText(String.valueOf(randomSequence[i]));
                                    }
                                    keypad_layout.setVisibility(View.VISIBLE);
                                }
                            }, 40);


                        }
                    }
                    else //PIN등록 요청
                    {
                        /*((LoginActivity)getActivity()).loginCommonDialog_oneButton
                                = new CommonDialog_oneButton((LoginActivity) getActivity(), "로그인", getString(R.string.pin_regi_noti),false,
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        ((LoginActivity)getActivity()).loginWayViewPager.setCurrentItem(0);
                                        ((LoginActivity)getActivity()).loginCommonDialog_oneButton.dismiss();

                                    }
                                });
                        if(((LoginActivity)getActivity()).loginCommonDialog_oneButton!=null){
                            ((LoginActivity)getActivity()).loginCommonDialog_oneButton.dismiss();
                        }
                        ((LoginActivity)getActivity()).loginCommonDialog_oneButton.show();
*/
                    }

                }
                else  //최초 일회 로그인 확인 예외 처리
                {
                    /*((LoginActivity)getActivity()).loginCommonDialog_oneButton
                            = new CommonDialog_oneButton((LoginActivity) getActivity(), "로그인", getString(R.string.login_first_lauch_noti),false,
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    ((LoginActivity)getActivity()).loginWayViewPager.setCurrentItem(0);
                                    ((LoginActivity)getActivity()).loginCommonDialog_oneButton.dismiss();

                                }
                            });
                    if(((LoginActivity)getActivity()).loginCommonDialog_oneButton!=null){
                        ((LoginActivity)getActivity()).loginCommonDialog_oneButton.dismiss();
                    }
                    ((LoginActivity)getActivity()).loginCommonDialog_oneButton.show();*/
                }



        }
        else{ // 화면에서 UI가 사라질때
            if(keypad_layout!=null){
                keypad_layout.setVisibility(View.GONE);
                deleteAll_PINCODE();

            }

        }
    }
  /*핀넘버의 값이 일치하는 지 확인 해주는 메서드 CHECKVALIDPINCODE
  * 일치하지 않으면 다 지워 버리고 일치하면 로그인인증(LOGINACTIVITY에 구현 )으로 이동한다.*/
    public void checkValidPINCODE(String pin){
        Log.v("SEE THE PIN CODE",":"+pin);
        if(pin.equals(((LoginActivity)getActivity()).getSharedString("PINCODE"))){
            ((LoginActivity)getActivity()).LoginAction(((LoginActivity)getActivity()).USERID,
                                                       ((LoginActivity)getActivity()).USERPWD,
                    ((LoginActivity)getActivity()).WAY_PIN,true);

            login_pin_text1.setText(getString(R.string.pin_auth_success));

        }else{
            Log.v("SEE THE PIN CODE",":"+pin);
            deleteAll_PINCODE();
            Vibrator vibe = (Vibrator)((LoginActivity)getActivity()).getSystemService(Context.VIBRATOR_SERVICE);
            login_pin_secretbox.setBackgroundResource(R.drawable.login_pin_box_input);
            login_pin_text1.setText(login_pin_text1.getResources().getString(R.string.pin_error));
            vibe.vibrate(300);
        }

    }
    public void input_PINCODE(TextView textView, ImageView [] imageView, final String [] str){
        for(int i = 0;i<PIN_LENGTH;i++){
            login_pin_secretbox.post(new Runnable() {
                @Override
                public void run() {

                    login_pin_secretbox.setBackgroundResource(R.drawable.login_pin_box_input_activated);

                }
            });


            if(iv_secret_boolmap[i]==false){

                Log.v("WHat is my Number","::"+textView.getText()+"sequence ::"+i);
                PIN[i]=textView.getText().toString();
                imageView[i].setImageResource(R.drawable.login_pin_circle_checked);
                iv_secret_boolmap[i]=true;

                if(i!=3)
                {
                break;
                }
                else{
                    iv_secret[i].postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            String pin="";
                            for(int i =0 ; i<4;i++){
                                pin=pin+String.valueOf(PIN[i]);
                            }

                            checkValidPINCODE(pin);
                        }
                    },200);


                }
            }



        }

    }
    public void delete_PINCODE(){
        for(int i =PIN_LENGTH-1;i>=0;i--){
            final int k = i;
            if(iv_secret_boolmap[k]==true){
                iv_secret_boolmap[k]=false;
                iv_secret[i].setImageResource(R.drawable.login_pin_circle_unchecked);
                if(k!=0){
                    break;
                }else{

                    login_pin_secretbox.setBackgroundResource(R.drawable.login_pin_box_input);
                }




            }

        }
    }
    public void deleteAll_PINCODE(){
        for(int i=0;i<4;i++){
            iv_secret_boolmap[i]=false;
            iv_secret[i].setImageResource(R.drawable.login_pin_circle_unchecked);
            login_pin_secretbox.setBackgroundResource(R.drawable.login_pin_box_input);

        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
