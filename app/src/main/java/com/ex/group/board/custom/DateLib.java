package com.ex.group.board.custom;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.StringTokenizer;

public class DateLib {
	
	/**
	 * 시간값
	 * @param t	   - Time String(yyyyMMddHHmmss) or ""
	 * @param type -  "0" : yyyyMMddHHmmss, "1" : yyyy:MM:dd HH:mm:ss, "2" : yyyy:MM:dd HH:mm:ss AM/PM,
	 * 				  "3" : yyyy:MM:dd, "4" : HH:MM:ss, "5" : HH:MM:ss AM/PM, "6" : 오전/오후 HH:MM, "7" : MM월 dd일, "8" : yyyyMMdd,
	 * 				  "9" : mm/dd, "10" : mm/dd PM/AM HH:MM
	 * 
	 * @return String
	 */
	public static String getTime(String t, int type) {
		String time;
		String r = "";
		if ("".equals(t)) {
			SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			time = timeStampFormat.format(new Date());
		} else {
			time = t;
		}
		switch (type) {
		case 1:
			break;
		case 2:
			r = time.substring(0, 4) + ":" + time.substring(4, 6) + ":"
					+ time.substring(6, 8) + " " + time.substring(8, 10) + ":"
					+ time.substring(10, 12) + ":" + time.substring(12, 14);
			return r;
		case 3:
			String m = "AM";
			if ((Integer.parseInt(time.substring(8, 10)) > 12)) {
				m = "PM";
			}
			r = time.substring(0, 4)
					+ "/"
					+ time.substring(4, 6)
					+ "/"
					+ time.substring(6, 8)
					+ " " +m
					+ (Integer.parseInt(time.substring(8, 10)) < 12 ? time
							.substring(8, 10) : Integer.parseInt(time
							.substring(8, 10))
							- 12 + "") + ":" + time.substring(10, 12) + ":"
					+ time.substring(12, 14);
			return r;
		case 4:
			r = time.substring(0, 4) + ":" + time.substring(4, 6) + ":"
					+ time.substring(6, 8);
			return r;
		case 5:
			String m1 = "AM";
			if ((Integer.parseInt(time.substring(8, 10)) > 12)) {
				m1 = "PM";
			}
			r = (Integer.parseInt(time.substring(8, 10)) < 12 ? time.substring(
					8, 10) : Integer.parseInt(time.substring(8, 10)) - 12 + "")
					+ ":"
					+ time.substring(10, 12)
					+ ":"
					+ time.substring(12, 14) + " " + m1;
			return r;

		case 6:
			String m2 = "오전";
			if ((Integer.parseInt(time.substring(8, 10)) > 11)) {
				m2 = "오후";
			}
			r = m2 + " " + (Integer.parseInt(time.substring(8, 10)) >= 10 ?
							((Integer.parseInt(time.substring(8, 10))) >= 13 ?
									Integer.parseInt(time.substring(8, 10)) - 12 : Integer.parseInt(time.substring(8, 10)))
							: /*"0"+ (Integer.parseInt(time.substring(8, 10)))*/time.substring(8, 10) ) +
			    ":"+time.substring(10, 12);
			CustomLog.L("dateLib", "time " + time);
			CustomLog.L("dateLib", "r " +r);
			return r;
			
		case 7:
			r = time.substring(4, 6) + "월 " + time.substring(6, 8) + "일";
			return r;
			
		case 8:
			r = time.substring(0, 4) + "" + time.substring(4, 6) + ""
					+ time.substring(6, 8);
			return r;
			
		case 9:
			r = time.substring(4, 6) + "/"+ time.substring(6, 8);
			return r;
			
		case 10:
			String m3 = "AM";
			if ((Integer.parseInt(time.substring(8, 10)) >= 12)) {
				m3 = "PM";
			}
			r = time.substring(4, 6) + "/"+ time.substring(6, 8) +
				" " + m3 + " "+
				(Integer.parseInt(time.substring(8, 10)) < 13 ? time.substring(
					8, 10) : Integer.parseInt(time.substring(8, 10)) - 12 + "")
					+ ":"
					+ time.substring(10, 12);
			return r;
		case 11:
			return time.substring(0, time.length()-2);
		case 12:
			r = time.substring(0, 4) + "년 " + Integer.parseInt(time.substring(4, 6)) + "월 "
			+ Integer.parseInt(time.substring(6, 8))+"일";
			return r;
		case 13:
			String m4 = "오전";
			if ((Integer.parseInt(time.substring(8, 10)) >= 12)) {
				m4 = "오후";
			}
			r = m4 + " " + (Integer.parseInt(time.substring(8, 10)) >= 10 ?
							((Integer.parseInt(time.substring(8, 10))) >= 13 ?
									Integer.parseInt(time.substring(8, 10)) - 12 : Integer.parseInt(time.substring(8, 10)))
							: /*"0"+ (Integer.parseInt(time.substring(8, 10)))*/time.substring(8, 10) ) +
				    ":"+time.substring(10, 12);
			
			return Integer.parseInt(time.substring(4, 6)) + "/"+ Integer.parseInt(time.substring(6, 8)) +
			" ("+getCurrentDayOfWeekKor(time)+") "+ r;
			
		case 14:
			String m5 = "AM";
			if ((Integer.parseInt(time.substring(8, 10)) >= 12)) {
				m5 = "PM";
			}
			r = m5 + " " + (Integer.parseInt(time.substring(8, 10)) >= 10 ?
							((Integer.parseInt(time.substring(8, 10))) >= 13 ?
									Integer.parseInt(time.substring(8, 10)) - 12 : Integer.parseInt(time.substring(8, 10)))
							: /*"0"+ (Integer.parseInt(time.substring(8, 10)))*/time.substring(8, 10) ) +
				    ":"+time.substring(10, 12);
			
			return Integer.parseInt(time.substring(4, 6)) + "/"+ Integer.parseInt(time.substring(6, 8)) +
			" ("+getCurrentDayOfWeekKor(time)+") "+ r;
		case 15:
			String m6 = "AM";
			if ((Integer.parseInt(time.substring(8, 10)) >= 12)) {
				m6 = "PM";
			}
			r = time.substring(0, 4)
					+ "/"
					+ time.substring(4, 6)
					+ "/"
					+ time.substring(6, 8)
					+ " " +m6+ " " 
					+ (Integer.parseInt(time.substring(8, 10)) < 12 ? time
							.substring(8, 10) : Integer.parseInt(time
							.substring(8, 10))
							- 12 + "") + ":" + time.substring(10, 12);
			return r;
		case 16:
			String m7 = "오전";
			if ((Integer.parseInt(time.substring(8, 10)) >= 12)) {
				m7 = "오후";
			}
			r = (Integer.parseInt(time.substring(4, 6))) +"/"+(Integer.parseInt(time.substring(6, 8)))+
					" " +m7 + " " + (Integer.parseInt(time.substring(8, 10)) >= 10 ?
							((Integer.parseInt(time.substring(8, 10))) >= 13 ?
									Integer.parseInt(time.substring(8, 10)) - 12 : Integer.parseInt(time.substring(8, 10)))
							: /*"0"+ (Integer.parseInt(time.substring(8, 10)))*/time.substring(8, 10) ) +
				    ":"+time.substring(10, 12);
							
			return r;
		case 17:
			r = Integer.parseInt(time.substring(4, 6)) + "/"+ Integer.parseInt(time.substring(6, 8));
			return r;
		case 18:
			r = time.substring(0, 4) + "년 " + Integer.parseInt(time.substring(4, 6)) + "월 "
			+ Integer.parseInt(time.substring(6, 8))+"일"+ Integer.parseInt(time.substring(8, 10))+"시"+ Integer.parseInt(time.substring(10, 12))+"분";
			return r;
		}

		return time;
	}

