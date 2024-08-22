package com.imchat.chanttyai.ui.fragment.manager;

import android.text.TextUtils;

import com.hyphenate.chat.EMUserInfo;
import com.imchat.chanttyai.base.Constants;
import com.imchat.chanttyai.beans.AIBean;
import com.imchat.chanttyai.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class ChooseMemManager {
    private ChooseMemManager() {
        mTempList = new ArrayList<>();
        mResultList = new ArrayList<>();
    }

    private static ChooseMemManager chooseMemManager;

    public static ChooseMemManager getInstance() {
        if (chooseMemManager == null) {
            chooseMemManager = new ChooseMemManager();
        }
        return chooseMemManager;
    }

    private List<AIBean> mTempList;

    private List<AIBean> mResultList;

    public void addOrRemove(AIBean bean, Map<String, EMUserInfo> AIMemberMap) {
        if (contains(bean)) {
            mTempList = mTempList.stream().filter(aiBean -> !Objects.equals(aiBean.getId(), bean.getId())).collect(Collectors.toList());
            onChange();
            return;
        }
        int leftSize = Constants.MAX_AI_IN_GROUP;
        if (AIMemberMap != null && !AIMemberMap.isEmpty()){
            leftSize -= AIMemberMap.size();
        }
        if (mTempList.size() >= leftSize) {
            ToastUtils.toast("最多添加5个智能体");
            return;
        }
        mTempList.add(bean);
        onChange();
    }

    private void onChange() {
        if (mOnChangeListener != null) {
            mOnChangeListener.onChange(mTempList.size());
        }
    }

    public List<AIBean> getTempList() {
        return mTempList;
    }

    public boolean contains(AIBean bean) {
        return mTempList.stream().filter(aiBean -> TextUtils.equals(aiBean.getEaAccount(), bean.getEaAccount())).toArray().length != 0;
    }

    private OnChangeListener mOnChangeListener;

    public void setOnChangeListener(OnChangeListener onChangeListener) {
        mOnChangeListener = onChangeListener;
    }

    public void clearAll() {
        clearTemp();
        clearResult();
    }

    public void setTempList(List<AIBean> list){
        mTempList = list;
    }

    public void clearResult() {
        mResultList.clear();
    }

    public void clearTemp() {
        mTempList.clear();
    }

    public void setResult() {
        mResultList.clear();
        mResultList.addAll(mTempList);
    }

    public void setDefault() {
        mTempList.clear();
        mTempList.addAll(mResultList);
    }

    public List<AIBean> getResultList() {
        return mResultList;
    }

    public String[] getResultArr() {
        return mResultList.stream()
                .map(AIBean::getEaAccount)
                .toArray(String[]::new);
    }

    public String[] getTempArr() {
        return mTempList.stream()
                .map(AIBean::getEaAccount)
                .toArray(String[]::new);
    }

    public interface OnChangeListener {
        void onChange(int size);
    }
}
