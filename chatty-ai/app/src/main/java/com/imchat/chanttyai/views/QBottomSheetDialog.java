package com.imchat.chanttyai.views;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.imchat.chanttyai.R;
import com.imchat.chanttyai.base.BaseActivity;
import com.imchat.chanttyai.utils.ClickHelper;

public abstract class QBottomSheetDialog extends BottomSheetDialog {
    @NonNull
    protected BaseActivity mContext;

    public QBottomSheetDialog(@NonNull BaseActivity context) {
        super(context);
        mContext = context;
        setContentView(getLayout());
    }

    protected abstract int getLayout();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initListener();
    }

    protected void initListener() {
        ImageView ivClose = findViewById(R.id.iv_close);
        if (ivClose != null) {
            ClickHelper.getInstance().setOnClickListener(ivClose,view -> {
                if (isShowing()) dismiss();
            },false);
        }

        TextView tvCancel = findViewById(R.id.tv_cancel);
        if (tvCancel != null){
            ClickHelper.getInstance().setOnClickListener(tvCancel, view -> {
                if (isShowing()) dismiss();
            },false);
        }
    }

    protected void initView() {

    }

    @Override
    protected void onStart() {
        super.onStart();

        View view = findViewById(R.id.design_bottom_sheet);
        view.setBackgroundColor(Color.TRANSPARENT);

        getBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);
    }
}