	public static String getData(String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
		Calendar m_Calendar = Calendar.getInstance();
		return sdf.format(new Date(m_Calendar.getTimeInMillis()));
	}

	

	/**
	 * 
	 * @param date - yyyyMMdd
	 * @return [0] = Year, [1] = Month, [2] = Day
	 */
	public static int [] calToDate(String date){
		int []tmp = new int[3];
		if(date.length() == 8){
			tmp[0] = Integer.parseInt(date.substring(0, 4));
			tmp[1] = Integer.parseInt(date.substring(4, 6));
			tmp[2] = Integer.parseInt(date.substring(6, 8));
		}else{
			for(int i=0;i<3;i++) tmp[i] = -1;
		}
		return tmp;
	}
	
	
	/**
	 * 
	 * @param date
	 *            - yyyyMMdd
	 * @param j
	 *            - 1 : yyyy, 2 : MM, 3 : dd
	 * @return [0] = Year, [1] = Month, [2] = Day
	 */
	public static int calToDate(String date, int j) {
		int tmp = -1;

		if (date.length() == 8) {
			switch (j) {
			case 1:
				tmp = Integer.parseInt(date.substring(0, 4));
				break;

			case 2:
				tmp = Integer.parseInt(date.substring(4, 6));
				break;

			case 3:
				tmp = Integer.parseInt(date.substring(6, 8));
				break;

			default:
				tmp = Integer.parseInt(date.substring(0, 8));
				break;

			}
		} else {
			for (int i = 0; i < 3; i++)
				tmp = -1;
		}
		return tmp;
	}

	/**
	 * 날자 비교 두 날자 값을 가지고 날차의 차이값을 반환 String Type : (yyyyMMddHHmmss)
	 * 
	 * @param a
	 * @param b
	 * @return Integer a 를 기준으로 b의 날자의 차이값을 반환
	 */

	public static int dayCompare(String a, String b) {
		// String days="";
		int days = 0;
		int[] month = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

		// 윤달
		if ((Integer.parseInt(a.substring(0, 4)) % 4 == 0)
				&& (Integer.parseInt(a.substring(0, 4)) % 100 != 0)
				|| (Integer.parseInt(a.substring(0, 4)) % 400 == 0)) {
			month[1] = 29;
		}
		if (a.length() >= 10 && b.length() >= 10) {
			days += (Integer.parseInt(a.substring(0, 4)) - Integer.parseInt(b
					.substring(0, 4))) * 365;
			days += (Integer.parseInt(a.substring(4, 6)) - Integer.parseInt(b
					.substring(4, 6)))
					* month[(Integer.parseInt(a.substring(4, 6)) - 1)];
			days += (Integer.parseInt(a.substring(6, 8)) - Integer.parseInt(b
					.substring(6, 8)));
		} else {
			days = -1;
		}

		return days;
	}
	
	/******************************************************************************
	 * 
	 * 함수명 : getAlramTime 인자 : 리턴값 : 설명 : 지정한 시간에서 minute 지난 시간을 리턴
	 * 
	 ******************************************************************************/
	public static String getAlramTime(String date, int minute) {
		SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		Calendar today = Calendar.getInstance();
		today.set(Calendar.YEAR, Integer.parseInt(date.substring(0, 4)));
		today.set(Calendar.MONTH, Integer.parseInt(date.substring(4, 6))-1);
		today.set(Calendar.DATE, Integer.parseInt(date.substring(6, 8)));
		today.set(Calendar.HOUR_OF_DAY, Integer.parseInt(date.substring(8, 10)));
		today.set(Calendar.MINUTE, Integer.parseInt(date.substring(10, 12)));
		today.set(Calendar.SECOND, 0);
		today.add(Calendar.MINUTE, -minute);
		
		return timeStampFormat.format(new Date(today.getTimeInMillis()));
	}
	
