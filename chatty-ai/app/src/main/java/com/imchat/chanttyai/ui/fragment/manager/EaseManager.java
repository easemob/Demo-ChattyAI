package com.imchat.chanttyai.ui.fragment.manager;

import androidx.fragment.app.FragmentManager;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMCursorResult;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMGroupManager;
import com.hyphenate.chat.EMGroupOptions;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMUserInfo;
import com.hyphenate.exceptions.HyphenateException;
import com.imchat.chanttyai.base.BaseActivity;
import com.imchat.chanttyai.base.Constants;
import com.imchat.chanttyai.listeners.FetchMessageListener;
import com.imchat.chanttyai.utils.thread.ThreadManager;
import com.imchat.chanttyai.views.LoadingDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EaseManager {

    private BaseActivity mActivity;
    private FragmentManager mFragmentManager;

    private EaseManager() {
    }

    private static EaseManager mEaseManager;

    public static EaseManager getInstance() {
        if (mEaseManager == null) {
            mEaseManager = new EaseManager();
        }
        return mEaseManager;
    }

    public void init(BaseActivity activity, FragmentManager fragmentManager) {
        mActivity = activity;
        mFragmentManager = fragmentManager;
    }

    private void showLoading() {
        LoadingDialog.getInstance().show(mActivity, mFragmentManager);
    }

    private void hideLoading() {
        LoadingDialog.getInstance().dismiss();
    }

    public int getUnreadCount(boolean isGroup) {
        int count = 0;
        Map<String, EMConversation> allConversations = EMClient.getInstance().chatManager().getAllConversations();
        for (Map.Entry<String, EMConversation> entry : allConversations.entrySet()) {
            EMConversation conversation = entry.getValue();
            if (isGroup == conversation.isGroup()) {
                count += conversation.getUnreadMsgCount();
            }
        }
        return count;
    }

    public void getConversationsFromServer(EMValueCallBack<Map<String, EMConversation>> callBack, boolean showLoading) {
        if (showLoading) showLoading();
        EMClient.getInstance().chatManager().asyncFetchConversationsFromServer(new EMValueCallBack<Map<String, EMConversation>>() {
            @Override
            public void onSuccess(Map<String, EMConversation> map) {
                // 获取会话成功后的处理逻辑
                ThreadManager.getInstance().runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoading();
                        callBack.onSuccess(map);
                    }
                });
            }

            @Override
            public void onError(int error, String errorMsg) {
                // 获取会话失败处理逻辑
                callBack.onError(error, errorMsg);
            }
        });
    }

    public void saveAvatar(String avatar, EMValueCallBack<String> callBack, boolean showLoading) {
        if (showLoading) showLoading();
        EMUserInfo userInfo = new EMUserInfo();
        userInfo.setUserId(EMClient.getInstance().getCurrentUser());
        userInfo.setAvatarUrl(avatar);
        EMClient.getInstance().userInfoManager().updateOwnInfo(userInfo, new EMValueCallBack<String>() {
            @Override
            public void onSuccess(String value) {
                ThreadManager.getInstance().runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoading();
                        callBack.onSuccess(value);
                    }
                });
            }

            @Override
            public void onError(int error, String errorMsg) {
                hideLoading();
                callBack.onError(error, errorMsg);
            }
        });
    }

    public void saveNickname(String nickname, EMValueCallBack<String> callBack, boolean showLoading) {
        if (showLoading) showLoading();
        EMUserInfo userInfo = new EMUserInfo();
        userInfo.setUserId(EMClient.getInstance().getCurrentUser());
        userInfo.setNickname(nickname);
        EMClient.getInstance().userInfoManager().updateOwnInfo(userInfo, new EMValueCallBack<String>() {
            @Override
            public void onSuccess(String value) {
                ThreadManager.getInstance().runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoading();
                        callBack.onSuccess(value);
                    }
                });
            }

            @Override
            public void onError(int error, String errorMsg) {
                hideLoading();
                callBack.onError(error, errorMsg);
            }
        });
    }

    public void getUserInfo(String account, EMValueCallBack<Map<String, EMUserInfo>> callBack, boolean showLoading) {
        if (showLoading) showLoading();
        String[] userId = new String[1];
        userId[0] = account;
        EMUserInfo.EMUserInfoType[] userInfoTypes = new EMUserInfo.EMUserInfoType[2];
        userInfoTypes[0] = EMUserInfo.EMUserInfoType.NICKNAME;
        userInfoTypes[1] = EMUserInfo.EMUserInfoType.AVATAR_URL;
        EMClient.getInstance().userInfoManager().fetchUserInfoByAttribute(userId, userInfoTypes, new EMValueCallBack<Map<String, EMUserInfo>>() {
            @Override
            public void onSuccess(Map<String, EMUserInfo> map) {
                ThreadManager.getInstance().runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoading();
                        callBack.onSuccess(map);
                    }
                });
            }

            @Override
            public void onError(int i, String s) {
                hideLoading();
                callBack.onError(i, s);
            }
        });
    }

    public void getUserInfo(List<String> members, EMValueCallBack<Map<String, EMUserInfo>> callBack, boolean showLoading) {
        if (showLoading) showLoading();
        String[] userIds = new String[members.size()];
        for (int i = 0; i < members.size(); i++) {
            userIds[i] = members.get(i);
        }

        EMUserInfo.EMUserInfoType[] userInfoTypes = new EMUserInfo.EMUserInfoType[2];
        userInfoTypes[0] = EMUserInfo.EMUserInfoType.NICKNAME;
        userInfoTypes[1] = EMUserInfo.EMUserInfoType.AVATAR_URL;
        EMClient.getInstance().userInfoManager().fetchUserInfoByAttribute(userIds, userInfoTypes, new EMValueCallBack<Map<String, EMUserInfo>>() {
            @Override
            public void onSuccess(Map<String, EMUserInfo> map) {
                ThreadManager.getInstance().runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoading();
                        callBack.onSuccess(map);
                    }
                });
            }

            @Override
            public void onError(int i, String s) {
                hideLoading();
                callBack.onError(i, s);
            }
        });
    }

    public void createGroup(String groupName, String[] members, EMValueCallBack<EMGroup> callBack, boolean showLoading) {
        if (showLoading) showLoading();
        EMGroupOptions option = new EMGroupOptions();
        option.maxUsers = 100;
        option.style = EMGroupManager.EMGroupStyle.EMGroupStylePublicOpenJoin;
        EMClient.getInstance().groupManager().asyncCreateGroup(groupName, "", members, "1", option, new EMValueCallBack<EMGroup>() {
            @Override
            public void onSuccess(EMGroup value) {
                ThreadManager.getInstance().runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoading();
                        callBack.onSuccess(value);
                    }
                });
            }

            @Override
            public void onError(int error, String errorMsg) {
                hideLoading();
                callBack.onError(error, errorMsg);
            }
        });
    }

    public void addUsers2Group(String groupId, String[] members, EMCallBack callBack, boolean showLoading) {
        if (showLoading) showLoading();
        EMClient.getInstance().groupManager().asyncAddUsersToGroup(groupId, members, new EMCallBack() {
            @Override
            public void onSuccess() {
                ThreadManager.getInstance().runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoading();
                        callBack.onSuccess();
                    }
                });
            }

            @Override
            public void onError(int i, String s) {
                hideLoading();
                callBack.onError(i, s);
            }
        });
    }

    public enum MEMBER_TYPE {
        ALL, HUMAN, AI
    }

    public void getGroupFromServer(String groupId, EMValueCallBack<EMGroup> callBack) {
        EMClient.getInstance().groupManager().asyncGetGroupFromServer(groupId, new EMValueCallBack<EMGroup>() {
            @Override
            public void onSuccess(EMGroup group) {
                ThreadManager.getInstance().runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onSuccess(group);
                    }
                });
            }

            @Override
            public void onError(int i, String s) {
                callBack.onError(i, s);
            }
        });
    }

    public void sortMessages(List<EMMessage> list) {
        Collections.sort(list, (m1, m2) -> (int) (m1.getMsgTime() - m2.getMsgTime()));
    }

    public void getMessages(EMConversation conversation, String startMsgId, FetchMessageListener listener) {
        EMClient.getInstance().chatManager().asyncFetchHistoryMessage(conversation.conversationId(), conversation.getType(), Constants.MSG_PAGE_SIZE, startMsgId, EMConversation.EMSearchDirection.UP,
                new EMValueCallBack<EMCursorResult<EMMessage>>() {
                    @Override
                    public void onSuccess(EMCursorResult<EMMessage> value) {
                        ThreadManager.getInstance().runOnMainThread(() -> {
                            List<EMMessage> fetchMessages = conversation.loadMoreMsgFromDB(startMsgId, Constants.MSG_PAGE_SIZE);
                            if (fetchMessages != null && !fetchMessages.isEmpty()) {
                                sortMessages(fetchMessages);
                                listener.onDone(fetchMessages, fetchMessages.get(0).getMsgId());
                                return;
                            }
                            listener.onFailed("已无更多消息..");
                        });
                    }

                    @Override
                    public void onError(int error, String errorMsg) {
                        listener.onFailed(errorMsg);
                    }
                }
        );
    }

    public void fetchGroupMembers(String groupId, MEMBER_TYPE type, EMValueCallBack<Map<String, EMUserInfo>> callBack, boolean showLoading) {
        ThreadManager.getInstance().runOnIOThread(() -> {
            try {
                EMGroup group = EMClient.getInstance().groupManager().getGroupFromServer(groupId, true);
                List<String> members = new ArrayList<>();
                members.add(group.getOwner());
                members.addAll(group.getMembers());
                if (type == MEMBER_TYPE.ALL) {

                } else if (type == MEMBER_TYPE.AI) {
                    members = members.stream().filter(member -> member.startsWith("bot")).collect(Collectors.toList());
                } else if (type == MEMBER_TYPE.HUMAN) {
                    members = members.stream().filter(member -> !member.startsWith("bot")).collect(Collectors.toList());
                }

                getUserInfo(members, callBack, showLoading);
            } catch (HyphenateException e) {
                callBack.onError(e.getErrorCode(), e.getDescription());
                throw new RuntimeException(e);
            }

        });
    }

    public void join2Group(String groupId, EMCallBack callBack, boolean showLoading) {
        if (showLoading) showLoading();
        EMClient.getInstance().groupManager().asyncJoinGroup(groupId, new EMCallBack() {
            @Override
            public void onSuccess() {
                ThreadManager.getInstance().runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoading();
                        callBack.onSuccess();
                    }
                });
            }

            @Override
            public void onError(int i, String s) {
                hideLoading();
                callBack.onError(i, s);
            }
        });

    }

    public void changeGroupName(String groupId, String name, EMCallBack callBack, boolean showLoading) {
        if (showLoading) showLoading();
        EMClient.getInstance().groupManager().asyncChangeGroupName(groupId, name, new EMCallBack() {
            @Override
            public void onSuccess() {
                ThreadManager.getInstance().runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoading();
                        callBack.onSuccess();
                    }
                });
            }

            @Override
            public void onError(int i, String s) {
                hideLoading();
                callBack.onError(i, s);
            }
        });

    }

    public void deleteOrLeaveGroup(boolean isDelete, String groupId, EMCallBack callBack, boolean showLoading) {
        if (showLoading) showLoading();
        if (isDelete) {
            EMClient.getInstance().groupManager().asyncDestroyGroup(groupId, new EMCallBack() {
                @Override
                public void onSuccess() {
                    ThreadManager.getInstance().runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            hideLoading();
                            callBack.onSuccess();
                        }
                    });
                }

                @Override
                public void onError(int i, String s) {
                    hideLoading();
                    callBack.onError(i, s);
                }
            });
            return;
        }
        leaveGroup(groupId, callBack, showLoading);
    }

    public void deleteGroupMembers(String groupId, List<String> users, EMCallBack callBack, boolean showLoading) {
        if (showLoading) showLoading();
        EMClient.getInstance().groupManager().asyncRemoveUsersFromGroup(groupId, users, new EMCallBack() {
            @Override
            public void onSuccess() {
                ThreadManager.getInstance().runOnMainThread(() -> {
                    hideLoading();
                    callBack.onSuccess();
                });
            }

            @Override
            public void onError(int i, String s) {
                hideLoading();
                callBack.onError(i, s);
            }
        });
    }

    private void leaveGroup(String groupId, EMCallBack callBack, boolean showLoading) {
        if (showLoading) showLoading();
        EMClient.getInstance().groupManager().asyncLeaveGroup(groupId, new EMCallBack() {
            @Override
            public void onSuccess() {
                ThreadManager.getInstance().runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoading();
                        callBack.onSuccess();
                    }
                });
            }

            @Override
            public void onError(int i, String s) {
                hideLoading();
                callBack.onError(i, s);
            }
        });

    }

    public void logout(EMCallBack callBack, boolean showLoading) {
        if (showLoading) showLoading();
        EMClient.getInstance().logout(false, new EMCallBack() {
            @Override
            public void onSuccess() {
                ThreadManager.getInstance().runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoading();
                        callBack.onSuccess();
                    }
                });
            }

            @Override
            public void onError(int i, String s) {
                hideLoading();
                callBack.onError(i, s);
            }
        });
    }
}
