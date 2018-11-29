package com.ex.group.approval.easy.domain;

import com.skt.pe.common.primitive.PrimitiveInfo;

@SuppressWarnings("serial")
public class VocCode extends PrimitiveInfo {
	/*
	 * 휴가종류코드
	 */
	public String getCode() {
		return getString("code");
	}
	/*
	 * 휴가종류코드명
	 */
	public String getCodeNm() {
		return getString("codeNm");
	}
	/*
	 * 가능한 휴가기간
	 */
	public int getTerm() {
		return getInt("term");
	}
}