package com.imchat.chanttyai.adapters;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.hyphenate.chat.EMUserInfo;
import com.imchat.chanttyai.base.BaseAdapter;
import com.imchat.chanttyai.beans.AIBean;
import com.imchat.chanttyai.databinding.ItemMentionBinding;
import com.imchat.chanttyai.listeners.OnItemClickListener;
import com.imchat.chanttyai.ui.fragment.manager.AvatarManager;
import com.imchat.chanttyai.utils.SharedPreferUtil;

public class MentionAdapter extends BaseAdapter<EMUserInfo, ItemMentionBinding> {

    public MentionAdapter(OnItemClickListener<EMUserInfo> onItemClickListener) {
        super(onItemClickListener);
    }

    @Override
    protected ItemMentionBinding getViewBinding(ViewGroup parent) {
        return ItemMentionBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
    }

    @Override
    protected void bindViewHolder(BaseAdapter<EMUserInfo, ItemMentionBinding>.NormalHolder holder, EMUserInfo bean, int pos) {
        String userId = bean.getUserId();
        String nickname = bean.getNickname();
        int head;
        if (userId.startsWith("bot")) {
            //机器人
            AIBean aiBean = SharedPreferUtil.getInstance().getAIBean(userId);
            head = AvatarManager.getInstance().getAIAvatarBean(aiBean.getPic()).getHead();
            nickname = aiBean.getBotName();
        } else {
            head = AvatarManager.getInstance().getUserAvatarRes(bean.getAvatarUrl());
            nickname = TextUtils.isEmpty(nickname) ? userId : nickname;
        }

        holder.mBinding.ivAvatar.setImageResource(head);
        holder.mBinding.tvName.setText(nickname);
    }

}
