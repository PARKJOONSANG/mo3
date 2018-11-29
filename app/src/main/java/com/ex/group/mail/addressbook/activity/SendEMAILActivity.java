package com.ex.group.mail.addressbook.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skt.pe.common.exception.SKTException;
import com.skt.pe.common.service.Controller;
import com.skt.pe.common.service.Parameters;
import com.skt.pe.common.service.XMLData;

import java.util.ArrayList;
import com.ex.group.folder.R;


/**
 * 이메일 보내는 화면
 * @author jokim
 *
 */
public class SendEMAILActivity extends BaseActivity {
	boolean m_bIsChild = false;
	
	LinearLayout m_linearLayout;
	
	private ArrayList<String> mEmployeeNum = new ArrayList<String>();
	private ArrayList<String> mEmployeeName = new ArrayList<String>();
	private ArrayList<String> mEmployeeIsMember = new ArrayList<String>();
	
	final int PEOPLE_DEPTEMAIL = 0;
    final int PEOPLE_MEMBEREMAIL = 1;
	final int PEOPLE_MIX_DEPTEMAIL = 2;
    final int PEOPLE_MIX_MEMBEREMAIL = 3;
    
    final int DIALOG_MEMBER_EMAIL = 1111;
    final int DIALOG_EMAIL_NOTICE = 2222;
    
    String m_szTitle = null;
    String m_szMsg = null;
    boolean isMix = false;
    boolean isMixAndDept = false;

	@Override
	protected int assignLayout() {
		// TODO Auto-generated method stub
		return R.layout.mail_member_membersend;
	}
	
	public void onCreateX(Bundle instanceState) {
		super.onCreateX(instanceState);
//		setContentView(R.layout.member_membersendemail);
		
		onPostCreateX();
	}
	
	protected void onPostCreateX() {
		Bundle b = getIntent().getExtras();
		m_bIsChild = b.getBoolean("isChild");
		TextView tv = (TextView)findViewById(R.id.MSSMSInputReceiver);
		
		mEmployeeNum = b.getStringArrayList("employeenum");
		mEmployeeName = b.getStringArrayList("employee");
		mEmployeeIsMember = b.getStringArrayList("isMember");
		
		if(m_bIsChild) {
			for(String isMember: mEmployeeIsMember) {
				if(isMember.equals("true")) {
					isMix = true;
					break;
				}
			}
			for(String isMember: mEmployeeIsMember) {
				if(isMember.equals("false")) {
					isMixAndDept = true;
					break;
				}
			}
		}
		
		((TextView)findViewById(R.id.title_text)).setText(R.string.mail_sendEmail);
		findViewById(R.id.layout_title).setVisibility(View.VISIBLE);
//		findViewById(R.id.layout_sign).setVisibility(View.VISIBLE);
		
		String names = "";
		for(String name: mEmployeeName) {
			names += name + ";";
		}
		tv.setText(names);
//		if(mEmployeeName.size() == 1) {
//			tv.setText(mEmployeeName.get(0));
//		} else {
//			tv.setText(mEmployeeName.get(0) + " " + getString(R.string.text_except) + " " +
//					(mEmployeeName.size() - 1) +
//					(m_bIsChild ? getString(R.string.text_team) : getString(R.string.text_person)));
//		}
		
		EditText et = (EditText)findViewById(R.id.MSSMSInputTitleText);
		et.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View a_view, int a_nKeyCode, KeyEvent a_keyEvent) {
				if (a_nKeyCode == KeyEvent.KEYCODE_ENTER) {
					return true;
				} // end if (a_nKeyCode == KeyEvent.KEYCODE_ENTER)
				
				return false;
			}
		});

		et = (EditText)findViewById(R.id.MSSMSInputText);
		et.setText("\n\n" + getString(R.string.mail_send_skone));
		et.setMinLines(4);
		
		Button sms = (Button) findViewById(R.id.multiselect_confirm);
        sms.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
            	EditText et = (EditText) findViewById(R.id.MSSMSInputTitleText);
            	String temp = et.getText().toString();
            	if (temp.equals("")) {
    				m_szTitle = getString(R.string.mail_text_error);
            		m_szDialogMessage = getString(R.string.mail_input_title);
        			showDialog(DIALOG_MEMBER_EMAIL);
        			return;
            	}
            	m_szTitle = temp;
            	
            	et = (EditText) findViewById(R.id.MSSMSInputText);
            	temp = et.getText().toString();
            	if (temp.equals("")) {
    				m_szTitle = getString(R.string.mail_text_error);
            		m_szDialogMessage = getString(R.string.mail_input_content);
        			showDialog(DIALOG_MEMBER_EMAIL);
        			return;
            	}
            	
