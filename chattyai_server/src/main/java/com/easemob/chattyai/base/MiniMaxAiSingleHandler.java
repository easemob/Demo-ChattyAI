package com.easemob.chattyai.base;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.easemob.chattyai.bean.ChatBot;
import com.easemob.chattyai.bean.Constant;
import com.easemob.chattyai.bean.minimax.*;
import com.easemob.chattyai.chat.service.ChatBotService;
import com.easemob.chattyai.chat.util.RedisUtil;
import com.easemob.chattyai.config.MiniMaxConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * MiniMax ai 的策略实现
 *
 * @author alonecoder
 * @date 2024-3-29 00:07:53
 */
@Slf4j
@Component
public class MiniMaxAiSingleHandler implements AiStrategy {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private MiniMaxConfig miniMaxConfig;

    @Autowired
    private ChatBotService chatBotService;

    @Override
    public boolean check(String aiName) {
        String AI_NAME = "minimax-single";
        return AI_NAME.equals(aiName);
    }

    @Override
    public String sendMessage(String sender, String receiver, String message, Boolean needContext) {
//        BotBean botBean = BotSettingUtil.bots.get(receiver);
//        if (botBean == null) {
//            botBean = BotSettingUtil.bots.get("boy0");
//        }
        ChatBot chatBot = chatBotService.getByEsId(receiver);
        if(chatBot == null || chatBot.getId() == null){
            log.error("机器人不存在");
            return null;
        }
        BotSetting botSetting = new BotSetting();
        botSetting.setBot_name(chatBot.getBotName());
        botSetting.setContent(chatBot.getDescribe());
        List<Messages> messages = new ArrayList<>(20);
        String key = Constant.CHAT_HISTORY_REDIS_KEY_PREFIX + sender + ":" + chatBot.getEaAccount();
        if (needContext) {
            //处理历史消息
            List<Object> list = redisUtil.getAll(key);
            if (list != null && !list.isEmpty()) {
                for (Object o : list) {
                    messages.add((Messages) o);
                }
            }
        }
        //添加到当前消息
        Messages messageTarget = new Messages();
        messageTarget.setSender_name(sender);
        messageTarget.setSender_type("USER");
        messageTarget.setText(message);
        messages.add(messageTarget);
        ReplyConstraints replyConstraints = new ReplyConstraints();
        replyConstraints.setSender_name(botSetting.getBot_name());
        replyConstraints.setSender_type("BOT");
        JSONObject res = sendMiniMaxMessage(messages, replyConstraints, botSetting);
        if (res == null) {
            log.error("miniMax返回有误");
            return null;
        }
        log.info("miniMax 返回的消息是：" + JSONUtil.toJsonStr(res));
        Object o = res.get("reply");
        if (o == null) {
            log.error("miniMax返回有误");
            return null;
        }
        if (needContext) {
            //处理历史消息
            redisUtil.rightPush(key, messageTarget);
            Messages miniMaxMessage = new Messages();
            miniMaxMessage.setSender_name(botSetting.getBot_name());
            miniMaxMessage.setSender_type("BOT");
            miniMaxMessage.setText(o.toString());
            redisUtil.rightPush(key, miniMaxMessage);
            long l = redisUtil.lGetListSize(key);
            if (l > 10) {
                redisUtil.leftpop(key, 2L);
            }
        }
        return o.toString();
    }


    public JSONObject sendMiniMaxMessage(List<Messages> messages, ReplyConstraints replyConstraints, BotSetting botSetting) {
        MiniMaxHttpEntity miniMaxHttpEntity = new MiniMaxHttpEntity();
        miniMaxHttpEntity.setModel("abab5.5-chat");
        miniMaxHttpEntity.setTokensToGenerate(500);
        miniMaxHttpEntity.setTemperature(0.9);
        miniMaxHttpEntity.setTopP(0.95);
        miniMaxHttpEntity.setStream(false);
        miniMaxHttpEntity.setReplyConstraints(replyConstraints);
        miniMaxHttpEntity.setSampleMessages(Collections.emptyList());
        miniMaxHttpEntity.setPlugins(Collections.emptyList());
        miniMaxHttpEntity.setMessages(messages);
        List<BotSetting> botSettings = new ArrayList<>();
        botSettings.add(botSetting);
        miniMaxHttpEntity.setBotSetting(botSettings);
        String url = miniMaxConfig.getUrl() + miniMaxConfig.getGroupId();
        Map<String, String> header = new HashMap<>();
        header.put("Authorization", "Bearer " + miniMaxConfig.getAppkey());
        header.put("Content-Type", "application/json");
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(miniMaxHttpEntity, true, false);
        String body = JSONUtil.toJsonStr(stringObjectMap);
        System.out.println("发送给miniMax的消息为：" + body);
        HttpResponse execute = HttpUtil.createPost(url).addHeaders(header).body(body).execute();
        /**
         * 返回格式
         * {
         *     "created":1701087162,
         *     "model":"abab5.5-chat",
         *     "reply":"哎呀，施主，切莫伤心，贫道给你讲个笑话吧。",
         *     "choices":[
         *         {
         *             "finish_reason":"stop",
         *             "messages":[
         *                 {
         *                     "sender_type":"BOT",
         *                     "sender_name":"江逝水",
         *                     "text":"哎呀，施主，切莫伤心，贫道给你讲个笑话吧。"
         *                 }
         *             ]
         *         }
         *     ],
         *     "usage":{
         *         "total_tokens":467
         *     },
         *     "input_sensitive":false,
         *     "output_sensitive":false,
         *     "id":"01b3bab93c1ac80f8216746289566b4f",
         *     "base_resp":{
         *         "status_code":0,
         *         "status_msg":""
         *     }
         * }
         */

        return JSONUtil.parseObj(execute.body());

    }
}
