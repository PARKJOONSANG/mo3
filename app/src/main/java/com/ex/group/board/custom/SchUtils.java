package com.ex.group.board.custom;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.skt.pe.common.conf.Constants;
import com.skt.pe.common.conf.Environ;
import com.skt.pe.common.conf.Resource;
import com.skt.pe.common.data.SKTUtil;
import com.skt.pe.common.dialog.DialogButton;
import com.skt.pe.common.dialog.SKTDialog;
import com.skt.pe.common.exception.SKTException;
import com.skt.pe.util.StringUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class SchUtils {
	
	public static int StToInt(Object object){
		return (object != null && !"".equals(object.toString())) ? Integer.parseInt(object.toString()) : -1;
	}
	
	public static boolean StTobool(Object object){
		return (object != null && !"".equals(object.toString())) ? Integer.parseInt(object.toString()) == 1 ? true : false : false;
	}
	
	public static String ChkNullStr(String value){
		try{
			if(!"".equals(value)){
				if(!"null".equals(value))
					return value;
				else return "";
			}
			else return "";
		}catch(Exception e){
			return "";
		}
	}
	
	public static String ChkNullStr(String value, String def){
		try{
			if(value != null) {
				if(!"".equals(value)){
					if(!"null".equals(value))
						return value;
				}
					
			}
		}catch(Exception e){
			return def;
		}
		return def;
	}
	
	public static int ChkNullStrToInt(String value){
		try{
			if(value != null){
				return Integer.parseInt(value);
			}
			else return 0;
		}catch(Exception e){
			return 0;
		}
	}
	
    
	public static int pixelToDip(Context context, int arg )
	{
		return (int) ( arg / context.getResources( ).getDisplayMetrics( ).density );
	}
	
	public static int DipTopixel(Context context, int arg )
	{
		return (int) ( arg * context.getResources( ).getDisplayMetrics( ).density );
	}
	
	public static String initZero(int num){
		return num < 10 ? "0"+num : num+""; 
	}
	
	
	/**
	 * 
	 * @param data - ArrayList<Map<String, Object>> Value
	 * @param key  - �����ϰ��� �ϴ� Key ��
	 * @return 	   - ArrayList<Map<String, Object>>
	 */
	public static ArrayList<Map<String, Object>> sortList(ArrayList<Map<String, Object>> data, String key){
		//ArrayList<Map<String, Object>> sortdata = new ArrayList<Map<String,Object>>();
		for(int i=0;i<data.size();i++){
			Map<String, Object> temp_map = new HashMap<String, Object>();
			for(int j=i;j<data.size();j++){
				if(data.get(i).containsKey(key) && data.get(j).containsKey(key)){
					if( Long.parseLong(data.get(i).get(key).toString()) < Long.parseLong(data.get(j).get(key).toString()) ) {
						//temp_map.putAll(data.get(j));
						temp_map.putAll(data.get(j));
						data.get(j).putAll(data.get(i));
						data.get(i).putAll(temp_map);
					}
						
				}
			}
		}
		return data;
	}
	
	/**
	 * 
	 * @param data - ArrayList<HashMap<String, String>> Value
	 * @param key  - �����ϰ��� �ϴ� Key ��
	 * @return 	   - ArrayList<HashMap<String, String>>
	 */
	public static ArrayList<HashMap<String, Object>> sortListH(ArrayList<HashMap<String, Object>> data, String key){
		//ArrayList<Map<String, Object>> sortdata = new ArrayList<Map<String,Object>>();
		for(int i=0;i<data.size();i++){
			HashMap<String, Object> temp_map = new HashMap<String, Object>();
			for(int j=i;j<data.size();j++){
				if(data.get(i).containsKey(key) && data.get(j).containsKey(key)){
					if( Double.parseDouble(data.get(i).get(key).toString()) > Double.parseDouble(data.get(j).get(key).toString()) ) {
						//temp_map.putAll(data.get(j));
						temp_map.putAll(data.get(j));
						data.get(j).putAll(data.get(i));
						data.get(i).putAll(temp_map);
					}
						
				}
			}
		}
		return data;
	}
	
	public static ArrayList<HashMap<String, String>> sortListH2(ArrayList<HashMap<String, String>> data, String key){
		//ArrayList<Map<String, Object>> sortdata = new ArrayList<Map<String,Object>>();
		for(int i=0;i<data.size();i++){
			HashMap<String, String> temp_map = new HashMap<String, String>();
			for(int j=i;j<data.size();j++){
				if(data.get(i).containsKey(key) && data.get(j).containsKey(key)){
					if( Double.parseDouble(data.get(i).get(key).toString()) > Double.parseDouble(data.get(j).get(key).toString()) ) {
						//temp_map.putAll(data.get(j));
						temp_map.putAll(data.get(j));
						data.get(j).putAll(data.get(i));
						data.get(i).putAll(temp_map);
					}
						
				}
			}
		}
		return data;
	}
	/**
	 * 
	 * @param data - ArrayList<HashMap<String, String>> Value
	 * @param key  - �����ϰ��� �ϴ� Key ��
	 * @return 	   - ArrayList<HashMap<String, String>>
	 */
	public static ArrayList<HashMap<String, String>> sortListStringH(ArrayList<HashMap<String, String>> data, String key){
		//ArrayList<Map<String, Object>> sortdata = new ArrayList<Map<String,Object>>();
		for(int i=0;i<data.size();i++){
			HashMap<String, String> temp_map = new HashMap<String, String>();
			for(int j=i;j<data.size();j++){
				if(data.get(i).containsKey(key) && data.get(j).containsKey(key)){
					if( data.get(j).get(key).compareTo(data.get(i).get(key)) < 0 ) {
						//temp_map.putAll(data.get(j));
						temp_map.putAll(data.get(j));
						data.get(j).putAll(data.get(i));
						data.get(i).putAll(temp_map);
					}/*
					int []first = HangulUtils.convertStringToUnicode(data.get(i).get(key));
					int []next = HangulUtils.convertStringToUnicode(data.get(j).get(key));
					int size;
					CustomLog.L("", first.toString());
					CustomLog.L("", next.toString());
					if(first.length > next.length) size = next.length;
					else size = first.length;
					for(int k=0;k<size;k++){
						if( first[k] > next[k] ) {
							temp_map.putAll(data.get(j));
							data.get(j).putAll(data.get(i));
							data.get(i).putAll(temp_map);
							if(k-1 != 0){
								temp_map.putAll(data.get(j));
								data.get(j).putAll(data.get(i));
								data.get(i).putAll(temp_map);
							}
							break;
						}
					}*/
					
						
				}
			}
		}
		return data;
	}
	
	/**
	 * ���� ����
	 * @param data - ArrayList<HashMap<String, String>> Value
	 * @param key  - �����ϰ��� �ϴ� Key ��
	 * @return 	   - ArrayList<HashMap<String, String>>
	 */
	public static ArrayList<HashMap<String, String>> sortListHR(ArrayList<HashMap<String, String>> data, String key){
		//ArrayList<Map<String, Object>> sortdata = new ArrayList<Map<String,Object>>();
		for(int i=0;i<data.size();i++){
			HashMap<String, String> temp_map = new HashMap<String, String>();
			for(int j=i;j<data.size();j++){
				if(data.get(i).containsKey(key) && data.get(j).containsKey(key)){
					if( Double.parseDouble(data.get(i).get(key).toString()) < Double.parseDouble(data.get(j).get(key).toString()) ) {
						//temp_map.putAll(data.get(j));
						temp_map.putAll(data.get(j));
						data.get(j).putAll(data.get(i));
						data.get(i).putAll(temp_map);
					}
						
				}
			}
		}
		return data;
	}
	
	
	/**
	 * ��Ʈ���� ���� ���̰��� ��������� üũ�Ѵ�.
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str)
	{
		return (str == null || str.trim().length() == 0);
	}

	/**
	 * ��Ʈ�� ���� ���̸� ������� �����Ѵ�.
	 * @param str 
	 * @return
	 */
	public static String seEmpty(String str)
	{
		if(str == null) return "";
		return str;
	}
	
	
	
	
	/**
	 * ���, ����, �ݿø� ó��
	 * @param strMode  - ����
	 * @param nCalcVal - ó���� ��(�Ҽ��� ���� ������ ����)
	 * @param nDigit   - ���� ���� �ڸ���(����Ŭ�� ROUND�Լ� �ڸ��� ����)
	 *                   -2:�ʴ���, -1:�����, 0:�Ҽ��� 1�ڸ�
	 *                   1:�Ҽ��� 2�ڸ�, 2:�Ҽ��� 3�ڸ�, 3:�Ҽ��� 4�ڸ�, 4:�Ҽ��� 5�ڸ� ó��
	 * @return String nCalcVal
	 */
	public static String calcMath(String strMode, double nCalcVal, int nDigit) {
	    if(strMode.equals("ROUND")) {        //�ݿø�
	        if(nDigit < 0) {
	            nDigit = -(nDigit);
	            nCalcVal = Math.round(nCalcVal / Math.pow(10, nDigit)) * Math.pow(10, nDigit);
	        } else {
	            nCalcVal = Math.round(nCalcVal * Math.pow(10, nDigit)) / Math.pow(10, nDigit);
	        }
	    } else if(strMode.equals("CEIL")) {  //���
	        if(nDigit < 0) {
	            nDigit = -(nDigit);
	            nCalcVal = Math.ceil(nCalcVal / Math.pow(10, nDigit)) * Math.pow(10, nDigit);
	        } else {
	            nCalcVal = Math.ceil(nCalcVal * Math.pow(10, nDigit)) / Math.pow(10, nDigit);
	        }
	    } else if(strMode.equals("FLOOR")) { //����
	        if(nDigit < 0) {
	            nDigit = -(nDigit);
	            nCalcVal = Math.floor(nCalcVal / Math.pow(10, nDigit)) * Math.pow(10, nDigit);
	        } else {
	            nCalcVal = Math.floor(nCalcVal * Math.pow(10, nDigit)) / Math.pow(10, nDigit);
	        }
	    } else {                        //�״��(������ �Ҽ��� ù° �ڸ����� �ݿø�)
	        nCalcVal = Math.round(nCalcVal);
	    }
	    return String.valueOf(nCalcVal);

	}
	
	/**
	 * �ϳ��� Enable �ϴ� View
	 * 
	 * @param v ������ View
	 */
	
	public static void setRadioTypeLayout(View[] v){
		for(int i=0;i<v.length;i++){
			if( Boolean.parseBoolean(v[i].getTag().toString()) ){
				v[i].setVisibility(View.VISIBLE);
			}else{
				v[i].setVisibility(View.GONE);
			}
		}
		
	}
	
	/**
	 * �ϳ��� Enable �ϴ� View
	 * 
	 * @param v ������ Button
	 * @param e Enable Image
	 * @param d Normal Image
	 */
	
	public static void setRadioTypeBtn(View[] v, int [] e, int[] d){
		
		for(int i=0;i<v.length;i++){
			if( Boolean.parseBoolean(v[i].getTag().toString()) ){
				v[i].setBackgroundResource(e[i]);
			}else{
				v[i].setBackgroundResource(d[i]);
			}
		}
		
	}
	
	/**
	 * TabImageButton
	 * Tab�� �̹����� Contents Layout�� visialbe ��  gone�� ����	 
	 * 
	 * @param v Enable�� ��� ������ Button View
	 * @param e Enable Image
	 * @param d Normal Image
	 * @param c Contents Layout
	 */
	
	public static void setRadioTypeBtn(View[] v, int [] e, int[] d, View c[]){
		
		for(int i=0;i<v.length;i++){
			if( Boolean.parseBoolean(v[i].getTag().toString()) ){
				v[i].setBackgroundResource(e[i]);
				c[i].setVisibility(View.VISIBLE);
			}else{
				v[i].setBackgroundResource(d[i]);
				c[i].setVisibility(View.GONE);
			}
		}
		
	}
	
	
	/**
	 * 
	 * @param 	s 		: 
	 * @param 	key		: 
	 * @param 	type	:	
	 * @return	HashMap<String, Object>
	 */
	public static HashMap<String, Object> setHashMapCursorValue(Cursor s, String[] key, String[] type){
		HashMap<String, Object> m = new HashMap<String,Object>();
		int n=0;
		try {
			if(key.length == type.length){				

				for (int i = 0; i < key.length; i++) {

					if (type[n] != null && !type[n].equals("")) {
						if (type[n].equals("int")) {
							int v = s.getInt(n);
							m.put(key[n], v);
						} else if (type[n].equals("String")) {
							String v = s.getString(n);
							m.put(key[n], v);
						} else if (type[n].equals("Date")) {
							Date v = new Date(s.getLong(n));
							m.put(key[n], v);
						} else if (type[n].equals("boolean")) {
							boolean v = s.getInt(n) == 0 ? false : true;
							m.put(key[n], v);
						} else {
							String v = s.getString(n);
							m.put(key[n], v);
						}
						n++;
					}
				}
				
				
			}
		} catch (Exception e) {
			// TODO: handle exception
			m = new HashMap<String,Object>();
		}
		return m;
		
	}
	
	/**
	 * 
	 * @param 	s 	: 
	 * @param	key	: 
	 * @param 	type	:	
	 * @return	HashMap<String, String>
	 */
	public static HashMap<String, String> setHashMapStringCursorValue(Cursor s, String[] key, String[] type){
		HashMap<String, String> m = new HashMap<String,String>();
		SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		int n=0;
		try {
			if(key.length == type.length){				

				for (int i = 0; i < key.length; i++) {

					if (type[n] != null && !type[n].equals("")) {
						if (type[n].equals("int")) {
							int v = s.getInt(n);
							m.put(key[n], v+"");
						} else if (type[n].equals("String")) {
							String v = s.getString(n);
							m.put(key[n], v);
						} else if (type[n].equals("Date")) {
							Date v = new Date(s.getLong(n));
							m.put(key[n], timeStampFormat.format(v));
						} else if (type[n].equals("boolean")) {
							boolean v = s.getInt(n) == 0 ? false : true;
							m.put(key[n], v+"");
						} else {
							String v = s.getString(n);
							m.put(key[n], v+"");
						}
						n++;
					}
				}
				
				
			}
		} catch (Exception e) {
			// TODO: handle exception
			m = new HashMap<String,String>();
		}
		return m;
		
	}
	
	
	/**
	 * 
	 * @param	s
	 * @param 	key
	 * @return 	HashMap<String, String>
	 */
	public static HashMap<String, String> setHashMapCursorValue(Cursor s, String[] key){
		HashMap<String, String> m = new HashMap<String,String>();
		
		try {					

			for (int i = 0; i < key.length; i++) {
				String v = s.getString(i);
				m.put(key[i], v);
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			m = new HashMap<String,String>();
		}
		return m;
		
	}
	
	
	public static boolean isNotNullMap(HashMap<String, Object> data, String key){
		return data.containsKey(key) && (data.get(key)!= null ||!"".equals(data.get(key).toString()) ) ? true : false;
	}
	
	public static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }
	
	public static boolean isExtraData(Bundle ex, String key){
		if(ex != null){
			if(ex.containsKey(key)){
				/*if(!"".equals(ex.getString(key))){
					return true;
				}*/
				return true;
			}
		}
			
		return false;
	}
	
	public static String toHexString(byte[] b)
	{
		if(b == null) 
			return "00";
		return new java.math.BigInteger(b).toString(16);
	}


	public static byte[] toHexByteArray(String str)
	{
		return new java.math.BigInteger(str,16).toByteArray();
	}
	
	
	/**
	 * 2011-07-20T15:30:00Z to 20110720153000
	 */
	public static String toCilentDate(String time){
		return time.substring(0, 4) + time.substring(5, 7) + time.substring(8, 10) + 
		time.substring(11, 13) + time.substring(14, 16) + time.substring(17, 19);
	}
	
	
	/**
	 * 20110720153000 to 2011-07-20T15:30:00Z
	 */
	public static String toServerDate(String time){
		return time.substring(0, 4) +"-"+ time.substring(4, 6) +"-"+ time.substring(6, 8) +"T"+ 
		time.substring(8, 10) +":"+ time.substring(10, 12) +":"+ time.substring(12, 14)+"Z";
	}
	
	public static String setDayTitle(Calendar cal){
		return cal.get(Calendar.YEAR)+"년 "+ (cal.get(Calendar.MONTH)+1 <10 ? "0"+(cal.get(Calendar.MONTH)+1) :(cal.get(Calendar.MONTH)+1)) +"월 " +
		(cal.get(Calendar.DAY_OF_MONTH) < 10 ? "0"+(cal.get(Calendar.DAY_OF_MONTH)) : cal.get(Calendar.DAY_OF_MONTH))+"일";
	}
	
	public static String setWeekTitle(Calendar cal){
		Calendar f = setWeekFirstDay(cal);
		Calendar l = setWeekLastDay(cal);
		return f.get(Calendar.YEAR)+"년 "+
		(f.get(Calendar.MONTH)+1) +"월 " +
		(f.get(Calendar.DAY_OF_MONTH)) +"일  ~ "+
		(l.get(Calendar.YEAR) == f.get(Calendar.YEAR) ? "" : l.get(Calendar.YEAR)+"년 ") +
		(l.get(Calendar.MONTH)+1)+"월 " +
		(l.get(Calendar.DAY_OF_MONTH))+"일";
		/*return f.get(Calendar.YEAR)+"년 "+ 
		(f.get(Calendar.MONTH)+1 <10 ? "0"+(f.get(Calendar.MONTH)+1) :(f.get(Calendar.MONTH)+1)) +"월 " +
		(f.get(Calendar.DAY_OF_MONTH) < 10 ? "0"+(f.get(Calendar.DAY_OF_MONTH)) : f.get(Calendar.DAY_OF_MONTH))+"일  ~ "+
		(l.get(Calendar.YEAR) == f.get(Calendar.YEAR) ? "" : l.get(Calendar.YEAR)+"년 ") + 
		(l.get(Calendar.MONTH)+1 <10 ? "0"+(l.get(Calendar.MONTH)+1) :(l.get(Calendar.MONTH)+1)) +"월 " +
		(l.get(Calendar.DAY_OF_MONTH) < 10 ? "0"+(l.get(Calendar.DAY_OF_MONTH)) : l.get(Calendar.DAY_OF_MONTH))+"일";*/
	}
	
	public static String setWeekFooter(Calendar cal){
		Calendar f = setWeekFirstDay(cal);
		Calendar l = setWeekLastDay(cal);
		return (f.get(Calendar.MONTH)+1) +"월 " +
		(f.get(Calendar.DAY_OF_MONTH)) +" ~ "+
		/*(l.get(Calendar.MONTH)+1)+"월 " +*/
		(l.get(Calendar.DAY_OF_MONTH));
		/*return f.get(Calendar.YEAR)+"년 "+ 
		(f.get(Calendar.MONTH)+1 <10 ? "0"+(f.get(Calendar.MONTH)+1) :(f.get(Calendar.MONTH)+1)) +"월 " +
		(f.get(Calendar.DAY_OF_MONTH) < 10 ? "0"+(f.get(Calendar.DAY_OF_MONTH)) : f.get(Calendar.DAY_OF_MONTH))+"일  ~ "+
		(l.get(Calendar.YEAR) == f.get(Calendar.YEAR) ? "" : l.get(Calendar.YEAR)+"년 ") + 
		(l.get(Calendar.MONTH)+1 <10 ? "0"+(l.get(Calendar.MONTH)+1) :(l.get(Calendar.MONTH)+1)) +"월 " +
		(l.get(Calendar.DAY_OF_MONTH) < 10 ? "0"+(l.get(Calendar.DAY_OF_MONTH)) : l.get(Calendar.DAY_OF_MONTH))+"일";*/
	}
	
	
	public static String setMonthTitle(Calendar cal){
		return cal.get(Calendar.YEAR)+"년 "+
		(cal.get(Calendar.MONTH)+1) +"월 " ;
		/*return f.get(Calendar.YEAR)+"년 "+ 
		(f.get(Calendar.MONTH)+1 <10 ? "0"+(f.get(Calendar.MONTH)+1) :(f.get(Calendar.MONTH)+1)) +"월 " +
		(f.get(Calendar.DAY_OF_MONTH) < 10 ? "0"+(f.get(Calendar.DAY_OF_MONTH)) : f.get(Calendar.DAY_OF_MONTH))+"일  ~ "+
		(l.get(Calendar.YEAR) == f.get(Calendar.YEAR) ? "" : l.get(Calendar.YEAR)+"년 ") + 
		(l.get(Calendar.MONTH)+1 <10 ? "0"+(l.get(Calendar.MONTH)+1) :(l.get(Calendar.MONTH)+1)) +"월 " +
		(l.get(Calendar.DAY_OF_MONTH) < 10 ? "0"+(l.get(Calendar.DAY_OF_MONTH)) : l.get(Calendar.DAY_OF_MONTH))+"일";*/
	}
	
	/**
	 * 
	 * @param date yyyyMMddHHmmSS
	 * @param mode
	 * @return
	 */
	public static String setCalStrDate(Calendar date){
		return date.get(Calendar.YEAR)+""+
		(date.get(Calendar.MONTH)+1 <10 ? "0"+(date.get(Calendar.MONTH)+1) :(date.get(Calendar.MONTH)+1)) +""+
		(date.get(Calendar.DAY_OF_MONTH) < 10 ? "0"+(date.get(Calendar.DAY_OF_MONTH)) : date.get(Calendar.DAY_OF_MONTH))+""+
		(date.get(Calendar.HOUR_OF_DAY) <10 ? "0"+(date.get(Calendar.HOUR_OF_DAY)) :(date.get(Calendar.HOUR_OF_DAY))) +""+
		(date.get(Calendar.MINUTE) <10 ? "0"+(date.get(Calendar.MINUTE)) :(date.get(Calendar.MINUTE))) +""+
		(date.get(Calendar.SECOND) <10 ? "0"+(date.get(Calendar.SECOND)) :(date.get(Calendar.SECOND)));
	}
	
	
	public static String setDBDate(Calendar date){
		return date.get(Calendar.YEAR)+"-"+(date.get(Calendar.MONTH)+1 <10 ? "0"+(date.get(Calendar.MONTH)+1) :(date.get(Calendar.MONTH)+1)) +"-" +
		(date.get(Calendar.DAY_OF_MONTH) < 10 ? "0"+(date.get(Calendar.DAY_OF_MONTH)) : date.get(Calendar.DAY_OF_MONTH))+"T%'";
	}
	
	/**
	 * 
	 * @param time - 서버에서온 시간값
	 * @return 오전/오후 00:00
	 */
	public static String toServerDateHour(String time){
		String startH = time.substring(11, 13);
		startH = Integer.parseInt(startH) < 13 ? ("오전 " + Integer.parseInt(startH)) : "오후 " + (Integer.parseInt(startH)-12);
		
		return startH+":"+time.substring(14, 16);
	}
	
	
	/**
	 * 
	 * @param time - 서버에서온 시간값
	 * @return 오전/오후 00:00
	 */
	public static String toServerDateDay(String time){
		String startH = time.substring(5, 8);
		startH = Integer.parseInt(startH) < 13 ? ("오전 " + Integer.parseInt(startH)) : "오후 " + (Integer.parseInt(startH)-12);
		
		return startH+":"+time.substring(14, 16);
	}
	
	
	
	/**
	 * 
	 * @param date 		yyyyMMddHHmmss
	 * @param select	Calendar Type
	 * @param value		add Value
	 * @return time		long
	 */
	public static Long getTimeLong(String date, int select, int value) {
		Calendar today = Calendar.getInstance();
		if(date == null){
			date = getCurrentDateStr();
		}else if(date.length() > 17)	date = toCilentDate(date);
				
		if(date != null){
			if(date.length() > 4) today.set(Calendar.YEAR, Integer.parseInt(date.substring(0, 4)));
			if(date.length() > 6)today.set(Calendar.MONTH, Integer.parseInt(date.substring(4, 6))-1);
			else today.set(Calendar.MONTH, 1);
			if(date.length() > 8)today.set(Calendar.DATE, Integer.parseInt(date.substring(6, 8)));
			else today.set(Calendar.DATE, 1);
			if(date.length() > 10)today.set(Calendar.HOUR_OF_DAY, Integer.parseInt(date.substring(8, 10)));
			else today.set(Calendar.HOUR_OF_DAY, 0);
			if(date.length() > 12)today.set(Calendar.MINUTE, Integer.parseInt(date.substring(10, 12)));
			else today.set(Calendar.MINUTE, 0);
			if(date.length() > 14) today.set(Calendar.SECOND, Integer.parseInt(date.substring(12, 14)));
			else today.set(Calendar.SECOND, 0);
			if(select != 0)	today.add(select, value);
			
			return new Date(today.getTimeInMillis()).getTime();
		}else
			return null;
	}
	
	/**
	 * 
	 * @param date 		yyyyMMddHHmmss
	 * @param select	Calendar Type
	 * @param value		add Value
	 * @return time		long
	 */
	public static Long getTimeLong2(String date, int select, int value) {
		Calendar today = Calendar.getInstance();
		if(date == null){
			date = getCurrentDateStr();
		}else if(date.length() > 17)	date = toCilentDate(date);
				
		if(date != null){
			if(date.length() > 4) today.set(Calendar.YEAR, Integer.parseInt(date.substring(0, 4)));
			if(date.length() > 6)today.set(Calendar.MONTH, Integer.parseInt(date.substring(4, 6))-1);
			else today.set(Calendar.MONTH, 1);
			if(date.length() > 8)today.set(Calendar.DATE, Integer.parseInt(date.substring(6, 8)));
			else today.set(Calendar.DATE, 1);
			if(date.length() > 10)today.set(Calendar.HOUR_OF_DAY, Integer.parseInt(date.substring(8, 10)));
			else today.set(Calendar.HOUR_OF_DAY, 0);
			if(date.length() > 12)today.set(Calendar.MINUTE, Integer.parseInt(date.substring(10, 12)));
			else today.set(Calendar.MINUTE, 0);
			if(date.length() > 14) today.set(Calendar.SECOND, Integer.parseInt(date.substring(12, 14)));
			else today.set(Calendar.SECOND, 0);
			if(select != 0)	today.add(select, value);
			
			return today.getTimeInMillis();
		}else
			return null;
	}
	
	
	/**
	 * 
	 * @param date 		yyyyMMddHHmmss
	 * @param select	Calendar Type
	 * @param value		add Value
	 * @return time 	String yyyyMMddHHmmss
	 */
	public static String getTimeStr(String date, int select, int value) {
		Calendar today = Calendar.getInstance();
		SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		if(date == null){
			date = getCurrentDateStr();
		}else if(date.length() > 19)	date = toCilentDate(date);
		
		if(date != null){
			if(date.length() > 19)	date = toCilentDate(date);
			
			if(date.length() > 4) today.set(Calendar.YEAR, Integer.parseInt(date.substring(0, 4)));
			if(date.length() > 6)today.set(Calendar.MONTH, Integer.parseInt(date.substring(4, 6))-1);
			else today.set(Calendar.MONTH, 1);
			if(date.length() > 8)today.set(Calendar.DATE, Integer.parseInt(date.substring(6, 8)));
			else today.set(Calendar.DATE, 1);
			if(date.length() > 10)today.set(Calendar.HOUR_OF_DAY, Integer.parseInt(date.substring(8, 10)));
			else today.set(Calendar.HOUR_OF_DAY, 0);
			if(date.length() > 12)today.set(Calendar.MINUTE, Integer.parseInt(date.substring(10, 12)));
			else today.set(Calendar.MINUTE, 0);
			if(date.length() > 14) today.set(Calendar.SECOND, Integer.parseInt(date.substring(12, 14)));
			else today.set(Calendar.SECOND, 0);
			today.add(select, value);
			
			return timeStampFormat.format(new Date(today.getTimeInMillis()));
		}else
			return null;	
		
	}
	
	
	/**
	 * 
	 * @param date 		yyyyMMddHHmmss
	 * @param select	Calendar Type
	 * @param value		add Value
	 * @return time 	String yyyyMMddHHmmss
	 */
	public static String getServerTimeStr(String date, int select, int value) {
		Calendar today = Calendar.getInstance();
		SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		if(date == null){
			date = getCurrentDateStr();
		}else if(date.length() > 19)	date = toCilentDate(date);
		
		if(date != null){
			if(date.length() > 19)	date = toCilentDate(date);
			
			if(date.length() > 4) today.set(Calendar.YEAR, Integer.parseInt(date.substring(0, 4)));
			if(date.length() > 7)today.set(Calendar.MONTH, Integer.parseInt(date.substring(5, 7))-1);
			else today.set(Calendar.MONTH, 1);
			if(date.length() > 10)today.set(Calendar.DATE, Integer.parseInt(date.substring(8, 10)));
			else today.set(Calendar.DATE, 1);
			if(date.length() > 13)today.set(Calendar.HOUR_OF_DAY, Integer.parseInt(date.substring(11, 13)));
			else today.set(Calendar.HOUR_OF_DAY, 0);
			if(date.length() > 16)today.set(Calendar.MINUTE, Integer.parseInt(date.substring(14, 16)));
			else today.set(Calendar.MINUTE, 0);
			if(date.length() > 19) today.set(Calendar.SECOND, Integer.parseInt(date.substring(17, 19)));
			else today.set(Calendar.SECOND, 0);
			today.add(select, value);			
			
			
			return timeStampFormat.format(new Date(today.getTimeInMillis()));
		}else
			return null;	
		
	}
	
	
	/**
	 * 
	 * @param date 		yyyyMMddHHmmss
	 * @param select	Calendar Type
	 * @param value		add Value
	 * @return time 	String yyyyMMddHHmmss
	 */
	public static Long getServerTimeStr2(String date) {
		Calendar today = Calendar.getInstance();
		
			
			today.set(Calendar.YEAR, Integer.parseInt(date.substring(0, 4)));
			today.set(Calendar.MONTH, Integer.parseInt(date.substring(5, 7))-1);
			today.set(Calendar.DATE, Integer.parseInt(date.substring(8, 10)));
			
			today.set(Calendar.HOUR_OF_DAY, 0);
			today.set(Calendar.MINUTE, 0);
			today.set(Calendar.SECOND, 0);
			today.add(Calendar.SECOND, -1);
			
			
			return today.getTimeInMillis();
	
		
	}
	
	
	/**
	 * 
	 * @param date 		yyyyMMddHHmmss
	 * @param select	Calendar Type
	 * @param value		add Value
	 * @return time 	String yyyyMMddHHmmss
	 */
	public static Long getServerTimeStr3(String date) {
		Calendar today = Calendar.getInstance();
		SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		
			today.set(Calendar.YEAR, Integer.parseInt(date.substring(0, 4)));
			today.set(Calendar.MONTH, Integer.parseInt(date.substring(5, 7))-1);
			today.set(Calendar.DATE, Integer.parseInt(date.substring(8, 10)));
			today.set(Calendar.HOUR_OF_DAY, 23);
			today.set(Calendar.MINUTE, 59);
			today.set(Calendar.SECOND,59);
					
			
			
			return today.getTimeInMillis();
	
		
	}
	
	/**
	 * 
	 * @param date 		yyyyMMddHHmmss
	 * @param select	Calendar Type
	 * @param value		add Value
	 * @return time 	String yyyyMMddHHmmss
	 */
	public static Long getServerTimeStrLong(String date, int select, int value) {
		Calendar today = Calendar.getInstance();
		if(date == null){
			date = getCurrentDateStr();
		}else if(date.length() > 17)	date = toCilentDate(date);
		
		if(date != null){
			if(date.length() > 19)	date = toCilentDate(date);
			
			if(date.length() > 4) today.set(Calendar.YEAR, Integer.parseInt(date.substring(0, 4)));
			if(date.length() > 7)today.set(Calendar.MONTH, Integer.parseInt(date.substring(5, 7))-1);
			else today.set(Calendar.MONTH, 1);
			if(date.length() > 10)today.set(Calendar.DATE, Integer.parseInt(date.substring(8, 10)));
			else today.set(Calendar.DATE, 1);
			if(date.length() > 13)today.set(Calendar.HOUR_OF_DAY, Integer.parseInt(date.substring(11, 13)));
			else today.set(Calendar.HOUR_OF_DAY, 0);
			if(date.length() > 16)today.set(Calendar.MINUTE, Integer.parseInt(date.substring(14, 16)));
			else today.set(Calendar.MINUTE, 0);
			if(date.length() > 19) today.set(Calendar.SECOND, Integer.parseInt(date.substring(17, 19)));
			else today.set(Calendar.SECOND, 0);
			today.add(select, -value);	
			
			return today.getTimeInMillis();
		}else
			return null;	
		
	}
	
	/**
	 * TimeInMillis to String yyyyMMddHHmmSS
	 * @param time
	 * @return
	 */	
	public static String LongToStrTime(Long time){
		SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		return timeStampFormat.format(time);
	}
	
	/**
	 * TimeInMillis to String yyyyMMddHHmmSS
	 * @param time Calendar
	 * @return
	 */	
	public static String CalendarToStrTime(Calendar time){
		SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		return timeStampFormat.format(time.getTimeInMillis());
	}
	
	/**
	 * TimeInMillis to String yyyyMMddHHmmSS
	 * @param time Calendar
	 * @return
	 */	
	public static Long CalendarToStrTimeZeroLong(Calendar time){
		Calendar d = (Calendar)time.clone();
		d.set(Calendar.HOUR_OF_DAY, 0);
		d.set(Calendar.MINUTE, 0);
		d.set(Calendar.SECOND, 0);
		return d.getTimeInMillis();
	}
	
	/**
	 * TimeInMillis to String yyyyMMddHHmmSS
	 * @param time Calendar
	 * @return
	 */	
	public static Long CalendarToStrTimeZeroLongEnd(Calendar time){
		Calendar d = (Calendar)time.clone();
		d.set(Calendar.HOUR_OF_DAY, 0);
		d.set(Calendar.MINUTE, 0);
		d.set(Calendar.SECOND, 0);
		d.add(Calendar.SECOND, -1);
		return d.getTimeInMillis();
	}
	
	
	/** 
	 * Calendar to Server Time 2011-07-20T15:30:00Z
	 * @param time Calendar
	 * @return
	 */	
	public static String CalendarToSvStrTime(Calendar time){
		SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
		String t = timeStampFormat.format(time.getTimeInMillis());
		return t.substring(0, 10)+"T"+t.substring(10, t.length())+"Z";
	}
	
	/**
	 * 
	 * @return yyyyMMddHHmmss currentTime
	 */
	public static String getCurrentDateStr() {
		SimpleDateFormat tFormatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String currentDate = tFormatter.format(new java.util.Date());
		return (currentDate);
	}

	/**
	 *
	 * @return Long milliseconds since Jan. 1, 1970
	 */
	public static Long getCurrentDateLong() {;
		return new java.util.Date().getTime();
	}

	/**
	 *
	 * @return Long milliseconds since Jan. 1, 1970
	 */
	public static Calendar getCurrentDateCalendar() {
		SimpleDateFormat tFormatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String date = tFormatter.format(new java.util.Date());
	
		Calendar today = Calendar.getInstance();
		today.set(Calendar.YEAR, Integer.parseInt(date.substring(0, 4)));
		today.set(Calendar.MONTH, Integer.parseInt(date.substring(4, 6))-1);
		today.set(Calendar.DATE, Integer.parseInt(date.substring(6, 8)));
		today.set(Calendar.HOUR_OF_DAY, Integer.parseInt(date.substring(8, 10)));
		today.set(Calendar.MINUTE, Integer.parseInt(date.substring(10, 12)));
		today.set(Calendar.SECOND, Integer.parseInt(date.substring(12, 14)));
			
		return (Calendar) today.clone();
	}
	
	
	
	/**
	 * 
	 * @param date 2011-07-20T15:30:00Z to Calendar
	 * @return
	 */
	public static Calendar SeverDateToCalendar(String date) {
		Calendar today = Calendar.getInstance();
		//CustomLog.L("SeverDateToCalendar", "date " + date);
		if(date !=null && !"".equals(date)){
			if(date.length()>= 4) today.set(Calendar.YEAR, Integer.parseInt(date.substring(0, 4)));
			else today.set(Calendar.YEAR, 0);
			if(date.length()>= 7) today.set(Calendar.MONTH, Integer.parseInt(date.substring(5, 7))-1);
			else today.set(Calendar.MONTH, 0);
			if(date.length()>= 10) today.set(Calendar.DATE, Integer.parseInt(date.substring(8, 10)));
			else today.set(Calendar.DATE, 0);
			if(date.length()>= 13) today.set(Calendar.HOUR_OF_DAY, Integer.parseInt(date.substring(11, 13)));
			else today.set(Calendar.HOUR_OF_DAY, 0);
			if(date.length()>= 16) today.set(Calendar.MINUTE, Integer.parseInt(date.substring(14, 16)));
			else today.set(Calendar.MINUTE, 0);
			if(date.length()>= 19) today.set(Calendar.SECOND, Integer.parseInt(date.substring(17, 19)));
			else today.set(Calendar.SECOND, 0);

		}					
		return (Calendar) today.clone();
	}
	
	
	/**
	 * 
	 * @param date yyyy - yyyyMMddHHmmSS
	 * @return
	 */
	public static Calendar strDateToCalendar(String date) {
		Calendar today = Calendar.getInstance();
		today.set(Calendar.YEAR, Integer.parseInt(date.substring(0, 4)));
		if(date.length()>= 6) today.set(Calendar.MONTH, Integer.parseInt(date.substring(4, 6))-1);
		else today.set(Calendar.MONTH, 0);
		
		if(date.length()>= 8) today.set(Calendar.DATE, Integer.parseInt(date.substring(6, 8)));
		else today.set(Calendar.DATE, 1);
		
		if(date.length()>=10) today.set(Calendar.HOUR_OF_DAY, Integer.parseInt(date.substring(8, 10)));
		else today.set(Calendar.HOUR_OF_DAY, 0);
		
		if(date.length()>=12) today.set(Calendar.MINUTE, Integer.parseInt(date.substring(10, 12)));
		else today.set(Calendar.MINUTE, 0);
		
		if(date.length()>=14) today.set(Calendar.SECOND, Integer.parseInt(date.substring(12, 14)));
		else today.set(Calendar.SECOND, 0);
			
		return (Calendar) today.clone();
	}
	
	
	public static String setZrToStr(int num){
		return  num < 10 ? "0"+num : num+"";		
	}
	
	/**
	 * 0:year, 1:Month, 2:Day
	 * @param d
	 * @return
	 */
	public static int[] setWeekDays(Calendar d){
		int[] cal = new int[6];
		cal[0] = d.get(Calendar.YEAR);
		cal[1] = d.get(Calendar.MONTH);
		cal[2] = d.get(Calendar.DAY_OF_MONTH);
		cal[3] = d.get(Calendar.HOUR_OF_DAY);
		cal[4] = d.get(Calendar.MINUTE);
		cal[5] = d.get(Calendar.SECOND);
		return cal;
	}
	
	
	public static String setWeekDbDaysStr(Calendar d){
		int[] cal = new int[6];
		cal[0] = d.get(Calendar.YEAR);
		cal[1] = d.get(Calendar.MONTH);
		cal[2] = d.get(Calendar.DAY_OF_MONTH);
		cal[3] = d.get(Calendar.HOUR_OF_DAY);
		cal[4] = d.get(Calendar.MINUTE);
		cal[5] = d.get(Calendar.SECOND);
		return d.get(Calendar.YEAR)+""+(d.get(Calendar.MONTH)+1)+""+d.get(Calendar.DAY_OF_MONTH)+"000000";
	}
	
	/**
	 * 2011-07-20T15:30:00Z
	 * @param d
	 * @return
	 */
	public static String setWeekDbDaysStrSvTy(Calendar d){
		int[] cal = new int[6];
		cal[0] = d.get(Calendar.YEAR);
		cal[1] = d.get(Calendar.MONTH);
		cal[2] = d.get(Calendar.DAY_OF_MONTH);
		cal[3] = d.get(Calendar.HOUR_OF_DAY);
		cal[4] = d.get(Calendar.MINUTE);
		cal[5] = d.get(Calendar.SECOND);
		return d.get(Calendar.YEAR)+"-"+((d.get(Calendar.MONTH)+1) <10 ? "0"+(d.get(Calendar.MONTH)+1) : (d.get(Calendar.MONTH)+1) )+
		"-"+(d.get(Calendar.DAY_OF_MONTH) <10 ? "0"+d.get(Calendar.DAY_OF_MONTH) : d.get(Calendar.DAY_OF_MONTH));
	}
	
	public static int[] setWeekDayNum(Calendar cal){
		int [] daynum = new int[7]; 
		Calendar date = setWeekFirstDay(cal);
		for(int i=0;i<7;i++){
			daynum[i] = date.get(Calendar.DAY_OF_MONTH);
			date.add(Calendar.DAY_OF_MONTH, 1);
		}		
		return daynum;
	}
	
	/**
	 * Calendar 에서 각 주에 첫번째 날
	 * @param cal
	 * @return
	 */
	public static Calendar setWeekFirstDay(Calendar cal){
		Calendar d = (Calendar)cal.clone();
		d.add(Calendar.DAY_OF_MONTH, (d.getFirstDayOfWeek() - d.get(Calendar.DAY_OF_WEEK)));
		d.set(Calendar.HOUR_OF_DAY, 0);
		d.set(Calendar.MINUTE, 0);
		d.set(Calendar.SECOND, 0);
		return (Calendar)d.clone();
	}
	
	/**
	 * Calendar 에서 각 주에 첫번째 날
	 * @param cal
	 * @return
	 */
	public static Calendar setWeekLastDay(Calendar cal){
		Calendar d = (Calendar)cal.clone();
		d.add(Calendar.DAY_OF_MONTH, (7 - d.get(Calendar.DAY_OF_WEEK)));
		d.add(Calendar.DAY_OF_MONTH, 1);
		d.set(Calendar.HOUR_OF_DAY, 0);
		d.set(Calendar.MINUTE, 0);
		d.set(Calendar.SECOND, 0);
		d.add(Calendar.SECOND, -1);
		return (Calendar)d.clone();
	}
	/**
	 * 주간에서 사용하는 조건
	 * @param calendar
	 * @return
	 */
	public static String setWeekWhereStr(Calendar calendar){
		String where = null;
		
		int [] weekFirst = setWeekDays(setWeekFirstDay(calendar));
		int [] weekLast = setWeekDays(setWeekLastDay(calendar));
		
		
		CustomLog.L("setWeekWhereStr", "weekFirat day month " + weekFirst[1] +", weekFirstDay " + weekFirst[2]);
		CustomLog.L("setWeekWhereStr", "weekLast day month " + weekLast[1] +", weekLast " + weekLast[2]);
		return where;
	}
	
	/**
	 * 서버에서 내려온 Date Type의 날짜 값의 차 (Day)
	 * ex) 2011-07-20T15:30:00Z, 2011-07-20T15:30:00Z
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public static int diffServerDateDay(String start, String end) {
		GregorianCalendar s = new GregorianCalendar(Integer.parseInt(start.substring(0, 4)),
				(Integer.parseInt(start.substring(5, 7))-1), Integer.parseInt(start.substring(8, 10)));
		GregorianCalendar e = new GregorianCalendar(Integer.parseInt(end.substring(0, 4)),
				(Integer.parseInt(end.substring(5, 7))-1), Integer.parseInt(end.substring(8, 10)));
		int days = 0;
		if( (s.get(GregorianCalendar.YEAR) == e.get(GregorianCalendar.YEAR)) &&  (s.get(GregorianCalendar.MONTH) == e.get(GregorianCalendar.MONTH)) &&
				(s.get(GregorianCalendar.DAY_OF_MONTH) == e.get(GregorianCalendar.DAY_OF_MONTH)) ){
			days = -2;
		}else if( (s.get(GregorianCalendar.YEAR) >= e.get(GregorianCalendar.YEAR)) &&  (s.get(GregorianCalendar.MONTH) >= e.get(GregorianCalendar.MONTH)) &&
				(s.get(GregorianCalendar.DAY_OF_MONTH) > e.get(GregorianCalendar.DAY_OF_MONTH)) ){
			days = -1;
		}else{
			while (s.getTime().compareTo(e.getTime()) < 0) {
				s.add(Calendar.DAY_OF_MONTH, 1);
				days++;
			}
		}
			
		
		return (days);
	}

	
	/**
	 * 
	 * @param mContext
	 * @param view    	View	
	 * @param res_n		Normal Image   
	 * @param res_e		Endalbe Image
	 * @return View tag
	 */
	public static boolean setViewTagImageSwichBackgroud(Context mContext, View v, int res_n, int res_e){
		if(Boolean.parseBoolean(v.getTag().toString())){
			v.setTag("false");
			v.setBackgroundDrawable(mContext.getResources().getDrawable(res_n));
		}else{
			v.setTag("true");
			v.setBackgroundDrawable(mContext.getResources().getDrawable(res_e));
		}
		return Boolean.parseBoolean(v.getTag().toString());
	}
	
	
	/**
	 * 
	 * @param mContext
	 * @param view    	View	
	 * @param res_n		Normal Image   
	 * @param res_e		Endalbe Image
	 * @return View tag
	 */
	public static boolean setViewTagImageSwich(Context mContext, View v, int res_n, int res_e){
		if(Boolean.parseBoolean(v.getTag().toString())){
			v.setTag("false");
			if(v instanceof ImageView)	((ImageView)v).setImageDrawable(mContext.getResources().getDrawable(res_n));
			else v.setBackgroundDrawable(mContext.getResources().getDrawable(res_n));
		}else{
			v.setTag("true");
			if(v instanceof ImageView)	((ImageView)v).setImageDrawable(mContext.getResources().getDrawable(res_e));
			else v.setBackgroundDrawable(mContext.getResources().getDrawable(res_e));
		}
		CustomLog.L("setViewTagImageSwich", v instanceof ImageView ? "true" : "false");
		return Boolean.parseBoolean(v.getTag().toString());
	}
	
	
	
	
	public static boolean isValidationDate(Context context, Calendar date){
		boolean flag = true;
		Calendar current = getCurrentDateCalendar();
		/*CustomLog.L("isValidationDate", date.get(Calendar.YEAR)+"");
		CustomLog.L("isValidationDate", (current.get(Calendar.YEAR)+3)+"");
		CustomLog.L("isValidationDate", (current.get(Calendar.YEAR)-3)+"");*/
		if( (date.get(Calendar.YEAR) > (current.get(Calendar.YEAR)+3) ) || (date.get(Calendar.YEAR) < (current.get(Calendar.YEAR)-3) )) {
			flag = false;
		}
			
		return flag;
	}
	
	
	
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
	
	public static String getAlramTime(String date, int minute) {
		SimpleDateFormat tFormatter = new SimpleDateFormat("yyyyMMddHHmmss");
		Calendar today = Calendar.getInstance();
		today.set(Calendar.YEAR, Integer.parseInt(date.substring(0, 4)));
		today.set(Calendar.MONTH, Integer.parseInt(date.substring(4, 6))-1);
		today.set(Calendar.DATE, Integer.parseInt(date.substring(6, 8)));
		today.set(Calendar.HOUR_OF_DAY, Integer.parseInt(date.substring(8, 10)));
		today.set(Calendar.MINUTE, Integer.parseInt(date.substring(10, 12)));
		today.set(Calendar.SECOND, 0);
		today.add(Calendar.MINUTE, -minute);
		return tFormatter.format(today.getTimeInMillis());
	}
	
	public static boolean isCalendarEqual(Calendar a, Calendar b){
		return 	(a.get(Calendar.YEAR) == b.get(Calendar.YEAR)) &&
				(a.get(Calendar.MONTH) == b.get(Calendar.MONTH)) &&
				(a.get(Calendar.DAY_OF_MONTH) == b.get(Calendar.DAY_OF_MONTH));
	}
	
	public static String AppVersion(Context context){
		String version="";
		try {
		   PackageInfo i = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
		   version = i.versionName;
		} catch(NameNotFoundException e) { }
		
		return version;
	}
	
	private static final FrameLayout.LayoutParams ZOOM_PARAMS = new FrameLayout.LayoutParams(
			ViewGroup.LayoutParams.FILL_PARENT,
			ViewGroup.LayoutParams.WRAP_CONTENT,
			Gravity.BOTTOM
	);  
	public static View zoom = null;
	/**
	 * 내용을 웹뷰에 로드한다.
	 * @param context cotext
	 * @param webView 웹뷰
	 * @param htmlFlag html여부
	 * @param body 내용
	 * @param autoImageFlag 이미지자동로드여부
	 * @throws SKTException
	 */
	public static void loadWebView(Context context, WebView webView, boolean htmlFlag, String body, boolean autoImageFlag) throws SKTException {
		// Setting
		WebSettings settings = webView.getSettings();
		settings.setLoadsImagesAutomatically(autoImageFlag);
		settings.setSavePassword(false); 
        settings.setSaveFormData(false);
		settings.setJavaScriptEnabled(true);
		settings.setSupportZoom(false);
		settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		webView.setWebViewClient(new SKTWebViewClient(context));
		//webView.setWebChromeClient(new SKTWebViewClient(context));
		
		FrameLayout mContentView = (FrameLayout)((Activity)context).getWindow().getDecorView().findViewById(android.R.id.content);
		/*zoom = webView.getZoomControls();   
		if(mContentView.indexOfChild(zoom) == -1) {;
			mContentView.addView(zoom, ZOOM_PARAMS);
		}
		zoom.setVisibility(View.GONE);*/

		String mimeType = htmlFlag ? "text/html" : "text/plain";
		webView.loadDataWithBaseURL("x-data://base/", body, mimeType, "utf-8", null);
	}
	
	
	
	 /**
     * 웹뷰 클라이언트 클래스
     * @author june
     *
     */
    private static class SKTWebViewClient extends WebViewClient {

    	private Context context = null;

    	public SKTWebViewClient(Context context) {
    		this.context = context;
    	}

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
        	android.util.Log.d("WebView", url);
        	if(url.startsWith(WebView.SCHEME_TEL)) {
        		//TODO
        		return true;		
        	} else if (url.startsWith(WebView.SCHEME_MAILTO)) {
        		url = url.replaceAll(WebView.SCHEME_MAILTO, "");
        		
        		if(SKTUtil.isInstallPackage(context, Constants.CoreComponent.APP_ID_EMAIL) != null) {
        			SKTUtil.runMailWrite(context, url);
        		} else {
        			String msg = StringUtil.format(Resource.getString(context, "_E034"), Constants.CoreComponent.APP_NM_EMAIL);

    				SKTDialog dlg = new SKTDialog(context);
    				dlg.getDialog(msg, new DialogButton(0) {
    					public void onClick(DialogInterface dialog, int which) {
    						SKTUtil.goMobileOffice(context, Constants.CoreComponent.APP_ID_EMAIL);
    						SKTUtil.closeApp(context);
    					}
    				}).show();			
        		}
        		return true;		
        	} else if (url.startsWith(WebView.SCHEME_GEO)) {
        		//TODO
        		return true;		
        	} else {
        		int offset = url.indexOf(Constants.PREFIX_TODAY);
        		
        		if(offset != -1) {
        			String id = url.substring(offset + Constants.PREFIX_TODAY.length());
        			SKTUtil.runToday(context, id);
        			return true;
        		} else {
            		String extension = MimeTypeMap.getFileExtensionFromUrl(url);
            		String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);

            		android.util.Log.d("WebView", "extension[" + extension + "]");
            		android.util.Log.d("WebView", "mimeType[" + mimeType + "]");

            		if (mimeType != null) {				
            			return shouldHandleMimeType(mimeType, url);
            		} else {
            			StringBuffer sb = new StringBuffer(url);
            			if(sb.indexOf("x-data://base/") == 0) {
            				sb.replace(0, "x-data://base/".length(), Environ.HTTP +"://");
            			}
            			
            			Intent i = new Intent(Intent.ACTION_VIEW);
            			Uri u = Uri.parse(sb.toString());
            			i.setData(u); 
            			context.startActivity(i);
            			return true;
            		}
            		//return super.shouldOverrideUrlLoading(view, url);        			
        		}
        	}
        } 

    	private boolean shouldHandleMimeType(String mimeType, String url) {
    		//TODO
    		if (mimeType.startsWith("video/")) {			
    			return true;		
    		}		
    		return false;	
    	}
   } 
	
	
}
