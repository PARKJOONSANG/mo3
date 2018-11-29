package com.ex.group.mail.addressbook.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.skt.pe.common.activity.SKTActivity;
import com.skt.pe.common.conf.Constants;
import com.skt.pe.common.conf.EnvironManager;
import com.skt.pe.common.dialog.DialogButton;
import com.skt.pe.common.dialog.SKTDialog;
import com.skt.pe.common.exception.SKTException;
import com.skt.pe.common.service.XMLData;

/**
 * 기본 정의 Activity 클래스
 * @author jokim
 *
 */
public abstract class BaseActivity extends SKTActivity {
	
	public static final int DIALOG_CLICKED_BUTTON_FIRST = 0;   // 첫번째 버튼 눌렀을 경우
	public static final int DIALOG_CLICKED_BUTTON_SECOND = 1;  // 두번째 버튼을 눌렀을 경우 
	public static final int DIALOG_CLICKED_BUTTON_THIRD = 2;   // 세번째 버튼을 눌렀을 경우
	
	public static final int DIALOG_NO_BUTTON = 0;	           // 버튼 없는 다이얼로그
	public static final int DIALOG_ONE_BUTTON = 1;	           // 버튼 한개 다이얼 로그 (알림창같은 경우에 사용)
	public static final int DIALOG_TWO_BUTTON = 2;             // 버튼 두개 다이얼 로그 (확인, 취소 경우)
	public static final int DIALOG_THREE_BUTTON = 3;           // 버튼 세개 다이얼 로그 (YES, NO, CANCEL의 경우)
	public static final int DIALOG_CIRCLE = 4;	               // 무한 원형  다이얼 로그
	
	public final static String ACTION_EMAIL_CLIENT = Constants.Action.EMAIL_WRITE;
	
//	public static final int MENU_ITEM_EXIT = 0xffff;    // 종료 메뉴
	
//	private static final int DIALOG_PRGORESS = 0xffff;  // 다이얼로그
	public static final int DIALOG_SERVER_ERROR = 0xffff;  // 서버 오류
	
	protected String m_szResponseCode;                  // 서버 요청시 응답 코드 문자열
	protected String m_szResponseMessage;               // 서버 요청시 응답 문자열
//	protected String m_szExceptionMessage;              // 서버 요청시 예외상황 문자열
	protected String m_szDialogMessage;                 // 다이얼로그에 표시할 문자열
	protected String m_szTitle;							// 다이얼로그에 표시할 제목
//	protected ProgressDialog m_progressDialog;          // 서버 요청시 표시 다이얼로그
//	protected Handler m_handler;                        // 네트워크 처리를 위한 Handler
	protected int addJobListCnt;						// 겸직자 정보 리스트
	protected int m_nRequestType;                       // 서버 요청 타입
	protected Object m_requestObject;                   // 서버 요청 객체
	
    /** Called when the activity is first created. */
    @Override
    public void onCreateX(Bundle a_savedInstanceState)
    {
    	EnvironManager.setNeedEncPwd(true);
    	EnvironManager.setTestMdn("01071119341");
    } // end public void onCreate(Bundle savedInstanceState)
	
	@Override
	protected Dialog onCreateDialog(int a_nId) {
		
    	
    	return null;
		
	} // end protected Dialog onCreateDialog(int a_nId)
	
	protected int onActionPre(String primitive) {
		
		return Action.SERVICE_RETRIEVING;
		
	}
		
	protected XMLData onAction(String primitive, String... args) throws SKTException {
		
		return onProcessThread();
		
	}
	
	protected void onActionPost(String primitive, XMLData result, SKTException e) {
		
		if (e == null || e.getAlertReset() == false) {
			
			SKTException exception = null;
			
			if (e != null) {
		
				exception = new SKTException(e.getEncodedMessage(this));
				exception.setAlertReset();
				
			} // end if (e != null)
			
			onPostThread(result, exception);
			
		} // end if (e == null || e.getAlertReset() == false)
								
	}
	
	

