package com.miaxis.weomosdk.message.handle;

import android.util.Log;

import com.easysocket.EasySocket;
import com.easysocket.connection.heartbeat.HeartManager;
import com.easysocket.entity.OriginReadData;
import com.easysocket.utils.LogUtil;
import com.miaxis.weomosdk.WeiMoFacade;
import com.miaxis.weomosdk.api.ZZResponse;
import com.miaxis.weomosdk.command.handle.SimpleCommandHandle;
import com.miaxis.weomosdk.constant.Command;
import com.miaxis.weomosdk.constant.Constant;
import com.miaxis.weomosdk.data.SimpleMessage;
import com.miaxis.weomosdk.entity.HeartBeatEntity;
import com.miaxis.weomosdk.utils.MmkvHelper;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @ClassName: AuthHandle
 * @Author: cheng.peng
 * @Date: 2022/5/24 19:25
 * 鉴权成功后保存clientId
 */
public class AuthHandle extends SimpleCommandHandle {
    private String TAG=AuthHandle.class.getSimpleName();
    @Override
    public String getCommand() {
        return "AUTH_BACK";
    }

    @Override
    public String getDomainId() {
        return "";
    }

    @Override
    public String getActionId() {
        return "";
    }

    @Override
    public boolean onCustomCallBack() {
  //    startHeartbeat();
        return true;
    }

    @Override
    public void intentHandle(String clientId, String data) {
        Log.d("CommandDispatcher","auth Handle "+Constant.CLIENT_ID);
        Log.d("CommandDispatcher","DEVICE_NUMBER "+Constant.DEVICE_NUMBER);
        Constant.CLIENT_ID=clientId;
        MmkvHelper.getInstance().putObject("clientId",clientId);
        startHeartbeat();
        Constant.IS_INIT=true;
        WeiMoFacade.connectCallback.response(new ZZResponse(0,"注册成功"));

    }

    @Override
    public Object getData() {
        return null;
    }

    // 启动心跳检测功能
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
