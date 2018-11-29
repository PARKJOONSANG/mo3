package com.ex.group.mail.activity;/*package com.ex.group.mail.activity;



import java.util.Map;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Handler.Callback;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ex.group.mail.R;
import com.ex.group.mail.data.EmailDatabaseHelper;
import com.ex.group.mail.data.EmailMainListData;
import com.ex.group.mail.service.EmailAddressRunnable;
import com.ex.group.mail.service.EmailMainThread;
import com.ex.group.mail.service.EmailReceiveThread;
import com.ex.group.mail.util.EmailClientUtil;
import com.ex.group.mail.widget.MainListAdapter;
import com.skt.pe.common.activity.SKTActivity;
import com.skt.pe.common.conf.Constants;
import com.skt.pe.common.conf.EnvironManager;
import com.skt.pe.common.conf.Resource;
import com.skt.pe.common.data.AuthData;
import com.skt.pe.common.data.SKTUtil;
import com.skt.pe.common.dialog.DialogButton;
import com.skt.pe.common.dialog.SKTDialog;
import com.skt.pe.common.exception.SKTException;
import com.skt.pe.common.service.XMLData;
import com.skt.pe.util.StringUtil;

*//**
 * 메일함 목록 화면
 * @author sjsun5318
 *
 *//*
public class EmailMainActivity_back extends SKTActivity implements OnItemClickListener, OnClickListener, OnMenuItemClickListener {
	String TAG = "MAIN";
	
	private EmailMainListData[] 	m_MainListDatas = null;
	private MainListAdapter 		adapter			= null;
	private Intent 					intent			= null;
	private TextView 				title			= null;
	private TextView				lastupdate		= null;
	private ListView 				list			= null;			//편지함
	private ListView 				doc_list			= null;	//문서함
	private Menu 					mMenu			= null;
	
	
	private  Handler mHandler = new Handler(new android.os.Handler.Callback() {
		*//**
		 * EmailMainThread 에서 넘어오는 handling
		 *//*
		@Override
		public boolean handleMessage(Message msg) {
			Log.d("handleMessage","EmailClientUtil.id = "+msg.what+":"+EmailClientUtil.id);
			if(msg.what == EmailClientUtil.START_RUNABLE) {
				setUpdateStart();
				if(mMenu != null) {
					mMenu.findItem(R.id.MENUREFRESH).setEnabled(false);
				}
			} else if(msg.what == EmailClientUtil.END_RUNABLE) {
				EmailDatabaseHelper helper = new EmailDatabaseHelper(EmailMainActivity_back.this);
				try {
					//현재 시간 가져온다.
					String up = helper.getBoxUpdate(EmailClientUtil.id, EmailClientUtil.companyCd);
					setUpdateEnd();
					
					if(!StringUtil.isNull(up)) {
						lastupdate.setText(up);
					}
					//메일함(폴더) 데이타 조회 id=>로그인 아이디					
					EmailMainListData[] mainListDatas = helper.getMainTableData(EmailClientUtil.id ,EmailClientUtil.companyCd);
					Log.i(TAG, "mainListDatas.. "+mainListDatas.length);
					adapter.clear();
					setUI(mainListDatas);
					EmailClientUtil.mainUpdate = false;
					if(mMenu != null) {
						mMenu.findItem(R.id.MENUREFRESH).setEnabled(true);
					}
					
				} finally {
					helper.close();
				}
			} else if(msg.what == EmailClientUtil.ADDRESS) {
				Log.d("HandlerMessage ","handlerMessage = " +EmailClientUtil.ADDRESS);
				Log.d("HandlerMessage ","handlerMessage = " +(String)msg.obj);
				Log.d("HandlerMessage ","handlerMessage = " +EmailClientUtil.empNm);
				
				EmailClientUtil.empNm = (String)msg.obj;
				EmailClientUtil.setMail(EmailMainActivity_back.this, EmailClientUtil.id, EmailClientUtil.empNm);				
				title.setText(EmailClientUtil.empNm);
				
			} else if(msg.what == EmailClientUtil.NO_TABLE_ERROR) {
				EmailClientUtil.mainUpdate = false;
				
			} else if(msg.what == EmailClientUtil.SKT_EXCEPTION) {
				EmailClientUtil.mainUpdate = false;
				EmailMainThread.setRunning(EmailClientUtil.END_RUNABLE);
				SKTException e = (SKTException)msg.obj;
				onCommonError(e);
			}
			return false;
		}
	});
	
	private void setUpdateStart() {
		findViewById(R.id.update_progress).setVisibility(View.VISIBLE);
		findViewById(R.id.updateBtn).setVisibility(View.GONE);
	}
	
	private void setUpdateEnd() {
		findViewById(R.id.update_progress).setVisibility(View.GONE);
		findViewById(R.id.updateBtn).setVisibility(View.VISIBLE);
	}
	
	
	*//**
	 * onCreate 메소드
	 * @param savedInstanceState
	 *//*
	@Override
    public void onCreateX(Bundle savedInstanceState) {
		
		EnvironManager.setNeedEncPwd(true);
    	EnvironManager.setTestEncPwd("FMH4T1+J2dgF/nOBXE4i+A==");
    	EnvironManager.setTestCompany("E10");
    	EnvironManager.setTestMdn("01071118162");
    	EmailClientUtil.LOGMODE = true;
    	
    	EmailMainThread.setRunning(EmailClientUtil.END_RUNABLE);
    	EmailReceiveThread.setRunning(null, EmailClientUtil.END_RUNABLE);
    	Log.d("onCreatex", "sj   EmailClientUtil.id =  " + EmailClientUtil.id);
    	
	}
    
	*//**
	 * 액션처리후 리스트 설정
	 * @param xmlData
	 *//*
	private void setUI(EmailMainListData[] data){
		if(!StringUtil.isNull(EmailClientUtil.empNm)) {
			title.setText(EmailClientUtil.empNm);
		}
		for(int a = 0 ; a < data.length ; a ++) {
			adapter.add(data[a]);
		}
    }
    
	
	 * (non-Javadoc)
	 * @see com.skt.pe.common.activity.SKTActivity#onAction(java.lang.String, java.lang.String[])
	 
    @Override
	protected XMLData onAction(String primitive, String... args)
	throws SKTException {
		return null;
	}
    
    
     * (non-Javadoc)
     * @see com.skt.pe.common.activity.SKTActivity#onActionPost(java.lang.String, com.skt.pe.common.service.XMLData, com.skt.pe.common.exception.SKTException)
     
	@Override
	protected void onActionPost(String primitive, XMLData result, SKTException e) {
		
	}

	
	 * (non-Javadoc)
	 * @see com.skt.pe.common.activity.SKTActivity#onActionPre(java.lang.String)
	 
	@Override
	protected int onActionPre(String primitive) {

		return Action.SERVICE_RETRIEVING;
	}

	
	 * (non-Javadoc)
	 * @see com.skt.pe.common.activity.SKTActivity#onActivityResultX(int, int, android.content.Intent)
	 
	@Override
	protected void onActivityResultX(int requestCode, int resultCode,
			Intent data) {
		
	}

	*//**
	 * onStart 메소드
	 *//*
	@Override
	protected void onStartX() {
		Log.d("xxxxxxxxxxx", "sj   onStart() " + EmailMainThread.getRunning());
		try {
			EmailClientUtil.companyCd 	= StringUtil.isNull(SKTUtil.getCheckedCompanyCd(this)) ? EnvironManager.getTestCompanyCd() : SKTUtil.getCheckedCompanyCd(this);
			EmailClientUtil.mdn			= StringUtil.isNull(SKTUtil.getMdn(this)) ? "mdn" : SKTUtil.getMdn(this);
			Map<String,String> map = SKTUtil.getGMPAuth(this);
			//login 처리
			//EmailClientUtil.id			= "19707314";//"19203920"; //"19203920"; //테스트 위해 주석 map.get(AuthData.ID_ID); 19516018 한도영, 19203920 김기형
			Log.i(TAG, map.get(AuthData.ID_ID));
			EmailClientUtil.id = "19516018";
//			EmailClientUtil.id			= map.get(AuthData.ID_ID);
			Log.d("onStartX", "sj   EmailClientUtil.id =  " + EmailClientUtil.id);
		} catch (SKTException e) {
			e.printStackTrace();
		}
		
		findViewById(R.id.updateBtn).setOnClickListener(this);
		findViewById(R.id.sendmail).setOnClickListener(this);
		title 		  	= (TextView)findViewById(R.id.settitle);
		lastupdate 		= (TextView)findViewById(R.id.lastupdate);
		list 			= (ListView)findViewById(R.id.MAINLISTVIEW);
		doc_list 			= (ListView)findViewById(R.id.MAINDOCVIEW);
		adapter 		= new MainListAdapter(this,R.layout.mail_box_item);
		
		list.setAdapter(adapter);
		list.setOnItemClickListener(this);
		doc_list.setAdapter(adapter);
		doc_list.setOnItemClickListener(this);
		EmailDatabaseHelper helper = new EmailDatabaseHelper(EmailMainActivity_back.this);
		try {
			if(EmailMainThread.getRunning()) {
				EmailMainThread.runningThread.interrupt();
			}
			Log.d("xxxxxxxxxxx", "sj   aa" + EmailMainThread.getRunning());
			String up = helper.getBoxUpdate(EmailClientUtil.id, EmailClientUtil.companyCd);
			if(StringUtil.isNull(up)) {
				lastupdate.setText("");
			} else {
				lastupdate.setText(up);
			}
			checkEmailAddr();
			title.setText(EmailClientUtil.empNm);
			
			m_MainListDatas = helper.getMainTableData(EmailClientUtil.id, EmailClientUtil.companyCd);

			if(m_MainListDatas != null) {
				setUI(m_MainListDatas);
			}
		}catch(Exception e){
			Log.e("","error : EmailMainActivity = " +e.toString());
		
		}finally {
			helper.close();
		}

		Log.d("xxxxxxxxxxx", "sj   aa" + EmailClientUtil.mainUpdate + EmailReceiveThread.getRunning());
		
		if(!EmailClientUtil.mainUpdate && !EmailReceiveThread.getRunning()) {
			EmailClientUtil.mainUpdate = true;
			//메일함(폴더) 가져오는 스레드
			EmailMainThread thread = new EmailMainThread(this,mHandler);
			thread.start();
		}

	}	
	
	*//**
	 * 메일 확인후 요청
	 *//*
	private void checkEmailAddr() {
//		if(StringUtil.isNull(EmailClientUtil.empNm)) {
			EmailAddressRunnable addr = new EmailAddressRunnable(this,mHandler,EmailClientUtil.id,EmailClientUtil.companyCd);
			new Thread(addr).start();
//		} 
		
	}
	
	*//**
	 * 옵션 메뉴 생성
	 * @param menu
	 * @return
	 *//*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		if(mMenu == null) {
			inflater.inflate(R.menu.foldermenu, menu);
			this.mMenu = menu;
			
		} 
		if(EmailClientUtil.mainUpdate) {
			menu.findItem(R.id.MENUREFRESH).setEnabled(false);
		}
	
		return true;
	}

	*//**
	 * 옵션 메뉴 선택 이벤트 헨들러<br>
	 * - 업데이트
	 * - 편지쓰기
	 * - 서명설정
	 * @param item
	 * @return
	 *//*
	@Override 
	public boolean onOptionsItemSelected(MenuItem item) {
		
		if(item.getItemId()==R.id.MENUWRITE){
			intent = new Intent(this,EmailWriteActivity.class);
			intent.putExtra("aaa", "aaa");
			intent.putExtra("refer", "main");
			startActivity(intent);
		}else if(item.getItemId()==R.id.MENUREFRESH){
			checkEmailAddr();
			EmailMainThread thread = new EmailMainThread(this,mHandler);
			thread.start();
			EmailClientUtil.mainUpdate = true;
//		}else if(item.getItemId()== R.id.MENU_SIGN_SETING){
//			EmailDialog di = new EmailDialog(this);
//			di.show();
		}
		return super.onOptionsItemSelected(item); 
	}
	
	*//**
	 * 편지함 목록 선택시 이벤트 핸들러(리스트 이동)	   
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 *//*
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if(EmailMainThread.getRunning()) {
			EmailMainThread.runningThread.interrupt();
		}

		intent = new Intent(this,EmailReceiveActivity.class);
		intent.putExtra("mainData", adapter.getItem(arg2));
		startActivity(intent);	
	}
	
	*//**
	 * 설정할 레이아웃을 리턴한다.
	 *//*
	@Override
	protected int assignLayout() {
		return R.layout.mail_box;
	}

	*//**
	 * onClick 이벤트 핸들러<br>
	 * - Update 버튼
	 * @param v
	 *//*
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.updateBtn) {
			if(EmailClientUtil.mainUpdate) {
				return;
			} else {
				checkEmailAddr();
				EmailMainThread thread = new EmailMainThread(this,mHandler);
				thread.start();
				EmailClientUtil.mainUpdate = true;
			}
		}else if(v.getId() == R.id.sendmail){
			intent = new Intent(this,EmailWriteActivity.class);
			intent.putExtra("aaa", "aaa");
			intent.putExtra("refer", "main");
			startActivity(intent);			
		}
	}
	
	
	 * (non-Javadoc)
	 * @see com.skt.pe.common.activity.SKTActivity#OnAuthDone()
	 
	public void OnAuthDone() {
		blocking(-1);
		onCreateX(null);
	}

	*//**
	 * 특정에러 코드 핸들링<br>
	 * @param ex
	 *//*
	@Override
	public void onCommonError(SKTException ex) {
		super.onCommonError(ex);
		if(ex != null) {
			if("5404".equals(ex.getErrCode())) {
				ex.alert(this, new DialogButton(0) {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				});	
			} else if(Constants.Status.E_NOT_CONNECT_SSLVPN_ID.equals(ex.getErrCode())) {
				SKTDialog dlg = new SKTDialog(this);
				dlg.getDialog("확인",  Resource._E072_ko, new DialogButton(0) {
					public void onClick(DialogInterface arg0, int arg1) {
						SKTUtil.closeApp(EmailMainActivity_back.this);
					}
				}).show();
			} else if(Constants.Status.E_NOT_INSTALL_SSLVPN_ID.equals(ex.getErrCode())) {
				SKTDialog dlg = new SKTDialog(this);
				dlg.getDialog("확인", Resource._E071_ko, new DialogButton(0) {
					public void onClick(DialogInterface arg0, int arg1) {
						SKTUtil.closeApp(EmailMainActivity_back.this);
					}
				}).show();
			}
		}
	}

	@Override
	public boolean onMenuItemClick(MenuItem arg0) {
		// TODO Auto-generated method stub
		
		return false;
	}

	 (non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		Log.d("xxxxxxxxxxx", "sj   onDestroy() " + EmailMainThread.getRunning());
		
		EmailDatabaseHelper helper = new EmailDatabaseHelper(EmailMainActivity_back.this);
		helper.dropTables();
		helper.close();
		
		super.onDestroy();
	}

	 (non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 
	@Override
	public void onBackPressed() {
		Log.d("xxxxxxxxxxx", "sj   onBackPressed() " + EmailMainThread.getRunning());
		if(EmailMainThread.getRunning()) {
			EmailMainThread.runningThread.interrupt();
			EmailMainThread.setRunning(EmailClientUtil.END_RUNABLE);
			EmailClientUtil.mainUpdate = false;
		}
		if(EmailReceiveThread.getRunning()) {
			EmailReceiveThread.runningThread.interrupt();
			EmailReceiveThread.setRunning(EmailClientUtil.END_RUNABLE);
		}
		super.onBackPressed();
	}

	// Handler 를 이용하여 처리하기
	private boolean m_Flag = false;
	Handler back_Handler = new Handler(new Callback() {
		public boolean handleMessage(Message msg) {
			if(msg.what == 0) {
				m_Flag = false;
			}
			return true;
		}
	});	

	// 뒤로가기버튼설정
	public boolean onKeyDown(int KeyCode, KeyEvent event) {
		super.onKeyDown(KeyCode, event);
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			switch (KeyCode) {
			case KeyEvent.KEYCODE_BACK: // 뒤로 키와 같은 기능을 한다.
				if (false) {
					Toast.makeText(this, "'뒤로' 버튼을 한번 더 누르시면 종료됩니다.",
							Toast.LENGTH_SHORT).show();
					// 버튼클릭시 true
					m_Flag = true;
					// Handler 호출 (2초 이후 back_Check 값 false)
					back_Handler.sendEmptyMessageDelayed(0, 2000);
					return false;
				} else {
					
					try{
						SKTUtil.runApp(this, "com.ex.group.folder");	
					}catch(Exception e){
						e.printStackTrace();
					}						
					
					moveTaskToBack(true);
					finish();
					return true;
				}
			}
			return false;
		}		
		return false;
	}		
	
}*/