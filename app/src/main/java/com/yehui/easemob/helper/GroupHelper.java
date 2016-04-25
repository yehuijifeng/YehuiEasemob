package com.yehui.easemob.helper;

import com.easemob.EMValueCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupInfo;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.GroupChangeListener;
import com.easemob.exceptions.EaseMobException;
import com.yehui.easemob.bean.GroupBean;
import com.yehui.easemob.contants.GroupContant;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Luhao on 2016/4/25.
 * 群聊帮助类
 */
public class GroupHelper {
    private static GroupHelper groupHelper;

    private GroupHelper() {
        eventBus = EventBus.getDefault();
    }

    public GroupHelper getInstance() {
        if (groupHelper == null) {
            synchronized (GroupHelper.class) {
                if (groupHelper == null) {
                    groupHelper = new GroupHelper();
                }
            }
        }
        return groupHelper;
    }

    private Thread thread;
    private EventBus eventBus;
    private GroupBean groupBean;

    private void initGroupHelper() {
        groupBean = new GroupBean();
    }

    /**
     * 在服务器端创建一个私有群.
     * groupName：要创建的群聊的名称
     * desc：群聊简介
     * members：群聊成员,用户名;为空时这个创建的群组只包含自己
     * isAllowInvite:是否允许群成员邀请人进群
     * maxUsers:创建群的数量
     * isPublic:是否是公开群；公开群：公开群可以被用户搜索到，并且可以直接加入或者申请加入；私有群：私有群，不能被搜索到，只能通过群主加人进群，或者设置了allowInvite为true，即允许群成员邀 请，那么群成员也可以邀请群外面的人进入群聊，此种群群成员可以邀请，不能踢人，类似微信群。
     * idNeedApprovalRequired:是否需要群验证
     */
    public void createGroup(final String groupName, final String desc, final String[] members, final boolean isAllowInvite, final int maxUsers, final boolean isPublic, final boolean idNeedApprovalRequired) {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMGroup emGroup;
                    int maxUser = maxUsers;
                    if (maxUsers <= 0) maxUser = 200;
                    if (maxUsers > 2000) maxUser = 2000;
                    //传入maxUsers后设置自定义的最大用户数，最大为2000
                    if (isPublic)
                        emGroup = EMGroupManager.getInstance().createPublicGroup(groupName, desc, members, idNeedApprovalRequired, maxUser);//需异步处理
                    else
                        emGroup = EMGroupManager.getInstance().createPrivateGroup(groupName, desc, members, isAllowInvite, maxUser);//需异步处理
                    eventBus.post(emGroup);
                } catch (EaseMobException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public void createGroup(String groupName, String desc, int maxUsers, boolean isPublic) {
        createGroup(groupName, desc, null, false, maxUsers, isPublic, false);
    }

    public void createGroup(String groupName, String desc, boolean isPublic) {
        createGroup(groupName, desc, null, false, 500, isPublic, false);
    }

    public void createGroup(String groupName, String desc, String[] members, int maxUsers, boolean isPublic) {
        createGroup(groupName, desc, members, false, maxUsers, isPublic, false);
    }

    /**
     * 群聊加人
     *
     * @param groupId      发出邀请的群id
     * @param newmembers   邀请的人员id数组
     * @param isMembersAdd 是否设置了群成员可以邀请人
     */
    public void addUsersToGroup(final String groupId, final String[] newmembers, final boolean isMembersAdd) {

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (!isMembersAdd)
                        //群主加人调用此方法
                        EMGroupManager.getInstance().addUsersToGroup(groupId, newmembers);//需异步处理
                    else
                        //私有群里，如果开放了群成员邀请，群成员邀请调用下面方法
                        EMGroupManager.getInstance().inviteUser(groupId, newmembers, null);//需异步处理
                    initGroupHelper();
                    groupBean.setGroupId(groupId);
                    groupBean.setNewmembers(newmembers);
                    eventBus.post(groupBean);
                } catch (EaseMobException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public void addUsersToGroup(String groupId, String[] newmembers) {
        addUsersToGroup(groupId, newmembers, false);
    }

    /**
     * 群聊加人
     *
     * @param groupId  发出邀请的群id
     * @param userName 踢出的人员id
     */
    public void removeUserFromGroup(final String groupId, final String userName) {

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMGroupManager.getInstance().removeUserFromGroup(groupId, userName);//需异步处理
                    initGroupHelper();
                    groupBean.setUserName(userName);
                    eventBus.post(groupBean);
                } catch (EaseMobException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    /**
     * 加入某个群聊,只能用于加入公开群
     *
     * @param groupId       群id
     * @param requestStr    加入语
     * @param isMembersOnly 目标群是否是不需要申请的
     */
    public void applyJoinToGroup(final String groupId, final String requestStr, final boolean isMembersOnly) {

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (isMembersOnly)
                        //如果群开群是自由加入的，即group.isMembersOnly()为false，直接join
                        EMGroupManager.getInstance().joinGroup(groupId);//需异步处理
                    else
                        //需要申请和验证才能加入的，即group.isMembersOnly()为true，调用下面方法
                        EMGroupManager.getInstance().applyJoinToGroup(groupId, requestStr);//需异步处理  initGroupHelper();
                    initGroupHelper();
                    groupBean.setGroupId(groupId);
                    groupBean.setRequestStr(requestStr);
                    eventBus.post(groupBean);
                } catch (EaseMobException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public void applyJoinToGroup(String groupId, String requestStr) {
        applyJoinToGroup(groupId, requestStr, false);
    }

    public void applyJoinToGroup(String groupId) {
        applyJoinToGroup(groupId, "求加入", false);
    }

    /**
     * 退出群聊
     *
     * @param groupId
     */
    public void exitFromGroup(final String groupId) {

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMGroupManager.getInstance().exitFromGroup(groupId);//需异步处理
                    initGroupHelper();
                    groupBean.setGroupId(groupId);
                    eventBus.post(groupBean);
                } catch (EaseMobException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    /**
     * 解散群聊
     *
     * @param groupId
     */
    public void exitAndDeleteGroup(final String groupId) {

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMGroupManager.getInstance().exitAndDeleteGroup(groupId);//需异步处理
                    initGroupHelper();
                    groupBean.setGroupId(groupId);
                    eventBus.post(groupBean);
                } catch (EaseMobException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    /**
     * 获取群聊列表
     * 从服务器获取自己加入的和创建的群聊列表（两种方式），此api获取的群组sdk会自动保存到内存和db。
     * 注意，获取到的列表里的群聊只有groupname和groupid等简单配置信息
     *
     * @return
     */
    public void getGroupsFromServer() {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                List<EMGroup> grouplist = null;//需异步处理
                try {
                    grouplist = EMGroupManager.getInstance().getGroupsFromServer();
                    eventBus.post(grouplist);
                } catch (EaseMobException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public void asyncGetGroupsFromServer(final EMValueCallBack emValueCallBack) {
        EMGroupManager.getInstance().asyncGetGroupsFromServer(new EMValueCallBack<List<EMGroup>>() {

            @Override
            public void onSuccess(List<EMGroup> value) {
                // TODO Auto-generated method stub
                emValueCallBack.onSuccess(value);
            }

            @Override
            public void onError(int error, String errorMsg) {
                // TODO Auto-generated method stub
                emValueCallBack.onError(error, errorMsg);
            }
        });
    }

    /**
     * 从本地加载群聊列表
     */
    public void getAllGroups() {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                //从本地加载群聊列表
                List<EMGroup> grouplist = EMGroupManager.getInstance().getAllGroups();
                eventBus.post(grouplist);
            }
        });
        thread.start();
    }

    /**
     * 获取所有公开群列表（两种方式）
     */
    public void getAllPublicGroupsFromServer() {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<EMGroupInfo> groupsList = EMGroupManager.getInstance().getAllPublicGroupsFromServer();//需异步处理
                    eventBus.post(groupsList);
                } catch (EaseMobException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public void asyncGetAllPublicGroupsFromServer(final EMValueCallBack emValueCallBack) {
        EMGroupManager.getInstance().asyncGetAllPublicGroupsFromServer(new EMValueCallBack<List<EMGroupInfo>>() {

            @Override
            public void onSuccess(List<EMGroupInfo> value) {
                // TODO Auto-generated method stub
                emValueCallBack.onSuccess(value);
            }

            @Override
            public void onError(int error, String errorMsg) {
                // TODO Auto-generated method stub
                emValueCallBack.onError(error, errorMsg);

            }
        });
    }

    /**
     * @param groupId          需要改变名称的群组的id
     * @param changedGroupName 改变后的群组名称
     */
    public void getAllPublicGroupsFromServer(final String groupId, final String changedGroupName) {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMGroupManager.getInstance().changeGroupName(groupId, changedGroupName);//需异步处理
                } catch (EaseMobException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    /**
     * 获取单个群聊信息
     *
     * @param groupId EMGroup参数说明
     *                方法和说明
     *                int	getAffiliationsCount()；获取群组成员人数
     *                java.lang.String	getDescription()
     *                java.lang.String	getGroupId()获取群ID
     *                java.lang.String	getGroupName()获取群名称
     *                java.lang.String	getId()
     *                long	getLastModifiedTime()
     *                int	getMaxUsers()获取群组的最大人数限制
     *                java.util.List<java.lang.String>	getMembers()
     *                boolean	getMsgBlocked()已过时。使用isMsgBlocked
     *                java.lang.String	getName()
     *                java.lang.String	getOwner()
     *                boolean	isAllowInvites()
     *                boolean	isMembersOnly()
     *                boolean	isMsgBlocked()获取群消息是否被屏蔽
     *                boolean	isPublic()
     *                void	removeMember(java.lang.String username)
     *                void	setAffiliationsCount(int affCount)
     *                void	setDescription(java.lang.String description)
     *                void	setGroupId(java.lang.String groupId)设置群ID
     *                void	setGroupName(java.lang.String groupName)设置群名称
     *                void	setId(java.lang.String id)
     *                void	setIsPublic(boolean isPublic)已过时。use setPublic(boolean isPublic)
     *                void	setLastModifiedTime(long lastModifiedTime)
     *                void	setMaxUsers(int maxUsers)设置群组的最大的人数的限制
     *                void	setMembers(java.util.List<java.lang.String> userNames)
     *                void	setMsgBlocked(boolean isMsgBlocked)
     *                void	setName(java.lang.String name)
     *                void	setOwner(java.lang.String owner)
     *                void	setPublic(boolean isPublic)
     */
    public void getAllPublicGroupsFromServer(final String groupId) {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //根据群聊ID从本地获取群聊基本信息
                    //EMGroup group = EMGroupManager.getInstance().getGroup(groupId);
                    //根据群聊ID从服务器获取群聊基本信息
                    EMGroup group = EMGroupManager.getInstance().getGroupFromServer(groupId);
                    //保存获取下来的群聊基本信息
                    EMGroupManager.getInstance().createOrUpdateLocalGroup(group);
                    eventBus.post(group);
                } catch (EaseMobException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    /**
     * 屏蔽群消息后，就不能接收到此群的消息 （群创建者不能屏蔽群消息）（还是群里面的成员，但不再接收消息）
     *
     * @param groupId， 群id
     */
    public void blockGroupMessage(final String groupId) {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMGroupManager.getInstance().blockGroupMessage(groupId);//需异步处理
                } catch (EaseMobException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    /**
     * 取消屏蔽群消息,就可以正常收到群的所有消息
     *
     * @param groupId
     */
    public void unblockGroupMessage(final String groupId) {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMGroupManager.getInstance().unblockGroupMessage(groupId);//需异步处理
                } catch (EaseMobException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    /**
     * 群聊不提醒只显示数目
     *
     * @param groupIds
     */
    public void setReceiveNotNoifyGroup(final List<String> groupIds) {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                //如果群聊只是想提示数目，不响铃。可以通过此属性设置，此属性是本地属性
                EMChatManager.getInstance().getChatOptions().setReceiveNotNoifyGroup(groupIds);
            }
        });
        thread.start();
    }

    /**
     * 将用户加到群组的黑名单，被加入黑名单的用户无法加入群，无法收发此群的消息
     * （只有群主才能设置群的黑名单）
     *
     * @param groupId,  群组的id
     * @param username, 待屏蔽的用户名
     * @throws EaseMobException 出错会抛出
     */
    public void blockUser(final String groupId, final String username) {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMGroupManager.getInstance().blockUser(groupId, username);//需异步处理
                } catch (EaseMobException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    /**
     * 将用户从群组的黑名单移除（只有群主才能调用此函数）
     *
     * @param groupId,  群组的id
     * @param username, 待解除屏蔽的 用户名
     */
    public void unblockUser(final String groupId, final String username) {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMGroupManager.getInstance().unblockUser(groupId, username);//需异步处理
                } catch (EaseMobException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    /**
     * 获取群组的黑名单用户列表
     * （只有群主才能调用此函数）
     *
     * @return List<String>
     * @throws EaseMobException 获取失败
     */
    public void getBlockedUsers(final String groupId) {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<String> blockedList = EMGroupManager.getInstance().getBlockedUsers(groupId);//需异步处理
                    eventBus.post(blockedList);
                } catch (EaseMobException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    /**
     * 群聊事件监听
     */
    public void addGroupChangeListener() {
        initGroupHelper();
        EMGroupManager.getInstance().addGroupChangeListener(new GroupChangeListener() {
            @Override
            public void onUserRemoved(String groupId, String groupName) {
                //当前用户被管理员移除出群聊
                groupBean.setGroupId(groupId);
                groupBean.setGroupName(groupName);
                groupBean.setGroupStatus(GroupContant.GROUP_REMOVE_USER);
                eventBus.post(groupBean);
            }

            @Override
            public void onInvitationReceived(String groupId, String groupName, String inviter, String reason) {
                //收到加入群聊的邀请
                groupBean.setGroupId(groupId);
                groupBean.setGroupName(groupName);
                groupBean.setInviter(inviter);
                groupBean.setRequestStr(reason);
                groupBean.setGroupStatus(GroupContant.GROUP_RECEIVE_ADD);
                eventBus.post(groupBean);
            }

            @Override
            public void onInvitationDeclined(String groupId, String invitee, String reason) {
                //群聊邀请被拒绝
                groupBean.setGroupId(groupId);
                groupBean.setInviter(invitee);
                groupBean.setRequestStr(reason);
                groupBean.setGroupStatus(GroupContant.GROUP_RECEIVE_ADD_NO);
                eventBus.post(groupBean);
            }

            @Override
            public void onInvitationAccpted(String groupId, String inviter, String reason) {
                //群聊邀请被接受
                groupBean.setGroupId(groupId);
                groupBean.setInviter(inviter);
                groupBean.setRequestStr(reason);
                groupBean.setGroupStatus(GroupContant.GROUP_RECEIVE_ADD_YES);
                eventBus.post(groupBean);
            }

            @Override
            public void onGroupDestroy(String groupId, String groupName) {
                //群聊被创建者解散
                groupBean.setGroupId(groupId);
                groupBean.setGroupName(groupName);
                groupBean.setGroupStatus(GroupContant.GROUP_DELETE);
                eventBus.post(groupBean);
            }

            @Override
            public void onApplicationReceived(String groupId, String groupName, String applyer, String reason) {
                //收到加群申请
                groupBean.setGroupId(groupId);
                groupBean.setGroupName(groupName);
                groupBean.setInviter(applyer);//申请人的username
                groupBean.setRequestStr(reason);
                groupBean.setGroupStatus(GroupContant.GROUP_REMOVE_USER_SET);
                eventBus.post(groupBean);
            }

            @Override
            public void onApplicationAccept(String groupId, String groupName, String accepter) {
                //加群申请被同意
                groupBean.setGroupId(groupId);
                groupBean.setGroupName(groupName);
                groupBean.setInviter(accepter);//同意人得username
                groupBean.setGroupStatus(GroupContant.GROUP_REMOVE_USER_SET_YES);
                eventBus.post(groupBean);
            }

            @Override
            public void onApplicationDeclined(String groupId, String groupName, String decliner, String reason) {
                // 加群申请被拒绝
                groupBean.setGroupId(groupId);
                groupBean.setGroupName(groupName);
                groupBean.setInviter(decliner);//拒绝人得username
                groupBean.setRequestStr(reason);
                groupBean.setGroupStatus(GroupContant.GROUP_REMOVE_USER_SET_NO);
                eventBus.post(groupBean);
            }
        });
    }
}
