package com.imchat.chanttyai.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.imchat.chanttyai.base.BaseAdapter;
import com.imchat.chanttyai.beans.MineOptBean;
import com.imchat.chanttyai.databinding.ItemMineOptBinding;
import com.imchat.chanttyai.listeners.OnItemClickListener;

import java.util.List;

public class MineOptAdapter extends BaseAdapter<MineOptBean, ItemMineOptBinding> {
    public MineOptAdapter(List<MineOptBean> list, OnItemClickListener<MineOptBean> onItemClickListener) {
        super(list,onItemClickListener);
    }

    @Override
    protected ItemMineOptBinding getViewBinding(ViewGroup parent) {
        return ItemMineOptBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
    }

    @Override
    protected void bindViewHolder(BaseAdapter<MineOptBean, ItemMineOptBinding>.NormalHolder holder, MineOptBean bean, int pos) {
        holder.mBinding.ivIcon.setImageResource(bean.getIcon());
        holder.mBinding.tvTitle.setText(bean.getTitle());
        holder.mBinding.tvSub.setText(bean.getSub());
    }
}
