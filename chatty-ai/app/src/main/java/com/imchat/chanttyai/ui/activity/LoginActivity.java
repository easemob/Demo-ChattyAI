package com.imchat.chanttyai.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.imchat.chanttyai.base.BaseActivity;
import com.imchat.chanttyai.databinding.ActivityLoginBinding;
import com.imchat.chanttyai.utils.CommonUtils;
import com.imchat.chanttyai.utils.SharedPreferUtil;
import com.imchat.chanttyai.utils.ToastUtils;
import com.imchat.chanttyai.utils.thread.ThreadManager;
import com.imchat.chanttyai.views.LoadingDialog;

public class LoginActivity extends BaseActivity<ActivityLoginBinding> {


    public static void start(Context context) {
        context.startActivity(new Intent(context, LoginActivity.class));
    }

    @Override
    protected ActivityLoginBinding getViewBinding() {
        return ActivityLoginBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {

    }

    public void next(View view) {
        String phone = mBinding.etPhone.getText().toString();
        if (!CommonUtils.isPhone(phone)) {
            return;
        }
        LoadingDialog.getInstance().show(mContext, mFragmentManager, "登录中...");
        createAccount(phone);
    }

    private void createAccount(String phone) {
        ThreadManager.getInstance().runOnIOThread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 注册失败会抛出 HyphenateException。
                    // 同步方法，会阻塞当前线程。
                    EMClient.getInstance().createAccount(phone, SharedPreferUtil.psw);
                    login(phone);
                } catch (HyphenateException e) {
                    //失败
                    //已经注册过了
                    if (e.getErrorCode() == 203) {
                        //直接登录
                        login(phone);
                        return;
                    }
                    dismissLoading();
                    //否则直接报错
                    ToastUtils.toast(e.getMessage());
                }
            }
        });

    }

    private void dismissLoading(){
        LoadingDialog.getInstance().dismiss();
    }

    private void login(String phone) {
        EMClient.getInstance().login(phone, SharedPreferUtil.psw, new EMCallBack() {
            @Override
            public void onSuccess() {
                saveGetGo(phone);
            }

            @Override
            public void onError(int i, String s) {
                if (i == 200) {
                    saveGetGo(phone);
                    return;
                }
                dismissLoading();
                ToastUtils.toast(s);
            }
        });
    }

    private void saveGetGo(String phone) {
        SharedPreferUtil.getInstance().setAccount(phone);
        SharedPreferUtil.getInstance().fetchSaveAIs(mContext,this::OnDone);
    }

    private void OnDone() {
        MainActivity.start(mContext);
        finishThis();
    }
}
