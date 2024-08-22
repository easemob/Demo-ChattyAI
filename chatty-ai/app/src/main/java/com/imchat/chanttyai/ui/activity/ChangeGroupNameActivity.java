package com.imchat.chanttyai.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;

import com.hyphenate.EMCallBack;
import com.imchat.chanttyai.base.BaseActivity;
import com.imchat.chanttyai.base.Constants;
import com.imchat.chanttyai.beans.GroupBean;
import com.imchat.chanttyai.databinding.ActivityChangeGroupNameBinding;
import com.imchat.chanttyai.livedatas.LiveDataBus;
import com.imchat.chanttyai.ui.fragment.manager.EaseManager;
import com.imchat.chanttyai.utils.ClickHelper;
import com.imchat.chanttyai.utils.DisplayUtil;
import com.imchat.chanttyai.utils.GsonUtils;
import com.imchat.chanttyai.utils.ToastUtils;

public class ChangeGroupNameActivity extends BaseActivity<ActivityChangeGroupNameBinding> {

    private boolean mSaveEnabled;
    private GroupBean mGroupBean;

    public static void start(Context context, String str) {
        Intent intent = new Intent(context, ChangeGroupNameActivity.class);
        intent.putExtra(Constants.KEY_GROUP, str);
        context.startActivity(intent);
    }

    @Override
    protected ActivityChangeGroupNameBinding getViewBinding() {
        return ActivityChangeGroupNameBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        mGroupBean = GsonUtils.toBean(getIntent().getStringExtra(Constants.KEY_GROUP), GroupBean.class);
        String name = mGroupBean.getName();
        mBinding.etName.setText(name);
        mBinding.tvNameCount.setText(name.length() + "/32");
        switchCreateDisabled();
    }

    private void switchCreateDisabled() {
        mSaveEnabled = mBinding.etName.getText().toString().length() > 0;
        mBinding.tvSave.setTextColor(Color.parseColor(mSaveEnabled ? "#33B1FF" : "#464E53"));
    }

    @Override
    protected void initListener() {
        DisplayUtil.listenEtLength(mBinding.etName,mBinding.tvNameCount,32,this::switchCreateDisabled);
        ClickHelper.getInstance().setOnClickListener(mBinding.tvSave,this::onSave,false);
    }

    private void onSave(View view) {
        if (!mSaveEnabled) {
            return;
        }
        doChangeGroupName();
    }

    private void doChangeGroupName() {
        String name = mBinding.etName.getText().toString();
        EaseManager.getInstance().changeGroupName(mGroupBean.getId(), name, new EMCallBack() {
            @Override
            public void onSuccess() {
                ToastUtils.toast("修改成功");
                LiveDataBus.get().with(Constants.KEY_REFRESH_GROUP_NAME,String.class).postValue(name);
                finishThis();
            }

            @Override
            public void onError(int i, String s) {
                ToastUtils.toast("修改失败：" + i + s);
            }
        },true);
    }
}
