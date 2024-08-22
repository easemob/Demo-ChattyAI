package com.easemob.chattyai.chat.service;

import com.easemob.chattyai.bean.ChatBot;

import java.util.List;
import java.util.Map;

public interface ChatBotService {


    List<ChatBot> list(Map<String, Object> props, String order, int page, int pagesize);

    ChatBot getById(Long id);

    Integer save(ChatBot chatBot);

    List<ChatBot> getOpenBot();

    int count(Map<String, Object> props);

    int deleteById(Long id);

    ChatBot getByEsId(String esId);
}
