package com.miaxis.weomosdk.message.handle;

import android.util.Log;

import com.easysocket.EasySocket;
import com.easysocket.connection.heartbeat.HeartManager;
import com.easysocket.entity.OriginReadData;
import com.easysocket.utils.LogUtil;
import com.miaxis.weomosdk.WeiMoFacade;
import com.miaxis.weomosdk.api.ZZResponse;
import com.miaxis.weomosdk.command.handle.SimpleCommandHandle;
import com.miaxis.weomosdk.constant.ActionIdTypeEnum;
import com.miaxis.weomosdk.constant.Command;
import com.miaxis.weomosdk.constant.CommandTypeEnum;
import com.miaxis.weomosdk.constant.Constant;
import com.miaxis.weomosdk.constant.DomainIdTypeEnum;
import com.miaxis.weomosdk.data.SimpleMessage;
import com.miaxis.weomosdk.entity.HeartBeatEntity;
import com.miaxis.weomosdk.entity.InitEntity;
import com.miaxis.weomosdk.utils.GsonUtils;
import com.miaxis.weomosdk.utils.MmkvHelper;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 丢失初始化信息
 */
 public class AuthLostHandle extends SimpleCommandHandle {
    @Override
    public boolean onCustomCallBack() {
   //     startHeartbeat();
        return true;
    }

    @Override
    public void intentHandle(String clientId, String data) {
        Log.d("CommandDispatcher","authLost Handle");
        Constant.CLIENT_ID=clientId;
        MmkvHelper.getInstance().putObject("clientId",clientId);
        InitEntity entity= GsonUtils.GsonToBean(data, InitEntity.class);
        //   MmkvHelper.getInstance().putObject("appKey",);
        if(entity.getChannelList()!=null&&entity.getChannelList().size()>0){ //获取到多个通道
            for(int i=0;i<entity.getChannelList().size();i++){
                Constant.APP_KEY=entity.getChannelList().get(i).getAppKey();
                Constant.APP_SECRET=entity.getChannelList().get(i).getAppSecret();
                Constant.DEVICE_CHANNEL=entity.getChannelList().get(i).getChannelNumber();
                Constant.CHANNEL_NAME=entity.getChannelList().get(i).getChannelName();
                MmkvHelper.getInstance().putObject("appKey",entity.getChannelList().get(i).getAppKey());
                MmkvHelper.getInstance().putObject("appSecret",entity.getChannelList().get(i).getAppSecret());
                MmkvHelper.getInstance().putObject("channelNumber",entity.getChannelList().get(i).getChannelNumber());
                MmkvHelper.getInstance().putObject("channelName",entity.getChannelList().get(i).getChannelName());
            }

        }
        Constant.DEVICE_NUMBER=entity.getDeviceNumber();
        MmkvHelper.getInstance().putObject("deviceNumber",entity.getDeviceNumber());
        Constant.IS_INIT=true;
        WeiMoFacade.connectCallback.response(new ZZResponse(0,"注册成功"));
        startHeartbeat();

    }

    @Override
    public String getCommand() {
      //  return Command.AUTH_BACK;
        return CommandTypeEnum.AUTH_BACK.getCmd();
    }

    @Override
    public String getDomainId() {
        return DomainIdTypeEnum.BASE.getCode();
    }

    @Override
    public String getActionId() {
        return ActionIdTypeEnum.INITCHANNEL.getCode();
    }

    @Override
    public Object getData() {
        return null;
    }


    private void startHeartbeat() {
        SimpleMessage<HeartBeatEntity> heartBeatMessage = new SimpleMessage<>();
        heartBeatMessage.setClientId(Constant.CLIENT_ID);
        heartBeatMessage.setCmd(Command.PING);
        Log.d("CommandDispatcher","DEVICE_NUMBER "+Constant.DEVICE_NUMBER);
        heartBeatMessage.setData(HeartBeatEntity.obtain(Constant.DEVICE_NUMBER, System.currentTimeMillis()));
        EasySocket.getInstance().startHeartBeat(heartBeatMessage.pack(), new HeartManager.HeartbeatListener() {
            @Override
            public boolean isServerHeartbeat(OriginReadData orginReadData) {
                try {
                    String s = orginReadData.getBodyString();
                    JSONObject jsonObject = new JSONObject(s);
                    if ("PONG".equals(jsonObject.getString("cmd"))) {
                        LogUtil.d("---> 收到服务端心跳");
                        return true;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });

    }
}
