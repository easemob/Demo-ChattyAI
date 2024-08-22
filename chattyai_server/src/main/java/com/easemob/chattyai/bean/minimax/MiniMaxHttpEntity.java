package com.easemob.chattyai.bean.minimax;

import lombok.Data;

import java.util.List;

/**
 * @BelongsProject: chattyai
 * @BelongsPackage: com.easemob.chattyai.bean
 * @Author: alonecoder
 * @CreateTime: 2023-11-27  19:15
 * @Description: MiniMaxHttpEntity
 * @Version: 1.0
 */
@Data
public class MiniMaxHttpEntity {
    private String model;
    private int tokensToGenerate;
    private double temperature;
    private double topP;
    private boolean stream;
    private ReplyConstraints replyConstraints;
    private List<String> sampleMessages;
    private List<String> plugins;
    private List<Messages> messages;
    private List<BotSetting> botSetting;

}

