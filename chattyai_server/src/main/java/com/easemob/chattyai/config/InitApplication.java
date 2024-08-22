package com.easemob.chattyai.config;


import com.easemob.chattyai.base.AiStrategy;
import com.easemob.chattyai.chat.service.ChattyService;
import com.easemob.im.ApiClient;
import com.easemob.im.ApiException;
import com.easemob.im.Configuration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class InitApplication implements ApplicationContextAware {

    @Value("${easemob.appkey}")
    private String appkey;

    @Value("${easemob.clientId}")
    private String clientId;

    @Value("${easemob.clientSecret}")
    private String clientSecret;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext)  {
        //初始化 环信sdk
        try{
            Configuration.setDefaultApiClient(ApiClient.builder()
                    .setBasePath("https://a1.easemob.com")
                    .setAppKey(appkey)
                    .setClientId(clientId)
                    .setClientSecret(clientSecret)
                    .build());
            log.info("ease init sdk success");
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }

        //初始化 AI策略接口的所有实现对象
        Map<String, AiStrategy> beans = applicationContext.getBeansOfType(AiStrategy.class);
        ChattyService chattyService = applicationContext.getBean(ChattyService.class);
        chattyService.setBeans(beans);
    }
}
