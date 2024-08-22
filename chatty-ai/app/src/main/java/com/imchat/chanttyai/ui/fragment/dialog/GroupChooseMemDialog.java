package com.imchat.chanttyai.ui.fragment.dialog;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMUserInfo;
import com.imchat.chanttyai.R;
import com.imchat.chanttyai.adapters.GroupChooseMemAdapter;
import com.imchat.chanttyai.adapters.MemTabAdapter;
import com.imchat.chanttyai.base.App;
import com.imchat.chanttyai.base.BaseActivity;
import com.imchat.chanttyai.base.Constants;
import com.imchat.chanttyai.beans.AIBean;
import com.imchat.chanttyai.beans.MemTitleBean;
import com.imchat.chanttyai.http.QObserver;
import com.imchat.chanttyai.listeners.OnItemClickListener;
import com.imchat.chanttyai.livedatas.LiveDataBus;
import com.imchat.chanttyai.ui.fragment.manager.ChooseMemManager;
import com.imchat.chanttyai.ui.fragment.manager.EaseManager;
import com.imchat.chanttyai.utils.ClickHelper;
import com.imchat.chanttyai.utils.DisplayUtil;
import com.imchat.chanttyai.utils.SharedPreferUtil;
import com.imchat.chanttyai.utils.ToastUtils;
import com.imchat.chanttyai.views.QBottomSheetDialog;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class GroupChooseMemDialog extends QBottomSheetDialog {

    private EMGroup mGroup;
    private MemTabAdapter mTabAdapter;
    private GroupChooseMemAdapter mMainAdapter;
    private List<MemTitleBean> mList;
    private TextView mTvNext;

    public GroupChooseMemDialog(@NonNull BaseActivity context) {
        super(context);
    }

    public GroupChooseMemDialog(@NonNull BaseActivity context, EMGroup group) {
        super(context);
        mGroup = group;
    }

    @Override
    protected int getLayout() {
        return R.layout.dialog_choose_mem;
    }

    @Override
    protected void initView() {
        super.initView();

        mTvNext = findViewById(R.id.tv_next);
        if (mGroup != null) {
            mTvNext.setText("确认");
        }

        initRvTab();

        initRvMain();

        getGroupMember();
    }

    private void getGroupMember() {
        if (mGroup == null){
            return;
        }
        EaseManager.getInstance().fetchGroupMembers(mGroup.getGroupId(), EaseManager.MEMBER_TYPE.AI, new EMValueCallBack<Map<String, EMUserInfo>>() {
            @Override
            public void onSuccess(Map<String, EMUserInfo> map) {
                mMainAdapter.setAIMembers(map);
            }

            @Override
            public void onError(int i, String s) {

            }
        },true);
    }

    @Override
    protected void initListener() {
        super.initListener();
        ChooseMemManager.getInstance().setOnChangeListener(size -> {
            setNextState();
        });

        ClickHelper.getInstance().setOnClickListener(mTvNext,this::onNext,true);
    }

    private void setNextState() {
        if (mTvNext == null) {
            return;
        }
        DisplayUtil.setTextDisabled(mTvNext,getChooseSize());
    }

    private void onNext(View view) {
        if (getChooseSize() == 0) {
            return;
        }
        if (mGroup != null) {
            addGroupMembers();
            return;
        }
        dismiss();
    }

    private int getChooseSize(){
        return ChooseMemManager.getInstance().getTempList().size();
    }

    //添加群成员
    private void addGroupMembers() {
        EaseManager.getInstance().addUsers2Group(mGroup.getGroupId(), ChooseMemManager.getInstance().getTempArr(), new EMCallBack() {
            @Override
            public void onSuccess() {
                ToastUtils.toast("添加成员成功");
                LiveDataBus.get().with(Constants.KEY_REFRESH_MEMBERS, String.class).postValue("");
            }

            @Override
            public void onError(int i, String s) {
                ToastUtils.toast("添加成员失败：" + i + s);
            }
        },true);
        dismiss();
    }

    private void initRvMain() {
        SwipeRecyclerView rvMain = findViewById(R.id.rv_main);

        mMainAdapter = new GroupChooseMemAdapter();
        rvMain.setAdapter(mMainAdapter);

        fetch(0);
    }

    private void initRvTab() {
        RecyclerView rvTab = findViewById(R.id.rv_tab);

        mList = new ArrayList<>();
        mList.add(new MemTitleBean(Constants.TYPE_PUBLIC, "公开智能体"));
        mList.add(new MemTitleBean(Constants.TYPE_ALL, "我聊过的"));
        mList.add(new MemTitleBean(Constants.TYPE_MINE, "我创建的"));

        mTabAdapter = new MemTabAdapter(mList, new OnItemClickListener<MemTitleBean>() {
            @Override
            public void onItemClick(MemTitleBean bean, int pos) {
                mTabAdapter.setChecked(pos);

                //请求数据填充
                fetch(pos);
            }

        });
        rvTab.setAdapter(mTabAdapter);
    }

    private void fetch(int pos) {
        MemTitleBean bean = mList.get(pos);
        //获取智能体
        Map<String, String> map = new HashMap<>();
        map.put("type", bean.getType());
        if (!TextUtils.equals(Constants.TYPE_PUBLIC, bean.getType())) {
            map.put("phone", SharedPreferUtil.getInstance().getAccount());
        }
        App.getApi().getBot(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new QObserver<List<AIBean>>(mContext) {
                    @Override
                    public void next(List<AIBean> aiBeans) {
                        if (pos == 1) {
                            //我聊过的 需要手动处理
                            Map<String, EMConversation> allConversations = EMClient.getInstance().chatManager().getAllConversations();
                            aiBeans = aiBeans.stream().filter(aiBean -> allConversations.containsKey(aiBean.getEaAccount())).collect(Collectors.toList());
                        }
                        mMainAdapter.setData(aiBeans);
                    }
                });
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        ChooseMemManager.getInstance().setDefault();
        mMainAdapter.notifyDataSetChanged();
        setNextState();
    }


    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        //dismiss的时候 设置目标值 发通知
        if (mGroup == null) {
            ChooseMemManager.getInstance().setResult();
            LiveDataBus.get().with(Constants.KEY_SHOW_GROUP_CREATE, String.class).postValue("");
        }
        //清除
        ChooseMemManager.getInstance().clearTemp();
        mMainAdapter.notifyDataSetChanged();
        setNextState();
    }
}
