package com.easemob.chattyai.bean.minimax;

import lombok.Data;

import java.io.Serializable;

/**
 * @BelongsProject: chattyai
 * @BelongsPackage: com.easemob.chattyai.bean
 * @Author: alonecoder
 * @CreateTime: 2023-11-27  19:16
 * @Description: TODO
 * @Version: 1.0
 */
@Data
public class Messages implements Serializable {
    private String sender_type;
    private String sender_name;
    private String text;
}
