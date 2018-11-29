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
import com.skt.pe.widget.wheelview.OnScrollStopListener;
import com.skt.pe.widget.wheelview.WheelView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class WheelTimePickerDialogHelper implements OnScrollStopListener {
	private final String TAG = "WheelTimePickerDialogHelper";
//	private View view;
	private TextView title = null;
	private WheelView wvHour, wvMin; //, wvAmPm;
	private Date currentDate;
	
	private String[] ampmData, hourData, minData;
	
	// 2015-03-09 Join 추가 시작 - 갤럭시 노트4용 selection 이상 처리를 위한 변수 추가
	Context context;
	Display display;
	// 2015-03-09 Join 추가 끝
	
	public WheelTimePickerDialogHelper(View view, Date initDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(initDate);

		int initHour = cal.get(Calendar.HOUR_OF_DAY);
		int initMin = cal.get(Calendar.MINUTE);
		
		Log.d(TAG, "initData: " + initHour + ":" + initMin);
		
//		int initAmPm = cal.get(Calendar.AM_PM) == Calendar.AM ? 0 : 1;
		
		loadWheelData();
		
		// 2015-03-09 Join 수정 시작 - 갤럭시 노트4용 selection 수정을 위한 구문
		context = view.getContext();
		display = ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		// 2015-03-09 Join 수정 끝
		
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		int pixels = (int) ((metrics.density * WheelSpec.height) + 0.5f);
		
		title = (TextView) view.findViewById(R.id.title);
		
		wvHour = (WheelView)view.findViewById(R.id.WHEEL_HOUR);
		wvHour.setRowHeight(pixels);
		wvHour.setRowNum(WheelSpec.wheelRowNum);
		wvHour.setTextSize(WheelSpec.textSize);
		wvHour.setItems(hourData);
		
		// 2015-03-09 Join 수정 시작 - 갤럭시 노트4용 selection 수정
		/*if(!(display.getWidth() == 1440 && display.getHeight() == 2560)) {
			wvHour.setSelection(initHour - 1);
		}
		else {
			wvHour.setSelection(initHour);
		}*/
		wvHour.setSelection(initHour - 1);
		// 2015-03-09 Join 수정 끝
		wvHour.setOnScrollStopListener(this);
		
		wvMin = (WheelView)view.findViewById(R.id.WHEEL_MIN);
		wvMin.setRowHeight(pixels);
		wvMin.setRowNum(WheelSpec.wheelRowNum);
		wvMin.setTextSize(WheelSpec.textSize);
		wvMin.setRepeat(false);
		wvMin.setItems(minData);
		// 2015-03-09 Join 수정 시작 - 갤럭시 노트4용 selection 수정
		/*if(!(display.getWidth() == 1440 && display.getHeight() == 2560)) {
			wvMin.setSelection(initMin < 30 ? 0 : 1);
		}
		else {
			int minIndex = initMin < 30 ? 0 : 1;
			wvMin.setSelection(minIndex + 1);
		}*/
//		wvMin.setSelection(initMin < 30 ? 0 : 1);
		
//		wvMin.setSelection(initMin < 30 ? 0 : 1);
		//10분 단위 추가
		wvMin.setSelection(initMin%10 == 0 ? initMin : ((initMin/10)+1)*10 );
		// 2015-03-09 Join 수정 끝
		
		wvMin.setOnScrollStopListener(this);
		/*
		wvAmPm = (WheelView)view.findViewById(R.id.WHEEL_AMPM);
		wvAmPm.setRowHeight(WheelSpec.height);
		wvAmPm.setRowNum(WheelSpec.wheelRowNum);
		wvAmPm.setTextSize(WheelSpec.textSize);
		wvAmPm.setRepeat(false);
		wvAmPm.setItems(ampmData);
		wvAmPm.setSelection(initAmPm);
		wvAmPm.setOnScrollStopListener(this);
		*/
//		setCurrentDate(initHour, initMin < 30 ? 0 : 30);
		//10분 단위 추가
		setCurrentDate(initHour, initMin%10 == 0 ? initMin : ((initMin/10)+1)*10);
		
	}
	
	@Override
	public void onScrollStop(AbsListView view, int arg1) {
		int hour = 0;
		int min = 0;
		
		// 2015-03-09 Join 수정 시작 - 갤럭시 노트4용 selection 수정에 따른 날짜 추출방법 수정
		/*if(!(display.getWidth() == 1440 && display.getHeight() == 2560)) {
			hour = wvHour.getIndex();
			min = wvMin.getIndex() == 0 ? 0 : 30;
		}
		else {
			hour = wvHour.getIndex();
			min = wvMin.getIndex() < 2 ? 0 : 30;
		}*/
		hour = wvHour.getIndex();
//		min = wvMin.getIndex() == 0 ? 0 : 30;
		min = (wvMin.getIndex())*10;		//10분단위 추가
		// 2015-03-09 Join 수정 끝

		Log.d(TAG, "Selected: " + hour + ":" + min);
		Log.d(TAG, "Selected Index: " + wvHour.getIndex() + ":" + wvMin.getIndex());
		
		// 2015-03-09 Join 수정 시작 - 갤럭시 노트4용 selection 수정
		/*if(!(display.getWidth() == 1440 && display.getHeight() == 2560)) {
			setCurrentDate(hour + 1, min);
		}
		else {
			setCurrentDate(hour, min);
		}*/
		setCurrentDate(hour + 1, min);
		// 2015-03-09 Join 수정 끝
		
	}
	
	public Date getCurrentDate() {
		return this.currentDate;
	}
	
	public String toString() {
		return WheelTimePickerDialogHelper.generateDateFormat(getCurrentDate());
	}
	
	public static String generateDateFormat(Date inputDate) {
		SimpleDateFormat format = new SimpleDateFormat(DateStatus.dateFormat);
		return format.format(inputDate).toString();
	}
	
	private void loadWheelData() {
		ampmData = new String[2];
		ampmData[0] = "오전";
		ampmData[1] = "오후";
		
		hourData = new String[24];
		for(int i = 1; i < hourData.length + 1; i++) {
			hourData[i - 1] = String.format("%02d" + "시", i);
		}
		
		minData = new String[6];
		minData[0] = "00분";
		minData[1] = "10분";
		minData[2] = "20분";
		minData[3] = "30분";
		minData[4] = "40분";
		minData[5] = "50분";
	}
	
	private void setCurrentDate(int hourOfDay, int min) {
		this.currentDate = (new GregorianCalendar(0, 0, 0, hourOfDay, min, 0)).getTime();
		title.setText(WheelTimePickerDialogHelper.generateDateFormat(this.currentDate));
	}
	
	protected class WheelSpec {
		protected final static int wheelRowNum = 5;
		protected final static int textSize = 22;
		protected final static int height = 70;
	}

	protected class DateStatus {
		protected final static String dateFormat = "a hh:mm";
	}
}
