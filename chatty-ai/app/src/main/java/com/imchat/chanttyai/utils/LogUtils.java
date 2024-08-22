package com.imchat.chanttyai.utils;

import android.util.Log;

public class LogUtils {
    private static final boolean ON = true;
    public static void e(String msg){
        if (!ON){
            return;
        }
        Log.e("qqqqqqqq",msg);
    }
}
