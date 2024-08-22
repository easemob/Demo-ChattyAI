package com.easemob.chattyai.bean.easemob;

import lombok.Data;

/**
 * @BelongsProject: chattyai
 * @BelongsPackage: com.easemob.chattyai.bean
 * @Author: alonecoder
 * @CreateTime: 2023-11-25  14:20
 * @Description: 环信IM的回调内容
 * @Version: 1.0
 */
@Data
public class CallbackEntity {

    /**
     * 一般情况下返回false，阻止发送给目标人，因为我们是需要发送给机器人的，机器人收到收不到，意义不大
     */
    private Boolean valid;

    /**
     * 自定义信息，可以随意传递
     */
    private String code;

    /**
     * 暂时不需要，消息体
     */
    private Object payload;

    public CallbackEntity(){}

    public CallbackEntity(Boolean valid){
        this.valid = valid;
        this.code= "";
        this.payload = null;
    }

}
