package com.easemob.chattyai.bean;

/**
 * @BelongsProject: chattyai
 * @BelongsPackage: com.easemob.chattyai.bean
 * @Author: alonecoder
 * @CreateTime: 2023-11-30  19:48
 * @Description: TODO
 * @Version: 1.0
 */
public interface Constant {


    /**
     * 用户单聊聊天记录的前缀
     */
    String CHAT_HISTORY_REDIS_KEY_PREFIX = "chattyai:chathistory:";
    /**
     * 用户群聊聊天记录的前缀
     */
    String CHAT_GROUP_HISTORY_REDIS_KEY_PREFIX = "chattyai:group:chathistory:";


    /**
     * 默认的机器人账号前缀
     */
    String DEFAULT_BOT_ACCOUNT_PREFIX= "bot";

    /**
     * 默认机器人账号使用的密码
     */
    String DEFAULT_BOT_PASSWORD="botPassword2024";


}
