package com.imchat.chanttyai.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.imchat.chanttyai.base.BaseAdapter;
import com.imchat.chanttyai.beans.NavBean;
import com.imchat.chanttyai.databinding.ItemNavBinding;
import com.imchat.chanttyai.ui.fragment.manager.EaseManager;
import com.imchat.chanttyai.utils.ClickHelper;
import com.imchat.chanttyai.utils.SharedPreferUtil;

import java.util.List;

public class NavAdapter extends BaseAdapter<NavBean, ItemNavBinding> {

    private OnContentClickListener mOnContentClickListener;
    private int mCheckedPos;

    public NavAdapter(List<NavBean> list, OnContentClickListener onContentClickListener) {
        super(list);
        mOnContentClickListener = onContentClickListener;
    }

    @Override
    protected ItemNavBinding getViewBinding(ViewGroup parent) {
        return ItemNavBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
    }

    @Override
    protected void bindViewHolder(BaseAdapter<NavBean, ItemNavBinding>.NormalHolder holder, NavBean bean, int pos) {
        holder.mBinding.ivIcon.setImageResource(pos == mCheckedPos ? bean.getIconChecked() : bean.getIconUnchecked());
        holder.mBinding.tvTitle.setText(bean.getTitle());
        holder.mBinding.tvTitle.setTextColor(Color.parseColor(pos == mCheckedPos ? "#009EFF" : "#919BA1"));
        if (bean.getUnRead() == 0) {
            holder.mBinding.tvUnread.setVisibility(View.GONE);
        } else {
            holder.mBinding.tvUnread.setVisibility(View.VISIBLE);
            holder.mBinding.tvUnread.setText(SharedPreferUtil.getInstance().getUnReadCount(bean.getUnRead()));
        }

        ClickHelper.getInstance().setOnClickListener(holder.mBinding.llContent, view -> {
            mCheckedPos = pos;
            if (mOnContentClickListener != null){
                mOnContentClickListener.onClick(bean,pos);
            }
            notifyDataSetChanged();

        },false);
    }

    public void setBadge() {
        mList.get(1).setUnRead(EaseManager.getInstance().getUnreadCount(true));
        mList.get(2).setUnRead(EaseManager.getInstance().getUnreadCount(false));
        notifyDataSetChanged();
    }

    public interface OnContentClickListener{
        void onClick(NavBean bean,int position);
    }
}
