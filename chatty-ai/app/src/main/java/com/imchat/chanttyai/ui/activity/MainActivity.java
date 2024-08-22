package com.imchat.chanttyai.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.TextView;

import com.hyphenate.EMCallBack;
import com.imchat.chanttyai.R;
import com.imchat.chanttyai.adapters.FragmentAdapter;
import com.imchat.chanttyai.adapters.NavAdapter;
import com.imchat.chanttyai.base.BaseActivity;
import com.imchat.chanttyai.base.BaseFragment;
import com.imchat.chanttyai.base.Constants;
import com.imchat.chanttyai.beans.GroupBean;
import com.imchat.chanttyai.beans.NavBean;
import com.imchat.chanttyai.databinding.ActivityMainBinding;
import com.imchat.chanttyai.livedatas.LiveDataBus;
import com.imchat.chanttyai.ui.fragment.ConversationFragment;
import com.imchat.chanttyai.ui.fragment.HomeFragment;
import com.imchat.chanttyai.ui.fragment.MineFragment;
import com.imchat.chanttyai.ui.fragment.manager.EaseManager;
import com.imchat.chanttyai.utils.ClickHelper;
import com.imchat.chanttyai.utils.CommonUtils;
import com.imchat.chanttyai.utils.DisplayUtil;
import com.imchat.chanttyai.utils.GsonUtils;
import com.imchat.chanttyai.utils.SharedPreferUtil;
import com.imchat.chanttyai.utils.ToastUtils;
import com.imchat.chanttyai.views.dialog.BaseNiceDialog;
import com.imchat.chanttyai.views.dialog.NiceDialog;
import com.imchat.chanttyai.views.dialog.ViewConvertListener;
import com.imchat.chanttyai.views.dialog.ViewHolder;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity<ActivityMainBinding> {

    private static final int REQUEST_CLIP_BOARD = 1001;
    private NavAdapter mNavAdapter;

    public static void start(Context context) {
        context.startActivity(new Intent(context, MainActivity.class));
    }

    @Override
    protected ActivityMainBinding getViewBinding() {
        return ActivityMainBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initView() {

    }


    @Override
    protected void initData() {
        initNav();

        onUpdateUnread("");
    }

    private void initNav() {

        List<NavBean> list = new ArrayList<>();
        list.add(new NavBean(R.drawable.aobot, R.drawable.aobot_focus, "智能体"));
        list.add(new NavBean(R.drawable.group, R.drawable.group_focus, "群聊"));
        list.add(new NavBean(R.drawable.bubble_fill, R.drawable.bubble_fill_focus, "会话"));
        list.add(new NavBean(R.drawable.mine, R.drawable.mine_focus, "个人"));

        mNavAdapter = new NavAdapter(list, (navBean, pos) ->
                mBinding.vp.setCurrentItem(pos));
        mBinding.rvNav.setAdapter(mNavAdapter);

        List<BaseFragment> fragmentList = new ArrayList<>();
        fragmentList.add(new HomeFragment());
        fragmentList.add(new ConversationFragment(true));
        fragmentList.add(new ConversationFragment(false));
        fragmentList.add(new MineFragment());

        // FragmentPagerAdapter 来处理多个 Fragment 页面
        FragmentAdapter fragmentPagerAdapter = new FragmentAdapter(mFragmentManager, fragmentList);
        // viewPager 设置 adapter
        mBinding.vp.setAdapter(fragmentPagerAdapter);

        mBinding.vp.setOffscreenPageLimit(fragmentList.size());
    }

    @Override
    protected void initListener() {
        LiveDataBus.get().with(Constants.KEY_UPDATE_UNREAD, String.class).observe(this, this::onUpdateUnread);
        LiveDataBus.get().with(Constants.MSG_READ, String.class).observe(this, this::onUpdateUnread);
        LiveDataBus.get().with(Constants.MSG_RECEIVED, String.class).observe(this, this::onUpdateUnread);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus){
            listen2Clipboard();
        }
    }

    private void listen2Clipboard() {
        String clipboardStr = CommonUtils.getClipboardStr(mContext);
        if (!TextUtils.isEmpty(clipboardStr) && clipboardStr.contains(Constants.TEXT_SHARE)) {

            GroupBean bean = GsonUtils.toBean(clipboardStr.replace(Constants.TEXT_SHARE, ""), GroupBean.class);

            if (TextUtils.equals(bean.getInvitor(), SharedPreferUtil.getInstance().getAccount())){
                return;
            }
            NiceDialog.init().setLayoutId(R.layout.dialog_layout)
                    .setConvertListener(new ViewConvertListener() {
                        @Override
                        protected void convertView(ViewHolder holder, BaseNiceDialog dialog) {

                            ((TextView) holder.getView(R.id.tv_title)).setText("加入群聊");
                            ((TextView) holder.getView(R.id.tv_content)).setText("是否加入群聊：" + bean.getName() + "？");

                            ClickHelper.getInstance().setOnClickListener(holder.getView(R.id.tv_confirm), view -> {
                                CommonUtils.clearClipboard(mContext);
                                dialog.dismissAllowingStateLoss();
                                joinGroup(bean);
                            },false);
                            ClickHelper.getInstance().setOnClickListener(holder.getView(R.id.tv_cancel),view -> {
                                CommonUtils.clearClipboard(mContext);
                                dialog.dismissAllowingStateLoss();
                            },false);
                        }
                    })
                    .setMargin(DisplayUtil.dp2px(mContext, 10))
                    .setOutCancel(false)
                    .show(mFragmentManager);
        }
    }

    private void joinGroup(GroupBean bean) {
        EaseManager.getInstance().join2Group(bean.getId(), new EMCallBack() {
            @Override
            public void onSuccess() {
                ChatActivity.start(mContext, bean.getId(), true);
            }

            @Override
            public void onError(int i, String s) {
                if (i == 601){
                    ToastUtils.toast("您已在当前群");
                    ChatActivity.start(mContext, bean.getId(), true);
                    return;
                }
                ToastUtils.toast("加入群聊失败：" + i + s);
            }
        },true);
    }

    private void onUpdateUnread(String s) {
        mNavAdapter.setBadge();
    }
}
