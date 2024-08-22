package com.imchat.chanttyai.adapters;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.chat.EMUserInfo;
import com.hyphenate.exceptions.HyphenateException;
import com.imchat.chanttyai.base.BaseActivity;
import com.imchat.chanttyai.base.BaseAdapter;
import com.imchat.chanttyai.base.Constants;
import com.imchat.chanttyai.beans.MentionMsgBean;
import com.imchat.chanttyai.databinding.ItemConversationBinding;
import com.imchat.chanttyai.listeners.OnItemClickListener;
import com.imchat.chanttyai.ui.fragment.manager.AvatarManager;
import com.imchat.chanttyai.ui.fragment.manager.EaseManager;
import com.imchat.chanttyai.utils.DateUtils;
import com.imchat.chanttyai.utils.GsonUtils;
import com.imchat.chanttyai.utils.SharedPreferUtil;
import com.othershe.combinebitmap.CombineBitmap;
import com.othershe.combinebitmap.layout.DingLayoutManager;

import java.util.List;
import java.util.Map;

public class ConversationAdapter extends BaseAdapter<Map.Entry<String, EMConversation>, ItemConversationBinding> {
    private final BaseActivity mContext;

    public ConversationAdapter(OnItemClickListener<Map.Entry<String, EMConversation>> onItemClickListener, BaseActivity context) {
        super(onItemClickListener);
        mContext = context;
    }

    @Override
    protected ItemConversationBinding getViewBinding(ViewGroup parent) {
        return ItemConversationBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void bindViewHolder(BaseAdapter<Map.Entry<String, EMConversation>, ItemConversationBinding>.NormalHolder holder, Map.Entry<String, EMConversation> map, int pos) {
        EMConversation conversation = map.getValue();

        if (conversation.isGroup()) {
            setGroupDisplay(holder, conversation);
        } else {
            setSingleDisplay(holder, conversation);
        }

        //未读
        EMMessage lastMessage = conversation.getLastMessage();
        int unReadCount = conversation.getUnreadMsgCount();
        if (unReadCount > 0) {
            holder.mBinding.tvUnread.setVisibility(View.VISIBLE);
            holder.mBinding.tvUnread.setText(SharedPreferUtil.getInstance().getUnReadCount(unReadCount));
        } else {
            holder.mBinding.tvUnread.setVisibility(View.GONE);
        }
        holder.mBinding.tvTime.setText(DateUtils.formatTimestamp(lastMessage.getMsgTime(), true));
    }

    private void setSingleDisplay(NormalHolder holder, EMConversation conversation) {
        EMMessage lastMessage = conversation.getLastMessage();
        EMMessageBody body = lastMessage.getBody();
        String account = conversation.conversationId();
        //名称
        String name = SharedPreferUtil.getInstance().getAIBean(account).getBotName();
        holder.mBinding.tvName.setText(TextUtils.isEmpty(name) ? account : name);
        //头像
        holder.mBinding.ivAvatar.setImageResource(AvatarManager.getInstance().getAIAvatarBean(SharedPreferUtil.getInstance().getAIBean(account).getPic()).getHead());

        //内容
        if (body instanceof EMTextMessageBody) {
            String content = ((EMTextMessageBody) body).getMessage();
            holder.mBinding.tvContent.setText(content);
        }
    }

    private void setGroupDisplay(NormalHolder holder, EMConversation conversation) {
        EMGroup group = EMClient.getInstance().groupManager().getGroup(conversation.conversationId());

        if (group != null) {
            handleSuccess(holder, conversation, group);
            return;
        }

        EaseManager.getInstance().getGroupFromServer(conversation.conversationId(), new EMValueCallBack<EMGroup>() {
            @Override
            public void onSuccess(EMGroup group) {
                handleSuccess(holder, conversation, group);
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    private void handleSuccess(BaseAdapter<Map.Entry<String, EMConversation>, ItemConversationBinding>.NormalHolder holder, EMConversation conversation, EMGroup group) {
        //名称
        holder.mBinding.tvName.setText(group.getGroupName());
        EaseManager.getInstance().fetchGroupMembers(conversation.conversationId(), EaseManager.MEMBER_TYPE.ALL, new EMValueCallBack<Map<String, EMUserInfo>>() {
            @Override
            public void onSuccess(Map<String, EMUserInfo> map) {

                //头像
                CombineBitmap.init(mContext)
                        .setLayoutManager(new DingLayoutManager()) // 必选， 设置图片的组合形式，支持WechatLayoutManager、DingLayoutManager
                        .setSize(50) // 必选，组合后Bitmap的尺寸，单位dp
                        .setGap(1) // 单个图片之间的距离，单位dp，默认0dp
                        .setResourceIds(SharedPreferUtil.getInstance().getAvatarResArr(map)) // 要加载的图片资源id数组
                        .setImageView(holder.mBinding.ivAvatar) // 直接设置要显示图片的ImageView
                        .build();
                //内容
                EMMessage lastMessage = conversation.getLastMessage();
                EMMessageBody body = lastMessage.getBody();
                if (body instanceof EMTextMessageBody) {
                    String from = lastMessage.getFrom();
                    String content = "";
                    boolean isAt = false;

                    try {
                        String str = lastMessage.getStringAttribute(Constants.KEY_MENTION);
                        if (!TextUtils.isEmpty(str)) {
                            List<MentionMsgBean> list = GsonUtils.toList(str, MentionMsgBean.class);
                            isAt = list.stream().anyMatch(item -> TextUtils.equals(item.getId(), SharedPreferUtil.getInstance().getAccount()));
                        }
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                    }
                    content = ((EMTextMessageBody) body).getMessage();

                    if (TextUtils.equals(from, SharedPreferUtil.getInstance().getAccount())) {
                        holder.mBinding.tvContent.setText(conversation.isGroup() ? ("我：" + content) : content);
                    } else {
                        EMUserInfo userInfo = map.get(from);
                        String nickname = "";
                        if (from.startsWith("bot")) {
                            //机器人
                            nickname = SharedPreferUtil.getInstance().getAIBean(from).getBotName();
                        } else {
                            //人类
                            nickname = userInfo != null && !TextUtils.isEmpty(userInfo.getNickname()) ? userInfo.getNickname() : from;
                        }
                        holder.mBinding.tvContent.setText(isAt && conversation.getUnreadMsgCount() > 0 ? "[有人@我]" : (nickname + "：") + content);
                    }
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        }, false);
    }
}
