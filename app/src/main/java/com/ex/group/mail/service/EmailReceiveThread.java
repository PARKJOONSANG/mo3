package com.ex.group.mail.service;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.ex.group.mail.data.EmailDatabaseHelper;
import com.ex.group.mail.data.EmailMainListData;
import com.ex.group.mail.data.EmailReceiveListData;
import com.ex.group.mail.data.ExceptionWrapper;
import com.ex.group.mail.util.EmailClientUtil;
import com.skt.pe.common.conf.Environ;
import com.skt.pe.common.exception.SKTException;
import com.skt.pe.common.service.Controller;
import com.skt.pe.common.service.Parameters;
import com.skt.pe.common.service.XMLData;
import com.skt.pe.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 메일리스트 Thread
 * @author sjsun5318
 *
 */
public class EmailReceiveThread extends Thread {
	
	private Context context;
	private XMLData xmlData;
	private EmailMainListData mainData;
	//private String				update;
	private String id = null;	//선택한 메일함의 폴더 아이디
	private int	    			pageNumber;  
	//private int 				totalPageCnt;
	
	private Handler handler;
	private int 				type;
	public  boolean				stopFlag = false;
	public static Thread runningThread;
	
	public static final int TYPE_1 = 1;
	public static final int TYPE_2 = 2;
	
	private static int runningTemp = EmailClientUtil.END_RUNABLE;
	
	private static Map<String,Integer> running = new HashMap<String,Integer>();

	private final int TOTAL       = 0;
	private final int UNREAD 	  = 1;
	private final int FIRSTID     = 2;
	private final int LASTID      = 3;
	private final int BRM_DOC_HOST= 4;
	private final int MAIN_BRMHOST= 5;
	private final int TOTAL_SIZE  = 6;
		
	private final int CHECK       = 0;
	private final int READMAIL    = 1;
	private final int BLINK       = 2;
	private final int ATTACH      = 3;
	private final int TITLE       = 4;
	private final int SENDER   	  = 5;
	private final int RECIEVEDATE = 6;
	private final int SIZE 		  = 7;
	private final int IMPORTANT   = 8;
	private final int WORK 		  = 9;
	
	private final int P_CHECK      = 0;
	private final int P_RECVNUM    = 1;
	private final int P_ATTACH     = 2;
	private final int P_TITLE      = 3;
	private final int P_SENDER     = 4;
	private final int P_SAVEDATE   = 5;
	private final int P_SIZE 	   = 6;
	private final int P_BOXNAME    = 7;
	
		
	public EmailReceiveThread(Context context, Handler handler, EmailMainListData mainData, int page, int type) {
		this.context = context;
		this.handler = handler;
		this.mainData = mainData;
		this.pageNumber = page;
		//this.totalPageCnt = 1;
		if(mainData != null) {			
			this.id = mainData.getId();
		}
		this.type = type;

		runningThread = this;
	}
	
	/**
	 * stop 체크
	 */
	@Override
	public void interrupt() {
		stopFlag = true;
		super.interrupt();
	}
	
	/**
	 * 실행중인 Therad 값 가져오기
	 * @param seq
	 * @return
	 */
	public static int getRunningVal(String seq) {
		if(seq == null) {
			return runningTemp;
		} else {
			return EmailClientUtil.START_RUNABLE;
		}
	}

	/**
	 * seq 가 같은 Therad 가 실행중인지 확인
	 * @param seq
	 * @return
	 */
	public static boolean getRunning(String seq) {
		if(seq == null) {
			if(runningTemp == EmailClientUtil.START_RUNABLE) {
				return true;
			} else {
				return false;
			}
		} else if(running.get(seq) != null && running.get(seq).intValue() == EmailClientUtil.START_RUNABLE) {
			return true;
		} else {
			return false;
		} 
	}

	/**
	 * Thread 가 실행중인지 확인
	 * @return
	 */
	public static boolean getRunning() {
		if(runningTemp == EmailClientUtil.START_RUNABLE) {
			return true;
		} else {
			Iterator<String> it = running.keySet().iterator();
			while(it.hasNext()) {
				String seq = it.next();
				if(running.get(seq)!=null && running.get(seq).intValue()==EmailClientUtil.START_RUNABLE) {
					return true;
				}
			}
		}
		
		return false;
	}

