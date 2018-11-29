package com.ex.group.approval.easy.dialog;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.TextView;

import com.ex.group.folder.R;
import com.ex.group.approval.easy.util.DateUtils;
import com.skt.pe.widget.wheelview.OnScrollStopListener;
import com.skt.pe.widget.wheelview.WheelView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class WheelDatePickerDialogHelper implements OnScrollStopListener {
	private final String TAG = "WheelDatePickerDialogHelper";
//	private View view;
	private TextView title = null;
	private WheelView wvYear, wvMonth, wvDay;
	private Date currentDate;
	
	private String[] yearData, monthData, day28Data, day29Data, day30Data, day31Data;
	
	// 2015-03-09 Join 추가 시작 - 갤럭시 노트4용 selection 이상 처리를 위한 변수 추가
	Context context;
	Display display;
	// 2015-03-09 Join 추가 끝
	
	public WheelDatePickerDialogHelper(View view, Date initDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(initDate);

		int initYear = cal.get(Calendar.YEAR);
		int initMonth = cal.get(Calendar.MONTH) + 1;
		int initDay = cal.get(Calendar.DATE);
		
		fillWheelInitData();
		
		// 2015-03-09 Join 수정 시작 - 갤럭시 노트4용 selection 수정을 위한 구문
		context = view.getContext();
		display = ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		// 2015-03-09 Join 수정 끝
		
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		int pixels = (int) ((metrics.density * WheelSpec.height) + 0.5f);
		
		title = (TextView) view.findViewById(R.id.title);
		
		wvYear = (WheelView)view.findViewById(R.id.WHEEL_YEAR);
		wvYear.setRowHeight(pixels);
		wvYear.setRowNum(WheelSpec.wheelRowNum);
		wvYear.setTextSize(WheelSpec.textSize);
		wvYear.setItems(yearData);
		
		// 2015-03-09 Join 수정 시작 - 갤럭시 노트4용 selection 수정
		/*if(!(display.getWidth() == 1440 && display.getHeight() == 2560)) {
			wvYear.setSelection(initYear - DateStatus.startYear);
		}
		else {
			wvYear.setSelection(initYear - DateStatus.startYear + 1);
		}*/
		wvYear.setSelection(initYear - DateStatus.startYear);
		// 2015-03-09 Join 수정 끝
		
		wvYear.setOnScrollStopListener(this);
		
		wvMonth = (WheelView)view.findViewById(R.id.WHEEL_MONTH);
		wvMonth.setRowHeight(pixels);
		wvMonth.setRowNum(WheelSpec.wheelRowNum);
		wvMonth.setTextSize(WheelSpec.textSize);
		wvMonth.setItems(monthData);
		// 2015-03-09 Join 수정 시작 - 갤럭시 노트4용 selection 수정
		/*if(!(display.getWidth() == 1440 && display.getHeight() == 2560)) {
			wvMonth.setSelection(initMonth - 1);
		}
		else {
			wvMonth.setSelection(initMonth);
		}*/
		wvMonth.setSelection(initMonth - 1);
		// 2015-03-09 Join 수정 끝
		wvMonth.setOnScrollStopListener(this);
		
		wvDay = (WheelView)view.findViewById(R.id.WHEEL_DAY);
		wvDay.setRowHeight(pixels);
		wvDay.setRowNum(WheelSpec.wheelRowNum);
		wvDay.setTextSize(WheelSpec.textSize);
		
		switch (DateUtils.getLastDay(initYear, initMonth - 1)) {
			case 31:
				wvDay.setItems(day31Data);
				break;
			case 30:
				wvDay.setItems(day30Data);
				break;
			case 29:
				wvDay.setItems(day29Data);
				break;
			case 28:
				wvDay.setItems(day28Data);
				break;
		}
		// 2015-03-09 Join 수정 시작 - 갤럭시 노트4용 selection 수정
		/*if(!(display.getWidth() == 1440 && display.getHeight() == 2560)) {
			wvDay.setSelection(initDay - 1);
		}
		else {
			wvDay.setSelection(initDay);
		}*/
		wvDay.setSelection(initDay - 1);
		// 2015-03-09 Join 수정 끝
		wvDay.setOnScrollStopListener(this);
		
		setCurrentDate(initYear, initMonth, initDay);
		
	}
	
	@Override
	public void onScrollStop(AbsListView view, int arg1) {
		int year = 0;
		int mon = 0;
		int day = 0;
		
		// 2015-03-09 Join 수정 시작 - 갤럭시 노트4용 selection 수정에 따른 날짜 추출방법 수정
		/*if(!(display.getWidth() == 1440 && display.getHeight() == 2560)) {
			 year = wvYear.getIndex() + DateStatus.startYear;
			mon = wvMonth.getIndex();
			day = wvDay.getIndex();
		}
		else {
			year = wvYear.getIndex() - 1 + DateStatus.startYear;
			mon = wvMonth.getIndex() - 1;
			day = wvDay.getIndex();
		}*/
		year = wvYear.getIndex() + DateStatus.startYear;
		mon = wvMonth.getIndex();
		day = wvDay.getIndex();
		// 2015-03-09 Join 수정 끝
		
		// getLastDay 사용법
		// 년은 실제년을 써야함
		// 월은 실제월 - 1 값을 써야함
		if (view.getId() != R.id.WHEEL_DAY) {
			if(DateUtils.getLastDay(year, mon) == 31) {
				wvDay.setItems(day31Data);
			} else if(DateUtils.getLastDay(year, mon) == 30) {
				wvDay.setItems(day30Data);
			} else if(DateUtils.getLastDay(year, mon) == 29) {
				wvDay.setItems(day29Data);
			} else if(DateUtils.getLastDay(year, mon) == 28) {
				wvDay.setItems(day28Data);
			}
//			Log.d(TAG, "day11111 ============ " + day);
			if(day > wvDay.getCount() - 1) {
				day = wvDay.getCount() - 1;
			}
//			Log.d(TAG, "day22222 ============ " + day);
			wvDay.setSelection(day);
		}

		Log.d(TAG, "Selected: " + year + ":" + mon + ":" + day);
		// 2015-03-09 Join 수정 시작 - 갤럭시 노트4용 selection 수정에 따른 날짜 추출방법 수정
		/*if(!(display.getWidth() == 1440 && display.getHeight() == 2560)) {
			setCurrentDate(year, mon+1, day+1);
		} else {
			setCurrentDate(year, mon+1, day);
		}*/
		setCurrentDate(year, mon+1, day+1);
		// 2015-03-09 Join 수정 끝
		
	}
	
	public Date getCurrentDate() {
		return this.currentDate;
	}
	
	public static String generateDateFormat(Date inputDate) {
		SimpleDateFormat format = new SimpleDateFormat(DateStatus.dateFormat);
		return format.format(inputDate).toString();
	}
	
	public String toString() {
		return WheelDatePickerDialogHelper.generateDateFormat(getCurrentDate());
	}
	
	private void setCurrentDate(int year, int monthOfYear, int dayOfMonth) {
		this.currentDate = (new GregorianCalendar(year, monthOfYear-1, dayOfMonth)).getTime();
		title.setText(WheelDatePickerDialogHelper.generateDateFormat(this.currentDate));
	}
	
	private void fillWheelInitData() {
		yearData = new String[20];
		monthData = new String[12];
		day28Data = new String[28];
		day29Data = new String[29];
		day30Data = new String[30];
		day31Data = new String[31];
		
		for(int i = DateStatus.startYear; i < DateStatus.endYear; i++) {
			yearData[i - DateStatus.startYear] = String.format("%04d" + "년", i);
		} // end for
		for(int i = 1; i <= 12; i++) {
			monthData[i - 1] = String.format("%02d" + "월", i);
		} // end for
		for(int i = 1; i <= 31; i++) {
			if(i <= 28) {
				day28Data[i - 1] = String.format("%02d" + "일", i);
			} // end if
			if(i <= 29) {
				day29Data[i - 1] = String.format("%02d" + "일", i);
			} // end if
			if(i <= 30) {
				day30Data[i - 1] = String.format("%02d" + "일", i);
			} // end if
			day31Data[i - 1] = String.format("%02d" + "일", i);
		} // end for
	}
	
	/*
	public static class DateUtil {
	}
	*/

	protected class WheelSpec {
		protected final static int wheelRowNum = 5;
		protected final static int textSize = 22;
		protected final static int height = 70;
	}

	protected class DateStatus {
		protected final static int startYear = 2000;
		protected final static int endYear = 2020;
		protected final static String dateFormat = "yyyy년 MM월 dd일 (E)";
	}
}
