package com.ex.group.approval.easy.domain;

import com.skt.pe.common.primitive.PrimitiveInfo;

@SuppressWarnings("serial")
public class AttachFile extends PrimitiveInfo {
	/*
	 * 첨부파일 종류 (신규)
	 */
	public String getAttachType() {
		return getString("attachType");
	}
	/*
	 * 첨부파일 이름 (기존 : fileName)
	 */
	public String getName() {
		return getString("name");
	}
	/*
	 * 첨부파일 ID 또는 Url (기존 : fileId)
	 */
	public String getIdOrUrl() {
		return getString("idOrUrl");
	}
	/*
	 * 첨부파일 크기(?) (신규)
	 */
	public String getLength() {
		return getString("length");
	}
	
	/*
	 * 보안등급
	public String getConfidentialLevel(){
		return getString("confidentialLevel");
	}*/
}