package com.ex.group.board.data;

import java.util.ArrayList;

/**
 * 
 *  <pre>
 *	com.ex.group.board.data
 *	BoardListDAO.java
 *	</pre>
 *
 *	@Author : 박정호
 * 	@E-MAIL : yee1074@innoace.com
 *	@Date	: 2011. 11. 23. 
 *
 *	TODO
 */
public class BoardListDAO {

	private int currentTotalPage;
	private boolean endPage;
	private int listCnt;
	private String boardNew;
	
	private int currentPage;
	
	private ArrayList<BoardListInfo> listInfo;
	
	
	public BoardListDAO() {
		super();
		currentTotalPage = 0;
		endPage = false;
		listCnt = 0;
		boardNew = "";
		currentPage = 0;
		listInfo = new ArrayList<BoardListInfo>();
	}

	public int getCurrentTotalPage() {
		return currentTotalPage;
	}

	public void setCurrentTotalPage(int currentTotalPage) {
		this.currentTotalPage = currentTotalPage;
	}

	public boolean isEndPage() {
		return endPage;
	}

	public void setEndPage(boolean endPage) {
		this.endPage = endPage;
	}

	public int getListCnt() {
		return listCnt;
	}

	public void setListCnt(int listCnt) {
		this.listCnt = listCnt;
	}

	public String getBoardNew() {
		return boardNew;
	}

	public void setBoardNew(String boardNew) {
		this.boardNew = boardNew;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	
	public void initListInfo(){
		listInfo = new ArrayList<BoardListInfo>();
	}
	public ArrayList<BoardListInfo> getListInfo() {
		return listInfo;
	}

	public void setListInfo(ArrayList<BoardListInfo> listInfo) {
		this.listInfo = listInfo;
	}

	@Override
	public String toString() {
		return "BoardListDAO [boardNew=" + boardNew + "\n, currentPage="
				+ currentPage + "\n, currentTotalPage=" + currentTotalPage
				+ "\n, endPage=" + endPage + "\n, listCnt=" + listCnt
				+ "\n, listInfo=" + listInfo + "]";
	}

	
	
	
}
