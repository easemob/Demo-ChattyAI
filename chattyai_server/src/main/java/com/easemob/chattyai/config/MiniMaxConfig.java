package com.easemob.chattyai.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @BelongsProject: chattyai
 * @BelongsPackage: com.easemob.chattyai.config
 * @Author: alonecoder
 * @CreateTime: 2023-11-27  18:44
 * @Description: MiniMax配置文件
 * @Version: 1.0
 */
@Data
@Configuration
public class MiniMaxConfig {

    @Value("${miniMax.groupId}")
    private String groupId;

    @Value("${miniMax.appkey}")
    private String appkey;

    @Value("${miniMax.url}")
    private String url;
}
