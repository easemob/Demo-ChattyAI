package com.easemob.chattyai.task;

import cn.hutool.core.date.DateUtil;
import com.easemob.chattyai.bean.ChatBot;
import com.easemob.chattyai.bean.Constant;
import com.easemob.chattyai.chat.service.ChatBotService;
import com.easemob.chattyai.chat.service.ChattyService;
import com.easemob.chattyai.chat.util.GreetUtil;
import com.easemob.chattyai.chat.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @BelongsProject: chattyai
 * @BelongsPackage: com.easemob.chattyai.task
 * @Author: alonecoder
 * @CreateTime: 2023-11-30  19:42
 * @Description: 问候语定时任务
 * @Version: 1.0
 */
@Component
@Slf4j
public class GreetTask {

    @Autowired
    private ChattyService chattyService;

    @Autowired
    private ChatBotService chatBotService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 固定每天早上9点 中午12点，晚上21点 给用户发送消息
     */
    @Scheduled(cron = "0 0 9,12,21 * * ? ")
    public void test(){
        log.info("定时任务开始执行");
        List<String> res = redisUtil.getKeys(Constant.CHAT_HISTORY_REDIS_KEY_PREFIX);
        if(res == null || res.size() == 0){
            return;
        }
        log.info("定时任务开始执行，共有{}个用户需要发送问候语",res.size());
        //查出所有机器人，
        Map<String,Object> props = new HashMap<>(3);
        List<ChatBot> list = chatBotService.list(props,"create_time desc",0,0);
        List<String> eaAccount = list.stream().map(ChatBot::getEaAccount).collect(Collectors.toList());

        for (String re : res) {
            String to4From = re.replaceAll(Constant.CHAT_HISTORY_REDIS_KEY_PREFIX,"");
            String[] split = to4From.split(":");
            String from  = split[0];
            String to = split[1];
            if(eaAccount.contains(to)) {
                String message = GreetUtil.getRomdanGreet();
                chattyService.sendMessageText(from, to, message);
            }else{
                log.info("机器人已经被删除，不发送问候语");
                Cursor<byte[]> cursor = redisTemplate.getConnectionFactory().getConnection().scan(ScanOptions.scanOptions()
                        .match(Constant.CHAT_HISTORY_REDIS_KEY_PREFIX + ":*:"+to)
                        .count(1000)
                        .build());

                List<String> keys = new ArrayList<>();
                while (cursor.hasNext()) {
                    keys.add(new String(cursor.next()));
                }
                for (String key : keys) {
                    redisTemplate.delete(key);
                }
            }
        }
    }

}