	/******************************************************************************
	 * 
	 * 함수명 : getAlramTime 인자 : 리턴값 : 설명 : 지정한 시간에서 minute 지난 시간을 리턴
	 * 
	 ******************************************************************************/
	public static Long getAlramTimeL(String date, int minute) {
		Calendar today = Calendar.getInstance();
		today.set(Calendar.YEAR, Integer.parseInt(date.substring(0, 4)));
		today.set(Calendar.MONTH, Integer.parseInt(date.substring(4, 6))-1);
		today.set(Calendar.DATE, Integer.parseInt(date.substring(6, 8)));
		today.set(Calendar.HOUR_OF_DAY, Integer.parseInt(date.substring(8, 10)));
		today.set(Calendar.MINUTE, Integer.parseInt(date.substring(10, 12)));
		today.set(Calendar.SECOND, 0);
		today.add(Calendar.MINUTE, -minute);
		return new Date(today.getTimeInMillis()).getTime();
	}
	
	
	/******************************************************************************
	 * 
	 * 함수명 : getAlramTime 인자 : 리턴값 : 설명 : 지정한 시간에서 minute 지난 시간을 리턴
	 * 
	 ******************************************************************************/
	public static Long getAlramTimeS(String date, int Second) {
		Calendar today = Calendar.getInstance();
		today.set(Calendar.YEAR, Integer.parseInt(date.substring(0, 4)));
		today.set(Calendar.MONTH, Integer.parseInt(date.substring(4, 6))-1);
		today.set(Calendar.DATE, Integer.parseInt(date.substring(6, 8)));
		today.set(Calendar.HOUR_OF_DAY, Integer.parseInt(date.substring(8, 10)));
		today.set(Calendar.MINUTE, Integer.parseInt(date.substring(10, 12)));
		today.set(Calendar.SECOND, +Second);
		return new Date(today.getTimeInMillis()).getTime();
	}
	
	/******************************************************************************
	 * 
	 * 함수명 : getTimeL 인자 : 리턴값 : 설명 : 지정한 시간에서 minute 지난 시간을 리턴
	 * 
	 ******************************************************************************/
	public static Long getTimeL(String date, int minute) {
		Calendar today = Calendar.getInstance();
		today.set(Calendar.YEAR, Integer.parseInt(date.substring(0, 4)));
		today.set(Calendar.MONTH, Integer.parseInt(date.substring(4, 6))-1);
		today.set(Calendar.DATE, Integer.parseInt(date.substring(6, 8)));
		today.set(Calendar.HOUR_OF_DAY, Integer.parseInt(date.substring(8, 10)));
		today.set(Calendar.MINUTE, Integer.parseInt(date.substring(10, 12)));
		today.set(Calendar.SECOND, 0);
		today.add(Calendar.MINUTE, -minute);
		
		return new Date(today.getTimeInMillis()).getTime();
	}
	
	/******************************************************************************
	 * 
	 * 함수명 : getTimeL 인자 : 리턴값 : 설명 : 지정한 시간에서 minute 지난 시간을 리턴
	 * 
	 ******************************************************************************/
	public static Long getTimeHL(String date, int houre) {
		Calendar today = Calendar.getInstance();
		today.set(Calendar.YEAR, Integer.parseInt(date.substring(0, 4)));
		today.set(Calendar.MONTH, Integer.parseInt(date.substring(4, 6))-1);
		today.set(Calendar.DATE, Integer.parseInt(date.substring(6, 8)));
		today.set(Calendar.HOUR_OF_DAY, Integer.parseInt(date.substring(8, 10)));
		today.set(Calendar.MINUTE, Integer.parseInt(date.substring(10, 12)));
		today.set(Calendar.SECOND, 0);
		today.add(Calendar.HOUR_OF_DAY, houre);
		
		return new Date(today.getTimeInMillis()).getTime();
	}
	
		
	/******************************************************************************
	 * 
	 * 함수명 : getTimes 인자 : 리턴값 : 설명 : 지정한 시간에서 minute 지난 시간을 리턴
	 * 
	 ******************************************************************************/
	public static String getTimes(String date, int day) {
		SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		Calendar today = Calendar.getInstance();
		today.set(Calendar.YEAR, Integer.parseInt(date.substring(0, 4)));
		today.set(Calendar.MONTH, Integer.parseInt(date.substring(4, 6))-1);
		if(day == 0 ){
			today.set(Calendar.DATE, Integer.parseInt(date.substring(6, 8)) + -1);
			today.set(Calendar.HOUR_OF_DAY, 23);
			today.set(Calendar.MINUTE,59);
		}else{
			today.set(Calendar.DATE, Integer.parseInt(date.substring(6, 8)) + day);
			today.set(Calendar.HOUR_OF_DAY, 0);
			today.set(Calendar.MINUTE,0);
		}
		today.set(Calendar.SECOND, 0);
		
		return timeStampFormat.format(new Date(today.getTimeInMillis()));
	}
	
	/******************************************************************************
	 * 
	 * 함수명 : getTimes 인자 : 리턴값 : 설명 : 지정한 시간에서 minute 지난 시간을 리턴
	 * 
	 ******************************************************************************/
	public static Long getTimes1(String date, int day) {
		SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		Calendar today = Calendar.getInstance();
		today.set(Calendar.YEAR, Integer.parseInt(date.substring(0, 4)));
		today.set(Calendar.MONTH, Integer.parseInt(date.substring(4, 6))-1);
		if(day == 0 ){
			today.set(Calendar.DATE, Integer.parseInt(date.substring(6, 8)) + -1);
			today.set(Calendar.HOUR_OF_DAY, 23);
			today.set(Calendar.MINUTE,59);
		}else{
			today.set(Calendar.DATE, Integer.parseInt(date.substring(6, 8)) + day);
			today.set(Calendar.HOUR_OF_DAY, 0);
			today.set(Calendar.MINUTE,0);
		}
		today.set(Calendar.SECOND, 0);
		
		return new Date(today.getTimeInMillis()).getTime();
	}
	