	/**
	 * seq 값으로 Therad 실행시키기
	 * @param seq
	 * @param state
	 */
	public static void setRunning(String seq, int state) {
		if(seq == null) {
			runningTemp = state;
		} else {
			runningTemp = state;
			running.put(seq, state);
		}
	}
	
	public static void setRunning(int state) {
		runningTemp = state;
		running = new HashMap<String,Integer>();
	}
	
	/**
	 * request 보낼 parameters 셋팅
	 * @param context
	 * @param mainData
	 * @throws SKTException
	 */
	private XMLData getXmlData(Context context, EmailMainListData mainData) throws SKTException {
		XMLData xml = null;
		//EmailDatabaseHelper helper = new EmailDatabaseHelper(context);
		Parameters params;
		Controller controller = new Controller(context);
		
		if(mainData != null) {
			id = mainData.getId();
		} 
		
		
		if("P".equals(mainData.getBoxType())) {
			params = new Parameters(EmailClientUtil.COMMON_MAILADV_PRIVATELIST);
			params.put("mailFolderName", mainData.getBoxName());
		}else{
			params = new Parameters(EmailClientUtil.COMMON_MAILADV_LIST);
		}
    	
    	
		params.put("userID", EmailClientUtil.nedmsID);
    	String mailFolderKind = EmailClientUtil.getMailFolderKind(mainData.getBoxType());
    	params.put("mailFolderKind", mailFolderKind); 
    	params.put("paging", Integer.toString(pageNumber));
    	//params.put("list_num","15");
		
		//helper.close();
		xml = controller.request(params, false, Environ.FILE_SERVICE);
		
		return xml;
	}
	
