package com.miaxis.mr230m;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.miaxis.mr230m.mr990.finger.MR990FingerStrategy;
import com.miaxis.mr230m.util.mkUtil;

import org.zz.api.MXFaceIdAPI;
import org.zz.api.MXResult;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Tank
 * @date 2021/8/19 5:39 下午
 * @des
 * @updateAuthor
 * @updateDes
 */
public class App extends Application {

    private static final String TAG = "App";
    private static App context;
    public final ExecutorService threadExecutor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());


    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        init();
    }

    public static App getInstance() {
        return context;
    }

    public MXResult<?> init() {
        mkUtil.init(this);
        Log.e(TAG, "上电" );
        Intent intent = new Intent("com.miaxis.power");
        intent.putExtra("type",0x12);
        intent.putExtra("value",true);
        context.sendBroadcast(intent);

        Intent i = new Intent("com.miaxis.power");
        i.putExtra("type",0x11);
        i.putExtra("value",true);
        context.sendBroadcast(i);

//        WeiMoFacade.init(context, mkUtil.getInstance().decodeString("ip", "192.168.11.183"),
//                mkUtil.getInstance().decodeString("port", "19999"),
//                mkUtil.getInstance().decodeString("userName", "9906240014"), response -> {
//                    if(response.code==0){
//                        Log.e(TAG, "初始化成功" );
//                    }else {
//
//                    }
//                    return false;
//                });
        MR990FingerStrategy.getInstance().init();
        return MXFaceIdAPI.getInstance().mxInitAlg(this, null, null);
    }

}


