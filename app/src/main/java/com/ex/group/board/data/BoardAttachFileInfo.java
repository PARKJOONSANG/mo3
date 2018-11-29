package com.ex.group.board.data;

/**
 * 
 *  <pre>
 *	com.ex.group.board.data
 *	BoardAttachFileInfo.java
 *	</pre>
 *
 *	@Author : 박정호
 * 	@E-MAIL : yee1074@innoace.com
 *	@Date	: 2011. 11. 23. 
 *
 *	TODO
 *	첨부파일 정보
 */
public class BoardAttachFileInfo {
	private String fileId;
	private String fileName;
	private String fileSize;
	
	
	public String getFileId() {
		return fileId;
	}
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFileSize() {
		return fileSize;
	}
	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}
	
	@Override
	public String toString() {
		return "BoardAttachFileInfo [fileId=" + fileId + ", fileName="
				+ fileName + ", fileSize=" + fileSize + "]";
	}
	
	
}
