package com.imchat.chanttyai.http;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.imchat.chanttyai.R;
import com.imchat.chanttyai.base.BaseActivity;
import com.imchat.chanttyai.beans.CommonBean;
import com.imchat.chanttyai.utils.GsonUtils;
import com.imchat.chanttyai.utils.LogUtils;
import com.imchat.chanttyai.utils.ToastUtils;
import com.imchat.chanttyai.views.LoadingDialog;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class QObserver<T> implements Observer<CommonBean<T>> {
    private BaseActivity mContext;
    private SmartRefreshLayout mSrl;
    private boolean mShowError = true;

    private boolean mShowLoading = true;

    public QObserver(BaseActivity context) {
        mContext = context;
    }

    public QObserver(BaseActivity context, SmartRefreshLayout srl) {
        mContext = context;
        mSrl = srl;
    }

    public QObserver(BaseActivity context, boolean showError) {
        mContext = context;
        mShowError = showError;
    }

    public QObserver(BaseActivity context, boolean showError, boolean showLoading) {
        mContext = context;
        mShowError = showError;
        mShowLoading = showLoading;
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {
        start();
        showDialog();
    }

    private void showDialog() {
        if (!mShowLoading) {
            return;
        }
        LoadingDialog.getInstance().show(mContext, mContext.getSupportFragmentManager());
    }

    @Override
    public void onNext(@NonNull CommonBean<T> commonBean) {
        if (TextUtils.equals(commonBean.getCode(), "1")) {
            LogUtils.e("onNext: " + GsonUtils.toJson(commonBean));
            next(commonBean.getData());
        } else {
            if (mShowError) ToastUtils.toast(commonBean.getMessage());
            LogUtils.e("error: " +commonBean.getCode() +" "+ commonBean.getMessage());
            error(commonBean.getCode(), commonBean.getMessage());
        }
        dismissDialog();
    }

    @Override
    public void onError(@NonNull Throwable e) {
        LogUtils.e("error: " + e.getMessage());
        error("xxxx", e.getMessage());
        if (mShowError) ToastUtils.toast(e.getMessage());
        dismissDialog();
    }

    private void dismissDialog() {
        if (!mShowLoading){
            return;
        }
        LoadingDialog.getInstance().dismiss();
    }

    @Override
    public void onComplete() {
        complete();
        dismissDialog();

        finishRefreshLoadmore();
    }

    private void finishRefreshLoadmore() {
        if (mSrl != null) {
            mSrl.finishRefresh();
            mSrl.finishLoadMore();
        }
    }

    protected void start() {
    }

    public abstract void next(T t);

    public void error(String code, String msg) {

    }

    public void complete() {

    }
}
