package com.ex.group.mail.addressbook.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.ex.group.mail.addressbook.widget.AddressSearchAppWidgetProvider;
import com.skt.pe.common.activity.SKTActivity;
import com.skt.pe.common.dialog.SKTDialog;
import com.skt.pe.common.exception.SKTException;
import com.skt.pe.common.service.XMLData;
import com.skt.pe.util.StringUtil;

import com.ex.group.folder.R;


public class WidgetSearchActivity extends SKTActivity {
	@Override
	protected XMLData onAction(String primitive, String... args)
			throws SKTException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void onActionPost(String primitive, XMLData result, SKTException e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected int onActionPre(String primitive) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected void onActivityResultX(int requestCode, int resultCode,
			Intent data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected int assignLayout() {
		// TODO Auto-generated method stub
		return R.layout.mail_member_membersearchwidget;
	}

	@Override
	protected void onCreateX(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
//		setContentView(R.layout.member_membersearchwidget);
		final EditText text = (EditText)findViewById(R.id.TEXT_INPUT);
		text.setOnEditorActionListener(new OnEditorActionListener() {
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				// TODO Auto-generated method stub
				Button button = (Button)findViewById(R.id.BUTTON_INPUT);
				button.performClick();
				return false;
			}
		});
		Button button = (Button)findViewById(R.id.BUTTON_INPUT);
		button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
            	String curSearch = text.getText().toString();
            	char[] digit = curSearch.toCharArray();
            	int len = digit.length;
            	
            	if(len < 2) {
            		String dialogMessage = "문자는 2자 이상, 숫자는 3자 이상 입력하셔야 합니다.";
            		new SKTDialog(WidgetSearchActivity.this).getDialog(dialogMessage).show();
        			return;
            	} else if(len < 3) {
            		boolean isAllDigit = true;
            		for(int i = 0; i < len; i++) {
                		if(digit[i] < 0x30 || digit[i] > 0x39) {
                			isAllDigit = false;
                			break;
                		}
            		}
            		if(isAllDigit) {
            			String dialogMessage = "문자는 2자 이상, 숫자는 3자 이상 입력하셔야 합니다.";
            			new SKTDialog(WidgetSearchActivity.this).getDialog(dialogMessage)
            					.show();
            			return;
            		}
            	}
            	if(!StringUtil.checkKeyCode(curSearch)) {
            		String dialogMessage = "한글, 영문, 숫자만 입력 가능합니다.";
            		new SKTDialog(WidgetSearchActivity.this).getDialog(dialogMessage).show();
        			return;
            	}
            	
				Intent intent = new Intent(WidgetSearchActivity.this,
						AddressSearchActivity.class);
				intent.putExtra("keyword", text.getText().toString());
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				WidgetSearchActivity.this.startActivity(intent);
				finish();
			}
		});
		showKeyPad();
	}

	@Override
	protected void onStartX() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(AddressSearchAppWidgetProvider.SHOW_ACTION);
		sendBroadcast(intent);
		super.onPause();
	}
	
	/**
	 * 키패드를 보여준다.
	 */
	public void showKeyPad() {
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }
}