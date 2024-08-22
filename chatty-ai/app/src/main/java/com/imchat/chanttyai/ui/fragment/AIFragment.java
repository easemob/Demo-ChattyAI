package com.imchat.chanttyai.ui.fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.hyphenate.chat.EMClient;
import com.imchat.chanttyai.R;
import com.imchat.chanttyai.adapters.AIAdapter;
import com.imchat.chanttyai.base.App;
import com.imchat.chanttyai.base.BaseFragment;
import com.imchat.chanttyai.base.Constants;
import com.imchat.chanttyai.beans.AIBean;
import com.imchat.chanttyai.beans.Empty;
import com.imchat.chanttyai.databinding.FragmentAiBinding;
import com.imchat.chanttyai.http.QObserver;
import com.imchat.chanttyai.listeners.OnItemClickListener;
import com.imchat.chanttyai.livedatas.LiveDataBus;
import com.imchat.chanttyai.ui.activity.ChatActivity;
import com.imchat.chanttyai.ui.activity.CreateAIActivity;
import com.imchat.chanttyai.utils.ClickHelper;
import com.imchat.chanttyai.utils.DisplayUtil;
import com.imchat.chanttyai.utils.SharedPreferUtil;
import com.imchat.chanttyai.utils.ToastUtils;
import com.imchat.chanttyai.views.dialog.BaseNiceDialog;
import com.imchat.chanttyai.views.dialog.NiceDialog;
import com.imchat.chanttyai.views.dialog.ViewConvertListener;
import com.imchat.chanttyai.views.dialog.ViewHolder;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.yanzhenjie.recyclerview.OnItemMenuClickListener;
import com.yanzhenjie.recyclerview.SwipeMenu;
import com.yanzhenjie.recyclerview.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.SwipeMenuItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AIFragment extends BaseFragment<FragmentAiBinding> {

    private final String mType;
    private AIAdapter mAdapter;

    public AIFragment(String type) {
        mType = type;
    }

    @Override
    protected FragmentAiBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentAiBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initView() {
        initRV();
    }

    private void initRV() {
        mBinding.rv.setSwipeMenuCreator(new SwipeMenuCreator() {
            @Override
            public void onCreateMenu(SwipeMenu leftMenu, SwipeMenu rightMenu, int position) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(mContext)
                        .setWidth(DisplayUtil.dp2px(mContext, 80))
                        .setImage(R.drawable.delete);
                rightMenu.addMenuItem(deleteItem);
            }
        });

        // 菜单点击监听。
        mBinding.rv.setOnItemMenuClickListener(new OnItemMenuClickListener() {
            @Override
            public void onItemClick(SwipeMenuBridge menuBridge, int position) {
                menuBridge.closeMenu();

                if (!TextUtils.equals(mAdapter.getData(position).getCreateAccount(), SharedPreferUtil.getInstance().getAccount())) {
                    ToastUtils.toast("只能删除自己创建的");
                    return;
                }
                NiceDialog.init().setLayoutId(R.layout.dialog_layout)
                        .setConvertListener(new ViewConvertListener() {
                            @Override
                            protected void convertView(ViewHolder holder, BaseNiceDialog dialog) {

                                ((TextView) holder.getView(R.id.tv_title)).setText("确认删除智能体？");
                                ((TextView) holder.getView(R.id.tv_content)).setText("删除后的智能体不可恢复");
                                ClickHelper.getInstance().setOnClickListener(holder.getView(R.id.tv_confirm), view -> {
                                    dialog.dismissAllowingStateLoss();
                                    doDelete(position);
                                },false);
                                ClickHelper.getInstance().setOnClickListener(holder.getView(R.id.tv_cancel),view -> dialog.dismissAllowingStateLoss(),false);
                            }
                        })
                        .setMargin(DisplayUtil.dp2px(mContext, 10))
                        .setOutCancel(false)
                        .show(mFragmentManager);
            }
        });

        mAdapter = new AIAdapter(new OnItemClickListener<AIBean>() {
            @Override
            public void onItemClick(AIBean aiBean, int pos) {
                ChatActivity.start(mContext, aiBean.getEaAccount(), false);
            }
        });
        mBinding.rv.setAdapter(mAdapter);
    }

    private void doDelete(int position) {
        AIBean aiBean = mAdapter.getData(position);
        Map<String, Object> map = new HashMap<>();
        map.put("id", aiBean.getId());
        map.put("createAccount", aiBean.getCreateAccount());
        App.getApi().removeMyBot(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new QObserver<Empty>(mContext) {
                    @Override
                    public void next(Empty empty) {
                        ToastUtils.toast("删除成功");
                        //同时删除会话
                        EMClient.getInstance().chatManager().deleteConversation(aiBean.getEaAccount(), true);
                        LiveDataBus.get().with(Constants.KEY_REFRESH_AI_LIST, String.class).postValue("");
                        LiveDataBus.get().with(Constants.KEY_UPDATE_UNREAD,String.class).postValue("");
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchData();
    }

    @Override
    protected void initData() {

    }

    private void fetchData() {
        //获取智能体
        Map<String, String> map = new HashMap<>();
        map.put("type", mType);
        if (TextUtils.equals(Constants.TYPE_MINE, mType)) {
            map.put("phone", SharedPreferUtil.getInstance().getAccount());
        }
        App.getApi().getBot(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new QObserver<List<AIBean>>(mContext, mBinding.srl) {
                    @Override
                    public void next(List<AIBean> aiBeans) {
                        mAdapter.setData(aiBeans);
                    }
                });

    }

    @Override
    protected void initListener() {
        ClickHelper.getInstance().setOnClickListener(mBinding.llCreateAi,this::onCreateAI,false);
        mBinding.srl.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                fetchData();
            }
        });
        LiveDataBus.get().with(Constants.KEY_REFRESH_AI_LIST, String.class).observe(this, this::onRefreshAIList);
    }

    private void onRefreshAIList(String s) {
        fetchData();
    }

    public void onCreateAI(View view) {

        //获取智能体
        Map<String, String> map = new HashMap<>();
        map.put("type", Constants.TYPE_MINE);
        map.put("phone", SharedPreferUtil.getInstance().getAccount());
        App.getApi().getBot(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new QObserver<List<AIBean>>(mContext) {
                    @Override
                    public void next(List<AIBean> aiBeans) {
                        if (aiBeans.size() >= 3){
                            ToastUtils.toast("您创建的智能体已经达到上限");
                            return;
                        }
                        CreateAIActivity.start(mContext);
                    }
                });


    }
}
