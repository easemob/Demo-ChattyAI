package com.imchat.chanttyai.ui.fragment.dialog;

import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hyphenate.chat.EMUserInfo;
import com.imchat.chanttyai.R;
import com.imchat.chanttyai.adapters.MentionAdapter;
import com.imchat.chanttyai.adapters.MentionTabAdapter;
import com.imchat.chanttyai.base.BaseActivity;
import com.imchat.chanttyai.beans.AIBean;
import com.imchat.chanttyai.listeners.OnItemClickListener;
import com.imchat.chanttyai.utils.ClickHelper;
import com.imchat.chanttyai.utils.SharedPreferUtil;
import com.imchat.chanttyai.views.QBottomSheetDialog;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MentionDialog extends QBottomSheetDialog {
    private final OnMentionListener mOnMentionListener;
    private MentionTabAdapter mTabAdapter;
    private MentionAdapter mMainAdapter;

    List<EMUserInfo> mList = new ArrayList<>();

    public MentionDialog(@NonNull BaseActivity context, Map<String, EMUserInfo> memberMap,OnMentionListener onMentionListener) {
        super(context);
        mOnMentionListener = onMentionListener;
        initList(memberMap);
    }

    private void initList(Map<String, EMUserInfo> memberMap) {
        if (memberMap == null || memberMap.isEmpty()){
            return;
        }
        for (Map.Entry<String, EMUserInfo> entry : memberMap.entrySet()) {
            mList.add(entry.getValue());
        }
    }

    @Override
    protected void initView() {
        super.initView();
        ClickHelper.getInstance().setOnClickListener(findViewById(R.id.tv_cancel),this::dismissDialog,false);

        initRvTab();

        initRvMain();
    }

    private void initRvTab() {
        RecyclerView rvTab = findViewById(R.id.rv_tab);

        List<String> list = new ArrayList<>();
        list.add("智能体");
        list.add("我的朋友");

        mTabAdapter = new MentionTabAdapter(list, (str, pos) -> {
            mTabAdapter.setChecked(pos);

            //请求数据填充
            filterByPos(pos);
        });
        rvTab.setAdapter(mTabAdapter);
    }

    private void filterByPos(int pos) {
        if (mList.isEmpty()){
            return;
        }
        List<EMUserInfo> userInfoList = mList.stream().filter(item -> (pos == 0) == item.getUserId().startsWith("bot")).filter(item->!TextUtils.equals(SharedPreferUtil.getInstance().getAccount(),item.getUserId())).collect(Collectors.toList());
        mMainAdapter.setData(userInfoList);
    }

    private void initRvMain() {
        SwipeRecyclerView rvMain = findViewById(R.id.rv_main);

        mMainAdapter = new MentionAdapter(new OnItemClickListener<EMUserInfo>() {
            @Override
            public void onItemClick(EMUserInfo userInfo, int pos) {
                mOnMentionListener.onMention(userInfo, SharedPreferUtil.getInstance().getAIBean(userInfo.getUserId()));
                dismiss();
            }
        });
        rvMain.setAdapter(mMainAdapter);

        filterByPos(0);
    }

    private void dismissDialog(View view) {
        dismiss();
    }

    @Override
    protected int getLayout() {
        return R.layout.dialog_mention;
    }

    public interface OnMentionListener{
        void onMention(EMUserInfo userInfo, AIBean aiBean);
    }
}
