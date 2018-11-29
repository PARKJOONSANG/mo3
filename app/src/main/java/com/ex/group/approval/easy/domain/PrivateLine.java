package com.ex.group.approval.easy.domain;

import com.skt.pe.common.primitive.PrimitiveInfo;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class PrivateLine extends PrimitiveInfo {
	private List<PrivateState> mStateList = new ArrayList<PrivateState>();
	
	/*
	 * 사용자 저장된 결재경로 ID (기존 : privateLineId)
	 */
	public String getAliasId() {
		return getString("aliasId");
	}
	/*
	 * 사용자 저장된 결재경로명
	 */
	public String getAliasName() {
		return getString("aliasName");
	}
	/*
	 * 사용자 저장된 결재경로 설명 (기존 : privateLineName)
	 */
	public String getAliasDesc() {
		return getString("aliasDesc");
	}
	/*
	 * ??
	 */
	public String getDefaultAlias() {
		return getString("defaultAlias");
	}
	/*
	 * 결재경로 상세 정보
	 */
	public List<PrivateState> getStateList() {
		return mStateList;
	}
}