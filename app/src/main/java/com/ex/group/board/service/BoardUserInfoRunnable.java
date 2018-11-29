package com.ex.group.board.service;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.ex.group.board.data.UserInfo;
import com.skt.pe.common.conf.Environ;
import com.skt.pe.common.exception.SKTException;
import com.skt.pe.common.service.Controller;
import com.skt.pe.common.service.Parameters;
import com.skt.pe.common.service.XMLData;

//2014-03-04 JSJ 로그인 사원정보 받기.
public class BoardUserInfoRunnable implements Runnable {


	private Context mContext;
	private String mdn;
	private String companyCd;
	private XMLData xmlData;
	//2014-02-10 JSJ address 에 사용자 이름이 저장되던 내용을 empNm 으로 변경
	private String empNm; //로그인 사용자 이름
	private String emailAddress;
	private Handler mHandler;
	
	public BoardUserInfoRunnable(Context context , Handler handler , String mdn , String companyCd) {
		this.mContext = context;
		this.mdn = mdn;
		this.companyCd = companyCd;
		this.mHandler = handler;
	}
	/**
	 * request 처리를 위한 파라민터 셋팅
	 * @param context
	 * @param primitive
	 * @return
	 * @throws SKTException
	 */
	private XMLData getXmlData(Context context , String primitive) throws SKTException {
		XMLData xml = null;
		Log.d("","primitive  = " + primitive);
		Parameters params = new Parameters(primitive);
		Controller controller = new Controller(context);
		//2014-02-06 JSJ 사원 아이디,이름 추가
		params.put("empId", UserInfo.id);
		params.put("empName", UserInfo.empNm);
		params.put("companyCd", companyCd);
		params.put("lang", "ko");
				
		xml = controller.request(params, false, Environ.FILE_SERVICE);
		
		return xml;
	}
	
	/**
	 * 에러 핸들러로 보내기
	 * @param what
	 * @param e
	 */
	private void setErrorMessage(int what , SKTException e) {
		Message msg = Message.obtain();
		msg.what = what;
		msg.obj = e;
		mHandler.sendMessage(msg);
	}
	
	/**
	 * Runnable 실행
	 */
	@Override
	public void run() {
		try {
			this.xmlData 	= getXmlData(this.mContext,"COMMON_MAILNEW_GETADDRESS");
			UserInfo.emailAddress = xmlData.get("emailAddress");
			UserInfo.empNm = xmlData.get("empNm");
			
			Log.d("run", "UserInfo companyCd : "+ UserInfo.companyCd);
			Log.d("run", "UserInfo emailAddress : "+ UserInfo.emailAddress);
			Log.d("run", "UserInfo emailAddress_id : "+ UserInfo.emailAddress_id);
			Log.d("run", "UserInfo empNm : "+ UserInfo.empNm);
			Log.d("run", "UserInfo id : "+ UserInfo.id);
			Log.d("run", "UserInfo mdn : "+ UserInfo.mdn);
			
			Message msg = Message.obtain();
			msg.what = 0;
			msg.obj = empNm;
			mHandler.sendMessage(msg);
			
		} catch (SKTException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

}
