package com.imchat.chanttyai.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.imchat.chanttyai.R;
import com.imchat.chanttyai.base.BaseAdapter;
import com.imchat.chanttyai.beans.AIBean;
import com.imchat.chanttyai.beans.AvatarBean;
import com.imchat.chanttyai.databinding.ItemAiBinding;
import com.imchat.chanttyai.listeners.OnItemClickListener;
import com.imchat.chanttyai.ui.fragment.manager.AvatarManager;
import com.imchat.chanttyai.utils.ClickHelper;
import com.imchat.chanttyai.utils.DisplayUtil;

public class AIAdapter extends BaseAdapter<AIBean,ItemAiBinding> {
    private final OnItemClickListener<AIBean> mOnItemClickListener;

    public AIAdapter(OnItemClickListener<AIBean> onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    protected ItemAiBinding getViewBinding(ViewGroup parent) {
        return ItemAiBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
    }

    @Override
    protected void bindViewHolder(BaseAdapter<AIBean, ItemAiBinding>.NormalHolder holder, AIBean bean, int pos) {

        int head = R.drawable.onlight;
        AvatarBean avatarBean = AvatarManager.getInstance().getAIAvatarBean(bean.getPic());
        if (avatarBean != null){
            head = avatarBean.getHead();
        }

        holder.mBinding.ivAvatar.setImageResource(head);
        holder.mBinding.tvName.setText(bean.getBotName());
        holder.mBinding.tvDesc.setText(bean.getDescribe());

        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) holder.itemView.getLayoutParams();
        if (mList != null && !mList.isEmpty() && pos ==  mList.size()-1){
            layoutParams.bottomMargin = DisplayUtil.dp2px(holder.itemView.getContext(),70);
        }else {
            layoutParams.bottomMargin = 0;
        }
        holder.itemView.setLayoutParams(layoutParams);


        ClickHelper.getInstance().setOnClickListener(holder.mBinding.tvChat, view -> {
            if (mOnItemClickListener != null){
                mOnItemClickListener.onItemClick(bean,pos);
            }
        },true);
    }
}
