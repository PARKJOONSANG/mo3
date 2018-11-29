package com.ex.group.mail.data;


import android.os.Parcel;
import android.os.Parcelable;

import com.ex.group.mail.util.EmailClientUtil;

/**
 * 메일리스트 데이타
 * @author sjsun5318
 *
 */
public class EmailReceiveListData implements Parcelable {
		
		private String mdn;
		private String companyCd;
		private String boxType;
		private String parentBoxType;
		private String boxId;
		private String boxChangKey;
		private String boxNmae;			//폴더 이름
		private String totalCnt;
		private String updateDate;
		private String mailId;			//mailId <= msgId
		private String mailChangeKey;
		private String mailSubject;
		private String hasAttachments;
		private String receivedDate;
		private String sendDate;
		private String isRead;
		private String fromInfo;
		private String toList;
		private boolean	isDel = false;
		private String mailType;
		private String unreadCnt;

		public EmailReceiveListData(){}
		
		@Override
		public int describeContents() {
			return 0;
		}
		

		@Override
		public void writeToParcel(Parcel p, int a) {

			p.writeString(mdn);
			p.writeString(companyCd);
			p.writeString(boxType);
			p.writeString(parentBoxType);
			p.writeString(boxId);
			p.writeString(boxChangKey);
			p.writeString(boxNmae);
			p.writeString(totalCnt);
			p.writeString(updateDate);
			p.writeString(mailId);
			p.writeString(mailChangeKey);
			p.writeString(mailSubject);
			p.writeString(hasAttachments);
			p.writeString(receivedDate);
			p.writeString(sendDate);
			p.writeString(isRead);
			p.writeString(fromInfo);
			p.writeString(toList);
			p.writeString(mailType);
			p.writeString(unreadCnt);		
			
		}
		
		public static final Parcelable.Creator<EmailReceiveListData> CREATOR = new Creator<EmailReceiveListData>() {
			
			public EmailReceiveListData createFromParcel(Parcel parcel) {
				EmailReceiveListData data = new EmailReceiveListData();
				data.setMdn(parcel.readString());
				data.setCompanyCd(parcel.readString());
				data.setBoxType(parcel.readString());
				data.setParentBoxType(parcel.readString());
				data.setBoxId(parcel.readString());
				data.setBoxChangKey(parcel.readString());
				data.setBoxNmae(parcel.readString());
				data.setTotalCnt(parcel.readString());
				data.setUpdateDate(parcel.readString());
				data.setMailId(parcel.readString());
				data.setMailChangeKey(parcel.readString());
				data.setMailSubject(parcel.readString());
				data.setHasAttachments(parcel.readString());
				data.setReceivedDate(parcel.readString());
				data.setSendDate(parcel.readString());
				data.setIsRead(parcel.readString());
				data.setFromInfo(parcel.readString());
				data.setToList(parcel.readString());
				data.setMailType(parcel.readString());
				data.setUnreadCnt(parcel.readString());
								
				return data;
			}
			
			public EmailReceiveListData[] newArray(int size) {
				return new EmailReceiveListData[size];
			}
			
		};
		
		
		
		
		/**
		 * @return the unreadCnt
		 */
		public String getUnreadCnt() {
			return unreadCnt;
		}

		/**
		 * @param unreadCnt the unreadCnt to set
		 */
		public void setUnreadCnt(String unreadCnt) {
			this.unreadCnt = unreadCnt;
		}

		/**
		 * @return the mailType
		 */
		public String getMailType() {
			return mailType;
		}

		/**
		 * @param mailType the mailType to set
		 */
		public void setMailType(String mailType) {
			this.mailType = mailType;
		}

		public boolean isDel() {
			return isDel;
		}

		public void setDel(boolean isDel) {
			this.isDel = isDel;
		}

		public String getParentBoxType() {
			return parentBoxType;
		}

		public void setParentBoxType(String parentBoxType) {
			this.parentBoxType = parentBoxType;
		}

		public String getBoxNmae() {
			return boxNmae;
		}

		public void setBoxNmae(String boxNmae) {
			this.boxNmae = boxNmae;
		}

		public String getUpdateDate() {
			return updateDate;
		}

		public void setUpdateDate(String updateDate) {
			this.updateDate = updateDate;
		}

		public String getFromInfo() {
			return fromInfo;
		}

		public void setFromInfo(String fromInfo) {
			this.fromInfo = fromInfo;
		}

		public String getToList() {
			return toList;
		}

		public void setToList(String toList) {
			this.toList = toList;
		}

		public String getMdn() {
			return mdn;
		}

		public void setMdn(String mdn) {
			this.mdn = mdn;
		}

		public String getTotalCnt() {
			return totalCnt;
		}

		public void setTotalCnt(String totalCnt) {
			this.totalCnt = totalCnt;
		}

