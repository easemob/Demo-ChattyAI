package com.imchat.chanttyai.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMUserInfo;
import com.imchat.chanttyai.R;
import com.imchat.chanttyai.adapters.ChatAdapter;
import com.imchat.chanttyai.base.App;
import com.imchat.chanttyai.base.AppManager;
import com.imchat.chanttyai.base.BaseActivity;
import com.imchat.chanttyai.base.Constants;
import com.imchat.chanttyai.beans.AIBean;
import com.imchat.chanttyai.beans.Empty;
import com.imchat.chanttyai.beans.MentionMsgBean;
import com.imchat.chanttyai.databinding.ActivityChatBinding;
import com.imchat.chanttyai.http.QObserver;
import com.imchat.chanttyai.listeners.FetchMessageListener;
import com.imchat.chanttyai.livedatas.LiveDataBus;
import com.imchat.chanttyai.ui.fragment.dialog.MentionDialog;
import com.imchat.chanttyai.ui.fragment.manager.AvatarManager;
import com.imchat.chanttyai.ui.fragment.manager.EaseManager;
import com.imchat.chanttyai.utils.ClickHelper;
import com.imchat.chanttyai.utils.GsonUtils;
import com.imchat.chanttyai.utils.LogUtils;
import com.imchat.chanttyai.utils.SharedPreferUtil;
import com.imchat.chanttyai.utils.ToastUtils;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ChatActivity extends BaseActivity<ActivityChatBinding> implements View.OnClickListener {

    private String mChatTo;
    private ChatAdapter mAdapter;
    private EMConversation mConversation;
    private Map<String, EMUserInfo> mMemberMap;
    private MentionDialog mMentionDialog;
    private boolean mIsGroup;

    private String mStartMsgId = "";

    private List<EMMessage> mList = new ArrayList<>();

    public static void start(Context context, String chatTo, boolean isGroup) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(Constants.KEY_CHAT_TO, chatTo);
        intent.putExtra(Constants.KEY_IS_GROUP, isGroup);
        context.startActivity(intent);
    }

    @Override
    protected ActivityChatBinding getViewBinding() {
        return ActivityChatBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initView() {

    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void initData() {
        mChatTo = getIntent().getStringExtra(Constants.KEY_CHAT_TO);
        mIsGroup = getIntent().getBooleanExtra(Constants.KEY_IS_GROUP, false);

        getConversation();

        initBg();

        initRv();

        loadMsgHistory(true);

        fetchMem("");
    }

    private void getConversation() {
        if (mConversation == null) {
            mConversation = EMClient.getInstance().chatManager().getConversation(mChatTo);
        }
    }

    private void fetchMem(String s) {
        if (!mIsGroup) {
            return;
        }
        EaseManager.getInstance().fetchGroupMembers(mChatTo, EaseManager.MEMBER_TYPE.ALL, new EMValueCallBack<Map<String, EMUserInfo>>() {
            @Override
            public void onSuccess(Map<String, EMUserInfo> map) {
                mMemberMap = map;
                mAdapter.setUserInfo(map);
            }

            @Override
            public void onError(int i, String s) {

            }
        }, false);

    }

    private void initBg() {
        mBinding.ivMore.setVisibility(mIsGroup ? View.VISIBLE : View.GONE);

        AIBean aiBean = SharedPreferUtil.getInstance().getAIBean(mChatTo);

        int bg = AvatarManager.getInstance().getAIAvatarBean(aiBean.getPic()).getBg();
        if (bg != R.drawable.empty) {
            mBinding.ivBg.setImageResource(bg);
        }

        String chatName = aiBean.getBotName();
        if (mIsGroup) {
            chatName = EMClient.getInstance().groupManager().getGroup(mChatTo).getGroupName();
        }
        mBinding.tvName.setText(chatName);
    }

    private void loadMsgHistory(boolean toLast) {
        if (mConversation == null) {
            return;
        }

        EaseManager.getInstance().getMessages(mConversation, mStartMsgId, new FetchMessageListener() {
            @Override
            public void onDone(List<EMMessage> messages, String startMessageId) {
                mList.addAll(messages);
                mStartMsgId = startMessageId;
                mAdapter.setData(mList);
                mBinding.srl.finishRefresh();
                scrollList(toLast);

                //更新未读
                mConversation.markAllMessagesAsRead();
                LiveDataBus.get().with(Constants.MSG_READ).postValue("");
            }

            @Override
            public void onFailed(String msg) {
                ToastUtils.toast(msg);
                mBinding.srl.finishRefresh();
            }
        });
    }

    private void updateMsg() {
        if (mConversation == null) {
            return;
        }

        String startMsgId = "";
        if (mList != null && !mList.isEmpty()) {
            startMsgId = mList.get(mList.size() - 1).getMsgId();
        }
        List<EMMessage> messages = mConversation.loadMoreMsgFromDB(startMsgId, 1, EMConversation.EMSearchDirection.DOWN);
        mList.addAll(messages);
        mAdapter.setData(mList);
        scrollList(true);

        //更新未读
        mConversation.markAllMessagesAsRead();
        LiveDataBus.get().with(Constants.MSG_READ).postValue("");
    }

    private void scrollList(boolean toLast) {
        int count = mAdapter.getItemCount() - 1;
        mBinding.rv.post(() -> mBinding.rv.scrollToPosition(toLast ? count : 0));
    }

    private void initRv() {
        mAdapter = new ChatAdapter(mIsGroup, bean -> insertMention(bean, true));
        mBinding.rv.setAdapter(mAdapter);
    }

    @Override
    protected void initListener() {
        ClickHelper.getInstance().setOnClickListener(mBinding.tvSend, this, false);
        mBinding.etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int i2) {
                LogUtils.e(start + " " + before);
                String content = mBinding.etContent.getText().toString().trim();
                mBinding.tvSend.setVisibility(TextUtils.isEmpty(content) ? View.GONE : View.VISIBLE);

                int cursorPosition = mBinding.etContent.getSelectionStart();
                if (mIsGroup && cursorPosition > 0 && charSequence.charAt(cursorPosition - 1) == '@' && before == 0) {
                    // 最后一个字符是@时触发的逻辑
                    showMentionDialog();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        ClickHelper.getInstance().setOnClickListener(mBinding.ivMore, this, true);

        //回车发送
//        mBinding.etContent.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
//                if (actionId == EditorInfo.IME_ACTION_SEND ||
//                        (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
//                                keyEvent.getAction() == KeyEvent.ACTION_DOWN)) {
//                    // 在这里执行发送操作
//                    sendMsg();
//                    return true;
//                }
//                return false;
//            }
//        });

        LiveDataBus.get().with(Constants.KEY_REFRESH_MEMBERS, String.class).observe(this, this::fetchMem);

        LiveDataBus.get().with(Constants.MSG_RECEIVED, String.class).observe(this, this::onMsgReceived);

        LiveDataBus.get().with(Constants.KEY_REFRESH_GROUP_NAME, String.class).observe(this, this::refreshGroupName);

        LiveDataBus.get().with(Constants.KEY_DELETE_GROUP, String.class).observe(this, this::groupDelete);

        setKeyboardListener(() -> scrollList(true));

        mBinding.srl.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                loadMsgHistory(false);
            }
        });
    }

    private void groupDelete(String s) {
        finishThis();
    }

    private void refreshGroupName(String name) {
        mBinding.tvName.setText(name);
    }

    private void onMsgReceived(String nul) {
        updateMsg();
    }

    private void sendMsg() {
        String content = mBinding.etContent.getText().toString();
        EMMessage message = EMMessage.createTextSendMessage(content, mChatTo);
        message.setChatType(mIsGroup ? EMMessage.ChatType.GroupChat : EMMessage.ChatType.Chat);
        if (!mentionedMemList.isEmpty()) {
            message.setAttribute(Constants.KEY_MENTION, GsonUtils.toJson(mentionedMemList));
        }
        // 发送消息
        EMClient.getInstance().chatManager().sendMessage(message);

        //如果是群聊@ 给后端也发消息
        if (mIsGroup) {
            sendGroupMsg();
        }

        getConversation();
        mBinding.etContent.setText("");
        updateMsg();
        if (!mentionedMemList.isEmpty()) mentionedMemList.clear();
    }

    private List<MentionMsgBean> mentionedMemList = new ArrayList<>();

    private void showMentionDialog() {
        mMentionDialog = new MentionDialog(mContext, mMemberMap, (userInfo, aiBean) -> {
            //点击人物
            String nickname;
            String id;
            String avatar;
            if (aiBean != null && !TextUtils.isEmpty(aiBean.getEaAccount())) {
                nickname = aiBean.getBotName();
                id = aiBean.getEaAccount();
                avatar = aiBean.getPic();
            } else {
                nickname = userInfo.getNickname();
                if (TextUtils.isEmpty(nickname)) {
                    nickname = userInfo.getUserId();
                }
                id = userInfo.getUserId();
                avatar = userInfo.getAvatarUrl();
            }

            insertMention(new MentionMsgBean(id, nickname, avatar), false);
        });

        mMentionDialog.setOnDismissListener(dialogInterface -> toggleKeyboard());

        mMentionDialog.show();
    }

    private void insertMention(MentionMsgBean bean, boolean addAt) {
        int cursorPosition = mBinding.etContent.getSelectionStart();

        //添加需要@的成员
        mentionedMemList.add(bean);

        String content = mBinding.etContent.getText().toString();
        StringBuilder sb = new StringBuilder(content);
        sb.insert(cursorPosition, (addAt ? "@" : "") + bean.getName() + " ");

        content = sb.toString();
        mBinding.etContent.setText(content);

        //光标移动到最后
        mBinding.etContent.setSelection(content.length());
    }

    private void sendGroupMsg() {
        if (mentionedMemList == null || mentionedMemList.isEmpty()) {
            return;
        }

        String[] mentionAccounts = mentionedMemList.stream().filter(mentionMsgBean -> mBinding.etContent.getText().toString().contains("@" + mentionMsgBean.getName())).map(MentionMsgBean::getId).toArray(String[]::new);
        if (mentionAccounts.length == 0) {
            return;
        }

        String myDoMentionMessage = mBinding.etContent.getText().toString();

        for (MentionMsgBean mentionMsgBean : mentionedMemList) {
            myDoMentionMessage = myDoMentionMessage.replace("@" + mentionMsgBean.getName() + " ", "");
        }

        if (TextUtils.isEmpty(myDoMentionMessage)) {
            myDoMentionMessage = mAdapter.getMyLastMessage();
        }

        if (TextUtils.isEmpty(myDoMentionMessage)) {
            return;
        }

        Map<String, Object> map = new HashMap<>();
        map.put("senderAccount", SharedPreferUtil.getInstance().getAccount());
        map.put("botAccount", mentionAccounts);
        map.put("groupId", mChatTo);
        map.put("message", myDoMentionMessage);
        LogUtils.e(GsonUtils.toJson(map));
        App.getApi().sendGroupMessage(map)
                .subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new QObserver<Empty>(mContext, false, false) {
                    @Override
                    public void next(Empty empty) {
                        LogUtils.e("@成功");
                    }
                });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.tv_send) {
            sendMsg();
            return;
        }

        if (id == R.id.iv_back) {
            AppManager.getInstance().finishActivity(this);
            return;
        }

        if (id == R.id.iv_more) {
            GroupManageActivity.start(mContext, mChatTo);
        }
    }
}
