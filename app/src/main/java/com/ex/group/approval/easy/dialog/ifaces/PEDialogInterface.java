package com.ex.group.approval.easy.dialog.ifaces;

import android.content.DialogInterface;


/**
 * 공통 다이얼로그
 * @author jokim
 *
 */
public interface PEDialogInterface extends DialogInterface {
	public interface OnClickListener {
		public void onClick(Object obj, String arg);
	}
}