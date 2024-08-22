package com.imchat.chanttyai.ui.fragment.dialog;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMGroup;
import com.imchat.chanttyai.R;
import com.imchat.chanttyai.base.BaseActivity;
import com.imchat.chanttyai.base.Constants;
import com.imchat.chanttyai.beans.GroupBean;
import com.imchat.chanttyai.livedatas.LiveDataBus;
import com.imchat.chanttyai.ui.activity.ChangeGroupNameActivity;
import com.imchat.chanttyai.ui.fragment.manager.EaseManager;
import com.imchat.chanttyai.utils.ClickHelper;
import com.imchat.chanttyai.utils.DisplayUtil;
import com.imchat.chanttyai.utils.GsonUtils;
import com.imchat.chanttyai.utils.SharedPreferUtil;
import com.imchat.chanttyai.utils.ToastUtils;
import com.imchat.chanttyai.views.QBottomSheetDialog;
import com.imchat.chanttyai.views.dialog.BaseNiceDialog;
import com.imchat.chanttyai.views.dialog.NiceDialog;
import com.imchat.chanttyai.views.dialog.ViewConvertListener;
import com.imchat.chanttyai.views.dialog.ViewHolder;

public class GroupManageDialog extends QBottomSheetDialog {
    private EMGroup mGroup;
    private FragmentManager mFragmentManager;

    public GroupManageDialog(@NonNull BaseActivity context, EMGroup group, FragmentManager fragmentManager) {
        super(context);
        mGroup = group;
        mFragmentManager = fragmentManager;
    }

    @Override
    protected void initView() {
        super.initView();
        boolean isOwner = TextUtils.equals(mGroup.getOwner(), SharedPreferUtil.getInstance().getAccount());
        findViewById(R.id.ll_change_name).setVisibility(isOwner ? View.VISIBLE : View.GONE);
        ((TextView) findViewById(R.id.tv_delete)).setText(isOwner ? "删除群聊" : "退出群聊");
    }

    @Override
    protected void initListener() {
        super.initListener();
        ClickHelper.getInstance().setOnClickListener(findViewById(R.id.tv_change_name),this::onJump2ChangeName,true);
        ClickHelper.getInstance().setOnClickListener(findViewById(R.id.tv_delete),this::onDeleteGroup,false);
    }

    private void onDeleteGroup(View view) {
        dismiss();
        NiceDialog.init().setLayoutId(R.layout.dialog_layout)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    protected void convertView(ViewHolder holder, BaseNiceDialog dialog) {

                        ((TextView) holder.getView(R.id.tv_title)).setText("确认删除群聊？");
                        ((TextView) holder.getView(R.id.tv_content)).setText("删除后的群聊不可恢复");
                        ClickHelper.getInstance().setOnClickListener(holder.getView(R.id.tv_confirm),view1 -> {
                            dialog.dismissAllowingStateLoss();
                            doDeleteGroup();
                        },false);
                        ClickHelper.getInstance().setOnClickListener(holder.getView(R.id.tv_cancel),view12 -> dialog.dismissAllowingStateLoss(),false);
                    }
                })
                .setMargin(DisplayUtil.dp2px(mContext, 10))
                .setOutCancel(false)
                .show(mFragmentManager);
    }

    private void doDeleteGroup() {
        boolean isOwner = TextUtils.equals(mGroup.getOwner(), SharedPreferUtil.getInstance().getAccount());
        EaseManager.getInstance().deleteOrLeaveGroup(isOwner, mGroup.getGroupId(), new EMCallBack() {
            @Override
            public void onSuccess() {
                ToastUtils.toast(isOwner ? "删除成功" : "退出成功");
                LiveDataBus.get().with(Constants.KEY_DELETE_GROUP, String.class).postValue("");
            }

            @Override
            public void onError(int i, String s) {

            }
        },true);
    }

    private void onJump2ChangeName(View view) {
        dismiss();
        ChangeGroupNameActivity.start(mContext, GsonUtils.toJson(new GroupBean(mGroup.getGroupId(), mGroup.getGroupName())));
    }

    @Override
    protected int getLayout() {
        return R.layout.dialog_group_manage;
    }
}
