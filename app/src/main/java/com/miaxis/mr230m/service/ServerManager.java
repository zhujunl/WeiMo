package com.miaxis.mr230m.service;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.miaxis.mr230m.http.net.MyCallback;
import com.miaxis.mr230m.miaxishttp.bean.TokenRefreshResonse;
import com.miaxis.mr230m.miaxishttp.net.MiaxisRetrofit;
import com.miaxis.mr230m.util.mkUtil;

public class ServerManager {

    private ServerManager() {
    }

    public static ServerManager getInstance() {
        return SingletonHolder.instance;
    }

    private static class SingletonHolder {
        private static final ServerManager instance = new ServerManager();
    }

    /**
     * ================================ 静态内部类单例 ================================
     **/


    private HandlerThread handlerThread;
    private Handler handler;
    String TAG="ServerManager";


    public void startRefresh() {
        Log.d(TAG, "开启" );
        handlerThread = new HandlerThread("refresh");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                refresh();
            }
        };
        handler.sendMessage(handler.obtainMessage(0));
    }

    public void stopRefresh() {
        Log.d(TAG, "停止" );
        if (handler != null) {
            handler.removeMessages(0);
            handler = null;
        }
        if (handlerThread != null) {
            handlerThread.quitSafely();
        }
    }

    private void refresh() {
        try {
            handler.removeMessages(0);
            String ip= mkUtil.getInstance().decodeString("weiIp", "");
            if (TextUtils.isEmpty(ip)){
                return;
            }
            MiaxisRetrofit.getApiService(ip).RefreshToken().enqueue(new MyCallback<TokenRefreshResonse>() {
                @Override
                public void onSuccess(TokenRefreshResonse tokenRefreshResonse) {
                    if (tokenRefreshResonse.getCode()==200){
                        Log.d(TAG, "refresh成功:");
                    }else {
                        Log.e(TAG, "refresh失败:"+tokenRefreshResonse.getMsg());
                    }
                }

                @Override
                public void onFailed(Throwable t) {
                    Log.e(TAG, "refresh错误:"+t.getMessage());
                }
            });
        } catch (Exception e) {
            Log.e("asd", "" + e.getMessage());
        } finally {
            prepareForNextHeartBeat();
        }
    }

    private void prepareForNextHeartBeat() {
        if (handler != null) {
            Message message = handler.obtainMessage(0);
            handler.sendMessageDelayed(message, 8*60 * 1000);
        }
    }

}
