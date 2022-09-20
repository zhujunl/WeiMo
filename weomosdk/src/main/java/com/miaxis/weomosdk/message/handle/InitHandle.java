package com.miaxis.weomosdk.message.handle;

import android.util.Log;

import com.easysocket.EasySocket;
import com.miaxis.weomosdk.command.handle.SimpleCommandHandle;
import com.miaxis.weomosdk.constant.Command;
import com.miaxis.weomosdk.constant.Constant;
import com.miaxis.weomosdk.data.ActionMessage;
import com.miaxis.weomosdk.data.ChannelMessage;
import com.miaxis.weomosdk.entity.AuthEntity;
import com.miaxis.weomosdk.entity.InitEntity;
import com.miaxis.weomosdk.utils.GsonUtils;
import com.miaxis.weomosdk.utils.MmkvHelper;
import com.miaxis.weomosdk.utils.SignUtils;

/**
 * @ClassName: InitHandle
 * @Author: cheng.peng
 * @Date: 2022/5/25 9:58
 * 平台下发设备初始化信息
 */
public class InitHandle extends SimpleCommandHandle {
    private String TAG=InitHandle.class.getSimpleName();

    @Override
    public String getCommand() {
        return "PUSH_DATA";
    }

    @Override
    public String getDomainId() {
        return "3000";
    }

    @Override
    public String getActionId() {
        return "3005";
    }

    @Override
    public Object getData() {
        return null;
    }

    @Override
    public boolean onCustomCallBack() {
        onInitCallBack();
        return false;
    }

    @Override
    public void intentHandle(String clientId, String data) {
        Log.d(TAG,"init handle");
        InitEntity entity= GsonUtils.GsonToBean(data,InitEntity.class);
        //   MmkvHelper.getInstance().putObject("appKey",);
        if(entity.getChannelList()!=null&&entity.getChannelList().size()>0){ //获取到多个通道
            for(int i=0;i<entity.getChannelList().size();i++){
                Constant.APP_KEY=entity.getChannelList().get(i).getAppKey();
                Constant.APP_SECRET=entity.getChannelList().get(i).getAppSecret();
                Constant.DEVICE_CHANNEL=entity.getChannelList().get(i).getChannelNumber();
                MmkvHelper.getInstance().putObject("appKey",entity.getChannelList().get(i).getAppKey());
                MmkvHelper.getInstance().putObject("appSecret",entity.getChannelList().get(i).getAppSecret());
                MmkvHelper.getInstance().putObject("channelNumber",entity.getChannelList().get(i).getChannelNumber());
            }
        }
        Constant.DEVICE_NUMBER=entity.getDeviceNumber();
        MmkvHelper.getInstance().putObject("deviceNumber",entity.getDeviceNumber());



    }


    private void onInitCallBack(){
        ChannelMessage<ActionMessage<AuthEntity>> channelMessage=new ChannelMessage<>();
        channelMessage.setCmd(Command.PUSH_DATA_BACK);
        channelMessage.setClientId(Constant.CLIENT_ID);
        channelMessage.setDc("1:"+Constant.DEVICE_CHANNEL);
        long timeStamp=System.currentTimeMillis();
        Log.d("soc","appKey "+Constant.APP_KEY+" appSecret "+Constant.APP_SECRET+"clientId "+Constant.CLIENT_ID);
        String signature= SignUtils.tcpSignature(Command.PUSH_DATA_BACK,Constant.CLIENT_ID,timeStamp,Constant.APP_SECRET,Constant.APP_KEY);
        Log.d("soc", " signature "+signature);
        channelMessage.setSignature(signature);
        channelMessage.setTimestamp(timeStamp);

        ActionMessage<AuthEntity> data=new ActionMessage<>();
        data.setClientId(Constant.CLIENT_ID);
        data.setActionId(getActionId());
        data.setDomainId(getDomainId());
        data.setValue(0);
        data.setDeviceChannelNumber(Constant.DEVICE_CHANNEL);
        AuthEntity authEntity =new AuthEntity();
        authEntity.setIMEI(MmkvHelper.getInstance().getObject("IMEI",String.class));
        data.setData(authEntity);
        channelMessage.setData(data);
        EasySocket.getInstance().upMessage(channelMessage.pack());
    }


}
