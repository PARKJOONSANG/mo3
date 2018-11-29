package com.ex.group.board.custom;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class CustomLog {
	
	public static int num = 0;
	public static AtomicInteger num2 = new AtomicInteger();
	public static boolean flag = false;
	public static String tag = "Custom";
	
	public static void L(String tag, String msg){
		if(flag){
			Log.d(tag, "["+ String.format("%25s",tag)+ "]" + "  ["+ String.format("%04d", num++) + "]  " + msg);
		}
	}
	
	public static void L(String tag, Object msg){
		if(msg instanceof Integer){
			Log.d(tag, "["+ String.format("%25s",tag)+ "]" + "  ["+ String.format("%04d", num++) + "]  " + "Integer value "+msg.toString());
		}else if(msg instanceof Long){
			Log.d(tag, "["+ String.format("%25s",tag)+ "]" + "  ["+ String.format("%04d", num++) + "]  " + "Long value "+msg.toString());
		}else if(msg instanceof Float){
			Log.d(tag, "["+ String.format("%25s",tag)+ "]" + "  ["+ String.format("%04d", num++) + "]  " + "Float value "+msg.toString());
		}else if(msg instanceof Map){
			Log.d(tag, "["+ String.format("%25s",tag)+ "]" + "  ["+ String.format("%04d", num++) + "]  " + "Map value "+msg.toString());
		}else if(msg instanceof List){
			Log.d(tag, "["+ String.format("%25s",tag)+ "]"  + "  ["+ String.format("%04d", num++) + "] Start " +
					"-------------------------------------------------------------------------------------------- " + "\n");	
			for(int i = 0; i<(((ArrayList)msg).size()); i++){
				Log.d(tag, "["+ String.format("%25s",tag)+ "]" + "  ["+ String.format("%04d", num++) + "]  " + ((ArrayList) msg).get(i).toString());
			}
			Log.d(tag, "["+ String.format("%25s",tag)+ "]"  + "  ["+ String.format("%04d", num++) + "] End " +
					"---------------------------------------------------------------------------------------------- " + "\n");
		}
	}
	
	public static void L(String tag, String[] msg){
		Log.d(tag, "["+ String.format("%25s",tag)+ "]"  + "  ["+ String.format("%04d", num++) + "] Start " +
				"-------------------------------------------------------------------------------------------- " + "\n");	
		if(flag){
			for(int i=0;i<msg.length;i++){
				Log.d(tag, "[ StringArray "+ String.format("%12s",i)+ "]"  + "  ["+ String.format("%04d", num++) + "]  " + msg[i]+"\n");
			}
		}
		Log.d(tag, "["+ String.format("%25s",tag)+ "]"  + "  ["+ String.format("%04d", num++) + "] End " +
				"---------------------------------------------------------------------------------------------- " + "\n");
	}
	
	public static void L(String tag, ArrayList<HashMap<String, String>> msg, String[] key){
		Log.d(tag, "["+ String.format("%25s",tag)+ "]"  + "  ["+ String.format("%04d", num++) + "] Start " +
				"-------------------------------------------------------------------------------------------- " + "\n");	
		for(int i=0;i<msg.size();i++){			
			for(int j=0;j<key.length;j++){
				String c = "";
				if(msg.get(i).containsKey(key[j])){
					if(msg.get(i).get(key[j]) != null && msg.get(i).get(key[j]).length() > 0){
						c = "KeyValue : "+key[j] +" , Value : "+ msg.get(i).get(key[j]);
					}else{
						c ="null";
					}
				}
				
				Log.d(tag, "[ ArrayList ("+ String.format("%12s",i)+ ")]"  + "  ["+ String.format("%04d", num++) + "]  " + c+"\n");
			}
			Log.d(tag, "-------------------------------------------------------------------------------------------" +
					"----------------------------------------------------------------------------- " + "\n");
		}
		Log.d(tag, "["+ String.format("%25s",tag)+ "]"  + "  ["+ String.format("%04d", num++) + "] End " +
				"---------------------------------------------------------------------------------------------- " + "\n");
	}
	
	public static void L(String tag, ArrayList<HashMap<String, String>> msg){
		Log.d(tag, "["+ String.format("%25s",tag)+ "]"  + "  ["+ String.format("%04d", num++) + "] Start " +
				"-------------------------------------------------------------------------------------------- " + "\n");	
		for(int i=0;i<msg.size();i++){			
			Log.d(tag, "["+ String.format("%25s",tag)+ "]" + "  ["+ String.format("%04d", num++) + "]  " + msg.get(i).toString());
		}
		Log.d(tag, "["+ String.format("%25s",tag)+ "]"  + "  ["+ String.format("%04d", num++) + "] End " +
				"---------------------------------------------------------------------------------------------- " + "\n");
	}
	public static void E( String msg ){
		if(flag){
			num++;
			Log.d(tag,"Temp  "+"["+ String.format("%04d", num2) + "]  " + msg);
		}
	}
}
