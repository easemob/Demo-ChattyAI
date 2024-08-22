package com.imchat.chanttyai.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;

import com.hyphenate.EMValueCallBack;
import com.imchat.chanttyai.base.BaseActivity;
import com.imchat.chanttyai.base.Constants;
import com.imchat.chanttyai.databinding.ActivityChangeNameBinding;
import com.imchat.chanttyai.ui.fragment.manager.EaseManager;
import com.imchat.chanttyai.utils.ClickHelper;
import com.imchat.chanttyai.utils.DisplayUtil;
import com.imchat.chanttyai.utils.ToastUtils;

public class ChangeNameActivity extends BaseActivity<ActivityChangeNameBinding> {

    public static void start(Context context, String nickname) {
        Intent intent = new Intent(context, ChangeNameActivity.class);
        intent.putExtra(Constants.KEY_NICKNAME, nickname);
        context.startActivity(intent);
    }

    private boolean mSaveEnabled;

    @Override
    protected ActivityChangeNameBinding getViewBinding() {
        return ActivityChangeNameBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        String nickname = getIntent().getStringExtra(Constants.KEY_NICKNAME);
        mBinding.etName.setText(nickname);
        mBinding.tvNameCount.setText(nickname.length() + "/20");
        switchCreateDisabled();
    }

    @Override
    protected void initListener() {
        DisplayUtil.listenEtLength(mBinding.etName,mBinding.tvNameCount,20,this::switchCreateDisabled);
        ClickHelper.getInstance().setOnClickListener(mBinding.tvSave,this::onSave,false);
    }

    private void onSave(View view) {
        if (!mSaveEnabled) {
            return;
        }
        String nickname = mBinding.etName.getText().toString();
        EaseManager.getInstance().saveNickname(nickname, new EMValueCallBack<String>() {
            @Override
            public void onSuccess(String value) {
                ToastUtils.toast("保存成功");
                finishThis();
            }

            @Override
            public void onError(int error, String errorMsg) {
            }
        },true);
    }

    private void switchCreateDisabled() {
        mSaveEnabled = mBinding.etName.getText().toString().length() > 0;
        mBinding.tvSave.setTextColor(Color.parseColor(mSaveEnabled ? "#33B1FF" : "#464E53"));
    }
}
