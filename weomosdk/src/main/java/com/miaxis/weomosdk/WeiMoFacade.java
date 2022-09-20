package com.miaxis.weomosdk;

import android.content.Context;
import android.location.Location;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.easysocket.EasySocket;
import com.easysocket.callback.SimpleCallBack;
import com.easysocket.config.EasySocketOptions;
import com.easysocket.entity.OriginReadData;
import com.easysocket.entity.SocketAddress;
import com.miaxis.weomosdk.api.ZZCallback;
import com.miaxis.weomosdk.api.ZZResponse;
import com.miaxis.weomosdk.command.CommandDispatcher;
import com.miaxis.weomosdk.command.PostCommandParse;
import com.miaxis.weomosdk.constant.Constant;
import com.miaxis.weomosdk.constant.NetWorkSettingConstant;
import com.miaxis.weomosdk.data.ActionMessage;
import com.miaxis.weomosdk.data.ChannelMessage;
import com.miaxis.weomosdk.entity.LoginEntity;
import com.miaxis.weomosdk.entity.RequestActiveBean;
import com.miaxis.weomosdk.entity.RequestAuthBean;
import com.miaxis.weomosdk.entity.RequestPersonInfoBean;
import com.miaxis.weomosdk.entity.RequestRemoveBean;
import com.miaxis.weomosdk.entity.ResponseActiveBean;
import com.miaxis.weomosdk.entity.ResponseAuthBean;
import com.miaxis.weomosdk.entity.ResponseLoginBean;
import com.miaxis.weomosdk.entity.ResponsePersonInfoBean;
import com.miaxis.weomosdk.message.callback.CallbackIDFactoryImpl;
import com.miaxis.weomosdk.message.handle.AuthHandle;
import com.miaxis.weomosdk.message.handle.AuthLostHandle;
import com.miaxis.weomosdk.message.handle.InitHandle;
import com.miaxis.weomosdk.utils.DESUtils;
import com.miaxis.weomosdk.utils.GsonUtils;
import com.miaxis.weomosdk.utils.IMEIUtil;
import com.miaxis.weomosdk.utils.LocationUtils;
import com.miaxis.weomosdk.utils.MacUtils;
import com.miaxis.weomosdk.utils.MmkvHelper;
import com.tencent.mmkv.MMKV;

import androidx.annotation.RequiresApi;

/**
 * @ClassName: WeiMoFacade
 * @Author: cheng.peng
 * @Date: 2022/9/15 15:16
 */
public class WeiMoFacade {
    private static String TAG=WeiMoFacade.class.getSimpleName();
    private Context mContext;
    public static ZZCallback zzCallback = null;
    public static ZZCallback connectCallback = null;


    public static  void  init(Context context,String clientId){
        Constant.CLIENT_ID=clientId;
        MMKV.initialize(context);

        initConfigs();
        Log.d("CommandDispatcher","getIMEI "+IMEIUtil.getIMEI(context));
        MmkvHelper.getInstance().putObject("IMEI", IMEIUtil.getIMEI(context));
        MmkvHelper.getInstance().putObject("MAC", MacUtils.getLocalMacAddressFromWifiInfo(context));
        Location location = LocationUtils.getInstance( context ).showLocation();
        if(location!=null){
            Log.d(TAG,"lat "+location.getLatitude()+"lon "+location.getLongitude());
            MmkvHelper.getInstance().putObject("LAT", location.getLatitude());
            MmkvHelper.getInstance().putObject("LON", location.getLongitude());
        }
        initSocket(context);
    }


