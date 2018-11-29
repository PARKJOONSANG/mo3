package com.ex.group.folder.dialog;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ex.group.folder.R;

public class CommonDialog extends Dialog{
	private static final String TAG = "CommonDialog";

	private Context mContext;





	public CommonDialog(Context context, String title, String content, boolean cancelable, View.OnClickListener confirmListener, View.OnClickListener cancelListener)
	{
		super(context, R.style.DialogTheme);
		initialize(context, title, content, confirmListener, cancelListener);
		setCancelable(cancelable);
	}


	private void initialize(Context context, String title, String content, View.OnClickListener confirmListener,  View.OnClickListener cancelListener){
		mContext	= context;

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.common_dialog);
		
		TextView tv_title = (TextView)this.findViewById(R.id.tv_title);
		TextView tv_content = (TextView)this.findViewById(R.id.tv_content);
		TextView btnConfirm = (TextView)this.findViewById(R.id.btn_confirm);
		TextView btnCancel = (TextView)this.findViewById(R.id.btn_close);

		btnConfirm.setOnClickListener(confirmListener);

			btnCancel.setOnClickListener(cancelListener);

		tv_title.setText(title);
		tv_content.setText(content);
	}





}
