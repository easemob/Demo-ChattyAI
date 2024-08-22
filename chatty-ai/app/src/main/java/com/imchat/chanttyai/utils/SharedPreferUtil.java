package com.imchat.chanttyai.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.hyphenate.chat.EMUserInfo;
import com.imchat.chanttyai.base.App;
import com.imchat.chanttyai.base.BaseActivity;
import com.imchat.chanttyai.base.Constants;
import com.imchat.chanttyai.beans.AIBean;
import com.imchat.chanttyai.http.QObserver;
import com.imchat.chanttyai.listeners.DoneCallback;
import com.imchat.chanttyai.ui.fragment.manager.AvatarManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SharedPreferUtil {
    private static final SharedPreferUtil ourInstance = new SharedPreferUtil();
    private static final String KEY_ACCOUNT = "key_account";
    private static final String KEY_USER = "key_user";

    public static final String psw = "1234567890!@#";
    private static final String KEY_AIS = "key_ais";

    public static SharedPreferUtil getInstance() {
        return ourInstance;
    }

    private SharedPreferUtil() {
    }

    private final SharedPreferences mSharedPreference = App.getApplication().getSharedPreferences("chatty_config", Context.MODE_PRIVATE);


    public void setAccount(String account) {
        mSharedPreference.edit().putString(KEY_ACCOUNT, account).commit();
    }

    public String getAccount() {
        return mSharedPreference.getString(KEY_ACCOUNT, "");
    }

    public EMUserInfo getMe() {
        return GsonUtils.toBean(mSharedPreference.getString(KEY_USER, ""), EMUserInfo.class);
    }

    public void saveMe(String json) {
        mSharedPreference.edit().putString(KEY_USER, json).commit();
    }

    public void fetchSaveAIs(BaseActivity context, DoneCallback callback) {

        Map<String, String> map = new HashMap<>();
        map.put("type", Constants.TYPE_ALL);
        map.put("phone", SharedPreferUtil.getInstance().getAccount());
        App.getApi().getBot(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new QObserver<List<AIBean>>(context, true, false) {
                    @Override
                    public void next(List<AIBean> aiBeans) {
                        mSharedPreference.edit().putString(KEY_AIS, GsonUtils.toJson(aiBeans)).commit();
                        if (callback != null) {
                            callback.onDone();
                        }
                    }
                });
    }

    public void fetchSaveAIs(BaseActivity context) {

        Map<String, String> map = new HashMap<>();
        map.put("type", Constants.TYPE_ALL);
        map.put("phone", SharedPreferUtil.getInstance().getAccount());
        App.getApi().getBot(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new QObserver<List<AIBean>>(context, true, false) {
                    @Override
                    public void next(List<AIBean> aiBeans) {
                        mSharedPreference.edit().putString(KEY_AIS, GsonUtils.toJson(aiBeans)).commit();
                    }
                });
    }

    public EMUserInfo getUserInfo(Map<String, EMUserInfo> map, String account) {
        if (!account.contains("bot") && map.containsKey(account)) {
            return map.get(account);
        }
        return null;
    }

    public List<AIBean> getAIBeans(List<String> eaAccounts) {
        List<AIBean> list = GsonUtils.toList(mSharedPreference.getString(KEY_AIS, ""), AIBean.class);
        return list.stream().filter(aiBean -> eaAccounts.contains(aiBean.getEaAccount())).collect(Collectors.toList());
    }

    public AIBean getAIBean(String eaAccount) {
        List<AIBean> list = GsonUtils.toList(mSharedPreference.getString(KEY_AIS, ""), AIBean.class);
        AIBean[] re = list.stream().filter(aiBean -> TextUtils.equals(aiBean.getEaAccount(), eaAccount)).toArray(AIBean[]::new);
        if (re.length == 0) {
            return new AIBean();
        }
        return re[0];
    }

    public int[] getAvatarResArr(Map<String, EMUserInfo> map) {
        List<Integer> list = new ArrayList<>();

        for (Map.Entry<String, EMUserInfo> entry : map.entrySet()) {
            EMUserInfo userInfo = entry.getValue();
            //如果是机器人
            if (userInfo.getUserId().startsWith("bot")) {
                list.add(AvatarManager.getInstance().getAIAvatarBean(getAIBean(entry.getKey()).getPic()).getHead());
            } else {
                //如果是人类
                list.add(AvatarManager.getInstance().getUserAvatarRes(userInfo.getAvatarUrl()));
            }
        }
        //只取前4个
        if (list.size() > 3) {
            list = list.subList(0,4);
        }

        int[] re = new int[list.size()];
        for (int i = 0; i < re.length; i++) {
            re[i] = list.get(i);
        }
        return re;
    }

    public String getUnReadCount(int count) {
        if (count == 0) {
            return "";
        }
        if (count > 0 && count < 100) {
            return String.valueOf(count);
        }
        return " 99+ ";
    }

    public void clear() {
        mSharedPreference.edit().clear().commit();
    }


}
