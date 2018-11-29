package com.ex.group.board.data;

import java.util.ArrayList;

/**
 * 
 *  <pre>
 *	com.ex.group.board.data
 *	BoardDetailInfo.java
 *	</pre>
 *
 *	@Author : 박정호
 * 	@E-MAIL : yee1074@innoace.com
 *	@Date	: 2011. 11. 23. 
 *
 *	TODO
 *	게시판 정보
 */
public class BoardDetailInfo {

	private String id;
	private String title;
	private String writeDate;
	private String author;
	private String category;
	private String viewUrl;
	private String body;
	private String team;
	private String attachments;
	private String gubun;
	private String attachFileList;
	private String hasScript;
	private ArrayList<BoardAttachFileInfo> attachFileInfos;
	
	private boolean check = false;
	private boolean selected = false;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getWriteDate() {
		return writeDate;
	}
	public void setWriteDate(String writeDate) {
		this.writeDate = writeDate;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getViewUrl() {
		return viewUrl;
	}
	public void setViewUrl(String viewUrl) {
		this.viewUrl = viewUrl;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getTeam() {
		return team;
	}
	public void setTeam(String team) {
		this.team = team;
	}
	public String getAttachments() {
		return attachments;
	}
	public void setAttachments(String attachments) {
		this.attachments = attachments;
	}
	public String getAttachFileList() {
		return attachFileList;
	}
	public void setAttachFileList(String attachFileList) {
		this.attachFileList = attachFileList;
	}
	
	public ArrayList<BoardAttachFileInfo> getAttachFileInfos() {
		return attachFileInfos;
	}
	public void setAttachFileInfos(ArrayList<BoardAttachFileInfo> attachFileInfos) {
		this.attachFileInfos = attachFileInfos;
	}
	public boolean isCheck() {
		return check;
	}
	public void setCheck(boolean check) {
		this.check = check;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}	
	public String getGubun() {
		return gubun;
	}
	public void setGubun(String gubun) {
		this.gubun = gubun;
	}	
	public String getHasScript() {
		return hasScript;
	}
	public void setHasScript(String hasScript) {
		this.hasScript = hasScript;
	}
	
	
	@Override
	public String toString() {
		return "BoardDetailInfo [attachFileInfos=" + attachFileInfos
				+ ", attachFileList=" + attachFileList + ", attachments="
				+ attachments + ", author=" + author + ", body=" + body
				+ ", category=" + category + ", check=" + check + ", gubun="
				+ gubun + ", hasScript=" + hasScript + ", id=" + id
				+ ", selected=" + selected + ", team=" + team + ", title="
				+ title + ", viewUrl=" + viewUrl + ", writeDate=" + writeDate
				+ "]";
	}
	
	
	
}