	/**
	 * 서버에 전송 요청등 시간이 걸리는 작업 처리를 한다.
	 * @param a_nType 요청 종류
	 * @param a_object 요청에 필요한 객체 (상속 받은 곳에서 필요한 객체로 캐스팅해서 쓴다,)
	 */
	
	public void requestData(int a_nRequestType, Object a_requestObject) {

		m_nRequestType = a_nRequestType;
		m_requestObject = a_requestObject;
		m_szResponseCode = null;
		m_szResponseMessage = null;

		new Action("").execute("");

	} // end public void request(int a_nType, Object a_object)
	
	protected void onStartX()
	{
		
	}
	
	@Override
	protected void onActivityResultX(int requestCode, int resultCode, Intent data)
	{
		
	}
	
//	@Override
//	public boolean onPrepareOptionsMenu(Menu menu) {
//
//		if (menu.findItem(MENU_ITEM_EXIT) == null) {
//		
//			menu.add(0, MENU_ITEM_EXIT, Menu.NONE, R.string.menu_exit);
//			
//		} // end if (menu.findItem(MENU_ITEM_EXIT) == null)
//		
//		return true;
//		
//	}
//	
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//
//		//super.onOptionsItemSelected(item);
//
//		if (item.getItemId() == MENU_ITEM_EXIT) {
//
//			ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
//			am.restartPackage(getPackageName());
//			
//			finish();
//			
//			return true;
//			
//		} // end if (item.getItemId() == MENU_ITEM_EXIT)
//		
//		return false;
//		
//	}
	
	/**
	 * Thread를 통해 데이터를 처리하는 함수 
	 * (절대 UI 관련된 부분은 update 하면 안된다. UI update는 onPostNetwork()에서 처리해야 한다.)
	 */
	
	public XMLData onProcessThread() throws SKTException {
		
		return null;
		
	} // end public XMLData onProcessThread() throws SKTException
	
	/**
	 * Thread 작업 이후 화면 변경을 처리하는 부분.
	 */
	
	public void onPostThread(XMLData a_result, SKTException a_exception) {
		
				
	} // end public void onPostThread(XMLData a_result, SKTException a_exception)
	
	/**
	 * 다이얼로그 버튼을 눌렀을 경우 처리한다.
	 * @param a_nDialogId 다이얼로그 종류 
	 * @param a_nDialogButtonType 눌러진 버튼 문자열 
	 * @param a_nClickedButton 눌러진 버튼
	 */
	
	public void onClickDialog(int a_nDialogId, String a_szButtonText, int a_nClickedButton) {
		
		
	} // end public void onClickDialog(int a_nDialogId, String a_szButtonText, int a_nClickedButton)
	
	/**
	 * Dialog를 생성한다.
	 * 
	 * @param a_activity 요청한 Activity(보통 BaseActivity를 상속하여 this를 입력)
	 * @param a_nDialogId Dialog를 ID
	 * @param a_szMessage Dialog를 표시할 문자열
	 * @param a_nButtonType 버튼 종류 (BaseActivity.DIALOG_PRGORESS, BaseActivity.DIALOG_ONE_BUTTON, BaseActivity.DIALOG_TWO_BUTTON, BaseActivity.DIALOG_THREE_BUTTON)
	 * @param a_szFirstButton 첫번째 버튼의 문자열
	 * @param a_szSecondButton 두번째 버튼의 문자열
	 * @param a_szThirdButton 세번째 버튼의 문자열
	 * @return 생성된 Dialog
	 */
	
