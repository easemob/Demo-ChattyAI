package com.easemob.chattyai.chat.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.easemob.chattyai.bean.minimax.BotSetting;
import com.easemob.chattyai.bean.minimax.Messages;
import com.easemob.chattyai.bean.minimax.MiniMaxHttpEntity;
import com.easemob.chattyai.bean.minimax.ReplyConstraints;
import com.easemob.chattyai.config.MiniMaxConfig;

import java.util.*;

/**
 * @BelongsProject: chattyai
 * @BelongsPackage: com.easemob.chattyai.chat.util
 * @Author: alonecoder
 * @CreateTime: 2023-11-27  18:48
 * @Description: MiniMax AI模型工具类
 * @Version: 1.0
 */
public class MiniMaxUtil {


    private MiniMaxUtil(){}

    private static MiniMaxConfig miniMaxConfig;

    private static MiniMaxConfig getMiniMaxConfig(){
        if(miniMaxConfig == null){
            miniMaxConfig = SpringUtils.getBean(MiniMaxConfig.class);
        }
        return miniMaxConfig;
    }


}
