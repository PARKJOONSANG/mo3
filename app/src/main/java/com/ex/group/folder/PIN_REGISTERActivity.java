package com.ex.group.folder;

import android.content.Context;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ex.group.folder.dialog.CommonDialog_oneButton;
import com.ex.group.folder.utility.BaseActivity;

import java.util.Random;

import com.ex.group.folder.R;

public class PIN_REGISTERActivity extends BaseActivity {
    String first;
    String second;
    CommonDialog_oneButton cdo;
    private ImageView button_prev;

    public String getFirst() { return first; }public void setFirst(String first) { this.first = first; }
    public String getSecond() { return second; }public void setSecond(String second) { this.second = second; }

    final int FIRST_COUNT = 1;
    final int LAST_COUNT =0;
    int count =0;
    private final int PIN_LENGTH =4;
    private TextView login_pin_text1;
    private LinearLayout login_pin_secretbox;
    private ImageView[]iv_secret = new ImageView[4];
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin__register);
        UISETTING();
        keypadShowing();

    }

    public void UISETTING(){
        up = AnimationUtils.loadAnimation(PIN_REGISTERActivity.this,R.anim.up);
        up.setInterpolator(AnimationUtils.loadInterpolator(PIN_REGISTERActivity.this,android.R.anim.accelerate_decelerate_interpolator));
        down=AnimationUtils.loadAnimation(PIN_REGISTERActivity.this,R.anim.down);
        button_prev=(ImageView)findViewById(R.id.button_prev);
        button_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        login_pin_text1=(TextView)findViewById(R.id.login_pin_text1);
        login_pin_text1.setText(getString(R.string.pin_regi_request1));
        keypad_layout=(LinearLayout)findViewById(R.id.keypad_layout);
        iv_shuffle=(ImageView)findViewById(R.id.iv_shuffle);
        iv_delete=(ImageView)findViewById(R.id.iv_delete);
        login_pin_secretbox=(LinearLayout)findViewById(R.id.login_pin_secretbox);
        //비밀 번호 활성화
        for(int i =0;i<4;i++){
            iv_secret[i]=(ImageView)findViewById(iv_secret_ID[i]);
        }

        //키패드 UI SETTING
        for(int f=0;f<10;f++){
            iv_number[f]=(ImageView)findViewById(iv_number_ID[f]);
            tv_number[f]=(TextView)findViewById(tv_number_ID[f]);
            iv_number[f].setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if( motionEvent.getAction()==MotionEvent.ACTION_BUTTON_PRESS){
                        Vibrator vibrator=(Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        //vibrator.vibrate(5);
                    }

                    return false;
                }
            });
        }
        /*onclickListener 설정 */
        //키패드 UI 활성화
        for(int f=0;f<10;f++){
            final int k =f;// f 값은 iv_number[f].setOnclickListener로 하면 후위 연산에 의해 값이 바뀐다 .따라서 값이 바뀌지 않는 final int k를 생성 해 줘야한다.

            iv_number[k].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    ////////////////////
                    input_PINCODE(tv_number[k],iv_secret,PIN);
                    Vibrator vibrator=(Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                    vibrator.vibrate(5);
                }
            });
        }

        iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete_PINCODE();
                Vibrator vibrator=(Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(5);

            }
        });
        iv_shuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                randomSequence=random0to9();
                for(int i=0;i<10;i++){
                    tv_number[i].setText(String.valueOf(randomSequence[i]));}
                Vibrator vibrator=(Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(5);
            }
        });





    }




    public void keypadShowing(){
        if (login_pin_text1 != null) {
            login_pin_text1.setText(getString(R.string.pin_regi_request1));
            login_pin_text1.setTextColor(getResources().getColor(R.color.login_pin_text));
            keypad_layout.setAnimation(AnimationUtils.loadAnimation(PIN_REGISTERActivity.this, R.anim.up));
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
            }, 40);}

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
                            count= (count+1)%2;
                            checkValidPINCODE(pin,count);
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
    public void checkValidPINCODE(String pin,int count){
        Log.v("SEE THE PIN CODE",":"+pin);

        if(count==FIRST_COUNT)           //첫번째 PIN 입력했을경우
        {
            setFirst(pin);

            login_pin_secretbox.setBackgroundResource(R.drawable.login_pin_box_input);
            login_pin_text1.setText(login_pin_text1.getResources().getString(R.string.pin_regi_request2));
            login_pin_text1.setTextColor(getResources().getColor(R.color.login_pin_text));
            deleteAll_PINCODE();
            vibrate(100);
        }
        else if(count ==LAST_COUNT)    //두번째 PIN 입력했을경우

        {
            setSecond(pin);

            if(getFirst().equals(getSecond()))    //첫번째와 두번째 값이 같을경우  성공 다이얼로그를 띄워주며 액티비티를 종료 한다.
            {
                setSharedString("PINCODE",pin);
                setSharedint("WAYTOLOGIN",1);
                vibrate(100);
                cdo= new CommonDialog_oneButton(PIN_REGISTERActivity.this, "", getString(R.string.pin_regi_sucess), false,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                 vibrate(5);

                                cdo.dismiss();
                                finish();
                            }
                        });
                cdo.show();
            }
            else                                 //만약에 첫번째 값과 두번째 값이 다를 경우 초기화 시킨다.
            {
                deleteAll_PINCODE();
                login_pin_text1.setText(login_pin_text1.getResources().getString(R.string.pin_error));
                login_pin_text1.setTextColor(getResources().getColor(R.color.login_pin_text_error));
                login_pin_text1.setClickable(false);
                login_pin_text1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        login_pin_text1.setText(getString(R.string.pin_regi_request1));
                        login_pin_text1.setTextColor(getResources().getColor(R.color.login_pin_text));
                        login_pin_text1.setClickable(true);
                    }
                },500);
                count=0;
                vibrate(300);
            }
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

}
