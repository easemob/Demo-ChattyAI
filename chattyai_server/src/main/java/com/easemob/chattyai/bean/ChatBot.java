package com.easemob.chattyai.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ChatBot implements Serializable {

    private Long id;
    /**
     * 机器人名称
     */
    private String botName;
    /**
     * 描述
     */
    private String describe;
    /**
     * 头像
     */
    private String pic;
    /**
     * 是否公开
     */
    private Integer open;
    /**
     * 机器人对应环信IM的账号
     */
    private String eaAccount;
    /**
     * 创建人的环信IM的账号
     */
    private String createAccount;
    /**
     * 创建时间
     */
    private Date createTime;

}
