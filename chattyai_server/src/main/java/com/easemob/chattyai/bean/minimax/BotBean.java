package com.easemob.chattyai.bean.minimax;

import lombok.Data;

/**
 * @BelongsProject: chattyai
 * @BelongsPackage: com.easemob.chattyai.bean
 * @Author: alonecoder
 * @CreateTime: 2023-11-26  13:32
 * @Description: TODO
 * @Version: 1.0
 */
@Data
public class BotBean {

    /**
     * 机器人对应的账号
     */
    private String account;
    /**
     * 机器人的名字
     */
    private String name;

    /**
     * 机器人的性别
     */
    private Integer gender;
    /**
     * 机器人的详细描述
     */
    private String content;
    /**
     * 机器人的简单描述
     */
    private String desc;
}
