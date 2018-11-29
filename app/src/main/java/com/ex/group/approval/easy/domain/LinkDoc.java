package com.ex.group.approval.easy.domain;

import com.skt.pe.common.primitive.PrimitiveInfo;

@SuppressWarnings("serial")
public class LinkDoc extends PrimitiveInfo {
	/*
	 * 링크문서 제목
	 */
	public String getTitle() {
		return getString("title");
	}
}