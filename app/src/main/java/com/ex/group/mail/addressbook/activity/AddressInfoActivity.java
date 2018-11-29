package com.ex.group.mail.addressbook.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Note;
import android.provider.ContactsContract.CommonDataKinds.Organization;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ex.group.mail.addressbook.data.EmployeeAddJobData;
import com.ex.group.mail.addressbook.data.EmployeeData;
import com.ex.group.mail.addressbook.data.MemberSearchSQLite;
import com.skt.pe.common.conf.Constants;
import com.skt.pe.common.data.SKTUtil;
import com.skt.pe.common.exception.SKTException;
import com.skt.pe.common.service.Controller;
import com.skt.pe.common.service.Parameters;
import com.skt.pe.common.service.XMLData;
import com.skt.pe.crypto.seed.SeedCrypter;
import com.skt.pe.util.ResourceUtil;
import com.skt.pe.util.StringUtil;

import java.io.ByteArrayOutputStream;

import com.ex.group.folder.R;

/**
 * 멤버 상세보기 화면
 * @author jokim
 *
 */
public class AddressInfoActivity extends BaseActivity
{
	private final String TAG = "AddressInfoActivity";
    EmployeeData m_CurEmployeeData;
    
    MemberSearchSQLite m_dbHelper = null;
    SQLiteDatabase m_dbMember = null;
    
    String mType;
    String mCompanyCode;
    String mDeptCode;
    String changeKey;
	String empId;
    
    long mId;
    static long rawContactId = 0;
    boolean mIsMyProfile = false;
    
    final int SAVE_PHONE_ADDR = 0;
    
    final int PEOPLE_MEMBERCONTENT = 0;
    final int COMMON_MAIL_CONTACTCONTENT = 1;
    
    final int DIALOG_MEMBER_INFO = 1111;
    final int DIALOG_OVERWRITE_CONFIRM = 1112;
    final int DIALOG_COMPLETE_SAVE = 1113;
    final int DIALOG_NO_APP = 1114;
    
    public LinearLayout m_LinearLayout;
    public EmployeeAddJobData[] m_szEmployeeAddJobData;

	@Override
	protected int assignLayout() 
	{			
//		if(mType == null || "".equals(mType)) 
//			mType = "M";
//		
//		if(!mType.equals("E"))		
			return R.layout.mail_member_memberinfo;
//		else
//			return R.layout.member_outlook_info;
	}
	
	/* (non-Javadoc)
	 * @see com.sk.pe.group.activity.BaseActivity#onCreateX(android.os.Bundle)
	 */
	public void onCreateX(Bundle instanceState)
	{
		super.onCreateX(instanceState);		
		onPostCreateX();
	}
	