	/******************************************************************************
	 * 
	 * 함수명 : getTimes 인자 : 리턴값 : 설명 : 지정한 시간에서 minute 지난 시간을 리턴
	 * 
	 ******************************************************************************/
	public static String getTimeM(String date, int month) {
		SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		Calendar today = Calendar.getInstance();
		today.set(Calendar.YEAR, Integer.parseInt(date.substring(0, 4)));
		today.set(Calendar.MONTH, Integer.parseInt(date.substring(4, 6))-1 + month);
		today.set(Calendar.DATE,1);
		today.set(Calendar.HOUR_OF_DAY, 0);
		today.set(Calendar.MINUTE,0);
		today.set(Calendar.SECOND, 0);
		//today.add(Calendar.DATE, month);
		
		return timeStampFormat.format(new Date(today.getTimeInMillis()));
	}
	
	
	/******************************************************************************
	 * 
	 * 함수명 : getTimes 인자 : 리턴값 : 설명 : 지정한 시간에서 minute 지난 시간을 리턴
	 * 
	 ******************************************************************************/
	public static Long getTimeM1(String date, int month) {
		SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		Calendar today = Calendar.getInstance();
		today.set(Calendar.YEAR, Integer.parseInt(date.substring(0, 4)));
		today.set(Calendar.MONTH, Integer.parseInt(date.substring(4, 6))-1 + month);
		today.set(Calendar.DATE,1);
		today.set(Calendar.HOUR_OF_DAY, 0);
		today.set(Calendar.MINUTE,0);
		today.set(Calendar.SECOND, 0);
		today.add(Calendar.DATE, month);
		CustomLog.L("getTimeM1", timeStampFormat.format(new Date(today.getTimeInMillis())));
		return new Date(today.getTimeInMillis()).getTime();
	}
	
	
	/******************************************************************************
	 * 
	 * 함수명 : getTimes 인자 : 리턴값 : 설명 : 지정한 시간에서 minute 지난 시간을 리턴
	 * 
	 ******************************************************************************/
	public static Long getTimeH(String date, int houre) {
		SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		Calendar today = Calendar.getInstance();
		today.set(Calendar.YEAR, Integer.parseInt(date.substring(0, 4)));
		today.set(Calendar.MONTH, Integer.parseInt(date.substring(4, 6))-1);
		today.set(Calendar.DATE, Integer.parseInt(date.substring(6, 8)));
		today.set(Calendar.HOUR_OF_DAY, Integer.parseInt(date.substring(8, 10)));
		today.set(Calendar.MINUTE, Integer.parseInt(date.substring(10, 12)));
		today.set(Calendar.SECOND, 0);
		today.add(Calendar.HOUR_OF_DAY, houre);
		CustomLog.L("getTimeM1", timeStampFormat.format(new Date(today.getTimeInMillis())));
		return new Date(today.getTimeInMillis()).getTime();
	}
	
	/******************************************************************************
	 * 
	 * 함수명 : getTimes 인자 : 리턴값 : 설명 : 지정한 시간에서 minute 지난 시간을 리턴
	 * 
	 ******************************************************************************/
	public static String getTimeStrH(String date, int houre) {
		SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		Calendar today = Calendar.getInstance();
		today.set(Calendar.YEAR, Integer.parseInt(date.substring(0, 4)));
		today.set(Calendar.MONTH, Integer.parseInt(date.substring(4, 6))-1);
		today.set(Calendar.DATE, Integer.parseInt(date.substring(6, 8)));
		today.set(Calendar.HOUR_OF_DAY, Integer.parseInt(date.substring(8, 10)));
		today.set(Calendar.MINUTE, Integer.parseInt(date.substring(10, 12)));
		today.set(Calendar.SECOND, 0);
		today.add(Calendar.HOUR_OF_DAY, houre);
		CustomLog.L("getTimeM1", timeStampFormat.format(new Date(today.getTimeInMillis())));
		return timeStampFormat.format(new Date(today.getTimeInMillis()));
	}
	
	
	/******************************************************************************
	 * 
	 * Function : DateLib Argument : Return : Description : 생성자
	 * 
	 ******************************************************************************/
	public DateLib() {
	}

	/******************************************************************************
	 * 
	 * 함수명 : getCurrentYear 인자 : 리턴값 : 설명 : 현재 년도 리턴
	 * 
	 ******************************************************************************/
	public static int getCurrentYear() {
		Calendar today = Calendar.getInstance();
		return (today.get(Calendar.YEAR));
	}

	/******************************************************************************
	 * 
	 * 함수명 : getCurrentMonth 인자 : 리턴값 : 설명 : 현재 월 리턴
	 * 
	 ******************************************************************************/
	public static int getCurrentMonth() {
		Calendar today = Calendar.getInstance();
		return (today.get(Calendar.MONTH) - Calendar.JANUARY + 1);
	}
	
	/******************************************************************************
	 * 
	 * 함수명 : getCurrentMonth 인자 : 리턴값 : 설명 : 현재 월 리턴
	 * 
	 ******************************************************************************/
	public static int getCurrentMonth(String date) {
		Calendar today = Calendar.getInstance();
		
		today.set(Calendar.YEAR, Integer.parseInt(date.substring(0, 4)));
		today.set(Calendar.MONTH, Integer.parseInt(date.substring(4, 6))-1);
		return (today.get(Calendar.MONTH) - Calendar.JANUARY + 1);
	}
	
