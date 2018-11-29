package com.ex.group.mail.data;

import com.skt.pe.common.exception.SKTException;

import java.io.Serializable;

/**
 * Serializable 만들기
 * @author sjsun5318
 *
 */
public class ExceptionWrapper implements Serializable {

	private static final long serialVersionUID = 1L;

	private SKTException e;

	public ExceptionWrapper(SKTException e) {
		this.e = e;
	}
	
	public SKTException getE() {
		return e;
	}

	public void setE(SKTException e) {
		this.e = e;
	}
	
	
}
