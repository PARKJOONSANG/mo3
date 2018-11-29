package com.ex.group.mail.service;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.ex.group.mail.data.EmailDatabaseHelper;
import com.ex.group.mail.data.EmailMainListData;
import com.ex.group.mail.util.EmailClientUtil;
import com.skt.pe.common.exception.SKTException;
import com.skt.pe.common.service.Controller;
import com.skt.pe.common.service.Parameters;
import com.skt.pe.common.service.XMLData;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * 메일함 Thread
 * @author sjsun5318
 *
 */
public class EmailMainThread extends Thread {

	private Context mContext;
	private Handler mHandler;
	private XMLData xmlData;
	
	private static int running = EmailClientUtil.END_RUNABLE;
	public  boolean		stopFlag = false;
	public static Thread runningThread;
	
	public EmailMainThread(Context context, Handler handler) {
		this.mContext = context;
		this.mHandler = handler;
		runningThread = this;
	}

	/**
	 * stopFlag 설정
	 */
	@Override
	public void interrupt() {
		stopFlag = true;
		super.interrupt();
	}

	/**
	 * Thread 실행중인지 확인
	 * @return
	 */
	public static boolean getRunning() {
		if(running == EmailClientUtil.END_RUNABLE)
			return false;
		else 
			return true;
	}
	
	/**
	 * Thread 실행중 Flag
	 * @param state
	 */
	public static void setRunning(int state) {
		running = state; 
	}
	
	/**
	 * request 처리를 위한 파라미터 셋팅
	 * @param a_szprim
	 * @return
	 * @throws SKTException
	 */
	public XMLData getXmlData(String a_szprim) throws SKTException {

		Parameters params = new Parameters(a_szprim);
		//params.put("companyCd", EmailClientUtil.companyCd);
		//params.put("lang", EmailClientUtil.LANG);
		
		//2014-02-06 JSJ 사원 아이디,이름 추가
		params.put("empId", EmailClientUtil.id);
		params.put("empName", EmailClientUtil.empNm);
		params.put("userID", EmailClientUtil.nedmsID);
				
		Controller controller = new Controller(this.mContext);
		return controller.request(params);
	}
	
	/**
	 * Handler 메세지 셋팅
	 * @param what
	 */
	private void setMessage(int what) {
		Message msg = Message.obtain();
		msg.what = what;
		mHandler.sendMessage(msg);
	}
	/**
	 * Error Handler 메세지 셋팅
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
	 * 현재 시간 가져오기
	 * @return
	 */
	private String getNowDate() {
		DateFormat dateFormat = new SimpleDateFormat("MM.dd a hh:mm");
        Date date = new Date();
        return dateFormat.format(date);
	}
	
