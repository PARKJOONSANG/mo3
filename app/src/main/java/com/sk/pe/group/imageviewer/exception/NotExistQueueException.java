package com.sk.pe.group.imageviewer.exception;

public class NotExistQueueException extends RuntimeException {
	public NotExistQueueException() {
		super();
	}
	
	public String getMessage() {
		return "존재하지 않은 Queue의 내용이 요청되었습니다.";
	}
}