	/******************************************************************************
	 * 
	 * 함수명 : getCurrentMonth 인자 : 리턴값 : 설명 : 현재 월 리턴
	 * 
	 ******************************************************************************/
	public static int getCurrentMonth(int month) {
		Calendar today = Calendar.getInstance();
		today.set(Calendar.MONTH, month-1);
		today.set(Calendar.DATE, 1);
		//today.getTimeInMillis()
		return (today.get(Calendar.MONTH) - Calendar.JANUARY + 1);
	}
	
	/******************************************************************************
	 * 
	 * 함수명 : getCurrentMonth 인자 : 리턴값 : 설명 : 현재 월 리턴
	 * 
	 ******************************************************************************/
	public static Long getCurrentMonthTime(String date) {
		Calendar today = Calendar.getInstance();
		today.set(Calendar.YEAR, Integer.parseInt(date.substring(0, 4)));
		today.set(Calendar.MONTH, Integer.parseInt(date.substring(4, 6))-1);
		today.set(Calendar.DATE, 1);
		today.set(Calendar.HOUR, 0);
		today.set(Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 1);
		return today.getTimeInMillis();
	}
	
	/******************************************************************************
	 * 
	 * 함수명 : getCurrentMonth 인자 : 리턴값 : 설명 : 현재 월 리턴
	 * 
	 ******************************************************************************/
	public static Long getCurrentMonthTime2(String date) {
		Calendar today = Calendar.getInstance();
		today.set(Calendar.YEAR, Integer.parseInt(date.substring(0, 4)));
		today.set(Calendar.MONTH, Integer.parseInt(date.substring(4, 6))-1);
		today.set(Calendar.DATE, Integer.parseInt(date.substring(6, 8)));
		today.set(Calendar.HOUR, 0);
		today.set(Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 1);
		return today.getTimeInMillis();
	}

	/******************************************************************************
	 * 
	 * 함수명 : getCurrentWeek 인자 : 리턴값 : 설명 : 현재 주 리턴
	 * 
	 ******************************************************************************/
	public static int getCurrentWeek() {
		Calendar today = Calendar.getInstance();
		return (today.get(Calendar.WEEK_OF_MONTH));
	}

	/******************************************************************************
	 * 
	 * 함수명 : getCurrentWeekOfYear 인자 : 리턴값 : 설명 : 현재 주 리턴
	 * 
	 ******************************************************************************/
	public static int getCurrentWeekOfYear() {
		Calendar today = Calendar.getInstance();
		return (today.get(Calendar.WEEK_OF_YEAR));
	}

	/******************************************************************************
	 * 
	 * 함수명 : getCurrentDay 인자 : 리턴값 : 설명 : 현재 날짜 리턴
	 * 
	 ******************************************************************************/
	public static int getCurrentDay() {
		Calendar today = Calendar.getInstance();
		return (today.get(Calendar.DATE));
	}

	/******************************************************************************
	 * 
	 * 함수명 : getCurrentHour 인자 : 리턴값 : 설명 : 현재 시(hour) 리턴
	 * 
	 ******************************************************************************/
	public static int getCurrentHour() {
		Calendar today = Calendar.getInstance();
		return (today.get(Calendar.HOUR_OF_DAY));
	}

	/******************************************************************************
	 * 
	 * 함수명 : getCurrentMinute 인자 : 리턴값 : 설명 : 현재 분(minute) 리턴
	 * 
	 ******************************************************************************/
	public static int getCurrentMinute() {
		Calendar today = Calendar.getInstance();
		return (today.get(Calendar.MINUTE));
	}

	/******************************************************************************
	 * 
	 * 함수명 : getCurrentDayOfWeek 인자 : 리턴값 : 설명 : 현재 요일 리턴
	 * 
	 ******************************************************************************/
	public static int getCurrentDayOfWeek() {
		Calendar today = Calendar.getInstance();
		return (today.get(Calendar.DAY_OF_WEEK));
	}

