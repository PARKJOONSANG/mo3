package com.ex.group.approval.easy.domain;

import com.skt.pe.common.primitive.PrimitiveInfo;

@SuppressWarnings("serial")
public class FormInfo extends PrimitiveInfo {
	/*
	 * 기안자 설명
	 */
	public String getName() {
		return getString("name");
	}
	/*
	 * 자기차량번호 (존재하지 않으면 자기차량 여부에서 'Y' 선택 불가능)
	 */
	public String getCarNumber() {
		return getString("carNumber");
	}
	/*
	 * 사용한 사무외출일수
	 */
	public String getOutDate() {
		return getString("outDate");
	}
	/*
	 * 입력되는 기본전화번호
	 */
	public String getPhone() {
		return getString("phone");
	}
	
	// 2015-06-26 Join 추가 - 필요 시 아래 구문 추가
	/*
	 * 자녀 이름
	 */
//	public String getChildName() {
//		return getString("childName");
//	}
	/*
	 * 자녀 주민등록번호 앞 6자리
	 */
//	public String getChildJmdrbeonho() {
//		return getString("childJmdrbeonho");
//	}
	// 2015-06-26 Join 추가 끝
	
	/*
	 * 육아여부
	 */
	public boolean getHaveChild() {
		return Boolean.TRUE.equals(getString("haveChild"));
	}
	
	public static class Boolean {
		public final static String TRUE = "Y";
		public final static String FALSE = "Y";
	}
}