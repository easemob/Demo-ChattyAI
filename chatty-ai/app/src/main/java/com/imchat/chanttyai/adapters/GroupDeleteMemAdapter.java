package com.imchat.chanttyai.adapters;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.hyphenate.chat.EMUserInfo;
import com.imchat.chanttyai.base.BaseAdapter;
import com.imchat.chanttyai.databinding.ItemGroupDeleteMemBinding;
import com.imchat.chanttyai.ui.fragment.manager.AvatarManager;
import com.imchat.chanttyai.ui.fragment.manager.DeleteMemManager;
import com.imchat.chanttyai.utils.ClickHelper;

public class GroupDeleteMemAdapter extends BaseAdapter<EMUserInfo, ItemGroupDeleteMemBinding> {
    @Override
    protected ItemGroupDeleteMemBinding getViewBinding(ViewGroup parent) {
        return ItemGroupDeleteMemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
    }

    @Override
    protected void bindViewHolder(BaseAdapter<EMUserInfo, ItemGroupDeleteMemBinding>.NormalHolder holder, EMUserInfo bean, int pos) {
        String account = bean.getUserId();
        String nickname = TextUtils.isEmpty(bean.getNickname())? account :bean.getNickname();
        holder.mBinding.ivAvatar.setImageResource(AvatarManager.getInstance().getUserAvatarRes(bean.getAvatarUrl()));
        holder.mBinding.tvName.setText(nickname);

        holder.mBinding.cb.setChecked(DeleteMemManager.getInstance().contains(account));

        ClickHelper.getInstance().setOnClickListener(holder.itemView, view -> {
            DeleteMemManager.getInstance().addOrRemove(account);
            notifyDataSetChanged();
        },false);
    }
}
