package com.easemob.chattyai.base;

/**
 * AIStrategy  AI策略模式接口
 * @author alonecoder
 * @date 2024-03-28  22:19
 */
public interface AiStrategy {


    /**
     * 检测是否使用方法
     * @param aiName
     * @return 是否使用该AI来处理
     */
    boolean check(String aiName);


    /**
     * 给AI发送消息的方法  实现该接口时，请自行决定参数是否使用
     * 该方法主体实现功能是给AI发送消息，获取到AI返回的消息内容进行返回
     * @param sender 发送人
     * @param receiver 接收人
     * @param message 发送的消息体
     * @param needContext 是否需要上下文
     * @return 直接返回AI给的回复
     */
    String sendMessage(String sender,String receiver,String message,Boolean needContext);
}
