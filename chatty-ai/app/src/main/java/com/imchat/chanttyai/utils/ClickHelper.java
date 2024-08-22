package com.imchat.chanttyai.utils;

import android.os.Handler;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

public class ClickHelper {
    private static ClickHelper clickHelper;
    private ClickHelper(){}

    private Map<View,Boolean> mMap = new HashMap<>();

    public static ClickHelper getInstance(){
        if (clickHelper == null){
            clickHelper = new ClickHelper();
        }
        return clickHelper;
    }

    public void setOnClickListener(View view, View.OnClickListener onClickListener,boolean ctr){
        if (view == null){
            return;
        }
        view.setOnClickListener(view1 -> {

            if (!mMap.containsKey(view)){
                mMap.put(view,true);
            }

            if (ctr && !Boolean.TRUE.equals(mMap.get(view))){
                return;
            }

            mMap.replace(view,false);

            onClickListener.onClick(view1);

            new Handler().postDelayed(() -> mMap.remove(view), 1000);
        });
    }
}
