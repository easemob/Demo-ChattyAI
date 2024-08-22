package com.easemob.chattyai.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 发送群消息后，当用户点击某个AI头像，会给服务端发送的请求体
 */
@Data
public class GroupMessage implements Serializable {

    /**
     * 发送人用户id
     */
    private String senderAccount;
    /**
     * botid
     */
    private List<String> botAccount;
    /**
     * 群id
     */
    private String groupId;
    /**
     * 发送的消息内容
     */
    private String message;
}
