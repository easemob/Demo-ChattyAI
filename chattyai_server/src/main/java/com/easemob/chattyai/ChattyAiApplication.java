package com.easemob.chattyai;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * ChattyAI 项目启动类
 */
@ComponentScan({"com.easemob.chattyai.**"})
@MapperScan("com.easemob.chattyai.chat.dao")
@SpringBootApplication
@EnableScheduling
public class ChattyAiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChattyAiApplication.class, args);
	}

}
