package com.imchat.chanttyai.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.imchat.chanttyai.base.BaseAdapter;
import com.imchat.chanttyai.databinding.ItemMentionTabBinding;
import com.imchat.chanttyai.listeners.OnItemClickListener;

import java.util.List;

public class MentionTabAdapter extends BaseAdapter<String, ItemMentionTabBinding> {

    private int checkedPos;

    public MentionTabAdapter(List<String> list, OnItemClickListener<String> onItemClickListener) {
        super(list,onItemClickListener);
    }

    @Override
    protected ItemMentionTabBinding getViewBinding(ViewGroup parent) {
        return ItemMentionTabBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
    }

    @Override
    protected void bindViewHolder(BaseAdapter<String, ItemMentionTabBinding>.NormalHolder holder, String str, int pos) {
        holder.mBinding.tvName.setText(str);

        holder.mBinding.tvName.setTextColor(checkedPos == pos ? Color.WHITE:Color.parseColor("#5E686E"));
        holder.mBinding.vTab.setVisibility(checkedPos == pos ? View.VISIBLE:View.INVISIBLE);
    }

    public void setChecked(int pos){
        checkedPos = pos;
        notifyDataSetChanged();
    }
}
