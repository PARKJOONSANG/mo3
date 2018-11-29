package com.ex.group.approval.easy.domain;

import android.util.Log;

import com.skt.pe.common.primitive.PrimitiveInfo;

@SuppressWarnings("serial")
public class SancLine extends PrimitiveInfo {
	private final String TAG = "SancLine";
	/*
	 * 결재종류 - 110 : 결재, 111 : 협조, 112 : 공람, 113 : 전결, 114 : 대결, 115 : 담당, 116 : 결재안함, 117 : 대결(사후보고), 
	 *                 118 : 감사(부서), 119 : 검토, 120 : 상임감사(개인), 121 : 부서 협조, 122 : 부서결재 접수, 123 : 부서결재 기안, 
	 *                 124 : 부서결재 반송, 125 : 기안, 126 : 대결(한문 대), 127 : 대결(권한대행), 128 : 사후보고(한문대), 130 : 기안전결, 
	 *                 131 : 협조(병렬), 132 : 협조안함, 133 : 협조안함(병렬), 134 : 부서 협조(병렬), 135 : 참조, 136 : 기업후결, 137 : 기업후열, 
	 *                 138 : 상임감사 후결, 139 : 협조후결(일반), 140 : 협조후열(일반), 141 : 통제
	 */
	public String getKind() {
		return getString("kind");
	}
	/*
	 * 결재종류 한글명
	 */
	public String getSancType() {
		return getString("sancType");
	}
	/*
	 * 결재자 구분 - 100 : 사람, 101 : 부서, 102 : 타기관사람 (기존 : taskName)
	 */
	public String getActorType() {
		return getString("actorType");
	}
	/*
	 * 결재자 정보 (기존 : bossName / bossPosition / deptNm)
	 * ex) <name>김명식/4급/정보개발팀</name>
	 */
	public String getBossName() {
		String name = null;
		String approvalInfo = getString("name");
		if(approvalInfo != null) {
			Log.d(TAG, "approvalInfo ================= " + approvalInfo);
			String[] arrInfo = approvalInfo.split("/");
			if(arrInfo.length > 0)		name = arrInfo[0];
		}
		return name;
	}
	public String getBossPosition() {
		String position = null;
		String approvalInfo = getString("name");
		if(approvalInfo != null) {
			Log.d(TAG, "approvalInfo ================= " + approvalInfo);
			String[] arrInfo = approvalInfo.split("/");
			if(arrInfo.length > 0)		position = arrInfo[1];
		}
		return position;
	}
	public String getDeptNm() {
		String dept = null;
		String approvalInfo = getString("name");
		if(approvalInfo != null) {
			Log.d(TAG, "approvalInfo ================= " + approvalInfo);
			String[] arrInfo = approvalInfo.split("/");
			if(arrInfo.length > 0)		dept = arrInfo[2];
		}
		return dept;
	}
	/*
	 * 결재자의 결재상태
	 */
	public String getSancStatus() {
		return getString("sancStatus");
	}
	/*
	 * 처리시간 (기존 : dateCompleted)
	 */
	public String getProcessedTime() {
		return getString("processedTime");
	}
	/*
	 * 결재의견
	 */
	public String getSancComment() {
		return getString("sancComment");
	}
}