package com.imchat.chanttyai.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

public class DisplayUtil {
    public interface Callback {
        void onDone();
    }

    public static void setTextDisabled(TextView textView,int size){
        textView.setTextColor(Color.parseColor(size == 0 ? "#464E53" : "#33B1FF"));
    }

    public static void listenEtLength(EditText editText, TextView textView, int maxLength, Callback callback) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int length = editText.getText().toString().length();
                textView.setText(length + "/" + maxLength);
                callback.onDone();
            }
        });
    }

    public static void hideTabToast(TabLayout tabLayout) {
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                tab.view.setLongClickable(false);
                // 针对android 26及以上需要设置setTooltipText为null
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    // 可以设置null也可以是""
                    tab.view.setTooltipText(null);
                    // tab.view.setTooltipText("");
                }
            }
        }
    }

    public static void hideNavToast(BottomNavigationView nav, int[] tabIds) {
        ViewGroup menu = (ViewGroup) nav.getChildAt(0);
        for (int tabId : tabIds) {
            menu.findViewById(tabId).setOnLongClickListener(view -> true);
        }
    }

    public static int getFullHeight(Context context) {
        return getStatusBarHeight(context) + getHeight(context) + getNavigationBarHeight(context);
    }

    public static int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        //获取status_bar_height资源的ID
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }


    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 获取宽度
     *
     * @param mContext 上下文
     * @return 宽度值，px
     */
    public static int getWidth(Context mContext) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) mContext.getApplicationContext().getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    /**
     * 获取高度
     *
     * @param mContext 上下文
     * @return 高度值，px
     */
    public static int getHeight(Context mContext) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) mContext.getApplicationContext().getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    public static int getNavigationBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }

}
