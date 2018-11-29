package com.ex.group.mail.addressbook.activity;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ex.group.mail.addressbook.data.MinistryData;
import com.skt.pe.common.conf.Constants;
import com.skt.pe.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

import com.ex.group.folder.R;
/**
 * 멤버 다중 선택 화면
 * @author jokim
 *
 */
public class AddressMultiSelectActivity extends BaseActivity {
	
	ListView mListView;
	SelectArrayAdapter mAdapter;
	
    boolean m_bIsChild = false;
    String m_szSuffixDept;
    String mType;
    String mNames[];
    String mCodes[];
    String mInfos[];
    boolean mIsData[];
    boolean mIsMembers[];
    boolean isSelectAll = false;
    boolean mUseEmail = false;
    
    private String names[];
    private String teams[];
    private String roles[];
    private boolean orgChartInfo = false;
    
    final int DIALOG_MEMBER_MULTISELECT = 1111;
    final int DIALOG_NO_APP = 1112;
    final int DIALOG_OVER_COUNT = 1113;
    
    final int DIALOG_NOT_EMAIL = 5000;

	@Override
	protected int assignLayout() {
		// TODO Auto-generated method stub
		return R.layout.mail_member_membermultiselect;
	}
	
	public void onCreateX(Bundle instanceState)
	{
		super.onCreateX(instanceState);
//		setContentView(R.layout.member_membermultiselect);		
		orgChartInfo = getIntent().getBooleanExtra("orgChart", false);
		mType = getIntent().getStringExtra("type");
		if(mType == null || "".equals(mType)) {
			mType = "sms";
		}
		
		mUseEmail = getIntent().getBooleanExtra("useEmail", false);
		mNames = getIntent().getStringArrayExtra("names");
		mCodes = getIntent().getStringArrayExtra("codes");
		mInfos = getIntent().getStringArrayExtra("infos");
		mIsData = getIntent().getBooleanArrayExtra("isData");
		mIsMembers = getIntent().getBooleanArrayExtra("isMembers");
		m_bIsChild = getIntent().getBooleanExtra("isChild", false);
		
		//팀 & 직급 필요..!!
		teams = getIntent().getStringArrayExtra("team");
		roles = getIntent().getStringArrayExtra("role");
		 
		mListView = (ListView)findViewById(R.id.MMSelectList);
        mAdapter = new SelectArrayAdapter(this);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> adapterView, View view, int index,
                                    long arg3) {
				// TODO Auto-generated method stub
				
				if ("sms".equals(mType)) {
					if(!mIsData[index]) {
						return;
					}
				}
				
				MinistryData data = (MinistryData)mListView.getItemAtPosition(index);
				ImageView v = (ImageView)view.findViewById(R.id.employee_check);
				
				if(data.m_bIsChecked) {
					data.m_bIsChecked = false;
					v.setImageResource(R.drawable.mail_member_check_off);
				} else {
					data.m_bIsChecked = true;
					v.setImageResource(R.drawable.mail_member_check_on);
				}
			}
		});
		
		for(int i = 0; i < mNames.length; i++) {
			if(m_bIsChild) {
				addCurTeamData(mCodes[i], mNames[i], mInfos[i], null, mIsMembers[i] ,teams[i], roles[i]);
			} else {
				addCurTeamData(mCodes[i], mNames[i], mInfos[i], null, false, teams[i], roles[i]);
			}
			Log.d("xxxxxxxxxxx", "xxxxxxxxxxxxx"  + mCodes[i]);
		}
        
        onPostCreateX();
	}
	
	protected void onPostCreateX() {
		TextView tv = (TextView)findViewById(R.id.multiselecttitle_text);
		if(mType.equals("sms")) {
			tv.setText(R.string.mail_send_group_sms);
		} else {
			tv.setText(R.string.mail_send_group_email);
		}
		
		Button mUpButton = (Button)findViewById(R.id.multiselecttitle_check);
		mUpButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
            	allItemSelected();
            	mAdapter.notifyDataSetChanged();
            	
            	Button mTempButton = (Button)view;
            	if(isSelectAll) {
            		mTempButton.setText(R.string.mail_unselect_all);
            	} else {
            		mTempButton.setText(R.string.mail_select_all);
            	}
            }
        });
		
		mUpButton = (Button)findViewById(R.id.multiselect_confirm);
		mUpButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
            	int count = getItemSelected();
            	Intent intent;
            	if(count > 0) {
//        			if(mType.equals("sms") && !m_bIsChild) {
            		Log.d("xxxxxxxxx", "xxxxxxxxxxxxx   " + mType);
        			if(mType.equals("sms")) {
		            	if(count > 50) {
		            		m_szTitle = getString(R.string.mail_text_send);
		            		m_szDialogMessage = getString(R.string.mail_over_count);
		            		showDialog(DIALOG_OVER_COUNT);
		            		return;
		            	}
		            	sendSms();
        			} else {
        				
//	        			if(mType.equals("sms")) {
		            		intent = new Intent(ACTION_EMAIL_CLIENT);
//	        			} else {
//			            	intent = new Intent(AddressMultiSelectActivity.this,
//			            			SendEMAILActivity.class);
//	        			}	        				        	
	        			
//	        			if(orgChartInfo) //조직도에서만 다른 메일을 보냈을 시
////	        			if(!mUseEmail)
//	        			{
//	        				ArrayList<String> mEmployeeNum = new ArrayList<String>();
//			            	ArrayList<String> mEmployeeName = new ArrayList<String>();
//			            	ArrayList<String> mEmployeeIsMember = new ArrayList<String>();
//			            	MinistryData data = null;
//			            	
//			            	for(int i = 0; i < mListView.getCount(); i++) {
//			            		data = (MinistryData)mListView.getItemAtPosition(i);
//			        			if(data.m_bIsChecked) {
//			        				mEmployeeNum.add(data.m_szSerialNo);
//			        				mEmployeeName.add(data.m_szName);
//			        				mEmployeeIsMember.add(data.isMember ? "true" : "false");
//			        			}
//			        		}
//			            	intent.putStringArrayListExtra("employeenum", mEmployeeNum);
//			            	intent.putStringArrayListExtra("employee", mEmployeeName);
//			            	intent.putStringArrayListExtra("isMember", mEmployeeIsMember);
//			            	intent.putExtra("isChild", m_bIsChild);
//			            	SKTUtil.log(Log.DEBUG, "Xxxxxxxxxx", "xxxxxxxxxxxxxx  bbbbbb  111"  );
//			            	startActivityForResult(intent, 1);
//	        			}
//	        			else
//	        			{
	        				try 
	    					{	      
//	        					sendMailToAddress(intent);
	        					sendMailToName(intent);	    						
	    					} 
	    					catch(ActivityNotFoundException e)
	    					{
	    						m_szTitle = getString(R.string.mail_title_no_mail);
	    						m_szDialogMessage = getString(R.string.mail_no_emailclient);
	    	            		showDialog(DIALOG_NO_APP);
	    					}
//	        			}
	        			
        			}
            	}
            }
        });
		
		mUpButton = (Button)findViewById(R.id.multiselect_cancel);
		mUpButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
            	finish();
            }
        });
	}
	
	private void sendMailToAddress(Intent intent)
	{
		intent = new Intent(ACTION_EMAIL_CLIENT);
    	MinistryData data = null;
    	String emails = "";
    	
    	for(int i = 0; i < mListView.getCount(); i++) 
    	{
    		data = (MinistryData)mListView.getItemAtPosition(i);
			
    		if(data.m_bIsChecked && !StringUtil.isNull(data.m_szDepartment))			
				emails += data.m_szDepartment + ((i != mListView.getCount() - 1) ? ";" : "");			
		}
    	
    	intent.putExtra("toList", emails);
		startActivity(intent);
	}
	
	/*
	 * 이름으로 메일 보내기.
	 */
	private void sendMailToName(Intent intent)
	{
		intent = new Intent(ACTION_EMAIL_CLIENT);
//		intent.putExtra("wtype", "urlforward");
    	MinistryData data = null;
    				            
    	List<String> nameList = new ArrayList<String>();
    	List<String> emailList = new ArrayList<String>();
    	List<String> vvipList = new ArrayList<String>();
    	
    	for(int i = 0; i < mListView.getCount(); i++) 
    	{
    		data = (MinistryData)mListView.getItemAtPosition(i);
			
    		if(data.m_bIsChecked)
    		{
    			nameList.add(data.m_szName);
    			emailList.add(data.m_szSerialNo);
    			vvipList.add(checkVvip(data.m_szVvip));
    		}
		}
    	
    	intent.putExtra("type", "side");
    	String nameArray[] = new String[nameList.size()];
    	for(int i = 0; i < nameArray.length; i++)
    		nameArray[i] = nameList.get(i);
    	String emailArray[] = new String[emailList.size()];
    	for(int i = 0; i < emailArray.length; i++){
    		emailArray[i] = emailList.get(i);
    	}
    	String vvipArray[] = new String[vvipList.size()];
    	for(int i = 0; i < vvipArray.length; i++)
    		vvipArray[i] = vvipList.get(i);
    	intent.putExtra("names", nameArray);
    	intent.putExtra("emails", emailArray);				            
    	intent.putExtra("vvip", vvipArray);				            
		startActivity(intent);
	}
	
	/**
	 * 문자 메세지 보내는 함수
	 */
	private void sendSms() {
    	MinistryData data = null;
    	String target = "";
    	int cnt = 0;
    	
    	for(int i = 0; i < mListView.getCount(); i++) {
    		data = (MinistryData)mListView.getItemAtPosition(i);
			if(data.m_bIsChecked) {
				cnt++;
				target += data.m_szSerialNo + ";";
			}
			if(cnt >= 50) {
				break;
			}
		} 
        Uri uri = Uri.parse("sms:" + (String)target);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    	
	}
	
	@Override
	protected void onActivityResultX(int requestCode, int resultCode, Intent data) {
	}
	
	protected Dialog onCreateDialog(int a_nId) {
		Dialog aDialog = null;
    	
		switch (a_nId) {
		case DIALOG_OVER_COUNT:
		case DIALOG_NO_APP:
		case DIALOG_MEMBER_MULTISELECT:
			aDialog = createDialog(a_nId, m_szDialogMessage, DIALOG_ONE_BUTTON,
					getString(R.string.mail_ok_button), null, null);
    		break;
		case DIALOG_NOT_EMAIL :
			aDialog = createDialog(a_nId, "E-Mail 연동 미구현", DIALOG_ONE_BUTTON,
					getString(R.string.mail_ok_button), null, null);
		} // end switch (a_nId)
    	
    	return aDialog;
	}
	
	/* (non-Javadoc)
	 * 팝업 버튼 클릭 이벤트 핸들러<br>
	 * @see com.sk.pe.group.activity.BaseActivity#onClickDialog(int, java.lang.String, int)
	 */
	public void onClickDialog(int a_nDialogId, String a_szButtonText, int a_nClickedButton) {
		switch (a_nDialogId) {
		case DIALOG_MEMBER_MULTISELECT:
			finish();
    		break;
		case DIALOG_NO_APP:
			
			// 스토어   설치 후 주석 삭제 
			
			Intent intent = new Intent(Constants.Action.STORE_DETAIL_VIEW);
			intent.putExtra("APP_ID", "MOGP000001");
			startActivity(intent);
						
			
			break;
		case DIALOG_OVER_COUNT:
			sendSms();
			break;
		} // end switch (a_nDialogId)
	} // end public void onClickDialog(int a_nDialogId, String a_szButtonText, int a_nClickedButton)
	
	private void addCurTeamData(String a_ParamA, String a_ParamB, String a_ParamC,
                                String a_ParamD, boolean isMember, String team, String role)
	{
		MinistryData data = new MinistryData();
		data.m_szSerialNo = checkTrim(a_ParamA);
		data.m_szName = checkTrim(a_ParamB);
		data.m_szDepartment = checkTrim(a_ParamC);
		data.m_szLocation = checkTrim(a_ParamD);
		data.m_bIsChecked = false;
		data.isMember = isMember;
		data.m_szTeam = team;
		data.m_szRole = role;
		
		mAdapter.add(data);
	}
	
	private String checkTrim(String a_sParam) {
		if (a_sParam==null)
			return "";
		return a_sParam;
	}
	
	private String checkVvip(String a_sParam) {
		if (a_sParam==null)
			return "N";
		return a_sParam;
	}
	
	/**
	 * 전체 멤버 선택/취소
	 */
	protected void allItemSelected() {
		if(isSelectAll) {
			isSelectAll = false;
		} else {
			isSelectAll = true;
		}
		for(int i = 0; i < mListView.getCount(); i++) {
			MinistryData data = (MinistryData)mListView.getItemAtPosition(i);
			if(mIsData[i]) {
				data.m_bIsChecked = isSelectAll;
			}
		}
	}
	
	/**
	 * 선택된 멤버 목록 카운트 리턴
	 * @return 선택된 멤버 카운트
	 */
	protected int getItemSelected() {
		int nCount = 0;
		
		for(int i = 0; i < mListView.getCount(); i++) {
			MinistryData data = (MinistryData)mListView.getItemAtPosition(i);
			
			if(data.m_bIsChecked) {
				nCount++;
			}
		}
		
		return nCount;
	}
	
	private class SelectArrayAdapter extends ArrayAdapter<MinistryData> {
		public SelectArrayAdapter(Context context) {
			super(context, 0);
		}

		/* (non-Javadoc)
		 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if(convertView == null) {
				convertView = getLayoutInflater().inflate(R.layout.mail_member_employeeteam, null);
			}
        	final MinistryData data = (MinistryData)getItem(position);
        	 
        	TextView name = (TextView)convertView.findViewById(R.id.ROW_EMPLOYEE_NAME);
            name.setText(data.m_szName);             
            TextView team = (TextView)convertView.findViewById(R.id.ROW_EMPLOYEE_TEAM);
            team.setText(data.m_szTeam);
                        
//            if(StringUtil.isNull(team.getText().toString()) || StringUtil.isNull(data.m_szRole))            
//            	team.setVisibility(View.GONE);            
            TextView role = (TextView)convertView.findViewById(R.id.ROW_EMPLOYEE_CORP);
            role.setText(data.m_szRole);
 
        	final ImageView iv = (ImageView)convertView.findViewById(R.id.employee_check);
//        	if(!mIsData[position]) {
//        		name.setTextColor(Color.LTGRAY);
//        		iv.setImageResource(R.drawable.member_check_off);
//        	} else {
//        		name.setTextColor(Color.BLACK);
	        	if(data.m_bIsChecked) {
	        		iv.setImageResource(R.drawable.mail_member_check_on);
	        	} else {
	        		iv.setImageResource(R.drawable.mail_member_check_off);
	        	}
//        	}
			
			return convertView;
		}
	}
}