	/**
	 * 받은 xmlData 값넣기(db insert,delete위해)
	 * @param xmlData
	 * @param mainData
	 * @return
	 */
	private ArrayList<EmailReceiveListData> setData(XMLData xmlData, EmailMainListData mainData) {
				
		ArrayList<EmailReceiveListData> listData = new ArrayList<EmailReceiveListData>();
		try {

			xmlData.setList("otherinfo");
			
			String total  	  = xmlData.get("total"); 		 //총페이지 수
			String unread 	  = xmlData.get("unread");       //미열람 총 갯수
			String total_size = xmlData.get("TOTAL_SIZE");   //총 메일건수
			
			int int_total_size = 0;
			int int_unread     = 0;
			if(total_size!=null && !"".equals(total_size)) int_total_size = Integer.parseInt(total_size);
			if(unread!=null && !"".equals(unread)) int_unread = Integer.parseInt(unread);
			
			xmlData.setList("record");
			
			String boxType = mainData.getBoxType(); //"I1", "I2"
			//if("I1".equals(mainData.getBoxType())|| "I2".equals(mainData.getBoxType())){
				//listData.addAll(getBoxTypeReceiveData(xmlData, mainData.getBoxType()));
			//}else{
				for(int i=0; i<xmlData.size(); i++) {
					EmailReceiveListData data = new EmailReceiveListData();
					
					data.setMdn(mainData.getMdn()); //로그인 아이디
					data.setCompanyCd(mainData.getCompanyCd()); //EX
					data.setBoxType(mainData.getBoxType()); //I2
					data.setParentBoxType(mainData.getParentBoxType()); //I2
					data.setBoxId(mainData.getBoxId()); //1 폴더순서
					data.setBoxChangKey(mainData.getBoxChangeKey()); //?
					data.setBoxNmae(mainData.getBoxName()); //받은편지함-열람
					data.setUpdateDate(mainData.getUpdatedate());
					if("I1".equals(mainData.getBoxType())){
						//미열람 총페이지수? 미열람 총갯수 unread 갯수 /5 = 총페이지수														
						if(int_unread>15){
							//data.setTotalCnt((int_unread/5)+"");
							data.setTotalCnt((int_unread/14)+"");
						}else{
							data.setTotalCnt("1");	
						}
						
					}else if("I2".equals(mainData.getBoxType())){
						//열람 총페이지수?   열람 총갯수 total_size - unread /5 = 총페이지수							
						//data.setTotalCnt(((int_total_size - int_unread)/5)+"");
						data.setTotalCnt(((int_total_size - int_unread)/14)+"");
					}else{
						data.setTotalCnt(StringUtil.isNull(total) ? "" : total);
					}
					data.setMailChangeKey(""); //필요한지 체크
					
					XMLData childXmlData = xmlData.getChild(i);
					childXmlData.setList("userdata");				
					data.setMailId(EmailClientUtil.setValue(childXmlData.get("msgId"))); // mailId <= msgId
					
					/////////////////////////////////////////////////////////////////////////
					//mailType setting
					StringBuffer mailType = new StringBuffer();
					//긴급,답장요구,비밀편지,수신자숨김,업무구분(업무용,개인용),
					if("1".equals(EmailClientUtil.setValue(childXmlData.get("isFast")))) mailType.append("isFast;");
					if("1".equals(EmailClientUtil.setValue(childXmlData.get("reply")))) mailType.append("isReply;");
					if("1".equals(EmailClientUtil.setValue(childXmlData.get("secret")))) mailType.append("isSecret;");						
					/////////////////////////////////////////////////////////////////////////
					
					childXmlData.setList("field");
					
					if("1".equals(StringUtil.isNull(childXmlData.getContent(WORK)))) mailType.append("isWork;");
											
					if("P".equals(boxType)){
						data.setMailSubject(StringUtil.isNull(childXmlData.getContent(P_TITLE)) ? "제목없음" : childXmlData.getContent(P_TITLE));
						data.setHasAttachments(StringUtil.isNull(childXmlData.getContent(P_ATTACH)) ? "" : childXmlData.getContent(P_ATTACH)); //true, false // 0첨부있음 1첨부없음	첨부없으면 0 있으면 11 이상하게 넘어온다.
						data.setReceivedDate(StringUtil.isNull(childXmlData.getContent(P_SAVEDATE)) ? "" : childXmlData.getContent(P_SAVEDATE));
						data.setSendDate("");	// 보낸날짜 보낸편지함에서 확인해볼것							
						data.setUnreadCnt(EmailClientUtil.setValue(total_size));
						data.setFromInfo(StringUtil.isNull(childXmlData.getContent(P_SENDER)) ? "" : replaceValue(childXmlData.getContent(P_SENDER)));
					}else{
						data.setMailSubject(StringUtil.isNull(childXmlData.getContent(TITLE)) ? "제목없음" : childXmlData.getContent(TITLE));
						data.setHasAttachments(StringUtil.isNull(childXmlData.getContent(ATTACH)) ? "" : childXmlData.getContent(ATTACH)); //true, false // 0첨부있음 1첨부없음	첨부없으면 0 있으면 11 이상하게 넘어온다.
						data.setReceivedDate(StringUtil.isNull(childXmlData.getContent(RECIEVEDATE)) ? "" : childXmlData.getContent(RECIEVEDATE));
						data.setSendDate("");	// 보낸날짜 보낸편지함에서 확인해볼것
													 
						if("I1".equals(boxType)){
							data.setIsRead("0"); // true, false // 1열람, 0미열람
						}else if("I2".equals(boxType)){
							data.setIsRead("1"); // true, false // 1열람, 0미열람
						}else{
							data.setIsRead(StringUtil.isNull(childXmlData.getContent(READMAIL)) ? "" : childXmlData.getContent(READMAIL)); // true, false // 1열람, 0미열람
						}
						
						data.setMailType(mailType.toString()); 
						data.setUnreadCnt(EmailClientUtil.setValue(total_size));
						data.setFromInfo(StringUtil.isNull(childXmlData.getContent(SENDER)) ? "" : replaceValue(childXmlData.getContent(SENDER)));
					}

					if("S".equals(boxType)) data.setToList(setToList(childXmlData.getContent(SENDER)));

					listData.add(data);
				}					
			//}
		} catch (SKTException e) {
			//e.alert(context);			
		}
        return listData;
	}
	//받은편지함 미열람, 열람 데이타 처리
	/*
	private ArrayList<EmailReceiveListData> getBoxTypeReceiveData(XMLData xmlData, String boxType){
		ArrayList<EmailReceiveListData> returnListData = new ArrayList<EmailReceiveListData>();
		
		if("I1".equals(boxType)){//미열람
			
		}else{//열람
			
		}
		return returnListData;
	}*/
	
