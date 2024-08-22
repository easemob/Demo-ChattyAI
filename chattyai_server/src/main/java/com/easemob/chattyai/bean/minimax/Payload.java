package com.easemob.chattyai.bean.minimax;

import lombok.Data;

import java.util.List;

/**
 * @BelongsProject: chattyai
 * @BelongsPackage: com.easemob.chattyai.bean
 * @Author: alonecoder
 * @CreateTime: 2023-11-26  13:31
 * @Description: TODO
 * @Version: 1.0
 */
@Data
public class Payload {
    private List<Bodies> bodies;
    private Ext ext;
    private String from;
    private Meta meta;
    private String to;
    private String type;
}
