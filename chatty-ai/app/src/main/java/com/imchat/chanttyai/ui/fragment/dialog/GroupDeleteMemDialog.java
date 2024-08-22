package com.imchat.chanttyai.ui.fragment.dialog;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMUserInfo;
import com.imchat.chanttyai.R;
import com.imchat.chanttyai.adapters.GroupDeleteMemAdapter;
import com.imchat.chanttyai.base.BaseActivity;
import com.imchat.chanttyai.base.Constants;
import com.imchat.chanttyai.livedatas.LiveDataBus;
import com.imchat.chanttyai.ui.fragment.manager.DeleteMemManager;
import com.imchat.chanttyai.ui.fragment.manager.EaseManager;
import com.imchat.chanttyai.utils.ClickHelper;
import com.imchat.chanttyai.utils.DisplayUtil;
import com.imchat.chanttyai.utils.SharedPreferUtil;
import com.imchat.chanttyai.utils.ToastUtils;
import com.imchat.chanttyai.views.QBottomSheetDialog;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GroupDeleteMemDialog extends QBottomSheetDialog {
    private EMGroup mGroup;
    private TextView mTvConfirm;

    public GroupDeleteMemDialog(@NonNull BaseActivity context, EMGroup group) {
        super(context);
        mGroup = group;
    }

    @Override
    protected void initView() {
        super.initView();

        initRvMain();

        mTvConfirm = findViewById(R.id.tv_confirm);
    }

    private void initRvMain() {
        SwipeRecyclerView rvMain = findViewById(R.id.rv_main);

        GroupDeleteMemAdapter adapter = new GroupDeleteMemAdapter();
        rvMain.setAdapter(adapter);

        EaseManager.getInstance().fetchGroupMembers(mGroup.getGroupId(), EaseManager.MEMBER_TYPE.HUMAN, new EMValueCallBack<Map<String, EMUserInfo>>() {
            @Override
            public void onSuccess(Map<String, EMUserInfo> map) {
                List<EMUserInfo> list = new ArrayList<>();
                for (Map.Entry<String, EMUserInfo> entry : map.entrySet()) {
                    EMUserInfo userInfo = entry.getValue();
                    if (TextUtils.equals(userInfo.getUserId(), SharedPreferUtil.getInstance().getAccount())){
                        continue;
                    }
                    list.add(userInfo);
                }
                adapter.setData(list);
            }

            @Override
            public void onError(int i, String s) {

            }
        },true);
    }

    @Override
    protected void initListener() {
        super.initListener();
        super.initListener();
        DeleteMemManager.getInstance().setOnChangeListener(size -> {
            setNextState();
        });

        ClickHelper.getInstance().setOnClickListener(mTvConfirm,this::onConfirm,true);
    }

    private void onConfirm(View view) {
        if (getChooseSize() == 0) {
            return;
        }
        doDeleteMem();
    }

    private void doDeleteMem() {
        List<String> choosedList = DeleteMemManager.getInstance().getChoosedList();
        EaseManager.getInstance().deleteGroupMembers(mGroup.getGroupId(), choosedList, new EMCallBack() {
            @Override
            public void onSuccess() {
                ToastUtils.toast("删除成功");
                LiveDataBus.get().with(Constants.KEY_REFRESH_MEMBERS,String.class).postValue("");
            }

            @Override
            public void onError(int i, String s) {
                ToastUtils.toast("删除失败：" + i + s);
            }
        },true);
        dismiss();
        DeleteMemManager.getInstance().clear();
    }

    private void setNextState() {
        if (mTvConfirm == null) {
            return;
        }
        DisplayUtil.setTextDisabled(mTvConfirm, getChooseSize());
    }

    private int getChooseSize() {
        return DeleteMemManager.getInstance().getChoosedList().size();
    }

    @Override
    protected int getLayout() {
        return R.layout.dialog_delete_mem;
    }
}