	//리스트 이름;이름 형식으로 변경
	private String setToList(String param){
		String returnValue = "";
		if(param.indexOf(",")>-1){			
			String[] splitString = param.split(",");
			for (int i = 0; i < splitString.length; i++) {
				if(i==0) {
					if(splitString[i].indexOf("/")>-1){				
						returnValue = splitString[i].substring(0, splitString[i].indexOf("/"));										
					}else{
						returnValue = splitString[i];				
					}
				}else{
					if(splitString[i].indexOf("/")>-1){				
						returnValue = returnValue+";"+splitString[i].substring(0, splitString[i].indexOf("/"));										
					}else{
						returnValue = returnValue+";"+splitString[i];				
					}					
				}				
			}
		}else{			
			if(param.indexOf("/")>-1){				
				returnValue = param.substring(0, param.indexOf("/"));				
			}else{
				returnValue = replaceValue(param);				
			}
		}
		if(StringUtil.isNull(returnValue)) returnValue ="정보없음";
		return returnValue;
	}

	private String replaceValue(String param){
		String returnValue = "";
		int leftTag  = param.indexOf("<");
		int rightTag = param.indexOf(">");
		if(leftTag>-1 && rightTag>-1 && leftTag < rightTag){
			returnValue = param.substring(0,leftTag);
		}else{
			returnValue = param;
		}
		return returnValue;
	}
	/**
	 * BroadCast 보낼 값 셋팅
	 * @param id
	 * @param type
	 * @param what
	 */
	private void setBroadCast(String id, int type, int what, String cnt) {
		Intent intent = new Intent("EMAIL_CLIENT");
		intent.putExtra("SEQ"    , id);
		intent.putExtra("TYPE"   , type);
		intent.putExtra("COMMAND", what);
		intent.putExtra("CNT", cnt);
		context.sendBroadcast(intent);
	}

	/**
	 * BroadCast로 에러메세지 셋팅
	 * @param id
	 * @param type
	 * @param what
	 * @param e
	 */
	private void setErrorBroadCast(String id, int type, int what, SKTException e) {
		Intent intent = new Intent("EMAIL_CLIENT");
		intent.putExtra("SEQ"    , id);
		intent.putExtra("TYPE"   , type);
		intent.putExtra("COMMAND", what);
		intent.putExtra("EXCEPTION", new ExceptionWrapper(e));
		context.sendBroadcast(intent);
	}

	/**
	 * Handler 로 보낼 Message 셋팅
	 * @param type
	 * @param what
	 * @param items
	 */
	private void setMessage(int type, int what, ArrayList<EmailReceiveListData> items) {
		Message msg = Message.obtain();
		msg.what = what;
		msg.arg1 = type;
		msg.obj = items;
		handler.sendMessage(msg);
	}
	
	/**
	 * Handler 로 보낼 Error Message 셋팅
	 * @param type
	 * @param what
	 * @param e
	 */
	private void setErrorMessage(int type, int what , SKTException e) {
		Message msg = Message.obtain();
		msg.what = what;
		msg.arg1 = type;
		msg.obj = e;
		handler.sendMessage(msg);
	}

