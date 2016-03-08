package com.yehui.easemob.helper;


import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactListener;
import com.easemob.chat.EMContactManager;
import com.easemob.exceptions.EaseMobException;
import com.yehui.easemob.bean.FriendBean;
import com.yehui.easemob.contants.EasemobContant;
import com.yehui.easemob.interfaces.FriendStatusInterfaces;
import com.yehui.utils.utils.EmptyUtil;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Luhao on 2016/3/4.
 * 好友体系状态监听帮助类
 */
public class FriendStatushelper implements FriendStatusInterfaces {

    private static volatile FriendStatushelper instance;

    private FriendStatushelper() {
        eventBus = EventBus.getDefault();
        friendBean = new FriendBean();
    }

    public static synchronized FriendStatushelper getInstance() {
        if (instance == null) {
            synchronized (FriendStatushelper.class) {
                if (instance == null) {
                    instance = new FriendStatushelper();
                }
            }
        }
        return instance;
    }

    private Thread thread;
    private static EventBus eventBus;
    private FriendBean friendBean;

    private void initStatus() {
        friendBean = new FriendBean();
    }

//    public void colseThread() {
//        if (thread != null)
//            thread.interrupt();
//    }

    /**
     * 初始化好友监听
     */
    public void getFriendStatus() {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                EMContactManager.getInstance().setContactListener(new EMContactListener() {
                    @Override
                    public void onContactAgreed(String username) {
                        //好友请求被同意
                        onContactAgreeds(username);
                    }

                    @Override
                    public void onContactRefused(String username) {
                        //好友请求被拒绝
                        onContactRefuseds(username);
                    }

                    @Override
                    public void onContactInvited(String username, String reason) {
                        //收到好友邀请
                        onContactInviteds(username, reason);
                    }

                    @Override
                    public void onContactDeleted(List<String> usernameList) {
                        //被删除时回调此方法
                        onContactDeleteds(usernameList);
                    }

                    @Override
                    public void onContactAdded(List<String> usernameList) {
                        //增加了联系人时回调此方法
                        onContactAddeds(usernameList);
                    }
                });
            }
        });
        thread.start();
    }

    /**
     * 获取好友的username list，开发者需要根据username去自己服务器获取好友的详情
     * 如果使用环信的好友体系需要先设置 EMChatManager.getInstance().getChatOptions().setUseRoster(true)
     *
     * @status 获取好友的状态，1：正常；2，刷新；3，重载
     */
    @Override
    public void getFriendsLists(final int status) {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                initStatus();
                friendBean.setStatusMsg(status + "");
                friendBean.setStatusCode(EasemobContant.getFriendList);
                try {
                    friendBean.setFriendNameist(EMContactManager.getInstance().getContactUserNames());
                    friendBean.setOperation(true);
                } catch (EaseMobException e) {
                    friendBean.setOperation(false);
                } finally {
                    eventBus.post(friendBean);
                }
            }
        });
        thread.start();
    }

    /**
     * 添加好友
     *
     * @param toAddUsername 添加的好友的username
     * @param reason        添加理由
     */
    @Override
    public void addFriends(final String toAddUsername, final String reason) {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                initStatus();
                friendBean.setStatusCode(EasemobContant.addContact);
                try {
                    EMContactManager.getInstance().addContact(toAddUsername, reason);//需异步处理
                    friendBean.setOperation(true);
                    friendBean.setStatusMsg("你添加 " + toAddUsername + "为好友的请求已经发出，请等待对方同意");
                } catch (EaseMobException e) {
                    friendBean.setOperation(false);
                    friendBean.setStatusMsg("添加好友失败！");
                } finally {
                    eventBus.post(friendBean);
                }
            }
        });
        thread.start();
    }

    /**
     * 删除好友
     *
     * @param username 添加的好友的username
     */
    @Override
    public void deleteFriends(final String username) {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                initStatus();
                friendBean.setStatusCode(EasemobContant.deleteContact);
                try {
                    EMContactManager.getInstance().deleteContact(username);//需异步处理
                    friendBean.setOperation(true);
                    friendBean.setStatusMsg("你成功将" + username + "从你的好友列表中删除");
                } catch (EaseMobException e) {
                    friendBean.setOperation(false);
                    friendBean.setStatusMsg("删除好友失败！");
                } finally {
                    eventBus.post(friendBean);
                }
            }
        });
        thread.start();
    }

    /**
     * 同意好友请求
     *
     * @param username 添加的好友的username
     */
    @Override
    public void acceptFriendRequests(final String username) {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                initStatus();
                friendBean.setStatusCode(EasemobContant.acceptFriendRequest);
                try {
                    EMChatManager.getInstance().acceptInvitation(username);//需异步处理
                    friendBean.setOperation(true);
                    friendBean.setStatusMsg("你同意了" + username + "的好友请求");
                } catch (EaseMobException e) {
                    friendBean.setOperation(false);
                    friendBean.setStatusMsg("同意好友请求失败！");
                } finally {
                    eventBus.post(friendBean);
                }
            }
        });
        thread.start();
    }

    /**
     * 拒绝好友请求
     *
     * @param username 添加的好友的username
     */
    @Override
    public void refuseFriendRequests(final String username) {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                initStatus();
                friendBean.setStatusCode(EasemobContant.refuseInvitation);
                try {
                    EMChatManager.getInstance().refuseInvitation(username);//需异步处理
                    friendBean.setOperation(true);
                    friendBean.setStatusMsg("你拒绝了" + username + "的好友请求");
                } catch (EaseMobException e) {
                    friendBean.setOperation(false);
                    friendBean.setStatusMsg("拒绝好友请求失败！");
                } finally {
                    eventBus.post(friendBean);
                }
            }
        });
        thread.start();
    }

    /**
     * 获取黑名单列表
     * 从本地获取黑名单中的用户的usernames
     */
    @Override
    public void getBlackListUserNames() {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                initStatus();
                friendBean.setStatusCode(EasemobContant.getBlackListUsernames);
                try {
                    EMContactManager.getInstance().getBlackListUsernames();
                    friendBean.setOperation(true);
                    friendBean.setStatusMsg("获取黑名单成功");
                } catch (Exception e) {
                    friendBean.setOperation(false);
                    friendBean.setStatusMsg("获取黑名单失败！");
                } finally {
                    eventBus.post(friendBean);
                }
            }
        });
        thread.start();
    }

    /**
     * 把用户加入到黑名单
     * 从本地获取黑名单中的用户的usernames
     * 第二个参数如果为true，则把用户加入到黑名单后双方发消息时对方都收不到；false,则
     * 我能给黑名单的中用户发消息，但是对方发给我时我是收不到的
     */
    @Override
    public void addUserToBlackLists(final String username, final boolean isSendMessage) {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                initStatus();
                friendBean.setStatusCode(EasemobContant.addUserToBlackList);
                try {
                    EMContactManager.getInstance().addUserToBlackList(username, isSendMessage);//需异步处理
                    friendBean.setOperation(true);
                    friendBean.setStatusMsg("将" + username + "加入黑名单成功");
                } catch (EaseMobException e) {
                    friendBean.setOperation(false);
                    friendBean.setStatusMsg("添加黑名单失败！");
                } finally {
                    eventBus.post(friendBean);
                }
            }
        });
        thread.start();
    }

    /**
     * 把用户从黑名单中移除
     * 从本地获取黑名单中的用户的usernames
     * 第二个参数如果为true，则把用户加入到黑名单后双方发消息时对方都收不到；false,则
     * 我能给黑名单的中用户发消息，但是对方发给我时我是收不到的
     */
    @Override
    public void deleteUserFromBlackLists(final String username) {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                initStatus();
                friendBean.setStatusCode(EasemobContant.deleteUserFromBlackList);
                try {
                    EMContactManager.getInstance().deleteUserFromBlackList(username);//需异步处理
                    friendBean.setOperation(true);
                    friendBean.setStatusMsg("将" + username + "移除黑名单成功");
                } catch (EaseMobException e) {
                    friendBean.setOperation(false);
                    friendBean.setStatusMsg("移除黑名单失败！");
                } finally {
                    eventBus.post(friendBean);
                }
            }
        });
        thread.start();
    }

    @Override
    public void onContactAgreeds(String username) {
        //好友请求被同意
        initStatus();
        friendBean.setStatusCode(EasemobContant.onContactAgreed);
        friendBean.setFriendName(username);
        friendBean.setStatusMsg(username + "同意了你的好友请求");
        eventBus.post(friendBean);
    }

    @Override
    public void onContactRefuseds(String username) {
        //好友请求被拒绝
        initStatus();
        friendBean.setStatusCode(EasemobContant.onContactRefused);
        friendBean.setFriendName(username);
        friendBean.setStatusMsg(username + "拒绝了你的好友请求");
        eventBus.post(friendBean);
    }

    @Override
    public void onContactInviteds(String username, String reason) {
        //收到好友邀请
        initStatus();
        friendBean.setStatusCode(EasemobContant.onContactInvited);
        friendBean.setFriendName(username);
        friendBean.setStatusMsg("\'" + username + "\'   请求添加你为好友\n申请原因：" + reason);
        eventBus.post(friendBean);
    }

    @Override
    public void onContactDeleteds(List<String> usernameList) {
        //被删除时回调此方法
        if(friendBean.getStatusCode()==EasemobContant.onContactRefused)return;
        initStatus();
        friendBean.setStatusCode(EasemobContant.onContactDeleted);
        friendBean.setFriendNameist(usernameList);
        String username = "";
        for (int i = 0; i < usernameList.size(); i++) {
            if (!EmptyUtil.isStringEmpty(usernameList.get(i))) {
                if (usernameList.size() == 1)
                    username += usernameList.get(i);
                else
                    username += usernameList.get(i) + ",";
            }
        }
        friendBean.setStatusMsg(username + "将你从好友列表删除");
        eventBus.post(friendBean);
    }


    @Override
    public void onContactAddeds(List<String> usernameList) {
        //增加了联系人时回调此方法
        if(friendBean.getStatusCode()==EasemobContant.onContactAgreed)return;
        initStatus();
        friendBean.setStatusCode(EasemobContant.onContactAdded);
        friendBean.setFriendNameist(usernameList);
        String username = "";
        for (int i = 0; i < usernameList.size(); i++) {
            if (!EmptyUtil.isStringEmpty(usernameList.get(i))) {
                if (usernameList.size() == 1)
                    username += usernameList.get(i);
                else
                    username += usernameList.get(i) + ",";
            }
        }
        friendBean.setStatusMsg(username + "将你添加到了好友列表");
        eventBus.post(friendBean);
    }
}
