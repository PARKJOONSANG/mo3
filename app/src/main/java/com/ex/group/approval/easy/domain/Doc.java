package com.ex.group.approval.easy.domain;

import com.skt.pe.common.primitive.PrimitiveInfo;

@SuppressWarnings("serial")
public class Doc extends PrimitiveInfo {
	// 2014-07-18 Join 수정 : 간이결재 고도화로 인한 수정
	/*
	 * 문서종류
	 */
	public String getStrDocType() {
		return getString("strDocType");
	}
	/*
	 * 문서타입
	 * 일반결재 : general, 간이결재 : simple
	 */
	public String getDocType() {
		return getString("DocType");
	}
	/*
	 * 기안 제목
	 */
	public String getTitle() {
		return getString("title");
	}
	/*
	 * 문서도착 시간 (기존 : date -> arrivedTime -> draftTime)
	 */
	public String getArrivedTime() {
		return getString("draftTime");
	}
	/*
	 * 기안 작성자 명 (기존 : writer)
	 */
	public String getCreatorPersonInfo() {
		return getString("creatorPersonInfo");
	}
	/*
	 * 기안자 부서명 (기존 : deptNm)
	 */
	public String getCreatorDeptInfo() {
		return getString("creatorDeptInfo");
	}
	/* 
	 * 문서 고유 키 (기존 : docKey)
	 */
	public String getDocID(){
		return getString("docID");
	}
	/*
	 * 비밀글 여부 - 0 : 비보안문서, 1 : 보안문서
	 */
	public boolean isSecret() {
		return "0".equals(getString("isSecret"));
	}
	/*
	 * 열람 여부 - 0 : 읽지 않음, 1 : 읽음
	 */
	public boolean isRead() {
		return "1".equals(getString("isRead"));
	}
	/*
	 * 첨부파일 여부 - 0 : 첨부있음, 1 : 첨부없음
	 */
	public boolean isAttach() {
		return "0".equals(getString("isAttach"));
	}
	/*
	 * 수정여부 - 0 : 수정함, 1 : 수정안함 
	 */
	public boolean isModified() {
		return "0".equals(getString("isModified"));
	}
	/*
	 * 결재종류 - 110 : 결재, 111 : 협조, 112 : 공람, 113 : 전결, 114 : 대결, 115 : 담당, 116 : 결재안함, 117 : 대결(사후보고), 
	 *                 118 : 감사(부서), 119 : 검토, 120 : 상임감사(개인), 121 : 부서 협조, 122 : 부서결재 접수, 123 : 부서결재 기안, 
	 *                 124 : 부서결재 반송, 125 : 기안, 126 : 대결(한문 대), 127 : 대결(권한대행), 128 : 사후보고(한문대), 130 : 기안전결, 
	 *                 131 : 협조(병렬), 132 : 협조안함, 133 : 협조안함(병렬), 134 : 부서 협조(병렬), 135 : 참조, 136 : 기업후결, 137 : 기업후열, 
	 *                 138 : 상임감사 후결, 139 : 협조후결(일반), 140 : 협조후열(일반), 141 : 통제
	 */
	public String getSancKind() {
		return getString("sancKind");
	}
	/*
	 * 결재 진행상태 (기존 : signStatus) - 20 : 초기상태 , 21 : 진행, 22 : 반송, 23 : 완결, 24 : 부서협조중, 25 : 부서감사중, 
	 *                                                    26 : 부서결재중 자기부서 반송, 27 : 기안취소, 28 : 반려문서 등록, 29 :사용 불가 상태(연동시) , 
	 *                                                    79 :담당완료
	 */
	public String getStatus() {
		return getString("status");
	}
}