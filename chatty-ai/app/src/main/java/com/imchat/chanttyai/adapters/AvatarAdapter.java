package com.imchat.chanttyai.adapters;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.imchat.chanttyai.base.App;
import com.imchat.chanttyai.base.BaseAdapter;
import com.imchat.chanttyai.base.Constants;
import com.imchat.chanttyai.beans.AvatarBean;
import com.imchat.chanttyai.databinding.ItemAiAvatarBinding;
import com.imchat.chanttyai.utils.ClickHelper;
import com.imchat.chanttyai.utils.DisplayUtil;

import java.util.List;

public class AvatarAdapter extends BaseAdapter<AvatarBean, ItemAiAvatarBinding> {

    private RecyclerView mRv;
    private int mType;
    private int mCheckedPos;

    public AvatarAdapter(RecyclerView rv, List<AvatarBean> list, String avatar, int type) {
        super(list);
        mRv = rv;
        mType = type;
        setChecked(list, avatar);
    }

    private void setChecked(List<AvatarBean> list, String avatar) {
        if (TextUtils.isEmpty(avatar)){
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            if (TextUtils.equals(avatar,list.get(i).getPic())){
                mCheckedPos = i;
                mRv.scrollToPosition(mCheckedPos);
                notifyDataSetChanged();
                break;
            }
        }
    }

    @Override
    protected ItemAiAvatarBinding getViewBinding(ViewGroup parent) {
        return ItemAiAvatarBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
    }

    @Override
    protected void bindViewHolder(BaseAdapter<AvatarBean, ItemAiAvatarBinding>.NormalHolder holder, AvatarBean bean, int pos) {

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                mType == Constants.AVATAR_TYPE_AI ? (int) (DisplayUtil.getWidth(App.getApplication()) / (2 * 0.5625)) : DisplayUtil.getWidth(App.getApplication()) / 2);
        holder.mBinding.fl.setLayoutParams(params);

        holder.mBinding.fl.setBackgroundResource(mType == Constants.AVATAR_TYPE_AI ? bean.getBg(): bean.getHead());

        holder.mBinding.cb.setChecked(pos == mCheckedPos);
        ClickHelper.getInstance().setOnClickListener(holder.mBinding.fl, view -> {
            mCheckedPos = pos;
            notifyDataSetChanged();
        },false);
    }

    public AvatarBean getCheckedAvatar() {
        return mList.get(mCheckedPos);
    }
}
