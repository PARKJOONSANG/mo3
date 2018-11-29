package com.ex.group.mail.addressbook.dialog;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.ex.group.folder.R;

/**
 * Created by heejung on 2017-05-23.
 */

 public class CommonProgress extends Dialog {
    final static String TAG = "CommonProgress";
    ImageView iv_logo;
    TextView tv_content;

     public CommonProgress(Context context){

        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.common_progress);
        Log.e(TAG, "--------------------common progress--------------------");
        iv_logo = (ImageView)findViewById(R.id.iv_logo);
        tv_content = (TextView)findViewById(R.id.tv_content);

         Animation animation = new AlphaAnimation(1, 0);
        animation.setDuration(2000);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.REVERSE);

        tv_content.setAnimation(animation);
         iv_logo.startAnimation(animation);

    }

    public void setMessage(String msg){
        tv_content.setText(msg);
    }
    public void setMessage(int msg){
        tv_content.setText(msg);
    }


}
