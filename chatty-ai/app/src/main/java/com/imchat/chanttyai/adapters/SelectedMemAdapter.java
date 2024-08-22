package com.imchat.chanttyai.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.imchat.chanttyai.R;
import com.imchat.chanttyai.base.BaseAdapter;
import com.imchat.chanttyai.beans.AIBean;
import com.imchat.chanttyai.databinding.ItemGroupSelectedMemBinding;
import com.imchat.chanttyai.ui.fragment.manager.AvatarManager;

import java.util.ArrayList;
import java.util.List;

public class SelectedMemAdapter extends BaseAdapter<AIBean, ItemGroupSelectedMemBinding> {
    public SelectedMemAdapter(ArrayList<AIBean> aiBeans, List<SHOW_MODE> list) {
        super(aiBeans,list);
    }

    @Override
    protected int getCustomLayout() {
        return R.layout.item_group_add;
    }

    @Override
    protected ItemGroupSelectedMemBinding getViewBinding(ViewGroup parent) {
        return ItemGroupSelectedMemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
    }

    @Override
    protected void bindViewHolder(BaseAdapter<AIBean, ItemGroupSelectedMemBinding>.NormalHolder holder, AIBean bean, int pos) {

        holder.mBinding.ivAvatar.setImageResource(AvatarManager.getInstance().getAIAvatarBean(bean.getPic()).getHead());
        holder.mBinding.tvName.setText(bean.getBotName());
    }
}
