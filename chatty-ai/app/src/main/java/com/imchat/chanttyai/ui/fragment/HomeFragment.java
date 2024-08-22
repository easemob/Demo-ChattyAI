package com.imchat.chanttyai.ui.fragment;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.imchat.chanttyai.adapters.FragmentAdapter;
import com.imchat.chanttyai.base.BaseFragment;
import com.imchat.chanttyai.base.Constants;
import com.imchat.chanttyai.databinding.FragmentHomeBinding;
import com.imchat.chanttyai.utils.DisplayUtil;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends BaseFragment<FragmentHomeBinding> {
    private final String[] titles = {"公开智能体", "我创建的"};

    @Override
    protected FragmentHomeBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentHomeBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        initVP();
        initTab();
    }

    private void initVP() {
        List<BaseFragment> fragmentList = new ArrayList<>();
        fragmentList.add(new AIFragment(Constants.TYPE_PUBLIC));
        fragmentList.add(new AIFragment(Constants.TYPE_MINE));

        // FragmentPagerAdapter 来处理多个 Fragment 页面
        FragmentAdapter fragmentPagerAdapter = new FragmentAdapter(mFragmentManager, fragmentList);
        // viewPager 设置 adapter
        mBinding.vp.setAdapter(fragmentPagerAdapter);
        mBinding.vp.setOffscreenPageLimit(fragmentList.size());
    }

    private void initTab() {
        mBinding.tl.addTab(mBinding.tl.newTab());
        mBinding.tl.setupWithViewPager(mBinding.vp, false);

//        mBinding.tl.addOnTabSelectedListener(new TlOnPageChangeListener(18,16));
        for (int i = 0; i < titles.length; i++) {
            mBinding.tl.getTabAt(i).setText(titles[i]);
        }

        DisplayUtil.hideTabToast(mBinding.tl);
    }

    @Override
    protected void initListener() {

    }
}
