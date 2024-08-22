package com.easemob.chattyai.chat.controller;

import cn.hutool.core.util.StrUtil;
import com.easemob.chattyai.api.ResponseModel;
import com.easemob.chattyai.bean.ChatBot;
import com.easemob.chattyai.bean.Constant;
import com.easemob.chattyai.bean.minimax.BotBean;
import com.easemob.chattyai.chat.service.ChatBotService;
import com.easemob.chattyai.chat.service.ChattyService;
import com.easemob.chattyai.chat.util.BotSettingUtil;
import com.easemob.im.api.model.EMCreateUsersResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @BelongsProject: chattyai
 * @BelongsPackage: com.easemob.chattyai.chat.controller
 * @Author: alonecoder
 * @CreateTime: 2023-11-26  12:15
 * @Description: 获取bot 用户的信息
 * @Version: 1.0
 */
@RestController
public class BotsController {

    @Autowired
    private ChattyService chattyService;

    @Autowired
    private ChatBotService chatBotService;


    /**
     * 获取全部公开的机器人
     * @return
     */
    @GetMapping("/getOpenBot")
    public ResponseModel getOpenBot(){
        Map<String,Object> props = new HashMap<>(3);
        props.put("open",1);
        List<ChatBot> list = chatBotService.list(props,"create_time desc",0,0);
        return ResponseModel.ok(list);
    }

    /**
     * 获取我创建的机器人
     * @param phone
     * @return
     */
    @GetMapping("/getMyBot")
    public ResponseModel getMyBot(String phone){
        if(StringUtils.isBlank(phone)){
            return ResponseModel.error("phone is required");
        }
        Map<String,Object> props = new HashMap<>(3);
        props.put("createAccount",phone);
        List<ChatBot> list = chatBotService.list(props,"create_time desc",1,3);
        return ResponseModel.ok(list);
    }

    /**
     * 获取默认的4个机器人
     * @return
     */
    @GetMapping("/getDefaultBot")
    public ResponseModel getDefaultBot(){
        Map<String,Object> props = new HashMap<>(3);
        props.put("createAccount","default");
        List<ChatBot> list = chatBotService.list(props,"create_time desc",1,3);
        return ResponseModel.ok(list);
    }

    /**
     * 获取机器人接口
     * @param type
     * @param phone
     * @return
     */
    @GetMapping("/getBot")
    public ResponseModel getBot(String type,String phone){
        if(StringUtils.isBlank(type)){
            return ResponseModel.error("type is required");
        }
        int page  = 0;
        int pageSize = 0;
        Map<String,Object> props = new HashMap<>(3);
        if("1".equals(type)){
            //获取公开的机器人
            props.put("open",1);

        }else if("2".equals(type)){
            if(StringUtils.isBlank(phone)){
                return ResponseModel.error("phone is required");
            }
            props.put("createAccount",phone);
            page = 1;
            pageSize = 3;
        }else if("3".equals(type)){
            //获取默认的4个机器人
            props.put("createAccount","default");
        }else if("4".equals(type)){
            if(StringUtils.isBlank(phone)){
                return ResponseModel.error("phone is required");
            }
            props.put("open",1);
            List<ChatBot> list = chatBotService.list(props,"create_time desc",page,pageSize);
            props.clear();
            props.put("createAccount",phone);
            props.put("open",0);
            page = 1;
            pageSize = 3;
            List<ChatBot> list1 = chatBotService.list(props,"create_time desc",page,pageSize);
            if(list1.size() > 0){
                list.addAll(list1);
            }
            return ResponseModel.ok(list);
        }else{
            return ResponseModel.error("illegal type");
        }
        List<ChatBot> list = chatBotService.list(props,"create_time desc",page,pageSize);
        return ResponseModel.ok(list);
    }


    @PostMapping("/createBot")
    public ResponseModel addBot(@RequestBody ChatBot chatBot){
        if(chatBot == null){
            return ResponseModel.error("illegal request");
        }
        if(StringUtils.isBlank(chatBot.getBotName())){
            return ResponseModel.error("botName is required");
        }
        if(StringUtils.isBlank(chatBot.getDescribe())){
            return ResponseModel.error("describe is required");
        }
        if(StringUtils.isBlank(chatBot.getCreateAccount())){
            return ResponseModel.error("createAccount is required");
        }
        if(chatBot.getOpen() == null){
            //如果没有传递open字段，则认为不开放
            chatBot.setOpen(0);
        }
        if(StringUtils.isBlank(chatBot.getPic())){
            //没设置头像就设置默认头像
            chatBot.setPic("/avatar/1222675079884701696.jpg");
        }
        Map<String,Object> props = new HashMap<>(3);
        props.put("createAccount",chatBot.getCreateAccount());
        int res = chatBotService.count(props);
        if(res >= 3){
            return ResponseModel.error("Failed to create bot.number is over limited");
        }
        //创建机器人账号
        String eaAccount = BotSettingUtil.getRondomBotAccount();
        chatBot.setEaAccount(eaAccount);
        EMCreateUsersResult user = chattyService.createUser(eaAccount, Constant.DEFAULT_BOT_PASSWORD);
        if(user == null){
            return ResponseModel.error("Failed to create bot.plz try again later");
        }
        res = chatBotService.save(chatBot);
        if(res > 0){
            return ResponseModel.ok();
        }
        return ResponseModel.error("Failed to create bot.plz try again later");
    }


    /**
     * 获取机器人用户的信息
     * @return
     */
    @GetMapping("/getBotUsers")
    public List<BotBean> getBotUsers(){
        Map<String, BotBean> bots = BotSettingUtil.bots;
        return new ArrayList<>(bots.values());
    }


    /**
     * 删除我创建的机器人
     * @param params
     * @return
     */
    @PostMapping("/removeMyBot")
    public ResponseModel addBot(@RequestBody Map<String,Object> params) {
        if (params == null) {
            return ResponseModel.error("illegal request");
        }
        Object ido = params.get("id");
        if(ido == null || StrUtil.isBlank(ido.toString())){
            return ResponseModel.error("id is required");
        }
        Object createAccount = params.get("createAccount");
        if(createAccount == null || StrUtil.isBlank(createAccount.toString())){
            return ResponseModel.error("createAccount is required");
        }
        Long id = Long.parseLong(ido.toString());
        ChatBot chatBot = chatBotService.getById(Long.parseLong(id.toString()));
        if(chatBot == null){
            return ResponseModel.error("bot isn't exist");
        }
        if(!Objects.equals(createAccount,chatBot.getCreateAccount())){
            return ResponseModel.error("illegal operation");
        }

        int i  = chatBotService.deleteById(id);
        if(i > 0) {
            //删除我创建的机器人之后，需要通知环信IM删除账号
            chattyService.deleteUser(chatBot.getEaAccount());
        }
        return ResponseModel.ok();

    }

}