    public static  void  init(Context context,String ip,String port,String clientId,ZZCallback callback){
        Constant.CLIENT_ID=clientId;
        NetWorkSettingConstant.SERVER_IP=ip;
        NetWorkSettingConstant.SERVER_PORT=port;
        connectCallback=callback;
        MMKV.initialize(context);
        initConfigs();
        Log.d("CommandDispatcher","getIMEI "+IMEIUtil.getIMEI(context));
        Log.d("CommandDispatcher","DEVICE_CHANNEL "+Constant.DEVICE_CHANNEL+"clientId "+Constant.CLIENT_ID);
        MmkvHelper.getInstance().putObject("IMEI", IMEIUtil.getIMEI(context));
        MmkvHelper.getInstance().putObject("MAC", MacUtils.getLocalMacAddressFromWifiInfo(context));
        Location location = LocationUtils.getInstance( context ).showLocation();
        if(location!=null){
            Log.d(TAG,"lat "+location.getLatitude()+"lon "+location.getLongitude());
            MmkvHelper.getInstance().putObject("LAT", location.getLatitude());
            MmkvHelper.getInstance().putObject("LON", location.getLongitude());
        }
        initSocket(context);
    }

    public static  void  init(Context context,ZZCallback callback){
        MMKV.initialize(context);
        zzCallback=callback;
        initSocket(context);
    }

    private static void initConfigs(){
        Constant.APP_KEY=MmkvHelper.getInstance().getObject("appKey", String.class);
        Constant.APP_SECRET=MmkvHelper.getInstance().getObject("appSecret", String.class);
      //  Constant.CLIENT_ID=MmkvHelper.getInstance().getObject("clientId", String.class);
        Constant.DEVICE_CHANNEL=MmkvHelper.getInstance().getObject("channelNumber", String.class);
        Constant.DEVICE_NUMBER=MmkvHelper.getInstance().getObject("deviceNumber", String.class);
    }

