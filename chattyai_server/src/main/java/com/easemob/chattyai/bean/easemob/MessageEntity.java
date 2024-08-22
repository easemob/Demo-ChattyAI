package com.easemob.chattyai.bean.easemob;

import com.easemob.chattyai.bean.minimax.Payload;
import lombok.Data;

/**
 * @BelongsProject: chattyai
 * @BelongsPackage: com.easemob.chattyai.bean
 * @Author: alonecoder
 * @CreateTime: 2023-11-26  13:30
 * @Description: TODO
 * @Version: 1.0
 */
@Data
public class MessageEntity {
    private String callId;
    private long timestamp;
    private String chatType;
    private String from;
    private String to;
    private String msgId;
    private Payload payload;
    private String security;
    private String appId;
}
