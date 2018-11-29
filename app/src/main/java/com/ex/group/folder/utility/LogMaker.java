package com.ex.group.folder.utility;

import android.util.Log;

import java.text.DateFormat;

public class LogMaker {


        public static void logStart(){
            Log.v("","-\n\n\n--------------------------------------------------[START]----------");
        }
    public static void logEnd(){
        Log.v("","--------------------------------------------------[THE END]----------\n\n\n\n-");
    }
        public static void logmaking(String name, String value){

            Log.v("","["+name+"]-["+value+"]");

        }
    public static void logmaking(String name, int value){

        Log.v("","["+name+"]-["+value+"]");

    }
    public static void logmaking(String name, boolean value){

        Log.v("","["+name+"]-["+value+"]");

    }
    }