    /**
     * socket 连接
     */
    private static void initSocket(Context context){
        CommandDispatcher.getInstance().registerIntentHandler(new InitHandle());
        CommandDispatcher.getInstance().registerIntentHandler(new AuthHandle());
        CommandDispatcher.getInstance().registerIntentHandler(new AuthLostHandle());

        String serverIp = MmkvHelper.getInstance().getObject("SERVER_IP", String.class);
        String serverPort = MmkvHelper.getInstance().getObject("SERVER_PORT", String.class);
        // socket配置
        EasySocketOptions options = new EasySocketOptions.Builder()
                // 主机地址，请填写自己的IP地址，以getString的方式是为了隐藏作者自己的IP地址
            //    .setSocketAddress(new SocketAddress(serverIp==null? NetWorkSettingConstant.SERVER_IP:serverIp, Integer.parseInt(serverPort==null?NetWorkSettingConstant.SERVER_PORT:serverPort)))
                .setSocketAddress(new SocketAddress(NetWorkSettingConstant.SERVER_IP, 19999))
                .setCallbackIDFactory(new CallbackIDFactoryImpl())
                // 定义消息协议，方便解决 socket黏包、分包的问题，如果客户端定义了消息协议，那么
                // 服务端也要对应对应的消息协议，如果这里没有定义消息协议，服务端也不需要定义
                // .setReaderProtocol(new DefaultMessageProtocol())
                .setHeartbeatFreq(15*1000)
                .build();

        // 初始化
        EasySocket.getInstance()
                .createConnection(options, context);// 创建一个socket连接
        CommandDispatcher.getInstance().init();

    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void login(String userName, String password, ZZCallback callback){
        LoginEntity entity=new LoginEntity();
        entity.setUsername(userName);
        entity.setPassword(password);
        ActionMessage<LoginEntity> actionMessage=new ActionMessage<>(entity);
        actionMessage.setActionId("3010");
        actionMessage.setDomainId("3000");
        actionMessage.setData(entity);

        ChannelMessage<ActionMessage<LoginEntity>> message=new ChannelMessage<>();
        String dc= Constant.DEVICE_CHANNEL;
        message.setCmd("UPLOAD_DATA");
        message.setClientId(MmkvHelper.getInstance().getObject("clientId",String.class));
        message.setDc("1:"+dc);
        message.setData(actionMessage);
        //EasySocket.getInstance().upMessage(message.pack());

        EasySocket.getInstance().upCallbackMessage(message).onCallBack(new SimpleCallBack(message.getCallbackId()) {
            @Override
            public void onResponse(OriginReadData data) {
                Log.d(TAG, "get data " + data.getBodyString());
                PostCommandParse commandParse = new PostCommandParse();
                commandParse.parse(data.getBodyString());
                if (commandParse.getValue() == 0) {
                    ResponseLoginBean bean= GsonUtils.GsonToBean(commandParse.getData(),
                            ResponseLoginBean.class);
                    MmkvHelper.getInstance().putObject("userId",bean.getUserId());
                    Log.d(TAG,"log success");
                }else {
                    Log.d(TAG,"log failure");
                }
                callback.response(new ZZResponse(commandParse.getValue(),commandParse.getData()));
            }
        });
    }




    /**
     * 微模块业务激活
     */
    public static void activeWeiMo(String cid,String samId, ZZCallback callback){
        if(!Constant.IS_INIT){
            callback.response(new ZZResponse(-1,"请先初始化微模"));
            return;
        }
        RequestActiveBean bean=new RequestActiveBean();
        bean.setCid(cid);
        bean.setSamid(samId);
        ActionMessage<RequestActiveBean> actionMessage = new ActionMessage<>(bean);
        actionMessage.setActionId("101001");
        actionMessage.setDomainId("101000");

        ChannelMessage<ActionMessage<RequestActiveBean>> channelMessage = new ChannelMessage<>();
        channelMessage.setCmd("UPLOAD_DATA");
        channelMessage.setClientId(Constant.CLIENT_ID);
        channelMessage.setData(actionMessage);

        EasySocket.getInstance().upCallbackMessage(channelMessage)
                .onCallBack(new SimpleCallBack(channelMessage.getCallbackId()) {
                    @Override
                    public void onResponse(OriginReadData data) {
                        Log.d(TAG, "get data " + data.getBodyString());
                        PostCommandParse commandParse = new PostCommandParse();
                        commandParse.parse(data.getBodyString());
                        if (commandParse.getValue() == 0) {
                            ResponseActiveBean responseActiveBean=GsonUtils.GsonToBean(commandParse.getData(),ResponseActiveBean.class);
                            Constant.DEVICE_ID=responseActiveBean.getMdeviceid();
                            Constant.CID=cid;
                            Constant.SAMID=samId;
                            Log.d(TAG,"active info "+responseActiveBean.getActiveinfo()+" mdeviceId "+responseActiveBean.getMdeviceid());

                        }
                        callback.response(new ZZResponse(commandParse.getValue(),commandParse.getData()));
                    }
                });

    }

    /**
     * 移除微模块
     */
    public static void removeWeiMo(ZZCallback callback){
        RequestRemoveBean bean=new RequestRemoveBean();
        bean.setCid(Constant.CID);
        bean.setSamid(Constant.SAMID);
        bean.setMdeviceid(Constant.DEVICE_ID);
        ActionMessage<RequestRemoveBean> actionMessage = new ActionMessage<>(bean);
        actionMessage.setActionId("101002");
        actionMessage.setDomainId("101000");

        ChannelMessage<ActionMessage<RequestRemoveBean>> channelMessage = new ChannelMessage<>();
        channelMessage.setCmd("UPLOAD_DATA");
        channelMessage.setClientId(Constant.CLIENT_ID);
        channelMessage.setData(actionMessage);

        EasySocket.getInstance().upCallbackMessage(channelMessage)
                .onCallBack(new SimpleCallBack(channelMessage.getCallbackId()) {
                    @Override
                    public void onResponse(OriginReadData data) {
                        Log.d(TAG, "get data " + data.getBodyString());
                        PostCommandParse commandParse = new PostCommandParse();
                        commandParse.parse(data.getBodyString());
                        if (commandParse.getValue() == 0) {
                            RequestRemoveBean responseActiveBean=GsonUtils.GsonToBean(commandParse.getData(),RequestRemoveBean.class);
                            Log.d(TAG," mdeviceId "+responseActiveBean.getMdeviceid());
                            Constant.CID="";
                            Constant.SAMID="";
                            Constant.DEVICE_ID="";
                        }
                        callback.response(new ZZResponse(commandParse.getValue(),commandParse.getData()));
                    }
                });
    }

    /**
     * 微模在线授权
     */
    public static void authorizeWeiMo(String authReq,ZZCallback callback){
        if(TextUtils.isEmpty(Constant.DEVICE_ID)){
            callback.response(new ZZResponse(-1,"请先激活微模"));
            return;
        }
        RequestAuthBean bean=new RequestAuthBean();
        bean.setCid(Constant.CID);
        bean.setSamid(Constant.SAMID);
        bean.setMdeviceid(Constant.DEVICE_ID);
        bean.setAuthreq(authReq);
        ActionMessage<RequestAuthBean> actionMessage = new ActionMessage<>(bean);
        actionMessage.setActionId(" ");
        actionMessage.setDomainId("101000");

        ChannelMessage<ActionMessage<RequestAuthBean>> channelMessage = new ChannelMessage<>();
        channelMessage.setCmd("UPLOAD_DATA");
        channelMessage.setClientId(Constant.CLIENT_ID);
        channelMessage.setData(actionMessage);

        EasySocket.getInstance().upCallbackMessage(channelMessage)
                .onCallBack(new SimpleCallBack(channelMessage.getCallbackId()) {
                    @Override
                    public void onResponse(OriginReadData data) {
                        Log.d(TAG, "get data " + data.getBodyString());
                        PostCommandParse commandParse = new PostCommandParse();
                        commandParse.parse(data.getBodyString());
                        if (commandParse.getValue() == 0) {
                            ResponseAuthBean responseAuthBean=GsonUtils.GsonToBean(commandParse.getData(),ResponseAuthBean.class);
                            Log.d(TAG,"auth info "+responseAuthBean.getAuthresp());
                        }
                        callback.response(new ZZResponse(commandParse.getValue(),""));
                    }
                });
    }


    /**
     * 读取个人信息
     */
    public static void readPersonInfo(String frameData,String samSigncert,ZZCallback callback){

        if(TextUtils.isEmpty(Constant.DEVICE_ID)){
            callback.response(new ZZResponse(-1,"请先激活微模"));
            return;
        }

        RequestPersonInfoBean bean=new RequestPersonInfoBean();
        bean.setCid(Constant.CID);
        bean.setSamid(Constant.SAMID);
        bean.setMdeviceid(Constant.DEVICE_ID);
        bean.setFramedata(frameData);
        bean.setSamsigncert(samSigncert);
      //  bean.setFramedata("MDUyMzIwMTkwOTI3MDAwMDAwMDE3MDIwMjAyMDIwMjAyMDIwMjAyADAAMAA3ADAANAAzADAAf5UfZyAAIAAgACAAIAAgAIjwbCLl1Hhz91I4O2aAmNYBrgx5m9O6v/cjDlB0uP52RmJLAOqaMv4E1rdmAFkUyPatXAj+ehHIJwxWXGgDRYwQIySMR4xh/Al2Nt0hEUAjM2Ai4oLtFEZ2HdT+0npYU5xbUlyGLGMFp8mofeZkqD9oFCVnzVuJnFdBc0FXfAhATgO+Y0q0wz+lOzT/5pBi/ynM5GTznxEPxxDekbp3r8KoXbqEStQWTuBpMY6YkSQ+9h8WkPPhvy12X8hN7yj3JipKLYUNhma1tMJgRdu28XCxDSmMlbtrXnwmTXWfmYsSIiljfKGS3GZC7Dw2Rbdr3k+8Np5gafzrDvmecCHNUrg2LOEvQVuMojaK80vqXSnGaGeMdUYLp+Y21MlgolriCwXXB+Erahta2atUbZdvX17BNwbiG4Fl/j89+++5+iAY+XG3cus6X7xr6QThquvnentG56BulpJVRatU0+3AGyxNWXVB4qu3t0iaC5tVaVe7JXZqDUVND73PDlON0ttEHVVSd1jahLbj5/libyhoEy2PxQLsammdjgWXQ8XVykVV6Hp+9FbaxKAomc4w2Ie6h8eDfsZIPCkL0w1PnvgA05VXc3R0lxndiX2H2gNXvZ9yFAtJXY6mrBtaYkLPbXd2uHRzOx1b8hlTsiDEdO9cXpWAE7NbWSTTN/U/NNOuDUi6vph+DnVJXMFdmYXLcaAI4kpYCtAHoGhPeBaDVbmpZDU=");
       // bean.setSamsigncert("MIIBoTCCAUagAwIBAgITMDA1MjMxOTA5MjcwMDAwMDE3MDAMBggqgRzPVQGDdQUAMD4xCzAJBgNVBAYTAkNOMQ8wDQYDVQQKEwZHQUhaWlgxDDAKBgNVBAsTA0tEQzEQMA4GA1UEAxMHU0FNUk9PVDAeFw0yMjA3MDgwMzAzMzZaFw0zMjA3MDUwMzAzMzZaME0xCzAJBgNVBAYTAkNOMQ8wDQYDVQQKEwZHQUhaWlgxDDAKBgNVBAsTA0tEQzEfMB0GA1UEAxMWMDUyMzIwMTkwOTI3MDAwMDAwMDE3MDBZMBMGByqGSM49AgEGCCqBHM9VAYItA0IABDi9QSvs1V3ssMQeZjScortGclUpu9XtsNv0ivpxIkFRiHcTORhsBSqzCi8XAjPfg9apjfs/VCsSqcCWPjlwieujEjAQMA4GA1UdDwEB/wQEAwIGwDAMBggqgRzPVQGDdQUAA0cAMEQCIE9KL0H90yb6pvT3nHvAGN3o5ItO9kDKCa9J+nQ2D/EVAiBBiMi466VvdoNDZu/7QP0MvqONdA5/AYJ/VaKH1K3LxA==");

        ActionMessage<RequestPersonInfoBean> actionMessage = new ActionMessage<>(bean);
        actionMessage.setActionId("101004");
        actionMessage.setDomainId("101000");

        ChannelMessage<ActionMessage<RequestPersonInfoBean>> channelMessage = new ChannelMessage<>();
        channelMessage.setCmd("UPLOAD_DATA");
        channelMessage.setClientId(Constant.CLIENT_ID);
        channelMessage.setData(actionMessage);

        EasySocket.getInstance().upCallbackMessage(channelMessage)
                .onCallBack(new SimpleCallBack(channelMessage.getCallbackId()) {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(OriginReadData data) {
                        Log.d(TAG, "get data " + data.getBodyString());
                        PostCommandParse commandParse = new PostCommandParse();
                        commandParse.parse(data.getBodyString());
                        if (commandParse.getValue() == 0) {
                            ResponsePersonInfoBean responsePersonInfoBean=GsonUtils.GsonToBean(commandParse.getData(),ResponsePersonInfoBean.class);
                            Log.d(TAG,"read info "+responsePersonInfoBean.getChinesename());

                            Log.d(TAG,"des info "+ DESUtils.decrypt("12345678",responsePersonInfoBean.getChinesename()));

                        }
                        callback.response(new ZZResponse(commandParse.getValue(),""));
                    }
                });
    }

    private static void setResponse(int ret,String msg){
        ZZResponse response=new ZZResponse(ret,msg);
        zzCallback.response(response);
    }

}