	protected void onPostCreateX()
	{		
		Bundle b = getIntent().getExtras();
		mType = b.getString("type");
		
		mDeptCode = b.getString("deptCode");
		String m_szSerialNo = b.getString("employee");
		try {
			mCompanyCode = SKTUtil.getCheckedCompanyCd(this);
		} catch (SKTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if("E".equals(mType)){
			changeKey = b.getString("changeKey");
		}else{
		
		}
		
		boolean isLast = b.getBoolean("isLast", false);
		if(myId.equals(m_szSerialNo)) 
			mIsMyProfile = true;
		
//		if(!isLast && ("M".equals(mType) || "E".equals(mType))) 
//		{
//			mCompanyCode = b.getString("companyCode");
//		} else {
//		}
		
//		if(mCompanyCode == null) 
//			mCompanyCode = "";		
		if("E".equals(mType)) 
		{
			m_CurEmployeeData = b.getParcelable("data");
			requestData(COMMON_MAIL_CONTACTCONTENT, "");			
		} 
		else
			requestData(PEOPLE_MEMBERCONTENT, m_szSerialNo);
	}
	
	/* (non-Javadoc)
	 * 팝업 생성 함수
	 * @see com.sk.pe.group.activity.BaseActivity#onCreateDialog(int)
	 */
	protected Dialog onCreateDialog(int a_nId) {
		Dialog aDialog = null;
    	
		switch (a_nId) {
		case DIALOG_NO_APP:
		case DIALOG_MEMBER_INFO:
		case DIALOG_COMPLETE_SAVE:
			aDialog = createDialog(a_nId, m_szDialogMessage, DIALOG_ONE_BUTTON,
					getString(R.string.mail_ok_button), null, null);
    		break;
		case DIALOG_OVERWRITE_CONFIRM:
			aDialog = createDialog(a_nId, m_szDialogMessage, DIALOG_TWO_BUTTON,
					getString(R.string.mail_ok_button), getString(R.string.mail_cancel_button), null);
		} // end switch (a_nId)
    	
    	return aDialog;
	}
	
	/* (non-Javadoc)
	 * 팝업 클릭 이벤트 핸들러
	 * @see com.sk.pe.group.activity.BaseActivity#onClickDialog(int, java.lang.String, int)
	 */
	public void onClickDialog(int a_nDialogId, String a_szButtonText, int a_nClickedButton) {
		Intent intent = null;
		
		switch (a_nDialogId) {
		case DIALOG_MEMBER_INFO:
			finish();
    		break;
		case DIALOG_COMPLETE_SAVE:
//			intent = new Intent("com.android.contacts.action.LIST_DEFAULT");
//			startActivity(intent);
			long _id = existContact(this, m_CurEmployeeData);
			Intent i = new Intent(Intent.ACTION_VIEW);
		    Uri ui = Contacts.CONTENT_URI;
		    i.setData(Uri.parse(ui+"/"+  _id));
		    startActivity(i);
			
			break;
		case DIALOG_OVERWRITE_CONFIRM:
			if(a_nClickedButton == DIALOG_CLICKED_BUTTON_FIRST) {
				deleteContact(this, mId);
				createContactEntry(this, m_CurEmployeeData, mId);
				m_szTitle = getString(R.string.mail_title_save_phone);
				m_szDialogMessage = getString(R.string.mail_text_save_phone);
				showDialog(DIALOG_COMPLETE_SAVE);
			}
			break;
		case DIALOG_NO_APP:
			
			intent = new Intent(Constants.Action.STORE_DETAIL_VIEW);
			intent.putExtra("APP_ID", "MOGP000001");
			startActivity(intent);
			
			break;
		} // end switch (a_nDialogId)
	} // end public void onClickDialog(int a_nDialogId, String a_szButtonText, int a_nClickedButton)
	
	/* (non-Javadoc)
	 * 옵션 메뉴 생성 함수<br>
	 * - 폰에 주소록 저장
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, SAVE_PHONE_ADDR, Menu.NONE, R.string.mail_save_phone_addr);
		
		return super.onCreateOptionsMenu(menu);
	}
    
	/* (non-Javadoc)
	 * 옵션 메뉴 선택 이벤트 핸들러<br>
	 * - 폰에 주소록 저장
	 * @see android.app.Activity#onMenuItemSelected(int, android.view.MenuItem)
	 */
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		if(item.getItemId() == SAVE_PHONE_ADDR) {
			mId = existContact(this, m_CurEmployeeData);
			if(mId > 0) {   
				m_szTitle = getString(R.string.mail_title_save_phone);
				m_szDialogMessage = m_CurEmployeeData.m_szName + getString(R.string.mail_text_overwrite);
				showDialog(DIALOG_OVERWRITE_CONFIRM);
			} else {
				if (createContactEntry(this, m_CurEmployeeData, mId)) {
					m_szTitle = getString(R.string.mail_title_save_phone);
					m_szDialogMessage = getString(R.string.mail_text_save_phone);
					showDialog(DIALOG_COMPLETE_SAVE);
				}
				
			}
			
			return true;
		}
		
		return super.onMenuItemSelected(featureId, item);
	}
	
	
	/**
	 * 주소록에 멤버 저장
	 * @param act 액티비티
	 * @param data 멤버 정보
	 * @param mId 기존 멤버의 주소록 아이디
	 */
	public static boolean createContactEntry(Activity act, EmployeeData data, long mId)
	{
        // Get values from UI
		String name = data.m_szName;
		String phone = data.m_szCellPhoneNo;
		String officePhone = data.m_szOfficePhoneNo;
		String email = data.m_szMail;
//		String extEmail = data.m_szMail;
		String company = data.m_szCompany;
		String dept = data.m_szDepartment;
		String role = data.m_szRole;
		String location = data.m_szLocation;
//		String messenger = data.m_szMessenger;
		String landline = data.getLandline();
		String memo = data.getMemo();
		String photo = data.m_szPicturePath;
		ContentValues values = new ContentValues();
		
		try {
			
		
		if(mId > 0) {
			rawContactId = mId;
		} else {
			values.put(RawContacts.CONTACT_ID, name);
			Uri rawContactUri = act.getContentResolver().insert(RawContacts.CONTENT_URI, values);
			rawContactId = ContentUris.parseId(rawContactUri);
		}
		 if(!StringUtil.isNull(photo)) {
			 Bitmap img2 = null ;
			 try {
				 if(photo.startsWith("http")) {
					 img2 = ResourceUtil.getBitmapByUrl(photo);

				 } else {
					 img2 = ResourceUtil.decodeBitmap(photo);
				 }
			 } catch (SKTException e) {
				 // TODO Auto-generated catch block
				 e.printStackTrace();
			 }
			 
			
			 ByteArrayOutputStream stream = new ByteArrayOutputStream();
			 img2.compress(Bitmap.CompressFormat.JPEG, 75, stream);
			 
			 values.clear();//사진
			 values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
			 values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE);
			 values.put(ContactsContract.CommonDataKinds.Photo.PHOTO, stream.toByteArray() );
			 act.getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);
		}

		// 이름 입력
		values.clear();
		values.put(Data.RAW_CONTACT_ID, rawContactId);
		values.put(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE);
		values.put(StructuredName.DISPLAY_NAME, name);
		act.getContentResolver().insert(Data.CONTENT_URI, values);

