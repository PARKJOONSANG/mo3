package com.ex.group.approval.easy.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 날짜 관련 유틸리티 클래스
 * @author jokim
 *
 */
public class DateUtils {
	
	public static final long DAY_OF_MILLIS = 24 * 60 * 60 * 1000;     // 1일의 miliseconds
	
	/**
	 * 해당 날짜를 초기화 한다. (시, 분, 초, 밀리초를 0으로 초기화)
	 * 
	 * @param a_calendar 초기화할 Calendar
	 */
	
	public static void initDate(Calendar a_calendar) {
		
		a_calendar.set(Calendar.HOUR_OF_DAY, 0);
		a_calendar.set(Calendar.MINUTE, 0);
		a_calendar.set(Calendar.SECOND, 0);
		a_calendar.set(Calendar.MILLISECOND, 0);
		
	} // end public static void initDate(Calendar a_calendar)
	
	/**
	 * 해당 날짜의 문자열에서 해당 값을 정수로 변환하여 리턴한다. (YYYYMMDD 형식이어야 함)
	 * @param a_szDate 기준 날짜 문자열
	 * @param a_nOption 가져올 값의 상수(Calendar.YEAR, Calendar.MONTH, Calendar.DATE)
	 * @return 해당 
	 */
	
	public static int getDate(String a_szDate, int a_nOption) {
		
		if (a_szDate == null) {
			
			return 0;
			
		} // end if (a_szDate == null)
		
		if (a_szDate.length() != 8) {
			
			return 0;
			
		} // end if (a_szDate.length() != 8)
		
		int nResult = 0;
		
		try {
			
			switch (a_nOption) {
			
				case Calendar.YEAR:
					
					nResult = Integer.parseInt(a_szDate.substring(0, 4));
					break;
					
				case Calendar.MONTH:
					
					nResult = Integer.parseInt(a_szDate.substring(4, 6));
					break;
					
				case Calendar.DATE:
					
					nResult = Integer.parseInt(a_szDate.substring(6, 8));
					break;
			
			} // end switch (a_nOption)
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		} // end try
		
		return nResult;
		
	} // end public static int getDate(String a_szDate, int a_nOption)
	
	/**
     * 해당 월에 일을 설정해 준다.
     * 
     * @param a_nMonth 설정할 월
     */
    
    public static int getLastDay(int a_nYear, int a_nMonth) {

    	int nLastDay = 0;
    	
    	switch (a_nMonth) {
		
			// 31일 경우
			case 0:
			case 2:
			case 4:
			case 6:
			case 7:
			case 9:
			case 11:
				
				nLastDay = 31;
				
				break;
			// 30일 경우
			case 3:
			case 5:
			case 8:
			case 10:
				
				nLastDay = 30;
				
				break;
			// 2월인 경우
			case 1:
				
				// 윤년인 경우
				if ((((a_nYear % 400) == 0 || (a_nYear % 4) == 0 && (a_nYear % 100) != 0))) {
					
					nLastDay = 29;
												
				} // end if ((((a_nYear % 400) == 0 || (a_nYear % 4) == 0 && (a_nYear % 100) != 0)))
				
				else {
					
					nLastDay = 28;
					
				} // end else
				
				break;
		
		} // end switch (a_nMonth)
    	
    	return nLastDay;
    	
    } // end public static int getLastDay(int a_nYear, int a_nMonth)
    /*
    public static String getDayOfString(Context context, int a_nDay) 
    {    	
    	String day = null;
    	
    	switch (a_nDay) 
    	{		
			case Calendar.SUNDAY: 
				day = context.getResources().getString(R.string.SUNDAY); break;			
			case Calendar.MONDAY: 
				day = context.getResources().getString(R.string.MONDAY); break;					
			case Calendar.TUESDAY: 
				day = context.getResources().getString(R.string.TUESDAY); break;					
			case Calendar.WEDNESDAY: 
				day = context.getResources().getString(R.string.WEDNESDAY); break;						
			case Calendar.THURSDAY:	
				day = context.getResources().getString(R.string.THURSDAY); break;						
			case Calendar.FRIDAY: 
				day = context.getResources().getString(R.string.FRIDAY); break;					
			case Calendar.SATURDAY:	
				day = context.getResources().getString(R.string.SATURDAY); break;			
		} // end switch (m_calendar.get(Calendar.DAY_OF_WEEK))
    	
    	return (day == null) ? "" : day + context.getResources().getString(R.string.day_of_week_laststring);    	    	
    } // end public static String getDayOfString(int a_nDay)
    */
    public static String getDate(long a_nTimeMillis) {
    
    	Calendar aCalendar = Calendar.getInstance();
    	aCalendar.setTimeInMillis(a_nTimeMillis);
    	
    	return String.format("%4d.%02d.%02d", aCalendar.get(Calendar.YEAR), aCalendar.get(Calendar.MONTH) + 1, aCalendar.get(Calendar.DAY_OF_MONTH));
    	
    }
    
    /**
     * 서버로부터 받은 날짜를 원하는 Pattern으로 변경해준다.
     * 
     * @param source 서버로부터 받은 날짜
     */
    public static String datePatternChange(String source, String pattern) {
		String changedDate = null;
		if(source != null && source.length() > 0 && pattern != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HHmmss");
			try {
				Date date = sdf.parse(source);
				sdf = new SimpleDateFormat(pattern);
				changedDate = sdf.format(date);
				
			} catch(ParseException e) {
				e.printStackTrace();
			}
			
		}
		
		return changedDate;
	}

} // end public class DateUtils
