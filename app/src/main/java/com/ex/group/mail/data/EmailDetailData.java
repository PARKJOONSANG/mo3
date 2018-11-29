package com.ex.group.mail.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.ex.group.mail.util.EmailClientUtil;
import com.skt.pe.common.exception.SKTException;
import com.skt.pe.common.service.XMLData;
import com.skt.pe.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 메일 상세 화면 데이타
 * @author sjsun5318
 *
 */
public class EmailDetailData implements Parcelable {
	private String mailId;
	private String changeKey;
	private String mailTitle;
	private String bodyType;
	private String body;
	private String hasAttachments;
	private String receivedDate;
	private String sendDate;
	private String isRead;
	private String isImg;
	
	private NameMail myInfo;
	private NameMail fromInfo;
	private NameMail[] toList;
	private NameMail[] ccList;
	private NameMail[] bccList;
	
	private List<String> toTempList = new ArrayList<String>();
	private List<String> ccTempList = new ArrayList<String>();
	private List<String> bccTempList = new ArrayList<String>();
	
	private List<String> toIdTempList = new ArrayList<String>();
	private List<String> ccIdTempList = new ArrayList<String>();
	private List<String> bcIdcTempList = new ArrayList<String>();

	private XMLData xml;
	
	public EmailDetailData() {
			this.mailId 				= "메일 ID(URL)";
			this.changeKey 				= "사용하지 않음";
			this.mailTitle 				= "메일 제목";
			this.bodyType 				= "text";
			this.body 					= "bodybodybodybodybodybodybodybodybodybodybodybodybodybodybodybodybodybodybodybodybodybodybodybodybodybodybodybodybodybodybodybody";
			this.hasAttachments 		= "true";
			this.receivedDate 			= "yyyy-MM-ddTHH:mm:ssZ’";
			this.sendDate 				= "yyyy-MM-ddTHH:mm:ssZ’";
			this.isRead 				= "true";
		
			this.myInfo = new NameMail("myInfoName","myInfoEmail");
			this.fromInfo = new NameMail("fromInfoName","fromInfoEmail");
			
			
        	this.toList = new NameMail[2];
        	for(int a = 0 ; a < toList.length ; a ++){
        		toList[a] = new NameMail("toListName","toListEmail");
        	}
        	
        	this.ccList = new NameMail[2];
        	for(int a = 0 ; a < toList.length ; a ++){
        		ccList[a] = new NameMail("ccListName","ccListEmail");
        	}
        	
        	this.bccList = new NameMail[2];
        	for(int a = 0 ; a < toList.length ; a ++){
        		bccList[a] = new NameMail("bccListName","bccListEmail");
        	}        	
        	
	}
	
	/**
	 * 데이타 셋팅
	 * @param xml
	 */
	public EmailDetailData(XMLData xml){
		this.xml = xml;
		try {
			//COMMON_MAILADV_CONTENT 받은값 넘어온다.
			xml.setList("NFMailInfoD");
			
			//메일 아이디 리스트 에서 가져와야한다.
			this.mailTitle 				= StringUtil.isNull(xml.get("title")) 		? "" : xml.get("title");
			this.changeKey 				= "";
			this.bodyType 				= StringUtil.isNull(xml.get("isHtml")) 		? "" : xml.get("isHtml");
			this.body 					= StringUtil.isNull(xml.get("contentBinId"))? "" : xml.get("contentBinId");
			
			this.hasAttachments 		= StringUtil.isNull(xml.get("fileCnt")) 	? "" : xml.get("fileCnt");
			Log.i("Detail", "hasAttachments : "+this.hasAttachments);

			this.receivedDate 			= "";
			this.sendDate 				= StringUtil.isNull(xml.get("sendTime")) 		? "" : xml.get("sendTime");
			this.isRead 				= StringUtil.isNull(xml.get("isRead")) 			? "" : xml.get("isRead");

			xml.setList("NFMailRecipientD");

    		for (int i = 0; i < xml.size(); i++) {
    			//1 수신, 2 참조 3 비밀참조
    			String rcptKind = xml.get(i,"rcptKind");
    			if("1".equals(rcptKind)) {
    				toTempList.add(xml.get(i,"alias"));
    				toIdTempList.add(xml.get(i,"empNo"));
    			}    			
    			if("2".equals(rcptKind)) {
    				ccTempList.add(xml.get(i,"alias"));
    				ccIdTempList.add(xml.get(i,"empNo"));
    			}    			
			}
    		//받은 우편 일때
			this.fromInfo = new NameMail(EmailClientUtil.getNameString(xml.get("senderDesc")), StringUtil.isNull(xml.get("senderEmpNo")) ? "" : xml.get("senderEmpNo"));
			
			if(!toTempList.isEmpty()){
				this.toList = new NameMail[toTempList.size()];
			
				for (int i = 0; i < toList.length; i++) {
					toList[i] = new NameMail(toTempList.get(i),toIdTempList.get(i));
				}				
			}    
			if(!ccTempList.isEmpty()){
				this.ccList = new NameMail[ccTempList.size()];
				for (int i = 0; i < ccList.length; i++) {
					ccList[i] = new NameMail(ccTempList.get(i),ccIdTempList.get(i));
				}
			} 			
		} catch (SKTException e) {
			e.printStackTrace();
		}
		
	}

