package com.ex.group.mail.addressbook.activity;

import android.app.Dialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
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

import com.ex.group.mail.addressbook.data.MemberSearchSQLite;
import com.ex.group.mail.addressbook.data.MinistryData;
import com.ex.group.mail.addressbook.data.TreeData;
import com.skt.pe.common.conf.Constants;
import com.skt.pe.common.data.SKTUtil;
import com.skt.pe.common.exception.SKTException;
import com.skt.pe.common.service.Controller;
import com.skt.pe.common.service.Parameters;
import com.skt.pe.common.service.XMLData;
import com.skt.pe.util.StringUtil;
import com.skt.pe.util.XMLUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.ex.group.folder.R;

public class SendGroupMessageActivity extends BaseActivity implements OnItemClickListener,
        OnClickListener {
	
	
    private ArrayList<MinistryData> mCurTeamData = new ArrayList<MinistryData>();
    private ArrayList<MinistryData> mLastTeamData = new ArrayList<MinistryData>();

    private LinearLayout mUpButton;
    private LinearLayout mLinearLayout;
    private TextView mUpButtonText;
    private TextView mSuffixDeptText;
    private ImageView tempView;
    
    TreeData temTreepData;
    String tempTitle = null;
    String tempCompany = null;
    String tempCompanyCd = null;
    String tempCompanyNm = null;
    String tempParentTitle = null;
    String tempDeptCode = null;
    String tempPretempDeptCode = "";
    String tempParentDeptCode = null;
    String tempParenttempDeptCode = "";
    String tempMemberCnt = "0";
    String treeTempDeptCode = "";
    String treeTempParentDeptCode = "";
    String tempReqCompanyCd = "";
    boolean m_bIsChild = false;
    boolean mIsTreeView = false;
    boolean mIsBackOff = false;			// 트리로 가서 트리 클릭 후 리스트로 왔을 때 back으로 트리로 돌아가도록
    boolean mIsLastView = false;
    boolean mIsMultiSelect = false;
    boolean mIsSingleSelect = false;
    boolean top = false;
    private ListView mListView;
    MemberSearchSQLite m_dbHelper = null;
    SQLiteDatabase m_dbMember = null;
    
    final int PEOPLE_DEPTMEMBER = 0;
    final int PEOPLE_DEPT = 1;
    final int PEOPLE_DEPT_ADD = 2;
    final int PEOPLE_MY_COMPANY = 3;
    final int PEOPLE_COMPANY = 4;
    final int PEOPLE_DEPT1 = 5;
    
    final int DIALOG_MEMBER_LIST = 1111;
    final int DIALOG_NO_APP = 1113;
    final int DIALOG_SEND_FAIL = 2222;
    
    final int LAST_VIEW = 0;
    final int SEND_SMS = 1;
    final int SEND_EMAIL = 2;
    final int CHANGE_VIEW_TYPE = 3;
    
    final int LIST_LAYOUT = 0;
    final int TREE_LAYOUT = 1;
    final int LAST_LAYOUT = 2;
    
    private String mApprovalType;
    private String mEmpType;
    private String myDeptCode = "";
    private String appType = "";
    private int sendType;
    private Map<String, String> dataName = new HashMap<String, String>();
    private Map<String, String> dataEmpId = new HashMap<String, String>();
    private Map<String, String> dataVvip = new HashMap<String, String>();
    private Map<String, String> dataNumber = new HashMap<String, String>();
    private Map<String, String> dataEmail = new HashMap<String, String>();
    
    private int mMemberCnt = 0;
    
	@Override
	protected int assignLayout() {
		return R.layout.mail_member_groupsms;
	}

    @Override
    public void onCreateX(Bundle savedInstanceState) {
    	super.onCreateX(savedInstanceState);        
        onPostCreateX();
    }
    
	protected void onPostCreateX() 
	{
		
		TextView tv = (TextView)findViewById(R.id.multiselecttitle_text);
		String mType = getIntent().getStringExtra("mType");
		if(mType.equals("sms")) {
			tv.setText(R.string.mail_send_group_sms);
		} else {
			tv.setText(R.string.mail_send_group_email);
		}
		
		mIsMultiSelect = getIntent().getBooleanExtra("isMultiSelect", false);
		mIsSingleSelect = getIntent().getBooleanExtra("isSingleSelect", false);
		mApprovalType = getIntent().getStringExtra("approvalType");
		mEmpType = getIntent().getStringExtra("empType");
		myDeptCode = getIntent().getStringExtra("deptCode");
		sendType = getIntent().getIntExtra("sendType", 2);
		appType = getIntent().getStringExtra("appType");
		mListView = (ListView)findViewById(R.id.list);
		
		mLinearLayout = (LinearLayout)findViewById(R.id.up_button_layout);
		mUpButton = (LinearLayout)findViewById(R.id.up_button);
		mUpButtonText = (TextView)findViewById(R.id.up_button_text);
		mSuffixDeptText = (TextView)findViewById(R.id.suffix_dept_text);
//		mLogoLayout = (LinearLayout)findViewById(R.id.company_layout);
		
		try {
			tempReqCompanyCd = SKTUtil.getCheckedCompanyCd(this);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		requestData(PEOPLE_DEPT, myDeptCode);
		
		final Button multiselecttitle_check = (Button)findViewById(R.id.multiselecttitle_check);
		
		multiselecttitle_check.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
        
            	allItemSelected(multiselecttitle_check);
            	mTeamAdapter.notifyDataSetChanged();
            }
        });
		
		Button mUpButton = (Button)findViewById(R.id.multiselect_confirm);
		mUpButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
            	
            	Iterator<String> it = dataEmpId.keySet().iterator();
				int mapSize = dataEmpId.size();
				String[] empId = new String[dataEmpId.size()];
				String[] empname = new String[dataEmpId.size()];
				String[] empNumber = new String[dataEmpId.size()];
				String[] empVvip = new String[dataEmpId.size()];
				String[] empEmail = new String[dataEmail.size()];
				int i = 0;
				while (it.hasNext()) {
					String key = it.next();
					empId[i] = dataEmpId.get(key);
					empname[i] = dataName.get(key);
					empNumber[i] = dataNumber.get(key);
					empVvip[i] = dataVvip.get(key);
					empEmail[i] = dataEmail.get(key);
					i++;
				}
            	
				if (sendType == 1) {
					String target = "";
					for(int j = 0; j < empNumber.length; j++) {
						target += empNumber[j] + ";";
						if(j >= 50) {
							break;
						}
					} 
					
					android.util.Log.i("target", "xxxxxxxxxx " + target);
					Uri uri = Uri.parse("sms:" + (String)target);
					Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
					intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
					startActivity(intent);
				} else {
					Intent intent ;
					try {
						if ("MEMBERSEARCH".equals(appType)) {
							intent = new Intent(ACTION_EMAIL_CLIENT);
							intent.putExtra("type", "side");
							intent.putExtra("names", empname);
							intent.putExtra("emails", empEmail);				            
							intent.putExtra("vvip", empVvip);				            
							startActivity(intent);	
						}else{
							
							intent = new Intent();
							intent.putExtra("type", "side");
							intent.putExtra("names", empname);
							intent.putExtra("emails", empEmail);				            
							intent.putExtra("vvip", empVvip);
							setResult(RESULT_OK, intent);
							finish();
						}
					} catch(Exception e) {
						m_szTitle = getString(R.string.mail_title_no_mail);
						m_szDialogMessage = getString(R.string.mail_no_emailclient);
						showDialog(DIALOG_NO_APP);
					}
					
				}
            }
            
        });
