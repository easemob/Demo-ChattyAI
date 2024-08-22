package com.imchat.chanttyai.adapters;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.hyphenate.chat.EMUserInfo;
import com.imchat.chanttyai.R;
import com.imchat.chanttyai.base.BaseAdapter;
import com.imchat.chanttyai.databinding.ItemManageMemBinding;
import com.imchat.chanttyai.ui.fragment.manager.AvatarManager;
import com.imchat.chanttyai.utils.SharedPreferUtil;

import java.util.List;

public class ManageMemAdapter extends BaseAdapter<EMUserInfo, ItemManageMemBinding> {
    private boolean mIsHuman;

    public ManageMemAdapter(boolean isHuman) {
        mIsHuman = isHuman;
    }
    public ManageMemAdapter(List<SHOW_MODE> list, boolean isHuman) {
        super(list,isHuman);
        mIsHuman = isHuman;
    }

    @Override
    protected int getCustomLayout() {
        if (mIsHuman){
            return R.layout.item_remove_mem;
        }
        return R.layout.item_group_add;
    }

    @Override
    protected ItemManageMemBinding getViewBinding(ViewGroup parent) {
        return ItemManageMemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
    }

    @Override
    protected void bindViewHolder(BaseAdapter<EMUserInfo, ItemManageMemBinding>.NormalHolder holder, EMUserInfo bean, int pos) {

        if (mIsHuman){
            String nickname = bean.getNickname();
            holder.mBinding.ivAvatar.setImageResource(AvatarManager.getInstance().getUserAvatarRes(bean.getAvatarUrl()));
            holder.mBinding.tvName.setText(TextUtils.isEmpty(nickname)?bean.getUserId():nickname);
        }else {
            holder.mBinding.ivAvatar.setImageResource(AvatarManager.getInstance().getAIAvatarBean(SharedPreferUtil.getInstance().getAIBean(bean.getUserId()).getPic()).getHead());
            holder.mBinding.tvName.setText(SharedPreferUtil.getInstance().getAIBean(bean.getUserId()).getBotName());
        }
    }
}
