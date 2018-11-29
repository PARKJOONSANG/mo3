package com.ex.group.mail.addressbook.activity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ex.group.mail.addressbook.data.MinistryData;
import com.ex.group.mail.addressbook.data.SmsData;
import com.skt.pe.common.data.SKTUtil;
import com.skt.pe.common.exception.SKTException;
import com.skt.pe.common.service.Controller;
import com.skt.pe.common.service.Parameters;
import com.skt.pe.common.service.XMLData;
import com.skt.pe.util.XMLUtil;

import java.util.ArrayList;
import java.util.Map;

import com.ex.group.folder.R;

/**
 * 단체로 문자 보내는 화면
 * @author jokim
 *
 */
public class SendGroupSMSActivity extends BaseActivity implements OnClickListener {
    private final int PEOPLE_DEPT = 0;
    
    private final int DIALOG_MEMBER_LIST = 0;
    
    private LinearLayout mUpButton;
    private LinearLayout mLinearLayout;
    private LinearLayout mLogoLayout;
    private TextView mSuffixDeptText;
    private TextView mUpButtonText;
    private TextView mAddList;
    private TextView mAddList2;
    private ListView mListView;
    private Button mBtnSelect;
    private Button mBtnAddList;
    private Button mBtnSend;

    private String tempDeptCode = null;
    private String tempParentDeptCode = null;
    private String treeTempDeptCode = "";
    private String tempTitle = null;
    private String tempMemberCnt = "0";
    private boolean isSelectAll;
    private ArrayList<MinistryData> mCurTeamData;
    public static ArrayList<SmsData> sSmsList;
    
	/**
	 * @see android.app.Activity#onCreate(Bundle)
	 */
	@Override
	public void onCreateX(Bundle savedInstanceState) {
    	super.onCreateX(savedInstanceState);
		// TODO Put your code here
		mListView = (ListView)findViewById(R.id.list);
		mLinearLayout = (LinearLayout)findViewById(R.id.up_button_layout);
		mUpButton = (LinearLayout)findViewById(R.id.up_button);
		mUpButtonText = (TextView)findViewById(R.id.up_button_text);
		mSuffixDeptText = (TextView)findViewById(R.id.suffix_dept_text);
		
		
		mCurTeamData = new ArrayList<MinistryData>();
		sSmsList = new ArrayList<SmsData>();
//		updateReceiveList();
		
		//ch
		String myCompany = getCompanyName();
//		((TextView)findViewById(R.id.up_button_text)).setText(myCompany);
//		((TextView)findViewById(R.id.up_button_text)).setText(R.string.label_organchart);
		
		String deptCode = getIntent().getStringExtra("deptCode");
		if(deptCode == null) {
			deptCode = "";
		}
		requestData(PEOPLE_DEPT, deptCode);
	}

	@Override
	protected int assignLayout() {
		// TODO Auto-generated method stub
		return R.layout.mail_member_groupsms;
	}

	protected Dialog onCreateDialog(int a_nId) {
		Dialog aDialog = null;
    	
		switch (a_nId) {
		case DIALOG_MEMBER_LIST:
			aDialog = createDialog(a_nId, m_szDialogMessage, DIALOG_ONE_BUTTON,
					getString(R.string.mail_ok_button), null, null);
    		break;
		} // end switch (a_nId)
    	
    	return aDialog;
	}
    
	private void setTeamName(String parentTeamName, String currentTeamName)
	{
//		if(parentTeamName != null)
//			mSuffixDeptText.setText(parentTeamName);
//		
		SKTUtil.log(Log.DEBUG, "xxxx", "xxxx currentTeamName :;  " + currentTeamName);
//		if(currentTeamName != null)
			mSuffixDeptText.setText(currentTeamName);		
	}
	
    /* (non-Javadoc)
     * 팝업 클릭 이벤트 핸들러
     * @see com.sk.pe.group.activity.BaseActivity#onClickDialog(int, java.lang.String, int)
     */
    public void onClickDialog(int a_nDialogId, String a_szButtonText, int a_nClickedButton) {
		switch (a_nDialogId) {
		case DIALOG_MEMBER_LIST:
//			finish();
    		break;
		} // end switch (a_nDialogId)
	}

