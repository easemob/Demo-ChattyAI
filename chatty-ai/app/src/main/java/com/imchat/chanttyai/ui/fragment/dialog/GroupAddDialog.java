package com.imchat.chanttyai.ui.fragment.dialog;

import android.graphics.Color;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMMessage;
import com.imchat.chanttyai.R;
import com.imchat.chanttyai.adapters.SelectedMemAdapter;
import com.imchat.chanttyai.base.BaseActivity;
import com.imchat.chanttyai.base.BaseAdapter;
import com.imchat.chanttyai.base.Constants;
import com.imchat.chanttyai.livedatas.LiveDataBus;
import com.imchat.chanttyai.ui.activity.ChatActivity;
import com.imchat.chanttyai.ui.fragment.manager.ChooseMemManager;
import com.imchat.chanttyai.ui.fragment.manager.EaseManager;
import com.imchat.chanttyai.utils.ClickHelper;
import com.imchat.chanttyai.utils.DisplayUtil;
import com.imchat.chanttyai.utils.LogUtils;
import com.imchat.chanttyai.utils.ToastUtils;
import com.imchat.chanttyai.views.QBottomSheetDialog;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class GroupAddDialog extends QBottomSheetDialog {

    private SwipeRecyclerView mRv;
    private SelectedMemAdapter mAdapter;
    private TextView mTvCreate;
    private EditText mEtName;
    private TextView mTvNameCount;
    private boolean mCreateEnabled;

    public GroupAddDialog(@NonNull BaseActivity context) {
        super(context);
    }

    @Override
    protected void initView() {
        super.initView();
        mRv = findViewById(R.id.rv);
        mTvCreate = findViewById(R.id.tv_create);
        mEtName = findViewById(R.id.et_name);
        mTvNameCount = findViewById(R.id.tv_name_count);

        List<BaseAdapter.SHOW_MODE> list = new ArrayList<>();
        list.add(BaseAdapter.SHOW_MODE.SHOW_CUSTOM);
        mAdapter = new SelectedMemAdapter(new ArrayList<>(), list);
        mRv.setAdapter(mAdapter);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mAdapter.setOnCustomItemClickListener(this::onGroupAddMem);

        ClickHelper.getInstance().setOnClickListener(mTvCreate,this::onCreateGroup,true);

        DisplayUtil.listenEtLength(mEtName, mTvNameCount,32,this::switchCreateDisabled);

        LiveDataBus.get().with(Constants.KEY_SHOW_GROUP_CREATE, String.class).observe(mContext, this::onRefresh);
    }

    private void switchCreateDisabled() {
        mCreateEnabled = mEtName.getText().toString().length() > 0 && ChooseMemManager.getInstance().getResultList().size() > 0;
        mTvCreate.setTextColor(Color.parseColor(mCreateEnabled ? "#33B1FF" : "#464E53"));
    }

    private void onRefresh(String s) {
        mAdapter.setData(ChooseMemManager.getInstance().getResultList());
        switchCreateDisabled();
    }


    private void onGroupAddMem() {
        LiveDataBus.get().with(Constants.KEY_SHOW_GROUP_ADD_MEM).postValue("");
    }

    @Override
    protected int getLayout() {
        return R.layout.dialog_add_group;
    }

    private void onCreateGroup(View view) {
        if (!mCreateEnabled) {
            return;
        }
        EaseManager.getInstance().createGroup(mEtName.getText().toString(),  ChooseMemManager.getInstance().getResultArr(), new EMValueCallBack<EMGroup>() {
            @Override
            public void onSuccess(EMGroup value) {
                String groupId = value.getGroupId();
                ToastUtils.toast("创建成功");
                sendCreateGroupMsg("欢迎欢迎", groupId);
                dismiss();
                ChatActivity.start(mContext,groupId,true);
                LiveDataBus.get().with(Constants.KEY_REFRESH_CONVERSATION, String.class).postValue("");
            }

            @Override
            public void onError(int error, String errorMsg) {
                LogUtils.e("创建失败:" + error + errorMsg);
                ToastUtils.toast("创建失败，请重试~");
            }
        },true);
    }

    public void sendCreateGroupMsg(String msg, String groupId) {
        EMMessage message = EMMessage.createTxtSendMessage(msg, groupId);
        message.setChatType(EMMessage.ChatType.GroupChat);
        EMClient.getInstance().chatManager().sendMessage(message);
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ChooseMemManager.getInstance().clearAll();
    }
}
