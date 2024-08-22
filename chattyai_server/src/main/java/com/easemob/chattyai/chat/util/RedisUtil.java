package com.easemob.chattyai.chat.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @BelongsProject: chattyai
 * @BelongsPackage: com.easemob.chattyai.chat.util
 * @Author: alonecoder
 * @CreateTime: 2023-11-27  20:27
 * @Description: RedisUtil
 * @Version: 1.0
 */
@Slf4j
@Component
public class RedisUtil {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 获取list缓存的内容
     *
     * @param key   键
     * @param start 开始 0 是第一个元素
     * @param end   结束 -1代表所有值
     * @return
     * @取出来的元素 总数 end-start+1
     */
    public List<Object> lGet(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            log.error(key, e);
            return null;
        }
    }

    /**
     * 获取list缓存的内容
     *
     * @param key   键
     * @return
     */
    public List<Object> getAll(String key) {
        try {
            return redisTemplate.opsForList().range(key, 0, -1);
        } catch (Exception e) {
            log.error(key, e);
            return null;
        }
    }

    /**
     * 获取list缓存的长度
     *
     * @param key 键
     * @return
     */
    public long lGetListSize(String key) {
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            log.error(key, e);
            return 0;
        }
    }

    /**
     * 删除掉左侧的count个
     *
     * @param key
     * @param count
     * @return
     */
    public long leftpop(String key, Long count) {
        try {
            redisTemplate.opsForList().leftPop(key,count);
            return 1;
        } catch (Exception e) {
            log.error(key, e);
            return 0;
        }
    }

    /**
     *
     * @param key
     * @param object
     * @return
     */
    public long rightPush(String key, Object object) {
        try {
            redisTemplate.opsForList().rightPush(key, object);
            return 1;
        } catch (Exception e) {
            log.error(key, e);
            return 0;
        }
    }

    public List<String> getKeys(String prefix) {
        Cursor<byte[]> cursor = redisTemplate.getConnectionFactory().getConnection().scan(ScanOptions.scanOptions()
                .match(prefix + "*")
                .count(1000)
                .build());

        List<String> keys = new ArrayList<>();
        while (cursor.hasNext()) {
            keys.add(new String(cursor.next()));
        }
        return keys;
    }
}
