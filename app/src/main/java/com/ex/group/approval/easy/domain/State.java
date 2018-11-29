package com.ex.group.approval.easy.domain;

import com.skt.pe.common.primitive.PrimitiveInfo;

@SuppressWarnings("serial")
public class State extends PrimitiveInfo {
	/*
	 * 문서현황
	 */
	public String getStatus() {
		return getString("status");
	}
	/*
	 * 승인/미승인 구분
	 */
	public String getSignYn() {
		return getString("signYn");
	}
	/*
	 * 결재자 구분 문자열
	 */
	public String getTaskName() {
		return getString("taskName");
	}
	/*
	 * 수신일자
	 */
	public String getDateReceived() {
		return getString("dateReceived");
	}
	/*
	 * 승인 또는 반려일자
	 */
	public String getDateCompleted() {
		return getString("dateCompleted");
	}
	/*
	 * 결재자 ID
	 */
	public String getUserId(){
		return getString("userId");
	}
	/*
	 * 결재자 내부 이메일 ID
	 */
	public String getInEmail() {
		return getString("inEmail");
	}
	/*
	 * 결재자 이름
	 */
	public String getName() {
		return getString("name");
	}
	/*
	 * 결재자 직위/직책명
	 */
	public String getDutyNm() {
		return getString("dutyNm");
	}
	/*
	 * 결재자 부서코드
	 */
	public String getDeptCd() {
		return getString("deptCd");
	}
	/*
	 * 결재자 부서명
	 */
	public String getDeptNm() {
		return getString("deptNm");
	}
}