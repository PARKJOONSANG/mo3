package com.ex.group.mail.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.ex.group.mail.util.EmailClientUtil;

public class Userdata implements Parcelable {

	private String skinUrl;
	private String contentUrl;
	private String reply;
	private String secret;
	private String answer;
	private String check;
	private String mailKind;
	private String msgId;
	private String folderName;
	private String senderID;
	
	public Userdata(){}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel p, int a) {
		p.writeString(skinUrl);
		p.writeString(contentUrl);
		p.writeString(reply);
		p.writeString(secret);
		p.writeString(answer);
		p.writeString(check);
		p.writeString(mailKind);
		p.writeString(msgId);
		p.writeString(folderName);
		p.writeString(senderID);
		
	}
	
	public static final Parcelable.Creator<Userdata> CREATOR = new Creator<Userdata>(){
		
		public Userdata createFromParcel(Parcel parcel){
			Userdata data = new Userdata();
			
			return data;
		}

		@Override
		public Userdata[] newArray(int size) {
			// TODO Auto-generated method stub
			return new Userdata[size];
		}
	};

	public String getSkinUrl() {
		return skinUrl;
	}
	public void setSkinUrl(String skinUrl) {
		this.skinUrl = skinUrl;
	}
	public String getContentUrl() {
		return contentUrl;
	}
	public void setContentUrl(String contentUrl) {
		this.contentUrl = contentUrl;
	}
	public String getReply() {
		return reply;
	}
	public void setReply(String reply) {
		this.reply = reply;
	}
	public String getSecret() {
		return secret;
	}
	public void setSecret(String secret) {
		this.secret = secret;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public String getCheck() {
		return check;
	}
	public void setCheck(String check) {
		this.check = check;
	}
	public String getMailKind() {
		return mailKind;
	}
	public void setMailKind(String mailKind) {
		this.mailKind = mailKind;
	}
	public String getMsgId() {
		return msgId;
	}
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	public String getFolderName() {
		return folderName;
	}
	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}
	public String getSenderID() {
		return senderID;
	}
	public void setSenderID(String senderID) {
		this.senderID = senderID;
	}
	
	public void setEncryption() {
		skinUrl     = EmailClientUtil.seedEncrypt(skinUrl);
		contentUrl  = EmailClientUtil.seedEncrypt(contentUrl);
		reply       = EmailClientUtil.seedEncrypt(reply);
		secret      = EmailClientUtil.seedEncrypt(secret);
		answer      = EmailClientUtil.seedEncrypt(answer);
		check       = EmailClientUtil.seedEncrypt(check);
		mailKind    = EmailClientUtil.seedEncrypt(mailKind);
		msgId       = EmailClientUtil.seedEncrypt(msgId);
		folderName  = EmailClientUtil.seedEncrypt(folderName);
		senderID    = EmailClientUtil.seedEncrypt(senderID);
	}
	
	public void setDecryption() {
		skinUrl     = EmailClientUtil.seedDecrypt(skinUrl);
		contentUrl  = EmailClientUtil.seedDecrypt(contentUrl);
		reply       = EmailClientUtil.seedDecrypt(reply);
		secret      = EmailClientUtil.seedDecrypt(secret);
		answer      = EmailClientUtil.seedDecrypt(answer);
		check       = EmailClientUtil.seedDecrypt(check);
		mailKind    = EmailClientUtil.seedDecrypt(mailKind);
		msgId       = EmailClientUtil.seedDecrypt(msgId);
		folderName  = EmailClientUtil.seedDecrypt(folderName);
		senderID    = EmailClientUtil.seedDecrypt(senderID);
	}
}
