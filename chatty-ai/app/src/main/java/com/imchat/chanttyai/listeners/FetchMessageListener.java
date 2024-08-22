package com.imchat.chanttyai.listeners;

import com.hyphenate.chat.EMMessage;

import java.util.List;

public interface FetchMessageListener {
    void onDone(List<EMMessage> messages,String startMessageId);

    void onFailed(String msg);
}
