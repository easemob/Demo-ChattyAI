package com.easemob.chattyai.chat.service.impl;

import com.easemob.chattyai.bean.ChatBot;
import com.easemob.chattyai.chat.dao.ChatBotMapper;
import com.easemob.chattyai.chat.service.ChatBotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class ChatBotServiceImpl implements ChatBotService {

    @Autowired
    private ChatBotMapper chatBotMapper;

    @Override
    public int count(Map<String, Object> props) {
        return chatBotMapper.count(props);
    }

    @Override
    public int deleteById(Long id) {
        return chatBotMapper.deleteById(id);
    }

    @Override
    public ChatBot getByEsId(String esId) {
        return chatBotMapper.getByEsId(esId);
    }

    @Override
    public List<ChatBot> list(Map<String, Object> props, String order, int page, int pagesize) {
        int first = (page - 1) * pagesize;
        return chatBotMapper.listByPage(props, order, first, pagesize);
    }

    @Override
    public ChatBot getById(Long id) {
        return chatBotMapper.getById(id);
    }

    @Override
    public Integer save(ChatBot chatBot) {
        chatBot.setCreateTime(new Date());
        return chatBotMapper.insert(chatBot);
    }

    @Override
    public List<ChatBot> getOpenBot() {
        Map<String,Object> props = new HashMap<>(3);
        props.put("open",1);
        return chatBotMapper.listByPage(props, "create_time desc", 0, 0);
    }



}
