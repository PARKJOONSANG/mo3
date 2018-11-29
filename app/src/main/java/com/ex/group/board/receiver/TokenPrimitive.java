package com.ex.group.board.receiver;

import com.skt.pe.common.activity.SKTActivity.Action;
import com.skt.pe.common.primitive.Primitive;


@SuppressWarnings("serial")
public class TokenPrimitive extends Primitive {
	public final static String name = "COMMON_MO_TOKEN";
	
	private final static String[] paramNames = {
		"token"
	};
	
	public TokenPrimitive() {
		super(TokenPrimitive.name, paramNames, Action.SERVICE_SENDING);
		this.init();
	}
	
	private void init() {
	}
	
	public void setToken(String token) {
		super.addParameter(paramNames[0], token);
	}
}
