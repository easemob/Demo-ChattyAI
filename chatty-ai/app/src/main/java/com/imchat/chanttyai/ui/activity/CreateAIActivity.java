package com.imchat.chanttyai.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import com.imchat.chanttyai.base.App;
import com.imchat.chanttyai.base.AppManager;
import com.imchat.chanttyai.base.BaseActivity;
import com.imchat.chanttyai.base.Constants;
import com.imchat.chanttyai.beans.AIBean;
import com.imchat.chanttyai.beans.Empty;
import com.imchat.chanttyai.databinding.ActivityCreateAiBinding;
import com.imchat.chanttyai.http.QObserver;
import com.imchat.chanttyai.livedatas.LiveDataBus;
import com.imchat.chanttyai.ui.fragment.manager.AvatarManager;
import com.imchat.chanttyai.utils.ClickHelper;
import com.imchat.chanttyai.utils.SharedPreferUtil;
import com.imchat.chanttyai.utils.ToastUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CreateAIActivity extends BaseActivity<ActivityCreateAiBinding> {

    private boolean mCreateEnabled;

    private String mAiAvatarPos;

    @Override
    protected ActivityCreateAiBinding getViewBinding() {
        return ActivityCreateAiBinding.inflate(getLayoutInflater());
    }

    public static void start(Context context) {
        context.startActivity(new Intent(context, CreateAIActivity.class));
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        fetchData();
    }

    private void fetchData() {
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
                        mBinding.tvCount.setText("创建个数"+ aiBeans.size() +"/3");
                    }
                });

    }

    @Override
    protected void initListener() {
        ClickHelper.getInstance().setOnClickListener(mBinding.tvCreate,this::onCreateAI,false);
        ClickHelper.getInstance().setOnClickListener(mBinding.ivAvatar,this::onJump2ChooseAvatar,true);
        mBinding.etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mBinding.tvNameCount.setText(mBinding.etName.getText().toString().length() + "/32");
                switchCreateDisabled();
            }
        });

        mBinding.etDesc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mBinding.tvDescCount.setText(mBinding.etDesc.getText().toString().length() + "/1024");
                switchCreateDisabled();
            }
        });

        LiveDataBus.get().with(Constants.KEY_AI_AVATAR, String.class).observe(this, this::onAvatarSelected);
    }

    private void onAvatarSelected(String pos) {
        mAiAvatarPos = pos;
        mBinding.ivAvatar.setImageResource(AvatarManager.getInstance().getAIAvatarBean(pos).getHead());
    }

    private void onJump2ChooseAvatar(View view) {
        ChooseAvatarActivity.start(mContext, mAiAvatarPos,Constants.AVATAR_TYPE_AI);
    }

    private void switchCreateDisabled() {
        mCreateEnabled = mBinding.etName.getText().toString().length() > 0 && mBinding.etDesc.getText().toString().length() > 0;
        mBinding.tvCreate.setTextColor(Color.parseColor(mCreateEnabled ? "#33B1FF" : "#464E53"));
    }


    public void onCreateAI(View view) {
        if (!mCreateEnabled) {
            return;
        }

        Map<String, Object> map = new HashMap<>();
        map.put("botName", mBinding.etName.getText().toString());
        map.put("describe", mBinding.etDesc.getText().toString());
        if (TextUtils.isEmpty(mAiAvatarPos)) {
            mAiAvatarPos = AvatarManager.getInstance().getRandomAvatar();
        }
        map.put("pic", mAiAvatarPos);
        map.put("createAccount", SharedPreferUtil.getInstance().getAccount());
        map.put("open", mBinding.cb.isChecked() ? 1 : 0);

        App.getApi().createBot(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new QObserver<Empty>(mContext) {
                    @Override
                    public void next(Empty empty) {
                        ToastUtils.toast("创建成功");
                        SharedPreferUtil.getInstance().fetchSaveAIs(mContext);
                        AppManager.getInstance().finishActivity(CreateAIActivity.class);
                    }
                });
    }
}
