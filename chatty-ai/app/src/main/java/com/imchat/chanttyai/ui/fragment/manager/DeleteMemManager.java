package com.imchat.chanttyai.ui.fragment.manager;

import java.util.ArrayList;
import java.util.List;

public class DeleteMemManager {
    List<String> mChoosedList = new ArrayList<>();

    private DeleteMemManager() {
    }

    private static DeleteMemManager deleteMemManager;

    private OnChangeListener mOnChangeListener;

    public static DeleteMemManager getInstance() {
        if (deleteMemManager == null) {
            deleteMemManager = new DeleteMemManager();
        }
        return deleteMemManager;
    }

    public boolean contains(String id) {
        return mChoosedList.contains(id);
    }

    public void addOrRemove(String id) {
        if (contains(id)) {
            mChoosedList.remove(id);
            onChange();
            return;
        }
        mChoosedList.add(id);
        onChange();
    }

    public void setOnChangeListener(OnChangeListener onChangeListener) {
        mOnChangeListener = onChangeListener;
    }

    public List<String> getChoosedList() {
        return mChoosedList;
    }

    private void onChange() {
        if (mOnChangeListener != null) {
            mOnChangeListener.onChange(mChoosedList.size());
        }
    }

    public void clear() {
        mChoosedList.clear();
    }

    public interface OnChangeListener {
        void onChange(int size);
    }
}
