package com.ex.group.folder.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.ex.group.folder.R;


public class CustomprogressDialog extends Dialog{
    public Context c;
    public TextView imgLog;
    AnimationDrawable  anima;
    ImageView img;
    TextView textView;
    String help;


    public CustomprogressDialog(@NonNull Context context,  @Nullable String string) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCanceledOnTouchOutside(false);
        c= context;
        if(string!=null)
        help="";
    }
    public void setContent(String str){
       help =str;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_porgress_dialog);
        this.setTitle("sdfsdfsd");
        img =findViewById(R.id.imgimg);
        img.setImageResource(R.drawable.anima);
        img.setScaleType(ImageView.ScaleType.FIT_CENTER);
        anima =(AnimationDrawable)img.getDrawable();
        img.post(new Runnable() {
            @Override
            public void run() {
                anima.start();
            }
        });
        textView =findViewById(R.id.texttext);
        textView.setText(help);
        //textView.setText(this.help);

    }
}
