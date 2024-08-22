package com.easemob.chattyai.chat.service.impl;

import cn.hutool.json.JSONObject;
import com.easemob.chattyai.base.AiStrategy;
import com.easemob.chattyai.bean.GroupMessage;
import com.easemob.chattyai.chat.service.ChattyService;
import com.easemob.im.ApiException;
import com.easemob.im.api.MessageApi;
import com.easemob.im.api.UserApi;
import com.easemob.im.api.model.*;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @BelongsProject: chattyai
 * @BelongsPackage: com.easemob.chattyai.chat.service.impl
 * @Author: alonecoder
 * @CreateTime: 2023-11-25  00:43
 * @Description: CharttyServiceImpl
 * @Version: 1.0
 */
@Slf4j
@Service
public class CharttyServiceImpl implements ChattyService {

    private UserApi userApi;

    private MessageApi messageApi;


    @Value("${config.ai}")
    private String ai;

    @Setter
    private Map<String, AiStrategy> beans;

    /**
     * 这里之所以这么复制，是因为如果使用@Autowired注解进行注入时
     * 环信SDK没有初始化，可能会导致在创建UserApi对象时，环信SDK属性赋值不上
     * 这里采用了Double check Lock DCL机制，保证了userApi只会初始化一次
     * @return
     */
    private UserApi getUserApi(){
        if(this.userApi == null){
            synchronized (this){
                if(userApi == null){
                    this.userApi = new UserApi();
                }
            }
        }
        return userApi;
    }


    /**
     * 这里之所以这么复制，是因为如果使用@Autowired注解进行注入时
     * 环信SDK没有初始化，可能会导致在创建messageApi对象时，环信SDK属性赋值不上
     * 这里采用了Double check Lock DCL机制，保证了messageApi只会初始化一次
     * @return
     */
    private MessageApi getMessageApi(){
        if(this.messageApi == null){
            synchronized (this){
                if(this.messageApi == null){
                    this.messageApi = new MessageApi();
                }
            }
        }
        return messageApi;
    }



    /**
     * 创建用户
     *
     * @return
     */
    public EMCreateUsersResult createUser(String username, String password) {
        List<EMCreateUser> emCreateUserList = new ArrayList<>();
        EMCreateUser createUser = new EMCreateUser();
        createUser.setUsername(username);
        createUser.setPassword(password);
        emCreateUserList.add(createUser);
        try {
            return getUserApi().createUsers(emCreateUserList);
        } catch (ApiException e) {
            e.getMessage();
        }
        return null;
    }

    @Override
    public EMDeleteUserResult deleteUser(String username) {
        if(StringUtils.isBlank(username)){
            return null;
        }
        try {
            return getUserApi().deleteUser(username);
        } catch (ApiException e) {
            e.getMessage();
        }
        return null;
    }


    public void removeUser(String username){

    }

    /**
     * 和AI进行聊天
     *
     * @param to   接收人
     * @param from 发送人
     * @param text 发送内容
     */
    public void chatToAi(String to, String from, String text) {
        String singleHandler = "minimax-single";
        String message = "";
        for (AiStrategy aiStrategy : beans.values()){
            if(aiStrategy.check(singleHandler)){
                message = aiStrategy.sendMessage(from,to,text,true);
                break;
            }
        }
        if(StringUtils.isBlank(message)){
            log.error("chat callback . message is empty");
            return;
        }
        /**
         * 这里需要反向发送消息，给minimax ai回复的内容反向发送给发送人
         */
        EMSendMessageResult messageIds = sendMessageText(from, to, message);
    }


    /**
     * 发送单条消息
     *
     * @param to      发给谁
     * @param from    发送人
     * @param message 发送的文本消息
     * @return
     */
    public EMSendMessageResult sendMessageText(String to, String from, String message) {
        EMCreateMessage emCreateMessage = new EMCreateMessage();
        emCreateMessage.setFrom(from);
        emCreateMessage.setTo(new ArrayList<String>(){{add(to);}});
        emCreateMessage.setType("txt");
        EMMessageContent messageContent = new EMMessageContent();
        messageContent.setMsg(message);
        emCreateMessage.setBody(messageContent);
        EMSendMessageResult response = null;
        try {
            response = getMessageApi().sendMessagesToUser(emCreateMessage);
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }
        return response;
    }

    @Override
    public int groupMessage(GroupMessage groupMessage) {
        String groupHandler = "minimax-group";
        String message = "";
        AiStrategy targetAiStrategy = null;
        for (AiStrategy aiStrategy : beans.values()){
            if(aiStrategy.check(groupHandler)){
                targetAiStrategy = aiStrategy;
                break;
            }
        }
        if(targetAiStrategy == null){
            log.error("targetAiStrategy is null");
            return 0;
        }
        List<String> botAccountList = groupMessage.getBotAccount();
        if(botAccountList == null || botAccountList.size() == 0){
            return 0;
        }
        for (String botAccount : botAccountList) {
            message = targetAiStrategy.sendMessage(groupMessage.getSenderAccount(), botAccount, groupMessage.getMessage(), true);
//            if (StringUtils.isBlank(message)) {
//                log.error("message is empty");
//            }else {
//                sendGroupMessageText(groupMessage, botAccount, message);
//            }
            sendGroupMessageText(groupMessage, botAccount, message);
        }
        return 0;
    }


    /**
     * 发送群聊消息
     *
     * @param groupMessage 自定义群聊消息体
     * @param botAccount   智能体环信id
     * @param message      消息体
     */
    public void sendGroupMessageText(GroupMessage groupMessage, String botAccount, String message){
        if(StringUtils.isBlank(message)){
            return;
        }

        EMCreateMessage emCreateMessage = new EMCreateMessage();
        emCreateMessage.setFrom(botAccount);
        emCreateMessage.setTo(new ArrayList<String>() {{
            add(groupMessage.getGroupId());
        }});
        emCreateMessage.setType("txt");
        EMMessageContent messageContent = new EMMessageContent();
        messageContent.setMsg(message);
        emCreateMessage.setBody(messageContent);
        //如果发送人不为空，则返回消息的时候回at一下发送人
        if (StringUtils.isNotBlank(groupMessage.getSenderAccount())) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.set("em_at_list", new ArrayList<String>().add(groupMessage.getSenderAccount()));
            emCreateMessage.setExt(jsonObject);
        }
        try {
            EMSendMessageResult response = getMessageApi().sendMessagesToGroup(emCreateMessage);
            log.info("send group message success"+response);
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }
}
