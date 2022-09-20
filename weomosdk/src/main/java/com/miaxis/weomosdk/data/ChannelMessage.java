package com.miaxis.weomosdk.data;

import android.util.Log;

import com.miaxis.weomosdk.constant.Constant;
import com.miaxis.weomosdk.utils.SignUtils;


/**
 * @ClassName: ChannelCallBack
 * @Author: cheng.peng
 * @Date: 2022/5/26 18:29
 * 通道信息--
 * 要添加dc,签名，时间戳的消息
 * 可以用泛型+注解的模式优化数据的构造.
 */
public class ChannelMessage<T>  extends SimpleMessage<T> {
    private String dc;
    private String signature;
    private Long timestamp;

    //@RequiresApi(api = Build.VERSION_CODES.O)
    public ChannelMessage() {

    }


    public String getDc() {
        return dc;
    }

    public void setDc(String dc) {
        this.dc = dc;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }


    public void setCmd(String cmd) {
        this.cmd = cmd;
        updateChannelMessage();
    }

    public void updateChannelMessage(){
        dc="1:"+ Constant.DEVICE_CHANNEL;
        timestamp=System.currentTimeMillis();
        Log.d("Temp ","AppKey "+Constant.APP_KEY+"app sercert "+Constant.APP_SECRET);
        Log.d("Temp ","getCmd  "+getCmd());
        signature= SignUtils.tcpSignature(cmd,Constant.CLIENT_ID,timestamp,Constant.APP_SECRET,Constant.APP_KEY);
    }




}
