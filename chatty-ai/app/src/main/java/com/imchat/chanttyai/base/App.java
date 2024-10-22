package com.imchat.chanttyai.base;

import android.app.Application;
import android.graphics.Color;

import com.google.gson.GsonBuilder;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.chat.EMTextMessageBody;
import com.imchat.chanttyai.http.ApiService;
import com.imchat.chanttyai.listeners.AppLifecycleListener;
import com.imchat.chanttyai.livedatas.LiveDataBus;
import com.imchat.chanttyai.ui.fragment.manager.AvatarManager;
import com.imchat.chanttyai.utils.NotificationHelper;
import com.imchat.chanttyai.utils.SharedPreferUtil;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class App extends Application {
    private static Application mApplication;
    private static ApiService sApi;
    private AppLifecycleListener mAppLifecycleListener;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;

        initSrl();

        initIm();

        initHttp();

        initLife();

        initAvatars();
    }

    private void initSrl() {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator((context, layout) -> {
//              设置头的属性
            ClassicsHeader header = new ClassicsHeader(context);
//              设置背景颜色
//            header.setPrimaryColorId(R.color.C6);
//              设置字体颜色
            header.setAccentColor(Color.parseColor("#94A0B1"));
//              设置字体大小
            header.setTextSizeTitle(15);

            return header;

        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator((context, layout) -> {
            //指定为经典Footer，默认是 BallPulseFooter
            //设置脚的属性
            ClassicsFooter footer = new ClassicsFooter(context);
            // 设置背景颜色
//            footer.setPrimaryColorId(R.color.C6);
            // 设置字体颜色
            footer.setAccentColor(Color.parseColor("#94A0B1"));
            // 设置字体大小
            footer.setTextSizeTitle(15);
            return footer;
        });
    }

    private void initAvatars() {
        AvatarManager.getInstance().init();
    }

    private void initLife() {
        mAppLifecycleListener = new AppLifecycleListener();
        registerActivityLifecycleCallbacks(mAppLifecycleListener);
    }

    public boolean isAppInForeground() {
        return mAppLifecycleListener.isAppInForeground();
    }

    private void initHttp() {
        OkHttpClient client = new OkHttpClient
                .Builder()
                //断线重连
                .retryOnConnectionFailure(true)
                //链接超时时间
                .connectTimeout(Constants.NET_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                //写超时时间
                .writeTimeout(Constants.NET_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                //读超时时间
                .readTimeout(Constants.NET_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .build();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.HTTP_HOST)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create())) //增加返回值为Gson的支持(以实体类返回)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) //Observer<T>的支持
                .client(client)//配置OkHttpClient
                .build();

        sApi = retrofit.create(ApiService.class);

    }

    private void initIm() {
        EMOptions options = new EMOptions();
        options.setAppKey(Constants.APP_KEY);
        EMClient.getInstance().init(mApplication, options);

        EMClient.getInstance().chatManager().addMessageListener(new EMMessageListener() {
            // 收到消息，遍历消息队列，解析和显示。
            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                if (!isAppInForeground()) {
                    for (EMMessage message : messages) {
                        EMMessageBody body = message.getBody();
                        if (body instanceof EMTextMessageBody) {
                            String msgStr = ((EMTextMessageBody) body).getMessage();
                            NotificationHelper.showNotification(mApplication, SharedPreferUtil.getInstance().getAIBean(message.getFrom()).getBotName(), msgStr);
                        }
                    }
                }
                LiveDataBus.get().with(Constants.MSG_RECEIVED).postValue("");
            }
        });
    }

    public static ApiService getApi() {
        return sApi;
    }

    public static Application getApplication() {
        return mApplication;
    }

}