		// 전화번호 입력
		values.clear();
		values.put(Data.RAW_CONTACT_ID, rawContactId);
		values.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
		values.put(Phone.TYPE, Phone.TYPE_MOBILE);
		values.put(Phone.NUMBER, phone);
		act.getContentResolver().insert(Data.CONTENT_URI, values);

		//집전화 입력
		values.clear();
		values.put(Data.RAW_CONTACT_ID, rawContactId);
		values.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
		values.put(Phone.TYPE, Phone.TYPE_HOME);
		values.put(Phone.NUMBER, landline);
		act.getContentResolver().insert(Data.CONTENT_URI, values);

		// 회사 전화번호 입력
		values.clear();
		values.put(Data.RAW_CONTACT_ID, rawContactId);
		values.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
		values.put(Phone.TYPE, Phone.TYPE_WORK);
		values.put(Phone.NUMBER, officePhone);
		act.getContentResolver().insert(Data.CONTENT_URI, values);

		// 내부메일 입력
		values.clear();
		values.put(Data.RAW_CONTACT_ID, rawContactId);
		values.put(Data.MIMETYPE, Email.CONTENT_ITEM_TYPE);
		values.put(Email.TYPE, Email.TYPE_WORK);
		values.put(Email.DATA, email);
		act.getContentResolver().insert(Data.CONTENT_URI, values);

		//메모 입력
		values.clear();
		values.put(Data.RAW_CONTACT_ID, rawContactId);
		values.put(Data.MIMETYPE, Note.CONTENT_ITEM_TYPE);
		values.put(Note.NOTE, memo);
		act.getContentResolver().insert(Data.CONTENT_URI, values);

		// 외부메일 입력
//		values.clear();
//		values.put(Data.RAW_CONTACT_ID, rawContactId);
//		values.put(Data.MIMETYPE, Email.CONTENT_ITEM_TYPE);
//		values.put(Email.TYPE, Email.TYPE_HOME);
//		values.put(Email.DATA, extEmail);
//		act.getContentResolver().insert(Data.CONTENT_URI, values);
//		android.util.Log.i("member", dataUri.toString());

		// 회사명, 부서명, 근무지, 직급 입력
		values.clear();
		values.put(Data.RAW_CONTACT_ID, rawContactId);
		values.put(Data.MIMETYPE, Organization.CONTENT_ITEM_TYPE);
		values.put(Organization.TYPE, Organization.TYPE_WORK);
		values.put(Organization.COMPANY, company);
		values.put(Organization.DEPARTMENT, dept);
		values.put(Organization.OFFICE_LOCATION, location);
		values.put(Organization.TITLE, role);
		act.getContentResolver().insert(Data.CONTENT_URI, values);