	/******************************************************************************
	 * 
	 * 함수명 : getWeek 인자 : 리턴값 : 설명 : 특정일의 주 리턴
	 * 
	 ******************************************************************************/
	public static int getWeek(int year, int month, int day) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month - 1);
		cal.set(Calendar.DATE, day);
		return (cal.get(Calendar.WEEK_OF_MONTH));
	}

	/******************************************************************************
	 * 
	 * 함수명 : getWeekOfYear 인자 : 리턴값 : 설명 : 특정일의 주 리턴
	 * 
	 ******************************************************************************/
	public static int getWeekOfYear(int year, int month, int day) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month - 1);
		cal.set(Calendar.DATE, day);
		return (cal.get(Calendar.WEEK_OF_YEAR));
	}

	/******************************************************************************
	 * 
	 * 함수명 : getMaxWeekOfYear 인자 : 리턴값 : 설명 : 특정 년도가 몇 주로 이루어져 있는지 리턴
	 * 
	 ******************************************************************************/
	public static int getMaxWeekOfYear(int year) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		return (cal.getActualMaximum(Calendar.WEEK_OF_YEAR));
	}

	/******************************************************************************
	 * 
	 * 함수명 : getFirstDayOfWeek 인자 : 리턴값 : 설명 : 해당 주의 첫번째 요일의 일자를 리턴
	 * 
	 ******************************************************************************/
	public static int getFirstDayOfWeek(int year, int month, int week) {
		int nWeek = 1;
		// Calendar cal = Calendar.getInstance();
		if (week == 1)
			return (1);
		for (int i = 1; i <= getLastDayOfMonth(year, month); i++) {
			if (i != 1 && getDayOfWeekKor(year, month, i).equals("일")) {
				nWeek++;
				if (nWeek == week)
					return (i);
			}
		}
		return (0);
	}

	/******************************************************************************
	 * 
	 * 함수명 : getCurrentDayOfWeekKor 인자 : 리턴값 : 설명 : 현재 요일(한글) 리턴
	 * 
	 ******************************************************************************/
	public static String getCurrentDayOfWeekKor(String date) {
		String dayKor = "";
		Calendar today = Calendar.getInstance();
		today.set(Calendar.YEAR, Integer.parseInt(date.substring(0, 4)));
		today.set(Calendar.MONTH, Integer.parseInt(date.substring(4, 6))-1);
		today.set(Calendar.DATE, Integer.parseInt(date.substring(6, 8)));
		switch (today.get(Calendar.DAY_OF_WEEK)) {
		case Calendar.MONDAY: {
			dayKor = "월";
			break;
		}
		case Calendar.TUESDAY: {
			dayKor = "화";
			break;
		}
		case Calendar.WEDNESDAY: {
			dayKor = "수";
			break;
		}
		case Calendar.THURSDAY: {
			dayKor = "목";
			break;
		}
		case Calendar.FRIDAY: {
			dayKor = "금";
			break;
		}
		case Calendar.SATURDAY: {
			dayKor = "토";
			break;
		}
		case Calendar.SUNDAY: {
			dayKor = "일";
			break;
		}
		}
		return (dayKor);
	}

	
	/******************************************************************************
	 * 
	 * 함수명 : getCurrentDayOfWeekKor 인자 : 리턴값 : 설명 : 현재 요일(한글) 리턴
	 * 
	 ******************************************************************************/
	public static String getCurrentDayOfWeekKor() {
		String dayKor = "";
		Calendar today = Calendar.getInstance();
		switch (today.get(Calendar.DAY_OF_WEEK)) {
		case Calendar.MONDAY: {
			dayKor = "월";
			break;
		}
		case Calendar.TUESDAY: {
			dayKor = "화";
			break;
		}
		case Calendar.WEDNESDAY: {
			dayKor = "수";
			break;
		}
		case Calendar.THURSDAY: {
			dayKor = "목";
			break;
		}
		case Calendar.FRIDAY: {
			dayKor = "금";
			break;
		}
		case Calendar.SATURDAY: {
			dayKor = "토";
			break;
		}
		case Calendar.SUNDAY: {
			dayKor = "일";
			break;
		}
		}
		return (dayKor);
	}

	/******************************************************************************
	 * 
	 * 함수명 : getCurrentDayOfWeekEn 인자 : 리턴값 : 설명 : 현재 요일(영문약자) 리턴
	 * 
	 ******************************************************************************/
	public static String getCurrentDayOfWeekEn() {
		String dayEn = "";
		Calendar today = Calendar.getInstance();
		switch (today.get(Calendar.DAY_OF_WEEK)) {
		case Calendar.MONDAY: {
			dayEn = "MON.";
			break;
		}
		case Calendar.TUESDAY: {
			dayEn = "TUE.";
			break;
		}
		case Calendar.WEDNESDAY: {
			dayEn = "WED.";
			break;
		}
		case Calendar.THURSDAY: {
			dayEn = "THU.";
			break;
		}
		case Calendar.FRIDAY: {
			dayEn = "FRI.";
			break;
		}
		case Calendar.SATURDAY: {
			dayEn = "SAT.";
			break;
		}
		case Calendar.SUNDAY: {
			dayEn = "SUN.";
			break;
		}
		}
		return (dayEn);
	}

	/******************************************************************************
	 * 
	 * 함수명 : getDayOfWeek 인자 : 리턴값 : 설명 : 특정일의 요일 리턴
	 * 
	 ******************************************************************************/
	public static int getDayOfWeek(int year, int month, int day) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month - 1);
		cal.set(Calendar.DATE, day);
		return (cal.get(Calendar.DAY_OF_WEEK));
	}

	/******************************************************************************
	 * 
	 * 함수명 : getDayOfWeekKor 인자 : 리턴값 : 설명 : 특정일의 요일(한글) 리턴
	 * 
	 ******************************************************************************/
	public static String getDayOfWeekKor(int year, int month, int day) {
		String dayKor = "";
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month - 1);
		cal.set(Calendar.DATE, day);
		switch (cal.get(Calendar.DAY_OF_WEEK)) {
		case Calendar.MONDAY: {
			dayKor = "월";
			break;
		}
		case Calendar.TUESDAY: {
			dayKor = "화";
			break;
		}
		case Calendar.WEDNESDAY: {
			dayKor = "수";
			break;
		}
		case Calendar.THURSDAY: {
			dayKor = "목";
			break;
		}
		case Calendar.FRIDAY: {
			dayKor = "금";
			break;
		}
		case Calendar.SATURDAY: {
			dayKor = "토";
			break;
		}
		case Calendar.SUNDAY: {
			dayKor = "일";
			break;
		}
		}
		return (dayKor);
	}
	
	/******************************************************************************
	 * 
	 * 함수명 : getDayOfWeekKor 인자 : 리턴값 : 설명 : 특정일의 요일(한글) 리턴
	 * @param date = yyyyMMdd
	 * 
	 ******************************************************************************/
	public static String getDayOfWeekKor(String date) {
		String dayKor = "";
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, Integer.parseInt(date.substring(0, 4)) );
		cal.set(Calendar.MONTH, (Integer.parseInt(date.substring(4, 6))-1) );
		cal.set(Calendar.DATE, Integer.parseInt(date.substring(6, 8)));
		switch (cal.get(Calendar.DAY_OF_WEEK)) {
		case Calendar.MONDAY: {
			dayKor = "월요일";
			break;
		}
		case Calendar.TUESDAY: {
			dayKor = "화요일";
			break;
		}
		case Calendar.WEDNESDAY: {
			dayKor = "수요일";
			break;
		}
		case Calendar.THURSDAY: {
			dayKor = "목요일";
			break;
		}
		case Calendar.FRIDAY: {
			dayKor = "금요일";
			break;
		}
		case Calendar.SATURDAY: {
			dayKor = "토요일";
			break;
		}
		case Calendar.SUNDAY: {
			dayKor = "일요일";
			break;
		}
		}
		return (dayKor);
	}

	/******************************************************************************
	 * 
	 * 함수명 : getDayOfWeekEn 인자 : 리턴값 : 설명 : 특정일의 요일(영문) 리턴
	 * 
	 ******************************************************************************/
	public static String getDayOfWeekEn(int year, int month, int day) {
		String dayKor = "";
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month - 1);
		cal.set(Calendar.DATE, day);
		switch (cal.get(Calendar.DAY_OF_WEEK)) {
		case Calendar.MONDAY: {
			dayKor = "MON.";
			break;
		}
		case Calendar.TUESDAY: {
			dayKor = "TUE.";
			break;
		}
		case Calendar.WEDNESDAY: {
			dayKor = "WED.";
			break;
		}
		case Calendar.THURSDAY: {
			dayKor = "THU.";
			break;
		}
		case Calendar.FRIDAY: {
			dayKor = "FRI.";
			break;
		}
		case Calendar.SATURDAY: {
			dayKor = "SAT.";
			break;
		}
		case Calendar.SUNDAY: {
			dayKor = "SUN.";
			break;
		}
		}
		return (dayKor);
	}

	/******************************************************************************
	 * 
	 * 함수명 : getLastDayOfMonth 인자 : 리턴값 : 설명 : 해당월의 마지막 날짜를 리턴
	 * 
	 ******************************************************************************/
	public static int getLastDayOfMonth(int year, int month) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month - 1);
		cal.set(Calendar.DATE, 1);
		return (cal.getActualMaximum(Calendar.DAY_OF_MONTH));
	}

	/******************************************************************************
	 * 
	 * 함수명 : getCurrentDate 인자 : 리턴값 : 설명 : 0000-00-00 타입 리턴
	 * 
	 ******************************************************************************/
	public static String getCurrentDate() {
		SimpleDateFormat tFormatter = new SimpleDateFormat("yyyy-MM-dd");
		String currentDate = tFormatter.format(new java.util.Date());
		return (currentDate);
	}

	/******************************************************************************
	 *
	 * 함수명 : getNullDate 인자 : 리턴값 : 설명 : 0000-00-00 리턴
	 *
	 ******************************************************************************/
	public static String getNullDate(String DB_TYPE) {
		String retDateTime;
		if (DB_TYPE.equals("ORACLE") || DB_TYPE.equals("MSSQL"))
			retDateTime = "1900-01-01";
		else
			retDateTime = "0000-00-00";
		return (retDateTime);
	}

	/******************************************************************************
	 *
	 * 함수명 : getCurrentDateTime 인자 : 리턴값 : 설명 : 0000-00-00 00:00:00 타입 리턴
	 *
	 ******************************************************************************/
	public static String getCurrentDateTime() {
		SimpleDateFormat tFormatter = new SimpleDateFormat(
				"yyyyMMddHHmmss");
		String currentDate = tFormatter.format(new java.util.Date());
		return (currentDate);
	}

	/******************************************************************************
	 * 
	 * 함수명 : getNullDateTime 인자 : 리턴값 : 설명 : 0000-00-00 00:00:00 리턴
	 * 
	 ******************************************************************************/
	public static String getNullDateTime(String DB_TYPE) {
		String retDateTime;
		if (DB_TYPE.equals("ORACLE") || DB_TYPE.equals("MSSQL"))
			retDateTime = "1900-01-01 00:00:00";
		else
			retDateTime = "0000-00-00 00:00:00";
		return (retDateTime);
	}

	/******************************************************************************
	 * 
	 * 함수명 : setDBDate 인자 : 리턴값 : 설명 : 0000-00-00 타입
	 * 
	 ******************************************************************************/
	public static String setDBDate(String date) {
		String formattedDate = date;
		try {
			if (formattedDate.length() == 8) {
				formattedDate = formattedDate.substring(0, 4) + "-"
						+ formattedDate.substring(4, 6) + "-"
						+ formattedDate.substring(6, 8);
			} else if (formattedDate.equals("0000-00-00")
					|| formattedDate.equals("")) {
				formattedDate = "0000-00-00";
			} else {
				SimpleDateFormat tFormatter = new SimpleDateFormat("yyyy-MM-dd");
				formattedDate = tFormatter.format(tFormatter
						.parse(formattedDate));
			}
		} catch (Exception e) {
			int year = 0, mon = 0, day = 0;
			int n = 0;
			StringTokenizer token = new StringTokenizer(formattedDate, "-/:. ");
			while (token.hasMoreTokens()) {
				n++;
				int val = Integer.parseInt(token.nextToken());
				switch (n) {
				case 1: {
					year = val;
					break;
				}
				case 2: {
					mon = val;
					break;
				}
				case 3: {
					day = val;
					break;
				}
				}
				formattedDate = new DecimalFormat("0000").format(year) + "-"
						+ new DecimalFormat("00").format(mon) + "-"
						+ new DecimalFormat("00").format(day);
			}
		}
		return (formattedDate);
	}

	/******************************************************************************
	 * 
	 * 함수명 : setDBDateTime 인자 : 리턴값 : 설명 : 0000-00-00 00:00:00 타입
	 * 
	 ******************************************************************************/
	public static String setDBDateTime(String dateTime) {
		String formattedDateTime = dateTime;
		try {
			if (formattedDateTime.equals("0000-00-00 00:00:00")
					|| formattedDateTime.equals("")) {
				formattedDateTime = "0000-00-00 00:00:00";
			} else {
				SimpleDateFormat tFormatter = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				formattedDateTime = tFormatter.format(tFormatter
						.parse(formattedDateTime));
			}
		} catch (Exception e) {
			int year = 0, mon = 0, day = 0, hour = 0, min = 0, sec = 0;
			int n = 0;
			StringTokenizer token = new StringTokenizer(formattedDateTime,
					"-/:. ");
			while (token.hasMoreTokens()) {
				n++;
				int val = Integer.parseInt(token.nextToken());
				switch (n) {
				case 1: {
					year = val;
					break;
				}
				case 2: {
					mon = val;
					break;
				}
				case 3: {
					day = val;
					break;
				}
				case 4: {
					hour = val;
					break;
				}
				case 5: {
					min = val;
					break;
				}
				case 6: {
					sec = val;
					break;
				}
				}
				formattedDateTime = new DecimalFormat("0000").format(year)
						+ "-" + new DecimalFormat("00").format(mon) + "-"
						+ new DecimalFormat("00").format(day) + " "
						+ new DecimalFormat("00").format(hour) + ":"
						+ new DecimalFormat("00").format(min) + ":"
						+ new DecimalFormat("00").format(sec);
			}
		}
		return (formattedDateTime);
	}

	/******************************************************************************
	 * 
	 * 함수명 : getDiffDays 인자 : 리턴값 : 설명 : 두 날짜간의 차이 return
	 * 
	 ******************************************************************************/
	public static int getDiffDays(int year1, int month1, int day1, int year2,
			int month2, int day2) {
		GregorianCalendar start = new GregorianCalendar(year1, month1 - 1, day1);
		GregorianCalendar end = new GregorianCalendar(year2, month2 - 1, day2);
		int days = 0;
		while (start.getTime().compareTo(end.getTime()) < 0) {
			start.add(Calendar.DAY_OF_MONTH, 1);
			days++;
		}
		return (days);
	}

	/******************************************************************************
	 * 
	 * 함수명 : getNowDiffDayTimes 인자 : 리턴값 : 설명 : 현재시간에서 경과시간 return
	 * 
	 ******************************************************************************/
	public static String[] getNowDiffDayTimes(int year, int month, int day,
                                              int hour, int min, int sec) {
		Calendar dayTime = Calendar.getInstance();
		dayTime.set(year, month - 1, day, hour, min, sec);

		long time = (System.currentTimeMillis() - dayTime.getTimeInMillis()) / 1000;

		return (getSecToDayTime(time));
	}

	/******************************************************************************
	 * 
	 * 함수명 : getSecToTime 인자 : 리턴값 : 설명 : 초를 시간으로 return
	 * 
	 ******************************************************************************/
	public static String[] getSecToTime(long time) {
		String[] formatTime = new String[2];

		// 경과 시간
		long h = time / 60 / 60;
		time = time - h * 60 * 60;
		// 경과 분
		long m = time / 60;

		formatTime[0] = Long.toString(h);
		formatTime[1] = Long.toString(m);

		return (formatTime);
	}

	/******************************************************************************
	 * 
	 * 함수명 : getNowDiffDayTimesLong 인자 : 리턴값 : 설명 : 현재시간에서 경과시간 return (long)
	 * 
	 ******************************************************************************/
	public static long getNowDiffDayTimesLong(int year, int month, int day,
			int hour, int min, int sec) {
		Calendar dayTime = Calendar.getInstance();
		dayTime.set(year, month - 1, day, hour, min, sec);

		long time = (System.currentTimeMillis() - dayTime.getTimeInMillis()) / 1000;

		return (time);
	}

	/******************************************************************************
	 * 
	 * 함수명 : getDayToSecTime 인자 : 리턴값 : 설명 : 날짜를 초로 리턴 return
	 * 
	 ******************************************************************************/
	public static long getDayToSecTime(int year, int month, int day, int hour,
			int min, int sec) {
		Calendar dayTime = Calendar.getInstance();
		dayTime.set(year, month - 1, day, hour, min, sec);

		return (dayTime.getTimeInMillis());
	}

	/******************************************************************************
	 * 
	 * 함수명 : getSecToDayTime 인자 : 리턴값 : 설명 : 초를 일,시간으로 return
	 * 
	 ******************************************************************************/
	public static String[] getSecToDayTime(long time) {
		String[] formatTime = new String[3];

		// 경과 일자
		long d = time / 60 / 60 / 24;
		time = time - d * 60 * 60 * 24;
		// 경과 시간
		long h = time / 60 / 60;
		time = time - h * 60 * 60;
		// 경과 분
		long m = time / 60;

		formatTime[0] = Long.toString(d);
		formatTime[1] = Long.toString(h);
		formatTime[2] = Long.toString(m);

		return (formatTime);
	}

	/******************************************************************************
	 * 
	 * 함수명 : getDateFromNWeek 인자 : 리턴값 : 설명 : year,WEEK_OF_YEAR,요일을 통해 특정일을 구함
	 * 
	 ******************************************************************************/
	public static Date getDateFromNWeek(int year, int nWeek, int i) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.WEEK_OF_YEAR, nWeek);
		switch (i) {
		case 0: {
			cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
			break;
		}
		case 1: {
			cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
			break;
		}
		case 2: {
			cal.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
			break;
		}
		case 3: {
			cal.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
			break;
		}
		case 4: {
			cal.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
			break;
		}
		case 5: {
			cal.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
			break;
		}
		case 6: {
			cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
			break;
		}
		}
		return (new Date(cal.get(Calendar.YEAR) - 1900,
				cal.get(Calendar.MONTH), cal.get(Calendar.DATE)));
	}

	

	/**
	 * Date타입을 (yyyy-MM-dd)형식으로 가져옴
	 * 
	 * @param date
	 * @return
	 */
	public static String getDate(Date date) {
		return getDate(date, "yyyy-MM-dd");
	}

	/**
	 * Date타입을 format 형식으로 가져옴(SimpleDateFormat 형식) 포맷 예) 'yyyy/MM/dd',
	 * 'yyyy-MM-dd HH:mm:ss'
	 * 
	 * @param date
	 * @param format
	 * @return
	 */
	public static String getDate(Date date, String format) {
		if (date == null)
			return "";

		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);

	}

}