package com.imchat.chanttyai.ui.fragment.manager;

import android.text.TextUtils;

import com.imchat.chanttyai.R;
import com.imchat.chanttyai.beans.AvatarBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AvatarManager {
    List<AvatarBean> mAIAvatarList;
    List<AvatarBean> mUserAvatarList;
    private static AvatarManager avatarManager;
    private AvatarManager(){}
    public static AvatarManager getInstance(){
        if (avatarManager == null){
             avatarManager = new AvatarManager();
        }
        return avatarManager;
    }

    public void init(){
        mAIAvatarList = new ArrayList<>();
        mAIAvatarList.add(new AvatarBean("1", R.drawable.avatar_1,R.drawable.per_bg_1));
        mAIAvatarList.add(new AvatarBean("2", R.drawable.avatar_2,R.drawable.per_bg_2));
        mAIAvatarList.add(new AvatarBean("3", R.drawable.avatar_3,R.drawable.per_bg_3));
        mAIAvatarList.add(new AvatarBean("4", R.drawable.avatar_4,R.drawable.per_bg_4));
        mAIAvatarList.add(new AvatarBean("5", R.drawable.avatar_5,R.drawable.per_bg_5));
        mAIAvatarList.add(new AvatarBean("6", R.drawable.avatar_6,R.drawable.per_bg_6));
        mAIAvatarList.add(new AvatarBean("7", R.drawable.avatar_7,R.drawable.per_bg_7));
        mAIAvatarList.add(new AvatarBean("8", R.drawable.avatar_8,R.drawable.per_bg_8));

        mUserAvatarList = new ArrayList<>();
        mUserAvatarList.add(new AvatarBean("1",R.drawable.avatar1));
        mUserAvatarList.add(new AvatarBean("2",R.drawable.avatar2));
        mUserAvatarList.add(new AvatarBean("3",R.drawable.avatar3));
        mUserAvatarList.add(new AvatarBean("4",R.drawable.avatar4));
        mUserAvatarList.add(new AvatarBean("5",R.drawable.avatar5));
        mUserAvatarList.add(new AvatarBean("6",R.drawable.avatar6));
        mUserAvatarList.add(new AvatarBean("7",R.drawable.avatar7));
        mUserAvatarList.add(new AvatarBean("8",R.drawable.avatar8));
        mUserAvatarList.add(new AvatarBean("9",R.drawable.avatar9));
        mUserAvatarList.add(new AvatarBean("10",R.drawable.avatar10));
        mUserAvatarList.add(new AvatarBean("11",R.drawable.avatar11));
        mUserAvatarList.add(new AvatarBean("12",R.drawable.avatar12));
        mUserAvatarList.add(new AvatarBean("13",R.drawable.avatar13));
        mUserAvatarList.add(new AvatarBean("14",R.drawable.avatar14));
        mUserAvatarList.add(new AvatarBean("15",R.drawable.avatar15));
        mUserAvatarList.add(new AvatarBean("16",R.drawable.avatar16));
        mUserAvatarList.add(new AvatarBean("17",R.drawable.avatar17));
        mUserAvatarList.add(new AvatarBean("18",R.drawable.avatar18));
        mUserAvatarList.add(new AvatarBean("19",R.drawable.avatar19));
        mUserAvatarList.add(new AvatarBean("20",R.drawable.avatar20));
    }

    public String getRandomAvatar(){
        int random = new Random().nextInt(mAIAvatarList.size());
        return mAIAvatarList.get(random).getPic();
    }

    public List<AvatarBean> getAllAIAvatarList(){
        return mAIAvatarList;
    }

    public int getUserAvatarRes(String pic){
        for (AvatarBean avatarBean : mUserAvatarList) {
            if (TextUtils.equals(pic,avatarBean.getPic())){
                return avatarBean.getHead();
            }
        }
        return R.drawable.onlight;
    }

    public AvatarBean getAIAvatarBean(String pic){
        for (AvatarBean bean : mAIAvatarList) {
            if (TextUtils.equals(pic,bean.getPic())){
                return bean;
            }
        }
        //默认
        return new AvatarBean("0",R.drawable.onlight,R.drawable.empty);
    }

    public List<AvatarBean> getUserAvatarList() {
        return mUserAvatarList;
    }

    public List<AvatarBean> getAIAvatarList(){
        return mAIAvatarList;
    }
}
