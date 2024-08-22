package com.imchat.chanttyai.adapters;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.chat.EMUserInfo;
import com.imchat.chanttyai.R;
import com.imchat.chanttyai.beans.AIBean;
import com.imchat.chanttyai.beans.MentionMsgBean;
import com.imchat.chanttyai.ui.fragment.manager.AvatarManager;
import com.imchat.chanttyai.ui.fragment.manager.EaseManager;
import com.imchat.chanttyai.utils.DateUtils;
import com.imchat.chanttyai.utils.SharedPreferUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MsgHolder> {

    protected static final int TYPE_LEFT = 0;
    protected static final int TYPE_RIGHT = 1;
    private List<EMMessage> mList;
    private Map<String, EMUserInfo> mUserMap = new HashMap<>();

    private boolean mIsGroup;
    private OnAvatarLongClick mOnAvatarLongClick;

    public void setData(List<EMMessage> list) {
        mList = list;
        EaseManager.getInstance().sortMessages(mList);
        notifyDataSetChanged();
    }

    public ChatAdapter(boolean isGroup, OnAvatarLongClick onAvatarLongClick) {
        mIsGroup = isGroup;
        mOnAvatarLongClick = onAvatarLongClick;
    }

    @NonNull
    @Override
    public ChatAdapter.MsgHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_LEFT) {
            return new MsgHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_left, parent, false));
        }
        return new MsgHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_right, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.MsgHolder holder, int position) {
        EMMessage currentMessage = mList.get(position);
        String from = currentMessage.getFrom();
        if (getItemViewType(position) == TYPE_LEFT) {
            String name = "";
            String avatar = "";
            if (from.startsWith("bot")) {
                AIBean aiBean = SharedPreferUtil.getInstance().getAIBean(from);
                name = aiBean.getBotName();
                avatar = aiBean.getPic();
                holder.mTvName.setText(name);
                holder.mIvAvatar.setImageResource(AvatarManager.getInstance().getAIAvatarBean(avatar).getHead());
            } else {
                EMUserInfo userInfo = SharedPreferUtil.getInstance().getUserInfo(mUserMap, from);

                name = userInfo != null && !TextUtils.isEmpty(userInfo.getNickname()) ? userInfo.getNickname() : from;
                holder.mTvName.setText(name);

                avatar = userInfo != null && !TextUtils.isEmpty(userInfo.getAvatarUrl()) ? userInfo.getAvatarUrl() : "0";
                holder.mIvAvatar.setImageResource(AvatarManager.getInstance().getUserAvatarRes(avatar));
            }

            holder.mTvName.setVisibility(mIsGroup ? View.VISIBLE : View.GONE);

            String finalName = name;
            String finalAvatar = avatar;
            if (mIsGroup) {
                holder.mIvAvatar.setOnLongClickListener(view -> {
                    if (mOnAvatarLongClick != null) {
                        mOnAvatarLongClick.onLongClick(new MentionMsgBean(from, finalName, finalAvatar));
                        return true;
                    }
                    return false;
                });
            }
        }

        EMMessageBody body = currentMessage.getBody();
        if (body instanceof EMTextMessageBody) {
            holder.mTvContent.setText(((EMTextMessageBody) body).getMessage());
        }
        long currentTime = mList.get(position).getMsgTime();
        String timeStr = DateUtils.formatTimestamp(currentTime, true);
        holder.mTvTime.setText(timeStr);
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        EMMessage message = mList.get(position);
        if (TextUtils.equals(message.getFrom(), SharedPreferUtil.getInstance().getAccount())) {
            return TYPE_RIGHT;
        }
        return TYPE_LEFT;
    }

    public void setUserInfo(Map<String, EMUserInfo> map) {
        mUserMap = map;
        notifyDataSetChanged();
    }

    public String getMyLastMessage() {
        if (mList == null || mList.size() == 0){
            return "";
        }
        List<EMMessage> list = mList.stream().filter(msg -> TextUtils.equals(msg.getFrom(), SharedPreferUtil.getInstance().getAccount())).collect(Collectors.toList());
        if (list.size() < 2) {
            return "";
        }
        EMMessageBody body = list.get(list.size() - 1).getBody();
        return ((EMTextMessageBody) body).getMessage();
    }

    protected static class MsgHolder extends RecyclerView.ViewHolder {

        private ImageView mIvAvatar;
        private TextView mTvName;
        protected TextView mTvContent;
        protected TextView mTvTime;

        public MsgHolder(@NonNull View itemView) {
            super(itemView);
            mTvName = itemView.findViewById(R.id.tv_name);
            mIvAvatar = itemView.findViewById(R.id.iv_avatar);
            mTvContent = itemView.findViewById(R.id.tv_content);
            mTvTime = itemView.findViewById(R.id.tv_time);

        }
    }

    public interface OnAvatarLongClick {
        void onLongClick(MentionMsgBean mentionMsgBean);
    }
}