//		
		mUpButton = (Button)findViewById(R.id.multiselect_cancel);
		mUpButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
            	finish();
            }
        });
		
	}
	
	public void allItemSelected(Button bt){
		
		for (int i = 0; i < mCurTeamData.size(); i++) {
			MinistryData data = (MinistryData)mListView.getItemAtPosition(i);
			SKTUtil.log(Log.DEBUG, "XXX", "xxxxx aaaaa  bt.getText()" + bt.getText());
			SKTUtil.log(Log.DEBUG, "XXX", "xxxxx aaaaa  data.isMember  " + data.isMember);
			SKTUtil.log(Log.DEBUG, "XXX", "xxxxx aaaaa  data.m_szSerialNo  " + data.m_szSerialNo);
			SKTUtil.log(Log.DEBUG, "XXX", "xxxxx aaaaa  data.isMember" + data.isMember);
			SKTUtil.log(Log.DEBUG, "XXX", "xxxxx aaaaa  dataEmpId.containsKey(data.m_szSerialNo)" + dataEmpId.containsKey(data.m_szSerialNo));
			if (getResources().getString(R.string.mail_select_all).equals(bt.getText())) {
				if (data.isMember) {
					if (!dataEmpId.containsKey(data.m_szSerialNo)) {
						dataEmpId.put(data.m_szSerialNo, data.m_szSerialNo);
						dataName.put(data.m_szSerialNo, data.m_szName);
						dataVvip.put(data.m_szSerialNo, data.m_szVvip);
						dataNumber.put(data.m_szSerialNo, data.m_szCellPhoneNo);
						dataEmail.put(data.m_szSerialNo, data.m_szEmail);
					}
				}
				
			} else {
				if (dataEmpId.containsKey(data.m_szSerialNo)) {
					dataEmpId.remove(data.m_szSerialNo);
					dataName.remove(data.m_szSerialNo);
					dataVvip.remove(data.m_szSerialNo);
					dataNumber.remove(data.m_szSerialNo);
					dataEmail.remove(data.m_szSerialNo);
					
				}
				
			}
			
		}
		if (getResources().getString(R.string.mail_select_all).equals(bt.getText())) {
			bt.setText(getResources().getString(R.string.mail_unselect_all));
		}else{
			bt.setText(getResources().getString(R.string.mail_select_all));
		}
	}
	
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
    
	
	
	@Override
	protected void onActivityResultX(int requestCode, int resultCode, Intent data) {
		if (requestCode==1) {
			if (resultCode==2)
				finish();
		}
	}

	protected Dialog onCreateDialog(int a_nId) {
		Dialog aDialog = null;
    	
		switch (a_nId) {
		
			case DIALOG_MEMBER_LIST:
			case DIALOG_SEND_FAIL:
			case DIALOG_NO_APP :
				aDialog = createDialog(a_nId, m_szDialogMessage, DIALOG_ONE_BUTTON, getString(R.string.mail_ok_button), null, null);
	    		break;
				
		} // end switch (a_nId)
    	
    	return aDialog;
	}
    
    /* (non-Javadoc)
     * 팝업 버튼 클릭 이벤트 핸들러
     * @see com.sk.pe.group.activity.BaseActivity#onClickDialog(int, java.lang.String, int)
     */
    public void onClickDialog(int a_nDialogId, String a_szButtonText, int a_nClickedButton) {
		switch (a_nDialogId) {
		
			case DIALOG_MEMBER_LIST:
//				finish();
	    		break;
			case DIALOG_NO_APP:
				Intent intent = new Intent(Constants.Action.STORE_DETAIL_VIEW);
				intent.putExtra("APP_ID", "MOGP000001");
				startActivity(intent);
				break;
		} // end switch (a_nDialogId)
	} // end public void onClickDialog(int a_nDialogId, String a_szButtonText, int a_nClickedButton)
    
