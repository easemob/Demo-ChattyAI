package com.easemob.chattyai.chat.dao;


import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

import com.easemob.chattyai.bean.ChatBot;

/**
 * @BelongsProject: chattyai
 * @BelongsPackage: com.easemob.chattyai.chat.dao
 * @Author: AnAloneJaver
 * @CreateTime: 2024-03-27 00:19:20
 * @Description: 聊天机器人
 * @Version: 1.0
 */
public interface ChatBotMapper {

    Integer count(@Param("props") Map<String, Object> props);

    List<ChatBot> listByPage(@Param("props") Map<String, Object> props, @Param("orderby") String orderby, @Param("first") int first,
                             @Param("pagesize") int pagesize);

    ChatBot getById(Long id);

    Integer insert(ChatBot chatBot);

    int deleteById(@Param("id") Long id);

    ChatBot getByEsId(String eaAccount);
}
