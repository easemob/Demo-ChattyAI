package com.easemob.chattyai.chat.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.easemob.chattyai.api.ResponseModel;
import com.easemob.chattyai.bean.Constant;
import com.easemob.chattyai.bean.GroupMessage;
import com.easemob.chattyai.bean.easemob.CallbackEntity;
import com.easemob.chattyai.bean.easemob.MessageEntity;
import com.easemob.chattyai.chat.service.ChattyService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @BelongsProject: chattyai
 * @BelongsPackage: com.easemob.chattyai.chat.controller
 * @Author: alonecoder
 * @CreateTime: 2023-11-26  12:15
 * @Description: ChattyController
 * @Version: 1.0
 */
@RestController
@Slf4j
@RequestMapping("/chatty")
public class ChattyController {

    @Autowired
    private ChattyService chattyService;

    /**
     * 回调接口接口
     * @param params
     * @return
     */
    @RequestMapping(value = "callback.json", method = RequestMethod.POST, headers = { "content-type=application/json" })
    public CallbackEntity callback(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        MessageEntity messageEntity = BeanUtil.mapToBean(params, MessageEntity.class, true, new CopyOptions());
        log.info("收到消息：{}",messageEntity);
        //不是对机器人发起的消息不处理
        if(messageEntity != null && StringUtils.isNotBlank(messageEntity.getTo())
                && !messageEntity.getTo().startsWith(Constant.DEFAULT_BOT_ACCOUNT_PREFIX)){
            return new CallbackEntity(true);
        }
        //如果不是单聊不回复
        if(StringUtils.isNotBlank(messageEntity.getChatType()) && !Objects.equals(messageEntity.getChatType(), "chat")){
            return new CallbackEntity(true);
        }
        //消息
        String msg = messageEntity.getPayload().getBodies().get(0).getMsg();
        chattyService.chatToAi(messageEntity.getTo(),messageEntity.getFrom(),msg);
        return new CallbackEntity(true);
    }


    /**
     * 群聊单独接口
     * @param groupMessage
     * @param request
     * @return
     */
    @RequestMapping(value = "groupMessage.json", method = RequestMethod.POST, headers = { "content-type=application/json" })
    public ResponseModel groupMessage(@RequestBody GroupMessage groupMessage, HttpServletRequest request) {
        if(groupMessage == null){
            return ResponseModel.error("illegal request");
        }
        List<String> botAccount = groupMessage.getBotAccount();
        if(botAccount == null || botAccount.size() == 0){
            return ResponseModel.error("botAccount is required");
        }
        if(StringUtils.isBlank(groupMessage.getGroupId())){
            return ResponseModel.error("groupId is required");
        }
        if(StringUtils.isBlank(groupMessage.getMessage())){
            return ResponseModel.error("message is required");
        }
        log.info("收到群消息：{}",groupMessage);
        int res = chattyService.groupMessage(groupMessage);
        if(res  == 1) {
            return ResponseModel.ok();
        }
        return ResponseModel.error("send message failed");
    }


}
