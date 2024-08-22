package com.imchat.chanttyai.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.hyphenate.EMValueCallBack;
import com.imchat.chanttyai.adapters.AvatarAdapter;
import com.imchat.chanttyai.base.BaseActivity;
import com.imchat.chanttyai.base.Constants;
import com.imchat.chanttyai.beans.AvatarBean;
import com.imchat.chanttyai.databinding.ActivityChooseAiAvatarBinding;
import com.imchat.chanttyai.livedatas.LiveDataBus;
import com.imchat.chanttyai.ui.fragment.manager.AvatarManager;
import com.imchat.chanttyai.ui.fragment.manager.EaseManager;
import com.imchat.chanttyai.utils.ClickHelper;

import java.util.List;

public class ChooseAvatarActivity extends BaseActivity<ActivityChooseAiAvatarBinding> {

    private AvatarAdapter mAdapter;
    private int mType;
    private String mAvatar;

    public static void start(Context context, String avatar,int type) {
        Intent intent = new Intent(context, ChooseAvatarActivity.class);
        intent.putExtra(Constants.KEY_CHOOSE_AVATAR_TYPE, type);
        intent.putExtra(Constants.KEY_CHOOSED_AVATAR,avatar);
        context.startActivity(intent);
    }

    @Override
    protected ActivityChooseAiAvatarBinding getViewBinding() {
        return ActivityChooseAiAvatarBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initView() {
        mType = getIntent().getIntExtra(Constants.KEY_CHOOSE_AVATAR_TYPE, Constants.AVATAR_TYPE_AI);
        mAvatar = getIntent().getStringExtra(Constants.KEY_CHOOSED_AVATAR);
        mBinding.tvTitle.setText(mType == Constants.AVATAR_TYPE_AI?"选择智能体头像":"选择头像");
        initRV();
    }

    private void initRV() {
        List<AvatarBean> list = mType == Constants.AVATAR_TYPE_AI ? AvatarManager.getInstance().getAIAvatarList() : AvatarManager.getInstance().getUserAvatarList();
        mAdapter = new AvatarAdapter(mBinding.rv,list, mAvatar,mType);
        mBinding.rv.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        ClickHelper.getInstance().setOnClickListener(mBinding.tvConfirm,this::onConfirm,false);
    }

    private void onConfirm(View view) {
        String pic = mAdapter.getCheckedAvatar().getPic();
        if (mType == Constants.AVATAR_TYPE_AI) {
            LiveDataBus.get().with(Constants.KEY_AI_AVATAR, String.class).postValue(pic);
            finishThis();
            return;
        }
        saveUserAvatar(pic);
    }

    private void saveUserAvatar(String pic) {
        EaseManager.getInstance().saveAvatar(pic, new EMValueCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                finishThis();
            }

            @Override
            public void onError(int i, String s) {

            }
        },true);
    }
}
