package com.easemob.chattyai.chat.service;

import com.easemob.chattyai.base.AiStrategy;
import com.easemob.chattyai.bean.GroupMessage;
import com.easemob.im.api.model.EMCreateUsersResult;
import com.easemob.im.api.model.EMDeleteUserResult;
import com.easemob.im.api.model.EMSendMessageResult;

import java.util.Map;

/**
 * @BelongsProject: chattyai
 * @BelongsPackage: com.easemob.chattyai.chat.service
 * @Author: alonecoder
 * @CreateTime: 2023-11-25  00:43
 * @Description: ChattyService
 * @Version: 1.0
 */
public interface ChattyService {

    void setBeans(Map<String, AiStrategy> beans);

    /**
     * 创建用户
     * @param username
     * @param password
     * @return
     */
    EMCreateUsersResult createUser(String username, String password);


    EMDeleteUserResult deleteUser(String username);


    /**
     *
     * @param to  接收人
     * @param from 发送人
     * @param text 发送内容
     * @return
     */
    void chatToAi(String to, String from, String text);


    /**
     * 发送消息
     * @param to
     * @param from
     * @param message
     * @return
     */
    EMSendMessageResult sendMessageText(String to, String from, String message);

    /**
     * 处理群聊消息
     * @param groupMessage
     */
    int groupMessage(GroupMessage groupMessage);
}
