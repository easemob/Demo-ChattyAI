package com.imchat.chanttyai.base;

public interface Constants {
    String KEY_CHAT_TO = "key_chat_to";
    String MSG_RECEIVED = "msg_received";
    String MSG_READ = "msg_read";

    String TYPE_PUBLIC = "1";
    String TYPE_MINE = "2";
    String TYPE_ALL = "4";


    long NET_TIMEOUT_SECONDS = 30;
    String HTTP_HOST = "此处输入服务部署地址";
    String APP_KEY = "此处输入环信APPKEY";
    String KEY_AI_AVATAR = "key_ai_avatar";
    String KEY_SHOW_GROUP_ADD_MEM = "key_show_group_add_mem";
    String KEY_SHOW_GROUP_CREATE = "key_show_group_create";
    String KEY_REFRESH_CONVERSATION = "key_refresh_conversation";
    String KEY_UPDATE_UNREAD = "key_update_unread";
    String KEY_USER_INFO = "key_user_info";
    String KEY_UPDATE_USER = "key_update_user";
    String KEY_NICKNAME = "key_nickname";

    int AVATAR_TYPE_AI = 0;
    int AVATAR_TYPE_USER = 1;
    String KEY_CHOOSE_AVATAR_TYPE = "key_choose_avatar_type";
    String KEY_REFRESH_AI_LIST = "key_refresh_ai_list";
    String KEY_IS_GROUP = "key_is_group";
    String KEY_REFRESH_MEMBERS = "key_refresh_members";
    String TEXT_SHARE = "could u join in this group?";
    String KEY_GROUP = "key_group";
    String KEY_REFRESH_GROUP_NAME = "key_refresh_group_name";
    String KEY_DELETE_GROUP = "key_delete_group";
    String KEY_MENTION = "key_mention";
    String KEY_CHOOSED_AVATAR = "key_choosed_avatar";

    int MAX_AI_IN_GROUP = 5;

    int MSG_PAGE_SIZE = 20;
}
