package com.ex.group.board.custom;

import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class SchValidation {

	
	/***
	 * 안내문구 보여주기
	 * 
	 * @param context : 요청하는 Context (ex) SecurityActivity.this
	 * @param msg : 안내문구
	 * @return
	 */
	public static boolean showToast(Context context, String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
		return false;
	}
	
	public static boolean showToastLong(Context context, String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
		return false;
	}
	
	
	/***
	 * 안내문구 보여주기
	 * 
	 * @param context : 요청하는 Context (ex) SecurityActivity.this
	 * @param msg : 안내문구
	 * @return
	 */
	public static boolean showToast(Context context, HashMap map, String key, String msg) {
		if(map != null && map.containsKey(key)){
			if(!"".equals(map.get(key).toString())){
				return true;
			}
		}
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
		return false;
		
		
	}
	
	/***
	 * 입력값 길이 체크 최소 몇자리 부터 최대 몇자리 까지
	 * 
	 * @param context : 요청하는 Context (ex) SecurityActivity.this
	 * @param eText : 체크 할 EditText
	 * @param min : 최소 입력 길이
	 * @param max : 최대 입력 길이
	 * @param msg : 안내문구
	 * @return
	 */
	public static boolean showToastPhoneNum(Context context, EditText eText, int min, int max, String msg) {
		if( (eText.length() < min || eText.length() > max) && eText != null) {			
			Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
			eText.setText("");
			eText.requestFocus();
			return false;			
		}else{
			String num = eText.getText().toString().substring(0, 3);
			if(num.equals("010") || num.equals("011") || num.equals("016") || num.equals("019") || 
					num.equals("015")|| num.equals("070")|| num.equals("018")){	
				return true;
			}else{
				Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
				eText.setText("");
				eText.requestFocus();
				return false;
			}
		}
	}
	
	/***
	 * 입력값 길이 체크 최소 몇자리 부터 최대 몇자리 까지
	 * 
	 * @param context : 요청하는 Context (ex) SecurityActivity.this
	 * @param eText : 체크 할 EditText
	 * @param min : 최소 입력 길이
	 * @param max : 최대 입력 길이
	 * @param msg : 안내문구
	 * @return
	 */
	public static boolean showToast(Context context, EditText eText, int min, int max, String msg) {
		if ( (eText.length() < min || eText.length() > max) && eText != null && !"".equals(eText.toString().trim()) ) {
			Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
			eText.setText("");
			eText.requestFocus();
			return false;
		} else
			return true;
	}

	/***
	 * 입력값 길이 정해진 것
	 * 
	 * @param context : 요청하는 Context (ex) SecurityActivity.this
	 * @param eText : 체크 할 EditText
	 * @param length : 입력 할 길이
	 * @param msg : 안내문구
	 * @return
	 */
	public static boolean showToast(Context context, EditText eText, int length, String msg) {
		if (eText.length() != length) {
			Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
			eText.setText("");
			eText.requestFocus();
			return false;
		} else
			return true;
	}
	
	/***
	 * 입력값 길이 정해진 것
	 * 
	 * @param context : 요청하는 Context (ex) SecurityActivity.this
	 * @param eText : 체크 할 EditText
	 * @param length : 입력 할 길이
	 * @param msg : 안내문구
	 * @return
	 */
	public static boolean showToastMinLen(Context context, EditText eText, int length, String msg) {
		if (eText.length() < length) {
			Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
			eText.setText("");
			eText.requestFocus();
			return false;
		} else
			return true;
	}
	
	
	public static boolean showToast(Context context, boolean flag, String msg) {
		if (!flag) {
			Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
			return false;
		} else
			return true;
	}
	
	/***
	 * 입력값이 null이 아닐때
	 * 
	 * @param context : 요청하는 Context (ex) SecurityActivity.this
	 * @param eText : 체크 할 EditText
	 * @param msg : 안내문구
	 * @return
	 */
	public static boolean showToast(Context context, EditText eText, String msg) {
		if ("".equals(eText.getText().toString()) || eText.length() < 1) {
			eText.setFocusable(true);
			eText.setText("");
			eText.requestFocus();
			return false;
		} else
			return true;
	}
	
	/***
	 * 입력값이 null이 아닐때
	 * 
	 * @param context : 요청하는 Context (ex) SecurityActivity.this
	 * @param eText : 체크 할 EditText
	 * @param msg : 안내문구
	 * @return
	 */
	public static boolean showToast(Context context, String eText, String msg) {
		if (eText == null || "".equals(eText.toString()) || eText.length() < 1) {					
			Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
			return false;
		} else
			return true;
	}
	
	/***
	 * 입력값 체크 유무
	 * 
	 * @param context : 요청하는 Context (ex) SecurityActivity.this
	 * @param eText : 체크 할 EditText
	 * @param str : 비교문자열
	 * @param msg : 안내문구
	 * @return
	 */
	public static boolean showToast(Context context, EditText eText, String str, String msg) {
		if (eText.getText().toString().equals(str)) {
			Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
			eText.setText("");
			eText.requestFocus();
			return false;
		} else
			return true;
	}
	
	
}
