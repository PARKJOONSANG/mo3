package com.ex.group.board.activity;

import android.app.Dialog;
import android.content.Context;
import android.view.WindowManager;

/**
 * 공통 다이얼로그
 * @author jokim
 *
 */
public class CommonDialog extends Dialog {
	public CommonDialog(Context context) {
		super(context, android.R.style.Theme_Translucent_NoTitleBar);
		WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();    
		lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		lpWindow.dimAmount = 0.8f;
		getWindow().setAttributes(lpWindow);
	}
}