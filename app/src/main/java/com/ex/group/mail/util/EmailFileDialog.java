package com.ex.group.mail.util;


import android.app.Dialog;
import android.content.Context;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.ex.group.folder.R;

/**
 * 파일 다운로드 시  Dialog 
 * @author sjsun5318
 *
 */
public class EmailFileDialog extends Dialog implements OnCheckedChangeListener {
	
	public static boolean EFD_CHECKED = false;

	public EmailFileDialog(Context context) {
		super(context, android.R.style.Theme_Translucent_NoTitleBar);
		WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
		lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		lpWindow.dimAmount = 0.8f;
		getWindow().setAttributes(lpWindow);
		setContentView(R.layout.mail_dialog_download);
	}

	public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		// TODO Auto-generated method stub
		CheckBox cb =  (CheckBox)findViewById(R.id.CheckNext);
		EFD_CHECKED = cb.isChecked();
	}

}
