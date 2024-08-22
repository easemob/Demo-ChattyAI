package com.imchat.chanttyai.ui.fragment;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.imchat.chanttyai.R;
import com.imchat.chanttyai.adapters.ConversationAdapter;
import com.imchat.chanttyai.base.BaseFragment;
import com.imchat.chanttyai.base.Constants;
import com.imchat.chanttyai.databinding.FragmentConversationBinding;
import com.imchat.chanttyai.listeners.OnItemClickListener;
import com.imchat.chanttyai.livedatas.LiveDataBus;
import com.imchat.chanttyai.ui.activity.ChatActivity;
import com.imchat.chanttyai.ui.fragment.dialog.GroupAddDialog;
import com.imchat.chanttyai.ui.fragment.dialog.GroupChooseMemDialog;
import com.imchat.chanttyai.ui.fragment.manager.ChooseMemManager;
import com.imchat.chanttyai.ui.fragment.manager.EaseManager;
import com.imchat.chanttyai.utils.ClickHelper;
import com.imchat.chanttyai.utils.DisplayUtil;
import com.imchat.chanttyai.utils.ToastUtils;
import com.imchat.chanttyai.views.dialog.BaseNiceDialog;
import com.imchat.chanttyai.views.dialog.NiceDialog;
import com.imchat.chanttyai.views.dialog.ViewConvertListener;
import com.imchat.chanttyai.views.dialog.ViewHolder;
import com.yanzhenjie.recyclerview.OnItemMenuClickListener;
import com.yanzhenjie.recyclerview.SwipeMenu;
import com.yanzhenjie.recyclerview.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.SwipeMenuItem;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ConversationFragment extends BaseFragment<FragmentConversationBinding> {
    private boolean mIsGroup;

    private GroupAddDialog mGroupAddDialog;
    private GroupChooseMemDialog mChooseMemDialog;

    public ConversationFragment(){

    }
    public ConversationFragment(boolean isGroup) {
        mIsGroup = isGroup;
    }

    private ConversationAdapter mAdapter;

    @Override
    protected FragmentConversationBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentConversationBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initView() {
        mBinding.ivTitle.setImageResource(mIsGroup ? R.drawable.group_title : R.drawable.conversation_title);
        mBinding.ivAdd.setVisibility(mIsGroup ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void initData() {
        initRv();
        loadConversations("");
    }

    private void fetchConversation() {
        EaseManager.getInstance().getConversationsFromServer(new EMValueCallBack<Map<String, EMConversation>>() {
            @Override
            public void onSuccess(Map<String, EMConversation> allConversations) {
                handleConversations(allConversations);
            }

            @Override
            public void onError(int i, String s) {

            }
        },false);
    }

    private void initRv() {
        mAdapter = new ConversationAdapter(new OnItemClickListener<Map.Entry<String, EMConversation>>() {
            @Override
            public void onItemClick(Map.Entry<String, EMConversation> map, int pos) {
                ChatActivity.start(mContext, map.getKey(),mIsGroup);
            }
        }, mContext);

        mBinding.rv.setSwipeMenuCreator(new SwipeMenuCreator() {
            @Override
            public void onCreateMenu(SwipeMenu leftMenu, SwipeMenu rightMenu, int position) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(mContext);
                deleteItem.setText("删除")
                        .setWidth(DisplayUtil.dp2px(mContext, 100))
                        .setHeight(-1)
                        .setTextColor(Color.parseColor("#F9FAFA"))
                        .setBackgroundColor(Color.parseColor("#FF3355"));
                // 各种文字和图标属性设置。
                rightMenu.addMenuItem(deleteItem); // 在Item左侧添加一个菜单。
            }
        });


        mBinding.rv.setOnItemMenuClickListener(new OnItemMenuClickListener() {
            @Override
            public void onItemClick(SwipeMenuBridge menuBridge, int position) {
                // 任何操作必须先关闭菜单，否则可能出现Item菜单打开状态错乱。
                menuBridge.closeMenu();

                // 左侧还是右侧菜单：
                int direction = menuBridge.getDirection();
                // 菜单在Item中的Position：
                int menuPosition = menuBridge.getPosition();

                if (direction == SwipeRecyclerView.RIGHT_DIRECTION && menuPosition == 0) {
                    delete(position);
                }
            }
        });

        mBinding.rv.setAdapter(mAdapter);
    }

    private void delete(int position) {
        NiceDialog.init().setLayoutId(R.layout.dialog_layout)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    protected void convertView(ViewHolder holder, BaseNiceDialog dialog) {

                        ClickHelper.getInstance().setOnClickListener(holder.getView(R.id.tv_confirm), view -> {
                            Map.Entry<String, EMConversation> map = mAdapter.getData(position);
                            EMClient.getInstance().chatManager().deleteConversation(map.getKey(), true);
                            loadConversations("");
                            ToastUtils.toast("已删除");
                            LiveDataBus.get().with(Constants.KEY_UPDATE_UNREAD,String.class).postValue("");
                            dialog.dismissAllowingStateLoss();
                        },false);
                        ClickHelper.getInstance().setOnClickListener(holder.getView(R.id.tv_cancel),view -> dialog.dismissAllowingStateLoss(),false);
                    }
                })
                .setMargin(DisplayUtil.dp2px(mContext, 10))
                .setOutCancel(false)
                .show(mFragmentManager);
    }


    private void loadConversations(String s) {
        Map<String, EMConversation> allConversations = EMClient.getInstance().chatManager().getAllConversations();
        if (noneData(allConversations.isEmpty())){
            fetchConversation();
            return;
        }
        handleConversations(allConversations);
    }

    private void handleConversations(Map<String, EMConversation> allConversations){
        boolean none = allConversations.isEmpty();
        if (noneData(none)) {
            return;
        }
        List<Map.Entry<String, EMConversation>> list = new ArrayList<>(allConversations.entrySet());
        list = filter(list);
        sort(list);

        mAdapter.setData(list);
    }

    private List<Map.Entry<String, EMConversation>> filter(List<Map.Entry<String, EMConversation>> list) {
        return list.stream().filter(map -> map.getValue().isGroup() == mIsGroup).collect(Collectors.toList());
    }

    private void sort(List<Map.Entry<String, EMConversation>> list) {
        Collections.sort(list, (m1, m2) -> (int) (m2.getValue().getLastMessage().getMsgTime() - m1.getValue().getLastMessage().getMsgTime()));
    }

    private boolean noneData(boolean none) {
        mBinding.rv.setVisibility(none ? View.GONE : View.VISIBLE);
        mBinding.ivNone.setVisibility(none ? View.VISIBLE : View.GONE);
        return none;
    }

    @Override
    protected void initListener() {
        LiveDataBus.get().with(Constants.MSG_RECEIVED, String.class).observe(this, this::loadConversations);
        LiveDataBus.get().with(Constants.MSG_READ,String.class).observe(this, this::loadConversations);
        LiveDataBus.get().with(Constants.KEY_REFRESH_AI_LIST,String.class).observe(this, this::loadConversations);

        ClickHelper.getInstance().setOnClickListener(mBinding.ivAdd,this::onAddGroup,true);

        if (mIsGroup) {
            LiveDataBus.get().with(Constants.KEY_SHOW_GROUP_CREATE, String.class).observe(this, this::onShowGroupAdd);
            LiveDataBus.get().with(Constants.KEY_SHOW_GROUP_ADD_MEM, String.class).observe(this, this::onShowChooseMember);
            LiveDataBus.get().with(Constants.KEY_REFRESH_GROUP_NAME,String.class).observe(this,this::onRefreshConversation);
            LiveDataBus.get().with(Constants.KEY_DELETE_GROUP,String.class).observe(this,this::onRefreshConversation);
        }
        LiveDataBus.get().with(Constants.KEY_REFRESH_CONVERSATION, String.class).observe(this, this::onRefreshConversation);
    }

    private void onRefreshConversation(String s) {
        loadConversations("");
        ChooseMemManager.getInstance().clearAll();
    }

    private void onShowGroupAdd(String s) {
//        hideGroupChooseMemDialog();
        showGroupAddDialog();
    }

    private void onShowChooseMember(String s) {
//        hideGroupAddDialog();
        showGroupChooseMemDialog();
    }

    private void showGroupChooseMemDialog() {
        if (mChooseMemDialog != null && mChooseMemDialog.isShowing()) {
            return;
        }
        mChooseMemDialog = new GroupChooseMemDialog(mContext);
        mChooseMemDialog.show();
    }

    private void hideGroupChooseMemDialog() {
        if (mChooseMemDialog != null) {
            mChooseMemDialog.dismiss();
        }
    }

    private void onAddGroup(View view) {
        showGroupAddDialog();
    }

    private void showGroupAddDialog() {
        if (mGroupAddDialog != null && mGroupAddDialog.isShowing()) {
            return;
        }
        mGroupAddDialog = new GroupAddDialog(mContext);
        mGroupAddDialog.show();
    }

    private void hideGroupAddDialog() {
        if (mGroupAddDialog != null) {
            mGroupAddDialog.dismiss();
        }
    }
}
