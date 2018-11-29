package com.ex.group.board.data;

/**
 * @author ng
 *
 */
public class BoardListInfo {
	
	private String id;
	private String title;
	private String writeDate;
	private String author;
	private String team;
	private String read;
	private String attachment;
	private String level;
	private String parentId;
	private String childrenCnt;
	
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
	public String getTeam() {
		return team;
	}
	public void setTeam(String team) {
		this.team = team;
	}
	public String getRead() {
		return read;
	}
	public void setRead(String read) {
		this.read = read;
	}
	public String getAttachment() {
		return attachment;
	}
	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getChildrenCnt() {
		return childrenCnt;
	}
	public void setChildrenCnt(String childrenCnt) {
		this.childrenCnt = childrenCnt;
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
	@Override
	public String toString() {
		return "BoardListInfo [attachment=" + attachment + ", author=" + author
				+ ", check=" + check + ", childrenCnt=" + childrenCnt + ", id="
				+ id + ", level=" + level + ", parentId=" + parentId
				+ ", read=" + read + ", selected=" + selected + ", team="
				+ team + ", title=" + title + ", writeDate=" + writeDate + "]\n";
	}
	
	
}
