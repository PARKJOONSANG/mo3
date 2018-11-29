package com.ex.group.board.constants;

public final class Constants {
	// 3.1.1. 게시물 목록 조회
	public final static String BOARD_LIST_PRIMITIVE = "COMMON_BOARD_LISTNEW";
	
	// 3.1.2. 게시물 상세 조회
	public final static String BOARD_DETAIL_PRIMITIVE = "COMMON_BOARD_CONTENTNEW";
	
	// 게시판별 유형 코드
	/** type = 1 : 긴급게시판   ( id : B0000100 ) -->> 감사청렴 게시판 으로 변경(2017.04.20) */
	public final static String BOARD_TYPE1 = "1";
	/** type = 2 : 업무게시판   ( id : B0000200 ) */
	public final static String BOARD_TYPE2 = "2";
	/** type = 3 : 홍보게시판   ( id : B0018805 ) */
	public final static String BOARD_TYPE3 = "3";
	/** type = 4 : 경조사게시판 ( id : B0000400 ) */
	public final static String BOARD_TYPE4 = "4";
	/** type = 5 : 노조게시판   ( id : B0000500 ) */
	public final static String BOARD_TYPE5 = "5";
	/** type = 6 : 자유게시판   ( id : B0000300 ) */
	public final static String BOARD_TYPE6 = "6";

	public final static int BOARD_SHOW_MONTH = 3;

	
	public static class RequestCode{
		public static final int DetailActivity = 100;
		public static final int NotiRegCod = 100352;
	}
	
	
	public static class RequestFeild{
		/** [리스트] 게시판별 유형 코드 */
		public final static String LIST_TYPE = "boardType";
		/** [리스트] 요청 페이지 정보 (초기 요청 시 1로 설정) */
		public final static String LIST_PG = "pg";
		/** [리스트] 검색어 */
		public final static String LIST_KWD = "kwd";
		/** [리스트] 검색 유형
					searchType = 0 : 검색안함
					searchType = 1 : 게시자 검색
					searchType = 2 : 제목 검색
		*/
		public final static String LIST_SEARCHTYPE = "searchType";
		
		/** [상세] 게시판별 유형 코드 */
		public final static String DETAIL_TYPE = "boardType";
		/** [상세] 게시물 등록키 */
		public final static String DETAIL_BOARDKEY = "boardKey";
		/** [상세] 게시물의 등록 년원 (예 : 201108) */
		public final static String DETAIL_YEARMON = "yearmon";
		
		
	}
	
};
