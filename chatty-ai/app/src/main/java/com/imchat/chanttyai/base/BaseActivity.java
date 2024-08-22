package com.imchat.chanttyai.base;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewbinding.ViewBinding;

import com.imchat.chanttyai.R;
import com.imchat.chanttyai.ui.fragment.manager.EaseManager;
import com.imchat.chanttyai.utils.ClickHelper;
import com.imchat.chanttyai.utils.CommonUtils;
import com.imchat.chanttyai.utils.StatusBarUtil;

import pub.devrel.easypermissions.EasyPermissions;

public abstract class BaseActivity<VB extends ViewBinding> extends AppCompatActivity {

    protected BaseActivity<VB> mContext;
    protected VB mBinding;
    protected FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setTranslucentFullScreen(this, null);

        mBinding = getViewBinding();

        View root = mBinding.getRoot();

        setContentView(root);

        root.setPadding(0, root.getPaddingTop() + CommonUtils.getStatusBarHeight(), 0, 0);

        mContext = this;
        mFragmentManager = getSupportFragmentManager();

        initView();
        initData();
        initListener();

        AppManager.getInstance().addActivity(this);

        ImageView ivBack = findViewById(R.id.iv_back);
        if (ivBack != null) {
            ClickHelper.getInstance().setOnClickListener(ivBack,this::finishAc,false);
        }

        EaseManager.getInstance().init(mContext, mFragmentManager);
    }

    private void finishAc(View view) {
        finishThis();
    }

    protected void setKeyboardListener(OnKeyBoardListener listener) {
        // 获取根布局的View实例
        View rootView = findViewById(android.R.id.content);

        // 获取根布局的ViewTreeObserver
        ViewTreeObserver viewTreeObserver = rootView.getViewTreeObserver();

        // 添加布局变化监听器
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // 获取根布局可见区域的矩形
                Rect r = new Rect();
                rootView.getWindowVisibleDisplayFrame(r);

                // 计算屏幕高度和可见区域的差值
                int screenHeight = rootView.getRootView().getHeight();
                int heightDiff = screenHeight - (r.bottom - r.top);

                // 判断键盘是否可见
                boolean isKeyboardVisible = heightDiff > screenHeight * 0.15; // 可根据实际情况调整阈值

                // 处理键盘的弹出和隐藏事件
                if (isKeyboardVisible) {
                    // 键盘弹出
                    // 在这里添加您的处理逻辑，例如滚动RecyclerView到最后一项
                    listener.onChange();
                }
            }
        });
    }

    protected interface OnKeyBoardListener {
        void onChange();
    }

    public BaseActivity getActivity() {
        return mContext;
    }

    protected abstract VB getViewBinding();

    protected abstract void initView();

    protected abstract void initData();

    protected abstract void initListener();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finishThis();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // 将结果转发给 EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    protected void finishThis() {
        AppManager.getInstance().finishActivity(this);
    }

    protected void toggleKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
    }
}