		public String getBoxId() {
			return boxId;
		}
		public void setBoxId(String boxId) {
			this.boxId = boxId;
		}
		public String getBoxChangKey() {
			return boxChangKey;
		}
		public void setBoxChangKey(String boxChangKey) {
			this.boxChangKey = boxChangKey;
		}
		public String getBoxType() {
			return boxType;
		}
		public void setBoxType(String boxType) {
			this.boxType = boxType;
		}
		
		public String getCompanyCd() {
			return companyCd;
		}
		public void setCompanyCd(String companyCd) {
			this.companyCd = companyCd;
		}
		public String getMailId() {
			return mailId;
		}
		public void setMailId(String mailId) {
			this.mailId = mailId;
		}
		public String getMailChangeKey() {
			return mailChangeKey;
		}
		public void setMailChangeKey(String mailChangeKey) {
			this.mailChangeKey = mailChangeKey;
		}
		
		public String getMailSubject() {
			return mailSubject;
		}

		public void setMailSubject(String mailSubject) {
			this.mailSubject = mailSubject;
		}

		public String getHasAttachments() {
			return hasAttachments;
		}
		public void setHasAttachments(String hasAttachments) {
			this.hasAttachments = hasAttachments;
		}
		public String getReceivedDate() {
			return receivedDate;
		}
		public void setReceivedDate(String receivedDate) {
			this.receivedDate = receivedDate;
		}
		public String getSendDate() {
			return sendDate;
		}
		public void setSendDate(String sendDate) {
			this.sendDate = sendDate;
		}
		public String getIsRead() {
			return isRead;
		}
		public void setIsRead(String isRead) {
			this.isRead = isRead;
		}
				
		public void setEncryption() {
//			id 				= EmailClientUtil.seedEncrypt(id);
//			mdn 			= EmailClientUtil.seedEncrypt(mdn);
//			companyCd 		= EmailClientUtil.seedEncrypt(companyCd);
			boxType 		= EmailClientUtil.seedEncrypt(boxType);
			parentBoxType 	= EmailClientUtil.seedEncrypt(parentBoxType);
			boxId 			= EmailClientUtil.seedEncrypt(boxId);
			boxChangKey 	= EmailClientUtil.seedEncrypt(boxChangKey);
			boxNmae 		= EmailClientUtil.seedEncrypt(boxNmae);
			totalCnt 		= EmailClientUtil.seedEncrypt(totalCnt);
			updateDate 		= EmailClientUtil.seedEncrypt(updateDate);
			mailId 			= EmailClientUtil.seedEncrypt(mailId);
			mailChangeKey 	= EmailClientUtil.seedEncrypt(mailChangeKey);
			mailSubject 	= EmailClientUtil.seedEncrypt(mailSubject);
			hasAttachments 	= EmailClientUtil.seedEncrypt(hasAttachments);
//			receivedDate 	= EmailClientUtil.seedEncrypt(receivedDate);
//			sendDate 		= EmailClientUtil.seedEncrypt(sendDate);
			isRead 			= EmailClientUtil.seedEncrypt(isRead);
			fromInfo 		= EmailClientUtil.seedEncrypt(fromInfo);
			toList 			= EmailClientUtil.seedEncrypt(toList);
			mailType 		= EmailClientUtil.seedEncrypt(mailType);
			unreadCnt 		= EmailClientUtil.seedEncrypt(unreadCnt);			
		}

		public void setDecryption() {
//			id 				= EmailClientUtil.seedEncrypt(id);
//			mdn 			= EmailClientUtil.seedEncrypt(mdn);
//			companyCd 		= EmailClientUtil.seedEncrypt(companyCd);
			boxType 		= EmailClientUtil.seedDecrypt(boxType);
			parentBoxType 	= EmailClientUtil.seedDecrypt(parentBoxType);
			boxId 			= EmailClientUtil.seedDecrypt(boxId);
			boxChangKey 	= EmailClientUtil.seedDecrypt(boxChangKey);
			boxNmae 		= EmailClientUtil.seedDecrypt(boxNmae);
			totalCnt 		= EmailClientUtil.seedDecrypt(totalCnt);
			updateDate 		= EmailClientUtil.seedDecrypt(updateDate);
			mailId 			= EmailClientUtil.seedDecrypt(mailId);
			mailChangeKey 	= EmailClientUtil.seedDecrypt(mailChangeKey);
			mailSubject 	= EmailClientUtil.seedDecrypt(mailSubject);
			hasAttachments 	= EmailClientUtil.seedDecrypt(hasAttachments);
//			receivedDate 	= EmailClientUtil.seedDecrypt(receivedDate);
//			sendDate 		= EmailClientUtil.seedDecrypt(sendDate);
			isRead 			= EmailClientUtil.seedDecrypt(isRead);
			fromInfo 		= EmailClientUtil.seedDecrypt(fromInfo);
			toList 			= EmailClientUtil.seedDecrypt(toList);
			mailType 		= EmailClientUtil.seedDecrypt(mailType);
			unreadCnt 		= EmailClientUtil.seedDecrypt(unreadCnt);
		}
				
		
}