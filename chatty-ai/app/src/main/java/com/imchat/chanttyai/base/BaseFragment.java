package com.imchat.chanttyai.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewbinding.ViewBinding;

import com.imchat.chanttyai.ui.fragment.manager.EaseManager;

import pub.devrel.easypermissions.EasyPermissions;


public abstract class BaseFragment<VB extends ViewBinding> extends Fragment {

    protected VB mBinding;

    protected BaseActivity mContext;
    protected BaseFragment mCurrentFragment;
    protected FragmentManager mFragmentManager;

    public BaseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = (BaseActivity) context;
        mFragmentManager = getChildFragmentManager();

        EaseManager.getInstance().init(mContext, mFragmentManager);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = getViewBinding(inflater, container);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initListener();
        initData();
    }

    protected void onMore() {
    }

    protected abstract VB getViewBinding(LayoutInflater inflater, ViewGroup container);

    protected abstract void initView();

    protected abstract void initData();

    protected abstract void initListener();

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // 将结果转发给 EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}