package com.ex.group.folder.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.ex.group.folder.R;

public class CommonDialog_oneButton extends Dialog{
	private static final String TAG = "CommonDialog_oneButton";

	private Context mContext;

	public CommonDialog_oneButton(Context context, String title, String content, boolean cancelable, @Nullable  View.OnClickListener confirmListener)
	{
		super(context, R.style.DialogTheme);
		initialize(context, title, content, confirmListener);
		setCancelable(cancelable);
	}


	private void initialize(Context context, String title, String content, @Nullable View.OnClickListener confirmListener){
		mContext	= context;

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.common_dialog_negative);
		
		TextView tv_title = (TextView)this.findViewById(R.id.tv_title);
		TextView tv_content = (TextView)this.findViewById(R.id.tv_content);
		TextView btnConfirm = (TextView)this.findViewById(R.id.btn_confirm);


		if(confirmListener==null){

			btnConfirm.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					dismiss();
				}
			});
		}else {
			btnConfirm.setOnClickListener(confirmListener);
		}


		tv_title.setText(title);
		tv_content.setText(content);
	}




}