	/* (non-Javadoc)
	 * 액션 처리 후 UI 세팅<br>
	 * - 부서 정보 요청
	 * @see com.sk.pe.group.activity.BaseActivity#onProcessThread()
	 */
	public XMLData onProcessThread() throws SKTException {
		Parameters params = null;
	    Controller controller = null;
	    XMLData nXMLData = null;
		String req = (String)m_requestObject;
		String comCd = "";
	    
		switch (m_nRequestType) {
		case PEOPLE_DEPT:
//			if(req != null && !req.equals("")) {
//				comCd = req.substring(0, req.indexOf("."));
//			}
        	params = new Parameters("COMMON_PEOPLE_DEPT");
//        	params.put("infoCompanyCd", comCd);
        	params.put("infoCompanyCd", SKTUtil.getCheckedCompanyCd(this));
			params.put("deptCode", (String)m_requestObject);
			break;
		}

		controller = new Controller(this);
		nXMLData = controller.request(params);
		
		return nXMLData;
	}

	/* (non-Javadoc)
	 * 액션 처리 핸들러<br>
	 * - 부서 정보 요청
	 * @see com.sk.pe.group.activity.BaseActivity#onPostThread(com.skt.pe.common.service.XMLData, com.skt.pe.common.exception.SKTException)
	 */
	public void onPostThread(XMLData a_XMLData, SKTException a_exception) {
    	// 정상 응답이  발생하였으면 오류 메시지 보여주고 리턴
		isSelectAll = false;
		mBtnSelect.setText(R.string.mail_select_all);
		
		if(a_exception != null) {
			m_szTitle = getString(R.string.mail_text_error);
    		m_szDialogMessage = a_exception.getMessage();
			showDialog(DIALOG_MEMBER_LIST);
			return;
		} // end else if (m_szResponseCode != null && m_szResponseCode.equals("1000") == false)
		try {
			switch (m_nRequestType) {
			case PEOPLE_DEPT:
				m_szResponseCode = a_XMLData.get("result");
				m_szResponseMessage = a_XMLData.get("resultMessage");
				
				tempParentDeptCode = a_XMLData.get("parentDeptCode");
				tempDeptCode = XMLUtil.getTextContent(a_XMLData.getNode("/mo/dept/deptCode"));
				tempTitle = XMLUtil.getTextContent(a_XMLData.getNode("/mo/dept/suffixDept"));
				tempMemberCnt = a_XMLData.get("memberCnt");
				if(tempMemberCnt == null) {
					tempMemberCnt = "0";
				}

				mCurTeamData.clear();
				
				a_XMLData.setList("deptMember");
				for(int i = 0; i < a_XMLData.size(); i++) {
					addCurTeamData(a_XMLData.get(i, "id"), a_XMLData.get(i, "name"),
							a_XMLData.get(i, "telNum"), a_XMLData.get(i, "role"), tempTitle, "", "",
							"", a_XMLData.get(i, "email"), a_XMLData.get(i, "empId"), true);
				}
				
				a_XMLData.setList("childDept");
				for(int i = 0; i < a_XMLData.size(); i++) {
					addCurTeamData(a_XMLData.get(i, "deptCode"), a_XMLData.get(i, "suffixDept"), "",
							"", a_XMLData.get(i, "haveChild"), "", "", "", "", "", false);
				}
				
//				if(!tempDeptCode.contains("ROOT")) {
				if(!tempDeptCode.contains("")) {
    				showDepartment(tempTitle);
				} else {
//					Bitmap img = null;
//					
//					try {
//						String szLogo = a_XMLData.get("companyLogo");
//						if(szLogo.startsWith("http")) {
//							img = ResourceUtil.getBitmapByUrl(szLogo);
//						} else {
//							img = ResourceUtil.decodeBitmap(szLogo);
//						}
//						ImageView logo = (ImageView)findViewById(R.id.LOGO_COMPANY);
//						logo.setImageBitmap(img);
//					} catch(Exception e) {
//						e.printStackTrace();
//					}
    				showOrgAll();
				}
				break;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * 클릭 이벤트 핸들러<br>
	 * - 다중 선택 버튼<br>
	 * - 확인 버튼<br>
	 * - 보내기 버튼<br>
	 * - 목록 추가 버튼
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId() == R.id.multiselecttitle_check) {
	    	allItemSelected();
	    	mTeamAdapter.notifyDataSetChanged();
	    	
	    	Button mTempButton = (Button)v;
	    	if(isSelectAll) {
	    		mTempButton.setText(R.string.mail_unselect_all);
	    	} else {
	    		mTempButton.setText(R.string.mail_select_all);
	    	}
		} else if(v.getId() == R.id.multiselect_confirm) {
			ArrayList<MinistryData> data = getSelectedNames();
			
			for(int i = 0; i < data.size(); i++) {
				int j;
				MinistryData employee = data.get(i);
				
				if(sSmsList.size() >= 50) {
					m_szTitle = getString(R.string.mail_add_receiver);
		    		m_szDialogMessage = getString(R.string.mail_add_receiver_text);
					showDialog(DIALOG_MEMBER_LIST);
					break;
				}
				
				for(j = 0; j < sSmsList.size(); j++) {
					SmsData sms = sSmsList.get(j);
					
					if(sms.getPhone().equals(employee.m_szCellPhoneNo)) {
						break;
					}
				}
				if(j != sSmsList.size()) {
					continue;
				}
				
				sSmsList.add(new SmsData(employee.m_szName, employee.m_szCellPhoneNo));
			}
			updateReceiveList();
//		} else if(v.getId() == R.id.multiselect_send) {
//			sendSms();
//		} else if(v.getId() == R.id.suffix_dept_text || v.getId() == R.id.add_list_text2) {
//			Intent intent = new Intent(this, SendGroupSMSListActivity.class);
//			startActivityForResult(intent, 1);
		}
	}
	
	@Override
	protected void onActivityResultX(int requestCode, int resultCode, Intent data) {
		updateReceiveList();
	}
	
	/**
	 * 추가된 목록 카운트 업데이트 함수
	 */
	private void updateReceiveList() {
		mAddList.setText(getString(R.string.mail_receive_list) + " (" + sSmsList.size() + "/50)");
		mAddList2.setText(getString(R.string.mail_receive_list) + " (" + sSmsList.size() + "/50)");
	}
	
	/**
	 * 문자 메세지 보내기 함수
	 */
	private void sendSms() {
    	String target = "";
    	
    	for(SmsData sms: sSmsList) {
    		String phone = sms.getPhone();
    		
    		target += phone + ";";
    	}
        Uri uri = Uri.parse("sms:" + (String)target);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
	}
	
	/**
	 * 모두 선택/취소 함수
	 */
	private void allItemSelected() {
		if(isSelectAll) {
			isSelectAll = false;
		} else {
			isSelectAll = true;
		}
		for(int i = 0; i < mListView.getCount(); i++) {
			MinistryData data = (MinistryData)mListView.getItemAtPosition(i);
			if(data.isMember) {
				data.m_bIsChecked = isSelectAll;
			}
		}
	}
	
	/**
	 * 선택된 멤버 및 부서 목록 리턴
	 * @return 선택된 멤버 및 부서 목록
	 */
	private ArrayList<MinistryData> getSelectedNames() {
		ArrayList<MinistryData> data = new ArrayList<MinistryData>();
		
		for(MinistryData min: mCurTeamData) {
			if(min.m_bIsChecked && min.m_szCellPhoneNo.length()>1) {
				data.add(min);
			}
		}
		
		return data;
	}
	
	/**
	 * 회사 이름 리턴
	 * @return 회사 이름
	 */
	public String getCompanyName() {
		String ret = "";
		
		try {
			String code = SKTUtil.getCheckedCompanyCd(this);
			Map<String, String> map = SKTUtil.getCompanyList(this);
			
			ret = map.get(code);
		} catch(SKTException e) {
			e.printStackTrace();
		}
		
		return ret;
	}

	private void addCurTeamData(String a_ParamA, String a_ParamB, String a_ParamC, String a_ParamD,
                                String a_ParamE, String a_ParamF, String a_ParamG, String a_ParamH, String a_ParamI,
                                String a_ParamJ, boolean isMember) {
		MinistryData data = new MinistryData();
		data.m_szSerialNo = checkTrim(a_ParamA);
		data.m_szName = checkTrim(a_ParamB);
		data.m_szCellPhoneNo = checkPhone(a_ParamC);
		data.m_szRole = checkTrim(a_ParamD);
		data.m_szDepartment = checkTrim(a_ParamE);
		data.m_szCompany = checkTrim(a_ParamF);
		data.m_szType = checkTrim(a_ParamG);
		data.m_szCompanyCd = checkTrim(a_ParamH);
		data.m_szEmail = checkTrim(a_ParamI);
		data.m_szEmpId = checkTrim(a_ParamJ);
		data.m_nCountNum = mCurTeamData.size();
		data.m_bIsChecked = false;
		data.isMember = isMember;
		mCurTeamData.add(data);
	}
	
	private String checkTrim(String a_sParam) {
		if(a_sParam == null) {
			return "";
		}
		
		return a_sParam.trim();
	}
	
	private String checkPhone(String phone) {
		String ret = "";
		
		if(phone == null) {
			return ret;
		}
		
		ret = phone.replace("-", "").trim();
		
		return ret;
	}

    /**
     * 부서 이름 보여주기
     * @param a_szTeamName 부서이름
     */
    private void showDepartment(String a_szTeamName) {
    	
//    	mLinearLayout.setVisibility(View.VISIBLE);
//    	mLogoLayout.setVisibility(View.GONE);
//    	mUpButtonText.setText(a_szTeamName);
        
    	((TextView)findViewById(R.id.txt_team_name)).setText(a_szTeamName);
    	
    	mUpButton.setEnabled(true);
    	mUpButton.setBackgroundResource(R.drawable.mail_member_btn_updept_selector);
    	
    	mUpButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
            	requestData(PEOPLE_DEPT, tempParentDeptCode);
            }
        });
        mListView.setAdapter(mTeamAdapter);
        mListView.setOnItemClickListener(mTeamClickListener);
    }

    /**
     * 회사명 보여주기
     */
    private void showOrgAll() {
//    	mLinearLayout.setVisibility(View.GONE);
//    	mLogoLayout.setVisibility(View.VISIBLE);
    	
    	mUpButton.setEnabled(false);
    	mUpButton.setBackgroundResource(R.drawable.mail_member_btn_updept_off_selector);
    	
        mListView.setAdapter(mTeamAdapter);
        mListView.setOnItemClickListener(mTeamClickListener);
    }

    public BaseAdapter mTeamAdapter = new BaseAdapter() {
        public View getView(int position, View convertView, ViewGroup parent) {
            final MinistryData data = (MinistryData)getItem(position);
            
            if(data.isMember) {
            	
            	convertView = getLayoutInflater().inflate(R.layout.mail_member_employeeteam, null);
            	 
//            	TextView tv = (TextView)convertView.findViewById(R.id.employee_name);
//            	tv.setText(data.m_szName);
//            	TextView dv = (TextView)convertView.findViewById(R.id.employee_data);
//            	dv.setText(data.m_szDepartment);
            	
            	TextView name = (TextView)convertView.findViewById(R.id.ROW_EMPLOYEE_NAME);
                name.setText(data.m_szName);
                 
                TextView team = (TextView)convertView.findViewById(R.id.ROW_EMPLOYEE_TEAM);
                team.setText(data.m_szDepartment);
                
                if(data.m_szDepartment.equals("")) {
                	team.setVisibility(View.GONE);
                } else {
                	team.setVisibility(View.VISIBLE);
                }
                 
                TextView role = (TextView)convertView.findViewById(R.id.ROW_EMPLOYEE_ROLE);
                role.setText(data.m_szRole);
                
                
                final ImageView iv = (ImageView)convertView.findViewById(R.id.employee_check);
                
                if(data.m_bIsChecked) {
	        		iv.setImageResource(R.drawable.mail_member_btn_check);
	        	} else {
	        		iv.setImageResource(R.drawable.mail_member_btn_uncheck);
	        	}
//                if(data.m_szCellPhoneNo == null || data.m_szCellPhoneNo.length() > 1){
//                	name.setTextColor(Color.LTGRAY);
//                }else{
//                	name.setTextColor(Color.BLACK);
//            	if(data.m_bIsChecked) {
//	        		iv.setImageResource(R.drawable.member_btn_check);
//	        	} else {
//	        		iv.setImageResource(R.drawable.member_btn_uncheck);
//	        	}
//                }
 
            	
            	/* 
            	convertView = getLayoutInflater().inflate(R.layout.member_employeeteam, null);
            	
            	convertView.findViewById(R.id.employee_line).setVisibility(View.VISIBLE);
                ImageView line = (ImageView)convertView.findViewById(R.id.employee_line);
                if(position == getCount() - 1) {
    	        	line.setImageResource(R.drawable.member_dot_tree_01);
    	        } else {
    	        	line.setImageResource(R.drawable.member_dot_tree_03);
    	        }
                TextView name = (TextView)convertView.findViewById(R.id.ROW_EMPLOYEE_NAME);
                name.setText(data.m_szName);
                TextView team = (TextView)convertView.findViewById(R.id.ROW_EMPLOYEE_TEAM);
                team.setText(data.m_szDepartment);
                TextView role = (TextView)convertView.findViewById(R.id.ROW_EMPLOYEE_ROLE);
                role.setText(data.m_szRole);
                ImageView sms = (ImageView)convertView.findViewById(R.id.ROW_EMPLOYEE_SMS);
                final ImageView phone =
                		(ImageView)convertView.findViewById(R.id.ROW_EMPLOYEE_PHONE);
            	if(data.m_bIsChecked) {
        			phone.setImageResource(R.drawable.member_btn_check);
        		} else {
        			phone.setImageResource(R.drawable.member_btn_uncheck);
        		}
        		phone.setVisibility(View.VISIBLE);
        		sms.setVisibility(View.GONE);
                phone.setOnClickListener(new OnClickListener() {;
    				public void onClick(View v) {
    					// TODO Auto-generated method stub
						if(data.m_bIsChecked) {
							data.m_bIsChecked = false;
							phone.setImageResource(R.drawable.member_btn_uncheck);
						} else {
							data.m_bIsChecked = true;
							phone.setImageResource(R.drawable.member_btn_check);
						}
    				}
    			});
    			*/
            	
            } else {
            	
            	convertView = getLayoutInflater().inflate(R.layout.mail_member_row_team, null);
                TextView name = (TextView)convertView.findViewById(R.id.ROW_TEAM_NAME);
                name.setText(data.m_szName);
                
                /*
                convertView = getLayoutInflater().inflate(R.layout.member_row_team, null);
            	ImageView line = (ImageView)convertView.findViewById(R.id.team_line);
                if(position == getCount() - 1) {
                	line.setImageResource(R.drawable.member_dot_tree_01);
                } else {
                	line.setImageResource(R.drawable.member_dot_tree_02);
                }
                TextView name = (TextView)convertView.findViewById(R.id.ROW_TEAM_NAME);
                name.setText(data.m_szName);
                */
            }
            
            return convertView;
        }

        public long getItemId(int position) {
            return position;
        }

        public Object getItem(int position) {
            return mCurTeamData.get(position);
        }

        public int getCount() {
            return mCurTeamData.size();
        }
    };
    
    public OnItemClickListener mTeamClickListener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			// TODO Auto-generated method stub
            MinistryData data = (MinistryData)mCurTeamData.get(arg2);
            
            if(data.isMember) {
				ImageView phone = (ImageView)arg1.findViewById(R.id.employee_check);
				if(data.m_bIsChecked) {
        			data.m_bIsChecked = false;
        			phone.setImageResource(R.drawable.mail_member_btn_uncheck);
        		} else {
        			data.m_bIsChecked = true;
        			phone.setImageResource(R.drawable.mail_member_btn_check);
        		}
				 
            } else {
	            requestData(PEOPLE_DEPT, data.m_szSerialNo);
            }
		}
    };
}