package com.imchat.chanttyai.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.imchat.chanttyai.base.BaseAdapter;
import com.imchat.chanttyai.beans.MemTitleBean;
import com.imchat.chanttyai.databinding.ItemMemTabBinding;
import com.imchat.chanttyai.listeners.OnItemClickListener;

import java.util.List;

public class MemTabAdapter extends BaseAdapter<MemTitleBean, ItemMemTabBinding> {

    private int checkedPos;

    public MemTabAdapter(List<MemTitleBean> list, OnItemClickListener<MemTitleBean> onItemClickListener) {
        super(list,onItemClickListener);
    }

    @Override
    protected ItemMemTabBinding getViewBinding(ViewGroup parent) {
        return ItemMemTabBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
    }

    @Override
    protected void bindViewHolder(BaseAdapter<MemTitleBean, ItemMemTabBinding>.NormalHolder holder, MemTitleBean memTitleBean, int pos) {
        holder.mBinding.tvName.setText(memTitleBean.getTitle());

        holder.mBinding.tvName.setTextColor(checkedPos == pos ? Color.WHITE:Color.parseColor("#5E686E"));
        holder.mBinding.vTab.setVisibility(checkedPos == pos ? View.VISIBLE:View.INVISIBLE);
    }

    public void setChecked(int pos){
        checkedPos = pos;
        notifyDataSetChanged();
    }
}