	/**
	 * Therad 실행
	 */
	@Override
	public void run() {
		boolean uiFlag = type==TYPE_2 ? true : false;
		
		setRunning(id, EmailClientUtil.START_RUNABLE);				
		setBroadCast(id, type, EmailClientUtil.START_RUNABLE, "");

		EmailDatabaseHelper helper = new EmailDatabaseHelper(context);

		try {
			if(stopFlag)
				throw new SKTException("xxxxxxxxxxx 1");
			
			this.xmlData = getXmlData(this.context, mainData);

			if(stopFlag)
				throw new SKTException("xxxxxxxxxxx 2");
			if(id == null) {				
				EmailMainListData dataList = new EmailMainListData();
				dataList.setMdn(EmailClientUtil.id);
				dataList.setCompanyCd(EmailClientUtil.companyCd);
	        	dataList.setBoxType(StringUtil.isNull(xmlData.get("mailBoxType")) ? "" : xmlData.get("mailBoxType"));
	        	dataList.setBoxId("");
	        	dataList.setBoxChangeKey("");
	        	dataList.setBoxName("받은편지함");
	        	dataList.setTotalCnt(StringUtil.isNull(xmlData.get("totalPageCnt")) ? "" : xmlData.get("totalPageCnt"));
	        	dataList.setUnreadCnt("");
	        	dataList.setParentBoxType(StringUtil.isNull(xmlData.get("mailBoxType")) ? "" : xmlData.get("mailBoxType"));
	        	dataList.setUpdatedate(EmailDatabaseHelper.getNowDate());
	        	dataList.setBoxOrder("0");
	        	helper.insertMainTable(dataList);
	        	
				if(stopFlag)
					throw new SKTException("xxxxxxxxxxx 3");

	        	id = helper.getMainTableString(dataList);
	        	
				if(stopFlag)
					throw new SKTException("xxxxxxxxxxx 4");

	        	mainData = dataList;
	        	setRunning(id, EmailClientUtil.START_RUNABLE);
			}
			
			if(stopFlag)
				throw new SKTException("xxxxxxxxxxx 5");

			ArrayList<EmailReceiveListData> list = setData(xmlData, mainData);
			
			if(stopFlag)
				throw new SKTException("xxxxxxxxxxx 6");
			
			runningThread.isInterrupted();

			if(type == TYPE_1) {			
				setMessage(type, EmailClientUtil.START_RUNABLE, list);
				uiFlag = true;
				
				if(stopFlag)
					throw new SKTException("xxxxxxxxxxx 7");
				
				helper.createTable(id);
				
				if(stopFlag)
					throw new SKTException("xxxxxxxxxxx 8");
				
				helper.insertReceiveTable(list, id, this);
				
				if(stopFlag)
					throw new SKTException("xxxxxxxxxxx 9");
			} else if(type == TYPE_2) {				
				if(pageNumber > 1) {
					if(stopFlag)
						throw new SKTException("xxxxxxxxxxx 10");
					helper.selectInsertReceiveTable(list, id, this);
					
					if(stopFlag)
						throw new SKTException("xxxxxxxxxxx 11");
				} else {
					if(stopFlag)
						throw new SKTException("xxxxxxxxxxx 12");
					helper.deleteInsertReceiveTable(list, id, this);					
					if(stopFlag)
						throw new SKTException("xxxxxxxxxxx 13");
				}
			}

			setRunning(id, EmailClientUtil.END_RUNABLE);
			setBroadCast(id, type, EmailClientUtil.END_RUNABLE, list.size() > 0 ? list.get(0).getUnreadCnt() : "0");	
		} catch (SKTException e) {
			if(!uiFlag) {
				setErrorMessage(type, EmailClientUtil.SKT_EXCEPTION, e);
			}
			setRunning(id, EmailClientUtil.END_RUNABLE);
			setErrorBroadCast(id, type, EmailClientUtil.SKT_EXCEPTION , e);
		} catch (Exception e) {
			if(!uiFlag) {
				setErrorMessage(type, EmailClientUtil.SKT_EXCEPTION, new SKTException(e));
			}
			setRunning(id, EmailClientUtil.END_RUNABLE);
			setErrorBroadCast(id, type, EmailClientUtil.NO_TABLE_ERROR , new SKTException(e));
		} finally {
			helper.close();
		}	
	}
 		
}