//    @Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (keyCode==KeyEvent.KEYCODE_BACK) {
//			if (!tempParentDeptCode.equals("")) {			
//				if (m_bIsChild) {
//					requestData(PEOPLE_DEPT, tempParentDeptCode);
//		        	tempParenttempDeptCode = tempDeptCode;
//				}else {
//					if (tempPretempDeptCode.equals(tempDeptCode))
//		        		requestData(PEOPLE_DEPT, tempDeptCode);
//		        	else
//		        		requestData(PEOPLE_DEPT, tempParentDeptCode);
//		        	tempPretempDeptCode = "";
//		        	tempParenttempDeptCode = tempDeptCode;
//				}
//			}
//		}
//		return super.onKeyDown(keyCode, event);
//	}
    
    
    private String setString(String value) {
    	if(StringUtil.isNull(value)) {
    		return "";
    	} else {
    		return value;
    	}
    }

	/* (non-Javadoc)
	 * 액션 처리 후 UI 세팅<br>
	 * - 부서 목록 요청<br>
	 * - 부서 목록 및 멤버 요청<br>
	 * - 내가 소록 회사 코드 요청
	 * @see com.sk.pe.group.activity.BaseActivity#onPostThread(com.skt.pe.common.service.XMLData, com.skt.pe.common.exception.SKTException)
	 */
	public void onPostThread(XMLData a_XMLData, SKTException a_exception) {
    	// 정상 응답이  발생하였으면 오류 메시지 보여주고 리턴]
		if(a_exception != null) {
			m_szTitle = getString(R.string.mail_text_error);
    		m_szDialogMessage = a_exception.getMessage();
			showDialog(DIALOG_MEMBER_LIST);
			
			if(m_nRequestType == PEOPLE_DEPT_ADD) {
				if(temTreepData.m_bIsExpanded) {
					temTreepData.m_bIsExpanded = false;
					tempView.setImageResource(R.drawable.mail_member_arrow_diagram_down);
				} else {
					temTreepData.m_bIsExpanded = true;
					tempView.setImageResource(R.drawable.mail_member_arrow_diagram_right);
				}
			}
			return;
		} // end else if (m_szResponseCode != null && m_szResponseCode.equals("1000") == false)
		try {
			switch (m_nRequestType) 
			{
			case PEOPLE_DEPTMEMBER :				
				m_szResponseCode = a_XMLData.get("result");
				m_szResponseMessage = a_XMLData.get("resultMessage");
				
				tempParentDeptCode = treeTempParentDeptCode = a_XMLData.get("parentDeptCode");
												
				tempDeptCode = a_XMLData.get("deptCode");
				tempTitle = a_XMLData.get("suffixDept");
				tempCompany = a_XMLData.get("companyNm");
				tempCompanyCd = a_XMLData.get("companyCd");
				a_XMLData.setList("deptMember");
			
				//영문명 추가의 필요.. !!!!
				mCurTeamData.clear();
				for (int i=0; i<a_XMLData.size(); i++) {
					addCurTeamData(
							a_XMLData.get(i, "id"),
							a_XMLData.get(i, "name"),
							a_XMLData.get(i, "telNum"),
							a_XMLData.get(i, "role"),
							tempDeptCode,
							a_XMLData.get(i, "email"),
							a_XMLData.get(i, "empId"),
							tempTitle,
							tempCompany,
							setString(a_XMLData.get(i, "engName")),
							a_XMLData.get(i, "vvip"),
							a_XMLData.get(i, "teamManager"),
							a_XMLData.get(i, "companyCd"));
				}
				
				showTeam(tempTitle, tempCompanyCd);
				setTeamName(a_XMLData.get("parentDeptName"), tempTitle);
				
				break;
				
			case PEOPLE_DEPT:
				m_szResponseCode = a_XMLData.get("result");
				m_szResponseMessage = a_XMLData.get("resultMessage");
				tempCompanyCd = a_XMLData.get("companyCd");
				tempCompanyNm = a_XMLData.get("companyNm");
				if(!mIsTreeView) {
					tempParentDeptCode = a_XMLData.get("parentDeptCode");
				} else {
					treeTempParentDeptCode = a_XMLData.get("parentDeptCode");
				}
				//tempDeptCode = a_XMLData.get("deptCode");
				treeTempDeptCode = "";
				if(mIsTreeView) {
					treeTempDeptCode =
							XMLUtil.getTextContent(a_XMLData.getNode("/mo/dept/deptCode"));
				} else {
					tempDeptCode = XMLUtil.getTextContent(a_XMLData.getNode("/mo/dept/deptCode"));
				}
				tempTitle = XMLUtil.getTextContent(a_XMLData.getNode("/mo/dept/suffixDept"));
				
				if (!StringUtil.isNull(a_XMLData.get("parentDeptNm"))) {
					tempParentTitle = a_XMLData.get("parentDeptNm");
				}else if(!StringUtil.isNull(a_XMLData.get("tempCompanyNm"))) {
					tempParentTitle = tempCompanyNm;
				}else{
					tempParentTitle = getString(R.string.mail_groupcompany);
				}
				
				tempMemberCnt = a_XMLData.get("memberCnt");
				if (tempMemberCnt==null)
					tempMemberCnt = "0";

				if(mIsTreeView) {
					
				} else {
					mCurTeamData.clear();
					mMemberCnt = 0;
					
					a_XMLData.setList("deptMember");
					for (int i=0; i<a_XMLData.size(); i++) {
						mMemberCnt++;
						String tempInfoCompanyCd = "";
						if (!StringUtil.isNull(a_XMLData.get(i,"companyCd"))) {
							tempInfoCompanyCd = a_XMLData.get(i,"companyCd");
						}else{
							tempInfoCompanyCd = tempCompanyCd; 
						}
						addCurTeamData(a_XMLData.get(i, "id"), a_XMLData.get(i, "name"),
								a_XMLData.get(i, "telNum"), a_XMLData.get(i, "role"),
								tempTitle, tempCompany, "", "", a_XMLData.get(i, "email"),
								a_XMLData.get(i, "empId"), true, tempTitle,
								setString(a_XMLData.get(i, "engName")), a_XMLData.get(i, "vvip"),
								a_XMLData.get(i, "teamManager"), tempInfoCompanyCd);
					}
					
					a_XMLData.setList("childDept");
					for (int i=0; i<a_XMLData.size(); i++) {
						String tempInfoCompanyCd = "";
						if (!StringUtil.isNull(a_XMLData.get(i,"companyCd"))) {
							tempInfoCompanyCd = a_XMLData.get(i,"companyCd");
						}else{
							tempInfoCompanyCd = tempCompanyCd; 
						}
						addCurTeamData(a_XMLData.get(i, "deptCode"), a_XMLData.get(i, "suffixDept"),
								tempCompany, "", a_XMLData.get(i, "haveChild"), "", "", "", "", "", false,
								tempTitle, setString(a_XMLData.get(i, "engName")), 
								a_XMLData.get(i, "vvip"), a_XMLData.get(i, "teamManager"), tempInfoCompanyCd);
					}
					if(StringUtil.isNull(treeTempDeptCode))
					{	
						setTeamName(tempParentTitle, tempTitle);
	    				showDepartment(tempParentTitle, tempCompanyCd);	    				
					} 
					else 
					{
						setTeamName(null, tempTitle);
						
//						Bitmap img = null;
//						
//						try {
//							String szLogo = a_XMLData.get("companyLogo");
//							if(szLogo.startsWith("http")) {
//								img = ResourceUtil.getBitmapByUrl(szLogo);
//							} else {
//								img = ResourceUtil.decodeBitmap(szLogo);
//							}
//							ImageView logo = (ImageView)findViewById(R.id.LOGO_COMPANY);
//							logo.setImageBitmap(img);
//						} catch(Exception e) {
//							e.printStackTrace();
//						}
	    				showOrgAll();
					}
				}
				break;
	    	case PEOPLE_MY_COMPANY:
	    		requestData(PEOPLE_DEPT, myDeptCode);
				
				break;
	    	case PEOPLE_COMPANY:
//	    		m_szResponseCode = a_XMLData.get("result");
//				m_szResponseMessage = a_XMLData.get("resultMessage");
//				
//				treeTempParentDeptCode = a_XMLData.get("parentDeptCode");
//				//tempDeptCode = a_XMLData.get("deptCode");

	    		
	    		
	    		
	    		
	    		break;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * 액션 처리 핸들러<br>
	 * - 부서 목록 요청<br>
	 * - 부서 목록 및 멤버 요청<br>
	 * - 내가 소록 회사 코드 요청
	 * @see com.sk.pe.group.activity.BaseActivity#onProcessThread()
	 */
	public XMLData onProcessThread() throws SKTException {
		Parameters params = null;
	    Controller controller = null;
	    XMLData nXMLData = null;
		String req = (String)m_requestObject;
	    
		switch (m_nRequestType) {
		case PEOPLE_DEPTMEMBER:
//			if(req != null && !req.equals("")) {
//				if(req.indexOf(".") < 0) {
//					comCd = req;
//				} else {
//					comCd = req.substring(0, req.indexOf("."));
//				}
//			}
			
			params = new Parameters("COMMON_PEOPLE_DEPTMEMBER");
        	params.put("infoCompanyCd", tempReqCompanyCd);
			params.put("deptCode", req);
			if(mApprovalType != null && !mApprovalType.equals("")) {
				params.put("approvalType", mApprovalType);
			}
			if(mEmpType != null && !mEmpType.equals("")) {
				params.put("empType", mEmpType);
			}
			break;
		case PEOPLE_DEPT:
			
//			Log.e("xx",req);
//			if(req != null && !req.equals("")) {
//				comCd = req.substring(0, req.indexOf("."));
//			}			
        	params = new Parameters("COMMON_PEOPLE_DEPT");
//        	if("all".equals((String)m_requestObject)){
//        		params.put("infoCompanyCd", "all");
//        		params.put("deptCode", "");
//        	}else{
        		params.put("infoCompanyCd", tempReqCompanyCd);
        		params.put("deptCode", (String)m_requestObject);
//        	}
			if(mApprovalType != null && !mApprovalType.equals("")) {
				params.put("approvalType", mApprovalType);
			}
			if(mEmpType != null && !mEmpType.equals("")) {
				params.put("empType", mEmpType);
			}
			break;
		case PEOPLE_DEPT_ADD:
//			if(req != null && !req.equals("")) {
//				comCd = req.substring(0, req.indexOf("."));
//			}
        	params = new Parameters("COMMON_PEOPLE_DEPT");
        	params.put("infoCompanyCd", tempReqCompanyCd);
			params.put("deptCode", (String)m_requestObject);
			if(mApprovalType != null && !mApprovalType.equals("")) {
				params.put("approvalType", mApprovalType);
			}
			if(mEmpType != null && !mEmpType.equals("")) {
				params.put("empType", mEmpType);
			}
			break;
		case PEOPLE_MY_COMPANY:
			params = new Parameters("COMMON_PEOPLE_MEMBERCONTENT");
			params.put("type", "M");
			break;
			
		case PEOPLE_COMPANY:
			params = new Parameters("COMMON_PEOPLE_COMPANY");
			params.put("infoCompanyCd", "");
			break;
		
		}

		controller = new Controller(this);
		nXMLData = controller.request(params);
		
		return nXMLData;
	}
	
	protected boolean checkedItem() {
		for (int i=0; i<mCurTeamData.size(); i++) {
			if (mCurTeamData.get(i).m_szDepartment.equals("") && mCurTeamData.get(i).m_szLocation.equals(""))
				return false;
		}
		return true;
	}
	
	private void addCurTeamData(String a_ParamA, String a_ParamB, String a_ParamC, String a_ParamD,
                                String a_ParamE, String a_ParamF, String a_ParamG, String teamName, String companyNm,
                                String engName, String vvip, String teamManager, String companyCd) {
		addCurTeamData(a_ParamA, a_ParamB, a_ParamC, a_ParamD, a_ParamE, "", "", "", a_ParamF,
				a_ParamG, false, teamName, engName, vvip, teamManager, companyCd);
	}

	private void addCurTeamData(String a_ParamA, String a_ParamB, String a_ParamC, String a_ParamD,
                                String a_ParamE, String a_ParamF, String a_ParamG, String a_ParamH, String a_ParamI,
                                String a_ParamJ, boolean isMember, String teamName, String engName, String vvip, String teamManager, String companyCd) {
		MinistryData data = new MinistryData();
		data.m_szSerialNo = checkTrim(a_ParamA);
		data.m_szName = checkTrim(a_ParamB);
		data.m_szCellPhoneNo = checkPhone(a_ParamC);
		data.m_szRole = checkTrim(a_ParamD);
		data.m_szDepartment = checkTrim(a_ParamE);
		data.m_szCompany = checkTrim(a_ParamF);
		data.m_szType = checkTrim(a_ParamG);
		data.m_szCompanyCd = checkTrim(companyCd);
		data.m_szEmail = checkTrim(a_ParamI);
		data.m_szEmpId = checkTrim(a_ParamJ);
		data.m_nCountNum = mCurTeamData.size();
		data.m_bIsChecked = false;
		data.isMember = isMember;
		data.m_szTeam = teamName;
		data.m_szEngName = engName;
		data.m_szVvip = vvip;
		data.m_szTeamManager = teamManager;
		
		
		if(mIsLastView) {
			mLastTeamData.add(data);
		} else {
			mCurTeamData.add(data);
		}
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

	private void setTeamName(String parentTeamName, String currentTeamName)
	{
//		if(parentTeamName != null)
//			mSuffixDeptText.setText(parentTeamName);
//		
		SKTUtil.log(Log.DEBUG, "xxxx", "xxxx currentTeamName :;  " + currentTeamName);
//		if(currentTeamName != null)
			mSuffixDeptText.setText(currentTeamName);		
	}
	
	/**
	 * 팀명 보이기
	 * @param a_szTeamName 팀명
	 */
	private void showTeam(String a_szTeamName, String CompanyCd)
	{		
		mLinearLayout.setVisibility(View.VISIBLE);
//    	mLogoLayout.setVisibility(View.GONE);
//    	if (!StringUtil.isNull(a_szTeamName)) {
//    		mSuffixDeptText.setText(a_szTeamName);	
//		}else{
//			mSuffixDeptText.setText(getString(R.string.groupcompany));
//		}
    	tempReqCompanyCd = CompanyCd;
    	//    	findViewById(R.id.up_line).setVisibility(View.GONE);
    		mUpButton.setOnClickListener(new OnClickListener() {
    			public void onClick(View view)
    			{
    				if (tempPretempDeptCode.equals(tempDeptCode)){
    					SKTUtil.log(Log.DEBUG, "xxxx", "xxxxxx  tempDeptCode  :: if  " + tempDeptCode);
    					requestData(PEOPLE_DEPT, tempDeptCode);
    				}else{
    					SKTUtil.log(Log.DEBUG, "xxxx", "xxxxxx  tempDeptCode  ::  else  " + tempDeptCode);
    					requestData(PEOPLE_DEPT, tempParentDeptCode);
    				}
    				tempPretempDeptCode = "";
    				tempParenttempDeptCode = tempDeptCode;
    				Button bt = (Button)findViewById(R.id.multiselecttitle_check);
    				bt.setText(getResources().getString(R.string.mail_select_all));
    			}
    		});
        m_bIsChild = false;
        mListView.setAdapter(mEmaployeeAdapter);
        mListView.setOnItemClickListener(mMemeberClickListener);
    }

    /**
     * 부서명 보이기
     * @param a_szTeamName 부서명
     */
    private void showDepartment(String a_szTeamName, String CompanyCd) {
    	mLinearLayout.setVisibility(View.VISIBLE);
//    	mLogoLayout.setVisibility(View.GONE);
//    	mSuffixDeptText.setText(a_szTeamName);
    	tempReqCompanyCd = CompanyCd;
//    	findViewById(R.id.up_line).setVisibility(View.VISIBLE);
    	
    	if(StringUtil.isNull(tempParentDeptCode)){
    		mUpButton.setOnClickListener(null);
    	} else{
    		mUpButton.setOnClickListener(new OnClickListener()
    		{
    			public void onClick(View view)
    			{
    				requestData(PEOPLE_DEPT, tempParentDeptCode);
    				tempParenttempDeptCode = tempDeptCode;
    				Button bt = (Button)findViewById(R.id.multiselecttitle_check);
    				bt.setText(getResources().getString(R.string.mail_select_all));
    			}
    		});
    	}
        m_bIsChild = true;
        mListView.setAdapter(mTeamAdapter);
        mListView.setOnItemClickListener(mTeamClickListener);
    }

    /**
     * 회사명 보이기
     */
    private void showOrgAll() {
    	mLinearLayout.setVisibility(View.GONE);
//    	mLogoLayout.setVisibility(View.VISIBLE);
        m_bIsChild = true;
        mListView.setAdapter(mTeamAdapter);
        mListView.setOnItemClickListener(mTeamClickListener);
    }

    

//    public OnItemClickListener mDepartmentClickListener = new OnItemClickListener()
//    {
//		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//				long arg3) {
//			// TODO Auto-generated method stub
//        	MinistryData department = (MinistryData)mCurTeamData.get(arg2);
//
//    		if(department.m_szDepartment.equals("N")) {
//    			requestData(PEOPLE_DEPTMEMBER, department.m_szSerialNo);
//    		} else {
//    			requestData(PEOPLE_DEPT, department.m_szSerialNo);
//    		}
//		}
//    };

    public OnItemClickListener mTeamClickListener = new OnItemClickListener()
    {
    	
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                long arg3) {
            MinistryData data = (MinistryData)mCurTeamData.get(arg2);
            SKTUtil.log(Log.DEBUG, "xxxxxx", "xxxxxx   653 " );
            if(data.isMember) {
            	SKTUtil.log(Log.DEBUG, "xxxxxx", "xxxxxxxx  m_szEmpId()  ::" + data.m_szEmpId );
            	SKTUtil.log(Log.DEBUG, "xxxxxx", "xxxxxxxx  dataEmpId.size()  ::" +  dataEmpId.size() );
            	if (dataEmpId.containsKey(data.m_szSerialNo)) {
            		dataEmpId.remove(data.m_szSerialNo);
            		dataName.remove(data.m_szSerialNo);
            		dataVvip.remove(data.m_szSerialNo);
            		dataNumber.remove(data.m_szSerialNo);
            		dataEmail.remove(data.m_szSerialNo);
				} else {
					dataEmpId.put(data.m_szSerialNo, data.m_szEmpId);
            		dataName.put(data.m_szSerialNo, data.m_szName);
            		dataVvip.put(data.m_szSerialNo, data.m_szVvip);
            		dataNumber.put(data.m_szSerialNo, data.m_szCellPhoneNo);
            		dataEmail.put(data.m_szSerialNo, data.m_szEmail);
				}
            	
            	mTeamAdapter.notifyDataSetChanged();
            } else {
            	
	            if(data.m_szDepartment.equals("N")) {
	            	requestData(PEOPLE_DEPT, data.m_szSerialNo);
	            } else {
	            	tempReqCompanyCd = data.m_szCompanyCd;
	            	requestData(PEOPLE_DEPT, data.m_szSerialNo);
	            }
            }
		}
    };
    
    public OnItemClickListener mMemeberClickListener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                long arg3) {
			// TODO Auto-generated method stub
			MinistryData data = (MinistryData)mCurTeamData.get(arg2);
			SKTUtil.log(Log.DEBUG, "xxxxxx", "xxxxxx   695 " );
			//TODO 멤버  수정..
			SKTUtil.log(Log.DEBUG, "xxxxxx", "xxxxxxxx  dataEmpId.size()  ::" +  dataEmpId.size() );
        	if (dataEmpId.containsKey(data.m_szSerialNo)) {
        		dataEmpId.remove(data.m_szSerialNo);
        		dataName.remove(data.m_szSerialNo);
        		dataVvip.remove(data.m_szSerialNo);
        		dataNumber.remove(data.m_szSerialNo);
        		dataEmail.remove(data.m_szSerialNo);
			} else {
				dataEmpId.put(data.m_szSerialNo, data.m_szEmpId);
        		dataName.put(data.m_szSerialNo, data.m_szName);
        		dataVvip.put(data.m_szSerialNo, data.m_szVvip);
        		dataNumber.put(data.m_szSerialNo, data.m_szCellPhoneNo);
        		dataEmail.put(data.m_szSerialNo, data.m_szEmail);
			}
			
			mEmaployeeAdapter.notifyDataSetChanged();
		}
    };
	
	/**
	 * 선택한 멤버 또는 부서 리턴
	 * @return 멤버 또는 부서 목록
	 */
	private ArrayList<MinistryData> getSelectedNames() {
		ArrayList<MinistryData> data = new ArrayList<MinistryData>();
		for(MinistryData min: (mIsLastView ? mLastTeamData : mCurTeamData)) {
			if(min.m_bIsChecked) {
				data.add(min);
			}
		}
		
		return data;
	}

	/* (non-Javadoc)
	 * 클릭 이벤트 핸들러<br>
	 * - 선택 버튼<br>
	 * - 취소 버튼
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId() == R.id.multiselect_confirm) {
			ArrayList<MinistryData> data = getSelectedNames();
			String[] names = new String[data.size()];
			String[] depts = new String[data.size()];
			String[] emails = new String[data.size()];
			
			for(int i = 0; i < data.size(); i++) {
				MinistryData employee = data.get(i);
				names[i] = employee.m_szName;
				depts[i] = employee.m_szDepartment;
				emails[i] = employee.m_szEmail;
			}
			
			Intent intent = new Intent();
			intent.putExtra("names", names);
			intent.putExtra("depts", depts);
			intent.putExtra("emails", emails);
			AddressTabActivity parent = (AddressTabActivity)getParent();
			parent.setResult(RESULT_OK, intent);
			finish();
		} else if(v.getId() == R.id.multiselect_cancel) {
			AddressTabActivity parent = (AddressTabActivity)getParent();
			parent.setResult(RESULT_CANCELED);
			finish();
		}
	}

    public BaseAdapter mEmaployeeAdapter = new BaseAdapter()
    {
    	public View getView(int position, View convertView, ViewGroup parent)
        {
            final MinistryData data = (MinistryData)getItem(position);
            
            if(data.isMember) 
            {
            	convertView = getLayoutInflater().inflate(R.layout.mail_member_row_employee_2, null);
            	
            	ImageView line = (ImageView)convertView.findViewById(R.id.employee_circle);
            	if ((getCount()-1) == position ) {
            		line.setBackgroundDrawable(getResources().getDrawable(R.drawable.mail_member_line_02));
//            		line.setBackgroundDrawable(getResources().getDrawable(R.drawable.member_line_02));
            		/// 이미지 받으면 수정 
            	} else {
					line.setBackgroundDrawable(getResources().getDrawable(R.drawable.mail_member_line_02));
				}
            	
            	ImageView line1 = (ImageView)convertView.findViewById(R.id.employee_line);
            	line1.setVisibility(View.VISIBLE);
            	if ((mMemberCnt -1 ) == position ) {
            		line1.setBackgroundDrawable(getResources().getDrawable(R.drawable.mail_member_line_04));
				} else {
					line1.setBackgroundDrawable(getResources().getDrawable(R.drawable.mail_member_line_03));
				}
             	
                
            	final TextView check = (TextView)convertView.findViewById(R.id.ROW_EMPLOYEE_CHECK);
             	if (dataEmpId.containsKey(data.m_szSerialNo)) {
             		check.setBackgroundDrawable(getResources().getDrawable(R.drawable.mail_member_check_on));
     			}else{
     				check.setBackgroundDrawable(getResources().getDrawable(R.drawable.mail_member_check_off));
     			}
            	
                TextView name = (TextView)convertView.findViewById(R.id.ROW_EMPLOYEE_NAME);
                name.setText(data.m_szName);
                if (StringUtil.isNull(data.m_szCellPhoneNo)) {
                	name.setTextColor(Color.LTGRAY);
                	check.setBackgroundDrawable(getResources().getDrawable(R.drawable.mail_member_check_off));
                }
                
                TextView team = (TextView)convertView.findViewById(R.id.ROW_EMPLOYEE_CORP);
                team.setText(data.m_szTeam);
                TextView role = (TextView)convertView.findViewById(R.id.ROW_EMPLOYEE_DEPT);
                role.setText(data.m_szRole);
                SKTUtil.log(Log.DEBUG, "xxxxx", "xxxxxxxxx  1331 " +data.m_szTeam  );
                if("Y".equals(data.m_szTeamManager)) {
                	name.setTextColor(Color.rgb(0x1C, 0x8C, 0xC8));
                } else {
                	name.setTextColor(Color.BLACK);
                }
                
                if("Y".equals(data.m_szVvip)) {
                	team.setVisibility(View.INVISIBLE);
                } else {
                	team.setVisibility(View.VISIBLE);
                }
                
            } 
            else 
            {
                convertView = getLayoutInflater().inflate(R.layout.mail_member_row_team, null);
                convertView.findViewById(R.id.team_line).setVisibility(View.VISIBLE);
            	ImageView line = (ImageView)convertView.findViewById(R.id.team_line);
            	if ((getCount()-1) == position ) {
            		line.setBackgroundDrawable(getResources().getDrawable(R.drawable.mail_member_line_05));
				} else {
					line.setBackgroundDrawable(getResources().getDrawable(R.drawable.mail_member_line_01));
				}
                TextView name = (TextView)convertView.findViewById(R.id.ROW_TEAM_NAME);
                name.setText(data.m_szName);
            }
            
            return convertView;
        }

        public long getItemId(int position)
        {
            return position;
        }

        public Object getItem(int position)
        {
            return mCurTeamData.get(position);
        }

        public int getCount()
        {
            return mCurTeamData.size();
        }
    };
    
    public OnItemClickListener mLastClickListener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                long arg3) {
			// TODO Auto-generated method stub
			MinistryData data = (MinistryData)mLastTeamData.get(arg2);
			
			if(mIsMultiSelect) {
				ImageView phone = (ImageView)arg1.findViewById(R.id.ROW_EMPLOYEE_PHONE);
        		if(data.m_bIsChecked) {
        			data.m_bIsChecked = false;
        			phone.setImageResource(R.drawable.mail_member_check_off);
        		} else {
        			data.m_bIsChecked = true;
        			phone.setImageResource(R.drawable.mail_member_check_on);
        		}
			} else {
	        	Intent intent = new Intent(SendGroupMessageActivity.this, AddressInfoActivity.class);
	        	intent.putExtra("type", data.m_szType);
		    	intent.putExtra("isLast", true);
		    	intent.putExtra("companyCode", data.m_szCompanyCd);
		    	intent.putExtra("deptCode", data.m_szDepartment);
	        	intent.putExtra("employee", data.m_szSerialNo);
	        	startActivityForResult(intent, 1);
			}
		}
    };

    public BaseAdapter mTeamAdapter = new BaseAdapter() {
    	public View getView(int position, View convertView, ViewGroup parent)
        {
            final MinistryData data = (MinistryData)getItem(position);
            
            if(data.isMember) 
            {
            	convertView = getLayoutInflater().inflate(R.layout.mail_member_row_employee_2, null);
            	
            	ImageView line = (ImageView)convertView.findViewById(R.id.employee_circle);
            	if ((getCount()-1) == position ) {
            		line.setBackgroundDrawable(getResources().getDrawable(R.drawable.mail_member_line_02));
//            		line.setBackgroundDrawable(getResources().getDrawable(R.drawable.member_line_02));
            		/// 이미지 받으면 수정 
            	} else {
					line.setBackgroundDrawable(getResources().getDrawable(R.drawable.mail_member_line_02));
				}
            	
            	ImageView line1 = (ImageView)convertView.findViewById(R.id.employee_line);
            	line1.setVisibility(View.VISIBLE);
            	if ((mMemberCnt -1 ) == position ) {
            		line1.setBackgroundDrawable(getResources().getDrawable(R.drawable.mail_member_line_04));
				} else {
					line1.setBackgroundDrawable(getResources().getDrawable(R.drawable.mail_member_line_03));
				}
             	
                
            	final TextView check = (TextView)convertView.findViewById(R.id.ROW_EMPLOYEE_CHECK);
             	if (dataEmpId.containsKey(data.m_szSerialNo)) {
             		check.setBackgroundDrawable(getResources().getDrawable(R.drawable.mail_member_check_on));
     			}else{
     				check.setBackgroundDrawable(getResources().getDrawable(R.drawable.mail_member_check_off));
     			}
            	
                TextView name = (TextView)convertView.findViewById(R.id.ROW_EMPLOYEE_NAME);
                name.setText(data.m_szName);
                if (StringUtil.isNull(data.m_szCellPhoneNo)) {
                	name.setTextColor(Color.LTGRAY);
                	check.setBackgroundDrawable(getResources().getDrawable(R.drawable.mail_member_check_off));
                }
                
                TextView team = (TextView)convertView.findViewById(R.id.ROW_EMPLOYEE_CORP);
                team.setText(data.m_szTeam);
                TextView role = (TextView)convertView.findViewById(R.id.ROW_EMPLOYEE_DEPT);
                role.setText(data.m_szRole);
                SKTUtil.log(Log.DEBUG, "xxxxx", "xxxxxxxxx  1331 " +data.m_szTeam  );
                if("Y".equals(data.m_szTeamManager)) {
                	name.setTextColor(Color.rgb(0x1C, 0x8C, 0xC8));
                } else {
                	name.setTextColor(Color.BLACK);
                }
                
                if("Y".equals(data.m_szVvip)) {
                	team.setVisibility(View.INVISIBLE);
                } else {
                	team.setVisibility(View.VISIBLE);
                }
                
            } 
            else 
            {
                convertView = getLayoutInflater().inflate(R.layout.mail_member_row_team, null);
                convertView.findViewById(R.id.team_line).setVisibility(View.VISIBLE);
            	ImageView line = (ImageView)convertView.findViewById(R.id.team_line);
            	if ((getCount()-1) == position ) {
            		line.setBackgroundDrawable(getResources().getDrawable(R.drawable.mail_member_line_05));
				} else {
					line.setBackgroundDrawable(getResources().getDrawable(R.drawable.mail_member_line_01));
				}
                TextView name = (TextView)convertView.findViewById(R.id.ROW_TEAM_NAME);
                name.setText(data.m_szName);
            }
            
            return convertView;
        }

        public long getItemId(int position)
        {
            return position;
        }

        public Object getItem(int position)
        {
            return mCurTeamData.get(position);
        }

        public int getCount()
        {
            return mCurTeamData.size();
        }
    };
    

	/* (non-Javadoc)
	 * 리스트의 아이템 클릭 이벤트 핸들러<br>
	 * - 트리 클릭
	 * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
	 */
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
//		mIsTreeView = false;
//		findViewById(R.id.list_layout).setVisibility(View.VISIBLE);
//		mTreeView.setVisibility(View.GONE);
//		TreeData data = mTreeAdapter.getItem(arg2);
//		tempReqCompanyCd = data.m_szCompanyCd;
		//		if(data.m_bHasChild) {
//			requestData(PEOPLE_DEPT, data.m_szDept);
//		} else {
//			try {
//				requestData(PEOPLE_DEPTMEMBER, SKTUtil.getCheckedCompanyCd(this));
//			} catch(SKTException e) {
//				e.printStackTrace();
//			}
//		}
	}
    
    /* (non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if(mIsBackOff && !mIsTreeView) {
			mIsTreeView = true;
		} else {
			super.onBackPressed();
//			showCloseDlg();
		}
	}
	
//	private void showNoSearch(boolean isShow) {
//		View tempView;
//		
//		if(isShow) {
//			tempView = findViewById(R.id.last_view);
//			tempView.setVisibility(View.INVISIBLE);
//			tempView = findViewById(R.id.notice_text);
//			tempView.setVisibility(View.VISIBLE);
//		} else {
//			tempView = findViewById(R.id.last_view);
//			tempView.setVisibility(View.VISIBLE);
//			tempView = findViewById(R.id.notice_text);
//			tempView.setVisibility(View.INVISIBLE);
//		}
//	}
	
//	private void getLastSearch() {
//		mIsLastView = true;
//		mIsTreeView = false;
//		mLastTeamData.clear();
//		m_dbHelper = new MemberSearchSQLite(this);
//		m_dbMember = m_dbHelper.getWritableDatabase();
//		Cursor cs = null;
//
//		try {
//			cs = m_dbMember.query(MemberSearchSQLite.DATABASE_TABLE,
//					new String[] { "serial", "deptname", "name", "telnum", "role", "work",
//							"company", "phone", "email", "type", "companycd", "searchdate" }, null,
//					null, null, null, "searchdate desc");
//			
//			if (cs == null || cs.getCount() == 0) {
//				showNoSearch(true);
//			} else {
//				showNoSearch(false);
//				
//				addCurTeamData("", getString(R.string.recent_search), "", "", "", "");
//				
//				cs.moveToFirst();
//				do {
//					addCurTeamData(cs.getString(0), cs.getString(2), cs.getString(3),
//							cs.getString(4), cs.getString(1), cs.getString(6), cs.getString(9),
//							cs.getString(10), cs.getString(8), false);
//				} while(cs.moveToNext());
//			}
//		} catch(SQLiteException e) {
//			android.util.Log.e("MemberSearch", e.getMessage());
//		} finally {
//			if(cs != null) {
//				cs.close();
//			}
//		}
//		
//		m_dbMember.close();
//		
//		showLayout(LAST_LAYOUT);
//		mLastView.setAdapter(mLastViewAdapter);
//		mLastView.setOnItemClickListener(mLastClickListener);
//	}

}