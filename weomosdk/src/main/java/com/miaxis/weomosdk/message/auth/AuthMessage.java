package com.miaxis.weomosdk.message.auth;


import com.miaxis.weomosdk.message.AbsMessage;

/**
 * @ClassName: AuthMessage
 * @Author: cheng.peng
 * @Date: 2022/5/24 18:08
 */
public class AuthMessage<T>  extends AbsMessage {
    private String clientId;
    private String cmd;
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }
}
