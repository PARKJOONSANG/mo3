package com.ex.group.approval.easy.domain;

import com.skt.pe.common.exception.SKTException;
import com.skt.pe.common.primitive.PrimitiveInfo;
import com.skt.pe.common.service.XMLData;

@SuppressWarnings("serial")
public class HtmlInfo extends PrimitiveInfo {
	/*
	 * HTML 정보
	 */
	public String getHtml() {
		return getString("html");
	}
	/*
	 * 이미지 정보
	 */
	public String getImage() {
		return getString("image");
	}
	public void setXml(XMLData child) throws SKTException {
		XMLData html = child.getChild("html");
		super.map.put("html", html.get("body"));
		XMLData image = child.getChild("image");
		super.map.put("image", child.get("body"));
	}
}