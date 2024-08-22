package com.imchat.chanttyai.ui.fragment;

import android.Manifest;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMUserInfo;
import com.imchat.chanttyai.R;
import com.imchat.chanttyai.adapters.MineOptAdapter;
import com.imchat.chanttyai.base.AppManager;
import com.imchat.chanttyai.base.BaseFragment;
import com.imchat.chanttyai.beans.MineOptBean;
import com.imchat.chanttyai.databinding.FragmentMineBinding;
import com.imchat.chanttyai.listeners.OnItemClickListener;
import com.imchat.chanttyai.ui.activity.LoginActivity;
import com.imchat.chanttyai.ui.activity.UserInfoActivity;
import com.imchat.chanttyai.ui.fragment.manager.AvatarManager;
import com.imchat.chanttyai.ui.fragment.manager.EaseManager;
import com.imchat.chanttyai.utils.ClickHelper;
import com.imchat.chanttyai.utils.CommonUtils;
import com.imchat.chanttyai.utils.DisplayUtil;
import com.imchat.chanttyai.utils.GsonUtils;
import com.imchat.chanttyai.utils.SharedPreferUtil;
import com.imchat.chanttyai.utils.ToastUtils;
import com.imchat.chanttyai.views.dialog.BaseNiceDialog;
import com.imchat.chanttyai.views.dialog.NiceDialog;
import com.imchat.chanttyai.views.dialog.ViewConvertListener;
import com.imchat.chanttyai.views.dialog.ViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pub.devrel.easypermissions.EasyPermissions;

public class MineFragment extends BaseFragment<FragmentMineBinding> implements EasyPermissions.PermissionCallbacks {

    private MineOptAdapter mAdapter;
    private EMUserInfo mUserInfo;
    private static final int REQUEST_CALL_PHONE = 1001;

    @Override
    protected FragmentMineBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentMineBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initView() {
        initRv();
    }

    private void initRv() {
        List<MineOptBean> list = new ArrayList<>();
        list.add(new MineOptBean(R.drawable.mine_info, "个人信息", ""));
        list.add(new MineOptBean(R.drawable.mine_easemob, "关于环信", "easemob.com"));
        list.add(new MineOptBean(R.drawable.mine_business, "联系商务", "400-622-1776"));

        mAdapter = new MineOptAdapter(list, new OnItemClickListener<MineOptBean>() {
            @Override
            public void onItemClick(MineOptBean mineOptBean, int pos) {
                if (pos == 0) {
                    //个人信息
                    UserInfoActivity.start(mContext, mUserInfo);
                    return;
                }
                if (pos == 1) {
                    //关于环信
                    jumpBrowser();
                    return;
                }
                if (pos == 2) {
                    if (!EasyPermissions.hasPermissions(mContext, Manifest.permission.CALL_PHONE)) {
                        EasyPermissions.requestPermissions(mContext, "需要拨打电话权限", REQUEST_CALL_PHONE, Manifest.permission.CALL_PHONE);
                    } else {
                        callPhone();
                    }

                }
            }
        });
        mBinding.rv.setAdapter(mAdapter);
    }

    private void jumpBrowser() {
        NiceDialog.init().setLayoutId(R.layout.dialog_layout)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    protected void convertView(ViewHolder holder, BaseNiceDialog dialog) {

                        ((TextView) holder.getView(R.id.tv_title)).setText("关于环信");
                        ((TextView) holder.getView(R.id.tv_content)).setText("是否跳转至环信官网？");
                        ClickHelper.getInstance().setOnClickListener(holder.getView(R.id.tv_confirm), view -> {
                            dialog.dismissAllowingStateLoss();
                            CommonUtils.jump2Browser("http://easemob.cn/3sayjg", mContext);
                        },false);
                        ClickHelper.getInstance().setOnClickListener(holder.getView(R.id.tv_cancel),view -> dialog.dismissAllowingStateLoss(),false);
                    }
                })
                .setMargin(DisplayUtil.dp2px(mContext, 10))
                .setOutCancel(false)
                .show(mFragmentManager);
    }

    //联系商务
    private void callPhone() {
        NiceDialog.init().setLayoutId(R.layout.dialog_layout)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    protected void convertView(ViewHolder holder, BaseNiceDialog dialog) {

                        ((TextView) holder.getView(R.id.tv_title)).setText("联系商务");
                        ((TextView) holder.getView(R.id.tv_content)).setText("开始拨打：400-622-1776");
                        ClickHelper.getInstance().setOnClickListener(holder.getView(R.id.tv_confirm),view -> {
                            dialog.dismissAllowingStateLoss();
                            CommonUtils.callPhone("4006221776", mContext);
                        },false);
                        ClickHelper.getInstance().setOnClickListener(holder.getView(R.id.tv_cancel),view -> dialog.dismissAllowingStateLoss(),false);
                    }
                })
                .setMargin(DisplayUtil.dp2px(mContext, 10))
                .setOutCancel(false)
                .show(mFragmentManager);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        if (requestCode == REQUEST_CALL_PHONE) {
            callPhone();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        ToastUtils.toast("请同意权限申请后重试");
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onResume() {
        super.onResume();
        fetchUserInfo();
    }

    private void fetchUserInfo() {
        String account = SharedPreferUtil.getInstance().getAccount();
        EaseManager.getInstance().getUserInfo(account,
                new EMValueCallBack<Map<String, EMUserInfo>>() {
                    @Override
                    public void onSuccess(Map<String, EMUserInfo> map) {
                        mUserInfo = map.get(account);

                        SharedPreferUtil.getInstance().saveMe(GsonUtils.toJson(mUserInfo));

                        String nickname = mUserInfo.getNickname();

                        mBinding.ivAvatar.setImageResource(AvatarManager.getInstance().getUserAvatarRes(mUserInfo.getAvatarUrl()));
                        mBinding.tvName.setText(TextUtils.isEmpty(nickname) ? account : nickname);
                        mBinding.tvId.setText("用户ID：" + account);
                    }

                    @Override
                    public void onError(int i, String s) {
                        ToastUtils.toast("获取用户信息失败：" + i + s);
                    }
                }, false);
    }

    @Override
    protected void initListener() {
        ClickHelper.getInstance().setOnClickListener(mBinding.tvLogout,this::onLogout,false);
    }

    private void onLogout(View view) {

        NiceDialog.init().setLayoutId(R.layout.dialog_layout)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    protected void convertView(ViewHolder holder, BaseNiceDialog dialog) {

                        ((TextView) holder.getView(R.id.tv_title)).setText("退出登录");
                        holder.getView(R.id.tv_content).setVisibility(View.GONE);
                        ClickHelper.getInstance().setOnClickListener(holder.getView(R.id.tv_confirm),view1 -> {
                            dialog.dismissAllowingStateLoss();
                            doLogout();
                        },false);
                        ClickHelper.getInstance().setOnClickListener(holder.getView(R.id.tv_cancel),view12 -> dialog.dismissAllowingStateLoss(),false);
                    }
                })
                .setMargin(DisplayUtil.dp2px(mContext, 10))
                .setOutCancel(false)
                .show(mFragmentManager);


    }

    private void doLogout() {
        EaseManager.getInstance().logout(new EMCallBack() {
            @Override
            public void onSuccess() {
                //清除sp
                SharedPreferUtil.getInstance().clear();

                AppManager.getInstance().finishAllActivity();
                LoginActivity.start(mContext);
            }

            @Override
            public void onError(int i, String s) {

            }
        }, true);
    }


}
