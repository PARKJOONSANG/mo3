package com.ex.group.approval.easy.domain;

import com.skt.pe.common.primitive.PrimitiveInfo;

@SuppressWarnings("serial")
public class OpinionInfo extends PrimitiveInfo {
	/*
	 * 의견 작성자
	 */
	public String getWriter() {
		return getString("writer");
	}
	
	/*
	 * 의견 작성자 직급
	 */
	public String getTitle() {
		return getString("title");
	}
	
	/*
	 * 의견 내용
	 */
	public String getContent() {
		return getString("content");
	}
	
	/*
	 * 의견 등록일
	 */
	public String getDate() {
		return getString("date");
	}
	
	/*
	 * 본인 여부
	 */
	public String getIsWriter() {
		return getString("isWriter");
	}
}