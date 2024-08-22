package com.imchat.chanttyai.base;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.imchat.chanttyai.R;
import com.imchat.chanttyai.listeners.OnCustomItemClickListener;
import com.imchat.chanttyai.listeners.OnItemClickListener;
import com.imchat.chanttyai.utils.ClickHelper;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseAdapter<T, VB extends ViewBinding> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected static final int TYPE_NORMAL = 0;
    protected static final int TYPE_NO_MORE = 1;
    protected static final int TYPE_CUSTOM = 2;
    protected static final int TYPE_NONE = 3;
    private List<SHOW_MODE> mShowModeList = new ArrayList<>();

    protected List<T> mList;
    private OnItemClickListener<T> mOnItemClickListener;

    private OnCustomItemClickListener mOnCustomItemClickListener;

    public BaseAdapter(List<SHOW_MODE> list, boolean b) {
        mShowModeList = list;
    }

    public enum SHOW_MODE{
       DEFAULT,SHOW_NONE,SHOW_CUSTOM,SHOW_NO_MORE
   }

    public void setData(List<T> list) {
        mList = list;
        notifyDataSetChanged();
    }

    public T getData(int position){
        return mList.get(position);
    }

    public void showNoMore() {
        if (mShowModeList == null){
            mShowModeList = new ArrayList<>();
            mShowModeList.add(SHOW_MODE.SHOW_NO_MORE);
        }
        notifyDataSetChanged();
    }

    public void hideNoMore() {
       if (mShowModeList != null && mShowModeList.contains(SHOW_MODE.SHOW_NO_MORE)){
           mShowModeList.remove(SHOW_MODE.SHOW_NO_MORE);
       }
        notifyDataSetChanged();
    }

    public BaseAdapter() {
       initShowMode();
    }

    private void initShowMode(){
       mShowModeList.add(SHOW_MODE.SHOW_NONE);
    }

    public BaseAdapter(List<T> list,List<SHOW_MODE> showModeList){
        mList = list;
        mShowModeList = showModeList;
    }

    public BaseAdapter(List<T> list) {
        initShowMode();
        mList = list;
    }

    public BaseAdapter(List<T> list, OnItemClickListener<T> onItemClickListener) {
        initShowMode();
        mList = list;
        mOnItemClickListener = onItemClickListener;
    }

    public BaseAdapter(OnItemClickListener<T> onItemClickListener) {
        initShowMode();
        mOnItemClickListener = onItemClickListener;
    }

    public void setOnCustomItemClickListener(OnCustomItemClickListener onCustomItemClickListener) {
        mOnCustomItemClickListener = onCustomItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       //展示自定义item
       if (mShowModeList.contains(SHOW_MODE.SHOW_CUSTOM) && getCustomLayout() != 0 && viewType == TYPE_CUSTOM){
           return new CustomHolder(LayoutInflater.from(parent.getContext()).inflate(getCustomLayout(),parent,false));
       }
       //展示空item
        if (mShowModeList.contains(SHOW_MODE.SHOW_NONE) && viewType == TYPE_NONE){
            return new NoneHolder(LayoutInflater.from(parent.getContext()).inflate(getNoneLayout(),parent,false));
        }
        //展示no more的item
        if (viewType == TYPE_NO_MORE) {
            return new NoMoreHolder(LayoutInflater.from(parent.getContext()).inflate(getNoMoreLayout(), parent, false));
        }
        //展示普通item
        return new NormalHolder(getViewBinding(parent));
    }

    protected int getNoneLayout() {
        return R.layout.item_none;
    }

    protected abstract VB getViewBinding(ViewGroup parent);


    protected int getNoMoreLayout() {
        return R.layout.item_no_more;
    }

    protected int getCustomLayout(){
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (mShowModeList.contains(SHOW_MODE.SHOW_NONE) && (mList == null || mList.size() == 0)){
            return TYPE_NONE;
        }
        if (mList == null || position == mList.size()) {
            if (mShowModeList.contains(SHOW_MODE.SHOW_NO_MORE)){
                return TYPE_NO_MORE;
            }
            if (mShowModeList.contains(SHOW_MODE.SHOW_CUSTOM)){
                return TYPE_CUSTOM;
            }
        }
        return TYPE_NORMAL;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if (viewType == TYPE_NO_MORE || viewType == TYPE_CUSTOM || viewType == TYPE_NONE) {
            return;
        }
        T t = mList.get(position);
        ClickHelper.getInstance().setOnClickListener(holder.itemView, view -> {
            if (mOnItemClickListener != null)
                mOnItemClickListener.onItemClick(t, holder.getAdapterPosition());
        },true);
        bindViewHolder((NormalHolder) holder, t, position);
    }

    protected abstract void bindViewHolder(NormalHolder holder, T bean, int pos);

    @Override
    public int getItemCount() {
        if (mList == null){
            if (mShowModeList.contains(SHOW_MODE.SHOW_NO_MORE) || mShowModeList.contains(SHOW_MODE.SHOW_CUSTOM) || mShowModeList.contains(SHOW_MODE.SHOW_NONE)){
                return 1;
            }else {
                return 0;
            }
        }else {
            if (mShowModeList.contains(SHOW_MODE.SHOW_NO_MORE)|| mShowModeList.contains(SHOW_MODE.SHOW_CUSTOM)){
                return mList.size()+1;
            }else {
                return mList.size();
            }
        }
    }

    public void deleteItem(int position) {
        mList.remove(position);
        notifyDataSetChanged();
    }


    protected class NormalHolder extends RecyclerView.ViewHolder {
        public VB mBinding;

        public NormalHolder(@NonNull VB itemView) {
            super(itemView.getRoot());
            mBinding = itemView;
        }
    }

    protected class NoMoreHolder extends RecyclerView.ViewHolder {

        public NoMoreHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    protected class NoneHolder extends RecyclerView.ViewHolder{
        public NoneHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    protected class CustomHolder extends RecyclerView.ViewHolder{
        public CustomHolder(@NonNull View itemView) {
            super(itemView);

            ClickHelper.getInstance().setOnClickListener(itemView,view -> {
                if (mOnCustomItemClickListener != null){
                    mOnCustomItemClickListener.onItemClick();
                }
            },false);
        }
    }

    public void clear(){
        if (mList != null){
            mList.clear();
            notifyDataSetChanged();
        }
    }
}
