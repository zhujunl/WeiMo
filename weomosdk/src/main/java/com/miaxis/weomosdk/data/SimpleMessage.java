package com.miaxis.weomosdk.data;

import com.miaxis.weomosdk.constant.Constant;
import com.miaxis.weomosdk.message.sender.CallbackSender;

/**
 * @ClassName: SimpleCallback
 * @Author: cheng.peng
 * @Date: 2022/5/26 18:25
 * 心跳 注册回调
 * 这里的callback 指的是设备向协议层发送信息。
 */
public  class SimpleMessage<T> extends CallbackSender {
    public String cmd;
    private String clientId;
    private T data;

    public SimpleMessage() {
        clientId= Constant.CLIENT_ID;
    }


    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
