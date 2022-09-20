package com.miaxis.weomosdk.command;


import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.easysocket.EasySocket;
import com.easysocket.entity.OriginReadData;
import com.easysocket.entity.SocketAddress;
import com.easysocket.interfaces.conn.ISocketActionListener;
import com.easysocket.interfaces.conn.SocketActionListener;
import com.easysocket.utils.LogUtil;
import com.miaxis.weomosdk.WeiMoFacade;
import com.miaxis.weomosdk.api.ZZCallback;
import com.miaxis.weomosdk.api.ZZResponse;
import com.miaxis.weomosdk.command.handle.AbsCommandHandle;
import com.miaxis.weomosdk.command.handle.IIntentHandle;
import com.miaxis.weomosdk.constant.Constant;
import com.miaxis.weomosdk.entity.AuthEntity;
import com.miaxis.weomosdk.message.auth.AuthMessage;
import com.miaxis.weomosdk.utils.MmkvHelper;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: CommandDispatcher
 * @Author: cheng.peng
 * @Date: 2022/5/23 17:23
 * 终端命令发送器
 */
public class CommandDispatcher {
    private String TAG=CommandDispatcher.class.getSimpleName();

    /**
     * 根据command名注册
     * 同一个命令 可以在多个地方注册
     * 只在最后一次注册的地方回调 即后面注册的handle会覆盖前面注册 但后面注册的取消注册 又会让前一个注册的handle处理
     */
    private Map<String, List<AbsCommandHandle>> intentMap= new HashMap<>();;

    private static CommandDispatcher instance;

    public static CommandDispatcher getInstance() {
        if (instance == null) {
            instance = new CommandDispatcher();
        }
        return instance;
    }


    public void init(){
        EasySocket.getInstance().subscribeSocketAction(socketActionListener);

    }

    public void init(ZZCallback zzCallback){
        EasySocket.getInstance().subscribeSocketAction(socketActionListener);
    }



    /**
     * socket行为监听
     */
    private ISocketActionListener socketActionListener = new SocketActionListener() {
        /**
         * socket连接成功
         * @param socketAddress
         */
        @Override
        public void onSocketConnSuccess(SocketAddress socketAddress) {
            LogUtil.d("端口" + socketAddress.getPort() + "---> 连接成功");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                   AuthMessage();
                }
            },200);

        }

        /**
         * socket连接失败
         * @param socketAddress
         * @param isNeedReconnect 是否需要重连
         */
        @Override
        public void onSocketConnFail(SocketAddress socketAddress, boolean isNeedReconnect) {

        }

        /**
         * socket断开连接
         * @param socketAddress
         * @param isNeedReconnect 是否需要重连
         */
        @Override
        public void onSocketDisconnect(SocketAddress socketAddress, boolean isNeedReconnect) {
            LogUtil.d(socketAddress.getPort() + "端口" + "---> socket断开连接，是否需要重连：" + isNeedReconnect);
            WeiMoFacade.connectCallback.response(new ZZResponse(-1,"断开连接"));
        }

        /**
         * socket接收的数据
         * @param socketAddress
         * @param readData
         */
        @Override
        public void onSocketResponse(SocketAddress socketAddress, String readData) {
            Log.d(TAG,"socket read data "+readData);
            //主线程执行
            PostCommandParse commandParse = new PostCommandParse();
            commandParse.parse(readData);
            boolean interrupt = skillDispatch(commandParse);
            if(!interrupt){ //兜底处理

            }
        }

        @Override
        public void onSocketResponse(SocketAddress socketAddress, OriginReadData originReadData) {
            super.onSocketResponse(socketAddress, originReadData);
        //    LogUtil.d(socketAddress.getPort() + "端口" + "SocketActionListener收到数据-->" + originReadData.getBodyString());
        }
    };

    

    public boolean skillDispatch(CommandParse commandParser) {
        String key = commandParser.getKey();
        //如果业务层有注册，交给业务层处理
        if (intentMap.containsKey(key)) {
            if(key.equals("AUTH")){
                intentNotify(commandParser);
            }else {
                intentNotify(commandParser);
                String actionId=commandParser.getActionId();
                String domainId=commandParser.getDomainId();
            //    AbsCommandHandle handle=intentMap.get(command);
             //   if(actionId.equals())
            }

            return true;
        }
        return  false;

    }


    private void intentNotify(CommandParse skillParse) {
        Log.d("CommandDispatcher","skill key "+skillParse.getKey());
        List<AbsCommandHandle> list = intentMap.get(skillParse.getKey());
        if (list != null && list.size() > 0) {
            IIntentHandle handle = list.get(list.size() - 1);
            handle.intentHandle(skillParse);
        }
    }


    public void registerIntentHandler(AbsCommandHandle intentHandle) {
        if (TextUtils.isEmpty(intentHandle.getKey())) return;
        List<AbsCommandHandle> list = intentMap.get(intentHandle.getKey());
        if (list == null) {
            list = new ArrayList<>();
            Log.d(TAG,"intent map register key");
            intentMap.put(intentHandle.getKey(), list);
        }
        if (list.contains(intentHandle)) return;
        list.add(intentHandle);
    }


    public void unregisterIntentHandle(AbsCommandHandle intentHandle) {
        List<AbsCommandHandle> list = intentMap.get(intentHandle.getKey());
        if (list != null && list.contains(intentHandle)) {
            list.remove(intentHandle);
            if (list.isEmpty()) {
                intentMap.remove(intentHandle.getKey());
            }
        }
    }


    private void AuthMessage() {
        String registerId = MmkvHelper.getInstance().getObject("REGISTER_ID", String.class);
        Log.d(TAG,"registerId "+registerId+"ClientId "+Constant.CLIENT_ID);
        if (!TextUtils.isEmpty(MmkvHelper.getInstance().getObject("appKey", String.class))) {
            AuthEntity entity = new AuthEntity();
            entity.setAppKey(MmkvHelper.getInstance().getObject("appKey", String.class));
            entity.setAppSecret(MmkvHelper.getInstance().getObject("appSecret", String.class));
            entity.setDeviceChannelNumber(MmkvHelper.getInstance().getObject("channelNumber", String.class));
            entity.setIMEI(MmkvHelper.getInstance().getObject("IMEI",String.class));
            entity.setMac(MmkvHelper.getInstance().getObject("MAC",String.class));
            AuthMessage<AuthEntity> authMessage = new AuthMessage<>();
            authMessage.setData(entity);
          //  authMessage.setClientId(registerId);
            authMessage.setClientId(Constant.CLIENT_ID);
            authMessage.setCmd("AUTH");
            EasySocket.getInstance().upMessage(authMessage.pack());
        } else {
            AuthEntity entity = new AuthEntity();
            entity.setIMEI(MmkvHelper.getInstance().getObject("IMEI",String.class));
            entity.setMac(MmkvHelper.getInstance().getObject("MAC",String.class));
            AuthMessage<AuthEntity> authMessage = new AuthMessage<>();
            authMessage.setData(entity);
            authMessage.setClientId(Constant.CLIENT_ID);
            authMessage.setCmd("AUTH");

   /*       SimpleMessage testMessage = new SimpleMessage();
            testMessage.setCmd("AUTH");
            testMessage.setClientId(registerId);
            testMessage.setData(entity);*/
            // 发送
            EasySocket.getInstance().upMessage(authMessage.pack());
        }
    }

}