//            	et = (EditText)findViewById(R.id.text_sign);
//            	temp += "\n\n" + et.getText().toString();
            	m_szMsg = temp;
            		
            	if(m_bIsChild) {
            		if(isMix) {
            			requestData(PEOPLE_MIX_MEMBEREMAIL, null);
            		} else {
            			requestData(PEOPLE_DEPTEMAIL, null);
            		}
            	} else {
        			requestData(PEOPLE_MEMBEREMAIL, null);
            	}
            }
        });
        
        sms = (Button) findViewById(R.id.multiselect_cancel);
        sms.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
            	finish();
            }
        });
	}

	protected void onResumeX() {
		showKeyPad();
	}
	
	/* (non-Javadoc)
	 * 액션 처리 후 UI 세팅<br>
	 * - 부서에 이메일 보내기 요청<br>
	 * - 멤버들에게 이메일 보내기 요청
	 * @see com.sk.pe.group.activity.BaseActivity#onPostThread(com.skt.pe.common.service.XMLData, com.skt.pe.common.exception.SKTException)
	 */
	public void onPostThread(XMLData a_XMLData, SKTException a_exception) {
		// 정상 응답이  발생하였으면 오류 메시지 보여주고 리턴
		if (a_exception != null) {
			m_szTitle = getString(R.string.mail_text_error);
    		m_szDialogMessage = a_exception.getMessage();
			showDialog(DIALOG_MEMBER_EMAIL);
			return;
		} // end else if (m_szResponseCode != null && m_szResponseCode.equals("1000") == false)
    	
		try {
			switch (m_nRequestType) {
			case PEOPLE_MIX_DEPTEMAIL:
			case PEOPLE_MIX_MEMBEREMAIL:
			case PEOPLE_DEPTEMAIL:
			case PEOPLE_MEMBEREMAIL:
    			m_szResponseCode = a_XMLData.get("result");
				m_szResponseMessage = a_XMLData.get("resultMessage");
				break;
			}
			if (m_szResponseMessage.equals("서비스 요청 성공")) {
				if(m_nRequestType == PEOPLE_MIX_MEMBEREMAIL) {
					if(isMixAndDept) {
						requestData(PEOPLE_MIX_DEPTEMAIL, null);
					} else {
	            		m_szTitle = getString(R.string.mail_text_send);
						m_szDialogMessage = getString(R.string.mail_request_email_send);
						showDialog(DIALOG_EMAIL_NOTICE);
					}
				} else {
            		m_szTitle = getString(R.string.mail_text_send);
					m_szDialogMessage = getString(R.string.mail_request_email_send);
					showDialog(DIALOG_EMAIL_NOTICE);
				}
			} else {
        		m_szTitle = getString(R.string.mail_text_send);
				m_szDialogMessage = m_szResponseMessage;
				showDialog(DIALOG_MEMBER_EMAIL);
			}
		}catch (Exception e) {
		}
	}

	/* (non-Javadoc)
	 * 액션 처리 핸들러<br>
	 * - 부서에 이메일 보내기 요청<br>
	 * - 멤버들에게 이메일 보내기 요청
	 * @see com.sk.pe.group.activity.BaseActivity#onProcessThread()
	 */
	public XMLData onProcessThread() throws SKTException {
		StringBuffer sb = new StringBuffer();
		
		Parameters params = null;
	    Controller controller = null;
	    XMLData nXMLData = null;
	    
		switch (m_nRequestType) {
		case PEOPLE_DEPTEMAIL:
        	params = new Parameters("COMMON_MAIL_DEPTEMAIL");
        	sb.setLength(0);
        	sb.append(mEmployeeNum.get(0));
        	for(int i = 1; i < mEmployeeNum.size(); i++) {
        		sb.append("|" + mEmployeeNum.get(i));
        	}
        	params.put("deptCode", sb.toString());
        	params.put("title", m_szTitle);
			params.put("msg", m_szMsg);
			break;
		case PEOPLE_MEMBEREMAIL:
			params = new Parameters("COMMON_MAIL_MEMBEREMAIL");
        	sb.setLength(0);
        	sb.append(mEmployeeNum.get(0));
        	for(int i = 1; i < mEmployeeNum.size(); i++) {
        		sb.append("|" + mEmployeeNum.get(i));
        	}
        	params.put("id", sb.toString());
        	params.put("title", m_szTitle);
			params.put("msg", m_szMsg);
			break;
		case PEOPLE_MIX_MEMBEREMAIL:
			params = new Parameters("COMMON_MAIL_MEMBEREMAIL");
        	sb.setLength(0);
        	for(int i = 0; i < mEmployeeNum.size(); i++) {
        		if(mEmployeeIsMember.get(i).equals("true")) {
        			sb.append("|" + mEmployeeNum.get(i));
        		}
        	}
        	sb.deleteCharAt(0);
        	params.put("id", sb.toString());
        	params.put("title", m_szTitle);
			params.put("msg", m_szMsg);
			break;
		case PEOPLE_MIX_DEPTEMAIL:
        	params = new Parameters("COMMON_MAIL_DEPTEMAIL");
        	sb.setLength(0);
        	for(int i = 0; i < mEmployeeNum.size(); i++) {
        		if(mEmployeeIsMember.get(i).equals("false")) {
        			sb.append("|" + mEmployeeNum.get(i));
        		}
        	}
        	sb.deleteCharAt(0);
        	params.put("deptCode", sb.toString());
        	params.put("title", m_szTitle);
			params.put("msg", m_szMsg);
			break;
		}
		
		controller = new Controller(this);
		nXMLData = controller.request(params);
		
		return nXMLData;
	}
	
	protected Dialog onCreateDialog(int a_nId) {
		Dialog aDialog = null;
    	
		switch (a_nId) {
		case DIALOG_MEMBER_EMAIL:
		case DIALOG_EMAIL_NOTICE:
			aDialog = createDialog(a_nId, m_szDialogMessage, DIALOG_ONE_BUTTON,
					getString(R.string.mail_ok_button), null, null);
    		break;
		} // end switch (a_nId)
    	
    	return aDialog;
	}
	
	/* (non-Javadoc)
	 * 팝업 클릭 이벤트 핸들러
	 * @see com.sk.pe.group.activity.BaseActivity#onClickDialog(int, java.lang.String, int)
	 */
	public void onClickDialog(int a_nDialogId, String a_szButtonText, int a_nClickedButton) {
		switch (a_nDialogId) {
		case DIALOG_EMAIL_NOTICE:
			finish();
    		break;
		} // end switch (a_nDialogId)
	} // end public void onClickDialog(int a_nDialogId, String a_szButtonText, int a_nClickedButton)

	/**
	 * 가상 키패드 보여주기
	 */
	public void showKeyPad() {
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        
		EditText et = (EditText) findViewById(R.id.MSSMSInputTitleText);
        if(!showSoftInputWindow(et)) {
            if(et != null) {
            	et.setFocusableInTouchMode(true);
            	et.requestFocus();
            }
            
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    showKeyPad();
                }
            }, 100);
        }
    }
}