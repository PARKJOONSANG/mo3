package com.ex.group.approval.easy.primitive;

import com.ex.group.approval.easy.constant.UserInfo;
import com.ex.group.approval.easy.domain.AttachFile;
import com.ex.group.approval.easy.domain.SancLine;
import com.skt.pe.common.activity.SKTActivity.Action;
import com.skt.pe.common.exception.SKTException;
import com.skt.pe.common.primitive.Primitive;
import com.skt.pe.common.service.XMLData;

import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("serial")
public class DetailPrimitive extends Primitive {
	// 결재문서 상세조회
	public final static String name = "COMMON_APPROVALNEW_DETAIL";
	
	private final static String[] paramNames = {
		"userID", "docID", "docType"
	};
	
	private String docID = null;		// Document 식별자 (기존 : docKey)
	private String title = null;		// 문서제목 (기존과 동일)
	private boolean isSecret = false;	// 0 : 비보안 문서, 1 : 보안문서
	private String drafter = null;		// 기안자 (기존 : writer)
	private String draftTime = null;		// 기안일 (기존 : date)
	private String parentSeqNum = null;		//
	private String seqNum = null;		// 결재선 일련번호(순서)
	private String packNum = null;		// 안이 여러 개 일 경우 안 순서
	private String status = null;		// 문서상태
	private String completedTime = null;		// 문서 최종결재 완료시간
	
//	private String confidentialLevel = null;		// 보안등급
	
	private List<SancLine> sancLineList = new ArrayList<SancLine>();
	private List<AttachFile> attachFileList = new ArrayList<AttachFile>();
	private String contentUrl = null;
	
	public DetailPrimitive() {
		super(DetailPrimitive.name, paramNames, Action.SERVICE_RETRIEVING);
		this.init();
	}
	
	private void init() {
//		this.setUserID("19203920");
//		this.setUserID("19516018");
		UserInfo userInfo = UserInfo.getInstance();
		this.setUserID(userInfo.getUserID());
		this.setDocType("simple");
//		this.setDocID("WF5B92073DAC105A0B531ED166003866E7");
	}
	
	public void setUserID(String userID) {
		super.addParameter(paramNames[0], userID);
	}
	
	public void setDocID(String docID) {
		super.addParameter(paramNames[1], docID);
	}
	
	public void setDocType(String docType) {
		super.addParameter(paramNames[2], docType);
	}
	
	public String getDocID() {
		return this.docID;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public boolean getIsSecret() {
		return this.isSecret;
	}
	
	public String getDrafter() {
		return this.drafter;
	}
	
	public String getDraftTime() {
		return this.draftTime;
	}
	
	public String getParentSeqNum() {
		return this.parentSeqNum;
	}
	
	public String getSeqNum() {
		return this.seqNum;
	}
	
	public String getPackNum() {
		return this.packNum;
	}
	
	public String getStatus() {
		return this.status;
	}
	
	public String getCompletedTime() {
		return this.completedTime;
	}
	
	public String getContentUrl() {
		return this.contentUrl;
	}
	
	/*public String getConfidentialLevel() {
		return this.confidentialLevel;
	}*/
	
	public List<SancLine> getSancLineList() {
		return this.sancLineList;
	}
	
	public List<AttachFile> getAttachFileList() {
		return this.attachFileList;
	}
	
	@Override
	public void convertXML(XMLData xmldata) throws SKTException {
		super.convertXML(xmldata);
		
		this.docID = xmldata.get("docID");
		this.title = xmldata.get("title");
		this.isSecret = "1".equals(xmldata.get("isSecret"));
		this.drafter = xmldata.get("drafter");
		this.draftTime = xmldata.get("draftTime");
		this.parentSeqNum = xmldata.get("parentSeqNum");
		this.seqNum = xmldata.get("seqNum");
		this.packNum = xmldata.get("packNum");
		this.status = xmldata.get("status");
		this.completedTime = xmldata.get("completedTime");
		this.contentUrl = xmldata.get("contentUrl");
//		this.confidentialLevel = xmldata.get("confidentialLevel");
		
		int listCnt = parseInt(xmldata.getChild("SancLineList").get("sancLineListCnt"));
		if(xmldata.getChild("SancLineList").get("sancLineListCnt") != null && listCnt > 0) {
			for (int i = 0; i < listCnt; i++) {
				SancLine sancLine = new SancLine();
				sancLine.setXml(xmldata.getChild("SancLineList"), i, "SancLine");
				this.sancLineList.add(sancLine);
			}
		}
		
		listCnt = parseInt(xmldata.getChild("AttachList").get("attachListCnt"));
		if(xmldata.getChild("AttachList").get("attachListCnt") != null && listCnt > 0) {
			for (int i = 0; i < listCnt; i++) {
				AttachFile attachFile = new AttachFile();
				attachFile.setXml(xmldata.getChild("AttachList"), i, "Attach");
				this.attachFileList.add(attachFile);
			}
		}
	}
	
	@Override
	protected String toPrimitiveString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append("docID=");
		sb.append(this.getDocID());
		sb.append("&");
		sb.append("title=");
		sb.append(this.getTitle());
		sb.append("&");
		sb.append("isSecret=");
		sb.append(this.getIsSecret());
		sb.append("&");
		sb.append("drafter=");
		sb.append(this.getDrafter());
		sb.append("&");
		sb.append("draftTime=");
		sb.append(this.getDraftTime());
		sb.append("&");
		sb.append("parentSeqNum=");
		sb.append(this.getParentSeqNum());
		sb.append("&");
		sb.append("seqNum=");
		sb.append(this.getSeqNum());
		sb.append("&");
		sb.append("packNum=");
		sb.append(this.getPackNum());
		sb.append("&");
		sb.append("status=");
		sb.append(this.getStatus());
		sb.append("&");
		sb.append("completedTime=");
		sb.append(this.getCompletedTime());
		sb.append("&");
		sb.append("contentUrl=");
		sb.append(this.getContentUrl());
		sb.append("&");
		/*sb.append("confidentialLevel=");
		sb.append(this.getConfidentialLevel());
		sb.append("&");*/
		
		int listCnt = sancLineList.size();
		for (int i=0; i<listCnt; i++) {
			sb.append(this.sancLineList.get(i).toString());
		}
		
		listCnt = attachFileList.size();
		for (int i=0; i<listCnt; i++) {
			sb.append(this.attachFileList.get(i).toString());
		}

		return sb.toString();
	}
}
