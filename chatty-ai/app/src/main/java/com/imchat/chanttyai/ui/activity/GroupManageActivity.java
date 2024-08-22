package com.imchat.chanttyai.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMUserInfo;
import com.imchat.chanttyai.adapters.ManageMemAdapter;
import com.imchat.chanttyai.base.BaseActivity;
import com.imchat.chanttyai.base.BaseAdapter;
import com.imchat.chanttyai.base.Constants;
import com.imchat.chanttyai.beans.GroupBean;
import com.imchat.chanttyai.databinding.ActivityGroupManageBinding;
import com.imchat.chanttyai.livedatas.LiveDataBus;
import com.imchat.chanttyai.ui.fragment.dialog.GroupChooseMemDialog;
import com.imchat.chanttyai.ui.fragment.dialog.GroupDeleteMemDialog;
import com.imchat.chanttyai.ui.fragment.dialog.GroupManageDialog;
import com.imchat.chanttyai.ui.fragment.manager.ChooseMemManager;
import com.imchat.chanttyai.ui.fragment.manager.EaseManager;
import com.imchat.chanttyai.utils.ClickHelper;
import com.imchat.chanttyai.utils.CommonUtils;
import com.imchat.chanttyai.utils.GsonUtils;
import com.imchat.chanttyai.utils.SharedPreferUtil;
import com.imchat.chanttyai.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GroupManageActivity extends BaseActivity<ActivityGroupManageBinding> {

    private EMGroup mGroup;
    private ManageMemAdapter mHumanAdapter;
    private ManageMemAdapter mAiAdapter;
    private GroupChooseMemDialog mChooseMemDialog;
    private GroupDeleteMemDialog mDeleteMemDialog;

    public static void start(Context context, String chatTo) {
        Intent intent = new Intent(context, GroupManageActivity.class);
        intent.putExtra(Constants.KEY_CHAT_TO, chatTo);
        context.startActivity(intent);
    }

    @Override
    protected ActivityGroupManageBinding getViewBinding() {
        return ActivityGroupManageBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initView() {

    }

    private void initRv() {
        List<BaseAdapter.SHOW_MODE> modeList = new ArrayList<>();
        if (TextUtils.equals(SharedPreferUtil.getInstance().getAccount(), mGroup.getOwner())){
            modeList.add(BaseAdapter.SHOW_MODE.SHOW_CUSTOM);
        }
        mHumanAdapter = new ManageMemAdapter(modeList, true);
        mAiAdapter = new ManageMemAdapter(modeList, false);

        mBinding.rvHuman.setAdapter(mHumanAdapter);
        mBinding.rvAi.setAdapter(mAiAdapter);
    }

    @Override
    protected void initData() {
        String chatTo = getIntent().getStringExtra(Constants.KEY_CHAT_TO);
        mGroup = EMClient.getInstance().groupManager().getGroup(chatTo);

        initRv();

        fetchMembers();
    }

    private void fetchMembers() {

        mBinding.tvTitle.setText(mGroup.getGroupName());

        EaseManager.getInstance().fetchGroupMembers(mGroup.getGroupId(), EaseManager.MEMBER_TYPE.ALL,new EMValueCallBack<Map<String, EMUserInfo>>() {
            @Override
            public void onSuccess(Map<String, EMUserInfo> map) {

                List<EMUserInfo> list = new ArrayList<>();
                List<String> eaAccounts = new ArrayList<>();
                for (Map.Entry<String, EMUserInfo> entry : map.entrySet()) {
                    list.add(entry.getValue());
                    eaAccounts.add(entry.getKey());
                }

                //设置已选中的成员
                ChooseMemManager.getInstance().setTempList(SharedPreferUtil.getInstance().getAIBeans(eaAccounts));

                List<EMUserInfo> humanList = list.stream().filter(userInfo -> !userInfo.getUserId().startsWith("bot")).collect(Collectors.toList());
                mBinding.llHuman.setVisibility(View.VISIBLE);
                mHumanAdapter.setData(humanList);

                List<EMUserInfo> aiList = list.stream().filter(userInfo -> userInfo.getUserId().startsWith("bot")).collect(Collectors.toList());
                mBinding.llAi.setVisibility(View.VISIBLE);
                mAiAdapter.setData(aiList);
            }

            @Override
            public void onError(int i, String s) {

            }
        },true);


    }

    @Override
    protected void initListener() {
        mHumanAdapter.setOnCustomItemClickListener(this::onDeleteMemClick);
        mAiAdapter.setOnCustomItemClickListener(this::onAddMemClick);
        ClickHelper.getInstance().setOnClickListener(mBinding.tvShare,this::onShareClick,false);
        ClickHelper.getInstance().setOnClickListener(mBinding.ivMore,this::onMoreClick,true);
        LiveDataBus.get().with(Constants.KEY_REFRESH_MEMBERS, String.class).observe(this, this::refreshMembers);
        LiveDataBus.get().with(Constants.KEY_REFRESH_GROUP_NAME, String.class).observe(this, this::refreshGroupName);
        LiveDataBus.get().with(Constants.KEY_DELETE_GROUP, String.class).observe(this, this::groupDelete);
    }

    private void onDeleteMemClick() {
        showGroupDeleteMemDialog();
    }

    private void showGroupDeleteMemDialog() {
        if (mDeleteMemDialog != null && mDeleteMemDialog.isShowing()){
            return;
        }
        mDeleteMemDialog = new GroupDeleteMemDialog(mContext,mGroup);
        mDeleteMemDialog.show();
    }

    private void groupDelete(String s) {
        finishThis();
    }

    private void refreshGroupName(String name) {
        mBinding.tvTitle.setText(name);
    }

    private void onMoreClick(View view) {
        GroupManageDialog groupManageDialog = new GroupManageDialog(mContext, mGroup, mFragmentManager);
        groupManageDialog.show();
    }

    private void onShareClick(View view) {
        CommonUtils.copyTextToClipboard(mContext, Constants.TEXT_SHARE + GsonUtils.toJson(new GroupBean(mGroup.getGroupId(), mGroup.getGroupName(),SharedPreferUtil.getInstance().getAccount())));
        ToastUtils.toast("群聊链接已复制！");
    }

    private void refreshMembers(String s) {
        fetchMembers();
    }

    private void onAddMemClick() {
        showGroupChooseMemDialog();
    }

    private void showGroupChooseMemDialog() {
        if (mChooseMemDialog != null && mChooseMemDialog.isShowing()) {
            return;
        }
        mChooseMemDialog = new GroupChooseMemDialog(mContext, mGroup);
        mChooseMemDialog.show();
    }
}