	public String getIsImg() {
		return isImg;
	}

	public void setIsImg(String isImg) {
		this.isImg = isImg;
	}

	public NameMail getMyInfo() {
		return myInfo;
	}

	public void setMyInfo(NameMail myInfo) {
		this.myInfo = myInfo;
	}

	public NameMail getFromInfo() {
		return fromInfo;
	}

	public void setFromInfo(NameMail fromInfo) {
		this.fromInfo = fromInfo;
	}

	public NameMail[] getToList() {
		return toList;
	}

	public void setToList(NameMail[] toList) {
		this.toList = toList;
	}

	public NameMail[] getCcList() {
		return ccList;
	}

	public void setCcList(NameMail[] ccList) {
		this.ccList = ccList;
	}

	public NameMail[] getBccList() {
		return bccList;
	}

	public void setBccList(NameMail[] bccList) {
		this.bccList = bccList;
	}

	/**
	 * @return the mailId
	 */
	public String getMailId() {
		return mailId;
	}




	/**
	 * @param mailId the mailId to set
	 */
	public void setMailId(String mailId) {
		this.mailId = mailId;
	}




	/**
	 * @return the changeKey
	 */
	public String getChangeKey() {
		return changeKey;
	}




	/**
	 * @param changeKey the changeKey to set
	 */
	public void setChangeKey(String changeKey) {
		this.changeKey = changeKey;
	}




	/**
	 * @return the mailTitle
	 */
	public String getMailTitle() {
		return mailTitle;
	}




	/**
	 * @param mailTitle the mailTitle to set
	 */
	public void setMailTitle(String mailTitle) {
		this.mailTitle = mailTitle;
	}




	/**
	 * @return the bodyType
	 */
	public String getBodyType() {
		return bodyType;
	}




	/**
	 * @param bodyType the bodyType to set
	 */
	public void setBodyType(String bodyType) {
		this.bodyType = bodyType;
	}




	/**
	 * @return the body
	 */
	public String getBody() {
		return body;
	}




	/**
	 * @param body the body to set
	 */
	public void setBody(String body) {
		this.body = body;
	}




	/**
	 * @return the hasAttachments
	 */
	public String getHasAttachments() {
		return hasAttachments;
	}




	/**
	 * @param hasAttachments the hasAttachments to set
	 */
	public void setHasAttachments(String hasAttachments) {
		this.hasAttachments = hasAttachments;
	}




	/**
	 * @return the receivedDate
	 */
	public String getReceivedDate() {
		return receivedDate;
	}




	/**
	 * @param receivedDate the receivedDate to set
	 */
	public void setReceivedDate(String receivedDate) {
		this.receivedDate = receivedDate;
	}




	/**
	 * @return the sendDate
	 */
	public String getSendDate() {
		return sendDate;
	}




	/**
	 * @param sendDate the sendDate to set
	 */
	public void setSendDate(String sendDate) {
		this.sendDate = sendDate;
	}




	/**
	 * @return the isRead
	 */
	public String getIsRead() {
		return isRead;
	}




	/**
	 * @param isRead the isRead to set
	 */
	public void setIsRead(String isRead) {
		this.isRead = isRead;
	}




	/**
	 * @return the xml
	 */
	public XMLData getXml() {
		return xml;
	}




	/**
	 * @param xml the xml to set
	 */
	public void setXml(XMLData xml) {
		this.xml = xml;
	}




	@Override
	public int describeContents() {
		return 0;
	}
	
	/**
	 * writeToParcel 메소드
	 */
	@Override
	public void writeToParcel(Parcel p, int a) {

		p.writeString(mailId);
		p.writeString(changeKey);
		p.writeString(mailTitle);
		p.writeString(bodyType);
		p.writeString(body);
		p.writeString(hasAttachments);
		p.writeString(receivedDate);
		p.writeString(sendDate);
		p.writeString(isRead);
		p.writeString(isImg);
		
	}
	
	/**
	 * Parcelable 만들기
	 */
	public static final Parcelable.Creator<EmailDetailData> CREATOR = new Creator<EmailDetailData>() {
		
		public EmailDetailData createFromParcel(Parcel parcel) {
			EmailDetailData data = new EmailDetailData();
			data.setMailId(parcel.readString());
			data.setChangeKey(parcel.readString());
			data.setMailTitle(parcel.readString());
			data.setBodyType(parcel.readString());
			data.setBody(parcel.readString());
			data.setHasAttachments(parcel.readString());
			data.setReceivedDate(parcel.readString());
			data.setSendDate(parcel.readString());
			data.setIsRead(parcel.readString());
			data.setIsImg(parcel.readString());
			return data;
		}
		
		public EmailDetailData[] newArray(int size) {
			return new EmailDetailData[size];
		}
		
	};
	
	
}

	
	
	