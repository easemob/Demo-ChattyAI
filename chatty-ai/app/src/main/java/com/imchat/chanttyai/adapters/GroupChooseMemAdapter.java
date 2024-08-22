package com.imchat.chanttyai.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.hyphenate.chat.EMUserInfo;
import com.imchat.chanttyai.R;
import com.imchat.chanttyai.base.BaseAdapter;
import com.imchat.chanttyai.beans.AIBean;
import com.imchat.chanttyai.beans.AvatarBean;
import com.imchat.chanttyai.databinding.ItemGroupChooseMemBinding;
import com.imchat.chanttyai.ui.fragment.manager.AvatarManager;
import com.imchat.chanttyai.ui.fragment.manager.ChooseMemManager;
import com.imchat.chanttyai.utils.ClickHelper;

import java.util.HashMap;
import java.util.Map;

public class GroupChooseMemAdapter extends BaseAdapter<AIBean, ItemGroupChooseMemBinding> {

    private Map<String, EMUserInfo> mAIMemberMap = new HashMap<>();

    @Override
    protected ItemGroupChooseMemBinding getViewBinding(ViewGroup parent) {
        return ItemGroupChooseMemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
    }

    @Override
    protected void bindViewHolder(BaseAdapter<AIBean, ItemGroupChooseMemBinding>.NormalHolder holder, AIBean bean, int pos) {
        int head = R.drawable.onlight;
        AvatarBean avatarBean = AvatarManager.getInstance().getAIAvatarBean(bean.getPic());
        if (avatarBean != null){
            head = avatarBean.getHead();
        }

        holder.mBinding.ivAvatar.setImageResource(head);
        holder.mBinding.tvName.setText(bean.getBotName());
        holder.mBinding.tvDesc.setText(bean.getDescribe());
        holder.mBinding.cb.setChecked(ChooseMemManager.getInstance().contains(bean) || mAIMemberMap.containsKey(bean.getEaAccount()));

        ClickHelper.getInstance().setOnClickListener(holder.itemView, view -> {
            if (mAIMemberMap.containsKey(bean.getEaAccount())){
                return;
            }
            ChooseMemManager.getInstance().addOrRemove(bean,mAIMemberMap);
            notifyDataSetChanged();
        },false);
    }

    public void setAIMembers(Map<String, EMUserInfo> map) {
        mAIMemberMap = map;
        notifyDataSetChanged();
    }
}