		// 메신저 입력
//		values.clear();
//		values.put(Data.RAW_CONTACT_ID, rawContactId);
//		values.put(Data.MIMETYPE, Im.CONTENT_ITEM_TYPE);
//		values.put(Im.TYPE, Im.TYPE_WORK);
//		values.put(Im.PROTOCOL, Im.PROTOCOL_CUSTOM);
//		values.put(Im.CUSTOM_PROTOCOL, "ETC");
//		values.put(Im.DATA, messenger);
//		act.getContentResolver().insert(Data.CONTENT_URI, values);
//		android.util.Log.i("member", dataUri.toString());

		} catch (Exception e) {
			return false;
		}
		return true;

	}

	/**
	 * 주소록에서 멤버 정보 삭제
	 * @param act 액티비티
	 * @param id 멤버의 주소록 아이디
	 */
	public static void deleteContact(Activity act, long id)
	{
//		String income = RawContacts.CONTACT_ID + " = " + id;
//		act.getContentResolver().delete(RawContacts.CONTENT_URI, income, null);
//		android.util.Log.i("member", "delete id = " + id);
//		String cid = "";
		String income = Data.RAW_CONTACT_ID + " = " + id;
//		Cursor c = act.managedQuery(Data.CONTENT_URI, new String[] { Data.CONTACT_ID }, income,
//				null, null);
//		while(c.moveToNext()) {
//			try {
//				cid = c.getString(c.getColumnIndex(Data.CONTACT_ID));
//				android.util.Log.i("member", "aaa = " + cid);
//			} catch(Exception e) {
//				break;
//			}
//		}
		act.getContentResolver().delete(Data.CONTENT_URI, income, null);
//		act.getContentResolver().delete(RawContacts.CONTENT_URI,
//				RawContacts.CONTACT_ID + " = " + cid, null);
	}

	/**
	 * 멤버가 주소록에 존재하는지 여부
	 * @param act 액티비티
	 * @param data 멤버 정보
	 * @return 멤버의 주소록 아이디
	 */
	public static long existContact(Activity act, EmployeeData data) {
		long id = 0;
		Cursor c = act.managedQuery(Data.CONTENT_URI,
				new String[] { Data.RAW_CONTACT_ID, Phone.NUMBER },
				Phone.NUMBER + " = '" + data.m_szCellPhoneNo.replace("-", "") + "' AND " +
						Phone.DISPLAY_NAME + " = '" + data.m_szName + "'",
				null, null);

		while(c.moveToNext()) {
			id = c.getLong(c.getColumnIndex(Data.RAW_CONTACT_ID));
			String number = c.getString(c.getColumnIndex(Phone.NUMBER));
			android.util.Log.i("member", "id = " + id + ", phone = " + number);
			break;
		}

		return id;
	}

	@Override
	protected void onActivityResultX(int requestCode, int resultCode, Intent data) {
		if (requestCode==1) {
			if (resultCode==2)
				finish();
		}
	}

	/**
	 * 화면 UI 설정
	 */
	private void setUI()
	{

		//사진을 넣는다.
		ImageView imgView;
		try
		{
	        imgView = (ImageView)findViewById(R.id.imgPhoto);

	        if(!m_CurEmployeeData.m_szPicturePath.equals(""))
	        {
	        	Bitmap img;

	        	if(m_CurEmployeeData.m_szPicturePath.startsWith("http")) {
	        		img = ResourceUtil.getBitmapByUrl(m_CurEmployeeData.m_szPicturePath);
	        	} else {
	        		img = ResourceUtil.decodeBitmap(m_CurEmployeeData.m_szPicturePath);
	        	}
		        imgView.setImageBitmap(img);
		        imgView.setBackgroundResource(0);
		        img = imgView.getDrawingCache();

	        } else {
	        	imgView.setImageResource(R.drawable.mail_member_photo_face);
	        }
        } catch (SKTException e) { android.util.Log.e("member", e.getMessage()); }
		
        m_LinearLayout = (LinearLayout) findViewById(R.id.addjobList);
        m_LinearLayout.removeAllViews();
        //이름
		TextView tv = null;
		tv = (TextView) findViewById(R.id.EMPLOYEE_INFO_NAME);
		tv.setText(m_CurEmployeeData.m_szName);
		
		if ("Y".equals(m_CurEmployeeData.m_szTeamLeader)) {
			tv.setTextColor(Color.rgb(0x1C, 0x8C, 0xC8));
		}
		
		// 사번 
		tv = (TextView)findViewById(R.id.employee_serialno);
		tv.setText(m_CurEmployeeData.m_szSerialNo);
//		if ("Y".equals(m_CurEmployeeData.m_szTeamLeader)) {
//			tv.setTextColor(Color.rgb(0x1C, 0x8C, 0xC8));
//		}

		//직위
		tv = (TextView)findViewById(R.id.employee_role);
		tv.setText(m_CurEmployeeData.m_szRole);
//		if("N".equals(m_CurEmployeeData.m_szVvip)){
//			//아이디
//		}else{
//			tv = (TextView)findViewById(R.id.employee_id);
//			tv.setVisibility(View.GONE);			
//		}
		// 입사일
		tv = (TextView)findViewById(R.id.employee_joindate);
		tv.setText(m_CurEmployeeData.getJoinDate());
		
		//최종승진일
		tv = (TextView)findViewById(R.id.employee_promotiondate);
		tv.setText(m_CurEmployeeData.getPromotionDate());

		//회사명
		tv = (TextView)findViewById(R.id.company_name);
		tv.setText(m_CurEmployeeData.m_szCompany);
		//부서명
		tv = (TextView)findViewById(R.id.suffix_dept);
		tv.setText(m_CurEmployeeData.m_szTeam);
		
		//휴대전화
		if(!m_CurEmployeeData.m_szCellPhoneNo.equals("")) 
		{
			findViewById(R.id.layout_tel).setVisibility(View.VISIBLE);
			tv = (TextView) findViewById(R.id.EMPLOYEE_INFO_TEL);
			tv.setText(m_CurEmployeeData.m_szCellPhoneNo);
		}
		
		//회사전화
		if(!m_CurEmployeeData.m_szOfficePhoneNo.equals("")) 
		{
			findViewById(R.id.layout_office).setVisibility(View.VISIBLE);
			tv = (TextView) findViewById(R.id.EMPLOYEE_INFO_OFFICE);
			tv.setText(m_CurEmployeeData.m_szOfficePhoneNo);
		}

		// 외부 메일 -> E-Mail
		if(!m_CurEmployeeData.m_szMail.equals("")) 
		{
			findViewById(R.id.layout_outmail).setVisibility(View.VISIBLE);
			tv = (TextView) findViewById(R.id.EMPLOYEE_INFO_OUTMAIL);
			tv.setText(m_CurEmployeeData.m_szMail);
		}

		// 구내전화
		if(!StringUtil.isNull(m_CurEmployeeData.m_szInnerPhoneNo))
		{
			findViewById(R.id.layout_inner).setVisibility(View.VISIBLE);
			tv = (TextView) findViewById(R.id.EMPLOYEE_INFO_INNER);
			if("Y".equals(m_CurEmployeeData.m_szVvip)){
				tv.setText("");
			}else{
				tv.setText(m_CurEmployeeData.m_szInnerPhoneNo);
			}
		}		
		
		
//		//회사메일
//		if(!StringUtil.isNull(m_CurEmployeeData.m_szInnerMail)) 
//		{
//
//			findViewById(R.id.layout_mail).setVisibility(View.VISIBLE);
//			tv = (TextView) findViewById(R.id.EMPLOYEE_INFO_MAIL);
//			if("Y".equals(m_CurEmployeeData.m_szVvip)){
//				tv.setText("");
//			}else{
//				tv.setText(m_CurEmployeeData.m_szInnerMail);
//			}
//		}		

//		//회사아이디
//		if(!StringUtil.isNull(m_CurEmployeeData.m_szTwitter))
//		{
//			findViewById(R.id.layout_corp_id).setVisibility(View.GONE);
//			tv = (TextView) findViewById(R.id.corp_id);
//			if("Y".equals(m_CurEmployeeData.m_szVvip)){
//				tv.setText("");
//			}else{
//				tv.setText(m_CurEmployeeData.m_szTwitter);
//			}
//		}

//		//업무
//		if(!StringUtil.isNull(m_CurEmployeeData.m_szWork)) 
//		{
//			findViewById(R.id.layout_work).setVisibility(View.VISIBLE);
//			tv = (TextView)findViewById(R.id.EMPLOYEE_INFO_WORK);
//			tv.setText(m_CurEmployeeData.m_szWork);
//		}
//		
//		//직무
//		if(!StringUtil.isNull(m_CurEmployeeData.m_szRole)) 
//		{
//			findViewById(R.id.layout_role).setVisibility(View.VISIBLE);
//			tv = (TextView)findViewById(R.id.role);
//			tv.setText(m_CurEmployeeData.m_szRole);
//		}
				
		//전화 아이콘을 눌렀을시
		ImageView imageBtn = (ImageView)findViewById(R.id.EMPLOYEE_INFO_TEL_BTN);
    	if (m_CurEmployeeData.m_szCellPhoneNo.equals("")) 
    		imageBtn.setVisibility(View.INVISIBLE);
    	else 
    	{
    		imageBtn.setVisibility(View.VISIBLE);
    		imageBtn.setTag(m_CurEmployeeData.m_szCellPhoneNo);
    		imageBtn.setOnClickListener(clickListener);
    	}
    	
    	//sms 아이콘을 눌렀을시
    	imageBtn = (ImageView) findViewById(R.id.EMPLOYEE_INFO_SMS_BTN);
    	if (m_CurEmployeeData.m_szCellPhoneNo.equals("")) 
    		imageBtn.setVisibility(View.INVISIBLE);
    	else 
    	{
    		imageBtn.setVisibility(View.VISIBLE);
    		imageBtn.setTag(m_CurEmployeeData.m_szCellPhoneNo);
    		imageBtn.setOnClickListener(clickListener);
    	}
    	
    	//회사전화 아이콘을 눌렀을시
    	imageBtn = (ImageView) findViewById(R.id.EMPLOYEE_INFO_OFFICE_BTN);
    	if(m_CurEmployeeData.m_szOfficePhoneNo.equals("")) 
    		imageBtn.setVisibility(View.INVISIBLE);
    	else
    	{
    		imageBtn.setVisibility(View.VISIBLE);
    		imageBtn.setTag(m_CurEmployeeData.m_szOfficePhoneNo);
    		imageBtn.setOnClickListener(clickListener);
    	}
		
    	//메일 아이콘을 눌렀을시
    	/* 2015-03-17 Join 수정 시작 - 기존 내부 메일 항목이 구내전화로 바꼈기 때문에 주석처리
    	imageBtn = (ImageView) findViewById(R.id.EMPLOYEE_INFO_MAIL_BTN);
    	if (m_CurEmployeeData.m_szInnerMail.equals("")) 
    		imageBtn.setVisibility(View.INVISIBLE);
    	else 
    	{
    		imageBtn.setVisibility(View.VISIBLE);
    		imageBtn.setTag(m_CurEmployeeData.m_szInnerMail);
    		imageBtn.setOnClickListener(clickListener);
    	}		
    	 2015-03-17 Join 수정 끝 */
    	
    	if(!"E".equals(mType)){


    		int addJobCnt = m_CurEmployeeData.m_szEmployeeAddJobData.length;

    		for (int i = 0; i < addJobCnt; i++) {
    			LinearLayout aLinearLayoutTitleInflate = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.mail_member_addjobview, null);
    			TextView tvComNm = (TextView) aLinearLayoutTitleInflate.findViewById(R.id.addcompany_name);
    			tvComNm.setText(m_CurEmployeeData.m_szEmployeeAddJobData[i].getCompanyNm());
    			TextView tvSufixDt = (TextView) aLinearLayoutTitleInflate.findViewById(R.id.addsuffix_dept);
    			tvSufixDt.setText(m_CurEmployeeData.m_szEmployeeAddJobData[i].getSuffixDept());

    			m_LinearLayout.addView(aLinearLayoutTitleInflate);
    		}
    	}
	}
	
	private OnClickListener clickListener = new OnClickListener()
	{
		@Override
        public void onClick(View v)
		{		
			Uri uri = null;
			Intent intent = null;
			
			switch(v.getId())
			{
			case R.id.EMPLOYEE_INFO_TEL_BTN :
			case R.id.EMPLOYEE_INFO_OFFICE_BTN :			
				uri = Uri.parse("tel:" + (String)v.getTag());
    			intent = new Intent(Intent.ACTION_DIAL, uri);
    			startActivity(intent);    			
    			break;
    			
			case R.id.EMPLOYEE_INFO_SMS_BTN :
				uri = Uri.parse("sms:" + (String)v.getTag());
                intent = new Intent(Intent.ACTION_SENDTO, uri);
                startActivity(intent);
				break;
				
			case R.id.EMPLOYEE_INFO_MAIL_BTN :
//				intent = new Intent(AddressInfoActivity.this,
//            			SendEMAILActivity.class);
//            	ArrayList<String> mEmployeeName = new ArrayList<String>();
//            	ArrayList<String> mEmployeePhoneNum = new ArrayList<String>();
//				mEmployeePhoneNum.add(m_CurEmployeeData.m_szSerialNo);
//				mEmployeeName.add(m_CurEmployeeData.m_szName);
//            	intent.putStringArrayListExtra("employeenum", mEmployeePhoneNum);
//            	intent.putStringArrayListExtra("employee", mEmployeeName);
//            	intent.putExtra("isChild", false);
				try
        		{    	
					intent = new Intent(ACTION_EMAIL_CLIENT);
					SKTUtil.log(Log.DEBUG, "xxxxxxxxx", "xxxxxxxxx ACTION_EMAIL_CLIENT  :: " + ACTION_EMAIL_CLIENT);
					intent.putExtra("type", "side");
					intent.putExtra("names", new String[]{m_CurEmployeeData.m_szName});
					intent.putExtra("emails", new String[]{m_CurEmployeeData.m_szInnerMail});
					intent.putExtra("vvip", new String[]{m_CurEmployeeData.m_szVvip.equals("Y") ? "true" : "false"});
					//intent.putExtra("vvip", new String[]{m_CurEmployeeData.m_szVvip.equals("N") ? "true" : "false"});

        			startActivity(intent);
        		}
        		catch(ActivityNotFoundException e)
        		{
        			m_szTitle = getString(R.string.mail_title_no_mail);
					m_szDialogMessage = getString(R.string.mail_no_emailclient);
            		showDialog(DIALOG_NO_APP);
        		}
				break;
			}
		}		
	};

	public String setTrimData(String param){

		String tempData;
		if("".equals(param) || param == null){
			tempData = "";
		}else{
			tempData = param.trim();
		}
		
		return tempData;
		
		
	}
	
	/* (non-Javadoc)
	 * 액션 처리 후 UI 세팅<br>
	 * - 멤버 상세 정보
	 * @see com.sk.pe.group.activity.BaseActivity#onPostThread(com.skt.pe.common.service.XMLData, com.skt.pe.common.exception.SKTException)
	 */
	public void onPostThread(XMLData a_XMLData, SKTException a_exception)
	{
		// 정상 응답이  발생하였으면 오류 메시지 보여주고 리턴
		if (a_exception != null) {
			m_szTitle = getString(R.string.mail_text_error);
    		m_szDialogMessage = a_exception.getMessage();
			showDialog(DIALOG_MEMBER_INFO);
			return;
		} // end else if (m_szResponseCode != null && m_szResponseCode.equals("1000") == false)

    	
		switch (m_nRequestType) 
		{
			case COMMON_MAIL_CONTACTCONTENT :
				try
				{				
					if(m_CurEmployeeData == null)
						m_CurEmployeeData = new EmployeeData();
										
					m_CurEmployeeData.setLandline(setTrimData(a_XMLData.get("telNum")))
					 				 .setCurrentKey(setTrimData(a_XMLData.get("changeKey")))
					 				 .setEmpId(setTrimData(a_XMLData.get("id")))
					 				 .setRole(setTrimData(a_XMLData.get("role")))
									 .setOfficePhoneNo(a_XMLData.get("phone"))
									 .setInnerPhoneNo(a_XMLData.get("innerTelNum"))
									 .setCellPhoneNo(a_XMLData.get("telNum"))
					 				 .setName(setTrimData(a_XMLData.get("name")))
					 				 .setCompany(setTrimData(a_XMLData.get("companyNm")))
					 				 .setVvip(setTrimData(a_XMLData.get("vvip")))
					 				 .setTeam(setTrimData(a_XMLData.get("suffixDept")))
					 				 .setPicturePath(a_XMLData.get("photo"))
					 				 .setInnerMail(a_XMLData.get("inEmail"))
					 				 .setMail(a_XMLData.get("outEmail"))
					 				 .setSerialNo(a_XMLData.get("id"))
					 				 .setEngName(a_XMLData.get("engName"))
					 				 .setTeamLeader(a_XMLData.get("teamManager"));
					
				}
				
				catch(Exception e) {
					e.printStackTrace();
				}

				break;
		
			case PEOPLE_MEMBERCONTENT:
				try {
					m_szResponseCode = a_XMLData.get("result");
					m_szResponseMessage = a_XMLData.get("resultMessage");
					
					m_CurEmployeeData = new EmployeeData()
						   .setSerialNo(a_XMLData.get("id"))
						   .setDutNm(a_XMLData.get("dutNm"))
						   .setDepartment(a_XMLData.get("deptCode"))
						   .setUpTeam(a_XMLData.get("prefixDept"))
						   .setTeam(a_XMLData.get("suffixDept"))
						   .setName(a_XMLData.get("name"))
						   .setPicturePath(a_XMLData.get("photo"))
						   .setLocation(a_XMLData.get("loc"))
						   .setOfficePhoneNo(a_XMLData.get("phone"))
						   .setCellPhoneNo(a_XMLData.get("telNum"))
						   .setMail(a_XMLData.get("extEmail"))
						   .setInnerMail(a_XMLData.get("inEmail"))
						   .setInnerPhoneNo(a_XMLData.get("innerTelNum"))
						   .setMail(a_XMLData.get("outEmail"))
						   .setWzone(a_XMLData.get("wzone"))
						   .setWork(a_XMLData.get("work"))
						   .setRole(a_XMLData.get("role"))
						   .setMessenger(a_XMLData.get("messenger"))
						   .setTwitter(a_XMLData.get("twitter"))
						   .setCompany(a_XMLData.get("companyNm"))
						   .setEngName(a_XMLData.get("engName"))
						   .setVvip(a_XMLData.get("vvip"))
						   .setTeamLeader(a_XMLData.get("teamManager"))
						   .setJoinDate(a_XMLData.get("joinDate"))
						   .setPromotionDate(a_XMLData.get("promotionDate"));
					
					a_XMLData.setList("addJobList");
					int listCnt = Integer.parseInt(a_XMLData.get("listCnt"));
					a_XMLData.setList("addJob");

					m_CurEmployeeData.m_szEmployeeAddJobData  = new EmployeeAddJobData[listCnt];
					for (int i = 0; i < listCnt; i++) {

						m_CurEmployeeData.m_szEmployeeAddJobData[i] = new EmployeeAddJobData();
						m_CurEmployeeData.m_szEmployeeAddJobData[i].setId(a_XMLData.get(i, "id"));  
						m_CurEmployeeData.m_szEmployeeAddJobData[i].setDeptCode(a_XMLData.get(i, "deptCode"));  
						m_CurEmployeeData.m_szEmployeeAddJobData[i].setSuffixDept(a_XMLData.get(i, "suffixDept"));  
						m_CurEmployeeData.m_szEmployeeAddJobData[i].setCompanyCd(a_XMLData.get(i, "companyCd"));  
						m_CurEmployeeData.m_szEmployeeAddJobData[i].setTwitter(a_XMLData.get(i, "twitter"));  
						m_CurEmployeeData.m_szEmployeeAddJobData[i].setCompanyNm(a_XMLData.get(i, "companyNm"));  

					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				
				break;
		}
		
		setUI();
		saveDB();
	}
	
	/**
	 * 최근 조회 데이터베이스에 저장
	 */
	private void saveDB() {
		
		/*
		 * 암호화를 위한 SEEDLibary 를 사용하기 위해서는 
		 * KISACryptoLib.jar 와  SEEDLibary 프로젝트를 빌드패스에 참조 시켜줘야함 .
		 */
		m_dbHelper = new MemberSearchSQLite(this);
        m_dbMember = m_dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        
        int nDBCount = 0;
        Cursor cs = null;
        
        if(m_CurEmployeeData.m_szSerialNo.equals("") || mIsMyProfile) {
        	m_dbMember.close();
			m_dbHelper.close();
			return;
		}
	    try 
	    {
	    	String comCd = SeedCrypter.encrypt(SKTUtil.getCheckedCompanyCd(this));
	    	String tempType = SeedCrypter.encrypt(mType);
	    	
			cs = m_dbMember.query(MemberSearchSQLite.DATABASE_TABLE,
					new String[] { "serial", "deptname", "name", "telnum", "role", "work",
							"company", "phone", "email", "innerphone", "type", "infocompanycd", "empid",
							"searchdate" ,"deptcode"},
					"type = '" + tempType + "' AND companycd = '" + comCd + "'", null, null, null,
					null);
			
			if(cs != null) {
				nDBCount = cs.getCount();
				if(nDBCount > 0) {
					cs.moveToFirst();
					do {
						if("E".equals(mType)) {
							if(m_CurEmployeeData.m_szName
											.equals(SeedCrypter.decrypt(cs.getString(cs.getColumnIndex("name")))) &&
									m_CurEmployeeData.m_szInnerMail
											.equals(SeedCrypter.decrypt(cs.getString(cs.getColumnIndex("email"))))) {
								m_dbMember.execSQL("DELETE FROM member where serial = '" +
										SeedCrypter.encrypt(m_CurEmployeeData.m_szSerialNo) + "' AND type = '" +
										tempType + "' AND companycd = '" + comCd + "'");
								nDBCount--;
								break;
							}
						} else {
							if(m_CurEmployeeData.m_szSerialNo
									.equals(SeedCrypter.decrypt(cs.getString(cs.getColumnIndex("serial"))))) {
								m_dbMember.execSQL("DELETE FROM member where serial = '" +
										SeedCrypter.encrypt(m_CurEmployeeData.m_szSerialNo) + "' AND type = '" +
										tempType + "' AND companycd = '" + comCd + "'");
								nDBCount--;
								break;
							}
						}
					} while(cs.moveToNext());
				}
				if(nDBCount == 15) {
					cs.moveToFirst();
					if("E".equals(mType)) {
						m_dbMember.execSQL("DELETE FROM member where name = '" +
								cs.getString(cs.getColumnIndex("name")) + "' AND email = '" +
								cs.getString(cs.getColumnIndex("email")) + "' AND type = '" +
								tempType + "' AND companycd = '" + comCd + "'");
					} else {
						m_dbMember.execSQL("DELETE FROM member where serial = '" +
								cs.getString(cs.getColumnIndex("serial")) + "' AND type = '" +
								tempType + "' AND companycd = '" + comCd + "'");
					}
				}
			}
			
			cv.put("serial", SeedCrypter.encrypt(m_CurEmployeeData.m_szSerialNo));
        	cv.put("deptname", SeedCrypter.encrypt(m_CurEmployeeData.m_szTeam));
        	cv.put("name", SeedCrypter.encrypt(m_CurEmployeeData.m_szName));
        	cv.put("telnum", SeedCrypter.encrypt(m_CurEmployeeData.m_szCellPhoneNo));
        	cv.put("role", SeedCrypter.encrypt(m_CurEmployeeData.m_szRole));
        	cv.put("work", SeedCrypter.encrypt(m_CurEmployeeData.m_szWork));
        	cv.put("company", SeedCrypter.encrypt(m_CurEmployeeData.m_szCompany));
        	cv.put("phone", SeedCrypter.encrypt(m_CurEmployeeData.m_szOfficePhoneNo));
        	cv.put("email", SeedCrypter.encrypt(m_CurEmployeeData.m_szInnerMail));
        	cv.put("innerphone", SeedCrypter.encrypt(m_CurEmployeeData.m_szInnerPhoneNo));
        	cv.put("type", tempType);
        	cv.put("infocompanycd", SeedCrypter.encrypt(mCompanyCode));
        	cv.put("empid", SeedCrypter.encrypt(m_CurEmployeeData.m_szEmpId));
        	cv.put("companycd", comCd);
        	cv.put("searchdate", "");
        	cv.put("deptcode", SeedCrypter.encrypt(m_CurEmployeeData.m_szDepartment));        	
        	cv.put("currentkey", SeedCrypter.encrypt(m_CurEmployeeData.getCurrentKey()));
        	cv.put("landline", SeedCrypter.encrypt(m_CurEmployeeData.getLandline()));
        	cv.put("vvip", SeedCrypter.encrypt(m_CurEmployeeData.m_szVvip));
        	cv.put("teamManager", SeedCrypter.encrypt(m_CurEmployeeData.m_szTeamLeader));
        	cv.put("memo", SeedCrypter.encrypt(setTrimData(m_CurEmployeeData.getMemo())));
        	
			m_dbMember.insert(MemberSearchSQLite.DATABASE_TABLE, null, cv);
	    } catch(Exception e) {
        	e.printStackTrace();
        } finally {
        	if(cs != null) {
        		cs.close();
        	}
        }
		m_dbMember.close();
		m_dbHelper.close();
	}

	/* (non-Javadoc)
	 * 액션 처리 핸들러<br>
	 * - 멤버 상세 정보
	 * @see com.sk.pe.group.activity.BaseActivity#onProcessThread()
	 */
	public XMLData onProcessThread() throws SKTException
	{
		Parameters params = null;
	    Controller controller = null;
	    XMLData nXMLData = null;
	    
	    switch (m_nRequestType) 
		{
			case COMMON_MAIL_CONTACTCONTENT :
				params = new Parameters("COMMON_MAIL_CONTACTCONTENT");
				params.put("ID", changeKey);
				params.put("companyCd", mCompanyCode);
				break;
		
			case PEOPLE_MEMBERCONTENT:
				params = new Parameters("COMMON_PEOPLE_MEMBERCONTENT");
				params.put("id", (String)m_requestObject);
				params.put("type", "G");
				params.put("infoCompanyCd", mCompanyCode);
				params.put("deptCode", mDeptCode);				
				break;
		}
		
		controller = new Controller(this);
		nXMLData = controller.request(params);
		
		return nXMLData;
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(AddressSearchActivity.LAST_SEARCH_UPDATE);
		sendBroadcast(intent);
		super.onBackPressed();
	}
}


