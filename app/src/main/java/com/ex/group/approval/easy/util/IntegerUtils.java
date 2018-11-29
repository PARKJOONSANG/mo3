package com.ex.group.approval.easy.util;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;


public class IntegerUtils {
	public final static String INTEGER_FORMAT = "_@@";
	
	public static TextView formatInteger(TextView view, int integer, int color) {
		return format(view, (String) view.getText(), color, String.valueOf(integer));
	}
	
	public static void formatInteger(TextView view, int integer) {
		TextView tv = format(view, (String) view.getText(), Color.BLACK, String.valueOf(integer));
		view.setText(tv.getText());
	}
	
	public static void formatInteger(TextView view, String...params) {
		TextView tv = format(view, (String) view.getText(), Color.BLACK, params);
		view.setText(tv.getText());
	}
	
	private static TextView format(TextView tv, String fmt, int color, String...params) {
    	if(fmt == null)
    		return null;

    	if(fmt.length()>0 && fmt.charAt(0)=='_') {
    		fmt = fmt.substring(1);
    	}
    	
    	final String SEP = "@@";
	
    	tv.setText("");
    	if(params!=null && params.length>0) {
    		int offset = 0;
    		for(int i=0; i<params.length; i++) {
    			int buffer = fmt.indexOf(SEP, offset);
    			if(buffer > -1) {
    				tv.append(fmt.substring(offset, buffer));
    				
    				SpannableStringBuilder sp = new SpannableStringBuilder(params[i]);
    				if (color != 0xFFFFFFFF) 
    					sp.setSpan(new ForegroundColorSpan(color), 0, sp.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    				tv.append(sp);

    				offset = buffer + SEP.length();
    			} else {
    				tv.setText(fmt);
    	    		return tv;
    			}
    		}
    		tv.append(fmt.substring(offset));
    		return tv;
    	} else {
    		tv.setText(fmt);
    		return tv;
    	}
    }
}