	public Dialog createDialog(final int a_nDialogId, final String a_szMessage,
                               final int a_nButtonType, final String a_szFirstButton, final String a_szSecondButton, final String a_szThirdButton) {

		switch (a_nButtonType) {
		
			case BaseActivity.DIALOG_CIRCLE:
			{

				ProgressDialog aProgressDialog = new ProgressDialog(this);
	    		aProgressDialog.setMessage(a_szMessage);
	    		aProgressDialog.setIndeterminate(true);
	    		aProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	    		aProgressDialog.setCancelable(false);

	    		return aProgressDialog;
			}
			
			case BaseActivity.DIALOG_NO_BUTTON:
			{

				AlertDialog.Builder aBuilder = new AlertDialog.Builder(this);
				aBuilder.setMessage(a_szMessage);
				aBuilder.setCancelable(true);
				
				AlertDialog alertDialog = aBuilder.create();
				
				return alertDialog;
			}
			
			case BaseActivity.DIALOG_ONE_BUTTON:
			{
				
				SKTDialog dialog = new SKTDialog(this, SKTDialog.DLG_TYPE_1);
				
				return dialog.getDialog(m_szTitle, a_szMessage, new DialogButton(a_szFirstButton) {
					public void onClick(DialogInterface dialog, int id) {
						
						dialog.dismiss();
						removeDialog(a_nDialogId);
						onClickDialog(a_nDialogId, a_szFirstButton, BaseActivity.DIALOG_CLICKED_BUTTON_FIRST);
						
					}
				});

				
				
//				return dialog;
			}
			
			case BaseActivity.DIALOG_TWO_BUTTON:
			{

				SKTDialog dialog = new SKTDialog(this, SKTDialog.DLG_TYPE_2);
								
				return dialog.getDialog(m_szTitle, a_szMessage, new DialogButton(a_szFirstButton) {
					public void onClick(DialogInterface dialog, int id) {
						
						dialog.dismiss();
						removeDialog(a_nDialogId);
						onClickDialog(a_nDialogId, a_szFirstButton, BaseActivity.DIALOG_CLICKED_BUTTON_FIRST);
						
					}
				},
				new DialogButton(a_szSecondButton) {
					public void onClick(DialogInterface dialog, int id) {
						
						dialog.dismiss();
						removeDialog(a_nDialogId);
						onClickDialog(a_nDialogId, a_szSecondButton, BaseActivity.DIALOG_CLICKED_BUTTON_SECOND);
						
					}
				});
				
//				return dialog;

				
			}
			
			case BaseActivity.DIALOG_THREE_BUTTON:
			{	

				AlertDialog.Builder aBuilder = new AlertDialog.Builder(this);
				aBuilder.setMessage(a_szMessage);
				aBuilder.setCancelable(true);
				aBuilder.setPositiveButton(a_szFirstButton, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();
						removeDialog(a_nDialogId);
						onClickDialog(a_nDialogId, a_szFirstButton, BaseActivity.DIALOG_CLICKED_BUTTON_FIRST);
					}
				});
				
				aBuilder.setNeutralButton(a_szSecondButton, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();
						removeDialog(a_nDialogId);
						onClickDialog(a_nDialogId, a_szSecondButton, BaseActivity.DIALOG_CLICKED_BUTTON_SECOND);
					}
				});
				
				aBuilder.setNegativeButton(a_szThirdButton, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();
						removeDialog(a_nDialogId);
						onClickDialog(a_nDialogId, a_szThirdButton, BaseActivity.DIALOG_CLICKED_BUTTON_THIRD);
					}
				});
				
				AlertDialog alertDialog = aBuilder.create();
				
				return alertDialog;
			}
			
			default:
				
				return null;
		
		} // end switch (a_nId)
		
	} // end public static Dialog createDialog(final int a_nDialogId, final String a_szMessage,
	
	/**
     * 소프트 입력기 닫기
     * 
     * @param a_oView
     * 현재 수정중인 뷰
     */
    public static boolean hideSoftInputWindow(View a_oView) {
        InputMethodManager imm = (InputMethodManager) a_oView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        return imm.hideSoftInputFromWindow(a_oView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 소프트 입력기 띄우기
     * 
     * @param a_oView 현재 수정중인 뷰
     */
    public static boolean showSoftInputWindow(View a_oView) {
        InputMethodManager imm = (InputMethodManager) a_oView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        return imm.showSoftInput(a_oView, InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }
	
} // end class BaseActivity