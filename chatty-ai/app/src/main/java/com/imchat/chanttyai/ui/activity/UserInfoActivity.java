package com.imchat.chanttyai.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMUserInfo;
import com.imchat.chanttyai.base.BaseActivity;
import com.imchat.chanttyai.base.Constants;
import com.imchat.chanttyai.databinding.ActivityUserInfoBinding;
import com.imchat.chanttyai.ui.fragment.manager.AvatarManager;
import com.imchat.chanttyai.ui.fragment.manager.EaseManager;
import com.imchat.chanttyai.utils.ClickHelper;
import com.imchat.chanttyai.utils.GsonUtils;
import com.imchat.chanttyai.utils.SharedPreferUtil;
import com.imchat.chanttyai.utils.ToastUtils;

import java.util.Map;

public class UserInfoActivity extends BaseActivity<ActivityUserInfoBinding> {

    private String mNickname;
    private String mUserAvatar;

    public static void start(Context context, EMUserInfo userInfo) {
        Intent intent = new Intent(context, UserInfoActivity.class);
        intent.putExtra(Constants.KEY_USER_INFO, GsonUtils.toJson(userInfo));
        context.startActivity(intent);
    }

    @Override
    protected ActivityUserInfoBinding getViewBinding() {
        return ActivityUserInfoBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchUserInfo();
    }

    private void fetchUserInfo() {
        String account = SharedPreferUtil.getInstance().getAccount();
        EaseManager.getInstance().getUserInfo(account, new EMValueCallBack<Map<String, EMUserInfo>>() {
            @Override
            public void onSuccess(Map<String, EMUserInfo> map) {
                EMUserInfo userInfo = map.get(account);
                mNickname = userInfo.getNickname();

                mUserAvatar = userInfo.getAvatarUrl();
                mBinding.ivAvatar.setImageResource(AvatarManager.getInstance().getUserAvatarRes(mUserAvatar));
                mBinding.tvName.setText(TextUtils.isEmpty(mNickname) ? account : mNickname);
            }

            @Override
            public void onError(int i, String s) {
                ToastUtils.toast("获取用户信息失败：" + i + s);
            }
        },true);
    }

    @Override
    protected void initListener() {
        ClickHelper.getInstance().setOnClickListener(mBinding.llAvatar,this::onChangeAvatar,true);
        ClickHelper.getInstance().setOnClickListener(mBinding.llName,this::onChangeName,true);
    }

    private void onChangeName(View view) {
        ChangeNameActivity.start(mContext,mNickname);
    }

    private void onChangeAvatar(View view) {
        ChooseAvatarActivity.start(mContext,mUserAvatar,Constants.AVATAR_TYPE_USER);
    }
}
