package com.imchat.chanttyai.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;

import com.imchat.chanttyai.base.App;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtils {

    private static boolean canClick = true;
    public static void setOnClickListener(View view, View.OnClickListener onClickListener,boolean jumpPage){
        if (view == null){
            return;
        }
        view.setOnClickListener(view1 -> {
            if (jumpPage && !canClick){
                return;
            }
            canClick = false;
            onClickListener.onClick(view1);

            new Handler().postDelayed(() -> canClick = true, 1000);
        });
    }


    public static int getStatusBarHeight() {
        int result = 0;
        int resourceId = App.getApplication().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = App.getApplication().getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static boolean isPhone(String phone){

        if (TextUtils.isEmpty(phone)){
            ToastUtils.toast("请输入手机号码！");
            return false;
        }

        if (phone.length() < 11){
            ToastUtils.toast("请输入手机号码，长度为11位！");
            return false;
        }

        // 定义手机号码的正则表达式
        String regex = "^[1][3-9]\\d{9}$";

        // 创建 Pattern 对象
        Pattern pattern = Pattern.compile(regex);

        // 创建 Matcher 对象
        Matcher matcher = pattern.matcher(phone);

        if (!matcher.matches()){
            ToastUtils.toast("请输入正确的手机号码格式！");
            return false;
        }

        return true;
    }

    public static String getClipboardStr(Context context) {
        String clipboardContent = "";
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        // 检查剪切板是否有内容
        if (clipboard.hasPrimaryClip()) {
            ClipData clip = clipboard.getPrimaryClip();
            // 获取剪切板中第一项的内容
            if (clip != null && clip.getItemCount() > 0) {
                CharSequence text = clip.getItemAt(0).getText();
                if (text != null) {
                    // 这里你有了剪切板的文本内容
                    clipboardContent = text.toString();
                }
            }
        }
        return clipboardContent;
    }

    public static void copyTextToClipboard(Context context, String text) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard != null) {
            clipboard.setPrimaryClip(ClipData.newPlainText("label", text));
        }
    }

    public static void clearClipboard(Context context) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                clipboard.clearPrimaryClip();
            }else {
                clipboard.setPrimaryClip(ClipData.newPlainText("", ""));
            }
        }
    }

    public static void jump2Browser(String url, Context context) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);

        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
        boolean isIntentSafe = activities.size() > 0;

        if (isIntentSafe) {
            context.startActivity(intent);
        }
    }

    public static void callPhone(String phoneNumber, Context context) {
        Uri phoneUri = Uri.parse("tel:" + phoneNumber);
        Intent intent = new Intent(Intent.ACTION_CALL, phoneUri);

        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
        boolean isIntentSafe = activities.size() > 0;

        if (isIntentSafe) {
            context.startActivity(intent);
        }
    }
}
