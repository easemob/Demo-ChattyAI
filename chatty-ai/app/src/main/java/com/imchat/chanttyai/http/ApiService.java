package com.imchat.chanttyai.http;


import com.imchat.chanttyai.beans.AIBean;
import com.imchat.chanttyai.beans.BotBean;
import com.imchat.chanttyai.beans.CommonBean;
import com.imchat.chanttyai.beans.Empty;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface ApiService {
    @GET
    Call<ResponseBody> getUsers(@Url String url);


    /*创建智能体*/
    @POST("createBot")
    Observable<CommonBean<Empty>> createBot(@Body Map<String, Object> map);

    /*删除智能体*/
    @POST("removeMyBot")
    Observable<CommonBean<Empty>> removeMyBot(@Body Map<String, Object> map);

    /*获取我创建的智能体*/
    @GET("getMyBot")
    Observable<CommonBean<List<AIBean>>> getMyBot(@QueryMap Map<String, String> map);

    /*获取公开的智能体*/
    @GET("getOpenBot")
    Observable<CommonBean<List<AIBean>>> getOpenBot();

    /*获取智能体*/
    @GET("getBot")
    Observable<CommonBean<List<AIBean>>> getBot(@QueryMap Map<String, String> map);

    /*发送群聊消息*/
    @POST("chatty/groupMessage.json")
    Observable<CommonBean<Empty>> sendGroupMessage(@Body Map<String, Object> map);
}