	/**
	 * xmlData 리스트 데이타 넣기
	 * @param xmlData
	 * @return
	 */
	public ArrayList<EmailMainListData> setListUI(XMLData xmlData) {
		 ArrayList<EmailMainListData> childDataList = new ArrayList<EmailMainListData>();
		try {
			//받은 편지함-열람, 보낸편지함, 지운편지함, 개인문서, 중요문서
			//default mail함정보 셋팅
			
			childDataList.addAll(defaultBoxInfo());

			int size = childDataList.size();
			//xmlData.setList("NTreeNode");
			XMLData NTN_ChildNodes = xmlData.getChild("NTN_ChildNodes");
			NTN_ChildNodes.setList("NTreeNode");
			
			for(int i=0; i<NTN_ChildNodes.size(); i++) {
				EmailMainListData dataList = new EmailMainListData();
	        	dataList.setMdn(EmailClientUtil.id);
	        	dataList.setCompanyCd(EmailClientUtil.companyCd);

				dataList.setBoxType("P");
				dataList.setBoxId((size+i)+"");
				dataList.setBoxName(NTN_ChildNodes.get(i,"NTN_Title"));//
				dataList.setTotalCnt("0");
				dataList.setUnreadCnt("0"); //받은편지함 안읽은 건수
				dataList.setParentBoxType("P");
				dataList.setBoxOrder((size+i)+"");		
				childDataList.add(dataList);
			}

			xmlData.setList("otherinfo");
			childDataList.get(0).setUnreadCnt(xmlData.get("unreadMailCnt"));
								
		} catch(SKTException e) {
		} 
		return childDataList;
	}	
	
	
	/**
	 * Therad 실행
	 */
	@Override
	public void run() {
		setRunning(EmailClientUtil.START_RUNABLE);
		setMessage(EmailClientUtil.START_RUNABLE);

		EmailDatabaseHelper mHelper = new EmailDatabaseHelper(mContext);

		try {
			if(stopFlag) {
				stopFlag = false;
				throw new SKTException("xxxxxxxxxxx main 1");
			}
							
			this.xmlData = getXmlData(EmailClientUtil.COMMON_MAILADV_PRIVATEBOX);
			
			if(stopFlag) {
				stopFlag = false;
				throw new SKTException("xxxxxxxxxxx main 2");
			}
			
			
			if(stopFlag) {
				stopFlag = false;
				throw new SKTException("xxxxxxxxxxx main 3");
			}
			if(xmlData != null){
				//메일함 저장(폴더)			
				mHelper.insertMainTable(setListUI(xmlData), this);
			}
						
			if(stopFlag) {
				stopFlag = false;
				throw new SKTException("xxxxxxxxxxx main 4");
			}
			//메일 update 테이블 넣기 mdn(로그인id), companyCd, nowDate			
			mHelper.insertBoxUpdate(EmailClientUtil.id, EmailClientUtil.companyCd, getNowDate());
			
			if(stopFlag) {
				stopFlag = false;
				throw new SKTException("xxxxxxxxxxx main 5");
			}
			
			setRunning(EmailClientUtil.END_RUNABLE);
			setMessage(EmailClientUtil.END_RUNABLE);
		} catch (SKTException e) {
			setMessage(EmailClientUtil.END_RUNABLE);
			setRunning(EmailClientUtil.END_RUNABLE);
			setErrorMessage(EmailClientUtil.SKT_EXCEPTION, e);
		} catch (Exception e) {
			setMessage(EmailClientUtil.END_RUNABLE);
			setRunning(EmailClientUtil.END_RUNABLE);
			setErrorMessage(EmailClientUtil.NO_TABLE_ERROR , new SKTException(e));
		} finally {
			mHelper.close();
		}
		
	}
	
	private ArrayList<EmailMainListData> defaultBoxInfo(){
		ArrayList<EmailMainListData> returnBoxInfo =  new ArrayList<EmailMainListData>();
		
		for (int i = 0; i < 4; i++) {
			EmailMainListData dataList = new EmailMainListData();
        	dataList.setMdn(EmailClientUtil.id);
        	dataList.setCompanyCd(EmailClientUtil.companyCd);
			switch (i) {
			case 0:
				dataList.setBoxType("I1");
				dataList.setBoxId(i+"");
				dataList.setBoxName(EmailClientUtil.RECEIVE_I1_BOX_NAME);
				dataList.setTotalCnt("0");
				dataList.setUnreadCnt("0"); //받은편지함-미열람 안읽은 건수
				dataList.setParentBoxType("I1");
				dataList.setBoxOrder(i+"");
				break;
			case 1:
				dataList.setBoxType("I2");
				dataList.setBoxId(i+"");
				dataList.setBoxName(EmailClientUtil.RECEIVE_I2_BOX_NAME);
				dataList.setTotalCnt("0");
				dataList.setUnreadCnt("0"); //받은편지함 안읽은 건수
				dataList.setParentBoxType("I2");
				dataList.setBoxOrder(i+"");				
				break;
			case 2:
				dataList.setBoxType("S");
				dataList.setBoxId(i+"");
				dataList.setBoxName(EmailClientUtil.SEND_BOX_NAME);
				dataList.setTotalCnt("0");
				dataList.setUnreadCnt("0"); //보낸편지함 안읽은 건수
				dataList.setParentBoxType("S");
				dataList.setBoxOrder(i+"");				
				break;
			case 3:
				dataList.setBoxType("D");
				dataList.setBoxId(i+"");
				dataList.setBoxName(EmailClientUtil.DELETE_BOX_NAME);
				dataList.setTotalCnt("0");
				dataList.setUnreadCnt("0"); //지운 편지함 안읽은 건수
				dataList.setParentBoxType("D");
				dataList.setBoxOrder(i+"");				
				break;				
			}
			returnBoxInfo.add(dataList);
		}
		return returnBoxInfo;
	}
}
