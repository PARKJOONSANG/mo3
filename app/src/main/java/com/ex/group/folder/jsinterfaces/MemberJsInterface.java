package com.ex.group.folder.jsinterfaces;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.Context;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Data;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import java.util.ArrayList;

public class MemberJsInterface {
	final String TAG = "MemberJsInterface";
	final String saveSuccess = " 님 연락처를 저장하였습니다.";
	final String saveFailed = " 님 연락처 저장을 실패했습니다.\n다시 시도해 주십시오.";
//	String code;
	Context mContext;
	WebView webView;
	
	
	public MemberJsInterface(Context context, WebView webView){
		this.mContext = context;
		this.webView = webView;
	}
	//tree.jsp
	@JavascriptInterface
	public void setCode(final String code, final String modal){
//		this.code = code;
		Log.i(TAG, "parentDeptCode ===========>>>>"+code);
		Log.i(TAG, "modalLength ===========>>>>"+modal);
		
		webView.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(!("Y").equals(modal)){
					if(!("undefined").equals(code)){
						webView.loadUrl("javascript:getData('"+code+"')");	
					}
					else{
						((Activity)mContext).finish();
					}
				}
				else{
//					Toast.makeText(mContext, "modal hide! modal length ::: "+modal, Toast.LENGTH_SHORT).show();
					webView.loadUrl("javascript:$('#getUserDetail').modal('hide');");
				}
					
				
			}
		});
	}
	
	//main.jsp
	@JavascriptInterface
	public void setFinish(final String modal){
		Log.i(TAG, "setFinish======>>"+modal);
		webView.post(new Runnable() {
			
			@Override
			public void run() {
				if(!("Y").equals(modal)){
					((Activity)mContext).finish();
				}
				else{
					webView.loadUrl("javascript:$('#getUserDetail').modal('hide');");
				}
			}
		});
		
	}
	
	
	@JavascriptInterface
	public void saveContact(String names, String mobile, String tel, String email){
		
		Log.i(TAG, "name===>"+names +"  mobile====> "+mobile+"   tel======> "+tel+"    email======>>>>"+email);
		
		 ArrayList<ContentProviderOperation> operationList = new ArrayList<ContentProviderOperation>(); 
		 
		    operationList.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI) 
		            .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null) 
		            .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null) 
		            .build()); 

		    // first and last names 
		    operationList.add(ContentProviderOperation.newInsert(Data.CONTENT_URI) 
		            .withValueBackReference(Data.RAW_CONTACT_ID, 0) 
		            .withValue(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE) 
		            .withValue(StructuredName.GIVEN_NAME, names) 
		            .build()); 

		    operationList.add(ContentProviderOperation.newInsert(Data.CONTENT_URI) 
		            .withValueBackReference(Data.RAW_CONTACT_ID, 0) 
		            .withValue(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE)
		            .withValue(Phone.NUMBER, mobile)
		            .withValue(Phone.TYPE, Phone.TYPE_MOBILE)
		            .build());
		    
		    operationList.add(ContentProviderOperation.newInsert(Data.CONTENT_URI) 
		    		.withValueBackReference(Data.RAW_CONTACT_ID, 0) 
		    		.withValue(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE)
		    		.withValue(Phone.NUMBER, tel)
		    		.withValue(Phone.TYPE, Phone.TYPE_WORK)
		    		.build());
		   
		    
		    operationList.add(ContentProviderOperation.newInsert(Data.CONTENT_URI) 
		            .withValueBackReference(Data.RAW_CONTACT_ID, 0)
		            .withValue(Data.MIMETYPE, Email.CONTENT_ITEM_TYPE)
		            .withValue(Email.DATA, email+"@ex.co.kr")
		            .withValue(Email.TYPE, Email.TYPE_WORK)
		            .build());

		    try{ 
		        ContentProviderResult[] results = mContext.getContentResolver().applyBatch(ContactsContract.AUTHORITY, operationList);
		        Toast.makeText(mContext, names+saveSuccess, Toast.LENGTH_SHORT).show();
		    }catch(Exception e){ 
		        e.printStackTrace(); 
		        Toast.makeText(mContext, names+saveFailed, Toast.LENGTH_SHORT).show();
		    } 
	}
	
}