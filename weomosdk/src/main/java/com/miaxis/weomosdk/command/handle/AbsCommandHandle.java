package com.miaxis.weomosdk.command.handle;

import android.util.Log;

import com.easysocket.EasySocket;
import com.miaxis.weomosdk.constant.Command;
import com.miaxis.weomosdk.constant.Constant;
import com.miaxis.weomosdk.data.ActionMessage;
import com.miaxis.weomosdk.data.ChannelMessage;
import com.miaxis.weomosdk.utils.SignUtils;

/**
 * @ClassName: AbsCommandHandle
 * @Author: cheng.peng
 * @Date: 2022/5/23 18:08
 */
public abstract class AbsCommandHandle<T> implements IIntentHandle<T>{

    public  String getKey(){
        return  getCommand()+getDomainId()+getActionId();
    }

    public abstract String getCommand();

    public abstract String getDomainId();

    public abstract String getActionId();

    public abstract T getData();

    /**
     * 设备通用回调,无action;
     */
   // @RequiresApi(api = Build.VERSION_CODES.O)
    public void onCommonCallback(){
        ChannelMessage<ActionMessage<T>> channelMessage=new ChannelMessage<>();
        channelMessage.setCmd(Command.PUSH_DATA_BACK);
        channelMessage.setClientId(Constant.CLIENT_ID);
        channelMessage.setDc("1:"+Constant.DEVICE_CHANNEL);
        long timeStamp=System.currentTimeMillis();
        Log.d("soc","appKey "+Constant.APP_KEY+" appSecret "+Constant.APP_SECRET+"clientId "+Constant.CLIENT_ID);
        String signature= SignUtils.tcpSignature(Command.PUSH_DATA_BACK,Constant.CLIENT_ID,timeStamp,Constant.APP_SECRET,Constant.APP_KEY);
        Log.d("soc", " signature "+signature);
        channelMessage.setSignature(signature);
        channelMessage.setTimestamp(timeStamp);

        ActionMessage<T> data=new ActionMessage<>();
        data.setClientId(Constant.CLIENT_ID);
        data.setActionId(getActionId());
        data.setDomainId(getDomainId());
        data.setValue(0);
        data.setReason("no reason");
        data.setDeviceChannelNumber(Constant.DEVICE_CHANNEL);
        data.setData(getData());
        channelMessage.setData(data);
        EasySocket.getInstance().upMessage(channelMessage.pack());
    }







}
