package com.yehui.easemob.bean;

import com.easemob.chat.EMMessage;

/**
 * Created by Luhao on 2016/3/8.
 */
public class SetMessageBean {
    private int setMsgCode;
    private EMMessage emMessage;

    public int getSetMsgCode() {
        return setMsgCode;
    }

    public void setSetMsgCode(int setMsgCode) {
        this.setMsgCode = setMsgCode;
    }

    public EMMessage getEmMessage() {
        return emMessage;
    }

    public void setEmMessage(EMMessage emMessage) {
        this.emMessage = emMessage;
    }
}
